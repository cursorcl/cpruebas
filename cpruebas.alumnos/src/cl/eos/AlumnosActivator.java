package cl.eos;

import java.io.IOException;
import java.net.URL;

import org.apache.log4j.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import cl.eos.controller.AlumnosContoller;
import cl.eos.interfaces.AActivator;

public class AlumnosActivator extends AActivator {

	static final Logger LOG = Logger.getLogger(AlumnosActivator.class);

	public AlumnosActivator() {

		controller = new AlumnosContoller();

		URL url = AlumnosActivator.class.getResource("/cl/eos/view/AlumnosTableTree.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader();
		try {
			pane = (Parent) fxmlLoader.load(url.openStream());
			view = fxmlLoader.getController();
			view.setPanel(pane);
			controller.addView(view);
		} catch (IOException e) {
			LOG.error(e);
		}

	}

}
