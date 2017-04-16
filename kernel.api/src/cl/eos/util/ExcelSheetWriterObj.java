package cl.eos.util;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
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

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

/**
 * Clase que permite exportar a excel datos asociados a las tablas.
 *
 * @author Sisdef.
 */
public final class ExcelSheetWriterObj {

    private static final int SHEET_NAME_LENGTH = 30;
    private static CellStyle cStyle;

    private static void applyLeftFirstColumnStyle(Cell cell) {
        ExcelSheetWriterObj.cStyle = cell.getSheet().getWorkbook().createCellStyle();
        ExcelSheetWriterObj.cStyle.setAlignment(CellStyle.ALIGN_LEFT);
        ExcelSheetWriterObj.cStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
        ExcelSheetWriterObj.cStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
        ExcelSheetWriterObj.cStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
        ExcelSheetWriterObj.cStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
        ExcelSheetWriterObj.cStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        ExcelSheetWriterObj.cStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cell.setCellStyle(ExcelSheetWriterObj.cStyle);

    }

    public static void applyNormalStyle(Cell cell) {
        ExcelSheetWriterObj.cStyle = cell.getSheet().getWorkbook().createCellStyle();
        ExcelSheetWriterObj.cStyle.setAlignment(CellStyle.ALIGN_CENTER);
        ExcelSheetWriterObj.cStyle.setBorderLeft(CellStyle.BORDER_THIN);
        ExcelSheetWriterObj.cStyle.setBorderRight(CellStyle.BORDER_THIN);
        ExcelSheetWriterObj.cStyle.setBorderTop(CellStyle.BORDER_THIN);
        ExcelSheetWriterObj.cStyle.setBorderBottom(CellStyle.BORDER_THIN);

        cell.setCellStyle(ExcelSheetWriterObj.cStyle);
    }

    private static void applySheetSubTitleStyle(Cell cell) {
        final Font font = cell.getSheet().getWorkbook().createFont();
        font.setFontHeightInPoints((short) 20);
        font.setFontName("IMPACT");
        font.setItalic(true);
        font.setColor(HSSFColor.BRIGHT_GREEN.index);

        ExcelSheetWriterObj.cStyle = cell.getSheet().getWorkbook().createCellStyle();
        ExcelSheetWriterObj.cStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
        ExcelSheetWriterObj.cStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
        ExcelSheetWriterObj.cStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
        ExcelSheetWriterObj.cStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
        ExcelSheetWriterObj.cStyle.setAlignment(CellStyle.ALIGN_CENTER);
        ExcelSheetWriterObj.cStyle.setFont(font);
        ExcelSheetWriterObj.cStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cell.setCellStyle(ExcelSheetWriterObj.cStyle);
    }

    public static void applySheetTitleStyle(Cell cell) {
        final Font font = cell.getSheet().getWorkbook().createFont();
        font.setFontHeightInPoints((short) ExcelSheetWriterObj.SHEET_NAME_LENGTH);
        font.setFontName("IMPACT");
        font.setItalic(true);
        font.setColor(HSSFColor.BRIGHT_GREEN.index);

        ExcelSheetWriterObj.cStyle = cell.getSheet().getWorkbook().createCellStyle();
        ExcelSheetWriterObj.cStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
        ExcelSheetWriterObj.cStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
        ExcelSheetWriterObj.cStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
        ExcelSheetWriterObj.cStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
        ExcelSheetWriterObj.cStyle.setAlignment(CellStyle.ALIGN_CENTER);
        ExcelSheetWriterObj.cStyle.setFont(font);
        ExcelSheetWriterObj.cStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cell.setCellStyle(ExcelSheetWriterObj.cStyle);
    }

    public static void applyTitleStyle(Cell cell) {
        ExcelSheetWriterObj.cStyle = cell.getSheet().getWorkbook().createCellStyle();
        ExcelSheetWriterObj.cStyle.setAlignment(CellStyle.ALIGN_CENTER);
        ExcelSheetWriterObj.cStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
        ExcelSheetWriterObj.cStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
        ExcelSheetWriterObj.cStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
        ExcelSheetWriterObj.cStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
        ExcelSheetWriterObj.cStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        ExcelSheetWriterObj.cStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cell.setCellStyle(ExcelSheetWriterObj.cStyle);
    }

    public static void convertirDatosALibroDeExcel(List<TableView<? extends Object>> listaTablas) {
        final Workbook wbook = new HSSFWorkbook();
        for (final TableView<? extends Object> tableView : listaTablas) {

            ExcelSheetWriterObj.crearLibroConDatosDeTabla(tableView, wbook);
        }
        ExcelSheetWriterObj.crearDocExcel(wbook);
    }

