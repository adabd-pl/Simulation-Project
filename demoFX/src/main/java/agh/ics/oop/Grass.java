package agh.ics.oop;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Grass {
    Vector2d position;

    public Grass(Vector2d position){
        this.position= position;
    }

    public Vector2d getPosition() {
        return position;
    }

    public String toString(){
        return "*";
    }


    public void draw(GraphicsContext g , int fieldSize){
        g.setFill(Color.GREEN);
        g.fillRect(this.getPosition().x*fieldSize +1, this.getPosition().y*fieldSize +1 ,fieldSize-1, fieldSize-1);
    }
}
