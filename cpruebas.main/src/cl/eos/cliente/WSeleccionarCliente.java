package cl.eos.cliente;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WSeleccionarCliente extends Application {

	@Override
	public void start(Stage primaryStage) {
	    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ClientSelection.fxml"));
	    try {
	        BorderPane root = (BorderPane) fxmlLoader.load();
            SeleccionCliente controller = (SeleccionCliente) fxmlLoader.getController();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            //primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public static void main(String[] args) {
		launch(args);
	}
}
