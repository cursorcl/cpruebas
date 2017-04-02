package cl.eos.detection;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.Formas;
import cl.eos.persistence.models.Profesor;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.util.Utils;
import javafx.stage.FileChooser;

public class ImpresionPrueba {

	private Logger log =  Logger.getLogger("Impresion Prueba");
    private static final int RUT_ROW = 74;
    private static final int[] RUT_COLS = { 57, 73, 91, 106, 121, 140, 156, 171, 191 };
    private static final int FORMA_ROW = 66;
    private static final int[] FORMA_COLS = { 229, 244, 259 };
    private static final Point RUT_POINT = new Point(40, 88);

    private static final Point FECHA_POINT = new Point(383, 69);
    private static final Point COLEGIO_POINT = new Point(356, 93);
    private static final Point NOMBRES_POINT = new Point(300, 125);
    private static final Point APELLIDOS_POINT = new Point(300, 158);
    private static final Point PROFESOR_POINT = new Point(300, 189);
    private static final Point ASIGNATURA_POINT = new Point(300, 221);
    private static final Point CURSO_POINT = new Point(475, 221);

    public static void main(String[] args) {
        BufferedImage imageEmpty;
        try {
            imageEmpty = ImageIO.read(new File("./res/cpruebas.vacia.png"));
            final BufferedImage image = new BufferedImage(imageEmpty.getWidth(), imageEmpty.getHeight(),
                    imageEmpty.getType());
            final Graphics2D g2 = (Graphics2D) image.getGraphics();
            final RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHints(rh);
            g2.drawImage(imageEmpty, 0, 0, imageEmpty.getWidth(), imageEmpty.getHeight(), null);
            ImageIO.write(image, "png", new File("./res/patron.png"));
            final PDDocument doc = new PDDocument();
            try {
                final PDPage page = new PDPage();
                doc.addPage(page);
                PDPageContentStream content;
                content = new PDPageContentStream(doc, page, true, true);
                final PDXObjectImage ximage = new PDJpeg(doc, image, 1f);
                ximage.setWidth((int) page.getMediaBox().getWidth());
                ximage.setHeight((int) page.getMediaBox().getHeight());
                content.drawImage(ximage, 0, 0);
                content.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }

            final String fileName = String.format("ejemplo");
            final File output = new File("./res/" + fileName + ".pdf");
            doc.setDocumentInformation(new PDDocumentInformation());
            doc.getDocumentInformation().setTitle(fileName);
            doc.getDocumentInformation().setAuthor("EOS");
            doc.save(output);
        } catch (IOException | COSVisitorException e1) {
            e1.printStackTrace();
        }
    }

    private final int FIRST_ROW = 350;
    private final int FIRST_COL = 55;

    private final int GROUP_SIZE = 5;
    private final int CIRCLE_WIDTH = 10;
    private final int STEP_ROW = CIRCLE_WIDTH + CIRCLE_WIDTH / 3;

    private final int STEP_COL = CIRCLE_WIDTH / 2;

    private final String[] TITLE_LETER = { "A", "B", "C", "D", "E" };
    private final Font LETTERS_FONT = new Font("Courier", Font.PLAIN, 10);

    private final Font OPTIONS_FONT = new Font("Courier", Font.PLAIN, 6);
    private int nroAlternativas;

    private int HEIGHT_FONT;

    private List<RespuestasEsperadasPrueba> respEsperadas = new ArrayList<RespuestasEsperadasPrueba>();
    private int row = FIRST_ROW;
    private int col = FIRST_COL;

    private int nro = 1;
    private int colAlternativas;
    private BufferedImage image;

    private Graphics2D g2;

    public ImpresionPrueba() {

    }

    private void addImageToPdf(BufferedImage image, PDDocument doc) {
        try {
            final PDPage page = new PDPage();
            doc.addPage(page);
            PDPageContentStream content;
            content = new PDPageContentStream(doc, page, true, true);
            final PDXObjectImage ximage = new PDJpeg(doc, image, 1f);
            ximage.setWidth((int) page.getMediaBox().getWidth());
            ximage.setHeight((int) page.getMediaBox().getHeight());
            content.drawImage(ximage, 0, 0);
            content.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }

    }

