package agh.ics.oop;

import javafx.scene.canvas.GraphicsContext;

public class SecondMap extends GrassField{
    ///SECOND MAP WITHOUT WALLS
    public SecondMap(int amountOfGrass, Vector2d rightUpper) {
        super(amountOfGrass, rightUpper);
    }
    @Override
    public void moveAnimal(Animal animal, Vector2d.MoveDirection step , GraphicsContext g , int fieldSize) {
        this.multipleMap.get(animal.position).get(this.multipleMap.get(animal.position).indexOf(animal)).moveSecondMap(step);

    }


}
