package colegio;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

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
import cl.eos.imp.view.AFormView;
import cl.eos.ot.OTRangoCurso;
import cl.eos.ot.OTResumenColegio;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.EvaluacionEjeTematico;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.NivelEvaluacion;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RangoEvaluacion;
import cl.eos.persistence.util.Comparadores;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.Utils;

public class ResumenColegioView extends AFormView implements EventHandler<ActionEvent> {

  private static final String ASIGNATURA_ID = "idAsignatura";

  private static Logger log = Logger.getLogger(ResumenColegioView.class.getName());
  
  private static final String COLEGIO_ID = "idColegio";
  @SuppressWarnings("rawtypes")
  @FXML
  private TableView tblPME;
  @FXML
  private TableView<OTResumenColegio> tblCursos;
  @FXML
  private TableColumn<OTResumenColegio, String> colCurso;
  @FXML
  private TableColumn<OTResumenColegio, Integer> colTotal;
  @FXML
  private TableColumn<OTResumenColegio, Integer> colEvaluados;
  @FXML
  private TableColumn<OTResumenColegio, Integer> colAprobados;
  @FXML
  private TableColumn<OTResumenColegio, Integer> colReprobados;
  @FXML
  private TableColumn<OTResumenColegio, Float> colPEvaluados;
  @FXML
  private TableColumn<OTResumenColegio, Float> colPAprobados;
  @FXML
  private TableColumn<OTResumenColegio, Float> colPReprobados;

  @SuppressWarnings("rawtypes")
  @FXML
  private TableView tblResumenTotal;
  @FXML
  private ComboBox<Colegio> cmbColegios;
  @FXML
  private ComboBox<Asignatura> cmbAsignatura;
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

  Map<String, Object> parameters = new HashMap<String, Object>();
  private Map<Long, OTResumenColegio> mapaCursos = new HashMap<Long, OTResumenColegio>();

  private List<OTResumenColegio> lstCursos = new LinkedList<OTResumenColegio>();

  private ObservableList<RangoEvaluacion> oList;