    public static void convertirDatosALibroDeExcel(TableView<? extends Object> tabla) {
        final Workbook wbook = new HSSFWorkbook();
        ExcelSheetWriterObj.crearLibroConDatosDeTabla(tabla, wbook);
        ExcelSheetWriterObj.crearDocExcel(wbook);
    }

    public static void convertirDatosALibroDeExcel(TreeTableView<? extends Object> tabla) {
        final Workbook wbook = new HSSFWorkbook();
        tabla.getRoot().setExpanded(true);
        ExcelSheetWriterObj.crearLibroConDatosDeTabla(tabla, wbook);
        ExcelSheetWriterObj.crearDocExcel(wbook);
    }

    public static void convertirDatosColumnasDoblesALibroDeExcel(List<TableView<? extends Object>> listaTablas) {
        final Workbook wbook = new HSSFWorkbook();
        for (final TableView<? extends Object> tableView : listaTablas) {

            ExcelSheetWriterObj.crearLibroConDatosDeTablaColumnasDobles(tableView, wbook);
        }
        ExcelSheetWriterObj.crearDocExcel(wbook);
    }

    private static void crearDatosHeader(TableView<? extends Object> tabla, Workbook wbook) {
        String id = tabla.getId();
        if (id.length() > ExcelSheetWriterObj.SHEET_NAME_LENGTH) {
            id = id.substring(0, ExcelSheetWriterObj.SHEET_NAME_LENGTH);
        }
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

    private static void crearDatosHeader(TreeTableView<? extends Object> tabla, Workbook wbook) {

        String id = tabla.getId();
        if (id.length() > ExcelSheetWriterObj.SHEET_NAME_LENGTH) {
            id = id.substring(0, ExcelSheetWriterObj.SHEET_NAME_LENGTH);
        }
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

    private static void crearDatosHeaderColumnasDobles(TableView<? extends Object> tabla, Workbook wbook) {

        String id = tabla.getId();
        if (id.length() > ExcelSheetWriterObj.SHEET_NAME_LENGTH) {
            id = id.substring(0, ExcelSheetWriterObj.SHEET_NAME_LENGTH);
        }
        final Sheet sheet1 = wbook.createSheet(id);
        System.out.println("# Sheets=" + wbook.getNumberOfSheets() + " del wb=" + wbook + " de la hoja:" + sheet1);
        final HSSFCellStyle style = (HSSFCellStyle) wbook.createCellStyle();

        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setAlignment(CellStyle.VERTICAL_CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);

        final Row filaTitulo = sheet1.createRow(0);
        final Row filaSubtitulo = sheet1.createRow(1);
        int idx = 0;
        for (int indice = 0; indice < tabla.getColumns().size(); indice++) {
            final TableColumn<? extends Object, ?> columna = tabla.getColumns().get(indice);
            if (columna.getColumns() != null && !columna.getColumns().isEmpty()) {
                for (int n = 0; n < columna.getColumns().size(); n++) {
                    Cell cell = filaTitulo.createCell(idx);
                    cell.setCellValue("");
                    if (n == 0) {
                        cell.setCellValue(tabla.getColumns().get(indice).getText());
                    }
                    cell.setCellStyle(style);

                    cell = filaSubtitulo.createCell(idx);
                    cell.setCellValue(columna.getColumns().get(n).getText());
                    cell.setCellStyle(style);
                    idx++;
                }

                sheet1.addMergedRegion(new CellRangeAddress(0, 0, (indice - 1) * 4 + 1, (indice - 1) * 4 + 4));

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

    /**
     * Metodo que permite crear los datos en la tabla.
     *
     * @param modelo
     *            modelo de tabla.
     * @param wbook
     *            libro de excel.
     * @return workbook libro de excel modificado.
     */
    private static void crearDatosTabla(TableView<? extends Object> tabla, final Workbook wbook) {
        String id = tabla.getId();
        if (id.length() > ExcelSheetWriterObj.SHEET_NAME_LENGTH) {
            id = id.substring(0, ExcelSheetWriterObj.SHEET_NAME_LENGTH);
        }

        final Sheet sheet1 = wbook.getSheet(id);

        for (int indiceFila = 0; indiceFila < tabla.getItems().size(); indiceFila++) {
            final Row fila = sheet1.createRow(indiceFila + 1);
            ExcelSheetWriterObj.recorrerColumnas(tabla, indiceFila, fila);
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
    private static void crearDatosTabla(TreeTableView<? extends Object> tabla, final Workbook wbook) {
        String id = tabla.getId();
        if (id.length() > ExcelSheetWriterObj.SHEET_NAME_LENGTH) {
            id = id.substring(0, ExcelSheetWriterObj.SHEET_NAME_LENGTH);
        }
        final Sheet sheet1 = wbook.getSheet(id);

        int indiceFila = 0;
        while (tabla.getTreeItem(indiceFila) != null) {
            tabla.getTreeItem(indiceFila).setExpanded(true);
            final Row fila = sheet1.createRow(indiceFila + 1);
            ExcelSheetWriterObj.recorrerColumnas(tabla, indiceFila, fila);
            indiceFila++;
        }
    }

    private static void crearDatosTablaColumnasDobles(TableView<? extends Object> tabla, Workbook wbook) {
        String id = tabla.getId();
        if (id.length() > ExcelSheetWriterObj.SHEET_NAME_LENGTH) {
            id = id.substring(0, ExcelSheetWriterObj.SHEET_NAME_LENGTH);
        }
        final Sheet sheet1 = wbook.getSheet(id);

        for (int indiceFila = 0; indiceFila < tabla.getItems().size(); indiceFila++) {
            final Row fila = sheet1.createRow(indiceFila + 2);
            ExcelSheetWriterObj.recorrerColumnasDobles(tabla, indiceFila, fila);
        }
    }

    public static void crearDocExcel(final Workbook wbWork) {
        final long time = Calendar.getInstance().getTimeInMillis();
        final String nombreDoc = "wb" + time + ".xls";
        final String path = Utils.getDefaultDirectory().toString() + File.separator + nombreDoc;

        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(path);
            wbWork.write(fileOut);
        } catch (final IOException e) {
            final Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Problemas al grabar");
            alert.setHeaderText("No se pudo grabar archivo");
            alert.setContentText("Nombre:" + path);
            alert.showAndWait();
        }

        finally {
            try {
                fileOut.close();
            } catch (final IOException e) {
                final Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Problemas al cerrar archivo");
                alert.setHeaderText("No se pudo cerrar archivo");
                alert.setContentText("Nombre:" + path);
                alert.showAndWait();
            }
        }
        ExcelSheetWriterObj.mostrarDocumentoExcel(path);
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
    private static void crearLibroConDatosDeTabla(final TableView<? extends Object> tabla, Workbook wbook) {
        ExcelSheetWriterObj.crearDatosHeader(tabla, wbook);
        ExcelSheetWriterObj.crearDatosTabla(tabla, wbook);
    }

    private static void crearLibroConDatosDeTabla(final TreeTableView<? extends Object> tabla, Workbook wbook) {
        ExcelSheetWriterObj.crearDatosHeader(tabla, wbook);
        ExcelSheetWriterObj.crearDatosTabla(tabla, wbook);
    }

    private static void crearLibroConDatosDeTablaColumnasDobles(final TableView<? extends Object> tabla,
            Workbook wbook) {
        ExcelSheetWriterObj.crearDatosHeaderColumnasDobles(tabla, wbook);
        ExcelSheetWriterObj.crearDatosTablaColumnasDobles(tabla, wbook);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void generarReporteComparativoColegioEjeHabilidadCurso(final TableView<? extends Object> tabla,
            String colegio) {

        final Workbook wbook = new HSSFWorkbook();

        String id = tabla.getId();
        if (id.length() > ExcelSheetWriterObj.SHEET_NAME_LENGTH) {
            id = id.substring(0, ExcelSheetWriterObj.SHEET_NAME_LENGTH);
        }
        final Sheet sheet = wbook.createSheet(id);

        Row header = sheet.createRow(0);
        header.setHeightInPoints(sheet.getDefaultRowHeightInPoints());
        Cell cell = header.createCell(0);
        cell.setCellValue("COMPARATIVO COLEGIO EJE Y HABILIDADES POR CURSO");
        ExcelSheetWriterObj.applySheetTitleStyle(cell);
        header.setHeightInPoints(40);
        header = sheet.createRow(1);
        cell = header.createCell(0);
        ExcelSheetWriterObj.applySheetSubTitleStyle(cell);
        cell.setCellValue(colegio);
        header.setHeightInPoints(ExcelSheetWriterObj.SHEET_NAME_LENGTH);

        final Row header1 = sheet.createRow(2);
        final Row header2 = sheet.createRow(3);
        // Estableciendo los titulos.
        int indice = 0;
        for (final TableColumn tc : tabla.getColumns()) {
            if (tc.getColumns() != null && !tc.getColumns().isEmpty()) {
                boolean first = true;
                for (final Object obj : tc.getColumns()) {
                    final Cell cell1 = header1.createCell(indice);
                    ExcelSheetWriterObj.applyTitleStyle(cell1);
                    final Cell cell2 = header2.createCell(indice);
                    ExcelSheetWriterObj.applyTitleStyle(cell2);
                    if (first) {
                        cell1.setCellValue(tc.getText());
                        first = false;
                    }
                    final TableColumn tcI = (TableColumn) obj;
                    cell2.setCellValue(tcI.getText());
                    indice++;
                }
                sheet.addMergedRegion(new CellRangeAddress(2, 2, indice - 3, indice - 1));
            } else {
                final Cell cell1 = header1.createCell(indice);
                ExcelSheetWriterObj.applyTitleStyle(cell1);
                final Cell cell2 = header2.createCell(indice);
                ExcelSheetWriterObj.applyTitleStyle(cell2);
                cell2.setCellValue(tc.getText());
                indice++;
            }
        }

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, indice - 1));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, indice - 1));

        indice = 4;
        final ObservableList<? extends Object> list = tabla.getItems();
        for (final Object row : list) {
            final Row register = sheet.createRow(indice++);
            final ObservableList<String> itemsRow = (ObservableList<String>) row;
            int colIndex = 0;
            for (final String cellValue : itemsRow) {
                cell = register.createCell(colIndex++);
                if (colIndex > 1) {
                    ExcelSheetWriterObj.applyNormalStyle(cell);
                } else {
                    ExcelSheetWriterObj.applyLeftFirstColumnStyle(cell);
                }
                cell.setCellValue(cellValue);
            }
        }
        sheet.autoSizeColumn(0);
        ExcelSheetWriterObj.crearDocExcel(wbook);
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
            Runtime.getRuntime().exec("cmd /c start \"\" \"" + archivo.getAbsolutePath() + "\"");
        } catch (final Exception e) {
            e.getMessage();
        }
        archivo.deleteOnExit();
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
    private static void recorrerColumnas(final TableView<? extends Object> tabla, final int indiceFila,
            final Row fila) {
        for (int indiceColumna = 0; indiceColumna < tabla.getColumns().size(); indiceColumna++) {

            final Cell cell = fila.createCell(indiceColumna);
            final TableColumn<? extends Object, ?> valores = tabla.getColumns().get(indiceColumna);
            final Object valor = valores.getCellData(indiceFila);
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
            } else if (valor instanceof ImageIcon || valor == null || valor instanceof Color) {
                cell.setCellValue("");
            } else {
                cell.setCellValue(valor.toString());
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
    private static void recorrerColumnas(final TreeTableView<? extends Object> tabla, final int indiceFila,
            final Row fila) {
        for (int indiceColumna = 0; indiceColumna < tabla.getColumns().size(); indiceColumna++) {

            final Cell cell = fila.createCell(indiceColumna);
            final TreeTableColumn<? extends Object, ?> valores = tabla.getColumns().get(indiceColumna);
            final Object valor = valores.getCellData(indiceFila);
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
            } else if (valor instanceof ImageIcon || valor == null || valor instanceof Color) {
                cell.setCellValue("");
            } else {
                cell.setCellValue(valor.toString());
            }
        }
    }

    private static void recorrerColumnasDobles(final TableView<? extends Object> tabla, final int indiceFila,
            final Row fila) {

        int idx = 0;
        for (int indiceColumna = 0; indiceColumna < tabla.getColumns().size(); indiceColumna++) {

            final TableColumn<? extends Object, ?> valores = tabla.getColumns().get(indiceColumna);
            if (valores.getColumns() != null && !valores.getColumns().isEmpty()) {
                for (int n = 0; n < valores.getColumns().size(); n++) {
                    final TableColumn<? extends Object, ?> values = valores.getColumns().get(n);
                    final Object valor = values.getCellData(indiceFila);
                    final Cell cell = fila.createCell(idx);
                    final CellStyle style = cell.getCellStyle();
                    style.setAlignment(CellStyle.ALIGN_CENTER);
                    style.setAlignment(CellStyle.VERTICAL_CENTER);
                    ExcelSheetWriterObj.setValueToCell(valor, cell);
                    cell.setCellStyle(style);
                    idx++;
                }
            } else {
                final Object valor = valores.getCellData(indiceFila);
                final Cell cell = fila.createCell(idx);
                ExcelSheetWriterObj.setValueToCell(valor, cell);
                final CellStyle style = cell.getCellStyle();
                style.setAlignment(CellStyle.ALIGN_CENTER);
                style.setAlignment(CellStyle.VERTICAL_CENTER);
                ExcelSheetWriterObj.setValueToCell(valor, cell);
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
        } else if (valor instanceof ImageIcon || valor == null || valor instanceof Color) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(valor.toString());
        }

    }

    /**
     * Constructor de la clase.
     */
    private ExcelSheetWriterObj() {
        // Sin implementar.
    }

}
