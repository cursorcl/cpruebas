package cl.eos.imp.view;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import cl.eos.interfaces.view.IView;
import cl.eos.interfaces.view.IWindowManager;

public class WindowManager implements IWindowManager {

  private Pane root;
  private Stage mainStage;
  private Group group;

  private static WindowManager instance = null;
  private WindowsView window = new WindowsView();

  private Stage stage = new Stage();

  private WindowManager() {
    // secondStage.initStyle(StageStyle.UNDECORATED);
    // secondStage.initStyle(StageStyle.TRANSPARENT);


  }

  public static WindowManager getInstance() {
    if (instance == null) {
      instance = new WindowManager();
    }
    return instance;
  }

  @Override
  public void show(IView window) {
    if (group != null) {
//      Window w = new Window(window.getTitle());
      TitledPane w = new TitledPane();
//      w.getLeftIcons().add(new CloseIcon(w));
//      w.getRightIcons().add(new MinimizeIcon(w));

//      w.getContentPane().getChildren().add((Parent) window.getPanel());
      w.setText(window.getTitle());
      w.setContent((Parent) window.getPanel());
//      w.getChildren().add((Parent) window.getPanel());
      group.getChildren().add(w);
    }
  }

  @Override
  public void hide(IView window) {
    if (window instanceof Node && root != null) {
    }

  }

  @Override
  public void hideAll() {
    for (Node node : root.getChildren()) {
      root.getChildren().remove(node);
    }
  }

  @Override
  public Object getRoot() {
    return root;
  }

  @Override
  public void setRoot(Object root) throws Exception {
    if (root instanceof Pane) {
      this.root = (Pane) root;
      window.setVisible(false);
      this.root.getChildren().setAll(window);
    } else if (root instanceof Stage) {
      mainStage = (Stage) root;
      stage.initOwner(mainStage);
      stage.initModality(Modality.NONE);
      //stage.centerOnScreen();
      stage.initStyle(StageStyle.UTILITY);
    } else if (root instanceof Group) {
      this.group = (Group) root;
    }
  }
}
