package cl.eos.calidadlectora;

import java.io.IOException;
import java.net.URL;

import cl.eos.interfaces.AActivator;
import javafx.fxml.FXMLLoader;

public class CalidadLectoraActivator extends AActivator {
    public CalidadLectoraActivator() {

        controller = new Contoller();

        final URL url = CalidadLectoraActivator.class.getResource("/cl/eos/calidadlectora/CalidadLectora.fxml");
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
