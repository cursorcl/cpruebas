package cl.eos.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTPreguntasHabilidad;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.Pair;

public class ResumenHabilidadesView extends AFormView implements
		EventHandler<ActionEvent> {

	@FXML
	private TableView<OTPreguntasHabilidad> tblHabilidades;
	@FXML
	private TableColumn<OTPreguntasHabilidad, String> colNombre;
	@FXML
	private TableColumn<OTPreguntasHabilidad, String> colDescripcion;
	@FXML
	private TableColumn<OTPreguntasHabilidad, String> colLogrado;
	@FXML
	private TableColumn<OTPreguntasHabilidad, String> colNoLogrado;

	@FXML
	private TextField txtPrueba;
	@FXML
	private TextField txtCurso;
	@FXML
	private TextField txtAsignatura;
	@FXML
	private TextField txtHabilidad;

	final NumberAxis xAxis = new NumberAxis();
	final CategoryAxis yAxis = new CategoryAxis();
	@FXML
	private BarChart<String, Number> graficoBarra = new BarChart<String, Number>(
			yAxis, xAxis);

	private HashMap<Habilidad, OTPreguntasHabilidad> mapaHabilidades;
	@FXML
	private MenuItem mnuExportarHabilidad;

	public ResumenHabilidadesView() {

	}

	@FXML
	public void initialize() {
		inicializarTablaHabilidades();
		accionClicTabla();
		this.setTitle("Resumen Respuestas por Habilidades");
		graficoBarra.setTitle("Gráfico Respuestas por habilidad");
		xAxis.setLabel("Country");
		yAxis.setLabel("Value");
		mnuExportarHabilidad.setOnAction(this);
	}

	private void inicializarTablaHabilidades() {
		tblHabilidades.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		colNombre
				.setCellValueFactory(new PropertyValueFactory<OTPreguntasHabilidad, String>(
						"name"));
		colDescripcion
				.setCellValueFactory(new PropertyValueFactory<OTPreguntasHabilidad, String>(
						"descripcion"));
		colLogrado
				.setCellValueFactory(new PropertyValueFactory<OTPreguntasHabilidad, String>(
						"slogrado"));
		colNoLogrado
				.setCellValueFactory(new PropertyValueFactory<OTPreguntasHabilidad, String>(
						"snlogrado"));
	}

	private void accionClicTabla() {
		tblHabilidades.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<OTPreguntasHabilidad>() {

					@Override
					public void changed(
							ObservableValue<? extends OTPreguntasHabilidad> observable,
							OTPreguntasHabilidad oldValue,
							OTPreguntasHabilidad newValue) {
						ObservableList<OTPreguntasHabilidad> itemsSelec = tblHabilidades
								.getSelectionModel().getSelectedItems();

						if (itemsSelec.size() == 1) {
							OTPreguntasHabilidad habilidad = itemsSelec.get(0);
							txtHabilidad.setText(habilidad.getName());

							Float porcentajeLogrado = habilidad.getLogrado();
							Float porcentajeNologrado = habilidad
									.getNologrado();

							XYChart.Series series1 = new XYChart.Series();
							series1.setName("%");
							series1.getData().add(
									new XYChart.Data<String, Float>("Logrado",
											porcentajeLogrado));
							series1.getData().add(
									new XYChart.Data<String, Float>(
											"No Logrado", porcentajeNologrado));
							graficoBarra.getData().clear();
							graficoBarra.getData().add(series1);
							
							for (Series<String, Number> s : graficoBarra.getData()) {
					            for (Data<String, Number> d : s.getData()) {
					                Tooltip.install(d.getNode(), new Tooltip(
					                        String.format("%s = %2.1f", 
					                                d.getXValue().toString(), 
					                                d.getYValue().doubleValue())));
					            }
					        }
						}
					}
				});
	}

	@Override
	public void onFound(IEntity entity) {
		if (entity instanceof EvaluacionPrueba) {

			txtAsignatura.setText(((EvaluacionPrueba) entity).getAsignatura());
			txtCurso.setText(((EvaluacionPrueba) entity).getCurso().getName());
			txtPrueba
					.setText(((EvaluacionPrueba) entity).getPrueba().getName());
			obtenerResultados((EvaluacionPrueba) entity);

			if (mapaHabilidades != null && !mapaHabilidades.isEmpty()) {

				ArrayList<OTPreguntasHabilidad> listado = new ArrayList<>(
						mapaHabilidades.values());
				ObservableList<OTPreguntasHabilidad> oList = FXCollections
						.observableList(listado);
				tblHabilidades.setItems(oList);
			}
		}
	}

	private void obtenerResultados(EvaluacionPrueba entity) {
		List<PruebaRendida> pruebasRendidas = entity.getPruebasRendidas();
		
		mapaHabilidades = new HashMap<Habilidad, OTPreguntasHabilidad>();

		Prueba prueba = entity.getPrueba();
		List<RespuestasEsperadasPrueba> respuestasEsperadas = prueba
				.getRespuestas();

		for(RespuestasEsperadasPrueba  resp: respuestasEsperadas)
		{
			mapaHabilidades.put(resp.getHabilidad(), new OTPreguntasHabilidad());
		}
		
		for (PruebaRendida pruebaRendida : pruebasRendidas) {
			String respuesta = pruebaRendida.getRespuestas();

			for(Habilidad hab: mapaHabilidades.keySet())
			{
				OTPreguntasHabilidad otPreguntas = mapaHabilidades.get(hab);
				Pair<Integer, Integer> result = obtenerBuenasTotales(respuesta, respuestasEsperadas, hab);
				otPreguntas.setHabilidad(hab);
				otPreguntas.setBuenas(otPreguntas.getBuenas() + result.getFirst());
				otPreguntas.setTotal(otPreguntas.getTotal() + result.getSecond());
				
			}
		}
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
			if (!resp.isAnulada()) {
				if (resp.getHabilidad().equals(hab)) {
					if (respuestas.length() > n) {
						String sResp = respuestas.substring(n, n + 1);
						if ("+".equals(sResp)
								|| resp.getRespuesta().equalsIgnoreCase(sResp)) {
							nroBuenas++;
						}
					}
					nroPreguntas++;
				}
			}
		}
		return new Pair<Integer, Integer>(nroBuenas, nroPreguntas);
	}
	
	@Override
	public void handle(ActionEvent event) {
		Object source = event.getSource();
		if (source == mnuExportarHabilidad) {

			tblHabilidades.setId("Habilidades");

			List<TableView<? extends Object>> listaTablas = new LinkedList<>();
			listaTablas.add((TableView<? extends Object>) tblHabilidades);

			ExcelSheetWriterObj.convertirDatosALibroDeExcel(listaTablas);
		}
	}
}
