package cl.eos.interfaces.view;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.entity.IPersistenceListener;

/**
 * Extiende a IView. Este tipo de vistas permiten realizar cambios a los
 * objetos. Viene siendo una vista de lectura / escritura.
 *
 * @author eosorio
 *
 */
public interface IFormView extends IView {

    void delete(Collection<? extends IEntity> otObject);

    /**
     * Metodo que se usa para enviar a eliminar una entidad.
     * 
     * @param otObject
     *            Objeto que se quire eliminar.
     */
    void delete(IEntity otObject);

    void delete(IEntity otObject, boolean confirm);

    void delete(List<? extends IEntity> otObject);
    
    void deleteByParams(Class<? extends IEntity> entityClazz, Map<String, Object> params);

    void findAll(Class<? extends IEntity> entityClazz);

    void findById(Class<? extends IEntity> entityClazz, Long id);

    void findById(Class<? extends IEntity> entityClazz, Long id, IPersistenceListener listener);

    void findByName(Class<? extends IEntity> entityClazz, String name);

    void findByName(Class<? extends IEntity> entityClazz, String name, IPersistenceListener listener);

    IEntity findSynchroById(Class<? extends IEntity> entityClazz, Long id);
    List<? extends IEntity> findSynchroByParams(Class<? extends IEntity> entityClazz, Map<String, Object> params);
    

    /**
     * Metodo que se usa para enviar a grabar una entidad.
     * 
     * @param otObject
     *            Objeto que se quire grabar.
     */
    @Override
    void onSelected(IEntity otObject);

    /**
     * Metodo que se usa para enviar a grabar una entidad.
     * 
     * @param otObject
     *            Objeto que se quire grabar.
     */
    IEntity save(IEntity otObject);

    /**
     * Metodo que se usa para indicar la selección de una entidad.
     * 
     * @param otObject
     *            Objeto que se quire seleccionar.
     */
    void select(IEntity otObject);

    /**
     * Metodo que se usa para enviar a grabar una entidad.
     * 
     * @param otObject
     *            Objeto que se quire grabar.
     */
    boolean validate();
}
