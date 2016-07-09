package comunal.nivel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTPreguntasEvaluacion;
import cl.eos.ot.OTPreguntasHabilidad;
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.EvaluacionEjeTematico;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.persistence.models.TipoAlumno;
import cl.eos.persistence.models.TipoColegio;
import cl.eos.persistence.util.Comparadores;
import cl.eos.util.ExcelSheetWriterObj;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class Nivel_ComparativoComunalHabilidadView extends AFormView implements EventHandler<ActionEvent> {

    private static Logger log = Logger.getLogger(Nivel_ComparativoComunalHabilidadView.class);
    private NumberFormat formatter = new DecimalFormat("#0.00");
    @FXML
    private Label lblTitulo;
    @FXML
    private MenuItem mnuExportarHabilidad;
    @FXML
    private MenuItem mnuExportarEvaluacion;
    @FXML
    private TableView<ObservableList<String>> tblHabilidades;
    @FXML
    private TableView<ObservableList<String>> tblEvaluaciones;

    private HashMap<Habilidad, HashMap<String, OTPreguntasHabilidad>> mapaHabilidad;

    private Map<Long, EvaluacionEjeTematico> mEvaluaciones;

    private Map<EvaluacionEjeTematico, HashMap<String, OTPreguntasEvaluacion>> mapEvaAlumnos = null;

    @FXML
    private ComboBox<TipoAlumno> cmbTipoAlumno;
    @FXML
    private Button btnGenerar;
    @FXML
    private ComboBox<TipoColegio> cmbTipoColegio;

    long tipoAlumno = Constants.PIE_ALL;
    long tipoColegio = Constants.TIPO_COLEGIO_ALL;

    private boolean llegaOnFound = false;
    private boolean llegaEvaluacionEjeTematico = false;
    private boolean llegaTipoAlumno = false;
    private ArrayList<String> titulosColumnas;
    private Prueba prueba;
    private boolean llegaTipoColegio;

    public Nivel_ComparativoComunalHabilidadView() {

    }

    @FXML
    public void initialize() {
        this.setTitle("Resumen Comparativo Comunal Habilidad x Nivel");
        inicializarTablaHabilidades();
        inicializarTablaEvaluacion();
        mnuExportarHabilidad.setOnAction(this);
        mnuExportarEvaluacion.setOnAction(this);
        btnGenerar.setOnAction(this);
        cmbTipoAlumno.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (cmbTipoAlumno.getSelectionModel() == null)
                    return;
                tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedItem().getId();
            }
        });

        cmbTipoColegio.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (cmbTipoColegio.getSelectionModel().getSelectedItem() == null)
                    return;
                tipoColegio = cmbTipoColegio.getSelectionModel().getSelectedItem().getId();
            }
        });

    }

    private void inicializarTablaHabilidades() {
        tblHabilidades.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void inicializarTablaEvaluacion() {
        tblEvaluaciones.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @Override
    public void onFound(IEntity entity) {
        if (entity instanceof Prueba) {
            prueba = (Prueba) entity;
            llegaOnFound = true;
        }
        procesaDatosReporte();
    }

    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            Object entity = list.get(0);
            if (entity instanceof EvaluacionEjeTematico) {
                llegaEvaluacionEjeTematico = true;
                mEvaluaciones = new HashMap<Long, EvaluacionEjeTematico>();
                for (Object object : list) {
                    EvaluacionEjeTematico eje = (EvaluacionEjeTematico) object;
                    mEvaluaciones.put(eje.getId(), eje);
                }
            }
            if (entity instanceof TipoAlumno) {
                ObservableList<TipoAlumno> tAlumnoList = FXCollections.observableArrayList();
                llegaTipoAlumno = true;
                for (Object iEntity : list) {
                    tAlumnoList.add((TipoAlumno) iEntity);
                }
                cmbTipoAlumno.setItems(tAlumnoList);
                cmbTipoAlumno.getSelectionModel().select((int) Constants.PIE_ALL);
            }
            if (entity instanceof TipoColegio) {
                ObservableList<TipoColegio> tColegioList = FXCollections.observableArrayList();
                llegaTipoColegio = true;
                for (Object iEntity : list) {
                    tColegioList.add((TipoColegio) iEntity);
                }
                cmbTipoColegio.setItems(tColegioList);
                TipoColegio tColegio = new TipoColegio();
                tColegio.setId(Constants.TIPO_COLEGIO_ALL);
                cmbTipoColegio.getSelectionModel().select(tColegio);
            }
        }
        procesaDatosReporte();
    }

    private void procesaDatosReporte() {
        if (llegaEvaluacionEjeTematico && llegaTipoAlumno && llegaOnFound && llegaTipoColegio) {
            llenarDatosTabla();
            desplegarDatosHabilidades();
            desplegarDatosEvaluaciones();
        }
    }

    private void llenarDatosTabla() {

        if (llegaOnFound && llegaEvaluacionEjeTematico) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(prueba.getAsignatura());
            buffer.append(" ");
            buffer.append(prueba.getCurso());
            lblTitulo.setText(buffer.toString());

            mapaHabilidad = new HashMap<Habilidad, HashMap<String, OTPreguntasHabilidad>>();
            mapEvaAlumnos = new HashMap<EvaluacionEjeTematico, HashMap<String, OTPreguntasEvaluacion>>();
            HashMap<String, OTPreguntasHabilidad> mapaColegios = null;

            List<EvaluacionPrueba> listaEvaluaciones = prueba.getEvaluaciones();

            creacionColumnasEjesTematicos(listaEvaluaciones);
            creacionColumnasEvaluaciones(listaEvaluaciones);

            for (EvaluacionPrueba evaluacionPrueba : listaEvaluaciones) {
                String colegioNivel = evaluacionPrueba.getColegioTipoCurso();

                List<PruebaRendida> pruebasRendidas = evaluacionPrueba.getPruebasRendidas();
                List<RespuestasEsperadasPrueba> respuestasEsperadas = prueba.getRespuestas();

                for (PruebaRendida pruebaRendida : pruebasRendidas) {
                    if (pruebaRendida == null || pruebaRendida.getAlumno() == null
                            || pruebaRendida.getAlumno().getTipoAlumno() == null)
                        continue;
                    Alumno alumno = pruebaRendida.getAlumno();

                    if (tipoAlumno != Constants.PIE_ALL && tipoAlumno != alumno.getTipoAlumno().getId())
                        continue;
                    if (tipoColegio != Constants.TIPO_COLEGIO_ALL
                            && tipoColegio != alumno.getColegio().getTipoColegio().getId())
                        continue;

                    generaDatosEvaluacion(pruebaRendida, colegioNivel);

                    String respuesta = pruebaRendida.getRespuestas().toUpperCase();
                    Alumno al = pruebaRendida.getAlumno();
                    if (al == null) {
                        log.error(String.format("NO EXISTE ALUMNO: %s %s", colegioNivel, respuesta));
                        continue; // Caso que el alumno sea nulo.
                    }
                    log.info(String.format("%s %s %s %s %s %s", colegioNivel, al.getRut(), al.getName(),
                            al.getPaterno(), al.getMaterno(), respuesta));

                    if (respuesta == null || respuesta.length() < prueba.getNroPreguntas()) {
                        informarProblemas(colegioNivel, al, respuesta);
                        continue;
                    }

                    char[] cRespuesta = respuesta.toUpperCase().toCharArray();

                    for (RespuestasEsperadasPrueba respuestasEsperadasPrueba : respuestasEsperadas) {
                        if (respuestasEsperadasPrueba.isAnulada()) {
                            continue;
                        }

                        Habilidad habilidad = respuestasEsperadasPrueba.getHabilidad();
                        Integer numeroPreg = respuestasEsperadasPrueba.getNumero();
                        if (mapaHabilidad.containsKey(habilidad)) {
                            HashMap<String, OTPreguntasHabilidad> mapa = mapaHabilidad.get(habilidad);

                            if (mapa.containsKey(colegioNivel)) {
                                OTPreguntasHabilidad otPregunta = mapa.get(colegioNivel);

                                if (cRespuesta[numeroPreg - 1] == respuestasEsperadasPrueba.getRespuesta()
                                        .toCharArray()[0]) {
                                    otPregunta.setBuenas(otPregunta.getBuenas() + 1);
                                }
                                otPregunta.setTotal(otPregunta.getTotal() + 1);
                            } else {
                                OTPreguntasHabilidad otPreguntas = new OTPreguntasHabilidad();
                                otPreguntas.setHabilidad(habilidad);
                                if (cRespuesta[numeroPreg - 1] == respuestasEsperadasPrueba.getRespuesta()
                                        .toCharArray()[0]) {
                                    otPreguntas.setBuenas(1);
                                } else {
                                    otPreguntas.setBuenas(0);
                                }
                                otPreguntas.setTotal(1);

                                mapa.put(colegioNivel, otPreguntas);
                            }
                        } else {
                            OTPreguntasHabilidad otPreguntas = new OTPreguntasHabilidad();
                            otPreguntas.setHabilidad(habilidad);
                            if (cRespuesta[numeroPreg - 1] == respuestasEsperadasPrueba.getRespuesta()
                                    .toCharArray()[0]) {
                                otPreguntas.setBuenas(1);
                            } else {
                                otPreguntas.setBuenas(0);
                            }
                            otPreguntas.setTotal(1);

                            mapaColegios = new HashMap<String, OTPreguntasHabilidad>();
                            mapaColegios.put(colegioNivel, otPreguntas);
                            mapaHabilidad.put(habilidad, mapaColegios);
                        }
                    }
                }
            }
        }
    }

    private void generaDatosEvaluacion(PruebaRendida pruebaRendida, String colegioNivel) {

        HashMap<String, OTPreguntasEvaluacion> mapaOT = new HashMap<String, OTPreguntasEvaluacion>();

        Float pBuenas = pruebaRendida.getPbuenas();
        for (Entry<Long, EvaluacionEjeTematico> mEvaluacion : mEvaluaciones.entrySet()) {

            EvaluacionEjeTematico evaluacionAl = mEvaluacion.getValue();
            if (mapEvaAlumnos.containsKey(evaluacionAl)) {
                HashMap<String, OTPreguntasEvaluacion> evaluacion = mapEvaAlumnos.get(evaluacionAl);
                if (evaluacion.containsKey(colegioNivel)) {
                    OTPreguntasEvaluacion otPreguntas = evaluacion.get(colegioNivel);

                    if (pBuenas >= evaluacionAl.getNroRangoMin() && pBuenas <= evaluacionAl.getNroRangoMax()) {
                        otPreguntas.setAlumnos(otPreguntas.getAlumnos() + 1);
                    }
                } else {

                    OTPreguntasEvaluacion pregunta = new OTPreguntasEvaluacion();
                    if (pBuenas >= evaluacionAl.getNroRangoMin() && pBuenas <= evaluacionAl.getNroRangoMax()) {
                        pregunta.setAlumnos(1);
                    } else {
                        pregunta.setAlumnos(0);
                    }
                    pregunta.setEvaluacion(evaluacionAl);
                    evaluacion.put(colegioNivel, pregunta);
                }
            } else {
                OTPreguntasEvaluacion pregunta = new OTPreguntasEvaluacion();
                if (pBuenas >= evaluacionAl.getNroRangoMin() && pBuenas <= evaluacionAl.getNroRangoMax()) {
                    pregunta.setAlumnos(1);
                } else {
                    pregunta.setAlumnos(0);
                }
                pregunta.setEvaluacion(evaluacionAl);

                mapaOT = new HashMap<String, OTPreguntasEvaluacion>();
                mapaOT.put(colegioNivel, pregunta);
                mapEvaAlumnos.put(evaluacionAl, mapaOT);
            }
        }

    }

    private void desplegarDatosHabilidades() {
        ObservableList<ObservableList<String>> registros = FXCollections.observableArrayList();

        for (Entry<Habilidad, HashMap<String, OTPreguntasHabilidad>> mapa : mapaHabilidad.entrySet()) {

            ObservableList<String> row = FXCollections.observableArrayList();

            row.add(((Habilidad) mapa.getKey()).getName());

            HashMap<String, OTPreguntasHabilidad> resultados = mapa.getValue();

            for (String string : titulosColumnas) {
                OTPreguntasHabilidad otPregunta = resultados.get(string);
                if (otPregunta != null) {// EOS
                    row.add(formatter.format(otPregunta.getLogrado()));
                } else {// EOS
                    row.add("");
                }
            }

            registros.add(row);
        }
        tblHabilidades.setItems(registros);
    }

    private void desplegarDatosEvaluaciones() {

        Map<String, Integer> totales = new HashMap<String, Integer>();

        ObservableList<ObservableList<String>> registroseEva = FXCollections.observableArrayList();
        ObservableList<String> row = null;
        int total = 0;

        List<EvaluacionEjeTematico> ejes = new ArrayList<>(mapEvaAlumnos.keySet());
        Collections.sort(ejes, Comparadores.comparaEvaluacionEjeTematico());
        for (EvaluacionEjeTematico eje : ejes) {
            HashMap<String, OTPreguntasEvaluacion> resultados = mapEvaAlumnos.get(eje);
            row = FXCollections.observableArrayList();
            row.add(eje.getName());

            for (String string : titulosColumnas) {
                OTPreguntasEvaluacion otPregunta = resultados.get(string);
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
        for (String string : titulosColumnas) {
            Integer valor = totales.get(string);
            row.add(String.valueOf(valor == null ? 0 : valor));
        }
        registroseEva.add(row);

        tblEvaluaciones.setItems(registroseEva);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void creacionColumnasEjesTematicos(List<EvaluacionPrueba> pListaEvaluaciones) {
        tblHabilidades.getColumns().clear();

        TableColumn columna0 = new TableColumn("Eje Temático");
        columna0.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {

                return new SimpleStringProperty(param.getValue().get(0).toString());
            }
        });
        columna0.setPrefWidth(100);
        tblHabilidades.getColumns().add(columna0);

        titulosColumnas = new ArrayList<>();
        int indice = 1;
        List<EvaluacionPrueba> listaEvaluaciones = pListaEvaluaciones;
        for (EvaluacionPrueba evaluacion : listaEvaluaciones) {
            // Columnas
            final int col = indice;
            final String colegioCurso = evaluacion.getColegioTipoCurso();
            if (!titulosColumnas.contains(colegioCurso)) {
                titulosColumnas.add(colegioCurso);
                TableColumn columna = new TableColumn(colegioCurso);
                columna.setCellValueFactory(
                        new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                                return new SimpleStringProperty(param.getValue().get(col).toString());
                            }
                        });
                columna.setPrefWidth(100);
                tblHabilidades.getColumns().add(columna);
                indice++;
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void creacionColumnasEvaluaciones(List<EvaluacionPrueba> pListaEvaluaciones) {
        tblEvaluaciones.getColumns().clear();

        TableColumn columna0 = new TableColumn("");
        columna0.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().get(0).toString());
            }
        });
        columna0.setPrefWidth(100);
        tblEvaluaciones.getColumns().add(columna0);

        int indice = 1;
        for (String evaluacion : titulosColumnas) {

            // Columnas
            final int col = indice;
            final String colegioCurso = evaluacion;
            TableColumn columna = new TableColumn(colegioCurso);
            columna.setCellValueFactory(
                    new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                        public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(col).toString());
                        }
                    });
            columna.setPrefWidth(100);
            tblEvaluaciones.getColumns().add(columna);
            indice++;
        }
    }

    @Override
    public void handle(ActionEvent event) {
        Object source = event.getSource();
        if (source == mnuExportarHabilidad || source == mnuExportarEvaluacion) {

            tblHabilidades.setId("Habilidades");
            tblEvaluaciones.setId("Evaluación");

            List<TableView<? extends Object>> listaTablas = new LinkedList<>();
            listaTablas.add((TableView<? extends Object>) tblHabilidades);
            listaTablas.add((TableView<? extends Object>) tblEvaluaciones);
            ExcelSheetWriterObj.convertirDatosALibroDeExcel(listaTablas);
        } else if (source == btnGenerar) {
            if (prueba != null && tipoAlumno != -1) {
                procesaDatosReporte();
            }
        }
    }

    private void informarProblemas(String colegioCurso, Alumno al, String respuesta) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Alumno con respuestas incompletas.");
        alert.setHeaderText(String.format("%s/%s", colegioCurso, al.toString()));
        alert.setContentText(String.format("La respuesta [%s] es incompleta", respuesta));
        alert.showAndWait();
    }
}
