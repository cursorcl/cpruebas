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
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

import cl.eos.imp.view.UtilsAlert;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.TipoAlumno;
import informe.informes.IInforme;
import informe.informes.imp.InformeEjeEvaluacion;
import informe.informes.imp.InformeEjesXCurso;
import informe.informes.imp.InformeHabilidadXCurso;
import informe.informes.imp.InformeHabilidades;
import informe.informes.imp.InformeResumenPME;
import informe.informes.imp.InformeResumenTotalAlumnos;
import informe.informes.imp.InformeResumenTotalGeneral;
import informe.informes.imp.InformeXObjetivos_Nivel;
import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * Se deben inscribir los informes que se han de generar.
 *
 * Cada informe debe tener indicado el número de sección al que pertenece con el
 * fin de poder ordenarlos.
 *
 * @author colegio
 *
 */
public class InformeManager {

    static String FIELD_TIPOPRUEBA = "TipoPrueba";
    static String FIELD_ESTABLECIMIENTO = "Establecimiento";
    static String FIELD_CIUDAD = "Ciudad";
    static String FIELD_ANOESCOLAR = "AnoEscolar";

    public static int CICLO_1 = 1;
    public static int CICLO_2 = 2;
    public static int CICLO_3 = 3;
    public static int CICLO_4 = 4;
    public static int CICLO_5 = 5;
    public static int CICLO_6 = 6;
    public static int CICLO_7 = 7;
    public static int CICLO_8 = 8;

    public static int TABLA = 1;

    boolean sorted = false;
    List<IInforme> informes;
    private final XWPFDocument doc;
    File file;
    Colegio colegio;

    public InformeManager(Colegio colegio, File selectedFile) throws FileNotFoundException, IOException {

        file = selectedFile;
        this.colegio = colegio;
        doc = new XWPFDocument(new FileInputStream(System.getProperty("user.dir") + "/res/PAUTA.dotx"));

        add(new InformeResumenTotalGeneral());
        add(new InformeResumenTotalAlumnos());
        add(new InformeResumenPME());
        add(new InformeEjeEvaluacion());
        add(new InformeHabilidades());
        add(new InformeEjesXCurso());
        add(new InformeHabilidadXCurso());
        add(new InformeXObjetivos_Nivel());

    }

    public void add(IInforme informe) {
        if (informes == null) {
            informes = new ArrayList<IInforme>();
        }
        informes.add(informe);
        sorted = false;
    }

    public void finish() {
        try {

            final FileOutputStream out = new FileOutputStream(file);
            doc.write(out);
            out.close();
        } catch (final IOException e) {
            Platform.runLater(() -> {
                final Alert dlg = UtilsAlert.makeExceptionAlert("No se ha podido grabar el archivo.",
                        "Debe generalo nuevamente", e);
                dlg.show();
            });
        }
    }

    protected void generarPaginaAsignatura(XWPFDocument document, Asignatura asignatura) {

        final CTDocument1 lDoc = document.getDocument();
        final CTBody body = lDoc.getBody();

        // Establece el estilo a la sección que ya existe.
        XWPFParagraph para = document.createParagraph();
        CTP ctp = para.getCTP();
        CTPPr paragraphProperties = ctp.addNewPPr();
        CTSectPr section = body.addNewSectPr();
        CTVerticalJc textAlignment = CTVerticalJc.Factory.newInstance();
        textAlignment.setVal(STVerticalJc.TOP);
        section.setVAlign(textAlignment);
        paragraphProperties.setSectPr(section);

        // Se escribe el nuevo párrafo
        para = document.createParagraph();
        para.setAlignment(ParagraphAlignment.CENTER);
        final XWPFRun run = para.createRun();
        run.setFontSize(20);
        run.setText(asignatura.getName().toUpperCase());
        run.addCarriageReturn();
        run.setText(colegio.getName().toUpperCase());
        run.addCarriageReturn();
        // para.setPageBreak(true);

        // Se asigna la nueva sección a los párrafos anteriores
        para = document.createParagraph();
        ctp = para.getCTP();
        paragraphProperties = ctp.addNewPPr();
        section = body.addNewSectPr();
        textAlignment = CTVerticalJc.Factory.newInstance();
        textAlignment.setVal(STVerticalJc.CENTER);
        section.setVAlign(textAlignment);
        paragraphProperties.setSectPr(section);

        // Se crea nueva sección como las primeras.
        // para = document.createParagraph();
        // ctp = para.getCTP();
        // paragraphProperties = ctp.addNewPPr();
        // section = body.addNewSectPr();
        // textAlignment = CTVerticalJc.Factory.newInstance();
        // textAlignment.setVal(STVerticalJc.TOP);
        // section.setVAlign(textAlignment);
        // paragraphProperties.setSectPr(section);

    }

    public List<IInforme> getInformes() {
        if (informes == null)
            return null;
        return informes;
    }

    public void processAsignatura(TipoAlumno tipoAlumno, Colegio colegio, Asignatura asignatura) {

        generarPaginaAsignatura(doc, asignatura);
        for (final IInforme informe : informes) {
            informe.execute(tipoAlumno, colegio, asignatura);
            informe.page(doc);
            informe.graph(doc);
            final XWPFParagraph paragraph = doc.createParagraph();
            paragraph.setPageBreak(true);
        }
    }

    public void remove(IInforme informe) {
        if (informes == null)
            return;
        informes.remove(informe);
    }

    public void updateFields(String tipoPrueba, String anoEscolar) {
        final POIXMLProperties props = doc.getProperties();
        final POIXMLProperties.CustomProperties cp = props.getCustomProperties();
        if (cp != null) {
            final List<CTProperty> ctProperties = cp.getUnderlyingProperties().getPropertyList();
            for (final CTProperty ctp : ctProperties) {
                if (ctp.getName().equalsIgnoreCase(InformeManager.FIELD_TIPOPRUEBA)) {
                    ctp.setLpwstr(tipoPrueba.toUpperCase().trim());
                } else if (ctp.getName().equalsIgnoreCase(InformeManager.FIELD_ESTABLECIMIENTO)) {
                    ctp.setLpwstr(colegio.getName().toUpperCase().trim());
                } else if (ctp.getName().equalsIgnoreCase(InformeManager.FIELD_CIUDAD)) {
                    final String ciudad = colegio.getCiudad() == null ? "-----" : colegio.getCiudad();
                    ctp.setLpwstr(ciudad.toUpperCase().trim());
                } else if (ctp.getName().equalsIgnoreCase(InformeManager.FIELD_ANOESCOLAR)) {
                    ctp.setLpwstr(anoEscolar.toUpperCase().trim());
                }

            }
            doc.enforceUpdateFields();
        }
    }

}
