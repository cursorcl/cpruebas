package cpruebas.pruebas.reportes;

import java.io.FileOutputStream;
import java.math.BigInteger;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

public class CreateWordTableMerge {

    public static void main(String[] args) throws Exception {

        final XWPFDocument document = new XWPFDocument();

        XWPFParagraph paragraph = document.createParagraph();
        final XWPFRun run = paragraph.createRun();
        run.setText("The table:");

        // create table
        final XWPFTable table = document.createTable(3, 5);

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 5; col++) {
                table.getRow(row).getCell(col).setText("row " + row + ", col " + col);
            }
        }

        // create and set column widths for all columns in all rows
        // most examples don't set the type of the CTTblWidth but this
        // is necessary for working in all office versions
        for (int col = 0; col < 5; col++) {
            final CTTblWidth tblWidth = CTTblWidth.Factory.newInstance();
            tblWidth.setW(BigInteger.valueOf(1000));
            tblWidth.setType(STTblWidth.DXA);
            for (int row = 0; row < 3; row++) {
                CTTcPr tcPr = table.getRow(row).getCell(col).getCTTc().getTcPr();
                if (tcPr != null) {
                    tcPr.setTcW(tblWidth);
                } else {
                    tcPr = CTTcPr.Factory.newInstance();
                    tcPr.setTcW(tblWidth);
                    table.getRow(row).getCell(col).getCTTc().setTcPr(tcPr);
                }
            }
        }

        // using the merge methods
        CreateWordTableMerge.mergeCellVertically(table, 0, 0, 1);
        CreateWordTableMerge.mergeCellHorizontally(table, 1, 2, 3);
        CreateWordTableMerge.mergeCellHorizontally(table, 2, 1, 4);

        paragraph = document.createParagraph();

        final FileOutputStream out = new FileOutputStream(System.getProperty("user.dir") + "/create_table.docx");
        document.write(out);

        System.out.println("create_table.docx written successully");
    }

    static void mergeCellHorizontally(XWPFTable table, int row, int fromCol, int toCol) {
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

    static void mergeCellVertically(XWPFTable table, int col, int fromRow, int toRow) {
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
}