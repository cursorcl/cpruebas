package cl.eos.util;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

import javax.swing.ImageIcon;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.controlsfx.dialog.Dialogs;

/**
 * Clase que permite exportar a excel datos asociados a las tablas.
 * 
 * @author Sisdef.
 */
public final class ExcelSheetWriterObj {

	private static CellStyle titleStyle;
	private static CellStyle cStyle;

	/**
	 * Constructor de la clase.
	 */
	private ExcelSheetWriterObj() {
		// Sin implementar.
	}

	public static void convertirDatosALibroDeExcel(
			TableView<? extends Object> tabla) {
		final Workbook wbook = new HSSFWorkbook();
		crearLibroConDatosDeTabla(tabla, wbook);
		crearDocExcel(wbook);
	}

	public static void convertirDatosALibroDeExcel(
			TreeTableView<? extends Object> tabla) {
		final Workbook wbook = new HSSFWorkbook();
		tabla.getRoot().setExpanded(true);
		crearLibroConDatosDeTabla(tabla, wbook);
		crearDocExcel(wbook);
	}

	public static void convertirDatosALibroDeExcel(
			List<TableView<? extends Object>> listaTablas) {
		final Workbook wbook = new HSSFWorkbook();
		for (TableView<? extends Object> tableView : listaTablas) {

			crearLibroConDatosDeTabla(tableView, wbook);
		}
		crearDocExcel(wbook);
	}

	public static void convertirDatosColumnasDoblesALibroDeExcel(
			List<TableView<? extends Object>> listaTablas) {
		final Workbook wbook = new HSSFWorkbook();
		for (TableView<? extends Object> tableView : listaTablas) {

			crearLibroConDatosDeTablaColumnasDobles(tableView, wbook);
		}
		crearDocExcel(wbook);
	}

	private static void crearDocExcel(final Workbook wbWork) {
		long time = Calendar.getInstance().getTimeInMillis();
		final String nombreDoc = "wb" + time + ".xls";
		String path = Utils.getDefaultDirectory().toString() + File.separator
				+ nombreDoc;

		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(path);
			wbWork.write(fileOut);
		} catch (IOException e) {
			Dialogs.create().owner(null).title("Problemas al grabar")
					.masthead("No se pudo grabar archivo")
					.message("Nombre:" + path).showError();
		}

