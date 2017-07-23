package cl.eos.activator;

import java.io.IOException;
import java.net.URL;

import cl.eos.controller.PruebasController;
import cl.eos.interfaces.AActivator;
import javafx.fxml.FXMLLoader;

public class ListadoPruebasActivator extends AActivator {

    public ListadoPruebasActivator() {
        controller = new PruebasController();

        final URL url = ListadoPruebasActivator.class.getResource("/cl/eos/view/ListadoPruebas.fxml");
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
