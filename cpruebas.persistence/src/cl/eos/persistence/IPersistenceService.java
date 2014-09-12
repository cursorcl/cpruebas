package cl.eos.persistence;

import java.util.Map;

import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.entity.IPersistenceListener;

public interface IPersistenceService {

	void save(IEntity entity);

	void update(IEntity entity);

	void delete(IEntity entity);

	/**
	 * Busca todos los registros asociados al entity mencionado.
	 * 
	 * @param entityClazz
	 *            Entidad de la que se requieren todos los registros.
	 * @param listener
	 *            A quien se notifica del resultado.
	 */
	void findAll(Class<? extends IEntity> entityClazz,
			final IPersistenceListener listener);

	/**
	 * Busca la entidad asociada con el identificador.
	 * 
	 * @param entityClazz
	 *            Entidad de la que se requieren la búsqueda.
	 * @param listener
	 *            A quien se notifica del resultado.
	 */
	void findById(Class<? extends IEntity> entityClazz, Long id,
			final IPersistenceListener listener);

	/**
	 * Busca la entidad asociada con el nombre.
	 * 
	 * @param entityClazz
	 *            Entidad de la que se requieren la búsqueda.
	 * @param listener
	 *            A quien se notifica del resultado.
	 */
	void findByName(Class<? extends IEntity> entityClazz, String name,
			final IPersistenceListener listener);

	/**
	 * Ejecuta una consulta en base a una consulta nombrada.
	 * 
	 * @param namedQuery
	 *            Nombre del query que se ejecuta.
	 * @param parameters
	 *            parametros para la consulta.
	 * @param listener
	 *            A quien se notifica del resultado.
	 */
	void find(final String namedQuery, final Map<String, Object> parameters,
			final IPersistenceListener listener);

	void disconnect();

}