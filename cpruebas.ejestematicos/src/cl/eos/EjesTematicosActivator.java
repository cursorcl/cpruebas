package cl.eos;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import cl.eos.controller.EjesTematicosContoller;
import cl.eos.interfaces.AActivator;

public class EjesTematicosActivator extends AActivator {

	public EjesTematicosActivator() {

		controller = new EjesTematicosContoller();
		
		URL url = EjesTematicosActivator.class
				.getResource("/cl/eos/view/Ejes_tematicos.fxml");
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
