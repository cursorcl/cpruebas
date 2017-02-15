package colegio.nivel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.persistence.util.Comparadores;
import cl.eos.restful.tables.R_Alumno;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_Habilidad;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_RangoEvaluacion;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.restful.tables.R_TipoCurso;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.MapBuilder;
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

public class Nivel_ComparativoColegioHabilidadesView extends AFormView
    implements EventHandler<ActionEvent> {
  private static final String ASIGNATURA_ID = "asignatura_id";
  private static final String COLEGIO_ID = "Colegio_id";
  @SuppressWarnings("rawtypes")
  @FXML
  private TableView tblHabilidadesCantidad;
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
  private ObservableList<R_TipoCurso> cursoList;
  private ObservableList<R_RangoEvaluacion> rangoEvalList;
  private ObservableList<R_EvaluacionPrueba> evaluacionesPrueba;

  public Nivel_ComparativoColegioHabilidadesView() {
    setTitle("Comparativo Colegios Habilidades");
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
      tblHabilidadesCantidad.setId("Comp ColegiosHabilidades");
      List<TableView<? extends Object>> listaTablas = new ArrayList<>();
      listaTablas.add((TableView<? extends Object>) tblHabilidadesCantidad);
      ExcelSheetWriterObj.convertirDatosColumnasDoblesALibroDeExcel(listaTablas);
    }
  }

  private void handleColegios() {
    R_Colegio colegio = cmbColegios.getSelectionModel().getSelectedItem();
    if (colegio != null) {
      parameters.put(COLEGIO_ID, colegio.getId());
      Map<String, Object> param =
          MapBuilder.<String, Object>unordered().put(COLEGIO_ID, colegio.getId()).build();
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
          R_Curso curso = (R_Curso) iEntity;
          Optional<R_TipoCurso> op =
              cursoList.stream().filter(c -> c.getId().equals(curso.getTipocurso_id())).findFirst();
          if (!op.isPresent())
            continue;
          R_TipoCurso tipoCurso = op.get();
          if (!cursoList.contains(tipoCurso)) {
            cursoList.add(tipoCurso);
          }
        }
        FXCollections.sort(cursoList, Comparadores.comparaTipoCurso());
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
      if (entity instanceof R_RangoEvaluacion) {
        rangoEvalList = FXCollections.observableArrayList();
        for (Object object : list) {
          R_RangoEvaluacion rango = (R_RangoEvaluacion) object;
          rangoEvalList.add(rango);
        }
        generarReporte();
      }
    } else if (list != null && list.isEmpty()) {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("No hay registros.");
      alert.setHeaderText(this.getName());
      alert.setContentText("No se ha encontrado registros para la consulta.");
      alert.showAndWait();
    }
  }

  /**
   * Este metodo coloca las columnas a las dos tablas de la HMI. Coloca los cursos que estan
   * asociados al colegio, independiente que tenga o no evaluaciones.
   * 
   * @param pCursoList
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  private void llenarColumnas(List<R_TipoCurso> pCursoList,
      ObservableList<R_RangoEvaluacion> rangos) {
    TableColumn tc = new TableColumn("HABILIDADES");
    tc.setSortable(false);
    tc.setStyle("-fx-alignment: CENTER-LEFT;");
    tc.prefWidthProperty().set(250f);
    tc.setCellValueFactory(
        new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
          public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
            return new SimpleStringProperty(param.getValue().get(0).toString());
          }
        });
    tblHabilidadesCantidad.getColumns().add(tc);
    int indice = 1;
    for (R_TipoCurso curso : pCursoList) {
      tc = new TableColumn(curso.getName());
      tc.prefWidthProperty().set(50f);
      tc.setStyle("-fx-alignment: CENTER;");
      tc.setSortable(false);
      tc.setCellValueFactory(
          new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
              return new SimpleStringProperty(param.getValue().get(0).toString());
            }
          });
      // Estoy agregando subcolumnas
      for (R_RangoEvaluacion rng : rangos) {
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
      tblHabilidadesCantidad.getColumns().add(tc);
    }
  }

  /**
   * Aqui se llenan las tablas con los valores correspondientes.<br>
   * 1) Se obtienen los ejes tematicos de todas las pruebas.<br>
   * 2) Se obtienen las habilidades de todas las pruebas.<br>
   * 3) Se obtienen los porcentajes de aprobacion de cada colegio con respecto a cada habilidad y
   * habilidad.
   */
  private void generarReporte() {
    if (evaluacionesPrueba == null || rangoEvalList == null) {
      return;
    }
    llenarColumnas(cursoList, rangoEvalList);
    int nroTipoCursos = cursoList.size();
    int nroRangos = rangoEvalList.size();
    Map<R_Habilidad, List<OTAcumulador>> mapHabilidad = new HashMap<>();
    long tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedItem().getId();
    Map<Long, List<R_RespuestasEsperadasPrueba>> mapRespEsperadas = new HashMap<>();
    // Todas las evaluaciones asociadas (Todos los cursos)
    for (R_EvaluacionPrueba eval : evaluacionesPrueba) {
      // Se esta revisando un colegio.
      Map<String, Object> parameters =
          MapBuilder.<String, Object>unordered().put("evaluacionprueba_id", eval.getId()).build();
      List<R_PruebaRendida> pruebasRendidas =
          controller.findByParamsSynchro(R_PruebaRendida.class, parameters);
      List<R_RespuestasEsperadasPrueba> respEsperadas = mapRespEsperadas.get(eval.getPrueba_id());
      if (respEsperadas == null) {
        parameters =
            MapBuilder.<String, Object>unordered().put("prueba_id", eval.getPrueba_id()).build();
        respEsperadas =
            controller.findByParamsSynchro(R_RespuestasEsperadasPrueba.class, parameters);
      }
      // Estamos procesando un colegio/una prueba
      for (R_PruebaRendida pruebaRendida : pruebasRendidas) {
        // Se procesa un alumno.
        if (pruebaRendida.getAlumno_id() == null) {
          // Caso especial que indica que la prueba esta sin alumno.
          continue;
        }
        R_Alumno alumno = controller.findByIdSynchro(R_Alumno.class, pruebaRendida.getAlumno_id());
        if (tipoAlumno != Constants.PIE_ALL && tipoAlumno != alumno.getTipoalumno_id()) {
          // En este caso, no se considera este alumno para el
          // cálculo.
          continue;
        }
        // Obtengo el index de la columna que tengo que llenar (mas 1
        // por que la primera es de contenido
        // index * nroRangos Ya que cada TIPO DE CURSO tiene nroRangos
        // columnas
        // asociadas.
        int index = cursoList.indexOf(alumno.getTipocurso_id());
        if (index == -1) // Caso especial que indica que el alumno no es del colegio.
          continue;
        String respuestas = pruebaRendida.getRespuestas();
        if (respuestas == null || respuestas.isEmpty()) {
          continue;
        }
        for (int n = 0; n < respEsperadas.size(); n++) {
          // Sumando a ejes tematicos
          Long hId = respEsperadas.get(n).getHabilidad_id();
          Optional<R_Habilidad> op = mapHabilidad.keySet().stream().filter(m -> m.getId().equals(hId)).findFirst();
          R_Habilidad habilidad = null;
          if(op.isPresent())
            habilidad = op.get();
          if(habilidad == null)
          {
            habilidad =  controller.findByIdSynchro(R_Habilidad.class, hId);
          }
          if (!mapHabilidad.containsKey(habilidad)) {
            List<OTAcumulador> lista = new ArrayList<OTAcumulador>(nroTipoCursos);
            for (int idx = 0; idx < nroTipoCursos; idx++) {
              lista.add(null);
            }
            mapHabilidad.put(habilidad, lista);
          }
          List<OTAcumulador> lstEjes = mapHabilidad.get(habilidad);
          OTAcumulador otEjeEval = lstEjes.get(index); // Que columna tipocurso es
          if (otEjeEval == null) {
            otEjeEval = new OTAcumulador();
            int[] nroPersonas = new int[nroRangos];
            Arrays.fill(nroPersonas, 0);
            otEjeEval.setNroPersonas(nroPersonas);
            lstEjes.set(index, otEjeEval);
          }
        }
        for (R_Habilidad habilidad : mapHabilidad.keySet()) {
          List<OTAcumulador> lstHabs = mapHabilidad.get(habilidad);
          OTAcumulador otEjeEval = lstHabs.get(index);
          float porcentaje = obtenerPorcentaje(respuestas, respEsperadas, habilidad);
          for (int idx = 0; idx < nroRangos; idx++) {
            R_RangoEvaluacion rango = rangoEvalList.get(idx);
            if (rango.isInside(porcentaje)) {
              otEjeEval.getNroPersonas()[idx] = otEjeEval.getNroPersonas()[idx] + 1;
              break;
            }
          }
          lstHabs.set(index, otEjeEval);
        }
      }
    }
    // Ahora se debe llenar las tablas.
    generarTablaEjes(mapHabilidad);
  }

  /**
   * Se genera la tabal que contiene los % de logro por cada habilidad y por cada habilidad asociado
   * a cada colegio.
   * 
   * @param mapEjes Mapa que contiene los valores para cada colegio de los ejes.
   * @param mapHabilidades Mapa que contiene los valores para cada colegio de las habilidades.
   */
  @SuppressWarnings("unchecked")
  private void generarTablaEjes(Map<R_Habilidad, List<OTAcumulador>> mapEjes) {
    ObservableList<String> row = null;
    ObservableList<ObservableList<String>> items = FXCollections.observableArrayList();
    for (R_Habilidad habilidad : mapEjes.keySet()) {
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
      List<R_RespuestasEsperadasPrueba> respEsperadas, R_Habilidad habilidad) {
    float nroBuenas = 0;
    float nroPreguntas = 0;
    for (int n = 0; n < respEsperadas.size(); n++) {
      R_RespuestasEsperadasPrueba resp = respEsperadas.get(n);
      if (!resp.getAnulada()) {
        if (resp.getHabilidad_id().equals(habilidad.getId())) {
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
    tblHabilidadesCantidad.getItems().clear();
    tblHabilidadesCantidad.getColumns().clear();;
  }

  static int static_id = 0;

  static class Item {
    int id;
    String name;

    public Item() {
      this.id = (static_id++) / 4;
      this.name = "NAME " + static_id;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Item [id=");
      builder.append(id);
      builder.append(", name=");
      builder.append(name);
      builder.append("]");
      return builder.toString();
    }
  }

  public static void main(String[] args) {
    List<Item> items = Stream.generate(Item::new).limit(20).collect(Collectors.toList());
    items.forEach((n) -> System.out.println(n));
    List<Integer> tiposCurso =
        items.stream().map(i -> i.id).distinct().collect(Collectors.toList());
    tiposCurso.forEach((n) -> System.out.println(n));
  }
}
