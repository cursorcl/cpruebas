package cl.eos.detection;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

import cl.eos.persistence.models.RespuestasEsperadasPrueba;

public class ImpresionPrueba300 {

	private final int FIRST_ROW = 1450;
	private final int FIRST_COL = 150;
	private final int GROUP_SIZE = 5;

	private final int CIRCLE_WIDTH = 45;
	private final int STEP_ROW = (CIRCLE_WIDTH + 10);
	private final int STEP_COL = CIRCLE_WIDTH / 2;

	private final String[] TITLE_LETER = { "A", "B", "C", "D", "E" };

	private int nroAlternativas;
	private int HEIGHT_FONT;

	// private Prueba prueba;
	private List<RespuestasEsperadasPrueba> respEsperadas = new ArrayList<RespuestasEsperadasPrueba>();

	private int row = FIRST_ROW;
	private int col = FIRST_COL;
	private int nro = 1;

	private int colAlternativas;
	private BufferedImage image;
	private Graphics2D g2;

	public ImpresionPrueba300() {
		nroAlternativas = 5;
		colAlternativas = nroAlternativas;
		for (int n = 1; n <= 100; n++) {
			RespuestasEsperadasPrueba r = new RespuestasEsperadasPrueba();
			r.setRespuesta("A");
			if (n % 2 == 0) {
				r.setMental(Boolean.TRUE);
			}
			if (n % 3 == 0) {
				r.setVerdaderoFalso(Boolean.TRUE);
			}
			r.setId(new Long(n));
			respEsperadas.add(r);
		}
		colAlternativas = getMinNroAlternativas();
	}

	public BufferedImage draw() throws IOException {

		image = ImageIO.read(new File("./res/cpruebas.vacia.300.png"));

		g2 = (Graphics2D) image.getGraphics();

		g2.setFont(new Font("Arial", Font.PLAIN, 32));
		HEIGHT_FONT = g2.getFontMetrics().getHeight();
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(2f));
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
			} else {
				row += STEP_ROW;
			}
			if (Boolean.TRUE.equals(resp.getVerdaderoFalso())) {
				// dibujo linea Verdadero falso
				drawVFLine();

			} else if (Boolean.TRUE.equals(resp.getMental())) {
				// Dibujo linea Mental
				drawMentalLine();
			} else {
				// Dibujo alternativa completa
				drawLine();
			}
			nro++;
			n++;
		}
		return image;
	}

	private int getMinNroAlternativas() {
		int res = nroAlternativas;
		for (RespuestasEsperadasPrueba r : respEsperadas) {
			if (r.getVerdaderoFalso() != null && r.getMental() != null
					&& (r.getVerdaderoFalso() || r.getMental())) {
				res = Math.max(4, nroAlternativas);
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

	private void drawMentalLine() {
		int c = col;
		g2.drawString(String.valueOf(nro), c, row + HEIGHT_FONT);
		c = c + CIRCLE_WIDTH + STEP_COL * 2;

		g2.drawString("B", c, row + HEIGHT_FONT);
		c = c + CIRCLE_WIDTH + STEP_COL;
		g2.drawOval(c, row, CIRCLE_WIDTH, CIRCLE_WIDTH);
		c = c + CIRCLE_WIDTH + STEP_COL;
		g2.drawString("M", c, row + HEIGHT_FONT);
		c = c + CIRCLE_WIDTH + STEP_COL;
		g2.drawOval(c, row, CIRCLE_WIDTH, CIRCLE_WIDTH);

	}

	private void drawVFLine() {
		int c = col;
		g2.drawString(String.valueOf(nro), c, row + HEIGHT_FONT);
		c = c + CIRCLE_WIDTH + STEP_COL * 2;

		g2.drawString("V", c, row + HEIGHT_FONT);
		c = c + CIRCLE_WIDTH + STEP_COL;
		g2.drawOval(c, row, CIRCLE_WIDTH, CIRCLE_WIDTH);
		c = c + CIRCLE_WIDTH + STEP_COL;
		g2.drawString("F", c, row + HEIGHT_FONT);
		c = c + CIRCLE_WIDTH + STEP_COL;
		g2.drawOval(c, row, CIRCLE_WIDTH, CIRCLE_WIDTH);
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

	public static void main(String[] args) {
		ImpresionPrueba300 imp = new ImpresionPrueba300();
		try {

			BufferedImage image = imp.draw();

			try {

				PDDocument doc = new PDDocument();
				PDPage page = new PDPage();
				doc.addPage(page);
				PDPageContentStream content = new PDPageContentStream(doc,
						page, true, true);

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
