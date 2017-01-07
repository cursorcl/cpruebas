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
    <T extends IEntity> void findAll(Class<T> entityClazz);
    <T extends IEntity> void findAll(Class<T> entityClazz, int offset, int items);
    <T extends IEntity> void findAll(Class<T> entityClazz, IPersistenceListener listener);
    <T extends IEntity> void findAll(Class<T> entityClazz, IPersistenceListener listener, int offset, int items);
    <T extends IEntity> void findAll(Class<T> entityClazz, IView... view);
    <T extends IEntity> void findAll(Class<T> entityClazz, int offset, int items, IView... view);
    <T extends IEntity> void findByAllId(Class<T> entityClazz, Long[] objects);
    <T extends IEntity> void findByAllId(Class<T> entityClazz, Long[] objects, IPersistenceListener listener);
    <T extends IEntity> void findByAllId(Class<T> entityClazz, Long[] objects, IView... view);
    <T extends IEntity> void findById(Class<T> entityClazz, Long id);
    <T extends IEntity> void findById(Class<T> entityClazz, Long id, IPersistenceListener listener);
    <T extends IEntity> void findById(Class<T> entityClazz, Long id, IView... view);
    <T extends IEntity> void findByName(Class<T> entityClazz, String name);
    <T extends IEntity> void findByName(Class<T> entityClazz, String name, IPersistenceListener listener);
    <T extends IEntity> void findByName(Class<T> entityClazz, String name, IView... view);
    <T extends IEntity> void findByParam(Class<T> entityClazz, Map<String, Object> params);
    <T extends IEntity> void findByParam(Class<T> entityClazz, Map<String, Object> params, IPersistenceListener listener);
    <T extends IEntity> void findByParam(Class<T> entityClazz, Map<String, Object> params, IView... view);
    /****************SYNCHRO*****************/
    <T extends IEntity> List<T> findAllSynchro(final Class<T> entityClazz);
    <T extends IEntity> List<T> findAllSynchro(final Class<T> entityClazz, int offset, int items);
    <T extends IEntity> T findSynchroById(Class<T> entityClazz, Long id);
    <T extends IEntity> List<T> findByAllIdSynchro(Class<T> entityClazz, Long[] id);
    <T extends IEntity> T findByIdSynchro(Class<T> entityClazz, Long id);
    <T extends IEntity> T findByNameSynchro(Class<T> entityClazz, String name);
    <T extends IEntity> List<T> findByParamsSynchro(Class<T> entityClazz, Map<String, Object> params);
    /**
     * Obtiene el modelo.
     * 
     * @return Modelo de la funcion.
     */
    IModel getModel();
    List<IView> getViews();
    /**
     * Este metodo debe ser implementado por todos los controladores. Se puede utilizar para pedir la informacion desde
     * el repositorio.
     */
    void initialize();
    void notifyChangeStatus(Object status);
    void notifyDeleted(IEntity entity);
    void notifyFound(IEntity entity);
    void notifySaved(IEntity entity);
    void removeView(IView view);
    /**
     * Metodo que envia al almacenamiento el objeto. El controlador debe liberar de inmediato el control y notificar que
     * se encuetra en el estado grabando a todas sus vistas. El controlador queda a la espera del repositorio de la
     * notificacion del resultado de la grabacion.
     * 
     * @param entity
     *            Objeto a grabar.
     */
    IEntity save(IEntity entity);
    /**
     * Metodo que selecciona el objeto. El controlador debe notificar que el objeto esta seleccionado a todas la vistas.
     * 
     * @param entity
     *            Objeto a grabar.
     */
    void select(IEntity entity);
    <T extends IEntity> void delete(Collection<T> entity);
    /**
     * Metodo que elimina el objeto. El controlador debe liberar de inmediato el control y notificar que se encuetra en
     * el estado eliminando a todas sus vistas. El controlador queda a la espera del repositorio de la notificacion del
     * resultado del borrado.
     * 
     * @param entity
     *            Objeto a grabar.
     */
    <T extends IEntity> void delete(IEntity entity);
    <T extends IEntity> void delete(List<T> entity);
}
