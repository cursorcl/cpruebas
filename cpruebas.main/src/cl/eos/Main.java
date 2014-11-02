package cl.eos;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
  protected double xOffset;
  protected double yOffset;

  @Override
  public void start(Stage primaryStage) {
    try {
      setUserAgentStylesheet(STYLESHEET_MODENA);
      try {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
        StackPane root = (StackPane) fxmlLoader.load();
        MainController controller = (MainController) fxmlLoader.getController();
        controller.setStage(primaryStage);


        root.setOnMousePressed(new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
          }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
          }
        });


        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("ensemble2.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/cl/eos/images/logo32.png")));
        // primaryStage.setFullScreen(true);
        primaryStage.show();
      } catch (IOException e) {
        e.printStackTrace();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
