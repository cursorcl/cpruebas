package cl.eos.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.restful.tables.R_NivelEvaluacion;
import cl.eos.restful.tables.R_RangoEvaluacion;
import cl.eos.util.MapBuilder;
import cl.eos.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.BigDecimalField;

public class NivelEvaluacionRangoEvaluacionEdicionController extends AFormView implements EventHandler<ActionEvent> {
    @FXML private TextField txtRango;
    @FXML private TextField txtAbreviacion;
    @FXML private MenuItem mnuCancelar;
    @FXML private MenuItem mnuGrabar;
    @FXML private BigDecimalField bdMinimo;
    @FXML private Button btnCancelar;
    @FXML private Button btnGrabar;
    @FXML private TextField txtNivelEvaluacion;
    @FXML private BigDecimalField bdMaximo;
    @FXML private TableView<R_RangoEvaluacion> tblRangos;
    @FXML private TableColumn<R_RangoEvaluacion, Float> colRangoMaximo;
    @FXML private TableColumn<R_RangoEvaluacion, Float> colRangoMinimo;
    @FXML private TableColumn<R_RangoEvaluacion, String> colRangosAbrev;
    @FXML private TableColumn<R_RangoEvaluacion, String> colRangosNombre;
    @FXML private Label lblButton;
    @FXML private Button btnCambiar;
    @FXML private MenuItem mnuItemAgregar;
    @FXML private MenuItem mnuItemEliminar;
    private R_NivelEvaluacion nivel;
    private boolean agregando;
    private List<R_RangoEvaluacion> rangos;
    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == mnuGrabar || event.getSource() == btnGrabar) {
            final Stage stage = (Stage) btnCancelar.getScene().getWindow();
            stage.close();
            final List<R_RangoEvaluacion> toRemove = new ArrayList<R_RangoEvaluacion>();
            final ObservableList<R_RangoEvaluacion> items = tblRangos.getItems();
            final int size = items == null ? 0 : items.size();
            if (nivel == null) {
                nivel = new R_NivelEvaluacion.Builder().id(Utils.getLastIndex()).build();
                nivel.setName(txtNivelEvaluacion.getText());
                nivel.setNrorangos(items.size());
                for (final R_RangoEvaluacion rango : items) {
                    rango.setNivelevaluacion_id(nivel.getId());
                }
            } else {
                final List<R_RangoEvaluacion> lst = new ArrayList<>(rangos);
                // tengo que eliminar los rangos que han sido sacados
                // y tengo que agregar los nuevos
                for (final R_RangoEvaluacion rango : lst) {
                    boolean founded = false;
                    for (final R_RangoEvaluacion rng : items) {
                        if (rango.equals(rng)) {
                            founded = true;
                            rango.setAbreviacion(rng.getAbreviacion());
                            rango.setMaximo(rng.getMaximo());
                            rango.setMinimo(rng.getMinimo());
                            rango.setName(rng.getName());
                            break;
                        }
                    }
                    if (!founded) {
                        toRemove.add(rango);
                    }
                }
                for (final R_RangoEvaluacion rango : toRemove) {
                    controller.delete(rango);
                }
                for (final R_RangoEvaluacion rango : items) {
                    boolean founded = false;
                    for (final R_RangoEvaluacion rng : lst) {
                        if (rango.equals(rng)) {
                            founded = true;
                            break;
                        }
                    }
                    if (!founded) {
                        rangos.add(rango);
                    }
                }
            }
            nivel.setName(txtNivelEvaluacion.getText());
            nivel.setNrorangos(size);
            nivel = (R_NivelEvaluacion) save(nivel);
            for (final R_RangoEvaluacion rango : rangos) {
                save(rango);
            }
        }
        if (event.getSource() == btnCancelar) {
            final Stage stage = (Stage) btnCancelar.getScene().getWindow();
            stage.close();
        }
        if (event.getSource() == btnCambiar) {
            if (agregando) {
                final R_RangoEvaluacion rango = new R_RangoEvaluacion.Builder().id(Utils.getLastIndex()).build();
                rango.setAbreviacion(txtAbreviacion.getText());
                rango.setName(txtRango.getText());
                rango.setMinimo(bdMinimo.getNumber().floatValue());
                rango.setMaximo(bdMaximo.getNumber().floatValue());
                tblRangos.getItems().add(rango);
            } else {
                final int index = tblRangos.getSelectionModel().getSelectedIndex();
                final R_RangoEvaluacion rango = tblRangos.getSelectionModel().getSelectedItem();
                rango.setAbreviacion(txtAbreviacion.getText());
                rango.setName(txtRango.getText());
                rango.setMinimo(bdMinimo.getNumber().floatValue());
                rango.setMaximo(bdMaximo.getNumber().floatValue());
                tblRangos.getItems().set(index, rango);
            }
        }
        if (event.getSource() == mnuItemEliminar) {
            final R_RangoEvaluacion rango = tblRangos.getSelectionModel().getSelectedItem();
            if (rango != null) {
                final Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirma Eliminar");
                alert.setHeaderText(null);
                alert.setContentText("Confirma borrado de la prueba?");
                final Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    final int index = tblRangos.getSelectionModel().getSelectedIndex();
                    tblRangos.getItems().remove(index);
                }
            }
        }
        if (event.getSource() == mnuItemAgregar) {
            agregando = true;
            txtRango.setText("");
            txtAbreviacion.setText("");
            bdMinimo.setNumber(new BigDecimal(0));
            bdMaximo.setNumber(new BigDecimal(0));
            txtRango.requestFocus();
        }
    }
    private void inicializaTabla() {
        tblRangos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        colRangosNombre.setCellValueFactory(new PropertyValueFactory<R_RangoEvaluacion, String>("name"));
        colRangosAbrev.setCellValueFactory(new PropertyValueFactory<R_RangoEvaluacion, String>("abreviacion"));
        colRangoMinimo.setCellValueFactory(new PropertyValueFactory<R_RangoEvaluacion, Float>("minimo"));
        colRangoMaximo.setCellValueFactory(new PropertyValueFactory<R_RangoEvaluacion, Float>("maximo"));
        tblRangos.setOnMouseClicked(arg0 -> {
            if (tblRangos.getItems() == null || tblRangos.getItems().isEmpty()) return;
            final R_RangoEvaluacion rango = tblRangos.getSelectionModel().getSelectedItem();
            if (rango == null) return;
            txtRango.setText(rango.getName());
            txtAbreviacion.setText(rango.getAbreviacion());
            bdMinimo.setNumber(new BigDecimal(rango.getMinimo()));
            bdMaximo.setNumber(new BigDecimal(rango.getMaximo()));
            agregando = false;
        });
    }
    @FXML
    public void initialize() {
        inicializaTabla();
        mnuCancelar.setOnAction(this);
        mnuGrabar.setOnAction(this);
        btnGrabar.setOnAction(this);
        btnCancelar.setOnAction(this);
        btnCambiar.setOnAction(this);
        mnuItemAgregar.setOnAction(this);
        mnuItemEliminar.setOnAction(this);
    }
    @Override
    public void onFound(IEntity entity) {
        if (entity instanceof R_NivelEvaluacion) {
            nivel = (R_NivelEvaluacion) entity;
            txtNivelEvaluacion.setText(nivel.getName());
            Map<String, Object> params = MapBuilder.<String, Object> unordered().put("nivelevaluacion_id", nivel.getId()).build();
            rangos = controller.findByParamsSynchro(R_RangoEvaluacion.class, params);
            final ObservableList<R_RangoEvaluacion> lR_RangoEvaluacion = FXCollections.observableArrayList();
            for (final R_RangoEvaluacion iEntity : rangos) {
                lR_RangoEvaluacion.add(iEntity);
            }
            tblRangos.setItems(lR_RangoEvaluacion);
        }
    }
}
