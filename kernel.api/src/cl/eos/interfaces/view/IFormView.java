package cl.eos.interfaces.view;

import cl.eos.interfaces.entity.IEntity;

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
}
