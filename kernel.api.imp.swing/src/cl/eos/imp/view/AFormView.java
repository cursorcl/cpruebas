package cl.eos.imp.view;

import java.util.List;

import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.view.IFormView;

public abstract class AFormView extends AView implements IFormView {

    @Override
    public void delete(IEntity otObject) {
        if (controller != null && validate()) {
            controller.delete(otObject);
        }
    }

    @Override
    public void onDataArrived(List<Object> list) {

    }

    @Override
    public void onDeleted(IEntity entity) {

    }

    @Override
    public void onDeleting(IEntity entity) {

    }

    @Override
    public void onFound(IEntity entity) {

    }

    @Override
    public void onSaved(IEntity entity) {

    }

    @Override
    public void onSaving(IEntity entity) {

    }

    @Override
    public void onSelected(IEntity entity) {

    }

    @Override
    public IEntity save(IEntity otObject) {
        if (controller != null && validate()) {
            return controller.save(otObject);
        }
        return null;
    }

    @Override
    public void select(IEntity otObject) {
        if (controller != null) {
            controller.select(otObject);
        }
    }

    @Override
    public boolean validate() {
        return true;
    }

}
