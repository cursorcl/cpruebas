package cl.eos;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import cl.eos.controller.AsignaturasContoller;
import cl.eos.interfaces.AActivator;

public class AsignaturasActivator extends AActivator {

	public AsignaturasActivator() {

		controller = new AsignaturasContoller();
		
		URL url = AsignaturasActivator.class
				.getResource("/cl/eos/view/Asignaturas.fxml");
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
