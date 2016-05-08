package cl.eos.imp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cl.eos.interfaces.controller.IController;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.entity.IPersistenceListener;
import cl.eos.interfaces.model.IModel;
import cl.eos.provider.persistence.PersistenceServiceFactory;

public abstract class AModel implements IModel {

	protected List<IEntity> entities = new ArrayList<IEntity>();
	protected IController controller;

	@Override
	public IEntity save(IEntity entity) {
		IEntity result = PersistenceServiceFactory.getPersistenceService().save(entity);
		int idx = entities.indexOf(result);
		if (idx != -1)
			entities.remove(idx);
		entities.add(entity);
		return result;
	}

	@Override
	public IController getController() {
		return controller;
	}

	@Override
	public void setController(IController controller) {
		this.controller = controller;
	}

	@Override
	public IEntity delete(IEntity entity) {
		IEntity result = PersistenceServiceFactory.getPersistenceService().delete(entity);
		entities.remove(result);
		return result;
	}

	@Override
	public void delete(List<? extends IEntity> entity) {
		for (IEntity iEntity : entity) {
			delete(iEntity);
		}
	}

	@Override
	public IEntity update(IEntity entity) {
		IEntity result = PersistenceServiceFactory.getPersistenceService().save(entity);
		int idx = entities.indexOf(result);
		if (idx != -1)
			entities.remove(idx);
		entities.add(result);
		return result;
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

	@Override
	public void find(String namedQuery, Map<String, Object> parameters, IPersistenceListener listener) {
		PersistenceServiceFactory.getPersistenceService().find(namedQuery, parameters, listener);
	}

	@Override
	public void findById(Class<? extends IEntity> entityClazz, Long id, IPersistenceListener listener) {
		PersistenceServiceFactory.getPersistenceService().findById(entityClazz, id, listener);
	}

	@Override
	public void findByName(Class<? extends IEntity> entityClazz, String name, IPersistenceListener listener) {
		PersistenceServiceFactory.getPersistenceService().findByName(entityClazz, name, listener);
	}

	@Override
	public void findAll(Class<? extends IEntity> entityClazz) {
		if (controller != null && controller instanceof IPersistenceListener) {
			findAll(entityClazz, (IPersistenceListener) controller);
		}
	}

	@Override
	public void find(String namedQuery, Map<String, Object> parameters) {
		if (controller != null && controller instanceof IPersistenceListener) {
			find(namedQuery, parameters, (IPersistenceListener) controller);
		}
	}

	@Override
	public void findById(Class<? extends IEntity> entityClazz, Long id) {
		if (controller != null && controller instanceof IPersistenceListener) {
			findById(entityClazz, id, (IPersistenceListener) controller);
		}
	}

	@Override
	public void findByAllId(Class<? extends IEntity> entityClazz, Object[] id) {
		if (controller != null && controller instanceof IPersistenceListener) {

			PersistenceServiceFactory.getPersistenceService().findByAllId(entityClazz, id,
					(IPersistenceListener) controller);
		}
	}

	public void findByAllId(Class<? extends IEntity> entityClazz, Object[] id, IPersistenceListener listener) {
		PersistenceServiceFactory.getPersistenceService().findByAllId(entityClazz, id, listener);
	}

	@Override
	public void findByName(Class<? extends IEntity> entityClazz, String name) {
		if (controller != null && controller instanceof IPersistenceListener) {
			findByName(entityClazz, name, (IPersistenceListener) controller);
		}
	}

}
