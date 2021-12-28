package agh.ics.oop;


import com.example.demofx.StartApplication;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.*;
import java.util.SortedSet;

import static java.lang.Math.max;

public class Animal implements Comparable<Animal> , IChildrenObserver ,IChildrenObservable{
    Color colorFollowed = Color.rgb(255, 51, 0);
    Vector2d position = new Vector2d(2,2);
    Vector2d.MapDirection directionOnMap = Vector2d.MapDirection.NORTH;
    int[] genotype = new int[32];
    int energy  ;
    Circle animalG;
    public int days=0;
    int children=0;
    int fieldSize=20;

    Comparator comparator = new AnimalsComparator();
    SortedSet<IPositionChangeObserver> observers = new TreeSet(comparator);
    public GrassField map;
    ArrayList<IChildrenObserver> childrenObservers = new ArrayList<>();
    Set<Animal> descendants = new TreeSet<Animal>();

    public int getAfterClickDestendands() {
        return afterClickDestendands;
    }

    private int afterClickDestendands =0;
    private int afterClickCildren = 0;

    public int getAfterClickCildren() {
        return afterClickCildren;
    }



    public void setGenotype(int[] genotype) {
        this.genotype = genotype;
    }
    public int getDescendants(){ return this.descendants.size(); }

    public Vector2d getPosition(){
        return this.position;
    }
    public Circle getAnimalG(){
        return this.animalG;
    }
    public  int getChildren(){return this.children;}
    public int getEnergy() {return this.energy;}
    public String genotypeToString(){
        StringBuilder gens = new StringBuilder("");
        for (int i =0 ;i <genotype.length ;i++){
            gens.append(genotype[i]);
        }
        return String.valueOf(gens);
    }

    public static void main(String[] args) {
        Vector2d testPos = new Vector2d(1,2);
        Animal newPet = new Animal();
        System.out.println(newPet.isAt(testPos));
        newPet.move(Vector2d.MoveDirection.BACKWARD);
        System.out.println(newPet.toString());
        newPet.move(Vector2d.MoveDirection.FOWARD);
        System.out.println(newPet.toString());

    }

    public Animal(){
    }

    public void updateChildren(){
        this.children++;
    }

    public void updateEnergy(int value ,SimulationStats stats) {
        stats.moveAnimalStats(this, (GrassField) this.map,this.energy, max(0,this.energy-value));
        this.energy =max(0 , this.energy -value);

    }

    public void setDeathDate(int date){
        this.days=date;
    }

    ///CHILD OF TWO ANIMALS
    public Animal(GrassField map , Animal mother , Animal father){
        int genFromMother = mother.energy/(mother.energy+father.energy) * 32;
        this.map =map;
        if(Math.random()>0.5){
            Animal tmp = mother;
            mother = father;
            father = tmp;
        }
        for (int i = 0 ; i< 32;i++){
            if (genFromMother>0){
                this.genotype[i]=mother.genotype[i];
                genFromMother--;
            }
            else {
                this.genotype[i]=father.genotype[i];
            }
        }
        Arrays.sort(this.genotype);


        this.generatePositionChild();
        int newMotherEnergy = (int) (mother.energy * 0.75);
        int newFatherEnergy = (int) (father.energy * 0.75);
        this.energy= mother.energy - newMotherEnergy + father.energy -newFatherEnergy;

        map.statistics.moveAnimalStats(mother, (GrassField) this.map, mother.energy ,(int) (mother.energy * 0.75));
        map.statistics.moveAnimalStats(father, (GrassField) this.map, father.energy , (int) (father.energy * 0.75));

        mother.energy = newMotherEnergy;
        father.energy = newFatherEnergy;
        mother.newChildren(this);
        father.newChildren(this);

        this.childrenObservers.add(mother);
        this.childrenObservers.add(father);

        this.map.place(this);
        this.animalG= new Circle(this.getPosition().x*fieldSize + fieldSize/2 +1,this.getPosition().y*fieldSize + fieldSize/2 +1, fieldSize/2-1);
        this.animalG.setFill(setColor(0));

    }


    public Animal(GrassField map){
        for(int i=0;i<this.genotype.length;i++)
        {
            this.genotype[i] = (int)(Math.random() * 8);
        }
        this.map=map;
        this.generatePositionChild();
        Arrays.sort(this.genotype);
        this.energy = StartApplication.amountOfAnimals[3];
       // this.map.statistics.addedAnimalStats(this);
        this.map.place(this);
        this.animalG= new Circle(this.getPosition().x*fieldSize + fieldSize/2 +1,this.getPosition().y*fieldSize + fieldSize/2 +1, fieldSize/2-1);
        this.animalG.setFill(setColor(0));
    }

    public void generatePositionChild(){
        while(true){
            int x = (int) (Math.random() * map.getRightUpper().x);
            int y = (int) (Math.random() * map.getRightUpper().y);
            if (this.map.multipleMap.get(new Vector2d(x,y)) ==null || this.map.multipleMap.get(new Vector2d(x,y)).size()==0){
                this.position= new Vector2d( x, y);
                return;
            }
        }
    }

