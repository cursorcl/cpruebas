package colegio.nivel.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cl.eos.common.Constants;
import cl.eos.provider.persistence.PersistenceServiceFactory;
import cl.eos.restful.tables.R_Alumno;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_Ejetematico;
import cl.eos.restful.tables.R_Habilidad;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.util.Pair;
import cl.eos.util.Utils;
import cl.eos.view.ots.resumenxalumno.eje.habilidad.OTAlumnoResumen;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

public class Nivel_CursoEjeHabilidad {
    private final int FIXED_COLUMNS = 4;
    @SuppressWarnings("rawtypes") private TableView tblAlumnos;
    private List<R_Ejetematico> lstEjes;
    private List<R_Habilidad> lstHabs;
    private List<R_RespuestasEsperadasPrueba> respEsperadas;
    private List<R_PruebaRendida> pRendidas;
    private List<R_Alumno> alumnos;
    private R_TipoAlumno tipoAlumno;
    private R_Curso curso;
    @SuppressWarnings("rawtypes")
    public Nivel_CursoEjeHabilidad(List<R_RespuestasEsperadasPrueba> respEsperadas, List<R_PruebaRendida> pRendidas, List<R_Alumno> alumnos,
            R_TipoAlumno tipoAlumno, R_Curso curso) {
        super();
        this.tipoAlumno = tipoAlumno;
        this.curso = curso;
        this.respEsperadas = respEsperadas;
        this.pRendidas = pRendidas;
        this.alumnos = alumnos;
        tblAlumnos = new TableView();
        tblAlumnos.setId(this.curso.getName());
        lstEjes = getEjesTematicos();
        lstHabs = getHabilidades();
        generate();
    }
    public final void generate() {
        List<OTAlumnoResumen> puntos = obtenerPuntos();
        construirColumnas();
        llenarValores(puntos);
    }
    @SuppressWarnings("unchecked")
    private void llenarValores(List<OTAlumnoResumen> puntos) {
        ObservableList<ObservableList<String>> contenido = FXCollections.observableArrayList();
        ObservableList<String> row = null;
        for (OTAlumnoResumen ot : puntos) {
            row = FXCollections.observableArrayList();
            if (ot.getAlumno() != null) {
                row.add(ot.getAlumno().getRut());
                row.add(ot.getAlumno().getPaterno());
                row.add(ot.getAlumno().getMaterno());
                row.add(ot.getAlumno().getName());
            } else {
                row.add("--");
                row.add("--");
                row.add("--");
                row.add("--");
            }
            List<Float> values = ot.getPorcentajes();
            for (Float value : values) {
                row.add(String.valueOf(Utils.redondeo1Decimal(value)));
            }
            contenido.add(row);
        }
        tblAlumnos.setItems(contenido);
    }
    /**
     * Calcula los puntos obtenidos en cada eje/habilidad por alumno.
     * 
     * @param evaluacionPrueba
     *            La evaluacion de donde se obtienen los valores.
     * @param lstOtEjes
     *            Los ejes existentes en la prueba.
     * @param lstOtHabs
     *            Las habilidades exitentes en la prueba.
     * @return Mapa de alumno con todos los puntos por eje y habilidad.
     */
    private List<OTAlumnoResumen> obtenerPuntos() {
        List<OTAlumnoResumen> respuesta = new ArrayList<>();
        for (R_PruebaRendida pr : pRendidas) {
            String resps = pr.getRespuestas();
            Optional<R_Alumno> op = alumnos.stream().filter(a -> a.getId().equals(pr.getAlumno_id())).findFirst();
            R_Alumno alumno = op.isPresent() ? op.get() : null;
            if (tipoAlumno.getId() != Constants.PIE_ALL && tipoAlumno.getId() != alumno.getTipoalumno_id()) {
                // En este caso, no se considera este alumno para el
                // cálculo.
                continue;
            }
            OTAlumnoResumen ot = new OTAlumnoResumen(alumno);
            for (R_Ejetematico eje : lstEjes) {
                Pair<Integer, Integer> pair = obtenerBuenasTotales(resps, eje);
                Integer buenas = pair.getFirst();
                Integer cantidad = pair.getSecond();
                float porcentaje = buenas.floatValue() / cantidad.floatValue() * 100f;
                ot.getPorcentajes().add(porcentaje);
            }
            for (R_Habilidad hab : lstHabs) {
                Pair<Integer, Integer> pair = obtenerBuenasTotales(resps, hab);
                Integer buenas = pair.getFirst();
                Integer cantidad = pair.getSecond();
                float porcentaje = buenas.floatValue() / cantidad.floatValue() * 100f;
                ot.getPorcentajes().add(porcentaje);
            }
            respuesta.add(ot);
        }
        return respuesta;
    }
    /**
     * Este metodo evalua la cantidad de buenas de un String de respuesta contrastado contra las respuestas esperadas.
     * 
     * @param respuestas
     *            Las respuestas del alumno.
     * @param ahb
     *            La R_Habilidad en base al que se realiza el calculo.
     * @return Par <Preguntas buenas, Total de Preguntas> del eje.
     */
    private Pair<Integer, Integer> obtenerBuenasTotales(String respuestas, R_Habilidad hab) {
        int nroBuenas = 0;
        int nroPreguntas = 0;
        for (int n = 0; n < respEsperadas.size(); n++) {
            R_RespuestasEsperadasPrueba resp = respEsperadas.get(n);
            if (resp.getAnulada()) {
                continue;
            }
            if (resp.getHabilidad_id().equals(hab.getId())) {
                if (respuestas.length() > n) {
                    String sResp = respuestas.substring(n, n + 1);
                    if ("+".equals(sResp) || resp.getRespuesta().equalsIgnoreCase(sResp)) {
                        nroBuenas++;
                    }
                }
                nroPreguntas++;
            }
        }
        return new Pair<Integer, Integer>(nroBuenas, nroPreguntas);
    }
    /**
     * Este metodo evalua la cantidad de buenas de un String de respuesta contrastado contra las respuestas eperadas.
     * 
     * @param respuestas
     *            Las respuestas del alumno.
     * @param eje
     *            El Eje tematico en base al que se realiza el calculo.
     * @return Par <Preguntas buenas, Total de Preguntas> del eje.
     */
    private Pair<Integer, Integer> obtenerBuenasTotales(String respuestas, R_Ejetematico eje) {
        int nroBuenas = 0;
        int nroPreguntas = 0;
        for (int n = 0; n < respEsperadas.size(); n++) {
            R_RespuestasEsperadasPrueba resp = respEsperadas.get(n);
            if (resp.getAnulada()) {
                continue;
            }
            if (resp.getEjetematico_id().equals(eje.getId())) {
                if (respuestas.length() > n) {
                    String sResp = respuestas.substring(n, n + 1);
                    if ("+".equals(sResp) || resp.getRespuesta().equalsIgnoreCase(sResp)) {
                        nroBuenas++;
                    }
                }
                nroPreguntas++;
            }
        }
        return new Pair<Integer, Integer>(nroBuenas, nroPreguntas);
    }
    /**
     * Este metodo coloca las columnas a las dos tablas de la HMI. Coloca los cursos que estan asociados al colegio,
     * independiente que tenga o no evaluaciones.
     * 
     * @param pCursoList
     */
    @SuppressWarnings("unchecked")
    private void construirColumnas() {
        int index = 0;
        tblAlumnos.getColumns().add(getFixedColumns("RUT", index++, 100));
        tblAlumnos.getColumns().add(getFixedColumns("PATERNO", index++, 100));
        tblAlumnos.getColumns().add(getFixedColumns("MATERNO", index++, 100));
        tblAlumnos.getColumns().add(getFixedColumns("NOMBRE", index++, 150));
        for (R_Ejetematico eje : lstEjes) {
            tblAlumnos.getColumns().add(getPercentColumns(eje.getName(), index++));
        }
        for (R_Habilidad hab : lstHabs) {
            tblAlumnos.getColumns().add(getPercentColumns(hab.getName(), index++));
        }
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private TableColumn getFixedColumns(String name, final int index, int width) {
        TableColumn tc = new TableColumn(name);
        tc.setSortable(false);
        tc.setStyle("-fx-alignment: CENTER-LEFT;");
        tc.prefWidthProperty().set(width);
        tc.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().get(index).toString());
            }
        });
        return tc;
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private TableColumn getPercentColumns(String name, final int index) {
        int idx = index - FIXED_COLUMNS + 1;
        TableColumn tc = new TableColumn("(" + idx + ") " + name);
        makeHeaderWrappable(tc);
        tc.setSortable(false);
        tc.setStyle("-fx-alignment: CENTER;-fx-font-size: 8pt;-fx-text-alignment: center;");
        tc.prefWidthProperty().set(70f);
        tc.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().get(index).toString());
            }
        });
        return tc;
    }
    @SuppressWarnings("rawtypes")
    private void makeHeaderWrappable(TableColumn col) {
        Label label = new Label(col.getText());
        label.setStyle("-fx-padding: 8px;-fx-font-size: 7pt;");
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        StackPane stack = new StackPane();
        stack.getChildren().add(label);
        stack.prefWidthProperty().bind(col.widthProperty().subtract(5));
        label.prefWidthProperty().bind(stack.prefWidthProperty());
        col.setGraphic(stack);
    }
    @SuppressWarnings("rawtypes")
    public TableView getTblAlumnos() {
        return tblAlumnos;
    }
    @SuppressWarnings("rawtypes")
    public void setTblAlumnos(TableView tblAlumnos) {
        this.tblAlumnos = tblAlumnos;
    }
    /**
     * Obtiene todos las habilidades de una prueba y la cantidad de preguntas de cada una.
     * 
     * @param respEsperadas
     *            Lista de preguntas esperadas de una prueba.
     * @return Lista con las habilidades y la cantidad de preguntas de cada habilidad en la prueba.
     */
    private List<R_Habilidad> getHabilidades() {
        List<R_Habilidad> lstResult = new ArrayList<>();
        List<Long> listId = new ArrayList<>();
        for (R_RespuestasEsperadasPrueba r : respEsperadas) {
            if (!listId.contains(r.getHabilidad_id())) {
                listId.add(r.getHabilidad_id());
            }
        }
        Long[] ids = listId.toArray(new Long[listId.size()]);
        lstResult = PersistenceServiceFactory.getPersistenceService().findByAllIdSynchro(R_Habilidad.class, ids);
        listId = null;
        ids = null;
        return lstResult;
    }
    /**
     * Obtiene todos los ejes temáticos de una prueba.
     * 
     * @param respEsperadas
     *            Lista de preguntas esperadas de una prueba.
     * @return Lista con los ejes temáticos en la prueba.
     */
    private List<R_Ejetematico> getEjesTematicos() {
        List<R_Ejetematico> lstResult = new ArrayList<>();
        List<Long> listId = new ArrayList<>();
        for (R_RespuestasEsperadasPrueba r : respEsperadas) {
            if (!listId.contains(r.getHabilidad_id())) {
                listId.add(r.getEjetematico_id());
            }
        }
        Long[] ids = listId.toArray(new Long[listId.size()]);
        lstResult = PersistenceServiceFactory.getPersistenceService().findByAllIdSynchro(R_Ejetematico.class, ids);
        listId = null;
        ids = null;
        return lstResult;
    }
}
