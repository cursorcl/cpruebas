package cl.eos.model;

import cl.eos.imp.model.AModel;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.provider.persistence.PersistenceServiceFactory;

public class CursosModel extends AModel {

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
	public void update(IEntity entity) {
		PersistenceServiceFactory.getPersistenceService().save(entity);
		entities.remove(entity);
		entities.add(entity);
	}

}
