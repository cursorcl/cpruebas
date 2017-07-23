package cl.eos.imp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cl.eos.interfaces.controller.IController;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.entity.IPersistenceListener;
import cl.eos.interfaces.model.IModel;
import cl.eos.provider.persistence.PersistenceServiceFactory;

public abstract class AModel implements IModel {
    protected List<IEntity> entities = new ArrayList<IEntity>();
    protected IController controller;
    @Override
    public <T extends IEntity> void findAll(Class<T> entityClazz) {
        if (controller != null && controller instanceof IPersistenceListener) {
            findAll(entityClazz, (IPersistenceListener) controller);
        }
    }
    @Override
    public <T extends IEntity> void findAll(Class<T> entityClazz, int offset, int items) {
        if (controller != null && controller instanceof IPersistenceListener) {
            findAll(entityClazz, (IPersistenceListener) controller, offset, items);
        }
    }
    @Override
    public <T extends IEntity> void findAll(Class<T> entityClazz, IPersistenceListener listener) {
        PersistenceServiceFactory.getPersistenceService().findAll(entityClazz, listener);
    }
    @Override
    public <T extends IEntity> void findAll(Class<T> entityClazz, IPersistenceListener listener, int offset, int items) {
        PersistenceServiceFactory.getPersistenceService().findAll(entityClazz, listener, offset, items);
    }
    @Override
    public <T extends IEntity> void findByAllId(Class<T> entityClazz, Long[] id) {
        if (controller != null && controller instanceof IPersistenceListener) {
            PersistenceServiceFactory.getPersistenceService().findByAllId(entityClazz, id, (IPersistenceListener) controller);
        }
    }
    @Override
    public <T extends IEntity> void findByAllId(Class<T> entityClazz, Long[] id, IPersistenceListener listener) {
        PersistenceServiceFactory.getPersistenceService().findByAllId(entityClazz, id, listener);
    }
    @Override
    public <T extends IEntity> void findById(Class<T> entityClazz, Long id) {
        if (controller != null && controller instanceof IPersistenceListener) {
            findById(entityClazz, id, (IPersistenceListener) controller);
        }
    }
    @Override
    public <T extends IEntity> void findById(Class<T> entityClazz, Long id, IPersistenceListener listener) {
        PersistenceServiceFactory.getPersistenceService().findById(entityClazz, id, listener);
    }
    @Override
    public <T extends IEntity> void findByName(Class<T> entityClazz, String name) {
        if (controller != null && controller instanceof IPersistenceListener) {
            findByName(entityClazz, name, (IPersistenceListener) controller);
        }
    }
    @Override
    public <T extends IEntity> void findByName(Class<T> entityClazz, String name, IPersistenceListener listener) {
        PersistenceServiceFactory.getPersistenceService().findByName(entityClazz, name, listener);
    }
    public <T extends IEntity> void findByParams(Class<T> entityClazz, Map<String, Object> params) {
        if (controller != null && controller instanceof IPersistenceListener) {
            findByParams(entityClazz, params, (IPersistenceListener) controller);
        }
    }
    @Override
    public <T extends IEntity> void findByParams(Class<T> entityClazz, Map<String, Object> params, IPersistenceListener listener) {
        PersistenceServiceFactory.getPersistenceService().findByParams(entityClazz, params, listener);
    }
    @Override
    public <T extends IEntity> List<T> findAllSynchro(final Class<T> entityClazz) {
        return PersistenceServiceFactory.getPersistenceService().findAllSynchro(entityClazz);
    }
    @Override
    public <T extends IEntity> List<T> findAllSynchro(final Class<T> entityClazz, int offset, int items) {
        return PersistenceServiceFactory.getPersistenceService().findAllSynchro(entityClazz, offset, items);
    }
    @Override
    public <T extends IEntity> List<T> findByAllIdSynchro(Class<T> entityClazz, Long[] id) {
        return PersistenceServiceFactory.getPersistenceService().findByAllIdSynchro(entityClazz, id);
    }
    @Override
    public <T extends IEntity> T findByIdSynchro(Class<T> entityClazz, Long id) {
        return PersistenceServiceFactory.getPersistenceService().findByIdSynchro(entityClazz, id);
    }
    @Override
    public <T extends IEntity> T findByNameSynchro(Class<T> entityClazz, String name) {
        return PersistenceServiceFactory.getPersistenceService().findByNameSynchro(entityClazz, name);
    }
    @Override
    public <T extends IEntity> List<T> findByParamsSynchro(Class<T> entityClazz, Map<String, Object> params) {
        return PersistenceServiceFactory.getPersistenceService().findByParamsSynchro(entityClazz, params);
    }
    @Override
    public IEntity get(IEntity entity) {
        IEntity result = null;
        if (entities != null) {
            final int index = entities.indexOf(entity);
            if (index != -1) {
                result = entities.get(index);
            }
        }
        return result;
    }
    @Override
    public IEntity get(Long id) {
        IEntity result = null;
        if (entities != null) {
            for (final IEntity entity : entities) {
                if (entity.getId().equals(id)) {
                    result = entity;
                    break;
                }
            }
        }
        return result;
    }
    @Override
    public IController getController() {
        return controller;
    }
    @Override
    public void setController(IController controller) {
        this.controller = controller;
    }
    @Override
    public IEntity save(IEntity entity) {
        final IEntity result = PersistenceServiceFactory.getPersistenceService().save(entity);
        final int idx = entities.indexOf(result);
        if (idx != -1) entities.remove(idx);
        if(result != null) entities.add(entity);
        return result;
    }
    @Override
    public IEntity update(IEntity entity) {
        final IEntity result = PersistenceServiceFactory.getPersistenceService().update(entity);
        final int idx = entities.indexOf(result);
        if (idx != -1) entities.remove(idx);
        entities.add(result);
        return result;
    }
    @Override
    public IEntity delete(IEntity entity) {
        final IEntity result = PersistenceServiceFactory.getPersistenceService().delete(entity);
        entities.remove(result);
        return result;
    }
    @Override
    public <T extends IEntity> void delete(List<T> entity) {
        for (final IEntity iEntity : entity) {
            delete(iEntity);
        }
    }
    public <T extends IEntity>  void deleteByParams(Class<T> entity, Map<String, Object> params) {
      PersistenceServiceFactory.getPersistenceService().deleteByParams(entity, params);
    }
}