   public Animal(AbstractWorldMap map, Vector2d initialPosition){
        this.position=initialPosition;
        for(int i=0;i<this.genotype.length;i++)
        {
            this.genotype[i] = (int)(Math.random() * 8);
        }
        Arrays.sort(this.genotype);
        this.map= (GrassField) map;
        this.map.place(this);
        this.energy = StartApplication.amountOfAnimals[3];
        this.map.statistics.addedAnimalStats(this);

    }

    public String toString(){
        String positionString = directionOnMap.toString() + position.toString();
        return positionString;
    }

    public boolean isAt(Vector2d position){
        if (this.position.equals(position)){
            return true;
        }
        else{
            return false;
        }
    }

    public void move(Vector2d.MoveDirection direction) {
        int x = this.position.x;
        int y = this.position.y;
        switch (direction) {
            case LEFT:
                directionOnMap = directionOnMap.previous();
                break;
            case RIGHT:
                directionOnMap = directionOnMap.next();
                break;
        }
        switch (this.directionOnMap) {
            case NORTH:
                if (direction.equals(Vector2d.MoveDirection.FOWARD) && position.y  < this.map.getRightUp().y) {
                    y += 1;
                } else if (direction.equals(Vector2d.MoveDirection.BACKWARD) && position.y - 1 >= 0) {
                    y -= 1;
                }
                break;
            case EAST:
                if (direction.equals(Vector2d.MoveDirection.FOWARD) && position.x  < this.map.getRightUp().x) {
                    x += 1;
                } else if (direction.equals(Vector2d.MoveDirection.BACKWARD) && position.x - 1 >= 0) {
                    x -= 1;
                }
                break;
            case WEST:
                if (direction.equals(Vector2d.MoveDirection.FOWARD) && position.x - 1 >= 0) {
                    x -= 1;
                } else if (direction.equals(Vector2d.MoveDirection.BACKWARD) && position.x < this.map.getRightUp().x) {
                    x += 1;
                }
                break;
            case SOUTH:
                if (direction.equals(Vector2d.MoveDirection.FOWARD) && position.y - 1 >= 0) {
                    y -= 1;
                } else if (direction.equals(Vector2d.MoveDirection.BACKWARD) && position.y  < this.map.getRightUp().y) {
                    y += 1;
                }
                break;
            case NORTHWEST:
                if (direction.equals(Vector2d.MoveDirection.FOWARD) && position.y  < this.map.getRightUp().y && position.x-1>=0) {
                    y += 1;
                    x -= 1;
                } else if (direction.equals(Vector2d.MoveDirection.BACKWARD) && position.y - 1 >= 0) {
                    y -= 1;
                }
                break;
            case NORTHEAST:
                if (direction.equals(Vector2d.MoveDirection.FOWARD) && position.x  < this.map.getRightUp().x && position.y  < this.map.getRightUp().y) {
                    x += 1;
                    y+=1;
                } else if (direction.equals(Vector2d.MoveDirection.BACKWARD) && position.x - 1 >= 0) {
                    x -= 1;
                }
                break;
            case SOUTHWEST:
                if (direction.equals(Vector2d.MoveDirection.FOWARD) && position.x - 1 >= 0   && position.y-1>=0) {
                    x -= 1;
                    y-=1;
                } else if (direction.equals(Vector2d.MoveDirection.BACKWARD) && position.x   < this.map.getRightUp().x) {
                    x += 1;
                }
                break;
            case SOUTHEAST:
                if (direction.equals(Vector2d.MoveDirection.FOWARD) && position.y - 1 >= 0  && position.x<this.map.getRightUp().x) {
                    y -= 1;
                    x +=1;
                } else if (direction.equals(Vector2d.MoveDirection.BACKWARD) && position.y  < this.map.getRightUp().y) {
                    y += 1;
                }
        }

        if (!(x == position.x && y == position.y) && map.canMoveTo(new Vector2d(x,y)) ) {

            this.updateEnergy(map.getEnergyLoss() , map.statistics);
            this.positionChanged(this, position, new Vector2d(x,y));
            this.position= new Vector2d(x,y);

        }
    }


