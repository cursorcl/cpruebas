package cl.eos.persistence;

import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.entity.IPersistenceListener;

public interface IPersistenceService {

	
	void save(IEntity entity);
	void update(IEntity entity);
	void delete(IEntity entity);
	
	void findAll(Class<? extends IEntity> entityClazz, final IPersistenceListener listener);
	
	void disconnect();

}