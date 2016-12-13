package comunal;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTPreguntasEjes;
import cl.eos.ot.OTPreguntasEvaluacion;
import cl.eos.persistence.models.SAlumno;
import cl.eos.persistence.models.SEjeTematico;
import cl.eos.persistence.models.SEvaluacionEjeTematico;
import cl.eos.persistence.models.SEvaluacionPrueba;
import cl.eos.persistence.models.SPrueba;
import cl.eos.persistence.models.SPruebaRendida;
import cl.eos.persistence.models.SRespuestasEsperadasPrueba;
import cl.eos.persistence.models.STipoAlumno;
import cl.eos.persistence.models.STipoColegio;
import cl.eos.persistence.util.Comparadores;
import cl.eos.util.ExcelSheetWriterObj;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;

public class ComparativoComunalEjeView extends AFormView implements EventHandler<ActionEvent> {

    private static Logger log = Logger.getLogger(ComparativoComunalEjeView.class.getName());
    private final NumberFormat formatter = new DecimalFormat("#0.00");
    @FXML
    private Label lblTitulo;
    @FXML
    private MenuItem mnuExportarEjesTematicos;
    @FXML
    private MenuItem mnuExportarEvaluacion;
    @FXML
    private TableView<ObservableList<String>> tblEjesTematicos;
    @FXML
    private TableView<ObservableList<String>> tblEvaluacionEjesTematicos;

    private HashMap<SEjeTematico, HashMap<String, OTPreguntasEjes>> mapaEjesTematicos;

    private Map<Long, SEvaluacionEjeTematico> mEvaluaciones;

    private Map<SEvaluacionEjeTematico, HashMap<String, OTPreguntasEvaluacion>> mapEvaAlumnos = null;

    @FXML
    private ComboBox<STipoAlumno> cmbTipoAlumno;
    @FXML
    private Button btnGenerar;
    @FXML
    private ComboBox<STipoColegio> cmbTipoColegio;

    long tipoAlumno = Constants.PIE_ALL;
    long tipoColegio = Constants.TIPO_COLEGIO_ALL;

