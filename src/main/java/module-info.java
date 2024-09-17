module org.evaporatorsimulator.evaporatorsimulator {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.swing;
    requires javafx.media;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens org.evaporatorsimulator to javafx.fxml;
    opens org.evaporatorsimulator.view to javafx.fxml; // Abre el paquete de las vistas a javafx.fxml
    opens org.evaporatorsimulator.controller to javafx.fxml; // Abre el paquete del controlador a javafx.fxml


    exports org.evaporatorsimulator;
}