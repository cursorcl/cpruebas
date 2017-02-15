package cl.eos.imp.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import cl.eos.interfaces.controller.IController;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.entity.IPersistenceListener;
import cl.eos.interfaces.entity.PersistenceListenerAdapter;
import cl.eos.interfaces.model.IModel;
import cl.eos.interfaces.view.IView;

public abstract class AController implements IController, IPersistenceListener {
  protected List<IView> views;
  protected IEntity selectedEntity;
  protected IModel model;
  private boolean initialized = false;

  public AController() {
    if (model != null) {
      model.setController(this);
    }
  }

  @Override
  public void addView(IView view) {
    if (views == null) {
      views = new ArrayList<IView>();
    }
    if (!views.contains(view)) {
      views.add(view);
      if (view.getController() == null || view.getController() != view) {
        view.setController(this);
      }
    }
    if(!initialized)
    {
      initialize();
      initialized = true;
    }
  }

  @Override
  public void add(IEntity entity) {}

  @Override
  public <T extends IEntity> void delete(Collection<T> entity) {
    final List<T> list = new ArrayList<>(entity);
    delete(list);
  }

  @Override
  public void delete(IEntity entity) {
    if (model != null && entity.getId() != null) {
      final IEntity result = model.delete(entity);
      notifyDeleted(result);
    }
  }

  @Override
  public <T extends IEntity> void delete(List<T> entity) {
    while (entity.size() > 0) {
      delete(entity.get(0));
      entity.remove(0);
    }
  }
  
  public <T extends IEntity> void deleteByParams(Class<T> entity, Map<String, Object> params) {
    model.deleteByParams(entity, params);
  }


  @Override
  public <T extends IEntity> void findAll(Class<T> entityClazz, IPersistenceListener listener) {
    model.findAll(entityClazz, listener);
  }

  @Override
  public <T extends IEntity> void findAll(Class<T> entityClazz, final IView... pView) {
    model.findAll(entityClazz, new PersistenceListenerAdapter() {
      @Override
      public void onFindAllFinished(List<Object> list) {
        for (final IView view : pView) {
          view.onDataArrived(list);
        }
      }
    });
  }


  @Override
  public <T extends IEntity> void findByAllId(Class<T> entityClazz, Long[] objects,
      IPersistenceListener listener) {
    model.findByAllId(entityClazz, objects, listener);
  }

  @Override
  public <T extends IEntity> void findByAllId(Class<T> entityClazz, Long[] objects,
      final IView... pView) {
    model.findByAllId(entityClazz, objects, new PersistenceListenerAdapter() {
      @Override
      public void onFindAllFinished(List<Object> list) {
        for (final IView view : pView) {
          view.onDataArrived(list);
        }
      }
    });
  }


  @Override
  public <T extends IEntity> void findById(Class<T> entityClazz, Long id,
      IPersistenceListener listener) {
    model.findById(entityClazz, id, listener);
  }

  @Override
  public <T extends IEntity> void findById(Class<T> entityClazz, Long id, final IView... pView) {
    model.findById(entityClazz, id, new PersistenceListenerAdapter() {
      @Override
      public void onFound(IEntity entity) {
        for (final IView view : pView) {
          view.onFound(entity);
        }
      }
    });
  }

  @Override
  public <T extends IEntity> void findByName(Class<T> entityClazz, String name,
      IPersistenceListener listener) {
    model.findByName(entityClazz, name, listener);
  }

  @Override
  public <T extends IEntity> void findByName(Class<T> entityClazz, String name,
      final IView... pView) {
    model.findByName(entityClazz, name, new PersistenceListenerAdapter() {
      @Override
      public void onFound(IEntity entity) {
        for (final IView view : pView) {
          view.onFound(entity);
        }
      }
    });
  }

  @Override
  public <T extends IEntity> void findAll(Class<T> entityClazz, IPersistenceListener listener,
      int offset, int items) {}

  @Override
  public <T extends IEntity> void findAll(Class<T> entityClazz, int offset, int items,
      IView... view) {}

