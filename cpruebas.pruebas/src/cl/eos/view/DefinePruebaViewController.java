package cl.eos.view;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.Prueba;
import cl.eos.view.dnd.EjeTematicoDND;
import cl.eos.view.dnd.HabilidadDND;
import cl.eos.view.editablecells.EditingCellRespuesta;

public class DefinePruebaViewController extends AFormView {

  private static final String VALID_LETTERS = "ABCDEVF";

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
  private TableColumn<RegistroDefinePrueba, Habilidad> habCol;
  @FXML
  private TableColumn<RegistroDefinePrueba, EjeTematico> ejeCol;
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

  private ObservableList<RegistroDefinePrueba> registros;

  @FXML
  public void initialize() {

    tblRegistroDefinePrueba.setEditable(true);
    preguntaCol.setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, Integer>(
        "numero"));
    preguntaCol.setStyle("-fx-alignment: CENTER;");
    respuestaCol.setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, String>(
        "respuesta"));
    respuestaCol.setEditable(true);
    respuestaCol.setStyle("-fx-alignment: CENTER;");

    vfCol.setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, Boolean>(
        "verdaderoFalso"));
    vfCol.setCellFactory(CheckBoxTableCell.forTableColumn(vfCol));
    vfCol.setEditable(true);

    mentalCol
        .setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, Boolean>("mental"));
    mentalCol.setCellFactory(CheckBoxTableCell.forTableColumn(mentalCol));
    mentalCol.setEditable(true);
    
    
    habCol.setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, Habilidad>(
        "habilidad"));
    habCol
        .setCellFactory(new Callback<TableColumn<RegistroDefinePrueba, Habilidad>, TableCell<RegistroDefinePrueba, Habilidad>>() {
          @Override
          public TableCell<RegistroDefinePrueba, Habilidad> call(
              TableColumn<RegistroDefinePrueba, Habilidad> p) {
            return new HabilidadDND();
          }
        });
    ejeCol.setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, EjeTematico>(
        "ejeTematico"));
    ejeCol
        .setCellFactory(new Callback<TableColumn<RegistroDefinePrueba, EjeTematico>, TableCell<RegistroDefinePrueba, EjeTematico>>() {
          @Override
          public TableCell<RegistroDefinePrueba, EjeTematico> call(
              TableColumn<RegistroDefinePrueba, EjeTematico> p) {
            return new EjeTematicoDND();
          }
        });
    habilidadCol.setCellValueFactory(new PropertyValueFactory<Habilidad, String>("name"));
    ejeTematicoCol.setCellValueFactory(new PropertyValueFactory<EjeTematico, String>("name"));



    tblHabilidades.setOnDragDetected(new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        Dragboard db = tblHabilidades.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putIfAbsent(HabilidadDND.habilidadTrackDataFormat, tblHabilidades
            .getSelectionModel().getSelectedItem());
        db.setContent(content);
        event.consume();
      }
    });
    tblEjesTematicos.setOnDragDetected(new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        Dragboard db = tblHabilidades.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putIfAbsent(EjeTematicoDND.ejeTematicoTrackDataFormat, tblEjesTematicos
            .getSelectionModel().getSelectedItem());
        db.setContent(content);
        event.consume();
      }
    });
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
      registros = FXCollections.observableArrayList();
      prueba = (Prueba) entity;
      if (prueba.getResponses() != null) {
        String respuestas = prueba.getResponses();
        int nroPreguntas = prueba.getNroPreguntas();

        for (int n = 0; n < nroPreguntas; n++) {
          RegistroDefinePrueba registro = new RegistroDefinePrueba();
          registro.setNumero(n + 1);
          registro.setRespuesta(respuestas.substring(n, n + 1));
          registros.add(registro);
        }
      } else {
        int nroPreguntas = prueba.getNroPreguntas();
        for (int n = 0; n < nroPreguntas; n++) {
          RegistroDefinePrueba registro = new RegistroDefinePrueba();
          registro.setNumero(n + 1);
          registros.add(registro);
        }
      }

      respuestaCol
          .setCellFactory(new Callback<TableColumn<RegistroDefinePrueba, String>, TableCell<RegistroDefinePrueba, String>>() {
            @Override
            public TableCell<RegistroDefinePrueba, String> call(
                final TableColumn<RegistroDefinePrueba, String> column) {
              EditingCellRespuesta editing = new EditingCellRespuesta();
              editing.setValidValues(VALID_LETTERS.substring(0, prueba.getAlternativas()));
              return editing;
            }
          });

      tblRegistroDefinePrueba.setItems(registros);

    }
  }



}