    public BufferedImage drawAlternativas() throws IOException {
        int n = 1;

        for (final RespuestasEsperadasPrueba resp : respEsperadas) {
            if (n % GROUP_SIZE == 1) {
                if (n % 25 == 1) {
                    row = FIRST_ROW;
                    if (n != 1) {
                        col = col + (STEP_COL + CIRCLE_WIDTH) * (colAlternativas + 2);
                    }
                } else {
                    row += STEP_ROW * 2;
                }
                drawTitle();
                row += STEP_ROW / 2;
                if (n < 25) {
                    drawMarks(row);
                }

            } else {
                row += STEP_ROW;
            }
            if (Boolean.TRUE.equals(resp.getVerdaderoFalso())) {
                // dibujo linea Verdadero falso
                drawVFLine();

            } else if (Boolean.TRUE.equals(resp.getMental())) {
                // Dibujo linea Calculo
                drawCalcLine();
            } else {
                // Dibujo alternativa completa
                drawLine();
            }
            nro++;
            n++;
        }
        return image;
    }

    private BufferedImage drawAlternativas(Formas forma) throws IOException {
        int n = 1;
        final String orden = forma.getOrden();
        final String[] nOrden = orden.split(",");

        for (int m = 1; m <= 25; m++) {
            if (m % GROUP_SIZE == 1) {
                if (m % 25 == 1) {
                    row = FIRST_ROW;
                } else {
                    row += STEP_ROW * 2;
                }
                if (m < 25) {
                    drawMarks(row);
                }
                row += STEP_ROW / 2;
            } else {
                row += STEP_ROW;
            }
            n++;
        }

        n = 1;
        for (final String sIdx : nOrden) {
            final RespuestasEsperadasPrueba resp = respEsperadas.get(Integer.parseInt(sIdx) - 1);
            if (n % GROUP_SIZE == 1) {
                if (n % 25 == 1) {
                    row = FIRST_ROW;
                    if (n != 1) {
                        col = col + (STEP_COL + CIRCLE_WIDTH) * (colAlternativas + 2);
                    }
                } else {
                    row += STEP_ROW * 2;
                }
                if (n < 25) {
                    drawMarks(row);
                }
                drawTitle();
                row += STEP_ROW / 2;
            } else {
                row += STEP_ROW;
            }
            if (Boolean.TRUE.equals(resp.getVerdaderoFalso())) {
                // dibujo linea Verdadero falso
                drawVFLine();

            } else if (Boolean.TRUE.equals(resp.getMental())) {
                // Dibujo linea Calculo
                drawCalcLine();
            } else {
                // Dibujo alternativa completa
                drawLine();
            }
            nro++;
            n++;
        }
        return image;
    }

    private void drawCalcLine() {
        int c = col;

        g2.drawString(String.valueOf(nro), c, row + HEIGHT_FONT);
        c = c + CIRCLE_WIDTH + STEP_COL * 2;
        g2.setFont(OPTIONS_FONT);
        g2.drawString("B", c, row + HEIGHT_FONT);
        c = c + CIRCLE_WIDTH + STEP_COL;
        g2.drawOval(c, row, CIRCLE_WIDTH, CIRCLE_WIDTH);
        c = c + CIRCLE_WIDTH + STEP_COL;
        g2.drawString("M", c, row + HEIGHT_FONT);
        c = c + CIRCLE_WIDTH + STEP_COL;
        g2.drawOval(c, row, CIRCLE_WIDTH, CIRCLE_WIDTH);
        g2.setFont(LETTERS_FONT);
    }

