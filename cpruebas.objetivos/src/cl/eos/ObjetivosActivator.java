package cl.eos;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import cl.eos.controller.ObjetivosContoller;
import cl.eos.interfaces.AActivator;

public class ObjetivosActivator extends AActivator {

	public ObjetivosActivator() {

		controller = new ObjetivosContoller();
		
		URL url = ObjetivosActivator.class
				.getResource("/cl/eos/view/Objetivos.fxml");
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
