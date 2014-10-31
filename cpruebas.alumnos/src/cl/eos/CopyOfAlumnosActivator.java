package cl.eos;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import cl.eos.controller.AlumnosContoller;
import cl.eos.interfaces.AActivator;

public class CopyOfAlumnosActivator extends AActivator {

	public CopyOfAlumnosActivator() {

		controller = new AlumnosContoller();
		
		URL url = CopyOfAlumnosActivator.class
				.getResource("/cl/eos/view/Alumnos.fxml");
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
