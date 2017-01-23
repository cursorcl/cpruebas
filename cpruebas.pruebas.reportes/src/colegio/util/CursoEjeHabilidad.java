package colegio.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cl.eos.common.Constants;
import cl.eos.provider.persistence.PersistenceServiceFactory;
import cl.eos.restful.tables.R_Alumno;
import cl.eos.restful.tables.R_Ejetematico;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_Habilidad;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.util.MapBuilder;
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

@SuppressWarnings("rawtypes")
public class CursoEjeHabilidad {

  private final int FIXED_COLUMNS = 4;

  private TableView tblAlumnos;

  private R_EvaluacionPrueba evaluacionPrueba;
  private List<R_Ejetematico> lstEjes;
  private List<R_Habilidad> lstHabs;
  private List<R_RespuestasEsperadasPrueba> respEsperadas;
  private R_TipoAlumno tipoAlumno;


  public CursoEjeHabilidad(R_EvaluacionPrueba evaluacionPrueba, R_TipoAlumno tipoAlumno,
      List<R_RespuestasEsperadasPrueba> respEsperadas) {
    super();
    this.tipoAlumno = tipoAlumno;
    tblAlumnos = new TableView();
    tblAlumnos.setId(evaluacionPrueba.getName() + evaluacionPrueba.getId());
    this.respEsperadas = respEsperadas;
    setEvaluacionPrueba(evaluacionPrueba);
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
   * @param evaluacionPrueba La evaluacion de donde se obtienen los valores.
   * @param lstOtEjes Los ejes existentes en la prueba.
   * @param lstOtHabs Las habilidades exitentes en la prueba.
   * @return Mapa de alumno con todos los puntos por eje y habilidad.
   */
  private List<OTAlumnoResumen> obtenerPuntos() {

    List<OTAlumnoResumen> respuesta = new ArrayList<>();

    Map<String, Object> params = MapBuilder.<String, Object>unordered()
        .put("evaluacionprueba_id", evaluacionPrueba.getId()).build();
    List<R_PruebaRendida> pRendidas = PersistenceServiceFactory.getPersistenceService()
        .findByParamsSynchor(R_PruebaRendida.class, params);
    for (R_PruebaRendida pr : pRendidas) {
      String resps = pr.getRespuestas();
      R_Alumno alumno = PersistenceServiceFactory.getPersistenceService()
          .findByIdSynchro(R_Alumno.class, pr.getAlumno_id());

      if (tipoAlumno.getId() != Constants.PIE_ALL
          && tipoAlumno.getId() != alumno.getTipoalumno_id()) {
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
   * Este metodo evalua la cantidad de buenas de un String de respuesta contrastado contra las
   * respuestas esperadas.
   * 
   * @param respuestas Las respuestas del alumno.
   * @param ahb La R_Habilidad en base al que se realiza el calculo.
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
   * Este metodo evalua la cantidad de buenas de un String de respuesta contrastado contra las
   * respuestas eperadas.
   * 
   * @param respuestas Las respuestas del alumno.
   * @param eje El Eje tematico en base al que se realiza el calculo.
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
   * Este metodo coloca las columnas a las dos tablas de la HMI. Coloca los cursos que estan
   * asociados al colegio, independiente que tenga o no evaluaciones.
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

  @SuppressWarnings({"unchecked"})
  private TableColumn getFixedColumns(String name, final int index, int width) {
    TableColumn tc = new TableColumn(name);
    tc.setSortable(false);
    tc.setStyle("-fx-alignment: CENTER-LEFT;");
    tc.prefWidthProperty().set(width);
    tc.setCellValueFactory(
        new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
          public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
            return new SimpleStringProperty(param.getValue().get(index).toString());
          }
        });
    return tc;
  }

  @SuppressWarnings({"unchecked"})
  private TableColumn getPercentColumns(String name, final int index) {
    int idx = index - FIXED_COLUMNS + 1;
    TableColumn tc = new TableColumn("(" + idx + ") " + name);
    makeHeaderWrappable(tc);
    tc.setSortable(false);
    tc.setStyle("-fx-alignment: CENTER;-fx-font-size: 8pt;-fx-text-alignment: center;");
    tc.prefWidthProperty().set(70f);
    tc.setCellValueFactory(
        new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
          public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
            return new SimpleStringProperty(param.getValue().get(index).toString());
          }
        });
    return tc;
  }

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

  public TableView getTblAlumnos() {
    return tblAlumnos;
  }

  public void setTblAlumnos(TableView tblAlumnos) {
    this.tblAlumnos = tblAlumnos;
  }

  public R_EvaluacionPrueba getEvaluacionPrueba() {
    return evaluacionPrueba;
  }

  public final void setEvaluacionPrueba(R_EvaluacionPrueba evaluacionPrueba) {
    this.evaluacionPrueba = evaluacionPrueba;
    lstEjes = getEjesTematicos();
    lstHabs = getHabilidades();
    generate();
  }

  /**
   * Obtiene todos las habilidades de una prueba y la cantidad de preguntas de cada una.
   * 
   * @param respEsperadas Lista de preguntas esperadas de una prueba.
   * @return Lista con las habilidades y la cantidad de preguntas de cada habilidad en la prueba.
   */
  private List<R_Habilidad> getHabilidades() {
    List<Long> lstIdHabs = new ArrayList<>();
    for (R_RespuestasEsperadasPrueba r : respEsperadas) {
      if (!lstIdHabs.contains(r.getHabilidad_id())) {
        lstIdHabs.add(r.getHabilidad_id());
      }
    }
    Long[] arrIds = lstIdHabs.toArray(new Long[lstIdHabs.size()]);
    List<R_Habilidad> lstOtHabs = PersistenceServiceFactory.getPersistenceService()
        .findByAllIdSynchro(R_Habilidad.class, arrIds);
    return lstOtHabs;
  }

  /**
   * Obtiene todos los ejes temáticos de una prueba.
   * 
   * @param respEsperadas Lista de preguntas esperadas de una prueba.
   * @return Lista con los ejes temáticos en la prueba.
   */
  private List<R_Ejetematico> getEjesTematicos() {
    List<Long> lstIdEjes = new ArrayList<>();
    for (R_RespuestasEsperadasPrueba r : respEsperadas) {
      if (!lstIdEjes.contains(r.getEjetematico_id())) {
        lstIdEjes.add(r.getEjetematico_id());
      }
    }

    Long[] arrIds = lstIdEjes.toArray(new Long[lstIdEjes.size()]);
    List<R_Ejetematico> lstOtEjes = PersistenceServiceFactory.getPersistenceService()
        .findByAllIdSynchro(R_Ejetematico.class, arrIds);
    return lstOtEjes;

  }
}
