package cl.eos.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.controlsfx.dialog.Dialogs;

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
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
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
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.persistence.util.Comparadores;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.Pair;
import cl.eos.util.Utils;

public class ComparativoColegioEjeHabilidadxCursoView extends AFormView
		implements EventHandler<ActionEvent> {

	private static final String ASIGNATURA_ID = "idAsignatura";
	private static final String COLEGIO_ID = "idColegio";
	@FXML
	private TableView tblEjeshabilidades;
	@FXML
	private TableView tblEvaluacion;
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
	private ObservableList<Curso> listaCursos;
	private ObservableList<EvaluacionEjeTematico> rangosEvaluacionPorcentaje;
	private ObservableList<EvaluacionPrueba> listaEvaluacionesPrueba;
	private ArrayList<OTPreguntasEvaluacion> lst;

	public ComparativoColegioEjeHabilidadxCursoView() {
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
		mnuExportarGeneral.setOnAction(this);
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
				listaCursos = FXCollections.observableArrayList();
				for (Object iEntity : list) {
					listaCursos.add((Curso) iEntity);
				}
				FXCollections.sort(listaCursos,
						Comparadores.comparaResumeCurso());
			}
			if (entity instanceof EvaluacionEjeTematico) {
				rangosEvaluacionPorcentaje = FXCollections
						.observableArrayList();
				for (Object object : list) {
					EvaluacionEjeTematico evaluacion = (EvaluacionEjeTematico) object;
					rangosEvaluacionPorcentaje.add(evaluacion);
				}
				generarReporte();
			}
			if (entity instanceof EvaluacionPrueba) {
				listaEvaluacionesPrueba = FXCollections.observableArrayList();
				for (Object object : list) {
					EvaluacionPrueba evaluacion = (EvaluacionPrueba) object;
					listaEvaluacionesPrueba.add(evaluacion);
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
	private void llenarColumnas(ObservableList<Curso> pCursoList) {
		TableColumn tc = new TableColumn("EJE / HABILIDAD");
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
		tblEjeshabilidades.getColumns().add(tc);

		tc = new TableColumn("EVALUACION");
		tc.setStyle("-fx-alignment: CENTER-LEFT;");
		tc.prefWidthProperty().set(250f);
		tc.setSortable(false);
		tc.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(
					CellDataFeatures<ObservableList, String> param) {
				return new SimpleStringProperty(param.getValue().get(0)
						.toString());
			}
		});
		tblEvaluacion.getColumns().add(tc);

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
			tblEjeshabilidades.getColumns().add(tc);

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
			tblEvaluacion.getColumns().add(tc);
			indice++;
		}
	}

	/**
	 * Para ontener el resultado final se siguen los sieguientes pasos: <lu> <li>
	 * Obtener resumen de preguntas buenas de cada Eje/Hab x Alumno. </lu>
	 */
	private void generarReporte() {

		if (listaEvaluacionesPrueba == null
				|| rangosEvaluacionPorcentaje == null) {
			// No hay valores para procesar todo.
			return;
		}

		// Una iteracion por cada curso asociado al colegio con una evaluacion
		int nroCursos = listaEvaluacionesPrueba.size();

		List<OTCursoRangos> listaCursosConRangos = new ArrayList<OTCursoRangos>();
		List<EjeTematico> listaEjesTematicos =  new ArrayList<EjeTematico>();
		List<Habilidad> listaHablidades =  new ArrayList<Habilidad>();
		Map<IEntity, List<OTCursoRangos>> reporte = new HashMap<IEntity, List<OTCursoRangos>>();

		// Una evaluacion corresponde al conjunto de pruebas de un CURSO de un
		// COLEGIO de una ASIGNATURA.
		for (EvaluacionPrueba evaluacion : listaEvaluacionesPrueba) {

			List<RespuestasEsperadasPrueba> rEsperadas = evaluacion.getPrueba()
					.getRespuestas();

			// Obtengo lista de ejes y habilidades de la PRUEBA base de la
			// EVALUACION.
			for (RespuestasEsperadasPrueba rEsperada : rEsperadas) {
				if (!listaEjesTematicos.contains(rEsperada.getEjeTematico())) {
					listaEjesTematicos.add(rEsperada.getEjeTematico());
				}
				if (!listaHablidades.contains(rEsperada.getHabilidad())) {
					listaHablidades.add(rEsperada.getHabilidad());
				}
			}
		}
		for (EvaluacionPrueba evaluacion : listaEvaluacionesPrueba) {

			List<RespuestasEsperadasPrueba> respEsperadas = evaluacion
					.getPrueba().getRespuestas();

			int nroAlumnos = evaluacion.getPruebasRendidas().size();
			List<OTUnCurso> listaOTUnCurso =  new ArrayList<OTUnCurso>();
			
			// Cada prueba rendida equivale a un ALUMNO DEL CURSO
			int n = 0;
			for (PruebaRendida pRendida : evaluacion.getPruebasRendidas()) {
				String respuestas = pRendida.getRespuestas();
				
				for (RespuestasEsperadasPrueba resp : respEsperadas) {
					
					OTUnCurso otEje = new  OTUnCurso(resp.getEjeTematico(), nroAlumnos);
					if(listaOTUnCurso.contains(otEje))
					{
						// Me aseguro que es el que existe
						otEje = listaOTUnCurso.get(listaOTUnCurso.indexOf(otEje));
					}
					else
					{
						// Lo agrego a la lista.
						listaOTUnCurso.add(otEje);
					}
					
					OTUnCurso otHab = new  OTUnCurso(resp.getHabilidad(), nroAlumnos);
					if(listaOTUnCurso.contains(otHab))
					{
						// Me aseguro que es el que existe
						otHab = listaOTUnCurso.get(listaOTUnCurso.indexOf(otHab));
					}
					else
					{
						// Lo agrego a la lista.
						listaOTUnCurso.add(otHab);
					}
					
					otEje.setNroPreguntas(otEje.getNroPreguntas() + 1);
					otHab.setNroPreguntas(otHab.getNroPreguntas() + 1);
					
					if (respuestas.length() < n) {
						// No hay más respuesta
						break;
					}
					String r = respuestas.substring(n, n + 1);

					if (resp.getAnulada() || "O".equals(r) || "-".equals(r)) {
						// La respuesta no se considera
						n++;
						continue;
					}

					// Aqui agrego 1 a las buenas del eje y de la habilidad del alumno n.
					if (resp.getRespuesta().equals(r)) {
						int buenasEje = otEje.getBuenasPorAlumno()[n];
						otEje.getBuenasPorAlumno()[n] = buenasEje + 1;
						int buenasHab = otHab.getBuenasPorAlumno()[n];
						otHab.getBuenasPorAlumno()[n] = buenasHab + 1;
					}
				}
				n++; //Siguiente alumno.
			}
			// Aqui va la siguiente evaluacion (CURSO)
		}

		/*
		 * llenarColumnas(listaCursos); int nroCursos = listaCursos.size();
		 * Map<EjeTematico, List<OTPreguntasEjes>> mapEjes = new
		 * HashMap<EjeTematico, List<OTPreguntasEjes>>(); Map<Habilidad,
		 * List<OTPreguntasHabilidad>> mapHabilidades = new HashMap<Habilidad,
		 * List<OTPreguntasHabilidad>>(); Map<EvaluacionEjeTematico,
		 * List<OTPreguntasEvaluacion>> mapEvaluaciones = new
		 * HashMap<EvaluacionEjeTematico, List<OTPreguntasEvaluacion>>();
		 * 
		 * for (EvaluacionEjeTematico ejetem : rangosEvaluacionPorcentaje) { lst
		 * = new ArrayList<OTPreguntasEvaluacion>(nroCursos); for (int idx = 0;
		 * idx < nroCursos; idx++) { OTPreguntasEvaluacion otEval = new
		 * OTPreguntasEvaluacion(); otEval.setEvaluacion(ejetem); lst.add(idx,
		 * otEval); } mapEvaluaciones.put(ejetem, lst); }
		 * 
		 * int[] totalAlumnos = new int[nroCursos]; Arrays.fill(totalAlumnos,
		 * 0); int[] alumnosEvaluados = new int[nroCursos];
		 * Arrays.fill(alumnosEvaluados, 0);
		 * 
		 * // Todas las evaluaciones asociadas (Todos los cursos) for
		 * (EvaluacionPrueba eval : listaEvaluacionesPrueba) { // Se esta
		 * revisando un curso. eval.getPruebasRendidas().size();
		 * List<PruebaRendida> pruebasRendidas = eval.getPruebasRendidas();
		 * eval.getPrueba().getRespuestas().size();
		 * List<RespuestasEsperadasPrueba> respEsperadas = eval.getPrueba()
		 * .getRespuestas(); // Estamos procesando un curso/una prueba for
		 * (PruebaRendida pruebaRendida : pruebasRendidas) { // Se procesa un
		 * alumno.
		 * 
		 * // Obtengo el index de la columna que tengo que llenar (mas 1 // por
		 * que la primera es de // contenido if (pruebaRendida.getAlumno() ==
		 * null) { continue;
		 * 
		 * }
		 * 
		 * int index = listaCursos.indexOf(pruebaRendida.getAlumno()
		 * .getCurso());
		 * 
		 * if (index == -1) { continue; } totalAlumnos[index] =
		 * pruebaRendida.getAlumno().getCurso() .getAlumnos().size();
		 * alumnosEvaluados[index] = alumnosEvaluados[index] + 1;
		 * 
		 * String respuestas = pruebaRendida.getRespuestas(); if (respuestas ==
		 * null || respuestas.isEmpty()) { continue; }
		 * 
		 * for (int n = 0; n < respEsperadas.size(); n++) { // Sumando a ejes
		 * tematicos EjeTematico eje = respEsperadas.get(n).getEjeTematico(); if
		 * (!mapEjes.containsKey(eje)) { List<OTPreguntasEjes> lista = new
		 * ArrayList<OTPreguntasEjes>(); for (int idx = 0; idx < nroCursos;
		 * idx++) { lista.add(null); } mapEjes.put(eje, lista); }
		 * List<OTPreguntasEjes> lstEjes = mapEjes.get(eje); OTPreguntasEjes otE
		 * = lstEjes.get(index); // Se obtiene el // valor // asociado a la //
		 * columna. if (otE == null) { otE = new OTPreguntasEjes();
		 * otE.setEjeTematico(eje); lstEjes.set(index, otE); } Pair<Integer,
		 * Integer> buenasTotal = obtenerBuenasTotales( respuestas,
		 * respEsperadas, eje); otE.setBuenas(otE.getBuenas() +
		 * buenasTotal.getFirst()); otE.setTotal(otE.getTotal() +
		 * buenasTotal.getSecond()); lstEjes.set(index, otE);
		 * 
		 * // Sumando a habilidades Habilidad hab =
		 * respEsperadas.get(n).getHabilidad(); System.out.println(hab); if
		 * (!mapHabilidades.containsKey(hab)) {
		 * 
		 * List<OTPreguntasHabilidad> lista = new
		 * ArrayList<OTPreguntasHabilidad>(); for (int idx = 0; idx < nroCursos;
		 * idx++) { lista.add(null); } mapHabilidades.put(hab, lista); }
		 * List<OTPreguntasHabilidad> lstHabilidades = mapHabilidades .get(hab);
		 * OTPreguntasHabilidad otH = lstHabilidades.get(index);// Se // obtiene
		 * // el // valor // asociado // a // la // columna if (otH == null) {
		 * otH = new OTPreguntasHabilidad(); otH.setHabilidad(hab);
		 * lstHabilidades.set(index, otH); } buenasTotal =
		 * obtenerBuenasTotales(respuestas, respEsperadas, hab);
		 * otH.setBuenas(otE.getBuenas() + buenasTotal.getFirst());
		 * otH.setTotal(otE.getTotal() + buenasTotal.getSecond());
		 * lstHabilidades.set(index, otH);
		 * 
		 * } for (EvaluacionEjeTematico ejetem : rangosEvaluacionPorcentaje) {
		 * if (ejetem.isInside(pruebaRendida.getPbuenas())) {
		 * List<OTPreguntasEvaluacion> lstOt = mapEvaluaciones .get(ejetem);
		 * OTPreguntasEvaluacion ot = lstOt.get(index);
		 * ot.setAlumnos(ot.getAlumnos() + 1); break; } } } }
		 * 
		 * // Ahora se debe llenar las tablas.
		 * generarTablaEjesHabilidades(mapEjes, mapHabilidades);
		 * generarTablaEvaluaciones(mapEvaluaciones, totalAlumnos,
		 * alumnosEvaluados);
		 */

	}

	/**
	 * Se obtiene la lista de ejes/habilidades existentes en una prueba de un
	 * curso con los siguientes atributos asociados. Eje/Habilidad Numero
	 * Preguntas asociadas Lista de alumnos con las preguntas buenas por cada
	 * eje.
	 */
	private void obtenerResPregBuenasEHxAlumno() {
		// TODO Auto-generated method stub

	}

	private void generarTablaEvaluaciones(
			Map<EvaluacionEjeTematico, List<OTPreguntasEvaluacion>> mapEvaluaciones,
			int[] totalAlumnos, int[] alumnosEvaluados) {
		ObservableList<String> row = null;
		ObservableList<ObservableList<String>> items = FXCollections
				.observableArrayList();
		Collections.sort(rangosEvaluacionPorcentaje,
				Comparadores.comparaEvaluacionEjeTematico());

		for (EvaluacionEjeTematico eval : rangosEvaluacionPorcentaje) {
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
	 * habilidad asociado a cada curso.
	 * 
	 * @param mapEjes
	 *            Mapa que contiene los valores para cada curso de los ejes.
	 * @param mapHabilidades
	 *            Mapa que contiene los valores para cada curso de las
	 *            habilidades.
	 */
	private void generarTablaEjesHabilidades(
			Map<EjeTematico, List<OTPreguntasEjes>> mapEjes,
			Map<Habilidad, List<OTPreguntasHabilidad>> mapHabilidades) {
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
		row = FXCollections.observableArrayList();
		for (int m = 0; m <= nroCols; m++) {
			row.add(" ");
		}
		items.add(row);

		for (Habilidad hab : mapHabilidades.keySet()) {
			row = FXCollections.observableArrayList();
			List<OTPreguntasHabilidad> lst = mapHabilidades.get(hab);
			row.add(hab.getName());
			for (OTPreguntasHabilidad ot : lst) {
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

	private void clearContent() {
		tblEjeshabilidades.getItems().clear();
		tblEvaluacion.getItems().clear();
		tblEjeshabilidades.getColumns().clear();
		tblEvaluacion.getColumns().clear();
	}
}
