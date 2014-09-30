package cl.eos.view;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.view.editablecells.EditingCellRespuestasEvaluar;
import cl.eos.view.ots.OTAlumnosEvaluarManual;
import cl.eos.view.ots.OTPruebaRendida;

public class EvaluarPruebaView extends AFormView {

	private Prueba prueba;
	@FXML
	private TableView<OTPruebaRendida> tblListadoPruebas;
	@FXML
	private TableColumn<OTPruebaRendida, String> paternoCol;
	@FXML
	private TableColumn<OTPruebaRendida, String> maternoCol;
	@FXML
	private TableColumn<OTPruebaRendida, String> nombresCol;
	@FXML
	private TableColumn<OTPruebaRendida, String> respuestasCol;
	@FXML
	private TableColumn<OTPruebaRendida, Integer> buenasCol;
	@FXML
	private TableColumn<OTPruebaRendida, Integer> malasCol;
	@FXML
	private TableColumn<OTPruebaRendida, Integer> omitidasCol;
	@FXML
	private TableColumn<OTPruebaRendida, Float> notaCol;
	@FXML
	private TableColumn<OTPruebaRendida, Float> puntajeCol;
	@FXML
	private TableColumn<OTPruebaRendida, String> nivelCol;
	@FXML
	private ComboBox<Colegio> cmbColegios;
	@FXML
	private ComboBox<Curso> cmbCursos;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtAsignatura;
	@FXML
	private DatePicker dtpFecha;
	@FXML
	private ListView<EjeTematico> lstEjes;
	@FXML
	private ListView<Habilidad> lstHabilidad;
	@FXML
	private Button btnScanner;
	@FXML
	private Button btnManual;

	private EvaluarManualPruebaView evaluarManualPruebaView;

	public EvaluarPruebaView() {
		setTitle("Evaluar");
	}

	@FXML
	public void initialize() {
		cmbCursos.setDisable(true);
		dtpFecha.setValue(LocalDate.now());
		cmbColegios.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Colegio colegio = cmbColegios.getSelectionModel()
						.getSelectedItem();

				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("tcursoId", prueba.getCurso().getId());
				parameters.put("colegioId", colegio.getId());
				controller.find("Curso.findByTipoColegio", parameters);
			}
		});
		cmbCursos.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Curso curso =  cmbCursos.getSelectionModel().getSelectedItem();
				Colegio colegio = cmbColegios.getSelectionModel().getSelectedItem();
				List<EvaluacionPrueba> listEvaluaciones = prueba.getEvaluaciones();
				EvaluacionPrueba evaPrueba = null;
				if(listEvaluaciones != null &&  !listEvaluaciones.isEmpty())
				{
					for(EvaluacionPrueba evaluacion: listEvaluaciones)
					{

						if(evaluacion.getPrueba().equals(prueba) && evaluacion.getColegio().equals(colegio) && evaluacion.getCurso().equals(curso))
						{
							evaPrueba = evaluacion;
							break;
						}
					}
				}
				if(evaPrueba == null)
				{
					//Tengo que crear la evaluacion Prueba.
					evaPrueba = new EvaluacionPrueba();
					evaPrueba.setColegio(colegio);
					evaPrueba.setCurso(curso);
					evaPrueba.setPrueba(prueba);
				}
			}
		});
		btnScanner.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// ProcesadorPrueba procesador = new ProcesadorPrueba();

			}
		});
		definirTablaListadoPruebas();
		
		
	}

	private void definirTablaListadoPruebas() {
		paternoCol
				.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, String>(
						"paterno"));
		maternoCol
				.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, String>(
						"materno"));
		nombresCol
				.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, String>(
						"nombres"));
		buenasCol
				.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, Integer>(
						"buenas"));
		malasCol.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, Integer>(
				"malas"));
		omitidasCol
				.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, Integer>(
						"omitidas"));
		notaCol.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, Float>(
				"nota"));
		puntajeCol
				.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, Float>(
						"puntaje"));
		nivelCol.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, String>(
				"nivel"));
		tblListadoPruebas.setEditable(true);

		respuestasCol
				.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, String>(
						"respuestas"));
		respuestasCol.setEditable(true);
		respuestasCol
				.setCellFactory(new Callback<TableColumn<OTPruebaRendida, String>, TableCell<OTPruebaRendida, String>>() {

					@Override
					public TableCell<OTPruebaRendida, String> call(
							TableColumn<OTPruebaRendida, String> param) {
						return new EditingCellRespuestasEvaluar(prueba);
					}
				});

	}

	@Override
	public void onFound(IEntity entity) {
		if (entity instanceof Prueba) {
			prueba = (Prueba) entity;
			txtName.setText(prueba.getName());
			txtAsignatura.setText(prueba.getAsignatura().getName());
			List<RespuestasEsperadasPrueba> respuestas = prueba.getRespuestas();
			ObservableList<EjeTematico> lEjes = FXCollections
					.observableArrayList();
			ObservableList<Habilidad> lHabilidad = FXCollections
					.observableArrayList();

			for (RespuestasEsperadasPrueba respuesta : respuestas) {
				if (!lEjes.contains(respuesta.getEjeTematico())) {
					lEjes.add(respuesta.getEjeTematico());
				}
				if (!lHabilidad.contains(respuesta.getHabilidad())) {
					lHabilidad.add(respuesta.getHabilidad());
				}
			}
			lstEjes.setItems(lEjes);
			lstHabilidad.setItems(lHabilidad);
		}

	}

	@Override
	public void onDataArrived(List<Object> list) {
		if (list != null && !list.isEmpty()) {
			Object entity = list.get(0);
			if (entity instanceof Colegio) {
				ObservableList<Colegio> oList = FXCollections
						.observableArrayList();
				for (Object iEntity : list) {
					oList.add((Colegio) iEntity);
				}
				cmbColegios.setItems(oList);
			} else if (entity instanceof Curso) {
				ObservableList<Curso> oList = FXCollections
						.observableArrayList();
				for (Object iEntity : list) {
					oList.add((Curso) iEntity);
				}
				cmbCursos.setItems(oList);
				cmbCursos.setDisable(false);
			}

		}
	}

}
