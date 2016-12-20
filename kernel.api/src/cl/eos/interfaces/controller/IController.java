package cl.eos.interfaces.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.entity.IPersistenceListener;
import cl.eos.interfaces.model.IModel;
import cl.eos.interfaces.view.IView;

public interface IController {

    void add(IEntity entity);

    void addView(IView view);

    void delete(Collection<? extends IEntity> entity);

    /**
     * Metodo que elimina el objeto. El controlador debe liberar de inmediato el
     * control y notificar que se encuetra en el estado eliminando a todas sus
     * vistas. El controlador queda a la espera del repositorio de la
     * notificacion del resultado del borrado.
     * 
     * @param entity
     *            Objeto a grabar.
     */
    void delete(IEntity entity);

    void delete(List<? extends IEntity> entity);

    void find(String namedQuery, Map<String, Object> parameters);

    void find(String namedQuery, Map<String, Object> parameters, IPersistenceListener listener);

    void find(String namedQuery, Map<String, Object> parameters, IView... view);

    void findAll(Class<? extends IEntity> entityClazz);

    void findAll(Class<? extends IEntity> entityClazz, IPersistenceListener listener);

    void findAll(Class<? extends IEntity> entityClazz, IView... view);

    void findByAllId(Class<? extends IEntity> entityClazz, Long[] objects);

    void findByAllId(Class<? extends IEntity> entityClazz, Long[] objects, IPersistenceListener listener);

    void findByAllId(Class<? extends IEntity> entityClazz, Long[] objects, IView... view);

    void findById(Class<? extends IEntity> entityClazz, Long id);

    void findById(Class<? extends IEntity> entityClazz, Long id, IPersistenceListener listener);

    void findById(Class<? extends IEntity> entityClazz, Long id, IView... view);

    void findByName(Class<? extends IEntity> entityClazz, String name);

    void findByName(Class<? extends IEntity> entityClazz, String name, IPersistenceListener listener);

    void findByName(Class<? extends IEntity> entityClazz, String name, IView... view);

    IEntity findSynchroById(Class<? extends IEntity> entityClazz, Long id);

    /**
     * Obtiene el modelo.
     * 
     * @return Modelo de la funcion.
     */
    IModel getModel();

    List<IView> getViews();

    /**
     * Este metodo debe ser implementado por todos los controladores. Se puede
     * utilizar para pedir la informacion desde el repositorio.
     */
    void initialize();

    void notifyChangeStatus(Object status);

    void notifyDeleted(IEntity entity);

    void notifyFound(IEntity entity);

    void notifySaved(IEntity entity);

    void removeView(IView view);

    /**
     * Metodo que envia al almacenamiento el objeto. El controlador debe liberar
     * de inmediato el control y notificar que se encuetra en el estado grabando
     * a todas sus vistas. El controlador queda a la espera del repositorio de
     * la notificacion del resultado de la grabacion.
     * 
     * @param entity
     *            Objeto a grabar.
     */
    IEntity save(IEntity entity);

    /**
     * Metodo que selecciona el objeto. El controlador debe notificar que el
     * objeto esta seleccionado a todas la vistas.
     * 
     * @param entity
     *            Objeto a grabar.
     */

    void select(IEntity entity);

}
