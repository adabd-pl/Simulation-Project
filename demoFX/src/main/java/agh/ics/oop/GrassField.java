package agh.ics.oop;

import com.example.demofx.StartApplication;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.stream.Collectors;

public class GrassField  extends AbstractWorldMap    implements  IWorldMap {
    private int amountOfGrass;
    int energyLoss = StartApplication.amountOfAnimals[4];
    int startEnergy;
    Vector2d jungleLeftDown;
    Vector2d jungleRightUp;
    int energyFromGrass = StartApplication.amountOfAnimals[5];
    double sizeJungle;
    int actualDayMap = 0;


    public Vector2d getJungleRightUp() {
        return this.jungleRightUp;
    }

    public Vector2d getJungleLeftDown() {
        return this.jungleLeftDown;
    }

    public int getStartEnergy() {
        return startEnergy;
    }

    public int getEnergyLoss() {
        return energyLoss;
    }

    public Vector2d getRightUpper() {
        return rightUpper;
    }

    public void setStartEnergy(int startEnergy) {
        this.startEnergy = startEnergy;
    }

    public void setSizeJungle(double sizeJungle) {
        this.sizeJungle = sizeJungle;
    }

    public void setEnergyLoss(int energyLoss) {
        this.energyLoss = energyLoss;
    }

    public void setEnergyFromGrass(int energyFromGrass) {
        this.energyFromGrass = energyFromGrass;
    }


    @Override
    public Vector2d getRightUp() {
        return this.rightUpper;
    }


    public void addGrasses(int amountOfGrass) {
        Random generator = new Random();
        this.amountOfGrass = amountOfGrass;
        int i = 0;
        int cnt = 0;
        while (i < this.amountOfGrass) {
            Grass potential = new Grass(new Vector2d(generator.nextInt(this.rightUpper.x), generator.nextInt(this.rightUpper.y)));
            for (int j = 0; j < this.grasses.size(); j++) {
                if (potential.equals(this.grasses.get(j))) {
                    cnt += 1;
                    break;
                }
            }
            if (this.multipleMap.get(potential.getPosition()) != null) {
                cnt += 1;
                break;
            }
            if (cnt == 0) {
                this.grasses.add(potential);
                i++;
            }
        }
    }

    public void grassesToString() {
        for (int i = 0; i < grasses.size(); i++) {
            System.out.println("- " + grasses.get(i).position.toString());
        }
    }

    public GrassField(int amountOfGrass, Vector2d rightUpper) {
        this.rightUpper = rightUpper;
        this.amountOfGrass = amountOfGrass;

        addGrasses(amountOfGrass);

        this.jungleLeftDown = new Vector2d((int) (this.rightUpper.x / 2 - Math.round(this.rightUpper.x * StartApplication.jungleProp / 2)), (int) (this.rightUpper.y / 2 - Math.round(this.rightUpper.y * StartApplication.jungleProp / 2)));
        this.jungleRightUp = new Vector2d((int) (this.rightUpper.x / 2 + Math.round(this.rightUpper.x * StartApplication.jungleProp / 2)), (int) (this.rightUpper.y / 2 + Math.round(this.rightUpper.y * StartApplication.jungleProp / 2)));
        this.statistics.sumOfGrasses += amountOfGrass;
        this.statistics.addStatsGrass(amountOfGrass);
    }

    public static void main(String[] args) {
        GrassField mapTest = new GrassField(10, new Vector2d(10, 10));
        System.out.println(mapTest);
    }

    @Override
    public String toString() {
        System.out.println(this.animalsMap.toString());
        Vector2d leftDown = new Vector2d(0, 0);
        MapVisualizer map = new MapVisualizer(this);
        return map.draw(leftDown, rightUpper);
    }


    @Override
    public Object objectAt(Vector2d position) {
        Object getUp = super.objectAt(position);
        if (getUp != null) {
            return getUp;
        }
        for (int i = 0; i < (this.grasses.size()); i++) {
            if (this.grasses.get(i).position.equals(position)) {

                return this.grasses.get(i);
            }
        }
        return null;
    }


