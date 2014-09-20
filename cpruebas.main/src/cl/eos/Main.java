package cl.eos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
  @Override
  public void start(Stage primaryStage) {
    try {
       StackPane root = (StackPane) FXMLLoader.load(getClass().getResource("Main.fxml"));
       Scene scene = new Scene(root, 1024, 768);
       scene.getStylesheets().add(getClass().getResource("ensemble2.css").toExternalForm());
       primaryStage.setScene(scene);
       //primaryStage.setFullScreen(true);
       primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
