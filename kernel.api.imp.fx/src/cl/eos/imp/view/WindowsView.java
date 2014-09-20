package cl.eos.imp.view;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class WindowsView extends BorderPane {

  private Label windowTitle;

  public WindowsView() {
    windowTitle = new Label("Titulo de la ventana");
    setTop(windowTitle);
    
  }


  @FXML
  public void initialize() {

  }

  public void show(Node node) {
    setCenter(node);
    setVisible(true);
  }

  public void hide(Node node) {
    setVisible(false);
  }

  
}
