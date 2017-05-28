package cl.eos.cliente;

import java.io.File;
import java.io.IOException;

import cl.eos.exceptions.CPruebasException;
import cl.eos.util.Utils;
import cl.sisdef.license.ProductKeyValidation;
import cl.sisdef.license.UtilProductKey;
import cl.sisdef.license.impl.PropertyFileProductKeyStorage;
import cl.sisdef.license.impl.WMICSerialProvider;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WSeleccionarCliente extends Application {
  static final String PRODUCT_KEYSTORE = "keystore";
  private static final int PRODUCT_ID = 71;

  public static void main(String[] args) {
    Application.launch(args);

  }

  static public void processProductAuthentication(int productId) throws CPruebasException {
    final ProductKeyValidation pkv = UtilProductKey.validateProductKey(
        new PropertyFileProductKeyStorage(
            new File(Utils.getDefaultDirectory() + "/" + WSeleccionarCliente.PRODUCT_KEYSTORE), null),
        productId, new WMICSerialProvider(), true);
    if (pkv == null || pkv.isUsable() == false) {
      throw new CPruebasException("Producto no validado.");
    }
  }

  @Override
  public void start(Stage primaryStage) {
    try {

      WSeleccionarCliente.processProductAuthentication(WSeleccionarCliente.PRODUCT_ID);
      Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
      final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ClientSelection.fxml"));
      final BorderPane root = (BorderPane) fxmlLoader.load();
      final SeleccionCliente controller = (SeleccionCliente) fxmlLoader.getController();
      controller.setStage(primaryStage);
      final Scene scene = new Scene(root);
      scene.getStylesheets().add(getClass().getResource("ensemble2.css").toExternalForm());

      primaryStage.setScene(scene);
      primaryStage.initStyle(StageStyle.UNDECORATED);
      primaryStage.show();
    } catch (final CPruebasException e) {
      final Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Producto no validado.");
      alert.setHeaderText("Debe solicitar código de activación.");
      alert.setContentText("Solicitar código de activación al correo curso.cl@gmail.com.");
      alert.showAndWait();
    } catch (final IOException e) {
      e.printStackTrace();
    }

  }
}
