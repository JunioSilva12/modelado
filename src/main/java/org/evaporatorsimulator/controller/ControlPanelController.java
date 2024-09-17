package org.evaporatorsimulator.controller;


import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ControlPanelController {

    @FXML
    private Button startButton;

    @FXML
    private Button stopButton;

    @FXML
    private void initialize() {
        startButton.setOnAction(event -> startSimulation());
        stopButton.setOnAction(event -> stopSimulation());
    }

    private void startSimulation() {
        // Implementar la lógica de inicio de simulación
    }

    private void stopSimulation() {
        // Implementar la lógica de detener simulación
    }
}