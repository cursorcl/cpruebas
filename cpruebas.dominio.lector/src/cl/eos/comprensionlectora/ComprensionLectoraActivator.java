package cl.eos.comprensionlectora;

import java.io.IOException;
import java.net.URL;

import cl.eos.interfaces.AActivator;
import javafx.fxml.FXMLLoader;

public class ComprensionLectoraActivator extends AActivator {
    public ComprensionLectoraActivator() {

        controller = new Contoller();

        final URL url = ComprensionLectoraActivator.class
                .getResource("/cl/eos/comprensionlectora/SComprensionLectora.fxml");
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
