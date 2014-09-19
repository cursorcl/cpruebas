package cl.eos;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import cl.eos.controller.NivelEvaluacionController;
import cl.eos.interfaces.AActivator;

public class NivelEvaluacionActivator extends AActivator {
	public NivelEvaluacionActivator() {
		controller = new NivelEvaluacionController();
		
		URL url = NivelEvaluacionActivator.class
				.getResource("/cl/eos/view/NivelEvaluacion.fxml");
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
