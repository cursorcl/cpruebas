package cl.eos.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTEjeTematico;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Ejetematico;
import cl.eos.restful.tables.R_TipoPrueba;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.MapBuilder;
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
    private ComboBox<R_TipoPrueba> cmbTipoPrueba;

    @FXML
    private ComboBox<R_Asignatura> cmbAsignatura;
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

    private boolean b_TipoPrueba;

    private boolean b_EjeTematico;

    private boolean b_Asignatura;

    ObservableList<OTEjeTematico> lstEjes;
    ObservableList<R_TipoPrueba> lstTipoPruebas;
    ObservableList<R_Asignatura> lstAsignaturas;

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
                final List<R_Ejetematico> ejeTematico = new ArrayList<R_Ejetematico>(otSeleccionados.size());
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
            R_Ejetematico ejeTematico = null;
            if (entitySelected != null && entitySelected instanceof R_Ejetematico) {
                ejeTematico = (R_Ejetematico) entitySelected;
            } else {
                ejeTematico = new R_Ejetematico.Builder().id(Utils.getLastIndex()).build();
            }
            ejeTematico.setName(txtNombre.getText());
            ejeTematico.setTipoprueba_id(cmbTipoPrueba.getValue().getId());
            ejeTematico.setAsignatura_id(cmbAsignatura.getValue().getId());
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
        
        cmbAsignatura.setOnAction(h -> {
          R_Asignatura asignatura = cmbAsignatura.getSelectionModel().getSelectedItem();
          if (asignatura == null)
            return;
          Map<String, Object> params =
              MapBuilder.<String, Object>unordered().put("asignatura_id", asignatura.getId()).build();
          controller.findByParam(R_Ejetematico.class, params, this);
        });

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
            if (entity instanceof R_Ejetematico) {
                lstEjes = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    lstEjes.add(new OTEjeTematico((R_Ejetematico) iEntity));
                }
                b_EjeTematico = true;

            } else if (entity instanceof R_TipoPrueba) {
                lstTipoPruebas = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    lstTipoPruebas.add((R_TipoPrueba) iEntity);
                }
                cmbTipoPrueba.setItems(lstTipoPruebas);
                b_TipoPrueba = true;
            } else if (entity instanceof R_Asignatura) {
                lstAsignaturas = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    lstAsignaturas.add((R_Asignatura) iEntity);
                }
                cmbAsignatura.setItems(lstAsignaturas);
                b_Asignatura = true;
            }
            if (b_EjeTematico && b_TipoPrueba && b_Asignatura) {
                for (OTEjeTematico eje : lstEjes) {
                    R_Asignatura asignatura = lstAsignaturas.stream()
                            .filter(a -> a.getId().equals(eje.getEjeTematico().getAsignatura_id())).findFirst().get();
                    R_TipoPrueba tipoPrueba = lstTipoPruebas.stream()
                            .filter(t -> t.getId().equals(eje.getEjeTematico().getTipoprueba_id())).findFirst().get();
                    eje.setAsignatura(asignatura);
                    eje.setTipoprueba(tipoPrueba);
                }
                tblEjesTematicos.setItems(lstEjes);
            }
        }
    }

    @Override
    public void onDeleted(IEntity entity) {
        tblEjesTematicos.getItems().remove(new OTEjeTematico((R_Ejetematico) entity));
    }

    @Override
    public void onSaved(IEntity otObject) {
        R_Ejetematico eje = (R_Ejetematico) otObject;
        R_Asignatura asignatura = controller.findSynchroById(R_Asignatura.class, eje.getAsignatura_id());
        R_TipoPrueba tpoPrueba = controller.findSynchroById(R_TipoPrueba.class, eje.getTipoprueba_id());
        final OTEjeTematico ejeTematico = new OTEjeTematico((R_Ejetematico) otObject, tpoPrueba, asignatura);
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
