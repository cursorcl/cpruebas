package cl.eos.detection;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.Profesor;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;

public class ImpresionPrueba {

  private static final int RUT_ROW = 74;
  private static final int[] RUT_COLS = {65, 80, 98, 113, 128, 148, 163, 178, 197};
  private static final int[] RUT_ROWS = {90, 103, 116, 129, 142, 155, 168, 181, 194, 207, 220, 233};
  private static final Point FECHA_POINT = new Point(353, 74);
  private static final Point COLEGIO_POINT = new Point(470, 74);
  private static final Point NOMBRES_POINT = new Point(297, 108);
  private static final Point APELLIDOS_POINT = new Point(297, 140);
  private static final Point PROFESOR_POINT = new Point(297, 172);
  private static final Point ASIGNATURA_POINT = new Point(297, 204);
  private static final Point CURSO_POINT = new Point(470, 204);

  private final int FIRST_ROW = 350;
  private final int FIRST_COL = 55;
  private final int GROUP_SIZE = 5;

  private final int CIRCLE_WIDTH = 10;
  private final int STEP_ROW = (CIRCLE_WIDTH + CIRCLE_WIDTH / 3);
  private final int STEP_COL = CIRCLE_WIDTH / 2;

  private final String[] TITLE_LETER = {"A", "B", "C", "D", "E"};

  private final Font LETTERS_FONT = new Font("Arial", Font.PLAIN, 10);
  private final Font OPTIONS_FONT = new Font("Arial", Font.PLAIN, 8);

  private int nroAlternativas;
  private int HEIGHT_FONT;

  private List<RespuestasEsperadasPrueba> respEsperadas =
      new ArrayList<RespuestasEsperadasPrueba>();

  private int row = FIRST_ROW;
  private int col = FIRST_COL;
  private int nro = 1;

  private int colAlternativas;
  private BufferedImage image;
  private Graphics2D g2;

  public ImpresionPrueba() {

  }

