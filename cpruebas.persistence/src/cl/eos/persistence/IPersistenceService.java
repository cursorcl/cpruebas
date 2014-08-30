package cl.eos.persistence;

import cl.eos.interfaces.entity.IEntity;

public interface IPersistenceService {

	
	void save(IEntity entity);
	void delete(IEntity entity);
	void disconnect();

}