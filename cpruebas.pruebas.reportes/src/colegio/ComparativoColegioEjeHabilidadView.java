package colegio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.imp.view.ProgressForm;
import cl.eos.ot.OTPreguntasEjes;
import cl.eos.ot.OTPreguntasEvaluacion;
import cl.eos.ot.OTPreguntasHabilidad;
import cl.eos.persistence.util.Comparadores;
import cl.eos.restful.tables.R_Alumno;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_Ejetematico;
import cl.eos.restful.tables.R_EvaluacionEjetematico;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_Habilidad;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.MapBuilder;
import cl.eos.util.Pair;
import cl.eos.util.Utils;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
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
  private static final String ASIGNATURA_ID = "asignatura_id";
  private static final String COLEGIO_ID = "colegio_id";

  @FXML
  private TableView tblEjeshabilidades;
  @FXML
  private TableView tblEvaluacion;
  @FXML
  private ComboBox<R_Colegio> cmbColegios;
  @FXML
  private ComboBox<R_Asignatura> cmbAsignatura;
  @FXML
  private ComboBox<R_TipoAlumno> cmbTipoAlumno;

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
  private ObservableList<R_Curso> cursoList;
  private ObservableList<R_EvaluacionEjetematico> evalEjeTematicoList;
  private ObservableList<R_EvaluacionPrueba> evaluacionesPrueba;
  private ArrayList<OTPreguntasEvaluacion> lst;
  private R_Prueba prueba;
  private List<R_RespuestasEsperadasPrueba> respEsperadas;

  public ComparativoColegioEjeHabilidadView() {
    setTitle("Comparativo Colegio Ejes Temáticos y Habilidades");
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
    R_Colegio colegio = cmbColegios.getSelectionModel().getSelectedItem();
    if (colegio != null) {
      parameters.put(COLEGIO_ID, colegio.getId());
      Map<String, Object> param = new HashMap<String, Object>();
      param.put("colegio_id", colegio.getId());
      lblTitulo.setText(colegio.getName());
      controller.findByParam(R_Curso.class, param, this);
      clearContent();
    }
  }

  private void handleAsignatura() {
    R_Asignatura asignatura = cmbAsignatura.getSelectionModel().getSelectedItem();
    if (asignatura != null) {
      parameters.put(ASIGNATURA_ID, asignatura.getId());
      clearContent();
    }
  }

  private void handleReportes() {
    if (!parameters.isEmpty() && parameters.containsKey(COLEGIO_ID) && parameters.containsKey(ASIGNATURA_ID)) {

      controller.findByParam(R_EvaluacionPrueba.class, parameters, this);
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
      if (entity instanceof R_Colegio) {
        ObservableList<R_Colegio> oList = FXCollections.observableArrayList();
        for (Object iEntity : list) {
          oList.add((R_Colegio) iEntity);
        }
        cmbColegios.setItems(oList);
      }
      if (entity instanceof R_Asignatura) {
        ObservableList<R_Asignatura> oList = FXCollections.observableArrayList();
        for (Object iEntity : list) {
          oList.add((R_Asignatura) iEntity);
        }
        cmbAsignatura.setItems(oList);
      }
      if (entity instanceof R_Curso) {
        cursoList = FXCollections.observableArrayList();
        for (Object iEntity : list) {
          cursoList.add((R_Curso) iEntity);
        }
        FXCollections.sort(cursoList, Comparadores.comparaResumeCurso());
      }
      if (entity instanceof R_EvaluacionEjetematico) {
        evalEjeTematicoList = FXCollections.observableArrayList();
        for (Object object : list) {
          R_EvaluacionEjetematico evaluacion = (R_EvaluacionEjetematico) object;
          evalEjeTematicoList.add(evaluacion);
        }
        generarReporte();
      }
      if (entity instanceof R_TipoAlumno) {
        ObservableList<R_TipoAlumno> tAlumnoList = FXCollections.observableArrayList();
        for (Object iEntity : list) {
          tAlumnoList.add((R_TipoAlumno) iEntity);
        }
        cmbTipoAlumno.setItems(tAlumnoList);
      }
      if (entity instanceof R_EvaluacionPrueba) {
        evaluacionesPrueba = FXCollections.observableArrayList();
        for (Object object : list) {
          R_EvaluacionPrueba evaluacion = (R_EvaluacionPrueba) object;
          evaluacionesPrueba.add(evaluacion);
        }
        generarReporte();
      }

      if (entity instanceof R_RespuestasEsperadasPrueba) {
        respEsperadas = FXCollections.observableArrayList();
        for (Object object : list) {
          R_RespuestasEsperadasPrueba evaluacion = (R_RespuestasEsperadasPrueba) object;
          respEsperadas.add(evaluacion);
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
   * @return the prueba
   */
  public final R_Prueba getPrueba() {
    return prueba;
  }

  /**
   * @param prueba the prueba to set
   */
  public final void setPrueba(R_Prueba prueba) {
    this.prueba = prueba;
  }

  /**
   * Este metodo coloca las columnas a las dos tablas de la HMI. Coloca los cursos que estan
   * asociados al colegio, independiente que tenga o no evaluaciones.
   * 
   * @param pCursoList
   */
  @SuppressWarnings({"unchecked"})
  private void llenarColumnas(ObservableList<R_Curso> pCursoList) {
    Platform.runLater(new Runnable() {

      @Override
      public void run() {
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
        for (R_Curso curso : pCursoList) {
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
    });
  }

  /**
   * Aqui se llenan las tablas con los valores correspondientes.<br>
   * 1) Se obtienen los ejes tematicos de todas las pruebas.<br>
   * 2) Se obtienen las habilidades de todas las pruebas.<br>
   * 3) Se obtienen los porcentajes de aprobacion de de cada colegio con respecto a cada eje y
   * habilidad.
   */
  private void generarReporte() {

    if (evaluacionesPrueba == null || evalEjeTematicoList == null || cursoList == null) {
      return;
    }

    ProgressForm pForm = new ProgressForm();
    pForm.title("Procesando...");
    pForm.message("Esto tomará algunos segundos.");

    Task<Void> task = new Task<Void>() {

      @Override
      protected Void call() throws Exception {


        List<R_Ejetematico> lstNEjes = null;
        List<R_Habilidad> lstNHabs = null;

        long tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedItem().getId();
        updateMessage("Generando columnas de la tabla");
        llenarColumnas(cursoList);
        int nroCursos = cursoList.size();
        Map<Long, List<OTPreguntasEjes>> mapEjes = new HashMap<>();
        Map<Long, List<OTPreguntasHabilidad>> mapHabilidades = new HashMap<>();
        Map<R_EvaluacionEjetematico, List<OTPreguntasEvaluacion>> mapEvaluaciones = new HashMap<>();

        updateMessage("Generando mapa de evaluaciones");
        for (R_EvaluacionEjetematico ejetem : evalEjeTematicoList) {
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

        updateTitle("Procesando evaluaciones");
        int percent = 0;
        int total = evaluacionesPrueba.size();



        // Todas las evaluaciones asociadas (Todos los cursos)
        for (R_EvaluacionPrueba eval : evaluacionesPrueba) {
          R_Curso curso = cursoList.stream().filter(c -> c.getId().equals(eval.getCurso_id())).findFirst().orElse(null);

          updateMessage("Procesando " + curso.getName());
          updateProgress(++percent, total);

          
          
          Map<String, Object> params =
              MapBuilder.<String, Object>unordered().put("prueba_id", eval.getPrueba_id()).build();
          respEsperadas = controller.findByParamsSynchro(R_RespuestasEsperadasPrueba.class, params);
          // Obtener ejes y habilidades de esta prueba
          for (int n = 0; n < respEsperadas.size(); n++) {
            Long eje = respEsperadas.get(n).getEjetematico_id();
            if (!mapEjes.containsKey(eje)) {
              List<OTPreguntasEjes> lista = new ArrayList<>();
              for (int idx = 0; idx < nroCursos; idx++) {
                lista.add(null);
              }
              mapEjes.put(eje, lista);
            }
            Long hab = respEsperadas.get(n).getHabilidad_id();
            if (!mapHabilidades.containsKey(hab)) {

              List<OTPreguntasHabilidad> lista = new ArrayList<>();
              for (int idx = 0; idx < nroCursos; idx++) {
                lista.add(null);
              }
              mapHabilidades.put(hab, lista);
            }
          }
          Long[] ids = mapEjes.keySet().toArray(new Long[mapEjes.keySet().size()]);
          lstNEjes = controller.findByAllIdSynchro(R_Ejetematico.class, ids);
          ids = mapHabilidades.keySet().toArray(new Long[mapHabilidades.keySet().size()]);
          lstNHabs = controller.findByAllIdSynchro(R_Habilidad.class, ids);



          // Se esta revisando un colegio.

          params = MapBuilder.<String, Object>unordered().put("evaluacionprueba_id", eval.getId()).build();
          List<R_PruebaRendida> pruebasRendidas = controller.findByParamsSynchro(R_PruebaRendida.class, params);
          // Estamos procesando un colegio/una prueba

          // Obtengo el index de la columna que tengo que llenar (mas 1
          // por que la primera es de
          // contenido

          int index = cursoList.indexOf(curso);

          if (index == -1) {
            continue;
          }
          totalAlumnos[index] = 0;

          // Obtengo los items a considerar en el caso que hayan items
          // PIE.

          params = MapBuilder.<String, Object>unordered().put("curso_id", eval.getCurso_id()).build();
          List<R_Alumno> lstAlumnos = controller.findByParamsSynchro(R_Alumno.class, params);
          for (R_Alumno alumno : lstAlumnos) {
            if (tipoAlumno == Constants.PIE_ALL || alumno.getTipoalumno_id().equals(tipoAlumno)) {
              // le quito 1 al total de items, ya que este alumno no es
              // del grupo que sequiere representar en el reporte.
              totalAlumnos[index] = totalAlumnos[index] + 1;
            }
          }

          for (R_PruebaRendida pruebaRendida : pruebasRendidas) {
            // Se procesa un alumno.
            if (tipoAlumno != Constants.PIE_ALL && !pruebaRendida.getTipoalumno_id().equals(tipoAlumno)) {
              continue;
            }

            alumnosEvaluados[index] = alumnosEvaluados[index] + 1;

            String respuestas = pruebaRendida.getRespuestas();
            if (respuestas == null || respuestas.isEmpty()) {
              continue;
            }



            for (Long eje : mapEjes.keySet()) {

              R_Ejetematico nEje = lstNEjes.stream().filter(e -> e.getId().equals(eje)).findFirst().orElse(null);
              if (nEje == null)
                continue;

              List<OTPreguntasEjes> lstEjes = mapEjes.get(eje);
              OTPreguntasEjes otEje = lstEjes.get(index); // Se obtiene el
                                                          // asociado a la
                                                          // columna.
              if (otEje == null) {
                otEje = new OTPreguntasEjes();
                otEje.setEjeTematico(nEje);
                lstEjes.set(index, otEje);
              }
              Pair<Integer, Integer> buenasTotal = obtenerBuenasTotalesEjes(respuestas, respEsperadas, eje);

              otEje.setBuenas(otEje.getBuenas() + buenasTotal.getFirst());
              otEje.setTotal(otEje.getTotal() + buenasTotal.getSecond());
              lstEjes.set(index, otEje);
            }

            for (Long hab : mapHabilidades.keySet()) {
              R_Habilidad nHab = lstNHabs.stream().filter(h -> h.getId().equals(hab)).findFirst().orElse(null);
              if (hab == null)
                continue;

              List<OTPreguntasHabilidad> lstHabs = mapHabilidades.get(hab);

              // Se obtiene el asociado a la columna.
              OTPreguntasHabilidad otHabilidad = lstHabs.get(index);
              if (otHabilidad == null) {
                otHabilidad = new OTPreguntasHabilidad();
                otHabilidad.setHabilidad(nHab);
                lstHabs.set(index, otHabilidad);
              }
              Pair<Integer, Integer> buenasTotal = obtenerBuenasTotalesHab(respuestas, respEsperadas, hab);
              otHabilidad.setBuenas(otHabilidad.getBuenas() + buenasTotal.getFirst());
              otHabilidad.setTotal(otHabilidad.getTotal() + buenasTotal.getSecond());
              log.fine(String.format("HAB: %s %d/%d  ACUM: %d/%d", nHab.getName(), buenasTotal.getFirst(),
                  buenasTotal.getSecond(), otHabilidad.getBuenas(), otHabilidad.getTotal()));
              lstHabs.set(index, otHabilidad);
            }

            for (R_EvaluacionEjetematico ejetem : evalEjeTematicoList) {
              if (ejetem.isInside(pruebaRendida.getBuenas())) {
                List<OTPreguntasEvaluacion> lstOt = mapEvaluaciones.get(ejetem);
                OTPreguntasEvaluacion ot = lstOt.get(index);
                ot.setAlumnos(ot.getAlumnos() + 1);
                break;
              }
            }
          }
        }

        updateTitle("Generando tablas");
        updateMessage("Generando tablas de ejes y habilidades");
        // Ahora se debe llenar las tablas.
        generarTablaEjesHabilidades(mapEjes, mapHabilidades, lstNEjes, lstNHabs);
        updateMessage("Generando tablas evaluaciones");
        generarTablaEvaluaciones(mapEvaluaciones, totalAlumnos, alumnosEvaluados);

        return null;
      }

    };

    task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        pForm.getDialogStage().hide();
      }
    });
    task.setOnFailed(new EventHandler<WorkerStateEvent>() {

      @Override
      public void handle(WorkerStateEvent event) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Se ha producido un error");
        alert.setHeaderText("Ha fallado la generación del reporte");
        alert.setContentText(event.getSource().getMessage().toUpperCase());
      }
    });

    pForm.showWorkerProgress(task);
    Executors.newSingleThreadExecutor().execute(task);

  }

  @SuppressWarnings("unchecked")
  private void generarTablaEvaluaciones(Map<R_EvaluacionEjetematico, List<OTPreguntasEvaluacion>> mapEvaluaciones,
      int[] totalAlumnos, int[] alumnosEvaluados) {
    ObservableList<String> row = null;
    ObservableList<ObservableList<String>> items = FXCollections.observableArrayList();
    Collections.sort(evalEjeTematicoList, Comparadores.comparaEvaluacionEjeTematico());

    for (R_EvaluacionEjetematico eval : evalEjeTematicoList) {
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
   * Se genera la tabal que contiene los % de logro por cada eje y por cada habilidad asociado a
   * cada colegio.
   * 
   * @param mapEjes Mapa que contiene los valores para cada colegio de los ejes.
   * @param mapHabilidades Mapa que contiene los valores para cada colegio de las habilidades.
   */
  @SuppressWarnings("unchecked")
  private void generarTablaEjesHabilidades(Map<Long, List<OTPreguntasEjes>> mapEjes,
      Map<Long, List<OTPreguntasHabilidad>> mapHabilidades, List<R_Ejetematico> lstNEjes, List<R_Habilidad> lstNHabs) {
    ObservableList<String> row = null;
    ObservableList<ObservableList<String>> items = FXCollections.observableArrayList();
    int nroCols = 0;
    for (Long eje : mapEjes.keySet()) {

      R_Ejetematico nEje = lstNEjes.stream().filter(e -> e.getId().equals(eje)).findFirst().orElse(null);
      if (nEje == null)
        continue;

      row = FXCollections.observableArrayList();
      List<OTPreguntasEjes> lst = mapEjes.get(eje);
      nroCols = lst.size();
      row.add(nEje.getName());
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

    for (Long hab : mapHabilidades.keySet()) {

      R_Habilidad nHab = lstNHabs.stream().filter(e -> e.getId().equals(hab)).findFirst().orElse(null);
      if (nHab == null)
        continue;

      row = FXCollections.observableArrayList();
      List<OTPreguntasHabilidad> lst = mapHabilidades.get(hab);
      row.add(nHab.getName());
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
   * Este metodo evalua la cantidad de buenas de un String de respuesta contrastado contra las
   * respuestas esperadas.
   * 
   * @param respuestas Las respuestas del alumno.
   * @param respEsperadas Las respuestas correctas definidas en la prueba.
   * @param ahb La R_Habilidad en base al que se realiza el calculo.
   * @return Par <Preguntas buenas, Total de Preguntas> del eje.
   */
  private Pair<Integer, Integer> obtenerBuenasTotalesHab(String respuestas,
      List<R_RespuestasEsperadasPrueba> respEsperadas, Long hab) {
    int nroBuenas = 0;
    int nroPreguntas = 0;
    for (int n = 0; n < respEsperadas.size(); n++) {
      R_RespuestasEsperadasPrueba resp = respEsperadas.get(n);
      if (!resp.getAnulada()) {
        if (resp.getHabilidad_id().equals(hab)) {
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
   * Este metodo evalua la cantidad de buenas de un String de respuesta contrastado contra las
   * respuestas eperadas.
   * 
   * @param respuestas Las respuestas del alumno.
   * @param respEsperadas Las respuestas correctas definidas en la prueba.
   * @param eje El Eje tematico en base al que se realiza el calculo.
   * @return Par <Preguntas buenas, Total de Preguntas> del eje.
   */
  private Pair<Integer, Integer> obtenerBuenasTotalesEjes(String respuestas,
      List<R_RespuestasEsperadasPrueba> respEsperadas, Long eje) {
    int nroBuenas = 0;
    int nroPreguntas = 0;
    for (int n = 0; n < respEsperadas.size(); n++) {
      R_RespuestasEsperadasPrueba resp = respEsperadas.get(n);
      if (!resp.getAnulada()) {
        if (resp.getEjetematico_id().equals(eje)) {
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
    tblEjeshabilidades.getColumns().clear();;
    tblEvaluacion.getColumns().clear();
  }
}
