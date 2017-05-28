package cl.eos.cliente;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.prefs.Preferences;

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
  private TextField txtServer;
  @FXML
  private PasswordField txtPassword;

  private Stage primaryStage;
  private List<R_Usuarios> usuarios;
  private List<R_Clientes> lstClientes;
  private R_Usuarios user;
  private R_Clientes cliente;

  @FXML
  void initialize() {

    Preferences prefs = Preferences.userRoot().node(Environment.KEY);
    Environment.database = prefs.get("Database", "aplicac2_cliente");
    Environment.server = prefs.get("Server", Environment.server);
    Environment.client = prefs.getLong("Cliente", Environment.client);


    usuarios = PersistenceServiceFactory.getPersistenceService().findAllSynchro(R_Usuarios.class);
    lstClientes = PersistenceServiceFactory.getPersistenceService().findAllSynchro(R_Clientes.class);
    cliente = lstClientes.stream().filter(c -> c.getId().equals(Environment.client)).findAny().orElse(null);
    cmbClientes.setItems(FXCollections.observableArrayList(lstClientes));
    cmbClientes.getSelectionModel().select(cliente);

    txtServer.setText(Environment.server);

    txtServer.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        btnAccept.setDisable(validateEnableButton());
      }
    });

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

      if (!isValidURL(txtServer.getText())) {
        showErrorServer();
      }

      cliente = cmbClientes.getSelectionModel().getSelectedItem();
      user = usuarios.stream().filter(u -> u.getLogin().equals(txtUser.getText())).findFirst().orElse(null);
      String password = Utils.getMD5Hex(txtPassword.getText());
      if (user.getPassword().equals(password)) {
        Environment.client = cliente.getId();
        Environment.server = txtServer.getText();

        prefs.put("Database", Environment.database);
        prefs.put("Server", Environment.server);
        prefs.putLong("Cliente", Environment.client);

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
        || (cmbClientes.getSelectionModel().getSelectedItem() == null || txtServer.getText().isEmpty());
  }

  public boolean isValidURL(String url) {

    URL u = null;

    try {
      u = new URL(url);
    } catch (MalformedURLException e) {
      return false;
    }

    try {
      u.toURI();
    } catch (URISyntaxException e) {
      return false;
    }

    return true;
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

  private void showErrorServer() {
    final Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Dirección inválida.");
    alert.setHeaderText("La dirección del servidor es incorrecta. ");
    alert.setContentText("Debe ingresar una dirección de servidor correcta.");
    alert.showAndWait();
  }

  private void showApplication() {
    try {
      final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
      final StackPane root = (StackPane) fxmlLoader.load();
      final MainController controller = (MainController) fxmlLoader.getController();
      controller.setCliente(cliente);
      controller.setUsuario(user);
      controller.setClientes(lstClientes);
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
