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
import javax.print.attribute.ResolutionSyntax;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.PrintQuality;
import javax.print.attribute.standard.PrinterResolution;

public class PrintActionListener implements Runnable {

    private class ImagePrintable implements Printable {

        private final double x, y, width;
        private final int orientation;
        private final BufferedImage image;

        ImagePrintable(PrinterJob printJob, BufferedImage image) {

            final PageFormat pageFormat = printJob.defaultPage();
            x = pageFormat.getImageableX();
            y = pageFormat.getImageableY();
            width = pageFormat.getImageableWidth();
            orientation = pageFormat.getOrientation();
            this.image = image;
        }

        @Override
        public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {

            final Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
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
                return Printable.PAGE_EXISTS;
            } else {
                return Printable.NO_SUCH_PAGE;
            }
        }

    }

    private final BufferedImage image;

    public PrintActionListener(BufferedImage image) {
        this.image = image;
    }

    @Override
    public void run() {
        final PrintRequestAttributeSet printAttributes = new HashPrintRequestAttributeSet();
        printAttributes.add(PrintQuality.HIGH);
        printAttributes.add(new MediaPrintableArea(0f, 0f, 241.3f, 279.4f, MediaPrintableArea.MM));
        printAttributes.add(new PrinterResolution(600, 600, ResolutionSyntax.DPI));
        final PrinterJob printJob = PrinterJob.getPrinterJob();

        printJob.setPrintable(new ImagePrintable(printJob, image), printJob.getPageFormat(printAttributes));
        if (printJob.printDialog(printAttributes)) {
            try {
                printJob.print(printAttributes);
            } catch (final PrinterException prt) {
            }
        }
    }

}
