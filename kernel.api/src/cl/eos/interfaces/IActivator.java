package cl.eos.interfaces;

import cl.eos.interfaces.controller.IController;
import cl.eos.interfaces.view.IView;

public interface IActivator {

    IController getController();

    Object getPane();

    IView getView();
}
