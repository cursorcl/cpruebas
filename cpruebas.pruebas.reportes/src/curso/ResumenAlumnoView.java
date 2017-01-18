package curso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.provider.persistence.PersistenceServiceFactory;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.MapBuilder;
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
    @FXML private TextField txtPrueba;
    @FXML private TextField txtCurso;
    @FXML private TextField txtAsignatura;
    @FXML private MenuItem mnuExportarRespuestas;
    @FXML private MenuItem mnuExportarAlumnos;
    @FXML private TableView<R_PruebaRendida> tblAlumnos;
    @FXML private TableColumn<R_PruebaRendida, String> colARut;
    @FXML private TableColumn<R_PruebaRendida, String> colAPaterno;
    @FXML private TableColumn<R_PruebaRendida, String> colAMaterno;
    @FXML private TableColumn<R_PruebaRendida, String> colAName;
    @FXML private TableColumn<R_PruebaRendida, Integer> colABuenas;
    @FXML private TableColumn<R_PruebaRendida, Integer> colAMalas;
    @FXML private TableColumn<R_PruebaRendida, Integer> colAOmitidas;
    @FXML private TableView<ObservableList<String>> tblRespuestas;
    @FXML private ComboBox<R_TipoAlumno> cmbTipoAlumno;
    long tipoAlumno = Constants.PIE_ALL;
    @FXML private Button btnGenerar;
    private R_EvaluacionPrueba evaluacionPrueba;
    private List<R_PruebaRendida> list;
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
        colARut.setCellValueFactory(new PropertyValueFactory<R_PruebaRendida, String>("rut"));
        colAName.setCellValueFactory(new PropertyValueFactory<R_PruebaRendida, String>("nombre"));
        colAPaterno.setCellValueFactory(new PropertyValueFactory<R_PruebaRendida, String>("paterno"));
        colAMaterno.setCellValueFactory(new PropertyValueFactory<R_PruebaRendida, String>("materno"));
        colABuenas.setCellValueFactory(new PropertyValueFactory<R_PruebaRendida, Integer>("buenas"));
        colAMalas.setCellValueFactory(new PropertyValueFactory<R_PruebaRendida, Integer>("malas"));
        colAOmitidas.setCellValueFactory(new PropertyValueFactory<R_PruebaRendida, Integer>("omitidas"));
    }
    @Override
    public void onFound(IEntity entity) {
        if (entity instanceof R_EvaluacionPrueba) {
            evaluacionPrueba = (R_EvaluacionPrueba) entity;
            
            Map<String, Object> params = MapBuilder.<String, Object> unordered().put("evaluacionprueba_id", evaluacionPrueba.getId()).build();
            list = PersistenceServiceFactory.getPersistenceService().findByParamsSynchor(R_PruebaRendida.class, params);
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
        if (evaluacionPrueba != null && cmbTipoAlumno.getItems() != null && !cmbTipoAlumno.getItems().isEmpty()) {
            tblRespuestas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            ObservableList<R_PruebaRendida> oList = FXCollections.observableArrayList();
            if (list != null && !list.isEmpty()) {
                for (R_PruebaRendida pr : list) {
                    if (tipoAlumno == Constants.PIE_ALL || pr.getAlumno().getTipoAlumno().getId().equals(tipoAlumno)) {
                        oList.add(pr);
                    }
                }
                tblAlumnos.setItems(oList);
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
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void crearTabla(R_EvaluacionPrueba eval) {
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
            tableColumns[columnIndex].setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
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
        for (R_PruebaRendida pr : eval.getPruebasRendidas()) {
            if (tipoAlumno != Constants.PIE_ALL && !pr.getAlumno().getTipoAlumno().getId().equals(tipoAlumno)) {
                continue;
            }
            ObservableList<String> row = FXCollections.observableArrayList();
            row.add(pr.getRut());
            row.add(pr.getPaterno());
            row.add(pr.getMaterno());
            row.add(pr.getNombre());
            String resps = pr.getRespuestas().toUpperCase();
            for (int m = 0; m < eval.getNroPreguntas(); m++) {
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
}
