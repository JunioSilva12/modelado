package org.evaporatorsimulator;




import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.evaporatorsimulator.controller.MainController;

public class App extends Application {




    @Override
    public void start(Stage primaryStage) {
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.initStyle(StageStyle.UNDECORATED);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/evaporatorsimulator/view/main.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/org/evaporatorsimulator/view/styles.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setTitle("Evaporator Simulator");

            MainController controller = loader.getController();
            controller.setStage(primaryStage);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}