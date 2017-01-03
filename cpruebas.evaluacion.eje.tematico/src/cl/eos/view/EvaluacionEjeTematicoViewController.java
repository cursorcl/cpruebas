package cl.eos.view;

import java.math.BigDecimal;
import java.util.List;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.restful.tables.R_EvaluacionEjetematico;
import cl.eos.util.Utils;
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
    private TableView<R_EvaluacionEjetematico> tblEvaluacionEjeTematatico;
    @FXML
    private TableColumn<R_EvaluacionEjetematico, Long> colId;
    @FXML
    private TableColumn<R_EvaluacionEjetematico, String> colNombre;
    @FXML
    private TableColumn<R_EvaluacionEjetematico, Float> colMinimo;
    @FXML
    private TableColumn<R_EvaluacionEjetematico, Float> colMaximo;
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
            final R_EvaluacionEjetematico evaEjeTematico = tblEvaluacionEjeTematatico.getSelectionModel()
                    .getSelectedItem();
            if (evaEjeTematico != null) {
                delete(evaEjeTematico);
                limpiarControles();
            }
        }
        if (event.getSource() == mnuModificar || event.getSource() == mnuItemModificar) {
            final R_EvaluacionEjetematico evaluacionEje = tblEvaluacionEjeTematatico.getSelectionModel()
                    .getSelectedItem();
            if (evaluacionEje != null) {
                txtNombre.setText(evaluacionEje.getName());
                fltMinimo.setNumber(new BigDecimal(evaluacionEje.getNrorangomin()));
                fltMaximo.setNumber(new BigDecimal(evaluacionEje.getNrorangomax()));
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
                R_EvaluacionEjetematico eje = null;
                if (entitySelected != null && entitySelected instanceof R_EvaluacionEjetematico) {
                    eje = (R_EvaluacionEjetematico) entitySelected;
                } else {
                    eje = new R_EvaluacionEjetematico.Builder().id(Utils.getLastIndex()).build();
                }
                eje.setName(txtNombre.getText());
                eje.setNrorangomax(fltMaximo.getNumber().floatValue());
                eje.setNrorangomin(fltMinimo.getNumber().floatValue());
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
        colId.setCellValueFactory(new PropertyValueFactory<R_EvaluacionEjetematico, Long>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<R_EvaluacionEjetematico, String>("name"));
        colMinimo.setCellValueFactory(new PropertyValueFactory<R_EvaluacionEjetematico, Float>("nroRangoMin"));
        colMaximo.setCellValueFactory(new PropertyValueFactory<R_EvaluacionEjetematico, Float>("nroRangoMax"));
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
            if (entity instanceof R_EvaluacionEjetematico) {
                final ObservableList<R_EvaluacionEjetematico> oList = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    oList.add((R_EvaluacionEjetematico) iEntity);
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
            tblEvaluacionEjeTematatico.getItems().add((R_EvaluacionEjetematico) otObject);
        } else {
            tblEvaluacionEjeTematatico.getItems().set(index, (R_EvaluacionEjetematico) otObject);
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
