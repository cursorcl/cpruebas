package cl.eos.view;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import cl.eos.PruebasActivator;
import cl.eos.detection.ExtractorResultadosPrueba;
import cl.eos.detection.OTResultadoScanner;
import cl.eos.exceptions.CPruebasException;
import cl.eos.imp.view.AFormView;
import cl.eos.imp.view.ProgressForm;
import cl.eos.imp.view.WindowManager;
import cl.eos.interfaces.IActivator;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.util.Comparadores;
import cl.eos.restful.EntityUtils;
import cl.eos.restful.tables.R_Alumno;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_Profesor;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_RangoEvaluacion;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.restful.tables.R_TipoCurso;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.MapBuilder;
import cl.eos.util.Pair;
import cl.eos.util.Utils;
import cl.eos.view.editablecells.EditingCellRespuestasEvaluar;
import cl.eos.view.ots.OTPruebaRendida;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

public class EvaluarPruebaView extends AFormView {
    private class EHandlerCmbCurso implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            evalPrueba = null;
            final R_Curso curso = cmbCursos.getSelectionModel().getSelectedItem();
            if (curso != null) {
                final ObservableList<OTPruebaRendida> oList = FXCollections.observableArrayList();
                final R_Colegio colegio = cmbColegios.getSelectionModel().getSelectedItem();
                final R_Profesor profesor = cmbProfesor.getSelectionModel().getSelectedItem();
                Map<String, Object> params = MapBuilder.<String, Object> unordered().put("curso_id", curso.getId()).build();
                lstAlumnos = controller.findByParamsSynchro(R_Alumno.class, params);
                params = MapBuilder.<String, Object> unordered().put("curso_id", curso.getId()).put("colegio_id", colegio.getId()).build();
                final List<R_EvaluacionPrueba> listEvaluaciones = controller.findByParamsSynchro(R_EvaluacionPrueba.class, params);
                if (listEvaluaciones != null && !listEvaluaciones.isEmpty()) {
                    evalPrueba = listEvaluaciones.get(0);
                    params = MapBuilder.<String, Object> unordered().put("evaluacionprueba_id", evalPrueba.getId()).build();
                    List<R_PruebaRendida> lstPRendidas = controller.findByParamsSynchro(R_PruebaRendida.class, params);
                    List<Long> pRendAlumno = lstPRendidas.stream().map(p -> p.getAlumno_id()).collect(Collectors.toList());
                    List<R_Alumno> aNoRendido = lstAlumnos.stream().filter(a -> !pRendAlumno.contains(a.getId())).collect(Collectors.toList());
                    for (R_PruebaRendida r : lstPRendidas) {
                        Optional<R_Alumno> oAlumno = lstAlumnos.stream().filter(a -> a.getId().equals(r.getAlumno_id())).findFirst();
                        oList.add(new OTPruebaRendida(r, oAlumno.isPresent() ? oAlumno.get() : null));
                    }
                    if (aNoRendido != null && !aNoRendido.isEmpty()) {
                        for (final R_Alumno alumno : aNoRendido) {
                            R_PruebaRendida pRendida = new R_PruebaRendida.Builder().id(Utils.getLastIndex()).build();
                            pRendida.setAlumno_id(alumno.getId());
                            pRendida.setEvaluacionprueba_id(evalPrueba.getId());
                            oList.add(new OTPruebaRendida(pRendida, alumno));
                        }
                    }
                } else {
                    evalPrueba = new R_EvaluacionPrueba.Builder().id(Utils.getLastIndex()).build();
                    evalPrueba.setColegio_id(colegio.getId());
                    evalPrueba.setCurso_id(curso.getId());
                    evalPrueba.setPrueba_id(prueba.getId());
                    evalPrueba.setProfesor_id(profesor.getId());
                    evalPrueba.setFecha(dtpFecha.getValue().toEpochDay());
                    if (lstAlumnos != null && !lstAlumnos.isEmpty()) {
                        for (final R_Alumno alumno : lstAlumnos) {
                            R_PruebaRendida pRendida = new R_PruebaRendida.Builder().id(Utils.getLastIndex()).build();
                            pRendida.setAlumno_id(alumno.getId());
                            pRendida.setEvaluacionprueba_id(evalPrueba.getId());
                            oList.add(new OTPruebaRendida(pRendida, alumno));
                        }
                    }
                }
                FXCollections.sort(oList, EvaluarPruebaView.comparaPruebaRendida());
                tblListadoPruebas.setItems(oList);
                mnuGrabar.setDisable(false);
                mnuScanner.setDisable(false);
            } else {
                tblListadoPruebas.getItems().clear();
            }
        }
    }
    private static Logger log = Logger.getLogger(EvaluarPruebaView.class.getName());
    public static Comparator<? super OTPruebaRendida> comparaPruebaRendida() {
        return (o1, o2) -> {
            final StringBuffer sb1 = new StringBuffer();
            sb1.append(o1.getPaterno());
            sb1.append(o1.getMaterno());
            sb1.append(o1.getNombres());
            final StringBuffer sb2 = new StringBuffer();
            sb1.append(o2.getPaterno());
            sb1.append(o2.getMaterno());
            sb1.append(o2.getNombres());
            return sb1.toString().compareTo(sb2.toString());
        };
    }
    private R_Prueba prueba;
    private R_EvaluacionPrueba evalPrueba = null;
    @FXML private Label lblError;
    @FXML private TableView<OTPruebaRendida> tblListadoPruebas;
    @FXML private TableColumn<OTPruebaRendida, String> paternoCol;
    @FXML private TableColumn<OTPruebaRendida, String> maternoCol;
    @FXML private TableColumn<OTPruebaRendida, String> nombresCol;
    @FXML private TableColumn<OTPruebaRendida, String> respuestasCol;
    @FXML private TableColumn<OTPruebaRendida, Integer> buenasCol;
    @FXML private TableColumn<OTPruebaRendida, Integer> malasCol;
    @FXML private TableColumn<OTPruebaRendida, Integer> omitidasCol;
    @FXML private TableColumn<OTPruebaRendida, Float> notaCol;
    @FXML private TableColumn<OTPruebaRendida, Integer> puntajeCol;
    @FXML private TableColumn<OTPruebaRendida, Boolean> rindeCol;
    @FXML private TableColumn<OTPruebaRendida, R_RangoEvaluacion> nivelCol;
    @FXML private ComboBox<R_Colegio> cmbColegios;
    @FXML private ComboBox<R_Curso> cmbCursos;
    @FXML private ComboBox<R_Profesor> cmbProfesor;
    @FXML private TextField txtName;
    @FXML private TextField txtAsignatura;
    @FXML private TextField txtNroPreguntas;
    @FXML private TextField txtNroAlternativas;
    @FXML private DatePicker dtpFecha;
    @FXML private MenuItem mnuScanner;
    @FXML private MenuItem mnuGrabar;
    @FXML private MenuItem mnuVolver;
    @FXML private MenuItem mnuNorinde;
    @FXML private MenuItem mnuExportar;
    @FXML private MenuItem menuExportar;
    @FXML private BorderPane mainPane;
    private List<R_RespuestasEsperadasPrueba> respuestas;
    private R_Asignatura asignatura;
    private R_Curso curso;
    private R_Colegio colegio;
    private R_TipoCurso tipoCurso;
    private ObservableList<Object> lstEvaluaciones;
    private List<R_RangoEvaluacion> rangos;
    private List<R_Alumno> lstAlumnos;
    public EvaluarPruebaView() {
        setTitle("Evaluar");
    }
    private void definirTablaListadoPruebas() {
        paternoCol.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, String>("paterno"));
        maternoCol.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, String>("materno"));
        nombresCol.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, String>("nombres"));
        buenasCol.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, Integer>("buenas"));
        malasCol.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, Integer>("malas"));
        omitidasCol.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, Integer>("omitidas"));
        notaCol.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, Float>("nota"));
        puntajeCol.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, Integer>("puntaje"));
        nivelCol.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, R_RangoEvaluacion>("nivel"));
        rindeCol.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, Boolean>("rindioPrueba"));
        rindeCol.setCellFactory(CheckBoxTableCell.forTableColumn(rindeCol));
        tblListadoPruebas.setEditable(true);
        respuestasCol.setCellValueFactory(new PropertyValueFactory<OTPruebaRendida, String>("respuestas"));
        respuestasCol.setEditable(true);
        respuestasCol.setCellFactory(param -> new EditingCellRespuestasEvaluar(prueba, respuestas));
        respuestasCol.setOnEditCommit(event -> {
            // Aqui debo validar el resultado de la prueba.
            final String value = event.getNewValue();
            final OTPruebaRendida pRendida = event.getRowValue();
            pRendida.setRespuestas(value);
            if (value != null && !value.isEmpty()) {
                evaluar(value, pRendida);
            }
        });
    }
    protected void evaluar(String respsAlumno, OTPruebaRendida otRendida) {
        final int nroPreguntas = respuestas.size();
        final int nMax = Math.min(respsAlumno.length(), nroPreguntas);
        otRendida.setOmitidas(0);
        final int nroLast = Math.abs(respsAlumno.length() - nroPreguntas);
        if (nroLast > 0) {
            final char[] c = new char[nroLast];
            Arrays.fill(c, 'O');
            otRendida.setOmitidas(nroLast);
            final StringBuilder sBuilder = new StringBuilder(respsAlumno);
            sBuilder.append(c);
            respsAlumno = sBuilder.toString();
        }
        otRendida.setBuenas(0);
        otRendida.setMalas(0);
        for (int n = 0; n < nMax; n++) {
            final R_RespuestasEsperadasPrueba resp = respuestas.get(n);
            final String userResp = respsAlumno.substring(n, n + 1);
            String validResp = resp.getRespuesta();
            if (resp.getMental()) {
                validResp = "+";
            }
            if (userResp.toUpperCase().equals("O")) {
                otRendida.setOmitidas(otRendida.getOmitidas() + 1);
            } else if (userResp.toUpperCase().equals(validResp.toUpperCase())) {
                otRendida.setBuenas(otRendida.getBuenas() + 1);
            } else {
                otRendida.setMalas(otRendida.getMalas() + 1);
            }
        }
        final float porcDificultad = prueba.getExigencia() == null ? 60f : prueba.getExigencia();
        final float notaMinima = 1.0f;
        otRendida.setNota(Utils.getNota(nroPreguntas, porcDificultad, otRendida.getBuenas(), notaMinima));
        final float total = otRendida.getBuenas() + otRendida.getMalas() + otRendida.getOmitidas();
        final float porcentaje = (float) otRendida.getBuenas() / total * 100f;
        final R_RangoEvaluacion rango = EntityUtils.getRango(porcentaje, rangos);
        otRendida.setNivel(rango);
    }
    protected void handlerExportar() {
        ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblListadoPruebas);
    }
    protected void handlerGrabar() {
        evalPrueba.setProfesor_id(cmbProfesor.getSelectionModel().getSelectedItem().getId());
        if (validate()) {
            if (!lstEvaluaciones.contains(evalPrueba)) {
                final String s = String.format("%s %s %s %s", asignatura, colegio, curso,
                        LocalDate.ofEpochDay(evalPrueba.getFecha()).toString());
                evalPrueba.setPrueba_id(prueba.getId());
                evalPrueba.setName(s);
            }
            final List<R_PruebaRendida> lstPruebasRendidas = new ArrayList<>();
            for (final OTPruebaRendida otPRendida : tblListadoPruebas.getItems()) {
                if (otPRendida.isRindioPrueba() && otPRendida.getRespuestas() != null && !otPRendida.getRespuestas().trim().isEmpty()) {
                    R_PruebaRendida pRendida = otPRendida.getPruebaRendida();
                    pRendida.setEvaluacionprueba_id(evalPrueba.getId());
                    if (pRendida.getId() != null) {
                        pRendida = (R_PruebaRendida) save(pRendida);
                    }
                    lstPruebasRendidas.add(pRendida);
                } else {
                    if (otPRendida.getPruebaRendida() != null) {
                        delete(otPRendida.getPruebaRendida(), false);
                    }
                }
            }
            mnuGrabar.setDisable(true);
            mnuScanner.setDisable(true);
            cmbProfesor.getSelectionModel().clearSelection();
            cmbColegios.getSelectionModel().clearSelection();
            cmbCursos.getItems().clear();
            cmbProfesor.requestFocus();
            controller.findById(R_Prueba.class, prueba.getId(), this);
        }
    }
    protected void handlerLeerImagenes() throws IOException {
        final FileChooser fileChooser = new FileChooser();
        final FileChooser.ExtensionFilter imageExtFilter = new FileChooser.ExtensionFilter("Archivos de Imágenes ", "*.png", "*.jpg");
        fileChooser.getExtensionFilters().add(imageExtFilter);
        fileChooser.setInitialDirectory(Utils.getDefaultDirectory());
        fileChooser.setTitle("Seleccione Imégenes Respuesta");
        final List<File> files = fileChooser.showOpenMultipleDialog(null);
        if (files != null && !files.isEmpty()) {
            final ProgressForm dlg = new ProgressForm();
            dlg.title("Procesando pruebas");
            dlg.message("Esto tomará algunos minutos.");
            final Task<Pair<ObservableList<R_Alumno>, ObservableList<R_PruebaRendida>>> task = new Task<Pair<ObservableList<R_Alumno>, ObservableList<R_PruebaRendida>>>() {
                @Override
                protected Pair<ObservableList<R_Alumno>, ObservableList<R_PruebaRendida>> call() throws Exception {
                    final int max = prueba.getNropreguntas();
                    int n = 1;
                    final Pair<ObservableList<R_Alumno>, ObservableList<R_PruebaRendida>> results = new Pair<ObservableList<R_Alumno>, ObservableList<R_PruebaRendida>>();
                    results.setFirst(FXCollections.observableArrayList());
                    results.setSecond(FXCollections.observableArrayList());
                    final ExtractorResultadosPrueba procesador = ExtractorResultadosPrueba.getInstance();
                    if (procesador != null && procesador.isValid()) {
                        for (final File archivo : files) {
                            try {
                                final OTResultadoScanner resultado = procesador.process(archivo, max);
                                if (resultado != null) {
                                    R_PruebaRendida pRendida;
                                    Pair<R_Alumno, R_PruebaRendida> res = obtenerPruebaRendida(resultado); 
                                    pRendida = res.getSecond();
                                    
                                    pRendida.setEvaluacionprueba_id(evalPrueba.getId());
                                    results.getFirst().add(res.getFirst());
                                    results.getSecond().add(pRendida);
                                    updateMessage("Procesado Alumno id[:" + res.getFirst()+ "]");
                                    updateProgress(n++, files.size());
                                } else {
                                    updateMessage("No se obtivieron resultados para el archivo:" + archivo.getName());
                                    updateProgress(n++, files.size());
                                    EvaluarPruebaView.log.severe("No se obtuvieron resultados");
                                }
                            } catch (final CPruebasException e) {
                                EvaluarPruebaView.log.severe("Archivo:" + archivo.getName() + " " + e.getMessage());
                                results.getFirst().add(null);
                                updateMessage(e.getMessage());
                                updateProgress(n++, files.size());
                            }
                        }
                    }
                    return results;
                }
            };
            task.setOnFailed(event -> {
                dlg.getDialogStage().hide();
                final Runnable r = () -> {
                    final Alert info = new Alert(AlertType.ERROR);
                    info.setTitle("Proceso finalizado con error");
                    info.setHeaderText("Se ha producido un error al procesar las imagenes.");
                    if (task.getException() instanceof IOException) {
                        task.getException().printStackTrace();
                        info.setContentText("No se han encontrado los archivos de red");
                    } else {
                        task.getException().printStackTrace();
                        info.setContentText("Error desconocido");
                    }
                    info.show();
                };
                Platform.runLater(r);
            });
            task.setOnSucceeded(event -> {
                final Pair<ObservableList<R_Alumno>, ObservableList<R_PruebaRendida>> res = task.getValue();
                final ObservableList<R_PruebaRendida> pruebas = res.getSecond();
                final ObservableList<R_Alumno> alumnos = res.getFirst();
                if (pruebas != null && !pruebas.isEmpty()) {
                    int index = 0;
                    for (final R_PruebaRendida pr : pruebas) {
                        
                        Optional<R_RangoEvaluacion> oRango = rangos.stream().filter(rr -> rr.getId().equals( pr.getRango_id())).findFirst();
                        final OTPruebaRendida ot = new OTPruebaRendida(pr, alumnos.get(index++));
                        final int idx = tblListadoPruebas.getItems().indexOf(ot);
                        if (idx == -1) {
                            tblListadoPruebas.getItems().add(ot);
                        } else {
                            final OTPruebaRendida oPr = tblListadoPruebas.getItems().get(idx);
                            oPr.setBuenas(pr.getBuenas());
                            oPr.setMalas(pr.getMalas());
                            oPr.setOmitidas(pr.getOmitidas());
                            oPr.setRespuestas(pr.getRespuestas());
                            oPr.setNivel(oRango.isPresent() ? oRango.get() : null);
                            oPr.setNota(pr.getNota());
                            oPr.setPuntaje(Utils.getPuntaje(pr.getNota()));
                            tblListadoPruebas.getItems().set(idx, oPr);
                        }
                    }
                }
                final int nPruebas = pruebas == null ? 0 : pruebas.size();
                final int nMalas = (int) (alumnos == null ? 0 : alumnos.stream().filter(p-> p == null).count());
                dlg.getDialogStage().hide();
                final Runnable r = () -> {
                    final Alert info = new Alert(AlertType.INFORMATION);
                    info.setTitle("Proceso finalizado");
                    info.setHeaderText("Recuerde grabar los resultados.");
                    info.setContentText(String.format("Se han procesado %d archivos exitosos de un total de %d.", nPruebas, nPruebas + nMalas));
                    info.show();
                };
                Platform.runLater(r);
            });
            dlg.showWorkerProgress(task);
            Executors.newSingleThreadExecutor().execute(task);
        }
    }
    @FXML
    public void initialize() {
        mnuScanner.setDisable(true);
        mnuGrabar.setDisable(true);
        cmbCursos.setDisable(true);
        dtpFecha.setValue(LocalDate.now());
        cmbColegios.setOnAction(event -> {
            cmbCursos.getItems().clear();
            final R_Colegio colegio = cmbColegios.getSelectionModel().getSelectedItem();
            if (colegio != null) {
                final Map<String, Object> parameters = new HashMap<String, Object>();
                parameters.put("tipocursor_id", tipoCurso.getId());
                parameters.put("colegio_id", colegio.getId());
                controller.findByParam(R_Curso.class, parameters);
            }
        });
        cmbCursos.setOnAction(new EHandlerCmbCurso());
        definirTablaListadoPruebas();
        mnuGrabar.setOnAction(event -> handlerGrabar());
        mnuNorinde.setOnAction(event -> {
            final Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("No rinde prueba");
            alert.setHeaderText("Registro será marcado.");
            alert.setContentText("Confirma que no rindió prueba?");
            final Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                final OTPruebaRendida ot = tblListadoPruebas.getSelectionModel().getSelectedItem();
                ot.setBuenas(0);
                ot.setMalas(0);
                ot.setOmitidas(0);
                ot.setRespuestas("");
                ot.setRindioPrueba(false);
            }
        });
        mnuExportar.setOnAction(event -> handlerExportar());
        menuExportar.setOnAction(event -> handlerExportar());
        mnuScanner.setOnAction(event -> {
            try {
                handlerLeerImagenes();
            } catch (final IOException e) {
                final Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error lectura Scanner.");
                alert.setHeaderText("Se ha producido leer un los archivos.");
                alert.setContentText("Revise las imagenes que se quieren importar");
                alert.show();
            }
        });
        mnuVolver.setOnAction(event -> {
            final IActivator activator = new PruebasActivator();
            WindowManager.getInstance().show(activator.getView());
        });
    }
    private R_Alumno obtenerAlumno(String rut, R_Curso curso) {
        R_Alumno respuesta = null;
        for (final R_Alumno alumno : lstAlumnos) {
            if (alumno.getRut().equalsIgnoreCase(rut)) {
                respuesta = alumno;
                break;
            }
        }
        return respuesta;
    }
    private Pair<R_Alumno, R_PruebaRendida> obtenerPruebaRendida(OTResultadoScanner resultado) throws CPruebasException {
        final R_Curso curso = cmbCursos.getValue();
        R_Alumno alumno = null; 
        R_PruebaRendida pRendida = null;
        
        String rut = resultado.getRut();
        if (rut != null && !rut.isEmpty()) {
            final StringBuilder sbRut = new StringBuilder(rut).insert(rut.length() - 1, '-');
            rut = sbRut.toString();
            alumno = obtenerAlumno(rut, curso);
            if (alumno == null) {
                throw new CPruebasException(String.format("El rut: %s no pertenece al colegio", rut));
            } else {
                final StringBuilder strResps = new StringBuilder(resultado.getRespuestas());
                int buenas = 0;
                int malas = 0;
                int omitidas = 0;
                int anuladas = 0;
                for (int n = 0; n < prueba.getNropreguntas(); n++) {
                    String letter = strResps.substring(n, n + 1);
                    final R_RespuestasEsperadasPrueba rEsperada = respuestas.get(n);
                    if (rEsperada.getAnulada()) {
                        rEsperada.setRespuesta("*");
                        strResps.replace(n, n + 1, "*");
                        anuladas++;
                        continue;
                    }
                    if ("O".equalsIgnoreCase(letter)) {
                        omitidas++;
                    } else if ("M".equalsIgnoreCase(letter)) {
                        malas++;
                    } else {
                        if (rEsperada.getMental()) {
                            if ("B".equalsIgnoreCase(letter)) {
                                strResps.replace(n, n + 1, "+");
                                buenas++;
                            } else if ("D".equalsIgnoreCase(letter)) {
                                strResps.replace(n, n + 1, "-");
                                malas++;
                            } else {
                                malas++;
                            }
                        } else if (rEsperada.getVerdaderofalso()) {
                            if ("B".equalsIgnoreCase(letter)) {
                                strResps.replace(n, n + 1, "V");
                                letter = "V";
                            } else if ("D".equalsIgnoreCase(letter)) {
                                strResps.replace(n, n + 1, "F");
                                letter = "F";
                            }
                            if (rEsperada.getRespuesta().equalsIgnoreCase(letter)) {
                                buenas++;
                            } else {
                                malas++;
                            }
                        } else {
                            if (rEsperada.getRespuesta().equalsIgnoreCase(letter)) {
                                buenas++;
                            } else {
                                malas++;
                            }
                        }
                    }
                }
                final int nroPreguntas = prueba.getNropreguntas() - anuladas;
                final float nota = Utils.getNota(nroPreguntas, prueba.getExigencia(), buenas, prueba.getPuntajebase());
                pRendida = new R_PruebaRendida.Builder().id(Utils.getLastIndex()).build();
                pRendida.setAlumno_id(alumno.getId());
                pRendida.setBuenas(buenas);
                pRendida.setMalas(malas);
                pRendida.setOmitidas(omitidas);
                pRendida.setNota(nota);
                pRendida.setRespuestas(strResps.toString());
                final float porcentaje = (float) pRendida.getBuenas() / nroPreguntas * 100f;
                final R_RangoEvaluacion rango = EntityUtils.getRango(porcentaje, rangos);
                pRendida.setRango_id(rango.getId());
                pRendida.setRespuestas(strResps.toString());
            }
        }
        return new Pair<R_Alumno, R_PruebaRendida>(alumno, pRendida);
    }
    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            final Object entity = list.get(0);
            if (entity instanceof R_Colegio) {
                final ObservableList<R_Colegio> oList = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    oList.add((R_Colegio) iEntity);
                }
                cmbColegios.setItems(oList);
            } else if (entity instanceof R_Curso) {
                final ObservableList<R_Curso> oList = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    oList.add((R_Curso) iEntity);
                }
                cmbCursos.setItems(oList);
                cmbCursos.setDisable(false);
            } else if (entity instanceof R_Profesor) {
                final ObservableList<R_Profesor> oList = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    oList.add((R_Profesor) iEntity);
                }
                cmbProfesor.setItems(oList);
            } else if(entity instanceof R_EvaluacionPrueba)
            {
                lstEvaluaciones = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    lstEvaluaciones.add((R_EvaluacionPrueba) iEntity);
                }
                
            }
        }
    }
    @Override
    public void onFound(IEntity entity) {
        txtNroAlternativas.setText("");
        txtNroPreguntas.setText("");
        if (entity instanceof R_Prueba) {
            tblListadoPruebas.getItems().clear();
            cmbColegios.getSelectionModel().clearSelection();
            cmbProfesor.getSelectionModel().clearSelection();
            cmbCursos.getSelectionModel().clearSelection();

            prueba = (R_Prueba) entity;
            Map<String, Object> params = MapBuilder.<String, Object> unordered().put("prueba_id", prueba.getId()).build();
            respuestas = controller.findByParamsSynchro(R_RespuestasEsperadasPrueba.class, params);
            asignatura = controller.findByIdSynchro(R_Asignatura.class, prueba.getAsignatura_id());
            Collections.sort(respuestas, Comparadores.compararRespuestasEsperadas());            
            
            curso = controller.findByIdSynchro(R_Curso.class, prueba.getCurso_id());
            colegio = controller.findByIdSynchro(R_Colegio.class, curso.getColegio_id());
            tipoCurso = controller.findByIdSynchro(R_TipoCurso.class, curso.getTipocurso_id());
            
            params = MapBuilder.<String, Object> unordered().put("nivelevaluacion_id", prueba.getNivelevaluacion_id()).build();
            rangos = controller.findByParamsSynchro(R_RangoEvaluacion.class, params);
            
            params = MapBuilder.<String, Object> unordered().put("prueba_id", prueba.getId()).put("curso_id", curso.getId()).build();
            controller.findByParam(R_EvaluacionPrueba.class, params);
            
            txtName.setText(prueba.getName());
            txtNroAlternativas.setText(prueba.getAlternativas().toString());
            txtNroPreguntas.setText(prueba.getNropreguntas().toString());
            txtAsignatura.setText(asignatura.getName());
        }
    }
    private void removeAllStyles() {
        removeAllStyle(lblError);
        removeAllStyle(cmbColegios);
        removeAllStyle(cmbProfesor);
        removeAllStyle(cmbCursos);
        removeAllStyle(dtpFecha);
    }
    @Override
    public boolean validate() {
        boolean valid = true;
        if (cmbColegios.getValue() == null) {
            valid = false;
            cmbColegios.getStyleClass().add("bad");
        }
        if (cmbCursos.getValue() == null) {
            valid = false;
            cmbCursos.getStyleClass().add("bad");
        }
        if (cmbProfesor.getValue() == null) {
            valid = false;
            cmbProfesor.getStyleClass().add("bad");
        }
        if (dtpFecha.getValue() == null) {
            valid = false;
            dtpFecha.getStyleClass().add("bad");
        }
        if (valid) {
            lblError.setText(" ");
            removeAllStyles();
        } else {
            lblError.getStyleClass().add("bad");
            lblError.setText("Corregir campos destacados en color rojo");
        }
        return valid;
    }
}
