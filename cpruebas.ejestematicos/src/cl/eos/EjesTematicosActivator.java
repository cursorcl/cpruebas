package cl.eos;

import java.io.IOException;
import java.net.URL;

import cl.eos.controller.EjesTematicosContoller;
import cl.eos.interfaces.AActivator;
import javafx.fxml.FXMLLoader;

public class EjesTematicosActivator extends AActivator {

    public EjesTematicosActivator() {

        controller = new EjesTematicosContoller();

        final URL url = EjesTematicosActivator.class.getResource("/cl/eos/view/Ejes_tematicos.fxml");
        final FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            pane = fxmlLoader.load(url.openStream());
            view = fxmlLoader.getController();
            view.setPanel(pane);
            controller.addView(view);
        } catch (final IOException e) {
            e.printStackTrace();
        }

    }

}
