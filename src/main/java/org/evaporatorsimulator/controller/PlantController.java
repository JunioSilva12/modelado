package org.evaporatorsimulator.controller;


import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
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
import javafx.util.Duration;

public class PlantController {


    public ImageView V3 ;
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

    public Path AliFrio;
    public Path VapCaliente;
    public Path VacioL;
    public Path VapGen;
    public Path LinCon;
    public Path AliCal;


    private boolean isOpenV3 = false;
    private double waterLevel = 505; // Nivel del agua en la tubería
    private double emptyLevel = 505; // Nivel del agua en la tubería
    @FXML
    private Canvas gridCanvas;

    @FXML
    private Pane plantPane;
    @FXML

    private Path waterFlow; // El rectángulo que representa el líquido dentro de la tubería
    Timeline timeline = new Timeline();
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
        Tooltip linea6 = createCustomTooltip("linea de vapor vivo");


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
        Tooltip.install(AliFrio, linea1);
        Tooltip.install(AliCal, linea2);
        Tooltip.install(this.VacioL, linea3);
        Tooltip.install(this.LinCon, linea4);
        Tooltip.install(this.VapGen, linea5);
        Tooltip.install(this.VapCaliente, linea6);



        scale = new Scale(1, 1, 0, 0);
        plantPane.getTransforms().add(scale);
      plantPane.setOnScroll(this::handleZoom);

        plantPane.setOnMousePressed(this::handleMousePressed2);
        plantPane.setOnMouseDragged(this::handleMouseDragged2);
        drawGrid();
        // Inicialización de la vista de la planta







    }

    public void openV3(MouseEvent mouseEvent) {
   if(!isOpenV3) {
       V3.setImage(new Image(getClass().getResource("/org/evaporatorsimulator/view/images/valvula de bola-open.PNG").toExternalForm()));
       // Cambiar el color de la válvula a verde cuando se abra


       // Mostrar el rectángulo del líquido (el agua) y simular que llena la tubería
      // waterFlow.setVisible(true);

       // Mostrar el Path del líquido (el agua) en la tubería
       waterFlow.setVisible(true);

       // Timeline para manipular el strokeDashOffset y hacer que el trazo empiece desde arriba hacia abajo
       startWaterFlow();
   }else {
       V3.setImage(new Image(getClass().getResource("/org/evaporatorsimulator/view/images/valvula de bola-close.PNG").toExternalForm()));
       waterFlow.setVisible(true);
       startEmptyPipe();
   }
   this.isOpenV3=!isOpenV3;
    }


    private void startWaterFlow() {
        waterFlow.setVisible(true);
        waterFlow.setStroke(Color.BLUE);
        waterLevel = 505; // Reiniciar nivel de agua en la tubería


        waterFlow.setVisible(true);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.05), event -> {
            if (waterLevel < 665) {
                waterLevel += 1; // Incrementar nivel del agua

                updateWaterFlow();
            } else {
                stopWaterFlow();
            }
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void stopWaterFlow() {
        waterFlow.setVisible(true);
    }

    private void updateWaterFlow() {
        waterFlow.getElements().clear();
        waterFlow.getElements().add(new javafx.scene.shape.MoveTo(96, 505));
        waterFlow.getElements().add(new javafx.scene.shape.LineTo(96, waterLevel+10 ));
    }

    private void startEmptyPipe() {
        // waterFlow.setVisible(true);
        waterFlow.setStroke(Color.BLUE);
        emptyLevel = 505; // Reiniciar nivel de agua en la tubería


        waterFlow.setVisible(true);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.05), event -> {
            if (emptyLevel < 665) {
                emptyLevel += 1; // decrementa nivel del agua

                updateEmptyPipe();
            } else {
                stopEmptyPipe();
            }
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void stopEmptyPipe() {
        waterFlow.setVisible(true);
    }

    private void updateEmptyPipe() {
        waterFlow.getElements().clear();
        waterFlow.getElements().add(new javafx.scene.shape.MoveTo(96, emptyLevel+10));
        waterFlow.getElements().add(new javafx.scene.shape.LineTo(96, waterLevel ));
    }
}



