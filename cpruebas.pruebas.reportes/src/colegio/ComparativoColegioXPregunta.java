package colegio;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.imp.view.ProgressForm;
import cl.eos.restful.tables.R_Alumno;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.MapBuilder;
import javafx.application.Platform;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

/**
 * Obtiene reporte que presenta las notas de cada alumno
 * 
 * @author curso
 *
 */
public class ComparativoColegioXPregunta extends AFormView {

  private static final Font FONT_TITLE = new Font("Arial", 9);

  @FXML
  private BorderPane pnlContainer;

  @FXML
  private ComboBox<R_Colegio> cmbColegios;
  @FXML
  private ComboBox<R_Asignatura> cmbAsignatura;

  @FXML
  private ComboBox<R_TipoAlumno> cmbTipoAlumno;
  @FXML
  private Button btnProcesar;

  @FXML
  TabPane tabPane;

  @FXML
  private MenuItem mnuExportActual;

  @FXML
  private MenuItem mnuExportAll;

  private List<R_EvaluacionPrueba> lstEvaluaciones;

  @FXML
  public void initialize() {
    setTitle("Respuestas x Alumno");
    cmbColegios.setOnAction((event) -> {
      btnProcesar.setDisable(isDisable());
    });
    cmbAsignatura.setOnAction((event) -> {
      btnProcesar.setDisable(isDisable());
    });

    cmbTipoAlumno.setOnAction((event) -> {
      btnProcesar.setDisable(isDisable());
    });

    btnProcesar.setOnAction((event) -> executeReport());

    mnuExportAll.setOnAction((event) -> {
      processAllCourses();
    });

  }

