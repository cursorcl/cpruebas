package informe;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.TipoAlumno;

/**
 * Se deben inscribir los informes que se han de generar.
 * 
 * Cada informe debe tener indicado el número de sección al que pertenece con el
 * fin de poder ordenarlos.
 * 
 * @author curso
 *
 */
public class InformeManager {

    boolean sorted = false;
    List<Informe> informes;
    private XWPFDocument doc;

    public InformeManager() throws FileNotFoundException, IOException {
        // debo leer los informes desde alguna parte o inscribirlos aque
        doc = new XWPFDocument(new FileInputStream(System.getProperty("user.dir") + "/INFORME_PLANTILLA.dotm"));
        add(new InformeResumen());
    }

    public void add(Informe informe) {
        if (informes == null) {
            informes = new ArrayList<Informe>();
        }
        informes.add(informe);
        sorted = false;
    }

    public void remove(Informe informe) {
        if (informes == null)
            return;
        informes.remove(informe);
    }

    public List<Informe> getInformes() {
        if (informes == null)
            return null;
        return informes;
    }

    protected void generarPaginaAsignatura(XWPFDocument document, Asignatura asignatura) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = paragraph.createRun();
        run.setText(asignatura.getName());
        run.addCarriageReturn();
        run.addBreak(BreakType.PAGE);
        run.addBreak(BreakType.TEXT_WRAPPING); // cancels effect of page break
    }

    public void processAsignatura(TipoAlumno tipoAlumno, Colegio colegio, Asignatura asignatura) {

        generarPaginaAsignatura(doc, asignatura);
        for (Informe informe : informes) {
            informe.execute(tipoAlumno, colegio, asignatura);
            informe.page(doc);
        }
    }

    public void finish() {
        try {

            FileOutputStream out = new FileOutputStream(System.getProperty("user.dir") + "/Informe.docx");
            doc.write(out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
