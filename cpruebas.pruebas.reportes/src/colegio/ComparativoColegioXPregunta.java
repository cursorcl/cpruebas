package colegio;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import cl.eos.imp.view.AFormView;
import cl.eos.imp.view.ProgressForm;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.restful.tables.R_Alumno;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.MapBuilder;
import colegio.util.OTColegioCurso;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.util.Callback;

/**
 * Obtiene reporte que presenta las notas de cada alumno
 * 
 * @author curso
 *
 */
public class ComparativoColegioXPregunta extends AFormView {
  Logger log = Logger.getLogger(ComparativoColegioEjeEvaluacionView.class.getName());
  private static final Font FONT_TITLE = new Font("Arial", 9);

  @FXML
  private BorderPane pnlContainer;

  @FXML
  private ComboBox<OTColegioCurso> cmbCurso;

  @FXML
  private Button btnProcesar;

  @FXML
  private TableView tblReporte;

  @FXML
  private MenuItem mnuExportActual;

  @FXML
  private MenuItem mnuExportAll;

  private List<OTColegioCurso> lstData;

  private R_Prueba prueba;

  private List<R_EvaluacionPrueba> lstEvaluaciones;

  private List<R_PruebaRendida> lstPRendidas;

  private List<R_RespuestasEsperadasPrueba> lstREsperadas;

  @FXML
  public void initialize() {
    cmbCurso.setOnAction((event) -> {
      btnProcesar.setDisable(cmbCurso.getValue() == null);
    });
    btnProcesar.setOnAction((event) -> {
      OTColegioCurso ot = cmbCurso.getValue();

      R_EvaluacionPrueba evaluacion = lstEvaluaciones.stream()
          .filter(e -> e.getCurso_id().equals(ot.getCurso().getId())
              && e.getColegio_id().equals(ot.getColegio().getId()) && e.getPrueba_id().equals(prueba.getId()))
          .findFirst().orElse(null);

      if (evaluacion == null)
        return;
      Map<String, Object> params =
          MapBuilder.<String, Object>unordered().put("evaluacionprueba_id", evaluacion.getId()).build();
      controller.findByParam(R_PruebaRendida.class, params, this);
    });

    mnuExportActual.setOnAction((event) -> {
      tblReporte.setId(cmbCurso.getValue().toString());
      ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblReporte);
    });

