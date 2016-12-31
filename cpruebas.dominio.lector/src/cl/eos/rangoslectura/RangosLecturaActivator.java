package cl.eos.rangoslectura;

import java.io.IOException;
import java.net.URL;

import cl.eos.interfaces.AActivator;
import javafx.fxml.FXMLLoader;

public class RangosLecturaActivator extends AActivator {
    public RangosLecturaActivator() {

        controller = new RangosLecturaContoller();

        final URL url = RangosLecturaActivator.class.getResource("/cl/eos/rangoslectura/RangoLectura.fxml");
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
