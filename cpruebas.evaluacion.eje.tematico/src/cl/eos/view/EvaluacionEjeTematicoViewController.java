package cl.eos.view;

import java.math.BigDecimal;
import java.util.List;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.SEvaluacionEjeTematico;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import jfxtras.labs.scene.control.BigDecimalField;

public class EvaluacionEjeTematicoViewController extends AFormView implements EventHandler<ActionEvent> {

    private static final int LARGO_CAMPO_TEXT = 20;
    @FXML
    private MenuItem mnuGrabar;
    @FXML
    private MenuItem mnuAgregar;
    @FXML
    private MenuItem mnuModificar;
    @FXML
    private MenuItem mnuEliminar;
    @FXML
    private MenuItem mnuItemModificar;
    @FXML
    private MenuItem mnuItemEliminar;
    @FXML
    private TableView<SEvaluacionEjeTematico> tblEvaluacionEjeTematatico;
    @FXML
    private TableColumn<SEvaluacionEjeTematico, Long> colId;
    @FXML
    private TableColumn<SEvaluacionEjeTematico, String> colNombre;
    @FXML
    private TableColumn<SEvaluacionEjeTematico, Float> colMinimo;
    @FXML
    private TableColumn<SEvaluacionEjeTematico, Float> colMaximo;
    @FXML
    private TextField txtNombre;
    @FXML
    private BigDecimalField fltMinimo;
    @FXML
    private BigDecimalField fltMaximo;
    @FXML
    private Label lblError;

    public EvaluacionEjeTematicoViewController() {
        setTitle("Evaluación Eje Temático");
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == mnuEliminar || event.getSource() == mnuItemEliminar) {
            final SEvaluacionEjeTematico evaEjeTematico = tblEvaluacionEjeTematatico.getSelectionModel()
                    .getSelectedItem();
            if (evaEjeTematico != null) {
                delete(evaEjeTematico);
                limpiarControles();
            }
        }
        if (event.getSource() == mnuModificar || event.getSource() == mnuItemModificar) {
            final SEvaluacionEjeTematico evaluacionEje = tblEvaluacionEjeTematatico.getSelectionModel()
                    .getSelectedItem();
            if (evaluacionEje != null) {
                txtNombre.setText(evaluacionEje.getName());
                fltMinimo.setNumber(new BigDecimal(evaluacionEje.getNroRangoMin()));
                fltMaximo.setNumber(new BigDecimal(evaluacionEje.getNroRangoMax()));
                select(evaluacionEje);
            }
        }
        if (event.getSource() == mnuAgregar) {
            limpiarControles();
        }
        if (event.getSource() == mnuGrabar) {
            final IEntity entitySelected = getSelectedEntity();
            removeAllStyles();
            if (validate()) {
                if (lblError != null) {
                    lblError.setText(" ");
                }
                SEvaluacionEjeTematico eje = null;
                if (entitySelected != null && entitySelected instanceof SEvaluacionEjeTematico) {
                    eje = (SEvaluacionEjeTematico) entitySelected;
                } else {
                    eje = new SEvaluacionEjeTematico();
                }
                eje.setName(txtNombre.getText());
                eje.setNroRangoMax(fltMaximo.getNumber().floatValue());
                eje.setNroRangoMin(fltMinimo.getNumber().floatValue());
                save(eje);
                limpiarControles();
            } else {
                lblError.getStyleClass().add("bad");
                lblError.setText("Corregir campos destacados en color rojo");
            }

        }
    }

    private void inicializaTabla() {
        tblEvaluacionEjeTematatico.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        colId.setCellValueFactory(new PropertyValueFactory<SEvaluacionEjeTematico, Long>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<SEvaluacionEjeTematico, String>("name"));
        colMinimo.setCellValueFactory(new PropertyValueFactory<SEvaluacionEjeTematico, Float>("nroRangoMin"));
        colMaximo.setCellValueFactory(new PropertyValueFactory<SEvaluacionEjeTematico, Float>("nroRangoMax"));
    }

    @FXML
    public void initialize() {
        inicializaTabla();

        mnuItemModificar.setOnAction(this);
        mnuItemEliminar.setOnAction(this);
        mnuModificar.setOnAction(this);
        mnuEliminar.setOnAction(this);
        mnuGrabar.setOnAction(this);

        tblEvaluacionEjeTematatico.setOnMouseClicked(event -> {
            mnuItemModificar.setDisable(false);
            mnuItemEliminar.setDisable(false);
            mnuModificar.setDisable(false);
            mnuEliminar.setDisable(false);
        });

    }

    private void limpiarControles() {
        txtNombre.setText("");
        fltMinimo.setNumber(new BigDecimal(0));
        fltMaximo.setNumber(new BigDecimal(0));
        tblEvaluacionEjeTematatico.getSelectionModel().clearSelection();

    }

    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            final Object entity = list.get(0);
            if (entity instanceof SEvaluacionEjeTematico) {
                final ObservableList<SEvaluacionEjeTematico> oList = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    oList.add((SEvaluacionEjeTematico) iEntity);
                }
                tblEvaluacionEjeTematatico.setItems(oList);
            }
        }
    }

    @Override
    public void onDeleted(IEntity entity) {
        tblEvaluacionEjeTematatico.getItems().remove(entity);
    }

    @Override
    public void onSaved(IEntity otObject) {
        final int index = tblEvaluacionEjeTematatico.getItems().indexOf(otObject);
        if (index == -1) {
            tblEvaluacionEjeTematatico.getItems().add((SEvaluacionEjeTematico) otObject);
        } else {
            tblEvaluacionEjeTematatico.getItems().set(index, (SEvaluacionEjeTematico) otObject);
        }
    }

    private void removeAllStyles() {
        removeAllStyle(txtNombre);
        removeAllStyle(fltMinimo);
        removeAllStyle(fltMaximo);
    }

    @Override
    public boolean validate() {
        boolean valida = true;
        if (txtNombre.getText() == null || txtNombre.getText().equals("")) {
            txtNombre.getStyleClass().add("bad");
            valida = false;
        }
        if (txtNombre.getText() != null
                && txtNombre.getText().length() > EvaluacionEjeTematicoViewController.LARGO_CAMPO_TEXT) {
            txtNombre.getStyleClass().add("bad");
            valida = false;
        }
        if (fltMinimo.getNumber() == null) {
            fltMinimo.getStyleClass().add("bad");
            valida = false;
        }
        if (fltMaximo.getNumber() == null) {
            fltMaximo.getStyleClass().add("bad");
            valida = false;
        }

        if (fltMinimo.getNumber() != null && fltMaximo.getNumber() != null
                && fltMinimo.getNumber().compareTo(fltMaximo.getNumber()) >= 0) {
            fltMinimo.getStyleClass().add("bad");
            fltMaximo.getStyleClass().add("bad");
            valida = false;
        }
        return valida;
    }
}
