package curso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Objetivo;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.TipoAlumno;
import javafx.beans.property.ReadOnlyFloatWrapper;
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
import ot.ItemObjetivo;
import ot.ItemTablaObjetivo;
import ot.UtilReportBuilder;

/**
 * Obtiene los objetivos de un curso
 *
 * @author curso
 *
 */
public class PorObjetivosView extends AFormView {

    private static final String COLEGIO_ID = "colegioId";

    @FXML
    private TableView<ItemTablaObjetivo> tblObjetivos;
    @FXML
    private TableColumn<ItemTablaObjetivo, Objetivo> colObjetivos;
    @FXML
    private TableColumn<ItemTablaObjetivo, String> colPreguntas;
    @FXML
    private TableColumn<ItemTablaObjetivo, String> colEjes;
    @FXML
    private TableColumn<ItemTablaObjetivo, String> colHabilidades;

    @FXML
    private Button btnGenerarReporte;
    @FXML
    private ComboBox<TipoAlumno> cmbTipoAlumno;
    @FXML
    private ComboBox<Colegio> cmbColegio;
    @FXML
    private ComboBox<Curso> cmbCursos;
    @FXML
    private ComboBox<Asignatura> cmbAsignaturas;

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
    private void generateReport() {
        if (evaluacionPrueba != null && cmbTipoAlumno.getItems() != null && !cmbTipoAlumno.getItems().isEmpty()) {
            // Curso curso = cmbCursos.getValue();
            final Curso curso = evaluacionPrueba.getCurso();
            final List<PruebaRendida> pRendidas = evaluacionPrueba.getPruebasRendidas();

            if (pRendidas != null && !pRendidas.isEmpty()) {
                final List<ItemTablaObjetivo> reporte = UtilReportBuilder.reporteCurso(pRendidas);

                final ObservableList<ItemTablaObjetivo> itemsTable = FXCollections.observableList(reporte);
                final Optional<ItemTablaObjetivo> opFirst = itemsTable.stream().findFirst();
                if (!opFirst.isPresent())
                    return;

                while (tblObjetivos.getColumns().size() > 3)
                    tblObjetivos.getColumns().remove(tblObjetivos.getColumns().size() - 1);
                final List<ItemObjetivo> items = opFirst.get().getItems();
                for (int n = 0; n < items.size(); n++) {
                    final int idx = n;
                    final TableColumn<ItemTablaObjetivo, Number> column = new TableColumn<>(curso.getName());
                    column.setStyle("-fx-font-size:10;-fx-alignment: CENTER;");
                    column.setCellValueFactory(c -> {
                        final List<ItemObjetivo> lItems = c.getValue().getItems();
                        return new ReadOnlyFloatWrapper(lItems.get(idx).getPorcentajeAprobacion());
                    });

                    column.setCellFactory(c -> {
                        return new TableCell<ItemTablaObjetivo, Number>() {

                            @Override
                            protected void updateItem(Number value, boolean empty) {
                                super.updateItem(value, empty);
                                if (value != null) {
                                    setText(String.format("%5.2f%%", value.doubleValue()));
                                    if (value.doubleValue() < 60) {
                                        setTextFill(Color.WHITE);
                                        setStyle("-fx-font-size:10;-fx-alignment: CENTER;-fx-background-color: red");
                                    } else {
                                        setTextFill(Color.BLACK);
                                        setStyle("-fx-font-size:10;-fx-alignment: CENTER;-fx-background-color: white");
                                    }
                                }
                            }

                        };
                    });
                    tblObjetivos.getColumns().add(column);
                }
                tblObjetivos.setItems(itemsTable);
            }
        }
    }

    private void inicializeTable() {
        colObjetivos.setCellValueFactory(new PropertyValueFactory<ItemTablaObjetivo, Objetivo>("objetivo"));
        colPreguntas.setCellValueFactory(new PropertyValueFactory<ItemTablaObjetivo, String>("preguntas"));
        colPreguntas.setCellFactory(
                new Callback<TableColumn<ItemTablaObjetivo, String>, TableCell<ItemTablaObjetivo, String>>() {

                    @Override
                    public TableCell<ItemTablaObjetivo, String> call(TableColumn<ItemTablaObjetivo, String> param) {
                        TableCell<ItemTablaObjetivo, String> cell = new TableCell<>();
                        Text text = new Text();
                        cell.setGraphic(text);
                        cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
                        text.wrappingWidthProperty().bind(cell.widthProperty());
                        text.textProperty().bind(cell.itemProperty());
                        return cell;
                    }

                });

        colEjes.setCellValueFactory(new PropertyValueFactory<ItemTablaObjetivo, String>("ejesAsociados"));
        colHabilidades.setCellValueFactory(new PropertyValueFactory<ItemTablaObjetivo, String>("habilidades"));
    }

    @FXML
    public void initialize() {
        setTitle("Resumen de respuestas por objetivo");
        inicializeTable();
        cmbColegio.setOnAction(event -> {
            final Colegio colegio = cmbColegio.getSelectionModel().getSelectedItem();
            if (colegio != null) {
                clearContent();
                final Map<String, Object> parameters = new HashMap<>();
                parameters.put(PorObjetivosView.COLEGIO_ID, colegio.getId());
                controller.find("Curso.findByColegio", parameters);

            }
        });
        cmbTipoAlumno.setOnAction(event -> {
            if (cmbTipoAlumno.getSelectionModel() == null)
                return;
            tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedItem().getId();
        });
        btnGenerarReporte.setOnAction(event -> generateReport());
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
            if (entity instanceof Curso) {
                final List<Curso> values = list.stream().map(t -> (Curso) t).collect(Collectors.toList());
                final ObservableList<Curso> curso = FXCollections.observableArrayList(values);
                cmbCursos.setItems(curso);
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
        }

    }

    @Override
    public void onFound(IEntity entity) {
        if (entity instanceof EvaluacionPrueba) {
            evaluacionPrueba = (EvaluacionPrueba) entity;
            generateReport();
        }
    }

}
