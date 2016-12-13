package colegio.nivel.sinHacer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import cl.eos.imp.view.AFormView;
import cl.eos.imp.view.ProgressForm;
import cl.eos.persistence.models.SAsignatura;
import cl.eos.persistence.models.SColegio;
import cl.eos.persistence.models.SCurso;
import cl.eos.persistence.models.SEvaluacionPrueba;
import cl.eos.persistence.models.STipoAlumno;
import cl.eos.persistence.util.Comparadores;
import cl.eos.util.ExcelSheetWriterObj;
import colegio.nivel.util.Nivel_CursoEjeHabilidad;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;

public class Nivel_ResumenColegioXAlumnoEjeHabilidadView extends AFormView implements EventHandler<ActionEvent> {

    private static Logger log = Logger.getLogger(Nivel_ResumenColegioXAlumnoEjeHabilidadView.class.getName());
    private static final String ASIGNATURA_ID = "idAsignatura";
    private static final String COLEGIO_ID = "idColegio";
    @FXML
    private TabPane tabPane;
    @FXML
    private ComboBox<SColegio> cmbColegios;
    @FXML
    private ComboBox<SAsignatura> cmbAsignatura;
    @FXML
    private ComboBox<STipoAlumno> cmbTipoAlumno;
    @FXML
    private Button btnReportes;
    @FXML
    private Label lblColegio;
    @FXML
    private Label lblTitulo;
    @FXML
    private MenuItem mnuExportarGeneral;

    private final Map<String, Object> parameters = new HashMap<String, Object>();
    private ObservableList<SCurso> cursoList;
    private ObservableList<SEvaluacionPrueba> evaluacionesPrueba;
    private ArrayList<Nivel_CursoEjeHabilidad> lstCursoEjeHabilidad;

    public Nivel_ResumenColegioXAlumnoEjeHabilidadView() {
        setTitle("Resumen SColegio/Ejes Temáticos/Habilidades x SAlumno");
    }

    private void clearContent() {
        tabPane.getTabs().clear();
        if (lstCursoEjeHabilidad != null && !lstCursoEjeHabilidad.isEmpty()) {
            for (final Nivel_CursoEjeHabilidad curso : lstCursoEjeHabilidad) {
                curso.getTblAlumnos().getItems().clear();
                curso.setTblAlumnos(null);
            }
            lstCursoEjeHabilidad.clear();
        }
    }

