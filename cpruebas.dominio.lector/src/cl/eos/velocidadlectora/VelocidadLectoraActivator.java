package cl.eos.velocidadlectora;

import java.io.IOException;
import java.net.URL;

import cl.eos.interfaces.AActivator;
import javafx.fxml.FXMLLoader;

public class VelocidadLectoraActivator extends AActivator {
    public VelocidadLectoraActivator() {

        controller = new VelocidadLectoraContoller();

        final URL url = VelocidadLectoraActivator.class.getResource("/cl/eos/velocidadlectora/SVelocidadLectora.fxml");
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
