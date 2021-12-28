package com.example.demofx;

import agh.ics.oop.Grass;
import agh.ics.oop.GrassField;
import javafx.application.Platform;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import static java.lang.Integer.max;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class ViewStats {
    final CategoryAxis xAxis2 = new CategoryAxis();
    final NumberAxis yAxis2 = new NumberAxis();
    final LineChart<String,Number> lineChart2 = new LineChart<>(xAxis2, yAxis2);
    XYChart.Series<String, Number> series2 = new XYChart.Series<>();
    XYChart.Series<String, Number> series12 = new XYChart.Series<>();
    Text[] paramPanel2 = new Text[]{new Text("0"),new Text("0"),new Text("0"),new Text("0"),new Text("0"),new Text("0"),new Text("0")};
    GridPane statsPanel2 = new GridPane();
    final int WINDOW_SIZE = 20;
    private ScheduledExecutorService scheduledExecutorService2;

    public ViewStats(GrassField map ){
        xAxis2.setLabel("Day");
        xAxis2.setAnimated(false);
        yAxis2.setLabel("Value");
        yAxis2.setAnimated(false);
        lineChart2.setAnimated(false);
        series2.setName("All animals");
        series12.setName("Average energy");
        lineChart2.setCreateSymbols(false);

        lineChart2.getData().add(series2);
        lineChart2.getData().add(series12);
        addToStatsPanel(statsPanel2 ,map, paramPanel2);

    }
    public LineChart getChart (){
        return lineChart2;
    }

    public GridPane getPanelStats(){
        return statsPanel2;
    }

    public void runChart(GrassField map, int actualDay) {
        paramPanel2[0].setText(String.valueOf(map.statistics.getAllAnimals())); //+"\n" +String.valueOf(this.map.statistics.getAverageEnergy())+"\n"+String.valueOf(this.map.statistics.getDeathAnimals())+"\n"+ String.valueOf(this.map.statistics.getAverageChildrensAlive())+"\n"+"0" +"\n"+String.valueOf(this.map.statistics.averageLiveLength()));
        paramPanel2[1].setText(String.valueOf(map.statistics.getAverageEnergy()));
        paramPanel2[2].setText(String.valueOf(map.statistics.getDeathAnimals()));
        paramPanel2[3].setText(String.valueOf(map.statistics.getAverageChildrensAlive()));
        paramPanel2[4].setText(map.statistics.getDominantGenotype());
        paramPanel2[5].setText(String.valueOf(map.statistics.averageLiveLength()));
        paramPanel2[6].setText(String.valueOf(map.statistics.getSumGrasses()));
        series2.getData().add(new XYChart.Data<>(String.valueOf( actualDay), map.statistics.getAllAnimals()));
        series12.getData().add(new XYChart.Data<>(String.valueOf(actualDay),map.statistics.getAverageEnergy()));
        if (series2.getData().size() > WINDOW_SIZE)
            series2.getData().remove(0);
        if (series12.getData().size() > WINDOW_SIZE)
            series12.getData().remove(0);

    }

    public void addToStatsPanel(GridPane panel  , GrassField map , Text[] param){
        panel.add(new Text("All animals: "),0,0,1,1);
        panel.add(new Text("Average energy: "),0,1,1,1);
        panel.add(new Text("All death: "),0,2,1,1);
        panel.add(new Text("Children per animal: "),0,3,1,1);
        panel.add(new Text("Dominant genotype: "),0,4,1,1);
        panel.add(new Text("Average live length: "),0,5,1,1);
        panel.add(new Text("All grasses: "),0,6,1,1);
        panel.add(param[0],1,0,1,1);
        panel.add(param[1],1,1,1,1);
        panel.add(param[2],1,2,1,1);
        panel.add(param[3],1,3,1,1);
        panel.add(param[4],1,4,1,1);
        panel.add(param[5],1,5,1,1);
        panel.add(param[6],1,6,1,1);

    }



}
