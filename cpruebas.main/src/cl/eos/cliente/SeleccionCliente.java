package cl.eos.cliente;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import cl.eos.Environment;
import cl.eos.cliente.Clientes.Cliente;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
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
        final File file = new File("res/Clientes.xml");
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(Clientes.class);
            final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            final Clientes clientes = (Clientes) jaxbUnmarshaller.unmarshal(file);
            final ObservableList<Clientes.Cliente> lst = FXCollections.observableArrayList(clientes.cliente);
            cmbClientes.setItems(lst);
            btnAccept.setOnAction(event -> {
                final Cliente cliente = cmbClientes.getSelectionModel().getSelectedItem();

                if (cliente != null) {
                    Environment.database = cliente.alias;
                    showApplication();
                } else {
                    final Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error selección de Cliente.");
                    alert.setHeaderText("Debe seleccionar un cliente. ");
                    alert.setContentText("No ha seleccionado un cliente para iniciar la aplicación.");
                    alert.showAndWait();
                }

            });
            btnCancel.setOnAction(event -> Platform.exit());
        } catch (final JAXBException e) {
            e.printStackTrace();
        }
    }

    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;

    }

    private void showApplication() {
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
            final StackPane root = (StackPane) fxmlLoader.load();
            final MainController controller = (MainController) fxmlLoader.getController();
            controller.setStage(primaryStage);

            root.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });
            root.setOnMouseDragged(event -> {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            });

            final Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            primaryStage.setX((primScreenBounds.getWidth() - SeleccionCliente.WIDTH) / 2);
            primaryStage.setY((primScreenBounds.getHeight() - SeleccionCliente.HEIGHT) / 2);
            final Scene scene = new Scene(root, SeleccionCliente.WIDTH, SeleccionCliente.HEIGHT);

            scene.getStylesheets().add(getClass().getResource("ensemble2.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.getIcons()
                    .add(new Image(SeleccionCliente.class.getResourceAsStream("/cl/eos/cliente/images/logo32.png")));
            primaryStage.show();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

}
