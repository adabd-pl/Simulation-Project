package agh.ics.oop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimulationStatsTest {
    //dodanie zwierzecia do statystyk
    @Test
    public void statsTest(){
        SimulationStats test = new SimulationStats();
        Animal animal = new Animal();
        test.addedAnimalStats(animal);
       assertEquals(animal.genotypeToString(), test.getDominantGenotype());
    }
}
