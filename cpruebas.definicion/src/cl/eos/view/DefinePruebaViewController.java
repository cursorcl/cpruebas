package cl.eos.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cl.eos.Environment;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.util.Comparadores;
import cl.eos.restful.tables.R_Clientes;
import cl.eos.restful.tables.R_Ejetematico;
import cl.eos.restful.tables.R_EstadoPruebaCliente;
import cl.eos.restful.tables.R_Formas;
import cl.eos.restful.tables.R_Habilidad;
import cl.eos.restful.tables.R_Objetivo;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_Prueba.Estado;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.MapBuilder;
import cl.eos.util.Utils;
import cl.eos.view.dnd.EjeTematicoDND;
import cl.eos.view.dnd.HabilidadDND;
import cl.eos.view.dnd.ObjetivoDND;
import cl.eos.view.editablecells.EditingCellRespuesta;
import cl.eos.view.editablecells.StyleChangingRowFactory;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class DefinePruebaViewController extends AFormView {
  private static final String VALID_LETTERS = "ABCDE";
  private String respsValidas = "";
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
  private TableColumn<RegistroDefinePrueba, R_Habilidad> habCol;
  @FXML
  private TableColumn<RegistroDefinePrueba, R_Ejetematico> ejeCol;
  @FXML
  private TableColumn<RegistroDefinePrueba, R_Objetivo> objCol;
  @FXML
  private TableColumn<RegistroDefinePrueba, String> badCol;
  @FXML
  private TableView<R_Ejetematico> tblEjesTematicos;
  @FXML
  private TableColumn<R_Ejetematico, String> ejeTematicoCol;
  @FXML
  private TableView<R_Objetivo> tblObjetivos;
  @FXML
  private TableColumn<R_Objetivo, String> objetivoCol;
  @FXML
  private TableView<R_Habilidad> tblHabilidades;
  @FXML
  private TableColumn<R_Habilidad, String> habilidadCol;
  @FXML
  private MenuItem mnuGrabar;
  @FXML
  private MenuItem mnuExportar;
  @FXML
  private Button btnListo;
  @FXML
  private TextField txtRespuestas;
  @FXML
  private Label lblCount;
  private R_Prueba prueba;
  private ObservableList<RegistroDefinePrueba> registros;
  private StyleChangingRowFactory<RegistroDefinePrueba> rowFactory;
  private List<R_Formas> formas;

  public DefinePruebaViewController() {
    setTitle("Definir Prueba");
  }

  private void actualizaLabelCuenta(int nro) {
    lblCount.setText(String.format("%d/%d", nro, prueba.getNropreguntas()));
  }

  protected void ejecutaGrabar() {
    if (validate()) {
      final String responses = txtRespuestas.getText();
      prueba.setResponses(responses);
      final List<R_RespuestasEsperadasPrueba> fromPrueba = getRespuestasEsperadas();
      int n = 0;
      for (R_RespuestasEsperadasPrueba resp : fromPrueba) {
        resp.setPrueba_id(prueba.getId());
        resp = (R_RespuestasEsperadasPrueba) save(resp);
        fromPrueba.set(n++, resp);
      }
      final List<R_Formas> formas = getFormasPrueba();
      n = 0;
      for (final R_Formas forma : formas) {
        forma.setPrueba_id(prueba.getId());
        R_Formas lforma = (R_Formas) save(forma);
        formas.set(n++, lforma);
      }
    }
    List<R_Clientes> clientes = controller.findAllSynchro(R_Clientes.class);
    Map<String, Object> params = new HashMap<>();
    params.put("prueba_id", prueba.getId());
    for (R_Clientes cliente : clientes) {
      params.put("cliente_id", cliente.getId());
      List<R_EstadoPruebaCliente> estados = controller.findByParamsSynchro(R_EstadoPruebaCliente.class, params);
      if (estados != null && !estados.isEmpty()) {
        R_EstadoPruebaCliente estado = estados.get(0);
        if (estado.getEstado_id() < (long) R_Prueba.Estado.DEFINIDA.getId())
          controller.deleteByParams(R_EstadoPruebaCliente.class, params);
      }

      R_EstadoPruebaCliente eCliente = new R_EstadoPruebaCliente.Builder().prueba_id(prueba.getId())
          .estado_id((long) R_Prueba.Estado.DEFINIDA.getId()).build();
      controller.save(eCliente);
    }

  }

  protected void ejecutarAccionListo() {
    final String resps = txtRespuestas.getText();
    int n = 0;
    while (n < resps.length() && n < registros.size()) {
      final RegistroDefinePrueba registro = registros.get(n);
      final String r = resps.substring(n, ++n).toUpperCase();
      if (r.equals("V") || r.equals("F")) {
        registro.setVerdaderoFalso(true);
        registro.setRespuesta(r);
      } else if (r.equals(" ")) {
        registro.setMental(true);
        registro.setRespuesta(r);
      } else if (respsValidas.indexOf(r) != -1) {
        registro.setMental(false);
        registro.setVerdaderoFalso(false);
        registro.setRespuesta(r);
      }
    }
  }

  private List<R_Formas> getFormasPrueba() {
    if (formas == null) {
      formas = new ArrayList<R_Formas>();
    }
    final int nroFormas = prueba.getNroformas().intValue();
    while (formas.size() > nroFormas) {
      formas.remove(nroFormas);
    }
    while (formas.size() < nroFormas) {
      formas.add(new R_Formas.Builder().id(Utils.getLastIndex()).build());
    }
    final List<Integer> numeros = new ArrayList<Integer>();
    for (int n = 1; n <= prueba.getNropreguntas(); n++) {
      numeros.add(new Integer(n));
    }
    for (int n = 0; n < nroFormas; n++) {
      final R_Formas forma = formas.get(n);
      forma.setForma(n + 1);
      forma.setName("Forma " + (n + 1));
      String orden = new String();
      for (int idx = 0; idx < numeros.size(); idx++) {
        if (idx > 0) {
          orden += ",";
        }
        orden += numeros.get(idx).toString();
      }
      forma.setOrden(orden);
      forma.setPrueba_id(prueba.getId());
      formas.set(n, forma);
      Collections.shuffle(numeros);
    }
    return formas;
  }

  private List<R_RespuestasEsperadasPrueba> getRespuestasEsperadas() {
    Map<String, Object> params = MapBuilder.<String, Object>unordered().put("prueba_id", prueba.getId()).build();
    List<R_RespuestasEsperadasPrueba> fromPrueba =
        controller.findByParamsSynchro(R_RespuestasEsperadasPrueba.class, params);
    if (fromPrueba == null) {
      fromPrueba = new ArrayList<R_RespuestasEsperadasPrueba>();
    }
    int n = 0;
    while (n < fromPrueba.size() && n < registros.size()) {
      final RegistroDefinePrueba registro = registros.get(n);
      final R_RespuestasEsperadasPrueba respuesta = fromPrueba.get(n);
      respuesta.setEjetematico_id(registro.getEjeTematico().getId());
      respuesta.setHabilidad_id(registro.getHabilidad().getId());
      respuesta.setMental(registro.getMental());
      respuesta.setName(registro.getNumero().toString());
      respuesta.setNumero(registro.getNumero());
      respuesta.setRespuesta(registro.getRespuesta());
      respuesta.setObjetivo_id(registro.getObjetivo().getId());
      respuesta.setVerdaderofalso(registro.getVerdaderoFalso());
      n++;
    }
    if (fromPrueba.size() < registros.size()) {
      while (n < registros.size()) {
        final RegistroDefinePrueba registro = registros.get(n);
        final R_RespuestasEsperadasPrueba respuesta =
            new R_RespuestasEsperadasPrueba.Builder().id(Utils.getLastIndex()).build();
        respuesta.setEjetematico_id(registro.getEjeTematico().getId());
        respuesta.setHabilidad_id(registro.getHabilidad().getId());
        respuesta.setMental(registro.getMental());
        respuesta.setName(registro.getNumero().toString());
        respuesta.setNumero(new Integer(n + 1));
        respuesta.setRespuesta(registro.getRespuesta());
        respuesta.setVerdaderofalso(registro.getVerdaderoFalso());
        respuesta.setObjetivo_id(registro.getObjetivo().getId());
        fromPrueba.add(respuesta);
        n++;
      }
    } else if (fromPrueba.size() > registros.size()) {
      while (fromPrueba.size() > registros.size()) {
        fromPrueba.remove(registros.size());
      }
    }
    return fromPrueba;
  }

  @FXML
  public void initialize() {
    tblRegistroDefinePrueba.setEditable(true);
    tblRegistroDefinePrueba.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    registerDND();
    rowFactory = new StyleChangingRowFactory<>("bad");
    tblRegistroDefinePrueba.setRowFactory(rowFactory);
    preguntaCol.setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, Integer>("numero"));
    preguntaCol.setStyle("-fx-alignment: CENTER;");
    respuestaCol.setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, String>("respuesta"));
    respuestaCol.setEditable(true);
    respuestaCol.setStyle("-fx-alignment: CENTER;");
    vfCol.setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, Boolean>("verdaderoFalso"));
    vfCol.setCellFactory(CheckBoxTableCell.forTableColumn(vfCol));
    vfCol.setEditable(true);
    mentalCol.setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, Boolean>("mental"));
    mentalCol.setCellFactory(CheckBoxTableCell.forTableColumn(mentalCol));
    mentalCol.setEditable(true);
    habCol.setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, R_Habilidad>("habilidad"));
    ejeCol.setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, R_Ejetematico>("ejeTematico"));
    objCol.setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, R_Objetivo>("objetivo"));
    habilidadCol.setCellValueFactory(new PropertyValueFactory<R_Habilidad, String>("name"));
    ejeTematicoCol.setCellValueFactory(new PropertyValueFactory<R_Ejetematico, String>("name"));
    objetivoCol.setCellValueFactory(new PropertyValueFactory<R_Objetivo, String>("name"));
    tblRegistroDefinePrueba.setOnMouseClicked(event -> {
      final boolean ennabled = tblRegistroDefinePrueba.getSelectionModel().getSelectedItems() != null
          && !tblRegistroDefinePrueba.getSelectionModel().getSelectedItems().isEmpty();
      tblEjesTematicos.setDisable(!ennabled);
      tblHabilidades.setDisable(!ennabled);
      tblObjetivos.setDisable(!ennabled);
    });
    tblHabilidades.setOnDragDetected(event -> {
      final Dragboard db = tblHabilidades.startDragAndDrop(TransferMode.ANY);
      final ClipboardContent content = new ClipboardContent();
      content.putIfAbsent(HabilidadDND.habilidadTrackDataFormat, tblHabilidades.getSelectionModel().getSelectedItem());
      db.setContent(content);
      event.consume();
    });
    tblEjesTematicos.setOnDragDetected(event -> {
      final Dragboard db = tblHabilidades.startDragAndDrop(TransferMode.ANY);
      final ClipboardContent content = new ClipboardContent();
      content.putIfAbsent(EjeTematicoDND.ejeTematicoTrackDataFormat,
          tblEjesTematicos.getSelectionModel().getSelectedItem());
      db.setContent(content);
      event.consume();
    });
    tblObjetivos.setOnDragDetected(event -> {
      final Dragboard db = tblObjetivos.startDragAndDrop(TransferMode.ANY);
      final ClipboardContent content = new ClipboardContent();
      if (content != null) {
        content.putIfAbsent(ObjetivoDND.objetivoTrackDataFormat, tblObjetivos.getSelectionModel().getSelectedItem());
        db.setContent(content);
        event.consume();
      }
    });
    mnuGrabar.setOnAction(event -> ejecutaGrabar());
    mnuExportar.setOnAction(event -> {
      final List<TableView<? extends Object>> listaTablas = new LinkedList<>();
      listaTablas.add(tblRegistroDefinePrueba);
      listaTablas.add(tblEjesTematicos);
      listaTablas.add(tblHabilidades);
      ExcelSheetWriterObj.convertirDatosALibroDeExcel(listaTablas);
    });
    btnListo.setOnAction(event -> ejecutarAccionListo());
    txtRespuestas.textProperty().addListener((ChangeListener<String>) (ov, oldValue, newValue) -> {
      if (newValue.length() > prueba.getNropreguntas().intValue()) {
        txtRespuestas.setText(newValue.substring(0, prueba.getNropreguntas().intValue()));
      } else {
        final int len = newValue.length();
        if (len > 0) {
          final String s = newValue.substring(len - 1, len).toUpperCase();
          final String strValid = respsValidas + " VF";
          if (!strValid.contains(s)) {
            txtRespuestas.setText(oldValue);
          }
          actualizaLabelCuenta(txtRespuestas.getText().length());
        }
      }
    });
  }

  @Override
  public void onDataArrived(List<Object> list) {
    if (list != null && !list.isEmpty()) {
      final Object entity = list.get(0);
      if (entity instanceof R_Ejetematico) {
        final ObservableList<R_Ejetematico> ejesTematicos = FXCollections.observableArrayList();
        for (final Object lEntity : list) {
          ejesTematicos.add((R_Ejetematico) lEntity);
        }
        tblEjesTematicos.setItems(ejesTematicos);
      } else if (entity instanceof R_Habilidad) {
        final ObservableList<R_Habilidad> habilidades = FXCollections.observableArrayList();
        for (final Object lEntity : list) {
          habilidades.add((R_Habilidad) lEntity);
        }
        tblHabilidades.setItems(habilidades);
      } else if (entity instanceof R_Objetivo) {
        final ObservableList<R_Objetivo> objetivos = FXCollections.observableArrayList();
        for (final Object lEntity : list) {
          objetivos.add((R_Objetivo) lEntity);
        }
        tblObjetivos.setItems(objetivos);
      }
    }
  }

  @Override
  public void onFound(IEntity entity) {
    if (entity instanceof R_Prueba) {
      registros = FXCollections.observableArrayList();
      prueba = (R_Prueba) entity;
      Map<String, Object> params = MapBuilder.<String, Object>unordered().put("prueba_id", prueba.getId()).build();
      formas = controller.findByParamsSynchro(R_Formas.class, params);
      Map<String, Object> pParams = MapBuilder.<String, Object>unordered().put("prueba_id", prueba.getId())
          .put("cliente_id", Environment.client).build();
      List<R_EstadoPruebaCliente> lstEstadoPrueba =
          controller.findByParamsSynchro(R_EstadoPruebaCliente.class, pParams);
      Estado estadoPrueba = R_Prueba.Estado.CREADA;
      if (lstEstadoPrueba != null && !lstEstadoPrueba.isEmpty()) {
        estadoPrueba = R_Prueba.Estado.getEstado(lstEstadoPrueba.get(0).getEstado_id().intValue());
      }


      respsValidas = DefinePruebaViewController.VALID_LETTERS.substring(0, prueba.getAlternativas());
      respuestaCol.setCellFactory(column -> {
        final EditingCellRespuesta editing = new EditingCellRespuesta();
        editing.setValidValues(respsValidas);
        return editing;
      });
      List<R_RespuestasEsperadasPrueba> lrespuestas =
          controller.findByParamsSynchro(R_RespuestasEsperadasPrueba.class, params);
      txtRespuestas.setText("");
      if (lrespuestas != null && !lrespuestas.isEmpty()) {
        final StringBuffer resps = new StringBuffer();
        final List<R_RespuestasEsperadasPrueba> respuestas = new ArrayList<R_RespuestasEsperadasPrueba>();
        final int nroPreguntas = prueba.getNropreguntas();
        final int oldNroPreguntas = lrespuestas.size();
        for (final R_RespuestasEsperadasPrueba respuesta : lrespuestas) {
          respuestas.add(respuesta);
        }
        if (nroPreguntas > oldNroPreguntas) {
          for (int n = oldNroPreguntas + 1; n < nroPreguntas; n++) {
            final R_RespuestasEsperadasPrueba resp =
                new R_RespuestasEsperadasPrueba.Builder().id(Utils.getLastIndex()).build();
            resp.setNumero(n);
            respuestas.add(resp);
          }
        } else if (nroPreguntas < oldNroPreguntas) {
          while (respuestas.size() > nroPreguntas) {
            respuestas.remove(respuestas.size() - 1);
          }
        }
        Map<Long, R_Ejetematico> ejes = new HashMap<>();
        Map<Long, R_Habilidad> habilidades = new HashMap<>();
        Map<Long, R_Objetivo> objetivos = new HashMap<>();
        Collections.sort(respuestas, Comparadores.compararRespuestasEsperadas());
        for (final R_RespuestasEsperadasPrueba respuesta : respuestas) {
          final RegistroDefinePrueba registro = new RegistroDefinePrueba();
          registro.setNumero(respuesta.getNumero());
          registro.setRespuesta(respuesta.getRespuesta());
          R_Ejetematico eje = ejes.get(respuesta.getEjetematico_id());
          if (eje == null)
            eje = controller.findByIdSynchro(R_Ejetematico.class, respuesta.getEjetematico_id());
          R_Habilidad habilidad = habilidades.get(respuesta.getHabilidad_id());
          if (habilidad == null)
            habilidad = controller.findByIdSynchro(R_Habilidad.class, respuesta.getHabilidad_id());
          R_Objetivo objetivo = objetivos.get(respuesta.getObjetivo_id());
          if (objetivo == null)
            objetivo = controller.findByIdSynchro(R_Objetivo.class, respuesta.getObjetivo_id());
          registro.setEjeTematico(eje);
          registro.setHabilidad(habilidad);
          registro.setObjetivo(objetivo);
          registro.setVerdaderoFalso(respuesta.getVerdaderofalso());
          registro.setMental(respuesta.getMental());
          resps.append(respuesta.getRespuesta());
          registros.add(registro);
        }
        txtRespuestas.setText(resps.toString());
        actualizaLabelCuenta(txtRespuestas.getText().length());
      } else {
        final int nroPreguntas = prueba.getNropreguntas();
        for (int n = 0; n < nroPreguntas; n++) {
          final RegistroDefinePrueba registro = new RegistroDefinePrueba();
          registro.setNumero(n + 1);
          registros.add(registro);
          actualizaLabelCuenta(0);
        }
      }
      tblRegistroDefinePrueba.setItems(registros);
      final boolean editable = !estadoPrueba.equals(Estado.EVALUADA);
      txtRespuestas.setEditable(editable);
      txtRespuestas.setDisable(!editable);
      preguntaCol.setEditable(editable);
      respuestaCol.setEditable(editable);
      vfCol.setEditable(editable);
      mentalCol.setEditable(editable);
    }
  }

  private void registerDND() {
    tblRegistroDefinePrueba.setOnDragDropped(dragEvent -> {
      final ObservableList<RegistroDefinePrueba> seleccionados =
          tblRegistroDefinePrueba.getSelectionModel().getSelectedItems();
      if (seleccionados != null && !seleccionados.isEmpty()) {
        if (dragEvent.getDragboard().getContent(EjeTematicoDND.ejeTematicoTrackDataFormat) != null) {
          final R_Ejetematico ejeTematico =
              (R_Ejetematico) dragEvent.getDragboard().getContent(EjeTematicoDND.ejeTematicoTrackDataFormat);
          for (final RegistroDefinePrueba registro1 : seleccionados) {
            registro1.setEjeTematico(ejeTematico);
          }
        } else if (dragEvent.getDragboard().getContent(HabilidadDND.habilidadTrackDataFormat) != null) {
          final R_Habilidad habilidad =
              (R_Habilidad) dragEvent.getDragboard().getContent(HabilidadDND.habilidadTrackDataFormat);
          for (final RegistroDefinePrueba registro2 : seleccionados) {
            registro2.setHabilidad(habilidad);
          }
        } else if (dragEvent.getDragboard().getContent(ObjetivoDND.objetivoTrackDataFormat) != null) {
          final R_Objetivo objetivo =
              (R_Objetivo) dragEvent.getDragboard().getContent(ObjetivoDND.objetivoTrackDataFormat);
          for (final RegistroDefinePrueba registro3 : seleccionados) {
            registro3.setObjetivo(objetivo);
          }
        }
      }
    });
    tblRegistroDefinePrueba.setOnDragEntered(dragEvent -> tblRegistroDefinePrueba.setBlendMode(BlendMode.DARKEN));
    tblRegistroDefinePrueba.setOnDragExited(dragEvent -> tblRegistroDefinePrueba.setBlendMode(null));
    tblRegistroDefinePrueba.setOnDragOver(dragEvent -> {
      if (dragEvent.getDragboard().getContent(EjeTematicoDND.ejeTematicoTrackDataFormat) != null
          || dragEvent.getDragboard().getContent(HabilidadDND.habilidadTrackDataFormat) != null
          || dragEvent.getDragboard().getContent(ObjetivoDND.objetivoTrackDataFormat) != null) {
        dragEvent.acceptTransferModes(TransferMode.COPY);
      }
    });
  }

  @Override
  public boolean validate() {
    boolean valido = true;
    final ObservableList<Integer> badIdx = FXCollections.observableArrayList();
    int n = 0;
    boolean todosValidos = true;
    for (final RegistroDefinePrueba registro : registros) {
      valido = registro.getEjeTematico() != null;
      valido = valido && registro.getHabilidad() != null;
      valido = valido && !registro.getRespuesta().isEmpty();
      valido = valido && (registro.getMental() && " ".equals(registro.getRespuesta())
          || registro.getVerdaderoFalso() && "VF".contains(registro.getRespuesta().toUpperCase())
          || respsValidas.contains(registro.getRespuesta().toUpperCase()));
      if (!valido) {
        badIdx.add(n);
      }
      n++;
      todosValidos = todosValidos && valido;
    }
    if (!todosValidos) {
      rowFactory.getStyledRowIndices().setAll(badIdx);
    } else {
      badIdx.add(-1);
      rowFactory.getStyledRowIndices().setAll(badIdx);
    }
    return todosValidos;
  }
}
