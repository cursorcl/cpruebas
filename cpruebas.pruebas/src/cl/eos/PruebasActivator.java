package cl.eos;

import java.io.IOException;
import java.net.URL;

import cl.eos.controller.PruebasController;
import cl.eos.interfaces.AActivator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class PruebasActivator extends AActivator{

	public PruebasActivator() {
		controller = new PruebasController();
		
		URL url = PruebasActivator.class
				.getResource("/cl/eos/view/Pruebas.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader();
		try {
			pane = (Parent) fxmlLoader.load(url.openStream());
			view = fxmlLoader.getController();
			view.setPanel(pane);
			controller.addView(view);
			
		} catch (IOException e) {
			e.printStackTrace();
		}	}
}
