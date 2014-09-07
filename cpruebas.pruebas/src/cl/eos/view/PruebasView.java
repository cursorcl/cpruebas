package cl.eos.view;

import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Prueba;

public class PruebasView extends AFormView {

  @FXML
  private TableView<Prueba> tblListadoPruebas;
  @FXML
  private TableColumn<Prueba, Date> fechaCol;
  @FXML
  private TableColumn<Prueba, String> cursoCol;
  @FXML
  private TableColumn<Prueba, String> nameCol;
  @FXML
  private TableColumn<Prueba, String> asignaturaCol;
  @FXML
  private TableColumn<Prueba, String> profesorCol;
  @FXML
  private TableColumn<Prueba, Integer> nroPreguntasCol;
  @FXML
  private TableColumn<Prueba, Integer> formasCol;
  @FXML
  private TableColumn<Prueba, Integer> alternativasCol;

  public PruebasView() {}

  @FXML
  public void initialize() {
    tblListadoPruebas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    fechaCol.setCellValueFactory(new PropertyValueFactory<Prueba, Date>("fecha"));
    nameCol.setCellValueFactory(new PropertyValueFactory<Prueba, String>("name"));
    asignaturaCol.setCellValueFactory(new PropertyValueFactory<Prueba, String>("asignatura"));
    profesorCol.setCellValueFactory(new PropertyValueFactory<Prueba, String>("profesor"));
    formasCol.setCellValueFactory(new PropertyValueFactory<Prueba, Integer>("formas"));
    nroPreguntasCol.setCellValueFactory(new PropertyValueFactory<Prueba, Integer>("nroPreguntas"));
  }

  @Override
  public void onDataArrived(List<IEntity> list) {

    ObservableList<Prueba> pruebas = FXCollections.observableArrayList();
    for (IEntity entity : list) {
      pruebas.add((Prueba) entity);
    }


  }

}
