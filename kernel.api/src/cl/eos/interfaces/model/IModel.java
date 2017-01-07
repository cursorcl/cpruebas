package cl.eos.interfaces.model;

import java.util.List;
import java.util.Map;

import cl.eos.interfaces.controller.IController;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.entity.IPersistenceListener;

/**
 * Un modelo corresponde a la instancia que contiene los elementos, entre sus responsabilidades se encuentra:
 * <p>
 * <li>Persistencia de los elementos.
 * <li>Busqueda de los elementos.
 * <p>
 * Un modelo sabe que Entity accede en la persistencia, por lo tanto todas las operaciones que realiza son sobre la
 * entidad que maneja.
 *
 * <p>
 *
 * @author eosorio
 */
public interface IModel {
    <T extends IEntity> void findAll(Class<T> entityClazz);
    <T extends IEntity> void findAll(Class<T> entityClazz, int offset, int items);
    <T extends IEntity> void findAll(Class<T> entityClazz, IPersistenceListener listener);
    <T extends IEntity> void findAll(Class<T> entityClazz, IPersistenceListener listener, int offset, int items);
    <T extends IEntity> void findByAllId(Class<T> entityClazz, Long[] id);
    <T extends IEntity> void findByAllId(Class<T> entityClazz, Long[] id, IPersistenceListener listener);
    
    <T extends IEntity> void findById(Class<T> entityClazz, Long id);
    <T extends IEntity> void findById(Class<T> entityClazz, Long id, IPersistenceListener listener);
    
    <T extends IEntity> void findByName(Class<T> entityClazz, String name);
    <T extends IEntity> void findByName(Class<T> entityClazz, String name, IPersistenceListener listener);

    <T extends IEntity> void findByParams(Class<T> entityClazz, Map<String, Object> params);
    <T extends IEntity> void findByParams(Class<T> entityClazz, Map<String, Object> params, final IPersistenceListener listener);
    /**** SYNCHRO COMMANDS ***/
    <T extends IEntity> List<T> findAllSynchro(final Class<T> entityClazz);
    <T extends IEntity> List<T> findAllSynchro(final Class<T> entityClazz, int offset, int items);
    
    <T extends IEntity> List<T> findByAllIdSynchro(Class<T> entityClazz, Long[] id);
    <T extends IEntity> T findByIdSynchro(Class<T> entityClazz, Long id);
    
    <T extends IEntity> T findByNameSynchro(Class<T> entityClazz, String name);
    
    <T extends IEntity> List<T> findByParamsSynchro(Class<T> entityClazz, Map<String, Object> params);
    /**
     * Realiza la busqueda del elemento que tiene el mismo identificador de entity en la persistencia.
     * 
     * @param entity
     *            Entidad que se quiere buscar, se hace uso del identificador para la busqueda.
     * @return Entidad encontrada en la BD, de no encontrarse retorna <code>null</code>.
     */
    IEntity get(IEntity entity);
    /**
     * Realiza la busqueda del elemento que tiene el <code>id</code> en la persistencia.
     * 
     * @param id
     *            Identificador del elemento a buscar.
     * @return Entidad encontrada en la BD, de no encontrarse retorna <code>null</code>.
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
    <T extends IEntity> void delete(List<T> entity);
}
