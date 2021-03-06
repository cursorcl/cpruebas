package colegio.nivel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Objetivo;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.TipoAlumno;
import cl.eos.persistence.models.TipoCurso;
import cl.eos.util.Pair;
import javafx.beans.property.ReadOnlyFloatWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;
import ot.XItemObjetivo;
import ot.XItemTablaObjetivo;
import ot.XUtilReportBuilder;

/**
 * Obtiene los objetivos de un curso
 *
 * @author curso
 *
 */
public class Nivel_PorObjetivosColegioView extends AFormView {

    private static final String TITLE_FORMAT = "Porcentaje de logro por objetivo colegio %s / asignatura %s";
    private static final String TITLE = "Porcentaje de logro por objetivo";
    private static final String ASIGNATURA_ID = "idAsignatura";
    private static final String COLEGIO_ID = "idColegio";
    @FXML
    private TableView<XItemTablaObjetivo> tblObjetivos;
    @FXML
    private TableColumn<XItemTablaObjetivo, Objetivo> colObjetivos;

    @FXML
    private Button btnGenerarReporte;
    @FXML
    private ComboBox<TipoAlumno> cmbTipoAlumno;
    @FXML
    private ComboBox<Colegio> cmbColegio;
    @FXML
    private ComboBox<Asignatura> cmbAsignaturas;

    
    private Map<String, Object> parameters = new HashMap<String, Object>();

    long tipoAlumno = Constants.PIE_ALL;
    private ObservableList<EvaluacionPrueba> evaluacionesPrueba;

    protected void clearContent() {
        if (tblObjetivos.getItems() != null)
            tblObjetivos.getItems().clear();
        if (tblObjetivos.getColumns() != null) {
            for (int n = 3; n < tblObjetivos.getColumns().size(); n++)
                tblObjetivos.getColumns().remove(n);
        }
    }

