package cl.eos.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTAlumno;
import cl.eos.restful.tables.R_Alumno;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.MapBuilder;
import cl.eos.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AlumnosTableTreeView extends AFormView implements EventHandler<ActionEvent> {

  private static final int LARGO_CAMPO_TEXT = 100;
  static final Logger LOG = Logger.getLogger(AlumnosTableTreeView.class.getName());

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
  private MenuItem mnuImportar;

  @FXML
  private TextField txtRut;

  @FXML
  private TextField txtNombres;

  @FXML
  private TextField txtAPaterno;

  @FXML
  private TextField txtAMaterno;

  @FXML
  private TextField txtDireccion;

  @FXML
  private ComboBox<R_Colegio> cmbColegio;
  @FXML
  private ComboBox<R_Colegio> cmbSelectColegio;

  @FXML
  private ComboBox<R_Curso> cmbCurso;

  @FXML
  private ComboBox<R_Curso> cmbSelectCurso;

  @FXML
  private Label lblError;

  @FXML
  private TableView<OTAlumno> tblAlumnos;

  @FXML
  private TableColumn<OTAlumno, Long> colId;

  @FXML
  private TableColumn<OTAlumno, String> colRut;

  @FXML
  private TableColumn<OTAlumno, String> colName;

  @FXML
  private TableColumn<OTAlumno, String> colPaterno;

  @FXML
  private TableColumn<OTAlumno, String> colMaterno;
  @FXML
  private TableColumn<OTAlumno, String> colDireccion;

  @FXML
  private TableColumn<OTAlumno, String> colTipoAlumno;

  @FXML
  private ComboBox<R_TipoAlumno> cmbTipoAlumno;
  @FXML
  private Button btnVer;

  private ObservableList<R_Curso> oListCursos;
  private ObservableList<R_TipoAlumno> oListTipoAlumno;
  ObservableList<OTAlumno> lstAlumnos;

  private void accionClicTabla() {
    tblAlumnos.setOnMouseClicked(event -> {
      final ObservableList<OTAlumno> selected = tblAlumnos.getSelectionModel().getSelectedItems();

      if (selected.size() > 1) {
        mnItemModificar.setDisable(true);
        mnItemEliminar.setDisable(false);
        mnuModificar.setDisable(true);
        mnuEliminar.setDisable(false);
      } else if (selected.size() == 1) {

        mnItemModificar.setDisable(false);
        mnItemEliminar.setDisable(false);
        mnuModificar.setDisable(false);
        mnuEliminar.setDisable(false);
      }
    });
  }

  private void accionEliminar() {
    final ObservableList<OTAlumno> otSeleccionados = tblAlumnos.getSelectionModel().getSelectedItems();

    if (otSeleccionados == null || otSeleccionados.isEmpty()) {
      final Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Selección registro");
      alert.setHeaderText(getName());
      alert.setContentText("Debe seleccionar registro a procesar");
      alert.showAndWait();
      return;
    }

    final List<R_Alumno> lstAlumno = new ArrayList<>(otSeleccionados.size());
    for (final OTAlumno ot : otSeleccionados) {
      if (ot.getAlumno() != null) {
        final R_Alumno item = ot.getAlumno();
        lstAlumno.add(item);
      }
    }
    delete(lstAlumno);
    tblAlumnos.getSelectionModel().clearSelection();
    limpiarControles();
  }

  private void accionGrabar() {
    final IEntity entitySelected = getSelectedEntity();
    removeAllStyles();
    if (validate()) {
      if (lblError != null) {
        lblError.setText(" ");
      }
      R_Alumno alumno;
      if (entitySelected != null && entitySelected instanceof R_Alumno) {
        alumno = (R_Alumno) entitySelected;
      } else {
        alumno = new R_Alumno.Builder().id(Utils.getLastIndex()).build();
      }
      alumno.setRut(txtRut.getText());
      alumno.setName(txtNombres.getText());
      alumno.setPaterno(txtAPaterno.getText());
      alumno.setMaterno(txtAMaterno.getText());
      alumno.setDireccion(txtDireccion.getText());
      alumno.setColegio_id(cmbColegio.getValue().getId());
      alumno.setCurso_id(cmbCurso.getValue().getId());
      alumno.setTipoalumno_id(cmbTipoAlumno.getValue().getId());
      save(alumno);
    } else {
      lblError.getStyleClass().add("bad");
      lblError.setText("Corregir campos destacados en color rojo");
    }
    limpiarControles();
  }

  private void accionModificar() {
    final OTAlumno alumno = tblAlumnos.getSelectionModel().getSelectedItem();
    if (alumno != null) {
      txtRut.setText(alumno.getRut());
      txtNombres.setText(alumno.getName());
      txtAPaterno.setText(alumno.getPaterno());
      txtAMaterno.setText(alumno.getMaterno());
      txtDireccion.setText(alumno.getDireccion());
      cmbColegio.setValue(cmbSelectColegio.getSelectionModel().getSelectedItem());
      cmbCurso.setValue(cmbSelectCurso.getSelectionModel().getSelectedItem());
      cmbTipoAlumno.setValue(alumno.getTipoAlumno());
      select(alumno.getAlumno());
    }
  }

  private ObservableList<R_Curso> obtenerCursos(final R_Colegio colegioSeleccionado) {
    if (colegioSeleccionado == null)
      return null;

    final ObservableList<R_Curso> oList = FXCollections.observableArrayList();
    for (final R_Curso object : oListCursos) {
      if (object.getColegio_id().equals(colegioSeleccionado.getId())) {
        oList.add(object);
      }
    }
    return oList;
  }

  @Override
  public void handle(ActionEvent event) {
    final Object source = event.getSource();
    if (source == mnuModificar || source == mnItemModificar) {
      accionModificar();
    } else if (source == mnuGrabar) {
      accionGrabar();
    } else if (source == mnuEliminar || source == mnItemEliminar) {
      accionEliminar();
    } else if (source == mnuAgregar) {
      limpiarControles();
    } else if (source == mnuExportar || source == menuExportar) {
      tblAlumnos.setId("Alumnos");
      ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblAlumnos);
    } else if (source == cmbColegio) {
      ObservableList<R_Curso> lst = obtenerCursos(cmbColegio.getSelectionModel().getSelectedItem());
      cmbCurso.setItems(lst);
    } else if (source == cmbSelectColegio) {
      ObservableList<R_Curso> lst = obtenerCursos(cmbSelectColegio.getSelectionModel().getSelectedItem());
      cmbSelectCurso.setItems(lst);
    } else if (source == btnVer) {

      Map<String, Object> params = MapBuilder.<String, Object>unordered()
          .put("colegio_id", cmbSelectColegio.getSelectionModel().getSelectedItem().getId())
          .put("curso_id", cmbSelectCurso.getSelectionModel().getSelectedItem().getId()).build();
      controller.findByParam(R_Alumno.class, params, this);
    }
  }

  private void inicializaTabla() {
    tblAlumnos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    colId.setCellValueFactory(new PropertyValueFactory<OTAlumno, Long>("id"));
    colRut.setCellValueFactory(new PropertyValueFactory<OTAlumno, String>("rut"));
    colName.setCellValueFactory(new PropertyValueFactory<OTAlumno, String>("name"));
    colPaterno.setCellValueFactory(new PropertyValueFactory<OTAlumno, String>("paterno"));
    colMaterno.setCellValueFactory(new PropertyValueFactory<OTAlumno, String>("materno"));
    colTipoAlumno.setCellValueFactory(new PropertyValueFactory<OTAlumno, String>("tipoAlumno"));
  }

  @FXML
  public void initialize() {
    setTitle("Alumnos");
    inicializaTabla();
    accionClicTabla();

    mnuAgregar.setOnAction(this);
    mnuGrabar.setOnAction(this);
    mnuModificar.setOnAction(this);
    mnuEliminar.setOnAction(this);
    mnItemEliminar.setOnAction(this);
    mnItemModificar.setOnAction(this);

    mnuExportar.setOnAction(this);
    mnuImportar.setOnAction(this);

    mnItemModificar.setDisable(true);
    mnuModificar.setDisable(true);
    mnuEliminar.setDisable(true);
    mnItemEliminar.setDisable(true);

    cmbColegio.setOnAction(this);
    cmbSelectColegio.setOnAction(this);

    btnVer.setOnAction(this);

  }

  private void limpiarControles() {
    txtRut.clear();
    txtNombres.clear();
    txtAPaterno.clear();
    txtAMaterno.clear();
    txtDireccion.clear();
    cmbColegio.getSelectionModel().clearSelection();
    cmbCurso.getSelectionModel().clearSelection();
    tblAlumnos.getSelectionModel().clearSelection();
    cmbTipoAlumno.getSelectionModel().clearSelection();
    select(null);
  }

  @Override
  public void onDataArrived(List<Object> list) {

    if (list != null && !list.isEmpty()) {
      final Object entity = list.get(0);
      if (entity instanceof R_Alumno) {
        lstAlumnos = FXCollections.observableArrayList();
        for (final Object iEntity : list) {
          R_Alumno alumno = (R_Alumno) iEntity;
          final OTAlumno ot = new OTAlumno(alumno);
          Optional<R_TipoAlumno> op =
              oListTipoAlumno.stream().filter(t -> t.getId().equals(alumno.getTipoalumno_id())).findFirst();
          ot.setTipoAlumno(op.get());
          lstAlumnos.add(ot);
        }
        tblAlumnos.setItems(lstAlumnos);

      } else if (entity instanceof R_Curso) {
        oListCursos = FXCollections.observableArrayList();
        for (final Object iEntity : list) {
          oListCursos.add((R_Curso) iEntity);
        }
      } else if (entity instanceof R_Colegio) {
        final ObservableList<R_Colegio> oList = FXCollections.observableArrayList();
        for (final Object iEntity : list) {
          oList.add((R_Colegio) iEntity);
        }
        cmbColegio.setItems(oList);

        final ObservableList<R_Colegio> oLstSelColegio = FXCollections.observableArrayList();
        for (final Object iEntity : list) {
          oLstSelColegio.add((R_Colegio) iEntity);
        }
        cmbSelectColegio.setItems(oLstSelColegio);

      } else if (entity instanceof R_TipoAlumno) {
        oListTipoAlumno = FXCollections.observableArrayList();
        for (final Object iEntity : list) {
          oListTipoAlumno.add((R_TipoAlumno) iEntity);
        }
        cmbTipoAlumno.setItems(oListTipoAlumno);
      }

    }
  }

  @Override
  public void onDeleted(IEntity entity) {
    final OTAlumno otAlumno = new OTAlumno((R_Alumno) entity);
    tblAlumnos.getItems().remove(otAlumno);
  }

  @Override
  public void onSaved(IEntity otObject) {
    R_Alumno alumno = (R_Alumno) otObject;
    Long idColegio = cmbSelectColegio.getSelectionModel().getSelectedItem().getId();
    Long idCurso = cmbSelectCurso.getSelectionModel().getSelectedItem().getId();

    if (alumno.getColegio_id().equals(idColegio) && alumno.getCurso_id().equals(idCurso)) {
      final OTAlumno otAlumno = new OTAlumno(alumno);
      Optional<R_TipoAlumno> op =
          oListTipoAlumno.stream().filter(t -> t.getId().equals(alumno.getTipoalumno_id())).findFirst();
      otAlumno.setTipoAlumno(op.get());
      final int indice = tblAlumnos.getItems().lastIndexOf(otAlumno);
      if (indice != -1) {
        tblAlumnos.getItems().set(indice, otAlumno);
      } else {
        tblAlumnos.getItems().add(otAlumno);
      }
    }
  }

  private void removeAllStyles() {
    removeAllStyle(lblError);
    removeAllStyle(txtRut);
    removeAllStyle(txtNombres);
    removeAllStyle(txtAPaterno);
    removeAllStyle(txtAMaterno);
    removeAllStyle(cmbColegio);
    removeAllStyle(cmbCurso);
  }

  private boolean validaRut() {
    boolean valida = true;

    if (txtRut.getText() == null || txtRut.getText().trim().length() == 0) {
      txtRut.getStyleClass().add("bad");
      valida = false;
    } else if (!Utils.validarRut(txtRut.getText().trim())) {
      txtRut.getStyleClass().add("bad");
      valida = false;
    }
    if (cmbColegio.getSelectionModel().getSelectedItem() == null) {
      cmbColegio.getStyleClass().add("bad");
      valida = false;
    }
    if (cmbCurso.getSelectionModel().getSelectedItem() == null) {
      cmbColegio.getStyleClass().add("bad");
      valida = false;
    }
    if (txtAMaterno.getText() == null || txtAMaterno.getText().trim().length() == 0) {
      txtAMaterno.getStyleClass().add("bad");
      valida = false;
    }
    if (txtAPaterno.getText() == null || txtAPaterno.getText().trim().length() == 0) {
      txtAPaterno.getStyleClass().add("bad");
      valida = false;
    }
    if (txtNombres.getText() == null || txtNombres.getText().trim().length() == 0) {
      txtNombres.getStyleClass().add("bad");
      valida = false;
    }
    if (txtDireccion.getText() == null || txtDireccion.getText().trim().length() == 0) {
      txtDireccion.getStyleClass().add("bad");
      valida = false;
    }
    return valida;
  }

  @Override
  public boolean validate() {
    boolean valida = true;
    if (cmbColegio.getValue() == null) {
      cmbColegio.getStyleClass().add("bad");
      valida = false;
    }
    if (cmbCurso.getValue() == null) {
      cmbCurso.getStyleClass().add("bad");
      valida = false;
    }
    valida = valida && validaRut();
    if (txtNombres.getText() == null || "".equals(txtNombres.getText())) {
      txtNombres.getStyleClass().add("bad");
      valida = false;
    }
    if (txtNombres.getText() != null && txtNombres.getText().length() > AlumnosTableTreeView.LARGO_CAMPO_TEXT) {
      txtNombres.getStyleClass().add("bad");
      valida = false;
    }

    if (txtAPaterno.getText() == null || "".equals(txtAPaterno.getText())) {
      txtAPaterno.getStyleClass().add("bad");
      valida = false;
    }
    if (txtAPaterno.getText() != null && txtAPaterno.getText().length() > AlumnosTableTreeView.LARGO_CAMPO_TEXT) {
      txtAPaterno.getStyleClass().add("bad");
      valida = false;
    }
    if (txtAMaterno.getText() == null || "".equals(txtAMaterno.getText())) {
      txtAMaterno.getStyleClass().add("bad");
      valida = false;
    }
    if (txtAMaterno.getText() != null && txtAMaterno.getText().length() > AlumnosTableTreeView.LARGO_CAMPO_TEXT) {
      txtAMaterno.getStyleClass().add("bad");
      valida = false;
    }
    return valida;
  }
}
