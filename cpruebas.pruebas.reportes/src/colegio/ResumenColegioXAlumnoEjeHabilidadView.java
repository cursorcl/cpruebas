package colegio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import cl.eos.imp.view.AFormView;
import cl.eos.imp.view.ProgressForm;
import cl.eos.persistence.util.Comparadores;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.MapBuilder;
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
	private static final String ASIGNATURA_ID = "asignatura_id";
	private static final String COLEGIO_ID = "colegio_id";
	@FXML
	private TabPane tabPane;
	@FXML
	private ComboBox<R_Colegio> cmbColegios;
	@FXML
	private ComboBox<R_Asignatura> cmbAsignatura;
	@FXML
	private ComboBox<R_TipoAlumno> cmbTipoAlumno;
	@FXML
	private Button btnReportes;
	@FXML
	private Label lblColegio;
	@FXML
	private Label lblTitulo;
	@FXML
	private MenuItem mnuExportarGeneral;

	private Map<String, Object> parameters = new HashMap<String, Object>();
	private ObservableList<R_Curso> cursoList;
	private ObservableList<R_EvaluacionPrueba> evaluacionesPrueba;
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
		R_Colegio colegio = cmbColegios.getSelectionModel().getSelectedItem();
		if (colegio != null) {
			parameters.put(COLEGIO_ID, colegio.getId());
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("colegioId", colegio.getId());
			lblTitulo.setText(colegio.getName());
			controller.findByParam(R_Curso.class, param, this);
			clearContent();
		}
	}

	private void handleAsignatura() {
		R_Asignatura asignatura = cmbAsignatura.getSelectionModel().getSelectedItem();
		if (asignatura != null) {
			parameters.put(ASIGNATURA_ID, asignatura.getId());
			clearContent();
		}
	}

	private void handleReportes() {
		if (!parameters.isEmpty() && parameters.containsKey(COLEGIO_ID) && parameters.containsKey(ASIGNATURA_ID)) {

			controller.findByParam(R_EvaluacionPrueba.class, parameters, this);
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
			if (entity instanceof R_Colegio) {
				ObservableList<R_Colegio> oList = FXCollections.observableArrayList();
				for (Object iEntity : list) {
					oList.add((R_Colegio) iEntity);
				}
				cmbColegios.setItems(oList);
			}
			if (entity instanceof R_Asignatura) {
				ObservableList<R_Asignatura> oList = FXCollections.observableArrayList();
				for (Object iEntity : list) {
					oList.add((R_Asignatura) iEntity);
				}
				cmbAsignatura.setItems(oList);
			}
			if (entity instanceof R_Curso) {
				cursoList = FXCollections.observableArrayList();
				for (Object iEntity : list) {
					cursoList.add((R_Curso) iEntity);
				}
				FXCollections.sort(cursoList, Comparadores.comparaResumeCurso());
			}
			if (entity instanceof R_TipoAlumno) {
				ObservableList<R_TipoAlumno> tAlumnoList = FXCollections.observableArrayList();
				for (Object iEntity : list) {
					tAlumnoList.add((R_TipoAlumno) iEntity);
				}
				cmbTipoAlumno.setItems(tAlumnoList);
			}
			if (entity instanceof R_EvaluacionPrueba) {
				evaluacionesPrueba = FXCollections.observableArrayList();
				for (Object object : list) {
					R_EvaluacionPrueba evaluacion = (R_EvaluacionPrueba) object;
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
				// Asocia las respuestas esperadas a cada prueba.
				Map<Long, List<R_RespuestasEsperadasPrueba>> mapREsperadas =  new HashMap<>();
				for (R_EvaluacionPrueba eval : evaluacionesPrueba) {
				    R_Curso rCurso =  controller.findByIdSynchro(R_Curso.class, eval.getCurso_id());
				    List<R_RespuestasEsperadasPrueba> respEsperadas =  mapREsperadas.get(eval.getPrueba_id());
				    if(respEsperadas == null)
				    {
				      Map<String, Object> parameters =  MapBuilder.<String, Object> unordered().put("prueba_id", eval.getPrueba_id()).build();
				      respEsperadas = controller.findByParamsSynchro(R_RespuestasEsperadasPrueba.class, parameters);
				      mapREsperadas.put(eval.getPrueba_id(), respEsperadas);
				    }
					if (rCurso != null) {
						updateMessage(String.format("Procesando curso %s", rCurso.getName()));
						updateProgress(n++, total);
						
						CursoEjeHabilidad curso = new CursoEjeHabilidad(eval, cmbTipoAlumno.getSelectionModel().getSelectedItem(), respEsperadas);
						Runnable r = () -> {
							Tab tab = new Tab(rCurso.getName());
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

		//Executors.newSingleThreadExecutor().execute(task);

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
