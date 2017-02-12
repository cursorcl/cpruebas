package cl.eos.imp.view;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.entity.IPersistenceListener;
import cl.eos.interfaces.view.IFormView;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public abstract class AFormView extends AView implements IFormView {

    private IEntity selectedEntity = null;

    protected boolean confirmaEliminar() {
        final Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirma eliminación");
        alert.setHeaderText("Una vez borrado no se puede recuperar");
        alert.setContentText("Está seguro de borrar?");
        final Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;

    }

    @Override
    public void delete(Collection<? extends IEntity> otObject) {
        if (controller != null && confirmaEliminar()) {
            controller.delete(otObject);
        }
    }

    @Override
    public void delete(IEntity otObject) {
        delete(otObject, true);
    }

    @Override
    public void delete(IEntity otObject, boolean confirm) {
        if (controller != null) {
            boolean confirmed = true;
            if (confirm) {
                confirmed = confirmaEliminar();
            }
            if (confirmed) {
                controller.delete(otObject);
                selectedEntity = null;
            }
        }

    }

    @Override
    public void delete(List<? extends IEntity> otObject) {
        if (controller != null && confirmaEliminar()) {
            controller.delete(otObject);
        }
    }
    
    @Override
    public void deleteByParams(Class<? extends IEntity> entity, Map<String, Object> params)
    {
      controller.deleteByParams(entity, params);
    }
    

    @Override
    public void findAll(Class<? extends IEntity> entityClazz) {
        controller.findAll(entityClazz);
    }

    @Override
    public void findById(Class<? extends IEntity> entityClazz, Long id) {
        controller.findById(entityClazz, id, (IPersistenceListener) this);
    }

    @Override
    public void findById(Class<? extends IEntity> entityClazz, Long id, IPersistenceListener listener) {
        controller.findById(entityClazz, id, listener);
    }

    @Override
    public void findByName(Class<? extends IEntity> entityClazz, String name) {
        controller.findByName(entityClazz, name, (IPersistenceListener) this);
    }

    @Override
    public void findByName(Class<? extends IEntity> entityClazz, String name, IPersistenceListener listener) {
        controller.findByName(entityClazz, name, listener);
    }

    @Override
    public IEntity findSynchroById(Class<? extends IEntity> entityClazz, Long id) {
        return controller.findSynchroById(entityClazz, id);
    }

    /**
     * Obtiene la entidad seleccionada.
     * 
     * @return La entidad que se encuentra seleccionada en el momento.
     */
    public IEntity getSelectedEntity() {
        return selectedEntity;
    }

    @Override
    public void onSelected(IEntity entity) {
        if (selectedEntity != null && entity != null && !entity.equals(selectedEntity)) {
            selectedEntity = entity;
        }
    }

    /**
     * Elimina estilo del nodo que se asigna.
     * 
     * @param n
     */
    protected void removeAllStyle(Node n) {
        n.getStyleClass().removeAll("bad", "med", "good", "best");
        n.applyCss();
    }

    @Override
    public IEntity save(IEntity otObject) {
        IEntity resutl = null;
        if (controller != null && validate()) {
            resutl = controller.save(otObject);
            selectedEntity = null;
        }
        return resutl;
    }

    @Override
    public void select(IEntity otObject) {
        if (controller != null) {
            controller.select(otObject);
            selectedEntity = otObject;
        }
    }

    /**
     * Establece la entidad seleccionada.
     * 
     * @param selectedEntity
     *            La entidad que se selecciona.
     */
    public void setSelectedEntity(IEntity selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

    @Override
    public boolean validate() {
        return true;
    }
}
