package cl.eos.comprensionlectora;

import java.util.ArrayList;
import java.util.List;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.SComprensionLectora;
import cl.eos.util.ExcelSheetWriterObj;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ViewComprensionLectora extends AFormView implements EventHandler<ActionEvent> {
    private static final int LARGO_CAMPO_TEXT = 100;

    @FXML
    private Label lblError;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombre;
    @FXML
    private TableView<SComprensionLectora> tblTable;
    @FXML
    private TableColumn<SComprensionLectora, Long> colId;
    @FXML
    private TableColumn<SComprensionLectora, String> colNombre;
    @FXML
    private MenuItem mnuAgregar;
    @FXML
    private MenuItem mnuModificar;
    @FXML
    private MenuItem mnuEliminar;
    @FXML
    private MenuItem mnItemModificar;
    @FXML
    private MenuItem mnItemEliminar;
    @FXML
    private MenuItem mnuGrabar;
    @FXML
    private MenuItem menuExportar;
    @FXML
    private MenuItem mnuExportar;

    public ViewComprensionLectora() {
        setTitle("Comprensión de Lectura");
    }

    private void accionEliminar() {
        final ObservableList<SComprensionLectora> otSeleccionados = tblTable.getSelectionModel().getSelectedItems();
        if (otSeleccionados.size() == 0) {
            final Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Selección registro");
            alert.setHeaderText(getName());
            alert.setContentText("Debe seleccionar registro a procesar");
            alert.showAndWait();
        } else {
            if (otSeleccionados != null && !otSeleccionados.isEmpty()) {
                final List<SComprensionLectora> colegio = new ArrayList<SComprensionLectora>(otSeleccionados.size());
                for (final SComprensionLectora seleccionado : otSeleccionados) {
                    colegio.add(seleccionado);
                }
                delete(colegio);
                tblTable.getSelectionModel().clearSelection();
                limpiarControles();
            }
        }
    }

    private void accionGrabar() {
        final IEntity entitySelected = getSelectedEntity();
        removeAllStyles();
        if (validate()) {
            if (lblError != null) {
                lblError.setText(" ");
            }
            SComprensionLectora calidadLectora = null;
            if (entitySelected != null && entitySelected instanceof SComprensionLectora) {
                calidadLectora = (SComprensionLectora) entitySelected;
            } else {
                calidadLectora = new SComprensionLectora();
            }
            calidadLectora.setName(txtNombre.getText());
            save(calidadLectora);
            limpiarControles();
        } else {
            lblError.getStyleClass().add("bad");
            lblError.setText("Corregir campos destacados en color rojo");
        }

    }

    private void accionModificar() {
        final SComprensionLectora calidadLectora = tblTable.getSelectionModel().getSelectedItem();
        if (calidadLectora != null) {
            txtId.setText(String.format("%d", calidadLectora.getId()));
            txtNombre.setText(calidadLectora.getName());
        }
    }

    @Override
    public void handle(ActionEvent event) {
        final Object source = event.getSource();
        if (source == mnuAgregar) {
            limpiarControles();
        } else if (source == mnuModificar || source == mnItemModificar) {
            accionModificar();
        } else if (source == mnuGrabar) {
            accionGrabar();
        } else if (source == mnuEliminar || source == mnItemEliminar) {
            accionEliminar();
        } else if (source == mnuExportar || source == menuExportar) {
            tblTable.setId("Calidad");
            ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblTable);
        }
    }

    private void inicializaTabla() {
        tblTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        colId.setCellValueFactory(new PropertyValueFactory<SComprensionLectora, Long>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<SComprensionLectora, String>("name"));
        tblTable.setOnMouseClicked(event -> {
            final ObservableList<SComprensionLectora> itemsSelec = tblTable.getSelectionModel().getSelectedItems();
            if (itemsSelec.size() > 1) {
                mnItemModificar.setDisable(true);
                mnItemEliminar.setDisable(false);

                mnuModificar.setDisable(true);
                mnuEliminar.setDisable(false);
            } else if (itemsSelec.size() == 1) {

                mnItemModificar.setDisable(false);
                mnItemEliminar.setDisable(false);

                mnuModificar.setDisable(false);
                mnuEliminar.setDisable(false);
            }
        });
    }

    @FXML
    public void initialize() {
        inicializaTabla();
        mnuAgregar.setOnAction(this);
        mnuGrabar.setOnAction(this);
        mnuModificar.setOnAction(this);
        mnuEliminar.setOnAction(this);
        mnItemEliminar.setOnAction(this);
        mnItemModificar.setOnAction(this);
        mnuExportar.setOnAction(this);
        menuExportar.setOnAction(this);

        mnuModificar.setDisable(true);
        mnuEliminar.setDisable(true);
        mnItemEliminar.setDisable(true);
        mnItemModificar.setDisable(true);
    }

    private void limpiarControles() {
        txtId.clear();
        txtNombre.clear();
    }

    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            final Object entity = list.get(0);
            if (entity instanceof SComprensionLectora) {
                final ObservableList<SComprensionLectora> value = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    value.add((SComprensionLectora) iEntity);
                }
                tblTable.setItems(value.sorted());
            }
        }
    }

    @Override
    public void onDeleted(IEntity entity) {
        tblTable.getItems().remove(entity);
    }

    @Override
    public void onSaved(IEntity entity) {
        final SComprensionLectora calidadLectora = (SComprensionLectora) entity;
        final int indice = tblTable.getItems().lastIndexOf(calidadLectora);
        if (indice != -1) {
            tblTable.getItems().set(indice, calidadLectora);
        } else {
            tblTable.getItems().add(calidadLectora);
        }
    }

    private void removeAllStyles() {
        removeAllStyle(lblError);
        removeAllStyle(txtId);
        removeAllStyle(txtNombre);
    }

    @Override
    public boolean validate() {
        boolean valida = true;
        if (txtId.getText() == null || txtId.getText().equals("")) {
            txtId.getStyleClass().add("bad");
            valida = false;
        }

        if (txtNombre.getText() == null || txtNombre.getText().equals("")) {
            txtNombre.getStyleClass().add("bad");
            valida = false;
        }
        if (txtNombre.getText() != null && txtNombre.getText().length() > ViewComprensionLectora.LARGO_CAMPO_TEXT) {
            txtNombre.getStyleClass().add("bad");
            valida = false;
        }
        return valida;
    }

}
