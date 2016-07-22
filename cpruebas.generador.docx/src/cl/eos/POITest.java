package cl.eos;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;

public class POITest 
{
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        XWPFDocument doc = new XWPFDocument(new FileInputStream(System.getProperty("user.dir") + "/dd/INFORME_PLANTILLA.dotm"));

        

        FileOutputStream out = new FileOutputStream(System.getProperty("user.dir") + "/dd/simpleTable.docx");
        doc.write(out);
        out.close();
    }
}
