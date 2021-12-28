package agh.ics.oop;

import java.util.ArrayList;

public class SimulationEngine implements IEngine{
    Vector2d[] animalsOnMap;
    AbstractWorldMap mapWithAnimals;

    Vector2d.MoveDirection [] movesAnimals;

    public static void main(String[] args) {

    }

    public ArrayList<Animal> getAnimals(){
        return this.mapWithAnimals.animals;
    }

    public void printAnimals(){
        System.out.println(this.mapWithAnimals.animalsMap.toString());
    }

    public void animalsToString(){
        System.out.println(mapWithAnimals.toString());
    }

    public SimulationEngine(Vector2d.MoveDirection[] directions, AbstractWorldMap map, Vector2d[] positions) {
        this.animalsOnMap = positions;
        this.mapWithAnimals =  map;
        this.movesAnimals = directions;

        for (int i =0 ; i<animalsOnMap.length ; i++){
            Animal newPet = new Animal( this.mapWithAnimals,animalsOnMap[i]);
           // System.out.println(newPet.position.toString());
            this.mapWithAnimals.place(newPet);
              }

    }

    @Override
    public void run() {

        for (int i =0 ; i<this.movesAnimals.length; i++){
               int j = i%this.mapWithAnimals.animals.size();
               Vector2d oldPosition = this.mapWithAnimals.animals.get(j).position;
               System.out.println("After move " + j +this.mapWithAnimals.animals.get(j).toString()  + this.mapWithAnimals.animals.get(j).position.toString());
               Vector2d newPosition = this.mapWithAnimals.animals.get(j).position;


            System.out.println("Animals: ");
            for(int k=0 ; k<this.animalsOnMap.length; k++){
                System.out.println( "   " + this.mapWithAnimals.animals.get(k).toString() + " " + this.mapWithAnimals.animals.get(k).position.toString());
            }


            System.out.println("Map: ");
            System.out.println( this.mapWithAnimals.toString() );
            System.out.println("===========================");
           }
            for(int j=0 ; j<this.animalsOnMap.length; j++){
                System.out.println( this.mapWithAnimals.animals.get(j).toString() + this.mapWithAnimals.animals.get(j).position.toString());
            }

    }

    }
