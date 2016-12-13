package cl.eos.view;

import java.util.ArrayList;
import java.util.List;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTProfesor;
import cl.eos.persistence.models.SProfesor;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.Utils;
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

public class ProfesoresView extends AFormView implements EventHandler<ActionEvent> {

    private static final int LARGO_CAMPO_TEXT = 100;

    @FXML
    private MenuItem mnItemEliminar;

    @FXML
    private MenuItem mnItemModificar;

    @FXML
    private MenuItem mnuAgregar;

    @FXML
    private MenuItem mnuGrabar;

    @FXML
    private MenuItem mnuEliminar;

    @FXML
    private MenuItem mnuModificar;

    @FXML
    private MenuItem menuExportar;

    @FXML
    private MenuItem mnuExportar;

    @FXML
    private TextField txtRut;

    @FXML
    private TextField txtNombres;

    @FXML
    private TextField txtAPaterno;

    @FXML
    private TextField txtAMaterno;

    @FXML
    private Label lblError;

    @FXML
    private TableView<OTProfesor> tblProfesores;

    @FXML
    private TableColumn<OTProfesor, Long> colId;

    @FXML
    private TableColumn<OTProfesor, String> colRut;

    @FXML
    private TableColumn<OTProfesor, String> colPaterno;

    @FXML
    private TableColumn<OTProfesor, String> colMaterno;

    @FXML
    private TableColumn<OTProfesor, String> ColNombres;

    public ProfesoresView() {
        setTitle("Profesores");
    }

    private void accionClicTabla() {
        tblProfesores.setOnMouseClicked(event -> {
            final ObservableList<OTProfesor> itemsSelec = tblProfesores.getSelectionModel().getSelectedItems();
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
        final ObservableList<OTProfesor> otSeleccionados = tblProfesores.getSelectionModel().getSelectedItems();
        if (otSeleccionados.size() == 0) {

            final Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Selección registro");
            alert.setHeaderText(getName());
            alert.setContentText("Debe seleccionar registro a procesar");
            alert.showAndWait();
        } else {

            if (otSeleccionados != null && !otSeleccionados.isEmpty()) {
                final List<SProfesor> profesor = new ArrayList<SProfesor>(otSeleccionados.size());
                for (final OTProfesor ot : otSeleccionados) {
                    profesor.add(ot.getProfesor());
                }
                delete(profesor);
                tblProfesores.getSelectionModel().clearSelection();
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
            SProfesor Profesor = null;
            if (entitySelected != null && entitySelected instanceof SProfesor) {
                Profesor = (SProfesor) entitySelected;
            } else {
                Profesor = new SProfesor();
            }
            Profesor.setRut(txtRut.getText());
            Profesor.setName(txtNombres.getText());
            Profesor.setPaterno(txtAPaterno.getText());
            Profesor.setMaterno(txtAMaterno.getText());
            save(Profesor);
            limpiarControles();
        } else {
            lblError.getStyleClass().add("bad");
            lblError.setText("Corregir campos destacados en color rojo");
        }

    }

    private void accionModificar() {
        final OTProfesor profesor = tblProfesores.getSelectionModel().getSelectedItem();
        if (profesor != null) {
            txtRut.setText(profesor.getRut());
            txtNombres.setText(profesor.getName());
            txtAPaterno.setText(profesor.getPaterno());
            txtAMaterno.setText(profesor.getMaterno());
            select(profesor.getProfesor());
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
            tblProfesores.setId("Profesores");
            ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblProfesores);
        }
    }

    private void inicializaTabla() {
        tblProfesores.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        colId.setCellValueFactory(new PropertyValueFactory<OTProfesor, Long>("id"));
        colRut.setCellValueFactory(new PropertyValueFactory<OTProfesor, String>("rut"));
        colPaterno.setCellValueFactory(new PropertyValueFactory<OTProfesor, String>("paterno"));
        colMaterno.setCellValueFactory(new PropertyValueFactory<OTProfesor, String>("materno"));
        ColNombres.setCellValueFactory(new PropertyValueFactory<OTProfesor, String>("name"));
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
        mnuExportar.setOnAction(this);
        menuExportar.setOnAction(this);

        mnuModificar.setDisable(true);
        mnuEliminar.setDisable(true);
        mnItemEliminar.setDisable(true);
        mnItemModificar.setDisable(true);
    }

    private void limpiarControles() {
        txtRut.clear();
        txtNombres.clear();
        txtAPaterno.clear();
        txtAMaterno.clear();
        select(null);
    }

    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            final Object entity = list.get(0);
            if (entity instanceof SProfesor) {
                final ObservableList<OTProfesor> oList = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    oList.add(new OTProfesor((SProfesor) iEntity));
                }
                tblProfesores.setItems(oList);
            }
        }
    }

    @Override
    public void onDeleted(IEntity entity) {
        tblProfesores.getItems().remove(new OTProfesor((SProfesor) entity));
    }

    @Override
    public void onSaved(IEntity otObject) {
        final OTProfesor profesor = new OTProfesor((SProfesor) otObject);
        final int indice = tblProfesores.getItems().lastIndexOf(profesor);
        if (indice != -1) {
            tblProfesores.getItems().set(indice, profesor);
        } else {
            tblProfesores.getItems().add(profesor);
        }
    }

    private void removeAllStyles() {
        removeAllStyle(lblError);
        removeAllStyle(txtRut);
        removeAllStyle(txtNombres);
        removeAllStyle(txtAPaterno);
        removeAllStyle(txtAMaterno);
    }

    private boolean validaRut() {
        boolean valida;
        final String strRut = txtRut.getText();
        if (strRut.length() > 0) {
            if (Utils.validarRut(strRut)) {
                valida = true;
            } else {
                txtRut.getStyleClass().add("bad");
                valida = false;
            }
        } else {
            txtRut.getStyleClass().add("bad");
            valida = false;
        }
        return valida;
    }

    @Override
    public boolean validate() {
        boolean valida = true;
        valida = validaRut();
        if (txtNombres.getText() == null || txtNombres.getText().equals("")) {
            txtNombres.getStyleClass().add("bad");
            valida = false;
        }
        if (txtNombres.getText() != null && txtNombres.getText().length() > ProfesoresView.LARGO_CAMPO_TEXT) {
            txtNombres.getStyleClass().add("bad");
            valida = false;
        }

        if (txtAPaterno.getText() == null || txtAPaterno.getText().equals("")) {
            txtAPaterno.getStyleClass().add("bad");
            valida = false;
        }
        if (txtAPaterno.getText() != null && txtAPaterno.getText().length() > ProfesoresView.LARGO_CAMPO_TEXT) {
            txtAPaterno.getStyleClass().add("bad");
            valida = false;
        }
        if (txtAMaterno.getText() == null || txtAMaterno.getText().equals("")) {
            txtAMaterno.getStyleClass().add("bad");
            valida = false;
        }
        if (txtAMaterno.getText() != null && txtAMaterno.getText().length() > ProfesoresView.LARGO_CAMPO_TEXT) {
            txtAMaterno.getStyleClass().add("bad");
            valida = false;
        }
        return valida;
    }
}