    private void drawCirclesRut() {
        final int x = ImpresionPrueba.RUT_POINT.x;
        int y = ImpresionPrueba.RUT_POINT.y;

        for (int n = -1; n < ImpresionPrueba.RUT_COLS.length; n++) {
            y = ImpresionPrueba.RUT_POINT.y;
            for (int m = 0; m < 11; m++) {
                if (n == -1) {
                    if (m == 0) {
                        g2.fillRect(21, y, 15, 5);
                    }
                    if (m != 10) {
                        g2.drawString(String.valueOf(m), x, y + HEIGHT_FONT);
                    } else {
                        g2.drawString("K", x, y + HEIGHT_FONT);
                    }
                } else {
                    final boolean canDraw = m < 10 || m == 10 && n == ImpresionPrueba.RUT_COLS.length - 1;
                    if (canDraw) {
                        g2.drawOval(ImpresionPrueba.RUT_COLS[n], y, CIRCLE_WIDTH, CIRCLE_WIDTH);
                    }
                }
                y = y + CIRCLE_WIDTH + 2;
            }
        }

    }

    private void drawForma(Integer forma) {
        g2.fillOval(ImpresionPrueba.FORMA_COLS[forma - 1], ImpresionPrueba.FORMA_ROW, CIRCLE_WIDTH, CIRCLE_WIDTH);
    }

    private void drawHeader(Prueba prueba, Alumno alumno, Profesor profesor, LocalDate fecha) {

        g2.drawString(fecha.toString(), ImpresionPrueba.FECHA_POINT.x, ImpresionPrueba.FECHA_POINT.y);
        g2.drawString(alumno.getColegio().getName(), ImpresionPrueba.COLEGIO_POINT.x, ImpresionPrueba.COLEGIO_POINT.y);
        g2.drawString(alumno.getName(), ImpresionPrueba.NOMBRES_POINT.x, ImpresionPrueba.NOMBRES_POINT.y);
        g2.drawString(alumno.getPaterno() + " " + alumno.getMaterno(), ImpresionPrueba.APELLIDOS_POINT.x,
                ImpresionPrueba.APELLIDOS_POINT.y);
        final String strProrefsor = String.format("%s %s %s", profesor.getPaterno(), profesor.getMaterno(),
                profesor.getName());
        g2.drawString(strProrefsor, ImpresionPrueba.PROFESOR_POINT.x, ImpresionPrueba.PROFESOR_POINT.y);
        g2.drawString(prueba.getAsignatura().getName(), ImpresionPrueba.ASIGNATURA_POINT.x,
                ImpresionPrueba.ASIGNATURA_POINT.y);
        g2.drawString(alumno.getCurso().getName(), ImpresionPrueba.CURSO_POINT.x, ImpresionPrueba.CURSO_POINT.y);

    }

    // private boolean tienePregutasVFoCALC() {
    // boolean res = false;
    // for (RespuestasEsperadasPrueba r : respEsperadas) {
    // if (r.getVerdaderoFalso() != null && r.getMental() != null
    // && (r.getVerdaderoFalso() || r.getMental())) {
    // res = true;
    // break;
    // }
    // }
    // return res;
    // }

    private void drawLine() {
        int c = col;
        g2.drawString(String.valueOf(nro), c, row + HEIGHT_FONT);
        c = c + CIRCLE_WIDTH + STEP_COL * 2;
        for (int k = 0; k < nroAlternativas; k++) {
            g2.drawOval(c, row, CIRCLE_WIDTH, CIRCLE_WIDTH);
            c = c + CIRCLE_WIDTH + STEP_COL;
        }
    }

    private void drawMarks(int lRow) {

        g2.fillRect(17, lRow + HEIGHT_FONT, 15, 5);
        g2.fillRect(578, lRow + HEIGHT_FONT, 15, 5);

    }

    private void drawRut(String rut) {
    	log.info("RUT=" + rut);
        final String strRut = rut.replace("-", "");
        final char[] chrRut = strRut.toCharArray();
        int idxCol = 0;
        if (chrRut.length < 9) {
            idxCol = 1;
        }
        for (final char ch : chrRut) {
            final String str = String.valueOf(ch).toUpperCase();
            g2.drawString(str, ImpresionPrueba.RUT_COLS[idxCol] + 2, ImpresionPrueba.RUT_ROW);

            final int idxRow = str.equals("K") ? 10 : Integer.valueOf(str);
            final int y = ImpresionPrueba.RUT_POINT.y + idxRow * (CIRCLE_WIDTH + 2);
            g2.fillOval(ImpresionPrueba.RUT_COLS[idxCol], y, CIRCLE_WIDTH, CIRCLE_WIDTH);
            idxCol++;
        }
    }

