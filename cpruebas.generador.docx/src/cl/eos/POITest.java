package cl.eos;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class POITest {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        final XWPFDocument doc = new XWPFDocument(
                new FileInputStream(System.getProperty("user.dir") + "/dd/INFORME_PLANTILLA.dotm"));

        final FileOutputStream out = new FileOutputStream(System.getProperty("user.dir") + "/dd/simpleTable.docx");
        doc.write(out);
        out.close();
    }
}
