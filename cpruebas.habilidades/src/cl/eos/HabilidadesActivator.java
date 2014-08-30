package cl.eos;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import cl.eos.controller.HabilidadesContoller;
import cl.eos.interfaces.AActivator;

public class HabilidadesActivator extends AActivator {

	public HabilidadesActivator() {

		controller = new HabilidadesContoller();
		
		URL url = HabilidadesActivator.class
				.getResource("/cl/eos/view/Habilidades.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader();
		try {
			pane = (Parent) fxmlLoader.load(url.openStream());
			view = fxmlLoader.getController();
			controller.addView(view);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
