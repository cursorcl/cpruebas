package colegio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.imp.view.ProgressForm;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.util.Comparadores;
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
import comparativo.colegio.eje.habilidad.x.curso.OTCursoRangos;
import comparativo.colegio.eje.habilidad.x.curso.OTUnCursoUnEjeHabilidad;
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

public class ComparativoColegioEjeHabilidadxCursoView extends AFormView
    implements EventHandler<ActionEvent> {

  private static final String ASIGNATURA_ID = "asignatura_id";
  private static final String COLEGIO_ID = "colegio_id";
  @SuppressWarnings("rawtypes")
  @FXML
  private TableView tblEjeshabilidades;
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
  private MenuItem mnuExportarAlumnos;
  private R_Colegio colegioActivo;
  private R_Prueba prueba;

  private Map<String, Object> parameters = new HashMap<String, Object>();
  private ObservableList<R_Curso> listaCursos;
  private ObservableList<R_EvaluacionEjetematico> rangosEvaluacionPorcentaje;
  private ObservableList<R_EvaluacionPrueba> listaEvaluacionesPrueba;
  private Map<Long, List<R_RespuestasEsperadasPrueba>> mapRespEsperadas;
  private List<R_Habilidad> listaHablidades;
  private List<R_Ejetematico> listaEjesTematicos;

  public ComparativoColegioEjeHabilidadxCursoView() {
    setTitle("Comparativo R_Colegio Ejes Temáticos y Habilidades");
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

    if (source == mnuExportarAlumnos) {
      ExcelSheetWriterObj.generarReporteComparativoColegioEjeHabilidadCurso(tblEjeshabilidades,
          colegioActivo.getName());
      colegioActivo = null;
    }
  }

  private void handleColegios() {
    R_Colegio colegio = cmbColegios.getSelectionModel().getSelectedItem();
    if (colegio != null) {
      parameters.put(COLEGIO_ID, colegio.getId());
      Map<String, Object> param = new HashMap<String, Object>();
      param.put("colegio_id", colegio.getId());
      lblTitulo.setText(colegio.getName());
      controller.findByParam(R_Curso.class, param);
      colegioActivo = colegio;
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
    if (!parameters.isEmpty() && parameters.containsKey(COLEGIO_ID)
        && parameters.containsKey(ASIGNATURA_ID)) {
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
        listaCursos = FXCollections.observableArrayList();
        for (Object iEntity : list) {
          listaCursos.add((R_Curso) iEntity);
        }
        FXCollections.sort(listaCursos, Comparadores.comparaResumeCurso());
      }

      if (entity instanceof R_TipoAlumno) {
        ObservableList<R_TipoAlumno> tAlumnoList = FXCollections.observableArrayList();
        for (Object iEntity : list) {
          tAlumnoList.add((R_TipoAlumno) iEntity);
        }
        cmbTipoAlumno.setItems(tAlumnoList);

      }
      if (entity instanceof R_EvaluacionEjetematico) {
        rangosEvaluacionPorcentaje = FXCollections.observableArrayList();
        for (Object object : list) {
          R_EvaluacionEjetematico evaluacion = (R_EvaluacionEjetematico) object;
          rangosEvaluacionPorcentaje.add(evaluacion);
        }
        tareaGenerarReporte();
      }
      if (entity instanceof R_EvaluacionPrueba) {
        listaEvaluacionesPrueba = FXCollections.observableArrayList();
        for (Object object : list) {
          R_EvaluacionPrueba evaluacion = (R_EvaluacionPrueba) object;
          listaEvaluacionesPrueba.add(evaluacion);
        }
        tareaGenerarReporte();
      }
    } else if (list != null && list.isEmpty()) {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("No hay registros.");
      alert.setHeaderText(null);
      alert.setContentText("No se ha encontrado registros para la consulta.");
      alert.showAndWait();
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void makeTable() {
    TableColumn tc = new TableColumn("EJE / HABILIDAD");
    tc.setSortable(false);
    tc.setStyle("-fx-alignment: CENTER-LEFT;");
    tc.prefWidthProperty().set(250f);
    tc.setCellValueFactory(param -> new SimpleStringProperty(
        ((CellDataFeatures<ObservableList, String>) param).getValue().get(0).toString()));

    Runnable r = new Runnable() {
      @Override
      public void run() {
        tblEjeshabilidades.getColumns().add(tc);
      }

    };
    Platform.runLater(r);

    // Ordenar los cursos
    int nCol = 1;
    for (R_Curso curso : listaCursos) {

      TableColumn colCurso = new TableColumn(curso.getName());
      for (R_EvaluacionEjetematico evEjeHab : rangosEvaluacionPorcentaje) {
        final int idx = nCol;
        TableColumn colEjeHab = new TableColumn(evEjeHab.getName());
        colEjeHab.setSortable(false);
        colEjeHab.setStyle("-fx-alignment: CENTER;");
        colEjeHab.setCellValueFactory(
            new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
              public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().get(idx).toString());
              }
            });

        colCurso.getColumns().add(colEjeHab);
        nCol++;
      }

      r = new Runnable() {

        @Override
        public void run() {
          tblEjeshabilidades.getColumns().add(colCurso);
        }

      };
      Platform.runLater(r);
    }
  }

  private void fillColumnEjeHabilidad(List<R_Ejetematico> listaEjesTematicos,
      List<R_Habilidad> listaHablidades, Map<IEntity, List<OTCursoRangos>> reporte) {
    List<IEntity> entidades = new ArrayList<IEntity>();
    entidades.addAll(listaEjesTematicos);
    entidades.addAll(listaHablidades);

    ObservableList<String> registro = null;
    ObservableList<ObservableList<String>> items = FXCollections.observableArrayList();

    for (IEntity entity : entidades) {
      registro = FXCollections.observableArrayList();
      registro.add(entity.getName());
      List<OTCursoRangos> lista = reporte.get(entity);
      int nroEjes = rangosEvaluacionPorcentaje.size();

      for (int idx = 0; idx < lista.size(); idx++) {
        OTCursoRangos ot = lista.get(idx);
        for (int n = 0; n < nroEjes; n++) {

          if (ot != null && ot.getNroAlumnosXEjeHab() != null) {
            registro.add(String.format("%3d", ot.getNroAlumnosXEjeHab()[n]));
          } else {
            registro.add("-");
          }
        }
      }
      items.add(registro);
    }

    Runnable r = new Runnable() {
      @SuppressWarnings("unchecked")
      @Override
      public void run() {
        tblEjeshabilidades.getItems().setAll(items);
      }

    };
    Platform.runLater(r);
  }

  private List<OTUnCursoUnEjeHabilidad> evaluarUnCurso(
      List<R_RespuestasEsperadasPrueba> respEsperadas, R_EvaluacionPrueba evaluacion) {
    List<OTUnCursoUnEjeHabilidad> listaOTUnCurso = new ArrayList<OTUnCursoUnEjeHabilidad>();
    // Cada prueba rendida equivale a un ALUMNO DEL CURSO
    int nAlumno = 0;
    long tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedItem().getId();


    Map<String, Object> params = MapBuilder.<String, Object>unordered()
        .put("evaluacionprueba_id", evaluacion.getId()).build();
    List<R_PruebaRendida> pRendidas = controller.findByParamsSynchro(R_PruebaRendida.class, params);
    for (R_PruebaRendida pRendida : pRendidas) {

      if (tipoAlumno != Constants.PIE_ALL && tipoAlumno != pRendida.getTipoalumno_id()) {
        // En este caso, no se considera este alumno para el
        // cálculo.
        continue;
      }
      int nroAlumnos = pRendidas.size();
      evaluarUnAlumno(pRendida, respEsperadas, nAlumno, listaOTUnCurso, nroAlumnos);
      nAlumno++; // Siguiente alumno.
    }
    return listaOTUnCurso;
  }

  private List<OTUnCursoUnEjeHabilidad> evaluarUnAlumno(R_PruebaRendida pRendida,
      List<R_RespuestasEsperadasPrueba> respEsperadas, int nAlumno,
      List<OTUnCursoUnEjeHabilidad> listaOTUnCurso, int nroAlumnos) {
    String respuestas = pRendida.getRespuestas();
    int nResp = 0;


    for (R_RespuestasEsperadasPrueba resp : respEsperadas) {

      R_Ejetematico eje = listaEjesTematicos.stream()
          .filter(e -> e.getId().equals(resp.getEjetematico_id())).findFirst().orElse(null);

      R_Habilidad hab = listaHablidades.stream()
          .filter(h -> h.getId().equals(resp.getHabilidad_id())).findFirst().orElse(null);
      if (eje == null || hab == null)
        continue;

      OTUnCursoUnEjeHabilidad otEje = new OTUnCursoUnEjeHabilidad(eje, nroAlumnos);
      OTUnCursoUnEjeHabilidad otHab = new OTUnCursoUnEjeHabilidad(hab, nroAlumnos);

      if (listaOTUnCurso.contains(otEje)) {
        // Me aseguro que es el que existe
        otEje = listaOTUnCurso.get(listaOTUnCurso.indexOf(otEje));
      } else {
        // Lo agrego a la lista.
        listaOTUnCurso.add(otEje);
      }
      if (listaOTUnCurso.contains(otHab)) {
        // Me aseguro que es el que existe
        otHab = listaOTUnCurso.get(listaOTUnCurso.indexOf(otHab));
      } else {
        // Lo agrego a la lista.
        listaOTUnCurso.add(otHab);
      }

      if (nAlumno == 0) {
        // Contamos las preguntas para eje/habilidad solo en el
        // primer alumno.
        otEje.setNroPreguntas(otEje.getNroPreguntas() + 1);
        otHab.setNroPreguntas(otHab.getNroPreguntas() + 1);
      }

      if (respuestas.length() <= nResp) {
        // No hay más respuesta
        break;
      }
      String r = respuestas.substring(nResp, nResp + 1);

      if (resp.getAnulada() || "O".equals(r) || "-".equals(r)) {
        // La respuesta no se considera
        nResp++;
        continue;
      }

      // Aqui agrego 1 a las buenas del eje y de la habilidad del
      // alumno n.
      if (resp.getRespuesta().equals(r)) {
        float buenasEje = otEje.getBuenasPorAlumno()[nAlumno];
        otEje.getBuenasPorAlumno()[nAlumno] = buenasEje + 1;
        float buenasHab = otHab.getBuenasPorAlumno()[nAlumno];
        otHab.getBuenasPorAlumno()[nAlumno] = buenasHab + 1;
      }
      nResp++; // Siguiente respuesta
    }
    return listaOTUnCurso;
  }

  private Map<IEntity, List<OTCursoRangos>> makeMapReporte(int nroCursos) {
    Map<IEntity, List<OTCursoRangos>> reporte = new HashMap<IEntity, List<OTCursoRangos>>();

    for (R_EvaluacionPrueba evaluacion : listaEvaluacionesPrueba) {

      List<R_RespuestasEsperadasPrueba> rEsperadas =
          mapRespEsperadas.get(evaluacion.getPrueba_id());

      // Obtengo lista de habilidades de la PRUEBA base de la EVALUACION.
      for (R_RespuestasEsperadasPrueba rEsperada : rEsperadas) {
        R_Habilidad hab = listaHablidades.stream()
            .filter(h -> h.getId().equals(rEsperada.getHabilidad_id())).findFirst().orElse(null);
        R_Ejetematico eje = listaEjesTematicos.stream()
            .filter(e -> e.getId().equals(rEsperada.getEjetematico_id())).findFirst().orElse(null);
        if (hab == null || eje == null)
          continue;

        if (!reporte.containsKey(hab)) {
          List<OTCursoRangos> list =
              Stream.generate(OTCursoRangos::new).limit(nroCursos).collect(Collectors.toList());

          reporte.put(hab, list);
        }
        if (!reporte.containsKey(eje)) {
          List<OTCursoRangos> list =
              Stream.generate(OTCursoRangos::new).limit(nroCursos).collect(Collectors.toList());
          reporte.put(eje, list);
        }
      }
    }

    return reporte;
  }

  private List<R_Habilidad> makeListHabilidades() {

    // Una evaluacion corresponde al conjunto de pruebas de un CURSO de un
    // COLEGIO de una ASIGNATURA.


    List<Long> idsHabsTematicos = new ArrayList<>();
    for (R_EvaluacionPrueba evaluacion : listaEvaluacionesPrueba) {
      List<R_RespuestasEsperadasPrueba> respEsperadas = mapRespEsperadas.get(evaluacion.getId());
      // Obtengo lista de habilidades de la PRUEBA base de la EVALUACION.
      for (R_RespuestasEsperadasPrueba rEsperada : respEsperadas) {

        if (!idsHabsTematicos.contains(rEsperada.getHabilidad_id())) {
          idsHabsTematicos.add(rEsperada.getHabilidad_id());
        }
      }
    }
    Long[] ids = idsHabsTematicos.toArray(new Long[idsHabsTematicos.size()]);
    return controller.findByAllIdSynchro(R_Habilidad.class, ids);
  }

  private List<R_Ejetematico> makeListEjesTematicos() {
    // Una evaluacion corresponde al conjunto de pruebas de un CURSO de un
    // COLEGIO de una ASIGNATURA.
    List<Long> idsEjesTematicos = new ArrayList<>();
    for (R_EvaluacionPrueba evaluacion : listaEvaluacionesPrueba) {
      List<R_RespuestasEsperadasPrueba> respEsperadas = mapRespEsperadas.get(evaluacion.getId());
      // Obtengo lista de ejes de la PRUEBA base de la EVALUACION.
      for (R_RespuestasEsperadasPrueba rEsperada : respEsperadas) {
        if (!idsEjesTematicos.contains(rEsperada.getEjetematico_id())) {
          idsEjesTematicos.add(rEsperada.getEjetematico_id());
        }
      }
    }
    Long[] ids = idsEjesTematicos.toArray(new Long[idsEjesTematicos.size()]);

    return controller.findByAllIdSynchro(R_Ejetematico.class, ids);

  }

  private void tareaGenerarReporte() {

    if (listaEvaluacionesPrueba == null || rangosEvaluacionPorcentaje == null) {
      // No hay valores para procesar todo.
      return;
    }

    clearContent();
    ProgressForm pForm = new ProgressForm();
    pForm.title("Generando reporte");
    pForm.message("Esto tomará algunos segundos.");

    Task<Boolean> task = new Task<Boolean>() {
      @Override
      protected Boolean call() throws Exception {
        updateMessage("Generando reporte");
        Pair<Map<IEntity, List<OTCursoRangos>>, Pair<List<R_Ejetematico>, List<R_Habilidad>>> resultado =
            generarReporte();
        Map<IEntity, List<OTCursoRangos>> reporte = resultado.getFirst();
        List<R_Ejetematico> listaEjesTematicos = resultado.getSecond().getFirst();
        List<R_Habilidad> listaHablidades = resultado.getSecond().getSecond();
        updateMessage("Construyendo tabla");
        makeTable();
        updateMessage("Llenando valores en tabla");
        fillColumnEjeHabilidad(listaEjesTematicos, listaHablidades, reporte);
        return Boolean.TRUE;
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
        pForm.getDialogStage().hide();
      }
    });


    pForm.showWorkerProgress(task);
    Executors.newSingleThreadExecutor().execute(task);
  }

  /**
   * Para ontener el resultado final se siguen los sieguientes pasos: <lu>
   * <li>Obtener resumen de preguntas buenas de cada Eje/Hab x R_Alumno. </lu>
   */
  private Pair<Map<IEntity, List<OTCursoRangos>>, Pair<List<R_Ejetematico>, List<R_Habilidad>>> generarReporte() {


    // Una iteracion por cada colegio asociado al colegio con una evaluacion
    int nroCursos = listaEvaluacionesPrueba.size();

    mapRespEsperadas = makeRespEsperadas();
    listaEjesTematicos = makeListEjesTematicos();
    listaHablidades = makeListHabilidades();
    Map<IEntity, List<OTCursoRangos>> reporte = makeMapReporte(nroCursos);

    // Va a tener los resulados finales
    int nroCurso = 0;
    for (R_EvaluacionPrueba evaluacion : listaEvaluacionesPrueba) {

      List<R_RespuestasEsperadasPrueba> respEsperadas =
          mapRespEsperadas.get(evaluacion.getPrueba_id());
      List<OTUnCursoUnEjeHabilidad> listaOTUnCurso = evaluarUnCurso(respEsperadas, evaluacion);

      // Aqui cuento la cantida de items en los rangos
      for (OTUnCursoUnEjeHabilidad ot : listaOTUnCurso) {
        // Calcula cantidad de items en los rangos para un eje y un
        // colegio
        int[] alumXRango = ot.calculateAlumnosXRango(rangosEvaluacionPorcentaje);
        R_Curso curso = listaCursos.stream().filter(c -> c.getId().equals(evaluacion.getCurso_id()))
            .findFirst().orElse(null);

        OTCursoRangos cursoRango = new OTCursoRangos(curso, alumXRango);
        List<OTCursoRangos> listCursos = reporte.get(ot.getEjeHabilidad());
        listCursos.set(nroCurso, cursoRango); // Establesco el la
                                              // cantidad de items
                                              // en un porcentaje para
                                              // el colegio
      }

      nroCurso++;
      // Aqui va la siguiente evaluacion (CURSO)

    }
    // Tengo todos los resultados en el map (reporte)
    // Ahora debo generar la tabla.
    Pair<List<R_Ejetematico>, List<R_Habilidad>> listas =
        new Pair<List<R_Ejetematico>, List<R_Habilidad>>(listaEjesTematicos, listaHablidades);
    return new Pair<Map<IEntity, List<OTCursoRangos>>, Pair<List<R_Ejetematico>, List<R_Habilidad>>>(
        reporte, listas);
  }

  private Map<Long, List<R_RespuestasEsperadasPrueba>> makeRespEsperadas() {
    Map<Long, List<R_RespuestasEsperadasPrueba>> resps = new HashMap<>();
    for (R_EvaluacionPrueba evaluacion : listaEvaluacionesPrueba) {
      if (resps.get(evaluacion.getPrueba_id()) == null) {
        Map<String, Object> params = MapBuilder.<String, Object>unordered()
            .put("prueba_id", evaluacion.getPrueba_id()).build();
        List<R_RespuestasEsperadasPrueba> lstR_RespuestasEsperadasPrueba =
            controller.findByParamsSynchro(R_RespuestasEsperadasPrueba.class, params);
        resps.put(evaluacion.getPrueba_id(), lstR_RespuestasEsperadasPrueba);
      }
    }
    return resps;
  }

  private void clearContent() {
    tblEjeshabilidades.getItems().clear();
    tblEjeshabilidades.getColumns().clear();
  }

}
