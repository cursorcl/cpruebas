package cl.eos.imp.view;

import java.io.IOException;
import java.net.URL;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ProgressForm {
  private final Stage dialogStage;
  private Progress pr;


  public ProgressForm() {
    dialogStage = new Stage();
    dialogStage.initStyle(StageStyle.UTILITY);
    dialogStage.setResizable(false);
    dialogStage.initModality(Modality.APPLICATION_MODAL);

    final URL url = Progress.class.getResource("progress.fxml");
    final FXMLLoader fxmlLoader = new FXMLLoader();
    Parent pane;
    Scene scene;

    try {
      pane = (Parent) fxmlLoader.load(url.openStream());
      scene = new Scene(pane);

      pr = fxmlLoader.getController();
      dialogStage.setScene(scene);
    } catch (final IOException e) {
      e.printStackTrace();
    }

  }

  public Stage getDialogStage() {
    return dialogStage;
  }

  public void message(String message) {
    pr.message.setText(message);
  }

  public void updateMessage(String message) {
    pr.message.setText(message);
  }

  public void updateProgress(float step, float total) {
    pr.progressbar.progressProperty().set(step / total);
  }

  public void updateProgress(int step, int total) {
    updateProgress((float) step, (float) total);
  }


  public void showWorkerProgress(final Task<?> task) {
    pr.message.textProperty().bind(task.messageProperty());
    pr.progressbar.progressProperty().bind(task.progressProperty());
    dialogStage.show();
  }



  public void title(String title) {
    dialogStage.setTitle(title);
  }

}
