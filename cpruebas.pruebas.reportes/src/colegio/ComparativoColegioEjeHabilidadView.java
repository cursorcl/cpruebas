package colegio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.ot.OTPreguntasEjes;
import cl.eos.ot.OTPreguntasEvaluacion;
import cl.eos.ot.OTPreguntasHabilidad;
import cl.eos.persistence.models.SAlumno;
import cl.eos.persistence.models.SAsignatura;
import cl.eos.persistence.models.SColegio;
import cl.eos.persistence.models.SCurso;
import cl.eos.persistence.models.SEjeTematico;
import cl.eos.persistence.models.SEvaluacionEjeTematico;
import cl.eos.persistence.models.SEvaluacionPrueba;
import cl.eos.persistence.models.SHabilidad;
import cl.eos.persistence.models.SPruebaRendida;
import cl.eos.persistence.models.SRespuestasEsperadasPrueba;
import cl.eos.persistence.models.STipoAlumno;
import cl.eos.persistence.util.Comparadores;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.Pair;
import cl.eos.util.Utils;
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

@SuppressWarnings("rawtypes")
public class ComparativoColegioEjeHabilidadView extends AFormView implements EventHandler<ActionEvent> {

	private Logger log = Logger.getLogger(ComparativoColegioEjeHabilidadView.class.getName());
	private static final String ASIGNATURA_ID = "idAsignatura";
	private static final String COLEGIO_ID = "idColegio";
	
    @FXML
	private TableView tblEjeshabilidades;
	@FXML
	private TableView tblEvaluacion;
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
	private MenuItem mnuExportarGeneral;
	@FXML
	private MenuItem mnuExportarAlumnos;

	private Map<String, Object> parameters = new HashMap<String, Object>();
	private ObservableList<SCurso> cursoList;
	private ObservableList<SEvaluacionEjeTematico> evalEjeTematicoList;
	private ObservableList<SEvaluacionPrueba> evaluacionesPrueba;
	private ArrayList<OTPreguntasEvaluacion> lst;

	public ComparativoColegioEjeHabilidadView() {
		setTitle("Comparativo SColegio Ejes Temáticos y Habilidades");
	}

	@SuppressWarnings("unchecked")
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

