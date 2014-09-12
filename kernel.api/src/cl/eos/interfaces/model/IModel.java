package cl.eos.interfaces.model;

import java.util.List;
import java.util.Map;

import cl.eos.interfaces.controller.IController;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.entity.IPersistenceListener;

/**
 * Interface de un modelo.
 * 
 * @author eosorio
 */
public interface IModel {

	
	void setController(IController controller);
	IController getController();
	/**
	 * Graba el modelo.
	 */
	void save(IEntity entity);

	void delete(IEntity entity);
	
	void delete(List<? extends IEntity> entity);

	void update(IEntity entity);

	/**
	 * Busca todos los registros de una entidad.
	 * @param entityClazz Clase que se quiere buscar.
	 * @param listener A quien se le notifica del termino de la ejecucion.
	 */
	void findAll(Class<? extends IEntity> entityClazz, IPersistenceListener listener);
	void findAll(Class<? extends IEntity> entityClazz);
	void find(final String namedQuery, final Map<String, Object> parameters, IPersistenceListener listener);
	void find(final String namedQuery, final Map<String, Object> parameters);
	void findById(Class<? extends IEntity> entityClazz, Long id, IPersistenceListener listener);
	void findById(Class<? extends IEntity> entityClazz, Long id);
	void findByName(Class<? extends IEntity> entityClazz, String name, IPersistenceListener listener);
	void findByName(Class<? extends IEntity> entityClazz, String name);

	IEntity get(IEntity entity);
	IEntity get(Long id);
}
