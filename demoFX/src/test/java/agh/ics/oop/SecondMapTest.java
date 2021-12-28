package agh.ics.oop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SecondMapTest {
    //test działania mapy nr 2
    @Test
    public void testMap(){
        SecondMap newMap = new SecondMap(5,new Vector2d(5,5));
        newMap.place(new Animal(newMap,new Vector2d(3,4)));
        newMap.place(new Animal(newMap,new Vector2d(6,6)));
        newMap.toString();
        assertNotEquals(newMap.objectAt(new Vector2d(3,4)),null);

    }
}