    /**
     * Dibuja el titulo de una fila.
     * 
     * @param nroAlternativas
     */
    private void drawTitle() {
        int c = col;
        c = c + CIRCLE_WIDTH + STEP_COL * 2 + 2;
        for (int k = 0; k < nroAlternativas; k++) {
            g2.drawString(TITLE_LETER[k], c, row);
            c = c + CIRCLE_WIDTH + STEP_COL;
        }
    }

    private void drawVFLine() {
        int c = col;

        g2.drawString(String.valueOf(nro), c, row + HEIGHT_FONT);
        c = c + CIRCLE_WIDTH + STEP_COL * 2;
        g2.setFont(OPTIONS_FONT);
        g2.drawString("V", c, row + HEIGHT_FONT);
        c = c + CIRCLE_WIDTH + STEP_COL;
        g2.drawOval(c, row, CIRCLE_WIDTH, CIRCLE_WIDTH);
        c = c + CIRCLE_WIDTH + STEP_COL;
        g2.drawString("F", c, row + HEIGHT_FONT);
        c = c + CIRCLE_WIDTH + STEP_COL;
        g2.drawOval(c, row, CIRCLE_WIDTH, CIRCLE_WIDTH);
        g2.setFont(LETTERS_FONT);
    }

    public PDDocument imprimir(Prueba prueba, Curso curso, Profesor profesor, Colegio colegio, LocalDate fecha) {
        PDDocument doc = null;
        respEsperadas = prueba.getRespuestas();
        nroAlternativas = prueba.getAlternativas();
        colAlternativas = 5;
        try {

            final BufferedImage imageEmpty = ImageIO.read(new File("res/cpruebas.vacia.png"));
            image = new BufferedImage(imageEmpty.getWidth(), imageEmpty.getHeight(), imageEmpty.getType());
            g2 = (Graphics2D) image.getGraphics();
            final RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHints(rh);

            g2.setFont(LETTERS_FONT);
            HEIGHT_FONT = (int) (g2.getFontMetrics().getHeight() * 0.75);

            g2.setStroke(new BasicStroke(1f));

            doc = new PDDocument();

            /* Por ahora solo la forma 1 */
            prueba.getFormas().size();
            final Formas forma = prueba.getFormas().get(0);

            for (final Alumno alumno : curso.getAlumnos()) {
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, imageEmpty.getWidth(), imageEmpty.getHeight());
                g2.drawImage(imageEmpty, 0, 0, null);
                g2.setColor(Color.BLACK);
                g2.setFont(LETTERS_FONT);
                row = FIRST_ROW;
                col = FIRST_COL;
                nro = 1;
                drawCirclesRut();
                drawRut(alumno.getRut());
                drawForma(forma.getForma());
                drawHeader(prueba, alumno, profesor, fecha);
                final BufferedImage image = drawAlternativas(forma);
                addImageToPdf(image, doc);
            }
            final String fileName = String.format("%s-%s-%s-%s", fecha.toString(), prueba.getName(), colegio.getName(),
                    curso.getName());

            final FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName(fileName);
            final FileChooser.ExtensionFilter imageExtFilter = new FileChooser.ExtensionFilter("Archivos PDF ",
                    "*.pdf");
            fileChooser.getExtensionFilters().add(imageExtFilter);
            fileChooser.setInitialDirectory(Utils.getDefaultDirectory());
            fileChooser.setTitle("Seleccione Archivo a grabar");
            File output = fileChooser.showSaveDialog(null);

            if (output != null && !output.getAbsolutePath().toLowerCase().endsWith("pdf")) {
                output = new File(output + ".pdf");
            }

            if (output != null) {
                doc.setDocumentInformation(new PDDocumentInformation());
                doc.getDocumentInformation().setTitle(fileName);
                doc.getDocumentInformation().setAuthor("EOS");
                doc.save(output);
            }
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final COSVisitorException e) {
            e.printStackTrace();
        }
        return doc;
    }
}
