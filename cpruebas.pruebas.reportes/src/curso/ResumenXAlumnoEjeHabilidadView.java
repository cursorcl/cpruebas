package curso;

import java.util.ArrayList;
import java.util.List;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.persistence.models.TipoAlumno;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.Pair;
import cl.eos.util.Utils;
import cl.eos.view.ots.resumenxalumno.eje.habilidad.OTAlumnoResumen;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

public class ResumenXAlumnoEjeHabilidadView extends AFormView implements EventHandler<ActionEvent> {

	private final int FIXED_COLUMNS = 4;
	@SuppressWarnings("rawtypes")
	@FXML
	private TableView tblAlumnos;

	final NumberAxis xAxisE = new NumberAxis(0, 100, 10);
	final CategoryAxis yAxisE = new CategoryAxis();
	@FXML
	private LineChart<String, Number> grfEjes = new LineChart<String, Number>(yAxisE, xAxisE);
	@FXML
	private MenuItem mnuExportarAlumnos;

	final NumberAxis xAxisH = new NumberAxis(0, 100, 10);
	final CategoryAxis yAxisH = new CategoryAxis();
	@FXML
	private LineChart<String, Number> grfHabilidades = new LineChart<String, Number>(yAxisH, xAxisH);

	@FXML
	private ComboBox<TipoAlumno> cmbTipoAlumno;
	@FXML
	private Button btnGenerar;

	long tipoAlumno = Constants.PIE_ALL;

	private List<OTAlumnoResumen> puntos;
	private List<EjeTematico> lstOtEjes;
	private List<Habilidad> lstOtHabs;
	private EvaluacionPrueba evaluacionPrueba;

	public ResumenXAlumnoEjeHabilidadView() {
		setTitle("Resumen Eje/Habilidad x Alumno");
	}

	@SuppressWarnings("unchecked")
	@FXML
	public void initialize() {
		tblAlumnos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tblAlumnos.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ObservableList<String>>() {

			@Override
			public void changed(ObservableValue<? extends ObservableList<String>> observable,
					ObservableList<String> oldValue, ObservableList<String> newValue) {
				int index = tblAlumnos.getSelectionModel().getSelectedIndex();
				poblarGraficos(index);
			}
		});