		finally {
			try {
				fileOut.close();
			} catch (final IOException e) {
				Dialogs.create().owner(null)
						.title("Problemas al cerrar archivo")
						.masthead("No se pudo cerrar archivo")
						.message("Nombre:" + path).showError();
			}
		}
		mostrarDocumentoExcel(path);
	}

	/**
	 * Metodo que permite la creacion de los datos de la tabla en un libro de
	 * excel.
	 * 
	 * @param wbook
	 * 
	 * @param modelo
	 *            modelo de tabla.
	 * @return workbook libro de excel.
	 */
	private static void crearLibroConDatosDeTabla(
			final TableView<? extends Object> tabla, Workbook wbook) {
		crearDatosHeader(tabla, wbook);
		crearDatosTabla(tabla, wbook);
	}

	private static void crearLibroConDatosDeTablaColumnasDobles(
			final TableView<? extends Object> tabla, Workbook wbook) {
		crearDatosHeaderColumnasDobles(tabla, wbook);
		crearDatosTablaColumnasDobles(tabla, wbook);
	}

	private static void crearLibroConDatosDeTabla(
			final TreeTableView<? extends Object> tabla, Workbook wbook) {
		crearDatosHeader(tabla, wbook);
		crearDatosTabla(tabla, wbook);
	}

	private static void crearDatosHeader(TableView<? extends Object> tabla,
			Workbook wbook) {
		String id = tabla.getId();
		final Sheet sheet1 = wbook.createSheet(id);
		final CellStyle style = wbook.createCellStyle();

		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);

		final Row filaCabecera = sheet1.createRow(0);
		for (int indice = 0; indice < tabla.getColumns().size(); indice++) {
			final Cell cell = filaCabecera.createCell(indice);
			cell.setCellValue(tabla.getColumns().get(indice).getText());
			cell.setCellStyle(style);
		}

	}

	private static void crearDatosHeaderColumnasDobles(
			TableView<? extends Object> tabla, Workbook wbook) {

		String id = tabla.getId();
		final Sheet sheet1 = wbook.createSheet(id);
		System.out.println("# Sheets=" + wbook.getNumberOfSheets() + " del wb="
				+ wbook + " de la hoja:" + sheet1);
		final HSSFCellStyle style = (HSSFCellStyle) wbook.createCellStyle();

		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setAlignment(CellStyle.VERTICAL_CENTER);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);

		final Row filaTitulo = sheet1.createRow(0);
		final Row filaSubtitulo = sheet1.createRow(1);
		int idx = 0;
		for (int indice = 0; indice < tabla.getColumns().size(); indice++) {
			TableColumn<? extends Object, ?> columna = tabla.getColumns().get(
					indice);
			if (columna.getColumns() != null && !columna.getColumns().isEmpty()) {
				for (int n = 0; n < columna.getColumns().size(); n++) {
					Cell cell = filaTitulo.createCell(idx);
					cell.setCellValue("");
					if (n == 0) {
						cell.setCellValue(tabla.getColumns().get(indice)
								.getText());
					}
					cell.setCellStyle(style);

					cell = filaSubtitulo.createCell(idx);
					cell.setCellValue(columna.getColumns().get(n).getText());
					cell.setCellStyle(style);
					idx++;
				}

				sheet1.addMergedRegion(new CellRangeAddress(0, 0,
						(indice - 1) * 4 + 1, (indice - 1) * 4 + 4));

			} else {
				Cell cell = filaTitulo.createCell(idx);
				cell.setCellValue(tabla.getColumns().get(indice).getText());
				cell.setCellStyle(style);
				cell = filaSubtitulo.createCell(idx);
				cell.setCellValue("");
				cell.setCellStyle(style);
				idx++;

			}
		}

	}

	private static void crearDatosHeader(TreeTableView<? extends Object> tabla,
			Workbook wbook) {

		String id = tabla.getId();
		final Sheet sheet1 = wbook.createSheet(id);

		final CellStyle style = wbook.createCellStyle();

		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);

		final Row filaCabecera = sheet1.createRow(0);
		for (int indice = 0; indice < tabla.getColumns().size(); indice++) {
			final Cell cell = filaCabecera.createCell(indice);
			cell.setCellValue(tabla.getColumns().get(indice).getText());
			cell.setCellStyle(style);
		}

	}

	/**
	 * Metodo que permite crear los datos en la tabla.
	 * 
	 * @param modelo
	 *            modelo de tabla.
	 * @param wbook
	 *            libro de excel.
	 * @return workbook libro de excel modificado.
	 */
	private static void crearDatosTabla(TableView<? extends Object> tabla,
			final Workbook wbook) {
		String id = tabla.getId();
		final Sheet sheet1 = wbook.getSheet(id);

		for (int indiceFila = 0; indiceFila < tabla.getItems().size(); indiceFila++) {
			final Row fila = sheet1.createRow(indiceFila + 1);
			recorrerColumnas(tabla, indiceFila, fila);
		}
	}

	private static void crearDatosTablaColumnasDobles(
			TableView<? extends Object> tabla, Workbook wbook) {
		String id = tabla.getId();
		final Sheet sheet1 = wbook.getSheet(id);

		for (int indiceFila = 0; indiceFila < tabla.getItems().size(); indiceFila++) {
			final Row fila = sheet1.createRow(indiceFila + 2);
			recorrerColumnasDobles(tabla, indiceFila, fila);
		}
	}

	/**
	 * Metodo que permite crear los datos en la tabla.
	 * 
	 * @param modelo
	 *            modelo de tabla.
	 * @param wbook
	 *            libro de excel.
	 * @return workbook libro de excel modificado.
	 */
	private static void crearDatosTabla(TreeTableView<? extends Object> tabla,
			final Workbook wbook) {
		final Sheet sheet1 = wbook.getSheet(tabla.getId());

		int indiceFila = 0;
		while (tabla.getTreeItem(indiceFila) != null) {
			tabla.getTreeItem(indiceFila).setExpanded(true);
			final Row fila = sheet1.createRow(indiceFila + 1);
			recorrerColumnas(tabla, indiceFila, fila);
			indiceFila++;
		}
	}

	/**
	 * Metodo que permite el recorrido de las columnas.
	 * 
	 * @param modelo
	 *            modelo de tabla.
	 * @param indiceFila
	 *            indice de la fila.
	 * @param fila
	 *            fila.
	 */
	private static void recorrerColumnas(
			final TreeTableView<? extends Object> tabla, final int indiceFila,
			final Row fila) {
		for (int indiceColumna = 0; indiceColumna < tabla.getColumns().size(); indiceColumna++) {

			final Cell cell = fila.createCell(indiceColumna);
			TreeTableColumn<? extends Object, ?> valores = tabla.getColumns()
					.get(indiceColumna);
			Object valor = valores.getCellData(indiceFila);
			if (valor instanceof String) {
				cell.setCellValue((String) valor);
			} else if (valor instanceof Boolean) {
				cell.setCellValue((Boolean) valor);
			} else if (valor instanceof Date) {
				// cell.setCellValue(UtilesFechas.formatDate((Date)
				// valor));
			} else if (valor instanceof Integer) {
				cell.setCellValue((Integer) valor);
			} else if (valor instanceof Long) {
				cell.setCellValue((Long) valor);
			} else if (valor instanceof Double) {
				cell.setCellValue((Double) valor);
			} else if (valor instanceof Float) {
				cell.setCellValue((Float) valor);
			} else if ((valor instanceof ImageIcon) || (valor == null)
					|| valor instanceof Color) {
				cell.setCellValue("");
			} else {
				cell.setCellValue((String) valor.toString());
			}
		}
	}

	/**
	 * Metodo que permite el recorrido de las columnas.
	 * 
	 * @param modelo
	 *            modelo de tabla.
	 * @param indiceFila
	 *            indice de la fila.
	 * @param fila
	 *            fila.
	 */
	private static void recorrerColumnas(
			final TableView<? extends Object> tabla, final int indiceFila,
			final Row fila) {
		for (int indiceColumna = 0; indiceColumna < tabla.getColumns().size(); indiceColumna++) {

			final Cell cell = fila.createCell(indiceColumna);
			TableColumn<? extends Object, ?> valores = tabla.getColumns().get(
					indiceColumna);
			Object valor = valores.getCellData(indiceFila);
			if (valor instanceof String) {
				cell.setCellValue((String) valor);
			} else if (valor instanceof Boolean) {
				cell.setCellValue((Boolean) valor);
			} else if (valor instanceof Date) {
				// cell.setCellValue(UtilesFechas.formatDate((Date)
				// valor));
			} else if (valor instanceof Integer) {
				cell.setCellValue((Integer) valor);
			} else if (valor instanceof Long) {
				cell.setCellValue((Long) valor);
			} else if (valor instanceof Double) {
				cell.setCellValue((Double) valor);
			} else if (valor instanceof Float) {
				cell.setCellValue((Float) valor);
			} else if ((valor instanceof ImageIcon) || (valor == null)
					|| valor instanceof Color) {
				cell.setCellValue("");
			} else {
				cell.setCellValue((String) valor.toString());
			}
		}
	}

	private static void recorrerColumnasDobles(
			final TableView<? extends Object> tabla, final int indiceFila,
			final Row fila) {

		int idx = 0;
		for (int indiceColumna = 0; indiceColumna < tabla.getColumns().size(); indiceColumna++) {

			TableColumn<? extends Object, ?> valores = tabla.getColumns().get(
					indiceColumna);
			if (valores.getColumns() != null && !valores.getColumns().isEmpty()) {
				for (int n = 0; n < valores.getColumns().size(); n++) {
					TableColumn<? extends Object, ?> values = valores
							.getColumns().get(n);
					Object valor = values.getCellData(indiceFila);
					final Cell cell = fila.createCell(idx);
					CellStyle style = cell.getCellStyle();
					style.setAlignment(CellStyle.ALIGN_CENTER);
					style.setAlignment(CellStyle.VERTICAL_CENTER);
					setValueToCell(valor, cell);
					cell.setCellStyle(style);
					idx++;
				}
			} else {
				Object valor = valores.getCellData(indiceFila);
				final Cell cell = fila.createCell(idx);
				setValueToCell(valor, cell);
				CellStyle style = cell.getCellStyle();
				style.setAlignment(CellStyle.ALIGN_CENTER);
				style.setAlignment(CellStyle.VERTICAL_CENTER);
				setValueToCell(valor, cell);
				idx++;
			}

		}
	}

	private static void setValueToCell(Object valor, Cell cell) {
		if (valor instanceof String) {
			cell.setCellValue((String) valor);
		} else if (valor instanceof Boolean) {
			cell.setCellValue((Boolean) valor);
		} else if (valor instanceof Date) {
			// cell.setCellValue(UtilesFechas.formatDate((Date)
			// valor));
		} else if (valor instanceof Integer) {
			cell.setCellValue((Integer) valor);
		} else if (valor instanceof Long) {
			cell.setCellValue((Long) valor);
		} else if (valor instanceof Double) {
			cell.setCellValue((Double) valor);
		} else if (valor instanceof Float) {
			cell.setCellValue((Float) valor);
		} else if ((valor instanceof ImageIcon) || (valor == null)
				|| valor instanceof Color) {
			cell.setCellValue("");
		} else {
			cell.setCellValue((String) valor.toString());
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void generarReporteComparativoColegioEjeHabilidadCurso(
			final TableView<? extends Object> tabla, String colegio) {

		final Workbook wbook = new HSSFWorkbook();

		final Sheet sheet = wbook.createSheet(tabla.getId());

		Row header = sheet.createRow(0);
		header.setHeightInPoints(sheet.getDefaultRowHeightInPoints());
		Cell cell = header.createCell(0);
		cell.setCellValue("COMPARATIVO COLEGIO EJE Y HABILIDADES POR CURSO");
		applySheetTitleStyle(cell);
		header = sheet.createRow(1);
		header.setHeightInPoints(sheet.getDefaultRowHeightInPoints());
		cell = header.createCell(0);
		applySheetSubTitleStyle(cell);
		cell.setCellValue(colegio);

		Row header1 = sheet.createRow(2);
		Row header2 = sheet.createRow(3);
		// Estableciendo los titulos.
		int indice = 0;
		for (TableColumn tc : tabla.getColumns()) {
			if (tc.getColumns() != null && !tc.getColumns().isEmpty()) {
				boolean first = true;
				for (Object obj : tc.getColumns()) {
					Cell cell1 = header1.createCell(indice);
					applyTitleStyle(cell1);
					Cell cell2 = header2.createCell(indice);
					applyTitleStyle(cell2);
					if (first) {
						cell1.setCellValue(tc.getText());
						first = false;
					}
					TableColumn tcI = (TableColumn) obj;
					cell2.setCellValue(tcI.getText());
					indice++;
				}
				sheet.addMergedRegion(new CellRangeAddress(2, 2, indice - 3, indice - 1 ));
			} else {
				Cell cell1 = header1.createCell(indice);
				applyTitleStyle(cell1);
				Cell cell2 = header2.createCell(indice);
				applyTitleStyle(cell2);
				cell2.setCellValue(tc.getText());
				indice++;
			}
		}

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, indice - 1));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, indice - 1));

		indice = 4;
		ObservableList<? extends Object> list = tabla.getItems();
		for (Object row : list) {
			Row register = sheet.createRow(indice++);
			ObservableList<String> itemsRow = (ObservableList<String>) row;
			int colIndex = 0;
			for (String cellValue : itemsRow) {
				cell = register.createCell(colIndex++);
				if (colIndex > 1) {
					applyNormalStyle(cell);
				} else {
					applyLeftFirstColumnStyle(cell);
				}
				cell.setCellValue(cellValue);
			}
		}
		sheet.autoSizeColumn(0);
		crearDocExcel(wbook);
	}

	private static void applySheetSubTitleStyle(Cell cell) {
		Font font = cell.getSheet().getWorkbook().createFont();
		font.setFontHeightInPoints((short) 20);
		font.setFontName("IMPACT");
		font.setItalic(true);
		font.setColor(HSSFColor.BRIGHT_GREEN.index);

		cStyle = cell.getSheet().getWorkbook().createCellStyle();
		cStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
		cStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
		cStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
		cStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cStyle.setFont(font);
		cStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cell.setCellStyle(cStyle);
	}

	private static void applySheetTitleStyle(Cell cell) {
		Font font = cell.getSheet().getWorkbook().createFont();
		font.setFontHeightInPoints((short) 30);
		font.setFontName("IMPACT");
		font.setItalic(true);
		font.setColor(HSSFColor.BRIGHT_GREEN.index);

		cStyle = cell.getSheet().getWorkbook().createCellStyle();
		cStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
		cStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
		cStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
		cStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cStyle.setFont(font);
		cStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cell.setCellStyle(cStyle);
	}

	private static void applyLeftFirstColumnStyle(Cell cell) {
		cStyle = cell.getSheet().getWorkbook().createCellStyle();
		cStyle.setAlignment(CellStyle.ALIGN_LEFT);
		cStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
		cStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
		cStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
		cStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		cStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cell.setCellStyle(cStyle);

	}

	public static void applyTitleStyle(Cell cell) {
		cStyle = cell.getSheet().getWorkbook().createCellStyle();
		cStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
		cStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
		cStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
		cStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		cStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cell.setCellStyle(cStyle);
	}

	public static void applyNormalStyle(Cell cell) {
		cStyle = cell.getSheet().getWorkbook().createCellStyle();
		cStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cStyle.setBorderLeft(CellStyle.BORDER_THIN);
		cStyle.setBorderRight(CellStyle.BORDER_THIN);
		cStyle.setBorderTop(CellStyle.BORDER_THIN);
		cStyle.setBorderBottom(CellStyle.BORDER_THIN);

		cell.setCellStyle(cStyle);
	}

	/**
	 * Metodo que permite desplegar el documento de excel creado.
	 * 
	 * @param nombreDoc
	 *            Nombre del documento.
	 */
	private static void mostrarDocumentoExcel(final String nombreDoc) {
		final File archivo = new File(nombreDoc);
		try {
			Runtime.getRuntime().exec(
					"cmd /c start \"\" \"" + archivo.getAbsolutePath() + "\"");
		} catch (final Exception e) {
			e.getMessage();
		}
		archivo.deleteOnExit();
	}

}
