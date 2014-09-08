package cl.eos.imp.view;

import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.view.IFormView;

public abstract class AFormView extends AView implements IFormView {

	private IEntity selectedEntity = null;

	@Override
	public void save(IEntity otObject) {
		if (controller != null && validate()) {
			controller.save(otObject);
			selectedEntity = null;
		}
	}

	@Override
	public void delete(IEntity otObject) {
		if (controller != null) {
			controller.delete(otObject);
			selectedEntity = null;
		}
	}

	@Override
	public void select(IEntity otObject) {
		if (controller != null) {
			controller.select(otObject);
			selectedEntity = otObject;
		}
	}

	@Override
	public void onSelected(IEntity entity) {
		if (selectedEntity != null && entity != null
				&& !entity.equals(selectedEntity)) {
			selectedEntity = entity;
		}
	}

	@Override
	public boolean validate() {
		return true;
	}

	/**
	 * Obtiene la entidad seleccionada.
	 * 
	 * @return La entidad que se encuentra seleccionada en el momento.
	 */
	public IEntity getSelectedEntity() {
		return selectedEntity;
	}

	/**
	 * Establece la entidad seleccionada.
	 * 
	 * @param selectedEntity
	 *            La entidad que se selecciona.
	 */
	public void setSelectedEntity(IEntity selectedEntity) {
		this.selectedEntity = selectedEntity;
	}

}
