package cl.eos.view;

import java.util.ArrayList;
import java.util.List;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTCurso;
import cl.eos.persistence.models.Ciclo;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.TipoCurso;
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

public class CursosView extends AFormView implements EventHandler<ActionEvent> {

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
    private ComboBox<Ciclo> cmbNivel;

    @FXML
    private ComboBox<Colegio> cmbColegio;

    @FXML
    private ComboBox<TipoCurso> cmbTipoCurso;

    @FXML
    private Label lblError;

    @FXML
    private TableView<OTCurso> tblCurso;

    @FXML
    private TableColumn<OTCurso, Long> colId;

    @FXML
    private TableColumn<OTCurso, String> colNombre;

    @FXML
    private TableColumn<OTCurso, String> colNivel;

    @FXML
    private TableColumn<OTCurso, String> colColegio;

    @FXML
    private TableColumn<OTCurso, String> colTpCurso;

    public CursosView() {
        setTitle("Cursos");
    }

    private void accionClicTabla() {
        tblCurso.setOnMouseClicked(event -> {
            final ObservableList<OTCurso> itemsSelec = tblCurso.getSelectionModel().getSelectedItems();
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
        final ObservableList<OTCurso> otSeleccionados = tblCurso.getSelectionModel().getSelectedItems();
        if (otSeleccionados.size() == 0) {
            final Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Selección registro");
            alert.setHeaderText(getName());
            alert.setContentText("Debe seleccionar registro a procesar");
            alert.showAndWait();
        } else {

            if (otSeleccionados != null && !otSeleccionados.isEmpty()) {
                final List<Curso> curso = new ArrayList<Curso>(otSeleccionados.size());
                for (final OTCurso ot : otSeleccionados) {
                    curso.add(ot.getCurso());
                }
                delete(curso);
                tblCurso.getSelectionModel().clearSelection();
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
            Curso curso = null;
            if (entitySelected != null && entitySelected instanceof Curso) {
                curso = (Curso) entitySelected;
            } else {
                curso = new Curso();
            }
            curso.setName(txtNombre.getText());
            curso.setCiclo(cmbNivel.getValue());
            curso.setColegio(cmbColegio.getValue());
            curso.setTipoCurso(cmbTipoCurso.getValue());
            save(curso);
            limpiarControles();
        } else {
            lblError.getStyleClass().add("bad");
            lblError.setText("Corregir campos destacados en color rojo");
        }

    }

    private void accionModificar() {
        final OTCurso curso = tblCurso.getSelectionModel().getSelectedItem();
        if (curso != null) {
            txtNombre.setText(curso.getName());
            cmbNivel.setValue(curso.getCiclo());
            cmbColegio.setValue(curso.getColegio());
            cmbTipoCurso.setValue(curso.getTipoCurso());
            select(curso.getCurso());
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
            tblCurso.setId("Cursos");
            ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblCurso);
        }

    }

    private void inicializaTabla() {
        tblCurso.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        colId.setCellValueFactory(new PropertyValueFactory<OTCurso, Long>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<OTCurso, String>("name"));
        colColegio.setCellValueFactory(new PropertyValueFactory<OTCurso, String>("colegio"));
        colNivel.setCellValueFactory(new PropertyValueFactory<OTCurso, String>("ciclo"));
        colTpCurso.setCellValueFactory(new PropertyValueFactory<OTCurso, String>("tipoCurso"));
    }

    @FXML
    public void initialize() {
        inicializaTabla();
        accionClicTabla();
        mnuAgregar.setOnAction(this);
        mnuGrabar.setOnAction(this);
        mnuModificar.setOnAction(this);
        mnuEliminar.setOnAction(this);
        mnuExportar.setOnAction(this);
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
        cmbNivel.getSelectionModel().clearSelection();
        cmbColegio.getSelectionModel().clearSelection();
        tblCurso.getSelectionModel().clearSelection();
        cmbTipoCurso.getSelectionModel().clearSelection();
        select(null);
    }

    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            final Object entity = list.get(0);
            if (entity instanceof Curso) {
                final ObservableList<OTCurso> value = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    value.add(new OTCurso((Curso) iEntity));
                }
                tblCurso.setItems(value);
            } else if (entity instanceof Colegio) {
                final ObservableList<Colegio> value = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    value.add((Colegio) iEntity);
                }
                cmbColegio.setItems(value);
            } else if (entity instanceof Ciclo) {
                final ObservableList<Ciclo> oList = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    oList.add((Ciclo) iEntity);
                }
                cmbNivel.setItems(oList);
            } else if (entity instanceof TipoCurso) {
                final ObservableList<TipoCurso> oList = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    oList.add((TipoCurso) iEntity);
                }
                cmbTipoCurso.setItems(oList);
            }
        }
    }

    @Override
    public void onDeleted(IEntity entity) {
        tblCurso.getItems().remove(new OTCurso((Curso) entity));
    }

    @Override
    public void onSaved(IEntity otObject) {
        final OTCurso otCurso = new OTCurso((Curso) otObject);
        final int indice = tblCurso.getItems().lastIndexOf(otCurso);
        if (indice != -1) {
            tblCurso.getItems().set(indice, otCurso);
        } else {
            tblCurso.getItems().add(otCurso);
        }
        limpiarControles();
    }

    private void removeAllStyles() {
        removeAllStyle(lblError);
        removeAllStyle(txtNombre);
        removeAllStyle(cmbColegio);
    }

    @Override
    public boolean validate() {
        boolean valida = true;
        if (txtNombre.getText() == null || txtNombre.getText().equals("")) {
            txtNombre.getStyleClass().add("bad");
            valida = false;
        }
        if (txtNombre.getText() != null && txtNombre.getText().length() > CursosView.LARGO_CAMPO_TEXT) {
            txtNombre.getStyleClass().add("bad");
            valida = false;
        }
        if (cmbColegio.getValue() == null) {
            cmbColegio.getStyleClass().add("bad");
            valida = false;
        }
        return valida;
    }
}