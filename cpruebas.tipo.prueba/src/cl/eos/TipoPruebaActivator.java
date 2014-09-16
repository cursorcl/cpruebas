package cl.eos;

import java.io.IOException;
import java.net.URL;

import cl.eos.controller.TipoPruebaController;
import cl.eos.interfaces.AActivator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class TipoPruebaActivator extends AActivator {
	public TipoPruebaActivator() {
		TipoPruebaController controller = new TipoPruebaController();

		URL url = TipoPruebaActivator.class
				.getResource("/cl/eos/view/TipoPrueba.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader();
		try {
			pane = (Parent) fxmlLoader.load(url.openStream());
			view = fxmlLoader.getController();
			view.setParent(pane);
			controller.addView(view);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
