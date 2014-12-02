package cl.eos.imp.view;

import java.util.List;
import java.util.Map;

import javafx.scene.Node;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.entity.IPersistenceListener;
import cl.eos.interfaces.view.IFormView;

public abstract class AFormView extends AView implements IFormView {

  private IEntity selectedEntity = null;

  @Override
  public void save(IEntity otObject) {
    if (controller != null && validate()) {
      controller.save(otObject);
      selectedEntity = null;
    }
  }

  @Override
  public void delete(IEntity otObject) {
    delete(otObject, true);
  }

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
  public void select(IEntity otObject) {
    if (controller != null) {
      controller.select(otObject);
      selectedEntity = otObject;
    }
  }

  @Override
  public void onSelected(IEntity entity) {
    if (selectedEntity != null && entity != null && !entity.equals(selectedEntity)) {
      selectedEntity = entity;
    }
  }

  @Override
  public boolean validate() {
    return true;
  }

  /**
   * Obtiene la entidad seleccionada.
   * 
   * @return La entidad que se encuentra seleccionada en el momento.
   */
  public IEntity getSelectedEntity() {
    return selectedEntity;
  }

  /**
   * Establece la entidad seleccionada.
   * 
   * @param selectedEntity La entidad que se selecciona.
   */
  public void setSelectedEntity(IEntity selectedEntity) {
    this.selectedEntity = selectedEntity;
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

  protected boolean confirmaEliminar() {
    Action response =
        Dialogs.create().owner(null).title("Confirma eliminación")
            .masthead("Una vez borrado no se puede recuperar").message("Está seguro de borrar?")
            .actions(Dialog.Actions.OK, Dialog.Actions.CANCEL).showConfirm();
    return response == Dialog.Actions.OK;

  }


  public void find(String namedQuery, Map<String, Object> parameters, IPersistenceListener listener) {
    controller.find(namedQuery, parameters, listener);
  }

  public void findById(Class<? extends IEntity> entityClazz, Long id, IPersistenceListener listener) {
    controller.findById(entityClazz, id, listener);
  }

  public void findByName(Class<? extends IEntity> entityClazz, String name,
      IPersistenceListener listener) {
    controller.findByName(entityClazz, name, listener);
  }

  public void findAll(Class<? extends IEntity> entityClazz) {
    controller.findAll(entityClazz);
  }

  public void find(String namedQuery, Map<String, Object> parameters) {
    controller.find(namedQuery, parameters, (IPersistenceListener) this);
  }

  public void findById(Class<? extends IEntity> entityClazz, Long id) {
    controller.findById(entityClazz, id, (IPersistenceListener) this);
  }

  public void findByName(Class<? extends IEntity> entityClazz, String name) {
    controller.findByName(entityClazz, name, (IPersistenceListener) this);
  }
}
