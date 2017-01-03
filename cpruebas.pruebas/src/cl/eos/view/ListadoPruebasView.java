package cl.eos.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.SAsignatura;
import cl.eos.persistence.models.SColegio;
import cl.eos.persistence.models.SCurso;
import cl.eos.persistence.models.SEvaluacionEjeTematico;
import cl.eos.persistence.models.SHabilidad;
import cl.eos.persistence.models.SNivelEvaluacion;
import cl.eos.persistence.models.SProfesor;
import cl.eos.persistence.models.SPrueba;
import cl.eos.persistence.models.SPrueba.Estado;
import cl.eos.persistence.models.SRangoEvaluacion;
import cl.eos.persistence.models.STipoAlumno;
import cl.eos.persistence.models.STipoColegio;
import cl.eos.persistence.models.STipoCurso;
import cl.eos.persistence.models.STipoPrueba;
import cl.eos.view.editablecells.PruebaCellFactory;
import cl.eos.view.ots.OTPrueba;
import colegio.ComparativoColegioEjeEvaluacionView;
import colegio.ComparativoColegioEjeHabilidadView;
import colegio.ComparativoColegioEjeHabilidadxCursoView;
import colegio.ComparativoColegioHabilidadesView;
import colegio.PorObjetivosColegioView;
import colegio.ResumenColegioView;
import colegio.ResumenColegioXAlumnoEjeHabilidadView;
import colegio.nivel.Nivel_ComparativoColegioEjeEvaluacionView;
import colegio.nivel.Nivel_ComparativoColegioEjeHabilidadView;
import colegio.nivel.Nivel_ComparativoColegioEjeHabilidadxCursoView;
import colegio.nivel.Nivel_ComparativoColegioHabilidadesView;
import colegio.nivel.Nivel_PorObjetivosColegioView;
import colegio.nivel.Nivel_ResumenColegioView;
import comunal.ComparativoComunalEjeView;
import comunal.ComparativoComunalHabilidadView;
import comunal.ComunalCursoView;
import comunal.nivel.Nivel_ComparativoComunalEjeView;
import comunal.nivel.Nivel_ComparativoComunalHabilidadView;
import informe.InformeView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;

public class ListadoPruebasView extends AFormView implements EventHandler<ActionEvent> {

    @FXML
    private StackPane pnlRoot;
    @FXML
    private TableView<OTPrueba> tblListadoPruebas;
    @FXML
    private TableColumn<OTPrueba, LocalDate> fechaCol;
    @FXML
    private TableColumn<OTPrueba, String> cursoCol;
    @FXML
    private TableColumn<OTPrueba, String> nameCol;
    @FXML
    private TableColumn<OTPrueba, String> asignaturaCol;
    @FXML
    private TableColumn<OTPrueba, String> profesorCol;
    @FXML
    private TableColumn<OTPrueba, Integer> nroPreguntasCol;
    @FXML
    private TableColumn<OTPrueba, Integer> formasCol;
    @FXML
    private TableColumn<OTPrueba, Integer> alternativasCol;
    @FXML
    private TableColumn<OTPrueba, Estado> estadoCol;
    @FXML
    private MenuItem mnuCrear;
    @FXML
    private MenuItem mnuModificar;
    @FXML
    private MenuItem mnuEliminar;
    @FXML
    private MenuItem mnuPopupCrear;
    @FXML
    private MenuItem mnuPopupModificar;
    @FXML
    private MenuItem mnuPopupEliminar;
    @FXML
    private MenuItem mnuEvaluarPrueba;
    @FXML
    private MenuItem mnuAnularPregunta;
    @FXML
    private MenuItem mnuListaEvaluaciones;
    @FXML
    private MenuItem mnuComparativoComunal;
    @FXML
    private MenuItem mnuComparativoComunalNivel;
    @FXML
    private MenuItem mnuComparativoComunalHab;
    @FXML
    private MenuItem mnuComparativoComunalHabNivel;
    @FXML
    private MenuItem mnuComparativoColegioEjeHabil;
    @FXML
    private MenuItem mnuCompColegioEjeHabilXCurso;
    @FXML
    private MenuItem mnuComparativoColegioEjeHabilXNivel;
    @FXML
    private MenuItem mnuInforme;
    @FXML
    private MenuItem mnuColegio;
    @FXML
    private MenuItem mnuComunalEje;
    @FXML
    private MenuItem mnuEjesEvaluacion;
    @FXML
    private MenuItem mnuHabilidadEvaluacion;

