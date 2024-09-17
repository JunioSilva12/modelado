package org.evaporatorsimulator.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.transform.Scale;


public class PlantController {


    public ImageView tankAli;
    public ImageView i2;
    public ImageView i1;
    public ImageView tankCond;
    public ImageView tankProduct;
    public ImageView tankVacio;
    public ImageView medFlujoFir;
    public ImageView bombaB1;
    public ImageView bombaB2;
    public ImageView evap;
    public Path Ali3;
    public Path Ali2;
    public Path Ali1;

    @FXML
    private Canvas gridCanvas;

    @FXML
    private Pane plantPane;
    @FXML


    private final double MIN_SCALE = 1.0; // Escala mínima permitida
    private final double ZOOM_FACTOR = 1.1; // Factor de zoom

    private double initialX;
    private double initialY;
    private double initialTranslateX;
    private double initialTranslateY;
    private Scale scale ;

    private void handleMousePressed2(MouseEvent event) {
        initialX = event.getSceneX();
        initialY = event.getSceneY();
        initialTranslateX = plantPane.getTranslateX();
        initialTranslateY = plantPane.getTranslateY();
    }

    @FXML
    private void handleMouseDragged2(MouseEvent event) {
        double offsetX = event.getSceneX() - initialX;
        double offsetY = event.getSceneY() - initialY;
        plantPane.setTranslateX(initialTranslateX + offsetX);
        plantPane.setTranslateY(initialTranslateY + offsetY);
    }

    private void handleZoom(ScrollEvent event) {
        double delta = event.getDeltaY();
        double zoomFactor = ZOOM_FACTOR;
        if (delta < 0) {
            zoomFactor = 1 / ZOOM_FACTOR;
        }

        // Calcula el punto de zoom en el espacio del contenido
        double mouseX = event.getX();
        double mouseY = event.getY();

        double contentWidth = plantPane.getWidth();
        double contentHeight = plantPane.getHeight();

        double contentMouseX = (mouseX - plantPane.getLayoutX()) / scale.getX();
        double contentMouseY = (mouseY - plantPane.getLayoutY()) / scale.getY();

        double newScaleX = scale.getX() * zoomFactor;
        double newScaleY = scale.getY() * zoomFactor;

        // Asegúrate de que la escala no sea menor que MIN_SCALE
        if (newScaleX >= MIN_SCALE && newScaleY >= MIN_SCALE) {
            scale.setX(newScaleX);
            scale.setY(newScaleY);

            // Ajustar la posición del contenido para centrar el zoom en el mouse
            double newContentMouseX = contentMouseX * zoomFactor;
            double newContentMouseY = contentMouseY * zoomFactor;

            double offsetX = newContentMouseX - contentMouseX;
            double offsetY = newContentMouseY - contentMouseY;

            plantPane.setTranslateX(plantPane.getTranslateX() - offsetX);
            plantPane.setTranslateY(plantPane.getTranslateY() - offsetY);
        }
        event.consume();


    }
    private void drawGrid() {
        GraphicsContext gc = gridCanvas.getGraphicsContext2D();

        double width = gridCanvas.getWidth();
        double height = gridCanvas.getHeight();
        double gridSize = 20; // Tamaño de cada cuadrícula

        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(0.2);

        // Dibujar líneas verticales
        for (double x = 0; x < width; x += gridSize) {
            gc.strokeLine(x, 0, x, height);
        }

        // Dibujar líneas horizontales
        for (double y = 0; y < height; y += gridSize) {
            gc.strokeLine(0, y, width, y);
        }
    }
    private Tooltip createCustomTooltip(String text) {
        // Crear un Label con control de tamaño
        Label label = new Label(text);
        label.setMaxWidth(300);
        label.setMaxHeight(20);
        label.setWrapText(true);

        // Crear un Tooltip que contiene el Label
        Tooltip tooltip = new Tooltip();
        tooltip.setGraphic(new VBox(label)); // Usar VBox para asegurarse que el Label se muestra correctamente
        tooltip.setMaxWidth(400);  // Limitar el ancho del Tooltip
        tooltip.setWrapText(true); // Habilitar el ajuste de línea
        return tooltip;
    }
    @FXML
    private ImageView bombaVacioImg;
    @FXML
    private void initialize() {
        Tooltip tooltip1 = createCustomTooltip("Bomba de alto vacío ");
        Tooltip tooltip2 = createCustomTooltip(" bomba centrífuga");
        Tooltip tooltip3 = createCustomTooltip("Evaporador");
        Tooltip tooltip4 = createCustomTooltip("Tanque de alimento");
        Tooltip tooltip5 = createCustomTooltip("Tanque de condensado");
        Tooltip tooltip6 = createCustomTooltip("Tanques de productos");
        Tooltip tooltip7 = createCustomTooltip("Tanque trampa de vacío.");
        Tooltip tooltip8 = createCustomTooltip("Condensador 1");
        Tooltip tooltip9 = createCustomTooltip("Condensador 2");
        Tooltip tooltip10 = createCustomTooltip("Medidor de flujo Fillrite ");
        Tooltip linea1 = createCustomTooltip("alimento frio");
        Tooltip linea2 = createCustomTooltip("alimento caliente");
        Tooltip linea3 = createCustomTooltip("linea de vacio");
        Tooltip linea4 = createCustomTooltip("linea de condesado");
        Tooltip linea5 = createCustomTooltip("linea de vapor generado");


        Tooltip.install(bombaVacioImg, tooltip1);
        Tooltip.install(i1, tooltip8);
        Tooltip.install(i2, tooltip9);
        Tooltip.install(bombaB1, tooltip2);
        Tooltip.install(bombaB2, tooltip2);
        Tooltip.install(evap, tooltip3);
        Tooltip.install(medFlujoFir, tooltip10);
        Tooltip.install(tankVacio, tooltip7);
        Tooltip.install(tankCond, tooltip5);
        Tooltip.install(tankProduct, tooltip6);
        Tooltip.install(tankAli, tooltip4);
        Tooltip.install(Ali1, linea1);
        Tooltip.install(Ali2, linea1);
        Tooltip.install(Ali3, linea1);


        scale = new Scale(1, 1, 0, 0);
        plantPane.getTransforms().add(scale);
      plantPane.setOnScroll(this::handleZoom);

        plantPane.setOnMousePressed(this::handleMousePressed2);
        plantPane.setOnMouseDragged(this::handleMouseDragged2);
        drawGrid();
        // Inicialización de la vista de la planta




    }
 /*   @FXML
    private void onStartButtonClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Button Clicked");
        alert.setHeaderText(null);
        alert.setContentText("Start button was clicked!");
        alert.showAndWait();
    }*/

}