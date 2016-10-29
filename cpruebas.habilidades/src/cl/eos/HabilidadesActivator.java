package cl.eos;

import java.io.IOException;
import java.net.URL;

import cl.eos.controller.HabilidadesContoller;
import cl.eos.interfaces.AActivator;
import javafx.fxml.FXMLLoader;

public class HabilidadesActivator extends AActivator {

    public HabilidadesActivator() {

        controller = new HabilidadesContoller();

        final URL url = HabilidadesActivator.class.getResource("/cl/eos/view/Habilidades.fxml");
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