    @FXML
    private MenuItem mnuHabilidadEvaluacionXNivel;

    @FXML
    private MenuItem mnuHabilidadEvaluacionXAlumno;
    @FXML
    private MenuItem mnuNueva;
    @FXML
    private MenuItem mnuImprimirPrueba;
    @FXML
    private MenuItem mnuCompEjesHabXNivel;
    @FXML
    private MenuItem mnuHabilidadEvaluacionXAlumnoXNivel;
    @FXML
    private MenuItem mnuColegioXNivel;
    @FXML
    private MenuItem mnuXObjetivos;
    @FXML
    private MenuItem mnuXNivelObjetivos;
    @FXML
    private Pagination pagination;

    private EvaluacionPruebaView evaluacionPrueba;
    private AnularPreguntasViewController anularPregunta;
    private SPrueba prueba;
    private ComparativoComunalEjeView comparativoComunal;
    private ComparativoComunalHabilidadView comparativoComunalHabilidad;
    private ComunalCursoView comunalEje;

    private EvaluarPruebaView evaluarPruebaView;
    private ImprimirPruebaView imprimirPrueba;
    private ResumenColegioView resumenColegio;
    private int rowsPerPage = 25;

    public ListadoPruebasView() {
        setTitle("Pruebas");
    }

    private void accionClicTabla() {
        tblListadoPruebas.setOnMouseClicked(event -> {
            final ObservableList<OTPrueba> itemsSelec = tblListadoPruebas.getSelectionModel().getSelectedItems();

            if (itemsSelec.size() > 1) {

                mnuCrear.setDisable(false);
                mnuModificar.setDisable(true);
                mnuEliminar.setDisable(false);
                mnuComunalEje.setDisable(false);

                mnuPopupCrear.setDisable(false);
                mnuPopupModificar.setDisable(true);
                mnuPopupEliminar.setDisable(false);

                mnuImprimirPrueba.setDisable(true);
                mnuListaEvaluaciones.setDisable(true);
                mnuEvaluarPrueba.setDisable(true);
                mnuAnularPregunta.setDisable(true);
                mnuComparativoComunal.setDisable(true);
                mnuComparativoComunalHab.setDisable(true);

            } else if (itemsSelec.size() == 1) {

                final OTPrueba prueba = itemsSelec.get(0);
                mnuCrear.setDisable(false);
                mnuModificar.setDisable(false);
                mnuEliminar.setDisable(false);
                mnuPopupCrear.setDisable(false);
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
                    mnuListaEvaluaciones.setDisable(!estadoEvaluada);
                    mnuComunalEje.setDisable(!estadoEvaluada);
                    mnuComparativoComunal.setDisable(!estadoEvaluada);
                    mnuComparativoComunalHab.setDisable(!estadoEvaluada);
                } else if (estadoCreada) {
                    mnuEvaluarPrueba.setDisable(estadoCreada);
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
        if (source == mnuCrear || source == mnuPopupCrear) {
            handleCrear();
        } else if (source == mnuModificar || source == mnuPopupModificar) {
            handleModificar();
        } else if (source == mnuEliminar || source == mnuPopupEliminar) {
            handleEliminar();
        } else if (source == mnuEvaluarPrueba) {
            handlerEvaluar();
        } else if (source == mnuAnularPregunta) {
            handlerAnularPregunta();
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
        } else if (source == mnuXObjetivos) {
            handlerXObjetivos();
        } else if (source == mnuXNivelObjetivos) {
            handlerXNivelObjetivos();
        } 

    }

    private void handlerXNivelObjetivos() {
        final Nivel_PorObjetivosColegioView view = (Nivel_PorObjetivosColegioView) show(
                "/colegio/nivel/fxml/Nivel_PorObjetivosColegio.fxml");
        controller.findAll(SAsignatura.class, view);
        controller.findAll(STipoAlumno.class, view);
        controller.findAll(SColegio.class, view);

    }

    private void handlerXObjetivos() {
        final PorObjetivosColegioView view = (PorObjetivosColegioView) show("/colegio/fxml/PorObjetivosColegio.fxml");
        controller.findAll(SAsignatura.class, view);
        controller.findAll(STipoAlumno.class, view);
        controller.findAll(SColegio.class, view);
    }

    private void handleCrear() {
        final DefinirPrueba definicion = (DefinirPrueba) show("/cl/eos/view/definir_prueba.fxml");
        controller.findAll(STipoCurso.class, definicion);
        controller.findAll(SProfesor.class, definicion);
        controller.findAll(SAsignatura.class, definicion);
        controller.findAll(STipoPrueba.class, definicion);
        controller.findAll(SNivelEvaluacion.class, definicion);
        controller.findAll(SCurso.class, definicion);
        controller.findAll(SHabilidad.class, definicion);
    }

    private void handleEliminar() {

        final ObservableList<OTPrueba> otSeleccionados = tblListadoPruebas.getSelectionModel().getSelectedItems();
        if (otSeleccionados != null && !otSeleccionados.isEmpty()) {
            final List<SPrueba> pruebas = new ArrayList<SPrueba>(otSeleccionados.size());
            for (final OTPrueba ot : otSeleccionados) {
                pruebas.add(ot.getPrueba());
            }
            delete(pruebas);
            tblListadoPruebas.getSelectionModel().clearSelection();
        }
    }

    private void handleModificar() {
        if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
            prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();

            if (prueba != null) {
                if (!prueba.getEstado().equals(Estado.EVALUADA)) {
                    // TODO debo llamar la nueva creación de prueba con datos.
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
            final SPrueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();

            anularPregunta = (AnularPreguntasViewController) show("/cl/eos/view/AnulaPreguntasView.fxml");

            if (prueba != null) {
                controller.findById(SPrueba.class, prueba.getId(), anularPregunta);
            }
        }
    }

    private void handlerComparativoColegioEjeHabilidad() {
        final ComparativoColegioEjeHabilidadView resumenColegioEjeHabiliadad = (ComparativoColegioEjeHabilidadView) show(
                "/colegio/fxml/ComparativoColegioEjeHabilidad.fxml");
        show(resumenColegioEjeHabiliadad);
        controller.findAll(SColegio.class, resumenColegioEjeHabiliadad);
        controller.findAll(SAsignatura.class, resumenColegioEjeHabiliadad);
        controller.findAll(SEvaluacionEjeTematico.class, resumenColegioEjeHabiliadad);
        controller.findAll(STipoAlumno.class, resumenColegioEjeHabiliadad);
    }

    private void handlerComparativoColegioEjeHabilidadXNivel() {
        final Nivel_ComparativoColegioEjeHabilidadView resumenColegioEjeHabiliadad = (Nivel_ComparativoColegioEjeHabilidadView) show(
                "/colegio/nivel/fxml/Nivel_ComparativoColegioEjeHabilidad.fxml");
        show(resumenColegioEjeHabiliadad);
        controller.findAll(SColegio.class, resumenColegioEjeHabiliadad);
        controller.findAll(SAsignatura.class, resumenColegioEjeHabiliadad);
        controller.findAll(SEvaluacionEjeTematico.class, resumenColegioEjeHabiliadad);
        controller.findAll(STipoAlumno.class, resumenColegioEjeHabiliadad);
    }

    private void handlerComparativoComunal() {
        comparativoComunal = (ComparativoComunalEjeView) show("/comunal/fxml/ComparativoComunalEje.fxml");
        if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
            final SPrueba pPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
            if (pPrueba != null) {
                controller.findById(SPrueba.class, pPrueba.getId(), comparativoComunal);
                controller.findAll(SEvaluacionEjeTematico.class, comparativoComunal);
                controller.findAll(STipoAlumno.class, comparativoComunal);
                controller.findAll(STipoColegio.class, comparativoComunal);
            }
        }
    }

    private void handlerComparativoComunalHab() {
        comparativoComunalHabilidad = (ComparativoComunalHabilidadView) show(
                "/comunal/fxml/ComparativoComunalHabilidad.fxml");
        if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
            final SPrueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
            if (prueba != null) {
                controller.findById(SPrueba.class, prueba.getId(), comparativoComunalHabilidad);
                controller.findAll(SEvaluacionEjeTematico.class, comparativoComunalHabilidad);
                controller.findAll(STipoAlumno.class, comparativoComunalHabilidad);
                controller.findAll(STipoColegio.class, comparativoComunalHabilidad);
            }
        }
    }

    private void handlerComparativoComunalHabNivel() {
        final Nivel_ComparativoComunalHabilidadView comparativoComunalHabilidadNivel = (Nivel_ComparativoComunalHabilidadView) show(
                "/comunal/nivel/fxml/Nivel_ComparativoComunalHabilidad.fxml");
        if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
            final SPrueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
            if (prueba != null) {
                controller.findById(SPrueba.class, prueba.getId(), comparativoComunalHabilidadNivel);
                controller.findAll(SEvaluacionEjeTematico.class, comparativoComunalHabilidadNivel);
                controller.findAll(STipoAlumno.class, comparativoComunalHabilidadNivel);
                controller.findAll(STipoColegio.class, comparativoComunalHabilidadNivel);
            }
        }
    }

    private void handlerComparativoComunalNivel() {
        final Nivel_ComparativoComunalEjeView comparativoComunalNivel = (Nivel_ComparativoComunalEjeView) show(
                "/comunal/nivel/fxml/Nivel_ComparativoComunalEje.fxml");
        if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
            final SPrueba pPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
            if (pPrueba != null) {
                controller.findById(SPrueba.class, pPrueba.getId(), comparativoComunalNivel);
                controller.findAll(SEvaluacionEjeTematico.class, comparativoComunalNivel);
                controller.findAll(STipoAlumno.class, comparativoComunalNivel);
                controller.findAll(STipoColegio.class, comparativoComunalNivel);
            }
        }
    }

