package cl.eos.imp.view;

import java.awt.Dimension;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import cl.eos.interfaces.controller.IController;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.view.IView;

public abstract class AView implements IView {

	static final Logger LOG = Logger.getLogger(AView.class);
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

	}

	@Override
	public void removeView(IView view) {

	}

	@Override
	public List<IView> getViews() {
		return null;
	}

	@Override
	public void onChangeStatus(Object status) {
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public Dimension getPreferredSize() {
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

	}

	@Override
	public void onSaved(IEntity entity) {

	}

	@Override
	public void onDeleting(IEntity entity) {

	}

	@Override
	public void onDeleted(IEntity entity) {

	}

	@Override
	public void onSelected(IEntity entity) {

	}

	@Override
	public void onFound(IEntity entity) {

	}

	@Override
	public void onDataArrived(List<Object> list) {

	}

	@Override
	public void onError(String error) {

	}
	
	public void showOver(IView pane)
	{
	    WindowManager.getInstance().showOver(pane);
	}
	
	   public IView showOver(String fxml) {
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
	            LOG.error(e);
	        }
	        if (view != null) {
	            WindowManager.getInstance().showOver(view);
	        }
	        return view;
	    }
	
	public void show(IView pane) {
		WindowManager.getInstance().show(pane);
	}

	public IView show(String fxml) {
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
			LOG.error(e);
		}
		if (view != null) {
			WindowManager.getInstance().show(view);
		}
		return view;
	}

}
