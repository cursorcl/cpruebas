package cl.eos.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_EvaluacionEjetematico;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_Habilidad;
import cl.eos.restful.tables.R_NivelEvaluacion;
import cl.eos.restful.tables.R_Profesor;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_Prueba.Estado;
import cl.eos.restful.tables.R_RangoEvaluacion;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.restful.tables.R_TipoColegio;
import cl.eos.restful.tables.R_TipoCurso;
import cl.eos.restful.tables.R_TipoPrueba;
import cl.eos.util.Utils;
import cl.eos.view.editablecells.PruebaCellFactory;
import cl.eos.view.ots.OTPrueba;
import colegio.ComparativoColegioEjeEvaluacionView;
import colegio.ComparativoColegioEjeHabilidadView;
import colegio.ComparativoColegioEjeHabilidadxCursoView;
import colegio.ComparativoColegioHabilidadesView;
import colegio.ResumenColegioView;
import colegio.ResumenColegioXAlumnoEjeHabilidadView;
import colegio.nivel.Nivel_ComparativoColegioEjeEvaluacionView;
import colegio.nivel.Nivel_ComparativoColegioEjeHabilidadView;
import colegio.nivel.Nivel_ComparativoColegioEjeHabilidadxCursoView;
import colegio.nivel.Nivel_ComparativoColegioHabilidadesView;
import colegio.nivel.Nivel_ResumenColegioView;
import comunal.ComparativoComunalEjeView;
import comunal.ComparativoComunalHabilidadView;
import comunal.ComunalCursoView;
import comunal.nivel.Nivel_ComparativoComunalEjeView;
import comunal.nivel.Nivel_ComparativoComunalHabilidadView;
import informe.InformeView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import jfxtras.labs.scene.control.BigDecimalField;

