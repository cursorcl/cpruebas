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

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTPreguntasEjes;
import cl.eos.ot.OTPreguntasHabilidad;
import cl.eos.ot.OTRangoEvaluacion;
import cl.eos.ot.OTReporte;
import cl.eos.ot.OTResultado;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.NivelEvaluacion;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RangoEvaluacion;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.persistence.util.Comparadores;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.Utils;

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

  private Map<EjeTematico, Float> mapaEjesCurso = new HashMap<EjeTematico, Float>();
  private Map<EjeTematico, Float> mapaPorEjes = new HashMap<EjeTematico, Float>();
  private Map<EjeTematico, OTPreguntasEjes> mapaEjesAlumno =
      new HashMap<EjeTematico, OTPreguntasEjes>();

  private Map<Habilidad, Float> mapaHabilidadesCurso = new HashMap<Habilidad, Float>();
  Map<Habilidad, OTPreguntasHabilidad> mapaHabilidadAlumno =
      new HashMap<Habilidad, OTPreguntasHabilidad>();
  private Map<RangoEvaluacion, OTRangoEvaluacion> mRango;
  private Map<RangoEvaluacion, List<Map<EjeTematico, OTReporte>>> mapReporte =
      new HashMap<RangoEvaluacion, List<Map<EjeTematico, OTReporte>>>();

  private boolean llegaOnFound = false;
  private EvaluacionPrueba evaluacionPrueba;
  private ArrayList<EjeTematico> titulosColumnas = new ArrayList<>();
  private List<RangoEvaluacion> listaRangos;

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
    colRangoCantidad.setCellValueFactory(new PropertyValueFactory<OTRangoEvaluacion, Integer>(
        "cantidad"));
    colRangolLogro
        .setCellValueFactory(new PropertyValueFactory<OTRangoEvaluacion, Float>("logrado"));
  }

  @Override
  public void onFound(IEntity entity) {
    if (entity instanceof EvaluacionPrueba) {
      evaluacionPrueba = (EvaluacionPrueba) entity;
      listaRangos = evaluacionPrueba.getPrueba().getNivelEvaluacion().getRangos();
      mRango = new HashMap<RangoEvaluacion, OTRangoEvaluacion>();
      for (RangoEvaluacion rango : listaRangos) {
        OTRangoEvaluacion otRango = new OTRangoEvaluacion();
        otRango.setRango(rango);
        mRango.put(rango, otRango);
      }
      llegaOnFound = true;
    }
    if (llegaOnFound) {
      llegaOnFound = false;
      llenarDatosTabla();
    }
  }

  private void llenarDatosTabla() {
    tblReportePME.getColumns().clear();
    mapaEjesCurso.clear();
    mapaPorEjes.clear();
    mapaHabilidadesCurso.clear();
    mapaHabilidadAlumno.clear();
    mapReporte.clear();
    mapaEjesAlumno.clear();

    lblTitulo.setText(evaluacionPrueba.getName());
    List<PruebaRendida> pruebasRendidas = evaluacionPrueba.getPruebasRendidas();

    int totalAlumnos = evaluacionPrueba.getPruebasRendidas().size();
    float totalPreguntas = evaluacionPrueba.getPrueba().getNroPreguntas();

    Prueba prueba = evaluacionPrueba.getPrueba();
    NivelEvaluacion nivelEvaluacion = prueba.getNivelEvaluacion();
    List<RespuestasEsperadasPrueba> respuestasEsperadas = prueba.getRespuestas();

    float sumaNotas = 0;
    float sumaLogro = 0;
    for (PruebaRendida pRendida : pruebasRendidas) {

      sumaNotas = sumaNotas + pRendida.getNota();

      float logro = (pRendida.getBuenas() / totalPreguntas) * 100f;
      sumaLogro = sumaLogro + logro;

      RangoEvaluacion rango = nivelEvaluacion.getRango(logro);
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

  private void generaContenidosHabilidades(PruebaRendida pruebaRendida,
      List<RespuestasEsperadasPrueba> respuestasEsperadas) {
    
    int len = pruebaRendida.getEvaluacionPrueba().getPrueba().getNroPreguntas();
    String respuesta = pruebaRendida.getRespuestas().toUpperCase();
    char[] cRespuesta = new char[len];
    Arrays.fill(cRespuesta, 'O');

    char[] cR = respuesta.toUpperCase().toCharArray();
    for (int n = 0; n < cR.length; n++) {
      cRespuesta[n] = cR[n];
    }
    cR = null;
    
    for (RespuestasEsperadasPrueba respuestasEsperadasPrueba : respuestasEsperadas) {

      Habilidad habilidad = respuestasEsperadasPrueba.getHabilidad();

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

  private void generaContenidosEjes(PruebaRendida pruebaRendida,
      List<RespuestasEsperadasPrueba> respuestasEsperadas, NivelEvaluacion nivelEvaluacion) {
    int len = pruebaRendida.getEvaluacionPrueba().getPrueba().getNroPreguntas();
    String respuesta = pruebaRendida.getRespuestas().toUpperCase();
    char[] cRespuesta = new char[len];
    Arrays.fill(cRespuesta, 'O');

    char[] cR = respuesta.toUpperCase().toCharArray();
    for (int n = 0; n < cR.length; n++) {
      cRespuesta[n] = cR[n];
    }
    cR = null;
    for (RespuestasEsperadasPrueba respuestasEsperadasPrueba : respuestasEsperadas) {
      EjeTematico ejeTematico = respuestasEsperadasPrueba.getEjeTematico();

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

  private void generaContenidosReportes(NivelEvaluacion nivelEvaluacion) {
    // genera datos de reportes

    for (Entry<EjeTematico, Float> otEje : mapaPorEjes.entrySet()) {
      EjeTematico eje = otEje.getKey();
      float promedioLogro = otEje.getValue();
      RangoEvaluacion rango = nivelEvaluacion.getRango(promedioLogro);

      if (mapReporte.containsKey(rango)) {
        List<Map<EjeTematico, OTReporte>> lstMapEje = mapReporte.get(rango);
        boolean existe = false;
        for (Map<EjeTematico, OTReporte> map : lstMapEje) {
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

          Map<EjeTematico, OTReporte> nuevo = new HashMap<EjeTematico, OTReporte>();
          nuevo.put(eje, otReporte);
          lstMapEje.add(nuevo);
          mapReporte.put(rango, lstMapEje);
        }

      } else {
        OTReporte reporte = new OTReporte();
        reporte.setRango(rango);
        reporte.setTotal(1);
        reporte.setEje(eje);
        Map<EjeTematico, OTReporte> nuevo = new HashMap<EjeTematico, OTReporte>();
        nuevo.put(eje, reporte);

        List<Map<EjeTematico, OTReporte>> listaMapas =
            new LinkedList<Map<EjeTematico, OTReporte>>();
        listaMapas.add(nuevo);
        mapReporte.put(rango, listaMapas);
      }

    }
  }

  private void creacionColumnasEjesTematicos() {

    TableColumn columna0 = new TableColumn("");
    columna0
        .setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
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

    int indice = 1;
    for (EjeTematico titulo : titulosColumnas) {
      // Columnas
      final int col = indice;
      TableColumn columna = new TableColumn(titulo.getName());
      columna
          .setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
              return new SimpleStringProperty(param.getValue().get(col).toString());
            }
          });
      columna.setPrefWidth(100);
      tblReportePME.getColumns().add(columna);
      indice++;
    }
  }

  private void generarDespliegueContenidosRangos() {
    List<OTRangoEvaluacion> lista = new ArrayList<OTRangoEvaluacion>(mRango.values());
    Collections.sort(lista, Comparadores.comparaRangoEvaluacion());

    ObservableList<OTRangoEvaluacion> rangos = FXCollections.observableArrayList();
    for (OTRangoEvaluacion lEntity : lista) {

      rangos.add((OTRangoEvaluacion) lEntity);
    }

    tblRangos.setItems(rangos);
  }

  private void generarDespliegueContenidoEjesTematicos(int totalAlumnos) {

    ObservableList<OTResultado> mapa = FXCollections.observableArrayList();
    for (Entry<EjeTematico, OTPreguntasEjes> lEntity : mapaEjesAlumno.entrySet()) {
      OTResultado resultado = new OTResultado();
      resultado.setNombre(lEntity.getKey().getName());
      float valor = (float) lEntity.getValue().getBuenas() / (float) lEntity.getValue().getTotal();
      resultado.setLogrado(valor * 100);
      mapa.add(resultado);
    }
    tblEjesTematicos.setItems(mapa);

  }

  private void generarDespliegueContenidoHabilidades(int totalAlumnos) {

    ObservableList<OTResultado> mapa = FXCollections.observableArrayList();
    for (Entry<Habilidad, OTPreguntasHabilidad> lEntity : mapaHabilidadAlumno.entrySet()) {
      OTResultado resultado = new OTResultado();
      resultado.setNombre(lEntity.getKey().getName());
      float valor = (float) lEntity.getValue().getBuenas() / (float) lEntity.getValue().getTotal();
      resultado.setLogrado(valor * 100);
      mapa.add(resultado);
    }
    tblHabilidades.setItems(mapa);
  }

  private void generarDespliegueContenidoReporte(int totalAlumnos) {
    ObservableList<ObservableList> registroseReporte = FXCollections.observableArrayList();

    ObservableList<String> row = null;
    for (RangoEvaluacion rango : listaRangos) {
      List<Map<EjeTematico, OTReporte>> lstEjeTematico = mapReporte.get(rango);

      row = FXCollections.observableArrayList();
      row.add(rango.getName());

      if (lstEjeTematico == null) {
        for (int i = 0; i < mapaEjesCurso.size(); i++) {
          row.add(String.valueOf(0));
        }
      } else {
        for (EjeTematico ejeTematico : titulosColumnas) {
          OTReporte valor = null;
          for (Map<EjeTematico, OTReporte> map : lstEjeTematico) {
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

  private void generarDespliegueContenidosGenerales(int totalAlumnos,
      NivelEvaluacion nivelEvaluacion, float sumaNotas, float sumaLogro) {
    txtEvaluados.setText(String.valueOf(totalAlumnos));

    float promedio = sumaNotas / totalAlumnos;
    txtPromedio.setText(formatter.format(promedio));

    int puntaje = Utils.getPuntaje(promedio);
    txtPuntaje.setText(String.valueOf(puntaje));

    float promedioLogro = (sumaLogro / totalAlumnos);

    txtLogro.setText(formatter.format(promedioLogro));

    RangoEvaluacion rango = nivelEvaluacion.getRango(promedioLogro);
    txtNivel.setText(rango.getAbreviacion());
  }

  @Override
  public void handle(ActionEvent event) {
    Object source = event.getSource();
    if (source == mnuExportarEjesTematicos || source == mnuExportarHabilidades
        || source == mnuExportarRangos || source == mnuExportarReporte) {

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
    }
  }
}
