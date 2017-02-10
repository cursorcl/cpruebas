package cl.eos.persistence;

import java.util.List;
import java.util.Map;

import cl.eos.exception.ExceptionBD;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.entity.IPersistenceListener;

public interface IPersistenceService {

    <T extends IEntity> void findAll(Class<T> entityClazz, final IPersistenceListener listener);

    <T extends IEntity> void findAll(Class<T> entityClazz, IPersistenceListener listener, int offset, int items);

    <T extends IEntity> void findByAllId(Class<T> entityClazz, Long[] id, final IPersistenceListener listener);

    <T extends IEntity> void findById(Class<T> entityClazz, Long id, final IPersistenceListener listener);

    <T extends IEntity> void findByName(Class<T> entityClazz, String name, final IPersistenceListener listener);

    <T extends IEntity> void findByParams(Class<T> entityClazz, Map<String, Object> params,
            final IPersistenceListener listener);

    /**** Synchro Methods *****/

    <T extends IEntity> List<T> findAllSynchro(final Class<T> entityClazz);

    <T extends IEntity> List<T> findAllSynchro(final Class<T> entityClazz, int offset, int items);
    
    <T extends IEntity> List<T> findByAllIdSynchro(Class<T> entityClazz, Long[] id);
        
    <T extends IEntity> T findByIdSynchro(Class<T> entityClazz, Long id);

    <T extends IEntity> T findByNameSynchro(Class<T> entityClazz, String name);

    <T extends IEntity> List<T> findByParamsSynchro(Class<T> entityClazz, Map<String, Object> params);


    void insert(String entity, List<Object> list, IPersistenceListener listener) throws ExceptionBD;

    IEntity save(IEntity entity);

    IEntity update(IEntity entity);

    IEntity delete(IEntity entity);

    void disconnect();

    <T extends IEntity> void deleteByParams(T entity, Map<String, Object> params);
}
