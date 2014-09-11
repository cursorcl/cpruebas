package cl.eos.interfaces.view;

import java.util.List;
import java.util.Map;

import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.entity.IPersistenceListener;

/**
 * Extiende a IView. Este tipo de vistas permiten realizar cambios a los objetos. Viene siendo una
 * vista de lectura / escritura.
 * 
 * @author eosorio
 * 
 */
public interface IFormView extends IView {

  /**
   * Metodo que se usa para enviar a grabar una entidad.
   * 
   * @param otObject Objeto que se quire grabar.
   */
  void save(IEntity otObject);

  /**
   * Metodo que se usa para enviar a eliminar una entidad.
   * 
   * @param otObject Objeto que se quire eliminar.
   */
  void delete(IEntity otObject);

  void delete(List<? extends IEntity> otObject);

  /**
   * Metodo que se usa para indicar la selección de una entidad.
   * 
   * @param otObject Objeto que se quire seleccionar.
   */
  void select(IEntity otObject);

  /**
   * Metodo que se usa para enviar a grabar una entidad.
   * 
   * @param otObject Objeto que se quire grabar.
   */
  void onSelected(IEntity otObject);

  /**
   * Metodo que se usa para enviar a grabar una entidad.
   * 
   * @param otObject Objeto que se quire grabar.
   */
  boolean validate();

  void find(String namedQuery, Map<String, Object> parameters, IPersistenceListener listener);

  void findById(Class<? extends IEntity> entityClazz, Long id, IPersistenceListener listener);

  void findByName(Class<? extends IEntity> entityClazz, String name, IPersistenceListener listener);

  void findAll(Class<? extends IEntity> entityClazz);

  void find(String namedQuery, Map<String, Object> parameters);

  void findById(Class<? extends IEntity> entityClazz, Long id);

  void findByName(Class<? extends IEntity> entityClazz, String name);
}
