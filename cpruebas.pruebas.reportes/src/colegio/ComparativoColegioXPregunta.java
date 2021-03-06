package colegio;

import java.io.IOException;
import java.util.ArrayList;
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
import org.jfree.util.Log;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.imp.view.ProgressForm;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.persistence.models.TipoAlumno;
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
	private ComboBox<Colegio> cmbColegios;
	@FXML
	private ComboBox<Asignatura> cmbAsignatura;

	@FXML
	private ComboBox<TipoAlumno> cmbTipoAlumno;
	@FXML
	private Button btnProcesar;

	@FXML
	TabPane tabPane;

	@FXML
	private MenuItem mnuExportActual;

	@FXML
	private MenuItem mnuExportAll;

	private List<EvaluacionPrueba> lstEvaluaciones;

	private List<PruebaRendida> pruebasError;

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
		if (entity instanceof Asignatura) {
			cmbAsignatura.setItems(FXCollections
					.observableArrayList(list.stream().map(t -> (Asignatura) t).collect(Collectors.toList())));
		}
		if (entity instanceof Colegio) {
			cmbColegios.setItems(FXCollections
					.observableArrayList(list.stream().map(t -> (Colegio) t).collect(Collectors.toList())));
		}
		if (entity instanceof TipoAlumno) {
			cmbTipoAlumno.setItems(FXCollections
					.observableArrayList(list.stream().map(t -> (TipoAlumno) t).collect(Collectors.toList())));
		}
	}

	/**
	 * Se procesa cada registro para presetarlo en las tablas.
	 * 
	 * @param tipoAlumno
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void makeReport(List<PruebaRendida> lstPRendidas, TipoAlumno tipoAlumno) {
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
				for (PruebaRendida pr : lstPRendidas) {
					if (!tipoAlumno.getId().equals(Constants.PIE_ALL)
							&& !pr.getAlumno().getTipoAlumno().equals(tipoAlumno))
						continue;
					tab.setText(pr.getCurso());
					ObservableList<String> row = FXCollections.observableArrayList();
					row.add(pr.getAlumno().getPaterno());
					row.add(pr.getAlumno().getMaterno());
					row.add(pr.getAlumno().getName());
					String[] respuesta = pr.getRespuestas().split("");
					List<RespuestasEsperadasPrueba> respEsperadas = pr.getEvaluacionPrueba().getPrueba()
							.getRespuestas();
					if (!columnsCreated) {
						addColumns(tblReporte, respEsperadas);
						columnsCreated = true;
					}
					int n = 0;
					if (respEsperadas.size() > respuesta.length) {
						pruebasError.add(pr);
						Log.error("La catidad de respuestas esperadas es mayor que las respuesas!!!");
						for (RespuestasEsperadasPrueba resp : respEsperadas) {
							if (n < respuesta.length - 1) {
								String value = evaluateResp(resp, respuesta[n++]);
								row.add(value);
							}
						}
					} else {
						for (RespuestasEsperadasPrueba resp : respEsperadas) {
							String value = evaluateResp(resp, respuesta[n++]);
							row.add(value);
						}
					}
					contenido.add(row);
				}
				tblReporte.setItems(contenido);
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addColumns(TableView tblReporte, List<RespuestasEsperadasPrueba> lstREsperadas) {
		int idx = 3;
		for (RespuestasEsperadasPrueba resp : lstREsperadas) {
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
	 * Realiza la evaluación de la respuesta del alumno. Contrasta el valor dado
	 * por el alumno con el valor que se espera para la preguna.
	 * 
	 * @param resp
	 *            Respuesta esperada, valor correcto definido por el profesor.
	 * @param respAlumno
	 *            Respuesta dada por el alumno.
	 * @return String indicando el resultado de la evaluación.
	 */
	private String evaluateResp(RespuestasEsperadasPrueba resp, String respAlumno) {
		String returValue = "";
		if (resp.isAnulada()) {
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
	 * Genera una hoja por cada curso, la cual contiene la lista de alumnos y un
	 * indicador si la respuesta del alumno fué buena o mala.
	 */
	@SuppressWarnings("rawtypes")
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
			@SuppressWarnings("unchecked")
			@Override
			protected Boolean call() throws Exception {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						tabPane.getTabs().clear();
					}
				});

				pruebasError = new ArrayList<>();
				Colegio colegio = cmbColegios.getValue();
				Asignatura asignatura = cmbAsignatura.getValue();
				TipoAlumno tipoAlumno = cmbTipoAlumno.getValue();
				Map<String, Object> params = MapBuilder.<String, Object>unordered().put("idColegio", colegio.getId())
						.put("idAsignatura", asignatura.getId()).build();
				List<EvaluacionPrueba> lstEntities = (List<EvaluacionPrueba>) controller.findSynchro("EvaluacionPrueba.findEvaluacionByColegioAsig",
						params);
				if (lstEntities == null || lstEntities.isEmpty())
					return Boolean.FALSE;
				lstEvaluaciones = lstEntities.stream().map(e -> (EvaluacionPrueba) e).collect(Collectors.toList());

				final int max = lstEvaluaciones.size();
				int n = 1;

				for (EvaluacionPrueba evaluacion : lstEvaluaciones) {
					updateMessage("Procesado:" + evaluacion.toString());
					updateProgress(n++, max);

					params = MapBuilder.<String, Object>unordered().put("ideval", evaluacion.getId()).build();
					List<PruebaRendida> rendidas = (List<PruebaRendida>) controller.findSynchro("PruebaRendidaByEval.findAll", params);
					if (rendidas == null || rendidas.isEmpty())
						return Boolean.FALSE;
					makeReport(rendidas.stream().map(r -> (PruebaRendida) r).collect(Collectors.toList()), tipoAlumno);
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

			if (pruebasError != null && pruebasError.size() > 0) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Problemas con los siguientes alumnos.");
				alert.setHeaderText(
						"Alumnos tienen menos respuestas que las esperadas.\nReporte ha omitido dichos alumnos.");
				StringBuffer buff = new StringBuffer();
				for (PruebaRendida pr : pruebasError) {
					buff.append(String.format("%s %s Respuestas:%d/%d\n", pr.getAlumno(),
							pr.getAlumno().getCurso(), pr.getRespuestas().length(),
							pr.getEvaluacionPrueba().getPrueba().getResponses().length()));
				}
				alert.setContentText(buff.toString());
				alert.showAndWait();

			}
		});
		dlg.showWorkerProgress(task);
		Executors.newSingleThreadExecutor().execute(task);
	}
}
