package cl.eos.external.files.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cl.eos.external.files.utils.Constants;
import cl.eos.external.files.utils.Register;
import cl.eos.external.files.utils.Results;
import cl.eos.util.Utils;
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
public class ExcelExamsReader {


	public static void main(String[] args) {

		
		File f = new File("c:\\tmp\\multi_1A.xlsx");
		ExcelExamsReader importer = new ExcelExamsReader();
		Results results = importer.readExcelFile(f);
		if (results != null)
			for (Register reg : results.results)
				System.out.println(reg);

		System.out.println("BAD RESULTS");
		for (Register reg : results.badResults)
			System.out.println(reg);
	}

	/**
	 * Priemra aproximación. Leer todo el archivo y ponerlo en memoria en una lista.
	 * 
	 * @param fileName Archivo que se quiere leer.
	 * @return Lisado de registros leidos.
	 */
	public Results readExcelFile(File fileName) {
		final List<Register> listOfRegisters = new ArrayList<>();
		final List<Register> badRegisters = new ArrayList<>();
		FileInputStream fileInputStream;
		FileOutputStream fileOutputStream;
		try {
			fileInputStream = new FileInputStream(fileName.getAbsoluteFile());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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

			int nroSheets = workBook.getNumberOfSheets();
			for (int n = 0; n < nroSheets; n++) {
				XSSFSheet sheet = workBook.getSheetAt(n);
				Results results = processSheet(sheet);
				if (results.results != null && !results.results.isEmpty())
					listOfRegisters.addAll(results.results);
				if (results.badResults != null && !results.badResults.isEmpty())
					badRegisters.addAll(results.badResults);

			}

		} catch (final Exception e) {
			e.printStackTrace();
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
		// printToConsole(cellDataList);
		return new Results(listOfRegisters, badRegisters);
	}

	/**
	 * Extrae los datos de una hoja genera una lista.
	 * 
	 * Asume que la primera fila son los títulos de los campos.
	 * 
	 * @param sheet La hoja a procesar.
	 */
	private Results processSheet(XSSFSheet sheet) {
		return processSheet(sheet, true);
	}

	/**
	 * Extrae los datos de una hoja genera una lista.
	 * 
	 * @param sheet            la hoja de donde obtener los datos.
	 * @param firstRowIsHeader Indica si la hoja tiene la primera fila como titulo.
	 * @return Lista de registros leídos desde la hoja.
	 */
	private Results processSheet(XSSFSheet sheet, boolean firstRowIsHeader) {

		final List<Register> registers = new ArrayList<>();
		final List<Register> badRegisters = new ArrayList<>();

		final Iterator<Row> rowIterator = sheet.rowIterator();
		while (rowIterator.hasNext()) {
			int cellExpected = 0;
			final XSSFRow row = (XSSFRow) rowIterator.next();

			if (firstRowIsHeader) {
				firstRowIsHeader = false;
				continue;
			}

			boolean isValidRegister = true;
			Register register = new Register();
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
						register.setIdCurso ((long) cell.getNumericCellValue());
					} else if (cellExpected == Constants.IDASIGNATURA) {
						register.setIdAsignatura ((long) cell.getNumericCellValue());
					} else if (cellExpected == Constants.RUT) {
						register.setRut (cell.getStringCellValue());
						String rut =  register.getRut().trim().toUpperCase().replace(".", "").replace("-", "");
						rut = rut.substring(0, rut.length() - 1) + "-"  + rut.substring(rut.length()-1);
						if(!Utils.validarRut(rut)) {
							throw new Exception();
						}
							
					} else if (cellExpected == Constants.RESPUESTAS) {
						register.setRespuestas (cell.getStringCellValue());
					}
				} catch (Exception ex) {
					isValidRegister = false;
					XSSFCellStyle style = cell.getSheet().getWorkbook().createCellStyle();
					
					style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					style.setFillBackgroundColor(HSSFColor.RED.index);
					style.setFillForegroundColor(HSSFColor.RED.index);
					cell.setCellStyle(style);
					badRegisters.add(register);
				}
				cellExpected++;
			}
			if (isValidRegister)
				registers.add(register);
		}
		return new Results(registers, badRegisters);
	}
}
