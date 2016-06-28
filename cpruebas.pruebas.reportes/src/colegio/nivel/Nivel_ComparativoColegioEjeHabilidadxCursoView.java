package colegio.nivel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.imp.view.ProgressForm;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.EvaluacionEjeTematico;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.persistence.models.TipoAlumno;
import cl.eos.persistence.models.TipoCurso;
import cl.eos.persistence.util.Comparadores;
import cl.eos.util.ExcelSheetWriterObj;
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
    @SuppressWarnings("rawtypes")
    @FXML
    private TableView tblEjeshabilidades;
    @FXML
    private ComboBox<Colegio> cmbColegios;
    @FXML
    private ComboBox<Asignatura> cmbAsignatura;

    @FXML
    private ComboBox<TipoAlumno> cmbTipoAlumno;
    @FXML
    private Button btnReportes;
    @FXML
    private Label lblColegio;
    @FXML
    private Label lblTitulo;
    @FXML
    private MenuItem mnuExportarAlumnos;

    private Colegio colegioActivo;

    private Map<String, Object> parameters = new HashMap<String, Object>();
    private ObservableList<TipoCurso> listaTipoCursos;
    private ObservableList<EvaluacionEjeTematico> rangosEvaluacionPorcentaje;
    private ObservableList<EvaluacionPrueba> listaEvaluacionesPrueba;

    public Nivel_ComparativoColegioEjeHabilidadxCursoView() {
        setTitle("Comparativo Colegio Ejes Temáticos y Habilidades x Nivel");
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
            ExcelSheetWriterObj.generarReporteComparativoColegioEjeHabilidadCurso(tblEjeshabilidades,
                    colegioActivo.getName());
            colegioActivo = null;
        }
    }

    private void handleColegios() {
        Colegio colegio = cmbColegios.getSelectionModel().getSelectedItem();
        if (colegio != null) {
            parameters.put(COLEGIO_ID, colegio.getId());
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("coelgioId", colegio.getId());
            lblTitulo.setText(colegio.getName());
            controller.find("Curso.findByColegio", param);
            colegioActivo = colegio;
            clearContent();
        }
    }

    private void handleAsignatura() {
        Asignatura asignatura = cmbAsignatura.getSelectionModel().getSelectedItem();
        if (asignatura != null) {
            parameters.put(ASIGNATURA_ID, asignatura.getId());
            clearContent();
        }
    }

    private void handleReportes() {
        if (!parameters.isEmpty() && parameters.containsKey(COLEGIO_ID) && parameters.containsKey(ASIGNATURA_ID)) {

            controller.find("EvaluacionPrueba.findEvaluacionByColegioAsig", parameters, this);
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
            if (entity instanceof Colegio) {
                ObservableList<Colegio> oList = FXCollections.observableArrayList();
                for (Object iEntity : list) {
                    oList.add((Colegio) iEntity);
                }
                cmbColegios.setItems(oList);
            }
            if (entity instanceof Asignatura) {
                ObservableList<Asignatura> oList = FXCollections.observableArrayList();
                for (Object iEntity : list) {
                    oList.add((Asignatura) iEntity);
                }
                cmbAsignatura.setItems(oList);
            }
            if (entity instanceof TipoCurso) {
                listaTipoCursos = FXCollections.observableArrayList();
                for (Object iEntity : list) {
                    listaTipoCursos.add((TipoCurso) iEntity);
                }
                FXCollections.sort(listaTipoCursos, Comparadores.comparaTipoCurso());
            }

            if (entity instanceof TipoAlumno) {
                ObservableList<TipoAlumno> tAlumnoList = FXCollections.observableArrayList();
                for (Object iEntity : list) {
                    tAlumnoList.add((TipoAlumno) iEntity);
                }
                cmbTipoAlumno.setItems(tAlumnoList);

            }
            if (entity instanceof EvaluacionEjeTematico) {
                rangosEvaluacionPorcentaje = FXCollections.observableArrayList();
                for (Object object : list) {
                    EvaluacionEjeTematico evaluacion = (EvaluacionEjeTematico) object;
                    rangosEvaluacionPorcentaje.add(evaluacion);
                }
                tareaGenerarReporte();
            }
            if (entity instanceof EvaluacionPrueba) {
                listaEvaluacionesPrueba = FXCollections.observableArrayList();
                for (Object object : list) {
                    EvaluacionPrueba evaluacion = (EvaluacionPrueba) object;
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
        tc.setCellValueFactory(param -> new SimpleStringProperty(
                ((CellDataFeatures<ObservableList, String>) param).getValue().get(0).toString()));

        Runnable r = new Runnable() {
            @Override
            public void run() {
                tblEjeshabilidades.getColumns().add(tc);
            }

        };
        Platform.runLater(r);

        List<TipoCurso> mCursos = new ArrayList<TipoCurso>();
        for (EvaluacionPrueba evPrueba : listaEvaluacionesPrueba) {
            TipoCurso tipoCurso = evPrueba.getCurso().getTipoCurso();
            if (!mCursos.contains(tipoCurso))
                mCursos.add(tipoCurso);
        }
        // Ordenar los cursos
        int nCol = 1;
        for (TipoCurso tipoCurso : mCursos) {

            TableColumn colCurso = new TableColumn(tipoCurso.getName());
            for (EvaluacionEjeTematico evEjeHab : rangosEvaluacionPorcentaje) {
                final int idx = nCol;
                TableColumn colEjeHab = new TableColumn(evEjeHab.getName());
                colEjeHab.setSortable(false);
                colEjeHab.setStyle("-fx-alignment: CENTER;");
                colEjeHab.setCellValueFactory(
                        new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
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

    private void fillColumnEjeHabilidad(List<EjeTematico> listaEjesTematicos, List<Habilidad> listaHablidades,
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

    private List<OTUnCursoUnEjeHabilidad> evaluarUnCurso(List<RespuestasEsperadasPrueba> respEsperadas,
            EvaluacionPrueba evaluacion) {
        List<OTUnCursoUnEjeHabilidad> listaOTUnCurso = new ArrayList<OTUnCursoUnEjeHabilidad>();
        // Cada prueba rendida equivale a un ALUMNO DEL CURSO
        int nAlumno = 0;
        long tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedItem().getId();
        for (PruebaRendida pRendida : evaluacion.getPruebasRendidas()) {
            Alumno alumno = pRendida.getAlumno();
            if (tipoAlumno != Constants.PIE_ALL && tipoAlumno != alumno.getTipoAlumno().getId()) {
                // En este caso, no se considera este alumno para el
                // cálculo.
                continue;
            }
            evaluarUnAlumno(pRendida, respEsperadas, nAlumno, listaOTUnCurso);
            nAlumno++; // Siguiente alumno.
        }
        return listaOTUnCurso;
    }

    private List<OTUnCursoUnEjeHabilidad> evaluarUnAlumno(PruebaRendida pRendida,
            List<RespuestasEsperadasPrueba> respEsperadas, int nAlumno, List<OTUnCursoUnEjeHabilidad> listaOTUnCurso) {
        String respuestas = pRendida.getRespuestas();
        int nResp = 0;
        int nroAlumnos = pRendida.getEvaluacionPrueba().getPruebasRendidas().size();

        for (RespuestasEsperadasPrueba resp : respEsperadas) {

            OTUnCursoUnEjeHabilidad otEje = new OTUnCursoUnEjeHabilidad(resp.getEjeTematico(), nroAlumnos);
            OTUnCursoUnEjeHabilidad otHab = new OTUnCursoUnEjeHabilidad(resp.getHabilidad(), nroAlumnos);

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

    private Map<IEntity, List<OTTipoCursoRangos>> buildDataReport(int nroTipoCurso,
            List<EjeTematico> listaEjesTematicos, List<Habilidad> listaHablidades) {
        Map<IEntity, List<OTTipoCursoRangos>> report = new HashMap<IEntity, List<OTTipoCursoRangos>>();

        for (EvaluacionPrueba evaluacion : listaEvaluacionesPrueba) {
            List<RespuestasEsperadasPrueba> rEsperadas = evaluacion.getPrueba().getRespuestas();

            // Obtengo lista de habilidades de la PRUEBA base de la EVALUACION.
            for (RespuestasEsperadasPrueba rEsperada : rEsperadas) {
                if (!report.containsKey(rEsperada.getHabilidad())) {
                    List<OTTipoCursoRangos> list = Stream.generate(OTTipoCursoRangos::new).limit(nroTipoCurso)
                            .collect(Collectors.toList());

                    report.put(rEsperada.getHabilidad(), list);
                }
                if (!listaHablidades.contains(rEsperada.getHabilidad())) {
                    listaHablidades.add(rEsperada.getHabilidad());
                }

                if (!report.containsKey(rEsperada.getEjeTematico())) {
                    List<OTTipoCursoRangos> list = Stream.generate(OTTipoCursoRangos::new).limit(nroTipoCurso)
                            .collect(Collectors.toList());
                    report.put(rEsperada.getEjeTematico(), list);
                }

                if (!listaEjesTematicos.contains(rEsperada.getEjeTematico())) {
                    listaEjesTematicos.add(rEsperada.getEjeTematico());
                }
            }
        }

        return report;
    }

    private void tareaGenerarReporte() {

        if (listaEvaluacionesPrueba == null || rangosEvaluacionPorcentaje == null) {
            // No hay valores para procesar todo.
            return;
        }

        clearContent();
        ProgressForm pForm = new ProgressForm();
        pForm.title("Generando reporte");
        pForm.message("Esto tomará algunos segundos.");

        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                updateMessage("Generando reporte");
                Pair<Map<IEntity, List<OTTipoCursoRangos>>, Pair<List<EjeTematico>, List<Habilidad>>> resultado = generarReporte();
                Map<IEntity, List<OTTipoCursoRangos>> reporte = resultado.getFirst();
                List<EjeTematico> listaEjesTematicos = resultado.getSecond().getFirst();
                List<Habilidad> listaHablidades = resultado.getSecond().getSecond();
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
     * <li>Obtener resumen de preguntas buenas de cada Eje/Hab x Alumno. </lu>
     */
    private Pair<Map<IEntity, List<OTTipoCursoRangos>>, Pair<List<EjeTematico>, List<Habilidad>>> generarReporte() {

        // Obtener la cantidad de niveles que existen.

        List<TipoCurso> lstTipoCurso = new ArrayList<>();
        for (EvaluacionPrueba evaluacion : listaEvaluacionesPrueba) {
            TipoCurso tipoCurso = evaluacion.getCurso().getTipoCurso();
            if (!lstTipoCurso.contains(tipoCurso)) {
                lstTipoCurso.add(tipoCurso);
            }
        }
        int nroTipoCurso = lstTipoCurso.size();
        List<EjeTematico> listaEjesTematicos = new ArrayList<>();
        List<Habilidad> listaHablidades = new ArrayList<>();
        Map<IEntity, List<OTTipoCursoRangos>> reporte = buildDataReport(nroTipoCurso, listaEjesTematicos,
                listaHablidades);

        // Va a tener los resulados finales
        int nroCurso = -1;
        List<TipoCurso> tiposCurso = new ArrayList<>();
        for (EvaluacionPrueba evaluacion : listaEvaluacionesPrueba) {
            TipoCurso tipoCurso = evaluacion.getCurso().getTipoCurso();
            nroCurso = tiposCurso.indexOf(tipoCurso);
            if (nroCurso == -1) {
                tiposCurso.add(tipoCurso);
                nroCurso = tiposCurso.size() - 1;
            }

            List<RespuestasEsperadasPrueba> respEsperadas = evaluacion.getPrueba().getRespuestas();

            List<OTUnCursoUnEjeHabilidad> listaOTUnCurso = evaluarUnCurso(respEsperadas, evaluacion);

            // Aqui cuento la cantida de alumnos en los rangos
            for (OTUnCursoUnEjeHabilidad ot : listaOTUnCurso) {
                // Calcula cantidad de alumnos en los rangos para un eje y un
                // curso
                int[] alumXRango = ot.calculateAlumnosXRango(rangosEvaluacionPorcentaje);
                OTTipoCursoRangos cursoRango = new OTTipoCursoRangos(evaluacion.getCurso().getTipoCurso(), alumXRango);
                List<OTTipoCursoRangos> listCursos = reporte.get(ot.getEjeHabilidad());
                Logger.getLogger(this.getClass()).info(listCursos);
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
        Pair<List<EjeTematico>, List<Habilidad>> listas = new Pair<List<EjeTematico>, List<Habilidad>>(
                listaEjesTematicos, listaHablidades);
        return new Pair<Map<IEntity, List<OTTipoCursoRangos>>, Pair<List<EjeTematico>, List<Habilidad>>>(reporte,
                listas);
    }

    private void clearContent() {
        tblEjeshabilidades.getItems().clear();
        tblEjeshabilidades.getColumns().clear();
    }

}
