package cl.eos.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ExcelSheetReaderx {
    public static void main(String[] args) {
        // String fileName = "C:" + File.separator + "APP" + File.separator
        // + "ciclo.xls";
        // new ExcelSheetReader().readExcelFile(fileName);
    }

    private File fileNameLocal = null;

    /**
     * This method is used to read the data's from an excel file.
     * 
     * @param fileName
     *            - Name of the excel file.
     */
    @SuppressWarnings("unchecked")
    public List readExcelFile(File fileName) {
        fileNameLocal = fileName;
        /**
         * Create a new instance for cellDataList
         */
        final List cellDataList = new ArrayList();
        try {
            /**
             * Create a new instance for FileInputStream class
             */
            final FileInputStream fileInputStream = new FileInputStream(fileName.getAbsoluteFile());
            /**
             * // * Create a new instance for POIFSFileSystem class //
             */
            // POIFSFileSystem fsFileSystem = new
            // POIFSFileSystem(fileInputStream);
            /*
             * Create a new instance for HSSFWorkBook Class
             */
            final XSSFWorkbook workBook = new XSSFWorkbook(fileInputStream);
            final XSSFSheet hssfSheet = workBook.getSheetAt(0);
            /**
             * Iterate the rows and cells of the spreadsheet to get all the
             * datas.
             */
            int contColumnas = 0;
            final Iterator<Row> rowIterator = hssfSheet.rowIterator();
            while (rowIterator.hasNext()) {
                // XSSFRow hssfRow = (XSSFRow) rowIterator.next();
                // Iterator iterator = hssfRow.cellIterator();
                // List cellTempList = new ArrayList();
                // while (iterator.hasNext()) {
                // XSSFCell hssfCell = (XSSFCell) iterator.next();
                // cellTempList.add(hssfCell.getStringCellValue());
                // }
                // cellDataList.add(cellTempList);
                final XSSFRow hssfRow = (XSSFRow) rowIterator.next();
                if (hssfRow.getRowNum() == 0) {
                    final Iterator<Cell> iterator = hssfRow.cellIterator();
                    while (iterator.hasNext()) {
                        iterator.next();
                        contColumnas++;
                    }
                } else {
                    final Iterator iterator = hssfRow.cellIterator();
                    final List cellTempList = new ArrayList();
                    while (iterator.hasNext()) {
                        final XSSFCell hssfCell = (XSSFCell) iterator.next();
                        if (hssfCell.getCellType() == Cell.CELL_TYPE_BLANK) {
                            cellTempList.add(" ");
                        } else if (hssfCell.getCellType() == Cell.CELL_TYPE_STRING) {
                            cellTempList.add(hssfCell.getStringCellValue());
                        } else if (hssfCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            cellTempList.add(hssfCell.getNumericCellValue());
                        }
                    }
                    if (cellTempList.size() != contColumnas) {
                        final Alert alert = new Alert(AlertType.ERROR);
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
            workBook.close();
        } catch (final Exception e) {
            final Alert alert = new Alert(AlertType.ERROR);
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
}
