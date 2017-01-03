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

    public AController() {

        if (model != null) {
            {
                model.setController(this);
            }
        }
    }

    @Override
    public void add(IEntity entity) {
        // TODO Auto-generated method stub

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
        initialize();
    }

    @Override
    public void delete(Collection<? extends IEntity> entity) {
        final List<? extends IEntity> list = new ArrayList<>(entity);
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
    public void delete(List<? extends IEntity> entity) {
        while (entity.size() > 0) {
            delete(entity.get(0));
            entity.remove(0);
        }
    }

    @Override
    public void find(String namedQuery, Map<String, Object> parameters) {
        model.find(namedQuery, parameters, this);
    }

    @Override
    public void find(String namedQuery, Map<String, Object> parameters, IPersistenceListener listener) {
        model.find(namedQuery, parameters, listener);
    }

    @Override
    public void find(String namedQuery, Map<String, Object> parameters, final IView... pView) {
        model.find(namedQuery, parameters, new PersistenceListenerAdapter() {
            @Override
            public void onFindFinished(List<Object> list) {
                for (final IView view : pView) {
                    view.onDataArrived(list);
                }
            }
        });
    }

    @Override
    public void findAll(Class<? extends IEntity> entityClazz) {
        model.findAll(entityClazz, this);
    }
    
    /* (non-Javadoc)
     * @see cl.eos.interfaces.controller.IController#findAll(java.lang.Class, int, int)
     */
    @Override
    public void findAll(Class<? extends IEntity> entityClazz, int offset, int items) {
        model.findAll(entityClazz, this, offset, items);        
    }

    @Override
    public void findAll(Class<? extends IEntity> entityClazz, IPersistenceListener listener) {
        model.findAll(entityClazz, listener);
    }

    @Override
    public void findAll(Class<? extends IEntity> entityClazz, final IView... pView) {
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
    public void findByAllId(Class<? extends IEntity> entityClazz, Long[] id) {
        model.findByAllId(entityClazz, id);
    }

    @Override
    public void findByAllId(Class<? extends IEntity> entityClazz, Long[] objects, IPersistenceListener listener) {
        model.findByAllId(entityClazz, objects, listener);
    }

    @Override
    public void findByAllId(Class<? extends IEntity> entityClazz, Long[] objects, final IView... pView) {
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
    public void findById(Class<? extends IEntity> entityClazz, Long id) {
        model.findById(entityClazz, id, this);
    }

    @Override
    public void findById(Class<? extends IEntity> entityClazz, Long id, IPersistenceListener listener) {
        model.findById(entityClazz, id, listener);
    }

    @Override
    public void findById(Class<? extends IEntity> entityClazz, Long id, final IView... pView) {
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
    public void findByName(Class<? extends IEntity> entityClazz, String name) {
        model.findByName(entityClazz, name, this);
    }

    @Override
    public void findByName(Class<? extends IEntity> entityClazz, String name, IPersistenceListener listener) {
        model.findByName(entityClazz, name, listener);
    }

    @Override
    public void findByName(Class<? extends IEntity> entityClazz, String name, final IView... pView) {
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
    public <T extends IEntity> T findSynchroById(Class<T> entityClazz, Long id) {
        return model.findSynchroById(entityClazz, id);
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
            if (entity.getId() != null) {
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

    /* (non-Javadoc)
     * @see cl.eos.interfaces.controller.IController#findAll(java.lang.Class, cl.eos.interfaces.entity.IPersistenceListener, int, int)
     */
    @Override
    public void findAll(Class<? extends IEntity> entityClazz, IPersistenceListener listener, int offset, int items) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see cl.eos.interfaces.controller.IController#findAll(java.lang.Class, int, int, cl.eos.interfaces.view.IView[])
     */
    @Override
    public void findAll(Class<? extends IEntity> entityClazz, int offset, int items, IView... view) {
        // TODO Auto-generated method stub
        
    }
    
    

}