  private boolean isDisable() {
    return cmbColegios.getValue() == null || cmbAsignatura.getValue() == null || cmbTipoAlumno.getValue() == null;
  }

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
    if (entity instanceof R_Asignatura) {
      cmbAsignatura.setItems(
          FXCollections.observableArrayList(list.stream().map(t -> (R_Asignatura) t).collect(Collectors.toList())));
    }
    if (entity instanceof R_Colegio) {
      cmbColegios.setItems(
          FXCollections.observableArrayList(list.stream().map(t -> (R_Colegio) t).collect(Collectors.toList())));
    }
    if (entity instanceof R_TipoAlumno) {
      cmbTipoAlumno.setItems(
          FXCollections.observableArrayList(list.stream().map(t -> (R_TipoAlumno) t).collect(Collectors.toList())));
    }
  }

  /**
   * Se procesa cada registro para presetarlo en las tablas.
   * 
   * @param respEsperadas
   * @param curso
   * 
   * @param tipoAlumno
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  private void makeReport(List<R_PruebaRendida> lstPRendidas, List<R_RespuestasEsperadasPrueba> respEsperadas,
      R_Curso curso, Long tipoAlumno) {
    ObservableList<ObservableList<String>> contenido = FXCollections.observableArrayList();

    Platform.runLater(new Runnable() {

      @Override
      public void run() {
        TableView tblReporte = new TableView();
        makeColumns(tblReporte);
        Tab tab = new Tab();
        tab.setContent(tblReporte);
        tabPane.getTabs().add(tab);
        boolean columnsCreated = false;
        Long[] ids = lstPRendidas.stream().map(r -> r.getAlumno_id()).collect(Collectors.toList())
            .toArray(new Long[lstPRendidas.size()]);
        List<R_Alumno> alumnos = controller.findByAllIdSynchro(R_Alumno.class, ids);

        for (R_PruebaRendida pr : lstPRendidas) {
          if (!tipoAlumno.equals(Constants.PIE_ALL) && !pr.getTipoalumno_id().equals(tipoAlumno))
            continue;
          tab.setText(curso.getName());
          ObservableList<String> row = FXCollections.observableArrayList();
          R_Alumno alumno = alumnos.stream().filter(a -> a.getId().equals(pr.getAlumno_id())).findFirst().orElse(null);
          if (alumno == null)
            continue;
          row.add(alumno.getPaterno());
          row.add(alumno.getMaterno());
          row.add(alumno.getName());
          String[] respuesta = pr.getRespuestas().split("");

          if (!columnsCreated) {
            addColumns(tblReporte, respEsperadas);
            columnsCreated = true;
          }
          int n = 0;
          for (R_RespuestasEsperadasPrueba resp : respEsperadas) {
            String value = evaluateResp(resp, respuesta[n++]);
            row.add(value);
          }
          contenido.add(row);
        }
        tblReporte.setItems(contenido);
      }
    });
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void makeColumns(TableView tblReporte) {
    TableColumn col = new TableColumn();

    Label columnTitle = new Label("Paterno");
    col.setPrefWidth(120);
    columnTitle.setWrapText(true);
    columnTitle.setFont(FONT_TITLE);
    columnTitle.setTextAlignment(TextAlignment.CENTER);
    col.setGraphic(columnTitle);
    col.setText(columnTitle.getText());
    col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
      public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
        return new SimpleStringProperty(param.getValue().get(0).toString());
      }
    });

    tblReporte.getColumns().add(col);

    col = new TableColumn<>();
    columnTitle = new Label("Materno");
    col.setPrefWidth(120);
    columnTitle.setWrapText(true);
    columnTitle.setFont(FONT_TITLE);
    columnTitle.setTextAlignment(TextAlignment.CENTER);
    col.setGraphic(columnTitle);
    col.setText(columnTitle.getText());
    tblReporte.getColumns().add(col);
    col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
      public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
        return new SimpleStringProperty(param.getValue().get(1).toString());
      }
    });

    col = new TableColumn<>();
    columnTitle = new Label("Nombre");
    col.setPrefWidth(150);
    columnTitle.setWrapText(true);
    columnTitle.setFont(FONT_TITLE);
    columnTitle.setTextAlignment(TextAlignment.CENTER);
    col.setGraphic(columnTitle);
    col.setText(columnTitle.getText());
    col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
      public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
        return new SimpleStringProperty(param.getValue().get(2).toString());
      }
    });

    tblReporte.getColumns().add(col);

  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void addColumns(TableView tblReporte, List<R_RespuestasEsperadasPrueba> lstREsperadas) {
    int idx = 3;
    for (R_RespuestasEsperadasPrueba resp : lstREsperadas) {
      final int index = idx++;
      TableColumn col = new TableColumn<>();
      col.setPrefWidth(17);
      Label columnTitle = new Label(resp.getName() + " " + resp.getRespuesta());
      columnTitle.setPrefWidth(15);
      columnTitle.setPrefHeight(50);
      columnTitle.setWrapText(true);
      columnTitle.setFont(FONT_TITLE);
      columnTitle.setTextAlignment(TextAlignment.CENTER);
      col.setGraphic(columnTitle);
      col.setText(columnTitle.getText());
      col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
        public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
          return new SimpleStringProperty(param.getValue().get(index).toString());
        }
      });
      tblReporte.getColumns().add(col);
    }
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
      returValue = "C";
    } else {
      returValue = "I";
    }

    return returValue;
  }

  /**
   * Genera el archivo Excel con todos los cursos que hayan rendido la prueba.
   * 
   * Genera una hoja por cada curso, la cual contiene la lista de alumnos y un indicador si la
   * respuesta del alumno fué buena o mala.
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  private void processAllCourses() {

    final Workbook wbook = new HSSFWorkbook();
    for (Tab tab : tabPane.getTabs()) {

      TableView tblReporte = (TableView) tab.getContent();
      // Creación ed la hoja
      final Sheet sheet = wbook.createSheet(tab.getText());
      final Row header = sheet.createRow(0);
      header.setHeightInPoints(sheet.getDefaultRowHeightInPoints());
      // El título de la hoja
      Cell cellHeader = header.createCell(0);
      cellHeader.setCellValue(tab.getText());
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
      ObservableList<ObservableList<String>> items = tblReporte.getItems();
      int row = 2;
      for (ObservableList<String> valuesRow : items) {
        final Row fila = sheet.createRow(row++);
        int n = 0;
        for (String valueCol : valuesRow) {
          Cell cell = fila.createCell(n++);
          cell.setCellValue(valueCol);
          if (n > 2) {
            cell.getCellStyle().setAlignment(CellStyle.ALIGN_CENTER);
          }
        }
      }
    }
    ExcelSheetWriterObj.crearDocExcel(wbook);
  }

  private void executeReport() {
    final ProgressForm dlg = new ProgressForm();
    dlg.title("Procesando pruebas");
    dlg.message("Esto tomará algunos minutos.");

    final Task<Boolean> task = new Task<Boolean>() {
      @Override
      protected Boolean call() throws Exception {
        Platform.runLater(new Runnable() {

          @Override
          public void run() {
            tabPane.getTabs().clear();
          }
        });

        R_Colegio colegio = cmbColegios.getValue();
        R_Asignatura asignatura = cmbAsignatura.getValue();
        Long tipoAlumno = cmbTipoAlumno.getValue().getId();
        Map<String, Object> params = MapBuilder.<String, Object>unordered().put("colegio_id", colegio.getId())
            .put("asignatura_id", asignatura.getId()).build();

        lstEvaluaciones = controller.findByParamsSynchro(R_EvaluacionPrueba.class, params);
        if (lstEvaluaciones == null || lstEvaluaciones.isEmpty())
          return Boolean.FALSE;

        final int max = lstEvaluaciones.size();
        int n = 1;

        for (R_EvaluacionPrueba evaluacion : lstEvaluaciones) {
          updateMessage("Procesado:" + evaluacion.toString());
          updateProgress(n++, max);

          R_Curso curso = controller.findSynchroById(R_Curso.class, evaluacion.getCurso_id());

          params = MapBuilder.<String, Object>unordered().put("prueba_id", evaluacion.getPrueba_id()).build();
          List<R_RespuestasEsperadasPrueba> respEsperadas =
              controller.findByParamsSynchro(R_RespuestasEsperadasPrueba.class, params);

          params = MapBuilder.<String, Object>unordered().put("evaluacionprueba_id", evaluacion.getId()).build();
          List<R_PruebaRendida> rendidas = controller.findByParamsSynchro(R_PruebaRendida.class, params);
          if (rendidas == null || rendidas.isEmpty())
            return Boolean.FALSE;
          makeReport(rendidas, respEsperadas, curso, tipoAlumno);
        }
        return Boolean.TRUE;
      }
    };
    task.setOnFailed(event -> {
      dlg.getDialogStage().hide();
      final Runnable r = () -> {
        final Alert info = new Alert(AlertType.ERROR);
        info.setTitle("Proceso finalizado con error");
        info.setHeaderText("Se ha producido un error al procesar el reporte.");
        if (task.getException() instanceof IOException) {
          task.getException().printStackTrace();
          info.setContentText("No se ha podido procesar todos los reportes.");
        } else {
          task.getException().printStackTrace();
          info.setContentText("Error desconocido");
        }
        info.show();
      };
      Platform.runLater(r);
    });
    task.setOnSucceeded(event -> {
      dlg.getDialogStage().hide();
    });
    dlg.showWorkerProgress(task);
    Executors.newSingleThreadExecutor().execute(task);
  }
}