    @Override
    public boolean canMoveTo(Vector2d position) {
        return true;
    }


    @Override
    public void moveAnimal(Animal animal, Vector2d.MoveDirection step, GraphicsContext g, int fieldSize) {
        this.multipleMap.get(animal.position).get(this.multipleMap.get(animal.position).indexOf(animal)).move(step);
    }


    ///GENERATE ONE GRASS IN JUNGLE AND ONE OUTSIDE
    public void addOneGrass(GraphicsContext g, int fieldSize) {
        Random generator = new Random();
        int i = 0;
        int k = 0;
        int cnt = 1;
        while (i == 0 || k == 0) {
            Grass potential = new Grass(new Vector2d(generator.nextInt(this.rightUpper.x + 1), generator.nextInt(this.rightUpper.y + 1)));
            if (i == 0 && !(potential.getPosition().follows(this.jungleLeftDown) && potential.getPosition().precedes(this.jungleRightUp))) {
                for (int j = 0; j < this.grasses.size(); j++) {
                    if (potential.equals(this.grasses.get(j))) {
                        cnt += 1;
                        break;
                    }
                }
                if (this.animalsMap.get(potential.getPosition()) != null) {
                    cnt += 1;
                }
                if (cnt == 0) {
                    this.grasses.add(potential);
                    potential.draw(g, fieldSize);
                    i++;
                }
                cnt = 0;
            } else if (k == 0 && (potential.getPosition().follows(this.jungleLeftDown) && potential.getPosition().precedes(this.jungleRightUp))) {
                potential.draw(g, fieldSize);
                k++;
            }
        }
        this.statistics.addStatsGrass(2);
    }


    @Override
    public boolean place(Animal animal) {
        if (objectAt(animal.position) instanceof Grass) {
            this.grasses.remove(objectAt(animal.position));
            this.statistics.sumOfGrasses--;
        }
        if (this.multipleMap.get(animal.position) == null) {
            this.multipleMap.put(animal.position, new ArrayList<>());
        }
        this.multipleMap.get(animal.position).add(animal);
        animal.addObserver(this);
        return true;
    }

    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {
        int eatGrass = 0;
        if (this.objectAt(newPosition) instanceof Grass) {
            for (int i = 0; i < this.grasses.size(); i++) {
                if (this.grasses.get(i).position.equals(newPosition)) {
                    this.grasses.remove(i);
                    this.statistics.sumOfGrasses--;
                    eatGrass = StartApplication.amountOfAnimals[5];
                }
            }
        }
        if (eatGrass != 0) {
            animal.updateEnergy((-1 * eatGrass), animal.map.statistics);
        }
        this.multipleMap.get(oldPosition).remove(animal);
        if (this.multipleMap.get(newPosition) == null) {
            this.multipleMap.put(newPosition, new ArrayList<>());
        }
        this.multipleMap.get(newPosition).add(animal);
    }


    public void drawGrasses(GraphicsContext g, int fieldSize) {
        grasses.forEach(p -> p.draw(g, fieldSize));
    }

    public void drawFieldOfMap(GraphicsContext g, Color fieldFill, int fieldSize, int x, int y) {
        g.setFill(Color.BLACK);
        g.fillRect(x * fieldSize, y * fieldSize, fieldSize + 1, fieldSize + 1);
        g.setFill(fieldFill);
        g.fillRect(x * fieldSize + 1, y * fieldSize + 1, fieldSize - 1, fieldSize - 1);

    }

    public void showAllWithDominant() {
        for (int i = 0; i < this.multipleMap.size(); i++) {
            Object firstKey = this.multipleMap.keySet().toArray()[i];
            for (int j = 0; j < this.multipleMap.get(firstKey).size(); j++) {
                Animal animal = this.multipleMap.get(firstKey).get(j);
                if (animal != null && animal.genotypeToString().equals(this.statistics.getDominantGenotype())) {
                    animal.animalG.setFill(Color.rgb(0, 0, 200));
                }
            }
        }
    }
}
