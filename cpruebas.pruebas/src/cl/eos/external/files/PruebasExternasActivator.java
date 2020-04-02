package cl.eos.external.files;

import java.io.IOException;
import java.net.URL;

import cl.eos.interfaces.AActivator;
import javafx.fxml.FXMLLoader;

public class PruebasExternasActivator extends AActivator {

    public PruebasExternasActivator() {
        controller = new PruebasExternasController();

        final URL url = PruebasExternasActivator.class.getResource("/cl/eos/external/files/PruebasExternasView.fxml");
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
