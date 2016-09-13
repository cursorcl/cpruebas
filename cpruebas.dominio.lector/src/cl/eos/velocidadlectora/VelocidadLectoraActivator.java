package cl.eos.velocidadlectora;

import java.io.IOException;
import java.net.URL;

import cl.eos.interfaces.AActivator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class VelocidadLectoraActivator extends AActivator {
	public VelocidadLectoraActivator() {

		controller = new Contoller();
		
		URL url = VelocidadLectoraActivator.class
				.getResource("/cl/eos/velocidadlectora/VelocidadLectora.fxml");
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
