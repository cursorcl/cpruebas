package utils;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

public class WordUtil {

    public static void addImage(File file, String title, XWPFParagraph paragraph)
            throws InvalidFormatException, IOException {
        WordUtil.addImage(file, title, paragraph, 350, 200);
    }

    public static void addImage(File file, String title, XWPFParagraph paragraph, int width, int height)
            throws InvalidFormatException, IOException {
        final XWPFRun run = paragraph.createRun();
        paragraph.setAlignment(ParagraphAlignment.CENTER);

        final FileInputStream is = new FileInputStream(file);
        run.addBreak();
        run.addPicture(is, Document.PICTURE_TYPE_PNG, title, Units.toEMU(width), Units.toEMU(height)); // 300x150
                                                                                                       // pixels
        is.close();
    }

    public static void mergeCellHorizontally(XWPFTable table, int row, int fromCol, int toCol) {
        for (int colIndex = fromCol; colIndex <= toCol; colIndex++) {
            final CTHMerge hmerge = CTHMerge.Factory.newInstance();
            if (colIndex == fromCol) {
                // The first merged cell is set with RESTART merge value
                hmerge.setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                hmerge.setVal(STMerge.CONTINUE);
            }
            final XWPFTableCell cell = table.getRow(row).getCell(colIndex);
            // Try getting the TcPr. Not simply setting an new one every time.
            CTTcPr tcPr = cell.getCTTc().getTcPr();
            if (tcPr != null) {
                tcPr.setHMerge(hmerge);
            } else {
                // only set an new TcPr if there is not one already
                tcPr = CTTcPr.Factory.newInstance();
                tcPr.setHMerge(hmerge);
                cell.getCTTc().setTcPr(tcPr);
            }
        }
    }

