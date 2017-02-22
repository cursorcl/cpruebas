package comunal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.EEvaluados;
import cl.eos.ot.OTPreguntasEvaluacion;
import cl.eos.persistence.util.Comparadores;
import cl.eos.restful.tables.R_Alumno;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_EvaluacionEjetematico;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.restful.tables.R_TipoColegio;
import cl.eos.restful.tables.R_TipoCurso;
import cl.eos.util.MapBuilder;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class ResumenComunalView extends AFormView implements EventHandler<ActionEvent> {

  @FXML
  private Label lblTitulo;
  @FXML
  private TableView<ObservableList<String>> tblEvaluaciones;
  @FXML
  private TableView<ObservableList<String>> tblTotales;

  private HashMap<Long, R_EvaluacionEjetematico> mEvaluaciones;

  private ArrayList<String> titulosColumnas;
  @FXML
  private ComboBox<R_TipoAlumno> cmbTipoAlumno;
  @FXML
  private Button btnGenerar;
  @FXML
  private ComboBox<R_TipoColegio> cmbTipoColegio;


  long tipoAlumno = Constants.PIE_ALL;
  long tipoColegio = Constants.TIPO_COLEGIO_ALL;

  private boolean llegaEvaluacionEjeTematico;
  private boolean llegaTipoAlumno = false;
  private List<R_Prueba> listaPruebas;

  public ResumenComunalView() {
    setTitle("Resumen comunal");
  }

  @FXML
  public void initialize() {
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

  private void procesaDatosReporte() {
    if (llegaEvaluacionEjeTematico  && llegaTipoAlumno && llegaTipoColegio) {
      inicializarComponentes();
      llenarDatosTabla();
      creacionColumnasEvaluaciones(listaEvaluacionesTitulos);
      creacionColumnasTotalesEvaluaciones(listaEvaluacionesTitulos);
      desplegarDatosEvaluaciones();
      desplegarDatosTotales();
    }
  }

  private void desplegarDatosEvaluaciones() {
    ObservableList<ObservableList<String>> registros = FXCollections.observableArrayList();

    List<R_EvaluacionEjetematico> ejes = new ArrayList<>(mapEvaAlumnos.keySet());
    Collections.sort(ejes, Comparadores.comparaEvaluacionEjeTematico());
    for (R_EvaluacionEjetematico eje : ejes) {
      HashMap<String, OTPreguntasEvaluacion> resultados = mapEvaAlumnos.get(eje);

      ObservableList<String> row = FXCollections.observableArrayList();
      row.add(eje.getName());
      for (String string : titulosColumnas) {
        OTPreguntasEvaluacion otPregunta = resultados.get(string);
        row.add(String.valueOf(otPregunta.getAlumnos()));
      }

      registros.add(row);
    }
    tblEvaluaciones.setItems(registros);
  }

  private void desplegarDatosTotales() {

    Map<String, Float> totalEvaluados = new HashMap<String, Float>();
    Map<String, Float> totalInformados = new HashMap<String, Float>();

    ObservableList<ObservableList<String>> registroseEva = FXCollections.observableArrayList();
    ObservableList<String> row = FXCollections.observableArrayList();

    // Total evaluados
    float total = 0;
    for (HashMap<String, OTPreguntasEvaluacion> resultados : mapEvaAlumnos.values()) {

      for (String string : titulosColumnas) {
        OTPreguntasEvaluacion otPregunta = resultados.get(string);

        if (totalEvaluados.containsKey(string)) {
          total = otPregunta.getAlumnos() + totalEvaluados.get(string);
          totalEvaluados.replace(string, total);
        } else {
          totalEvaluados.put(string, (float) otPregunta.getAlumnos());
        }
      }
    }

    // Total informados
    for (R_Prueba prueba : listaPruebas) {
      R_TipoCurso lTipoCurso = lstTipoCurso.stream().filter(t -> t.getId().equals(prueba.getCurso_id())).findFirst().orElse(null);
      String tipoCurso = lTipoCurso.getName();
      
      List<R_EvaluacionPrueba> listaEvaluaciones =listaEvaluacionesTitulos.stream().filter(e -> e.getPrueba_id().equals(prueba.getId())).collect(Collectors.toList());

      for (R_EvaluacionPrueba evaluacionPrueba : listaEvaluaciones) {
        
        
        Map<String, Object> params = MapBuilder.<String, Object>unordered().put("curso_id", evaluacionPrueba.getCurso_id()).build();
        Collection<R_Alumno> alumnos = controller.findByParamsSynchro(R_Alumno.class, params);
        int totalAlumnos = 0;
        for (R_Alumno alumno : alumnos) {
          if (tipoAlumno != Constants.PIE_ALL && tipoAlumno != alumno.getTipoalumno_id())
            continue;
          if (tipoColegio != Constants.TIPO_COLEGIO_ALL && tipoColegio != alumno.getTipoalumno_id())
            continue;

          totalAlumnos++;
        }
        if (totalInformados.containsKey(tipoCurso)) {
          total = totalInformados.get(tipoCurso) + totalAlumnos;
          totalInformados.replace(tipoCurso, total);
        } else {
          totalInformados.put(tipoCurso, (float) totalAlumnos);
        }
      }
    }

    row.add(EEvaluados.TOTAL_EVA.getName());
    for (String tipoCurso : titulosColumnas) {
      row.add(String.valueOf(totalEvaluados.get(tipoCurso) == null ? 0 : totalEvaluados.get(tipoCurso)));
    }
    registroseEva.add(row);

    row = FXCollections.observableArrayList();
    row.add(EEvaluados.TOTAL_INF.getName());
    for (String tipoCurso : titulosColumnas) {
      row.add(String.valueOf(totalInformados.get(tipoCurso)));
    }
    registroseEva.add(row);

    row = FXCollections.observableArrayList();
    row.add(EEvaluados.VALIDACION.getName());
    for (String tipoCurso : titulosColumnas) {
      float nroEvaluados = 0f;
      if (totalEvaluados.get(tipoCurso) != null)
        nroEvaluados = totalEvaluados.get(tipoCurso);
      total = (nroEvaluados / totalInformados.get(tipoCurso)) * 100;
      row.add(String.valueOf(Math.round(total)));
    }
    registroseEva.add(row);

    tblTotales.setItems(registroseEva);
  }

  private List<R_EvaluacionPrueba> listaEvaluacionesTitulos = new LinkedList<R_EvaluacionPrueba>();
  private Map<R_EvaluacionEjetematico, HashMap<String, OTPreguntasEvaluacion>> mapEvaAlumnos;
  private boolean llegaTipoColegio;
  private R_Asignatura asignatura;
  List<R_Curso> lstCursos = new ArrayList<>();
  List<R_TipoCurso> lstTipoCurso = new ArrayList<>();

  private void llenarDatosTabla() {

    mapEvaAlumnos = new HashMap<R_EvaluacionEjetematico, HashMap<String, OTPreguntasEvaluacion>>();

    for (R_Prueba prueba : listaPruebas) {
      R_TipoCurso tipoCurso = lstTipoCurso.stream().filter(p -> prueba.getCurso_id().equals(p.getId())).findFirst().orElse(null);
      if (tipoCurso == null) {
        tipoCurso = controller.findByIdSynchro(R_TipoCurso.class, prueba.getCurso_id());
        lstTipoCurso.add(tipoCurso);
      }

      StringBuffer buffer = new StringBuffer();
      buffer.append(asignatura.getName());
      buffer.append(" ");
      buffer.append(tipoCurso.getName());
      lblTitulo.setText(buffer.toString());

      Map<String, Object> params = MapBuilder.<String, Object>unordered().put("prueba_id", prueba.getId())
          .put("asignatura_id", asignatura.getId()).build();
      List<R_EvaluacionPrueba> listaEvaluaciones = controller.findByParamsSynchro(R_EvaluacionPrueba.class, params);

      listaEvaluacionesTitulos.addAll(listaEvaluaciones);

      // ********** generar datos evaluaciones y totales.

      for (R_EvaluacionPrueba evaluacionPrueba : listaEvaluaciones) {

        R_Colegio colegio = controller.findByIdSynchro(R_Colegio.class, evaluacionPrueba.getColegio_id());
        if (tipoColegio != Constants.TIPO_COLEGIO_ALL && tipoColegio != colegio.getTipocolegio_id())
          continue;

        String nameTpCurso = tipoCurso.getName();

        HashMap<String, OTPreguntasEvaluacion> mapaOT = new HashMap<String, OTPreguntasEvaluacion>();

        params = MapBuilder.<String, Object>unordered().put("evaluacionprueba_id", evaluacionPrueba.getId()).build();
        List<R_PruebaRendida> lstPruebasRendidas = controller.findByParamsSynchro(R_PruebaRendida.class, params);

        for (R_PruebaRendida pruebaRendida : lstPruebasRendidas) {
          if (tipoAlumno != Constants.PIE_ALL && tipoAlumno != pruebaRendida.getTipoalumno_id())
            continue;


          Float pBuenas = pruebaRendida.getBuenas().floatValue();
          for (Entry<Long, R_EvaluacionEjetematico> mEvaluacion : mEvaluaciones.entrySet()) {

            R_EvaluacionEjetematico evaluacionAl = mEvaluacion.getValue();
            if (mapEvaAlumnos.containsKey(evaluacionAl)) {
              HashMap<String, OTPreguntasEvaluacion> evaluacion = mapEvaAlumnos.get(evaluacionAl);
              if (evaluacion.containsKey(nameTpCurso)) {
                OTPreguntasEvaluacion otPreguntas = evaluacion.get(nameTpCurso);

                if (pBuenas >= evaluacionAl.getNrorangomin() && pBuenas <= evaluacionAl.getNrorangomax()) {
                  otPreguntas.setAlumnos(otPreguntas.getAlumnos() + 1);
                }
              } else {

                OTPreguntasEvaluacion pregunta = new OTPreguntasEvaluacion();
                if (pBuenas >= evaluacionAl.getNrorangomin() && pBuenas <= evaluacionAl.getNrorangomax()) {
                  pregunta.setAlumnos(1);
                } else {
                  pregunta.setAlumnos(0);
                }
                pregunta.setEvaluacion(evaluacionAl);
                evaluacion.put(nameTpCurso, pregunta);
              }
            } else {
              OTPreguntasEvaluacion pregunta = new OTPreguntasEvaluacion();
              if (pBuenas >= evaluacionAl.getNrorangomin() && pBuenas <= evaluacionAl.getNrorangomax()) {
                pregunta.setAlumnos(1);
              } else {
                pregunta.setAlumnos(0);
              }
              pregunta.setEvaluacion(evaluacionAl);

              mapaOT = new HashMap<String, OTPreguntasEvaluacion>();
              mapaOT.put(nameTpCurso, pregunta);
              mapEvaAlumnos.put(evaluacionAl, mapaOT);
            }
          }
        }
      }
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private void creacionColumnasEvaluaciones(List<R_EvaluacionPrueba> pListaEvaluaciones) {
    TableColumn columna0 = new TableColumn("Niveles de logros");
    columna0.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
      public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {

        return new SimpleStringProperty(param.getValue().get(0).toString());
      }
    });
    columna0.setPrefWidth(100);
    // columna0.setStyle("-fx-alignment: CENTER;");
    tblEvaluaciones.getColumns().add(columna0);

    titulosColumnas = new ArrayList<>();
    int indice = 1;
    List<R_EvaluacionPrueba> listaEvaluaciones = pListaEvaluaciones;
    for (R_EvaluacionPrueba evaluacion : listaEvaluaciones) {

      // Columnas
      final int col = indice;
      
      R_Curso lCurso = lstCursos.stream().filter(c -> c.getId().equals(evaluacion.getCurso_id())).findFirst().orElse(null);
      if(lCurso == null)
      {
        lCurso = controller.findByIdSynchro(R_Curso.class, evaluacion.getCurso_id());
      }
      final R_Curso curso =  lCurso;
      R_TipoCurso lTipoCurso = lstTipoCurso.stream().filter(t -> t.getId().equals(curso.getTipocurso_id())).findFirst().orElse(null);
      String tipoCurso = lTipoCurso.getName();
      
      if (!titulosColumnas.contains(tipoCurso)) {
        titulosColumnas.add(tipoCurso);
        TableColumn columna = new TableColumn(tipoCurso);
        columna.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
          public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
            return new SimpleStringProperty(param.getValue().get(col).toString());
          }
        });
        columna.setPrefWidth(100);
        tblEvaluaciones.getColumns().add(columna);
        indice++;
      }
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private void creacionColumnasTotalesEvaluaciones(List<R_EvaluacionPrueba> pListaEvaluaciones) {
    TableColumn columna0 = new TableColumn("");
    columna0.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
      public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
        return new SimpleStringProperty(param.getValue().get(0).toString());
      }
    });
    columna0.setPrefWidth(100);
    tblTotales.getColumns().add(columna0);

    int indice = 1;
    for (String tpoCurso : titulosColumnas) {

      // Columnas
      final int col = indice;
      final String tipoCurso = tpoCurso;
      TableColumn columna = new TableColumn(tipoCurso);
      columna.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
        public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
          return new SimpleStringProperty(param.getValue().get(col).toString());
        }
      });
      columna.setPrefWidth(100);
      tblTotales.getColumns().add(columna);
      indice++;
    }
  }

  @Override
  public void onDataArrived(List<Object> list) {
    if (list != null && !list.isEmpty()) {
      Object entity = list.get(0);
      if (entity instanceof R_EvaluacionEjetematico) {
        llegaEvaluacionEjeTematico = true;
        mEvaluaciones = new HashMap<Long, R_EvaluacionEjetematico>();
        for (Object object : list) {
          R_EvaluacionEjetematico eje = (R_EvaluacionEjetematico) object;
          mEvaluaciones.put(eje.getId(), eje);
        }
      }
      if (entity instanceof R_TipoAlumno) {
        ObservableList<R_TipoAlumno> tAlumnoList = FXCollections.observableArrayList();
        llegaTipoAlumno = true;
        for (Object iEntity : list) {
          tAlumnoList.add((R_TipoAlumno) iEntity);
        }
        cmbTipoAlumno.setItems(tAlumnoList);
        cmbTipoAlumno.getSelectionModel().select((int) Constants.PIE_ALL);
      }
      if (entity instanceof R_TipoColegio) {
        ObservableList<R_TipoColegio> tColegioList = FXCollections.observableArrayList();
        llegaTipoColegio = true;
        for (Object iEntity : list) {
          tColegioList.add((R_TipoColegio) iEntity);
        }
        cmbTipoColegio.setItems(tColegioList);
        R_TipoColegio tColegio = new R_TipoColegio.Builder().id(Constants.TIPO_COLEGIO_ALL).build();
        cmbTipoColegio.getSelectionModel().select(tColegio);
      }
    }
  }



  @Override
  public void onFound(IEntity entity) {
    if (entity instanceof R_Asignatura) {
      asignatura = (R_Asignatura) entity;
    }
  }

  private void inicializarComponentes() {
    tblEvaluaciones.getColumns().clear();
    tblTotales.getColumns().clear();
    listaEvaluacionesTitulos.clear();
  }

  @Override
  public void handle(ActionEvent event) {
    if (event.getSource() == btnGenerar) {
      procesaDatosReporte();
    }

  }

  /**
   * @return the listaPruebas
   */
  public final List<R_Prueba> getListaPruebas() {
    return listaPruebas;
  }

  /**
   * @param listaPruebas the listaPruebas to set
   */
  public final void setListaPruebas(List<R_Prueba> listaPruebas) {
    this.listaPruebas = listaPruebas;
  }

}
