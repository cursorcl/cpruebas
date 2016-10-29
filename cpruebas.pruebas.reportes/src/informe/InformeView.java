package informe;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
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
import cl.eos.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import jfxtras.labs.scene.control.BigDecimalField;

public class InformeView extends AFormView {

    private static final String INFORME_COMPLETO = "Informe Completo";

    @FXML
    private Button btnGenerar;
    @FXML
    private AnchorPane pnlEdition;
    @FXML
    private Button btnCancelar;
    @FXML
    private TextField txtTipoPrueba;
    @FXML
    private ComboBox<Colegio> cmbColegio;
    @FXML
    private ComboBox<TipoAlumno> cmbTipoAlumno;
    @FXML
    private BigDecimalField bdAnoEscolar;

    ObservableList<Asignatura> lstAsignaturas;
    protected Colegio colegio;
    protected TipoAlumno tipoAlumno;
    protected XWPFDocument doc;
    Map<String, Object> parameters = new HashMap<>();

    public InformeView() {
        setTitle(InformeView.INFORME_COMPLETO);
    }

    @FXML
    public void initialize() {
        cmbColegio.setOnAction(event -> {
            colegio = cmbColegio.getSelectionModel().getSelectedItem();
            if (colegio == null)
                return;
        });
        cmbTipoAlumno.setOnAction(event -> tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedItem());
        btnGenerar.setOnAction(event -> {
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(Utils.getDefaultDirectory());
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Archivos Word", "*.doc", "*.docx"),
                    new ExtensionFilter("All Files", "*.*"));
            final File selectedFile = fileChooser.showSaveDialog(null);
            if (selectedFile == null)
                return;

            final ProgressForm dlg = new ProgressForm();
            dlg.title("Procesando Informe");
            dlg.message("Esto tomará algunos segundos...");

            final Task<Boolean> task = new Task<Boolean>() {

                @Override
                protected Boolean call() throws Exception {
                    final InformeManager manager = new InformeManager(colegio, selectedFile);
                    manager.updateFields(txtTipoPrueba.getText(), bdAnoEscolar.getNumber().toString());
                    final int nroAsignaturas = lstAsignaturas.size();
                    int n = 1;
                    for (final Asignatura asignatura : lstAsignaturas) {
                        updateMessage(String.format("Procesado asignatura:" + asignatura.getName()));
                        manager.processAsignatura(tipoAlumno, colegio, asignatura);
                        updateProgress(n++, nroAsignaturas);
                    }
                    manager.finish();
                    return Boolean.TRUE;
                }
            };
            task.setOnSucceeded((WorkerStateEvent t1) -> {
                dlg.getDialogStage().hide();
                WindowManager.getInstance().hide(InformeView.this);
            });
            task.setOnFailed((WorkerStateEvent t2) -> {
                dlg.getDialogStage().hide();
                throw new RuntimeException(task.getException());
            });

            dlg.showWorkerProgress(task);
            Executors.newSingleThreadExecutor().execute(task);

        });
        btnCancelar.setOnAction(event -> WindowManager.getInstance().hide(InformeView.this));
        bdAnoEscolar.setMinValue(BigDecimal.valueOf(2010));
        bdAnoEscolar.setMaxValue(BigDecimal.valueOf(2300));
        bdAnoEscolar.setStepwidth(BigDecimal.valueOf(1));
        bdAnoEscolar.setText(String.format("%d", LocalDate.now().getYear()));
    }

    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            final Object entity = list.get(0);
            if (entity instanceof Colegio) {
                final ObservableList<Colegio> pruebas = FXCollections.observableArrayList();
                for (final Object lEntity : list) {
                    pruebas.add((Colegio) lEntity);
                }
                cmbColegio.setItems(pruebas);
            } else if (entity instanceof Asignatura) {
                lstAsignaturas = FXCollections.observableArrayList();
                for (final Object lEntity : list) {
                    lstAsignaturas.add((Asignatura) lEntity);
                }
            } else if (entity instanceof TipoAlumno) {
                final ObservableList<TipoAlumno> lstTipoAlumno = FXCollections.observableArrayList();
                for (final Object lEntity : list) {
                    lstTipoAlumno.add((TipoAlumno) lEntity);
                }
                cmbTipoAlumno.setItems(lstTipoAlumno);
            }
        }
    }

}
