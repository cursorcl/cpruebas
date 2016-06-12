package cl.eos.detection;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.PrintQuality;
import javax.print.attribute.standard.PrinterResolution;

public class PrintActionListener implements Runnable {

	private final BufferedImage image;

	public PrintActionListener(BufferedImage image) {
		this.image = image;
	}

	@Override
	public void run() {
		PrintRequestAttributeSet printAttributes = new HashPrintRequestAttributeSet();
	    printAttributes.add(PrintQuality.HIGH);
	    printAttributes.add(new MediaPrintableArea(0f, 0f, 241.3f, 279.4f, MediaPrintableArea.MM));
	    printAttributes.add(new PrinterResolution(600, 600, PrinterResolution.DPI)); 
		PrinterJob printJob = PrinterJob.getPrinterJob();
		
		printJob.setPrintable(new ImagePrintable(printJob, image), printJob.getPageFormat(printAttributes));
		if (printJob.printDialog(printAttributes)) {
			try {
				printJob.print(printAttributes);
			} catch (PrinterException prt) {
			}
		}
	}

	private class ImagePrintable implements Printable {

		private final double x, y, width;
		private final int orientation;
		private final BufferedImage image;

		ImagePrintable(PrinterJob printJob, BufferedImage image) {
			
			PageFormat pageFormat = printJob.defaultPage();
			this.x = pageFormat.getImageableX();
			this.y = pageFormat.getImageableY();
			this.width = pageFormat.getImageableWidth();
			this.orientation = pageFormat.getOrientation();
			this.image = image;
		}

		@Override
		public int print(Graphics g, PageFormat pageFormat, int pageIndex)
				throws PrinterException {
				
			Graphics2D g2d = (Graphics2D) g; 
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
			                     RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			if (pageIndex == 0) {
				int pWidth;
				int pHeight;
				if (orientation == PageFormat.PORTRAIT) {
					pWidth = (int) Math.min(width, image.getWidth());
					pHeight = pWidth * image.getHeight() / image.getWidth();
				} else {
					pHeight = (int) Math.min(width, image.getHeight());
					pWidth = pHeight * image.getWidth() / image.getHeight();
				}
				g.drawImage(image, (int) x, (int) y, pWidth, pHeight, null);
				return PAGE_EXISTS;
			} else {
				return NO_SUCH_PAGE;
			}
		}

	}
	
}
