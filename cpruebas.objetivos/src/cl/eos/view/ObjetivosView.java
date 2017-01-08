package cl.eos.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Objetivo;
import cl.eos.restful.tables.R_TipoCurso;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.Utils;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

public class ObjetivosView extends AFormView implements EventHandler<ActionEvent> {
    private static final int LARGO_CAMPO_TEXT = 1024;
    @FXML private MenuItem mnuAgregar;
    @FXML private MenuItem mnuGrabar;
    @FXML private MenuItem mnuEliminar;
    @FXML private MenuItem mnuModificar;
    @FXML private MenuItem menuEliminar;
    @FXML private MenuItem menuModificar;
    @FXML private MenuItem menuExportar;
    @FXML private MenuItem mnuExportar;
    @FXML private TextField txtNombre;
    @FXML private TextArea txtDescripcion;
    @FXML private ComboBox<R_TipoCurso> cmbTipoCurso;
    @FXML private ComboBox<R_Asignatura> cmbAsignatura;
    @FXML private Label lblError;
    @FXML private TableView<R_Objetivo> tblObjetivos;
    @FXML private TableColumn<R_Objetivo, Float> colId;
    @FXML private TableColumn<R_Objetivo, String> colNombre;
    @FXML private TableColumn<R_Objetivo, String> colDescripcion;
    @FXML private TableColumn<R_Objetivo, R_TipoCurso> colTipoCurso;
    @FXML private TableColumn<R_Objetivo, R_Asignatura> colAsignatura;
    public ObjetivosView() {
        setTitle("Objetivos");
    }
    private void accionClicTabla() {
        tblObjetivos.setOnMouseClicked(event -> {
            final ObservableList<R_Objetivo> itemsSelec = tblObjetivos.getSelectionModel().getSelectedItems();
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
                accionModificar();
            }
        });
    }
    private void accionEliminar() {
        final ObservableList<R_Objetivo> otSeleccionados = tblObjetivos.getSelectionModel().getSelectedItems();
        if (otSeleccionados.size() == 0) {
            final Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Selección registro");
            alert.setHeaderText(getName());
            alert.setContentText("Debe seleccionar registro a procesar");
            alert.showAndWait();
        } else {
            if (otSeleccionados != null && !otSeleccionados.isEmpty()) {
                final List<R_Objetivo> objAborrar = new ArrayList<R_Objetivo>(otSeleccionados.size());
                for (final R_Objetivo ot : otSeleccionados) {
                    objAborrar.add(ot);
                }
                delete(objAborrar);
                tblObjetivos.getSelectionModel().clearSelection();
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
            R_Objetivo objetivo = null;
            if (entitySelected != null && entitySelected instanceof R_Objetivo) {
                objetivo = (R_Objetivo) entitySelected;
            } else {
                objetivo = new R_Objetivo.Builder().id(Utils.getLastIndex()).build();
            }
            objetivo.setName(txtNombre.getText());
            objetivo.setDescripcion(txtDescripcion.getText());
            objetivo.setTipocurso_id(cmbTipoCurso.getValue().getId());
            objetivo.setAsignatura_id(cmbAsignatura.getValue().getId());
            save(objetivo);
            limpiarControles();
        } else {
            lblError.getStyleClass().add("bad");
            lblError.setText("Corregir campos destacados en color rojo");
        }
    }
    private void accionModificar() {
        final R_Objetivo objetivo = tblObjetivos.getSelectionModel().getSelectedItem();
        if (objetivo != null) {
            txtNombre.setText(objetivo.getName());
            txtDescripcion.setText(objetivo.getDescripcion());
            Optional<R_TipoCurso> oCurso = cmbTipoCurso.getItems().stream().filter(t -> t.getId().equals(objetivo.getTipocurso_id())).findFirst();
            cmbTipoCurso.getSelectionModel().select(oCurso.isPresent() ? oCurso.get() : null);
            Optional<R_Asignatura> oAsignatura = cmbAsignatura.getItems().stream().filter(t -> t.getId().equals(objetivo.getAsignatura_id()))
                    .findFirst();
            cmbAsignatura.getSelectionModel().select(oAsignatura.isPresent() ? oAsignatura.get() : null);
            select(objetivo);
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
            tblObjetivos.setId("Ejes temáticos");
            ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblObjetivos);
        }
    }
    private void inicializaTabla() {
        tblObjetivos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        colId.setCellValueFactory(new PropertyValueFactory<R_Objetivo, Float>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<R_Objetivo, String>("name"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<R_Objetivo, String>("descripcion"));
        colTipoCurso.setCellValueFactory(new PropertyValueFactory<R_Objetivo, R_TipoCurso>("tipoCurso"));
        colAsignatura.setCellValueFactory(new PropertyValueFactory<R_Objetivo, R_Asignatura>("asignatura"));
        colDescripcion.setCellFactory(param -> {
            final TableCell<R_Objetivo, String> cell = new TableCell<>();
            final Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Region.USE_COMPUTED_SIZE);
            // text.wrappingWidthProperty().bind(cell.widthProperty());
            text.wrappingWidthProperty().bind(colDescripcion.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
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
        txtDescripcion.wrapTextProperty().set(true);
    }
    private void limpiarControles() {
        txtNombre.clear();
        txtDescripcion.clear();
        cmbAsignatura.getSelectionModel().clearSelection();
        cmbTipoCurso.getSelectionModel().clearSelection();
        select(null);
    }
    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            final Object entity = list.get(0);
            if (entity instanceof R_Objetivo) {
                final ObservableList<R_Objetivo> value = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    value.add((R_Objetivo) iEntity);
                }
                tblObjetivos.setItems(value);
            } else if (entity instanceof R_TipoCurso) {
                final ObservableList<R_TipoCurso> value = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    value.add((R_TipoCurso) iEntity);
                }
                cmbTipoCurso.setItems(value);
            } else if (entity instanceof R_Asignatura) {
                final ObservableList<R_Asignatura> value = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    value.add((R_Asignatura) iEntity);
                }
                cmbAsignatura.setItems(value);
            }
        }
    }
    @Override
    public void onDeleted(IEntity entity) {
        tblObjetivos.getItems().remove(entity);
    }
    @Override
    public void onSaved(IEntity otObject) {
        final R_Objetivo objetivo = (R_Objetivo) otObject;
        final int indice = tblObjetivos.getItems().lastIndexOf(objetivo);
        if (indice != -1) {
            tblObjetivos.getItems().set(indice, objetivo);
        } else {
            tblObjetivos.getItems().add(objetivo);
        }
    }
    private void removeAllStyles() {
        removeAllStyle(lblError);
        removeAllStyle(txtNombre);
        removeAllStyle(txtDescripcion);
        removeAllStyle(cmbTipoCurso);
        removeAllStyle(cmbAsignatura);
    }
    @Override
    public boolean validate() {
        removeAllStyles();
        boolean valida = true;
        if (txtNombre.getText() == null || txtNombre.getText().equals("")) {
            txtNombre.getStyleClass().add("bad");
            valida = false;
        }
        if (txtNombre.getText() != null && txtNombre.getText().length() > ObjetivosView.LARGO_CAMPO_TEXT) {
            txtNombre.getStyleClass().add("bad");
            valida = false;
        }
        if (txtDescripcion.getText() == null || txtNombre.getText().equals("")) {
            txtDescripcion.getStyleClass().add("bad");
            valida = false;
        }
        if (txtDescripcion.getText() != null && txtDescripcion.getText().length() > 2 * ObjetivosView.LARGO_CAMPO_TEXT) {
            txtDescripcion.getStyleClass().add("bad");
            valida = false;
        }
        if (cmbTipoCurso.getValue() == null) {
            cmbTipoCurso.getStyleClass().add("bad");
            valida = false;
        }
        if (cmbAsignatura.getValue() == null) {
            cmbAsignatura.getStyleClass().add("bad");
            valida = false;
        }
        return valida;
    }
}
