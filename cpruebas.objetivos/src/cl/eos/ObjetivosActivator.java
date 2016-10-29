package cl.eos;

import java.io.IOException;
import java.net.URL;

import cl.eos.controller.ObjetivosContoller;
import cl.eos.interfaces.AActivator;
import javafx.fxml.FXMLLoader;

public class ObjetivosActivator extends AActivator {

    public ObjetivosActivator() {

        controller = new ObjetivosContoller();

        final URL url = ObjetivosActivator.class.getResource("/cl/eos/view/Objetivos.fxml");
        final FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            pane = fxmlLoader.load(url.openStream());
            view = fxmlLoader.getController();
            view.setPanel(pane);
            controller.addView(view);
        } catch (final IOException e) {
            e.printStackTrace();
        }

    }

}
