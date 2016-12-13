package cl.eos.view;

import java.util.ArrayList;
import java.util.List;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTAsignatura;
import cl.eos.persistence.models.SAsignatura;
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

public class AsignaturasView extends AFormView implements EventHandler<ActionEvent> {

    private static final int LARGO_CAMPO_TEXT = 100;
    @FXML
    private MenuItem mnuAgregar;
    @FXML
    private MenuItem mnuGrabar;

    @FXML
    private MenuItem mnuEliminar;

    @FXML
    private MenuItem mnuModificar;

    @FXML
    private MenuItem mnItemEliminar;

    @FXML
    private MenuItem mnItemModificar;

    @FXML
    private MenuItem menuExportar;

    @FXML
    private MenuItem mnuExportar;

    @FXML
    private TextField txtNombre;

    @FXML
    private Label lblError;

    @FXML
    private TableView<OTAsignatura> tblAsignatura;
    @FXML
    private TableColumn<OTAsignatura, Long> colId;
    @FXML
    private TableColumn<OTAsignatura, String> colNombre;

    public AsignaturasView() {
        setTitle("Asignaturas");
    }

    private void accionClicTabla() {
        tblAsignatura.setOnMouseClicked(event -> {
            final ObservableList<OTAsignatura> itemsSelec = tblAsignatura.getSelectionModel().getSelectedItems();
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
        final ObservableList<OTAsignatura> otSeleccionados = tblAsignatura.getSelectionModel().getSelectedItems();

        if (otSeleccionados != null && !otSeleccionados.isEmpty()) {
            final List<SAsignatura> asignatura = new ArrayList<SAsignatura>(otSeleccionados.size());
            for (final OTAsignatura ot : otSeleccionados) {
                asignatura.add(ot.getAsignatura());
            }
            delete(asignatura);
            tblAsignatura.getSelectionModel().clearSelection();
            limpiarControles();
        }
    }

    private void accionGrabar() {
        final IEntity entitySelected = getSelectedEntity();
        removeAllStyles();
        if (validate()) {
            if (validaNombreAsignatura(txtNombre.getText())) {
                final Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Información");
                alert.setHeaderText("SAsignatura repetida");
                alert.setContentText("El nombre de la asignatura se encuentra repetida");
                alert.showAndWait();
            } else {
                if (lblError != null) {
                    lblError.setText(" ");
                }
                SAsignatura asignatura = null;
                if (entitySelected != null && entitySelected instanceof SAsignatura) {
                    asignatura = (SAsignatura) entitySelected;
                } else {
                    asignatura = new SAsignatura();
                }
                asignatura.setName(txtNombre.getText());
                save(asignatura);
                limpiarControles();
            }
        } else {
            lblError.getStyleClass().add("bad");
            lblError.setText("Corregir campos destacados en color rojo");
        }
    }

    private void accionModificar() {
        final OTAsignatura asignatura = tblAsignatura.getSelectionModel().getSelectedItem();
        if (asignatura != null) {
            txtNombre.setText(asignatura.getName());
            select(asignatura.getAsignatura());
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
            tblAsignatura.setId("OTAsignatura");
            ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblAsignatura);
        }
    }

    private void inicializaTabla() {
        tblAsignatura.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        colId.setCellValueFactory(new PropertyValueFactory<OTAsignatura, Long>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<OTAsignatura, String>("name"));
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
        tblAsignatura.getSelectionModel().clearSelection();
    }

    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            final Object entity = list.get(0);
            if (entity instanceof SAsignatura) {
                final ObservableList<OTAsignatura> oList = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    oList.add(new OTAsignatura((SAsignatura) iEntity));
                }
                tblAsignatura.setItems(oList);
            }
        }
    }

    @Override
    public void onDeleted(IEntity entity) {
        tblAsignatura.getItems().remove(new OTAsignatura((SAsignatura) entity));
    }

    @Override
    public void onSaved(IEntity otObject) {
        final OTAsignatura otAsignatura = new OTAsignatura((SAsignatura) otObject);
        final int indice = tblAsignatura.getItems().lastIndexOf(otAsignatura);
        if (indice != -1) {
            tblAsignatura.getItems().set(indice, otAsignatura);
        } else {
            tblAsignatura.getItems().add(otAsignatura);
        }
    }

    private void removeAllStyles() {
        removeAllStyle(lblError);
        removeAllStyle(txtNombre);
    }

    private boolean validaNombreAsignatura(final String nombre) {
        boolean existe = false;
        final ObservableList<OTAsignatura> listaAsignaturas = tblAsignatura.getItems();
        for (final OTAsignatura otAsignatura : listaAsignaturas) {
            if (otAsignatura.getName().toUpperCase().equals(nombre.toUpperCase())) {
                existe = true;
                break;
            }
        }
        return existe;
    }

    @Override
    public boolean validate() {
        boolean valida = true;
        if (txtNombre.getText() == null || txtNombre.getText().equals("")) {
            txtNombre.getStyleClass().add("bad");
            valida = false;
        }
        if (txtNombre.getText() != null && txtNombre.getText().length() > AsignaturasView.LARGO_CAMPO_TEXT) {
            txtNombre.getStyleClass().add("bad");
            valida = false;
        }
        return valida;
    }

}