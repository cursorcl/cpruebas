package cl.eos;

import java.io.IOException;
import java.net.URL;

import cl.eos.controller.AsignaturasContoller;
import cl.eos.interfaces.AActivator;
import javafx.fxml.FXMLLoader;

public class AsignaturasActivator extends AActivator {

    public AsignaturasActivator() {

        controller = new AsignaturasContoller();

        final URL url = AsignaturasActivator.class.getResource("/cl/eos/view/Asignaturas.fxml");
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
