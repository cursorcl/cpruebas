package cl.eos.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.Formas;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.Objetivo;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.Prueba.Estado;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.persistence.util.Comparadores;
import cl.eos.util.ExcelSheetWriterObj;
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
    private TableColumn<RegistroDefinePrueba, Habilidad> habCol;
    @FXML
    private TableColumn<RegistroDefinePrueba, EjeTematico> ejeCol;
    @FXML
    private TableColumn<RegistroDefinePrueba, Objetivo> objCol;
    @FXML
    private TableColumn<RegistroDefinePrueba, String> badCol;
    @FXML
    private TableView<EjeTematico> tblEjesTematicos;
    @FXML
    private TableColumn<EjeTematico, String> ejeTematicoCol;
    @FXML
    private TableView<Objetivo> tblObjetivos;
    @FXML
    private TableColumn<Objetivo, String> objetivoCol;

    @FXML
    private TableView<Habilidad> tblHabilidades;
    @FXML
    private TableColumn<Habilidad, String> habilidadCol;
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

    private Prueba prueba;

    private ObservableList<RegistroDefinePrueba> registros;
    private StyleChangingRowFactory<RegistroDefinePrueba> rowFactory;

    public DefinePruebaViewController() {
        setTitle("Definir Prueba");
    }

    private void actualizaLabelCuenta(int nro) {
        lblCount.setText(String.format("%d/%d", nro, prueba.getNroPreguntas()));
    }

    protected void ejecutaGrabar() {
        if (validate()) {
            final String responses = txtRespuestas.getText();
            prueba.setResponses(responses);
            final List<RespuestasEsperadasPrueba> fromPrueba = getRespuestasEsperadas();
            int n = 0;
            for (RespuestasEsperadasPrueba resp : fromPrueba) {
                resp.setPrueba(prueba);
                resp = (RespuestasEsperadasPrueba) save(resp);
                fromPrueba.set(n++, resp);
            }
            prueba.setRespuestas(fromPrueba);

            final List<Formas> formas = getFormasPrueba();
            n = 0;
            for (final Formas forma : formas) {
                forma.setPrueba(prueba);
                formas.set(n++, forma);
            }
            prueba.setFormas(formas);
            prueba = (Prueba) save(prueba);
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

    private List<Formas> getFormasPrueba() {
        List<Formas> formas = prueba.getFormas();
        if (formas == null) {
            formas = new ArrayList<Formas>();
        }
        final int nroFormas = prueba.getNroFormas().intValue();
        while (formas.size() > nroFormas) {
            formas.remove(nroFormas);
        }
        while (formas.size() < nroFormas) {
            formas.add(new Formas());
        }
        final List<Integer> numeros = new ArrayList<Integer>();
        for (int n = 1; n <= prueba.getNroPreguntas(); n++) {
            numeros.add(new Integer(n));
        }
        for (int n = 0; n < nroFormas; n++) {
            final Formas forma = formas.get(n);
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
            forma.setPrueba(prueba);
            formas.set(n, forma);
            Collections.shuffle(numeros);
        }
        return formas;
    }

    private List<RespuestasEsperadasPrueba> getRespuestasEsperadas() {
        List<RespuestasEsperadasPrueba> fromPrueba = prueba.getRespuestas();
        if (fromPrueba == null) {
            fromPrueba = new ArrayList<RespuestasEsperadasPrueba>();
        }
        int n = 0;
        while (n < fromPrueba.size() && n < registros.size()) {
            final RegistroDefinePrueba registro = registros.get(n);
            final RespuestasEsperadasPrueba respuesta = fromPrueba.get(n);
            respuesta.setEjeTematico(registro.getEjeTematico());
            respuesta.setHabilidad(registro.getHabilidad());
            respuesta.setMental(registro.getMental());
            respuesta.setName(registro.getNumero().toString());
            respuesta.setNumero(registro.getNumero());
            respuesta.setRespuesta(registro.getRespuesta());
            respuesta.setObjetivo(registro.getObjetivo());
            respuesta.setVerdaderoFalso(registro.getVerdaderoFalso());
            n++;
        }
        if (fromPrueba.size() < registros.size()) {

            while (n < registros.size()) {
                final RegistroDefinePrueba registro = registros.get(n);
                final RespuestasEsperadasPrueba respuesta = new RespuestasEsperadasPrueba();
                respuesta.setEjeTematico(registro.getEjeTematico());
                respuesta.setHabilidad(registro.getHabilidad());
                respuesta.setMental(registro.getMental());
                respuesta.setName(registro.getNumero().toString());
                respuesta.setNumero(new Integer(n + 1));
                respuesta.setRespuesta(registro.getRespuesta());
                respuesta.setVerdaderoFalso(registro.getVerdaderoFalso());
                respuesta.setObjetivo(registro.getObjetivo());
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

        habCol.setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, Habilidad>("habilidad"));
        ejeCol.setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, EjeTematico>("ejeTematico"));
        objCol.setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, Objetivo>("objetivo"));

        habilidadCol.setCellValueFactory(new PropertyValueFactory<Habilidad, String>("name"));
        ejeTematicoCol.setCellValueFactory(new PropertyValueFactory<EjeTematico, String>("name"));
        objetivoCol.setCellValueFactory(new PropertyValueFactory<Objetivo, String>("name"));

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
            content.putIfAbsent(HabilidadDND.habilidadTrackDataFormat,
                    tblHabilidades.getSelectionModel().getSelectedItem());
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
                content.putIfAbsent(ObjetivoDND.objetivoTrackDataFormat,
                        tblObjetivos.getSelectionModel().getSelectedItem());
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
            if (newValue.length() > prueba.getNroPreguntas().intValue()) {
                txtRespuestas.setText(newValue.substring(0, prueba.getNroPreguntas().intValue()));
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
            if (entity instanceof EjeTematico) {
                final ObservableList<EjeTematico> ejesTematicos = FXCollections.observableArrayList();
                for (final Object lEntity : list) {
                    ejesTematicos.add((EjeTematico) lEntity);
                }
                tblEjesTematicos.setItems(ejesTematicos);
            } else if (entity instanceof Habilidad) {
                final ObservableList<Habilidad> habilidades = FXCollections.observableArrayList();
                for (final Object lEntity : list) {
                    habilidades.add((Habilidad) lEntity);
                }
                tblHabilidades.setItems(habilidades);
            } else if (entity instanceof Objetivo) {
                final ObservableList<Objetivo> objetivos = FXCollections.observableArrayList();
                for (final Object lEntity : list) {
                    objetivos.add((Objetivo) lEntity);
                }
                tblObjetivos.setItems(objetivos);
            }

        }
    }

    @Override
    public void onFound(IEntity entity) {
        if (entity instanceof Prueba) {
            registros = FXCollections.observableArrayList();
            prueba = (Prueba) entity;

            respsValidas = DefinePruebaViewController.VALID_LETTERS.substring(0, prueba.getAlternativas());
            respuestaCol.setCellFactory(column -> {
                final EditingCellRespuesta editing = new EditingCellRespuesta();
                editing.setValidValues(respsValidas);
                return editing;
            });

            txtRespuestas.setText("");
            if (prueba.getRespuestas() != null && !prueba.getRespuestas().isEmpty()) {

                final StringBuffer resps = new StringBuffer();
                final List<RespuestasEsperadasPrueba> respuestas = new ArrayList<RespuestasEsperadasPrueba>();

                final int nroPreguntas = prueba.getNroPreguntas();
                final int oldNroPreguntas = prueba.getRespuestas().size();
                for (final RespuestasEsperadasPrueba respuesta : prueba.getRespuestas()) {
                    respuestas.add(respuesta);
                }
                if (nroPreguntas > oldNroPreguntas) {
                    for (int n = oldNroPreguntas + 1; n < nroPreguntas; n++) {
                        final RespuestasEsperadasPrueba resp = new RespuestasEsperadasPrueba();
                        resp.setNumero(n);
                        respuestas.add(resp);
                    }
                } else if (nroPreguntas < oldNroPreguntas) {
                    while (respuestas.size() > nroPreguntas) {
                        respuestas.remove(respuestas.size() - 1);
                    }
                }

                Collections.sort(respuestas, Comparadores.compararRespuestasEsperadas());
                for (final RespuestasEsperadasPrueba respuesta : respuestas) {
                    final RegistroDefinePrueba registro = new RegistroDefinePrueba();
                    registro.setNumero(respuesta.getNumero());
                    registro.setRespuesta(respuesta.getRespuesta());
                    registro.setEjeTematico(respuesta.getEjeTematico());
                    registro.setHabilidad(respuesta.getHabilidad());
                    registro.setObjetivo(respuesta.getObjetivo());
                    registro.setVerdaderoFalso(respuesta.getVerdaderoFalso());
                    registro.setMental(respuesta.getMental());
                    resps.append(respuesta.getRespuesta());
                    registros.add(registro);
                }
                txtRespuestas.setText(resps.toString());
                actualizaLabelCuenta(txtRespuestas.getText().length());
            } else {
                final int nroPreguntas = prueba.getNroPreguntas();
                for (int n = 0; n < nroPreguntas; n++) {
                    final RegistroDefinePrueba registro = new RegistroDefinePrueba();
                    registro.setNumero(n + 1);
                    registros.add(registro);
                    actualizaLabelCuenta(0);
                }
            }

            tblRegistroDefinePrueba.setItems(registros);

            final boolean editable = !prueba.getEstado().equals(Estado.EVALUADA);
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
            final ObservableList<RegistroDefinePrueba> seleccionados = tblRegistroDefinePrueba.getSelectionModel()
                    .getSelectedItems();
            if (seleccionados != null && !seleccionados.isEmpty()) {
                if (dragEvent.getDragboard().getContent(EjeTematicoDND.ejeTematicoTrackDataFormat) != null) {
                    final EjeTematico ejeTematico = (EjeTematico) dragEvent.getDragboard()
                            .getContent(EjeTematicoDND.ejeTematicoTrackDataFormat);
                    for (final RegistroDefinePrueba registro1 : seleccionados) {
                        registro1.setEjeTematico(ejeTematico);
                    }
                } else if (dragEvent.getDragboard().getContent(HabilidadDND.habilidadTrackDataFormat) != null) {
                    final Habilidad habilidad = (Habilidad) dragEvent.getDragboard()
                            .getContent(HabilidadDND.habilidadTrackDataFormat);
                    for (final RegistroDefinePrueba registro2 : seleccionados) {
                        registro2.setHabilidad(habilidad);
                    }
                } else if (dragEvent.getDragboard().getContent(ObjetivoDND.objetivoTrackDataFormat) != null) {
                    final Objetivo objetivo = (Objetivo) dragEvent.getDragboard()
                            .getContent(ObjetivoDND.objetivoTrackDataFormat);
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
