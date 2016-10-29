package cl.eos;

import java.io.IOException;
import java.net.URL;

import cl.eos.controller.ColegiosContoller;
import cl.eos.interfaces.AActivator;
import javafx.fxml.FXMLLoader;

public class ColegiosActivator extends AActivator {
    public ColegiosActivator() {

        controller = new ColegiosContoller();

        final URL url = ColegiosActivator.class.getResource("/cl/eos/view/Colegios.fxml");
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
