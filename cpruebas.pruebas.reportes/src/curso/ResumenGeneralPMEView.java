package curso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import cl.eos.ot.OTPreguntasEjes;
import cl.eos.ot.OTPreguntasHabilidad;
import cl.eos.ot.OTRangoEvaluacion;
import cl.eos.ot.OTReporte;
import cl.eos.ot.OTResultado;
import cl.eos.persistence.util.Comparadores;
import cl.eos.restful.EntityUtils;
import cl.eos.restful.tables.R_Ejetematico;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_Habilidad;
import cl.eos.restful.tables.R_NivelEvaluacion;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_RangoEvaluacion;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.MapBuilder;
import cl.eos.util.Utils;
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
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class ResumenGeneralPMEView extends AFormView implements EventHandler<ActionEvent> {

  private NumberFormat formatter = new DecimalFormat("#0.00");
  @FXML
  private Label lblTitulo;
  @FXML
  private MenuItem mnuExportarEjesTematicos;
  @FXML
  private MenuItem mnuExportarHabilidades;
  @FXML
  private MenuItem mnuExportarRangos;
  @FXML
  private MenuItem mnuExportarReporte;
  @FXML
  private TableView<OTResultado> tblEjesTematicos;
  @FXML
  private TableColumn<OTResultado, String> colEje;
  @FXML
  private TableColumn<OTResultado, String> colEjeLogro;

  @FXML
  private TableView<OTResultado> tblHabilidades;
  @FXML
  private TableColumn<OTResultado, String> colHabilidad;
  @FXML
  private TableColumn<OTResultado, String> colHabilidadLogro;

  @FXML
  private TableView<OTRangoEvaluacion> tblRangos;
  @FXML
  private TableColumn<OTRangoEvaluacion, String> colRango;
  @FXML
  private TableColumn<OTRangoEvaluacion, Integer> colRangoCantidad;
  @FXML
  private TableColumn<OTRangoEvaluacion, Float> colRangolLogro;
  @SuppressWarnings("rawtypes")
  @FXML
  private TableView tblReportePME;
  @FXML
  private TextField txtPromedio;
  @FXML
  private TextField txtPuntaje;
  @FXML
  private TextField txtNivel;
  @FXML
  private TextField txtLogro;
  @FXML
  private TextField txtEvaluados;

  @FXML
  private ComboBox<R_TipoAlumno> cmbTipoAlumno;

  long tipoAlumno = Constants.PIE_ALL;

  @FXML
  private Button btnGenerar;

  private Map<R_Ejetematico, Float> mapaEjesCurso = new HashMap<R_Ejetematico, Float>();
  private Map<R_Ejetematico, Float> mapaPorEjes = new HashMap<R_Ejetematico, Float>();
  private Map<R_Ejetematico, OTPreguntasEjes> mapaEjesAlumno = new HashMap<R_Ejetematico, OTPreguntasEjes>();

  private Map<R_Habilidad, Float> mapaHabilidadesCurso = new HashMap<R_Habilidad, Float>();
  Map<R_Habilidad, OTPreguntasHabilidad> mapaHabilidadAlumno = new HashMap<R_Habilidad, OTPreguntasHabilidad>();
  private Map<R_RangoEvaluacion, OTRangoEvaluacion> mRango;
  private Map<R_RangoEvaluacion, List<Map<R_Ejetematico, OTReporte>>> mapReporte =
      new HashMap<R_RangoEvaluacion, List<Map<R_Ejetematico, OTReporte>>>();

  private boolean llegaEvaluacion = false;
  private R_EvaluacionPrueba evaluacionPrueba;
  private List<R_Ejetematico> titulosColumnas = new ArrayList<>();
  private List<R_RangoEvaluacion> listaRangos;
  private boolean llegaTipoAlumno;
  private boolean llegaRangos;
  private List<R_PruebaRendida> pruebasRendidas;
  private R_Prueba prueba;
  private List<R_RespuestasEsperadasPrueba> respuestasEsperadas;
  private R_NivelEvaluacion nivelEvaluacion;
  private boolean llegaNivelEvaluacion;
  private List<R_Habilidad> habilidades;
  private List<R_Ejetematico> ejes;

  public ResumenGeneralPMEView() {
    setTitle("Resumen general P.M.E.");
  }

  @FXML
  public void initialize() {
    inicializarTablaEjes();
    inicializarTablaHabilidades();
    inicializarTablaEvaluacines();
    mnuExportarEjesTematicos.setOnAction(this);
    mnuExportarHabilidades.setOnAction(this);
    mnuExportarRangos.setOnAction(this);
    mnuExportarReporte.setOnAction(this);
    btnGenerar.setOnAction(this);
    cmbTipoAlumno.setOnAction(event -> {
      tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedIndex();
    });

  }

  private void inicializarTablaEjes() {
    tblEjesTematicos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    colEje.setCellValueFactory(new PropertyValueFactory<OTResultado, String>("nombre"));
    colEjeLogro.setCellValueFactory(new PropertyValueFactory<OTResultado, String>("logrado"));
  }

  private void inicializarTablaHabilidades() {
    tblHabilidades.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    colHabilidad.setCellValueFactory(new PropertyValueFactory<OTResultado, String>("nombre"));
    colHabilidadLogro.setCellValueFactory(new PropertyValueFactory<OTResultado, String>("logrado"));
  }

  private void inicializarTablaEvaluacines() {
    tblRangos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    colRango.setCellValueFactory(new PropertyValueFactory<OTRangoEvaluacion, String>("name"));
    colRangoCantidad.setCellValueFactory(new PropertyValueFactory<OTRangoEvaluacion, Integer>("cantidad"));
    colRangolLogro.setCellValueFactory(new PropertyValueFactory<OTRangoEvaluacion, Float>("logrado"));
    colRangolLogro.setCellFactory(tc -> new TableCell<OTRangoEvaluacion, Float>() {
      @Override
      protected void updateItem(Float value, boolean empty) {
          super.updateItem(value, empty) ;
          if (empty) {
              setText(null);
          } else {
              setText(String.format("%3.2f", value));
          }
      }
  });
  }

  @Override
  public void onFound(IEntity entity) {
    if (entity instanceof R_EvaluacionPrueba) {
      evaluacionPrueba = (R_EvaluacionPrueba) entity;
      llegaEvaluacion = true;
    } else if (entity instanceof R_NivelEvaluacion) {
      nivelEvaluacion = (R_NivelEvaluacion) entity;
      llegaNivelEvaluacion = true;
    }
    llenarDatosTabla();

  }

  @Override
  public void onDataArrived(List<Object> list) {
    if (list != null && !list.isEmpty()) {
      Object entity = list.get(0);
      if (entity instanceof R_TipoAlumno) {
        ObservableList<R_TipoAlumno> tAlumnoList = FXCollections.observableArrayList();
        for (Object iEntity : list) {
          tAlumnoList.add((R_TipoAlumno) iEntity);
        }
        cmbTipoAlumno.setItems(tAlumnoList);
        cmbTipoAlumno.getSelectionModel().select(0);
        llegaTipoAlumno = true;
        tipoAlumno = Constants.PIE_ALL;
        llenarDatosTabla();
      }
      if (entity instanceof R_RangoEvaluacion) {
        listaRangos = list.stream().map(e -> (R_RangoEvaluacion) e).collect(Collectors.toList());
        mRango = new HashMap<R_RangoEvaluacion, OTRangoEvaluacion>();
        for (R_RangoEvaluacion rango : listaRangos) {
          OTRangoEvaluacion otRango = new OTRangoEvaluacion();
          otRango.setRango(rango);
          mRango.put(rango, otRango);
        }
        llegaRangos = true;
        llenarDatosTabla();
      }
    }

  }

  private void llenarDatosTabla() {

    if (!llegaEvaluacion || !llegaTipoAlumno || !llegaRangos || !llegaNivelEvaluacion)
      return;

    tblReportePME.getColumns().clear();
    mapaEjesCurso.clear();
    mapaPorEjes.clear();
    mapaHabilidadesCurso.clear();
    mapaHabilidadAlumno.clear();
    mapReporte.clear();
    mapaEjesAlumno.clear();
    titulosColumnas.clear();
    for (R_RangoEvaluacion rng : mRango.keySet()) {
      OTRangoEvaluacion rango = mRango.get(rng);
      rango.setCantidad(0);
      rango.setLogrado(0);
    }

    lblTitulo.setText(evaluacionPrueba.getName());

    Map<String, Object> params =
        MapBuilder.<String, Object>unordered().put("evaluacionprueba_id", evaluacionPrueba.getId()).build();
    pruebasRendidas = controller.findByParamsSynchro(R_PruebaRendida.class, params);
    params = MapBuilder.<String, Object>unordered().put("prueba_id", prueba.getId()).build();
    respuestasEsperadas = controller.findByParamsSynchro(R_RespuestasEsperadasPrueba.class, params);

    int totalAlumnos = 0;
    float totalPreguntas = prueba.getNropreguntas();

    Long[] ids = respuestasEsperadas.stream().map(r -> r.getHabilidad_id()).distinct().toArray(l -> new Long[l]);
    habilidades = controller.findByAllIdSynchro(R_Habilidad.class, ids);

    ids = respuestasEsperadas.stream().map(r -> r.getEjetematico_id()).distinct().toArray(l -> new Long[l]);
    ejes = controller.findByAllIdSynchro(R_Ejetematico.class, ids);
    
    float sumaNotas = 0;
    float sumaLogro = 0;
    for (R_PruebaRendida pRendida : pruebasRendidas) {
      if (tipoAlumno != Constants.PIE_ALL && !pRendida.getTipoalumno_id().equals(tipoAlumno))
        continue;

      totalAlumnos++;

      sumaNotas = sumaNotas + pRendida.getNota();

      float logro = (pRendida.getBuenas() / totalPreguntas) * 100f;
      sumaLogro = sumaLogro + logro;

      R_RangoEvaluacion rango = EntityUtils.getRango(logro, listaRangos);
      OTRangoEvaluacion otRango = mRango.get(rango);
      otRango.setCantidad(otRango.getCantidad() + 1);
      otRango.setLogrado(((float) otRango.getCantidad()) / totalAlumnos);

      generaContenidosEjes(pRendida, respuestasEsperadas, nivelEvaluacion);
      generaContenidosHabilidades(pRendida, respuestasEsperadas);
      generaContenidosReportes(nivelEvaluacion);
    }
    creacionColumnasEjesTematicos();
    generarDespliegueContenidosGenerales(totalAlumnos, nivelEvaluacion, sumaNotas, sumaLogro);
    generarDespliegueContenidosRangos();
    generarDespliegueContenidoEjesTematicos(totalAlumnos);
    generarDespliegueContenidoHabilidades(totalAlumnos);
    generarDespliegueContenidoReporte(totalAlumnos);
  }

  private void generaContenidosHabilidades(R_PruebaRendida pruebaRendida,
      List<R_RespuestasEsperadasPrueba> respuestasEsperadas) {

    int len = prueba.getNropreguntas();
    String respuesta = pruebaRendida.getRespuestas().toUpperCase();
    char[] cRespuesta = new char[len];
    Arrays.fill(cRespuesta, 'O');

    char[] cR = respuesta.toUpperCase().toCharArray();
    for (int n = 0; n < cR.length; n++) {
      cRespuesta[n] = cR[n];
    }
    cR = null;


    for (R_RespuestasEsperadasPrueba respuestasEsperadasPrueba : respuestasEsperadas) {

      R_Habilidad habilidad = habilidades.stream()
          .filter(h -> h.getId().equals(respuestasEsperadasPrueba.getHabilidad_id())).findFirst().orElse(null);

      if (habilidad == null)
        continue;

      Integer numeroPreg = respuestasEsperadasPrueba.getNumero();
      if (mapaHabilidadAlumno.containsKey(habilidad)) {
        OTPreguntasHabilidad otPregunta = mapaHabilidadAlumno.get(habilidad);

        if (cRespuesta[numeroPreg - 1] == respuestasEsperadasPrueba.getRespuesta().toCharArray()[0]) {
          otPregunta.setBuenas(otPregunta.getBuenas() + 1);
        }
        otPregunta.setTotal(otPregunta.getTotal() + 1);
      } else {
        OTPreguntasHabilidad otPreguntas = new OTPreguntasHabilidad();
        otPreguntas.setHabilidad(habilidad);
        if (cRespuesta[numeroPreg - 1] == respuestasEsperadasPrueba.getRespuesta().toCharArray()[0]) {
          otPreguntas.setBuenas(1);
        } else {
          otPreguntas.setBuenas(0);
        }
        otPreguntas.setTotal(1);
        mapaHabilidadAlumno.put(habilidad, otPreguntas);
      }
    }

    for (OTPreguntasHabilidad otAlumno : mapaHabilidadAlumno.values()) {

      float valor = otAlumno.getLogrado();
      if (mapaHabilidadesCurso.containsKey(otAlumno.getHabilidad())) {

        Float totalValor = mapaHabilidadesCurso.get(otAlumno.getHabilidad());
        totalValor = totalValor + valor;

      } else {
        mapaHabilidadesCurso.put(otAlumno.getHabilidad(), valor);
      }
    }
  }

  private void generaContenidosEjes(R_PruebaRendida pruebaRendida,
      List<R_RespuestasEsperadasPrueba> respuestasEsperadas, R_NivelEvaluacion nivelEvaluacion) {
    int len = prueba.getNropreguntas();
    String respuesta = pruebaRendida.getRespuestas().toUpperCase();
    char[] cRespuesta = new char[len];
    Arrays.fill(cRespuesta, 'O');

    char[] cR = respuesta.toUpperCase().toCharArray();
    for (int n = 0; n < cR.length; n++) {
      cRespuesta[n] = cR[n];
    }
    cR = null;
    for (R_RespuestasEsperadasPrueba respuestasEsperadasPrueba : respuestasEsperadas) {
      R_Ejetematico ejeTematico = ejes.stream()
          .filter(e -> e.getId().equals(respuestasEsperadasPrueba.getEjetematico_id())).findFirst().orElse(null);

      Integer numeroPreg = respuestasEsperadasPrueba.getNumero();
      if (mapaEjesAlumno.containsKey(ejeTematico)) {
        OTPreguntasEjes otPregunta = mapaEjesAlumno.get(ejeTematico);

        if (cRespuesta[numeroPreg - 1] == respuestasEsperadasPrueba.getRespuesta().toCharArray()[0]) {
          otPregunta.setBuenas(otPregunta.getBuenas() + 1);
        }
        otPregunta.setTotal(otPregunta.getTotal() + 1);
      } else {
        OTPreguntasEjes otPreguntas = new OTPreguntasEjes();
        otPreguntas.setEjeTematico(ejeTematico);
        if (cRespuesta[numeroPreg - 1] == respuestasEsperadasPrueba.getRespuesta().toCharArray()[0]) {
          otPreguntas.setBuenas(1);
        } else {
          otPreguntas.setBuenas(0);
        }
        otPreguntas.setTotal(1);
        mapaEjesAlumno.put(ejeTematico, otPreguntas);

        if (!titulosColumnas.contains(ejeTematico)) {
          titulosColumnas.add(ejeTematico);
        }
      }
    }

    for (OTPreguntasEjes otAlumno : mapaEjesAlumno.values()) {
      float valor = otAlumno.getLogrado();
      if (mapaEjesCurso.containsKey(otAlumno.getEjeTematico())) {
        Float totalValor = mapaEjesCurso.get(otAlumno.getEjeTematico());
        totalValor = totalValor + valor;
        mapaEjesCurso.replace(otAlumno.getEjeTematico(), totalValor);
      } else {
        mapaEjesCurso.put(otAlumno.getEjeTematico(), valor);
      }
      mapaPorEjes.put(otAlumno.getEjeTematico(), valor);
    }

  }

  private void generaContenidosReportes(R_NivelEvaluacion nivelEvaluacion) {
    // genera datos de reportes

    for (Entry<R_Ejetematico, Float> otEje : mapaPorEjes.entrySet()) {
      R_Ejetematico eje = otEje.getKey();
      float promedioLogro = otEje.getValue();
      R_RangoEvaluacion rango = EntityUtils.getRango(promedioLogro, listaRangos);

      if (mapReporte.containsKey(rango)) {
        List<Map<R_Ejetematico, OTReporte>> lstMapEje = mapReporte.get(rango);
        boolean existe = false;
        for (Map<R_Ejetematico, OTReporte> map : lstMapEje) {
          if (map.containsKey(eje)) {
            OTReporte otReporte = map.get(eje);
            otReporte.setTotal(otReporte.getTotal() + 1);
            existe = true;
          }
        }
        if (!existe) {
          OTReporte otReporte = new OTReporte();
          otReporte.setRango(rango);
          otReporte.setTotal(1);
          otReporte.setEje(eje);

          Map<R_Ejetematico, OTReporte> nuevo = new HashMap<R_Ejetematico, OTReporte>();
          nuevo.put(eje, otReporte);
          lstMapEje.add(nuevo);
          mapReporte.put(rango, lstMapEje);
        }

      } else {
        OTReporte reporte = new OTReporte();
        reporte.setRango(rango);
        reporte.setTotal(1);
        reporte.setEje(eje);
        Map<R_Ejetematico, OTReporte> nuevo = new HashMap<R_Ejetematico, OTReporte>();
        nuevo.put(eje, reporte);

        List<Map<R_Ejetematico, OTReporte>> listaMapas = new LinkedList<Map<R_Ejetematico, OTReporte>>();
        listaMapas.add(nuevo);
        mapReporte.put(rango, listaMapas);
      }

    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void creacionColumnasEjesTematicos() {

    TableColumn columna0 = new TableColumn("");
    columna0.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
      public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
        return new SimpleStringProperty(param.getValue().get(0).toString());
      }
    });
    columna0.setPrefWidth(100);
    columna0.setCellFactory(new Callback<TableColumn, TableCell>() {

      public TableCell call(TableColumn param) {
        TableCell cell = new TableCell() {
          @Override
          public void updateItem(Object item, boolean empty) {
            if (item != null) {
              setText(item.toString());
            }
          }
        };
        return cell;
      }
    });

    tblReportePME.getColumns().add(columna0);

    if (titulosColumnas == null || titulosColumnas.isEmpty())
      return;

    int indice = 1;
    for (R_Ejetematico titulo : titulosColumnas) {
      // Columnas
      final int col = indice;
      TableColumn columna = new TableColumn(titulo.getName());
      columna.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
        public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
          return new SimpleStringProperty(param.getValue().get(col).toString());
        }
      });
      columna.setPrefWidth(100);
      columna.setStyle("-fx-alignment: CENTER;");
      tblReportePME.getColumns().add(columna);
      indice++;
    }
  }

  private void generarDespliegueContenidosRangos() {
    tblRangos.getItems().clear();

    if (mRango == null || mRango.isEmpty())
      return;


    List<OTRangoEvaluacion> lista = new ArrayList<OTRangoEvaluacion>(mRango.values());
    Collections.sort(lista, Comparadores.comparaRangoEvaluacion());

    ObservableList<OTRangoEvaluacion> rangos = FXCollections.observableArrayList();
    for (OTRangoEvaluacion lEntity : lista) {

      rangos.add((OTRangoEvaluacion) lEntity);
    }

    tblRangos.setItems(rangos);
  }

  private void generarDespliegueContenidoEjesTematicos(int totalAlumnos) {
    tblEjesTematicos.getItems().clear();

    if (mapaEjesAlumno == null || mapaEjesAlumno.isEmpty())
      return;

    ObservableList<OTResultado> mapa = FXCollections.observableArrayList();
    for (Entry<R_Ejetematico, OTPreguntasEjes> lEntity : mapaEjesAlumno.entrySet()) {
      OTResultado resultado = new OTResultado();
      resultado.setNombre(lEntity.getKey().getName());
      float valor = (float) lEntity.getValue().getBuenas() / (float) lEntity.getValue().getTotal();
      resultado.setLogrado(valor * 100);
      mapa.add(resultado);
    }
    tblEjesTematicos.setItems(mapa);

  }

  private void generarDespliegueContenidoHabilidades(int totalAlumnos) {

    tblHabilidades.getItems().clear();
    if (mapaHabilidadAlumno == null || mapaHabilidadAlumno.isEmpty())
      return;
    ObservableList<OTResultado> mapa = FXCollections.observableArrayList();
    for (Entry<R_Habilidad, OTPreguntasHabilidad> lEntity : mapaHabilidadAlumno.entrySet()) {
      OTResultado resultado = new OTResultado();
      resultado.setNombre(lEntity.getKey().getName());
      float valor = (float) lEntity.getValue().getBuenas() / (float) lEntity.getValue().getTotal();
      resultado.setLogrado(valor * 100);
      mapa.add(resultado);
    }
    tblHabilidades.setItems(mapa);
  }

  @SuppressWarnings("unchecked")
  private void generarDespliegueContenidoReporte(int totalAlumnos) {
    tblReportePME.getItems().clear();
    if (mapReporte == null || mapReporte.isEmpty())
      return;

    @SuppressWarnings("rawtypes")
    ObservableList<ObservableList> registroseReporte = FXCollections.observableArrayList();

    ObservableList<String> row = null;
    for (R_RangoEvaluacion rango : listaRangos) {
      List<Map<R_Ejetematico, OTReporte>> lstEjeTematico = mapReporte.get(rango);

      row = FXCollections.observableArrayList();
      row.add(rango.getName());

      if (lstEjeTematico == null) {
        for (int i = 0; i < mapaEjesCurso.size(); i++) {
          row.add(String.valueOf(0));
        }
      } else {
        for (R_Ejetematico ejeTematico : titulosColumnas) {
          OTReporte valor = null;
          for (Map<R_Ejetematico, OTReporte> map : lstEjeTematico) {
            if (map.containsKey(ejeTematico)) {
              valor = map.get(ejeTematico);
              break;
            }
          }
          if (valor == null) {
            row.add(String.valueOf(0));
          } else {
            row.add(String.valueOf(valor.getTotal()));
          }
        }
      }
      registroseReporte.add(row);
    }
    tblReportePME.setItems(registroseReporte);
  }

  private void generarDespliegueContenidosGenerales(int totalAlumnos, R_NivelEvaluacion nivelEvaluacion,
      float sumaNotas, float sumaLogro) {
    txtEvaluados.setText(String.valueOf(totalAlumnos));

    float promedio = totalAlumnos == 0 ? 0 : sumaNotas / totalAlumnos;
    txtPromedio.setText(formatter.format(promedio));

    int puntaje = Utils.getPuntaje(promedio);
    txtPuntaje.setText(String.valueOf(puntaje));

    float promedioLogro = totalAlumnos == 0 ? 0 : (sumaLogro / totalAlumnos);

    txtLogro.setText(formatter.format(promedioLogro));

    R_RangoEvaluacion rango = EntityUtils.getRango(promedioLogro, listaRangos);
    txtNivel.setText(rango.getAbreviacion());
  }

  @SuppressWarnings("unchecked")
  @Override
  public void handle(ActionEvent event) {
    Object source = event.getSource();
    if (source == mnuExportarEjesTematicos || source == mnuExportarHabilidades || source == mnuExportarRangos
        || source == mnuExportarReporte) {

      tblEjesTematicos.setId("Eje Temático");
      tblHabilidades.setId("Habilidades");
      tblRangos.setId("Rango");
      tblReportePME.setId("Reporte PME");

      List<TableView<? extends Object>> listaTablas = new LinkedList<>();
      listaTablas.add((TableView<? extends Object>) tblEjesTematicos);
      listaTablas.add((TableView<? extends Object>) tblHabilidades);
      listaTablas.add((TableView<? extends Object>) tblRangos);
      listaTablas.add((TableView<? extends Object>) tblReportePME);
      ExcelSheetWriterObj.convertirDatosALibroDeExcel(listaTablas);
    } else if (source == btnGenerar) {
      llenarDatosTabla();
    }
  }

  /**
   * @return the prueba
   */
  public final R_Prueba getPrueba() {
    return prueba;
  }

  /**
   * @param prueba the prueba to set
   */
  public final void setPrueba(R_Prueba prueba) {
    this.prueba = prueba;
  }
}
