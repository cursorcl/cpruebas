package cl.eos.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import org.controlsfx.dialog.Dialogs;

import cl.eos.imp.view.AFormView;
import cl.eos.ot.OTPreguntasEjes;
import cl.eos.ot.OTPreguntasEvaluacion;
import cl.eos.ot.OTPreguntasHabilidad;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.EvaluacionEjeTematico;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RangoEvaluacion;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.persistence.util.Comparadores;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.Pair;
import cl.eos.util.Utils;

public class ComparativoColegioEjeEvauacionView extends AFormView implements
		EventHandler<ActionEvent> {

	private static final String ASIGNATURA_ID = "idAsignatura";
	private static final String COLEGIO_ID = "idColegio";
	@FXML
	private TableView tblEjesCantidad;
	@FXML
	private ComboBox<Colegio> cmbColegios;
	@FXML
	private ComboBox<Asignatura> cmbAsignatura;
	@FXML
	private Button btnReportes;
	@FXML
	private Label lblColegio;
	@FXML
	private Label lblTitulo;
	@FXML
	private MenuItem mnuExportarGeneral;
	@FXML
	private MenuItem mnuExportarAlumnos;

	private Map<String, Object> parameters = new HashMap<String, Object>();
	private ObservableList<Curso> cursoList;
	private ObservableList<RangoEvaluacion> rangoEvalList;
	private ObservableList<EvaluacionPrueba> evaluacionesPrueba;
	private ArrayList<OTPreguntasEvaluacion> lst;

	public ComparativoColegioEjeEvauacionView() {
		setTitle("Comparativo Colegio Ejes Temáticos y Habilidades");
	}

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

		if (source == mnuExportarAlumnos || source == mnuExportarGeneral) {

			tblEjesCantidad.setId("Comparativo Ejes y Habilidades");
			//
			List<TableView<? extends Object>> listaTablas = new ArrayList<>();
			listaTablas.add((TableView<? extends Object>) tblEjesCantidad);
			ExcelSheetWriterObj.convertirDatosALibroDeExcel(listaTablas);
		}
	}

	private void handleColegios() {
		Colegio colegio = cmbColegios.getSelectionModel().getSelectedItem();
		if (colegio != null) {
			parameters.put(COLEGIO_ID, colegio.getId());
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("coelgioId", colegio.getId());
			lblTitulo.setText(colegio.getName());
			controller.find("Curso.findByColegio", param);
			clearContent();
		}
	}

	private void handleAsignatura() {
		Asignatura asignatura = cmbAsignatura.getSelectionModel()
				.getSelectedItem();
		if (asignatura != null) {
			parameters.put(ASIGNATURA_ID, asignatura.getId());
			clearContent();
		}
	}

	private void handleReportes() {
		if (!parameters.isEmpty() && parameters.containsKey(COLEGIO_ID)
				&& parameters.containsKey(ASIGNATURA_ID)) {

			controller.find("EvaluacionPrueba.findEvaluacionByColegioAsig",
					parameters, this);
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
		mnuExportarAlumnos.setOnAction(this);
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
			}
			if (entity instanceof Asignatura) {
				ObservableList<Asignatura> oList = FXCollections
						.observableArrayList();
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
				FXCollections
						.sort(cursoList, Comparadores.comparaResumeCurso());
			}
			if (entity instanceof EvaluacionPrueba) {
				evaluacionesPrueba = FXCollections.observableArrayList();
				for (Object object : list) {
					EvaluacionPrueba evaluacion = (EvaluacionPrueba) object;
					evaluacionesPrueba.add(evaluacion);
				}
				EvaluacionPrueba evaluacionPrueba = (EvaluacionPrueba) entity;
				rangoEvalList = FXCollections.observableArrayList();
				List<RangoEvaluacion> rngs = evaluacionPrueba.getPrueba()
						.getNivelEvaluacion().getRangos();
				for (RangoEvaluacion rng : rngs) {
					rangoEvalList.add(rng);
				}
				generarReporte();
			}
		} else if (list != null && list.isEmpty()) {
			Dialogs.create().owner(null).title("No hay registros.")
					.masthead(null)
					.message("No se ha encontrado registros para la consulta.")
					.showInformation();
		}
	}

	/**
	 * Este metodo coloca las columnas a las dos tablas de la HMI. Coloca los
	 * cursos que estan asociados al colegio, independiente que tenga o no
	 * evaluaciones.
	 * 
	 * @param pCursoList
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void llenarColumnas(ObservableList<Curso> pCursoList, ObservableList<RangoEvaluacion> rangos) {
		TableColumn tc = new TableColumn("EJES");
		tc.setSortable(false);
		tc.setStyle("-fx-alignment: CENTER-LEFT;");
		tc.prefWidthProperty().set(250f);
		tc.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(
					CellDataFeatures<ObservableList, String> param) {
				return new SimpleStringProperty(param.getValue().get(0)
						.toString());
			}
		});
		tblEjesCantidad.getColumns().add(tc);

		int indice = 1;
		for (Curso curso : pCursoList) {
			final int idx = indice;
			tc = new TableColumn(curso.getName());
			tc.prefWidthProperty().set(50f);
			tc.setStyle("-fx-alignment: CENTER;");
			tc.setSortable(false);
			tc.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
				public ObservableValue<String> call(
						CellDataFeatures<ObservableList, String> param) {
					return new SimpleStringProperty(param.getValue().get(idx)
							.toString());
				}
			});
			
			// Estoy agregando subcolumnas
			for(RangoEvaluacion rng: rangos)
			{
				TableColumn stc = new TableColumn(rng.getAbreviacion());
				stc.prefWidthProperty().set(50f);
				stc.setStyle("-fx-alignment: CENTER;");
				stc.setSortable(false);
				stc.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(
							CellDataFeatures<ObservableList, String> param) {
						return new SimpleStringProperty(param.getValue().get(idx)
								.toString());
					}
				});
				tc.getColumns().add(stc);
			}
			
			tblEjesCantidad.getColumns().add(tc);

			indice++;
		}
	}

	/**
	 * Aqui se llenan las tablas con los valores correspondientes.<br>
	 * 1) Se obtienen los ejes tematicos de todas las pruebas.<br>
	 * 2) Se obtienen las habilidades de todas las pruebas.<br>
	 * 3) Se obtienen los porcentajes de aprobacion de de cada curso con
	 * respecto a cada eje y habilidad.
	 */
	private void generarReporte() {

		if (evaluacionesPrueba == null || rangoEvalList == null) {
			// No hay valores para procesar todo.
			return;
		}

		llenarColumnas(cursoList, rangoEvalList);
		int nroCursos = cursoList.size();
		Map<EjeTematico, List<OTPreguntasEjes>> mapEjes = new HashMap<EjeTematico, List<OTPreguntasEjes>>();

		int[] totalAlumnos = new int[nroCursos];
		Arrays.fill(totalAlumnos, 0);
		int[] alumnosEvaluados = new int[nroCursos];
		Arrays.fill(alumnosEvaluados, 0);

		// Todas las evaluaciones asociadas (Todos los cursos)
		for (EvaluacionPrueba eval : evaluacionesPrueba) {
			// Se esta revisando un curso.
			eval.getPruebasRendidas().size();
			List<PruebaRendida> pruebasRendidas = eval.getPruebasRendidas();
			eval.getPrueba().getRespuestas().size();
			List<RespuestasEsperadasPrueba> respEsperadas = eval.getPrueba()
					.getRespuestas();
			// Estamos procesando un curso/una prueba
			for (PruebaRendida pruebaRendida : pruebasRendidas) {
				// Se procesa un alumno.

				// Obtengo el index de la columna que tengo que llenar (mas 1
				// por que la primera es de
				// contenido
				int index = cursoList.indexOf(pruebaRendida.getAlumno()
						.getCurso());

				totalAlumnos[index] = pruebaRendida.getAlumno().getCurso()
						.getAlumnos().size();
				alumnosEvaluados[index] = alumnosEvaluados[index] + 1;

				String respuestas = pruebaRendida.getRespuestas();
				if (respuestas == null || respuestas.isEmpty()) {
					continue;
				}

				for (int n = 0; n < respEsperadas.size(); n++) {
					// Sumando a ejes tematicos
					EjeTematico eje = respEsperadas.get(n).getEjeTematico();
					if (!mapEjes.containsKey(eje)) {
						List<OTPreguntasEjes> lista = new ArrayList<OTPreguntasEjes>();
						for (int idx = 0; idx < nroCursos; idx++) {
							lista.add(null);
						}
						mapEjes.put(eje, lista);
					}
					List<OTPreguntasEjes> lstEjes = mapEjes.get(eje);
					OTPreguntasEjes otE = lstEjes.get(index); // Se obtiene el
																// valor
																// asociado a la
																// columna.
					if (otE == null) {
						otE = new OTPreguntasEjes();
						otE.setEjeTematico(eje);
						lstEjes.set(index, otE);
					}
					Pair<Integer, Integer> buenasTotal = obtenerBuenasTotales(
							respuestas, respEsperadas, eje);
					otE.setBuenas(otE.getBuenas() + buenasTotal.getFirst());
					otE.setTotal(otE.getTotal() + buenasTotal.getSecond());
					lstEjes.set(index, otE);

				}
			}
		}

		// Ahora se debe llenar las tablas.
		generarTablaEjesHabilidades(mapEjes);
	}

	/**
	 * Se genera la tabal que contiene los % de logro por cada eje y por cada
	 * habilidad asociado a cada curso.
	 * 
	 * @param mapEjes
	 *            Mapa que contiene los valores para cada curso de los ejes.
	 * @param mapHabilidades
	 *            Mapa que contiene los valores para cada curso de las
	 *            habilidades.
	 */
	private void generarTablaEjesHabilidades(
			Map<EjeTematico, List<OTPreguntasEjes>> mapEjes) {
		ObservableList<String> row = null;
		ObservableList<ObservableList<String>> items = FXCollections
				.observableArrayList();
		int nroCols = 0;
		for (EjeTematico eje : mapEjes.keySet()) {
			row = FXCollections.observableArrayList();
			List<OTPreguntasEjes> lst = mapEjes.get(eje);
			nroCols = lst.size();
			row.add(eje.getName());
			for (OTPreguntasEjes ot : lst) {
				if (ot != null && ot.getBuenas() != null
						&& ot.getTotal() != null && ot.getTotal() != 0F) {
					float porcentaje = ot.getBuenas().floatValue()
							/ ot.getTotal().floatValue() * 100f;
					row.add(String.valueOf(Utils.redondeo1Decimal(porcentaje)));
				} else {
					row.add(" ");
				}
			}
			items.add(row);
		}
		tblEjesCantidad.setItems(items);

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
					if ("+".equals(sResp)
							|| resp.getRespuesta().equalsIgnoreCase(sResp)) {
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
					if ("+".equals(sResp)
							|| resp.getRespuesta().equalsIgnoreCase(sResp)) {
						nroBuenas++;
					}
				}
				nroPreguntas++;
			}
		}
		return new Pair<Integer, Integer>(nroBuenas, nroPreguntas);
	}

	private void clearContent() {
		tblEjesCantidad.getItems().clear();
		tblEjesCantidad.getColumns().clear();
		;
	}
}
