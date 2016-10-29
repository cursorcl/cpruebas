package cl.eos.interfaces;

import cl.eos.interfaces.controller.IController;
import cl.eos.interfaces.view.IView;

public class AActivator implements IActivator {

    protected IController controller;
    protected IView view;
    protected Object pane;

    @Override
    public IController getController() {
        return controller;
    }

    @Override
    public Object getPane() {
        return pane;
    }

    @Override
    public IView getView() {
        return view;
    }

}
