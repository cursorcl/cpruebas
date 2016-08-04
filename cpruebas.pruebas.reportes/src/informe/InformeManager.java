package informe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.POIXMLProperties;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextAlignment;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTextAlignment;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.TipoAlumno;
import cl.eos.persistence.models.TipoPrueba;
import informe.informes.IInforme;
import informe.informes.imp.InformeEjeEvaluacion;
import informe.informes.imp.InformeEjesXCurso;
import informe.informes.imp.InformeHabilidadXCurso;
import informe.informes.imp.InformeHabilidades;
import informe.informes.imp.InformeResumenPME;
import informe.informes.imp.InformeResumenTotalAlumnos;
import informe.informes.imp.InformeResumenTotalGeneral;

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

    static String FIELD_TIPOPRUEBA = "TipodePrueba";
    static String FIELD_ESTABLECIMIENTO = "Establecimiento";
    static String FIELD_CIUDAD = "Ciudad";
    static String FIELD_ANOESCOLAR = "AnoEscolar";

    boolean sorted = false;
    List<IInforme> informes;
    private XWPFDocument doc;
    File file;

    public InformeManager(File selectedFile) throws FileNotFoundException, IOException {

        this.file = selectedFile;
        doc = new XWPFDocument(new FileInputStream(System.getProperty("user.dir") + "/PAUTA.dotx"));

        add(new InformeResumenTotalGeneral());
        add(new InformeResumenTotalAlumnos());
        add(new InformeResumenPME());
        add(new InformeEjeEvaluacion());
        add(new InformeHabilidades());
        add(new InformeEjesXCurso());
        add(new InformeHabilidadXCurso());

    }

    public void updateFields(TipoPrueba tipoPrueba, Colegio colegio, String anoEscolar) {
        POIXMLProperties props = doc.getProperties();
        POIXMLProperties.CustomProperties cp = props.getCustomProperties();
        if (cp != null) {
            List<CTProperty> ctProperties = cp.getUnderlyingProperties().getPropertyList();
            for (CTProperty ctp : ctProperties) {
                if (ctp.getName().equals(FIELD_TIPOPRUEBA)) {
                    ctp.setLpwstr(tipoPrueba.getName().toUpperCase().trim());
                } else if (ctp.getName().equals(FIELD_ESTABLECIMIENTO)) {
                    ctp.setLpwstr(colegio.getName().toUpperCase().trim());
                } else if (ctp.getName().equals(FIELD_CIUDAD)) {

                    String ciudad = colegio.getCiudad() == null ? "-----" : colegio.getCiudad();
                    ctp.setLpwstr(ciudad.toUpperCase().trim());
                } else if (ctp.getName().equals(FIELD_ANOESCOLAR)) {
                    ctp.setLpwstr(anoEscolar.toUpperCase().trim());
                }

            }
            doc.enforceUpdateFields();
        }
    }

    public void add(IInforme informe) {
        if (informes == null) {
            informes = new ArrayList<IInforme>();
        }
        informes.add(informe);
        sorted = false;
    }

    public void remove(IInforme informe) {
        if (informes == null)
            return;
        informes.remove(informe);
    }

    public List<IInforme> getInformes() {
        if (informes == null)
            return null;
        return informes;
    }

    protected void generarPaginaAsignatura(XWPFDocument document, Asignatura asignatura) {

        CTDocument1 lDoc = document.getDocument();
        CTBody body = lDoc.getBody();
        CTSectPr section = body.addNewSectPr();
        CTVerticalJc textAlignment = CTVerticalJc.Factory.newInstance();
        textAlignment.setVal(STVerticalJc.CENTER);
        section.setVAlign(textAlignment);
        
        XWPFParagraph para = document.createParagraph();
        CTP ctp = para.getCTP();
        CTPPr br = ctp.addNewPPr();
        CTTextAlignment txtAlign = br.addNewTextAlignment();
        txtAlign.setVal(STTextAlignment.CENTER);
        para.setVerticalAlignment(TextAlignment.BOTTOM);
        br.setSectPr(section);

        para = document.createParagraph();
        
        XWPFRun run = para.createRun();
        run.setText(asignatura.getName());
        run.addCarriageReturn();
        para.setPageBreak(true);
        
        para = document.createParagraph();
        ctp = para.getCTP();
        br = ctp.addNewPPr();
        br.setSectPr(section);
        textAlignment = CTVerticalJc.Factory.newInstance();
        textAlignment.setVal(STVerticalJc.TOP);
        section.setVAlign(textAlignment);

    }

    public void processAsignatura(TipoAlumno tipoAlumno, Colegio colegio, Asignatura asignatura) {

        generarPaginaAsignatura(doc, asignatura);
        for (IInforme informe : informes) {
            informe.execute(tipoAlumno, colegio, asignatura);
            informe.page(doc);
            XWPFParagraph paragraph = doc.createParagraph();
            paragraph.setPageBreak(true);
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
