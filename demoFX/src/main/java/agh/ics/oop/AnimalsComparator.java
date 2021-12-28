package agh.ics.oop;

import java.util.Comparator;

public class AnimalsComparator implements Comparator<AbstractWorldMap> {
    @Override
    public int compare(AbstractWorldMap first, AbstractWorldMap second){
        return Integer.compare(first.animalsMap.size() , second.animalsMap.size());
    }
}
