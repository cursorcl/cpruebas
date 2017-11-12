package cl.eos.interfaces.model;

import java.util.List;
import java.util.Map;

import cl.eos.interfaces.controller.IController;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.entity.IPersistenceListener;

/**
 * Un modelo corresponde a la instancia que contiene los elementos, entre sus
 * responsabilidades se encuentra:
 * <p>
 * <li>Persistencia de los elementos.
 * <li>Busqueda de los elementos.
 * <p>
 * Un modelo sabe que Entity accede en la persistencia, por lo tanto todas las
 * operaciones que realiza son sobre la entidad que maneja.
 *
 * <p>
 *
 * @author eosorio
 */
public interface IModel {

    /**
     * Elimina el elemento en el medio de persistencia.
     * 
     * @param entity
     *            El elemento a ser eliminado.
     */
    IEntity delete(IEntity entity);

    /**
     * Elimina los elementos en el medio de persistencia.
     * 
     * @param entity
     *            Lista de elementos a ser eliminados.
     */
    void delete(List<? extends IEntity> entity);

    void find(final String namedQuery, final Map<String, Object> parameters);

    void find(final String namedQuery, final Map<String, Object> parameters, IPersistenceListener listener);

    void findAll(Class<? extends IEntity> entityClazz);

    /**
     * Busca todos los registros de una entidad.
     * 
     * @param entityClazz
     *            Clase que se quiere buscar.
     * @param listener
     *            A quien se le notifica del termino de la ejecucion.
     */
    void findAll(Class<? extends IEntity> entityClazz, IPersistenceListener listener);
    
    void findByAllId(Class<? extends IEntity> entityClazz, Object[] id);

    void findByAllId(Class<? extends IEntity> entityClazz, Object[] id, IPersistenceListener listener);

    void findById(Class<? extends IEntity> entityClazz, Long id);

    void findById(Class<? extends IEntity> entityClazz, Long id, IPersistenceListener listener);

    void findByName(Class<? extends IEntity> entityClazz, String name);

    void findByName(Class<? extends IEntity> entityClazz, String name, IPersistenceListener listener);

    IEntity findSynchroById(Class<? extends IEntity> entityClazz, Long id);
    
    List<IEntity> findSynchro(final String namedQuery, final Map<String, Object> parameters);
    
    /**
     * Realiza la busqueda del elemento que tiene el mismo identificador de
     * entity en la persistencia.
     * 
     * @param entity
     *            Entidad que se quiere buscar, se hace uso del identificador
     *            para la busqueda.
     * @return Entidad encontrada en la BD, de no encontrarse retorna
     *         <code>null</code>.
     */
    IEntity get(IEntity entity);

    /**
     * Realiza la busqueda del elemento que tiene el <code>id</code> en la
     * persistencia.
     * 
     * @param id
     *            Identificador del elemento a buscar.
     * @return Entidad encontrada en la BD, de no encontrarse retorna
     *         <code>null</code>.
     */
    IEntity get(Long id);

    /**
     * Obtiene el controlador asociado al modelo.
     * 
     * @return Controlador que se encuentra asociado al modelo.
     */
    IController getController();

    /**
     * Graba el elemento en el medio de persistencia.
     * 
     * @param entity
     *            El elemento a ser grabado.
     */
    IEntity save(IEntity entity);

    /**
     * Establece el controlador al modelo.
     * 
     * @param controller
     *            Controlador de la funcion que se asocia al modelo.
     */

    void setController(IController controller);

    /**
     * Graba el elemento en el medio de persistencia, asumiendo que existe.
     * 
     * @param entity
     *            El elemento a ser grabado.
     */
    IEntity update(IEntity entity);
    
    
    
    void findAll(Class<? extends IEntity> entityClazz, IPersistenceListener listener, int begin, int last);
}
