package cl.eos;

import java.io.IOException;
import java.net.URL;

import cl.eos.controller.TipoPruebaController;
import cl.eos.interfaces.AActivator;
import javafx.fxml.FXMLLoader;

public class TipoPruebaActivator extends AActivator {
    public TipoPruebaActivator() {
        final TipoPruebaController controller = new TipoPruebaController();

        final URL url = TipoPruebaActivator.class.getResource("/cl/eos/view/TipoPrueba.fxml");
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
