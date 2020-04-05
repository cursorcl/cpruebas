package cl.eos.external.files.excel;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cl.eos.external.files.utils.Constants;
import cl.eos.external.files.utils.Register;
import cl.eos.external.files.utils.RegisterForView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Esta clase es la encargada de leer los archivos excel.
 * 
 * <ul>
 * <li>Saber leer el archivo con los siguientes campos</li>
 * <ul>
 * <li>idColegio</li>
 * <li>idCurso</li>
 * <li>idAsignatura</li>
 * <li>rut</li>
 * <li>respuestas</li>
 * </ul>
 * </ul>
 * 
 * Casos de prueba.
 * <ul>
 * <li>No existe prueba para la comnbinación colegio, curso asignatura.</li>
 * <li>El alumno ya rindió prueba.</li>
 * <li>La Evaluación existe y hay alumnos que tienen nuevas pruebas.</li>
 * </ul>
 * 
 * 
 * @author eosorio
 *
 */
public class ExcelExams {

	Logger log = Logger.getLogger(ExcelExams.class.getName());
	private ReaderListener listener;

	/**
	 * Priemra aproximación. Leer todo el archivo y ponerlo en memoria en una lista.
	 * 
	 * @param fileName Archivo que se quiere leer.
	 * @return Lisado de registros leidos.
	 */
	public List<Register> readExcelFile(File fileName, ReaderListener listener) {
		this.listener = listener;
		final List<Register> listOfRegisters = new ArrayList<>();
		FileInputStream fileInputStream;
		FileOutputStream fileOutputStream;
		try {
			fileInputStream = new FileInputStream(fileName.getAbsoluteFile());
			if (listener != null)
				listener.onReadFile("Archivo abierto correctamente.");
		} catch (FileNotFoundException e) {
			final Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error en la lectura del archivo Excel.");
			alert.setHeaderText("No es posible abrir el archivo excel.");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
			return null;
		}

		XSSFWorkbook workBook = null;
		try {

			workBook = new XSSFWorkbook(fileInputStream);

			XSSFSheet sheet = workBook.getSheetAt(0);
			List<Register> results = processSheet(sheet);
			if (results != null && !results.isEmpty())
				listOfRegisters.addAll(results);

		} catch (final Exception e) {
			log.log(Level.INFO, "Error en la importación desde excel", e);
			final Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error en la importación desde excel");
			alert.setHeaderText("Informe el siguiente problmea");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		} finally {
			try {
				fileInputStream.close();
				fileOutputStream = new FileOutputStream(fileName.getAbsoluteFile());
				workBook.write(fileOutputStream);
				workBook.close();
				fileOutputStream.close();
			} catch (IOException e) {
			}

		}

		/**
		 * Call the printToConsole method to print the cell data in the console.
		 */
		return listOfRegisters;
	}

	/**
	 * Extrae los datos de una hoja genera una lista.
	 * 
	 * Asume que la primera fila son los títulos de los campos.
	 * 
	 * @param sheet La hoja a procesar.
	 */
	private List<Register> processSheet(XSSFSheet sheet) {
		return processSheet(sheet, true);
	}

	/**
	 * Extrae los datos de una hoja genera una lista.
	 * 
	 * @param sheet            la hoja de donde obtener los datos.
	 * @param firstRowIsHeader Indica si la hoja tiene la primera fila como titulo.
	 * @return Lista de registros leídos desde la hoja.
	 */
	private List<Register> processSheet(XSSFSheet sheet, boolean firstRowIsHeader) {

		final List<Register> registers = new ArrayList<>();

		final Iterator<Row> rowIterator = sheet.rowIterator();
		while (rowIterator.hasNext()) {
			int cellExpected = 0;
			final XSSFRow row = (XSSFRow) rowIterator.next();

			if (firstRowIsHeader) {
				firstRowIsHeader = false;
				continue;
			}

			Register register = new Register();
			register.nRow.set(row.getRowNum());
			for (int n = 0; n < Constants.NRO_COLS; n++) {
				XSSFCell cell = (XSSFCell) row.getCell(n);
				if (cell == null) {
					cell = row.createCell(n);
					cell.setCellValue("ERROR");
				}
				int cType = cell.getCellType();

				try {
					if (cType == Cell.CELL_TYPE_BLANK) {
						cell.setCellValue("BLANCO");
						throw new Exception();
					} else if (cellExpected == Constants.IDCOLEGIO) {
						register.setIdColegio((long) cell.getNumericCellValue());
					} else if (cellExpected == Constants.IDCURSO) {
						register.setIdCurso((long) cell.getNumericCellValue());
					} else if (cellExpected == Constants.IDASIGNATURA) {
						register.setIdAsignatura((long) cell.getNumericCellValue());
					} else if (cellExpected == Constants.RUT) {
						register.setRut(cell.getStringCellValue());
					} else if (cellExpected == Constants.RESPUESTAS) {
						register.setRespuestas(cell.getStringCellValue());
					}
				} catch (Exception ex) {
				}
				cellExpected++;
			}
			if (listener != null)
				listener.onReadRegister(register);
			registers.add(register);
		}

		return registers;
	}

