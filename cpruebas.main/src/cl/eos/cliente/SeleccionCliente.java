package cl.eos.cliente;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import cl.eos.imp.view.WindowButtons;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SeleccionCliente {

    @FXML
    private Button btnAccept;

    @FXML
    private Button btnCancel;

    @FXML
    private ComboBox<Clientes.Cliente> cmbClientes;
    @FXML
    void initialize() {
        File file = new File("res/Clientes.xml");
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(Clientes.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Clientes clientes = (Clientes) jaxbUnmarshaller.unmarshal(file);
            ObservableList<Clientes.Cliente> lst = FXCollections.observableArrayList(clientes.cliente);
            cmbClientes.setItems(lst);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    
//    public void setStage(Stage stage) {
//        this.stage = stage;
//        WindowButtons wButtons = new WindowButtons(stage);
//        AnchorPane.setRightAnchor(wButtons, 1.0);
//        AnchorPane.setTopAnchor(wButtons, 1.0);
//        pnlWindow.getChildren().add(wButtons);
//    }
}
