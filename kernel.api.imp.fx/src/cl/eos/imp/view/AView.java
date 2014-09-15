package cl.eos.imp.view;

import java.awt.Dimension;
import java.net.URL;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import cl.eos.interfaces.controller.IController;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.view.IView;

public abstract class AView implements IView {

  private String title;
  protected IController controller;
  protected Object parent;

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public void setController(IController controller) {
    this.controller = controller;
    controller.addView(this);
  }

  @Override
  public IController getController() {
    return this.controller;
  }

  public void addView(IView view) {
    // TODO Auto-generated method stub

  }

  @Override
  public void removeView(IView view) {
    // TODO Auto-generated method stub

  }

  @Override
  public List<IView> getViews() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void onChangeStatus(Object status) {
    // TODO Auto-generated method stub
  }

  @Override
  public String getName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Dimension getPreferredSize() {
    // TODO Auto-generated method stub
    return null;
  }

  public Object getParent() {
    return parent;
  }

  public void setParent(Object parent) {
    this.parent = parent;
  }

  @Override
  public void onSaving(IEntity entity) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onSaved(IEntity entity) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onDeleting(IEntity entity) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onDeleted(IEntity entity) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onSelected(IEntity entity) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onFound(IEntity entity) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onDataArrived(List<Object> list) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onError(String error) {
    // TODO Auto-generated method stub

  }

  public void show(Pane root, IView pane) {
    WindowManager.getInstance().show(root, pane.getParent(), true);
  }

  public IView show(Pane root, String fxml) {
    IView view = null;
    URL url = AView.class.getResource(fxml);
    FXMLLoader fxmlLoader = new FXMLLoader();
    try {
      Pane pane = (Pane) fxmlLoader.load(url.openStream());
      view = (IView) fxmlLoader.getController();
      view.setParent(pane);
      controller.addView(view);
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (view != null) {
      WindowManager.getInstance().show(root, view.getParent(), true);
    }
    return view;
  }
}
