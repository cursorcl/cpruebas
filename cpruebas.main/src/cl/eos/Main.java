package cl.eos;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			setUserAgentStylesheet(STYLESHEET_MODENA);
//			StackPane root = (StackPane) FXMLLoader.load(getClass()
//					.getResource("Main.fxml"));
//			
//			Scene scene = new Scene(root, 1024, 768);
//			scene.getStylesheets().add(
//					getClass().getResource("ensemble2.css").toExternalForm());
//			primaryStage.setScene(scene);
//			// primaryStage.setFullScreen(true);
//			primaryStage.show();

			URL url = getClass().getResource("Main.fxml");
			FXMLLoader fxmlLoader = new FXMLLoader();
			try {
				StackPane root = (StackPane) fxmlLoader.load(url.openStream());
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
