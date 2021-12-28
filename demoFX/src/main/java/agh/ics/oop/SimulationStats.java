package agh.ics.oop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

public class SimulationStats {
    /// SIMULATION STATISTICS
    Integer sumOfAnimals=0;
    Integer sumOfGrasses=0;
    int[] dominantGenotype;
    int sumEnergyAlive = 0 ;
    int sumLiveLength = 0;
    int averageChildrensAlive = 0;


    Integer sumOfAnimalsEnd=0;
    Integer sumOfGrassesEnd=0;
    int sumEnergyAliveEnd = 0 ;
    int averageLiveLengthEnd = 0;
    int averageChildrensAliveEnd = 0;
    Integer deaths =0;
    Map<Integer,int[]> genotypes = new HashMap< Integer, int[]>();

    public Map<Integer, int[]> getGenotypes() {
        return genotypes;
    }
    public Integer getLiveAnimals(){return this.sumOfAnimals-this.deaths;}
    public Integer getAllAnimals(){
        return this.sumOfAnimals;
    }
    public Integer getDeathAnimals(){
        return this.deaths;
    }
    public Integer getEnergy(){return  this.sumEnergyAlive;}

    public String getDominantGenotype(){
        StringBuilder gens = new StringBuilder("");
        for (int i =0 ;i <dominantGenotype.length ;i++){
            gens.append(dominantGenotype[i]);
        }
        return String.valueOf(gens);
    }
    public int getSumGrasses(){
        return this.sumOfGrasses;
    }

    public double getAverageEnergy(){
        if (this.sumOfAnimals.equals(this.deaths)){
            return 0;
        }
        return this.sumEnergyAlive/(this.sumOfAnimals-this.deaths);
    }

    public double averageLiveLength(){
        if(this.deaths == 0){
            return 0;
        }
        return  this.sumLiveLength/this.deaths;
    }

    public double getAverageChildrensAlive(){
        if(this.deaths.equals( this.sumOfAnimals)){
            return 0;
        }
        return this.averageChildrensAlive/(this.sumOfAnimals-this.deaths);
    }

    ///ADD ANIMAL INTO STATS
    public void addedAnimalStats(Animal animal){
        this.dominantGenotype = this.setDominantGenotype(animal.genotype);
        this.sumOfAnimals++;
        this.sumEnergyAlive= this.sumEnergyAlive + animal.getEnergy();

    }

    //UPDATE AFTER DEATH OF ANIMAL
    public void deadAnimalStats(Animal animal){
        this.deaths++;
        this.averageChildrensAlive-=(int)animal.children/2;
        this.sumLiveLength = this.sumLiveLength + animal.days;
        for (int i : genotypes.keySet()) {
            if(genotypes.get(i).equals(animal.genotype)){
                genotypes.put(i - 1, animal.genotype);
                break;
            }
        }
        this.dominantGenotype=checkDominantGenotype();
    }

    //UPDATE AFTER MOVE
    public void moveAnimalStats(Animal animal , GrassField map , int energyBefore, int energyAfter){
        this.sumEnergyAlive = this.sumEnergyAlive -energyBefore+energyAfter;
    }

    //UPDATE AFTER BIRTH
    public void birthAnimal(Animal parent1 , Animal parent2){
        this.averageChildrensAlive+=1;

    }

    //UPDATE AMOUNT OF GRASSES
    public void addStatsGrass(int i){
        this.sumOfGrasses=this.sumOfGrasses+i;
    }


    //UPDATE DOMINANT GENOTYPE
    public int[] setDominantGenotype(int[] n){
        for (int i : genotypes.keySet()) {
            if(genotypes.get(i).equals(n)){
                genotypes.put(i + 1, n);
                break;
            }
        }
        if(genotypes.containsValue(n)==false){
            genotypes.put(1, n);
        }
        //genotypes.forEach((key, value) -> System.out.println(key + " : " + value));
        int maxKey = 0;
        for (int i : genotypes.keySet()) {

            if (maxKey < i) {
                maxKey = i;
            }
        }
        return genotypes.get(maxKey);
    }

    public int[] checkDominantGenotype(){
        int maxKey = 0;
        for (int i : genotypes.keySet()) {
            if (maxKey < i) {
                maxKey = i;
            }
        }
        return genotypes.get(maxKey);
    }

    ///WRITE TO CSV FILE - HEADERS
    public void setHeadersCSV(File statisticsCSV) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter (new FileOutputStream(statisticsCSV, false));
        printWriter.append("sumOfAnimals");
        printWriter.append(";");
        printWriter.append("sumOfGrasses");
        printWriter.append(";");
        printWriter.append("sumEnergyAlive");
        printWriter.append(";");
        printWriter.append("averageLiveLength");
        printWriter.append(";");
        printWriter.append("averageChildrensAlive");
        printWriter.append(System.lineSeparator());
        printWriter.close();
    }
    ///WRITE TO CSV FILE
    public void toCSV(File statisticsCSV) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter (new FileOutputStream(statisticsCSV, true));
        this.sumOfAnimalsEnd+=this.sumOfAnimals;
        this.averageLiveLengthEnd+=this.averageLiveLength();
        this.averageChildrensAliveEnd+=this.getAverageChildrensAlive();
        this.sumOfGrassesEnd+=this.sumOfGrassesEnd;
        this.sumEnergyAliveEnd+=this.getAverageEnergy();

        printWriter.write(String.valueOf(this.sumOfAnimals));
        printWriter.write(";");
        printWriter.write(String.valueOf(this.sumOfGrasses));
        printWriter.write(";");
        printWriter.write(String.valueOf(this.getAverageEnergy()));
        printWriter.write(";");
        printWriter.write(String.valueOf(this.averageLiveLength()));
        printWriter.write(";");
        printWriter.write(String.valueOf(this.getAverageChildrensAlive()));
        printWriter.write(System.lineSeparator());
        printWriter.close();

    }


    public void summaryToCSV(File statisticsCSV ,int dayOfEnd) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter (new FileOutputStream(statisticsCSV, true));
        printWriter.append(System.lineSeparator());
        printWriter.append("Summary:");
        printWriter.append(System.lineSeparator());
        printWriter.append(String.valueOf(this.sumOfAnimalsEnd/dayOfEnd));
        printWriter.append(";");
        printWriter.append(String.valueOf(this.sumOfGrassesEnd/dayOfEnd));
        printWriter.append(";");
        printWriter.append(String.valueOf(this.sumEnergyAliveEnd/dayOfEnd));
        printWriter.append(";");
        printWriter.append(String.valueOf(this.averageLiveLengthEnd/dayOfEnd));
        printWriter.append(";");
        printWriter.append(String.valueOf(this.averageChildrensAliveEnd/dayOfEnd));
        printWriter.append(";");

    }

}