			tblEjeshabilidades.setId("Comparativo Ejes y Habilidades");
			tblEvaluacion.setId("Rango de Evaluaciones");
			//
			List<TableView<? extends Object>> listaTablas = new ArrayList<>();
			listaTablas.add((TableView<? extends Object>) tblEjeshabilidades);
			listaTablas.add((TableView<? extends Object>) tblEvaluacion);
			ExcelSheetWriterObj.convertirDatosALibroDeExcel(listaTablas);
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
		mnuExportarGeneral.setOnAction(this);
	}

	@Override
	public void onDataArrived(List<Object> list) {
		if (list != null && !list.isEmpty()) {
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
			if (entity instanceof SEvaluacionEjeTematico) {
				evalEjeTematicoList = FXCollections.observableArrayList();
				for (Object object : list) {
					SEvaluacionEjeTematico evaluacion = (SEvaluacionEjeTematico) object;
					evalEjeTematicoList.add(evaluacion);
				}
				generarReporte();
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
				generarReporte();
			}
		} else if (list != null && list.isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("No hay registros.");
			alert.setHeaderText(null);
			alert.setContentText("No se ha encontrado registros para la consulta.");
			alert.showAndWait();
		}
	}

	/**
	 * Este metodo coloca las columnas a las dos tablas de la HMI. Coloca los
	 * cursos que estan asociados al colegio, independiente que tenga o no
	 * evaluaciones.
	 * 
	 * @param pCursoList
	 */
	@SuppressWarnings({ "unchecked" })
	private void llenarColumnas(ObservableList<SCurso> pCursoList) {
		TableColumn tc = new TableColumn("EJE / HABILIDAD");
		tc.setSortable(false);
		tc.setStyle("-fx-alignment: CENTER-LEFT;");
		tc.prefWidthProperty().set(250f);
		tc.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
				return new SimpleStringProperty(param.getValue().get(0).toString());
			}
		});
		tblEjeshabilidades.getColumns().add(tc);

		tc = new TableColumn("EVALUACION");
		tc.setStyle("-fx-alignment: CENTER-LEFT;");
		tc.prefWidthProperty().set(250f);
		tc.setSortable(false);
		tc.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
				return new SimpleStringProperty(param.getValue().get(0).toString());
			}
		});
		tblEvaluacion.getColumns().add(tc);

		int indice = 1;
		for (SCurso curso : pCursoList) {
			final int idx = indice;
			tc = new TableColumn(curso.getName());
			tc.prefWidthProperty().set(50f);
			tc.setStyle("-fx-alignment: CENTER;");
			tc.setSortable(false);
			tc.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
				public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
					return new SimpleStringProperty(param.getValue().get(idx).toString());
				}
			});
			tblEjeshabilidades.getColumns().add(tc);

			tc = new TableColumn(curso.getName());
			tc.prefWidthProperty().set(50f);
			tc.setStyle("-fx-alignment: CENTER;");
			tc.setSortable(false);
			tc.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
				public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
					return new SimpleStringProperty(param.getValue().get(idx).toString());
				}
			});
			tblEvaluacion.getColumns().add(tc);
			indice++;
		}
	}

	/**
	 * Aqui se llenan las tablas con los valores correspondientes.<br>
	 * 1) Se obtienen los ejes tematicos de todas las pruebas.<br>
	 * 2) Se obtienen las habilidades de todas las pruebas.<br>
	 * 3) Se obtienen los porcentajes de aprobacion de de cada colegio con
	 * respecto a cada eje y habilidad.
	 */
	private void generarReporte() {

		if (evaluacionesPrueba == null || evalEjeTematicoList == null) {
			return;
		}

		long tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedItem().getId();

		llenarColumnas(cursoList);
		int nroCursos = cursoList.size();
		Map<SEjeTematico, List<OTPreguntasEjes>> mapEjes = new HashMap<>();
		Map<SHabilidad, List<OTPreguntasHabilidad>> mapHabilidades = new HashMap<>();
		Map<SEvaluacionEjeTematico, List<OTPreguntasEvaluacion>> mapEvaluaciones = new HashMap<>();

		for (SEvaluacionEjeTematico ejetem : evalEjeTematicoList) {
			lst = new ArrayList<>(nroCursos);
			for (int idx = 0; idx < nroCursos; idx++) {
				OTPreguntasEvaluacion otEval = new OTPreguntasEvaluacion();
				otEval.setEvaluacion(ejetem);
				lst.add(idx, otEval);
			}
			mapEvaluaciones.put(ejetem, lst);
		}

		int[] totalAlumnos = new int[nroCursos];
		Arrays.fill(totalAlumnos, 0);
		int[] alumnosEvaluados = new int[nroCursos];
		Arrays.fill(alumnosEvaluados, 0);

		// Todas las evaluaciones asociadas (Todos los cursos)
		for (SEvaluacionPrueba eval : evaluacionesPrueba) {

			// Se esta revisando un colegio.
			eval.getPruebasRendidas().size();
			List<SPruebaRendida> pruebasRendidas = eval.getPruebasRendidas();
			eval.getPrueba().getRespuestas().size();
			List<SRespuestasEsperadasPrueba> respEsperadas = eval.getPrueba().getRespuestas();
			// Estamos procesando un colegio/una prueba

			// Obtengo el index de la columna que tengo que llenar (mas 1
			// por que la primera es de
			// contenido
			int index = cursoList.indexOf(eval.getCurso());

			if (index == -1) {
				continue;
			}
			totalAlumnos[index] = 0;

			// Obtengo los items a considerar en el caso que hayan items
			// PIE.
			for (SAlumno alumno : eval.getCurso().getAlumnos()) {
				if (tipoAlumno == Constants.PIE_ALL || alumno.getTipoAlumno().getId().equals(tipoAlumno)) {
					// le quito 1 al total de items, ya que este alumno no es
					// del grupo que sequiere representar en el reporte.
					totalAlumnos[index] = totalAlumnos[index] + 1;
				}
			}

			for (SPruebaRendida pruebaRendida : pruebasRendidas) {
				// Se procesa un alumno.
				if (tipoAlumno != Constants.PIE_ALL
						&& !pruebaRendida.getAlumno().getTipoAlumno().getId().equals(tipoAlumno)) {
					continue;
				}

				alumnosEvaluados[index] = alumnosEvaluados[index] + 1;

				String respuestas = pruebaRendida.getRespuestas();
				if (respuestas == null || respuestas.isEmpty()) {
					continue;
				}

				// Obtener ejes y habilidades de esta prueba
				for (int n = 0; n < respEsperadas.size(); n++) {
					SEjeTematico eje = respEsperadas.get(n).getEjeTematico();
					if (!mapEjes.containsKey(eje)) {
						List<OTPreguntasEjes> lista = new ArrayList<>();
						for (int idx = 0; idx < nroCursos; idx++) {
							lista.add(null);
						}
						mapEjes.put(eje, lista);
					}
					SHabilidad hab = respEsperadas.get(n).getHabilidad();
					if (!mapHabilidades.containsKey(hab)) {

						List<OTPreguntasHabilidad> lista = new ArrayList<>();
						for (int idx = 0; idx < nroCursos; idx++) {
							lista.add(null);
						}
						mapHabilidades.put(hab, lista);
					}
				}

				for (SEjeTematico eje : mapEjes.keySet()) {
					List<OTPreguntasEjes> lstEjes = mapEjes.get(eje);
					OTPreguntasEjes otEje = lstEjes.get(index); // Se obtiene el
																// asociado a la
																// columna.
					if (otEje == null) {
						otEje = new OTPreguntasEjes();
						otEje.setEjeTematico(eje);
						lstEjes.set(index, otEje);
					}
					Pair<Integer, Integer> buenasTotal = obtenerBuenasTotales(respuestas, respEsperadas, eje);

					otEje.setBuenas(otEje.getBuenas() + buenasTotal.getFirst());
					otEje.setTotal(otEje.getTotal() + buenasTotal.getSecond());
					lstEjes.set(index, otEje);
				}

				for (SHabilidad hab : mapHabilidades.keySet()) {
					List<OTPreguntasHabilidad> lstHabs = mapHabilidades.get(hab);

					// Se obtiene el asociado a la columna.
					OTPreguntasHabilidad otHabilidad = lstHabs.get(index);
					if (otHabilidad == null) {
						otHabilidad = new OTPreguntasHabilidad();
						otHabilidad.setHabilidad(hab);
						lstHabs.set(index, otHabilidad);
					}
					Pair<Integer, Integer> buenasTotal = obtenerBuenasTotales(respuestas, respEsperadas, hab);
					otHabilidad.setBuenas(otHabilidad.getBuenas() + buenasTotal.getFirst());
					otHabilidad.setTotal(otHabilidad.getTotal() + buenasTotal.getSecond());
					log.fine(String.format("HAB: %s %d/%d  ACUM: %d/%d", hab.getName(), buenasTotal.getFirst(),
							buenasTotal.getSecond(), otHabilidad.getBuenas(), otHabilidad.getTotal()));
					lstHabs.set(index, otHabilidad);
				}

				for (SEvaluacionEjeTematico ejetem : evalEjeTematicoList) {
					if (ejetem.isInside(pruebaRendida.getPbuenas())) {
						List<OTPreguntasEvaluacion> lstOt = mapEvaluaciones.get(ejetem);
						OTPreguntasEvaluacion ot = lstOt.get(index);
						ot.setAlumnos(ot.getAlumnos() + 1);
						break;
					}
				}
			}
		}

		// Ahora se debe llenar las tablas.
		generarTablaEjesHabilidades(mapEjes, mapHabilidades);
		generarTablaEvaluaciones(mapEvaluaciones, totalAlumnos, alumnosEvaluados);

	}

	@SuppressWarnings("unchecked")
    private void generarTablaEvaluaciones(Map<SEvaluacionEjeTematico, List<OTPreguntasEvaluacion>> mapEvaluaciones,
			int[] totalAlumnos, int[] alumnosEvaluados) {
		ObservableList<String> row = null;
		ObservableList<ObservableList<String>> items = FXCollections.observableArrayList();
		Collections.sort(evalEjeTematicoList, Comparadores.comparaEvaluacionEjeTematico());

		for (SEvaluacionEjeTematico eval : evalEjeTematicoList) {
			row = FXCollections.observableArrayList();
			List<OTPreguntasEvaluacion> lst = mapEvaluaciones.get(eval);
			row.add(eval.getName());
			for (OTPreguntasEvaluacion ot : lst) {
				if (ot != null && ot.getAlumnos() != null) {
					row.add(String.valueOf(ot.getAlumnos()));
				} else {
					row.add(" ");
				}
			}
			items.add(row);
		}
		row = FXCollections.observableArrayList();
		for (int m = 0; m <= alumnosEvaluados.length; m++) {
			row.add(" ");
		}
		items.add(row);

		row = FXCollections.observableArrayList();
		row.add("EVALUADOS");
		for (int val : alumnosEvaluados) {
			row.add(String.valueOf(val));
		}
		items.add(row);

		row = FXCollections.observableArrayList();
		row.add("TOTAL");
		for (int val : totalAlumnos) {
			row.add(String.valueOf(val));
		}
		items.add(row);

		tblEvaluacion.setItems(items);
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
	@SuppressWarnings("unchecked")
    private void generarTablaEjesHabilidades(Map<SEjeTematico, List<OTPreguntasEjes>> mapEjes,
			Map<SHabilidad, List<OTPreguntasHabilidad>> mapHabilidades) {
		ObservableList<String> row = null;
		ObservableList<ObservableList<String>> items = FXCollections.observableArrayList();
		int nroCols = 0;
		for (SEjeTematico eje : mapEjes.keySet()) {
			row = FXCollections.observableArrayList();
			List<OTPreguntasEjes> lst = mapEjes.get(eje);
			nroCols = lst.size();
			row.add(eje.getName());
			for (OTPreguntasEjes ot : lst) {
				if (ot != null && ot.getBuenas() != null && ot.getTotal() != null && ot.getTotal() != 0F) {
					float porcentaje = ot.getBuenas().floatValue() / ot.getTotal().floatValue() * 100f;
					row.add(String.valueOf(Utils.redondeo1Decimal(porcentaje)));
				} else {
					row.add(" ");
				}
			}
			items.add(row);
		}
		row = FXCollections.observableArrayList();
		for (int m = 0; m <= nroCols; m++) {
			row.add(" ");
		}
		items.add(row);

		for (SHabilidad hab : mapHabilidades.keySet()) {
			row = FXCollections.observableArrayList();
			List<OTPreguntasHabilidad> lst = mapHabilidades.get(hab);
			row.add(hab.getName());
			for (OTPreguntasHabilidad ot : lst) {
				if (ot != null && ot.getBuenas() != null && ot.getTotal() != null && ot.getTotal() != 0F) {
					float porcentaje = ot.getBuenas().floatValue() / ot.getTotal().floatValue() * 100f;
					row.add(String.valueOf(Utils.redondeo1Decimal(porcentaje)));
				} else {
					row.add(" ");
				}
			}
			items.add(row);
		}

		tblEjeshabilidades.setItems(items);

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
	 *            La SHabilidad en base al que se realiza el calculo.
	 * @return Par <Preguntas buenas, Total de Preguntas> del eje.
	 */
	private Pair<Integer, Integer> obtenerBuenasTotales(String respuestas,
			List<SRespuestasEsperadasPrueba> respEsperadas, SHabilidad hab) {
		int nroBuenas = 0;
		int nroPreguntas = 0;
		for (int n = 0; n < respEsperadas.size(); n++) {
			SRespuestasEsperadasPrueba resp = respEsperadas.get(n);
			if (!resp.isAnulada()) {
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
			List<SRespuestasEsperadasPrueba> respEsperadas, SEjeTematico eje) {
		int nroBuenas = 0;
		int nroPreguntas = 0;
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
		return new Pair<Integer, Integer>(nroBuenas, nroPreguntas);
	}

	private void clearContent() {
		tblEjeshabilidades.getItems().clear();
		tblEvaluacion.getItems().clear();
		tblEjeshabilidades.getColumns().clear();
		;
		tblEvaluacion.getColumns().clear();
	}
}
