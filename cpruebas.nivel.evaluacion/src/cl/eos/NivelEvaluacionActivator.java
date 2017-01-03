package cl.eos;

import java.io.IOException;
import java.net.URL;

import cl.eos.controller.NivelEvaluacionController;
import cl.eos.interfaces.AActivator;
import javafx.fxml.FXMLLoader;

public class NivelEvaluacionActivator extends AActivator {
    public NivelEvaluacionActivator() {
        controller = new NivelEvaluacionController();

        final URL url = NivelEvaluacionActivator.class.getResource("/cl/eos/view/NivelEvaluacion.fxml");
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
