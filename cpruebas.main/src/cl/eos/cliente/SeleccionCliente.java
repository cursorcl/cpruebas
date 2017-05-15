package cl.eos.cliente;

import java.io.IOException;
import java.util.List;

import cl.eos.Environment;
import cl.eos.provider.persistence.PersistenceServiceFactory;
import cl.eos.restful.tables.R_Clientes;
import cl.eos.restful.tables.R_Usuarios;
import cl.eos.util.Utils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
  private ComboBox<R_Clientes> cmbClientes;
  @FXML
  private TextField txtUser;
  @FXML
  private PasswordField txtPassword;

  private Stage primaryStage;
  private List<R_Usuarios> usuarios;
  private List<R_Clientes> lstClientes;

  @FXML
  void initialize() {
    Environment.database = "aplicac2_cliente";
    usuarios = PersistenceServiceFactory.getPersistenceService().findAllSynchro(R_Usuarios.class);
    lstClientes = PersistenceServiceFactory.getPersistenceService().findAllSynchro(R_Clientes.class);
    cmbClientes.setItems(FXCollections.observableArrayList(lstClientes));

    txtUser.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        btnAccept.setDisable(validateEnableButton());
      }
    });
    txtPassword.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        btnAccept.setDisable(validateEnableButton());
      }
    });
    cmbClientes.valueProperty().addListener(new ChangeListener<R_Clientes>() {

      @Override
      public void changed(ObservableValue<? extends R_Clientes> observable, R_Clientes oldValue, R_Clientes newValue) {
        btnAccept.setDisable(validateEnableButton());
      }
    });
    btnAccept.setDisable(true);
    btnAccept.setOnAction(event -> {
      final R_Clientes cliente = cmbClientes.getSelectionModel().getSelectedItem();

      R_Usuarios user = usuarios.stream().filter(u -> u.getLogin().equals(txtUser.getText())).findFirst().orElse(null);
      String password = Utils.getMD5Hex(txtPassword.getText());
      if (user.getPassword().equals(password)) {
        Environment.client = cliente.getId();
        showApplication();
      } else {
        showError();
        exit();
      }



    });
    btnCancel.setOnAction(actionEvent -> {
      exit();
    });
  }

  private void exit() {
    Platform.exit();
    System.exit(0);
  }

  protected boolean validateEnableButton() {

    return txtPassword.getText().isEmpty() || txtUser.getText().isEmpty()
        || (cmbClientes.getSelectionModel().getSelectedItem() == null);
  }

  public void setStage(Stage primaryStage) {
    this.primaryStage = primaryStage;

  }

  private void showError() {
    final Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Usuario inválido.");
    alert.setHeaderText("El usuario y / o clave son incorrectas. ");
    alert.setContentText("Debe ingresar un usuario registrado en el sistema.");
    alert.showAndWait();
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
