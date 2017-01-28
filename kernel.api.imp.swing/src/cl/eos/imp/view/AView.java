package cl.eos.imp.view;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;

import cl.eos.interfaces.controller.IController;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.view.IView;

public abstract class AView implements IView {
    private String title;
    protected IController controller;
    protected JPanel pane;

    @Override
    public void addView(IView view) {

    }

    @Override
    public IController getController() {
        return controller;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Object getPanel() {
        return pane;
    }

    @Override
    public Dimension getPreferredSize() {
        return null;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public List<IView> getViews() {
        return null;
    }

    @Override
    public void onChangeStatus(Object status) {
    }

    @Override
    public void onDataArrived(List<Object> list) {

    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFound(IEntity entity) {

    }

    @Override
    public void removeView(IView view) {

    }

    @Override
    public void setController(IController controller) {
        this.controller = controller;
        controller.addView(this);
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

}
