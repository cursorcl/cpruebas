package cl.eos.view;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.TipoPrueba;

public class EjesTematicosView extends AFormView {

  @FXML
  private MenuItem mnuGrabar;

  @FXML
  private MenuItem mnItemEliminar;

  @FXML
  private MenuItem mnItemModificar;

  @FXML
  private TextField txtNombre;

  @FXML
  private ComboBox<TipoPrueba> cmbTipoPrueba;

  @FXML
  private ComboBox<Asignatura> cmbAsignatura;
  @FXML
  private Label lblError;

  @FXML
  private TableView<EjeTematico> tblEjesTematicos;

  @FXML
  private TableColumn<EjeTematico, String> colNombre;

  @FXML
  private TableColumn<EjeTematico, String> colTipoPrueba;

  @FXML
  private TableColumn<EjeTematico, String> colEnsayo;

  public EjesTematicosView() {

  }

  @FXML
  public void initialize() {
    inicializaTabla();
    accionGrabar();
    accionEliminar();
    accionModificar();
    accionClicTabla();
  }

  private void accionGrabar() {
    mnuGrabar.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        IEntity entitySelected = getSelectedEntity();
        removeAllStyles();
        if (validate()) {
          if (lblError != null) {
            lblError.setText(" ");
          }
          EjeTematico ejeTematico = null;
          if (entitySelected != null && entitySelected instanceof EjeTematico) {
            ejeTematico = (EjeTematico) entitySelected;
          } else {
            ejeTematico = new EjeTematico();
          }
          ejeTematico.setName(txtNombre.getText());
          ejeTematico.setTipoprueba(cmbTipoPrueba.getValue());
          ejeTematico.setAsignatura(cmbAsignatura.getValue());
          save(ejeTematico);
        } else {
          lblError.getStyleClass().add("bad");
          lblError.setText("Corregir campos destacados en color rojo");
        }
        limpiarControles();
      }
    });
  }

  private void accionModificar() {
    mnItemModificar.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        EjeTematico ejeTematico = tblEjesTematicos.getSelectionModel().getSelectedItem();
        if (ejeTematico != null) {
          txtNombre.setText(ejeTematico.getName());
          cmbAsignatura.setValue(ejeTematico.getAsignatura());
          cmbTipoPrueba.setValue(ejeTematico.getTipoprueba());
        }
      }
    });
  }

  private void accionClicTabla() {
    tblEjesTematicos.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        ObservableList<EjeTematico> itemsSelec =
            tblEjesTematicos.getSelectionModel().getSelectedItems();

        if (itemsSelec.size() > 1) {
          mnItemModificar.setDisable(false);
          mnItemEliminar.setDisable(true);
        } else if (itemsSelec.size() == 1) {
          select((IEntity) itemsSelec.get(0));
          mnItemModificar.setDisable(true);
          mnItemEliminar.setDisable(true);
        }
      }
    });
  }

  private void removeAllStyles() {
    removeAllStyle(lblError);
    removeAllStyle(txtNombre);
    removeAllStyle(cmbAsignatura);
    removeAllStyle(cmbTipoPrueba);
  }

  @Override
  public void onSaved(IEntity otObject) {
    int indice = tblEjesTematicos.getItems().lastIndexOf(otObject);
    if (indice != -1) {
      tblEjesTematicos.getItems().remove(otObject);
      tblEjesTematicos.getItems().add(indice, (EjeTematico) otObject);
    } else {
      tblEjesTematicos.getItems().add((EjeTematico) otObject);
    }
    System.out.println("Elemento grabando:" + otObject.toString());
  }

  private void limpiarControles() {
    txtNombre.clear();
    cmbAsignatura.getSelectionModel().clearSelection();
    cmbTipoPrueba.getSelectionModel().clearSelection();
    select(null);
  }

  private void inicializaTabla() {
    tblEjesTematicos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    colNombre.setCellValueFactory(new PropertyValueFactory<EjeTematico, String>("name"));
    colEnsayo.setCellValueFactory(new PropertyValueFactory<EjeTematico, String>("asignatura"));
    colTipoPrueba.setCellValueFactory(new PropertyValueFactory<EjeTematico, String>("tipoprueba"));
  }


  private void accionEliminar() {
    mnItemEliminar.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        ObservableList<EjeTematico> ejesTematicosSelec =
            tblEjesTematicos.getSelectionModel().getSelectedItems();
        for (EjeTematico ejeTematicoSel : ejesTematicosSelec) {
          delete(ejeTematicoSel);
        }
        tblEjesTematicos.getSelectionModel().clearSelection();
      }
    });
  }

  @Override
  public void onDataArrived(List<Object> list) {
    if (list != null && !list.isEmpty()) {
      Object entity = list.get(0);
      if (entity instanceof EjeTematico) {
        ObservableList<EjeTematico> value = FXCollections.observableArrayList();
        for (Object iEntity : list) {
          value.add((EjeTematico) iEntity);
        }
        tblEjesTematicos.setItems(value);
      } else if (entity instanceof TipoPrueba) {
        ObservableList<TipoPrueba> oList = FXCollections.observableArrayList();
        for (Object iEntity : list) {
          oList.add((TipoPrueba) iEntity);
        }
        cmbTipoPrueba.setItems(oList);
      } else if (entity instanceof Asignatura) {
        ObservableList<Asignatura> oList = FXCollections.observableArrayList();
        for (Object iEntity : list) {
          oList.add((Asignatura) iEntity);
        }
        cmbAsignatura.setItems(oList);

      }
    }
  }
}
