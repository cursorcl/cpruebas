package cl.eos;

import java.io.IOException;
import java.net.URL;

import cl.eos.controller.NivelEvaluacionController;
import cl.eos.interfaces.AActivator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class NivelEvaluacionActivator extends AActivator {

	public NivelEvaluacionActivator() {
		controller = new NivelEvaluacionController();

		URL url = NivelEvaluacionActivator.class.getResource("/cl/eos/view/cpruebas.niveles.evaluacion.rango.evaluacion.fxml");
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