    private void handlerCompColegioEjeHabilXCurso() {
        final ComparativoColegioEjeHabilidadxCursoView resColegioHabEjeCurso = (ComparativoColegioEjeHabilidadxCursoView) show(
                "/colegio/fxml/ComparativoColegioEjeHabilidadxCurso.fxml");
        show(resColegioHabEjeCurso);
        controller.findAll(SColegio.class, resColegioHabEjeCurso);
        controller.findAll(SAsignatura.class, resColegioHabEjeCurso);
        controller.findAll(SEvaluacionEjeTematico.class, resColegioHabEjeCurso);
        controller.findAll(STipoAlumno.class, resColegioHabEjeCurso);
    }

    private void handlerCompEjesHabXNivel() {
        if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
            final Nivel_ComparativoColegioEjeHabilidadxCursoView comparativoComunalHabilidad = (Nivel_ComparativoColegioEjeHabilidadxCursoView) show(
                    "/colegio/nivel/fxml/Nivel_ComparativoColegioEjeHabilidadxCurso.fxml");

            final SPrueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
            if (prueba != null) {
                controller.findById(SPrueba.class, prueba.getId(), comparativoComunalHabilidad);
                controller.findAll(SColegio.class, comparativoComunalHabilidad);
                controller.findAll(SAsignatura.class, comparativoComunalHabilidad);
                controller.findAll(SEvaluacionEjeTematico.class, comparativoComunalHabilidad);
                controller.findAll(STipoAlumno.class, comparativoComunalHabilidad);
                controller.findAll(STipoColegio.class, comparativoComunalHabilidad);
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
            controller.findByAllId(SPrueba.class, pruebas, comunalEje);
            controller.findAll(SEvaluacionEjeTematico.class, comunalEje);
            controller.findAll(STipoAlumno.class, comunalEje);
            controller.findAll(STipoColegio.class, comunalEje);
        }
    }

