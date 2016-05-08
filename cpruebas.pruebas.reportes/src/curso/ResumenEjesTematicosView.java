package curso;

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
import cl.eos.ot.OTPreguntasEjes;
import cl.eos.ot.OTPreguntasHabilidad;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.Pair;

public class ResumenEjesTematicosView extends AFormView  implements
EventHandler<ActionEvent>{

	@FXML
	private TableView<OTPreguntasEjes> tblEjesTematicos;
	@FXML
	private TableColumn<OTPreguntasEjes, String> colNombre;
	@FXML
	private TableColumn<OTPreguntasEjes, String> colDescripcion;
	@FXML
	private TableColumn<OTPreguntasEjes, Float> colLogrado;
	@FXML
	private TableColumn<OTPreguntasEjes, Float> colNoLogrado;

	@FXML
	private TextField txtPrueba;
	@FXML
	private TextField txtCurso;
	@FXML
	private TextField txtAsignatura;
	@FXML
	private TextField txtEjeTematico;

	final NumberAxis xAxis = new NumberAxis();
	final CategoryAxis yAxis = new CategoryAxis();
	@FXML
	private BarChart<String, Number> graficoBarra = new BarChart<String, Number>(
			yAxis, xAxis);

	@FXML
	private MenuItem mnuExportarEjes;

	private HashMap<EjeTematico, OTPreguntasEjes> mapaEjesTematicos;

	public ResumenEjesTematicosView() {

	}

	@FXML
	public void initialize() {
		inicializarTablaEjes();
		accionClicTabla();
		this.setTitle("Resumen Respuestas por Ejes Temáticos");
		graficoBarra.setTitle("Gráfico Respuestas por ejes temáticos");
		xAxis.setLabel("Country");
		yAxis.setLabel("Value");
		mnuExportarEjes.setOnAction(this);
	}

	private void inicializarTablaEjes() {
		tblEjesTematicos.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		colNombre
				.setCellValueFactory(new PropertyValueFactory<OTPreguntasEjes, String>(
						"name"));
		colDescripcion
				.setCellValueFactory(new PropertyValueFactory<OTPreguntasEjes, String>(
						"descripcion"));
		colLogrado
				.setCellValueFactory(new PropertyValueFactory<OTPreguntasEjes, Float>(
						"slogrado"));
		colNoLogrado
				.setCellValueFactory(new PropertyValueFactory<OTPreguntasEjes, Float>(
						"snlogrado"));
	}

	private void accionClicTabla() {
		tblEjesTematicos.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<OTPreguntasEjes>() {

					@Override
					public void changed(
							ObservableValue<? extends OTPreguntasEjes> observable,
							OTPreguntasEjes oldValue, OTPreguntasEjes newValue) {
						ObservableList<OTPreguntasEjes> itemsSelec = tblEjesTematicos
								.getSelectionModel().getSelectedItems();

						if (itemsSelec.size() == 1) {
							OTPreguntasEjes ejeTematico = itemsSelec.get(0);
							txtEjeTematico.setText(ejeTematico.getName());

							Float porcentajeLogrado = ejeTematico.getLogrado();
							Float porcentajeNologrado = ejeTematico
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

			if (mapaEjesTematicos != null && !mapaEjesTematicos.isEmpty()) {

				ArrayList<OTPreguntasEjes> listado = new ArrayList<>(
						mapaEjesTematicos.values());
				ObservableList<OTPreguntasEjes> oList = FXCollections
						.observableList(listado);
				tblEjesTematicos.setItems(oList);
			}
		}
	}

	private void obtenerResultados(EvaluacionPrueba entity) {
		List<PruebaRendida> pruebasRendidas = entity.getPruebasRendidas();

		mapaEjesTematicos = new HashMap<EjeTematico, OTPreguntasEjes>();

		Prueba prueba = entity.getPrueba();
		List<RespuestasEsperadasPrueba> respuestasEsperadas = prueba
				.getRespuestas();

		for(RespuestasEsperadasPrueba  resp: respuestasEsperadas)
		{
			mapaEjesTematicos.put(resp.getEjeTematico(), new OTPreguntasEjes());
		}
		
		for (PruebaRendida pruebaRendida : pruebasRendidas) {
			String respuesta = pruebaRendida.getRespuestas();

			for(EjeTematico hab: mapaEjesTematicos.keySet())
			{
				OTPreguntasEjes otPreguntas = mapaEjesTematicos.get(hab);
				Pair<Integer, Integer> result = obtenerBuenasTotales(respuesta, respuestasEsperadas, hab);
				otPreguntas.setEjeTematico(hab);
				otPreguntas.setBuenas(otPreguntas.getBuenas() + result.getFirst());
				otPreguntas.setTotal(otPreguntas.getTotal() + result.getSecond());
				
			}
		}
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
			if (!resp.isAnulada()) {
				if (resp.getEjeTematico().equals(eje)) {
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
		if (source == mnuExportarEjes) {

			tblEjesTematicos.setId("Ejes temáticos");

			List<TableView<? extends Object>> listaTablas = new LinkedList<>();
			listaTablas.add((TableView<? extends Object>) tblEjesTematicos);

			ExcelSheetWriterObj.convertirDatosALibroDeExcel(listaTablas);
		}
		
	}
}