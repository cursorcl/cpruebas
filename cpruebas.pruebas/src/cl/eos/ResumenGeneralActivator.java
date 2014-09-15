package cl.eos;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import cl.eos.controller.ResumenGeneralController;
import cl.eos.interfaces.AActivator;

public class ResumenGeneralActivator extends AActivator{

		public ResumenGeneralActivator() {
			controller = new ResumenGeneralController();
			
			URL url = ResumenGeneralActivator.class
					.getResource("/cl/eos/view/ResumenGeneral.fxml");
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
