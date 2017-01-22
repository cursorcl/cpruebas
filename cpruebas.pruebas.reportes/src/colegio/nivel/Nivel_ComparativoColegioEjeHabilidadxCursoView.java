package colegio.nivel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.imp.view.ProgressForm;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.util.Comparadores;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_Ejetematico;
import cl.eos.restful.tables.R_EvaluacionEjetematico;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_Habilidad;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.restful.tables.R_TipoCurso;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.MapBuilder;
import cl.eos.util.Pair;
import comparativo.colegio.eje.habilidad.x.curso.OTTipoCursoRangos;
import comparativo.colegio.eje.habilidad.x.curso.OTUnCursoUnEjeHabilidad;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class Nivel_ComparativoColegioEjeHabilidadxCursoView extends AFormView implements EventHandler<ActionEvent> {
    private static final String ASIGNATURA_ID = "idAsignatura";
    private static final String COLEGIO_ID = "idColegio";
    private static final Logger log = Logger.getLogger(Nivel_ComparativoColegioEjeHabilidadxCursoView.class.getName());
    @SuppressWarnings("rawtypes") @FXML private TableView tblEjeshabilidades;
    @FXML private ComboBox<R_Colegio> cmbColegios;
    @FXML private ComboBox<R_Asignatura> cmbAsignatura;
    @FXML private ComboBox<R_TipoAlumno> cmbTipoAlumno;
    @FXML private Button btnReportes;
    @FXML private Label lblColegio;
    @FXML private Label lblTitulo;
    @FXML private MenuItem mnuExportarAlumnos;
    private R_Colegio colegioActivo;
    private R_Prueba prueba;
    private Map<String, Object> parameters = new HashMap<String, Object>();
    private ObservableList<R_TipoCurso> listaTipoCursos;
    private ObservableList<R_EvaluacionEjetematico> rangosEvaluacionPorcentaje;
    private ObservableList<R_EvaluacionPrueba> listaEvaluacionesPrueba;
    /**
     * Respuestas esperadas
     */
    private Map<Long, List<R_RespuestasEsperadasPrueba>> respuestasEsperadas = new HashMap<>();
    Map<Long, R_Habilidad> mapHabilidades = new HashMap<>();
    Map<Long, R_Ejetematico> mapEjes = new HashMap<>();
    public Nivel_ComparativoColegioEjeHabilidadxCursoView() {
        setTitle("Comparativo R_Colegio Ejes Temáticos y Habilidades x Nivel");
    }
    @SuppressWarnings("unchecked")
    @Override
    public void handle(ActionEvent event) {
        Object source = event.getSource();
        if (source == cmbColegios) {
            handleColegios();
        }
        if (source == cmbAsignatura) {
            handleAsignatura();
        }
        if (source == btnReportes) {
            handleReportes();
        }
        if (source == mnuExportarAlumnos) {
            ExcelSheetWriterObj.generarReporteComparativoColegioEjeHabilidadCurso(tblEjeshabilidades, colegioActivo.getName());
            colegioActivo = null;
        }
    }
    private void handleColegios() {
        R_Colegio colegio = cmbColegios.getSelectionModel().getSelectedItem();
        if (colegio != null) {
            parameters.put(COLEGIO_ID, colegio.getId());
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("colegioId", colegio.getId());
            lblTitulo.setText(colegio.getName());
            controller.findByParam(R_Curso.class, param, this);
            colegioActivo = colegio;
            clearContent();
        }
    }
    private void handleAsignatura() {
        R_Asignatura asignatura = cmbAsignatura.getSelectionModel().getSelectedItem();
        if (asignatura != null) {
            parameters.put(ASIGNATURA_ID, asignatura.getId());
            clearContent();
        }
    }
    private void handleReportes() {
        if (!parameters.isEmpty() && parameters.containsKey(COLEGIO_ID) && parameters.containsKey(ASIGNATURA_ID)) {
            controller.findByParam(R_EvaluacionPrueba.class, parameters, this);
        }
    }
    @FXML
    public void initialize() {
        inicializaComponentes();
    }
    private void inicializaComponentes() {
        cmbColegios.setOnAction(this);
        cmbAsignatura.setOnAction(this);
        btnReportes.setOnAction(this);
        mnuExportarAlumnos.setOnAction(this);
    }
    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            Object entity = list.get(0);
            if (entity instanceof R_Colegio) {
                ObservableList<R_Colegio> oList = FXCollections.observableArrayList();
                for (Object iEntity : list) {
                    oList.add((R_Colegio) iEntity);
                }
                cmbColegios.setItems(oList);
            }
            if (entity instanceof R_Asignatura) {
                ObservableList<R_Asignatura> oList = FXCollections.observableArrayList();
                for (Object iEntity : list) {
                    oList.add((R_Asignatura) iEntity);
                }
                cmbAsignatura.setItems(oList);
            }
            if (entity instanceof R_TipoCurso) {
                listaTipoCursos = FXCollections.observableArrayList();
                for (Object iEntity : list) {
                    listaTipoCursos.add((R_TipoCurso) iEntity);
                }
                FXCollections.sort(listaTipoCursos, Comparadores.comparaTipoCurso());
            }
            if (entity instanceof R_TipoAlumno) {
                ObservableList<R_TipoAlumno> tAlumnoList = FXCollections.observableArrayList();
                for (Object iEntity : list) {
                    tAlumnoList.add((R_TipoAlumno) iEntity);
                }
                cmbTipoAlumno.setItems(tAlumnoList);
            }
            if (entity instanceof R_EvaluacionEjetematico) {
                rangosEvaluacionPorcentaje = FXCollections.observableArrayList();
                for (Object object : list) {
                    R_EvaluacionEjetematico evaluacion = (R_EvaluacionEjetematico) object;
                    rangosEvaluacionPorcentaje.add(evaluacion);
                }
                tareaGenerarReporte();
            }
            if (entity instanceof R_EvaluacionPrueba) {
                listaEvaluacionesPrueba = FXCollections.observableArrayList();
                for (Object object : list) {
                    R_EvaluacionPrueba evaluacion = (R_EvaluacionPrueba) object;
                    listaEvaluacionesPrueba.add(evaluacion);
                }
                tareaGenerarReporte();
            }
        } else if (list != null && list.isEmpty()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("No hay registros.");
            alert.setHeaderText(null);
            alert.setContentText("No se ha encontrado registros para la consulta.");
            alert.showAndWait();
        }
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void makeTable() {
        TableColumn tc = new TableColumn("EJE / HABILIDAD");
        tc.setSortable(false);
        tc.setStyle("-fx-alignment: CENTER-LEFT;");
        tc.prefWidthProperty().set(250f);
        tc.setCellValueFactory(param -> new SimpleStringProperty(((CellDataFeatures<ObservableList, String>) param).getValue().get(0).toString()));
        Runnable r = new Runnable() {
            @Override
            public void run() {
                tblEjeshabilidades.getColumns().add(tc);
            }
        };
        Platform.runLater(r);
        List<R_TipoCurso> mCursos = new ArrayList<R_TipoCurso>();
        for (R_EvaluacionPrueba evPrueba : listaEvaluacionesPrueba) {
            Optional<R_TipoCurso> op = listaTipoCursos.stream().filter(t -> t.getId().equals(evPrueba.getTipocurso_id())).findFirst();
            if (!op.isPresent()) continue;
            R_TipoCurso tipoCurso = op.get();
            if (!mCursos.contains(tipoCurso)) mCursos.add(tipoCurso);
        }
        // Ordenar los cursos
        int nCol = 1;
        for (R_TipoCurso tipoCurso : mCursos) {
            TableColumn colCurso = new TableColumn(tipoCurso.getName());
            for (R_EvaluacionEjetematico evEjeHab : rangosEvaluacionPorcentaje) {
                final int idx = nCol;
                TableColumn colEjeHab = new TableColumn(evEjeHab.getName());
                colEjeHab.setSortable(false);
                colEjeHab.setStyle("-fx-alignment: CENTER;");
                colEjeHab.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(idx).toString());
                    }
                });
                colCurso.getColumns().add(colEjeHab);
                nCol++;
            }
            r = new Runnable() {
                @Override
                public void run() {
                    tblEjeshabilidades.getColumns().add(colCurso);
                }
            };
            Platform.runLater(r);
        }
    }
    private void fillColumnEjeHabilidad(List<R_Ejetematico> listaEjesTematicos, List<R_Habilidad> listaHablidades,
            Map<IEntity, List<OTTipoCursoRangos>> reporte) {
        List<IEntity> entidades = new ArrayList<IEntity>();
        entidades.addAll(listaEjesTematicos);
        entidades.addAll(listaHablidades);
        ObservableList<String> registro = null;
        ObservableList<ObservableList<String>> items = FXCollections.observableArrayList();
        for (IEntity entity : entidades) {
            registro = FXCollections.observableArrayList();
            registro.add(entity.getName());
            List<OTTipoCursoRangos> lista = reporte.get(entity);
            int nroEjes = rangosEvaluacionPorcentaje.size();
            for (int idx = 0; idx < lista.size(); idx++) {
                OTTipoCursoRangos ot = lista.get(idx);
                for (int n = 0; n < nroEjes; n++) {
                    if (ot != null && ot.getNroAlumnosXEjeHab() != null) {
                        registro.add(String.format("%3d", ot.getNroAlumnosXEjeHab()[n]));
                    } else {
                        registro.add("-");
                    }
                }
            }
            items.add(registro);
        }
        Runnable r = new Runnable() {
            @SuppressWarnings("unchecked")
            @Override
            public void run() {
                tblEjeshabilidades.getItems().setAll(items);
            }
        };
        Platform.runLater(r);
    }
    private List<OTUnCursoUnEjeHabilidad> evaluarUnCurso(List<R_RespuestasEsperadasPrueba> respEsperadas, R_EvaluacionPrueba evaluacion) {
        List<OTUnCursoUnEjeHabilidad> listaOTUnCurso = new ArrayList<OTUnCursoUnEjeHabilidad>();
        // Cada prueba rendida equivale a un ALUMNO DEL CURSO
        int nAlumno = 0;
        long tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedItem().getId();
        Map<String, Object> params = MapBuilder.<String, Object> unordered().put("evaluacionprueba_id", evaluacion.getId()).build();
        List<R_PruebaRendida> lstPRendidas = controller.findByParamsSynchro(R_PruebaRendida.class, params);
        for (R_PruebaRendida pRendida : lstPRendidas) {
            if (tipoAlumno != Constants.PIE_ALL && pRendida.getTipoalumno_id() != null && tipoAlumno != pRendida.getTipoalumno_id()) {
                // En este caso, no se considera este alumno para el
                // cálculo.
                continue;
            }
            evaluarUnAlumno(pRendida, respEsperadas, nAlumno, listaOTUnCurso, lstPRendidas);
            nAlumno++; // Siguiente alumno.
        }
        return listaOTUnCurso;
    }
    private List<OTUnCursoUnEjeHabilidad> evaluarUnAlumno(R_PruebaRendida pRendida, List<R_RespuestasEsperadasPrueba> respEsperadas, int nAlumno,
            List<OTUnCursoUnEjeHabilidad> listaOTUnCurso, List<R_PruebaRendida> lstPRendidas) {
        String respuestas = pRendida.getRespuestas();
        int nResp = 0;
        int nroAlumnos = lstPRendidas.size();
        for (R_RespuestasEsperadasPrueba resp : respEsperadas) {
            R_Ejetematico eje = mapEjes.get(resp.getEjetematico_id());
            R_Habilidad hab = mapHabilidades.get(resp.getHabilidad_id());
            OTUnCursoUnEjeHabilidad otEje = new OTUnCursoUnEjeHabilidad(eje, nroAlumnos);
            OTUnCursoUnEjeHabilidad otHab = new OTUnCursoUnEjeHabilidad(hab, nroAlumnos);
            if (listaOTUnCurso.contains(otEje)) {
                // Me aseguro que es el que existe
                otEje = listaOTUnCurso.get(listaOTUnCurso.indexOf(otEje));
            } else {
                // Lo agrego a la lista.
                listaOTUnCurso.add(otEje);
            }
            if (listaOTUnCurso.contains(otHab)) {
                // Me aseguro que es el que existe
                otHab = listaOTUnCurso.get(listaOTUnCurso.indexOf(otHab));
            } else {
                // Lo agrego a la lista.
                listaOTUnCurso.add(otHab);
            }
            if (nAlumno == 0) {
                // Contamos las preguntas para eje/habilidad solo en el
                // primer alumno.
                otEje.setNroPreguntas(otEje.getNroPreguntas() + 1);
                otHab.setNroPreguntas(otHab.getNroPreguntas() + 1);
            }
            if (respuestas.length() <= nResp) {
                // No hay más respuesta
                break;
            }
            String r = respuestas.substring(nResp, nResp + 1);
            if (resp.getAnulada() || "O".equals(r) || "-".equals(r)) {
                // La respuesta no se considera
                nResp++;
                continue;
            }
            // Aqui agrego 1 a las buenas del eje y de la habilidad del
            // alumno n.
            if (resp.getRespuesta().equals(r)) {
                float buenasEje = otEje.getBuenasPorAlumno()[nAlumno];
                otEje.getBuenasPorAlumno()[nAlumno] = buenasEje + 1;
                float buenasHab = otHab.getBuenasPorAlumno()[nAlumno];
                otHab.getBuenasPorAlumno()[nAlumno] = buenasHab + 1;
            }
            nResp++; // Siguiente respuesta
        }
        return listaOTUnCurso;
    }
    private Map<IEntity, List<OTTipoCursoRangos>> buildDataReport(int nroTipoCurso, List<R_Ejetematico> listaEjesTematicos,
            List<R_Habilidad> listaHablidades) {
        Map<IEntity, List<OTTipoCursoRangos>> report = new HashMap<IEntity, List<OTTipoCursoRangos>>();
        // Voy a contener las respuestasEsperadas esperadas de cada prueba.
        for (R_EvaluacionPrueba evaluacion : listaEvaluacionesPrueba) {
            List<R_RespuestasEsperadasPrueba> rEsperadas = respuestasEsperadas.get(evaluacion.getPrueba_id());
            if (rEsperadas == null) {
                Map<String, Object> parameters = MapBuilder.<String, Object> unordered().put("prueba_id", evaluacion.getPrueba_id()).build();
                rEsperadas = controller.findByParamsSynchro(R_RespuestasEsperadasPrueba.class, parameters);
                respuestasEsperadas.put(evaluacion.getPrueba_id(), rEsperadas);
            }
            // Obtengo lista de habilidades de la PRUEBA base de la EVALUACION.
            for (R_RespuestasEsperadasPrueba rEsperada : rEsperadas) {
                R_Habilidad hab = mapHabilidades.get(rEsperada.getHabilidad_id());
                if (hab == null) {
                    hab = controller.findSynchroById(R_Habilidad.class, rEsperada.getHabilidad_id());
                    mapHabilidades.put(rEsperada.getHabilidad_id(), hab);
                }
                if (!report.containsKey(hab)) {
                    List<OTTipoCursoRangos> list = Stream.generate(OTTipoCursoRangos::new).limit(nroTipoCurso).collect(Collectors.toList());
                    report.put(hab, list);
                }
                if (!listaHablidades.contains(hab)) {
                    listaHablidades.add(hab);
                }
                R_Ejetematico eje = mapEjes.get(rEsperada.getEjetematico_id());
                if (eje == null) {
                    eje = controller.findSynchroById(R_Ejetematico.class, rEsperada.getEjetematico_id());
                    mapEjes.put(rEsperada.getEjetematico_id(), eje);
                }
                if (!report.containsKey(eje)) {
                    List<OTTipoCursoRangos> list = Stream.generate(OTTipoCursoRangos::new).limit(nroTipoCurso).collect(Collectors.toList());
                    report.put(eje, list);
                }
                if (!listaEjesTematicos.contains(eje)) {
                    listaEjesTematicos.add(eje);
                }
            }
        }
        return report;
    }
    private void tareaGenerarReporte() {
        if (listaEvaluacionesPrueba == null || rangosEvaluacionPorcentaje == null) return;
        clearContent();
        ProgressForm pForm = new ProgressForm();
        pForm.title("Generando reporte");
        pForm.message("Esto tomará algunos segundos.");
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                updateMessage("Generando reporte");
                Pair<Map<IEntity, List<OTTipoCursoRangos>>, Pair<List<R_Ejetematico>, List<R_Habilidad>>> resultado = generarReporte();
                Map<IEntity, List<OTTipoCursoRangos>> reporte = resultado.getFirst();
                List<R_Ejetematico> listaEjesTematicos = resultado.getSecond().getFirst();
                List<R_Habilidad> listaHablidades = resultado.getSecond().getSecond();
                updateMessage("Construyendo tabla");
                makeTable();
                updateMessage("Llenando valores en tabla");
                fillColumnEjeHabilidad(listaEjesTematicos, listaHablidades, reporte);
                return Boolean.TRUE;
            }
        };
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                pForm.getDialogStage().hide();
            }
        });
        task.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                pForm.getDialogStage().hide();
            }
        });
        pForm.showWorkerProgress(task);
        Executors.newSingleThreadExecutor().execute(task);
    }
    /**
     * Para ontener el resultado final se siguen los sieguientes pasos: <lu>
     * <li>Obtener resumen de preguntas buenas de cada Eje/Hab x R_Alumno. </lu>
     */
    private Pair<Map<IEntity, List<OTTipoCursoRangos>>, Pair<List<R_Ejetematico>, List<R_Habilidad>>> generarReporte() {
        // Obtener la cantidad de niveles que existen.
        List<R_TipoCurso> lstTipoCurso = new ArrayList<>();
        for (R_EvaluacionPrueba evaluacion : listaEvaluacionesPrueba) {
            Optional<R_TipoCurso> op = listaTipoCursos.stream().filter(t -> t.getId().equals(evaluacion.getTipocurso_id())).findFirst();
            if (!op.isPresent()) continue;
            R_TipoCurso tipoCurso = op.get();
            if (!lstTipoCurso.contains(tipoCurso)) {
                lstTipoCurso.add(tipoCurso);
            }
        }
        int nroTipoCurso = lstTipoCurso.size();
        List<R_Ejetematico> listaEjesTematicos = new ArrayList<>();
        List<R_Habilidad> listaHablidades = new ArrayList<>();
        Map<IEntity, List<OTTipoCursoRangos>> reporte = buildDataReport(nroTipoCurso, listaEjesTematicos, listaHablidades);
        // Va a tener los resulados finales
        int nroCurso = -1;
        List<R_TipoCurso> tiposCurso = new ArrayList<>();
        for (R_EvaluacionPrueba evaluacion : listaEvaluacionesPrueba) {
            Optional<R_TipoCurso> op = listaTipoCursos.stream().filter(t -> t.getId().equals(evaluacion.getTipocurso_id())).findFirst();
            if (!op.isPresent()) continue;
            R_TipoCurso tipoCurso = op.get();
            nroCurso = tiposCurso.indexOf(tipoCurso);
            if (nroCurso == -1) {
                tiposCurso.add(tipoCurso);
                nroCurso = tiposCurso.size() - 1;
            }
            List<R_RespuestasEsperadasPrueba> respEsperadas = getRespuestasEsperadas(evaluacion.getPrueba_id());
            List<OTUnCursoUnEjeHabilidad> listaOTUnCurso = evaluarUnCurso(respEsperadas, evaluacion);
            // Aqui cuento la cantida de items en los rangos
            for (OTUnCursoUnEjeHabilidad ot : listaOTUnCurso) {
                // Calcula cantidad de items en los rangos para un eje y un
                // colegio
                int[] alumXRango = ot.calculateAlumnosXRango(rangosEvaluacionPorcentaje);
                op = listaTipoCursos.stream().filter(t -> t.getId().equals(evaluacion.getTipocurso_id())).findFirst();
                if (!op.isPresent()) continue;
                tipoCurso = op.get();
                OTTipoCursoRangos cursoRango = new OTTipoCursoRangos(tipoCurso, alumXRango);
                List<OTTipoCursoRangos> listCursos = reporte.get(ot.getEjeHabilidad());
                log.fine("Se han obtenido:" + (listCursos != null ? listCursos.size() : 0) + " OTTipoCursoRangos");
                int idx = -1;
                if ((idx = listCursos.indexOf(cursoRango)) != -1) {
                    OTTipoCursoRangos oListCursos = listCursos.get(idx);
                    oListCursos.add(cursoRango);
                    listCursos.set(idx, oListCursos);
                } else {
                    listCursos.set(nroCurso, cursoRango);
                }
            }
        }
        // Tengo todos los resultados en el map (reporte)
        // Ahora debo generar la tabla.
        Pair<List<R_Ejetematico>, List<R_Habilidad>> listas = new Pair<List<R_Ejetematico>, List<R_Habilidad>>(listaEjesTematicos, listaHablidades);
        return new Pair<Map<IEntity, List<OTTipoCursoRangos>>, Pair<List<R_Ejetematico>, List<R_Habilidad>>>(reporte, listas);
    }
    /**
     * Obtiene las respuestas esperadas de una prueba. Luego la regustra en un mapa asociando la prueba con sus
     * respuestas, logrando así reutilizar las búsquedas.
     * 
     * @param prueba_id
     *            Identificador de la prueba
     * @return Lista de respuestas esperadas de la prueba.
     */
    private List<R_RespuestasEsperadasPrueba> getRespuestasEsperadas(Long prueba_id) {
        List<R_RespuestasEsperadasPrueba> rEsperadas = respuestasEsperadas.get(prueba_id);
        if (rEsperadas == null) {
            Map<String, Object> parameters = MapBuilder.<String, Object> unordered().put("prueba_id", prueba_id).build();
            rEsperadas = controller.findByParamsSynchro(R_RespuestasEsperadasPrueba.class, parameters);
            respuestasEsperadas.put(prueba_id, rEsperadas);
        }
        return rEsperadas;
    }
    /**
     * @return the prueba
     */
    public final R_Prueba getPrueba() {
        return prueba;
    }
    /**
     * @param prueba
     *            the prueba to set
     */
    public final void setPrueba(R_Prueba prueba) {
        this.prueba = prueba;
    }
    private void clearContent() {
        tblEjeshabilidades.getItems().clear();
        tblEjeshabilidades.getColumns().clear();
    }
}
