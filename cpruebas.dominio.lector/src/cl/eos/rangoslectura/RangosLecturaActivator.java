package cl.eos.rangoslectura;

import java.io.IOException;
import java.net.URL;

import cl.eos.interfaces.AActivator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class RangosLecturaActivator extends AActivator {
	public RangosLecturaActivator() {

		controller = new Contoller();
		
		URL url = RangosLecturaActivator.class
				.getResource("/cl/eos/rangoslectura/RangoLectura.fxml");
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