    mnuExportAll.setOnAction((event) -> {
      processAllCourses();
    });

  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public void onDataArrived(List<Object> list) {
    if (list == null || list.isEmpty()) {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("No hay registros.");
      alert.setHeaderText(this.getName());
      alert.setContentText("No se ha encontrado registros para la consulta.");
      alert.showAndWait();
      return;
    }
    final Object entity = list.get(0);
    if (entity instanceof R_RespuestasEsperadasPrueba) {

      lstREsperadas = list.stream().map(t -> (R_RespuestasEsperadasPrueba) t).collect(Collectors.toList());
      tblReporte.getColumns().clear();
      TableColumn col = new TableColumn();
      col.setText("Paterno");
      col.getStyleClass().add("double-line");
      col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
        public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
          return new SimpleStringProperty(param.getValue().get(0).toString());
        }
      });

      tblReporte.getColumns().add(col);

      col = new TableColumn<>();
      col.setText("Materno");
      col.getStyleClass().add("double-line");

      tblReporte.getColumns().add(col);
      col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
        public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
          return new SimpleStringProperty(param.getValue().get(1).toString());
        }
      });

      col = new TableColumn<>();
      col.setText("Nombre");
      col.getStyleClass().add("double-line");
      col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
        public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
          return new SimpleStringProperty(param.getValue().get(2).toString());
        }
      });

      tblReporte.getColumns().add(col);

      int idx = 3;
      lstREsperadas.sort(new Comparator<R_RespuestasEsperadasPrueba>() {
        @Override
        public int compare(R_RespuestasEsperadasPrueba o1, R_RespuestasEsperadasPrueba o2) {
          return o1.getNumero().compareTo(o2.getNumero());
        }
      });
      for (R_RespuestasEsperadasPrueba resp : lstREsperadas) {
        final int index = idx++;
        col = new TableColumn<>();
        col.setPrefWidth(25);
        col.setText(resp.getName() + " " + resp.getRespuesta());
        col.getStyleClass().add("double-line");
        col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
          public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
            return new SimpleStringProperty(param.getValue().get(index).toString());
          }
        });
        tblReporte.getColumns().add(col);
      }
    } else if (entity instanceof R_EvaluacionPrueba) {
      lstEvaluaciones = list.stream().map(item -> (R_EvaluacionPrueba) item).collect(Collectors.toList());
      List<Long> lstIdColegios =
          lstEvaluaciones.stream().map(e -> e.getColegio_id()).distinct().collect(Collectors.toList());
      List<R_Colegio> lstColegios =
          controller.findByAllIdSynchro(R_Colegio.class, lstIdColegios.toArray(new Long[lstIdColegios.size()]));

      List<Long> lstIdCursos =
          lstEvaluaciones.stream().map(e -> e.getCurso_id()).distinct().collect(Collectors.toList());
      List<R_Curso> lstCursos =
          controller.findByAllIdSynchro(R_Curso.class, lstIdCursos.toArray(new Long[lstIdCursos.size()]));

      List<OTColegioCurso> lstCmbValues = new ArrayList<>();
      for (R_EvaluacionPrueba eval : lstEvaluaciones) {
        R_Colegio colegio =
            lstColegios.stream().filter(c -> c.getId().equals(eval.getColegio_id())).findFirst().orElse(null);
        R_Curso curso = lstCursos.stream().filter(c -> c.getId().equals(eval.getCurso_id())).findFirst().orElse(null);

        lstCmbValues.add(new OTColegioCurso(colegio, curso));
      }


      cmbCurso.setItems(FXCollections.observableArrayList(lstCmbValues));
    } else if (entity instanceof R_PruebaRendida) {
      lstPRendidas = list.stream().map(item -> (R_PruebaRendida) item).collect(Collectors.toList());
      makeReport();
    }

  }

  @Override
  public void onFound(IEntity entity) {

  }

  /**
   * Se procesa cada registro para presetarlo en las tablas.
   */
  @SuppressWarnings("unchecked")
  private void makeReport() {
    ObservableList<ObservableList<String>> contenido = FXCollections.observableArrayList();
    ObservableList<String> row = null;
    List<Long> lIds = lstPRendidas.stream().map(pr -> pr.getAlumno_id()).collect(Collectors.toList());
    Long[] ids = lIds.toArray(new Long[lIds.size()]);
    List<R_Alumno> lstAlumnos = controller.findByAllIdSynchro(R_Alumno.class, ids);

    for (R_PruebaRendida pr : lstPRendidas) {
      row = FXCollections.observableArrayList();
      R_Alumno alumno = lstAlumnos.stream().filter(a -> a.getId().equals(pr.getAlumno_id())).findFirst().orElse(null);
      if (alumno == null)
        continue;

      row.add(alumno.getPaterno());
      row.add(alumno.getMaterno());
      row.add(alumno.getName());
      String[] respuesta = pr.getRespuestas().split("");
      int n = 0;
      lstREsperadas.sort(new Comparator<R_RespuestasEsperadasPrueba>() {
        @Override
        public int compare(R_RespuestasEsperadasPrueba o1, R_RespuestasEsperadasPrueba o2) {
          return o1.getNumero().compareTo(o2.getNumero());
        }
      });
      for (R_RespuestasEsperadasPrueba resp : lstREsperadas) {
        String value = evaluateResp(resp, respuesta[n++]);
        row.add(value);
      }
      contenido.add(row);
    }
    tblReporte.setItems(contenido);
  }

  public List<OTColegioCurso> getCmbData() {
    return lstData;
  }

  public void setCmbData(List<OTColegioCurso> lstData) {
    this.lstData = lstData;
    cmbCurso.getItems().clear();
    cmbCurso.setItems(FXCollections.observableArrayList(lstData));
  }

  public R_Prueba getPrueba() {
    return prueba;
  }

  public void setPrueba(R_Prueba prueba) {
    this.prueba = prueba;
  }

  /**
   * Realiza la evaluación de la respuesta del alumno. Contrasta el valor dado por el alumno con el
   * valor que se espera para la preguna.
   * 
   * @param resp Respuesta esperada, valor correcto definido por el profesor.
   * @param respAlumno Respuesta dada por el alumno.
   * @return String indicando el resultado de la evaluación.
   */
  private String evaluateResp(R_RespuestasEsperadasPrueba resp, String respAlumno) {
    String returValue = "";
    if (resp.getAnulada()) {
      returValue = "*";
    } else if (respAlumno.equalsIgnoreCase("O")) {
      returValue = "O";
    } else if (respAlumno.equalsIgnoreCase(resp.getRespuesta())) {
      returValue = "+";
    } else {
      returValue = "-";
    }

    return returValue;
  }

  static int hoja = 1;

  /**
   * Genera el archivo Excel con todos los cursos que hayan rendido la prueba.
   * 
   * Genera una hoja por cada curso, la cual contiene la lista de alumnos y un indicador si la
   * respuesta del alumno fué buena o mala.
   */
  @SuppressWarnings("rawtypes")
  private void processAllCourses() {

    ProgressForm pForm = new ProgressForm();
    pForm.title("Procesando Cursos");
    pForm.message("Esto tomará algunos segundos.");



    hoja = 1;
    Task<Void> task = new Task<Void>() {

      @Override
      protected Void call() throws Exception {
        final Workbook wbook = new HSSFWorkbook();
        final int total = lstEvaluaciones.size();
        lstEvaluaciones.forEach(evaluacion -> {
          
          log.info("Procesando evaluación:" + evaluacion.getName());
          updateMessage(evaluacion.getName());
          final int idx = lstEvaluaciones.indexOf(evaluacion);
          updateProgress(idx, total);
          
          
          Map<String, Object> params =
              MapBuilder.<String, Object>unordered().put("evaluacionprueba_id", evaluacion.getId()).build();

          R_Colegio colegio = controller.findSynchroById(R_Colegio.class, evaluacion.getColegio_id());
          R_Curso curso = controller.findSynchroById(R_Curso.class, evaluacion.getCurso_id());
          // Creación ed la hoja
          Sheet sheet = wbook.createSheet("H-" + hoja++);

          // El título de la hoja
          final Row header = sheet.createRow(0);
          header.setHeightInPoints(sheet.getDefaultRowHeightInPoints());
          Cell cellHeader = header.createCell(0);
          cellHeader.setCellValue(colegio + "-" + curso);
          ExcelSheetWriterObj.applySheetTitleStyle(cellHeader);
          cellHeader.getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
          header.setHeightInPoints(40);

          // El título, cada columna, de la hoja
          final CellStyle style = wbook.createCellStyle();
          style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
          style.setFillPattern(CellStyle.SOLID_FOREGROUND);
          final Row filaCabecera = sheet.createRow(1);
          for (int indice = 0; indice < tblReporte.getColumns().size(); indice++) {
            final Cell cell = filaCabecera.createCell(indice);
            TableColumn col = (TableColumn) tblReporte.getColumns().get(indice);
            cell.setCellValue(col.getText());
            cell.setCellStyle(style);
          }

          // Se colocan los valores a la tabla
          List<R_PruebaRendida> _lstPRendidas = controller.findByParamsSynchro(R_PruebaRendida.class, params);
          if (!(_lstPRendidas == null || _lstPRendidas.isEmpty())) {

            List<Long> lIds = _lstPRendidas.stream().map(pr -> pr.getAlumno_id()).collect(Collectors.toList());
            Long[] ids = lIds.toArray(new Long[lIds.size()]);
            List<R_Alumno> lstAlumnos = controller.findByAllIdSynchro(R_Alumno.class, ids);


            int row = 2;
            for (R_PruebaRendida pr : _lstPRendidas) {

              R_Alumno alumno =
                  lstAlumnos.stream().filter(a -> a.getId().equals(pr.getAlumno_id())).findFirst().orElse(null);

              final Row fila = sheet.createRow(row++);

              Cell cell = fila.createCell(0);
              cell.setCellValue(alumno.getPaterno());

              cell = fila.createCell(1);
              cell.setCellValue(alumno.getMaterno());

              cell = fila.createCell(2);
              cell.setCellValue(alumno.getName());

              String[] resps = pr.getRespuestas().split("");

              for (int n = 0; n < resps.length; n++) {
                String value = evaluateResp(lstREsperadas.get(n), resps[n]);
                cell = fila.createCell(n + 3);
                cell.setCellValue(value);
                cell.getCellStyle().setAlignment(CellStyle.ALIGN_CENTER);
              }
            }

          }
        });
        ExcelSheetWriterObj.crearDocExcel(wbook);
        return null;
      }
    };
    task.setOnSucceeded((event) -> {
      pForm.getDialogStage().hide();
    });
    task.setOnFailed((event) -> {
      log.severe("Se ha producido el siguiente error:" + event.getEventType().toString());
      pForm.getDialogStage().hide();
    });
    pForm.showWorkerProgress(task);
    Executors.newSingleThreadExecutor().execute(task);
  }

}
