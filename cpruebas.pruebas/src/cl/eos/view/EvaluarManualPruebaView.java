package cl.eos.view;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.view.ots.OTAlumnosEvaluarManual;
import cl.eos.view.ots.OTRespuestasPrueba;

public class EvaluarManualPruebaView extends AFormView {
  @FXML
  private TextField txtColegio;
  @FXML
  private TextField txtCurso;
  @FXML
  private TextField txtName;
  @FXML
  private TextField txtAsignatura;
  @FXML
  private TextField txtFecha;

  @FXML
  private TableView<OTRespuestasPrueba> tblRespuestasPrueba;
  @FXML
  private TableColumn<OTRespuestasPrueba, Integer> preguntaCol;
  @FXML
  private TableColumn<OTRespuestasPrueba, String> respuestaCol;
  @FXML
  private TableColumn<OTRespuestasPrueba, Boolean> vfCol;
  @FXML
  private TableColumn<OTRespuestasPrueba, Boolean> mentalCol;
  @FXML
  private Button btnListo;
  @FXML
  private TextField txtRespuestas;

  @FXML
  private TableView<OTAlumnosEvaluarManual> tblListadoAlumnos;
  @FXML
  private TableColumn<OTAlumnosEvaluarManual, String> rutCol;
  @FXML
  private TableColumn<OTAlumnosEvaluarManual, String> nombresCol;
  @FXML
  private TableColumn<OTAlumnosEvaluarManual, String> paternoCol;
  @FXML
  private TableColumn<OTAlumnosEvaluarManual, String> maternoCol;
  @FXML
  private TableColumn<OTAlumnosEvaluarManual, String> respuestasCol;
  private Prueba prueba;
  private Curso curso;
  private Colegio colegio;


  @FXML
  public void initialize() {
    rutCol.setCellValueFactory(new PropertyValueFactory<OTAlumnosEvaluarManual, String>("rut"));
    nombresCol.setCellValueFactory(new PropertyValueFactory<OTAlumnosEvaluarManual, String>(
        "nombres"));
    paternoCol.setCellValueFactory(new PropertyValueFactory<OTAlumnosEvaluarManual, String>(
        "paterno"));
    maternoCol.setCellValueFactory(new PropertyValueFactory<OTAlumnosEvaluarManual, String>(
        "materno"));
    respuestasCol.setCellValueFactory(new PropertyValueFactory<OTAlumnosEvaluarManual, String>(
        "respuestas"));

    tblListadoAlumnos.getSelectionModel().selectedItemProperty()
        .addListener(new ChangeListener<OTAlumnosEvaluarManual>() {

          @Override
          public void changed(ObservableValue<? extends OTAlumnosEvaluarManual> observable,
              OTAlumnosEvaluarManual oldValue, OTAlumnosEvaluarManual newValue) {
            txtRespuestas.setText(newValue.getRespuestas());
            ObservableList<OTRespuestasPrueba> lstRespuestas = FXCollections.observableArrayList();
            int n = 0;
            for (RespuestasEsperadasPrueba resp : prueba.getRespuestas()) {
              OTRespuestasPrueba ot = new OTRespuestasPrueba();
              ot.setCalculo(resp.getMental());
              ot.setNroPregunta(resp.getNumero());
              ot.setVerdaderoFalso(resp.getVerdaderoFalso());
              ot.setRespuesta("");
              String strResp = newValue.getRespuestas();
              if (strResp != null && !strResp.isEmpty()) {
                ot.setRespuesta(strResp.substring(n, n + 1));
              }
              lstRespuestas.add(ot);
              n++;
            }
            tblRespuestasPrueba.setItems(lstRespuestas);
          }
        });


    preguntaCol.setCellValueFactory(new PropertyValueFactory<OTRespuestasPrueba, Integer>(
        "nroPregunta"));
    preguntaCol.setStyle("-fx-alignment: CENTER;");
    respuestaCol.setCellValueFactory(new PropertyValueFactory<OTRespuestasPrueba, String>(
        "respuesta"));
    respuestaCol.setEditable(true);
    respuestaCol.setStyle("-fx-alignment: CENTER;");
    vfCol.setCellValueFactory(new PropertyValueFactory<OTRespuestasPrueba, Boolean>(
        "verdaderoFalso"));
    vfCol.setCellFactory(CheckBoxTableCell.forTableColumn(vfCol));
    mentalCol.setCellValueFactory(new PropertyValueFactory<OTRespuestasPrueba, Boolean>("calculo"));
    mentalCol.setCellFactory(CheckBoxTableCell.forTableColumn(vfCol));

  }

  @Override
  public void onFound(IEntity entity) {
    if (entity instanceof Prueba) {
      prueba = (Prueba) entity;
      txtName.setText(prueba.getName());
      txtAsignatura.setText(prueba.getAsignatura().getName());
    } else if (entity instanceof Colegio) {
      colegio = (Colegio) entity;
      txtColegio.setText(colegio.getName());
    } else if (entity instanceof Curso) {
      curso = (Curso) entity;
      txtCurso.setText(curso.getName());

      ObservableList<OTAlumnosEvaluarManual> lstAlumnos = FXCollections.observableArrayList();
      for (Alumno alumno : curso.getAlumnos()) {
        lstAlumnos.add(new OTAlumnosEvaluarManual(alumno, ""));
      }
      tblListadoAlumnos.setItems(lstAlumnos);
    }
  }



  public void setFecha(String fecha) {
    txtFecha.setText(fecha);
  }
}
