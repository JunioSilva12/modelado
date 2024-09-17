package org.evaporatorsimulator.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class ChartController {

    @FXML
    private LineChart<String, Number> chart;

    @FXML
    private void initialize() {
        // Inicialización del gráfico
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Simulation Data");
        chart.getData().add(series);
    }
}