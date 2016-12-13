package cl.eos.view;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import cl.eos.imp.view.AFormView;
import cl.eos.persistence.models.SCurso;
import cl.eos.persistence.models.SEvaluacionPrueba;
import cl.eos.persistence.models.SRangoEvaluacion;
import cl.eos.persistence.models.STipoAlumno;
import cl.eos.persistence.models.STipoPrueba;
import cl.eos.util.ExcelSheetWriterObj;
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
import javafx.scene.control.cell.PropertyValueFactory;

public class EvaluacionPruebaView extends AFormView implements EventHandler<ActionEvent> {

    @FXML
    private TableView<SEvaluacionPrueba> tblListadoPruebas;
    @FXML
    private TableColumn<SEvaluacionPrueba, LocalDate> fechaCol;
    @FXML
    private TableColumn<SEvaluacionPrueba, SCurso> cursoCol;
    @FXML
    private TableColumn<SEvaluacionPrueba, String> nameCol;
    @FXML
    private TableColumn<SEvaluacionPrueba, STipoPrueba> colTipo;
    @FXML
    private TableColumn<SEvaluacionPrueba, String> asignaturaCol;
    @FXML
    private TableColumn<SEvaluacionPrueba, String> profesorCol;
    @FXML
    private TableColumn<SEvaluacionPrueba, Integer> nroPreguntasCol;
    @FXML
    private TableColumn<SEvaluacionPrueba, Integer> formasCol;
    @FXML
    private TableColumn<SEvaluacionPrueba, Integer> colExigencia;
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

