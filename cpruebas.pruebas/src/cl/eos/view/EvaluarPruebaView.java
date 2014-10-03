package cl.eos.view;

import java.time.LocalDate;
import java.util.ArrayList;
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
import javafx.scene.control.TableColumn.CellEditEvent;
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
import cl.eos.persistence.models.Profesor;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RangoEvaluacion;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.util.Utils;
import cl.eos.view.editablecells.EditingCellRespuestasEvaluar;
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
	private TableColumn<OTPruebaRendida, Integer> puntajeCol;
	@FXML
	private TableColumn<OTPruebaRendida, String> nivelCol;
	@FXML
	private ComboBox<Colegio> cmbColegios;
	@FXML
	private ComboBox<Curso> cmbCursos;
	@FXML
	private ComboBox<Profesor> cmbProfesor;
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
		cmbCursos.setOnAction(new EHandlerCmbCurso());
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
				.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, Integer>(
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
		respuestasCol
				.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<OTPruebaRendida, String>>() {
					@Override
					public void handle(
							CellEditEvent<OTPruebaRendida, String> event) {
						// Aqui debo validar el resultado de la prueba.
						String value = event.getNewValue();
						PruebaRendida pRendida = event.getRowValue()
								.getPruebaRendida();
						if (value != null && !value.isEmpty()) {
							evaluar(value, pRendida);
						}
					}
				});
	}

	protected void evaluar(String value, PruebaRendida pRendida) {
		List<RespuestasEsperadasPrueba> respEsperadas = prueba.getRespuestas();
		
		int nMax = Math.min(value.length(), respEsperadas.size());
		pRendida.setOmitidas(Math.abs(value.length() - respEsperadas.size()));
		pRendida.setBuenas(0);
		pRendida.setMalas(0);
		for (int n = 0; n < nMax; n++) {
			RespuestasEsperadasPrueba resp = respEsperadas.get(n);
			String userResp = value.substring(n, n + 1);
			String validResp = resp.getRespuesta();
			if (userResp.toUpperCase().equals("*")) {
				pRendida.setOmitidas(pRendida.getOmitidas() + 1);
			} else if (userResp.toUpperCase().equals(validResp.toUpperCase())) {
				pRendida.setBuenas(pRendida.getBuenas() + 1);
			} else {
				pRendida.setMalas(pRendida.getMalas() + 1);
			}
		}
		int nroPreguntas = respEsperadas.size();
		float porcDificultad = prueba.getExigencia() == null ? 60f:prueba.getExigencia();
		float notaMinima = 1.0f;
		pRendida.setNota(Utils.getNota(nroPreguntas, porcDificultad, pRendida.getBuenas(), notaMinima));

		float total = pRendida.getBuenas() + pRendida.getMalas() + pRendida.getOmitidas();
		float porcentaje = ((float)pRendida.getBuenas()) / total * 100f;
		for(RangoEvaluacion rango: prueba.getNivelEvaluacion().getRangos())
		{
			if(porcentaje >= rango.getMinimo() && porcentaje <= rango.getMaximo())
			{
				
			}
				
		}


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
			} else if (entity instanceof Profesor) {
				ObservableList<Profesor> oList = FXCollections
						.observableArrayList();
				for (Object iEntity : list) {
					oList.add((Profesor) iEntity);
				}
				cmbProfesor.setItems(oList);
			}
		}
	}

	private class EHandlerCmbCurso implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			Curso curso = cmbCursos.getSelectionModel().getSelectedItem();
			Colegio colegio = cmbColegios.getSelectionModel().getSelectedItem();
			Profesor profesor = cmbProfesor.getSelectionModel()
					.getSelectedItem();
			List<EvaluacionPrueba> listEvaluaciones = prueba.getEvaluaciones();
			EvaluacionPrueba evalPrueba = null;
			if (listEvaluaciones != null && !listEvaluaciones.isEmpty()) {
				for (EvaluacionPrueba evaluacion : listEvaluaciones) {
					if (evaluacion.getPrueba().equals(prueba)
							&& evaluacion.getColegio().equals(colegio)
							&& evaluacion.getCurso().equals(curso)) {
						evalPrueba = evaluacion;
						break;
					}
				}
			}
			if (evalPrueba == null) {
				ObservableList<OTPruebaRendida> oList = FXCollections
						.observableArrayList();
				// Tengo que crear la evaluacion Prueba.
				evalPrueba = new EvaluacionPrueba();
				evalPrueba.setColegio(colegio);
				evalPrueba.setCurso(curso);
				evalPrueba.setPrueba(prueba);
				evalPrueba.setProfesor(profesor);
				evalPrueba.setFecha(dtpFecha.getValue().toEpochDay());
				evalPrueba.setPruebasRendidas(new ArrayList<PruebaRendida>());
				for (Alumno alumno : curso.getAlumnos()) {
					PruebaRendida pRendida = new PruebaRendida();
					pRendida.setAlumno(alumno);
					evalPrueba.getPruebasRendidas().add(pRendida);
					oList.add(new OTPruebaRendida(pRendida));
				}

				tblListadoPruebas.setItems(oList);
			} else {

			}
		}
	}
}