    private ArrayList<String> titulosColumnas;
    private SPrueba prueba;
    private boolean llegaOnFound = false;
    private boolean llegaTipoAlumno = false;
    private boolean llegaEvaluacionEjeTematico = false;
    private boolean llegaTipoColegio;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void creacionColumnasEjesTematicos(List<SEvaluacionPrueba> pListaEvaluaciones) {

        tblEjesTematicos.getColumns().clear();

        final TableColumn columna0 = new TableColumn("Eje Temático");
        columna0.setCellValueFactory(param -> new SimpleStringProperty(
                ((CellDataFeatures<ObservableList, String>) param).getValue().get(0).toString()));
        columna0.setPrefWidth(100);
        tblEjesTematicos.getColumns().add(columna0);

        titulosColumnas = new ArrayList<>();
        int indice = 1;
        final List<SEvaluacionPrueba> listaEvaluaciones = pListaEvaluaciones;
        for (final SEvaluacionPrueba evaluacion : listaEvaluaciones) {
            // Columnas
            final int col = indice;
            final String colegioCurso = evaluacion.getColegiocurso();
            titulosColumnas.add(colegioCurso);
            final TableColumn columna = new TableColumn(colegioCurso);
            columna.setCellValueFactory(param -> new SimpleStringProperty(
                    ((CellDataFeatures<ObservableList, String>) param).getValue().get(col).toString()));
            columna.setPrefWidth(100);
            tblEjesTematicos.getColumns().add(columna);
            indice++;
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void creacionColumnasEvaluaciones(List<SEvaluacionPrueba> pListaEvaluaciones) {
        tblEvaluacionEjesTematicos.getColumns().clear();
        final TableColumn columna0 = new TableColumn("");
        columna0.setCellValueFactory(param -> new SimpleStringProperty(
                ((CellDataFeatures<ObservableList, String>) param).getValue().get(0).toString()));
        columna0.setPrefWidth(100);
        tblEvaluacionEjesTematicos.getColumns().add(columna0);

        int indice = 1;
        for (final String evaluacion : titulosColumnas) {

            // Columnas
            final int col = indice;
            final String colegioCurso = evaluacion;
            final TableColumn columna = new TableColumn(colegioCurso);
            columna.setCellValueFactory(param -> new SimpleStringProperty(
                    ((CellDataFeatures<ObservableList, String>) param).getValue().get(col).toString()));
            columna.setPrefWidth(100);
            tblEvaluacionEjesTematicos.getColumns().add(columna);
            indice++;
        }
    }

    private void desplegarDatosEjesTematicos() {

        final ObservableList<ObservableList<String>> registros = FXCollections.observableArrayList();

        for (final Entry<SEjeTematico, HashMap<String, OTPreguntasEjes>> mapa : mapaEjesTematicos.entrySet()) {

            final ObservableList<String> row = FXCollections.observableArrayList();

            row.add(mapa.getKey().getName());

            final HashMap<String, OTPreguntasEjes> resultados = mapa.getValue();

            for (final String string : titulosColumnas) {
                final OTPreguntasEjes otPregunta = resultados.get(string);
                if (otPregunta != null) {// EOS
                    row.add(formatter.format(otPregunta.getLogrado()));
                } else {// EOS
                    row.add("");
                }
            }

            registros.add(row);
        }
        tblEjesTematicos.setItems(registros);
    }

    private void desplegarDatosEvaluaciones() {

        final Map<String, Integer> totales = new HashMap<>();

        final ObservableList<ObservableList<String>> registroseEva = FXCollections.observableArrayList();
        ObservableList<String> row = null;
        int total = 0;

        final List<SEvaluacionEjeTematico> ejes = new ArrayList<>(mapEvaAlumnos.keySet());
        Collections.sort(ejes, Comparadores.comparaEvaluacionEjeTematico());
        for (final SEvaluacionEjeTematico eje : ejes) {
            final HashMap<String, OTPreguntasEvaluacion> resultados = mapEvaAlumnos.get(eje);
            row = FXCollections.observableArrayList();
            row.add(eje.getName());

            for (final String string : titulosColumnas) {
                final OTPreguntasEvaluacion otPregunta = resultados.get(string);
                if (otPregunta != null) { // EOS
                    if (totales.containsKey(string)) {
                        total = otPregunta.getAlumnos() + totales.get(string);
                        totales.replace(string, total);
                    } else {
                        totales.put(string, otPregunta.getAlumnos());
                    }
                    row.add(String.valueOf(otPregunta.getAlumnos()));
                } else {// EOS
                    row.add("");
                    totales.put(string, 0);
                }

            }
            registroseEva.add(row);

        }
        row = FXCollections.observableArrayList();
        row.add("Totales");
        for (final String string : titulosColumnas) {
            final Integer valor = totales.get(string);
            row.add(valor == null ? "0" : String.valueOf(valor));
        }
        registroseEva.add(row);

        tblEvaluacionEjesTematicos.setItems(registroseEva);
    }

    private void generaDatosEvaluacion(SPruebaRendida pruebaRendida, String colegioCurso) {

        HashMap<String, OTPreguntasEvaluacion> mapaOT;

        final Float pBuenas = pruebaRendida.getPbuenas();
        for (final Entry<Long, SEvaluacionEjeTematico> mEvaluacion : mEvaluaciones.entrySet()) {

            final SEvaluacionEjeTematico evaluacionAl = mEvaluacion.getValue();
            if (mapEvaAlumnos.containsKey(evaluacionAl)) {
                final HashMap<String, OTPreguntasEvaluacion> evaluacion = mapEvaAlumnos.get(evaluacionAl);
                if (evaluacion.containsKey(colegioCurso)) {
                    final OTPreguntasEvaluacion otPreguntas = evaluacion.get(colegioCurso);

                    if (pBuenas >= evaluacionAl.getNroRangoMin() && pBuenas <= evaluacionAl.getNroRangoMax()) {
                        otPreguntas.setAlumnos(otPreguntas.getAlumnos() + 1);
                    }
                } else {

                    final OTPreguntasEvaluacion pregunta = new OTPreguntasEvaluacion();
                    if (pBuenas >= evaluacionAl.getNroRangoMin() && pBuenas <= evaluacionAl.getNroRangoMax()) {
                        pregunta.setAlumnos(1);
                    } else {
                        pregunta.setAlumnos(0);
                    }
                    pregunta.setEvaluacion(evaluacionAl);
                    evaluacion.put(colegioCurso, pregunta);
                }
            } else {
                final OTPreguntasEvaluacion pregunta = new OTPreguntasEvaluacion();
                if (pBuenas >= evaluacionAl.getNroRangoMin() && pBuenas <= evaluacionAl.getNroRangoMax()) {
                    pregunta.setAlumnos(1);
                } else {
                    pregunta.setAlumnos(0);
                }
                pregunta.setEvaluacion(evaluacionAl);

                mapaOT = new HashMap<>();
                mapaOT.put(colegioCurso, pregunta);
                mapEvaAlumnos.put(evaluacionAl, mapaOT);
            }
        }

    }

    @Override
    public void handle(ActionEvent event) {
        final Object source = event.getSource();
        if (source == mnuExportarEjesTematicos || source == mnuExportarEvaluacion) {

            tblEjesTematicos.setId("Ejes temáticos");
            tblEvaluacionEjesTematicos.setId("Evaluación");
            final List<TableView<? extends Object>> listaTablas = new LinkedList<>();
            listaTablas.add(tblEjesTematicos);
            listaTablas.add(tblEvaluacionEjesTematicos);

            ExcelSheetWriterObj.convertirDatosALibroDeExcel(listaTablas);
        } else if (source == btnGenerar) {
            if (prueba != null && tipoAlumno != -1) {
                procesaDatosReporte();
            }
        }
    }

    private void informarProblemas(String colegioCurso, SAlumno al, String respuesta) {
        final Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("SAlumno con respuestas incompletas.");
        alert.setHeaderText(String.format("%s/%s", colegioCurso, al.toString()));
        alert.setContentText(String.format("La respuesta [%s] es incompleta", respuesta));
        alert.showAndWait();

    }

    @FXML
    public void initialize() {
        setTitle("Resumen comparativo comunal ejes temáticas");
        tblEjesTematicos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblEvaluacionEjesTematicos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        mnuExportarEjesTematicos.setOnAction(this);
        mnuExportarEvaluacion.setOnAction(this);
        btnGenerar.setOnAction(this);
        cmbTipoAlumno.setOnAction(event -> {
            if (cmbTipoAlumno.getSelectionModel() == null)
                return;
            tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedItem().getId();
        });

        cmbTipoColegio.setOnAction(event -> {
            if (cmbTipoColegio.getSelectionModel().getSelectedItem() == null)
                return;
            tipoColegio = cmbTipoColegio.getSelectionModel().getSelectedItem().getId();
        });

    }

    private void llenarDatosTabla() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(prueba.getAsignatura());
        buffer.append(" ");
        buffer.append(prueba.getCurso());
        lblTitulo.setText(buffer.toString());

        mapaEjesTematicos = new HashMap<>();
        mapEvaAlumnos = new HashMap<>();
        HashMap<String, OTPreguntasEjes> mapaColegios;

        final List<SEvaluacionPrueba> listaEvaluaciones = prueba.getEvaluaciones();

        creacionColumnasEjesTematicos(listaEvaluaciones);
        creacionColumnasEvaluaciones(listaEvaluaciones);

        for (final SEvaluacionPrueba evaluacionPrueba : listaEvaluaciones) {
            final String colegioCurso = evaluacionPrueba.getColegiocurso();

            final List<SPruebaRendida> pruebasRendidas = evaluacionPrueba.getPruebasRendidas();
            final List<SRespuestasEsperadasPrueba> respuestasEsperadas = prueba.getRespuestas();

            for (final SPruebaRendida pruebaRendida : pruebasRendidas) {
                final SAlumno alumno = pruebaRendida.getAlumno();
                if (tipoAlumno != Constants.PIE_ALL && tipoAlumno != alumno.getTipoAlumno().getId())
                    continue;
                if (tipoColegio != Constants.TIPO_COLEGIO_ALL
                        && tipoColegio != alumno.getColegio().getTipoColegio().getId())
                    continue;

                generaDatosEvaluacion(pruebaRendida, colegioCurso);

                final String respuesta = pruebaRendida.getRespuestas().toUpperCase();

                if (alumno == null) {
                    ComparativoComunalEjeView.log
                            .severe(String.format("NO EXISTE ALUMNO: %s %s", colegioCurso, respuesta));
                    continue; // Caso que el alumno sea nulo.
                }
                ComparativoComunalEjeView.log.info(String.format("%s %s %s %s %s %s", colegioCurso, alumno.getRut(),
                        alumno.getName(), alumno.getPaterno(), alumno.getMaterno(), respuesta));

                if (respuesta == null || respuesta.length() < prueba.getNroPreguntas()) {
                    informarProblemas(colegioCurso, alumno, respuesta);
                    continue;
                }
                final char[] cRespuesta = respuesta.toUpperCase().toCharArray();

                for (final SRespuestasEsperadasPrueba respuestasEsperadasPrueba : respuestasEsperadas) {
                    if (respuestasEsperadasPrueba.isAnulada()) {
                        continue;
                    }
                    final SEjeTematico ejeTematico = respuestasEsperadasPrueba.getEjeTematico();
                    final Integer numeroPreg = respuestasEsperadasPrueba.getNumero();
                    if (mapaEjesTematicos.containsKey(ejeTematico)) {
                        final HashMap<String, OTPreguntasEjes> mapa = mapaEjesTematicos.get(ejeTematico);

                        if (mapa.containsKey(colegioCurso)) {
                            final OTPreguntasEjes otPregunta = mapa.get(colegioCurso);

                            if (cRespuesta[numeroPreg - 1] == respuestasEsperadasPrueba.getRespuesta()
                                    .toCharArray()[0]) {
                                otPregunta.setBuenas(otPregunta.getBuenas() + 1);
                            }
                            otPregunta.setTotal(otPregunta.getTotal() + 1);
                        } else {
                            final OTPreguntasEjes otPreguntas = new OTPreguntasEjes();
                            otPreguntas.setEjeTematico(ejeTematico);
                            if (cRespuesta[numeroPreg - 1] == respuestasEsperadasPrueba.getRespuesta()
                                    .toCharArray()[0]) {
                                otPreguntas.setBuenas(1);
                            } else {
                                otPreguntas.setBuenas(0);
                            }
                            otPreguntas.setTotal(1);

                            mapa.put(colegioCurso, otPreguntas);
                        }
                    } else {
                        final OTPreguntasEjes otPreguntas = new OTPreguntasEjes();
                        otPreguntas.setEjeTematico(ejeTematico);
                        if (cRespuesta[numeroPreg - 1] == respuestasEsperadasPrueba.getRespuesta().toCharArray()[0]) {
                            otPreguntas.setBuenas(1);
                        } else {
                            otPreguntas.setBuenas(0);
                        }
                        otPreguntas.setTotal(1);

                        mapaColegios = new HashMap<>();
                        mapaColegios.put(colegioCurso, otPreguntas);
                        mapaEjesTematicos.put(ejeTematico, mapaColegios);
                    }
                }
            }

        }
    }

    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            final Object entity = list.get(0);
            if (entity instanceof SEvaluacionEjeTematico) {
                llegaEvaluacionEjeTematico = true;
                mEvaluaciones = new HashMap<>();
                for (final Object object : list) {
                    final SEvaluacionEjeTematico eje = (SEvaluacionEjeTematico) object;
                    mEvaluaciones.put(eje.getId(), eje);
                }
            }
            if (entity instanceof STipoAlumno) {
                final ObservableList<STipoAlumno> tAlumnoList = FXCollections.observableArrayList();
                llegaTipoAlumno = true;
                for (final Object iEntity : list) {
                    tAlumnoList.add((STipoAlumno) iEntity);
                }
                cmbTipoAlumno.setItems(tAlumnoList);
                cmbTipoAlumno.getSelectionModel().select((int) Constants.PIE_ALL);
            }
            if (entity instanceof STipoColegio) {
                final ObservableList<STipoColegio> tColegioList = FXCollections.observableArrayList();
                llegaTipoColegio = true;
                for (final Object iEntity : list) {
                    tColegioList.add((STipoColegio) iEntity);
                }
                cmbTipoColegio.setItems(tColegioList);
                final STipoColegio tColegio = new STipoColegio();
                tColegio.setId(Constants.TIPO_COLEGIO_ALL);
                cmbTipoColegio.getSelectionModel().select(tColegio);
            }
        }
        procesaDatosReporte();
    }

    @Override
    public void onFound(IEntity entity) {
        if (entity instanceof SPrueba) {
            prueba = (SPrueba) entity;
            llegaOnFound = true;
        }
        procesaDatosReporte();
    }

    private void procesaDatosReporte() {
        if (llegaEvaluacionEjeTematico && llegaTipoAlumno && llegaOnFound && llegaTipoColegio) {
            llenarDatosTabla();
            desplegarDatosEjesTematicos();
            desplegarDatosEvaluaciones();
        }
    }
}
