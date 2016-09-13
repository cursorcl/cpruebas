package cl.eos.rangoslectura;

import java.util.ArrayList;
import java.util.List;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.CalidadLectora;
import cl.eos.persistence.models.ComprensionLectora;
import cl.eos.persistence.models.RangosLectura;
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
import javafx.scene.input.MouseEvent;

public class ViewRangoLecturas extends AFormView implements EventHandler<ActionEvent> {
    private static final int LARGO_CAMPO_TEXT = 100;

    @FXML
    private Label lblError;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombre;
    @FXML
    private TableView<CalidadLectora> tblTable;
    @FXML
    private TableColumn<CalidadLectora, Long> colId;
    @FXML
    private TableColumn<CalidadLectora, String> colNombre;
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

    public ViewRangoLecturas() {
        setTitle("Rango de Lectura");
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

    private void inicializaTabla() {
        tblTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        colId.setCellValueFactory(new PropertyValueFactory<CalidadLectora, Long>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<CalidadLectora, String>("name"));
        tblTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ObservableList<CalidadLectora> itemsSelec = tblTable.getSelectionModel().getSelectedItems();
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
            }
        });
    }

    private void accionModificar() {
        CalidadLectora calidadLectora = tblTable.getSelectionModel().getSelectedItem();
        if (calidadLectora != null) {
            txtId.setText(String.format("%d", calidadLectora.getId()));
            txtNombre.setText(calidadLectora.getName());
        }
    }

    private void accionEliminar() {
        ObservableList<CalidadLectora> otSeleccionados = tblTable.getSelectionModel().getSelectedItems();
        if (otSeleccionados.size() == 0) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Selección registro");
            alert.setHeaderText(this.getName());
            alert.setContentText("Debe seleccionar registro a procesar");
            alert.showAndWait();
        } else {
            if (otSeleccionados != null && !otSeleccionados.isEmpty()) {
                List<CalidadLectora> colegio = new ArrayList<CalidadLectora>(otSeleccionados.size());
                for (CalidadLectora seleccionado : otSeleccionados) {
                    colegio.add(seleccionado);
                }
                delete(colegio);
                tblTable.getSelectionModel().clearSelection();
                limpiarControles();
            }
        }
    }

    private void accionGrabar() {
        IEntity entitySelected = getSelectedEntity();
        removeAllStyles();
        if (validate()) {
            if (lblError != null) {
                lblError.setText(" ");
            }
            CalidadLectora calidadLectora = null;
            if (entitySelected != null && entitySelected instanceof CalidadLectora) {
                calidadLectora = (CalidadLectora) entitySelected;
            } else {
                calidadLectora = new CalidadLectora();
            }
            calidadLectora.setName(txtNombre.getText());
            save(calidadLectora);
            limpiarControles();
        } else {
            lblError.getStyleClass().add("bad");
            lblError.setText("Corregir campos destacados en color rojo");
        }

    }

    private void limpiarControles() {
        txtId.clear();
        txtNombre.clear();
    }

    @Override
    public void onSaved(IEntity entity) {
        CalidadLectora calidadLectora = (CalidadLectora) entity;
        int indice = tblTable.getItems().lastIndexOf(calidadLectora);
        if (indice != -1) {
            tblTable.getItems().set(indice, calidadLectora);
        } else {
            tblTable.getItems().add(calidadLectora);
        }
    }

    @Override
    public void onDeleted(IEntity entity) {
        tblTable.getItems().remove((CalidadLectora) entity);
    }

    private void removeAllStyles() {
        removeAllStyle(lblError);
        removeAllStyle(txtId);
        removeAllStyle(txtNombre);
    }

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
        if (txtNombre.getText() != null && txtNombre.getText().length() > LARGO_CAMPO_TEXT) {
            txtNombre.getStyleClass().add("bad");
            valida = false;
        }

        return valida;
    }

    @Override
    public void handle(ActionEvent event) {
        Object source = event.getSource();
        if (source == mnuAgregar) {
            limpiarControles();
        } else if (source == mnuModificar || source == mnItemModificar) {
            accionModificar();
        } else if (source == mnuGrabar) {
            accionGrabar();
        } else if (source == mnuEliminar || source == mnItemEliminar) {
            accionEliminar();
            // } else if (source == btnImagen) {
            // accionButtonImagen();
        } else if (source == mnuExportar || source == menuExportar) {
            tblTable.setId("Calidad");
            ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblTable);
        }
    }
    
    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            Object entity = list.get(0);
            if (entity instanceof RangosLectura) {
                ObservableList<RangosLectura> value = FXCollections.observableArrayList();
                for (Object iEntity : list) {
                    value.add((RangosLectura) iEntity);
                }
                //tblTable.setItems(value);
            }
        }
    }
}
