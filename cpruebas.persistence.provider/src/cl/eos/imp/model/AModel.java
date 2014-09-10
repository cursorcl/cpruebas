package cl.eos.imp.model;

import java.util.ArrayList;
import java.util.List;

import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.entity.IPersistenceListener;
import cl.eos.interfaces.model.IModel;
import cl.eos.provider.persistence.PersistenceServiceFactory;

public abstract class AModel implements IModel {

	protected List<IEntity> entities = new ArrayList<IEntity>();

	@Override
	public void save(IEntity entity) {
		PersistenceServiceFactory.getPersistenceService().save(entity);
		entities.add(entity);
	}

	@Override
	public void delete(IEntity entity) {
		PersistenceServiceFactory.getPersistenceService().delete(entity);
		entities.remove(entity);
	}

	@Override
	public void delete(List<? extends IEntity> entity) {
		for (IEntity iEntity : entity) {
			delete(iEntity);
		}		
	}
	
	@Override
	public void update(IEntity entity) {
		PersistenceServiceFactory.getPersistenceService().save(entity);
		entities.remove(entity);
		entities.add(entity);
	}

	@Override
	public void findAll(Class<? extends IEntity> entityClazz, IPersistenceListener listener) {
		PersistenceServiceFactory.getPersistenceService().findAll(entityClazz, listener);
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
