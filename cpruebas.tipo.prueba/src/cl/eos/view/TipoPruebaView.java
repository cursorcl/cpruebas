package cl.eos.view;

import java.util.ArrayList;
import java.util.List;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTTipoPrueba;
import cl.eos.persistence.models.TipoPrueba;
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

public class TipoPruebaView extends AFormView implements EventHandler<ActionEvent> {
    private static final int LARGO_CAMPO_TEXT = 100;

    @FXML
    private MenuItem mnuAgregar;

    @FXML
    private MenuItem mnuGrabar;

    @FXML
    private MenuItem mnItemEliminar;

    @FXML
    private MenuItem mnItemModificar;

    @FXML
    private MenuItem mnuEliminar;

    @FXML
    private MenuItem mnuModificar;

    @FXML
    private MenuItem menuExportar;

    @FXML
    private MenuItem mnuExportar;

    @FXML
    private TextField txtNombre;

    @FXML
    private Label lblError;

    @FXML
    private TableView<OTTipoPrueba> tblTipoPrueba;

    @FXML
    private TableColumn<OTTipoPrueba, Long> colId;

    @FXML
    private TableColumn<OTTipoPrueba, String> colNombre;

    public TipoPruebaView() {
        setTitle("Tipo Prueba");
    }

    private void accionClicTabla() {
        tblTipoPrueba.setOnMouseClicked(event -> {
            final ObservableList<OTTipoPrueba> itemsSelec = tblTipoPrueba.getSelectionModel().getSelectedItems();
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

    private void accionEliminar() {
        final ObservableList<OTTipoPrueba> otSeleccionados = tblTipoPrueba.getSelectionModel().getSelectedItems();
        if (otSeleccionados.size() == 0) {
            final Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Selección registro");
            alert.setHeaderText(getName());
            alert.setContentText("Debe seleccionar registro a procesar");
            alert.showAndWait();

        } else {

            if (otSeleccionados != null && !otSeleccionados.isEmpty()) {
                final List<TipoPrueba> tipoPrueba = new ArrayList<TipoPrueba>(otSeleccionados.size());
                for (final OTTipoPrueba ot : otSeleccionados) {
                    tipoPrueba.add(ot.getTipoPrueba());
                }
                delete(tipoPrueba);
                tblTipoPrueba.getSelectionModel().clearSelection();
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
            TipoPrueba tipoPrueba = null;
            if (entitySelected != null && entitySelected instanceof TipoPrueba) {
                tipoPrueba = (TipoPrueba) entitySelected;
            } else {
                tipoPrueba = new TipoPrueba();
            }
            tipoPrueba.setName(txtNombre.getText());
            save(tipoPrueba);
            limpiarControles();
        } else {
            lblError.getStyleClass().add("bad");
            lblError.setText("Corregir campos destacados en color rojo");
        }

    }

    private void accionModificar() {
        final OTTipoPrueba tipoPrueba = tblTipoPrueba.getSelectionModel().getSelectedItem();
        if (tipoPrueba != null) {
            txtNombre.setText(tipoPrueba.getName());
            select(tipoPrueba.getTipoPrueba());
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
            tblTipoPrueba.setId("Tipo prueba");
            ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblTipoPrueba);
        }
    }

    private void inicializaTabla() {
        tblTipoPrueba.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        colId.setCellValueFactory(new PropertyValueFactory<OTTipoPrueba, Long>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<OTTipoPrueba, String>("name"));
    }

    @FXML
    public void initialize() {
        inicializaTabla();
        accionClicTabla();
        mnuAgregar.setOnAction(this);
        mnuGrabar.setOnAction(this);
        mnuModificar.setOnAction(this);
        mnuEliminar.setOnAction(this);
        mnItemEliminar.setOnAction(this);
        mnItemModificar.setOnAction(this);
        menuExportar.setOnAction(this);
        mnuExportar.setOnAction(this);

        mnuModificar.setDisable(true);
        mnuEliminar.setDisable(true);
        mnItemEliminar.setDisable(true);
        mnItemModificar.setDisable(true);
    }

    private void limpiarControles() {
        txtNombre.clear();
        select(null);
    }

    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            final Object entity = list.get(0);
            if (entity instanceof TipoPrueba) {
                final ObservableList<OTTipoPrueba> oList = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    oList.add(new OTTipoPrueba((TipoPrueba) iEntity));
                }
                tblTipoPrueba.setItems(oList);
            }
        }
    }

    @Override
    public void onDeleted(IEntity entity) {
        tblTipoPrueba.getItems().remove(new OTTipoPrueba((TipoPrueba) entity));
    }

    @Override
    public void onSaved(IEntity otObject) {
        final OTTipoPrueba tipoPrueba = new OTTipoPrueba((TipoPrueba) otObject);
        final int indice = tblTipoPrueba.getItems().lastIndexOf(tipoPrueba);
        if (indice != -1) {
            tblTipoPrueba.getItems().set(indice, tipoPrueba);
        } else {
            tblTipoPrueba.getItems().add(tipoPrueba);
        }
    }

    private void removeAllStyles() {
        removeAllStyle(lblError);
        removeAllStyle(txtNombre);
    }

    @Override
    public boolean validate() {
        boolean valida = true;
        if (txtNombre.getText() == null || txtNombre.getText().equals("")) {
            txtNombre.getStyleClass().add("bad");
            valida = false;
        }
        if (txtNombre.getText() != null && txtNombre.getText().length() > TipoPruebaView.LARGO_CAMPO_TEXT) {
            txtNombre.getStyleClass().add("bad");
            valida = false;
        }
        return valida;
    }

}
