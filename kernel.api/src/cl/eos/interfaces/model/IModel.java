package cl.eos.interfaces.model;

import java.util.List;

import cl.eos.interfaces.entity.IEntity;

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

	List<IEntity> getAll();

	IEntity get(IEntity entity);

	IEntity get(Long id);
}