    private void handlerEjesEvaluacion() {
        final ComparativoColegioEjeEvaluacionView resumenEjeEvaluacion = (ComparativoColegioEjeEvaluacionView) show(
                "/colegio/fxml/ComparativoColegioEjeEvaluacion.fxml");
        show(resumenEjeEvaluacion);
        controller.findAll(SColegio.class, resumenEjeEvaluacion);
        controller.findAll(SAsignatura.class, resumenEjeEvaluacion);
        controller.findAll(STipoAlumno.class, resumenEjeEvaluacion);
    }

    private void handlerEvaluar() {
        evaluarPruebaView = (EvaluarPruebaView) show("/cl/eos/view/EvaluarPrueba.fxml");
        if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
            final SPrueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
            if (prueba != null) {
                controller.findById(SPrueba.class, prueba.getId(), evaluarPruebaView);
                controller.findAll(SColegio.class, evaluarPruebaView);
                controller.findAll(SProfesor.class, evaluarPruebaView);
            }
        }
    }

    private void handlerHabilidadEvaluacion() {
        final ComparativoColegioHabilidadesView resumenHabilidades = (ComparativoColegioHabilidadesView) show(
                "/colegio/fxml/ComparativoColegioHabilidades.fxml");
        show(resumenHabilidades);
        controller.findAll(SColegio.class, resumenHabilidades);
        controller.findAll(SAsignatura.class, resumenHabilidades);
        controller.findAll(STipoAlumno.class, resumenHabilidades);

    }

    private void handlerHabilidadEvaluacionXAlumno() {
        final ResumenColegioXAlumnoEjeHabilidadView resHabEjeAlumno = (ResumenColegioXAlumnoEjeHabilidadView) show(
                "/colegio/fxml/ResumenColegioXAlumnoEjeHabilidad.fxml");
        show(resHabEjeAlumno);
        controller.findAll(SColegio.class, resHabEjeAlumno);
        controller.findAll(SAsignatura.class, resHabEjeAlumno);
        controller.findAll(STipoAlumno.class, resHabEjeAlumno);

    }

    private void handlerHabilidadEvaluacionXAlumnoXNivel() {
        final Nivel_ComparativoColegioEjeEvaluacionView resHabEjeAlumno = (Nivel_ComparativoColegioEjeEvaluacionView) show(
                "/colegio/nivel/fxml/Nivel_ComparativoColegioEjeEvaluacion.fxml");
        show(resHabEjeAlumno);
        controller.findAll(SColegio.class, resHabEjeAlumno);
        controller.findAll(SAsignatura.class, resHabEjeAlumno);
        controller.findAll(STipoAlumno.class, resHabEjeAlumno);
    }

    private void handlerHabilidadEvaluacionXNivel() {
        final Nivel_ComparativoColegioHabilidadesView resumenHabilidades = (Nivel_ComparativoColegioHabilidadesView) show(
                "/colegio/nivel/fxml/Nivel_ComparativoColegioHabilidades.fxml");
        show(resumenHabilidades);
        controller.findAll(SColegio.class, resumenHabilidades);
        controller.findAll(SAsignatura.class, resumenHabilidades);
        controller.findAll(STipoAlumno.class, resumenHabilidades);
    }

    private void handlerImrpimirPrueba() {
        imprimirPrueba = (ImprimirPruebaView) show("/cl/eos/view/ImprimirPrueba.fxml");
        if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
            final SPrueba pPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
            if (pPrueba != null) {
                controller.findById(SPrueba.class, pPrueba.getId(), imprimirPrueba);
                final Map<String, Object> parameters = new HashMap<>();
                parameters.put("idAsignatura", pPrueba.getAsignatura().getId());
                controller.find("EjeTematico.findByAsigntura", parameters, imprimirPrueba);
                controller.findAll(SHabilidad.class, imprimirPrueba);
                controller.findAll(SProfesor.class, imprimirPrueba);
                controller.findAll(SColegio.class, imprimirPrueba);
            }
        }
    }

    private void handlerInforme() {
        final InformeView informe = (InformeView) showOver("/informe/informeView.fxml");
        show(informe);
        controller.findAll(SColegio.class, informe);
        controller.findAll(SAsignatura.class, informe);
        controller.findAll(STipoAlumno.class, informe);
    }

    private void handlerListaEvaluaciones() {
        evaluacionPrueba = (EvaluacionPruebaView) show("/cl/eos/view/R_EvaluacionPrueba.fxml");
        if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
            final SPrueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
            final Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("idPrueba", prueba.getId());
            controller.find("EvaluacionPrueba.findByPrueba", parameters, evaluacionPrueba);
        }
    }

    private void handlerResumenColegios() {
        resumenColegio = (ResumenColegioView) show("/colegio/fxml/ResumenColegio.fxml");
        show(resumenColegio);
        controller.findAll(SColegio.class, resumenColegio);
        controller.findAll(SAsignatura.class, resumenColegio);
        controller.findAll(SRangoEvaluacion.class, resumenColegio);
        controller.findAll(SEvaluacionEjeTematico.class, resumenColegio);
        controller.findAll(STipoAlumno.class, resumenColegio);
    }

    private void handlerResumenColegiosXNivel() {
        final Nivel_ResumenColegioView resumenColegio = (Nivel_ResumenColegioView) show(
                "/colegio/nivel/fxml/Nivel_ResumenColegio.fxml");
        show(resumenColegio);
        controller.findAll(SColegio.class, resumenColegio);
        controller.findAll(SAsignatura.class, resumenColegio);
        controller.findAll(SRangoEvaluacion.class, resumenColegio);
        controller.findAll(SEvaluacionEjeTematico.class, resumenColegio);
        controller.findAll(STipoAlumno.class, resumenColegio);
    }

    @FXML
    public void initialize() {
        tblListadoPruebas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        fechaCol.setCellValueFactory(new PropertyValueFactory<OTPrueba, LocalDate>("fechaLocal"));
        nameCol.setCellValueFactory(new PropertyValueFactory<OTPrueba, String>("name"));
        asignaturaCol.setCellValueFactory(new PropertyValueFactory<OTPrueba, String>("asignatura"));
        profesorCol.setCellValueFactory(new PropertyValueFactory<OTPrueba, String>("profesor"));

        cursoCol.setCellValueFactory(new PropertyValueFactory<OTPrueba, String>("curso"));
        nroPreguntasCol.setCellValueFactory(new PropertyValueFactory<OTPrueba, Integer>("nroPreguntas"));

        estadoCol.setCellValueFactory(new PropertyValueFactory<OTPrueba, Estado>("estado"));

        estadoCol.setCellFactory(new PruebaCellFactory());

        mnuCrear.setOnAction(this);
        mnuModificar.setOnAction(this);
        mnuEliminar.setOnAction(this);

        mnuPopupCrear.setOnAction(this);
        mnuPopupModificar.setOnAction(this);
        mnuPopupEliminar.setOnAction(this);
        mnuEvaluarPrueba.setOnAction(this);
        mnuAnularPregunta.setOnAction(this);
        mnuListaEvaluaciones.setOnAction(this);
        mnuComparativoComunal.setOnAction(this);
        mnuComparativoComunalNivel.setOnAction(this);
        mnuComparativoComunalHab.setOnAction(this);
        mnuComparativoComunalHabNivel.setOnAction(this);
        mnuComunalEje.setOnAction(this);
        mnuColegio.setOnAction(this);
        mnuImprimirPrueba.setOnAction(this);
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
        mnuXObjetivos.setOnAction(this);
        mnuXNivelObjetivos.setOnAction(this);
        pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int fromIndex = Math.min(oldValue.intValue(),  newValue.intValue()) * rowsPerPage ;
                int toIndex = Math.max(oldValue.intValue(),  newValue.intValue()) * rowsPerPage ;
                controller.findAll(null);
            }
        });
        
        accionClicTabla();
    }

    @Override
    public void onDataArrived(List<Object> list) {

        if (list != null && !list.isEmpty()) {
            final Object entity = list.get(0);
            if (entity instanceof SPrueba) {
                final ObservableList<OTPrueba> pruebas = FXCollections.observableArrayList();
                for (final Object lEntity : list) {
                    pruebas.add(new OTPrueba((SPrueba) lEntity));
                }
                tblListadoPruebas.setItems(pruebas);
            }
        }
    }

    @Override
    public void onDeleted(IEntity entity) {
        if (entity instanceof SPrueba) {
            final OTPrueba pEliminar = new OTPrueba((SPrueba) entity);
            tblListadoPruebas.getItems().remove(pEliminar);
        }

    }

    @Override
    public void onSaved(IEntity otObject) {
        if (otObject instanceof SPrueba) {
            final OTPrueba ot = new OTPrueba((SPrueba) otObject);
            final int indice = tblListadoPruebas.getItems().lastIndexOf(ot);
            if (indice != -1) {
                tblListadoPruebas.getItems().set(indice, ot);
            } else {
                tblListadoPruebas.getItems().add(ot);
            }
            prueba = null;
        }
    }

    @Override
    public boolean validate() {
        return true;
    }

}