    /**
     * Generar el reporte.
     */
    private void generateReport() {
        
        if (cmbAsignaturas.getItems() != null && cmbTipoAlumno.getItems() != null && cmbTipoAlumno.getSelectionModel().getSelectedItem() != null) {
            long tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedItem().getId();
            final List<PruebaRendida> pRendidas = new ArrayList<>();
            for(EvaluacionPrueba evaluacionPrueba : evaluacionesPrueba)
            {
                pRendidas.addAll(evaluacionPrueba.getPruebasRendidas());
            }

            if (pRendidas != null && !pRendidas.isEmpty()) {
                final Pair<List<TipoCurso>, List<XItemTablaObjetivo>> reporte = XUtilReportBuilder.reporteColegioxNivel(pRendidas, tipoAlumno);

                List<TipoCurso> cursos = reporte.getFirst();
                
                final ObservableList<XItemTablaObjetivo> itemsTable = FXCollections.observableList(reporte.getSecond());
                final Optional<XItemTablaObjetivo> opFirst = itemsTable.stream().findFirst();
                if (!opFirst.isPresent())
                    return;

                while (tblObjetivos.getColumns().size() > 3)
                    tblObjetivos.getColumns().remove(tblObjetivos.getColumns().size() - 1);
                
                
                for (int n = 0; n < cursos.size(); n++) {
                    final int idx = n;
                    TableColumn<XItemTablaObjetivo, String> headerColumn = new TableColumn<>(cursos.get(n).getName());
                    headerColumn.setStyle("-fx-font-size:10;-fx-alignment: CENTER;");

                    final TableColumn<XItemTablaObjetivo, String> columnEjes = new TableColumn<>("Ejes Asociados");
                    columnEjes.setStyle("-fx-font-size:10;-fx-alignment: CENTER-LEFT;");
                    columnEjes.setCellValueFactory(c -> {
                        if(c == null || c.getValue() == null || c.getValue().getItems() == null || c.getValue().getItems().get(idx) == null)
                            return new ReadOnlyStringWrapper("");
                        final List<XItemObjetivo> lItems = c.getValue().getItems();
                        return new ReadOnlyStringWrapper(lItems.get(idx).getEjesAsociados());
                    });
                    columnEjes.setCellFactory(
                            new Callback<TableColumn<XItemTablaObjetivo, String>, TableCell<XItemTablaObjetivo, String>>() {

                                @Override
                                public TableCell<XItemTablaObjetivo, String> call(
                                        TableColumn<XItemTablaObjetivo, String> param) {
                                    TableCell<XItemTablaObjetivo, String> cell = new TableCell<>();
                                    Text text = new Text();
                                    cell.setGraphic(text);
                                    cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
                                    text.wrappingWidthProperty().bind(cell.widthProperty());
                                    text.textProperty().bind(cell.itemProperty());
                                    return cell;
                                }
                            });

                    final TableColumn<XItemTablaObjetivo, String> columnHabilidades = new TableColumn<>(
                            "Habilidades Asociados");
                    columnHabilidades.setStyle("-fx-font-size:10;-fx-alignment: CENTER-LEFT;");
                    columnHabilidades.setCellValueFactory(c -> {
                        if(c == null || c.getValue() == null || c.getValue().getItems() == null || c.getValue().getItems().get(idx) == null)
                            return new ReadOnlyStringWrapper("");
                        final List<XItemObjetivo> lItems = c.getValue().getItems();
                        return new ReadOnlyStringWrapper(lItems.get(idx).getHabilidades());
                    });
                    columnHabilidades.setCellFactory(
                            new Callback<TableColumn<XItemTablaObjetivo, String>, TableCell<XItemTablaObjetivo, String>>() {

                                @Override
                                public TableCell<XItemTablaObjetivo, String> call(
                                        TableColumn<XItemTablaObjetivo, String> param) {
                                    TableCell<XItemTablaObjetivo, String> cell = new TableCell<>();
                                    Text text = new Text();
                                    cell.setGraphic(text);
                                    cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
                                    text.wrappingWidthProperty().bind(cell.widthProperty());
                                    text.textProperty().bind(cell.itemProperty());
                                    return cell;
                                }
                            });

                    final TableColumn<XItemTablaObjetivo, String> columnPreguntas = new TableColumn<>(
                            "Preguntas");
                    columnPreguntas.setCellFactory(
                            new Callback<TableColumn<XItemTablaObjetivo, String>, TableCell<XItemTablaObjetivo, String>>() {

                                @Override
                                public TableCell<XItemTablaObjetivo, String> call(
                                        TableColumn<XItemTablaObjetivo, String> param) {
                                    TableCell<XItemTablaObjetivo, String> cell = new TableCell<>();
                                    Text text = new Text();
                                    cell.setGraphic(text);
                                    cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
                                    text.wrappingWidthProperty().bind(cell.widthProperty());
                                    text.textProperty().bind(cell.itemProperty());
                                    return cell;
                                }
                            });
                    columnPreguntas.setStyle("-fx-font-size:10;-fx-alignment: CENTER-LEFT;");
                    columnPreguntas.setPrefWidth(100);
                    columnPreguntas.setMaxWidth(100);
                    columnPreguntas.setCellValueFactory(c -> {
                        if(c == null || c.getValue() == null || c.getValue().getItems() == null || c.getValue().getItems().get(idx) == null)
                            return new ReadOnlyStringWrapper("");
                        final List<XItemObjetivo> lItems = c.getValue().getItems();
                        return new ReadOnlyStringWrapper(lItems.get(idx).getPreguntas());
                    });

                    final TableColumn<XItemTablaObjetivo, Number> columnPercent = new TableColumn<>("% aprobación");
                    columnPercent.setStyle("-fx-font-size:10;-fx-alignment: CENTER;");
                    columnPercent.setCellValueFactory(c -> {
                        if(c == null || c.getValue() == null || c.getValue().getItems() == null || c.getValue().getItems().get(idx) == null)
                            return new ReadOnlyFloatWrapper(0f);
                        final List<XItemObjetivo> lItems = c.getValue().getItems();
                        return new ReadOnlyFloatWrapper(lItems.get(idx).getPorcentajeAprobacion());
                    });

                    columnPercent.setCellFactory(c -> {
                        return new TableCell<XItemTablaObjetivo, Number>() {

                            @Override
                            protected void updateItem(Number value, boolean empty) {
                                super.updateItem(value, empty);
                                if (value != null) {
                                    setText(String.format("%5.2f%%", value.doubleValue()));
                                    if (value.doubleValue() < 60) {
                                        setTextFill(Color.RED);
                                    }  else {
                                        setTextFill(Color.BLUE);
                                    }
                                }
                            }

                        };
                    });
                    headerColumn.getColumns().add(columnEjes);
                    headerColumn.getColumns().add(columnHabilidades);
                    headerColumn.getColumns().add(columnPreguntas);
                    headerColumn.getColumns().add(columnPercent);
                    tblObjetivos.getColumns().add(headerColumn);
                }
                tblObjetivos.setItems(itemsTable);
            }
        }
    }