    private SEvaluacionPrueba evaluacionPrueba;
    private ResumenGeneralView resumenGeneral;
    private ResumenAlumnoView resumenAlumno;
    private ResumenRespuestaView resumenRespuestas;
    private ResumenHabilidadesView resumeHabilidad;
    private ResumenEjesTematicosView resumeEjeTematico;
    private ResumenGeneralPMEView resumenGeneralPME;

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
            final SEvaluacionPrueba ot = tblListadoPruebas.getSelectionModel().getSelectedItem();
            // List<R_PruebaRendida> pRendidas = ot.getPruebasRendidas();
            // while(pRendidas.size() > 0)
            // {
            // delete(pRendidas.get(0), false);
            // }
            delete(ot);
            tblListadoPruebas.getItems().remove(index);
        }

    }

    private void handleResumenAlumno() {
        evaluacionPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem();
        if (evaluacionPrueba != null) {
            resumenAlumno = (ResumenAlumnoView) show("/curso/fxml/ResumenAlumno.fxml");

            controller.findById(SEvaluacionPrueba.class, evaluacionPrueba.getId(), resumenAlumno);
            controller.findAll(STipoAlumno.class, resumenAlumno);
        } else {
            final Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Selección registro");
            alert.setHeaderText(null);
            alert.setContentText("Debe seleccionar registro a procesar");
            alert.show();
        }
    }

    private void handleResumenEje() {
        evaluacionPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem();
        if (evaluacionPrueba != null) {
            resumeEjeTematico = (ResumenEjesTematicosView) show("/curso/fxml/ResumenEjesTematicos.fxml");
            controller.findById(SEvaluacionPrueba.class, evaluacionPrueba.getId(), resumeEjeTematico);
            controller.findAll(STipoAlumno.class, resumeEjeTematico);
        } else {
            final Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Selección registro");
            alert.setHeaderText("Resumen Respuestas por Ejes Temáticos");
            alert.setContentText("Debe seleccionar registro a procesar");
            alert.show();
        }

    }

    private void handleResumenGeneral() {
        evaluacionPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem();
        if (evaluacionPrueba != null) {
            resumenGeneral = (ResumenGeneralView) show("/curso/fxml/ResumenGeneral.fxml");
            controller.findById(SEvaluacionPrueba.class, evaluacionPrueba.getId(), resumenGeneral);
            controller.findAll(STipoAlumno.class, resumenGeneral);
        } else {

            final Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Selección registro");
            alert.setHeaderText("Resumen de respuestas generales");
            alert.setContentText("Debe seleccionar registro a procesar");
            alert.show();
        }
    }

    private void handleResumenHabilidad() {
        evaluacionPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem();
        if (evaluacionPrueba != null) {
            resumeHabilidad = (ResumenHabilidadesView) show("/curso/fxml/ResumenHabilidades.fxml");
            controller.findById(SEvaluacionPrueba.class, evaluacionPrueba.getId(), resumeHabilidad);
            controller.findAll(STipoAlumno.class, resumeHabilidad);
        } else {
            final Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Selección registro");
            alert.setHeaderText("Resumen Respuestas por Habilidades");
            alert.setContentText("Debe seleccionar registro a procesar");
            alert.show();
        }
    }

    private void handleResumenRespuesta() {
        evaluacionPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem();
        if (evaluacionPrueba != null) {
            if (resumenRespuestas == null) {
                resumenRespuestas = (ResumenRespuestaView) show("/curso/fxml/ResumenRespuestas.fxml");
            } else {
                show(resumenRespuestas);
            }
            controller.findById(SEvaluacionPrueba.class, evaluacionPrueba.getId(), resumenRespuestas);
            controller.findAll(STipoAlumno.class, resumenRespuestas);
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
        evaluacionPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem();
        if (evaluacionPrueba != null) {
            final ResumenXAlumnoEjeHabilidadView resXAlumnoEjeHab = (ResumenXAlumnoEjeHabilidadView) show(
                    "/curso/fxml/ResumenXAlumnoEjeHabilidad.fxml");
            controller.findById(SEvaluacionPrueba.class, evaluacionPrueba.getId(), resXAlumnoEjeHab);
            controller.findAll(STipoAlumno.class, resXAlumnoEjeHab);
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
        final SEvaluacionPrueba evaluacion = tblListadoPruebas.getSelectionModel().getSelectedItem();
        if (evaluacion != null) {
            if (resumenGeneralPME == null) {
                resumenGeneralPME = (ResumenGeneralPMEView) show("/curso/fxml/ResumenGeneralPME.fxml");
            } else {
                show(resumenGeneralPME);
            }
            controller.findById(SEvaluacionPrueba.class, evaluacion.getId(), resumenGeneralPME);
            controller.findAll(SRangoEvaluacion.class);
            controller.findAll(STipoAlumno.class);
        } else {
            final Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Selección registro");
            alert.setHeaderText("Resumen general P.M.E.");
            alert.setContentText("Debe seleccionar registro a procesar");
            alert.show();
        }
    }

    private void handlerResumenPorObjetivos() {
        evaluacionPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem();
        if (evaluacionPrueba != null) {

            final PorObjetivosView porObjetivosView = (PorObjetivosView) show("/curso/fxml/PorObjetivos.fxml");
            controller.findAll(STipoAlumno.class, porObjetivosView);
            controller.findById(SEvaluacionPrueba.class, evaluacionPrueba.getId(), porObjetivosView);
        } else {
            final Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Selección registro");
            alert.setHeaderText("Resumen Eje/R_Habilidad por alumno.");
            alert.setContentText("Debe seleccionar registro a procesar");
            alert.show();
        }
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
        nameCol.setCellValueFactory(new PropertyValueFactory<SEvaluacionPrueba, String>("name"));
        fechaCol.setCellValueFactory(new PropertyValueFactory<SEvaluacionPrueba, LocalDate>("fechaLocal"));
        colTipo.setCellValueFactory(new PropertyValueFactory<SEvaluacionPrueba, STipoPrueba>("tipo"));
        cursoCol.setCellValueFactory(new PropertyValueFactory<SEvaluacionPrueba, SCurso>("curso"));
        asignaturaCol.setCellValueFactory(new PropertyValueFactory<SEvaluacionPrueba, String>("asignatura"));
        formasCol.setCellValueFactory(new PropertyValueFactory<SEvaluacionPrueba, Integer>("formas"));
        profesorCol.setCellValueFactory(new PropertyValueFactory<SEvaluacionPrueba, String>("profesor"));
        nroPreguntasCol.setCellValueFactory(new PropertyValueFactory<SEvaluacionPrueba, Integer>("nroPreguntas"));
        colExigencia.setCellValueFactory(new PropertyValueFactory<SEvaluacionPrueba, Integer>("exigencia"));

        mnuEliminarEvaluacion.setOnAction(this);
    }

    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            tblListadoPruebas.getSelectionModel().clearSelection();
            final Object entity = list.get(0);
            if (entity instanceof SEvaluacionPrueba) {
                final ObservableList<SEvaluacionPrueba> evaluaciones = FXCollections.observableArrayList();
                for (final Object lEntity : list) {
                    if (((SEvaluacionPrueba) lEntity).getPruebasRendidas().size() > 0) {
                        evaluaciones.add((SEvaluacionPrueba) lEntity);
                    }
                }
                tblListadoPruebas.setItems(evaluaciones);
            }
        }
    }

}
