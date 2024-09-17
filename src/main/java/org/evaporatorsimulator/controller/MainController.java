package org.evaporatorsimulator.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



//aimport javafx.stage.Stage;
//import javafx.scene.layout.VBox;

public class MainController {

    @FXML
    private VBox rootPane;
    @FXML
    private VBox rightPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private HBox windowButtons;
    @FXML
    private Pane leftPane;

    private Stage stage;
    @FXML
    private HBox barApp;

    private double xOffset = 0;
    private double yOffset = 0;


    @FXML
    private void minimizeWindow() {
        if (stage != null) {
            stage.setIconified(true);
        }
    }

    @FXML
    private void closeWindow() {
        if (stage != null) {
            stage.close();
        }

    }
    @FXML
    private void handleMousePressed(MouseEvent event) {
        // Captura las coordenadas del mouse cuando se presiona
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    private void handleMouseDragged(MouseEvent event) {
        // Mueve la ventana según las coordenadas del mouse
        Stage stagebar = (Stage) barApp.getScene().getWindow();
        stagebar.setX(event.getScreenX() - xOffset);
        stagebar.setY(event.getScreenY() - yOffset);
    }


    @FXML
    private void initialize() {

        try {

            scrollPane.setPannable(true);

            barApp.setOnMousePressed(this::handleMousePressed);
            barApp.setOnMouseDragged(this::handleMouseDragged);
            if (rootPane == null) {
                System.err.println("rootPane es null en initialize()");
            } else {
                loadChartView();
                loadControlPanelView();

               loadPlantView();


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPlantView() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/evaporatorsimulator/view/plant.fxml"));
        Pane plantView = loader.load();
        leftPane.getChildren().add(plantView); // Verifica que rootPane no es null aquí
        //rootPane.setLeft(loader.load());
    }

    private void loadChartView() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/evaporatorsimulator/view/chart.fxml"));
        LineChart chartView = loader.load();
        rightPane.getChildren().add(chartView);
    }

    private void loadControlPanelView() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/evaporatorsimulator/view/controlPanel.fxml"));
        rightPane.getChildren().add(loader.load());
    }

    public void setStage(Stage primaryStage) {
        this.stage = primaryStage;

        // Asignar acción al evento setOnShown
        stage.setOnShown(event -> {
            // Stage ya está listo aquí
            this.stage = primaryStage;
        });
    }
}
