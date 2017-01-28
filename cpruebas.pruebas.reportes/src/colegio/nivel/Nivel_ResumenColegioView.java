package colegio.nivel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.ot.OTRangoTipoCurso;
import cl.eos.ot.OTResumenColegio;
import cl.eos.ot.OTResumenTipoCursoColegio;
import cl.eos.persistence.util.Comparadores;
import cl.eos.restful.EntityUtils;
import cl.eos.restful.tables.R_Alumno;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_EvaluacionEjetematico;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_NivelEvaluacion;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_RangoEvaluacion;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.restful.tables.R_TipoCurso;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.MapBuilder;
import cl.eos.util.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class Nivel_ResumenColegioView extends AFormView implements EventHandler<ActionEvent> {

  private static final String FX_ALIGNMENT_CENTER = "-fx-alignment: CENTER;";

  private static final String ASIGNATURA_ID = "asignatura_id";

  private static Logger log = Logger.getLogger(Nivel_ResumenColegioView.class.getName());

  private static final String COLEGIO_ID = "colegio_id";
  @SuppressWarnings("rawtypes")
  @FXML
  private TableView tblPME;
  @FXML
  private TableView<OTResumenTipoCursoColegio> tblCursos;
  @FXML
  private TableColumn<OTResumenTipoCursoColegio, String> colCurso;
  @FXML
  private TableColumn<OTResumenTipoCursoColegio, Integer> colTotal;
  @FXML
  private TableColumn<OTResumenTipoCursoColegio, Integer> colEvaluados;
  @FXML
  private TableColumn<OTResumenTipoCursoColegio, Integer> colAprobados;
  @FXML
  private TableColumn<OTResumenTipoCursoColegio, Integer> colReprobados;
  @FXML
  private TableColumn<OTResumenTipoCursoColegio, Float> colPEvaluados;
  @FXML
  private TableColumn<OTResumenTipoCursoColegio, Float> colPAprobados;
  @FXML
  private TableColumn<OTResumenTipoCursoColegio, Float> colPReprobados;

  @SuppressWarnings("rawtypes")
  @FXML
  private TableView tblResumenTotal;
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
  private MenuItem mnuExportarGeneral;
  @FXML
  private MenuItem mnuExportarPME;
  @FXML
  private MenuItem mnuExportarAlumnos;

  Map<String, Object> parameters = new HashMap<>();
  private Map<Long, OTResumenColegio> mapaCursos = new HashMap<>();

  private List<OTResumenTipoCursoColegio> lstCursos = new ArrayList<>();

  private ObservableList<R_RangoEvaluacion> lstRangoEvaluacion;

  private Map<Integer, R_EvaluacionEjetematico> tituloEvaluacion = new HashMap<>();

  private Map<Long, R_EvaluacionEjetematico> mEvaluaciones = new HashMap<>();

  private Map<R_TipoCurso, Map<R_RangoEvaluacion, OTRangoTipoCurso>> pmeCursos = new HashMap<>();

  private Map<R_EvaluacionEjetematico, Integer> mResumen = new HashMap<>();

  private OTResumenTipoCursoColegio resumenTotal;

  private Map<Integer, R_RangoEvaluacion> mapaRangos = new HashMap<>();

  private R_Prueba prueba;
  private ObservableList<R_TipoCurso> lstTipoCurso;

  private static final int ANCHO_COL = 83;

  public Nivel_ResumenColegioView() {
    setTitle("Resumen Colegio por Nivel");
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

    if (source == mnuExportarAlumnos || source == mnuExportarGeneral || source == mnuExportarPME) {

      tblCursos.setId("Resumen cursos");
      tblPME.setId("Resumen PME cursors");
      tblResumenTotal.setId("Resumen general colegeio");

      List<TableView<? extends Object>> listaTablas = new LinkedList<>();
      listaTablas.add((TableView<? extends Object>) tblCursos);
      listaTablas.add((TableView<? extends Object>) tblPME);
      listaTablas.add((TableView<? extends Object>) tblResumenTotal);
      ExcelSheetWriterObj.convertirDatosALibroDeExcel(listaTablas);
    }
  }

  private void handleColegios() {
    R_Colegio colegio = cmbColegios.getSelectionModel().getSelectedItem();
    if (colegio != null) {
      parameters.put(COLEGIO_ID, colegio.getId());
    }
  }

  private void handleAsignatura() {
    R_Asignatura asignatura = cmbAsignatura.getSelectionModel().getSelectedItem();
    if (asignatura != null) {
      parameters.put(ASIGNATURA_ID, asignatura.getId());
    }
  }

  private void handleReportes() {
    pmeCursos.clear();
    mapaCursos.clear();
    mResumen.clear();
    lstCursos.clear();
    tblCursos.getItems().clear();
    tblPME.getItems().clear();
    tblResumenTotal.getItems().clear();
    if (!parameters.isEmpty() && parameters.containsKey(COLEGIO_ID)
        && parameters.containsKey(ASIGNATURA_ID)) {
      controller.findByParam(R_EvaluacionPrueba.class, parameters, this);
    }
  }

  @FXML
  public void initialize() {
    inicializarTablaCursos();
    inicializaComponentes();
  }

  private void inicializarTablaCursos() {
    tblCursos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    colCurso.setCellValueFactory(
        new PropertyValueFactory<OTResumenTipoCursoColegio, String>("tipoCurso"));
    colTotal.setCellValueFactory(
        new PropertyValueFactory<OTResumenTipoCursoColegio, Integer>("totalAlumnos"));
    colEvaluados.setCellValueFactory(
        new PropertyValueFactory<OTResumenTipoCursoColegio, Integer>("totalEvaluados"));
    colAprobados.setCellValueFactory(
        new PropertyValueFactory<OTResumenTipoCursoColegio, Integer>("totalAprobados"));
    colReprobados.setCellValueFactory(
        new PropertyValueFactory<OTResumenTipoCursoColegio, Integer>("totalReprobados"));
    colPEvaluados.setCellValueFactory(
        new PropertyValueFactory<OTResumenTipoCursoColegio, Float>("alumnosEvaluados"));
    colPAprobados.setCellValueFactory(
        new PropertyValueFactory<OTResumenTipoCursoColegio, Float>("alumnosAprobados"));
    colPReprobados.setCellValueFactory(
        new PropertyValueFactory<OTResumenTipoCursoColegio, Float>("alumnosReprobados"));

  }

  private void inicializaComponentes() {
    pmeCursos.clear();
    mapaCursos.clear();
    mResumen.clear();
    lstCursos.clear();

    cmbColegios.setOnAction(this);
    cmbAsignatura.setOnAction(this);
    btnReportes.setOnAction(this);
    mnuExportarAlumnos.setOnAction(this);
    mnuExportarGeneral.setOnAction(this);
    mnuExportarPME.setOnAction(this);
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
      if (entity instanceof R_TipoAlumno) {
        ObservableList<R_TipoAlumno> tAlumnoList = FXCollections.observableArrayList();
        for (Object iEntity : list) {
          tAlumnoList.add((R_TipoAlumno) iEntity);
        }
        cmbTipoAlumno.setItems(tAlumnoList);
      }
      if (entity instanceof R_TipoCurso) {
        lstTipoCurso = FXCollections.observableArrayList();
        for (Object iEntity : list) {
          lstTipoCurso.add((R_TipoCurso) iEntity);
        }
      }
      if (entity instanceof R_RangoEvaluacion) {
        lstRangoEvaluacion = FXCollections.observableArrayList();
        for (Object iEntity : list) {
          lstRangoEvaluacion.add((R_RangoEvaluacion) iEntity);
        }
        if (!lstRangoEvaluacion.isEmpty()) {
          generarColumnasPME();
        }
      }
      if (entity instanceof R_EvaluacionPrueba) {
        generarReporteCursos(list);
      }
      if (entity instanceof R_EvaluacionEjetematico) {
        for (Object object : list) {
          R_EvaluacionEjetematico evaluacion = (R_EvaluacionEjetematico) object;
          mEvaluaciones.put(evaluacion.getId(), evaluacion);

        }
        if (mEvaluaciones != null) {
          agregarColumnasTbl();
        }
      }

    }
  }

  private void generarReporteCursos(List<Object> list) {
    if (list == null || list.isEmpty())
      return;

    R_EvaluacionPrueba firstEvaluacion = (R_EvaluacionPrueba) list.get(0);
    R_Colegio colegio = cmbColegios.getItems().stream()
        .filter(c -> c.getId().equals(firstEvaluacion.getCurso_id())).findFirst().orElse(null);
    StringBuilder string = new StringBuilder();
    string.append(colegio);
    lblColegio.setText(string.toString());

    R_NivelEvaluacion nivel =
        controller.findSynchroById(R_NivelEvaluacion.class, prueba.getNivelevaluacion_id());
    int totalColAlumnos = 0;
    int totalColEvaluados = 0;
    int totalColAprobados = 0;
    int totalColReprobados = 0;
    long tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedItem().getId();
    // Todas las evaluaciones asociadas (Todos los cursos)
    for (Object evaluacionPrueba : list) {
      R_EvaluacionPrueba evaluacion = (R_EvaluacionPrueba) evaluacionPrueba;
      OTResumenTipoCursoColegio resumenCurso = new OTResumenTipoCursoColegio();
      resumenCurso.setColegio(cmbColegios.getSelectionModel().getSelectedItem());


      R_TipoCurso tCurso = lstTipoCurso.stream()
          .filter(t -> t.getId().equals(evaluacion.getTipocurso_id())).findFirst().orElse(null);
      resumenCurso.setTipoCurso(tCurso);

      R_Curso curso = controller.findByIdSynchro(R_Curso.class, evaluacion.getCurso_id());
      if (curso == null)
        continue;
      resumenCurso.setName(curso.getName());
      Map<String, Object> parameters =
          MapBuilder.<String, Object>unordered().put("curso_id", curso.getId()).build();
      List<R_Alumno> lstAlumnos = controller.findByParamsSynchro(R_Alumno.class, parameters);

      int totalAlumnos = lstAlumnos.size();
      int totalEvaluados = 0;
      int totalAprobados = 0;
      int totalReprobados = 0;

      parameters = MapBuilder.<String, Object>unordered()
          .put("evaluacionprueba_id", evaluacion.getId()).build();
      List<R_PruebaRendida> rendidas =
          controller.findByParamsSynchro(R_PruebaRendida.class, parameters);

      // Estamos procesando un colegio/una prueba
      for (R_PruebaRendida pruebaRendida : rendidas) {
        R_Alumno alumno = lstAlumnos.stream()
            .filter(a -> a.getId().equals(pruebaRendida.getAlumno_id())).findFirst().orElse(null);
        if (alumno == null)
          continue;
        if (tipoAlumno != Constants.PIE_ALL && tipoAlumno != alumno.getTipoalumno_id()) {
          totalAlumnos--;
          continue;
        }
        totalEvaluados++;
        if (pruebaRendida.getNota() >= 4) {
          totalAprobados++;
        } else {
          totalReprobados++;
        }

        generaDatosRangos(evaluacion, pruebaRendida, nivel, curso);
        generaDatosGeneral(pruebaRendida);
      }

      resumenCurso.setTotalAlumnos(totalAlumnos);
      resumenCurso.setTotalEvaluados(totalEvaluados);
      resumenCurso.setTotalAprobados(totalAprobados);
      resumenCurso.setTotalReprobados(totalReprobados);
      resumenCurso.setAlumnosEvaluados(
          Utils.redondeo2Decimales((((float) totalEvaluados / (float) totalAlumnos) * 100f)));
      resumenCurso.setAlumnosAprobados(
          Utils.redondeo2Decimales((((float) totalAprobados / (float) totalEvaluados) * 100f)));
      resumenCurso.setAlumnosReprobados(
          Utils.redondeo2Decimales((((float) totalReprobados / (float) totalEvaluados) * 100f)));

      int idx = -1;
      if ((idx = lstCursos.indexOf(resumenCurso)) == -1) {
        lstCursos.add(resumenCurso);
      } else {
        OTResumenTipoCursoColegio rCurso = lstCursos.get(idx);
        rCurso.add(resumenCurso);
      }


      totalColAlumnos = totalColAlumnos + totalAlumnos;
      totalColEvaluados = totalColEvaluados + totalEvaluados;
      totalColAprobados = totalColAprobados + totalAprobados;
      totalColReprobados = totalColReprobados + totalReprobados;

    }

    Collections.sort(lstCursos, Comparadores.comparaResumeTipoCursoColegio());
    resumenTotal = new OTResumenTipoCursoColegio();
    R_TipoCurso curso = new R_TipoCurso.Builder().id(Long.MAX_VALUE).name("Total").build();
    resumenTotal.setTipoCurso(curso);
    resumenTotal.setTotalAlumnos(totalColAlumnos);
    resumenTotal.setTotalEvaluados(totalColEvaluados);
    resumenTotal.setTotalAprobados(totalColAprobados);
    resumenTotal.setTotalReprobados(totalColReprobados);
    resumenTotal.setAlumnosEvaluados(
        (Utils.redondeo2Decimales((float) totalColEvaluados / (float) totalColAlumnos) * 100f));
    resumenTotal.setAlumnosAprobados(
        Utils.redondeo2Decimales(((float) totalColAprobados / (float) totalColEvaluados) * 100f));
    resumenTotal.setAlumnosReprobados(
        Utils.redondeo2Decimales(((float) totalColReprobados / (float) totalColEvaluados) * 100f));
    lstCursos.add(resumenTotal);
    tblCursos.setItems(FXCollections.observableArrayList(lstCursos));

    generarDatosResumenPME();
    generarDatosResumenGeneral();
  }

  @SuppressWarnings("unchecked")
  private void generarDatosResumenGeneral() {
    ObservableList<Object> resumenGeneral = FXCollections.observableArrayList();

    ObservableList<Object> row = FXCollections.observableArrayList();
    row.add("Alumnos");
    row.add(Utils.redondeo2Decimales(resumenTotal.getTotalAlumnos()));
    row.add(Utils.redondeo2Decimales(resumenTotal.getTotalEvaluados()));
    row.add(Utils.redondeo2Decimales(resumenTotal.getTotalAprobados()));
    row.add(Utils.redondeo2Decimales(resumenTotal.getTotalReprobados()));

    ObservableList<Object> row1 = FXCollections.observableArrayList();
    row1.add("%");
    row1.add("100%");
    row1.add(Utils.redondeo2Decimales(resumenTotal.getAlumnosEvaluados()) + "%");
    row1.add(Utils.redondeo2Decimales(resumenTotal.getAlumnosAprobados()) + "%");
    row1.add(Utils.redondeo2Decimales(resumenTotal.getAlumnosReprobados()) + "%");

    ObservableList<Object> row2 = FXCollections.observableArrayList();
    row2.add("");
    row2.add("");
    row2.add("");
    row2.add((Utils.redondeo2Decimales(
        resumenTotal.getAlumnosAprobados() + resumenTotal.getAlumnosReprobados())) + "%");
    row2.add((Utils.redondeo2Decimales(
        resumenTotal.getAlumnosAprobados() + resumenTotal.getAlumnosReprobados())) + "%");

    float total = 0;
    for (Entry<Integer, R_EvaluacionEjetematico> ttEvaluacion : tituloEvaluacion.entrySet()) {
      Integer evaluacion = mResumen.get(ttEvaluacion.getValue());
      if (evaluacion != null) {
        row.add(Utils.redondeo2Decimales(evaluacion));
        float valor = ((float) evaluacion / (float) resumenTotal.getTotalEvaluados()) * 100f;
        row1.add(Utils.redondeo2Decimales(valor) + "%");
        total = total + valor;

      } else {
        row.add(0);
        row1.add(0);
      }
    }

    row2.add(Utils.redondeo2Decimales(total) + "%");
    row2.add(Utils.redondeo2Decimales(total) + "%");
    row2.add(Utils.redondeo2Decimales(total) + "%");

    resumenGeneral.add(row);
    resumenGeneral.add(row1);
    tblResumenTotal.setItems(resumenGeneral);

  }

  private void generaDatosGeneral(R_PruebaRendida pruebaRendida) {
    Float pBuenas = pruebaRendida.getBuenas().floatValue();
    for (Entry<Long, R_EvaluacionEjetematico> otResumenColegio : mEvaluaciones.entrySet()) {

      R_EvaluacionEjetematico otEvaluacion = otResumenColegio.getValue();
      if (otEvaluacion.isInside(pBuenas)) {
        if (mResumen.containsKey(otEvaluacion)) {
          Integer valor = mResumen.get(otEvaluacion);
          valor++;
          mResumen.put(otEvaluacion, valor);
        } else {
          mResumen.put(otEvaluacion, 1);
        }
      }
    }
  }



  @SuppressWarnings("unchecked")
  private void generarDatosResumenPME() {
    ObservableList<ObservableList<Object>> resumenPME = FXCollections.observableArrayList();

    Set<R_TipoCurso> listaCursos = pmeCursos.keySet();
    List<R_TipoCurso> cursos = new LinkedList<>();
    for (R_TipoCurso curso : listaCursos) {
      cursos.add(curso);
    }
    Collections.sort(cursos, Comparadores.comparaTipoCurso());

    for (R_TipoCurso lEntity : cursos) {
      ObservableList<Object> row = FXCollections.observableArrayList();

      row.add(lEntity.getName());

      Map<R_RangoEvaluacion, OTRangoTipoCurso> lista = pmeCursos.get(lEntity);
      for (Entry<Integer, R_RangoEvaluacion> otMapaRangos : mapaRangos.entrySet()) {
        OTRangoTipoCurso otRegistro = lista.get(otMapaRangos.getValue());
        if (otRegistro != null) {
          row.add(otRegistro.getTotal());
        } else {
          row.add(Integer.valueOf(0));
        }
      }

      resumenPME.add(row);
    }
    tblPME.setItems(resumenPME);
  }

  private void generaDatosRangos(R_EvaluacionPrueba evaluacion, R_PruebaRendida rendida, R_NivelEvaluacion nivelEvaluacion, R_Curso pCurso) {

    float porcentaje = (float) rendida.getBuenas() / (float) prueba.getNropreguntas() * 100f;
    
    R_RangoEvaluacion rango = EntityUtils.getRango(porcentaje, lstRangoEvaluacion);
    log.fine(String.format(";\"%s\";%f;%5.2f%%;\"%s\"", pCurso, rendida.getNota(),
        porcentaje, rango.getName()));

    
    R_TipoCurso tCurso = lstTipoCurso.stream().filter(t -> t.getId().equals(evaluacion.getTipocurso_id())).findFirst().orElse(null);
    if (pmeCursos.containsKey(tCurso)) {
      Map<R_RangoEvaluacion, OTRangoTipoCurso> prangos = pmeCursos.get(tCurso);
      if (prangos.containsKey(rango)) {
        OTRangoTipoCurso uRango = prangos.get(rango);
        uRango.setTotal(uRango.getTotal() + 1);

      } else {
        OTRangoTipoCurso rangoCurso = new OTRangoTipoCurso();
        rangoCurso.setCurso(tCurso);
        rangoCurso.setRango(rango);
        rangoCurso.setTotal(rangoCurso.getTotal() + 1);
        prangos.put(rango, rangoCurso);

      }

    } else {
      OTRangoTipoCurso rangoCurso = new OTRangoTipoCurso();
      rangoCurso.setCurso(tCurso);
      rangoCurso.setRango(rango);
      rangoCurso.setTotal(rangoCurso.getTotal() + 1);

      Map<R_RangoEvaluacion, OTRangoTipoCurso> pmeRangos = new HashMap<>();
      pmeRangos.put(rango, rangoCurso);
      pmeCursos.put(tCurso, pmeRangos);
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private void generarColumnasPME() {

    TableColumn columna0 = new TableColumn("Curso");
    columna0.setStyle("-fx-alignment: CENTER-LEFT;");
    columna0.setCellValueFactory(
        new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
          public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
            return new SimpleStringProperty(param.getValue().get(0).toString());
          }
        });
    columna0.setPrefWidth(ANCHO_COL);
    columna0.setCellFactory(param -> new TableCell() {
      @Override
      public void updateItem(Object item, boolean empty) {
        if (item != null) {
          setText(item.toString());
        }
      }
    });

    tblPME.getColumns().add(columna0);

    int indice = 1;
    for (Object rango : lstRangoEvaluacion) {

      R_RangoEvaluacion titulo = (R_RangoEvaluacion) rango;
      // Columnas
      final int col = indice;
      TableColumn columna = new TableColumn(titulo.getName());
      columna.setStyle(FX_ALIGNMENT_CENTER);
      columna.setCellValueFactory(
          new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
              return new SimpleStringProperty(param.getValue().get(col).toString());
            }
          });

      columna.setPrefWidth(ANCHO_COL);
      tblPME.getColumns().add(columna);
      mapaRangos.put(indice, titulo);
      indice++;
    }

  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private void agregarColumnasTbl() {
    TableColumn columna0 = new TableColumn("");
    columna0.setStyle("-fx-alignment: CENTER-LEFT;");
    columna0.setCellValueFactory(
        new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
          public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
            return new SimpleStringProperty(param.getValue().get(0).toString());
          }
        });
    columna0.setPrefWidth(ANCHO_COL);
    columna0.setCellFactory(new Callback<TableColumn, TableCell>() {

      public TableCell call(TableColumn param) {
        TableCell cell = new TableCell() {
          @Override
          public void updateItem(Object item, boolean empty) {
            if (item != null) {
              setText(item.toString());
            }
          }
        };
        return cell;
      }
    });
    tblResumenTotal.getColumns().add(columna0);

    TableColumn columna1 = new TableColumn("Total Escuela");
    columna1.setStyle(FX_ALIGNMENT_CENTER);
    columna1.setCellValueFactory(
        new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
          public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
            return new SimpleStringProperty(param.getValue().get(1).toString());
          }
        });
    columna1.setPrefWidth(ANCHO_COL);
    columna1.setCellFactory(new Callback<TableColumn, TableCell>() {

      public TableCell call(TableColumn param) {
        TableCell cell = new TableCell() {
          @Override
          public void updateItem(Object item, boolean empty) {
            if (item != null) {
              setText(item.toString());
            }
          }
        };
        return cell;
      }
    });
    tblResumenTotal.getColumns().add(columna1);

    TableColumn columna2 = new TableColumn("N° Alumnos");
    columna2.setStyle(FX_ALIGNMENT_CENTER);
    tblResumenTotal.getColumns().add(columna2);

    TableColumn columna3 = new TableColumn("Evaluados");
    columna3.setStyle(FX_ALIGNMENT_CENTER);
    columna3.setCellValueFactory(
        new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
          public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
            return new SimpleStringProperty(param.getValue().get(2).toString());
          }
        });
    columna3.setPrefWidth(ANCHO_COL);
    columna3.setCellFactory(new Callback<TableColumn, TableCell>() {

      public TableCell call(TableColumn param) {
        TableCell cell = new TableCell() {
          @Override
          public void updateItem(Object item, boolean empty) {
            if (item != null) {
              setText(item.toString());
            }
          }
        };
        return cell;
      }
    });
    columna2.getColumns().addAll(columna3);
    TableColumn columna7 = new TableColumn("Alumnos");
    columna7.setStyle(FX_ALIGNMENT_CENTER);
    tblResumenTotal.getColumns().add(columna7);

    TableColumn columna4 = new TableColumn("Aprobados");
    columna4.setStyle(FX_ALIGNMENT_CENTER);
    columna4.setCellValueFactory(
        new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
          public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
            return new SimpleStringProperty(param.getValue().get(3).toString());
          }
        });
    columna4.setPrefWidth(ANCHO_COL);
    columna4.setCellFactory(new Callback<TableColumn, TableCell>() {

      public TableCell call(TableColumn param) {
        TableCell cell = new TableCell() {
          @Override
          public void updateItem(Object item, boolean empty) {
            if (item != null) {
              setText(item.toString());
            }
          }
        };
        return cell;
      }
    });

    TableColumn columna5 = new TableColumn("Reprobados");
    columna5.setStyle(FX_ALIGNMENT_CENTER);
    columna5.setCellValueFactory(
        new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
          public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
            return new SimpleStringProperty(param.getValue().get(4).toString());
          }
        });
    columna5.setPrefWidth(ANCHO_COL);
    columna5.setCellFactory(new Callback<TableColumn, TableCell>() {

      public TableCell call(TableColumn param) {
        TableCell cell = new TableCell() {
          @Override
          public void updateItem(Object item, boolean empty) {
            if (item != null) {
              setText(item.toString());
            }
          }
        };
        return cell;
      }
    });

    columna7.getColumns().addAll(columna4, columna5);

    TableColumn columna6 = new TableColumn("");
    columna6.setStyle(FX_ALIGNMENT_CENTER);
    tblResumenTotal.getColumns().add(columna6);

    int indice = 5;
    for (Object rango : mEvaluaciones.values()) {

      R_EvaluacionEjetematico titulo = (R_EvaluacionEjetematico) rango;
      // Columnas
      final int column = indice;
      TableColumn columna = new TableColumn(titulo.getName());
      columna.setStyle(FX_ALIGNMENT_CENTER);
      columna.setCellValueFactory(
          new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
              return new SimpleStringProperty(param.getValue().get(column).toString());
            }
          });

      columna.setPrefWidth(ANCHO_COL);
      columna6.getColumns().add(columna);
      tituloEvaluacion.put(indice, titulo);
      indice++;
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

}
