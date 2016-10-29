package cl.eos;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Migrator extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    protected double xOffset;

    protected double yOffset;

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("Migrar");
            Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
            final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("migrator.fxml"));
            final StackPane root = (StackPane) fxmlLoader.load();
            final Scene scene = new Scene(root);
            scene.getStylesheets().add(this.getClass().getResource("list.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (final IOException e) {
            e.printStackTrace();
        }

    }
}
