package agh.ics.oop;

import java.util.ArrayList;

class RectangularMap  extends  AbstractWorldMap implements IWorldMap {


    protected Vector2d leftDown = new Vector2d(0, 0);
    protected Vector2d rightUpper = new Vector2d(4, 4);

   @Override
   public Vector2d getRightUp(){
       return this.rightUpper;
   }


    public static void main(String[] args) {

    }

public RectangularMap(){

}
    public RectangularMap(int height, int width) {
        super();

        this.leftDown = new Vector2d(0, 0);
        this.rightUpper = new Vector2d(width, height);
        ArrayList<Animal> animals = new ArrayList<>();
    }


 @Override
    public String toString(){
    MapVisualizer map = new MapVisualizer(this);
        return map.draw(this.leftDown,this.rightUpper);
    }


    @Override
    public boolean canMoveTo(Vector2d position) {
        if (position.precedes(this.rightUpper) && position.follows(this.leftDown) && objectAt(position)==null ) {
            return true;
        }
        return false;
    }

    @Override
    public boolean place(Animal animal) {
        if (!super.isOccupied(animal.position) && animal.position.precedes(this.rightUpper) && animal.position.follows(this.leftDown)) {
            this.animals.add(animal);
            //System.out.println(animal.toString() + "  " + this.animalsMap.toString());
            this.animalsMap.put(animal.position,animal);
            animal.addObserver((IPositionChangeObserver) this);
           return true;
        } else {
             return false;
        }
    }



}