    private void inicializeTable() {
        colObjetivos.setCellValueFactory(new PropertyValueFactory<XItemTablaObjetivo, Objetivo>("objetivo"));
    }

    @FXML
    public void initialize() {
        setTitle(TITLE);
        inicializeTable();

        cmbColegio.setOnAction(event -> {
            Colegio colegio = cmbColegio.getSelectionModel().getSelectedItem();
            if (colegio != null) {
                parameters.put(COLEGIO_ID, colegio.getId());
                Asignatura asig = cmbAsignaturas.getSelectionModel().getSelectedItem();
                String asignatura  = asig != null ? asig.getName() : "-"; 
                setTitle(String.format(TITLE_FORMAT, colegio.getName(),  asignatura ));
            }
        });

        cmbAsignaturas.setOnAction(event -> {
            Asignatura asignatura = cmbAsignaturas.getSelectionModel().getSelectedItem();
            if (asignatura != null) {
                parameters.put(ASIGNATURA_ID, asignatura.getId());
                
                Colegio colegio = cmbColegio.getSelectionModel().getSelectedItem();
                String strColegio = colegio != null ? colegio.getName() : "-"; 
                
                setTitle(String.format(TITLE_FORMAT, strColegio,  cmbAsignaturas.getSelectionModel().getSelectedItem() ));

            }
        });
        
        cmbTipoAlumno.setOnAction(event -> {
            if (cmbTipoAlumno.getSelectionModel() == null)
                return;
            tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedItem().getId();
        });
        btnGenerarReporte.setOnAction(event -> handlerReporte());
    }

    private void handlerReporte() {
        if (!parameters.isEmpty() && parameters.containsKey(COLEGIO_ID) && parameters.containsKey(ASIGNATURA_ID)) {
            controller.find("EvaluacionPrueba.findEvaluacionByColegioAsig", parameters, this);
        }
    }

    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            final Object entity = list.get(0);
            if (entity instanceof TipoAlumno) {
                final List<TipoAlumno> values = list.stream().map(t -> (TipoAlumno) t).collect(Collectors.toList());
                final ObservableList<TipoAlumno> tAlumnoList = FXCollections.observableArrayList(values);
                cmbTipoAlumno.setItems(tAlumnoList);
            }

            if (entity instanceof Colegio) {
                final List<Colegio> values = list.stream().map(t -> (Colegio) t).collect(Collectors.toList());
                final ObservableList<Colegio> colegios = FXCollections.observableArrayList(values);
                cmbColegio.setItems(colegios);
            }

            if (entity instanceof Asignatura) {
                final List<Asignatura> values = list.stream().map(t -> (Asignatura) t).collect(Collectors.toList());
                final ObservableList<Asignatura> asginaturas = FXCollections.observableArrayList(values);
                cmbAsignaturas.setItems(asginaturas);
            }
            
            if (entity instanceof EvaluacionPrueba) {
                evaluacionesPrueba = FXCollections.observableArrayList();
                for (Object object : list) {
                    EvaluacionPrueba evaluacion = (EvaluacionPrueba) object;
                    evaluacionesPrueba.add(evaluacion);
                }
                generateReport();
            }
        }

    }

}
