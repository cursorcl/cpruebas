package cl.eos;

import java.io.File;
import java.io.IOException;

import org.controlsfx.dialog.Dialogs;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import cl.eos.exceptions.CPruebasException;
import cl.eos.util.Utils;
import cl.sisdef.license.ProductKeyValidation;
import cl.sisdef.license.UtilProductKey;
import cl.sisdef.license.impl.PropertyFileProductKeyStorage;
import cl.sisdef.license.impl.WMICSerialProvider;

public class Main extends Application {
	protected double xOffset;
	protected double yOffset;
	static final String PRODUCT_KEYSTORE = "keystore";
	private static final int PRODUCT_ID = 71;
	@Override
	public void start(Stage primaryStage) {
		try {
			processProductAuthentication(PRODUCT_ID);
			setUserAgentStylesheet(STYLESHEET_MODENA);
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
						"Main.fxml"));
				StackPane root = (StackPane) fxmlLoader.load();
				MainController controller = (MainController) fxmlLoader
						.getController();
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

				Scene scene = new Scene(root, 1200, 740);
				scene.getStylesheets().add(
						getClass().getResource("ensemble2.css")
								.toExternalForm());
				primaryStage.setScene(scene);
				primaryStage.initStyle(StageStyle.UNDECORATED);
				primaryStage
						.getIcons()
						.add(new Image(
								Main.class
										.getResourceAsStream("/cl/eos/images/logo32.png")));
				// primaryStage.setFullScreen(true);
				primaryStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (CPruebasException e) {
			Dialogs.create().owner(null).title("Producto no validado.")
			.masthead("Debe solicitar código de activación.")
			.message("Solicitar código de activación al correo curso.cl@gmail.com.").showError();

		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	
    /**
    *
    * Metodo.
    * @param productId
    * @throws Exception
    */
   static public void processProductAuthentication(int productId) throws CPruebasException
   {
         ProductKeyValidation pkv = UtilProductKey.validateProductKey(
             new PropertyFileProductKeyStorage(new File(Utils.getDefaultDirectory() + "/" + PRODUCT_KEYSTORE), null),
             productId, new WMICSerialProvider(), true);
         if (pkv == null || pkv.isUsable() == false)
         {
           throw new CPruebasException("Producto no validado.");
         }
   }
}
