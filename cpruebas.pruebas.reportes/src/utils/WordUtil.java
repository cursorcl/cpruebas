package utils;

import java.awt.Font;
import java.math.BigInteger;
import java.util.List;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
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

    public static void mergeCellVertically(XWPFTable table, int col, int fromRow, int toRow) {
        for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
            CTVMerge vmerge = CTVMerge.Factory.newInstance();
            if (rowIndex == fromRow) {
                // The first merged cell is set with RESTART merge value
                vmerge.setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                vmerge.setVal(STMerge.CONTINUE);
            }
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
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

    public static void mergeCellHorizontally(XWPFTable table, int row, int fromCol, int toCol) {
        for (int colIndex = fromCol; colIndex <= toCol; colIndex++) {
            CTHMerge hmerge = CTHMerge.Factory.newInstance();
            if (colIndex == fromCol) {
                // The first merged cell is set with RESTART merge value
                hmerge.setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                hmerge.setVal(STMerge.CONTINUE);
            }
            XWPFTableCell cell = table.getRow(row).getCell(colIndex);
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

    public static void setTitleStyle(XWPFTableCell cell) {
        cell.setColor("777777");
        cell.removeParagraph(0);
        XWPFParagraph pgraph = cell.addParagraph();
        pgraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun lRun = pgraph.createRun();
        lRun.setFontSize(10);
    }

    public static void setBackgroundColor(XWPFTable table, int row, String color) {
        XWPFTableRow tRow = table.getRow(row);

        List<XWPFTableCell> cells = tRow.getTableCells();
        for (XWPFTableCell cell : cells) {
            CTTcPr cellPropertie = cell.getCTTc().addNewTcPr();

            CTShd ctshd = cellPropertie.addNewShd();
            ctshd.setColor("auto");
            ctshd.setVal(STShd.CLEAR);
            ctshd.setFill(color);
            XWPFParagraph para = cell.getParagraphs().get(0);
            // create a run to contain the content
            para.setAlignment(ParagraphAlignment.CENTER);
        }
    }

    public static void setAlignment(XWPFTable table, int row, ParagraphAlignment alignment) {
        XWPFTableRow tRow = table.getRow(row);

        List<XWPFTableCell> cells = tRow.getTableCells();
        for (XWPFTableCell cell : cells) {
            XWPFParagraph para = cell.getParagraphs().get(0);
            para.setAlignment(alignment);
        }
    }

    public static void setFont(XWPFTable table, int row, Font font) {
        XWPFTableRow tRow = table.getRow(row);

        List<XWPFTableCell> cells = tRow.getTableCells();
        for (XWPFTableCell cell : cells) {
            XWPFParagraph para = cell.getParagraphs().get(0);
            XWPFRun rh = para.createRun();
            rh.setFontSize(font.getSize());
            rh.setFontFamily(font.getFamily());
        }
    }

    public static void setBackgroundColor(XWPFTable table, int fr, int lr, int fc, int lc, String color) {
        for (int r = fr; r <= lr; r++) {
            XWPFTableRow tRow = table.getRow(r);
            for (int c = fc; c < lc; c++) {
                XWPFTableCell cell = tRow.getCell(c);
                CTTcPr cellPropertie = cell.getCTTc().addNewTcPr();

                CTShd ctshd = cellPropertie.addNewShd();
                ctshd.setColor("auto");
                ctshd.setVal(STShd.CLEAR);
                ctshd.setFill(color);
                XWPFParagraph para = cell.getParagraphs().get(0);
                // create a run to contain the content
                XWPFRun rh = para.createRun();
                rh.setFontSize(10);
                rh.setFontFamily("Courier");
                para.setAlignment(ParagraphAlignment.CENTER);
            }
        }
    }

    public static void setTableFormat(XWPFTable table) {
        setTableFormat(table, 1, 0);
    }

    public static void setTableFormat(XWPFTable table, int nroTitleRows, int nroResultRows) {
        setTableAlignment(table, STJc.Enum.forInt(2));
        List<XWPFTableRow> rows = table.getRows();
        int nroRows = table.getRows().size();
        int rowCt = 0;

        for (XWPFTableRow row : rows) {
            CTTrPr rowProperties = row.getCtRow().addNewTrPr();
            CTHeight rowHeight = rowProperties.addNewTrHeight();
            rowHeight.setVal(BigInteger.valueOf(360));
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                CTTcPr cellPropertie = cell.getCTTc().addNewTcPr();
                CTVerticalJc verticalCell = cellPropertie.addNewVAlign();
                verticalCell.setVal(STVerticalJc.CENTER);

                CTShd ctshd = cellPropertie.addNewShd();
                ctshd.setColor("auto");
                ctshd.setVal(STShd.CLEAR);

                XWPFParagraph para = cell.getParagraphs().get(0);
                XWPFRun rh = para.createRun();
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
    
    public static void setTableAlignment(XWPFTable table, STJc.Enum justification) {
        CTTblPr tblPr = table.getCTTbl().getTblPr();
        CTJc jc = (tblPr.isSetJc() ? tblPr.getJc() : tblPr.addNewJc());
        jc.setVal(justification);
    }
    
    
    public static void setAlignmentCell(XWPFTableCell cell, String text, ParagraphAlignment alignment)
    {
        XWPFParagraph para =  cell.getParagraphs().get(0);
        XWPFRun rh = para.createRun();
        para.setAlignment(alignment);
        rh.setText(text);
    }
    public static void setCenterText(XWPFTableCell cell, String text)
    {
        setAlignmentCell(cell, text, ParagraphAlignment.CENTER);
    }
    public static void setLeftText(XWPFTableCell cell, String text)
    {
        setAlignmentCell(cell, text, ParagraphAlignment.LEFT);
    }
    public static void setRightText(XWPFTableCell cell, String text)
    {
        setAlignmentCell(cell, text, ParagraphAlignment.RIGHT);
    }
    
    
}
