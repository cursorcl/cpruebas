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

import javax.imageio.ImageIO;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import cl.eos.restful.tables.R_Alumno;
import cl.eos.restful.tables.R_Formas;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.util.Utils;
import javafx.stage.FileChooser;

public class ImpresionPrueba {
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
    private List<R_RespuestasEsperadasPrueba> respEsperadas = new ArrayList<R_RespuestasEsperadasPrueba>();
    private int row = FIRST_ROW;
    private int col = FIRST_COL;
    private int nro = 1;
    private int colAlternativas;
    private BufferedImage image;
    private Graphics2D g2;
    String colegio;
    String asignatura;
    String curso;
    String profesor;
    List<R_Formas> formas;
    List<R_Alumno> alumnos;
    R_Prueba prueba;
    LocalDate fecha;
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
        for (final R_RespuestasEsperadasPrueba resp : respEsperadas) {
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
            if (Boolean.TRUE.equals(resp.getVerdaderofalso())) {
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
    private BufferedImage drawAlternativas(R_Formas forma) throws IOException {
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
            final R_RespuestasEsperadasPrueba resp = respEsperadas.get(Integer.parseInt(sIdx) - 1);
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
            if (Boolean.TRUE.equals(resp.getVerdaderofalso())) {
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
    private void drawHeader(R_Alumno alumno) {
        g2.drawString(fecha.toString(), ImpresionPrueba.FECHA_POINT.x, ImpresionPrueba.FECHA_POINT.y);
        g2.drawString(colegio, ImpresionPrueba.COLEGIO_POINT.x, ImpresionPrueba.COLEGIO_POINT.y);
        g2.drawString(alumno.getName(), ImpresionPrueba.NOMBRES_POINT.x, ImpresionPrueba.NOMBRES_POINT.y);
        g2.drawString(alumno.getPaterno() + " " + alumno.getMaterno(), ImpresionPrueba.APELLIDOS_POINT.x, ImpresionPrueba.APELLIDOS_POINT.y);
        g2.drawString(profesor, ImpresionPrueba.PROFESOR_POINT.x, ImpresionPrueba.PROFESOR_POINT.y);
        g2.drawString(asignatura, ImpresionPrueba.ASIGNATURA_POINT.x, ImpresionPrueba.ASIGNATURA_POINT.y);
        g2.drawString(curso, ImpresionPrueba.CURSO_POINT.x, ImpresionPrueba.CURSO_POINT.y);
    }
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
    public PDDocument imprimir() {
        PDDocument doc = null;
        nroAlternativas = prueba.getAlternativas();
        colAlternativas = 5;
        try {
            final BufferedImage imageEmpty = ImageIO.read(new File("res/cpruebas.vacia.png"));
            image = new BufferedImage(imageEmpty.getWidth(), imageEmpty.getHeight(), imageEmpty.getType());
            g2 = (Graphics2D) image.getGraphics();
            final RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHints(rh);
            g2.setFont(LETTERS_FONT);
            HEIGHT_FONT = (int) (g2.getFontMetrics().getHeight() * 0.75);
            g2.setStroke(new BasicStroke(1f));
            doc = new PDDocument();
            /* Por ahora solo la forma 1 */
            final R_Formas forma = formas.get(0);
            for (final R_Alumno alumno : alumnos) {
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
                drawHeader(alumno);
                final BufferedImage image = drawAlternativas(forma);
                addImageToPdf(image, doc);
            }
            final String fileName = String.format("%s-%s-%s-%s", fecha.toString(), prueba.getName(), colegio, curso);
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName(fileName);
            final FileChooser.ExtensionFilter imageExtFilter = new FileChooser.ExtensionFilter("Archivos PDF ", "*.pdf");
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
    /**
     * @return the respEsperadas
     */
    public final List<R_RespuestasEsperadasPrueba> getRespEsperadas() {
        return respEsperadas;
    }
    /**
     * @param respEsperadas
     *            the respEsperadas to set
     */
    public final void setRespEsperadas(List<R_RespuestasEsperadasPrueba> respEsperadas) {
        this.respEsperadas = respEsperadas;
    }
    /**
     * @return the colegio
     */
    public final String getColegio() {
        return colegio;
    }
    /**
     * @param colegio
     *            the colegio to set
     */
    public final void setColegio(String colegio) {
        this.colegio = colegio;
    }
    /**
     * @return the asignatura
     */
    public final String getAsignatura() {
        return asignatura;
    }
    /**
     * @param asignatura
     *            the asignatura to set
     */
    public final void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }
    /**
     * @return the curso
     */
    public final String getCurso() {
        return curso;
    }
    /**
     * @param curso
     *            the curso to set
     */
    public final void setCurso(String curso) {
        this.curso = curso;
    }
    /**
     * @return the profesor
     */
    public final String getProfesor() {
        return profesor;
    }
    /**
     * @param profesor
     *            the profesor to set
     */
    public final void setProfesor(String profesor) {
        this.profesor = profesor;
    }
    /**
     * @return the formas
     */
    public final List<R_Formas> getFormas() {
        return formas;
    }
    /**
     * @param formas
     *            the formas to set
     */
    public final void setFormas(List<R_Formas> formas) {
        this.formas = formas;
    }
    /**
     * @return the alumnos
     */
    public final List<R_Alumno> getAlumnos() {
        return alumnos;
    }
    /**
     * @param alumnos
     *            the alumnos to set
     */
    public final void setAlumnos(List<R_Alumno> alumnos) {
        this.alumnos = alumnos;
    }
    /**
     * @return the prueba
     */
    public final R_Prueba getPrueba() {
        return prueba;
    }
    /**
     * @param prueba
     *            the prueba to set
     */
    public final void setPrueba(R_Prueba prueba) {
        this.prueba = prueba;
    }
    /**
     * @return the fecha
     */
    public final LocalDate getFecha() {
        return fecha;
    }
    /**
     * @param fecha
     *            the fecha to set
     */
    public final void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public static class Builder {
        private List<R_RespuestasEsperadasPrueba> respEsperadas;
        private String colegio;
        private String asignatura;
        private String curso;
        private String profesor;
        private List<R_Formas> formas;
        private List<R_Alumno> alumnos;
        private R_Prueba prueba;
        private LocalDate fecha;
        public Builder respEsperadas(List<R_RespuestasEsperadasPrueba> respEsperadas) {
            this.respEsperadas = respEsperadas;
            return this;
        }
        public Builder colegio(String colegio) {
            this.colegio = colegio;
            return this;
        }
        public Builder asignatura(String asignatura) {
            this.asignatura = asignatura;
            return this;
        }
        public Builder curso(String curso) {
            this.curso = curso;
            return this;
        }
        public Builder profesor(String profesor) {
            this.profesor = profesor;
            return this;
        }
        public Builder formas(List<R_Formas> formas) {
            this.formas = formas;
            return this;
        }
        public Builder alumnos(List<R_Alumno> alumnos) {
            this.alumnos = alumnos;
            return this;
        }
        public Builder prueba(R_Prueba prueba) {
            this.prueba = prueba;
            return this;
        }
        public Builder fecha(LocalDate fecha) {
            this.fecha = fecha;
            return this;
        }
        public ImpresionPrueba build() {
            return new ImpresionPrueba(this);
        }
    }
    private ImpresionPrueba(Builder builder) {
        this.respEsperadas = builder.respEsperadas;
        this.colegio = builder.colegio;
        this.asignatura = builder.asignatura;
        this.curso = builder.curso;
        this.profesor = builder.profesor;
        this.formas = builder.formas;
        this.alumnos = builder.alumnos;
        this.prueba = builder.prueba;
        this.fecha = builder.fecha;
    }
}
