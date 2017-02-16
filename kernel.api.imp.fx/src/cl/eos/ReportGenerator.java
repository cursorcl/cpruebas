package cl.eos;

import java.util.concurrent.Executors;

import cl.eos.imp.view.ProgressForm;
import cl.eos.interfaces.view.IReport;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Singleton que permite ejecutar métodos en una hebra diferente a la de JAVA-FX. Da la posibilidad
 * de ejecutar con visualizador de avance.
 * 
 * @author curso
 *
 */
public class ReportGenerator {


  public static void execute(IReport report) {
    ProgressForm pForm = new ProgressForm();
    pForm.title("Procesando...");
    pForm.message("Esto tomará algunos segundos.");

    Task<Void> task = new Task<Void>() {

      @Override
      protected Void call() throws Exception {
        return null;
      }

    };

    task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        pForm.getDialogStage().hide();
      }
    });
    task.setOnFailed(new EventHandler<WorkerStateEvent>() {

      @Override
      public void handle(WorkerStateEvent event) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Se ha producido un error");
        alert.setHeaderText("Ha fallado la generación del reporte");
        alert.setContentText(event.getSource().getMessage().toUpperCase());
      }
    });

    pForm.showWorkerProgress(task);
    Executors.newSingleThreadExecutor().execute(task);
  }

}
