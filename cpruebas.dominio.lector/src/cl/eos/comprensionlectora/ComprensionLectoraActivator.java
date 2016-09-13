package cl.eos.comprensionlectora;

import java.io.IOException;
import java.net.URL;

import cl.eos.interfaces.AActivator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class ComprensionLectoraActivator extends AActivator {
	public ComprensionLectoraActivator() {

		controller = new Contoller();
		
		URL url = ComprensionLectoraActivator.class
				.getResource("/cl/eos/comprensionlectora/ComprensionLectora.fxml");
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
