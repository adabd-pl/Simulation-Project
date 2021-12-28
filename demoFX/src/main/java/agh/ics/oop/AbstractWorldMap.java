package agh.ics.oop;


import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public abstract class AbstractWorldMap implements IPositionChangeObserver {


    public AbstractWorldMap() {

    }


    public SimulationStats statistics= new SimulationStats();
    public Animal animalToFollow ;
    public abstract String toString();
    public ArrayList<Animal> animals = new ArrayList<>();
    protected ArrayList<Grass> grasses = new ArrayList<>();
    public HashMap<Vector2d , ArrayList< Animal>> multipleMap = new HashMap<Vector2d , ArrayList< Animal>>();
    public HashMap <Vector2d,Animal> animalsMap = new HashMap<Vector2d,Animal>();
     Vector2d rightUpper;
    //public abstract Vector2d getRightUp();
    public static void main(String[] args) {
        GrassField map = new GrassField(20 , new Vector2d(10,10));
        map.place(new Animal(map, new Vector2d(4,4)));
        System.out.println(map.getRightUp());
        System.out.println(map.toString());
        RectangularMap map1 = new RectangularMap(4,5);
        System.out.println(map1.getRightUp());

    }
  public Vector2d getRightUp(){
        return this.rightUpper;
    }
    public String toString1(){
        System.out.println("abstract");
        return this.toString();
    }

    public boolean canMoveTo(Vector2d position) {
          if ( objectAt(position)==null || objectAt(position) instanceof  Grass ) {

            return true;
        }
        return false;
    }

    public void moveAnimal(Animal animal, Vector2d.MoveDirection step, GraphicsContext g , int fieldSize) {
        System.out.println( animal.position.toString() + " -> " + step.toString());

        this.animalsMap.get(animal.position).move(step);

    }

    public boolean isOccupied(Vector2d position) {
        if (objectAt(position)!=null) {

            return true;
        }
        return false;
    }

    public Object objectAt(Vector2d position) {
        return this.multipleMap.get(position);

    }

    public boolean place (Animal animal){
        if (objectAt(animal.position) instanceof Grass) {
            this.grasses.remove(objectAt(animal.position));
        }
        if (!isOccupied(animal.position)) {
            animal.map = (GrassField) this;
            this.animals.add(animal);
            this.animalsMap.put(animal.position, animal);

            animal.addObserver((AbstractWorldMap) this );
            for (Vector2d pos  :animalsMap.keySet()) {
                System.out.println(pos);
            }
            return true;
        } else {

            return false;
        }
    }



    public void positionChanged(Animal animal , Vector2d oldPosition, Vector2d newPosition) {
        Animal newAnimal = (Animal) this.objectAt(oldPosition);
        this.animalsMap.put(newPosition , newAnimal);
        this.animalsMap.remove(oldPosition , this.objectAt(oldPosition));

    }
}

