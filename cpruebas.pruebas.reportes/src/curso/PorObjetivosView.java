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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
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

    @FXML
    public void initialize() {
        this.setTitle("Resumen de respuestas por objetivo");
        inicializeTable();
        cmbColegio.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Colegio colegio = cmbColegio.getSelectionModel().getSelectedItem();
                if (colegio != null) {
                    clearContent();
                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put(COLEGIO_ID, colegio.getId());
                    controller.find("Curso.findByColegio", parameters);

                }
            }
        });
        cmbTipoAlumno.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (cmbTipoAlumno.getSelectionModel() == null)
                    return;
                tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedItem().getId();
            }
        });
        btnGenerarReporte.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                generateReport();
            }
        });
    }

    protected void clearContent() {
        if (tblObjetivos.getItems() != null)
            tblObjetivos.getItems().clear();
        if (tblObjetivos.getColumns() != null) {
            for (int n = 3; n < tblObjetivos.getColumns().size(); n++)
                tblObjetivos.getColumns().remove(n);
        }
    }

    private void inicializeTable() {
        colObjetivos.setCellValueFactory(new PropertyValueFactory<ItemTablaObjetivo, Objetivo>("objetivo"));
        colPreguntas.setCellValueFactory(new PropertyValueFactory<ItemTablaObjetivo, String>("preguntas"));
        colEjes.setCellValueFactory(new PropertyValueFactory<ItemTablaObjetivo, String>("ejesAsociados"));
        colHabilidades.setCellValueFactory(new PropertyValueFactory<ItemTablaObjetivo, String>("habilidades"));
    }

    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            Object entity = list.get(0);
            if (entity instanceof TipoAlumno) {
                List<TipoAlumno> values = list.stream().map(t -> (TipoAlumno) t).collect(Collectors.toList());
                ObservableList<TipoAlumno> tAlumnoList = FXCollections.observableArrayList(values);
                cmbTipoAlumno.setItems(tAlumnoList);
            }
            if (entity instanceof Curso) {
                List<Curso> values = list.stream().map(t -> (Curso) t).collect(Collectors.toList());
                ObservableList<Curso> curso = FXCollections.observableArrayList(values);
                cmbCursos.setItems(curso);
            }

            if (entity instanceof Colegio) {
                List<Colegio> values = list.stream().map(t -> (Colegio) t).collect(Collectors.toList());
                ObservableList<Colegio> colegios = FXCollections.observableArrayList(values);
                cmbColegio.setItems(colegios);
            }

            if (entity instanceof Asignatura) {
                List<Asignatura> values = list.stream().map(t -> (Asignatura) t).collect(Collectors.toList());
                ObservableList<Asignatura> asginaturas = FXCollections.observableArrayList(values);
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

    /**
     * Generar el reporte.
     */
    private void generateReport() {
        if (evaluacionPrueba != null) // && cmbTipoAlumno.getItems() != null && !cmbTipoAlumno.getItems().isEmpty()) {
        {
//            Curso curso = cmbCursos.getValue();
            Curso curso = evaluacionPrueba.getCurso();
            List<PruebaRendida> pRendidas = evaluacionPrueba.getPruebasRendidas();

            if (pRendidas != null && !pRendidas.isEmpty()) {
                List<ItemTablaObjetivo> reporte = UtilReportBuilder.buildReportCurso(pRendidas);

                ObservableList<ItemTablaObjetivo> itemsTable = FXCollections.observableList(reporte);
                Optional<ItemTablaObjetivo> opFirst = itemsTable.stream().findFirst();
                if (!opFirst.isPresent())
                    return;
                
                while(tblObjetivos.getColumns().size() > 3)
                    tblObjetivos.getColumns().remove(tblObjetivos.getColumns().size() - 1);
                List<ItemObjetivo> items = opFirst.get().getItems();
                for (int n = 0; n < items.size(); n++) {
                    final int idx = n;
                    TableColumn<ItemTablaObjetivo, Number> column = new TableColumn<>(curso.getName());
                    column.setStyle("-fx-font-size:10;-fx-alignment: CENTER;");
                    column.setCellValueFactory(c -> {
                        List<ItemObjetivo> lItems = c.getValue().getItems();
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

}
