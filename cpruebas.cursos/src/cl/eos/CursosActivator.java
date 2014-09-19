package cl.eos;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import cl.eos.controller.CursosContoller;
import cl.eos.interfaces.AActivator;

public class CursosActivator extends AActivator {

	public CursosActivator() {

		controller = new CursosContoller();
		
		URL url = CursosActivator.class
				.getResource("/cl/eos/view/Cursos.fxml");
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