		mnuExportarAlumnos.setOnAction(this);
		btnGenerar.setOnAction(this);
		cmbTipoAlumno.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedIndex();
			}
		});
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == mnuExportarAlumnos) {
			tblAlumnos.setId("ResumenEjeHabilidadesxAlumno");
			ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblAlumnos);
		} else if (event.getSource() == btnGenerar) {
			generateReport();
		}
	}

	private void generateReport() {
		if (evaluacionPrueba != null && cmbTipoAlumno.getItems() != null && !cmbTipoAlumno.getItems().isEmpty()) {
			List<PruebaRendida> pRendidas = evaluacionPrueba.getPruebasRendidas();

			if (pRendidas != null && !pRendidas.isEmpty()) {
				List<RespuestasEsperadasPrueba> respEsperadas = evaluacionPrueba.getPrueba().getRespuestas();

				// Obteniendo los elementos
				lstOtEjes = getEjesTematicos(respEsperadas);
				lstOtHabs = getHabilidades(respEsperadas);
				puntos = obtenerPuntos(evaluacionPrueba, lstOtEjes, lstOtHabs);

				construirColumnas(lstOtEjes, lstOtHabs);
				llenarValores(puntos);
			}
		}
	}

	@Override
	public void onFound(IEntity entity) {
		if (entity instanceof EvaluacionPrueba) {
			evaluacionPrueba = (EvaluacionPrueba) entity;
			generateReport();
		}
	}

	@Override
	public void onDataArrived(List<Object> list) {
		if (list != null && !list.isEmpty()) {
			Object entity = list.get(0);
			if (entity instanceof TipoAlumno) {
				ObservableList<TipoAlumno> tAlumnoList = FXCollections.observableArrayList();
				for (Object iEntity : list) {
					tAlumnoList.add((TipoAlumno) iEntity);
				}
				cmbTipoAlumno.setItems(tAlumnoList);
				cmbTipoAlumno.getSelectionModel().select((int) Constants.PIE_ALL);
				generateReport();
			}
		}
	}

	/**
	 * Establece los valores en la tabla.
	 * 
	 * @param puntos
	 *            Los puntos de cada eje por alumno.
	 */
	@SuppressWarnings("unchecked")
	private void llenarValores(List<OTAlumnoResumen> puntos) {
		ObservableList<ObservableList<String>> contenido = FXCollections.observableArrayList();
		ObservableList<String> row = null;
		for (OTAlumnoResumen ot : puntos) {
			row = FXCollections.observableArrayList();
			row.add(ot.getAlumno().getRut());
			row.add(ot.getAlumno().getPaterno());
			row.add(ot.getAlumno().getMaterno());
			row.add(ot.getAlumno().getName());
			List<Float> values = ot.getPorcentajes();
			for (Float value : values) {
				row.add(String.valueOf(Utils.redondeo1Decimal(value)));
			}
			contenido.add(row);
		}
		tblAlumnos.setItems(contenido);

	}

	/**
	 * Calcula los puntos obtenidos en cada eje/habilidad por alumno.
	 * 
	 * @param evaluacionPrueba
	 *            La evaluacion de donde se obtienen los valores.
	 * @param lstOtEjes
	 *            Los ejes existentes en la prueba.
	 * @param lstOtHabs
	 *            Las habilidades exitentes en la prueba.
	 * @return Mapa de alumno con todos los puntos por eje y habilidad.
	 */
	private List<OTAlumnoResumen> obtenerPuntos(EvaluacionPrueba evaluacionPrueba, List<EjeTematico> lstEjes,
			List<Habilidad> lstHabs) {

		List<OTAlumnoResumen> respuesta = new ArrayList<OTAlumnoResumen>();

		List<PruebaRendida> pRendidas = new ArrayList<>();
		for (PruebaRendida pruebaRendida : evaluacionPrueba.getPruebasRendidas()) {
			if (tipoAlumno != Constants.PIE_ALL
					&& !pruebaRendida.getAlumno().getTipoAlumno().getId().equals(tipoAlumno)) {
				continue;
			}
			pRendidas.add(pruebaRendida);
		}

		List<RespuestasEsperadasPrueba> respEsperadas = evaluacionPrueba.getPrueba().getRespuestas();

		for (PruebaRendida pr : pRendidas) {
			String resps = pr.getRespuestas();
			Alumno alumno = pr.getAlumno();
			OTAlumnoResumen ot = new OTAlumnoResumen(alumno);
			for (EjeTematico eje : lstEjes) {
				Pair<Integer, Integer> pair = obtenerBuenasTotales(resps, respEsperadas, eje);
				Integer buenas = pair.getFirst();
				Integer cantidad = pair.getSecond();
				float porcentaje = buenas.floatValue() / cantidad.floatValue() * 100f;
				ot.getPorcentajes().add(porcentaje);
			}
			for (Habilidad hab : lstHabs) {
				Pair<Integer, Integer> pair = obtenerBuenasTotales(resps, respEsperadas, hab);
				Integer buenas = pair.getFirst();
				Integer cantidad = pair.getSecond();
				float porcentaje = buenas.floatValue() / cantidad.floatValue() * 100f;
				ot.getPorcentajes().add(porcentaje);
			}
			respuesta.add(ot);
		}
		return respuesta;
	}

	/**
	 * Este metodo evalua la cantidad de buenas de un String de respuesta
	 * contrastado contra las respuestas esperadas.
	 * 
	 * @param respuestas
	 *            Las respuestas del alumno.
	 * @param respEsperadas
	 *            Las respuestas correctas definidas en la prueba.
	 * @param ahb
	 *            La Habilidad en base al que se realiza el calculo.
	 * @return Par <Preguntas buenas, Total de Preguntas> del eje.
	 */
	private Pair<Integer, Integer> obtenerBuenasTotales(String respuestas,
			List<RespuestasEsperadasPrueba> respEsperadas, Habilidad hab) {
		int nroBuenas = 0;
		int nroPreguntas = 0;
		for (int n = 0; n < respEsperadas.size(); n++) {
			RespuestasEsperadasPrueba resp = respEsperadas.get(n);
			if (resp.getHabilidad().equals(hab)) {
				if (respuestas.length() > n) {
					String sResp = respuestas.substring(n, n + 1);
					if ("+".equals(sResp) || resp.getRespuesta().equalsIgnoreCase(sResp)) {
						nroBuenas++;
					}
				}
				nroPreguntas++;
			}
		}
		return new Pair<Integer, Integer>(nroBuenas, nroPreguntas);
	}

	/**
	 * Este metodo evalua la cantidad de buenas de un String de respuesta
	 * contrastado contra las respuestas eperadas.
	 * 
	 * @param respuestas
	 *            Las respuestas del alumno.
	 * @param respEsperadas
	 *            Las respuestas correctas definidas en la prueba.
	 * @param eje
	 *            El Eje tematico en base al que se realiza el calculo.
	 * @return Par <Preguntas buenas, Total de Preguntas> del eje.
	 */
	private Pair<Integer, Integer> obtenerBuenasTotales(String respuestas,
			List<RespuestasEsperadasPrueba> respEsperadas, EjeTematico eje) {
		int nroBuenas = 0;
		int nroPreguntas = 0;
		for (int n = 0; n < respEsperadas.size(); n++) {
			RespuestasEsperadasPrueba resp = respEsperadas.get(n);
			if (resp.getEjeTematico().equals(eje)) {
				if (respuestas.length() > n) {
					String sResp = respuestas.substring(n, n + 1);
					if ("+".equals(sResp) || resp.getRespuesta().equalsIgnoreCase(sResp)) {
						nroBuenas++;
					}
				}
				nroPreguntas++;
			}
		}
		return new Pair<Integer, Integer>(nroBuenas, nroPreguntas);
	}

	/**
	 * Obtiene todos las habilidades de una prueba y la cantidad de preguntas de
	 * cada una.
	 * 
	 * @param respEsperadas
	 *            Lista de preguntas esperadas de una prueba.
	 * @return Lista con las habilidades y la cantidad de preguntas de cada
	 *         habilidad en la prueba.
	 */
	private List<Habilidad> getHabilidades(List<RespuestasEsperadasPrueba> respEsperadas) {
		List<Habilidad> lstOtHabs = new ArrayList<Habilidad>();
		for (RespuestasEsperadasPrueba r : respEsperadas) {
			if (!lstOtHabs.contains(r.getHabilidad())) {
				lstOtHabs.add(r.getHabilidad());
			}
		}
		return lstOtHabs;
	}

	/**
	 * Obtiene todos los ejes temáticos de una prueba.
	 * 
	 * @param respEsperadas
	 *            Lista de preguntas esperadas de una prueba.
	 * @return Lista con los ejes temáticos en la prueba.
	 */
	private List<EjeTematico> getEjesTematicos(List<RespuestasEsperadasPrueba> respEsperadas) {
		List<EjeTematico> lstOtEjes = new ArrayList<EjeTematico>();
		for (RespuestasEsperadasPrueba r : respEsperadas) {
			if (!lstOtEjes.contains(r.getEjeTematico())) {
				lstOtEjes.add(r.getEjeTematico());
			}
		}
		return lstOtEjes;
	}

	/**
	 * Este metodo coloca las columnas a las dos tablas de la HMI. Coloca los
	 * cursos que estan asociados al colegio, independiente que tenga o no
	 * evaluaciones.
	 * 
	 * @param pCursoList
	 */
	@SuppressWarnings("unchecked")
	private void construirColumnas(List<EjeTematico> ejes, List<Habilidad> habs) {
		int index = 0;
		tblAlumnos.getColumns().clear();
		tblAlumnos.getColumns().add(getFixedColumns("RUT", index++, 250));
		tblAlumnos.getColumns().add(getFixedColumns("PATERNO", index++, 250));
		tblAlumnos.getColumns().add(getFixedColumns("MATERNO", index++, 250));
		tblAlumnos.getColumns().add(getFixedColumns("NOMBRE", index++, 320));
		for (EjeTematico eje : ejes) {
			tblAlumnos.getColumns().add(getPercentColumns(eje.getName(), index++));
		}
		for (Habilidad hab : habs) {
			tblAlumnos.getColumns().add(getPercentColumns(hab.getName(), index++));
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private TableColumn getFixedColumns(String name, final int index, int width) {
		TableColumn tc = new TableColumn(name);
		tc.setSortable(false);
		tc.setStyle("-fx-alignment: CENTER-LEFT;");
		tc.prefWidthProperty().set(width);
		tc.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
				return new SimpleStringProperty(param.getValue().get(index).toString());
			}
		});
		return tc;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private TableColumn getPercentColumns(String name, final int index) {
		int idx = index - FIXED_COLUMNS + 1;
		TableColumn tc = new TableColumn("(" + idx + ") " + name);
		makeHeaderWrappable(tc);
		tc.setSortable(false);
		tc.setStyle("-fx-alignment: CENTER;-fx-font-size: 8pt;-fx-text-alignment: center;");
		tc.prefWidthProperty().set(70f);
		tc.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
				return new SimpleStringProperty(param.getValue().get(index).toString());
			}
		});
		return tc;
	}

	@SuppressWarnings("rawtypes")
	private void makeHeaderWrappable(TableColumn col) {
		Label label = new Label(col.getText());
		label.setStyle("-fx-padding: 8px;-fx-font-size: 7pt;");
		label.setWrapText(true);
		label.setAlignment(Pos.CENTER);
		label.setTextAlignment(TextAlignment.CENTER);

		StackPane stack = new StackPane();
		stack.getChildren().add(label);
		stack.prefWidthProperty().bind(col.widthProperty().subtract(5));
		label.prefWidthProperty().bind(stack.prefWidthProperty());
		col.setGraphic(stack);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void poblarGraficos(int index) {
		OTAlumnoResumen ot = puntos.get(index);
		List<Float> porcentajes = ot.getPorcentajes();

		XYChart.Series seriesE = new XYChart.Series();

		seriesE.setName("Porcentaje logro Ejes");
		seriesE.getData().clear();
		int idx = 0;
		for (int i = 0; i < lstOtEjes.size(); i++) {
			seriesE.getData().add(new XYChart.Data<String, Number>(String.valueOf(idx + 1), porcentajes.get(idx++)));
		}
		grfEjes.setLegendSide(Side.RIGHT);
		grfEjes.getData().clear();
		grfEjes.getData().add(seriesE);
		grfEjes.getYAxis().autoRangingProperty().set(false);
		grfEjes.setLegendVisible(false);
		XYChart.Series seriesH = new XYChart.Series();
		seriesH.setName("Porcentaje logro Habilidades");
		seriesH.getData().clear();
		for (int i = 0; i < lstOtHabs.size(); i++) {
			seriesH.getData().add(new XYChart.Data<String, Number>(String.valueOf(idx + 1), porcentajes.get(idx++)));
		}

		grfHabilidades.setLegendVisible(false);
		grfHabilidades.getData().clear();
		grfHabilidades.getData().add(seriesH);
		grfHabilidades.getYAxis().autoRangingProperty().set(false);
	}
}
