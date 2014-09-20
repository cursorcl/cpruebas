package cl.eos.imp.view;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;

import cl.eos.interfaces.controller.IController;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.view.IView;

public  abstract class AView implements IView {
	private String title;
	protected IController controller;
	protected JPanel pane;

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

	@Override
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

	@Override
	public Object getPanel() {
		return pane;
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

	
}