    /**
     * Aqui se llenan las tablas con los valores correspondientes.<br>
     * 1) Se obtienen los ejes tematicos de todas las pruebas.<br>
     * 2) Se obtienen las habilidades de todas las pruebas.<br>
     * 3) Se obtienen los porcentajes de aprobacion de cada colegio con respecto
     * a cada eje y habilidad.
     */
    private void generarReporte() {

        if (evaluacionesPrueba == null)
            return;

        clearContent();
        FXCollections.sort(evaluacionesPrueba, Comparadores.comparaEvaluacionPruebaXCurso());
        final ProgressForm pForm = new ProgressForm();
        pForm.title("Procesando Cursos");
        pForm.message("Esto tomará algunos segundos.");

        final Task<ArrayList<Nivel_CursoEjeHabilidad>> task = new Task<ArrayList<Nivel_CursoEjeHabilidad>>() {
            @Override
            protected ArrayList<Nivel_CursoEjeHabilidad> call() throws Exception {
                final ArrayList<Nivel_CursoEjeHabilidad> lst = new ArrayList<>();
                int n = 1;
                final int total = evaluacionesPrueba.size();
                for (final SEvaluacionPrueba eval : evaluacionesPrueba) {
                    if (eval.getCurso() != null) {
                        updateMessage(String.format("Prcesando %s", eval.getCurso().getName()));
                        updateProgress(n++, total);
                        final Nivel_CursoEjeHabilidad curso = new Nivel_CursoEjeHabilidad(eval,
                                cmbTipoAlumno.getSelectionModel().getSelectedItem());
                        final Runnable r = () -> {
                            final Tab tab = new Tab(eval.getCurso().getName());
                            tab.setContent(curso.getTblAlumnos());
                            tabPane.getTabs().add(tab);
                        };
                        Platform.runLater(r);

                        lst.add(curso);
                    } else {
                        Nivel_ResumenColegioXAlumnoEjeHabilidadView.log.severe(eval.getName() + " Sin colegio");
                    }
                }
                return lst;
            }
        };
        task.setOnSucceeded(event -> {
            lstCursoEjeHabilidad = task.getValue();
            pForm.getDialogStage().hide();
        });
        task.setOnFailed(event -> {
            Nivel_ResumenColegioXAlumnoEjeHabilidadView.log.severe(event.getEventType().toString());
            pForm.getDialogStage().hide();
        });

        pForm.showWorkerProgress(task);
        Executors.newSingleThreadExecutor().execute(task);

        Executors.newSingleThreadExecutor().execute(task);

    }

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ActionEvent event) {
        final Object source = event.getSource();
        if (source == cmbColegios) {
            handleColegios();
        }
        if (source == cmbAsignatura) {
            handleAsignatura();
        }
        if (source == btnReportes) {
            handleReportes();
        }

        if (source == mnuExportarGeneral) {

            if (lstCursoEjeHabilidad != null && !lstCursoEjeHabilidad.isEmpty()) {
                final List<TableView<? extends Object>> listaTablas = new ArrayList<>();
                for (final Nivel_CursoEjeHabilidad curso : lstCursoEjeHabilidad) {
                    listaTablas.add(curso.getTblAlumnos());
                }
                ExcelSheetWriterObj.convertirDatosALibroDeExcel(listaTablas);
            }
        }
    }

    private void handleAsignatura() {
        final SAsignatura asignatura = cmbAsignatura.getSelectionModel().getSelectedItem();
        if (asignatura != null) {
            parameters.put(Nivel_ResumenColegioXAlumnoEjeHabilidadView.ASIGNATURA_ID, asignatura.getId());
            clearContent();
        }
    }

    private void handleColegios() {
        final SColegio colegio = cmbColegios.getSelectionModel().getSelectedItem();
        if (colegio != null) {
            parameters.put(Nivel_ResumenColegioXAlumnoEjeHabilidadView.COLEGIO_ID, colegio.getId());
            final Map<String, Object> param = new HashMap<String, Object>();
            param.put("colegioId", colegio.getId());
            lblTitulo.setText(colegio.getName());
            controller.find("SCurso.findByColegio", param);
            clearContent();
        }
    }

    private void handleReportes() {
        if (!parameters.isEmpty() && parameters.containsKey(Nivel_ResumenColegioXAlumnoEjeHabilidadView.COLEGIO_ID)
                && parameters.containsKey(Nivel_ResumenColegioXAlumnoEjeHabilidadView.ASIGNATURA_ID)) {

            controller.find("SEvaluacionPrueba.findEvaluacionByColegioAsig", parameters, this);
        }
    }

    private void inicializaComponentes() {
        cmbColegios.setOnAction(this);
        cmbAsignatura.setOnAction(this);
        btnReportes.setOnAction(this);
        mnuExportarGeneral.setOnAction(this);
    }

    @FXML
    public void initialize() {
        inicializaComponentes();
    }

    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            final Object entity = list.get(0);
            if (entity instanceof SColegio) {
                final ObservableList<SColegio> oList = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    oList.add((SColegio) iEntity);
                }
                cmbColegios.setItems(oList);
            }
            if (entity instanceof SAsignatura) {
                final ObservableList<SAsignatura> oList = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    oList.add((SAsignatura) iEntity);
                }
                cmbAsignatura.setItems(oList);
            }
            if (entity instanceof SCurso) {
                cursoList = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    cursoList.add((SCurso) iEntity);
                }
                FXCollections.sort(cursoList, Comparadores.comparaResumeCurso());
            }
            if (entity instanceof STipoAlumno) {
                final ObservableList<STipoAlumno> tAlumnoList = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    tAlumnoList.add((STipoAlumno) iEntity);
                }
                cmbTipoAlumno.setItems(tAlumnoList);
            }
            if (entity instanceof SEvaluacionPrueba) {
                evaluacionesPrueba = FXCollections.observableArrayList();
                for (final Object object : list) {
                    final SEvaluacionPrueba evaluacion = (SEvaluacionPrueba) object;
                    evaluacionesPrueba.add(evaluacion);
                }
                generarReporte();
            }
        } else if (list != null && list.isEmpty()) {
            final Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("No hay registros.");
            alert.setHeaderText(getName());
            alert.setContentText("No se ha encontrado registros para la consulta.");
            alert.showAndWait();
        }
    }
}
