package cl.eos.detection;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;

import boofcv.gui.image.ShowImages;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;

public class ImpresionPrueba {

	private final int FIRST_ROW = 344;
	private final int FIRST_COL = 21;
	private final int GROUP_SIZE = 5;

	private final int CIRCLE_WIDTH = 10;
	private final int STEP_ROW = CIRCLE_WIDTH + 5;
	private final int STEP_COL = CIRCLE_WIDTH / 2;

	private final String[] TITLE_LETER = { "A", "B", "C", "D", "E" };

	private int nroAlternativas;

	private Prueba prueba;
	private List<RespuestasEsperadasPrueba> respEsperadas = new ArrayList<RespuestasEsperadasPrueba>();

	private int row = FIRST_ROW;
	private int col = FIRST_COL;
	private int nro = 1;

	private int colAlternativas;
	private BufferedImage image;
	private Graphics2D g2;

	public ImpresionPrueba() {
		nroAlternativas = 3;
		colAlternativas = nroAlternativas;
		for (int n = 1; n <= 75; n++) {
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

		image = ImageIO.read(new File("./res/cpruebas.vacia.png"));

		g2 = (Graphics2D) image.getGraphics();
		g2.setColor(Color.RED);
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
		g2.drawString(String.valueOf(nro), c, row + 10);
		c = c + CIRCLE_WIDTH + STEP_COL * 2;
		for (int k = 0; k < nroAlternativas; k++) {
			g2.drawOval(c, row, CIRCLE_WIDTH, CIRCLE_WIDTH);
			c = c + CIRCLE_WIDTH + STEP_COL;
		}
	}

	private void drawMentalLine() {
		int c = col;
		g2.drawString(String.valueOf(nro), c, row + 10);
		c = c + CIRCLE_WIDTH + STEP_COL * 2;

		g2.drawString("B", c, row + 10);
		c = c + CIRCLE_WIDTH + STEP_COL;
		g2.drawOval(c, row, CIRCLE_WIDTH, CIRCLE_WIDTH);
		c = c + CIRCLE_WIDTH + STEP_COL;
		g2.drawString("M", c, row + 10);
		c = c + CIRCLE_WIDTH + STEP_COL;
		g2.drawOval(c, row, CIRCLE_WIDTH, CIRCLE_WIDTH);

	}

	private void drawVFLine() {
		int c = col;
		g2.drawString(String.valueOf(nro), c, row + 10);
		c = c + CIRCLE_WIDTH + STEP_COL * 2;

		g2.drawString("V", c, row + 10);
		c = c + CIRCLE_WIDTH + STEP_COL;
		g2.drawOval(c, row, CIRCLE_WIDTH, CIRCLE_WIDTH);
		c = c + CIRCLE_WIDTH + STEP_COL;
		g2.drawString("F", c, row + 10);
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
		c = c + CIRCLE_WIDTH + STEP_COL * 2;
		for (int k = 0; k < nroAlternativas; k++) {
			g2.drawString(TITLE_LETER[k], c, row);
			c = c + CIRCLE_WIDTH + STEP_COL;
		}
	}

	public static void main(String[] args) {
		ImpresionPrueba imp = new ImpresionPrueba();
		try {
			
			BufferedImage image = imp.draw();
			ShowImages.showWindow(image, "Prueba");
			
			ImageIO.write(image, "jpg", new File("./res/prueba.jpg"));
			
			PDDocument document = new PDDocument();
			PDPage page = new PDPage(PDPage.PAGE_SIZE_LETTER);
			
			PDJpeg img = new PDJpeg(document, image);
			
			PDPageContentStream contentStream = new PDPageContentStream(document, page);
			contentStream.drawImage(img, 0, 0);
			contentStream.close();
			document.addPage(page);	
			try {
				document.save(new File("./res/prueba.pdf"));
			} catch (COSVisitorException e) {
				e.printStackTrace();
			}

			document.close();
			
			
			PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		    pras.add(new Copies(1));
		    PrintService pss[] = PrintServiceLookup.lookupPrintServices(DocFlavor.INPUT_STREAM.JPEG, pras);
		    if (pss.length == 0)
		      throw new RuntimeException("No printer services available.");
		    PrintService ps = pss[pss.length - 1];
		    System.out.println("Printing to " + ps);
		    DocPrintJob job = ps.createPrintJob();
		    FileInputStream fin = new FileInputStream(new File("./res/prueba.jpg"));
		    Doc doc = new SimpleDoc(fin, DocFlavor.INPUT_STREAM.JPEG, null);
		    try {
				job.print(doc, pras);
			} catch (PrintException e) {
				e.printStackTrace();
			}
		    fin.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
