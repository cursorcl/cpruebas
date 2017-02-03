package cl.eos.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Objetivo;
import cl.eos.restful.tables.R_TipoCurso;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

public class ObjetivosView extends AFormView implements EventHandler<ActionEvent> {
  private static final int LARGO_CAMPO_TEXT = 1024;
  @FXML
  private MenuItem mnuAgregar;
  @FXML
  private MenuItem mnuGrabar;
  @FXML
  private MenuItem mnuEliminar;
  @FXML
  private MenuItem mnuModificar;
  @FXML
  private MenuItem menuEliminar;
  @FXML
  private MenuItem menuModificar;
  @FXML
  private MenuItem menuExportar;
  @FXML
  private MenuItem mnuExportar;
  @FXML
  private TextField txtNombre;
  @FXML
  private TextArea txtDescripcion;
  @FXML
  private ComboBox<R_TipoCurso> cmbTipoCurso;
  @FXML
  private ComboBox<R_Asignatura> cmbAsignatura;
  @FXML
  private Label lblError;
  @FXML
  private TableView<OTObjetivo> tblObjetivos;
  @FXML
  private TableColumn<OTObjetivo, Float> colId;
  @FXML
  private TableColumn<OTObjetivo, String> colNombre;
  @FXML
  private TableColumn<OTObjetivo, String> colDescripcion;
  @FXML
  private TableColumn<OTObjetivo, R_TipoCurso> colTipoCurso;
  @FXML
  private TableColumn<OTObjetivo, R_Asignatura> colAsignatura;
  private ObservableList<R_TipoCurso> lstTipoCurso;
  private ObservableList<R_Asignatura> lstAsignatura;

  public ObjetivosView() {
    setTitle("Objetivos");
  }

  private void accionClicTabla() {
    tblObjetivos.setOnMouseClicked(event -> {
      final ObservableList<OTObjetivo> itemsSelec =
          tblObjetivos.getSelectionModel().getSelectedItems();
      if (itemsSelec.size() > 1) {
        mnuModificar.setDisable(true);
        mnuEliminar.setDisable(false);
        menuModificar.setDisable(true);
        menuEliminar.setDisable(false);
      } else if (itemsSelec.size() == 1) {
        mnuModificar.setDisable(false);
        mnuEliminar.setDisable(false);
        menuModificar.setDisable(false);
        menuEliminar.setDisable(false);
        accionModificar();
      }
    });
  }

  private void accionEliminar() {
    final ObservableList<OTObjetivo> otSeleccionados =
        tblObjetivos.getSelectionModel().getSelectedItems();
    if (otSeleccionados.size() == 0) {
      final Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Selección registro");
      alert.setHeaderText(getName());
      alert.setContentText("Debe seleccionar registro a procesar");
      alert.showAndWait();
    } else {
      if (otSeleccionados != null && !otSeleccionados.isEmpty()) {
        final List<R_Objetivo> objAborrar = new ArrayList<R_Objetivo>(otSeleccionados.size());
        for (final OTObjetivo ot : otSeleccionados) {
          objAborrar.add(ot.getObjetivo());
        }
        delete(objAborrar);
        tblObjetivos.getSelectionModel().clearSelection();
        limpiarControles();
      }
    }
  }

  private void accionGrabar() {
    final IEntity entitySelected = getSelectedEntity();
    removeAllStyles();
    if (validate()) {
      if (lblError != null) {
        lblError.setText(" ");
      }
      R_Objetivo objetivo = null;
      if (entitySelected != null && entitySelected instanceof R_Objetivo) {
        objetivo = (R_Objetivo) entitySelected;
      } else {
        objetivo = new R_Objetivo.Builder().id(Utils.getLastIndex()).build();
      }
      objetivo.setName(txtNombre.getText());
      objetivo.setDescripcion(txtDescripcion.getText());
      objetivo.setTipocurso_id(cmbTipoCurso.getValue().getId());
      objetivo.setAsignatura_id(cmbAsignatura.getValue().getId());
      save(objetivo);
      limpiarControles();
    } else {
      lblError.getStyleClass().add("bad");
      lblError.setText("Corregir campos destacados en color rojo");
    }
  }

