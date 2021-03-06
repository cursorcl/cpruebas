package cl.eos;

import java.io.IOException;
import java.net.URL;

import cl.eos.controller.PruebasController;
import cl.eos.interfaces.AActivator;
import javafx.fxml.FXMLLoader;

public class PruebasActivator extends AActivator {

    public PruebasActivator() {
        controller = new PruebasController();

        final URL url = PruebasActivator.class.getResource("/cl/eos/view/Pruebas.fxml");
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
