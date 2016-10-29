package cl.eos;

import java.io.IOException;
import java.net.URL;

import cl.eos.controller.ProfesoresContoller;
import cl.eos.interfaces.AActivator;
import javafx.fxml.FXMLLoader;

public class ProfesoresActivator extends AActivator {

    public ProfesoresActivator() {

        controller = new ProfesoresContoller();

        final URL url = ProfesoresActivator.class.getResource("/cl/eos/view/Profesores.fxml");
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