  public ResumenColegioView() {
    setTitle("Resumen Colegio");
  }

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
    Colegio colegio = cmbColegios.getSelectionModel().getSelectedItem();
    if (colegio != null) {
      parameters.put(COLEGIO_ID, colegio.getId());
    }
  }

  private void handleAsignatura() {
    Asignatura asignatura = cmbAsignatura.getSelectionModel().getSelectedItem();
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
      controller.find("EvaluacionPrueba.findEvaluacionByColegioAsig", parameters, this);
    }
  }

  @FXML
  public void initialize() {
    inicializarTablaCursos();
    inicializaComponentes();
  }

  private void inicializarTablaCursos() {
    tblCursos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    colCurso.setCellValueFactory(new PropertyValueFactory<OTResumenColegio, String>("curso"));
    colTotal
        .setCellValueFactory(new PropertyValueFactory<OTResumenColegio, Integer>("totalAlumnos"));
    colEvaluados.setCellValueFactory(new PropertyValueFactory<OTResumenColegio, Integer>(
        "totalEvaluados"));
    colAprobados.setCellValueFactory(new PropertyValueFactory<OTResumenColegio, Integer>(
        "totalAprobados"));
    colReprobados.setCellValueFactory(new PropertyValueFactory<OTResumenColegio, Integer>(
        "totalReprobados"));
    colPEvaluados.setCellValueFactory(new PropertyValueFactory<OTResumenColegio, Float>(
        "alumnosEvaluados"));
    colPAprobados.setCellValueFactory(new PropertyValueFactory<OTResumenColegio, Float>(
        "alumnosAprobados"));
    colPReprobados.setCellValueFactory(new PropertyValueFactory<OTResumenColegio, Float>(
        "alumnosReprobados"));

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
      if (entity instanceof Colegio) {
        ObservableList<Colegio> oList = FXCollections.observableArrayList();
        for (Object iEntity : list) {
          oList.add((Colegio) iEntity);
        }
        cmbColegios.setItems(oList);
      }
      if (entity instanceof Asignatura) {
        ObservableList<Asignatura> oList = FXCollections.observableArrayList();
        for (Object iEntity : list) {
          oList.add((Asignatura) iEntity);
        }
        cmbAsignatura.setItems(oList);
      }
      if (entity instanceof RangoEvaluacion) {
        oList = FXCollections.observableArrayList();
        for (Object iEntity : list) {
          oList.add((RangoEvaluacion) iEntity);
        }
        if (oList.size() > 0) {
          generarColumnasPME();
        }
      }
      if (entity instanceof EvaluacionPrueba) {
        generarReporteCursos(list);
      }
      if (entity instanceof EvaluacionEjeTematico) {
        for (Object object : list) {
          EvaluacionEjeTematico evaluacion = (EvaluacionEjeTematico) object;
          mEvaluaciones.put(evaluacion.getId(), evaluacion);

        }
        if (mEvaluaciones != null) {
          agregarColumnasTbl();
        }
      }

    }
  }

  private Map<Integer, EvaluacionEjeTematico> tituloEvaluacion =
      new HashMap<Integer, EvaluacionEjeTematico>();

  private Map<Long, EvaluacionEjeTematico> mEvaluaciones =
      new HashMap<Long, EvaluacionEjeTematico>();

  private Map<Curso, Map<RangoEvaluacion, OTRangoCurso>> pmeCursos =
      new HashMap<Curso, Map<RangoEvaluacion, OTRangoCurso>>();

  private OTResumenColegio resumenTotal;

  private void generarReporteCursos(List<Object> list) {
    EvaluacionPrueba firstEvaluacion = (EvaluacionPrueba) list.get(0);
    StringBuffer string = new StringBuffer();
    string.append(firstEvaluacion.getColegio());
    lblColegio.setText(string.toString());

    NivelEvaluacion nivel = firstEvaluacion.getPrueba().getNivelEvaluacion();
    int totalColAlumnos = 0;
    int totalColEvaluados = 0;
    int totalColAprobados = 0;
    int totalColReprobados = 0;

    // Todas las evaluaciones asociadas (Todos los cursos)
    for (Object evaluacionPrueba : list) {
      EvaluacionPrueba evaluacion = (EvaluacionPrueba) evaluacionPrueba;
      OTResumenColegio resumenCurso = new OTResumenColegio();
      resumenCurso.setColegio(evaluacion.getColegio());
      resumenCurso.setCurso(evaluacion.getCurso());

      int totalAlumnos = evaluacion.getCurso().getAlumnos().size();
      int totalEvaluados = 0;
      int totalAprobados = 0;
      int totalReprobados = 0;

      List<PruebaRendida> rendidas = evaluacion.getPruebasRendidas();
      // Estamos procesando un curso/una prueba
      for (PruebaRendida pruebaRendida : rendidas) {
        totalEvaluados++;
        if (pruebaRendida.getNota() >= 4) {
          totalAprobados++;
        } else {
          totalReprobados++;
        }

        generaDatosRangos(pruebaRendida, nivel);
        generaDatosGeneral(pruebaRendida);
      }

      resumenCurso.setTotalAlumnos(totalAlumnos);
      resumenCurso.setTotalEvaluados(totalEvaluados);
      resumenCurso.setTotalAprobados(totalAprobados);
      resumenCurso.setTotalReprobados(totalReprobados);
      resumenCurso.setAlumnosEvaluados(Utils
          .redondeo2Decimales((((float) totalEvaluados / (float) totalAlumnos) * 100f)));
      resumenCurso.setAlumnosAprobados(Utils
          .redondeo2Decimales((((float) totalAprobados / (float) totalEvaluados) * 100f)));
      resumenCurso.setAlumnosReprobados(Utils
          .redondeo2Decimales((((float) totalReprobados / (float) totalEvaluados) * 100f)));
      lstCursos.add(resumenCurso);

      totalColAlumnos = totalColAlumnos + totalAlumnos;
      totalColEvaluados = totalColEvaluados + totalEvaluados;
      totalColAprobados = totalColAprobados + totalAprobados;
      totalColReprobados = totalColReprobados + totalReprobados;

    }

    resumenTotal = new OTResumenColegio();
    Curso curso = new Curso();
    curso.setId(Long.MAX_VALUE);
    curso.setName("Total");
    resumenTotal.setCurso(curso);
    resumenTotal.setTotalAlumnos(totalColAlumnos);
    resumenTotal.setTotalEvaluados(totalColEvaluados);
    resumenTotal.setTotalAprobados(totalColAprobados);
    resumenTotal.setTotalReprobados(totalColReprobados);
    resumenTotal.setAlumnosEvaluados((Utils.redondeo2Decimales((float) totalColEvaluados
        / (float) totalColAlumnos) * 100f));
    resumenTotal.setAlumnosAprobados(Utils
        .redondeo2Decimales(((float) totalColAprobados / (float) totalColEvaluados) * 100f));
    resumenTotal.setAlumnosReprobados(Utils
        .redondeo2Decimales(((float) totalColReprobados / (float) totalColEvaluados) * 100f));
    lstCursos.add(resumenTotal);

    generarDatosResumenCurso();
    generarDatosResumenPME();
    generarDatosResumenGeneral();
  }

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
    row2.add((Utils.redondeo2Decimales(resumenTotal.getAlumnosAprobados()
        + resumenTotal.getAlumnosReprobados()))
        + "%");
    row2.add((Utils.redondeo2Decimales(resumenTotal.getAlumnosAprobados()
        + resumenTotal.getAlumnosReprobados()))
        + "%");

    float total = 0;
    for (Entry<Integer, EvaluacionEjeTematico> ttEvaluacion : tituloEvaluacion.entrySet()) {
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

  private Map<EvaluacionEjeTematico, Integer> mResumen =
      new HashMap<EvaluacionEjeTematico, Integer>();

  private void generaDatosGeneral(PruebaRendida pruebaRendida) {
    // Integer pBuenas = pruebaRendida.getBuenas();
    Float pBuenas = pruebaRendida.getPbuenas();
    for (Entry<Long, EvaluacionEjeTematico> otResumenColegio : mEvaluaciones.entrySet()) {

      EvaluacionEjeTematico otEvaluacion = otResumenColegio.getValue();
      if (otEvaluacion.isInside(pBuenas)) {
        // if (pBuenas >= otEvaluacion.getNroRangoMin()
        // && pBuenas <= otEvaluacion.getNroRangoMax()) {
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

  private void generarDatosResumenCurso() {
    Collections.sort(lstCursos, Comparadores.comparaResumeColegio());
    ObservableList<OTResumenColegio> cursos = FXCollections.observableArrayList();
    for (OTResumenColegio lEntity : lstCursos) {
      cursos.add((OTResumenColegio) lEntity);
    }
    tblCursos.setItems(cursos);
  }

  private void generarDatosResumenPME() {
    ObservableList<ObservableList<Object>> resumenPME = FXCollections.observableArrayList();

    Set<Curso> listaCursos = pmeCursos.keySet();
    List<Curso> cursos = new LinkedList<Curso>();
    for (Curso curso : listaCursos) {
      cursos.add(curso);
    }
    Collections.sort(cursos, Comparadores.comparaResumeCurso());

    // for (Entry<Curso, Map<RangoEvaluacion, OTRangoCurso>> lEntity :
    // pmeCursos
    // .entrySet()) {

    for (Curso lEntity : cursos) {
      ObservableList<Object> row = FXCollections.observableArrayList();

      row.add(lEntity.getName());

      Map<RangoEvaluacion, OTRangoCurso> lista = pmeCursos.get(lEntity);
      for (Entry<Integer, RangoEvaluacion> otMapaRangos : mapaRangos.entrySet()) {
        OTRangoCurso otRegistro = lista.get(otMapaRangos.getValue());
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

  private void generaDatosRangos(PruebaRendida rendida, NivelEvaluacion nivelEvaluacion) {

    
//    float porcentaje = Utils.getPorcenta(rendida.getNota());
    float porcentaje = (float)rendida.getBuenas() / (float)rendida.getEvaluacionPrueba().getPrueba().getNroPreguntas() * 100f;
    RangoEvaluacion rango = nivelEvaluacion.getRango(porcentaje);
    
    log.info(String.format(";\"%s\";%f;%5.2f%%;\"%s\"", rendida.getCurso(), rendida.getNota(), porcentaje, rango.getName()));

    Curso curso = rendida.getEvaluacionPrueba().getCurso();
    if (pmeCursos.containsKey(curso)) {
      Map<RangoEvaluacion, OTRangoCurso> prangos = pmeCursos.get(curso);
      if (prangos.containsKey(rango)) {
        OTRangoCurso uRango = prangos.get(rango);
        uRango.setTotal(uRango.getTotal() + 1);

      } else {
        OTRangoCurso rangoCurso = new OTRangoCurso();
        rangoCurso.setCurso(curso);
        rangoCurso.setRango(rango);
        rangoCurso.setTotal(rangoCurso.getTotal() + 1);
        prangos.put(rango, rangoCurso);

      }

    } else {
      OTRangoCurso rangoCurso = new OTRangoCurso();
      rangoCurso.setCurso(curso);
      rangoCurso.setRango(rango);
      rangoCurso.setTotal(rangoCurso.getTotal() + 1);

      Map<RangoEvaluacion, OTRangoCurso> pmeRangos = new HashMap<RangoEvaluacion, OTRangoCurso>();
      pmeRangos.put(rango, rangoCurso);
      pmeCursos.put(curso, pmeRangos);
    }
  }

  private Map<Integer, RangoEvaluacion> mapaRangos = new HashMap<Integer, RangoEvaluacion>();
  private static int ANCHO_COL = 83;

  @SuppressWarnings({"rawtypes", "unchecked"})
  private void generarColumnasPME() {

    TableColumn columna0 = new TableColumn("Curso");
    columna0.setStyle("-fx-alignment: CENTER-LEFT;");
    columna0
        .setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
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

    tblPME.getColumns().add(columna0);

    int indice = 1;
    for (Object rango : oList) {

      RangoEvaluacion titulo = (RangoEvaluacion) rango;
      // Columnas
      final int col = indice;
      TableColumn columna = new TableColumn(titulo.getName());
      columna.setStyle("-fx-alignment: CENTER;");
      columna
          .setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
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
    columna0
        .setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
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
    columna1.setStyle("-fx-alignment: CENTER;");
    columna1
        .setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
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
    columna2.setStyle("-fx-alignment: CENTER;");
    tblResumenTotal.getColumns().add(columna2);

    TableColumn columna3 = new TableColumn("Evaluados");
    columna3.setStyle("-fx-alignment: CENTER;");
    columna3
        .setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
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
    columna7.setStyle("-fx-alignment: CENTER;");
    tblResumenTotal.getColumns().add(columna7);

    TableColumn columna4 = new TableColumn("Aprobados");
    columna4.setStyle("-fx-alignment: CENTER;");
    columna4
        .setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
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
    columna5.setStyle("-fx-alignment: CENTER;");
    columna5
        .setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
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
    columna6.setStyle("-fx-alignment: CENTER;");
    tblResumenTotal.getColumns().add(columna6);

    int indice = 5;
    for (Object rango : mEvaluaciones.values()) {

      EvaluacionEjeTematico titulo = (EvaluacionEjeTematico) rango;
      // Columnas
      final int column = indice;
      TableColumn columna = new TableColumn(titulo.getName());
      columna.setStyle("-fx-alignment: CENTER;");
      columna
          .setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
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

}