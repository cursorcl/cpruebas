package cl.eos.view;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import cl.eos.NivelEvaluacionActivator;
import cl.eos.controller.NivelEvaluacionControllerEdicion;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.view.IView;
import cl.eos.persistence.models.NivelEvaluacion;
import cl.eos.persistence.models.RangoEvaluacion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class NivelEvaluacionRangoEvaluacionController extends AFormView implements EventHandler<ActionEvent> {
    @FXML
    private TableView<RangoEvaluacion> tblRangos;
    @FXML
    private TableColumn<RangoEvaluacion, String> colRangosNombre;
    @FXML
    private TableColumn<RangoEvaluacion, String> colRangosAbrev;
    @FXML
    private TableColumn<RangoEvaluacion, Float> colRangoMinimo;
    @FXML
    private TableColumn<RangoEvaluacion, Float> colRangoMaximo;
    @FXML
    private TableView<NivelEvaluacion> tblNiveles;
    @FXML
    private TableColumn<NivelEvaluacion, String> colNivelNombre;
    @FXML
    private MenuItem mnuModificar;
    @FXML
    private MenuItem mnuEliminar;
    @FXML
    private MenuItem mnuItemEliminar;
    @FXML
    private MenuItem mnuAgregar;
    @FXML
    private MenuItem mnuItemAgregar;
    @FXML
    private Button btnModificar;
    @FXML
    private MenuItem mnuItemModificar;

    public NivelEvaluacionRangoEvaluacionController() {
        setTitle("Niveles de Evaluación");
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == mnuModificar || event.getSource() == mnuItemModificar
                || event.getSource() == btnModificar) {
            mostrarVentanaModificar();
        } else if (event.getSource() == mnuAgregar || event.getSource() == mnuItemAgregar) {
            mostrarVentanaAgregar();
        } else if (event.getSource() == mnuEliminar || event.getSource() == mnuItemEliminar) {
            final NivelEvaluacion nivel = (NivelEvaluacion) getSelectedEntity();
            while (nivel.getRangos().size() > 0) {
                delete(nivel.getRangos());
            }
            delete(nivel);
        }
    }

    private void inicializaTabla() {
        tblNiveles.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        colNivelNombre.setCellValueFactory(new PropertyValueFactory<NivelEvaluacion, String>("name"));
        tblRangos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        colRangosNombre.setCellValueFactory(new PropertyValueFactory<RangoEvaluacion, String>("name"));
        colRangosAbrev.setCellValueFactory(new PropertyValueFactory<RangoEvaluacion, String>("abreviacion"));
        colRangoMinimo.setCellValueFactory(new PropertyValueFactory<RangoEvaluacion, Float>("minimo"));
        colRangoMaximo.setCellValueFactory(new PropertyValueFactory<RangoEvaluacion, Float>("maximo"));

        tblNiveles.setOnMouseClicked(arg0 -> {
            final NivelEvaluacion nivel = tblNiveles.getSelectionModel().getSelectedItem();
            if (nivel != null) {
                select(nivel);
                final Collection<RangoEvaluacion> lstRangos = nivel.getRangos();
                tblRangos.getItems().clear();
                tblRangos.getItems().setAll(lstRangos);
            }
        });
    }

    @FXML
    public void initialize() {
        inicializaTabla();
        mnuModificar.setOnAction(this);
        mnuItemModificar.setOnAction(this);
        btnModificar.setOnAction(this);
        mnuAgregar.setOnAction(this);
        mnuItemAgregar.setOnAction(this);
        mnuEliminar.setOnAction(this);
        mnuItemEliminar.setOnAction(this);
    }

    public void mostrarVentanaAgregar() {
        final NivelEvaluacionControllerEdicion lController = new NivelEvaluacionControllerEdicion();
        final URL url = NivelEvaluacionActivator.class
                .getResource("/cl/eos/view/cpruebas.niveles.evaluacion.rango.evaluacion.edicion.fxml");
        final FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            final Parent pane = (Parent) fxmlLoader.load(url.openStream());
            final IView view = fxmlLoader.getController();
            view.setPanel(pane);
            lController.addView(view);
            final Stage ventana = new Stage();
            ventana.setTitle("Venta Dos");
            final Scene scene = new Scene(pane);
            ventana.initStyle(StageStyle.UNDECORATED);
            ventana.setScene(scene);
            ventana.show();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void mostrarVentanaModificar() {
        final NivelEvaluacionControllerEdicion lController = new NivelEvaluacionControllerEdicion();
        final URL url = NivelEvaluacionActivator.class
                .getResource("/cl/eos/view/cpruebas.niveles.evaluacion.rango.evaluacion.edicion.fxml");
        final FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            final Parent pane = (Parent) fxmlLoader.load(url.openStream());
            final IView view = fxmlLoader.getController();
            view.setPanel(pane);
            lController.addView(view);
            final Stage ventana = new Stage();
            ventana.setTitle("Venta Dos");
            final Scene scene = new Scene(pane);
            ventana.initStyle(StageStyle.UNDECORATED);
            ventana.setScene(scene);
            ventana.show();

            final IEntity nivel = getSelectedEntity();

            lController.findById(NivelEvaluacion.class, nivel.getId());

        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            final Object entity = list.get(0);
            if (entity instanceof NivelEvaluacion) {
                final ObservableList<NivelEvaluacion> oList = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    oList.add((NivelEvaluacion) iEntity);
                }
                tblNiveles.getItems().setAll(oList);
            }
        }
    }
}
