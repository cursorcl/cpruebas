package cl.eos.cliente;

import java.io.IOException;
import java.util.List;

import cl.eos.Environment;
import cl.eos.clone.Migrator;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.entity.IPersistenceListener;
import cl.eos.persistence.models.Clientes;
import cl.eos.provider.persistence.PersistenceServiceFactory;
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

	static final int WIDTH = 1200;
	static final int HEIGHT = 740;
	double xOffset;
	double yOffset;
	final ObservableList<Clientes> lstClientes = FXCollections.observableArrayList();

	@FXML
	private Button btnAccept;

	@FXML
	private Button btnCancel;

	@FXML
	private ComboBox<Clientes> cmbClientes;

	private Stage primaryStage;
	private Clientes cliente;

	@FXML
	void initialize() {

		List<String> databases = Migrator.databases();
		PersistenceServiceFactory.getCommonPersistenceService().findAll(Clientes.class, new IPersistenceListener() {

			@Override
			public void onFound(IEntity entity) {

			}

			@Override
			public void onFindFinished(List<Object> list) {

			}

			@Override
			public void onFindAllFinished(List<Object> list) {
				if (list == null)
					return;
				StringBuffer buffer =  null;
				if (list == null || list.isEmpty() || !(list.get(0) instanceof Clientes)) {
					return;
				}
				for (Object obj : list) {
					cliente = (Clientes) obj;
					if (databases.contains("cpr_" + cliente.getNombrefantasia().toLowerCase())) {
						lstClientes.add(cliente);
					} else {
						if(buffer == null)
						{
							buffer = new StringBuffer();
							buffer.append("Estos clientes no tienen su base de datos:");
						}
						buffer.append(cliente.getName());
						buffer.append(",");
					}
				}
				if(buffer != null)
				{
					buffer.replace(buffer.length() - 1, buffer.length(), ".");
					
					final Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Inconsistencia Clientes.");
					alert.setHeaderText("Cliente sin base de datos. ");
					alert.setContentText(buffer.toString());
					alert.showAndWait();
				}
				cmbClientes.setItems(lstClientes);
			}

			@Override
			public void onError(String error) {

			}
		});
		btnAccept.setOnAction(event -> {
			cliente = cmbClientes.getSelectionModel().getSelectedItem();

			if (cliente != null) {
				Environment.database = "cpr_" + cliente.getNombrefantasia();
				Environment.client = cliente.getName();
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
