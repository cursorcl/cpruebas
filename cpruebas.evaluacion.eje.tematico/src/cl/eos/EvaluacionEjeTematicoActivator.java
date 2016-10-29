package cl.eos;

import java.io.IOException;
import java.net.URL;

import cl.eos.controller.EvaluacionEjeTematicoController;
import cl.eos.interfaces.AActivator;
import javafx.fxml.FXMLLoader;

public class EvaluacionEjeTematicoActivator extends AActivator {

    public EvaluacionEjeTematicoActivator() {
        controller = new EvaluacionEjeTematicoController();

        final URL url = EvaluacionEjeTematicoActivator.class
                .getResource("/cl/eos/view/evaluacion.eje.tematico.view.fxml");
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
