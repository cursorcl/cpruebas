package cl.eos.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_TipoColegio;
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
    private ComboBox<R_TipoColegio> cmbTipoColegio;

    @FXML
    private TableView<R_Colegio> tblColegio;

    @FXML
    private TableColumn<R_Colegio, Long> colId;
    @FXML
    private TableColumn<R_Colegio, String> colNombre;

    @FXML
    private TableColumn<R_Colegio, String> colDireccion;

    @FXML
    private Label lblError;
    @FXML
    private TableColumn<R_Colegio, Long> colTipoColegio;
    
    protected ObservableList<R_TipoColegio> lstTipoColegio;

    public ColegiosView() {
        setTitle("Colegios");
    }

    private void accionClicTabla() {
        tblColegio.setOnMouseClicked(event -> {
            final ObservableList<R_Colegio> itemsSelec = tblColegio.getSelectionModel().getSelectedItems();
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
        final ObservableList<R_Colegio> otSeleccionados = tblColegio.getSelectionModel().getSelectedItems();
        if (otSeleccionados.size() == 0) {
            final Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Selección registro");
            alert.setHeaderText(getName());
            alert.setContentText("Debe seleccionar registro a procesar");
            alert.showAndWait();
        } else {
            if (otSeleccionados != null && !otSeleccionados.isEmpty()) {
                final List<R_Colegio> colegio = new ArrayList<>(otSeleccionados.size());
                for (final R_Colegio ot : otSeleccionados) {
                    colegio.add(ot);
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
            R_Colegio colegio = null;
            if (entitySelected != null && entitySelected instanceof R_Colegio) {
                colegio = (R_Colegio) entitySelected;
            } else {
                colegio = new R_Colegio.Builder().id(Utils.getLastIndex()).build();
            }
            colegio.setName(txtNombre.getText());
            colegio.setDireccion(txtDireccion.getText());
            colegio.setTipocolegio_id(cmbTipoColegio.getValue().getId());
            save(colegio);
            limpiarControles();
        } else {
            lblError.getStyleClass().add("bad");
            lblError.setText("Corregir campos destacados en color rojo");
        }

    }

    private void accionModificar() {
        final R_Colegio colegio = tblColegio.getSelectionModel().getSelectedItem();
        if (colegio != null) {
            txtNombre.setText(colegio.getName());
            txtDireccion.setText(colegio.getDireccion());
            R_TipoColegio tColegio =  new R_TipoColegio.Builder().id(colegio.getTipocolegio_id()).build();
            cmbTipoColegio.setValue(tColegio);
            select(colegio);
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
            tblColegio.setId("SColegio");
            ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblColegio);
        }
    }

    private void inicializaTabla() {
        tblColegio.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        colId.setCellValueFactory(new PropertyValueFactory<R_Colegio, Long>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<R_Colegio, String>("name"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<R_Colegio, String>("direccion"));

        colTipoColegio.setCellValueFactory(new PropertyValueFactory<R_Colegio, Long>("tipocolegio_id"));
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
            if (entity instanceof R_Colegio) {
                final ObservableList<R_Colegio> oList = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    oList.add((R_Colegio) iEntity);
                }
                tblColegio.setItems(oList);
            } else if (entity instanceof R_TipoColegio) {
                lstTipoColegio = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    lstTipoColegio.add((R_TipoColegio) iEntity);
                }
                cmbTipoColegio.setItems(lstTipoColegio);
                final  ObservableList<R_TipoColegio> lst = lstTipoColegio;
                colTipoColegio.setCellFactory(column -> {
                  return new TableCell<R_Colegio, Long>() {
                      @Override
                      protected void updateItem(Long item, boolean empty) {
                          super.updateItem(item, empty);
                          setStyle("");
                          if (item == null || empty) {
                              setText(null);
                              
                          } else {
                              // Format date.
                              if(lst != null)
                              {
                                setText(null);
                                R_TipoColegio tipo = lst.stream().filter(t -> t.getId().equals(item)).findAny().orElse(null);
                                if(tipo != null)
                                {
                                  setText(tipo.getName());
                                }
                              }
                          }
                      }
                  };
              });
            }
        }
    }

    @Override
    public void onDeleted(IEntity entity) {
        tblColegio.getItems().remove((R_Colegio) entity);
    }

    @Override
    public void onSaved(IEntity otObject) {
        final R_Colegio otColegio = (R_Colegio) otObject;
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
