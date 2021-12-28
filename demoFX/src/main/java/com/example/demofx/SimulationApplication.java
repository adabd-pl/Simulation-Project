package com.example.demofx;

import com.example.demofx.*;
import agh.ics.oop.*;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.adapter.JavaBeanIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.util.Pair;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.cert.PolicyNode;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.jar.Manifest;

import static java.lang.Integer.max;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class SimulationApplication extends Application implements IPositionChangeObserver, Runnable{

    private int rows ;
    private int columns ;
    private int fieldSize  = 20;
    private int    HEIGHT_MAP = columns*fieldSize;
    private int    WIDTH_MAP =  rows*fieldSize;
    private int    amountOfGrass = 40;
    private int deaths = 0;
    private int actualDay=0;
    private ScheduledExecutorService scheduledExecutorService1;
    private ScheduledExecutorService scheduledExecutorService2;
    final int WINDOW_SIZE = 20;
    int amountOfAnimalsStart ;
    int csv =1;
    Color colorOfJungle = Color.rgb(255, 204, 102);
    Text magicAlert = new Text("");

    public int getActualDay(){
        return actualDay;
    }

    private Parent CreateContent(GrassField map, ArrayList<Animal> animals ,LineChart<String, Number> chart ,GridPane statsPane ) throws FileNotFoundException {
        Pane root = new Pane();
        int[] isPaused = new int[]{0};
        Canvas canvas = new Canvas(WIDTH_MAP + 1000, HEIGHT_MAP + 200);
        GraphicsContext g = canvas.getGraphicsContext2D();
        ArrayList<Animal> animalstomoveF = new ArrayList<>();
        ArrayList<Animal> animalstomoveB = new ArrayList<>();


        File statisticsCSV = new File(Integer.toString(csv) + "test.csv");

        GridPane followPane = new GridPane();
        Text[] followAnimalField = new Text[]{new Text("0"), new Text("0"), new Text("0"), new Text("0")};
        addfollowAnimal(followPane, followAnimalField);
        followPane.setLayoutX(WIDTH_MAP + 600);
        followPane.setLayoutY(max(HEIGHT_MAP, 400) - 200);
        map.statistics.setHeadersCSV(statisticsCSV);
        Animal animalToFollow = null;
        root.getChildren().add(followPane);
        root.getChildren().addAll(canvas);

        int magicEffect[] = {0};
        if (StartApplication.magicOption == true) {
            magicEffect[0] = 3;
            magicAlert.setLayoutX(WIDTH_MAP + 600);
            magicAlert.setLayoutY(max(HEIGHT_MAP, 400) - 230);
            root.getChildren().add(magicAlert);
        }


        ///SIMULATION OF DAY
        ScheduledExecutorService scheduledExecutorServiceInside = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorServiceInside.scheduleAtFixedRate(() -> {
            if (isPaused[0] == 0) {
                Platform.runLater(() -> {
                    Vector2d.MoveDirection move;
                    if (map.statistics.getAllAnimals().equals(map.statistics.getDeathAnimals())) {
                        try {
                            map.statistics.summaryToCSV(statisticsCSV, actualDay);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }


                        scheduledExecutorServiceInside.shutdown();
                        return;
                    }
                    actualDay += 1;
                    animalstomoveB.clear();
                    animalstomoveF.clear();
                    //CHECK ANIMALS TO MOVE AND REMOVE DEATHS
                    for (int i = 0; i < map.multipleMap.size(); i++) {
                        Object firstKey = map.multipleMap.keySet().toArray()[i];
                        for (int j = 0; j < map.multipleMap.get(firstKey).size(); j++) {
                            if (map.multipleMap.get(firstKey).get(j) != null && map.multipleMap.get(firstKey).get(j).getEnergy() <= 0) {
                                removeDeadAnimal(firstKey, j, root, map);
                            } else if (map.multipleMap.get(firstKey).get(j) != null) {
                                map.multipleMap.get(firstKey).get(j).days = actualDay;

                                ///CHOOSE NEXT MOVE
                                move = map.multipleMap.get(firstKey).get(j).renderMove();
                                if (Vector2d.MoveDirection.FOWARD == move) {
                                    animalstomoveF.add(map.multipleMap.get(firstKey).get(j));
                                } else if (Vector2d.MoveDirection.BACKWARD == move) {
                                    animalstomoveB.add(map.multipleMap.get(firstKey).get(j));


                                } else {
                                    map.multipleMap.get(firstKey).get(j).updateEnergy(StartApplication.amountOfAnimals[4], map.statistics);
                                }
                            }
                        }
                    }

                    ///UPDATE POSITION OF ANIMAL IF MOVE FOWARD OR BACKWARD
                    for (int i = 0; i < animalstomoveF.size(); i++) {
                        update(g, map, animalstomoveF.get(i), Vector2d.MoveDirection.FOWARD, root, animalToFollow);
                    }
                    for (int i = 0; i < animalstomoveB.size(); i++) {
                        update(g, map, animalstomoveB.get(i), Vector2d.MoveDirection.FOWARD, root, animalToFollow);
                    }


                    ///CHECK AND PLACE NEW ANIMALS TO BIRTH
                    checkBirths(map, g, root, animalToFollow);

                    ///MAGIC OPTION GAME
                    if (magicEffect[0] > 0 && map.statistics.getLiveAnimals().equals(5)) {
                        generateMagic(map, g, magicAlert);
                        magicEffect[0]--;
                    }

                    ///ADD GRASS AFTER DAY
                    map.addOneGrass(g, fieldSize);

                    ///SAVE STATISTICS TO CSV
                    try {
                        map.statistics.toCSV(statisticsCSV);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                });
            }
        }, 400, 700, MILLISECONDS);  //400,200 dziala

        ///ADD CHART TO APP
        root.getChildren().add(chart);
        chart.setLayoutY(30);
        chart.setLayoutX(WIDTH_MAP + 50);

        ///ADD STATISTICS TO APP
        root.getChildren().add(statsPane);
        statsPane.setLayoutY(30);
        statsPane.setLayoutX(WIDTH_MAP + 600);


        ///BUTTON TO START SIMULATION
        Button buttonStop = new Button("Start symulacji");
        buttonStop.setLayoutX(WIDTH_MAP + 600);
        buttonStop.setLayoutY((int) max(HEIGHT_MAP, 400) - 70);
        buttonStop.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                isPaused[0] = 0;
            }
        }));
        root.getChildren().add(buttonStop);


        ///BUTTON TO STOP SIMULATION
        Button button3 = new Button("Stop");
        button3.setLayoutX(WIDTH_MAP + 700);
        button3.setLayoutY((int) max(HEIGHT_MAP, 400) - 70);
        button3.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                isPaused[0] = 1;
            }
        }));
        root.getChildren().add(button3);


        ///BUTTON TO SHOW ANIMALS WITH DOMINANT GEN
        Button buttonGens = new Button("Show dominant gen");
        buttonGens.setLayoutX(WIDTH_MAP + 750);
        buttonGens.setLayoutY((int) max(HEIGHT_MAP, 400) - 70);
        buttonGens.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                map.showAllWithDominant();
            }
        }));
        root.getChildren().add(buttonGens);

        root.setMinHeight(max(HEIGHT_MAP, 400));
        root.setMinWidth(WIDTH_MAP + 800);


        ///RENDER MAP
        g.clearRect(0, 0, WIDTH_MAP, HEIGHT_MAP);
        for (int j = 0; j <= columns; j++) {
            for (int i = 0; i <= rows; i++) {
                if (map.getJungleLeftDown().precedes(new Vector2d(i, j)) && map.getJungleRightUp().follows(new Vector2d(i, j))) {
                    map.drawFieldOfMap(g, colorOfJungle, fieldSize, i, j);
                } else {
                    map.drawFieldOfMap(g, Color.CORNSILK, fieldSize, i, j);
                }
            }
        }


        ///DRAW ANIMALS AND GRASSES
        animals.forEach(p -> p.draw(root, fieldSize, map.animalToFollow));
        map.drawGrasses(g, fieldSize);

        //GET ANIMAL TO FOLLOW
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                while (true) {

                    Platform.runLater(() -> {
                        if (map.animalToFollow != null) {
                            followAnimalField[0].setText(Integer.toString(map.animalToFollow.getChildren() - map.animalToFollow.getAfterClickCildren()));
                            followAnimalField[1].setText(Integer.toString(map.animalToFollow.getDescendants() - map.animalToFollow.getAfterClickDestendands()));
                            followAnimalField[2].setText(Integer.toString(map.animalToFollow.days));
                            followAnimalField[3].setText(map.animalToFollow.genotypeToString());
                        }
                    });
                    Thread.sleep(100);
                }
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
        csv++;
        ScrollPane newRoot = new ScrollPane();
        newRoot.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        newRoot.setContent(root);
        return newRoot;
    }


    public void update(GraphicsContext g,GrassField map , Animal animalToMove , Vector2d.MoveDirection direction ,Pane root ,Animal animalToFollow ){
        map.drawFieldOfMap(g,Color.CORNSILK, fieldSize,animalToMove.getPosition().x, animalToMove.getPosition().y);
        if(map.getJungleLeftDown().precedes(animalToMove.getPosition()) && map.getJungleRightUp().follows(animalToMove.getPosition())) {
            g.setFill(Color.rgb(255, 204, 102));
            g.fillRect(animalToMove.getPosition().x*fieldSize +1, animalToMove.getPosition().y*fieldSize +1 ,fieldSize-1, fieldSize-1);
        }
            if (animalToMove.getEnergy()>0){
                if(map instanceof SecondMap){
                    animalToMove.moveSecondMap(direction);
                }
                else {
                    animalToMove.move(direction);
                }
                root.getChildren().remove(animalToMove.getAnimalG());
                animalToMove.draw(root ,fieldSize ,animalToFollow);
        }
    }

    public void checkBirths(GrassField map  ,GraphicsContext g ,Pane root ,Animal animalToFollow){
            Animal parent1 =null;
            Animal parent2 =null;
            List<Pair> mPairs = new ArrayList<Pair>();
            int max1=0;
            int max2=0;
            //search possible parents
            for (int i= 0 ; i<map.multipleMap.size(); i++){
                parent2=null;
                parent1=null;
                max1=0;
                max2=0;
                Object firstKey = map.multipleMap.keySet().toArray()[i];
                for (int j= 0 ; j<map.multipleMap.get(firstKey).size(); j++) {
                    if (map.multipleMap.get(firstKey).get(j).getEnergy()>StartApplication.amountOfAnimals[3]/2  ){
                        if (max1 < map.multipleMap.get(firstKey).get(j).getEnergy()) {
                            parent2 = parent1;
                            parent1 = map.multipleMap.get(firstKey).get(j);

                        } else if (max2 < map.multipleMap.get(firstKey).get(j).getEnergy()) {
                            parent2 = map.multipleMap.get(firstKey).get(j);
                        }
                    }
                }
                if (parent1 != null && parent2 != null) {
                    Pair<Animal,Animal> pair = new Pair<>(parent1,parent2);
                    mPairs.add(pair);
                }
            }
            //generate children and update statistics
            for(int i =0 ; i<mPairs.size() ; i++){
                parent1 = (Animal) mPairs.get(i).getKey();
                parent2 = (Animal) mPairs.get(i).getValue();
                Animal child = new Animal(map, parent1, parent2);

                map.statistics.addedAnimalStats(child);
                map.statistics.birthAnimal(parent1,parent2);

                parent1.updateChildren();
                parent2.updateChildren();
                child.draw(root ,fieldSize, animalToFollow);
            }

    }


    @Override
    public void start(Stage stage ) throws IOException {
        rows= StartApplication.amountOfAnimals[1];
        columns= StartApplication.amountOfAnimals[2];
        this.HEIGHT_MAP=this.columns*fieldSize;
        this.WIDTH_MAP=this.rows*fieldSize;

        GrassField mapG= new GrassField(amountOfGrass , new Vector2d(rows,columns));
        SecondMap mapTest2= new SecondMap(amountOfGrass , new Vector2d(rows,columns));
        setParameters(mapG ,mapTest2);

        amountOfAnimalsStart=StartApplication.amountOfAnimals[0];

        ///PLACE ANIMALS ON MAPS
        ArrayList<Animal> animals = new ArrayList<>();
        for (int i= 0 ; i < amountOfAnimalsStart ; i++){
            animals.add(new Animal(mapG));
            mapG.statistics.addedAnimalStats(animals.get(i));
        }
        ArrayList<Animal> animals2 = new ArrayList<>();
        for (int i= 0 ; i < amountOfAnimalsStart ; i++){
            animals2.add(new Animal(mapTest2));
            mapTest2.statistics.addedAnimalStats(animals.get(i));
        }



        ///CHART AND STATISTICS FOR FIRST MAP (NORMAL MAP)
        ViewStats vievStatsPanels = new ViewStats(mapG );
        ViewStats vievStatsPanels1 = new ViewStats(mapTest2 );
        LineChart lineChart = vievStatsPanels.getChart();
        GridPane statsPanel = vievStatsPanels.getPanelStats();
        ///CHART AND STATISTICS TO SECOND MAP WITHOUT WALLS

        LineChart lineChart1 = vievStatsPanels1.getChart();
        GridPane statsPanel1 = vievStatsPanels1.getPanelStats();

        scheduledExecutorService2 = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService2.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                vievStatsPanels.runChart(mapG ,actualDay);
                vievStatsPanels1.runChart(mapTest2 , actualDay);
            });
        }, 400,600 , MILLISECONDS);

        ///GENERATE VIEW
        GridPane rootP = new GridPane();
        Parent pane1 = null;
        Parent pane2 = null;
        try {
            pane1 = CreateContent(mapG, animals, lineChart, statsPanel);
            pane2 = CreateContent(mapTest2, animals2, lineChart1, statsPanel1);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        rootP.add(pane1 , 0 ,0 );
        rootP.add(pane2 ,0,1);
        rootP.setGridLinesVisible(true);
        Scene scene = new Scene(rootP);
        stage.setScene(scene);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setHeight(primaryScreenBounds.getHeight());
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {

                Platform.exit();
                System.exit(0);
            }
        });
        stage.show();
        return;
    }


    //GENERATE NEW ANIMALS IN MAGIC OPTION
    public  void generateMagic(GrassField map, GraphicsContext g , Text magicAlert) {
        for (int i = 0; i < 5; i++) {
            Animal newAnimal = new Animal(map);
            Random generator = new Random();
            Object[] values =map.statistics.getGenotypes().values().toArray();
            Object randomValue = values[generator.nextInt(values.length)];
            newAnimal.setGenotype((int[])randomValue);
            map.statistics.addedAnimalStats(newAnimal);
            magicAlert.setText("MAGIC! 5 new animals");
            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(3)
            );
            visiblePause.setOnFinished(
                    event -> magicAlert.setVisible(false)
            );
            visiblePause.play();
        }

    }

    ///SET ENTRY PARAMETERS OF SIMULATION
    public void setParameters(GrassField map1 , SecondMap map2){
        map1.setSizeJungle(StartApplication.jungleProp);
        map2.setSizeJungle(StartApplication.jungleProp);
        map1.setEnergyFromGrass(StartApplication.amountOfAnimals[5]);
        map2.setEnergyFromGrass(StartApplication.amountOfAnimals[5]);
        map1.setEnergyLoss(StartApplication.amountOfAnimals[4]);
        map2.setEnergyLoss(StartApplication.amountOfAnimals[4]);
        map1.setStartEnergy(StartApplication.amountOfAnimals[3]);
        map2.setStartEnergy(StartApplication.amountOfAnimals[3]);
    }


    public void removeDeadAnimal(Object firstKey, int j ,Pane root ,GrassField map) {
           map.statistics.deadAnimalStats(map.multipleMap.get(firstKey).get(j));
           map.multipleMap.get(firstKey).get(j).setDeathDate(actualDay);
           root.getChildren().remove(map.multipleMap.get(firstKey).get(j).getAnimalG());
           map.multipleMap.get(firstKey).remove(j);
    }


    public void addfollowAnimal(GridPane panel , Text[] param){
        panel.add(new Text("Follow animal: "),0,0,2,1);
        panel.add(new Text("Children: "),0,1,1,1);
        panel.add(new Text("Descendands: "),0,2,1,1);
        panel.add(new Text("Day of death: "),0,3,1,1);
        panel.add(new Text("Genotype: "),0,4,1,1);
        panel.add(param[0],1,1,1,1);
        panel.add(param[1],1,2,1,1);
        panel.add(param[2],1,3,1,1);
        panel.add(param[3],1,4,1,1);

    }


    @Override
    public void positionChanged(Animal animal ,Vector2d oldPosition, Vector2d newPosition) {

    }
    @Override
    public void stop() throws Exception {
        super.stop();
    }

    @Override
    public void run() {

    }

    public static void main(String[] args) {
        launch();
    }



}