	public void saveExamsResults(File file, List<RegisterForView> goods, List<RegisterForView> bads,
			ReaderListener listener) {

		List<RegisterForView> listOfRegisters = new ArrayList<>(goods);
		listOfRegisters.addAll(bads);
		listOfRegisters.sort(new Comparator<RegisterForView>() {
			@Override
			public int compare(RegisterForView o1, RegisterForView o2) {
				return (int) (o1.getNRow() - o2.getNRow());
			}
		});

		this.listener = listener;
		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;
		try {
			fileInputStream = new FileInputStream(file.getAbsoluteFile());
			if (listener != null)
				listener.onReadFile("Archivo abierto correctamente.");
		} catch (FileNotFoundException e) {
			log.log(Level.INFO, "Error en la lectura del archivo Excel.", e);
			final Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error en la lectura del archivo Excel.");
			alert.setHeaderText("No es posible abrir el archivo excel.");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
			return;
		}

		XSSFWorkbook workBook;
		try {
			workBook = new XSSFWorkbook(fileInputStream);
		} catch (IOException e) {
			log.log(Level.INFO, "Error en la lectura del archivo Excel.", e);
			final Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error en la lectura del archivo Excel.");
			alert.setHeaderText("No es posible abrir el archivo excel.");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
			return;
		}

		try {

			int nRow = 0;
			String pattern = "yyyyMMddHHmmss";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String fecha = simpleDateFormat.format(new Date());
			XSSFSheet sheet = workBook.createSheet("PROCESADO-" + fecha);
			XSSFRow rowHeader = sheet.createRow(nRow);
			setHeader(rowHeader);

			for (RegisterForView register : listOfRegisters) {
				XSSFRow row = sheet.createRow(++nRow);
				setValue(row, register);
			}

			autoAdjustColumns(sheet);

		} catch (final Exception e) {
			log.log(Level.INFO, "Error en la grabación del excel de resultados.", e);
			final Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error en la grabación del excel de resultados.");
			alert.setHeaderText("Informe el siguiente problmea");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		} finally {
			try {
				fileInputStream.close();
				fileOutputStream = new FileOutputStream(file.getAbsoluteFile());
				workBook.write(fileOutputStream);
				workBook.close();
				fileOutputStream.close();
			} catch (IOException e) {
			}

		}
	}

	private void autoAdjustColumns(XSSFSheet sheet) {
		for (int n = 0; n < 10; n++) {
			sheet.autoSizeColumn(n);
		}

	}

	private void setValue(XSSFRow row, RegisterForView register) {
		XSSFCell cell = row.createCell(0);
		cell.setCellValue(register.getIdColegio());
		cell = row.createCell(1);
		cell.setCellValue(register.getIdCurso());
		cell = row.createCell(2);
		cell.setCellValue(register.getIdAsignatura());
		cell = row.createCell(3);
		cell.setCellValue(register.getRut().toUpperCase());
		cell = row.createCell(4);
		cell.setCellValue(register.getRespuestas().toUpperCase());
		cell = row.createCell(5);
		cell.setCellValue(register.status.get() == null ? "" : register.status.get().toString());
		cell = row.createCell(6);
		cell.setCellValue(register.colegio.get() == null ? "" : register.colegio.get().getName());
		cell = row.createCell(7);
		cell.setCellValue(register.curso.get() == null ? "" : register.curso.get().getName());
		cell = row.createCell(8);
		cell.setCellValue(register.asignatura.get() == null ? "" : register.asignatura.get().getName());
		cell = row.createCell(9);
		String alumno = register.alumno.get() == null ? ""
				: register.alumno.get().getPaterno() + " " + register.alumno.get().getMaterno() + " "
						+ register.alumno.get().getName();
		cell.setCellValue(alumno);
	}

	private void setHeader(XSSFRow row) {
		XSSFColor myColor = new XSSFColor(Color.LIGHT_GRAY);
		XSSFCellStyle style = null;

		XSSFCell cell = row.createCell(0);
		style = (XSSFCellStyle) cell.getCellStyle();
		style.setFillForegroundColor(myColor);
		cell.setCellValue("IdColegio");

		cell = row.createCell(1);
		style = (XSSFCellStyle) cell.getCellStyle();
		style.setFillForegroundColor(myColor);
		cell.setCellValue("IdCurso");

		cell = row.createCell(2);
		style = (XSSFCellStyle) cell.getCellStyle();
		style.setFillForegroundColor(myColor);
		cell.setCellValue("IdAsignatura");

		cell = row.createCell(3);
		style = (XSSFCellStyle) cell.getCellStyle();
		style.setFillForegroundColor(myColor);
		cell.setCellValue("Rut");

		cell = row.createCell(4);
		style = (XSSFCellStyle) cell.getCellStyle();
		style.setFillForegroundColor(myColor);
		cell.setCellValue("Respuestas");

		cell = row.createCell(5);
		style = (XSSFCellStyle) cell.getCellStyle();
		style.setFillForegroundColor(myColor);
		cell.setCellValue("Estado");

		cell = row.createCell(6);
		style = (XSSFCellStyle) cell.getCellStyle();
		style.setFillForegroundColor(myColor);
		cell.setCellValue("Colegio");

		cell = row.createCell(7);
		style = (XSSFCellStyle) cell.getCellStyle();
		style.setFillForegroundColor(myColor);
		cell.setCellValue("Curso");

		cell = row.createCell(8);
		style = (XSSFCellStyle) cell.getCellStyle();
		style.setFillForegroundColor(myColor);
		cell.setCellValue("Asignatura");

		cell = row.createCell(9);
		style = (XSSFCellStyle) cell.getCellStyle();
		style.setFillForegroundColor(myColor);
		cell.setCellValue("Alumno");

	}

}
