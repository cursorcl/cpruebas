package cl.eos.view;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import cl.eos.imp.view.AFormView;
import cl.eos.ot.OTEvaluacionPrueba;
import cl.eos.restful.tables.R_Alumno;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_NivelEvaluacion;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_RangoEvaluacion;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.restful.tables.R_TipoPrueba;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.MapBuilder;
import curso.PorObjetivosView;
import curso.ResumenAlumnoView;
import curso.ResumenEjesTematicosView;
import curso.ResumenGeneralPMEView;
import curso.ResumenGeneralView;
import curso.ResumenHabilidadesView;
import curso.ResumenRespuestaView;
import curso.ResumenXAlumnoEjeHabilidadView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class EvaluacionPruebaView extends AFormView implements EventHandler<ActionEvent> {
  @FXML
  private TableView<OTEvaluacionPrueba> tblListadoPruebas;
  @FXML
  private TableColumn<OTEvaluacionPrueba, LocalDate> fechaCol;
  @FXML
  private TableColumn<OTEvaluacionPrueba, R_Curso> cursoCol;
  @FXML
  private TableColumn<OTEvaluacionPrueba, String> nameCol;
  @FXML
  private MenuItem mnuResumenGeneral;
  @FXML
  private MenuItem mnuResumenAlumno;
  @FXML
  private MenuItem mnuResEjeHabxAlumno;
  @FXML
  private MenuItem mnuRespuestasPregunta;
  @FXML
  private MenuItem mnuRespuestasHabilidad;
  @FXML
  private MenuItem mnuRespuestasEje;
  @FXML
  private MenuItem menuResumenGeneral;
  @FXML
  private MenuItem menuResumenAlumno;
  @FXML
  private MenuItem menuRespuestasPregunta;
  @FXML
  private MenuItem menuRespuestasHabilidad;
  @FXML
  private MenuItem menuRespuestasEje;
  @FXML
  private MenuItem mnuResumenPME;
  @FXML
  private MenuItem menuResumenPME;
  @FXML
  private MenuItem mnuEjeHabXAlumno;
  @FXML
  private MenuItem mnuExportarExcel;
  @FXML
  private MenuItem menuExportarExcel;
  @FXML
  private MenuItem mnuPorObjetivos;
  @FXML
  private MenuItem mnuItemPorObjetivos;
  @FXML
  private MenuItem mnuEliminarEvaluacion;
  @FXML
  private TextField txtAsignatura;
  @FXML
  private TextField txtTipoPrueba;
  @FXML
  private TextField txtNroPreguntas;
  @FXML
  private TextField txtExigencia;
  private R_EvaluacionPrueba evaluacionPrueba;
  private ResumenGeneralView resumenGeneral;
  private ResumenAlumnoView resumenAlumno;
  private ResumenRespuestaView resumenRespuestas;
  private ResumenHabilidadesView resumeHabilidad;
  private ResumenEjesTematicosView resumeEjeTematico;
  private ResumenGeneralPMEView resumenGeneralPME;
  private R_Prueba prueba;
  private R_Asignatura asignatura;
  private R_TipoPrueba tipoPrueba;

  public EvaluacionPruebaView() {
    setTitle("Listado de evaluaciones");
  }

  @Override
  public void handle(ActionEvent event) {
    final Object source = event.getSource();
    if (source == mnuResumenGeneral || source == menuResumenGeneral) {
      handleResumenGeneral();
    } else if (source == mnuResumenAlumno || source == menuResumenAlumno) {
      handleResumenAlumno();
    } else if (source == mnuRespuestasPregunta || source == menuRespuestasPregunta) {
      handleResumenRespuesta();
    } else if (source == mnuRespuestasHabilidad || source == menuRespuestasHabilidad) {
      handleResumenHabilidad();
    } else if (source == mnuRespuestasEje || source == menuRespuestasEje) {
      handleResumenEje();
    } else if (source == mnuResumenPME || source == menuResumenPME) {
      handlerResumenPME();
    } else if (source == mnuExportarExcel || source == menuExportarExcel) {
      handlerResumenExcel();
    } else if (source == mnuEjeHabXAlumno || source == mnuResEjeHabxAlumno) {
      handlerResumenEjeHabXAlumno();
    } else if (source == mnuPorObjetivos || source == mnuItemPorObjetivos) {
      handlerResumenPorObjetivos();
    } else if (source == mnuEliminarEvaluacion) {
      handleEliminarEvaluacion();
    }
    tblListadoPruebas.getSelectionModel().clearSelection();
  }

  protected void handleEliminarEvaluacion() {
    final Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Confirma Eliminación");
    alert.setHeaderText("Borrará todas las pruebas rendidas asociadas.");
    alert.setContentText("Está seguro que quiere eliminar la evaluación?");
    final Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == ButtonType.OK) {
      final int index = tblListadoPruebas.getSelectionModel().getSelectedIndex();
      final R_EvaluacionPrueba ot = tblListadoPruebas.getSelectionModel().getSelectedItem().getEvaluacionPrueba();
      delete(ot);
      tblListadoPruebas.getItems().remove(index);
    }
  }

  private void handleResumenAlumno() {
    evaluacionPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getEvaluacionPrueba();
    if (evaluacionPrueba != null) {
      resumenAlumno = (ResumenAlumnoView) show("/curso/fxml/ResumenAlumno.fxml");
      controller.findById(R_EvaluacionPrueba.class, evaluacionPrueba.getId(), resumenAlumno);
      controller.findAll(R_TipoAlumno.class, resumenAlumno);

      Map<String, Object> params =
          MapBuilder.<String, Object>unordered().put("curso_id", evaluacionPrueba.getCurso_id()).build();
      controller.findByParam(R_Alumno.class, params, resumenAlumno);
    } else {
      final Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Selección registro");
      alert.setHeaderText(null);
      alert.setContentText("Debe seleccionar registro a procesar");
      alert.show();
    }
  }

  private void handleResumenEje() {
    evaluacionPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getEvaluacionPrueba();
    if (evaluacionPrueba != null) {

      resumeEjeTematico = (ResumenEjesTematicosView) show("/curso/fxml/ResumenEjesTematicos.fxml");
      resumeEjeTematico.setPrueba(prueba);
      controller.findById(R_Asignatura.class, evaluacionPrueba.getAsignatura_id());
      controller.findById(R_EvaluacionPrueba.class, evaluacionPrueba.getId(), resumeEjeTematico);
      controller.findAll(R_TipoAlumno.class, resumeEjeTematico);
    } else {
      final Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Selección registro");
      alert.setHeaderText("Resumen Respuestas por Ejes Temáticos");
      alert.setContentText("Debe seleccionar registro a procesar");
      alert.show();
    }
  }

  private void handleResumenGeneral() {
    evaluacionPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getEvaluacionPrueba();
    if (evaluacionPrueba != null) {
      resumenGeneral = (ResumenGeneralView) show("/curso/fxml/ResumenGeneral.fxml");
      controller.findById(R_EvaluacionPrueba.class, evaluacionPrueba.getId(), resumenGeneral);
      controller.findAll(R_TipoAlumno.class, resumenGeneral);

    } else {
      final Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Selección registro");
      alert.setHeaderText("Resumen de respuestas generales");
      alert.setContentText("Debe seleccionar registro a procesar");
      alert.show();
    }
  }

  private void handleResumenHabilidad() {
    evaluacionPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getEvaluacionPrueba();
    if (evaluacionPrueba != null) {
      resumeHabilidad = (ResumenHabilidadesView) show("/curso/fxml/ResumenHabilidades.fxml");

      controller.findById(R_Prueba.class, evaluacionPrueba.getPrueba_id());
      controller.findById(R_Asignatura.class, evaluacionPrueba.getAsignatura_id());
      controller.findById(R_EvaluacionPrueba.class, evaluacionPrueba.getId(), resumeHabilidad);
      controller.findAll(R_TipoAlumno.class, resumeHabilidad);
    } else {
      final Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Selección registro");
      alert.setHeaderText("Resumen Respuestas por Habilidades");
      alert.setContentText("Debe seleccionar registro a procesar");
      alert.show();
    }
  }

  private void handleResumenRespuesta() {
    evaluacionPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getEvaluacionPrueba();
    if (evaluacionPrueba != null) {
      if (resumenRespuestas == null) {
        resumenRespuestas = (ResumenRespuestaView) show("/curso/fxml/ResumenRespuestas.fxml");
      } else {
        show(resumenRespuestas);
      }

      controller.findAll(R_TipoAlumno.class, resumenRespuestas);
      controller.findById(R_Prueba.class, evaluacionPrueba.getPrueba_id());
      controller.findById(R_Asignatura.class, evaluacionPrueba.getAsignatura_id());
      controller.findById(R_EvaluacionPrueba.class, evaluacionPrueba.getId(), resumenRespuestas);

    } else {
      final Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Selección registro");
      alert.setHeaderText("Resumen de respuestas por pregunta");
      alert.setContentText("Debe seleccionar registro a procesar");
      alert.show();
    }
  }

  /**
   * Despliega HMI para presentar resultados de EjeHabXAlumno:
   */
  private void handlerResumenEjeHabXAlumno() {
    evaluacionPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getEvaluacionPrueba();
    if (evaluacionPrueba != null) {
      final ResumenXAlumnoEjeHabilidadView resXAlumnoEjeHab =
          (ResumenXAlumnoEjeHabilidadView) show("/curso/fxml/ResumenXAlumnoEjeHabilidad.fxml");
      controller.findById(R_EvaluacionPrueba.class, evaluacionPrueba.getId(), resXAlumnoEjeHab);
      controller.findAll(R_TipoAlumno.class, resXAlumnoEjeHab);
    } else {
      final Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Selección registro");
      alert.setHeaderText("Resumen Eje/R_Habilidad por alumno.");
      alert.setContentText("Debe seleccionar registro a procesar");
      alert.show();
    }
  }

  private void handlerResumenExcel() {
    tblListadoPruebas.setId("Listado de pruebas");
    ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblListadoPruebas);
  }

  private void handlerResumenPME() {
    final R_EvaluacionPrueba evaluacion = tblListadoPruebas.getSelectionModel().getSelectedItem().getEvaluacionPrueba();
    if (evaluacion != null) {
      resumenGeneralPME = (ResumenGeneralPMEView) show("/curso/fxml/ResumenGeneralPME.fxml");
      resumenGeneralPME.setPrueba(prueba);
      controller.findById(R_EvaluacionPrueba.class, evaluacion.getId());
      controller.findById(R_NivelEvaluacion.class, prueba.getNivelevaluacion_id());
      controller.findAll(R_RangoEvaluacion.class);
      controller.findAll(R_TipoAlumno.class);
    } else {
      final Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Selección registro");
      alert.setHeaderText("Resumen general P.M.E.");
      alert.setContentText("Debe seleccionar registro a procesar");
      alert.show();
    }
  }

  private void handlerResumenPorObjetivos() {
    evaluacionPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getEvaluacionPrueba();
    if (evaluacionPrueba != null) {
      final PorObjetivosView porObjetivosView = (PorObjetivosView) show("/curso/fxml/PorObjetivos.fxml");
      controller.findAll(R_TipoAlumno.class, porObjetivosView);
      controller.findById(R_EvaluacionPrueba.class, evaluacionPrueba.getId(), porObjetivosView);
    } else {
      final Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Selección registro");
      alert.setHeaderText("Resumen Eje/R_Habilidad por alumno.");
      alert.setContentText("Debe seleccionar registro a procesar");
      alert.show();
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
    asignatura = controller.findByIdSynchro(R_Asignatura.class, prueba.getAsignatura_id());
    tipoPrueba = controller.findByIdSynchro(R_TipoPrueba.class, prueba.getTipoprueba_id());
    txtAsignatura.setText(asignatura.getName());
    txtTipoPrueba.setText(tipoPrueba.getName());
    txtNroPreguntas.setText(String.format("%02d", prueba.getNropreguntas().intValue()));
    txtExigencia.setText(String.format("%d%%", prueba.getExigencia().intValue()));
  }

  /**
   * @return the asignatura
   */
  public final R_Asignatura getAsignatura() {
    return asignatura;
  }

  /**
   * @param asignatura the asignatura to set
   */
  public final void setAsignatura(R_Asignatura asignatura) {
    this.asignatura = asignatura;
  }

  /**
   * @return the tipoPrueba
   */
  public final R_TipoPrueba getTipoPrueba() {
    return tipoPrueba;
  }

  /**
   * @param tipoPrueba the tipoPrueba to set
   */
  public final void setTipoPrueba(R_TipoPrueba tipoPrueba) {
    this.tipoPrueba = tipoPrueba;
  }

  @FXML
  public void initialize() {
    mnuResumenGeneral.setOnAction(this);
    mnuResumenAlumno.setOnAction(this);
    mnuRespuestasPregunta.setOnAction(this);
    mnuRespuestasHabilidad.setOnAction(this);
    mnuRespuestasEje.setOnAction(this);
    mnuResumenPME.setOnAction(this);
    menuResumenGeneral.setOnAction(this);
    menuResumenAlumno.setOnAction(this);
    menuRespuestasPregunta.setOnAction(this);
    menuRespuestasHabilidad.setOnAction(this);
    menuRespuestasEje.setOnAction(this);
    menuResumenPME.setOnAction(this);
    mnuEjeHabXAlumno.setOnAction(this);
    mnuResEjeHabxAlumno.setOnAction(this);
    mnuExportarExcel.setOnAction(this);
    menuExportarExcel.setOnAction(this);
    mnuPorObjetivos.setOnAction(this);
    mnuItemPorObjetivos.setOnAction(this);
    tblListadoPruebas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    nameCol.setCellValueFactory(new PropertyValueFactory<OTEvaluacionPrueba, String>("name"));
    fechaCol.setCellValueFactory(new PropertyValueFactory<OTEvaluacionPrueba, LocalDate>("fechaLocal"));
    cursoCol.setCellValueFactory(new PropertyValueFactory<OTEvaluacionPrueba, R_Curso>("curso"));
    mnuEliminarEvaluacion.setOnAction(this);
  }

  @Override
  public void onDataArrived(List<Object> list) {
    if (list != null && !list.isEmpty()) {
      tblListadoPruebas.getSelectionModel().clearSelection();
      final Object entity = list.get(0);
      if (entity instanceof R_EvaluacionPrueba) {
        final ObservableList<OTEvaluacionPrueba> evaluaciones = FXCollections.observableArrayList();
        for (final Object lEntity : list) {
          R_EvaluacionPrueba evaluacion = (R_EvaluacionPrueba) lEntity;
          R_Curso curso = controller.findByIdSynchro(R_Curso.class, evaluacion.getCurso_id());
          OTEvaluacionPrueba ot = new OTEvaluacionPrueba.Builder().curso(curso).evaluacionPrueba(evaluacion).build();
          evaluaciones.add(ot);
        }
        tblListadoPruebas.setItems(evaluaciones);
      }
    }
  }
}
