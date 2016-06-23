package cl.eos.cliente;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import cl.eos.Environment;
import cl.eos.MainController;
import cl.eos.cliente.Clientes.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SeleccionCliente {

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 740;
    protected double xOffset;
    protected double yOffset;

    @FXML
    private Button btnAccept;

    @FXML
    private Button btnCancel;

    @FXML
    private ComboBox<Clientes.Cliente> cmbClientes;

    private Stage primaryStage;

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
            btnAccept.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    Cliente cliente = cmbClientes.getSelectionModel().getSelectedItem();

                    if (cliente != null) {
                        Environment.database = cliente.alias;
                        showApplication();
                    }

                }
            });
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private void showApplication() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Main.fxml"));
            StackPane root = (StackPane) fxmlLoader.load();
            MainController controller = (MainController) fxmlLoader.getController();
            controller.setStage(primaryStage);

            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });
            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    primaryStage.setX(event.getScreenX() - xOffset);
                    primaryStage.setY(event.getScreenY() - yOffset);
                }
            });

            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            primaryStage.setX((primScreenBounds.getWidth() - WIDTH) / 2);
            primaryStage.setY((primScreenBounds.getHeight() - HEIGHT) / 2);
            Scene scene = new Scene(root, WIDTH, HEIGHT);

            scene.getStylesheets().add(getClass().getResource("../ensemble2.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.getIcons()
                    .add(new Image(SeleccionCliente.class.getResourceAsStream("/cl/eos/images/logo32.png")));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;

    }

}