  private void accionModificar() {
    final OTObjetivo objetivo = tblObjetivos.getSelectionModel().getSelectedItem();
    if (objetivo != null) {
      txtNombre.setText(objetivo.getName());
      txtDescripcion.setText(objetivo.getDescripcion());
      cmbTipoCurso.getSelectionModel().select(objetivo.getTipoCurso());
      cmbAsignatura.getSelectionModel().select(objetivo.getAsignatura());
      select(objetivo.getObjetivo());
    }
  }

  private void accionPedirDatos() {
    R_Asignatura asignatura = cmbAsignatura.getSelectionModel().getSelectedItem();
    R_TipoCurso tipoCurso = cmbTipoCurso.getSelectionModel().getSelectedItem();
    if (asignatura == null || tipoCurso == null) {
      return;
    }
    Map<String, Object> params = MapBuilder.<String, Object>unordered()
        .put("asignatura_id", asignatura.getId()).put("tipocurso_id", tipoCurso.getId()).build();

    controller.findByParam(R_Objetivo.class, params, this);
  }

  @Override
  public void handle(ActionEvent event) {
    final Object source = event.getSource();
    if (source == mnuAgregar) {
      limpiarControles();
    } else if (source == mnuModificar || source == menuModificar) {
      accionModificar();
    } else if (source == mnuGrabar) {
      accionGrabar();
    } else if (source == mnuEliminar || source == menuEliminar) {
      accionEliminar();
    } else if (source == mnuExportar || source == menuExportar) {
      tblObjetivos.setId("Ejes temáticos");
      ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblObjetivos);
    } else if (source == cmbAsignatura || source == cmbTipoCurso) {
      accionPedirDatos();
    }
  }

  private void inicializaTabla() {
    tblObjetivos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    colId.setCellValueFactory(new PropertyValueFactory<OTObjetivo, Float>("id"));
    colNombre.setCellValueFactory(new PropertyValueFactory<OTObjetivo, String>("name"));
    colDescripcion.setCellValueFactory(new PropertyValueFactory<OTObjetivo, String>("descripcion"));
    colTipoCurso
        .setCellValueFactory(new PropertyValueFactory<OTObjetivo, R_TipoCurso>("tipoCurso"));
    colAsignatura
        .setCellValueFactory(new PropertyValueFactory<OTObjetivo, R_Asignatura>("asignatura"));
    colDescripcion.setCellFactory(param -> {
      final TableCell<OTObjetivo, String> cell = new TableCell<>();
      final Text text = new Text();
      cell.setGraphic(text);
      cell.setPrefHeight(Region.USE_COMPUTED_SIZE);
      // text.wrappingWidthProperty().bind(cell.widthProperty());
      text.wrappingWidthProperty().bind(colDescripcion.widthProperty());
      text.textProperty().bind(cell.itemProperty());
      return cell;
    });
  }

  @FXML
  public void initialize() {
    inicializaTabla();
    accionClicTabla();
    mnuAgregar.setOnAction(this);
    mnuGrabar.setOnAction(this);
    mnuModificar.setOnAction(this);
    mnuEliminar.setOnAction(this);
    menuModificar.setOnAction(this);
    menuEliminar.setOnAction(this);
    menuExportar.setOnAction(this);
    mnuExportar.setOnAction(this);
    mnuModificar.setDisable(true);
    mnuEliminar.setDisable(true);
    menuEliminar.setDisable(true);
    menuModificar.setDisable(true);
    txtDescripcion.wrapTextProperty().set(true);

    cmbAsignatura.setOnAction(this);
    cmbTipoCurso.setOnAction(this);
  }

  private void limpiarControles() {
    txtNombre.clear();
    txtDescripcion.clear();
    cmbAsignatura.getSelectionModel().clearSelection();
    cmbTipoCurso.getSelectionModel().clearSelection();
    select(null);
  }

  @Override
  public void onDataArrived(List<Object> list) {
    if (list != null && !list.isEmpty()) {
      final Object entity = list.get(0);
      if (entity instanceof R_Objetivo) {
        final ObservableList<OTObjetivo> value = FXCollections.observableArrayList();
        for (final Object iEntity : list) {
          R_Objetivo objetivo = (R_Objetivo) iEntity;
          OTObjetivo ot = makeOTObjetivo(objetivo);
          value.add(ot);
        }
        tblObjetivos.setItems(value);
      } else if (entity instanceof R_TipoCurso) {
        lstTipoCurso = FXCollections.observableArrayList();
        for (final Object iEntity : list) {
          lstTipoCurso.add((R_TipoCurso) iEntity);
        }
        cmbTipoCurso.setItems(lstTipoCurso);

      } else if (entity instanceof R_Asignatura) {
        lstAsignatura = FXCollections.observableArrayList();
        for (final Object iEntity : list) {
          lstAsignatura.add((R_Asignatura) iEntity);
        }
        cmbAsignatura.setItems(lstAsignatura);
      }
    }
  }

  @Override
  public void onDeleted(IEntity entity) {
    tblObjetivos.getItems().remove(entity);
  }

  @Override
  public void onSaved(IEntity otObject) {
    final R_Objetivo objetivo = (R_Objetivo) otObject;
    final int indice = tblObjetivos.getItems().lastIndexOf(objetivo);
    OTObjetivo ot = makeOTObjetivo(objetivo);
    if (indice != -1) {
      tblObjetivos.getItems().set(indice, ot);
    } else {
      tblObjetivos.getItems().add(ot);
    }
  }

  private OTObjetivo makeOTObjetivo(R_Objetivo objetivo) {
    R_TipoCurso tipoCurso = lstTipoCurso.stream()
        .filter(t -> t.getId().equals(objetivo.getTipocurso_id())).findFirst().orElse(null);
    R_Asignatura asignatura = lstAsignatura.stream()
        .filter(a -> a.getId().equals(objetivo.getAsignatura_id())).findFirst().orElse(null);
    return new OTObjetivo.Builder().asignatura(asignatura).tipocurso(tipoCurso).objetivo(objetivo)
        .build();

  }

  private void removeAllStyles() {
    removeAllStyle(lblError);
    removeAllStyle(txtNombre);
    removeAllStyle(txtDescripcion);
    removeAllStyle(cmbTipoCurso);
    removeAllStyle(cmbAsignatura);
  }

  @Override
  public boolean validate() {
    removeAllStyles();
    boolean valida = true;
    if (txtNombre.getText() == null || txtNombre.getText().equals("")) {
      txtNombre.getStyleClass().add("bad");
      valida = false;
    }
    if (txtNombre.getText() != null
        && txtNombre.getText().length() > ObjetivosView.LARGO_CAMPO_TEXT) {
      txtNombre.getStyleClass().add("bad");
      valida = false;
    }
    if (txtDescripcion.getText() == null || txtNombre.getText().equals("")) {
      txtDescripcion.getStyleClass().add("bad");
      valida = false;
    }
    if (txtDescripcion.getText() != null
        && txtDescripcion.getText().length() > 2 * ObjetivosView.LARGO_CAMPO_TEXT) {
      txtDescripcion.getStyleClass().add("bad");
      valida = false;
    }
    if (cmbTipoCurso.getValue() == null) {
      cmbTipoCurso.getStyleClass().add("bad");
      valida = false;
    }
    if (cmbAsignatura.getValue() == null) {
      cmbAsignatura.getStyleClass().add("bad");
      valida = false;
    }
    return valida;
  }
}