    public void moveSecondMap(Vector2d.MoveDirection direction) {
        int x = this.position.x;
        int y = this.position.y;
        switch (direction) {
            case LEFT:
                directionOnMap = directionOnMap.previous();
                break;
            case RIGHT:
                directionOnMap = directionOnMap.next();
                break;
        }
         switch (this.directionOnMap) {
            case NORTH:
                if (direction.equals(Vector2d.MoveDirection.FOWARD) ) {
                    y += 1;
                } else if (direction.equals(Vector2d.MoveDirection.BACKWARD) ) {
                    y -= 1;
                }
                break;
            case EAST:
                if (direction.equals(Vector2d.MoveDirection.FOWARD) ) {
                    x += 1;
                } else if (direction.equals(Vector2d.MoveDirection.BACKWARD)) {
                    x -= 1;
                }
                break;
            case WEST:
                if (direction.equals(Vector2d.MoveDirection.FOWARD)) {
                    x -= 1;
                } else if (direction.equals(Vector2d.MoveDirection.BACKWARD) ) {
                    x += 1;
                }
                break;
            case SOUTH:
                if (direction.equals(Vector2d.MoveDirection.FOWARD) ) {
                    y -= 1;
                } else if (direction.equals(Vector2d.MoveDirection.BACKWARD) ) {
                    y += 1;
                }
                break;
            case NORTHWEST:
                if (direction.equals(Vector2d.MoveDirection.FOWARD) ) {
                    y += 1;
                    x -= 1;
                } else if (direction.equals(Vector2d.MoveDirection.BACKWARD) ) {
                    y -= 1;
                }
                break;
            case NORTHEAST:
                if (direction.equals(Vector2d.MoveDirection.FOWARD)) {
                    x += 1;
                    y+=1;
                } else if (direction.equals(Vector2d.MoveDirection.BACKWARD) ) {
                    x -= 1;
                }
                break;
            case SOUTHWEST:
                if (direction.equals(Vector2d.MoveDirection.FOWARD) ) {
                    x -= 1;
                    y-=1;
                } else if (direction.equals(Vector2d.MoveDirection.BACKWARD) ) {
                    x += 1;
                }
                break;
            case SOUTHEAST:
                if (direction.equals(Vector2d.MoveDirection.FOWARD) ) {
                    y -= 1;
                    x +=1;
                } else if (direction.equals(Vector2d.MoveDirection.BACKWARD) ) {
                    y += 1;
                }
        }
        if (x<0){
            x= x+map.getRightUp().x;
        }
        else if(x>map.getRightUp().x){
            x=x-map.getRightUp().x;
        }
        if (y<0){
            y=y+map.getRightUp().y;
        }
        else if(y>map.getRightUp().y){
            y=y-map.getRightUp().y;
        }
        if (!(x == position.x && y == position.y) && map.canMoveTo(new Vector2d(x,y)) ) {
            this.updateEnergy(map.getEnergyLoss() , map.statistics);
            this.positionChanged(this, position, new Vector2d(x,y));
            this.position= new Vector2d(x,y);
        }
    }


    public void addObserver(IPositionChangeObserver observer){
        observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer){
        observers.remove(observer);
    }

    public void positionChanged(Animal animal , Vector2d oldPosition , Vector2d newPosition){
        for (IPositionChangeObserver observer : this.observers) {
            observer.positionChanged(this,  oldPosition, newPosition);
        }
    }

    public void draw(Pane g , int fieldSize ,Animal followAnimal){
        Animal animal = this;
        GrassField mapG = (GrassField) this.map;
        Circle animalOn = new Circle(this.getPosition().x*fieldSize + fieldSize/2 + 0.5,this.getPosition().y*fieldSize + fieldSize/2 + 0.5 , (fieldSize-1)/2);
        animalOn.setFill(this.setColor(0)); //animalG.getFill());
        this.animalG= animalOn;

        //onclick to follow animal
        animalOn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
               // animal.genotypeToString();
                //System.out.println(animal.getEnergy());
                animal.animalG.setFill(colorFollowed);
                animal.afterClickDestendands=animal.getDescendants();
                animal.afterClickCildren=animal.getChildren();
                if (mapG.animalToFollow !=null){

                    mapG.animalToFollow.animalG.setFill(mapG.animalToFollow.setColor(1));
                }
                mapG.animalToFollow = animal;
            }
        });
          g.getChildren().add(animalOn);
    }


    public Vector2d.MoveDirection renderMove(){
        int rnd = new Random().nextInt(this.genotype.length);
        for (int i = 0; i<this.genotype[rnd]; i++ ){
            this.directionOnMap = this.directionOnMap.next();
        }
        if (genotype[rnd]==4){
            return Vector2d.MoveDirection.BACKWARD;
        }
        if (genotype[rnd]==0){
            return Vector2d.MoveDirection.FOWARD;
        }
        return null;
    }


    public Color setColor(int stopFollow){
        if (this.animalG.getFill().equals(colorFollowed) && stopFollow==0){
            return colorFollowed;
        }
        if (this.energy>this.map.getStartEnergy()*0.8){
            return Color.web("#370f28");
        }
        if(this.energy>this.map.getStartEnergy()*0.6){
            return Color.web("#6e1f50");
        }
        if (this.energy>this.map.getStartEnergy()*0.4){
            return Color.web("#a62f79");
        }
        if(this.energy>this.map.getStartEnergy()*0.2){
            return Color.web("#a62f79");
        }

        return Color.web("#cc509d");

    }
    @Override
    public int compareTo(Animal o) {

        if(this.getEnergy()>o.getEnergy()){
            return -1;
        }
        return 1;

    }

    @Override
    public void addObserver(IChildrenObserver observer) {
        this.childrenObservers.add(observer);
    }

    @Override
    public void removeObserver(IChildrenObserver observer) {
        this.childrenObservers.remove(observer);
    }

    @Override
    public void newChildren() {

    }

    @Override
    public void newChildren(Animal newDescendant) {
        this.descendants.add(newDescendant);
        for(IChildrenObserver p : this.childrenObservers){
            p.newChildren(newDescendant);
        }

    }
}
