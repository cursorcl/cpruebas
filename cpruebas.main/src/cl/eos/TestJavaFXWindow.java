package cl.eos;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TestJavaFXWindow extends Application {
  
  @Override
  public void start(final Stage primaryStage) {
      Button btn = new Button();
      btn.setText("Open a New Window");
      btn.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
               
              Label secondLabel = new Label("Hello");
               
              StackPane secondaryLayout = new StackPane();
              secondaryLayout.getChildren().add(secondLabel);
               
              Scene secondScene = new Scene(secondaryLayout, 200, 100);

              Stage secondStage = new Stage();
              secondStage.setTitle("Second Stage");
              secondStage.setScene(secondScene);
               
              //Set position of second window, related to primary window.
              secondStage.setX(primaryStage.getX() + 250);
              secondStage.setY(primaryStage.getY() + 100);

              secondStage.show();
          }
      });
       
      StackPane root = new StackPane();
      root.getChildren().add(btn);
       
      Scene scene = new Scene(root, 300, 250);
       
      primaryStage.setTitle("java-buddy.blogspot.com");
      primaryStage.setScene(scene);
      primaryStage.show();
  }

  public static void main(String[] args) {
      launch(args);
  }
}