package cl.eos.util;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javax.swing.ImageIcon;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Clase que permite exportar a excel datos asociados a las tablas.
 * 
 * @author Sisdef.
 */
public final class ExcelSheetWriterObj {

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
			List<TableView<? extends Object>> listaTablas) {
		final Workbook wbook = new HSSFWorkbook();
		for (TableView<? extends Object> tableView : listaTablas) {

			crearLibroConDatosDeTabla(tableView, wbook);
		}
		crearDocExcel(wbook);
	}

	private static void crearDocExcel(final Workbook wbWork) {
		long time = Calendar.getInstance().getTimeInMillis();
		final String nombreDoc = "wb" + time + ".xls";
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(nombreDoc);
			wbWork.write(fileOut);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileOut.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		mostrarDocumentoExcel(nombreDoc);
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
		final Workbook workbook = wbook;
		crearDatosHeader(tabla, workbook);
		crearDatosTabla(tabla, workbook);
	}

	private static void crearDatosHeader(TableView<? extends Object> tabla,
			Workbook wbook) {

		final Sheet sheet1 = wbook.createSheet(tabla.getId());
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
		final Workbook workbook = wbook;
		final Sheet sheet1 = workbook.getSheet(tabla.getId());

		for (int indiceFila = 0; indiceFila < tabla.getItems().size(); indiceFila++) {
			final Row fila = sheet1.createRow(indiceFila + 1);
			recorrerColumnas(tabla, indiceFila, fila);
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
			} else if ((valor instanceof ImageIcon) || (valor == null)
					|| valor instanceof Color) {
				cell.setCellValue("");
			} else {
				cell.setCellValue((String) valor.toString());
			}
		}
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
