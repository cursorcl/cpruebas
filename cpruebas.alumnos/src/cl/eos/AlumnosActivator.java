package cl.eos;

import java.io.IOException;
import java.net.URL;

import org.apache.log4j.Logger;

import cl.eos.controller.AlumnosContoller;
import cl.eos.interfaces.AActivator;
import javafx.fxml.FXMLLoader;

public class AlumnosActivator extends AActivator {

    static final Logger LOG = Logger.getLogger(AlumnosActivator.class);

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
            AlumnosActivator.LOG.error(e);
        }

    }

}
