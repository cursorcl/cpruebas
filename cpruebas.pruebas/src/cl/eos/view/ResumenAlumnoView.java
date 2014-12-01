package cl.eos.view;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.util.ExcelSheetWriterObj;

public class ResumenAlumnoView extends AFormView implements EventHandler<ActionEvent> {
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
  private TableView<PruebaRendida> tblAlumnos;
  @FXML
  private TableColumn<PruebaRendida, String> colARut;
  @FXML
  private TableColumn<PruebaRendida, String> colAPaterno;
  @FXML
  private TableColumn<PruebaRendida, String> colAMaterno;
  @FXML
  private TableColumn<PruebaRendida, String> colAName;
  @FXML
  private TableColumn<PruebaRendida, Integer> colABuenas;
  @FXML
  private TableColumn<PruebaRendida, Integer> colAMalas;
  @FXML
  private TableColumn<PruebaRendida, Integer> colAOmitidas;

  @FXML
  private TableView<ObservableList<String>> tblRespuestas;

  // @FXML
  // private TableView<PruebaRendida> tblRespuestas;
  // @FXML
  // private TableColumn<PruebaRendida, String> colRRut;
  // @FXML
  // private TableColumn<PruebaRendida, String> colRPaterno;
  // @FXML
  // private TableColumn<PruebaRendida, String> colRMaterno;
  // @FXML
  // private TableColumn<PruebaRendida, String> colRName;

  public ResumenAlumnoView() {
    // TODO Auto-generated constructor stub
  }

  @FXML
  public void initialize() {
    this.setTitle("Resumen de respuestas por alumno");
    inicializarTablaAlumnos();
    clicTablaRespuesta();
    clicTablaAlumnos();
    mnuExportarRespuestas.setOnAction(this);
    mnuExportarAlumnos.setOnAction(this);
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
    colARut.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>("rut"));
    colAName.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>("nombre"));
    colAPaterno.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>("paterno"));
    colAMaterno.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>("materno"));
    colABuenas.setCellValueFactory(new PropertyValueFactory<PruebaRendida, Integer>("buenas"));
    colAMalas.setCellValueFactory(new PropertyValueFactory<PruebaRendida, Integer>("malas"));
    colAOmitidas.setCellValueFactory(new PropertyValueFactory<PruebaRendida, Integer>("omitidas"));

  }

  private void inicializarTablaRespuesta() {

    tblRespuestas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    // colRRut.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>("rut"));
    // colRName.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>("nombre"));
    // colRPaterno.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>("paterno"));
    // colRMaterno.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>("materno"));
  }

  @Override
  public void onFound(IEntity entity) {
    if (entity instanceof EvaluacionPrueba) {
      EvaluacionPrueba evaluacionPrueba = (EvaluacionPrueba) entity;

      int nroPreguntas = evaluacionPrueba.getNroPreguntas();
      final String responses = evaluacionPrueba.getResponses();

      inicializarTablaRespuesta();
      // for (int indice = 1; indice <= nroPreguntas; indice++) {
      // TableColumn<PruebaRendida, String> nro =
      // new TableColumn<PruebaRendida, String>(String.valueOf(indice));
      // nro.setPrefWidth(20);
      // nro.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>("respuestas"));
      //
      //
      //
      // nro.setCellFactory(new Callback<TableColumn<PruebaRendida, String>,
      // TableCell<PruebaRendida, String>>() {
      //
      // @Override
      // public TableCell<PruebaRendida, String> call(TableColumn<PruebaRendida, String> param) {
      // LetraRespuesta letraRespuesta = new LetraRespuesta();
      // letraRespuesta.setResponses(responses);
      // return letraRespuesta;
      // }
      // });
      // tblRespuestas.getColumns().add(nro);
      // }

      List<PruebaRendida> list = evaluacionPrueba.getPruebasRendidas();
      if (list != null && !list.isEmpty()) {
        ObservableList<PruebaRendida> oList = FXCollections.observableArrayList(list);
        tblAlumnos.setItems(oList);
        // tblRespuestas.setItems(oList);
      }

      if (evaluacionPrueba.getCurso() != null) {
        txtCurso.setText(evaluacionPrueba.getCurso().getName());
      }

      if (evaluacionPrueba.getPrueba() != null) {
        txtPrueba.setText(evaluacionPrueba.getPrueba().getName());
        if (evaluacionPrueba.getPrueba().getAsignatura() != null) {
          txtAsignatura.setText(evaluacionPrueba.getPrueba().getAsignatura().getName());
        }
      }
      crearTabla(evaluacionPrueba);

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
    }
  }

  private void crearTabla(EvaluacionPrueba eval) {

    String responses = eval.getPrueba().getResponses().toUpperCase();
    List<String> columns = new ArrayList<String>();

    columns.add("Rut");
    columns.add("Nombre");
    columns.add("Paterno");
    columns.add("Materno");
    for (int n = 0; n < eval.getNroPreguntas(); n++) {
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

    for (PruebaRendida pr : eval.getPruebasRendidas()) {
      ObservableList<String> row = FXCollections.observableArrayList();
      row.add(pr.getRut());
      row.add(pr.getPaterno());
      row.add(pr.getMaterno());
      row.add(pr.getNombre());

      String resps = pr.getRespuestas().toUpperCase();
      for (int m = 0; m < eval.getNroPreguntas(); m++) {

        String rC = responses.substring(m, m + 1);
        String rP = resps.substring(m, m + 1);


        if ("O".equals(rP)) {
          rP = "O";
        } else if ("+".equals(rP)) {
          rP = "B";
        } else if ("-".equals(rP)) {
          rP = "M";
        }
        row.add(rP);
      }
      csvData.add(row);
    }

    tblRespuestas.getItems().setAll(csvData);

  }
}
