package cl.eos;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import cl.eos.controller.ClientesContoller;
import cl.eos.interfaces.AActivator;
import javafx.fxml.FXMLLoader;

public class ClientesActivator extends AActivator {

    static final Logger LOG = Logger.getLogger(ClientesActivator.class.getName());

    public ClientesActivator() {

        controller = new ClientesContoller();

        final URL url = ClientesActivator.class.getResource("/cl/eos/view/clientes.fxml");
        final FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            pane = fxmlLoader.load(url.openStream());
            view = fxmlLoader.getController();
            view.setPanel(pane);
            controller.addView(view);
        } catch (final IOException e) {
            LOG.severe(e.getMessage());
        }

    }

}
