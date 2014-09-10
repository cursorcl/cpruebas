package cl.eos.imp.controller;

import java.util.ArrayList;
import java.util.List;

import cl.eos.interfaces.controller.IController;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.entity.IPersistenceListener;
import cl.eos.interfaces.model.IModel;
import cl.eos.interfaces.view.IView;

public abstract class AController implements IController, IPersistenceListener {

	protected List<IView> views;
	protected IEntity selectedEntity;

	protected IModel model;

	public AController() {
		initialize();
	}

	@Override
	public abstract void initialize();

	@Override
	public void save(IEntity entity) {
		if (model != null) {
			if (selectedEntity != null && entity.equals(selectedEntity)
					|| entity.getId() == null) {
				model.update(entity);
			} else {
				model.save(entity);
			}
			notifySaved(entity);
		}
	}

	@Override
	public void delete(IEntity entity) {
		if (model != null) {
			model.delete(entity);
			notifyDeleted(entity);
		}
	}

	@Override
	public void delete(List<? extends IEntity> entity) {
		if (model != null) {
			model.delete((IEntity)entity);
			for (IEntity iEntity : entity) {
				notifyDeleted(iEntity);
			}
		}
	}

	@Override
	public void select(IEntity otObject) {
		if (model != null) {
			selectedEntity = model.get(otObject);
		}
	}

	@Override
	public void addView(IView view) {
		if (views == null) {
			views = new ArrayList<IView>();
		}
		if (!views.contains(view)) {
			views.add(view);
			if (view.getController() == null || view.getController() != view) {
				view.setController(this);
			}
		}
	}

	@Override
	public void removeView(IView view) {
		if (views != null) {
			views.remove(view);
		}

	}

	@Override
	public List<IView> getViews() {
		return views;
	}

	@Override
	public void add(IEntity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyFound(IEntity entity) {
		if (views != null) {
			for (IView view : views) {
				view.onFound(entity);
			}
		}
	}

	@Override
	public void notifySaved(IEntity entity) {
		if (views != null) {
			for (IView view : views) {
				view.onSaved(entity);
			}
		}
	}

	@Override
	public void notifyDeleted(IEntity entity) {
		if (views != null) {
			for (IView view : views) {
				view.onDeleted(entity);
			}
		}
	}

	@Override
	public void notifyChangeStatus(Object status) {
		if (views != null) {
			for (IView view : views) {
				view.onChangeStatus(status);
			}
		}
	}

	@Override
	public IModel getModel() {
		return model;
	}

	@Override
	public void onFindAllFinished(List<IEntity> list) {
		if (views != null) {
			for (IView view : views) {
				view.onDataArrived(list);
			}
		}
	}

}
