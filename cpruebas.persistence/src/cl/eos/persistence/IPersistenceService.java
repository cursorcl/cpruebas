package cl.eos.persistence;

import java.util.List;

import cl.eos.interfaces.entity.IEntity;

public interface IPersistenceService {

	
	void save(IEntity entity);
	void update(IEntity entity);
	void delete(IEntity entity);
	
	List<IEntity> getAll(Class<? extends IEntity> entityClazz);
	
	void disconnect();

}