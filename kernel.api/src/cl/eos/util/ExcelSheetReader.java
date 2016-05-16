package cl.eos.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class ExcelSheetReader {

	private File fileNameLocal = null;

	/**
	 * This method is used to read the data's from an excel file.
	 * 
	 * @param fileName
	 *            - Name of the excel file.
	 */
	public List readExcelFile(File fileName) {
		fileNameLocal = fileName;
		/**
		 * Create a new instance for cellDataList
		 */
		List cellDataList = new ArrayList();
		try {
			/**
			 * Create a new instance for FileInputStream class
			 */
			FileInputStream fileInputStream = new FileInputStream(fileName.getAbsoluteFile());
			/**
			 * Create a new instance for POIFSFileSystem class
			 */
			POIFSFileSystem fsFileSystem = new POIFSFileSystem(fileInputStream);
			/*
			 * Create a new instance for HSSFWorkBook Class
			 */
			HSSFWorkbook workBook = new HSSFWorkbook(fsFileSystem);
			HSSFSheet hssfSheet = workBook.getSheetAt(0);
			/**
			 * Iterate the rows and cells of the spreadsheet to get all the
			 * datas.
			 */
			int contColumnas = 0;
			Iterator rowIterator = hssfSheet.rowIterator();
			while (rowIterator.hasNext()) {
				HSSFRow hssfRow = (HSSFRow) rowIterator.next();
				if (hssfRow.getRowNum() == 0) {
					Iterator iterator = hssfRow.cellIterator();
					while (iterator.hasNext()) {
						HSSFCell hssfCell = (HSSFCell) iterator.next();
						contColumnas++;
					}
				} else {
					Iterator iterator = hssfRow.cellIterator();
					List cellTempList = new ArrayList();
					while (iterator.hasNext()) {
						HSSFCell hssfCell = (HSSFCell) iterator.next();
						if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
							cellTempList.add(" ");
						} else if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
							cellTempList.add(hssfCell.getStringCellValue());
						} else if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
							cellTempList.add(hssfCell.getNumericCellValue());
						}
					}
					if (cellTempList.size() != contColumnas) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Importación desde excel");
						alert.setHeaderText("");
						alert.setContentText("Archivo " + fileNameLocal.getName() + " incompleto. \n" + "Se esperan "
								+ contColumnas + " columna(s), llegaron " + cellTempList.size() + " columna(s).");
						break;
					} else {
						cellDataList.add(cellTempList);
					}
				}
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error en la importación desde excel");
			alert.setHeaderText("Informe el siguiente problmea");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}

		/**
		 * Call the printToConsole method to print the cell data in the console.
		 */
		// printToConsole(cellDataList);
		return cellDataList;
	}

	/**
	 * This method is used to print the cell data to the console.
	 * 
	 * @param cellDataList
	 *            - List of the data's in the spreadsheet.
	 */
	private void printToConsole(List cellDataList) {
		for (int i = 0; i < cellDataList.size(); i++) {
			List cellTempList = (List) cellDataList.get(i);
			for (int j = 0; j < cellTempList.size(); j++) {
				HSSFCell hssfCell = (HSSFCell) cellTempList.get(j);
				String stringCellValue = hssfCell.toString();
				System.out.println(stringCellValue + "\t");
			}
			System.out.println();
		}
	}

}
