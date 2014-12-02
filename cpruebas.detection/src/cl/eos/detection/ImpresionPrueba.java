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

import javafx.stage.FileChooser;

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

public class ImpresionPrueba {

	private static final int RUT_ROW = 74;
	private static final int[] RUT_COLS = { 57, 73, 91, 106, 121, 140, 156,
			171, 191 };
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
	private final int STEP_ROW = (CIRCLE_WIDTH + CIRCLE_WIDTH / 3);
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

	public PDDocument imprimir(Prueba prueba, Curso curso, Profesor profesor,
			Colegio colegio, LocalDate fecha) {
		PDDocument doc = null;
		respEsperadas = prueba.getRespuestas();
		nroAlternativas = prueba.getAlternativas();
		colAlternativas = 5;
		try {


			BufferedImage imageEmpty = ImageIO.read(new File("res/cpruebas.vacia.png"));
			image = new BufferedImage(imageEmpty.getWidth(),
					imageEmpty.getHeight(), imageEmpty.getType());
			g2 = (Graphics2D) image.getGraphics();
			RenderingHints rh = new RenderingHints(
					RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2.setRenderingHints(rh);

			g2.setFont(LETTERS_FONT);
			HEIGHT_FONT = (int) (g2.getFontMetrics().getHeight() * 0.75);

			g2.setStroke(new BasicStroke(1f));

			doc = new PDDocument();

			/* Por ahora solo la forma 1 */
			Formas forma = prueba.getFormas().get(0);

			for (Alumno alumno : curso.getAlumnos()) {
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
				BufferedImage image = drawAlternativas(forma);
				addImageToPdf(image, doc);
			}
			String fileName = String.format("%s-%s-%s-%s", fecha.toString(),
					prueba.getName(), colegio.getName(), curso.getName());

			FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialFileName(fileName);
			FileChooser.ExtensionFilter imageExtFilter = new FileChooser.ExtensionFilter(
					"Archivos PDF ", "*.pdf");
			fileChooser.getExtensionFilters().add(imageExtFilter);
			fileChooser.setInitialDirectory(Utils.getDefaultDirectory());
			fileChooser.setTitle("Seleccione Archivo a grabar");
			File  output = fileChooser.showSaveDialog(null);
			
			if(output != null && !output.getAbsolutePath().toLowerCase().endsWith("pdf")){
				output = new File(output + ".pdf");
			}
			
			
			if (output != null) {
				doc.setDocumentInformation(new PDDocumentInformation());
				doc.getDocumentInformation().setTitle(fileName);
				doc.getDocumentInformation().setAuthor("EOS");
				doc.save(output);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (COSVisitorException e) {
			e.printStackTrace();
		}
		return doc;
	}

	private void drawForma(Integer forma) {
		g2.fillOval(FORMA_COLS[forma - 1], FORMA_ROW, CIRCLE_WIDTH,
				CIRCLE_WIDTH);
	}

	private void drawHeader(Prueba prueba, Alumno alumno, Profesor profesor,
			LocalDate fecha) {

		g2.drawString(fecha.toString(), FECHA_POINT.x, FECHA_POINT.y);
		g2.drawString(alumno.getColegio().getName(), COLEGIO_POINT.x,
				COLEGIO_POINT.y);
		g2.drawString(alumno.getName(), NOMBRES_POINT.x, NOMBRES_POINT.y);
		g2.drawString(alumno.getPaterno() + " " + alumno.getMaterno(),
				APELLIDOS_POINT.x, APELLIDOS_POINT.y);
		String strProrefsor = String.format("%s %s %s", profesor.getPaterno(),
				profesor.getMaterno(), profesor.getName());
		g2.drawString(strProrefsor, PROFESOR_POINT.x, PROFESOR_POINT.y);
		g2.drawString(prueba.getAsignatura().getName(), ASIGNATURA_POINT.x,
				ASIGNATURA_POINT.y);
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
			g2.drawString(str, RUT_COLS[idxCol] + 2, RUT_ROW);

			int idxRow = str.equals("K") ? 10 : Integer.valueOf(str);
			int y = RUT_POINT.y + idxRow * (CIRCLE_WIDTH + 2);
			g2.fillOval(RUT_COLS[idxCol], y, CIRCLE_WIDTH, CIRCLE_WIDTH);
			idxCol++;
		}
	}

	private void drawCirclesRut() {
		int x = RUT_POINT.x;
		int y = RUT_POINT.y;

		for (int n = -1; n < RUT_COLS.length; n++) {
			y = RUT_POINT.y;
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
					boolean canDraw = (m < 10)
							|| (m == 10 && n == RUT_COLS.length - 1);
					if (canDraw) {
						g2.drawOval(RUT_COLS[n], y, CIRCLE_WIDTH, CIRCLE_WIDTH);
					}
				}
				y = y + CIRCLE_WIDTH + 2;
			}
		}

	}

	private BufferedImage drawAlternativas(Formas forma) throws IOException {
		int n = 1;
		String orden = forma.getOrden();
		String[] nOrden = orden.split(",");

		for (String sIdx : nOrden) {
			RespuestasEsperadasPrueba resp = respEsperadas.get(Integer
					.parseInt(sIdx) - 1);
			if (n % GROUP_SIZE == 1) {
				if (n % 25 == 1) {
					row = FIRST_ROW;
					if (n != 1) {
						col = col + (STEP_COL + CIRCLE_WIDTH)
								* (colAlternativas + 2);
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

	public BufferedImage drawAlternativas() throws IOException {
		int n = 1;

		for (RespuestasEsperadasPrueba resp : respEsperadas) {
			if (n % GROUP_SIZE == 1) {
				if (n % 25 == 1) {
					row = FIRST_ROW;
					if (n != 1) {
						col = col + (STEP_COL + CIRCLE_WIDTH)
								* (colAlternativas + 2);
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

	private void drawMarks(int lRow) {

		g2.fillRect(17, lRow + HEIGHT_FONT, 15, 5);
		g2.fillRect(578, lRow + HEIGHT_FONT, 15, 5);

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
		BufferedImage imageEmpty;
		try {
			imageEmpty = ImageIO.read(new File("./res/cpruebas.vacia.png"));
			BufferedImage image = new BufferedImage(imageEmpty.getWidth(),
					imageEmpty.getHeight(), imageEmpty.getType());
			Graphics2D g2 = (Graphics2D) image.getGraphics();
			RenderingHints rh = new RenderingHints(
					RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2.setRenderingHints(rh);
			g2.drawImage(imageEmpty, 0, 0, imageEmpty.getWidth(),
					imageEmpty.getHeight(), null);
			ImageIO.write(image, "png", new File("./res/patron.png"));
			PDDocument doc = new PDDocument();
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

			String fileName = String.format("ejemplo");
			File output = new File("./res/" + fileName + ".pdf");
			doc.setDocumentInformation(new PDDocumentInformation());
			doc.getDocumentInformation().setTitle(fileName);
			doc.getDocumentInformation().setAuthor("EOS");
			doc.save(output);
		} catch (IOException | COSVisitorException e1) {
			e1.printStackTrace();
		}
	}
}
