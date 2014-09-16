package cl.eos.imp.view;

import java.util.List;

import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.view.IFormView;

public abstract class AFormView extends AView implements IFormView {

	@Override
	public void save(IEntity otObject) {
		if (controller != null && validate()) {
			controller.save(otObject);
		}
	}

	@Override
	public void delete(IEntity otObject) {
		if (controller != null && validate()) {
			controller.save(otObject);
		}
	}

	@Override
	public void select(IEntity otObject) {
		if (controller != null) {
			controller.select(otObject);
		}
	}

	@Override
	public boolean validate() {
		return true;
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
	
	

}
