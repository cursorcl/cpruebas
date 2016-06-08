package comunal;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTPreguntasEjes;
import cl.eos.ot.OTPreguntasEvaluacion;
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.EvaluacionEjeTematico;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.persistence.models.TipoAlumno;
import cl.eos.persistence.models.TipoColegio;
import cl.eos.persistence.util.Comparadores;
import cl.eos.util.ExcelSheetWriterObj;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;

public class ComparativoComunalEjeView extends AFormView implements EventHandler<ActionEvent> {

	private static Logger log = Logger.getLogger(ComparativoComunalEjeView.class);
	private NumberFormat formatter = new DecimalFormat("#0.00");
	@FXML
	private Label lblTitulo;
	@FXML
	private MenuItem mnuExportarEjesTematicos;
	@FXML
	private MenuItem mnuExportarEvaluacion;
	@FXML
	private TableView<ObservableList<String>> tblEjesTematicos;
	@FXML
	private TableView<ObservableList<String>> tblEvaluacionEjesTematicos;

	private HashMap<EjeTematico, HashMap<String, OTPreguntasEjes>> mapaEjesTematicos;

	private Map<Long, EvaluacionEjeTematico> mEvaluaciones;

	private Map<EvaluacionEjeTematico, HashMap<String, OTPreguntasEvaluacion>> mapEvaAlumnos = null;

	@FXML
	private ComboBox<TipoAlumno> cmbTipoAlumno;
	@FXML
	private Button btnGenerar;
	@FXML
	private ComboBox<TipoColegio> cmbTipoColegio;
	
	long tipoAlumno = Constants.PIE_ALL;
	long tipoColegio = Constants.TIPO_COLEGIO_ALL;
	
	private ArrayList<String> titulosColumnas;
	private Prueba prueba;
	private boolean llegaOnFound = false;
	private boolean llegaTipoAlumno = false;
	private boolean llegaEvaluacionEjeTematico = false;
	private boolean llegaTipoColegio;

