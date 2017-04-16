package colegio;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.MapBuilder;
import colegio.util.OTColegioCurso;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

	private Prueba prueba;

	private List<EvaluacionPrueba> lstEvaluaciones;

	private List<PruebaRendida> lstPRendidas;

	private List<RespuestasEsperadasPrueba> lstREsperadas;

	@FXML
	public void initialize() {
		cmbCurso.setOnAction((event) -> {
			btnProcesar.setDisable(cmbCurso.getValue() == null);
		});
		btnProcesar.setOnAction((event) -> {
			OTColegioCurso ot = cmbCurso.getValue();

			EvaluacionPrueba evaluacion = lstEvaluaciones.stream().filter(e -> e.getCurso().equals(ot.getCurso())
					&& e.getColegio().equals(ot.getColegio()) && e.getPrueba().equals(prueba)).findFirst().orElse(null);

			if (evaluacion == null)
				return;
			Map<String, Object> params = MapBuilder.<String, Object>unordered().put("ideval", evaluacion.getId())
					.build();
			controller.find("PruebaRendidaByEval.findAll", params);
		});

		mnuExportActual.setOnAction((event) -> {
			tblReporte.setId(cmbCurso.getValue().toString());
			ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblReporte);
		});

		mnuExportAll.setOnAction((event) -> {
			processAllCourses();
		});

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
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
		if (entity instanceof RespuestasEsperadasPrueba) {

			lstREsperadas = list.stream().map(t -> (RespuestasEsperadasPrueba) t).collect(Collectors.toList());
			tblReporte.getColumns().clear();
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

			int idx = 3;
			for (RespuestasEsperadasPrueba resp : lstREsperadas) {
				final int index = idx++;
				col = new TableColumn<>();
				col.setPrefWidth(17);
				columnTitle = new Label(resp.getName() + " " + resp.getRespuesta());
				columnTitle.setPrefWidth(15);
				columnTitle.setPrefHeight(50);
				columnTitle.setWrapText(true);
				columnTitle.setFont(FONT_TITLE);
				columnTitle.setTextAlignment(TextAlignment.CENTER);
				col.setGraphic(columnTitle);
				col.setText(columnTitle.getText());
				col.setCellValueFactory(
						new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
							public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
								return new SimpleStringProperty(param.getValue().get(index).toString());
							}
						});
				tblReporte.getColumns().add(col);
			}
		} else if (entity instanceof EvaluacionPrueba) {
			lstEvaluaciones = list.stream().map(item -> (EvaluacionPrueba) item).collect(Collectors.toList());
			List<OTColegioCurso> lstCmbValues = lstEvaluaciones.stream()
					.map(e -> new OTColegioCurso(e.getColegio(), e.getCurso())).collect(Collectors.toList());
			cmbCurso.setItems(FXCollections.observableArrayList(lstCmbValues));
		} else if (entity instanceof PruebaRendida) {
			lstPRendidas = list.stream().map(item -> (PruebaRendida) item).collect(Collectors.toList());
			makeReport();
		}

	}

	@Override
	public void onFound(IEntity entity) {
		if (entity instanceof EvaluacionPrueba) {

			EvaluacionPrueba e = (EvaluacionPrueba) entity;
			cmbCurso.setItems(
					FXCollections.observableArrayList(Arrays.asList(new OTColegioCurso(e.getColegio(), e.getCurso()))));
		}
	}

	/**
	 * Se procesa cada registro para presetarlo en las tablas.
	 */
	@SuppressWarnings("unchecked")
	private void makeReport() {
		ObservableList<ObservableList<String>> contenido = FXCollections.observableArrayList();
		ObservableList<String> row = null;
		for (PruebaRendida pr : lstPRendidas) {
			row = FXCollections.observableArrayList();
			row.add(pr.getAlumno().getPaterno());
			row.add(pr.getAlumno().getMaterno());
			row.add(pr.getAlumno().getName());
			String[] respuesta = pr.getRespuestas().split("");
			int n = 0;
			for (RespuestasEsperadasPrueba resp : lstREsperadas) {
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

	public Prueba getPrueba() {
		return prueba;
	}

	public void setPrueba(Prueba prueba) {
		this.prueba = prueba;
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
			returValue = "+";
		} else {
			returValue = "-";
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

		lstEvaluaciones.forEach(evaluacion -> {
			Map<String, Object> params = MapBuilder.<String, Object>unordered().put("ideval", evaluacion.getId())
					.build();

			// Creación ed la hoja
			final Sheet sheet = wbook.createSheet(evaluacion.getColegio() + "-" + evaluacion.getCurso());

			// El título de la hoja
			final Row header = sheet.createRow(0);
			header.setHeightInPoints(sheet.getDefaultRowHeightInPoints());
			Cell cellHeader = header.createCell(0);
			cellHeader.setCellValue(evaluacion.getColegio() + "-" + evaluacion.getCurso());
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
			List<IEntity> _lstPRendidas = controller.findSynchro("PruebaRendidaByEval.findAll", params);

			int row = 2;
			for (IEntity entity : _lstPRendidas) {
				final Row fila = sheet.createRow(row++);
				PruebaRendida pr = (PruebaRendida) entity;

				Cell cell = fila.createCell(0);
				cell.setCellValue(pr.getAlumno().getPaterno());

				cell = fila.createCell(1);
				cell.setCellValue(pr.getAlumno().getMaterno());

				cell = fila.createCell(2);
				cell.setCellValue(pr.getAlumno().getName());

				String[] resps = pr.getRespuestas().split("");

				for (int n = 0; n < resps.length; n++) {
					String value = evaluateResp(lstREsperadas.get(n), resps[n]);
					cell = fila.createCell(n + 3);
					cell.setCellValue(value);
					cell.getCellStyle().setAlignment(CellStyle.ALIGN_CENTER);
				}
			}

		});
		ExcelSheetWriterObj.crearDocExcel(wbook);

	}

}
