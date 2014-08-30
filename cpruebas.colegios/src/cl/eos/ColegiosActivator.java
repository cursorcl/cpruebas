package cl.eos;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import cl.eos.controller.ColegiosContoller;
import cl.eos.interfaces.AActivator;

public class ColegiosActivator extends AActivator {
	public ColegiosActivator() {

		controller = new ColegiosContoller();
		
		URL url = ColegiosActivator.class
				.getResource("/cl/eos/view/Colegios.fxml");
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
