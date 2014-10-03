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

	public Object getPanel() {
		return parent;
	}

	public void setPanel(Object parent) {
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

	public void show(IView pane, boolean addBreadcrumb) {
		if (addBreadcrumb) {
			WindowManager.getInstance().add(pane);
		} else {
			WindowManager.getInstance().show(pane);
		}
	}

	public void show(IView pane) {
		show(pane, false);
	}

	public IView show(String fxml) {
		return show(fxml, false);
	}

	public IView show(String fxml, boolean addBreadcrumb) {
		IView view = null;
		URL url = AView.class.getResource(fxml);
		FXMLLoader fxmlLoader = new FXMLLoader();
		try {
			Pane pane = (Pane) fxmlLoader.load(url.openStream());
			view = (IView) fxmlLoader.getController();
			view.setPanel(pane);
			controller.addView(view);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (view != null) {
			if (addBreadcrumb) {
				WindowManager.getInstance().add(view);
			} else {
				WindowManager.getInstance().show(view);
			}
		}
		return view;
	}

}
