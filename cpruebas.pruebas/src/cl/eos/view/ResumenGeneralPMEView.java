package cl.eos.view;

import java.awt.font.NumericShaper.Range;
import java.lang.reflect.Array;
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
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTPreguntasEjes;
import cl.eos.ot.OTPreguntasEvaluacion;
import cl.eos.ot.OTRangoEvaluacion;
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
import cl.eos.util.ExcelSheetWriter;
import cl.eos.util.Utils;

public class ResumenGeneralPMEView extends AFormView implements
		EventHandler<ActionEvent> {

	@FXML
	private Label lblTitulo;
	@FXML
	private MenuItem mnuExportarEjesTematicos;
	@FXML
	private MenuItem mnuExportarHabilidades;
	@FXML
	private TableView tblEjesTematicos;
	@FXML
	private TableColumn colEje;
	@FXML
	private TableColumn colEjeLogro;

	@FXML
	private TableView tblHabilidades;
	@FXML
	private TableColumn colHabilidad;
	@FXML
	private TableColumn colHabilidadLogro;

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

	private Map<EjeTematico, HashMap<String, OTPreguntasEjes>> mapaEjesTematicos;
	private Map<EvaluacionEjeTematico, HashMap<String, OTPreguntasEvaluacion>> mapEvaAlumnos = null;

	private ArrayList<String> titulosColumnas;
	private Map<Long, EjeTematico> mEjeTematico;
	private Map<Long, Habilidad> mHabilidad;
	private Map<RangoEvaluacion, Integer> mRango;

	public ResumenGeneralPMEView() {

	}

	@FXML
	public void initialize() {
		this.setTitle("Resumen General por curso");
		inicializarTablaEjes();
		inicializarTablaHabilidades();
		// mnuExportarEjesTematicos.setOnAction(this);
		// mnuExportarEvaluacion.setOnAction(this);
	}

	private void inicializarTablaEjes() {
		tblEjesTematicos.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
	}

	private void inicializarTablaHabilidades() {
		tblHabilidades.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
	}

	private void inicializarTablaEvaluacines() {
		tblRangos.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		colRango.setCellValueFactory(new PropertyValueFactory<OTRangoEvaluacion, String>(
				"nombre"));
		colRangoCantidad.setCellValueFactory(new PropertyValueFactory<OTRangoEvaluacion, Integer>(
				"nombre"));
		colRangolLogro.setCellValueFactory(new PropertyValueFactory<OTRangoEvaluacion, Float>(
				"nombre"));
	}

	private boolean llegaOnFound = false;
	private boolean llegaOnDAEje = false;
	private boolean llegaOnDAHabilidad = false;
	private boolean llegaOnDANivel = false;
	private EvaluacionPrueba evaluacionPrueba;

	private void procesaDatosReporte() {
		if (llegaOnDAEje && llegaOnDAHabilidad && llegaOnDANivel
				&& llegaOnFound) {
			llenarDatosTabla();
			desplegarDatosEjesTematicos();
			desplegarDatosEvaluaciones();
		}
	}

	@Override
	public void onFound(IEntity entity) {
		if (entity instanceof EvaluacionPrueba) {
			evaluacionPrueba = (EvaluacionPrueba) entity;
			llegaOnFound = true;
		}
		procesaDatosReporte();
	}

	@Override
	public void onDataArrived(List<Object> list) {
		if (list != null && !list.isEmpty()) {
			Object entity = list.get(0);

			if (entity instanceof EjeTematico) {
				llegaOnDAEje = true;
				mEjeTematico = new HashMap<Long, EjeTematico>();
				for (Object object : list) {
					EjeTematico eje = (EjeTematico) object;
					mEjeTematico.put(eje.getId(), eje);
				}
			}

			if (entity instanceof Habilidad) {
				llegaOnDAHabilidad = true;
				mHabilidad = new HashMap<Long, Habilidad>();
				for (Object object : list) {
					Habilidad habilidad = (Habilidad) object;
					mHabilidad.put(habilidad.getId(), habilidad);
				}
			}

			if (entity instanceof RangoEvaluacion) {
				llegaOnDANivel = true;
				mRango = new HashMap<RangoEvaluacion, Integer>();
				for (Object object : list) {

					RangoEvaluacion rango = (RangoEvaluacion) object;
					mRango.put(rango, 0);
				}
			}
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

		mapaEjesTematicos = new HashMap<EjeTematico, HashMap<String, OTPreguntasEjes>>();
		mapEvaAlumnos = new HashMap<EvaluacionEjeTematico, HashMap<String, OTPreguntasEvaluacion>>();
		Map<RangoEvaluacion, OTRangoEvaluacion> mapaRango = new HashMap<RangoEvaluacion, OTRangoEvaluacion>();

		HashMap<String, OTPreguntasEjes> mapaColegios = null;

		List<PruebaRendida> listaEvaluaciones = evaluacionPrueba
				.getPruebasRendidas();
		int totalAlumnos = evaluacionPrueba.getPruebasRendidas().size();
		int totalPreguntas = evaluacionPrueba.getPrueba().getNroPreguntas();
		Prueba prueba = evaluacionPrueba.getPrueba();
		NivelEvaluacion nivelEvaluacion = prueba.getNivelEvaluacion();

		float sumaNotas = 0;
		float sumaLogro = 0;
		for (PruebaRendida evaluacionPrueba : listaEvaluaciones) {

			sumaNotas = sumaNotas + evaluacionPrueba.getNota();

			int logro = evaluacionPrueba.getBuenas() / totalPreguntas;
			sumaLogro = sumaLogro + logro;

			RangoEvaluacion rango = nivelEvaluacion.getRango(logro);
			OTRangoEvaluacion otRango = mapaRango.get(rango);
			otRango.setCantidad(otRango.getCantidad() + 1);
			otRango.setLogrado(otRango.getCantidad() / totalAlumnos);

			// String colegioCurso = evaluacionPrueba.getColegiocurso();
			// List<RespuestasEsperadasPrueba> respuestasEsperadas =
			// evaluacionPrueba
			// .getRespuestas();
			//
			// for (PruebaRendida pruebaRendida : pruebasRendidas) {
			// generaDatosEvaluacion(pruebaRendida, colegioCurso);
			//
			// String respuesta = pruebaRendida.getRespuestas();
			// char[] cRespuesta = respuesta.toCharArray();
			//
			// foor (RespuestasEsperadasPrueba respuestasEsperadasPrueba :
			// respuestasEsperadas) {
			//
			// EjeTematico ejeTematico = respuestasEsperadasPrueba
			// .getEjeTematico();
			// Integer numeroPreg = respuestasEsperadasPrueba.getNumero();
			// if (mapaEjesTematicos.containsKey(ejeTematico)) {
			// HashMap<String, OTPreguntasEjes> mapa = mapaEjesTematicos
			// .get(ejeTematico);
			//
			// if (mapa.containsKey(colegioCurso)) {
			// OTPreguntasEjes otPregunta = mapa.get(colegioCurso);
			//
			// if (cRespuesta[numeroPreg - 1] == respuestasEsperadasPrueba
			// .getRespuesta().toCharArray()[0]) {
			// otPregunta
			// .setBuenas(otPregunta.getBuenas() + 1);
			// }
			// otPregunta.setTotal(otPregunta.getTotal() + 1);
			// } else {
			// OTPreguntasEjes otPreguntas = new OTPreguntasEjes();
			// otPreguntas.setEjeTematico(ejeTematico);
			// if (cRespuesta[numeroPreg - 1] == respuestasEsperadasPrueba
			// .getRespuesta().toCharArray()[0]) {
			// otPreguntas.setBuenas(1);
			// } else {
			// otPreguntas.setBuenas(0);
			// }
			// otPreguntas.setTotal(1);
			//
			// mapa.put(colegioCurso, otPreguntas);
			// }
			// } else {
			// OTPreguntasEjes otPreguntas = new OTPreguntasEjes();
			// otPreguntas.setEjeTematico(ejeTematico);
			// if (cRespuesta[numeroPreg - 1] == respuestasEsperadasPrueba
			// .getRespuesta().toCharArray()[0]) {
			// otPreguntas.setBuenas(1);
			// } else {
			// otPreguntas.setBuenas(0);
			// }
			// otPreguntas.setTotal(1);
			//
			// mapaColegios = new HashMap<String, OTPreguntasEjes>();
			// mapaColegios.put(colegioCurso, otPreguntas);
			// mapaEjesTematicos.put(ejeTematico, mapaColegios);
			// }
			// }
			// }
		}

		List<OTRangoEvaluacion> lista = new ArrayList<OTRangoEvaluacion>(
				mapaRango.values());
		Collections.sort(lista, Comparadores.comparaRangoEvaluacion());

		// Asignar evaluados
		txtEvaluados.setText(String.valueOf(totalAlumnos));
		// Asignar promedio.
		float promedio = sumaNotas / totalAlumnos;
		txtPromedio.setText(String.valueOf(promedio));
		// Asignar puntaje
		int puntaje = Utils.getPuntaje(promedio);
		txtPuntaje.setText(String.valueOf(puntaje));
		// Asignar promedio logro
		float promedioLogro = sumaLogro / totalAlumnos;
		txtLogro.setText(String.valueOf(promedio));
		// Asignar evaluacion
		RangoEvaluacion rango = nivelEvaluacion.getRango(promedioLogro);
		txtEvaluados.setText(rango.getAbreviacion());

	}

	private void generaDatosEvaluacion(PruebaRendida pruebaRendida,
			String colegioCurso) {

		HashMap<String, OTPreguntasEvaluacion> mapaOT = new HashMap<String, OTPreguntasEvaluacion>();

		Float pBuenas = pruebaRendida.getPbuenas();
		// for (Entry<Long, EvaluacionEjeTematico> mEvaluacion : mEvaluaciones
		// .entrySet()) {
		//
		// EvaluacionEjeTematico evaluacionAl = mEvaluacion.getValue();
		// if (mapEvaAlumnos.containsKey(evaluacionAl)) {
		// HashMap<String, OTPreguntasEvaluacion> evaluacion = mapEvaAlumnos
		// .get(evaluacionAl);
		// if (evaluacion.containsKey(colegioCurso)) {
		// OTPreguntasEvaluacion otPreguntas = evaluacion
		// .get(colegioCurso);
		//
		// if (pBuenas >= evaluacionAl.getNroRangoMin()
		// && pBuenas <= evaluacionAl.getNroRangoMax()) {
		// otPreguntas.setAlumnos(otPreguntas.getAlumnos() + 1);
		// }
		// } else {
		//
		// OTPreguntasEvaluacion pregunta = new OTPreguntasEvaluacion();
		// if (pBuenas >= evaluacionAl.getNroRangoMin()
		// && pBuenas <= evaluacionAl.getNroRangoMax()) {
		// pregunta.setAlumnos(1);
		// } else {
		// pregunta.setAlumnos(0);
		// }
		// pregunta.setEvaluacion(evaluacionAl);
		// evaluacion.put(colegioCurso, pregunta);
		// }
		// } else {
		// OTPreguntasEvaluacion pregunta = new OTPreguntasEvaluacion();
		// if (pBuenas >= evaluacionAl.getNroRangoMin()
		// && pBuenas <= evaluacionAl.getNroRangoMax()) {
		// pregunta.setAlumnos(1);
		// } else {
		// pregunta.setAlumnos(0);
		// }
		// pregunta.setEvaluacion(evaluacionAl);
		//
		// mapaOT = new HashMap<String, OTPreguntasEvaluacion>();
		// mapaOT.put(colegioCurso, pregunta);
		// mapEvaAlumnos.put(evaluacionAl, mapaOT);
		// }
		// }

	}

	private void desplegarDatosEjesTematicos() {
		ObservableList<ObservableList> registros = FXCollections
				.observableArrayList();

		for (Entry<EjeTematico, HashMap<String, OTPreguntasEjes>> mapa : mapaEjesTematicos
				.entrySet()) {

			ObservableList<String> row = FXCollections.observableArrayList();

			row.add(((EjeTematico) mapa.getKey()).getName());

			HashMap<String, OTPreguntasEjes> resultados = mapa.getValue();

			for (String string : titulosColumnas) {
				OTPreguntasEjes otPregunta = resultados.get(string);
				row.add(String.valueOf(otPregunta.getLogrado()));
			}

			registros.add(row);
		}
		tblEjesTematicos.setItems(registros);
	}

	private void desplegarDatosEvaluaciones() {

		Map<String, Integer> totales = new HashMap<String, Integer>();

		ObservableList<ObservableList> registroseEva = FXCollections
				.observableArrayList();
		ObservableList<String> row = null;
		int total = 0;
		for (Entry<EvaluacionEjeTematico, HashMap<String, OTPreguntasEvaluacion>> mapa : mapEvaAlumnos
				.entrySet()) {
			row = FXCollections.observableArrayList();
			row.add(((EvaluacionEjeTematico) mapa.getKey()).getName());
			HashMap<String, OTPreguntasEvaluacion> resultados = mapa.getValue();

			for (String string : titulosColumnas) {
				OTPreguntasEvaluacion otPregunta = resultados.get(string);
				if (totales.containsKey(string)) {
					total = otPregunta.getAlumnos() + totales.get(string);
					totales.replace(string, total);
				} else {
					totales.put(string, otPregunta.getAlumnos());
				}
				row.add(String.valueOf(otPregunta.getAlumnos()));
			}
			registroseEva.add(row);

		}
		row = FXCollections.observableArrayList();
		row.add("Totales");
		for (String string : titulosColumnas) {
			Integer valor = totales.get(string);
			row.add(String.valueOf(valor));
		}
		registroseEva.add(row);

		tblHabilidades.setItems(registroseEva);
	}

	private void creacionColumnasEjesTematicos(
			List<EvaluacionPrueba> pListaEvaluaciones) {
		TableColumn columna0 = new TableColumn("Eje Temático");
		columna0.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(
					CellDataFeatures<ObservableList, String> param) {

				return new SimpleStringProperty(param.getValue().get(0)
						.toString());
			}
		});
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
			columna.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
				public ObservableValue<String> call(
						CellDataFeatures<ObservableList, String> param) {
					return new SimpleStringProperty(param.getValue().get(col)
							.toString());
				}
			});
			columna.setPrefWidth(100);
			tblEjesTematicos.getColumns().add(columna);
			indice++;
		}
	}

	private void creacionColumnasEvaluaciones(
			List<EvaluacionPrueba> pListaEvaluaciones) {
		TableColumn columna0 = new TableColumn("");
		columna0.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(
					CellDataFeatures<ObservableList, String> param) {
				return new SimpleStringProperty(param.getValue().get(0)
						.toString());
			}
		});
		columna0.setPrefWidth(100);
		tblHabilidades.getColumns().add(columna0);

		int indice = 1;
		for (String evaluacion : titulosColumnas) {

			// Columnas
			final int col = indice;
			final String colegioCurso = evaluacion;
			TableColumn columna = new TableColumn(colegioCurso);
			columna.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
				public ObservableValue<String> call(
						CellDataFeatures<ObservableList, String> param) {
					return new SimpleStringProperty(param.getValue().get(col)
							.toString());
				}
			});
			columna.setPrefWidth(100);
			tblHabilidades.getColumns().add(columna);
			indice++;
		}
	}

	@Override
	public void handle(ActionEvent event) {
		Object source = event.getSource();
		if (source == mnuExportarEjesTematicos) {
			// ExportadorDeTablasAExcel
			// .convertirDatosALibroDeExcel(tblEjesTematicos);
		} else if (source == mnuExportarHabilidades) {
			ExcelSheetWriter.convertirDatosALibroDeExcel(tblHabilidades);
		}
	}
}