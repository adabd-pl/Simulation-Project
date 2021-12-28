package com.example.demofx;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class StartApplication extends Application {
    public static int amountOfAnimals[] = new int[]{100, 15,15,100,4,10};
    public static double jungleProp =0.4;
    public static boolean magicOption;


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Simulation options");
        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(20);

        gridPane.setPadding(new Insets(10, 100, 10, 100));
        Button button1 = new Button("Start Simulation");

        Slider slider[] = {new Slider(0, 100, 20), new Slider(0, 100, 2), new Slider(0, 100, 15), new Slider(0, 100, 100), new Slider(0, 100,  15), new Slider(0, 100, 15),new Slider(0, 1, 0.4)};
        Text chooseAmount[] = {new Text("20.0"), new Text("15.0"), new Text("15.0"), new Text("100.0"), new Text("2.0"), new Text("15.0") ,new Text("0.4")};
        ///domyślne wartości na potrzeby testów
        double[] value = {20, 15, 15, 100, 2, 15,0.4};

        slider[6].setMajorTickUnit(0.1);
        slider[6].setSnapToTicks(true);
        slider[6].setShowTickMarks(true);
        slider[6].setShowTickLabels(true);
        slider[6].valueProperty().addListener((obs, oldval, newVal) -> slider[6].setValue(Math.round(10.0 *newVal.doubleValue())/10.0));
        slider[6].valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                value[6] = (double) new_val;
                chooseAmount[6].setText(String.valueOf( new_val));
            }
        });

        Text headers[] = {new Text("Amount of animal: "), new Text("Width map: "), new Text("Height map: "), new Text("Start energy: "), new Text("Move energy: "), new Text("Grass energy: ") ,new Text("Jungle ratio: ")};
        for (int i = 0; i < headers.length; i++) {
            if (i!=6){
                sliderSet( value,  slider[i], chooseAmount[i] ,i);

            }
            gridPane.add(headers[i], 0, i*2, 2, 1);
            gridPane.add(slider[i], 0, i*2+1, 3, 1);
            gridPane.add(chooseAmount[i], 2, i*2, 1, 1);

        }


        gridPane.add(button1, 1, headers.length*2+1, 1, 2);

        //Magic version of simulation
        final CheckBox cb =new CheckBox("magic!");
        cb.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                magicOption = true;
            }
        });
        gridPane.add(cb , 2,headers.length*2+1,1,1);
        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);
        primaryStage.show();

        ///wprowadzenie danych do symulacji
        button1.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                try {
                    int cnt = 0;
                    SimulationApplication main = null;
                    for (int i = 0; i < amountOfAnimals.length; i++) {

                        if (0 != (int) value[i]) {
                            amountOfAnimals[i] = (int) value[i];
                            System.out.println(value[i] + "|" + (int) value[i]);
                            cnt++;
                        }
                    }
                    if(value[0] > value[1]*value[2]){
                        cnt--;
                    }
                    if (0 !=  value[6]) {
                        jungleProp =  value[6];
                        System.out.println(value[6] + "|" + (int) value[6]);
                        cnt++;
                    }
                    if (cnt == 7 ){
                        main = new SimulationApplication();  ///start aplikacji po wprowadzeniu wszytskich danych
                        main.start(new Stage());

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }));
        return;
    }


    public void sliderSet(  double[] value , Slider slider , Text chooseAmount , int i){
        slider.setMajorTickUnit(1.0);
        slider.setSnapToTicks(true);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);

        slider.valueProperty().addListener((obs, oldval, newVal) ->
                slider.setValue(Math.round(newVal.doubleValue())));
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                value[i] = new_val.intValue();
                chooseAmount.setText(String.valueOf( new_val));
            }
        });


    }

    public static  void main(String[] args){
            launch();


    }
}
