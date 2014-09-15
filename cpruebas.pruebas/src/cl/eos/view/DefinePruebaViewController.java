package cl.eos.view;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.Prueba;

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

  /**
   * A que prueba pertenece.
   */
  private Prueba prueba;

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


  @Override
  public void onFound(IEntity entity) {
    if (entity instanceof Prueba) {
      ObservableList<RegistroDefinePrueba> registros = FXCollections.observableArrayList();

      prueba = (Prueba) entity;
      if (prueba.getRespuestas() != null) {
        String respuestas = prueba.getRespuestas();
        int nroPreguntas = prueba.getNroPreguntas();
        for (int n = 0; n < nroPreguntas; n++) {
          RegistroDefinePrueba registro = new RegistroDefinePrueba();
          registro.setNumero(n);
          registro.setRespuesta(respuestas.substring(n, n + 1));
          registros.add(registro);
        }
      } else {
        int nroPreguntas = prueba.getNroPreguntas();
        for (int n = 0; n < nroPreguntas; n++) {
          RegistroDefinePrueba registro = new RegistroDefinePrueba();
          registro.setNumero(n);
          registros.add(registro);
        }
      }
      tblRegistroDefinePrueba.setItems(registros);
    }
  }

  public Prueba getPrueba() {
    return prueba;
  }

  public void setPrueba(Prueba prueba) {
    this.prueba = prueba;
    if (this.prueba != null) {
      this.prueba.getAlternativas();
    }
  }

  public static void main(String[] args) {
    String koala = "cualquier palabra";
    System.out.println(koala.substring(2, 3));
  }

}
