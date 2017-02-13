package curso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class ResumenAlumnoView extends AFormView implements EventHandler<ActionEvent> {
  private static final String M2 = "M";
  private static final String MINUS = "-";
  private static final String B = "B";
  private static final String PLUS = "+";
  private static final String O = "O";
  @FXML
  private TextField txtPrueba;
  @FXML
  private TextField txtCurso;
  @FXML
  private TextField txtAsignatura;
  @FXML
  private MenuItem mnuExportarRespuestas;
  @FXML
  private MenuItem mnuExportarAlumnos;
  @FXML
  private TableView<OTPruebaRendida> tblAlumnos;
  @FXML
  private TableColumn<OTPruebaRendida, String> colARut;
  @FXML
  private TableColumn<OTPruebaRendida, String> colAPaterno;
  @FXML
  private TableColumn<OTPruebaRendida, String> colAMaterno;
  @FXML
  private TableColumn<OTPruebaRendida, String> colAName;
  @FXML
  private TableColumn<OTPruebaRendida, Integer> colABuenas;
  @FXML
  private TableColumn<OTPruebaRendida, Integer> colAMalas;
  @FXML
  private TableColumn<OTPruebaRendida, Integer> colAOmitidas;
  @FXML
  private TableView<ObservableList<String>> tblRespuestas;
  @FXML
  private ComboBox<R_TipoAlumno> cmbTipoAlumno;
  long tipoAlumno = Constants.PIE_ALL;
  @FXML
  private Button btnGenerar;
  private R_EvaluacionPrueba evaluacionPrueba;
  private List<R_PruebaRendida> lstPruebaRendida;
  private R_Curso curso;
  private List<R_Alumno> lstAlumnos;
  private R_Prueba prueba;

  public ResumenAlumnoView() {}

  @FXML
  public void initialize() {
    this.setTitle("Resumen de respuestas por alumno");
    inicializarTablaAlumnos();
    clicTablaRespuesta();
    clicTablaAlumnos();
    mnuExportarRespuestas.setOnAction(this);
    mnuExportarAlumnos.setOnAction(this);
    btnGenerar.setOnAction(this);
    cmbTipoAlumno.setOnAction(event -> {
      tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedIndex();
    });
  }

  private void clicTablaRespuesta() {
    tblAlumnos.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        tblRespuestas.getSelectionModel().clearSelection();
        tblRespuestas.getSelectionModel().select(tblAlumnos.getSelectionModel().getSelectedIndex());
      }
    });
  }

  private void clicTablaAlumnos() {
    tblRespuestas.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        tblAlumnos.getSelectionModel().clearSelection();
        tblAlumnos.getSelectionModel().select(tblRespuestas.getSelectionModel().getSelectedIndex());
      }
    });
  }

  private void inicializarTablaAlumnos() {
    tblAlumnos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    colARut.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, String>("rut"));
    colAName.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, String>("nombre"));
    colAPaterno.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, String>("paterno"));
    colAMaterno.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, String>("materno"));
    colABuenas.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, Integer>("buenas"));
    colAMalas.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, Integer>("malas"));
    colAOmitidas.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, Integer>("omitidas"));
  }

  @Override
  public void onFound(IEntity entity) {
    if (entity instanceof R_EvaluacionPrueba) {
      evaluacionPrueba = (R_EvaluacionPrueba) entity;

      prueba = controller.findByIdSynchro(R_Prueba.class, evaluacionPrueba.getPrueba_id());
      Map<String, Object> params =
          MapBuilder.<String, Object>unordered().put("evaluacionprueba_id", evaluacionPrueba.getId()).build();
      lstPruebaRendida = controller.findByParamsSynchro(R_PruebaRendida.class, params);
      curso = controller.findByIdSynchro(R_Curso.class, evaluacionPrueba.getCurso_id());
      params = MapBuilder.<String, Object>unordered().put("curso_id", evaluacionPrueba.getCurso_id()).build();
      lstAlumnos = controller.findByParamsSynchro(R_Alumno.class, params);
      generateReport();
    }
  }

  @Override
  public void onDataArrived(List<Object> list) {
    if (list != null && !list.isEmpty()) {
      Object entity = list.get(0);
      if (entity instanceof R_TipoAlumno) {
        ObservableList<R_TipoAlumno> tAlumnoList = FXCollections.observableArrayList();
        for (Object iEntity : list) {
          tAlumnoList.add((R_TipoAlumno) iEntity);
        }
        cmbTipoAlumno.setItems(tAlumnoList);
        cmbTipoAlumno.getSelectionModel().select(0);
        generateReport();
      }
    }
  }

  @Override
  public void handle(ActionEvent event) {
    Object source = event.getSource();
    if (source == mnuExportarRespuestas || source == mnuExportarAlumnos) {
      tblAlumnos.setId("Alumnos");
      tblRespuestas.setId("Respuestas");
      List<TableView<? extends Object>> listaTablas = new LinkedList<>();
      listaTablas.add((TableView<? extends Object>) tblAlumnos);
      listaTablas.add((TableView<? extends Object>) tblRespuestas);
      ExcelSheetWriterObj.convertirDatosALibroDeExcel(listaTablas);
    } else if (source == btnGenerar) {
      generateReport();
    }
  }

  private void generateReport() {
    if (evaluacionPrueba != null && cmbTipoAlumno.getItems() != null && !cmbTipoAlumno.getItems().isEmpty() && lstAlumnos != null) {
      tblRespuestas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
      ObservableList<OTPruebaRendida> oList = FXCollections.observableArrayList();
      if (lstPruebaRendida != null && !lstPruebaRendida.isEmpty()) {
        for (R_PruebaRendida pr : lstPruebaRendida) {
          if (tipoAlumno == Constants.PIE_ALL || pr.getTipoalumno_id().equals(tipoAlumno)) {
            R_Alumno al = lstAlumnos.stream().filter(a -> a.getId().equals(pr.getAlumno_id())).findFirst().orElse(null);
            OTPruebaRendida ot  =  new OTPruebaRendida(pr, al);
            oList.add(ot);
          }
        }
        tblAlumnos.setItems(oList);
      }
      if (curso != null) {
        txtCurso.setText(curso.getName());
      }
      txtPrueba.setText(prueba.getName());
      R_Asignatura asignatura = controller.findByIdSynchro(R_Asignatura.class, prueba.getAsignatura_id());
      if (asignatura != null) {
        txtAsignatura.setText(asignatura.getName());
      }
      crearTabla(evaluacionPrueba);
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private void crearTabla(R_EvaluacionPrueba eval) {
    List<String> columns = new ArrayList<String>();
    columns.add("Rut");
    columns.add("Nombre");
    columns.add("Paterno");
    columns.add("Materno");
    for (int n = 0; n < prueba.getNropreguntas(); n++) {
      columns.add(String.valueOf(n + 1));
    }
    TableColumn[] tableColumns = new TableColumn[columns.size()];
    int columnIndex = 0;
    for (String columName : columns) {
      tableColumns[columnIndex] = new TableColumn(columName);
      final int idx = columnIndex;
      tableColumns[columnIndex]
          .setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
              return new SimpleStringProperty(param.getValue().get(idx).toString());
            }
          });
      tableColumns[columnIndex].setCellFactory(new Callback<TableColumn, TableCell>() {
        public TableCell call(TableColumn param) {
          TableCell cell = new TableCell() {
            @Override
            public void updateItem(Object item, boolean empty) {
              if (item != null) {
                setText(item.toString());
              }
            }
          };
          // cell.setAlignment(Pos.CENTER);
          return cell;
        }
      });
      if (columnIndex < 4) {
        tableColumns[columnIndex].setMaxWidth(100);
        tableColumns[columnIndex].setMinWidth(100);
        tableColumns[columnIndex].setSortable(true);
      } else {
        tableColumns[columnIndex].setMaxWidth(20);
        tableColumns[columnIndex].setMinWidth(20);
        tableColumns[columnIndex].setSortable(false);
      }
      tableColumns[columnIndex].setResizable(false);
      columnIndex++;
    }
    tblRespuestas.getColumns().setAll(tableColumns);
    ObservableList<ObservableList<String>> csvData = FXCollections.observableArrayList();
    for (R_PruebaRendida pr : lstPruebaRendida) {
      if (tipoAlumno != Constants.PIE_ALL && !pr.getTipoalumno_id().equals(tipoAlumno)) {
        continue;
      }
      ObservableList<String> row = FXCollections.observableArrayList();
      R_Alumno alumno = lstAlumnos.stream().filter(a -> a.getId().equals(pr.getAlumno_id())).findFirst().orElse(null);
      if (alumno == null)
        continue;
      row.add(alumno.getRut());
      row.add(alumno.getPaterno());
      row.add(alumno.getMaterno());
      row.add(alumno.getName());
      String resps = pr.getRespuestas().toUpperCase();
      for (int m = 0; m < prueba.getNropreguntas(); m++) {
        String rP = resps.substring(m, m + 1);
        if (ResumenAlumnoView.O.equals(rP)) {
          rP = ResumenAlumnoView.O;
        } else if (ResumenAlumnoView.PLUS.equals(rP)) {
          rP = ResumenAlumnoView.B;
        } else if (ResumenAlumnoView.MINUS.equals(rP)) {
          rP = ResumenAlumnoView.M2;
        }
        row.add(rP);
      }
      csvData.add(row);
    }
    tblRespuestas.getItems().setAll(csvData);
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
