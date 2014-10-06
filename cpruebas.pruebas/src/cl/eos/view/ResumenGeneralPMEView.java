package cl.eos.view;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTPreguntasEjes;
import cl.eos.ot.OTPreguntasEvaluacion;
import cl.eos.ot.OTPreguntasHabilidad;
import cl.eos.ot.OTRangoEvaluacion;
import cl.eos.ot.OTReporte;
import cl.eos.ot.OTResultado;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.EvaluacionEjeTematico;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.NivelEvaluacion;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RangoEvaluacion;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.persistence.util.Comparadores;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.Utils;

public class ResumenGeneralPMEView extends AFormView implements
		EventHandler<ActionEvent> {

	private NumberFormat formatter = new DecimalFormat("#0.00");
	@FXML
	private Label lblTitulo;
	@FXML
	private MenuItem mnuExportarEjesTematicos;
	@FXML
	private MenuItem mnuExportarHabilidades;
	@FXML
	private MenuItem mnuExportarRangos;
	@FXML
	private MenuItem mnuExportarReporte;
	@FXML
	private TableView<OTResultado> tblEjesTematicos;
	@FXML
	private TableColumn<OTResultado, String> colEje;
	@FXML
	private TableColumn<OTResultado, String> colEjeLogro;

	@FXML
	private TableView<OTResultado> tblHabilidades;
	@FXML
	private TableColumn<OTResultado, String> colHabilidad;
	@FXML
	private TableColumn<OTResultado, String> colHabilidadLogro;

	@FXML
	private TableView<OTRangoEvaluacion> tblRangos;
	@FXML
	private TableColumn<OTRangoEvaluacion, String> colRango;
	@FXML
	private TableColumn<OTRangoEvaluacion, Integer> colRangoCantidad;
	@FXML
	private TableColumn<OTRangoEvaluacion, Float> colRangolLogro;

	@FXML
	private TableView tblReportePME;

	@FXML
	private TextField txtPromedio;
	@FXML
	private TextField txtPuntaje;
	@FXML
	private TextField txtNivel;
	@FXML
	private TextField txtLogro;
	@FXML
	private TextField txtEvaluados;

	private Map<EjeTematico, Float> mapaEjesCurso = new HashMap<EjeTematico, Float>();
	private Map<Habilidad, Float> mapaHabilidadesCurso = new HashMap<Habilidad, Float>();
	private Map<RangoEvaluacion, OTRangoEvaluacion> mRango;
	Map<RangoEvaluacion, Map<EjeTematico, OTReporte>> mapReporte;

	public ResumenGeneralPMEView() {

	}

	@FXML
	public void initialize() {
		this.setTitle("Resumen General por curso");
		inicializarTablaEjes();
		inicializarTablaHabilidades();
		inicializarTablaEvaluacines();
		mnuExportarEjesTematicos.setOnAction(this);
		mnuExportarHabilidades.setOnAction(this);
		mnuExportarRangos.setOnAction(this);
		mnuExportarReporte.setOnAction(this);
	}

	private void inicializarTablaEjes() {
		tblEjesTematicos.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		colEje.setCellValueFactory(new PropertyValueFactory<OTResultado, String>(
				"nombre"));
		colEjeLogro
				.setCellValueFactory(new PropertyValueFactory<OTResultado, String>(
						"logrado"));
	}

	private void inicializarTablaHabilidades() {
		tblHabilidades.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		colHabilidad
				.setCellValueFactory(new PropertyValueFactory<OTResultado, String>(
						"nombre"));
		colHabilidadLogro
				.setCellValueFactory(new PropertyValueFactory<OTResultado, String>(
						"logrado"));
	}

	private void inicializarTablaEvaluacines() {
		tblRangos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		colRango.setCellValueFactory(new PropertyValueFactory<OTRangoEvaluacion, String>(
				"name"));
		colRangoCantidad
				.setCellValueFactory(new PropertyValueFactory<OTRangoEvaluacion, Integer>(
						"cantidad"));
		colRangolLogro
				.setCellValueFactory(new PropertyValueFactory<OTRangoEvaluacion, Float>(
						"logrado"));
	}

	private boolean llegaOnFound = false;
	private EvaluacionPrueba evaluacionPrueba;
	private ArrayList<String> titulosColumnas;

	private void procesaDatosReporte() {
		if (llegaOnFound) {
			llenarDatosTabla();
		}
	}

	@Override
	public void onFound(IEntity entity) {
		if (entity instanceof EvaluacionPrueba) {
			evaluacionPrueba = (EvaluacionPrueba) entity;
			List<RangoEvaluacion> list = evaluacionPrueba.getPrueba()
					.getNivelEvaluacion().getRangos();
			mRango = new HashMap<RangoEvaluacion, OTRangoEvaluacion>();
			for (RangoEvaluacion rango : list) {
				OTRangoEvaluacion otRango = new OTRangoEvaluacion();
				otRango.setRango(rango);
				mRango.put(rango, otRango);
			}
			llegaOnFound = true;
		}
		procesaDatosReporte();
	}

	private void llenarDatosTabla() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(evaluacionPrueba.getName());
		buffer.append(" ");
		buffer.append(evaluacionPrueba.getAsignatura());
		buffer.append(" ");
		buffer.append(evaluacionPrueba.getCurso().getName());
		lblTitulo.setText(buffer.toString());

		List<PruebaRendida> pruebasRendidas = evaluacionPrueba
				.getPruebasRendidas();

		int totalAlumnos = evaluacionPrueba.getPruebasRendidas().size();
		int totalPreguntas = evaluacionPrueba.getPrueba().getNroPreguntas();

		Prueba prueba = evaluacionPrueba.getPrueba();
		NivelEvaluacion nivelEvaluacion = prueba.getNivelEvaluacion();
		List<RespuestasEsperadasPrueba> respuestasEsperadas = prueba
				.getRespuestas();

		float sumaNotas = 0;
		float sumaLogro = 0;
		for (PruebaRendida pRendida : pruebasRendidas) {

			sumaNotas = sumaNotas + pRendida.getNota();

			int logro = pRendida.getBuenas() / totalPreguntas;
			sumaLogro = sumaLogro + logro;

			RangoEvaluacion rango = nivelEvaluacion.getRango(logro);
			OTRangoEvaluacion otRango = mRango.get(rango);
			otRango.setCantidad(otRango.getCantidad() + 1);
			otRango.setLogrado(otRango.getCantidad() / totalAlumnos);

			generaContenidosEjes(pRendida, respuestasEsperadas, nivelEvaluacion);
			generaContenidosHabilidades(pRendida, respuestasEsperadas);

		}

		generarDespliegueContenidosGenerales(totalAlumnos, nivelEvaluacion,
				sumaNotas, sumaLogro);
		generarDespliegueContenidosRangos();
		generarDespliegueContenidoEjesTematicos(totalAlumnos);
		generarDespliegueContenidoHabilidades(totalAlumnos);
		generarDespliegueContenidoReporte(totalAlumnos);
	}

	private void generaContenidosHabilidades(PruebaRendida pruebaRendida,
			List<RespuestasEsperadasPrueba> respuestasEsperadas) {

		Map<Habilidad, OTPreguntasHabilidad> mapaHabilidadAlumno = new HashMap<Habilidad, OTPreguntasHabilidad>();

		String respuesta = pruebaRendida.getRespuestas();
		char[] cRespuesta = respuesta.toCharArray();

		for (RespuestasEsperadasPrueba respuestasEsperadasPrueba : respuestasEsperadas) {

			Habilidad habilidad = respuestasEsperadasPrueba.getHabilidad();

			Integer numeroPreg = respuestasEsperadasPrueba.getNumero();
			if (mapaHabilidadAlumno.containsKey(habilidad)) {
				OTPreguntasHabilidad otPregunta = mapaHabilidadAlumno
						.get(habilidad);

				if (cRespuesta[numeroPreg - 1] == respuestasEsperadasPrueba
						.getRespuesta().toCharArray()[0]) {
					otPregunta.setBuenas(otPregunta.getBuenas() + 1);
				}
				otPregunta.setTotal(otPregunta.getTotal() + 1);
			} else {
				OTPreguntasHabilidad otPreguntas = new OTPreguntasHabilidad();
				otPreguntas.setHabilidad(habilidad);
				if (cRespuesta[numeroPreg - 1] == respuestasEsperadasPrueba
						.getRespuesta().toCharArray()[0]) {
					otPreguntas.setBuenas(1);
				} else {
					otPreguntas.setBuenas(0);
				}
				otPreguntas.setTotal(1);
				mapaHabilidadAlumno.put(habilidad, otPreguntas);
			}
		}

		for (OTPreguntasHabilidad otAlumno : mapaHabilidadAlumno.values()) {

			float valor = otAlumno.getLogrado();
			if (mapaHabilidadesCurso.containsKey(otAlumno.getHabilidad())) {

				Float totalValor = mapaHabilidadesCurso.get(otAlumno
						.getHabilidad());
				totalValor = totalValor + valor;

			} else {
				mapaHabilidadesCurso.put(otAlumno.getHabilidad(), valor);
			}
		}
	}

	private void generaContenidosEjes(PruebaRendida pruebaRendida,
			List<RespuestasEsperadasPrueba> respuestasEsperadas,
			NivelEvaluacion nivelEvaluacion) {

		Map<EjeTematico, OTPreguntasEjes> mapaEjeAlumno = new HashMap<EjeTematico, OTPreguntasEjes>();

		String respuesta = pruebaRendida.getRespuestas();
		char[] cRespuesta = respuesta.toCharArray();

		for (RespuestasEsperadasPrueba respuestasEsperadasPrueba : respuestasEsperadas) {

			EjeTematico ejeTematico = respuestasEsperadasPrueba
					.getEjeTematico();

			Integer numeroPreg = respuestasEsperadasPrueba.getNumero();
			if (mapaEjeAlumno.containsKey(ejeTematico)) {
				OTPreguntasEjes otPregunta = mapaEjeAlumno.get(ejeTematico);

				if (cRespuesta[numeroPreg - 1] == respuestasEsperadasPrueba
						.getRespuesta().toCharArray()[0]) {
					otPregunta.setBuenas(otPregunta.getBuenas() + 1);
				}
				otPregunta.setTotal(otPregunta.getTotal() + 1);
			} else {
				OTPreguntasEjes otPreguntas = new OTPreguntasEjes();
				otPreguntas.setEjeTematico(ejeTematico);
				if (cRespuesta[numeroPreg - 1] == respuestasEsperadasPrueba
						.getRespuesta().toCharArray()[0]) {
					otPreguntas.setBuenas(1);
				} else {
					otPreguntas.setBuenas(0);
				}
				otPreguntas.setTotal(1);
				mapaEjeAlumno.put(ejeTematico, otPreguntas);
				titulosColumnas.add(ejeTematico.getName());
			}
		}

		// genera datos de reportes
		mapReporte = new HashMap<RangoEvaluacion, Map<EjeTematico, OTReporte>>();
		for (Entry<EjeTematico, Float> otEje : mapaEjesCurso.entrySet()) {
			EjeTematico eje = otEje.getKey();
			float promedioLogro = otEje.getValue();
			RangoEvaluacion rango = nivelEvaluacion.getRango(promedioLogro);

			if (mapReporte.containsKey(rango)) {
				Map<EjeTematico, OTReporte> mapEje = mapReporte.get(rango);
				if (mapEje.containsKey(eje)) {
					OTReporte otReporte = mapEje.get(eje);
					otReporte.setTotal(otReporte.getTotal() + 1);
				} else {
					OTReporte otReporte = new OTReporte();
					otReporte.setRango(rango);
					otReporte.setTotal(1);
					otReporte.setEje(eje);

					Map<EjeTematico, OTReporte> nueov = new HashMap<EjeTematico, OTReporte>();
					nueov.put(eje, otReporte);
					mapReporte.put(rango, nueov);
				}
			} else {
				OTReporte reporte = new OTReporte();
				reporte.setRango(rango);
				reporte.setTotal(1);
				reporte.setEje(eje);
				Map<EjeTematico, OTReporte> nuevo = new HashMap<EjeTematico, OTReporte>();
				nuevo.put(eje, reporte);
				mapReporte.put(rango, nuevo);
			}
		}
		creacionColumnasEjesTematicos();
	}

	private void creacionColumnasEjesTematicos() {
		TableColumn columna0 = new TableColumn("");
		columna0.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(
					CellDataFeatures<ObservableList, String> param) {

				return new SimpleStringProperty(param.getValue().get(0)
						.toString());
			}
		});
		columna0.setPrefWidth(100);
		tblReportePME.getColumns().add(columna0);

		int indice = 1;
		for (String titulo : titulosColumnas) {
			// Columnas
			final int col = indice;
			TableColumn columna = new TableColumn(titulo);
			columna.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
				public ObservableValue<String> call(
						CellDataFeatures<ObservableList, String> param) {
					return new SimpleStringProperty(param.getValue().get(col)
							.toString());
				}
			});
			columna.setPrefWidth(100);
			tblReportePME.getColumns().add(columna);
			indice++;
		}
	}

	private void generarDespliegueContenidosRangos() {
		List<OTRangoEvaluacion> lista = new ArrayList<OTRangoEvaluacion>(
				mRango.values());
		Collections.sort(lista, Comparadores.comparaRangoEvaluacion());

		ObservableList<OTRangoEvaluacion> rangos = FXCollections
				.observableArrayList();
		for (OTRangoEvaluacion lEntity : lista) {

			rangos.add((OTRangoEvaluacion) lEntity);
		}

		tblRangos.setItems(rangos);
	}

	private void generarDespliegueContenidoEjesTematicos(int totalAlumnos) {

		ObservableList<OTResultado> mapa = FXCollections.observableArrayList();
		for (Entry<EjeTematico, Float> lEntity : mapaEjesCurso.entrySet()) {
			OTResultado resultado = new OTResultado();
			resultado.setNombre(lEntity.getKey().getName());
			float valor = lEntity.getValue() / totalAlumnos;
			resultado.setLogrado(String.valueOf(valor));
			mapa.add(resultado);
		}
		tblEjesTematicos.setItems(mapa);

	}

	private void generarDespliegueContenidoHabilidades(int totalAlumnos) {

		ObservableList<OTResultado> mapa = FXCollections.observableArrayList();
		for (Entry<Habilidad, Float> lEntity : mapaHabilidadesCurso.entrySet()) {
			OTResultado resultado = new OTResultado();
			resultado.setNombre(lEntity.getKey().getName());
			float valor = lEntity.getValue() / totalAlumnos;
			resultado.setLogrado(String.valueOf(valor));
			mapa.add(resultado);
		}
		tblHabilidades.setItems(mapa);
	}

	private void generarDespliegueContenidoReporte(int totalAlumnos) {
		ObservableList<ObservableList> registroseReporte = FXCollections
				.observableArrayList();

		ObservableList<String> row = null;
		int total = 0;
		for (Entry<RangoEvaluacion, Map<EjeTematico, OTReporte>> mReporte : mapReporte.entrySet()) {
			 Map<EjeTematico, OTReporte> mEjeTematico = mReporte.getValue();
			
			row = FXCollections.observableArrayList();
			row.add(((RangoEvaluacion)mReporte.getKey()).getName());

			ObservableList<OTResultado> m = FXCollections
					.observableArrayList();
			for (String columna : titulosColumnas) {
				OTReporte valor = mEjeTematico.get(columna);
				row.add(String.valueOf(valor.getTotal()));
			}
			registroseReporte.add(row);
		}
		tblReportePME.setItems(registroseReporte);
	}

	private void generarDespliegueContenidosGenerales(int totalAlumnos,
			NivelEvaluacion nivelEvaluacion, float sumaNotas, float sumaLogro) {
		txtEvaluados.setText(String.valueOf(totalAlumnos));

		float promedio = sumaNotas / totalAlumnos;
		txtPromedio.setText(formatter.format(promedio));

		int puntaje = Utils.getPuntaje(promedio);
		txtPuntaje.setText(String.valueOf(puntaje));

		float promedioLogro = sumaLogro / totalAlumnos;

		txtLogro.setText(formatter.format(promedio));

		RangoEvaluacion rango = nivelEvaluacion.getRango(promedioLogro);
		txtEvaluados.setText(rango.getAbreviacion());
	}

	@Override
	public void handle(ActionEvent event) {
		Object source = event.getSource();
		if (source == mnuExportarEjesTematicos) {
			ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblEjesTematicos);
		} else if (source == mnuExportarHabilidades) {
			ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblHabilidades);
		} else if (source == mnuExportarRangos) {
			ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblRangos);
		} else if (source == mnuExportarReporte) {
			ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblReportePME);
		}
	}
}