	@FXML
	public void initialize() {
		this.setTitle("Resumen comparativo comunal ejes temáticas");
		tblEjesTematicos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tblEvaluacionEjesTematicos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		mnuExportarEjesTematicos.setOnAction(this);
		mnuExportarEvaluacion.setOnAction(this);
		btnGenerar.setOnAction(this);
		cmbTipoAlumno.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(cmbTipoAlumno.getSelectionModel() == null)
					return;
				tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedItem().getId();
			}
		});
		
		cmbTipoColegio.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(cmbTipoColegio.getSelectionModel().getSelectedItem() == null)
					return;
				tipoColegio	= cmbTipoColegio.getSelectionModel().getSelectedItem().getId();
			}
		});
	
	}

	@Override
	public void onFound(IEntity entity) {
		if (entity instanceof Prueba) {
			prueba = (Prueba) entity;
			llegaOnFound = true;
		}
		procesaDatosReporte();
	}

	@Override
	public void onDataArrived(List<Object> list) {
		if (list != null && !list.isEmpty()) {
			Object entity = list.get(0);
			if (entity instanceof EvaluacionEjeTematico) {
				llegaEvaluacionEjeTematico = true;
				mEvaluaciones = new HashMap<>();
				for (Object object : list) {
					EvaluacionEjeTematico eje = (EvaluacionEjeTematico) object;
					mEvaluaciones.put(eje.getId(), eje);
				}
			}
			if (entity instanceof TipoAlumno) {
				ObservableList<TipoAlumno> tAlumnoList = FXCollections.observableArrayList();
				llegaTipoAlumno = true;
				for (Object iEntity : list) {
					tAlumnoList.add((TipoAlumno) iEntity);
				}
				cmbTipoAlumno.setItems(tAlumnoList);
				cmbTipoAlumno.getSelectionModel().select((int) Constants.PIE_ALL);
			}
			if (entity instanceof TipoColegio) {
				ObservableList<TipoColegio> tColegioList = FXCollections.observableArrayList();
				llegaTipoColegio = true;
				for (Object iEntity : list) {
					tColegioList.add((TipoColegio) iEntity);
				}
				cmbTipoColegio.setItems(tColegioList);
				TipoColegio tColegio = new TipoColegio();
				tColegio.setId(Constants.TIPO_COLEGIO_ALL);
				cmbTipoColegio.getSelectionModel().select(tColegio);
			}			
		}
		procesaDatosReporte();
	}

	private void procesaDatosReporte() {
		if (llegaEvaluacionEjeTematico && llegaTipoAlumno && llegaOnFound && llegaTipoColegio) {
			llenarDatosTabla();
			desplegarDatosEjesTematicos();
			desplegarDatosEvaluaciones();
		}
	}

	private void llenarDatosTabla() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(prueba.getAsignatura());
		buffer.append(" ");
		buffer.append(prueba.getCurso());
		lblTitulo.setText(buffer.toString());

		mapaEjesTematicos = new HashMap<>();
		mapEvaAlumnos = new HashMap<>();
		HashMap<String, OTPreguntasEjes> mapaColegios;

		List<EvaluacionPrueba> listaEvaluaciones = prueba.getEvaluaciones();

		creacionColumnasEjesTematicos(listaEvaluaciones);
		creacionColumnasEvaluaciones(listaEvaluaciones);

		for (EvaluacionPrueba evaluacionPrueba : listaEvaluaciones) {
			String colegioCurso = evaluacionPrueba.getColegiocurso();

			List<PruebaRendida> pruebasRendidas = evaluacionPrueba.getPruebasRendidas();
			List<RespuestasEsperadasPrueba> respuestasEsperadas = prueba.getRespuestas();

			for (PruebaRendida pruebaRendida : pruebasRendidas) {
				Alumno alumno = pruebaRendida.getAlumno();
				if (tipoAlumno != Constants.PIE_ALL && tipoAlumno != alumno.getTipoAlumno().getId()) 
					continue;
				if(tipoColegio != Constants.TIPO_COLEGIO_ALL && tipoColegio !=  alumno.getColegio().getTipoColegio().getId())
					continue;
				
				generaDatosEvaluacion(pruebaRendida, colegioCurso);

				String respuesta = pruebaRendida.getRespuestas().toUpperCase();

				if (alumno == null) {
					log.error(String.format("NO EXISTE ALUMNO: %s %s", colegioCurso, respuesta));
					continue; // Caso que el alumno sea nulo.
				}
				log.info(String.format("%s %s %s %s %s %s", colegioCurso, alumno.getRut(), alumno.getName(), alumno.getPaterno(),
						alumno.getMaterno(), respuesta));

				if (respuesta == null || respuesta.length() < prueba.getNroPreguntas()) {
					informarProblemas(colegioCurso, alumno, respuesta);
					continue;
				}
				char[] cRespuesta = respuesta.toUpperCase().toCharArray();

				for (RespuestasEsperadasPrueba respuestasEsperadasPrueba : respuestasEsperadas) {
					if (respuestasEsperadasPrueba.isAnulada()) {
						continue;
					}
					EjeTematico ejeTematico = respuestasEsperadasPrueba.getEjeTematico();
					Integer numeroPreg = respuestasEsperadasPrueba.getNumero();
					if (mapaEjesTematicos.containsKey(ejeTematico)) {
						HashMap<String, OTPreguntasEjes> mapa = mapaEjesTematicos.get(ejeTematico);

						if (mapa.containsKey(colegioCurso)) {
							OTPreguntasEjes otPregunta = mapa.get(colegioCurso);

							if (cRespuesta[numeroPreg - 1] == respuestasEsperadasPrueba.getRespuesta()
									.toCharArray()[0]) {
								otPregunta.setBuenas(otPregunta.getBuenas() + 1);
							}
							otPregunta.setTotal(otPregunta.getTotal() + 1);
						} else {
							OTPreguntasEjes otPreguntas = new OTPreguntasEjes();
							otPreguntas.setEjeTematico(ejeTematico);
							if (cRespuesta[numeroPreg - 1] == respuestasEsperadasPrueba.getRespuesta()
									.toCharArray()[0]) {
								otPreguntas.setBuenas(1);
							} else {
								otPreguntas.setBuenas(0);
							}
							otPreguntas.setTotal(1);

							mapa.put(colegioCurso, otPreguntas);
						}
					} else {
						OTPreguntasEjes otPreguntas = new OTPreguntasEjes();
						otPreguntas.setEjeTematico(ejeTematico);
						if (cRespuesta[numeroPreg - 1] == respuestasEsperadasPrueba.getRespuesta().toCharArray()[0]) {
							otPreguntas.setBuenas(1);
						} else {
							otPreguntas.setBuenas(0);
						}
						otPreguntas.setTotal(1);

						mapaColegios = new HashMap<>();
						mapaColegios.put(colegioCurso, otPreguntas);
						mapaEjesTematicos.put(ejeTematico, mapaColegios);
					}
				}
			}

		}
	}

	private void informarProblemas(String colegioCurso, Alumno al, String respuesta) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Alumno con respuestas incompletas.");
		alert.setHeaderText(String.format("%s/%s", colegioCurso, al.toString()));
		alert.setContentText(String.format("La respuesta [%s] es incompleta", respuesta));
		alert.showAndWait();

	}

	private void generaDatosEvaluacion(PruebaRendida pruebaRendida, String colegioCurso) {

		HashMap<String, OTPreguntasEvaluacion> mapaOT;

		Float pBuenas = pruebaRendida.getPbuenas();
		for (Entry<Long, EvaluacionEjeTematico> mEvaluacion : mEvaluaciones.entrySet()) {

			EvaluacionEjeTematico evaluacionAl = mEvaluacion.getValue();
			if (mapEvaAlumnos.containsKey(evaluacionAl)) {
				HashMap<String, OTPreguntasEvaluacion> evaluacion = mapEvaAlumnos.get(evaluacionAl);
				if (evaluacion.containsKey(colegioCurso)) {
					OTPreguntasEvaluacion otPreguntas = evaluacion.get(colegioCurso);

					if (pBuenas >= evaluacionAl.getNroRangoMin() && pBuenas <= evaluacionAl.getNroRangoMax()) {
						otPreguntas.setAlumnos(otPreguntas.getAlumnos() + 1);
					}
				} else {

					OTPreguntasEvaluacion pregunta = new OTPreguntasEvaluacion();
					if (pBuenas >= evaluacionAl.getNroRangoMin() && pBuenas <= evaluacionAl.getNroRangoMax()) {
						pregunta.setAlumnos(1);
					} else {
						pregunta.setAlumnos(0);
					}
					pregunta.setEvaluacion(evaluacionAl);
					evaluacion.put(colegioCurso, pregunta);
				}
			} else {
				OTPreguntasEvaluacion pregunta = new OTPreguntasEvaluacion();
				if (pBuenas >= evaluacionAl.getNroRangoMin() && pBuenas <= evaluacionAl.getNroRangoMax()) {
					pregunta.setAlumnos(1);
				} else {
					pregunta.setAlumnos(0);
				}
				pregunta.setEvaluacion(evaluacionAl);

				mapaOT = new HashMap<>();
				mapaOT.put(colegioCurso, pregunta);
				mapEvaAlumnos.put(evaluacionAl, mapaOT);
			}
		}

	}

	private void desplegarDatosEjesTematicos() {

		ObservableList<ObservableList<String>> registros = FXCollections.observableArrayList();

		for (Entry<EjeTematico, HashMap<String, OTPreguntasEjes>> mapa : mapaEjesTematicos.entrySet()) {

			ObservableList<String> row = FXCollections.observableArrayList();

			row.add((mapa.getKey()).getName());

			HashMap<String, OTPreguntasEjes> resultados = mapa.getValue();

			for (String string : titulosColumnas) {
				OTPreguntasEjes otPregunta = resultados.get(string);
				if (otPregunta != null) {// EOS
					row.add(formatter.format(otPregunta.getLogrado()));
				} else {// EOS
					row.add("");
				}
			}

			registros.add(row);
		}
		tblEjesTematicos.setItems(registros);
	}

	private void desplegarDatosEvaluaciones() {

		Map<String, Integer> totales = new HashMap<>();

		ObservableList<ObservableList<String>> registroseEva = FXCollections.observableArrayList();
		ObservableList<String> row = null;
		int total = 0;

		List<EvaluacionEjeTematico> ejes = new ArrayList<>(mapEvaAlumnos.keySet());
		Collections.sort(ejes, Comparadores.comparaEvaluacionEjeTematico());
		for (EvaluacionEjeTematico eje : ejes) {
			HashMap<String, OTPreguntasEvaluacion> resultados = mapEvaAlumnos.get(eje);
			row = FXCollections.observableArrayList();
			row.add(eje.getName());

			for (String string : titulosColumnas) {
				OTPreguntasEvaluacion otPregunta = resultados.get(string);
				if (otPregunta != null) { // EOS
					if (totales.containsKey(string)) {
						total = otPregunta.getAlumnos() + totales.get(string);
						totales.replace(string, total);
					} else {
						totales.put(string, otPregunta.getAlumnos());
					}
					row.add(String.valueOf(otPregunta.getAlumnos()));
				} else {// EOS
					row.add("");
					totales.put(string, 0);
				}

			}
			registroseEva.add(row);

		}
		row = FXCollections.observableArrayList();
		row.add("Totales");
		for (String string : titulosColumnas) {
			Integer valor = totales.get(string);
			row.add(valor == null ? "0" : String.valueOf(valor));
		}
		registroseEva.add(row);

		tblEvaluacionEjesTematicos.setItems(registroseEva);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void creacionColumnasEjesTematicos(List<EvaluacionPrueba> pListaEvaluaciones) {

		tblEjesTematicos.getColumns().clear();

		TableColumn columna0 = new TableColumn("Eje Temático");
		columna0.setCellValueFactory(param -> new SimpleStringProperty(
				((CellDataFeatures<ObservableList, String>) param).getValue().get(0).toString()));
		columna0.setPrefWidth(100);
		tblEjesTematicos.getColumns().add(columna0);

		titulosColumnas = new ArrayList<>();
		int indice = 1;
		List<EvaluacionPrueba> listaEvaluaciones = pListaEvaluaciones;
		for (EvaluacionPrueba evaluacion : listaEvaluaciones) {
			// Columnas
			final int col = indice;
			final String colegioCurso = evaluacion.getColegiocurso();
			titulosColumnas.add(colegioCurso);
			TableColumn columna = new TableColumn(colegioCurso);
			columna.setCellValueFactory(param -> new SimpleStringProperty(
					((CellDataFeatures<ObservableList, String>) param).getValue().get(col).toString()));
			columna.setPrefWidth(100);
			tblEjesTematicos.getColumns().add(columna);
			indice++;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void creacionColumnasEvaluaciones(List<EvaluacionPrueba> pListaEvaluaciones) {
		tblEvaluacionEjesTematicos.getColumns().clear();
		TableColumn columna0 = new TableColumn("");
		columna0.setCellValueFactory(param -> new SimpleStringProperty(
				((CellDataFeatures<ObservableList, String>) param).getValue().get(0).toString()));
		columna0.setPrefWidth(100);
		tblEvaluacionEjesTematicos.getColumns().add(columna0);

		int indice = 1;
		for (String evaluacion : titulosColumnas) {

			// Columnas
			final int col = indice;
			final String colegioCurso = evaluacion;
			TableColumn columna = new TableColumn(colegioCurso);
			columna.setCellValueFactory(param -> new SimpleStringProperty(
					((CellDataFeatures<ObservableList, String>) param).getValue().get(col).toString()));
			columna.setPrefWidth(100);
			tblEvaluacionEjesTematicos.getColumns().add(columna);
			indice++;
		}
	}

	@Override
	public void handle(ActionEvent event) {
		Object source = event.getSource();
		if (source == mnuExportarEjesTematicos || source == mnuExportarEvaluacion) {

			tblEjesTematicos.setId("Ejes temáticos");
			tblEvaluacionEjesTematicos.setId("Evaluación");
			List<TableView<? extends Object>> listaTablas = new LinkedList<>();
			listaTablas.add((TableView<? extends Object>) tblEjesTematicos);
			listaTablas.add((TableView<? extends Object>) tblEvaluacionEjesTematicos);

			ExcelSheetWriterObj.convertirDatosALibroDeExcel(listaTablas);
		} else if (source == btnGenerar) {
			if (prueba != null && tipoAlumno != -1) {
				procesaDatosReporte();
			}
		}
	}
}
