package curso;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTResumenGeneral;
import cl.eos.restful.tables.R_Alumno;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.MapBuilder;
import cl.eos.view.ots.OTPruebaRendida;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ResumenGeneralView extends AFormView implements EventHandler<ActionEvent> {
  @FXML
  private TextField txtPrueba;
  @FXML
  private TextField txtCurso;
  @FXML
  private TextField txtAsignatura;
  @FXML
  private TextField txtExigencia;
  @FXML
  private TextField txtNoPregunta;
  @FXML
  private TextField txtPjePrueba;

  @FXML
  private TableView<OTResumenGeneral> tblResumen;
  @FXML
  private TableColumn<OTResumenGeneral, String> colNombre;
  @FXML
  private TableColumn<OTResumenGeneral, Float> colNotas;
  @FXML
  private TableColumn<OTResumenGeneral, Float> colBuenas;
  @FXML
  private TableColumn<OTResumenGeneral, Integer> ColPuntos;
  @FXML
  private TableColumn<OTResumenGeneral, Float> colPuntaje;

  @FXML
  private TableView<OTPruebaRendida> tblAlumnos;
  @FXML
  private TableColumn<OTPruebaRendida, String> colRut;
  @FXML
  private TableColumn<OTPruebaRendida, String> colPaterno;
  @FXML
  private TableColumn<OTPruebaRendida, String> colMaterno;
  @FXML
  private TableColumn<OTPruebaRendida, String> colName;
  @FXML
  private TableColumn<OTPruebaRendida, Integer> colABuenas;
  @FXML
  private TableColumn<OTPruebaRendida, Integer> colAMalas;
  @FXML
  private TableColumn<OTPruebaRendida, Integer> colAOmitidas;
  @FXML
  private TableColumn<OTPruebaRendida, Float> colPBuenas;
  @FXML
  private TableColumn<OTPruebaRendida, Integer> colAPuntaje;
  @FXML
  private TableColumn<OTPruebaRendida, Float> colPPuntaje;
  @FXML
  private TableColumn<OTPruebaRendida, Float> colANota;
  @FXML
  private MenuItem mnuExportarAlumnos;
  @FXML
  private MenuItem mnuExportarResumen;

  @FXML
  private ComboBox<R_TipoAlumno> cmbTipoAlumno;

  @FXML
  private Button btnGenerar;

  long tipoAlumno = Constants.PIE_ALL;

  private Float notaMin = Float.MAX_VALUE;
  private Float notaMax = Float.MIN_VALUE;
  private Float pbuenasMin = Float.MAX_VALUE;
  private Float pbuenasMax = Float.MIN_VALUE;
  private Float ppuntajeMin = Float.MAX_VALUE;
  private Float ppuntajeMax = Float.MIN_VALUE;
  private Integer puntajeMin = Integer.MAX_VALUE;
  private Integer puntajeMax = Integer.MIN_VALUE;
  private R_EvaluacionPrueba evaluacionPrueba;

  private R_Prueba prueba;
  private R_Asignatura asignatura;
  private R_Curso curso;
  private List<R_PruebaRendida> lstPruebasRendidas;
  private List<R_Alumno> lstAlumnos;

  public ResumenGeneralView() {}

  private void asignarDatosEvalucion(R_EvaluacionPrueba entity, List<OTResumenGeneral> listaResumen) {
    if (curso != null) {
      txtCurso.setText(curso.getName());
      txtExigencia.setText(String.valueOf(prueba.getExigencia()));
    }

    if (prueba != null) {
      txtPrueba.setText(prueba.getName());
      txtPjePrueba.setText(prueba.getPuntajebase().toString());
      txtNoPregunta.setText(prueba.getNropreguntas().toString());
      if (asignatura != null) {
        txtAsignatura.setText(asignatura.getName());
      }
    }

    final List<R_PruebaRendida> list = lstPruebasRendidas;
    if (list != null && !list.isEmpty()) {
      final ObservableList<OTPruebaRendida> oList = FXCollections.observableArrayList();
      for (final R_PruebaRendida pr : list) {
        if (tipoAlumno != Constants.PIE_ALL && !pr.getTipoalumno_id().equals(tipoAlumno))
          continue;
        R_Alumno al = lstAlumnos.stream().filter(a -> a.getId().equals(pr.getAlumno_id())).findFirst().orElse(null);
        OTPruebaRendida ot = new OTPruebaRendida(pr, al);
        oList.add(ot);

      }
      tblAlumnos.setItems(oList);
    }

    final ObservableList<OTResumenGeneral> oList = FXCollections.observableArrayList(listaResumen);
    tblResumen.setItems(oList);
  }

  private void generateReport() {

    if (evaluacionPrueba == null || cmbTipoAlumno.getItems().isEmpty() || curso == null || asignatura == null
        || prueba == null || lstPruebasRendidas == null || lstAlumnos == null)
      return;

    final List<OTResumenGeneral> listaResumen = procesarValoresMinMax(lstPruebasRendidas);
    asignarDatosEvalucion(evaluacionPrueba, listaResumen);

  }

  @Override
  public void handle(ActionEvent event) {
    final Object source = event.getSource();
    if (source == mnuExportarResumen || source == mnuExportarAlumnos) {

      tblAlumnos.setId("Alumnos");
      tblResumen.setId("Resumen");

      final List<TableView<? extends Object>> listaTablas = new LinkedList<>();
      listaTablas.add(tblAlumnos);
      listaTablas.add(tblResumen);

      ExcelSheetWriterObj.convertirDatosALibroDeExcel(listaTablas);
    } else if (source == btnGenerar) {
      generateReport();
    }
  }

  private void inicializarTablaAlumnos() {
    tblAlumnos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    colRut.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, String>("rut"));
    colName.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, String>("nombre"));
    colPaterno.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, String>("paterno"));
    colMaterno.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, String>("materno"));
    colABuenas.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, Integer>("buenas"));
    colAMalas.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, Integer>("malas"));
    colAOmitidas.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, Integer>("omitidas"));
    colPBuenas.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, Float>("pbuenas"));
    colAPuntaje.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, Integer>("puntaje"));
    colPPuntaje.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, Float>("ppuntajes"));
    colANota.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, Float>("nota"));

  }

  private void inicializarTablaResumen() {
    tblResumen.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    colNombre.setCellValueFactory(new PropertyValueFactory<OTResumenGeneral, String>("name"));
    colNotas.setCellValueFactory(new PropertyValueFactory<OTResumenGeneral, Float>("nota"));
    colBuenas.setCellValueFactory(new PropertyValueFactory<OTResumenGeneral, Float>("pbuenas"));
    ColPuntos.setCellValueFactory(new PropertyValueFactory<OTResumenGeneral, Integer>("puntaje"));
    colPuntaje.setCellValueFactory(new PropertyValueFactory<OTResumenGeneral, Float>("ppuntaje"));

  }

  @FXML
  public void initialize() {
    setTitle("Resumen de respuestas generales");
    inicializarTablaResumen();
    inicializarTablaAlumnos();
    mnuExportarAlumnos.setOnAction(this);
    mnuExportarResumen.setOnAction(this);
    btnGenerar.setOnAction(this);

    cmbTipoAlumno.setOnAction(event -> {
      tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedIndex();
    });
  }

  @Override
  public void onDataArrived(List<Object> list) {
    if (list != null && !list.isEmpty()) {
      final Object entity = list.get(0);
      if (entity instanceof R_TipoAlumno) {
        final ObservableList<R_TipoAlumno> tAlumnoList = FXCollections.observableArrayList();
        for (final Object iEntity : list) {
          tAlumnoList.add((R_TipoAlumno) iEntity);
        }
        cmbTipoAlumno.setItems(tAlumnoList);
        cmbTipoAlumno.getSelectionModel().select(0);
        generateReport();
      }
      if (entity instanceof R_PruebaRendida) {
        lstPruebasRendidas = list.stream().map(p -> (R_PruebaRendida) p).collect(Collectors.toList());
        generateReport();
      }
    }
  }

  @Override
  public void onFound(IEntity entity) {
    if (entity instanceof R_EvaluacionPrueba) {
      evaluacionPrueba = (R_EvaluacionPrueba) entity;
      prueba = controller.findByIdSynchro(R_Prueba.class, evaluacionPrueba.getPrueba_id());
      txtPrueba.setText(prueba.getName());
      asignatura = controller.findByIdSynchro(R_Asignatura.class, prueba.getAsignatura_id());
      txtAsignatura.setText(asignatura.getName());
      curso = controller.findByIdSynchro(R_Curso.class, evaluacionPrueba.getCurso_id());
      txtCurso.setText(curso.getName());
      txtExigencia.setText(String.format("%d", prueba.getExigencia()));
      txtNoPregunta.setText(String.format("%d", prueba.getNropreguntas()));
      txtPjePrueba.setText(String.format("%d", prueba.getPuntajebase()));

      Map<String, Object> params = MapBuilder.<String, Object>unordered().put("curso_id", curso.getId()).build();
      lstAlumnos = controller.findByParamsSynchro(R_Alumno.class, params);
      params = MapBuilder.<String, Object>unordered().put("evaluacionprueba_id", evaluacionPrueba.getId()).build();
      controller.findByParam(R_PruebaRendida.class, params, this);
    }

  }

  private List<OTResumenGeneral> procesarValoresMinMax(List<R_PruebaRendida> pruebasRendidas) {
    notaMin = Float.MAX_VALUE;
    notaMax = Float.MIN_VALUE;
    pbuenasMin = Float.MAX_VALUE;
    pbuenasMax = Float.MIN_VALUE;
    ppuntajeMin = Float.MAX_VALUE;
    ppuntajeMax = Float.MIN_VALUE;
    puntajeMin = Integer.MAX_VALUE;
    puntajeMax = Integer.MIN_VALUE;
    int nCount = 0;
    for (final R_PruebaRendida pruebaRendida : pruebasRendidas) {

      if (tipoAlumno != Constants.PIE_ALL && !pruebaRendida.getTipoalumno_id().equals(tipoAlumno))
        continue;
      nCount++;
      if (pruebaRendida.getNota() < notaMin) {
        notaMin = pruebaRendida.getNota();
      }
      if (pruebaRendida.getNota() > notaMax) {
        notaMax = pruebaRendida.getNota();
      }

      if (pruebaRendida.getBuenas().floatValue() < pbuenasMin) {
        pbuenasMin = pruebaRendida.getBuenas().floatValue();
      }
      if (pruebaRendida.getBuenas().floatValue() > pbuenasMax) {
        pbuenasMax = pruebaRendida.getBuenas().floatValue();
      }

      if (pruebaRendida.getPpuntaje() < ppuntajeMin) {
        ppuntajeMin = pruebaRendida.getPpuntaje();
      }
      if (pruebaRendida.getPpuntaje() > ppuntajeMax) {
        ppuntajeMax = pruebaRendida.getPpuntaje();
      }

      if (pruebaRendida.getPuntaje() < puntajeMin) {
        puntajeMin = pruebaRendida.getPuntaje();
      }
      if (pruebaRendida.getPuntaje() > puntajeMax) {
        puntajeMax = pruebaRendida.getPuntaje();
      }
    }

    final List<OTResumenGeneral> listaResumen = new LinkedList<OTResumenGeneral>();

    if (nCount > 0) {

      final Float promNota = (notaMax + notaMin) / 2;
      final Float prompBuenas = (pbuenasMax + pbuenasMin) / 2;
      final Float prompPuntaje = (ppuntajeMax + ppuntajeMin) / 2;
      final Integer promPuntaje = (puntajeMax + puntajeMin) / 2;
      listaResumen.add(new OTResumenGeneral("Máximo", notaMax, pbuenasMax, ppuntajeMax, puntajeMax));
      listaResumen.add(new OTResumenGeneral("Mínimo", notaMin, pbuenasMin, ppuntajeMin, puntajeMin));
      listaResumen.add(new OTResumenGeneral("Promedio", promNota, prompBuenas, prompPuntaje, promPuntaje));
    } else {
      listaResumen.add(new OTResumenGeneral("Máximo", 0f, 0f, 0f, 0));
      listaResumen.add(new OTResumenGeneral("Mínimo", 0f, 0f, 0f, 0));
      listaResumen.add(new OTResumenGeneral("Promedio", 0f, 0f, 0f, 0));

    }

    return listaResumen;
  }
}
