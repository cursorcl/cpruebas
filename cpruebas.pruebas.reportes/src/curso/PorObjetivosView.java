package curso;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Objetivo;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.TipoAlumno;
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
public class PorObjetivosView extends AFormView {

    @FXML
    private TableView<XItemTablaObjetivo> tblObjetivos;
    @FXML
    private TableColumn<XItemTablaObjetivo, Objetivo> colObjetivos;

    @FXML
    private Button btnGenerarReporte;
    @FXML
    private ComboBox<TipoAlumno> cmbTipoAlumno;

    private EvaluacionPrueba evaluacionPrueba;

    long tipoAlumno = Constants.PIE_ALL;

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
    private void generateXReport() {
        if (evaluacionPrueba != null) {
            
            long tipoAlumno = Constants.PIE_ALL;
            if(cmbTipoAlumno.getItems() != null && !cmbTipoAlumno.getItems().isEmpty() && cmbTipoAlumno.getSelectionModel().getSelectedItem() != null)
            {
                tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedItem().getId();
            }
            
            final Curso curso = evaluacionPrueba.getCurso();
            final List<PruebaRendida> pRendidas = evaluacionPrueba.getPruebasRendidas();

            if (pRendidas != null && !pRendidas.isEmpty()) {
                final List<XItemTablaObjetivo> reporte = XUtilReportBuilder.reporteCurso(pRendidas, tipoAlumno);

                final ObservableList<XItemTablaObjetivo> itemsTable = FXCollections.observableList(reporte);
                final Optional<XItemTablaObjetivo> opFirst = itemsTable.stream().findFirst();
                if (!opFirst.isPresent())
                    return;

                while (tblObjetivos.getColumns().size() > 1)
                    tblObjetivos.getColumns().remove(tblObjetivos.getColumns().size() - 1);

                final List<XItemObjetivo> items = opFirst.get().getItems();
                for (int n = 0; n < items.size(); n++) {
                    final int idx = n;
                    TableColumn<XItemTablaObjetivo, String> headerColumn = new TableColumn<>(curso.getName());
                    headerColumn.setStyle("-fx-font-size:10;-fx-alignment: CENTER;");

                    final TableColumn<XItemTablaObjetivo, String> columnEjes = new TableColumn<>("Ejes Asociados");
                    columnEjes.setStyle("-fx-font-size:10;-fx-alignment: CENTER-LEFT;");
                    columnEjes.setCellValueFactory(c -> {
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
                    columnPreguntas.setStyle("-fx-font-size:10;-fx-alignment: CENTER-LEFT;");
                    columnPreguntas.setPrefWidth(100);
                    columnPreguntas.setMaxWidth(100);
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
                    columnPreguntas.setCellValueFactory(c -> {
                        final List<XItemObjetivo> lItems = c.getValue().getItems();
                        return new ReadOnlyStringWrapper(lItems.get(idx).getPreguntas());
                    });

                    final TableColumn<XItemTablaObjetivo, Number> columnPercent = new TableColumn<>("% aprobación");
                    columnPercent.setStyle("-fx-font-size:10;-fx-alignment: CENTER;");
                    columnPercent.setCellValueFactory(c -> {
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


    @FXML
    public void initialize() {
        setTitle("Resumen de respuestas por objetivo");
        colObjetivos.setCellValueFactory(new PropertyValueFactory<XItemTablaObjetivo, Objetivo>("objetivo"));
        cmbTipoAlumno.setOnAction(event -> {
            if (cmbTipoAlumno.getSelectionModel() == null)
                return;
            tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedItem().getId();
        });
        btnGenerarReporte.setOnAction(event -> generateXReport());
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
        }

    }

    @Override
    public void onFound(IEntity entity) {
        if (entity instanceof EvaluacionPrueba) {
            evaluacionPrueba = (EvaluacionPrueba) entity;
            generateXReport();
        }
    }

}
