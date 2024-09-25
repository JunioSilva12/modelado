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
   // private double waterLevel = 505; // Nivel del agua en la tubería
    double level;

    @FXML
    private Canvas gridCanvas;

    @FXML
    private Pane plantPane;
    @FXML

    private Path waterFlow; // El rectángulo que representa el líquido dentro de la tubería
    @FXML
    private Path waterFlow2;
    Timeline timeline ;
    private final double MIN_SCALE = 1.0; // Escala mínima permitida
    private final double ZOOM_FACTOR = 1.1; // Factor de zoom
    Timeline emptytimeline ;
    private double[][] animationStepsAli1 = {
            {96, 505,96, 665,0.02 ,1,1} ,   // Animación 1: {x, y, incrementoY, límiteY,time,isVertical, sentido}
            {100, 670, 200, 670,0.02 ,0,1},//fragmentacion de tubería
            {200, 670, 413, 670,0.02 ,0,1},// Animación 2: {x, y, incrementoY, límiteY,time,isVertical, sentido}
            {413, 670, 413, 620,0.02 ,1,0},// Animación 3: {x, y, incrementoY, límiteY,time,isVertical, sentido}
            {413, 620, 570, 620,0.02 ,0,1},// Animación 4: {x, y, incrementoY, límiteY,time,isVertical, sentido}
            {570, 620, 570, 580,0.02 ,1,0},// Animación 5: {x, y, incrementoY, límiteY,time,isVertical, sentido}
           {570, 580, 720, 580,0.02 ,0,1},// Animación 6 {x, y, incrementoY, límiteY,time,isVertical, sentido}
            {720, 580, 720, 255,0.02 ,1,0},// Animación 7: {x, y, incrementoY, límiteY,time,isVertical, sentido}
            {720, 255, 610, 255,0.02 ,0,0},// Animación 8: {x, y, incrementoX, límiteX,time}
            // Puedes agregar más configuraciones si cambian las coordenadas en cada animación
    };
    private double[][] animationStepsAli2 = {
            {200, 670,200, 340,0.02 ,1,0} ,   // Animación 1: {x, y, incrementoY, límiteY,time,isVertical, sentido}
            {200, 340, 124, 340,0.02 ,0,0},// Animación 2: {x, y, incrementoY, límiteY,time,isVertical, sentido}
            {124, 340, 124, 400,0.02 ,1,1},// Animación 3: {x, y, incrementoY, límiteY,time,isVertical, sentido}

    };

   // public int currentAnimationIndex = 0;
    private int totalAnimations = 9;       // Total de animaciones que queremos hacer
    private double emptyLevel = 505;

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
    // Crear un SequentialTransition para encadenar las animaciones

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
        Tooltip.install(this.waterFlow, linea1);
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

    private void runNextAnimation(int currentAniIndex, int totalAnimations , double[][] animationStepsAli , int aniId, Path flow, Boolean llenando) {
        final int[] currentAnimationIndex = {currentAniIndex};
        if (currentAnimationIndex[0] < totalAnimations) {
            // Resetea el nivel de agua para la nueva animación
            double[] currentAnimation = animationStepsAli[currentAnimationIndex[0]];
            double startX = currentAnimation[0];
            double startY = currentAnimation[1];
            double endX = currentAnimation[2];
            double endY = currentAnimation[3];
            //  double timmer = currentAnimation[4];
            boolean isVertical = currentAnimation[5] == 1;
            double speed = currentAnimation[4];
            double direction = currentAnimation[6];

            System.out.println("iniciando animacion "+(currentAnimationIndex[0] +1));
            System.out.println("speed animacion "+speed);
            System.out.println("startY animacion "+startY);
            System.out.println("startX animacion "+startX);
            System.out.println("increment animacion "+endX);
            System.out.println("limit animacion "+endY);
            final double[] waterLevel = {(isVertical) ? startY : startX};
            if(currentAnimationIndex[0] == 2 && aniId== 1){
                System.out.println("fragmentacion1");
                waterFlow2.setVisible(true);
                runNextAnimation(0,3,animationStepsAli2,2,this.waterFlow2, llenando);
            }
            // Crea un Timeline para la animación actual
            Timeline timeline = new Timeline();
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.05), event -> {

                if (isVertical) {
                    if(direction==1){
                        if(llenando){
                        if (waterLevel[0] < endY) {
                            waterLevel[0] += 1;  // Incrementa el nivel de agua
                            updateWaterFlow(startX, startY, endX, waterLevel[0], isVertical,currentAnimationIndex[0],flow);
                        } else {
                            System.out.println("Cerró animación to bottom" + (currentAnimationIndex[0] + 1));
                            currentAnimationIndex[0]++;  // Pasa a la siguiente animación
                            timeline.stop();  // Detén el timeline actual
                            runNextAnimation(currentAnimationIndex[0],totalAnimations,animationStepsAli,aniId,flow, llenando);  // Ejecuta la siguiente animación
                        }
                        }else{
                            if (waterLevel[0] < endY) {
                                waterLevel[0] += 1;  // Incrementa el nivel de agua
                                updateWaterFlow(startX, waterLevel[0], endX, endY, isVertical,currentAnimationIndex[0],flow);
                            } else {
                                System.out.println("Cerró animación to bottom" + (currentAnimationIndex[0] + 1));
                                currentAnimationIndex[0]++;  // Pasa a la siguiente animación
                                timeline.stop();  // Detén el timeline actual
                                runNextAnimation(currentAnimationIndex[0],totalAnimations,animationStepsAli,aniId,flow, llenando);  // Ejecuta la siguiente animación
                            }

                        }
                    }else {
                        if(llenando) {
                            if (waterLevel[0] > endY) {
                                waterLevel[0] -= 1;  // Incrementa el nivel de agua
                                updateWaterFlow(startX, startY, endX, waterLevel[0], isVertical, currentAniIndex, flow);
                            } else {
                                System.out.println("Cerró animación to up" + (currentAnimationIndex[0] + 1));
                                currentAnimationIndex[0]++;  // Pasa a la siguiente animación
                                timeline.stop();  // Detén el timeline actual
                                runNextAnimation(currentAnimationIndex[0], totalAnimations, animationStepsAli, aniId, flow, llenando);  // Ejecuta la siguiente animación
                            }
                        }else {
                            if (waterLevel[0] > endY) {
                                waterLevel[0] -= 1;  // Incrementa el nivel de agua
                                updateWaterFlow(startX, waterLevel[0], endX, endY, isVertical, currentAniIndex, flow);
                            } else {
                                System.out.println("Cerró animación to up" + (currentAnimationIndex[0] + 1));
                                currentAnimationIndex[0]++;  // Pasa a la siguiente animación
                                timeline.stop();  // Detén el timeline actual
                                runNextAnimation(currentAnimationIndex[0], totalAnimations, animationStepsAli, aniId, flow, llenando);  // Ejecuta la siguiente animación
                            }
                        }
                    }

                } else {
                    if(direction==1){
                        if(llenando) {
                            if (waterLevel[0] < endX) {
                                waterLevel[0] += 1;  // Incrementa el nivel de agua
                                updateWaterFlow(startX, startY, waterLevel[0], endY, isVertical, currentAniIndex, flow);
                            } else {
                                System.out.println("Cerró animación to Right" + (currentAnimationIndex[0] + 1));
                                currentAnimationIndex[0]++;  // Pasa a la siguiente animación
                                timeline.stop();  // Detén el timeline actual
                                runNextAnimation(currentAnimationIndex[0], totalAnimations, animationStepsAli, aniId, flow, llenando);  // Ejecuta la siguiente animación
                            }
                        }else {
                            if (waterLevel[0] < endX) {
                                waterLevel[0] += 1;  // Incrementa el nivel de agua
                                updateWaterFlow(waterLevel[0], startY,endX, endY, isVertical, currentAniIndex, flow);
                            } else {
                                System.out.println("Cerró animación to Right" + (currentAnimationIndex[0] + 1));
                                currentAnimationIndex[0]++;  // Pasa a la siguiente animación
                                timeline.stop();  // Detén el timeline actual
                                runNextAnimation(currentAnimationIndex[0], totalAnimations, animationStepsAli, aniId, flow, llenando);  // Ejecuta la siguiente animación
                            }
                        }
                    }else {
                        if(llenando) {
                            if (waterLevel[0] > endX) {
                                waterLevel[0] -= 1;  // Incrementa el nivel de agua
                                updateWaterFlow(startX, startY, waterLevel[0], endY, isVertical, currentAniIndex, flow);
                            } else {
                                System.out.println("Cerró animación to Left" + (currentAnimationIndex[0] + 1));
                                currentAnimationIndex[0]++;  // Pasa a la siguiente animación
                                timeline.stop();  // Detén el timeline actual
                                runNextAnimation(currentAnimationIndex[0], totalAnimations, animationStepsAli, aniId, flow, llenando);  // Ejecuta la siguiente animación
                            }
                        }else {
                            if (waterLevel[0] > endX) {
                                waterLevel[0] -= 1;  // Incrementa el nivel de agua
                                updateWaterFlow( waterLevel[0], startY, endX, endY, isVertical, currentAniIndex, flow);
                            } else {
                                System.out.println("Cerró animación to Left" + (currentAnimationIndex[0] + 1));
                                currentAnimationIndex[0]++;  // Pasa a la siguiente animación
                                timeline.stop();  // Detén el timeline actual
                                runNextAnimation(currentAnimationIndex[0], totalAnimations, animationStepsAli, aniId, flow, llenando);  // Ejecuta la siguiente animación
                            }
                        }
                    }

                }

            }));

            timeline.setCycleCount(Timeline.INDEFINITE);  // Se repetirá hasta que termine
            timeline.play();  // Inicia la animación
        } else {
            System.out.println("Todas las animaciones terminaron.");
            if(!llenando){
                stopEmptyPipe();
            }
        }
    }

    private KeyFrame createKeyFrame(int animationIndex) {
        if (animationIndex >= animationStepsAli1.length ) {
            System.out.println("Todas las animaciones completadas.");
            return null;  // Se puede retornar un KeyFrame nulo si ya se completaron todas las animaciones
        }

        double[] currentAnimation = animationStepsAli1[animationIndex];
        double startX = currentAnimation[0];
        double startY = currentAnimation[1];
        double endX = currentAnimation[2];
        double endY = currentAnimation[3];
      //  double timmer = currentAnimation[4];
        boolean isVertical = currentAnimation[5] == 1;
        double speed = currentAnimation[4];
       // System.out.println("iniciando animacion "+(currentAnimationIndex[0]+1));
        System.out.println("speed animacion "+speed);
        System.out.println("startY animacion "+startY);
        System.out.println("startX animacion "+startX);
        System.out.println("increment animacion "+endX);
        System.out.println("limit animacion "+endY);
        final double[] waterLevel = {startY};
        return new KeyFrame(Duration.seconds(speed), event -> {
            if (waterLevel[0] < endY) {
                waterLevel[0] += 1;
                updateWaterFlow(startX, startY,endX, waterLevel[0], isVertical,0,this.waterFlow);
            } else {
                System.out.println("cerro animacion");
                //event.consume();
                //currentAnimationIndex++;  // Pasar a la siguiente animación

            }
        });
    }


            private void startNextAnimation() {
//        if (currentAnimationIndex >= totalAnimations) {
//            System.out.println("Secuencia de animaciones completada.");
//            timeline.stop();  // Detener la animación después de completar todas
//            stopWaterFlow();
//            return;
//        }


                KeyFrame[] keyFramesAli = new KeyFrame[animationStepsAli1.length] ;
                for (int i=0 ;i<animationStepsAli1.length;i++) {
                    keyFramesAli[i]=createKeyFrame(i);

                }
                // sequentialTransition = new SequentialTransition();

                //Duration totalDuration = Duration.ZERO;
//                for (KeyFrame keyFram : keyFramesAli) {
//                    Timeline scaleTimeline = new Timeline();
//                    scaleTimeline.getKeyFrames().add(keyFram);
//                    sequentialTransition.getChildren().add(scaleTimeline);
//
//
//                }

                // Iniciar el Timeline
               // timeline.play();
                // Iniciar la animación
               // sequentialTransition.play();
                Boolean llenando=true;
                runNextAnimation(0,  this.totalAnimations , this.animationStepsAli1,1,this.waterFlow,llenando);  // Iniciar la primera animación
               // runNextAnimation(0,  3 , this.animationStepsAli2);

            }

    private void startWaterFlow() {
        waterFlow.setVisible(true);
        waterFlow.setStroke(Color.BLUE);


       // waterLevel = 505; // Reiniciar nivel de agua en la tubería
        // Validación: asegurarnos de que el array de velocidades esté correctamente inicializado


        waterFlow.getElements().clear(); // Limpia el Path antes de comenzar
         double waterLevel = 505;
        waterLevel = animationStepsAli1[0][1]; // Comienza con el nivel inicial de la primera animación
       // currentAnimationIndex = 0; // Reinicia el índice de la animación

        // Inicializar el Timeline
        //this.timeline = new Timeline();
        //timeline.setCycleCount(Timeline.INDEFINITE);  // Repetición indefinida controlada por lógica

        startNextAnimation();


    }

    private void stopWaterFlow() {
        if (timeline != null) {
            timeline.stop();
        }
        waterFlow.setVisible(true);
    }

    private void updateWaterFlow(double iniX , double iniY ,double endX,double endY ,Boolean isVertical ,int elementID,  Path flow) {
        System.out.print(" ele:"+elementID+"__");
        if (flow.getElements().size() > elementID*2) {
            // Ya hay un 'MoveTo' y 'LineTo', los actualizamos
            javafx.scene.shape.MoveTo moveTo = (javafx.scene.shape.MoveTo) flow.getElements().get(elementID*2);
            javafx.scene.shape.LineTo lineTo = (javafx.scene.shape.LineTo) flow.getElements().get(elementID*2+1);

            if (isVertical) {
                System.out.println("Actualizando tubo vertical: " + iniX + ".." + iniY + ".." + endY);
                moveTo.setX(iniX);
                moveTo.setY(iniY);
                lineTo.setX(iniX);
                lineTo.setY(endY);
            } else {
                System.out.println("Actualizando tubo horizontal: " + iniX + ".." + iniY + ".." + endX);
                moveTo.setX(iniX);
                moveTo.setY(iniY);
                lineTo.setX(endX);
                lineTo.setY(iniY);
            }
        } else {
            // Si no hay suficientes elementos, los creamos
            System.out.println("Creando nuevo tubo");

            javafx.scene.shape.MoveTo moveTo;
            javafx.scene.shape.LineTo lineTo;

            if (isVertical) {
                moveTo = new javafx.scene.shape.MoveTo(iniX, iniY);
                lineTo = new javafx.scene.shape.LineTo(iniX, endY);
            } else {
                moveTo = new javafx.scene.shape.MoveTo(iniX, iniY);
                lineTo = new javafx.scene.shape.LineTo(endX, iniY);
            }


            // Agrega los elementos al contenedor
            flow.getElements().add(moveTo);
            flow.getElements().add(lineTo);
        }




    }

    private void startEmptyPipe() {
         waterFlow.setVisible(true);
        waterFlow.setStroke(Color.BLUE);
        emptyLevel = 505; // Reiniciar nivel de agua en la tubería
        emptytimeline = new Timeline();
       level = emptyLevel;
        waterFlow.setVisible(true);
        //mientras va llenando va reescribiendo de donde empieza

        Boolean llenando=false;
        runNextAnimation(0,  this.totalAnimations , this.animationStepsAli1,1,this.waterFlow,llenando);  // Iniciar la primera animación


    }

    private void stopEmptyPipe() {
       // waterFlow.setVisible(false);
        waterFlow2.setVisible(false);
     //   timeline.stop();
    }

    private void updateEmptyPipe(double iniX , double iniY , Boolean isVertical, double level) {
        if(isVertical) {
            System.out.println("vaciando tubo vertical: "+iniX+".."+iniY+".."+level);
           // waterFlow.getElements().clear();

            waterFlow.getElements().add(new javafx.scene.shape.MoveTo(iniX, level ));
            waterFlow.getElements().add(new javafx.scene.shape.LineTo(iniX, iniY));
        }else{
            System.out.println("vaciando tubo horizontal: "+iniX+".."+iniY+".."+level);
            //waterFlow.getElements().clear();
            waterFlow.getElements().add(new javafx.scene.shape.MoveTo(iniX, iniY));
            waterFlow.getElements().add(new javafx.scene.shape.LineTo(level , iniY));
        }
    }
}



