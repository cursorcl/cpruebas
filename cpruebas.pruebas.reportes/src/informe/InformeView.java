package informe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import cl.eos.imp.view.AFormView;
import cl.eos.imp.view.ProgressForm;
import cl.eos.imp.view.WindowManager;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.TipoAlumno;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class InformeView extends AFormView {

    private static final String INFORME_COMPLETO = "Informe Completo";

    @FXML
    private Button btnGenerar;
    @FXML
    private AnchorPane pnlEdition;
    @FXML
    private Button btnCancelar;
    @FXML
    private ComboBox<Colegio> cmbColegio;
    @FXML
    private ComboBox<TipoAlumno> cmbTipoAlumno;
    @FXML
    ProgressBar progressBar;
    @FXML
    private Label lblAvance;

    ObservableList<Asignatura> lstAsignaturas;
    protected Colegio colegio;
    protected TipoAlumno tipoAlumno;
    protected XWPFDocument doc;
    Map<String, Object> parameters = new HashMap<>();

    public InformeView() {
        setTitle(INFORME_COMPLETO);
    }

    @FXML
    public void initialize() {
        cmbColegio.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                colegio = cmbColegio.getSelectionModel().getSelectedItem();
                if (colegio == null)
                    return;
            }
        });
        cmbTipoAlumno.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedItem();
            }
        });
        btnGenerar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Archivos Word", "*.doc", "*.docx"),
                        new ExtensionFilter("All Files", "*.*"));
                File selectedFile = fileChooser.showSaveDialog(null);
                if (selectedFile == null)
                    return;

                final ProgressForm dlg = new ProgressForm();
                dlg.title("Procesando Informe");
                dlg.message("Esto tomará algunos segundos...");
                
                
                final Task<Boolean> task = new Task<Boolean>() {

                    @Override
                    protected Boolean call() throws Exception {
                        InformeManager manager = new InformeManager(progressBar, selectedFile);
                        int nroAsignaturas = lstAsignaturas.size();
                        int n = 1;
                        for (Asignatura asignatura : lstAsignaturas) {
                            updateMessage(String.format("Procesado asignatura:" + asignatura.getName()));
                            manager.processAsignatura(tipoAlumno, colegio, asignatura);
                            updateProgress(n++, nroAsignaturas);
                        }
                        manager.finish();
                        return Boolean.TRUE;
                    }
                };
                task.setOnSucceeded((WorkerStateEvent t) -> {
                    dlg.getDialogStage().hide();
                    WindowManager.getInstance().hide(InformeView.this);
                });
                task.setOnFailed((WorkerStateEvent t) -> {
                    dlg.getDialogStage().hide();
                    throw new RuntimeException(task.getException());
                });
                
                dlg.showWorkerProgress(task);
                Executors.newSingleThreadExecutor().execute(task);

            }
        });
        btnCancelar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                WindowManager.getInstance().hide(InformeView.this);
            }
        });
    }

    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            Object entity = list.get(0);
            if (entity instanceof Colegio) {
                ObservableList<Colegio> pruebas = FXCollections.observableArrayList();
                for (Object lEntity : list) {
                    pruebas.add((Colegio) lEntity);
                }
                cmbColegio.setItems(pruebas);
            } else if (entity instanceof Asignatura) {
                lstAsignaturas = FXCollections.observableArrayList();
                for (Object lEntity : list) {
                    lstAsignaturas.add((Asignatura) lEntity);
                }
            } else if (entity instanceof TipoAlumno) {
                ObservableList<TipoAlumno> lstTipoAlumno = FXCollections.observableArrayList();
                for (Object lEntity : list) {
                    lstTipoAlumno.add((TipoAlumno) lEntity);
                }
                cmbTipoAlumno.setItems(lstTipoAlumno);
            }
        }
    }

}
