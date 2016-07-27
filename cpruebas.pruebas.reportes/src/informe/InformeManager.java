package informe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.TipoAlumno;
import javafx.scene.control.ProgressBar;

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
    ProgressBar progressBar;
    File file;

    public InformeManager(ProgressBar progressBar, File selectedFile) throws FileNotFoundException, IOException {
        
        this.file =  selectedFile;
        doc = new XWPFDocument(new FileInputStream(System.getProperty("user.dir") + "/INFORME_PLANTILLA.dotm"));
        this.progressBar = progressBar;
//        add(new InformeResumenTotalGeneral());
//        add(new InformeResumenTotalAlumnos());
//        add(new InformeResumenPME());
//        add(new InformeEjeEvaluacion());
//        add(new InformeHabilidades());
        add(new InformeEjesXCurso());
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
        paragraph = document.createParagraph();
        paragraph.setPageBreak(true);
    }

    public void processAsignatura(TipoAlumno tipoAlumno, Colegio colegio, Asignatura asignatura) {

        float step = 100f / (float)informes.size();
        progressBar.setProgress(0f);
        generarPaginaAsignatura(doc, asignatura);
        for (Informe informe : informes) {
            informe.execute(tipoAlumno, colegio, asignatura);
            informe.page(doc);
            XWPFParagraph paragraph = doc.createParagraph();
            paragraph.setPageBreak(true);
            progressBar.setProgress(progressBar.getProgress() + step);
        }
    }

    public void finish() {
        try {
            FileOutputStream out = new FileOutputStream(file);
            doc.write(out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
