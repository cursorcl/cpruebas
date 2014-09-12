package cl.eos;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import cl.eos.controller.EvaluacionPruebaController;
import cl.eos.interfaces.AActivator;

public class EvaluacionPruebaActivator extends AActivator{

		public EvaluacionPruebaActivator() {
			controller = new EvaluacionPruebaController();
			
			URL url = EvaluacionPruebaActivator.class
					.getResource("/cl/eos/view/EvaluacionPrueba.fxml");
			FXMLLoader fxmlLoader = new FXMLLoader();
			try {
				pane = (Parent) fxmlLoader.load(url.openStream());
				view = fxmlLoader.getController();
				view.setParent(pane);
				controller.addView(view);
			} catch (IOException e) {
				e.printStackTrace();
			}	}
}
