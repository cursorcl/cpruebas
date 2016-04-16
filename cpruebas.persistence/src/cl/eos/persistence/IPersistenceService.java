package cl.eos.persistence;

import java.util.List;
import java.util.Map;

import cl.eos.exception.ExceptionBD;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.entity.IPersistenceListener;

public interface IPersistenceService {

	IEntity save(IEntity entity);

	IEntity update(IEntity entity);

	IEntity delete(IEntity entity);

  /**
   * Se utiliza principalmente para ejecutar operaciones de actualizacion en la base de datos
   * (DELETE, UPDATE MASIVO etc.)
   * 
   * @param namedQuery Query nombrado asociado a una entidad.
   * @param parameters Los parametros que requiere el query.
   * @return Numero de filas afectadas.
   */
  int executeUpdate(final String namedQuery, Map<String, Object> parameters);

  /**
   * Busca todos los registros asociados al entity mencionado.
   * 
   * @param entityClazz Entidad de la que se requieren todos los registros.
   * @param listener A quien se notifica del resultado.
   */
  void findAll(Class<? extends IEntity> entityClazz, final IPersistenceListener listener);

  /**
   * Busca todos los registros asociados al entity mencionado.
   * La busqueda es sincrónica, por tanto el llamante queda detenido hasta terminar de procesar la consulta.
   * @param entityClazz Entidad de la que se requieren todos los registros. * @return
   * return Lista de valores encontrados.
   */
  List<Object> findAllSynchro(final Class<? extends IEntity> entityClazz);

  /**
   * Busca la entidad asociada con el identificador.
   * 
   * @param entityClazz Entidad de la que se requieren la b�squeda.
   * @param listener A quien se notifica del resultado.
   */
  void findById(Class<? extends IEntity> entityClazz, Long id, final IPersistenceListener listener);

  /**
   * Busca la entidad asociada con el identificador.
   * 
   * @param entityClazz Entidad de la que se requieren la b�squeda.
   * @param listener A quien se notifica del resultado.
   */
  void findByAllId(Class<? extends IEntity> entityClazz, Object[] id,
      final IPersistenceListener listener);

  /**
   * Busca la entidad asociada con el nombre.
   * 
   * @param entityClazz Entidad de la que se requieren la b�squeda.
   * @param listener A quien se notifica del resultado.
   */
  void findByName(Class<? extends IEntity> entityClazz, String name,
      final IPersistenceListener listener);

  /**
   * Ejecuta una consulta en base a una consulta nombrada.
   * 
   * @param namedQuery Nombre del query que se ejecuta.
   * @param parameters parametros para la consulta.
   * @param listener A quien se notifica del resultado.
   */
  void find(final String namedQuery, final Map<String, Object> parameters,
      final IPersistenceListener listener);

  void disconnect();

  void insert(String entity, List<Object> list, IPersistenceListener listener) throws ExceptionBD;

}
