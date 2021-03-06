package colegio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import cl.eos.imp.view.AFormView;
import cl.eos.imp.view.ProgressForm;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.TipoAlumno;
import cl.eos.persistence.util.Comparadores;
import cl.eos.util.ExcelSheetWriterObj;
import colegio.util.CursoEjeHabilidad;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
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

public class ResumenColegioXAlumnoEjeHabilidadView extends AFormView implements EventHandler<ActionEvent> {

	private static Logger log = Logger.getLogger(ResumenColegioXAlumnoEjeHabilidadView.class.getName());
	private static final String ASIGNATURA_ID = "idAsignatura";
	private static final String COLEGIO_ID = "idColegio";
	@FXML
	private TabPane tabPane;
	@FXML
	private ComboBox<Colegio> cmbColegios;
	@FXML
	private ComboBox<Asignatura> cmbAsignatura;
	@FXML
	private ComboBox<TipoAlumno> cmbTipoAlumno;
	@FXML
	private Button btnReportes;
	@FXML
	private Label lblColegio;
	@FXML
	private Label lblTitulo;
	@FXML
	private MenuItem mnuExportarGeneral;

	private Map<String, Object> parameters = new HashMap<String, Object>();
	private ObservableList<Curso> cursoList;
	private ObservableList<EvaluacionPrueba> evaluacionesPrueba;
	private ArrayList<CursoEjeHabilidad> lstCursoEjeHabilidad;

	public ResumenColegioXAlumnoEjeHabilidadView() {
		setTitle("Resumen Colegio/Ejes Temáticos/Habilidades x Alumno");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handle(ActionEvent event) {
		Object source = event.getSource();
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
				List<TableView<? extends Object>> listaTablas = new ArrayList<>();
				for (CursoEjeHabilidad curso : lstCursoEjeHabilidad) {
					listaTablas.add((TableView<? extends Object>) curso.getTblAlumnos());
				}
				ExcelSheetWriterObj.convertirDatosALibroDeExcel(listaTablas);
			}
		}
	}

	private void handleColegios() {
		Colegio colegio = cmbColegios.getSelectionModel().getSelectedItem();
		if (colegio != null) {
			parameters.put(COLEGIO_ID, colegio.getId());
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("colegioId", colegio.getId());
			lblTitulo.setText(colegio.getName());
			controller.find("Curso.findByColegio", param);
			clearContent();
		}
	}

	private void handleAsignatura() {
		Asignatura asignatura = cmbAsignatura.getSelectionModel().getSelectedItem();
		if (asignatura != null) {
			parameters.put(ASIGNATURA_ID, asignatura.getId());
			clearContent();
		}
	}

	private void handleReportes() {
		if (!parameters.isEmpty() && parameters.containsKey(COLEGIO_ID) && parameters.containsKey(ASIGNATURA_ID)) {

			controller.find("EvaluacionPrueba.findEvaluacionByColegioAsig", parameters, this);
		}
	}

	@FXML
	public void initialize() {
		inicializaComponentes();
	}

	private void inicializaComponentes() {
		cmbColegios.setOnAction(this);
		cmbAsignatura.setOnAction(this);
		btnReportes.setOnAction(this);
		mnuExportarGeneral.setOnAction(this);
	}

	@Override
	public void onDataArrived(List<Object> list) {
		if (list != null && !list.isEmpty()) {
			Object entity = list.get(0);
			if (entity instanceof Colegio) {
				ObservableList<Colegio> oList = FXCollections.observableArrayList();
				for (Object iEntity : list) {
					oList.add((Colegio) iEntity);
				}
				cmbColegios.setItems(oList);
			}
			if (entity instanceof Asignatura) {
				ObservableList<Asignatura> oList = FXCollections.observableArrayList();
				for (Object iEntity : list) {
					oList.add((Asignatura) iEntity);
				}
				cmbAsignatura.setItems(oList);
			}
			if (entity instanceof Curso) {
				cursoList = FXCollections.observableArrayList();
				for (Object iEntity : list) {
					cursoList.add((Curso) iEntity);
				}
				FXCollections.sort(cursoList, Comparadores.comparaResumeCurso());
			}
			if (entity instanceof TipoAlumno) {
				ObservableList<TipoAlumno> tAlumnoList = FXCollections.observableArrayList();
				for (Object iEntity : list) {
					tAlumnoList.add((TipoAlumno) iEntity);
				}
				cmbTipoAlumno.setItems(tAlumnoList);
			}
			if (entity instanceof EvaluacionPrueba) {
				evaluacionesPrueba = FXCollections.observableArrayList();
				for (Object object : list) {
					EvaluacionPrueba evaluacion = (EvaluacionPrueba) object;
					evaluacionesPrueba.add(evaluacion);
				}
				generarReporte();
			}
		} else if (list != null && list.isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("No hay registros.");
			alert.setHeaderText(this.getName());
			alert.setContentText("No se ha encontrado registros para la consulta.");
			alert.showAndWait();
		}
	}

	/**
	 * Aqui se llenan las tablas con los valores correspondientes.<br>
	 * 1) Se obtienen los ejes tematicos de todas las pruebas.<br>
	 * 2) Se obtienen las habilidades de todas las pruebas.<br>
	 * 3) Se obtienen los porcentajes de aprobacion de cada colegio con respecto a
	 * cada eje y habilidad.
	 */
	private void generarReporte() {

		if (evaluacionesPrueba == null)
			return;
		
		clearContent();
		FXCollections.sort(evaluacionesPrueba, Comparadores.comparaEvaluacionPruebaXCurso());
		ProgressForm pForm = new ProgressForm();
		pForm.title("Procesando Cursos");
		pForm.message("Esto tomará algunos segundos.");

		Task<ArrayList<CursoEjeHabilidad>> task = new Task<ArrayList<CursoEjeHabilidad>>() {
			@Override
			protected ArrayList<CursoEjeHabilidad> call() throws Exception {
				ArrayList<CursoEjeHabilidad> lst = new ArrayList<>();
				int n = 1;
				int total = evaluacionesPrueba.size();
				for (EvaluacionPrueba eval : evaluacionesPrueba) {
					if (eval.getCurso() != null) {
						updateMessage(String.format("Prcesando %s", eval.getCurso().getName()));
						updateProgress(n++, total);
						CursoEjeHabilidad curso = new CursoEjeHabilidad(eval,
								cmbTipoAlumno.getSelectionModel().getSelectedItem());
						Runnable r = () -> {
							Tab tab = new Tab(eval.getCurso().getName());
							tab.setContent(curso.getTblAlumnos());
							tabPane.getTabs().add(tab);
						};
						Platform.runLater(r);

						lst.add(curso);
					} else {
						log.severe(eval.getName() + " Sin colegio");
					}
				}
				return lst;
			}
		};
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				lstCursoEjeHabilidad = task.getValue();
				pForm.getDialogStage().hide();
			}
		});
		task.setOnFailed(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				log.severe("Al generar la prueba se ha producido el siguiente error:" + event.getEventType().toString());
				pForm.getDialogStage().hide();
			}
		});

		pForm.showWorkerProgress(task);
		Executors.newSingleThreadExecutor().execute(task);

		Executors.newSingleThreadExecutor().execute(task);

	}

	private void clearContent() {
		tabPane.getTabs().clear();
		if (lstCursoEjeHabilidad != null && !lstCursoEjeHabilidad.isEmpty()) {
			for (CursoEjeHabilidad curso : lstCursoEjeHabilidad) {
				curso.getTblAlumnos().getItems().clear();
				curso.setTblAlumnos(null);
			}
			lstCursoEjeHabilidad.clear();
		}
	}
}
