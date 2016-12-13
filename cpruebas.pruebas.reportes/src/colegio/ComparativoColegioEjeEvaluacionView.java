package colegio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.persistence.models.SAsignatura;
import cl.eos.persistence.models.SColegio;
import cl.eos.persistence.models.SCurso;
import cl.eos.persistence.models.SEjeTematico;
import cl.eos.persistence.models.SEvaluacionPrueba;
import cl.eos.persistence.models.SPruebaRendida;
import cl.eos.persistence.models.SRangoEvaluacion;
import cl.eos.persistence.models.SRespuestasEsperadasPrueba;
import cl.eos.persistence.models.STipoAlumno;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class ComparativoColegioEjeEvaluacionView extends AFormView implements EventHandler<ActionEvent> {

	private static final String ASIGNATURA_ID = "idAsignatura";
	private static final String COLEGIO_ID = "idColegio";
	@FXML
	private TableView<ObservableList<String>> tblEjesCantidad;
	@FXML
	private ComboBox<SColegio> cmbColegios;
	@FXML
	private ComboBox<SAsignatura> cmbAsignatura;
	@FXML
	private ComboBox<STipoAlumno> cmbTipoAlumno;
	@FXML
	private Button btnReportes;
	@FXML
	private Label lblColegio;
	@FXML
	private Label lblTitulo;
	@FXML
	private MenuItem mnuExportarAlumnos;

	private Map<String, Object> parameters = new HashMap<String, Object>();
	private ObservableList<SCurso> cursoList;
	private ObservableList<SRangoEvaluacion> rangoEvalList;
	private ObservableList<SEvaluacionPrueba> evaluacionesPrueba;

	public ComparativoColegioEjeEvaluacionView() {
		setTitle("Comparativo Colegios Ejes");
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

		if (source == mnuExportarAlumnos) {

			tblEjesCantidad.setId("Comparativo Colegios Ejes");
			List<TableView<? extends Object>> listaTablas = new ArrayList<>();
			listaTablas.add((TableView<? extends Object>) tblEjesCantidad);
			ExcelSheetWriterObj.convertirDatosColumnasDoblesALibroDeExcel(listaTablas);
		}
	}

	private void handleColegios() {
		SColegio colegio = cmbColegios.getSelectionModel().getSelectedItem();
		if (colegio != null) {
			parameters.put(COLEGIO_ID, colegio.getId());
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("colegioId", colegio.getId());
			lblTitulo.setText(colegio.getName());
			controller.find("SCurso.findByColegio", param);
			clearContent();
		}
	}

	private void handleAsignatura() {
		SAsignatura asignatura = cmbAsignatura.getSelectionModel().getSelectedItem();
		if (asignatura != null) {
			parameters.put(ASIGNATURA_ID, asignatura.getId());
			clearContent();
		}
	}

	private void handleReportes() {
		if (!parameters.isEmpty() && parameters.containsKey(COLEGIO_ID) && parameters.containsKey(ASIGNATURA_ID)) {

			controller.find("SEvaluacionPrueba.findEvaluacionByColegioAsig", parameters, this);
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
		if (list == null || list.isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("No hay registros.");
			alert.setHeaderText(this.getName());
			alert.setContentText("No se ha encontrado registros para la consulta.");
			alert.showAndWait();			
			return;
		}
		Object entity = list.get(0);
		if (entity instanceof SColegio) {
			ObservableList<SColegio> oList = FXCollections.observableArrayList();
			for (Object iEntity : list) {
				oList.add((SColegio) iEntity);
			}
			cmbColegios.setItems(oList);
		}
		if (entity instanceof SAsignatura) {
			ObservableList<SAsignatura> oList = FXCollections.observableArrayList();
			for (Object iEntity : list) {
				oList.add((SAsignatura) iEntity);
			}
			cmbAsignatura.setItems(oList);
		}
		if (entity instanceof SCurso) {
			cursoList = FXCollections.observableArrayList();
			for (Object iEntity : list) {
				cursoList.add((SCurso) iEntity);
			}
			FXCollections.sort(cursoList, Comparadores.comparaResumeCurso());
		}
		if (entity instanceof STipoAlumno) {
			ObservableList<STipoAlumno> tAlumnoList = FXCollections.observableArrayList();
			for (Object iEntity : list) {
				tAlumnoList.add((STipoAlumno) iEntity);
			}
			cmbTipoAlumno.setItems(tAlumnoList);
		}
		if (entity instanceof SEvaluacionPrueba) {
			evaluacionesPrueba = FXCollections.observableArrayList();
			for (Object object : list) {
				SEvaluacionPrueba evaluacion = (SEvaluacionPrueba) object;
				evaluacionesPrueba.add(evaluacion);
			}
			SEvaluacionPrueba evaluacionPrueba = (SEvaluacionPrueba) entity;
			rangoEvalList = FXCollections.observableArrayList();
			Collection<SRangoEvaluacion> rngs = evaluacionPrueba.getPrueba().getNivelEvaluacion().getRangos();
			for (SRangoEvaluacion rng : rngs) {
				rangoEvalList.add(rng);
			}
			generarReporte();
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
	private void llenarColumnas(ObservableList<SCurso> pCursoList, ObservableList<SRangoEvaluacion> rangos) {
		TableColumn tc = new TableColumn("EJES");
		tc.setSortable(false);
		tc.setStyle("-fx-alignment: CENTER-LEFT;");
		tc.prefWidthProperty().set(250f);
		tc.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
				return new SimpleStringProperty(param.getValue().get(0).toString());
			}
		});
		tblEjesCantidad.getColumns().add(tc);

		int indice = 1;
		for (SCurso curso : pCursoList) {
			tc = new TableColumn(curso.getName());
			tc.prefWidthProperty().set(50f);
			tc.setStyle("-fx-alignment: CENTER;");
			tc.setSortable(false);
			tc.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
				public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
					return new SimpleStringProperty(param.getValue().get(0).toString());
				}
			});
			// Estoy agregando subcolumnas
			for (SRangoEvaluacion rng : rangos) {
				final int lIdx = indice;
				TableColumn stc = new TableColumn(rng.getAbreviacion());
				stc.prefWidthProperty().set(50f);
				stc.setStyle("-fx-alignment: CENTER;");
				stc.setSortable(false);
				stc.setCellValueFactory(
						new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
							public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
								return new SimpleStringProperty(param.getValue().get(lIdx).toString());
							}
						});
				tc.getColumns().add(stc);
				indice++;
			}

			tblEjesCantidad.getColumns().add(tc);

		}
	}

	/**
	 * Aqui se llenan las tablas con los valores correspondientes.<br>
	 * 1) Se obtienen los ejes tematicos de todas las pruebas.<br>
	 * 2) Se obtienen las habilidades de todas las pruebas.<br>
	 * 3) Se obtienen los porcentajes de aprobacion de cada colegio con respecto a
	 * cada eje y habilidad.
	 */
	private void generarReporte() {

		if (evaluacionesPrueba == null || rangoEvalList == null) {
			return;
		}

		llenarColumnas(cursoList, rangoEvalList);
		int nroCursos = cursoList.size();
		int nroRangos = rangoEvalList.size();
		Map<SEjeTematico, List<OTAcumulador>> mapEjes = new HashMap<>();

		/*
		 * Aqui verificamos el TIPO ALUMNO SELECCIONADO PARA EL REPORTE
		 */
		long tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedItem().getId();

		// Todas las evaluaciones asociadas (Todos los cursos)
		for (SEvaluacionPrueba eval : evaluacionesPrueba) {
			// Se esta revisando un colegio.
			eval.getPruebasRendidas().size();
			List<SPruebaRendida> pruebasRendidas = eval.getPruebasRendidas();
			eval.getPrueba().getRespuestas().size();
			List<SRespuestasEsperadasPrueba> respEsperadas = eval.getPrueba().getRespuestas();
			// Estamos procesando un colegio/una prueba
			for (SPruebaRendida pruebaRendida : pruebasRendidas) {
				// Se procesa un alumno.

				// Obtengo el index de la columna que tengo que llenar (mas 1
				// por que la primera es de contenido
				// index * nroRangos Ya que cada colegio tiene nroRangos columnas
				// asociadas.
				if (pruebaRendida.getAlumno() == null) {
					// Caso especial que indica que la prueba esta sin alumno.
					continue;
				}

				if (tipoAlumno != Constants.PIE_ALL
						&& tipoAlumno != pruebaRendida.getAlumno().getTipoAlumno().getId()) {
					// En este caso, no se considera este alumno para el
					// cálculo.
					continue;
				}

				int index = cursoList.indexOf(pruebaRendida.getAlumno().getCurso());

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
					SEjeTematico eje = respEsperadas.get(n).getEjeTematico();
					if (!mapEjes.containsKey(eje)) {
						List<OTAcumulador> lista = new ArrayList<OTAcumulador>(nroCursos);
						for (int idx = 0; idx < nroCursos; idx++) {
							lista.add(null);
						}
						mapEjes.put(eje, lista);
					}
					List<OTAcumulador> lstEjes = mapEjes.get(eje);
					OTAcumulador otEjeEval = lstEjes.get(index); // Que columna
																	// (colegio
																	// es)
					if (otEjeEval == null) {
						otEjeEval = new OTAcumulador();
						int[] nroPersonas = new int[nroRangos];
						Arrays.fill(nroPersonas, 0);
						otEjeEval.setNroPersonas(nroPersonas);
						lstEjes.set(index, otEjeEval);
					}
				}
				for (SEjeTematico eje : mapEjes.keySet()) {
					List<OTAcumulador> lstEjes = mapEjes.get(eje);
					OTAcumulador otEjeEval = lstEjes.get(index);
					float porcentaje = obtenerPorcentaje(respuestas, respEsperadas, eje);

					for (int idx = 0; idx < nroRangos; idx++) {
						SRangoEvaluacion rango = rangoEvalList.get(idx);
						if (rango.isInside(porcentaje)) {
							otEjeEval.getNroPersonas()[idx] = otEjeEval.getNroPersonas()[idx] + 1;
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
	 * Se genera la tabal que contiene los % de logro por cada eje y por cada
	 * habilidad asociado a cada colegio.
	 * 
	 * @param mapEjes
	 *            Mapa que contiene los valores para cada colegio de los ejes.
	 * @param mapHabilidades
	 *            Mapa que contiene los valores para cada colegio de las
	 *            habilidades.
	 */
	private void generarTablaEjes(Map<SEjeTematico, List<OTAcumulador>> mapEjes) {
		ObservableList<String> row = null;
		ObservableList<ObservableList<String>> items = FXCollections.observableArrayList();
		for (SEjeTematico eje : mapEjes.keySet()) {
			row = FXCollections.observableArrayList();
			List<OTAcumulador> lst = mapEjes.get(eje);
			row.add(eje.getName());
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
		tblEjesCantidad.setItems(items);

	}

	private float obtenerPorcentaje(String respuestas, List<SRespuestasEsperadasPrueba> respEsperadas, SEjeTematico eje) {
		float nroBuenas = 0;
		float nroPreguntas = 0;
		for (int n = 0; n < respEsperadas.size(); n++) {
			SRespuestasEsperadasPrueba resp = respEsperadas.get(n);
			if (!resp.isAnulada()) {

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
		}
		float porcentaje = nroBuenas / nroPreguntas * 100f;
		return porcentaje;
	}

	private void clearContent() {
		tblEjesCantidad.getItems().clear();
		tblEjesCantidad.getColumns().clear();
		;
	}
}
