package cl.eos;

import java.io.IOException;
import java.net.URL;

import cl.eos.controller.EvaluacionEjeTematicoController;
import cl.eos.interfaces.AActivator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class EvaluacionEjeTematicoActivator extends AActivator {

	public EvaluacionEjeTematicoActivator() {
		controller = new EvaluacionEjeTematicoController();

		URL url = EvaluacionEjeTematicoActivator.class.getResource("/cl/eos/view/evaluacion.eje.tematico.view.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader();
		try {
			pane = (Parent) fxmlLoader.load(url.openStream());
			view = fxmlLoader.getController();
			view.setPanel(pane);
			controller.addView(view);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
