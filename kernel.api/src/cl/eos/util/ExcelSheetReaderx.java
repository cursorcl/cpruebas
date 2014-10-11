package cl.eos.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class ExcelSheetReaderx {
	/**
	 * This method is used to read the data's from an excel file.
	 * 
	 * @param fileName
	 *            - Name of the excel file.
	 */
	public List readExcelFile(File fileName) {
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
//			 * Create a new instance for POIFSFileSystem class
//			 */
//			POIFSFileSystem fsFileSystem = new POIFSFileSystem(fileInputStream);
			/*
			 * Create a new instance for HSSFWorkBook Class
			 */
			XSSFWorkbook  workBook = new XSSFWorkbook (fileInputStream);
			XSSFSheet hssfSheet = workBook.getSheetAt(0);
			/**
			 * Iterate the rows and cells of the spreadsheet to get all the
			 * datas.
			 */
			Iterator rowIterator = hssfSheet.rowIterator();
			while (rowIterator.hasNext()) {
				XSSFRow hssfRow = (XSSFRow) rowIterator.next();
				Iterator iterator = hssfRow.cellIterator();
				List cellTempList = new ArrayList();
				while (iterator.hasNext()) {
					XSSFCell hssfCell = (XSSFCell) iterator.next();
					cellTempList.add(hssfCell.getStringCellValue());
				}
				cellDataList.add(cellTempList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/**
		 * Call the printToConsole method to print the cell data in the console.
		 */
		//printToConsole(cellDataList);
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
				XSSFCell hssfCell = (XSSFCell) cellTempList.get(j);
				String stringCellValue = hssfCell.toString();
				System.out.println(stringCellValue + "\t");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
//		String fileName = "C:" + File.separator + "APP" + File.separator
//				+ "ciclo.xls";
//		new ExcelSheetReader().readExcelFile(fileName);
	}
}