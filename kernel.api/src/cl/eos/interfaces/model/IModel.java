package cl.eos.interfaces.model;

import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.entity.IPersistenceListener;

/**
 * Interface de un modelo.
 * 
 * @author eosorio
 */
public interface IModel {

	/**
	 * Graba el modelo.
	 */
	void save(IEntity entity);

	void delete(IEntity entity);

	void update(IEntity entity);

	void findAll(Class<? extends IEntity> entityClazz, IPersistenceListener listener);

	IEntity get(IEntity entity);

	IEntity get(Long id);
}
