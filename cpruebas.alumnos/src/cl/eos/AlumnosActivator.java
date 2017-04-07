package cl.eos;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import cl.eos.controller.AlumnosContoller;
import cl.eos.interfaces.AActivator;
import javafx.fxml.FXMLLoader;

public class AlumnosActivator extends AActivator {

  static final Logger LOG = Logger.getLogger(AlumnosActivator.class.getName());

  public AlumnosActivator() {

    controller = new AlumnosContoller();

    final URL url = AlumnosActivator.class.getResource("/cl/eos/view/AlumnosTableTree.fxml");
    final FXMLLoader fxmlLoader = new FXMLLoader();
    try {
      pane = fxmlLoader.load(url.openStream());
      view = fxmlLoader.getController();
      view.setPanel(pane);
      controller.addView(view);
    } catch (final IOException e) {
      e.printStackTrace();
      LOG.severe(e.getMessage());
    }

  }

}
