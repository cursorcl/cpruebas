package cl.eos.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTColegio;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.TipoColegio;
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
import javafx.scene.image.ImageView;

public class ColegiosView extends AFormView implements EventHandler<ActionEvent> {

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
    private TextField txtCiudad;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtDireccion;

    @FXML
    private ImageView imgColegio;

    @FXML
    private ComboBox<TipoColegio> cmbTipoColegio;

    @FXML
    private TableView<OTColegio> tblColegio;

    @FXML
    private TableColumn<OTColegio, Long> colId;
    @FXML
    private TableColumn<OTColegio, String> colNombre;

    @FXML
    private TableColumn<OTColegio, String> colDireccion;

    @FXML
    private Label lblError;
    @FXML
    private TableColumn<OTColegio, String> colTipoColegio;

    public ColegiosView() {
        setTitle("Colegios");
    }

    private void accionClicTabla() {
        tblColegio.setOnMouseClicked(event -> {
            final ObservableList<OTColegio> itemsSelec = tblColegio.getSelectionModel().getSelectedItems();
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

    // private void accionButtonImagen() {
    // FileChooser fileChooser = new FileChooser();
    // File file = fileChooser.showOpenDialog(null);
    // if (file != null) {
    // Dimension dim = Utils.getImageDim(file.getPath());
    // if (dim.getHeight() <= 256 && dim.getWidth() <= 256) {
    // try {
    // URL url = file.toURI().toURL();
    // imgColegio.setImage(new Image(url.toString()));
    // } catch (MalformedURLException e) {
    // e.printStackTrace();
    // }
    // }
    // }
    // }

    private void accionEliminar() {
        final ObservableList<OTColegio> otSeleccionados = tblColegio.getSelectionModel().getSelectedItems();
        if (otSeleccionados.size() == 0) {
            final Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Selección registro");
            alert.setHeaderText(getName());
            alert.setContentText("Debe seleccionar registro a procesar");
            alert.showAndWait();
        } else {
            if (otSeleccionados != null && !otSeleccionados.isEmpty()) {
                final List<Colegio> colegio = new ArrayList<Colegio>(otSeleccionados.size());
                for (final OTColegio ot : otSeleccionados) {
                    colegio.add(ot.getColegio());
                }
                delete(colegio);
                tblColegio.getSelectionModel().clearSelection();
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
            Colegio colegio = null;
            if (entitySelected != null && entitySelected instanceof Colegio) {
                colegio = (Colegio) entitySelected;
            } else {
                colegio = new Colegio();
            }
            colegio.setName(txtNombre.getText());
            colegio.setDireccion(txtDireccion.getText());
            colegio.setTipoColegio(cmbTipoColegio.getValue());
            save(colegio);
            limpiarControles();
        } else {
            lblError.getStyleClass().add("bad");
            lblError.setText("Corregir campos destacados en color rojo");
        }

    }

    private void accionModificar() {
        final OTColegio colegio = tblColegio.getSelectionModel().getSelectedItem();
        if (colegio != null) {
            txtNombre.setText(colegio.getName());
            txtDireccion.setText(colegio.getDireccion());
            cmbTipoColegio.setValue(colegio.getTipo());
            select(colegio.getColegio());
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
            // } else if (source == btnImagen) {
            // accionButtonImagen();
        } else if (source == mnuExportar || source == menuExportar) {
            tblColegio.setId("Colegio");
            ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblColegio);
        }
    }

    private void inicializaTabla() {
        tblColegio.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        colId.setCellValueFactory(new PropertyValueFactory<OTColegio, Long>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<OTColegio, String>("name"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<OTColegio, String>("direccion"));

        colTipoColegio.setCellValueFactory(new PropertyValueFactory<OTColegio, String>("tipo"));
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

        // btnImagen.setOnAction(this);
    }

    private void limpiarControles() {
        txtNombre.clear();
        txtDireccion.clear();
        cmbTipoColegio.setValue(null);
        tblColegio.getSelectionModel().clearSelection();
    }

    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            final Object entity = list.get(0);
            if (entity instanceof Colegio) {
                final ObservableList<OTColegio> oList = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    oList.add(new OTColegio((Colegio) iEntity));
                }
                tblColegio.setItems(oList);
            } else if (entity instanceof TipoColegio) {
                final ObservableList<TipoColegio> value = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    value.add((TipoColegio) iEntity);
                }
                cmbTipoColegio.setItems(value);
            }
        }
    }

    @Override
    public void onDeleted(IEntity entity) {
        tblColegio.getItems().remove(new OTColegio((Colegio) entity));
    }

    @Override
    public void onSaved(IEntity otObject) {
        final OTColegio otColegio = new OTColegio((Colegio) otObject);
        final int indice = tblColegio.getItems().lastIndexOf(otColegio);
        if (indice != -1) {
            tblColegio.getItems().set(indice, otColegio);
        } else {
            tblColegio.getItems().add(otColegio);
        }
    }

    private void removeAllStyles() {
        removeAllStyle(lblError);
        removeAllStyle(txtNombre);
        removeAllStyle(txtDireccion);
        removeAllStyle(cmbTipoColegio);
    }

    @Override
    public boolean validate() {
        boolean valida = true;
        if (txtNombre.getText() == null || txtNombre.getText().equals("")) {
            txtNombre.getStyleClass().add("bad");
            valida = false;
        }
        if (txtNombre.getText() != null && txtNombre.getText().length() > ColegiosView.LARGO_CAMPO_TEXT) {
            txtNombre.getStyleClass().add("bad");
            valida = false;
        }
        if (cmbTipoColegio.getValue() == null) {
            cmbTipoColegio.getStyleClass().add("bad");
            valida = false;
        }

        if (Objects.isNull(txtCiudad.getText()) || txtCiudad.getText().isEmpty()) {
            txtCiudad.getStyleClass().add("bad");
            valida = false;
        }
        return valida;
    }

}
