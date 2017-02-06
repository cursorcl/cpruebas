package cl.eos.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_Ejetematico;
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
import cl.eos.util.MapBuilder;
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
import comunal.ResumenComunalViewView;
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
  
  public static int rowsPerPage = 25;
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
  private TableColumn<OTPrueba, Integer> nroPreguntasCol;
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
  private MenuItem mnuComparativoComunalEje;
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
  private MenuItem mnuResumenComunal;
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
  private R_Prueba prueba;
  private ComparativoComunalEjeView comparativoComunal;
  private ComparativoComunalHabilidadView comparativoComunalHabilidad;
  private ResumenComunalViewView resumenComunal;
  private EvaluarPruebaView evaluarPruebaView;
  private ImprimirPruebaView imprimirPrueba;
  private ResumenColegioView resumenColegio;
  
  private ObservableList<R_Asignatura> lstAsignaturas;
  private ObservableList<R_TipoCurso> lstTipoCurso;
  private ObservableList<OTPrueba>  pruebas;

  public ListadoPruebasView() {
    super();
    setTitle("Pruebas");
  }

  private void accionClicTabla() {
    tblListadoPruebas.setOnMouseClicked(event -> {
      final ObservableList<OTPrueba> itemsSelec =
          tblListadoPruebas.getSelectionModel().getSelectedItems();
      if (itemsSelec.size() > 1) {
        mnuCrear.setDisable(false);
        mnuModificar.setDisable(true);
        mnuEliminar.setDisable(false);
        mnuResumenComunal.setDisable(false);
        mnuPopupCrear.setDisable(false);
        mnuPopupModificar.setDisable(true);
        mnuPopupEliminar.setDisable(false);
        mnuImprimirPrueba.setDisable(true);
        mnuListaEvaluaciones.setDisable(true);
        mnuEvaluarPrueba.setDisable(true);
        mnuAnularPregunta.setDisable(true);
        mnuComparativoComunalEje.setDisable(true);
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
          mnuResumenComunal.setDisable(estadoDefinida);
          mnuComparativoComunalEje.setDisable(estadoDefinida);
          mnuComparativoComunalHab.setDisable(estadoDefinida);
        } else if (estadoEvaluada) {
          mnuEvaluarPrueba.setDisable(!estadoEvaluada);
          mnuListaEvaluaciones.setDisable(!estadoEvaluada);
          mnuResumenComunal.setDisable(!estadoEvaluada);
          mnuComparativoComunalEje.setDisable(!estadoEvaluada);
          mnuComparativoComunalHab.setDisable(!estadoEvaluada);
        } else if (estadoCreada) {
          mnuEvaluarPrueba.setDisable(estadoCreada);
          mnuListaEvaluaciones.setDisable(estadoCreada);
          mnuResumenComunal.setDisable(estadoCreada);
          mnuComparativoComunalEje.setDisable(estadoCreada);
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
    } else if (source == mnuComparativoComunalEje) {
      handlerComparativoComunal();
    } else if (source == mnuComparativoComunalNivel) {
      handlerComparativoComunalNivel();
    } else if (source == mnuComparativoComunalHab) {
      handlerComparativoComunalHab();
    } else if (source == mnuComparativoComunalHabNivel) {
      handlerComparativoComunalHabNivel();
    } else if (source == mnuResumenComunal) {
      handlerResumenComunal();
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
    final Nivel_PorObjetivosColegioView view =
        (Nivel_PorObjetivosColegioView) show("/colegio/nivel/fxml/Nivel_PorObjetivosColegio.fxml");
    
    prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
    Map<String, Object> params = MapBuilder.<String, Object>unordered().put("prueba_id", prueba.getId()).build();
    controller.findByParam(R_RespuestasEsperadasPrueba.class, params);
    controller.findAll(R_Asignatura.class, view);
    controller.findAll(R_TipoAlumno.class, view);
    controller.findAll(R_Colegio.class, view);
  }

  private void handlerXObjetivos() {
    final PorObjetivosColegioView view =
        (PorObjetivosColegioView) show("/colegio/fxml/PorObjetivosColegio.fxml");
    controller.findAll(R_Asignatura.class, view);
    controller.findAll(R_TipoAlumno.class, view);
    controller.findAll(R_Colegio.class, view);
  }

  private void handleCrear() {
    final DefinirPrueba definicion = (DefinirPrueba) show("/cl/eos/view/definir_prueba.fxml");
    controller.findAll(R_TipoCurso.class, definicion);
    controller.findAll(R_Profesor.class, definicion);
    controller.findAll(R_Asignatura.class, definicion);
    controller.findAll(R_TipoPrueba.class, definicion);
    controller.findAll(R_NivelEvaluacion.class, definicion);
    controller.findAll(R_Curso.class, definicion);
    controller.findAll(R_Habilidad.class, definicion);
  }

  private void handleEliminar() {
    final ObservableList<OTPrueba> otSeleccionados =
        tblListadoPruebas.getSelectionModel().getSelectedItems();
    if (otSeleccionados != null && !otSeleccionados.isEmpty()) {
      final List<R_Prueba> pruebas = new ArrayList<R_Prueba>(otSeleccionados.size());
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
        if (Estado.getEstado(prueba.getEstado()).equals(Estado.EVALUADA)) {
          final Alert info = new Alert(AlertType.INFORMATION);
          info.setTitle("No se puede modificar.");
          info.setHeaderText("La prueba ya se encuentra evaluada.");
          info.setContentText("No se podrá modificar.");
          info.show();
        }
        else
        {
          final DefinirPrueba definicion = (DefinirPrueba) show("/cl/eos/view/definir_prueba.fxml");
          
          controller.findAll(R_TipoCurso.class, definicion);
          controller.findAll(R_Profesor.class, definicion);
          controller.findAll(R_Asignatura.class, definicion);
          controller.findAll(R_TipoPrueba.class, definicion);
          controller.findAll(R_NivelEvaluacion.class, definicion);
          controller.findAll(R_Curso.class, definicion);
          controller.findAll(R_Habilidad.class, definicion);
          definicion.setPrueba(prueba);
         
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
        Map<String, Object> params = new HashMap<>();
        params.put("prueba_id", prueba.getId());
        controller.findByParam(R_EvaluacionPrueba.class, params, anularPregunta);
      }
    }
  }

  private void handlerComparativoColegioEjeHabilidad() {
    final ComparativoColegioEjeHabilidadView resumenColegioEjeHabiliadad =
        (ComparativoColegioEjeHabilidadView) show(
            "/colegio/fxml/ComparativoColegioEjeHabilidad.fxml");
    show(resumenColegioEjeHabiliadad);
    final R_Prueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
    resumenColegioEjeHabiliadad.setPrueba(prueba);
    Map<String, Object> params =
        MapBuilder.<String, Object>unordered().put("prueba_id", prueba.getId()).build();
    controller.findByParam(R_RespuestasEsperadasPrueba.class, params, resumenColegioEjeHabiliadad);
    controller.findAll(R_Colegio.class, resumenColegioEjeHabiliadad);
    controller.findAll(R_Asignatura.class, resumenColegioEjeHabiliadad);
    controller.findAll(R_EvaluacionEjetematico.class, resumenColegioEjeHabiliadad);
    controller.findAll(R_TipoAlumno.class, resumenColegioEjeHabiliadad);
  }

  private void handlerComparativoColegioEjeHabilidadXNivel() {
    final Nivel_ComparativoColegioEjeHabilidadView resumenColegioEjeHabiliadad =
        (Nivel_ComparativoColegioEjeHabilidadView) show(
            "/colegio/nivel/fxml/Nivel_ComparativoColegioEjeHabilidad.fxml");
    show(resumenColegioEjeHabiliadad);
    final R_Prueba pPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
    resumenColegioEjeHabiliadad.setPrueba(pPrueba);
    Map<String, Object> params =
        MapBuilder.<String, Object>unordered().put("prueba_id", prueba.getId()).build();
    controller.findByParam(R_RespuestasEsperadasPrueba.class, params, resumenColegioEjeHabiliadad);
    controller.findAll(R_Colegio.class, resumenColegioEjeHabiliadad);
    controller.findAll(R_Asignatura.class, resumenColegioEjeHabiliadad);
    controller.findAll(R_EvaluacionEjetematico.class, resumenColegioEjeHabiliadad);
    controller.findAll(R_TipoAlumno.class, resumenColegioEjeHabiliadad);
  }

  private void handlerComparativoComunal() {
    comparativoComunal =
        (ComparativoComunalEjeView) show("/comunal/fxml/ComparativoComunalEje.fxml");
    if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
      final R_Prueba pPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
      if (pPrueba != null) {
        comparativoComunal.setPrueba(pPrueba);

        Map<String, Object> params =
            MapBuilder.<String, Object>unordered().put("prueba_id", pPrueba.getId()).build();
        controller.findByParam(R_EvaluacionPrueba.class, params);

        controller.findById(R_Asignatura.class, pPrueba.getAsignatura_id());
        controller.findByParam(R_RespuestasEsperadasPrueba.class, params);
        controller.findAll(R_EvaluacionEjetematico.class, comparativoComunal);
        controller.findAll(R_TipoAlumno.class, comparativoComunal);
        controller.findAll(R_TipoColegio.class, comparativoComunal);
      }
    }
  }

  private void handlerComparativoComunalHab() {
    comparativoComunalHabilidad =
        (ComparativoComunalHabilidadView) show("/comunal/fxml/ComparativoComunalHabilidad.fxml");
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
    final Nivel_ComparativoComunalHabilidadView comparativoComunalHabilidadNivel =
        (Nivel_ComparativoComunalHabilidadView) show(
            "/comunal/nivel/fxml/Nivel_ComparativoComunalHabilidad.fxml");
    if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
      final R_Prueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
      if (prueba != null) {
        controller.findById(R_Prueba.class, prueba.getId(), comparativoComunalHabilidadNivel);
        controller.findById(R_Asignatura.class, prueba.getAsignatura_id(),
            comparativoComunalHabilidadNivel);
        Map<String, Object> params =
            MapBuilder.<String, Object>unordered().put("prueba_id", prueba.getId()).build();
        controller.findByParam(R_EvaluacionPrueba.class, params, comparativoComunalHabilidadNivel);
        controller.findByParam(R_RespuestasEsperadasPrueba.class, params,
            comparativoComunalHabilidadNivel);
        controller.findAll(R_EvaluacionEjetematico.class, comparativoComunalHabilidadNivel);
        controller.findAll(R_TipoAlumno.class, comparativoComunalHabilidadNivel);
        controller.findAll(R_TipoColegio.class, comparativoComunalHabilidadNivel);
      }
    }
  }

  private void handlerComparativoComunalNivel() {
    final Nivel_ComparativoComunalEjeView comparativoComunalNivel =
        (Nivel_ComparativoComunalEjeView) show(
            "/comunal/nivel/fxml/Nivel_ComparativoComunalEje.fxml");
    if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
      final R_Prueba pPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
      if (pPrueba != null) {

        controller.findById(R_Prueba.class, pPrueba.getId(), comparativoComunalNivel);
        controller.findById(R_Asignatura.class, pPrueba.getAsignatura_id(),
            comparativoComunalNivel);
        Map<String, Object> params =
            MapBuilder.<String, Object>unordered().put("prueba_id", pPrueba.getId()).build();
        controller.findByParam(R_EvaluacionPrueba.class, params, comparativoComunalNivel);
        controller.findByParam(R_RespuestasEsperadasPrueba.class, params, comparativoComunalNivel);
        controller.findAll(R_EvaluacionEjetematico.class, comparativoComunalNivel);
        controller.findAll(R_TipoAlumno.class, comparativoComunalNivel);
        controller.findAll(R_TipoColegio.class, comparativoComunalNivel);
      }
    }
  }

  private void handlerCompColegioEjeHabilXCurso() {
    final ComparativoColegioEjeHabilidadxCursoView resColegioHabEjeCurso =
        (ComparativoColegioEjeHabilidadxCursoView) show(
            "/colegio/fxml/ComparativoColegioEjeHabilidadxCurso.fxml");
    show(resColegioHabEjeCurso);
    final R_Prueba pPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
    resColegioHabEjeCurso.setPrueba(pPrueba);

    Map<String, Object> params =
        MapBuilder.<String, Object>unordered().put("prueba_id", pPrueba.getId()).build();
    controller.findByParamsSynchro(R_RespuestasEsperadasPrueba.class, params);
    controller.findAll(R_Colegio.class, resColegioHabEjeCurso);
    controller.findAll(R_Asignatura.class, resColegioHabEjeCurso);
    controller.findAll(R_EvaluacionEjetematico.class, resColegioHabEjeCurso);
    controller.findAll(R_TipoAlumno.class, resColegioHabEjeCurso);
  }

  private void handlerCompEjesHabXNivel() {
    if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
      final Nivel_ComparativoColegioEjeHabilidadxCursoView comparativoComunalHabilidad =
          (Nivel_ComparativoColegioEjeHabilidadxCursoView) show(
              "/colegio/nivel/fxml/Nivel_ComparativoColegioEjeHabilidadxCurso.fxml");
      final R_Prueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
      if (prueba != null) {
        comparativoComunalHabilidad.setPrueba(prueba);
        // controller.findById(R_Curso.class, prueba.getCurso_id(), comparativoComunalHabilidad);
        controller.findAll(R_Colegio.class, comparativoComunalHabilidad);
        controller.findAll(R_Asignatura.class, comparativoComunalHabilidad);
        controller.findAll(R_EvaluacionEjetematico.class, comparativoComunalHabilidad);
        controller.findAll(R_TipoAlumno.class, comparativoComunalHabilidad);
        controller.findAll(R_TipoColegio.class, comparativoComunalHabilidad);
      }
    }
  }

  private void handlerResumenComunal() {
    
    //  TODO debo preocuparme que solamente se seleccionen pruebas del mismo ramo.
    resumenComunal = (ResumenComunalViewView) show("/comunal/fxml/ResumenComunal.fxml");
    final ObservableList<OTPrueba> otPruebas =
        tblListadoPruebas.getSelectionModel().getSelectedItems();
    if (otPruebas != null) {
      
      final Long[] pruebas = new Long[otPruebas.size()];
      int n = 0;
      for (final OTPrueba ot : otPruebas) {
        pruebas[n++] = ot.getPrueba().getId();
      }
      R_Prueba prueba = otPruebas.get(0).getPrueba();
      
      controller.findById(R_Asignatura.class, prueba.getAsignatura_id(), resumenComunal);
      controller.findByAllId(R_Prueba.class, pruebas, resumenComunal);
      controller.findAll(R_EvaluacionEjetematico.class, resumenComunal);
      controller.findAll(R_TipoAlumno.class, resumenComunal);
      controller.findAll(R_TipoColegio.class, resumenComunal);
    }
  }

  private void handlerEjesEvaluacion() {
    final ComparativoColegioEjeEvaluacionView resumenEjeEvaluacion =
        (ComparativoColegioEjeEvaluacionView) show(
            "/colegio/fxml/ComparativoColegioEjeEvaluacion.fxml");
    show(resumenEjeEvaluacion);
    final R_Prueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
    // Rangos para la evaluación
    Map<String, Object> params = MapBuilder.<String, Object>unordered()
        .put("nivelevaluacion_id", prueba.getNivelevaluacion_id()).build();
    controller.findByParam(R_RangoEvaluacion.class, params);
    // Respuestas esperadas de la prueba.
    params = MapBuilder.<String, Object>unordered().put("prueba_id", prueba.getId()).build();
    controller.findByParam(R_RespuestasEsperadasPrueba.class, params);



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
    final ComparativoColegioHabilidadesView resumenHabilidades =
        (ComparativoColegioHabilidadesView) show(
            "/colegio/fxml/ComparativoColegioHabilidades.fxml");
    show(resumenHabilidades);
    final R_Prueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
    // Rangos para la evaluación
    Map<String, Object> params = MapBuilder.<String, Object>unordered()
        .put("nivelevaluacion_id", prueba.getNivelevaluacion_id()).build();
    controller.findByParam(R_RangoEvaluacion.class, params);
    controller.findAll(R_Colegio.class, resumenHabilidades);
    controller.findAll(R_Asignatura.class, resumenHabilidades);
    controller.findAll(R_TipoAlumno.class, resumenHabilidades);
  }

  private void handlerHabilidadEvaluacionXAlumno() {
    final ResumenColegioXAlumnoEjeHabilidadView resHabEjeAlumno =
        (ResumenColegioXAlumnoEjeHabilidadView) show(
            "/colegio/fxml/ResumenColegioXAlumnoEjeHabilidad.fxml");
    show(resHabEjeAlumno);
    controller.findAll(R_Colegio.class, resHabEjeAlumno);
    controller.findAll(R_Asignatura.class, resHabEjeAlumno);
    controller.findAll(R_TipoAlumno.class, resHabEjeAlumno);
  }

  private void handlerHabilidadEvaluacionXAlumnoXNivel() {
    final Nivel_ComparativoColegioEjeEvaluacionView resHabEjeAlumno =
        (Nivel_ComparativoColegioEjeEvaluacionView) show(
            "/colegio/nivel/fxml/Nivel_ComparativoColegioEjeEvaluacion.fxml");
    Long idNivelEval =
        tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba().getNivelevaluacion_id();
    R_NivelEvaluacion nivelEvaluacion =
        controller.findByIdSynchro(R_NivelEvaluacion.class, idNivelEval);
    R_Prueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
    resHabEjeAlumno.setPrueba(prueba);
    resHabEjeAlumno.setNivelEvaluacion(nivelEvaluacion);
    show(resHabEjeAlumno);
    controller.findAll(R_Colegio.class, resHabEjeAlumno);
    controller.findAll(R_Asignatura.class, resHabEjeAlumno);
    controller.findAll(R_TipoAlumno.class, resHabEjeAlumno);
  }

  private void handlerHabilidadEvaluacionXNivel() {
    final Nivel_ComparativoColegioHabilidadesView nivel_comparativocolegiohabilidadesview =
        (Nivel_ComparativoColegioHabilidadesView) show(
            "/colegio/nivel/fxml/Nivel_ComparativoColegioHabilidades.fxml");
    show(nivel_comparativocolegiohabilidadesview);
    Long idNivelEval =
        tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba().getNivelevaluacion_id();
    Map<String, Object> parameters =
        MapBuilder.<String, Object>unordered().put("nivelevaluacion_id", idNivelEval).build();
    controller.findByParam(R_RangoEvaluacion.class, parameters);

    controller.findAll(R_Colegio.class, nivel_comparativocolegiohabilidadesview);
    controller.findAll(R_Asignatura.class, nivel_comparativocolegiohabilidadesview);
    controller.findAll(R_TipoAlumno.class, nivel_comparativocolegiohabilidadesview);
  }

  private void handlerImrpimirPrueba() {
    imprimirPrueba = (ImprimirPruebaView) show("/cl/eos/view/ImprimirPrueba.fxml");
    if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
      final R_Prueba pPrueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
      if (pPrueba != null) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("idAsignatura", pPrueba.getAsignatura_id());
        controller.findByParam(R_Ejetematico.class, parameters, imprimirPrueba);
        controller.findAll(R_Habilidad.class, imprimirPrueba);
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
    evaluacionPrueba = (EvaluacionPruebaView) show("/cl/eos/view/EvaluacionPrueba.fxml");
    if (tblListadoPruebas.getSelectionModel().getSelectedItem() != null) {
      final R_Prueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
      evaluacionPrueba.setPrueba(prueba);
      final Map<String, Object> parameters = new HashMap<String, Object>();
      parameters.put("prueba_id", prueba.getId());
      controller.findByParam(R_EvaluacionPrueba.class, parameters, evaluacionPrueba);
    }
  }

  private void handlerResumenColegios() {
    resumenColegio = (ResumenColegioView) show("/colegio/fxml/ResumenColegio.fxml");
    show(resumenColegio);
    final R_Prueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
    resumenColegio.setPrueba(prueba);
    Map<String, Object> params = MapBuilder.<String, Object>unordered()
        .put("nivelevaluacion_id", prueba.getNivelevaluacion_id()).build();
    controller.findByParam(R_RangoEvaluacion.class, params, resumenColegio);
    controller.findAll(R_Colegio.class, resumenColegio);
    controller.findAll(R_Asignatura.class, resumenColegio);
    controller.findAll(R_EvaluacionEjetematico.class, resumenColegio);
    controller.findAll(R_TipoAlumno.class, resumenColegio);
  }

  private void handlerResumenColegiosXNivel() {
    final Nivel_ResumenColegioView resumenColegio =
        (Nivel_ResumenColegioView) show("/colegio/nivel/fxml/Nivel_ResumenColegio.fxml");
    show(resumenColegio);
    final R_Prueba prueba = tblListadoPruebas.getSelectionModel().getSelectedItem().getPrueba();
    resumenColegio.setPrueba(prueba);

    controller.findAll(R_Colegio.class, resumenColegio);
    controller.findAll(R_Asignatura.class, resumenColegio);
    Map<String, Object> params = MapBuilder.<String, Object>unordered()
        .put("nivelevaluacion_id", prueba.getNivelevaluacion_id()).build();
    controller.findByParam(R_RangoEvaluacion.class, params, resumenColegio);
    controller.findAll(R_EvaluacionEjetematico.class, resumenColegio);
    controller.findAll(R_TipoAlumno.class, resumenColegio);
  }

  @FXML
  public void initialize() {
    tblListadoPruebas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    fechaCol.setCellValueFactory(new PropertyValueFactory<OTPrueba, LocalDate>("fechaLocal"));
    nameCol.setCellValueFactory(new PropertyValueFactory<OTPrueba, String>("name"));
    asignaturaCol.setCellValueFactory(new PropertyValueFactory<OTPrueba, String>("asignatura"));
    cursoCol.setCellValueFactory(new PropertyValueFactory<OTPrueba, String>("curso"));
    nroPreguntasCol
        .setCellValueFactory(new PropertyValueFactory<OTPrueba, Integer>("nroPreguntas"));
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
    mnuComparativoComunalEje.setOnAction(this);
    mnuComparativoComunalNivel.setOnAction(this);
    mnuComparativoComunalHab.setOnAction(this);
    mnuComparativoComunalHabNivel.setOnAction(this);
    mnuResumenComunal.setOnAction(this);
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
      if (entity instanceof R_Prueba) {
        pruebas = FXCollections.observableArrayList();
        for (final Object lEntity : list) {
          R_Prueba pPrueba = (R_Prueba) lEntity;
          pruebas.add(new OTPrueba(pPrueba));
        }
        assignValues();
      } else if (entity instanceof R_Asignatura) {
        lstAsignaturas = FXCollections.observableArrayList();
        for (final Object lEntity : list) {
          lstAsignaturas.add((R_Asignatura) lEntity);
        }
        assignValues();
      } else if (entity instanceof R_TipoCurso) {
        lstTipoCurso = FXCollections.observableArrayList();
        for (final Object lEntity : list) {
          lstTipoCurso.add((R_TipoCurso) lEntity);
        }
        assignValues();
      }
    }
  }

  private void assignValues() {
    
    if(lstAsignaturas == null || lstTipoCurso == null || pruebas == null)
    {
      return;
    }
    Map<Long, R_Asignatura> mapAsignaturas = lstAsignaturas.stream()
        .collect(Collectors.toMap(R_Asignatura::getId, Function.identity()));
    Map<Long, R_TipoCurso> mapTiposCurso = lstTipoCurso.stream()
        .collect(Collectors.toMap(R_TipoCurso::getId, Function.identity()));
    for (final OTPrueba ot : pruebas) {
      ot.setAsignatura( mapAsignaturas.get(ot.getPrueba().getAsignatura_id()));
      ot.setCurso(mapTiposCurso.get(ot.getPrueba().getCurso_id()));
    }
    tblListadoPruebas.setItems(pruebas);
    
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
    if (otObject instanceof R_Prueba) {
      final OTPrueba ot = new OTPrueba((R_Prueba) otObject);
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
