package cl.eos.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.view.FXDialogs;
import cl.eos.ot.OTPreguntasEvaluacion;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RangoEvaluacion;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.persistence.util.Comparadores;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.view.ots.ejeevaluacion.OTAcumulador;
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

public class ComparativoColegioHabilidadesView extends AFormView implements
		EventHandler<ActionEvent> {

	private static final String ASIGNATURA_ID = "idAsignatura";
	private static final String COLEGIO_ID = "idColegio";
	@FXML
	private TableView tblHabilidadesCantidad;
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

	public ComparativoColegioHabilidadesView() {
		setTitle("Comparativo Colegios Habilidades");
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

			tblHabilidadesCantidad.setId("Comp ColegiosHabilidades");
			List<TableView<? extends Object>> listaTablas = new ArrayList<>();
			listaTablas
					.add((TableView<? extends Object>) tblHabilidadesCantidad);
			ExcelSheetWriterObj
					.convertirDatosColumnasDoblesALibroDeExcel(listaTablas);
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
			FXDialogs.showInformation("No hay registros.", "No se ha encontrado registros para la consulta.");
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
	private void llenarColumnas(ObservableList<Curso> pCursoList,
			ObservableList<RangoEvaluacion> rangos) {
		TableColumn tc = new TableColumn("HABILIDADES");
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
		tblHabilidadesCantidad.getColumns().add(tc);

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
					return new SimpleStringProperty(param.getValue().get(0)
							.toString());
				}
			});
			// Estoy agregando subcolumnas
			for (RangoEvaluacion rng : rangos) {
				final int lIdx = indice;
				TableColumn stc = new TableColumn(rng.getAbreviacion());
				stc.prefWidthProperty().set(50f);
				stc.setStyle("-fx-alignment: CENTER;");
				stc.setSortable(false);
				stc.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(
							CellDataFeatures<ObservableList, String> param) {
						return new SimpleStringProperty(param.getValue()
								.get(lIdx).toString());
					}
				});
				tc.getColumns().add(stc);
				indice++;
			}

			tblHabilidadesCantidad.getColumns().add(tc);

		}
	}

	/**
	 * Aqui se llenan las tablas con los valores correspondientes.<br>
	 * 1) Se obtienen los ejes tematicos de todas las pruebas.<br>
	 * 2) Se obtienen las habilidades de todas las pruebas.<br>
	 * 3) Se obtienen los porcentajes de aprobacion de cada curso con respecto a
	 * cada habilidad y habilidad.
	 */
	private void generarReporte() {

		if (evaluacionesPrueba == null || rangoEvalList == null) {
			// No hay valores para procesar todo.
			return;
		}

		llenarColumnas(cursoList, rangoEvalList);
		int nroCursos = cursoList.size();
		int nroRangos = rangoEvalList.size();
		Map<Habilidad, List<OTAcumulador>> mapEjes = new HashMap<Habilidad, List<OTAcumulador>>();

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

				if (pruebaRendida.getAlumno() == null) {
					// Caso especial que indica que la prueba esta sin alumno.
					continue;
				}

				// Obtengo el index de la columna que tengo que llenar (mas 1
				// por que la primera es de contenido
				// index * nroRangos Ya que cada curso tiene nroRangos columnas
				// asociadas.
				int index = cursoList.indexOf(pruebaRendida.getAlumno()
						.getCurso());

				if (index == -1) { // Caso especial que indica que el alumno no
									// es del colegio.
					continue;
				}
				String respuestas = pruebaRendida.getRespuestas();
				if (respuestas == null || respuestas.isEmpty()) {
					continue;
				}

				for (int n = 0; n < respEsperadas.size(); n++) {
					// Sumando a ejes tematicos
					Habilidad habilidad = respEsperadas.get(n).getHabilidad();
					if (!mapEjes.containsKey(habilidad)) {
						List<OTAcumulador> lista = new ArrayList<OTAcumulador>(
								nroCursos);
						for (int idx = 0; idx < nroCursos; idx++) {
							lista.add(null);
						}
						mapEjes.put(habilidad, lista);
					}
					List<OTAcumulador> lstEjes = mapEjes.get(habilidad);
					OTAcumulador otEjeEval = lstEjes.get(index); // Que columna
																	// (curso
																	// es)
					if (otEjeEval == null) {
						otEjeEval = new OTAcumulador();
						int[] nroPersonas = new int[nroRangos];
						Arrays.fill(nroPersonas, 0);
						otEjeEval.setNroPersonas(nroPersonas);
						lstEjes.set(index, otEjeEval);
					}
				}
				for (Habilidad habilidad : mapEjes.keySet()) {
					List<OTAcumulador> lstEjes = mapEjes.get(habilidad);
					OTAcumulador otEjeEval = lstEjes.get(index);
					float porcentaje = obtenerPorcentaje(respuestas,
							respEsperadas, habilidad);

					for (int idx = 0; idx < nroRangos; idx++) {
						RangoEvaluacion rango = rangoEvalList.get(idx);
						if (rango.isInside(porcentaje)) {
							otEjeEval.getNroPersonas()[idx] = otEjeEval
									.getNroPersonas()[idx] + 1;
							break;
						}
					}

					lstEjes.set(index, otEjeEval);
				}

			}
		}

		// Ahora se debe llenar las tablas.
		generarTablaEjes(mapEjes);
	}

	/**
	 * Se genera la tabal que contiene los % de logro por cada habilidad y por
	 * cada habilidad asociado a cada curso.
	 * 
	 * @param mapEjes
	 *            Mapa que contiene los valores para cada curso de los ejes.
	 * @param mapHabilidades
	 *            Mapa que contiene los valores para cada curso de las
	 *            habilidades.
	 */
	private void generarTablaEjes(Map<Habilidad, List<OTAcumulador>> mapEjes) {
		ObservableList<String> row = null;
		ObservableList<ObservableList<String>> items = FXCollections
				.observableArrayList();
		for (Habilidad habilidad : mapEjes.keySet()) {
			row = FXCollections.observableArrayList();
			List<OTAcumulador> lst = mapEjes.get(habilidad);
			row.add(habilidad.getName());
			for (OTAcumulador otEje : lst) {
				if (otEje != null && otEje.getNroPersonas() != null) {
					int[] personas = otEje.getNroPersonas();
					for (int n = 0; n < rangoEvalList.size(); n++) {
						row.add(String.valueOf(personas[n]));
					}
				} else {
					for (int n = 0; n < rangoEvalList.size(); n++) {
						row.add("0");
					}
				}
			}
			items.add(row);
		}
		tblHabilidadesCantidad.setItems(items);

	}

	private float obtenerPorcentaje(String respuestas,
			List<RespuestasEsperadasPrueba> respEsperadas, Habilidad habilidad) {
		float nroBuenas = 0;
		float nroPreguntas = 0;
		for (int n = 0; n < respEsperadas.size(); n++) {
			RespuestasEsperadasPrueba resp = respEsperadas.get(n);
			if (!resp.isAnulada()) {
				if (resp.getHabilidad().equals(habilidad)) {
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
		float porcentaje = nroBuenas / nroPreguntas * 100f;
		return porcentaje;
	}

	private void clearContent() {
		tblHabilidadesCantidad.getItems().clear();
		tblHabilidadesCantidad.getColumns().clear();
		;
	}
}
