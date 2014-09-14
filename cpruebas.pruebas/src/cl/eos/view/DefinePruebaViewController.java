package cl.eos.view;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import cl.eos.imp.view.AFormView;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.Habilidad;

public class DefinePruebaViewController extends AFormView {

  @FXML
  private TableView<RegistroDefinePrueba> tblRegistroDefinePrueba;
  @FXML
  private TableColumn<RegistroDefinePrueba, Integer> preguntaCol;
  @FXML
  private TableColumn<RegistroDefinePrueba, String> respuestaCol;
  @FXML
  private TableColumn<RegistroDefinePrueba, Boolean> vfCol;
  @FXML
  private TableColumn<RegistroDefinePrueba, Boolean> mentalCol;


  @FXML
  private TableView<EjeTematico> tblEjesTematicos;
  @FXML
  private TableColumn<EjeTematico, String> ejeTematicoCol;

  @FXML
  private TableView<Habilidad> tblHabilidades;
  @FXML
  private TableColumn<Habilidad, String> habilidadCol;


  @FXML
  public void initialize() {
    preguntaCol.setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, Integer>(
        "numero"));
    respuestaCol.setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, String>(
        "respuesta"));
    vfCol.setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, Boolean>(
        "verdaderoFalso"));
    mentalCol
        .setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, Boolean>("mental"));
    habilidadCol.setCellValueFactory(new PropertyValueFactory<Habilidad, String>("name"));
    ejeTematicoCol.setCellValueFactory(new PropertyValueFactory<EjeTematico, String>("name"));
  }

  @Override
  public void onDataArrived(List<Object> list) {

    if (list != null && !list.isEmpty()) {
      Object entity = list.get(0);
      if (entity instanceof EjeTematico) {
        ObservableList<EjeTematico> ejesTematicos = FXCollections.observableArrayList();
        for (Object lEntity : list) {
          ejesTematicos.add((EjeTematico) lEntity);
        }
        tblEjesTematicos.setItems(ejesTematicos);
      } else if (entity instanceof Habilidad) {
        ObservableList<Habilidad> habilidades = FXCollections.observableArrayList();
        for (Object lEntity : list) {
          habilidades.add((Habilidad) lEntity);
        }
        tblHabilidades.setItems(habilidades);
      }
    }
  }


}
