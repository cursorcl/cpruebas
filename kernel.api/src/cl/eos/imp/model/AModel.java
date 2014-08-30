package cl.eos.imp.model;

import java.util.ArrayList;
import java.util.List;

import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.model.IModel;

public abstract class AModel implements IModel {

	protected List<IEntity> entities = new ArrayList<IEntity>();

	@Override
	public List<IEntity> getAll() {
		return entities;
	}

	@Override
	public IEntity get(Long id) {
		IEntity result = null;
		if (entities != null) {
			for (IEntity entity : entities) {
				if (entity.getId().equals(id)) {
					result = entity;
					break;
				}
			}
		}
		return result;
	}

	@Override
	public IEntity get(IEntity entity) {
		IEntity result = null;
		if (entities != null) {
			int index = entities.indexOf(entity);
			if (index != -1) {
				result = entities.get(index);
			}
		}
		return result;
	}

}
