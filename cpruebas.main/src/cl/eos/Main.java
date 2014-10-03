package cl.eos;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			setUserAgentStylesheet(STYLESHEET_MODENA);
			try {
			  FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
				StackPane root = (StackPane) fxmlLoader.load();
				MainController controller = (MainController) fxmlLoader
						.getController();
				controller.setStage(primaryStage);

				Scene scene = new Scene(root, 1024, 768);
				scene.getStylesheets().add(
						getClass().getResource("ensemble2.css")
								.toExternalForm());
				primaryStage.setScene(scene);
				// primaryStage.setFullScreen(true);
				primaryStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