    public static void mergeCellVertically(XWPFTable table, int col, int fromRow, int toRow) {
        for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
            final CTVMerge vmerge = CTVMerge.Factory.newInstance();
            if (rowIndex == fromRow) {
                // The first merged cell is set with RESTART merge value
                vmerge.setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                vmerge.setVal(STMerge.CONTINUE);
            }
            final XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
            // Try getting the TcPr. Not simply setting an new one every time.
            CTTcPr tcPr = cell.getCTTc().getTcPr();
            if (tcPr != null) {
                tcPr.setVMerge(vmerge);
            } else {
                // only set an new TcPr if there is not one already
                tcPr = CTTcPr.Factory.newInstance();
                tcPr.setVMerge(vmerge);
                cell.getCTTc().setTcPr(tcPr);
            }
        }
    }

    public static void setAlignment(XWPFTable table, int row, ParagraphAlignment alignment) {
        final XWPFTableRow tRow = table.getRow(row);

        final List<XWPFTableCell> cells = tRow.getTableCells();
        for (final XWPFTableCell cell : cells) {
            final XWPFParagraph para = cell.getParagraphs().get(0);
            para.setAlignment(alignment);
        }
    }

    public static void setAlignmentCell(XWPFTableCell cell, String text, ParagraphAlignment alignment) {
        final XWPFParagraph para = cell.getParagraphs().get(0);
        final XWPFRun rh = para.createRun();
        para.setAlignment(alignment);
        rh.setText(text);
    }

    public static void setBackgroundColor(XWPFTable table, int fr, int lr, int fc, int lc, String color) {
        for (int r = fr; r <= lr; r++) {
            final XWPFTableRow tRow = table.getRow(r);
            for (int c = fc; c < lc; c++) {
                final XWPFTableCell cell = tRow.getCell(c);
                final CTTcPr cellPropertie = cell.getCTTc().addNewTcPr();

                final CTShd ctshd = cellPropertie.addNewShd();
                ctshd.setColor("auto");
                ctshd.setVal(STShd.CLEAR);
                ctshd.setFill(color);
                final XWPFParagraph para = cell.getParagraphs().get(0);
                // create a run to contain the content
                final XWPFRun rh = para.createRun();
                rh.setFontSize(10);
                rh.setFontFamily("Courier");
                para.setAlignment(ParagraphAlignment.CENTER);
            }
        }
    }

    public static void setBackgroundColor(XWPFTable table, int row, String color) {
        final XWPFTableRow tRow = table.getRow(row);

        final List<XWPFTableCell> cells = tRow.getTableCells();
        for (final XWPFTableCell cell : cells) {
            final CTTcPr cellPropertie = cell.getCTTc().addNewTcPr();

            final CTShd ctshd = cellPropertie.addNewShd();
            ctshd.setColor("auto");
            ctshd.setVal(STShd.CLEAR);
            ctshd.setFill(color);
            final XWPFParagraph para = cell.getParagraphs().get(0);
            // create a run to contain the content
            para.setAlignment(ParagraphAlignment.CENTER);
        }
    }

    public static void setCenterText(XWPFTableCell cell, String text) {
        WordUtil.setAlignmentCell(cell, text, ParagraphAlignment.CENTER);
    }

    public static void setFont(XWPFTable table, int row, Font font) {
        final XWPFTableRow tRow = table.getRow(row);

        final List<XWPFTableCell> cells = tRow.getTableCells();
        for (final XWPFTableCell cell : cells) {
            final XWPFParagraph para = cell.getParagraphs().get(0);
            final XWPFRun rh = para.createRun();
            rh.setFontSize(font.getSize());
            rh.setFontFamily(font.getFamily());
        }
    }

    public static void setLeftText(XWPFTableCell cell, String text) {
        WordUtil.setAlignmentCell(cell, text, ParagraphAlignment.LEFT);
    }

    public static void setRightText(XWPFTableCell cell, String text) {
        WordUtil.setAlignmentCell(cell, text, ParagraphAlignment.RIGHT);
    }

    public static void setTableAlignment(XWPFTable table, STJc.Enum justification) {
        final CTTblPr tblPr = table.getCTTbl().getTblPr();
        final CTJc jc = tblPr.isSetJc() ? tblPr.getJc() : tblPr.addNewJc();
        jc.setVal(justification);
    }

    public static void setTableFormat(XWPFTable table) {
        WordUtil.setTableFormat(table, 1, 0);
    }

    public static void setTableFormat(XWPFTable table, int nroTitleRows, int nroResultRows) {
        WordUtil.setTableAlignment(table, STJc.Enum.forInt(2));
        final List<XWPFTableRow> rows = table.getRows();
        final int nroRows = table.getRows().size();
        int rowCt = 0;

        for (final XWPFTableRow row : rows) {
            final CTTrPr rowProperties = row.getCtRow().addNewTrPr();
            final CTHeight rowHeight = rowProperties.addNewTrHeight();
            rowHeight.setVal(BigInteger.valueOf(360));
            final List<XWPFTableCell> cells = row.getTableCells();
            for (final XWPFTableCell cell : cells) {
                final CTTcPr cellPropertie = cell.getCTTc().addNewTcPr();
                final CTVerticalJc verticalCell = cellPropertie.addNewVAlign();
                verticalCell.setVal(STVerticalJc.CENTER);

                final CTShd ctshd = cellPropertie.addNewShd();
                ctshd.setColor("auto");
                ctshd.setVal(STShd.CLEAR);

                final XWPFParagraph para = cell.getParagraphs().get(0);
                final XWPFRun rh = para.createRun();
                rh.setFontFamily("Courier");
                para.setAlignment(ParagraphAlignment.CENTER);

                if (rowCt < nroTitleRows) {
                    ctshd.setFill("A7BFDE");
                    rh.setFontSize(9);
                    rh.setBold(true);
                    rh.setItalic(true);
                } else if (nroResultRows > 0 && rowCt == nroRows - nroResultRows) {
                    ctshd.setFill("A7BFDE");
                    rh.setFontSize(9);
                    rh.setBold(true);
                    rh.setItalic(true);
                } else if (rowCt % 2 == 0) {
                    ctshd.setFill("D3DFEE");
                    rh.setFontSize(8);
                    rh.setBold(false);
                    rh.setItalic(false);

                } else {
                    ctshd.setFill("EDF2F8");
                    rh.setFontSize(8);
                    rh.setBold(false);
                    rh.setItalic(false);
                }
            }
            rowCt++;
        }
    }

    public static void setTitleStyle(XWPFTableCell cell) {
        cell.setColor("777777");
        cell.removeParagraph(0);
        final XWPFParagraph pgraph = cell.addParagraph();
        pgraph.setAlignment(ParagraphAlignment.CENTER);
        final XWPFRun lRun = pgraph.createRun();
        lRun.setFontSize(10);
    }
}
