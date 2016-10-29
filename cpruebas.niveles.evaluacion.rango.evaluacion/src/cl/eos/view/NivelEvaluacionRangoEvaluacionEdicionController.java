package cl.eos.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.NivelEvaluacion;
import cl.eos.persistence.models.RangoEvaluacion;
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
    @FXML
    private TextField txtRango;
    @FXML
    private TextField txtAbreviacion;
    @FXML
    private MenuItem mnuCancelar;
    @FXML
    private MenuItem mnuGrabar;
    @FXML
    private BigDecimalField bdMinimo;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnGrabar;
    @FXML
    private TextField txtNivelEvaluacion;
    @FXML
    private BigDecimalField bdMaximo;
    @FXML
    private TableView<RangoEvaluacion> tblRangos;
    @FXML
    private TableColumn<RangoEvaluacion, Float> colRangoMaximo;
    @FXML
    private TableColumn<RangoEvaluacion, Float> colRangoMinimo;
    @FXML
    private TableColumn<RangoEvaluacion, String> colRangosAbrev;
    @FXML
    private TableColumn<RangoEvaluacion, String> colRangosNombre;
    @FXML
    private Label lblButton;
    @FXML
    private Button btnCambiar;

    @FXML
    private MenuItem mnuItemAgregar;
    @FXML
    private MenuItem mnuItemEliminar;

    private NivelEvaluacion nivel;
    private boolean agregando;

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == mnuGrabar || event.getSource() == btnGrabar) {
            final Stage stage = (Stage) btnCancelar.getScene().getWindow();
            stage.close();
            final List<RangoEvaluacion> toRemove = new ArrayList<RangoEvaluacion>();
            final ObservableList<RangoEvaluacion> items = tblRangos.getItems();
            final int size = items == null ? 0 : items.size();
            if (nivel == null) {
                nivel = new NivelEvaluacion();
                nivel.setRangos(new ArrayList<RangoEvaluacion>());
                for (final RangoEvaluacion rango : items) {
                    rango.setNivelEvaluacion(nivel);
                    nivel.getRangos().add(rango);
                }
            } else {
                final List<RangoEvaluacion> lst = new ArrayList<>(nivel.getRangos());
                for (final RangoEvaluacion rango : lst) {
                    boolean founded = false;
                    for (final RangoEvaluacion rng : items) {
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
                for (final RangoEvaluacion rango : toRemove) {
                    nivel.getRangos().remove(rango);
                }
                for (final RangoEvaluacion rango : items) {
                    boolean founded = false;
                    for (final RangoEvaluacion rng : lst) {
                        if (rango.equals(rng)) {
                            founded = true;
                            break;
                        }
                    }
                    if (!founded) {
                        rango.setNivelEvaluacion(nivel);
                        nivel.getRangos().add(rango);
                    }
                }
            }

            nivel.setName(txtNivelEvaluacion.getText());
            nivel.setNroRangos(size);
            nivel = (NivelEvaluacion) save(nivel);
            for (final RangoEvaluacion rango : nivel.getRangos()) {
                save(rango);
            }
        }
        if (event.getSource() == btnCancelar) {
            final Stage stage = (Stage) btnCancelar.getScene().getWindow();
            stage.close();
        }
        if (event.getSource() == btnCambiar) {
            if (agregando) {
                final RangoEvaluacion rango = new RangoEvaluacion();
                rango.setAbreviacion(txtAbreviacion.getText());
                rango.setName(txtRango.getText());
                rango.setMinimo(bdMinimo.getNumber().floatValue());
                rango.setMaximo(bdMaximo.getNumber().floatValue());
                tblRangos.getItems().add(rango);

            } else {
                final int index = tblRangos.getSelectionModel().getSelectedIndex();
                final RangoEvaluacion rango = tblRangos.getSelectionModel().getSelectedItem();
                rango.setAbreviacion(txtAbreviacion.getText());
                rango.setName(txtRango.getText());
                rango.setMinimo(bdMinimo.getNumber().floatValue());
                rango.setMaximo(bdMaximo.getNumber().floatValue());
                tblRangos.getItems().set(index, rango);
            }
        }
        if (event.getSource() == mnuItemEliminar) {
            final RangoEvaluacion rango = tblRangos.getSelectionModel().getSelectedItem();
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
        colRangosNombre.setCellValueFactory(new PropertyValueFactory<RangoEvaluacion, String>("name"));
        colRangosAbrev.setCellValueFactory(new PropertyValueFactory<RangoEvaluacion, String>("abreviacion"));
        colRangoMinimo.setCellValueFactory(new PropertyValueFactory<RangoEvaluacion, Float>("minimo"));
        colRangoMaximo.setCellValueFactory(new PropertyValueFactory<RangoEvaluacion, Float>("maximo"));

        tblRangos.setOnMouseClicked(arg0 -> {
            if (tblRangos.getItems() == null || tblRangos.getItems().isEmpty())
                return;
            final RangoEvaluacion rango = tblRangos.getSelectionModel().getSelectedItem();
            if (rango == null)
                return;

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
        if (entity instanceof NivelEvaluacion) {
            nivel = (NivelEvaluacion) entity;
            txtNivelEvaluacion.setText(nivel.getName());

            final ObservableList<RangoEvaluacion> lsRangoEvaluacion = FXCollections.observableArrayList();
            for (final RangoEvaluacion iEntity : nivel.getRangos()) {
                lsRangoEvaluacion.add(iEntity);
            }
            tblRangos.setItems(lsRangoEvaluacion);
        }
    }

}
