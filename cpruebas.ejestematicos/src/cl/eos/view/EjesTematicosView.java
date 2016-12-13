package cl.eos.view;

import java.util.ArrayList;
import java.util.List;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTEjeTematico;
import cl.eos.persistence.models.SAsignatura;
import cl.eos.persistence.models.SEjeTematico;
import cl.eos.persistence.models.STipoPrueba;
import cl.eos.util.ExcelSheetWriterObj;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class EjesTematicosView extends AFormView implements EventHandler<ActionEvent> {

    @FXML
    private MenuItem mnuAgregar;

    @FXML
    private MenuItem mnuGrabar;

    @FXML
    private MenuItem mnuEliminar;

    @FXML
    private MenuItem mnuModificar;

    @FXML
    private MenuItem menuEliminar;

    @FXML
    private MenuItem menuModificar;

    @FXML
    private MenuItem menuExportar;

    @FXML
    private MenuItem mnuExportar;

    @FXML
    private TextField txtNombre;

    @FXML
    private ComboBox<STipoPrueba> cmbTipoPrueba;

    @FXML
    private ComboBox<SAsignatura> cmbAsignatura;
    @FXML
    private Label lblError;

    @FXML
    private TableView<OTEjeTematico> tblEjesTematicos;

    @FXML
    private TableColumn<OTEjeTematico, Float> colId;
    @FXML
    private TableColumn<OTEjeTematico, String> colNombre;

    @FXML
    private TableColumn<OTEjeTematico, String> colTipoPrueba;

    @FXML
    private TableColumn<OTEjeTematico, String> colEnsayo;

    public EjesTematicosView() {
        setTitle("Ejes Temáticos");
    }

    private void accionClicTabla() {
        tblEjesTematicos.setOnMouseClicked(event -> {
            final ObservableList<OTEjeTematico> itemsSelec = tblEjesTematicos.getSelectionModel().getSelectedItems();

            if (itemsSelec.size() > 1) {
                mnuModificar.setDisable(true);
                mnuEliminar.setDisable(false);

                menuModificar.setDisable(true);
                menuEliminar.setDisable(false);
            } else if (itemsSelec.size() == 1) {

                mnuModificar.setDisable(false);
                mnuEliminar.setDisable(false);

                menuModificar.setDisable(false);
                menuEliminar.setDisable(false);
            }
        });
    }

    private void accionEliminar() {
        final ObservableList<OTEjeTematico> otSeleccionados = tblEjesTematicos.getSelectionModel().getSelectedItems();
        if (otSeleccionados.size() == 0) {
            final Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Selección registro");
            alert.setHeaderText(getName());
            alert.setContentText("Debe seleccionar registro a procesar");
            alert.showAndWait();
        } else {

            if (otSeleccionados != null && !otSeleccionados.isEmpty()) {
                final List<SEjeTematico> ejeTematico = new ArrayList<SEjeTematico>(otSeleccionados.size());
                for (final OTEjeTematico ot : otSeleccionados) {
                    ejeTematico.add(ot.getEjeTematico());
                }
                delete(ejeTematico);
                tblEjesTematicos.getSelectionModel().clearSelection();
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
            SEjeTematico ejeTematico = null;
            if (entitySelected != null && entitySelected instanceof SEjeTematico) {
                ejeTematico = (SEjeTematico) entitySelected;
            } else {
                ejeTematico = new SEjeTematico();
            }
            ejeTematico.setName(txtNombre.getText());
            ejeTematico.setTipoprueba(cmbTipoPrueba.getValue());
            ejeTematico.setAsignatura(cmbAsignatura.getValue());
            save(ejeTematico);
            limpiarControles();
        } else {
            lblError.getStyleClass().add("bad");
            lblError.setText("Corregir campos destacados en color rojo");
        }

    }

    private void accionModificar() {
        final OTEjeTematico ejeTematico = tblEjesTematicos.getSelectionModel().getSelectedItem();
        if (ejeTematico != null) {
            txtNombre.setText(ejeTematico.getName());
            cmbAsignatura.setValue(ejeTematico.getAsignatura());
            cmbTipoPrueba.setValue(ejeTematico.getTipoprueba());
            select(ejeTematico.getEjeTematico());
        }
    }

    @Override
    public void handle(ActionEvent event) {
        final Object source = event.getSource();
        if (source == mnuAgregar) {
            limpiarControles();
        } else if (source == mnuModificar || source == menuModificar) {
            accionModificar();
        } else if (source == mnuGrabar) {
            accionGrabar();
        } else if (source == mnuEliminar || source == menuEliminar) {
            accionEliminar();
        } else if (source == mnuExportar || source == menuExportar) {
            tblEjesTematicos.setId("Ejes temáticos");
            ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblEjesTematicos);
        }

    }

    private void inicializaTabla() {
        tblEjesTematicos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        colId.setCellValueFactory(new PropertyValueFactory<OTEjeTematico, Float>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<OTEjeTematico, String>("name"));
        colEnsayo.setCellValueFactory(new PropertyValueFactory<OTEjeTematico, String>("asignatura"));
        colTipoPrueba.setCellValueFactory(new PropertyValueFactory<OTEjeTematico, String>("tipoprueba"));
    }

    @FXML
    public void initialize() {
        inicializaTabla();
        accionClicTabla();

        mnuAgregar.setOnAction(this);
        mnuGrabar.setOnAction(this);
        mnuModificar.setOnAction(this);
        mnuEliminar.setOnAction(this);
        menuModificar.setOnAction(this);
        menuEliminar.setOnAction(this);
        menuExportar.setOnAction(this);
        mnuExportar.setOnAction(this);

        mnuModificar.setDisable(true);
        mnuEliminar.setDisable(true);
        menuEliminar.setDisable(true);
        menuModificar.setDisable(true);

    }

    private void limpiarControles() {
        txtNombre.clear();
        cmbAsignatura.getSelectionModel().clearSelection();
        cmbTipoPrueba.getSelectionModel().clearSelection();
        select(null);
    }

    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            final Object entity = list.get(0);
            if (entity instanceof SEjeTematico) {
                final ObservableList<OTEjeTematico> value = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    value.add(new OTEjeTematico((SEjeTematico) iEntity));
                }
                tblEjesTematicos.setItems(value);

            } else if (entity instanceof STipoPrueba) {
                final ObservableList<STipoPrueba> oList = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    oList.add((STipoPrueba) iEntity);
                }
                cmbTipoPrueba.setItems(oList);
            } else if (entity instanceof SAsignatura) {
                final ObservableList<SAsignatura> oList = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    oList.add((SAsignatura) iEntity);
                }
                cmbAsignatura.setItems(oList);

            }
        }
    }

    @Override
    public void onDeleted(IEntity entity) {
        tblEjesTematicos.getItems().remove(new OTEjeTematico((SEjeTematico) entity));
    }

    @Override
    public void onSaved(IEntity otObject) {
        final OTEjeTematico ejeTematico = new OTEjeTematico((SEjeTematico) otObject);
        final int indice = tblEjesTematicos.getItems().lastIndexOf(ejeTematico);
        if (indice != -1) {
            tblEjesTematicos.getItems().set(indice, ejeTematico);
        } else {
            tblEjesTematicos.getItems().add(ejeTematico);
        }
    }

    private void removeAllStyles() {
        removeAllStyle(lblError);
        removeAllStyle(txtNombre);
        removeAllStyle(cmbAsignatura);
        removeAllStyle(cmbTipoPrueba);
    }
}
