package cl.eos.cliente;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jfxtras.scene.layout.VBox;

public class Main2 extends Application {
  protected double xOffset;
  protected double yOffset;

  @Override
  public void start(Stage primaryStage) {

    
    
    StackPane rootPane = new StackPane();
    VBox mainPane = new VBox(80);

    BorderPane helpOverlayPane = new BorderPane();
    helpOverlayPane.setMouseTransparent(true);

    Canvas fullScreenOverlayCanvas = new Canvas();
    fullScreenOverlayCanvas.setMouseTransparent(true);

    VBox debugPane = new VBox();
    debugPane.setAlignment(Pos.BASELINE_RIGHT);
    AnchorPane debugOverlay = new AnchorPane();
    debugOverlay.setMouseTransparent(true);
    debugOverlay.getChildren().add(debugPane);
    AnchorPane.setBottomAnchor(debugPane, 80.0);
    AnchorPane.setRightAnchor(debugPane, 20.0);

    rootPane.getChildren().addAll(mainPane, fullScreenOverlayCanvas, debugOverlay, helpOverlayPane);
    
    Scene scene = new Scene(rootPane, 1200, 740);
    primaryStage.setScene(scene);
    primaryStage.show();

  }

  public static void main(String[] args) {
    launch(args);
  }

}