  public void imprimir(Prueba prueba, Curso curso, Profesor profesor, Colegio colegio,
      LocalDate fecha) {
    respEsperadas = prueba.getRespuestas();
    colAlternativas =
        tienePregutasVFoCALC() ? Math.max(4, prueba.getAlternativas()) : prueba.getAlternativas();
    try {
      image = ImageIO.read(new File("./res/cpruebas.vacia.png"));
      g2 = (Graphics2D) image.getGraphics();
      g2.setFont(LETTERS_FONT);
      HEIGHT_FONT = (int) (g2.getFontMetrics().getHeight() * 0.75);
      g2.setColor(Color.BLACK);
      g2.setStroke(new BasicStroke(1f));

      PDDocument doc = new PDDocument();
      for (Alumno alumno : curso.getAlumnos()) {
        drawRut(alumno.getRut());
        drawHeader(prueba, alumno, profesor, fecha);
        BufferedImage image = drawAlternativas();
        addImageToPdf(image, doc);
      }
      doc.save("./res/miprueba.pdf");
      doc.close();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (COSVisitorException e) {
      e.printStackTrace();
    }

  }

  private void drawHeader(Prueba prueba, Alumno alumno, Profesor profesor, LocalDate fecha) {

    g2.drawString(fecha.toString(), FECHA_POINT.x, FECHA_POINT.y);
    g2.drawString(alumno.getColegio().getName(), COLEGIO_POINT.x, COLEGIO_POINT.y);
    g2.drawString(alumno.getName(), NOMBRES_POINT.x, NOMBRES_POINT.y);
    g2.drawString(alumno.getPaterno() + " " + alumno.getMaterno(), APELLIDOS_POINT.x,
        APELLIDOS_POINT.y);
    String strProrefsor =
        String.format("%s %s %s", profesor.getPaterno(), profesor.getMaterno(), profesor.getName());
    g2.drawString(strProrefsor, PROFESOR_POINT.x, PROFESOR_POINT.y);
    g2.drawString(prueba.getAsignatura().getName(), ASIGNATURA_POINT.x, ASIGNATURA_POINT.y);
    g2.drawString(alumno.getCurso().getName(), CURSO_POINT.x, CURSO_POINT.y);


  }

  private void drawRut(String rut) {
    String strRut = rut.replace("-", "");
    char[] chrRut = strRut.toCharArray();
    int idxCol = 0;
    if (chrRut.length < 9) {
      idxCol = 1;
    }
    for (char ch : chrRut) {
      String str = String.valueOf(ch).toUpperCase();
      g2.drawString(str, RUT_COLS[idxCol], RUT_ROW);
      int idxRow = str.equals("K") ? 11 : Integer.valueOf(str);
      g2.drawOval(RUT_COLS[idxCol], RUT_ROWS[idxRow], CIRCLE_WIDTH, CIRCLE_WIDTH);
    }
  }

  private BufferedImage drawAlternativas() throws IOException {
    int n = 1;
    for (RespuestasEsperadasPrueba resp : respEsperadas) {
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

  private boolean tienePregutasVFoCALC() {
    boolean res = false;
    for (RespuestasEsperadasPrueba r : respEsperadas) {
      if (r.getVerdaderoFalso() != null && r.getMental() != null
          && (r.getVerdaderoFalso() || r.getMental())) {
        res = true;
        break;
      }
    }
    return res;
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

  private void drawCalcLine() {
    int c = col;
    g2.setFont(OPTIONS_FONT);
    g2.drawString(String.valueOf(nro), c, row + HEIGHT_FONT);
    c = c + CIRCLE_WIDTH + STEP_COL * 2;

    g2.drawString("B", c, row + HEIGHT_FONT);
    c = c + CIRCLE_WIDTH + STEP_COL;
    g2.drawOval(c, row, CIRCLE_WIDTH, CIRCLE_WIDTH);
    c = c + CIRCLE_WIDTH + STEP_COL;
    g2.drawString("M", c, row + HEIGHT_FONT);
    c = c + CIRCLE_WIDTH + STEP_COL;
    g2.drawOval(c, row, CIRCLE_WIDTH, CIRCLE_WIDTH);
    g2.setFont(LETTERS_FONT);
  }

  private void drawVFLine() {
    int c = col;
    g2.setFont(OPTIONS_FONT);
    g2.drawString(String.valueOf(nro), c, row + HEIGHT_FONT);
    c = c + CIRCLE_WIDTH + STEP_COL * 2;

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

  private void addImageToPdf(BufferedImage image, PDDocument doc) {
    try {
      PDPage page = new PDPage();
      doc.addPage(page);
      PDPageContentStream content;
      content = new PDPageContentStream(doc, page, true, true);
      PDXObjectImage ximage = new PDJpeg(doc, image, 1f);
      ximage.setWidth((int) page.getMediaBox().getWidth());
      ximage.setHeight((int) page.getMediaBox().getHeight());
      content.drawImage(ximage, 0, 0);
      content.close();
    } catch (IOException e) {
      e.printStackTrace();
    }


  }

  public static void main(String[] args) {
    ImpresionPrueba imp = new ImpresionPrueba();
    try {

      BufferedImage image = imp.drawAlternativas();

      try {

        PDDocument doc = new PDDocument();
        PDPage page = new PDPage();
        doc.addPage(page);
        PDPageContentStream content = new PDPageContentStream(doc, page, true, true);

        PDXObjectImage ximage = new PDJpeg(doc, image, 1f);

        ximage.setWidth((int) page.getMediaBox().getWidth());
        ximage.setHeight((int) page.getMediaBox().getHeight());
        content.drawImage(ximage, 0, 0);
        content.close();
        doc.save("./res/miprueba.pdf");
        doc.close();
      } catch (COSVisitorException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      JFrame frame = new JFrame("Zoom a imagen");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      ZoomPanel zoom = new ZoomPanel(image);

      frame.add(zoom);
      frame.setPreferredSize(new Dimension(485, 300));

      frame.pack();
      frame.setVisible(true);

      // PrintActionListener print = new PrintActionListener(image);
      // Thread th = new Thread(print);
      // th.start();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
