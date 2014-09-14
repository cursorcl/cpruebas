package cl.eos.imp.view;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import cl.eos.interfaces.view.IWindowManager;

public class WindowManager implements IWindowManager {

  private Pane root;

  private static WindowManager instance = null;

  private WindowManager() {

  }

  public static WindowManager getInstance() {
    if (instance == null) {
      instance = new WindowManager();
    }
    return instance;
  }

  @Override
  public void show(Object window) {
    show(window, false);
  }

  @Override
  public void hide(Object window) {
    if (window instanceof Node && root != null) {
      root.getChildren().remove((Node) window);
    }

  }

  @Override
  public void hideAll() {
    for (Node node : root.getChildren()) {
      root.getChildren().remove(node);
    }
  }

  @Override
  public void show(Object window, boolean closeOthers) {
    if (window instanceof Node && root != null) {
      if (closeOthers) {
        root.getChildren().setAll((Node) window);
      } else {
        root.getChildren().add((Node) window);
      }
    }
  }

  @Override
  public void show(Object parent, Object window) {
    if(parent != null && window != null && parent instanceof Pane && window instanceof Node)
    {
      ((Pane)parent).getChildren().add((Node)window);
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
    } else {
      throw new Exception("No corresponde a un elemento válido como raíz");
    }
  }

  @Override
  public void show(Object parent, Object window, boolean closeOthers) {
    if (window instanceof Node && root != null) {
      if (closeOthers) {
        root.getChildren().setAll((Node) window);
      } else {
        root.getChildren().add((Node) window);
      }
    }
  }

}