  /*
   * (non-Javadoc)
   * 
   * @see cl.eos.interfaces.controller.IController#findByParam(java.lang.Class, java.util.Map,
   * cl.eos.interfaces.entity.IPersistenceListener)
   */
  @Override
  public <T extends IEntity> void findByParam(Class<T> entityClazz, Map<String, Object> params,
      IPersistenceListener listener) {
    model.findByParams(entityClazz, params, listener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see cl.eos.interfaces.controller.IController#findByParam(java.lang.Class, java.util.Map,
   * cl.eos.interfaces.view.IView[])
   */
  @Override
  public <T extends IEntity> void findByParam(Class<T> entityClazz, Map<String, Object> params,
      IView... pView) {
    model.findByParams(entityClazz, params, new PersistenceListenerAdapter() {
      @Override
      public void onFindFinished(List<Object> list) {
        for (final IView view : pView) {
          view.onDataArrived(list);
        }
      }
    });
  }

  @Override
  public <T extends IEntity> T findSynchroById(Class<T> entityClazz, Long id) {
    return model.findByIdSynchro(entityClazz, id);
  }

  @Override
  public <T extends IEntity> List<T> findAllSynchro(final Class<T> entityClazz) {
    return model.findAllSynchro(entityClazz);
  }

  @Override
  public <T extends IEntity> List<T> findAllSynchro(final Class<T> entityClazz, int offset,
      int items) {
    return model.findAllSynchro(entityClazz, offset, items);
  }

  @Override
  public <T extends IEntity> List<T> findByAllIdSynchro(Class<T> entityClazz, Long[] id) {
    return model.findByAllIdSynchro(entityClazz, id);
  }

  @Override
  public <T extends IEntity> T findByIdSynchro(Class<T> entityClazz, Long id) {
    return model.findByIdSynchro(entityClazz, id);
  }

  @Override
  public <T extends IEntity> T findByNameSynchro(Class<T> entityClazz, String name) {
    return model.findByNameSynchro(entityClazz, name);
  }

  @Override
  public <T extends IEntity> List<T> findByParamsSynchro(Class<T> entityClazz,
      Map<String, Object> params) {
    return model.findByParamsSynchro(entityClazz, params);
  }



  @Override
  public IModel getModel() {
    return model;
  }

  @Override
  public List<IView> getViews() {
    return views;
  }

  @Override
  public abstract void initialize();

  @Override
  public void notifyChangeStatus(Object status) {
    if (views != null) {
      for (final IView view : views) {
        view.onChangeStatus(status);
      }
    }
  }

  @Override
  public void notifyDeleted(IEntity entity) {
    if (views != null) {
      for (final IView view : views) {
        view.onDeleted(entity);
      }
    }
  }

  @Override
  public void notifyFound(IEntity entity) {
    if (views != null) {
      for (final IView view : views) {
        view.onFound(entity);
      }
    }
  }

  @Override
  public void notifySaved(IEntity entity) {
    if (views != null) {
      for (final IView view : views) {
        view.onSaved(entity);
      }
    }
  }

  @Override
  public void onError(String error) {
    if (views != null) {
      for (final IView view : views) {
        view.onError(error);
      }
    }
  }

  @Override
  public void onFindAllFinished(List<Object> list) {
    if (views != null) {
      for (final IView view : views) {
        view.onDataArrived(list);
      }
    }
  }

  @Override
  public void onFindFinished(List<Object> list) {
    if (views != null) {
      for (final IView view : views) {
        view.onDataArrived(list);
      }
    }
  }

  @Override
  public void onFound(IEntity entity) {
    if (views != null) {
      for (final IView view : views) {
        view.onFound(entity);
      }
    }
  }

  @Override
  public void removeView(IView view) {
    if (views != null) {
      views.remove(view);
    }
  }

  @Override
  public IEntity save(IEntity entity) {
    IEntity result = null;
    if (model != null) {
      result = model.findByIdSynchro(entity.getClass(), entity.getId());
      if (result != null) {
        result = model.update(entity);
      } else {
        result = model.save(entity);
      }
      notifySaved(result);
    }
    return result;
  }

  @Override
  public void select(IEntity otObject) {
    if (model != null) {
      selectedEntity = model.get(otObject);
    }
  }
}
