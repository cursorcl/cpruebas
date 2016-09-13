package cl.eos.calidadlectora;

import java.io.IOException;
import java.net.URL;

import cl.eos.interfaces.AActivator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class CalidadLectoraActivator extends AActivator {
	public CalidadLectoraActivator() {

		controller = new Contoller();
		
		URL url = CalidadLectoraActivator.class
				.getResource("/cl/eos/calidadlectora/CalidadLectora.fxml");
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
