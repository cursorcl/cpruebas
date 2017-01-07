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
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import cl.eos.imp.view.UtilsAlert;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_Formas;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.util.Utils;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

public class ImpresionPruebaVacia {
    private static final int[] RUT_COLS = { 57, 73, 91, 106, 121, 140, 156, 171, 191 };
    private static final int FORMA_ROW = 66;
    private static final int[] FORMA_COLS = { 229, 244, 259 };
    private static final Point RUT_POINT = new Point(40, 88);
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
    private int row = FIRST_ROW;
    private int col = FIRST_COL;
    private int nro = 1;
    private int colAlternativas;
    private BufferedImage image;
    private Graphics2D g2;
    R_Prueba prueba;
    R_Curso curso;
    R_Colegio colegio;
    LocalDate fecha;
    R_Formas forma;
    int nPruebas;
    List<R_RespuestasEsperadasPrueba> respEsperadas;
    public ImpresionPruebaVacia() {}
    public ImpresionPruebaVacia(R_Prueba prueba, R_Curso curso, R_Colegio colegio, LocalDate fecha, R_Formas forma, int nPruebas,
            List<R_RespuestasEsperadasPrueba> respEsperadas) {
        super();
        this.prueba = prueba;
        this.curso = curso;
        this.colegio = colegio;
        this.fecha = fecha;
        this.forma = forma;
        this.nPruebas = nPruebas;
        this.respEsperadas = respEsperadas;
    }
    public PDDocument imprimir() {
        PDDocument doc = null;
        nroAlternativas = prueba.getAlternativas();
        colAlternativas = 5;
        try {
            final File file = new File("res/cpruebas.vacia.png");
            final BufferedImage imageEmpty = ImageIO.read(file);
            image = new BufferedImage(imageEmpty.getWidth(), imageEmpty.getHeight(), imageEmpty.getType());
            g2 = (Graphics2D) image.getGraphics();
            final RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHints(rh);
            g2.setFont(LETTERS_FONT);
            HEIGHT_FONT = (int) (g2.getFontMetrics().getHeight() * 0.75);
            g2.setStroke(new BasicStroke(1f));
            doc = new PDDocument();
            /* Por ahora solo la forma 1 */
            for (int n = 0; n < nPruebas; n++) {
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, imageEmpty.getWidth(), imageEmpty.getHeight());
                g2.drawImage(imageEmpty, 0, 0, null);
                g2.setColor(Color.BLACK);
                g2.setFont(LETTERS_FONT);
                row = FIRST_ROW;
                col = FIRST_COL;
                nro = 1;
                drawCirclesRut();
                drawForma(forma.getForma());
                final BufferedImage image = drawAlternativas(forma);
                addImageToPdf(image, doc);
            }
            final String fileName = String.format("%s-%s-%s-%s-Vacia", fecha.toString(), prueba.getName(), colegio.getName(), curso.getName());
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName(fileName);
            final FileChooser.ExtensionFilter imageExtFilter = new FileChooser.ExtensionFilter("Archivos PDF ", "*.pdf");
            fileChooser.getExtensionFilters().add(imageExtFilter);
            fileChooser.setInitialDirectory(Utils.getDefaultDirectory());
            fileChooser.setTitle("Seleccione Archivo a grabar");
            File output = fileChooser.showSaveDialog(null);
            if (!output.getAbsolutePath().toLowerCase().endsWith("pdf")) {
                output = new File(output + ".pdf");
            }
            if (output != null) {
                doc.setDocumentInformation(new PDDocumentInformation());
                doc.getDocumentInformation().setTitle(fileName);
                doc.getDocumentInformation().setAuthor("EOS");
                try {
                    doc.save(output);
                } catch (final Exception ex) {
                    final Alert alert = UtilsAlert.makeExceptionAlert("Problemas con el archivo, no se ha podido almacenar.", ex);
                    alert.showAndWait();
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return doc;
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
        final String orden = forma.getOrden();
        final String[] nOrden = orden.split(",");
        for (int n = 1; n <= 25; n++) {
            if (n % GROUP_SIZE == 1) {
                if (n % 25 == 1) {
                    row = FIRST_ROW;
                } else {
                    row += STEP_ROW * 2;
                }
                drawMarks(row);
                row += STEP_ROW / 2;
            } else {
                row += STEP_ROW;
            }
        }
        int n = 1;
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
        final int x = ImpresionPruebaVacia.RUT_POINT.x;
        int y = ImpresionPruebaVacia.RUT_POINT.y;
        for (int n = -1; n < ImpresionPruebaVacia.RUT_COLS.length; n++) {
            y = ImpresionPruebaVacia.RUT_POINT.y;
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
                    final boolean canDraw = m < 10 || m == 10 && n == ImpresionPruebaVacia.RUT_COLS.length - 1;
                    if (canDraw) {
                        g2.drawOval(ImpresionPruebaVacia.RUT_COLS[n], y, CIRCLE_WIDTH, CIRCLE_WIDTH);
                    }
                }
                y = y + CIRCLE_WIDTH + 2;
            }
        }
    }
    private void drawForma(Integer forma) {
        g2.fillOval(ImpresionPruebaVacia.FORMA_COLS[forma - 1], ImpresionPruebaVacia.FORMA_ROW, CIRCLE_WIDTH, CIRCLE_WIDTH);
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
    /**
     * @return the prueba
     */
    public final R_Prueba getPrueba() {
        return prueba;
    }
    /**
     * @param prueba the prueba to set
     */
    public final void setPrueba(R_Prueba prueba) {
        this.prueba = prueba;
    }
    /**
     * @return the curso
     */
    public final R_Curso getCurso() {
        return curso;
    }
    /**
     * @param curso the curso to set
     */
    public final void setCurso(R_Curso curso) {
        this.curso = curso;
    }
    /**
     * @return the colegio
     */
    public final R_Colegio getColegio() {
        return colegio;
    }
    /**
     * @param colegio the colegio to set
     */
    public final void setColegio(R_Colegio colegio) {
        this.colegio = colegio;
    }
    /**
     * @return the fecha
     */
    public final LocalDate getFecha() {
        return fecha;
    }
    /**
     * @param fecha the fecha to set
     */
    public final void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    /**
     * @return the forma
     */
    public final R_Formas getForma() {
        return forma;
    }
    /**
     * @param forma the forma to set
     */
    public final void setForma(R_Formas forma) {
        this.forma = forma;
    }
    /**
     * @return the nPruebas
     */
    public final int getnPruebas() {
        return nPruebas;
    }
    /**
     * @param nPruebas the nPruebas to set
     */
    public final void setnPruebas(int nPruebas) {
        this.nPruebas = nPruebas;
    }
    /**
     * @return the respEsperadas
     */
    public final List<R_RespuestasEsperadasPrueba> getRespEsperadas() {
        return respEsperadas;
    }
    /**
     * @param respEsperadas the respEsperadas to set
     */
    public final void setRespEsperadas(List<R_RespuestasEsperadasPrueba> respEsperadas) {
        this.respEsperadas = respEsperadas;
    }
    public static class Builder {
        private int colAlternativas;
        private BufferedImage image;
        private R_Prueba prueba;
        private R_Curso curso;
        private R_Colegio colegio;
        private LocalDate fecha;
        private R_Formas forma;
        private int nPruebas;
        private List<R_RespuestasEsperadasPrueba> respEsperadas;
        public Builder colAlternativas(int colAlternativas) {
            this.colAlternativas = colAlternativas;
            return this;
        }
        public Builder image(BufferedImage image) {
            this.image = image;
            return this;
        }
        public Builder prueba(R_Prueba prueba) {
            this.prueba = prueba;
            return this;
        }
        public Builder curso(R_Curso curso) {
            this.curso = curso;
            return this;
        }
        public Builder colegio(R_Colegio colegio) {
            this.colegio = colegio;
            return this;
        }
        public Builder fecha(LocalDate fecha) {
            this.fecha = fecha;
            return this;
        }
        public Builder forma(R_Formas forma) {
            this.forma = forma;
            return this;
        }
        public Builder nPruebas(int nPruebas) {
            this.nPruebas = nPruebas;
            return this;
        }
        public Builder respEsperadas(List<R_RespuestasEsperadasPrueba> respEsperadas) {
            this.respEsperadas = respEsperadas;
            return this;
        }
        public ImpresionPruebaVacia build() {
            return new ImpresionPruebaVacia(this);
        }
    }
    private ImpresionPruebaVacia(Builder builder) {
        this.colAlternativas = builder.colAlternativas;
        this.image = builder.image;
        this.prueba = builder.prueba;
        this.curso = builder.curso;
        this.colegio = builder.colegio;
        this.fecha = builder.fecha;
        this.forma = builder.forma;
        this.nPruebas = builder.nPruebas;
        this.respEsperadas = builder.respEsperadas;
    }
}
