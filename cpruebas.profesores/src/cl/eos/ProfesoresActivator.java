package cl.eos;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import cl.eos.controller.ProfesoresContoller;
import cl.eos.interfaces.AActivator;

public class ProfesoresActivator extends AActivator {

	public ProfesoresActivator() {

		controller = new ProfesoresContoller();
		
		URL url = ProfesoresActivator.class
				.getResource("/cl/eos/view/Profesores.fxml");
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
