package cl.eos.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.EvaluacionEjeTematico;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.NivelEvaluacion;
import cl.eos.persistence.models.Profesor;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.Prueba.Estado;
import cl.eos.persistence.models.RangoEvaluacion;
import cl.eos.persistence.models.TipoAlumno;
import cl.eos.persistence.models.TipoColegio;
import cl.eos.persistence.models.TipoCurso;
import cl.eos.persistence.models.TipoPrueba;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
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

    private EvaluacionPruebaView evaluacionPrueba;
    private AnularPreguntasViewController anularPregunta;
    private Prueba prueba;
    private ComparativoComunalEjeView comparativoComunal;
    private ComparativoComunalHabilidadView comparativoComunalHabilidad;
    private ComunalCursoView comunalEje;

    private EvaluarPruebaView evaluarPruebaView;
    private ImprimirPruebaView imprimirPrueba;
    private ResumenColegioView resumenColegio;

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
        controller.findAll(Asignatura.class, view);
        controller.findAll(TipoAlumno.class, view);
        controller.findAll(Colegio.class, view);

    }

    private void handlerXObjetivos() {
        final PorObjetivosColegioView view = (PorObjetivosColegioView) show("/colegio/fxml/PorObjetivosColegio.fxml");
        controller.findAll(Asignatura.class, view);
        controller.findAll(TipoAlumno.class, view);
        controller.findAll(Colegio.class, view);
    }

    private void handleCrear() {
        final DefinirPrueba definicion = (DefinirPrueba) show("/cl/eos/view/definir_prueba.fxml");
        controller.findAll(TipoCurso.class, definicion);
        controller.findAll(Profesor.class, definicion);
        controller.findAll(Asignatura.class, definicion);
        controller.findAll(TipoPrueba.class, definicion);
        controller.findAll(NivelEvaluacion.class, definicion);
        controller.findAll(Curso.class, definicion);
        controller.findAll(Habilidad.class, definicion);
    }

    private void handleEliminar() {

        final ObservableList<OTPrueba> otSeleccionados = tblListadoPruebas.getSelectionModel().getSelectedItems();
        if (otSeleccionados != null && !otSeleccionados.isEmpty()) {
            final List<Prueba> pruebas = new ArrayList<Prueba>(otSeleccionados.size());
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
            final Prueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();

            anularPregunta = (AnularPreguntasViewController) show("/cl/eos/view/AnulaPreguntasView.fxml");

            if (prueba != null) {
                controller.findById(Prueba.class, prueba.getId(), anularPregunta);
            }
        }
    }

    private void handlerComparativoColegioEjeHabilidad() {
        final ComparativoColegioEjeHabilidadView resumenColegioEjeHabiliadad = (ComparativoColegioEjeHabilidadView) show(
                "/colegio/fxml/ComparativoColegioEjeHabilidad.fxml");
        show(resumenColegioEjeHabiliadad);
        controller.findAll(Colegio.class, resumenColegioEjeHabiliadad);
        controller.findAll(Asignatura.class, resumenColegioEjeHabiliadad);
        controller.findAll(EvaluacionEjeTematico.class, resumenColegioEjeHabiliadad);
        controller.findAll(TipoAlumno.class, resumenColegioEjeHabiliadad);
    }

    private void handlerComparativoColegioEjeHabilidadXNivel() {
        final Nivel_ComparativoColegioEjeHabilidadView resumenColegioEjeHabiliadad = (Nivel_ComparativoColegioEjeHabilidadView) show(
                "/colegio/nivel/fxml/Nivel_ComparativoColegioEjeHabilidad.fxml");
        show(resumenColegioEjeHabiliadad);
        controller.findAll(Colegio.class, resumenColegioEjeHabiliadad);
        controller.findAll(Asignatura.class, resumenColegioEjeHabiliadad);
        controller.findAll(EvaluacionEjeTematico.class, resumenColegioEjeHabiliadad);
        controller.findAll(TipoAlumno.class, resumenColegioEjeHabiliadad);
    }

    private void handlerComparativoComunal() {
        comparativoComunal = (ComparativoComunalEjeView) show("/comunal/fxml/ComparativoComunalEje.fxml");
        if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
            final Prueba pPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
            if (pPrueba != null) {
                controller.findById(Prueba.class, pPrueba.getId(), comparativoComunal);
                controller.findAll(EvaluacionEjeTematico.class, comparativoComunal);
                controller.findAll(TipoAlumno.class, comparativoComunal);
                controller.findAll(TipoColegio.class, comparativoComunal);
            }
        }
    }

    private void handlerComparativoComunalHab() {
        comparativoComunalHabilidad = (ComparativoComunalHabilidadView) show(
                "/comunal/fxml/ComparativoComunalHabilidad.fxml");
        if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
            final Prueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
            if (prueba != null) {
                controller.findById(Prueba.class, prueba.getId(), comparativoComunalHabilidad);
                controller.findAll(EvaluacionEjeTematico.class, comparativoComunalHabilidad);
                controller.findAll(TipoAlumno.class, comparativoComunalHabilidad);
                controller.findAll(TipoColegio.class, comparativoComunalHabilidad);
            }
        }
    }

    private void handlerComparativoComunalHabNivel() {
        final Nivel_ComparativoComunalHabilidadView comparativoComunalHabilidadNivel = (Nivel_ComparativoComunalHabilidadView) show(
                "/comunal/nivel/fxml/Nivel_ComparativoComunalHabilidad.fxml");
        if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
            final Prueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
            if (prueba != null) {
                controller.findById(Prueba.class, prueba.getId(), comparativoComunalHabilidadNivel);
                controller.findAll(EvaluacionEjeTematico.class, comparativoComunalHabilidadNivel);
                controller.findAll(TipoAlumno.class, comparativoComunalHabilidadNivel);
                controller.findAll(TipoColegio.class, comparativoComunalHabilidadNivel);
            }
        }
    }

    private void handlerComparativoComunalNivel() {
        final Nivel_ComparativoComunalEjeView comparativoComunalNivel = (Nivel_ComparativoComunalEjeView) show(
                "/comunal/nivel/fxml/Nivel_ComparativoComunalEje.fxml");
        if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
            final Prueba pPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
            if (pPrueba != null) {
                controller.findById(Prueba.class, pPrueba.getId(), comparativoComunalNivel);
                controller.findAll(EvaluacionEjeTematico.class, comparativoComunalNivel);
                controller.findAll(TipoAlumno.class, comparativoComunalNivel);
                controller.findAll(TipoColegio.class, comparativoComunalNivel);
            }
        }
    }

    private void handlerCompColegioEjeHabilXCurso() {
        final ComparativoColegioEjeHabilidadxCursoView resColegioHabEjeCurso = (ComparativoColegioEjeHabilidadxCursoView) show(
                "/colegio/fxml/ComparativoColegioEjeHabilidadxCurso.fxml");
        show(resColegioHabEjeCurso);
        controller.findAll(Colegio.class, resColegioHabEjeCurso);
        controller.findAll(Asignatura.class, resColegioHabEjeCurso);
        controller.findAll(EvaluacionEjeTematico.class, resColegioHabEjeCurso);
        controller.findAll(TipoAlumno.class, resColegioHabEjeCurso);
    }

    private void handlerCompEjesHabXNivel() {
        if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
            final Nivel_ComparativoColegioEjeHabilidadxCursoView comparativoComunalHabilidad = (Nivel_ComparativoColegioEjeHabilidadxCursoView) show(
                    "/colegio/nivel/fxml/Nivel_ComparativoColegioEjeHabilidadxCurso.fxml");

            final Prueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
            if (prueba != null) {
                controller.findById(Prueba.class, prueba.getId(), comparativoComunalHabilidad);
                controller.findAll(Colegio.class, comparativoComunalHabilidad);
                controller.findAll(Asignatura.class, comparativoComunalHabilidad);
                controller.findAll(EvaluacionEjeTematico.class, comparativoComunalHabilidad);
                controller.findAll(TipoAlumno.class, comparativoComunalHabilidad);
                controller.findAll(TipoColegio.class, comparativoComunalHabilidad);
            }
        }
    }

    private void handlerComunalEje() {

        comunalEje = (ComunalCursoView) show("/comunal/fxml/ComunalEje.fxml");

        final ObservableList<OTPrueba> otPruebas = tblListadoPruebas.getSelectionModel().getSelectedItems();
        if (otPruebas != null) {
            final Prueba[] pruebas = new Prueba[otPruebas.size()];
            int n = 0;
            for (final OTPrueba ot : otPruebas) {
                pruebas[n++] = ot.getPrueba();
            }
            controller.findByAllId(Prueba.class, pruebas, comunalEje);
            controller.findAll(EvaluacionEjeTematico.class, comunalEje);
            controller.findAll(TipoAlumno.class, comunalEje);
            controller.findAll(TipoColegio.class, comunalEje);
        }
    }

    private void handlerEjesEvaluacion() {
        final ComparativoColegioEjeEvaluacionView resumenEjeEvaluacion = (ComparativoColegioEjeEvaluacionView) show(
                "/colegio/fxml/ComparativoColegioEjeEvaluacion.fxml");
        show(resumenEjeEvaluacion);
        controller.findAll(Colegio.class, resumenEjeEvaluacion);
        controller.findAll(Asignatura.class, resumenEjeEvaluacion);
        controller.findAll(TipoAlumno.class, resumenEjeEvaluacion);
    }

    private void handlerEvaluar() {
        evaluarPruebaView = (EvaluarPruebaView) show("/cl/eos/view/EvaluarPrueba.fxml");
        if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
            final Prueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
            if (prueba != null) {
                controller.findById(Prueba.class, prueba.getId(), evaluarPruebaView);
                controller.findAll(Colegio.class, evaluarPruebaView);
                controller.findAll(Profesor.class, evaluarPruebaView);
            }
        }
    }

    private void handlerHabilidadEvaluacion() {
        final ComparativoColegioHabilidadesView resumenHabilidades = (ComparativoColegioHabilidadesView) show(
                "/colegio/fxml/ComparativoColegioHabilidades.fxml");
        show(resumenHabilidades);
        controller.findAll(Colegio.class, resumenHabilidades);
        controller.findAll(Asignatura.class, resumenHabilidades);
        controller.findAll(TipoAlumno.class, resumenHabilidades);

    }

    private void handlerHabilidadEvaluacionXAlumno() {
        final ResumenColegioXAlumnoEjeHabilidadView resHabEjeAlumno = (ResumenColegioXAlumnoEjeHabilidadView) show(
                "/colegio/fxml/ResumenColegioXAlumnoEjeHabilidad.fxml");
        show(resHabEjeAlumno);
        controller.findAll(Colegio.class, resHabEjeAlumno);
        controller.findAll(Asignatura.class, resHabEjeAlumno);
        controller.findAll(TipoAlumno.class, resHabEjeAlumno);

    }

    private void handlerHabilidadEvaluacionXAlumnoXNivel() {
        final Nivel_ComparativoColegioEjeEvaluacionView resHabEjeAlumno = (Nivel_ComparativoColegioEjeEvaluacionView) show(
                "/colegio/nivel/fxml/Nivel_ComparativoColegioEjeEvaluacion.fxml");
        show(resHabEjeAlumno);
        controller.findAll(Colegio.class, resHabEjeAlumno);
        controller.findAll(Asignatura.class, resHabEjeAlumno);
        controller.findAll(TipoAlumno.class, resHabEjeAlumno);
    }

    private void handlerHabilidadEvaluacionXNivel() {
        final Nivel_ComparativoColegioHabilidadesView resumenHabilidades = (Nivel_ComparativoColegioHabilidadesView) show(
                "/colegio/nivel/fxml/Nivel_ComparativoColegioHabilidades.fxml");
        show(resumenHabilidades);
        controller.findAll(Colegio.class, resumenHabilidades);
        controller.findAll(Asignatura.class, resumenHabilidades);
        controller.findAll(TipoAlumno.class, resumenHabilidades);
    }

    private void handlerImrpimirPrueba() {
        imprimirPrueba = (ImprimirPruebaView) show("/cl/eos/view/ImprimirPrueba.fxml");
        if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
            final Prueba pPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
            if (pPrueba != null) {
                controller.findById(Prueba.class, pPrueba.getId(), imprimirPrueba);
                final Map<String, Object> parameters = new HashMap<>();
                parameters.put("idAsignatura", pPrueba.getAsignatura().getId());
                controller.find("EjeTematico.findByAsigntura", parameters, imprimirPrueba);
                controller.findAll(Habilidad.class, imprimirPrueba);
                controller.findAll(Profesor.class, imprimirPrueba);
                controller.findAll(Colegio.class, imprimirPrueba);
            }
        }
    }

    private void handlerInforme() {
        final InformeView informe = (InformeView) showOver("/informe/informeView.fxml");
        show(informe);
        controller.findAll(Colegio.class, informe);
        controller.findAll(Asignatura.class, informe);
        controller.findAll(TipoAlumno.class, informe);
    }

    private void handlerListaEvaluaciones() {
        evaluacionPrueba = (EvaluacionPruebaView) show("/cl/eos/view/EvaluacionPrueba.fxml");
        if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
            final Prueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
            final Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("idPrueba", prueba.getId());
            controller.find("EvaluacionPrueba.findByPrueba", parameters, evaluacionPrueba);
        }
    }

    private void handlerResumenColegios() {
        resumenColegio = (ResumenColegioView) show("/colegio/fxml/ResumenColegio.fxml");
        show(resumenColegio);
        controller.findAll(Colegio.class, resumenColegio);
        controller.findAll(Asignatura.class, resumenColegio);
        controller.findAll(RangoEvaluacion.class, resumenColegio);
        controller.findAll(EvaluacionEjeTematico.class, resumenColegio);
        controller.findAll(TipoAlumno.class, resumenColegio);
    }

    private void handlerResumenColegiosXNivel() {
        final Nivel_ResumenColegioView resumenColegio = (Nivel_ResumenColegioView) show(
                "/colegio/nivel/fxml/Nivel_ResumenColegio.fxml");
        show(resumenColegio);
        controller.findAll(Colegio.class, resumenColegio);
        controller.findAll(Asignatura.class, resumenColegio);
        controller.findAll(RangoEvaluacion.class, resumenColegio);
        controller.findAll(EvaluacionEjeTematico.class, resumenColegio);
        controller.findAll(TipoAlumno.class, resumenColegio);
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
        accionClicTabla();
    }

    @Override
    public void onDataArrived(List<Object> list) {

        if (list != null && !list.isEmpty()) {
            final Object entity = list.get(0);
            if (entity instanceof Prueba) {
                final ObservableList<OTPrueba> pruebas = FXCollections.observableArrayList();
                for (final Object lEntity : list) {
                    pruebas.add(new OTPrueba((Prueba) lEntity));
                }
                tblListadoPruebas.setItems(pruebas);
            }
        }
    }

    @Override
    public void onDeleted(IEntity entity) {
        if (entity instanceof Prueba) {
            final OTPrueba pEliminar = new OTPrueba((Prueba) entity);
            tblListadoPruebas.getItems().remove(pEliminar);
        }

    }

    @Override
    public void onSaved(IEntity otObject) {
        if (otObject instanceof Prueba) {
            final OTPrueba ot = new OTPrueba((Prueba) otObject);
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
