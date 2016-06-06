package cl.eos;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Migrator extends Application {
	protected double xOffset;
	protected double yOffset;

	@Override
	public void start(Stage primaryStage) {
		try {
			 primaryStage.setTitle("Migrar");
			setUserAgentStylesheet(STYLESHEET_MODENA);
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("migrator.fxml"));
			StackPane root = (StackPane) fxmlLoader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(this.getClass().getResource("list.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