public class PruebasView extends AFormView implements EventHandler<ActionEvent> {
    @FXML private StackPane pnlRoot;
    @FXML private AnchorPane pnlEdition;
    @FXML private TableView<OTPrueba> tblListadoPruebas;
    @FXML private TableColumn<OTPrueba, LocalDate> fechaCol;
    @FXML private TableColumn<OTPrueba, String> cursoCol;
    @FXML private TableColumn<OTPrueba, String> nameCol;
    @FXML private TableColumn<OTPrueba, String> asignaturaCol;
    @FXML private TableColumn<OTPrueba, String> profesorCol;
    @FXML private TableColumn<OTPrueba, Integer> nroPreguntasCol;
    @FXML private TableColumn<OTPrueba, Integer> formasCol;
    @FXML private TableColumn<OTPrueba, Integer> alternativasCol;
    @FXML private TableColumn<OTPrueba, Estado> estadoCol;
    @FXML private ComboBox<R_TipoPrueba> cmbTipoPrueba;
    @FXML private ComboBox<R_Profesor> cmbProfesor;
    @FXML private ComboBox<R_TipoCurso> cmbCurso;
    @FXML private ComboBox<R_Asignatura> cmbAsignatura;
    @FXML private BigDecimalField bigDecimalForma;
    @FXML private BigDecimalField bigDecimaNroAlternativas;
    @FXML private BigDecimalField bigDecimalNroPreguntas;
    @FXML private BigDecimalField bigDecimalPuntajePregunta;
    @FXML private BigDecimalField bigDecimalExigencia;
    @FXML private ComboBox<R_NivelEvaluacion> cmbNivelEvaluacion;
    @FXML private Label lblError;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtName;
    @FXML private MenuItem mnuGrabar;
    @FXML private MenuItem mnuModificar;
    @FXML private MenuItem mnuEliminar;
    @FXML private MenuItem mnuPopupModificar;
    @FXML private MenuItem mnuPopupEliminar;
    @FXML private MenuItem mnuEvaluarPrueba;
    @FXML private MenuItem mnuAnularPregunta;
    @FXML private MenuItem mnuDefinirPrueba;
    @FXML private MenuItem mnuListaEvaluaciones;
    @FXML private MenuItem mnuComparativoComunal;
    @FXML private MenuItem mnuComparativoComunalNivel;
    @FXML private MenuItem mnuComparativoComunalHab;
    @FXML private MenuItem mnuComparativoComunalHabNivel;
    @FXML private MenuItem mnuComparativoColegioEjeHabil;
    @FXML private MenuItem mnuCompColegioEjeHabilXCurso;
    @FXML private MenuItem mnuComparativoColegioEjeHabilXNivel;
    @FXML private MenuItem mnuInforme;
    @FXML private MenuItem mnuColegio;
    @FXML private MenuItem mnuComunalEje;
    @FXML private MenuItem mnuEjesEvaluacion;
    @FXML private MenuItem mnuHabilidadEvaluacion;
    @FXML private MenuItem mnuHabilidadEvaluacionXNivel;
    @FXML private MenuItem mnuHabilidadEvaluacionXAlumno;
    @FXML private MenuItem mnuNueva;
    @FXML private MenuItem mnuImprimirPrueba;
    @FXML private MenuItem mnuCompEjesHabXNivel;
    @FXML private MenuItem mnuHabilidadEvaluacionXAlumnoXNivel;
    @FXML private MenuItem mnuColegioXNivel;
    private EvaluacionPruebaView evaluacionPrueba;
    private DefinePruebaViewController definePrueba;
    private AnularPreguntasViewController anularPregunta;
    private R_Prueba prueba;
    private ComparativoComunalEjeView comparativoComunal;
    private ComparativoComunalHabilidadView comparativoComunalHabilidad;
    private ComunalCursoView comunalEje;
    private EvaluarPruebaView evaluarPruebaView;
    private ImprimirPruebaView imprimirPrueba;
    private ResumenColegioView resumenColegio;
    public PruebasView() {
        setTitle("Pruebas");
    }
    private void accionClicTabla() {
        tblListadoPruebas.setOnMouseClicked(event -> {
            final ObservableList<OTPrueba> itemsSelec = tblListadoPruebas.getSelectionModel().getSelectedItems();
            if (itemsSelec.size() > 1) {
                mnuPopupEliminar.setDisable(false);
                mnuEliminar.setDisable(false);
                mnuComunalEje.setDisable(false);
                mnuPopupModificar.setDisable(true);
                mnuModificar.setDisable(true);
                mnuImprimirPrueba.setDisable(true);
                mnuListaEvaluaciones.setDisable(true);
                mnuDefinirPrueba.setDisable(true);
                mnuEvaluarPrueba.setDisable(true);
                mnuAnularPregunta.setDisable(true);
                mnuComparativoComunal.setDisable(true);
                mnuComparativoComunalHab.setDisable(true);
            } else if (itemsSelec.size() == 1) {
                final OTPrueba prueba = itemsSelec.get(0);
                mnuModificar.setDisable(false);
                mnuEliminar.setDisable(false);
                mnuPopupModificar.setDisable(false);
                mnuPopupEliminar.setDisable(false);
                mnuImprimirPrueba.setDisable(false);
                mnuAnularPregunta.setDisable(false);
                final boolean estadoDefinida = prueba.getEstado().equals(Estado.DEFINIDA);
                final boolean estadoEvaluada = prueba.getEstado().equals(Estado.EVALUADA);
                final boolean estadoCreada = prueba.getEstado().equals(Estado.CREADA);
                if (estadoDefinida) {
                    mnuEvaluarPrueba.setDisable(!estadoDefinida);
                    mnuListaEvaluaciones.setDisable(estadoDefinida);
                    mnuComunalEje.setDisable(estadoDefinida);
                    mnuComparativoComunal.setDisable(estadoDefinida);
                    mnuComparativoComunalHab.setDisable(estadoDefinida);
                } else if (estadoEvaluada) {
                    mnuEvaluarPrueba.setDisable(!estadoEvaluada);
                    mnuDefinirPrueba.setDisable(!estadoEvaluada);
                    mnuListaEvaluaciones.setDisable(!estadoEvaluada);
                    mnuComunalEje.setDisable(!estadoEvaluada);
                    mnuComparativoComunal.setDisable(!estadoEvaluada);
                    mnuComparativoComunalHab.setDisable(!estadoEvaluada);
                } else if (estadoCreada) {
                    mnuEvaluarPrueba.setDisable(estadoCreada);
                    mnuDefinirPrueba.setDisable(!estadoCreada);
                    mnuListaEvaluaciones.setDisable(estadoCreada);
                    mnuComunalEje.setDisable(estadoCreada);
                    mnuComparativoComunal.setDisable(estadoCreada);
                    mnuComparativoComunalHab.setDisable(estadoCreada);
                    mnuAnularPregunta.setDisable(true);
                }
                mnuModificar.setDisable(!estadoCreada);
                mnuPopupModificar.setDisable(!estadoCreada);
            }
        });
    }
    @Override
    public void handle(ActionEvent event) {
        final Object source = event.getSource();
        if (source == mnuModificar || source == mnuPopupModificar) {
            handleModificar();
        } else if (source == mnuGrabar) {
            handleGrabar();
        } else if (source == mnuEliminar || source == mnuPopupEliminar) {
            handleEliminar();
        } else if (source == mnuEvaluarPrueba) {
            handlerEvaluar();
        } else if (source == mnuAnularPregunta) {
            handlerAnularPregunta();
        } else if (source == mnuDefinirPrueba) {
            handlerDefinirPrueba();
        } else if (source == mnuListaEvaluaciones) {
            handlerListaEvaluaciones();
        } else if (source == mnuComparativoComunal) {
            handlerComparativoComunal();
        } else if (source == mnuComparativoComunalNivel) {
            handlerComparativoComunalNivel();
        } else if (source == mnuComparativoComunalHab) {
            handlerComparativoComunalHab();
        } else if (source == mnuComparativoComunalHabNivel) {
            handlerComparativoComunalHabNivel();
        } else if (source == mnuComunalEje) {
            handlerComunalEje();
        } else if (source == mnuColegio) {
            handlerResumenColegios();
        } else if (source == mnuImprimirPrueba) {
            handlerImrpimirPrueba();
        } else if (source == mnuNueva) {
            handlerNuevaPrueba();
        } else if (source == mnuComparativoColegioEjeHabil) {
            handlerComparativoColegioEjeHabilidad();
        } else if (source == mnuComparativoColegioEjeHabilXNivel) {
            handlerComparativoColegioEjeHabilidadXNivel();
        } else if (source == mnuEjesEvaluacion) {
            handlerEjesEvaluacion();
        } else if (source == mnuHabilidadEvaluacion) {
            handlerHabilidadEvaluacion();
        } else if (source == mnuHabilidadEvaluacionXNivel) {
            handlerHabilidadEvaluacionXNivel();
        } else if (source == mnuHabilidadEvaluacionXAlumno) {
            handlerHabilidadEvaluacionXAlumno();
        } else if (source == mnuCompColegioEjeHabilXCurso) {
            handlerCompColegioEjeHabilXCurso();
        } else if (source == mnuCompEjesHabXNivel) {
            handlerCompEjesHabXNivel();
        } else if (source == mnuHabilidadEvaluacionXAlumnoXNivel) {
            handlerHabilidadEvaluacionXAlumnoXNivel();
        } else if (source == mnuColegioXNivel) {
            handlerResumenColegiosXNivel();
        } else if (source == mnuInforme) {
            handlerInforme();
        }
    }
    private void handleEliminar() {
        final ObservableList<OTPrueba> otSeleccionados = tblListadoPruebas.getSelectionModel().getSelectedItems();
        if (otSeleccionados != null && !otSeleccionados.isEmpty()) {
            final List<R_Prueba> pruebas = new ArrayList<R_Prueba>(otSeleccionados.size());
            for (final OTPrueba ot : otSeleccionados) {
                pruebas.add(ot.getPrueba());
            }
            delete(pruebas);
            tblListadoPruebas.getSelectionModel().clearSelection();
            limpiarCampos();
        }
    }
    private void handleGrabar() {
        removeAllStyles();
        if (validate()) {
            if (prueba == null) {
                prueba = new R_Prueba.Builder().id(Utils.getLastIndex()).build();
            }
            prueba.setAlternativas(bigDecimaNroAlternativas.getNumber().intValue());
            prueba.setAsignatura_id(cmbAsignatura.getValue().getId());
            prueba.setCurso_id(cmbCurso.getValue().getId());
            prueba.setFecha(dpFecha.getValue().toEpochDay());
            prueba.setNroformas(bigDecimalForma.getNumber().intValue());
            prueba.setName(txtName.getText());
            prueba.setNivelevaluacion_id(cmbNivelEvaluacion.getValue().getId());
            prueba.setProfesor_id(cmbProfesor.getValue().getId());
            prueba.setPuntajebase(bigDecimalPuntajePregunta.getNumber().intValue());
            prueba.setNropreguntas(bigDecimalNroPreguntas.getNumber().intValue());
            prueba.setAlternativas(bigDecimaNroAlternativas.getNumber().intValue());
            prueba.setTipoprueba_id(cmbTipoPrueba.getValue().getId());
            prueba.setExigencia(bigDecimalExigencia.getNumber().intValue());
            prueba.setEstado(R_Prueba.Estado.CREADA.getId());
            save(prueba);
        }
    }
    private void handleModificar() {
        if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
            prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
            if (prueba != null) {
                if (!R_Prueba.Estado.getEstado(prueba.getEstado()).equals(Estado.EVALUADA)) {
                    bigDecimaNroAlternativas.setNumber(new BigDecimal(prueba.getAlternativas()));
                    Optional<R_Asignatura> oAsig = cmbAsignatura.getItems().stream().filter(a -> prueba.getAsignatura_id().equals(a.getId()))
                            .findFirst();
                    cmbAsignatura.getSelectionModel().select(oAsig.isPresent() ? oAsig.get() : null);
                    Optional<R_TipoCurso> oCurso = cmbCurso.getItems().stream().filter(a -> prueba.getCurso_id().equals(a.getId())).findFirst();
                    cmbCurso.getSelectionModel().select(oCurso.isPresent() ? oCurso.get() : null);
                    dpFecha.setValue(prueba.getFechaLocal());
                    bigDecimalForma.setNumber(new BigDecimal(prueba.getNroformas()));
                    txtName.setText(prueba.getName());
                    Optional<R_NivelEvaluacion> oNivel = cmbNivelEvaluacion.getItems().stream()
                            .filter(a -> prueba.getNivelevaluacion_id().equals(a.getId())).findFirst();
                    cmbNivelEvaluacion.getSelectionModel().select(oNivel.isPresent() ? oNivel.get() : null);
                    cmbProfesor.getSelectionModel().select(prueba.getProfesor());
                    bigDecimalPuntajePregunta.setNumber(new BigDecimal(prueba.getPuntajeBase()));
                    bigDecimalNroPreguntas.setNumber(new BigDecimal(prueba.getNroPreguntas()));
                    cmbTipoPrueba.getSelectionModel().select(prueba.getTipoPrueba());
                    prueba.setExigencia(bigDecimalExigencia.getNumber().intValue());
                } else {
                    final Alert info = new Alert(AlertType.INFORMATION);
                    info.setTitle("No se puede modificar.");
                    info.setHeaderText("La prueba ya se encuentra evaluada.");
                    info.setContentText("No se podrá modificar.");
                    info.show();
                }
            }
        }
    }
    private void handlerAnularPregunta() {
        if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
            final R_Prueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
            anularPregunta = (AnularPreguntasViewController) show("/cl/eos/view/AnulaPreguntasView.fxml");
            if (prueba != null) {
                controller.findById(R_Prueba.class, prueba.getId(), anularPregunta);
            }
        }
    }
    private void handlerComparativoColegioEjeHabilidad() {
        final ComparativoColegioEjeHabilidadView resumenColegioEjeHabiliadad = (ComparativoColegioEjeHabilidadView) show(
                "/colegio/fxml/ComparativoColegioEjeHabilidad.fxml");
        show(resumenColegioEjeHabiliadad);
        controller.findAll(R_Colegio.class, resumenColegioEjeHabiliadad);
        controller.findAll(R_Asignatura.class, resumenColegioEjeHabiliadad);
        controller.findAll(R_EvaluacionEjetematico.class, resumenColegioEjeHabiliadad);
        controller.findAll(R_TipoAlumno.class, resumenColegioEjeHabiliadad);
    }
    private void handlerComparativoColegioEjeHabilidadXNivel() {
        final Nivel_ComparativoColegioEjeHabilidadView resumenColegioEjeHabiliadad = (Nivel_ComparativoColegioEjeHabilidadView) show(
                "/colegio/nivel/fxml/Nivel_ComparativoColegioEjeHabilidad.fxml");
        show(resumenColegioEjeHabiliadad);
        controller.findAll(R_Colegio.class, resumenColegioEjeHabiliadad);
        controller.findAll(R_Asignatura.class, resumenColegioEjeHabiliadad);
        controller.findAll(R_EvaluacionEjetematico.class, resumenColegioEjeHabiliadad);
        controller.findAll(R_TipoAlumno.class, resumenColegioEjeHabiliadad);
    }
    private void handlerComparativoComunal() {
        comparativoComunal = (ComparativoComunalEjeView) show("/comunal/fxml/ComparativoComunalEje.fxml");
        if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
            final R_Prueba pPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
            if (pPrueba != null) {
                controller.findById(R_Prueba.class, pPrueba.getId(), comparativoComunal);
                controller.findAll(R_EvaluacionEjetematico.class, comparativoComunal);
                controller.findAll(R_TipoAlumno.class, comparativoComunal);
                controller.findAll(R_TipoColegio.class, comparativoComunal);
            }
        }
    }
    private void handlerComparativoComunalHab() {
        comparativoComunalHabilidad = (ComparativoComunalHabilidadView) show("/comunal/fxml/ComparativoComunalHabilidad.fxml");
        if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
            final R_Prueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
            if (prueba != null) {
                controller.findById(R_Prueba.class, prueba.getId(), comparativoComunalHabilidad);
                controller.findAll(R_EvaluacionEjetematico.class, comparativoComunalHabilidad);
                controller.findAll(R_TipoAlumno.class, comparativoComunalHabilidad);
                controller.findAll(R_TipoColegio.class, comparativoComunalHabilidad);
            }
        }
    }
    private void handlerComparativoComunalHabNivel() {
        final Nivel_ComparativoComunalHabilidadView comparativoComunalHabilidadNivel = (Nivel_ComparativoComunalHabilidadView) show(
                "/comunal/nivel/fxml/Nivel_ComparativoComunalHabilidad.fxml");
        if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
            final R_Prueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
            if (prueba != null) {
                controller.findById(R_Prueba.class, prueba.getId(), comparativoComunalHabilidadNivel);
                controller.findAll(R_EvaluacionEjetematico.class, comparativoComunalHabilidadNivel);
                controller.findAll(R_TipoAlumno.class, comparativoComunalHabilidadNivel);
                controller.findAll(R_TipoColegio.class, comparativoComunalHabilidadNivel);
            }
        }
    }
    private void handlerComparativoComunalNivel() {
        final Nivel_ComparativoComunalEjeView comparativoComunalNivel = (Nivel_ComparativoComunalEjeView) show(
                "/comunal/nivel/fxml/Nivel_ComparativoComunalEje.fxml");
        if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
            final R_Prueba pPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
            if (pPrueba != null) {
                controller.findById(R_Prueba.class, pPrueba.getId(), comparativoComunalNivel);
                controller.findAll(R_EvaluacionEjetematico.class, comparativoComunalNivel);
                controller.findAll(R_TipoAlumno.class, comparativoComunalNivel);
                controller.findAll(R_TipoColegio.class, comparativoComunalNivel);
            }
        }
    }
    private void handlerCompColegioEjeHabilXCurso() {
        final ComparativoColegioEjeHabilidadxCursoView resColegioHabEjeCurso = (ComparativoColegioEjeHabilidadxCursoView) show(
                "/colegio/fxml/ComparativoColegioEjeHabilidadxCurso.fxml");
        show(resColegioHabEjeCurso);
        controller.findAll(R_Colegio.class, resColegioHabEjeCurso);
        controller.findAll(R_Asignatura.class, resColegioHabEjeCurso);
        controller.findAll(R_EvaluacionEjetematico.class, resColegioHabEjeCurso);
        controller.findAll(R_TipoAlumno.class, resColegioHabEjeCurso);
    }
    private void handlerCompEjesHabXNivel() {
        if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
            final Nivel_ComparativoColegioEjeHabilidadxCursoView comparativoComunalHabilidad = (Nivel_ComparativoColegioEjeHabilidadxCursoView) show(
                    "/colegio/nivel/fxml/Nivel_ComparativoColegioEjeHabilidadxCurso.fxml");
            final R_Prueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
            if (prueba != null) {
                controller.findById(R_Prueba.class, prueba.getId(), comparativoComunalHabilidad);
                controller.findAll(R_Colegio.class, comparativoComunalHabilidad);
                controller.findAll(R_Asignatura.class, comparativoComunalHabilidad);
                controller.findAll(R_EvaluacionEjetematico.class, comparativoComunalHabilidad);
                controller.findAll(R_TipoAlumno.class, comparativoComunalHabilidad);
                controller.findAll(R_TipoColegio.class, comparativoComunalHabilidad);
            }
        }
    }
    private void handlerComunalEje() {
        comunalEje = (ComunalCursoView) show("/comunal/fxml/ComunalEje.fxml");
        final ObservableList<OTPrueba> otPruebas = tblListadoPruebas.getSelectionModel().getSelectedItems();
        if (otPruebas != null) {
            final Long[] pruebas = new Long[otPruebas.size()];
            int n = 0;
            for (final OTPrueba ot : otPruebas) {
                pruebas[n++] = ot.getPrueba().getId();
            }
            controller.findByAllId(R_Prueba.class, pruebas, comunalEje);
            controller.findAll(R_EvaluacionEjetematico.class, comunalEje);
            controller.findAll(R_TipoAlumno.class, comunalEje);
            controller.findAll(R_TipoColegio.class, comunalEje);
        }
    }
    private void handlerDefinirPrueba() {
        if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
            final R_Prueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
            definePrueba = (DefinePruebaViewController) show("/cl/eos/view/DefinePruebaView.fxml");
            if (prueba != null) {
                controller.findById(R_Prueba.class, prueba.getId(), definePrueba);
                final Map<String, Object> parameters = new HashMap<String, Object>();
                parameters.put("idAsignatura", prueba.getAsignatura().getId());
                controller.find("SEjeTematico.findByAsigntura", parameters, definePrueba);
                controller.findAll(R_Habilidad.class, definePrueba);
                final Map<String, Object> param = new HashMap<String, Object>();
                param.put("tipoCursoId", prueba.getCurso().getId());
                controller.find("Objetivo.findByTipoCurso", param, definePrueba);
            }
        }
    }
    private void handlerEjesEvaluacion() {
        final ComparativoColegioEjeEvaluacionView resumenEjeEvaluacion = (ComparativoColegioEjeEvaluacionView) show(
                "/colegio/fxml/ComparativoColegioEjeEvaluacion.fxml");
        show(resumenEjeEvaluacion);
        controller.findAll(R_Colegio.class, resumenEjeEvaluacion);
        controller.findAll(R_Asignatura.class, resumenEjeEvaluacion);
        controller.findAll(R_TipoAlumno.class, resumenEjeEvaluacion);
    }
    private void handlerEvaluar() {
        evaluarPruebaView = (EvaluarPruebaView) show("/cl/eos/view/EvaluarPrueba.fxml");
        if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
            final R_Prueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
            if (prueba != null) {
                controller.findById(R_Prueba.class, prueba.getId(), evaluarPruebaView);
                controller.findAll(R_Colegio.class, evaluarPruebaView);
                controller.findAll(R_Profesor.class, evaluarPruebaView);
            }
        }
    }
    private void handlerHabilidadEvaluacion() {
        final ComparativoColegioHabilidadesView resumenHabilidades = (ComparativoColegioHabilidadesView) show(
                "/colegio/fxml/ComparativoColegioHabilidades.fxml");
        show(resumenHabilidades);
        controller.findAll(R_Colegio.class, resumenHabilidades);
        controller.findAll(R_Asignatura.class, resumenHabilidades);
        controller.findAll(R_TipoAlumno.class, resumenHabilidades);
    }
    private void handlerHabilidadEvaluacionXAlumno() {
        final ResumenColegioXAlumnoEjeHabilidadView resHabEjeAlumno = (ResumenColegioXAlumnoEjeHabilidadView) show(
                "/colegio/fxml/ResumenColegioXAlumnoEjeHabilidad.fxml");
        show(resHabEjeAlumno);
        controller.findAll(R_Colegio.class, resHabEjeAlumno);
        controller.findAll(R_Asignatura.class, resHabEjeAlumno);
        controller.findAll(R_TipoAlumno.class, resHabEjeAlumno);
    }
    private void handlerHabilidadEvaluacionXAlumnoXNivel() {
        final Nivel_ComparativoColegioEjeEvaluacionView resHabEjeAlumno = (Nivel_ComparativoColegioEjeEvaluacionView) show(
                "/colegio/nivel/fxml/Nivel_ComparativoColegioEjeEvaluacion.fxml");
        show(resHabEjeAlumno);
        controller.findAll(R_Colegio.class, resHabEjeAlumno);
        controller.findAll(R_Asignatura.class, resHabEjeAlumno);
        controller.findAll(R_TipoAlumno.class, resHabEjeAlumno);
    }
    private void handlerHabilidadEvaluacionXNivel() {
        final Nivel_ComparativoColegioHabilidadesView resumenHabilidades = (Nivel_ComparativoColegioHabilidadesView) show(
                "/colegio/nivel/fxml/Nivel_ComparativoColegioHabilidades.fxml");
        show(resumenHabilidades);
        controller.findAll(R_Colegio.class, resumenHabilidades);
        controller.findAll(R_Asignatura.class, resumenHabilidades);
        controller.findAll(R_TipoAlumno.class, resumenHabilidades);
    }
    private void handlerImrpimirPrueba() {
        imprimirPrueba = (ImprimirPruebaView) show("/cl/eos/view/ImprimirPrueba.fxml");
        if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
            final R_Prueba pPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
            if (pPrueba != null) {
                controller.findById(R_Prueba.class, pPrueba.getId(), imprimirPrueba);
                controller.findAll(R_Profesor.class, imprimirPrueba);
                controller.findAll(R_Colegio.class, imprimirPrueba);
            }
        }
    }
    private void handlerInforme() {
        final InformeView informe = (InformeView) showOver("/informe/informeView.fxml");
        show(informe);
        controller.findAll(R_Colegio.class, informe);
        controller.findAll(R_Asignatura.class, informe);
        controller.findAll(R_TipoAlumno.class, informe);
    }
    private void handlerListaEvaluaciones() {
        evaluacionPrueba = (EvaluacionPruebaView) show("/cl/eos/view/R_EvaluacionPrueba.fxml");
        if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
            final R_Prueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
            final Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("idPrueba", prueba.getId());
            controller.find("EvaluacionPrueba.findByPrueba", parameters, evaluacionPrueba);
        }
    }
    private void handlerNuevaPrueba() {
        removeAllStyles();
        limpiarCampos();
        Platform.runLater(() -> txtName.requestFocus());
    }
    private void handlerResumenColegios() {
        resumenColegio = (ResumenColegioView) show("/colegio/fxml/ResumenColegio.fxml");
        show(resumenColegio);
        controller.findAll(R_Colegio.class, resumenColegio);
        controller.findAll(R_Asignatura.class, resumenColegio);
        controller.findAll(R_RangoEvaluacion.class, resumenColegio);
        controller.findAll(R_EvaluacionEjetematico.class, resumenColegio);
        controller.findAll(R_TipoAlumno.class, resumenColegio);
    }
    private void handlerResumenColegiosXNivel() {
        final Nivel_ResumenColegioView resumenColegio = (Nivel_ResumenColegioView) show("/colegio/nivel/fxml/Nivel_ResumenColegio.fxml");
        show(resumenColegio);
        controller.findAll(R_Colegio.class, resumenColegio);
        controller.findAll(R_Asignatura.class, resumenColegio);
        controller.findAll(R_RangoEvaluacion.class, resumenColegio);
        controller.findAll(R_EvaluacionEjetematico.class, resumenColegio);
        controller.findAll(R_TipoAlumno.class, resumenColegio);
    }
    @FXML
    public void initialize() {
        lblError.setText(" ");
        tblListadoPruebas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        fechaCol.setCellValueFactory(new PropertyValueFactory<OTPrueba, LocalDate>("fechaLocal"));
        nameCol.setCellValueFactory(new PropertyValueFactory<OTPrueba, String>("name"));
        asignaturaCol.setCellValueFactory(new PropertyValueFactory<OTPrueba, String>("asignatura"));
        profesorCol.setCellValueFactory(new PropertyValueFactory<OTPrueba, String>("profesor"));
        cursoCol.setCellValueFactory(new PropertyValueFactory<OTPrueba, String>("colegio"));
        nroPreguntasCol.setCellValueFactory(new PropertyValueFactory<OTPrueba, Integer>("nroPreguntas"));
        estadoCol.setCellValueFactory(new PropertyValueFactory<OTPrueba, Estado>("estado"));
        estadoCol.setCellFactory(new PruebaCellFactory());
        bigDecimalForma.setMinValue(new BigDecimal(1));
        bigDecimalForma.setMaxValue(new BigDecimal(5));
        bigDecimalForma.setStepwidth(new BigDecimal(1));
        bigDecimalForma.setNumber(new BigDecimal(1));
        bigDecimaNroAlternativas.setMinValue(new BigDecimal(3));
        bigDecimaNroAlternativas.setMaxValue(new BigDecimal(5));
        bigDecimaNroAlternativas.setStepwidth(new BigDecimal(1));
        bigDecimaNroAlternativas.setNumber(new BigDecimal(5));
        bigDecimalNroPreguntas.setMinValue(new BigDecimal(5));
        bigDecimalNroPreguntas.setMaxValue(new BigDecimal(90));
        bigDecimalNroPreguntas.setStepwidth(new BigDecimal(5));
        bigDecimalNroPreguntas.setNumber(new BigDecimal(30));
        bigDecimalPuntajePregunta.setMinValue(new BigDecimal(1));
        bigDecimalPuntajePregunta.setMaxValue(new BigDecimal(3));
        bigDecimalPuntajePregunta.setStepwidth(new BigDecimal(1));
        bigDecimalPuntajePregunta.setNumber(new BigDecimal(1));
        bigDecimalExigencia.setMinValue(new BigDecimal(40));
        bigDecimalExigencia.setMaxValue(new BigDecimal(80));
        bigDecimalExigencia.setStepwidth(new BigDecimal(10));
        bigDecimalExigencia.setNumber(new BigDecimal(60));
        dpFecha.setValue(LocalDate.now());
        mnuGrabar.setOnAction(this);
        mnuModificar.setOnAction(this);
        mnuPopupModificar.setOnAction(this);
        mnuEliminar.setOnAction(this);
        mnuPopupEliminar.setOnAction(this);
        mnuEvaluarPrueba.setOnAction(this);
        mnuAnularPregunta.setOnAction(this);
        mnuDefinirPrueba.setOnAction(this);
        mnuListaEvaluaciones.setOnAction(this);
        mnuComparativoComunal.setOnAction(this);
        mnuComparativoComunalNivel.setOnAction(this);
        mnuComparativoComunalHab.setOnAction(this);
        mnuComparativoComunalHabNivel.setOnAction(this);
        mnuComunalEje.setOnAction(this);
        mnuColegio.setOnAction(this);
        mnuImprimirPrueba.setOnAction(this);
        mnuNueva.setOnAction(this);
        mnuInforme.setOnAction(this);
        mnuComparativoColegioEjeHabil.setOnAction(this);
        mnuEjesEvaluacion.setOnAction(this);
        mnuHabilidadEvaluacion.setOnAction(this);
        mnuHabilidadEvaluacionXNivel.setOnAction(this);
        mnuHabilidadEvaluacionXAlumno.setOnAction(this);
        mnuCompColegioEjeHabilXCurso.setOnAction(this);
        mnuCompEjesHabXNivel.setOnAction(this);
        mnuHabilidadEvaluacionXAlumnoXNivel.setOnAction(this);
        mnuColegioXNivel.setOnAction(this);
        mnuComparativoColegioEjeHabilXNivel.setOnAction(this);
        accionClicTabla();
    }
    private void limpiarCampos() {
        cmbTipoPrueba.getSelectionModel().clearSelection();
        cmbProfesor.getSelectionModel().clearSelection();
        cmbCurso.getSelectionModel().clearSelection();
        cmbAsignatura.getSelectionModel().clearSelection();
        cmbNivelEvaluacion.getSelectionModel().clearSelection();
        bigDecimalForma.setNumber(new BigDecimal(1));
        bigDecimaNroAlternativas.setNumber(new BigDecimal(3));
        bigDecimalNroPreguntas.setNumber(new BigDecimal(5));
        bigDecimalPuntajePregunta.setNumber(new BigDecimal(1));
        dpFecha.setValue(LocalDate.now());
        txtName.setText(null);
    }
    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            final Object entity = list.get(0);
            if (entity instanceof R_Prueba) {
                final ObservableList<OTPrueba> pruebas = FXCollections.observableArrayList();
                for (final Object lEntity : list) {
                    pruebas.add(new OTPrueba((R_Prueba) lEntity));
                }
                tblListadoPruebas.setItems(pruebas);
            }
            if (entity instanceof R_TipoPrueba) {
                final ObservableList<R_TipoPrueba> tipoPruebas = FXCollections.observableArrayList();
                for (final Object lEntity : list) {
                    tipoPruebas.add((R_TipoPrueba) lEntity);
                }
                cmbTipoPrueba.setItems(tipoPruebas);
            }
            if (entity instanceof R_Profesor) {
                final ObservableList<R_Profesor> profesores = FXCollections.observableArrayList();
                for (final Object lEntity : list) {
                    profesores.add((R_Profesor) lEntity);
                }
                cmbProfesor.setItems(profesores);
            }
            if (entity instanceof R_TipoCurso) {
                final ObservableList<R_TipoCurso> cursos = FXCollections.observableArrayList();
                for (final Object lEntity : list) {
                    cursos.add((R_TipoCurso) lEntity);
                }
                cmbCurso.setItems(cursos);
            }
            if (entity instanceof R_Asignatura) {
                final ObservableList<R_Asignatura> asignaturas = FXCollections.observableArrayList();
                for (final Object lEntity : list) {
                    asignaturas.add((R_Asignatura) lEntity);
                }
                cmbAsignatura.setItems(asignaturas);
            }
            if (entity instanceof R_NivelEvaluacion) {
                final ObservableList<R_NivelEvaluacion> nivelEvaluacion = FXCollections.observableArrayList();
                for (final Object lEntity : list) {
                    nivelEvaluacion.add((R_NivelEvaluacion) lEntity);
                }
                cmbNivelEvaluacion.setItems(nivelEvaluacion);
            }
        }
    }
    @Override
    public void onDeleted(IEntity entity) {
        if (entity instanceof R_Prueba) {
            final OTPrueba pEliminar = new OTPrueba((R_Prueba) entity);
            tblListadoPruebas.getItems().remove(pEliminar);
        }
    }
    @Override
    public void onSaved(IEntity otObject) {
        R_Prueba prueba = null;
        if (otObject instanceof R_Prueba) {
            prueba = (R_Prueba) otObject;
        } else if (otObject instanceof R_RespuestasEsperadasPrueba) {
            final R_RespuestasEsperadasPrueba r = (R_RespuestasEsperadasPrueba) otObject;
            prueba = r.getPrueba();
        } else if (otObject instanceof R_EvaluacionPrueba) {
            final R_EvaluacionPrueba e = (R_EvaluacionPrueba) otObject;
            prueba = e.getPrueba();
        }
        if (prueba != null) {
            prueba = (R_Prueba) findByIdSynchro(R_Prueba.class, prueba.getId());
            final OTPrueba ot = new OTPrueba(prueba);
            final int indice = tblListadoPruebas.getItems().lastIndexOf(ot);
            if (indice != -1) {
                tblListadoPruebas.getItems().set(indice, ot);
            } else {
                tblListadoPruebas.getItems().add(ot);
            }
            limpiarCampos();
            prueba = null;
        }
    }
    private void removeAllStyles() {
        removeAllStyle(lblError);
        removeAllStyle(cmbTipoPrueba);
        removeAllStyle(cmbProfesor);
        removeAllStyle(cmbCurso);
        removeAllStyle(cmbAsignatura);
        removeAllStyle(cmbNivelEvaluacion);
        removeAllStyle(txtName);
        removeAllStyle(bigDecimalForma);
        removeAllStyle(bigDecimaNroAlternativas);
        removeAllStyle(bigDecimalNroPreguntas);
        removeAllStyle(bigDecimalPuntajePregunta);
        removeAllStyle(dpFecha);
        removeAllStyle(bigDecimalExigencia);
    }
    @Override
    public boolean validate() {
        boolean valid = true;
        if (cmbTipoPrueba.getValue() == null) {
            valid = false;
            cmbTipoPrueba.getStyleClass().add("bad");
        }
        if (cmbProfesor.getValue() == null) {
            valid = false;
            cmbProfesor.getStyleClass().add("bad");
        }
        if (cmbCurso.getValue() == null) {
            valid = false;
            cmbCurso.getStyleClass().add("bad");
        }
        if (cmbAsignatura.getValue() == null) {
            valid = false;
            cmbAsignatura.getStyleClass().add("bad");
        }
        if (cmbNivelEvaluacion.getValue() == null) {
            valid = false;
            cmbNivelEvaluacion.getStyleClass().add("bad");
        }
        if (txtName.getText() == null || txtName.getText().isEmpty()) {
            valid = false;
            txtName.getStyleClass().add("bad");
        }
        if (bigDecimalForma.getNumber() == null) {
            valid = false;
            bigDecimalForma.getStyleClass().add("bad");
        }
        if (bigDecimaNroAlternativas.getNumber() == null) {
            valid = false;
            bigDecimaNroAlternativas.getStyleClass().add("bad");
        }
        if (bigDecimalNroPreguntas.getNumber() == null) {
            valid = false;
            bigDecimalNroPreguntas.getStyleClass().add("bad");
        }
        if (bigDecimalPuntajePregunta.getNumber() == null) {
            valid = false;
            bigDecimalPuntajePregunta.getStyleClass().add("bad");
        }
        if (bigDecimalExigencia.getNumber() == null) {
            valid = false;
            bigDecimalExigencia.getStyleClass().add("bad");
        }
        if (dpFecha.getValue() == null) {
            valid = false;
            dpFecha.getStyleClass().add("bad");
        }
        if (valid) {
            lblError.setText(" ");
        } else {
            lblError.getStyleClass().add("bad");
            lblError.setText("Corregir campos destacados en color rojo");
        }
        return valid;
    }
}
