package cl.eos.detection.boofcv;

import georegression.struct.line.LineSegment2D_F32;
import georegression.struct.point.Point2D_I32;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.ExplicitGroup;

import boofcv.abst.feature.detect.line.DetectLineSegmentsGridRansac;
import boofcv.alg.distort.DistortImageOps;
import boofcv.alg.distort.PixelTransformAffine_F32;
import boofcv.alg.distort.impl.DistortSupport;
import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.Contour;
import boofcv.alg.filter.binary.ThresholdImageOps;
import boofcv.alg.misc.ImageStatistics;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.factory.feature.detect.line.FactoryDetectLineAlgs;
import boofcv.gui.binary.VisualizeBinaryData;
import boofcv.gui.feature.ImageLinePanel;
import boofcv.gui.image.ShowImages;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.ConnectRule;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageInt8;
import boofcv.struct.image.ImageSInt32;
import boofcv.struct.image.ImageUInt8;
import boofcv.struct.image.MultiSpectral;

/**
 * Demonstrates how to create binary images by thresholding, applying binary
 * morphological operations, and then extracting detected features by finding
 * their contours.
 *
 *  Imagen escaneada en una resolucion de 300dpi.
 *  1)
 *  1.l) El primer rectangulo oscuro está en 58,1541
 *  1.2) La primera columna de circulo a leer está en 290, 1528 y la segunda en 710, 1528
 *  1.3) Los circulos tienen una dimension de 44 pixeles
 *  1.4) La separacion entre ciruclos de un bloque es 6 pixeles.
 *  
 *  2)
 *  2.1) 58,1541 (+335)
 *  2.2) 58,1876 (+334)
 *  2.3) 58,2210 (+334)
 *  2.4) 58,2544 (+333)
 *  2.5) 58,2877
 *
 * @author Peter Abeles
 */
public class ExampleBinaryOps {

	public static void main(String args[]) {
		for (int m = 5; m < 6; m++) {
			BufferedImage limage = UtilImageIO.loadImage("./res/prueba_00" + m
					+ ".png");
			
			BufferedImage image = limage.getSubimage(0, 1450, 150, 1700);

			ImageFloat32 input = ConvertBufferedImage.convertFromSingle(image,
					null, ImageFloat32.class);

			ImageUInt8 binary = new ImageUInt8(input.width, input.height);
			ImageSInt32 label = new ImageSInt32(input.width, input.height);
			ThresholdImageOps.threshold(input, binary, (float) 145, true);
			ImageUInt8 filtered = BinaryImageOps.erode8(binary, 8, null);
			filtered = BinaryImageOps.dilate8(filtered, 8, null);

			List<Contour> contours = BinaryImageOps.contour(filtered,
					ConnectRule.EIGHT, label);

			int[] x = new int[contours.size()];
			int[] y = new int[contours.size()];
			int n = 0;
			for (Contour contour : contours) {
				int minX = Integer.MAX_VALUE;
				int minY = Integer.MAX_VALUE;
				for (Point2D_I32 point : contour.external) {
					if (point.x < minX) {
						minX = point.x;
					}
					if (point.y < minY) {
						minY = point.y;
					}
				}
				x[n] = minX;
				y[n] = minY;
				n++;
			}

			int dx = x[n - 1] - x[0];
			int dy = y[n - 1] - y[0];

			double radian = Math.PI / 2f - Math.atan2(dy, dx);
//			double radian = 2 * Math.PI + (Math.PI / 2f - Math.atan2(dy, dx));

			System.out.println("Angulo=" + radian);
			int colorExternal = 0xFFFFFF;
			int colorInternal = 0xFF2020;

			BufferedImage visualContour = VisualizeBinaryData.renderContours(
					contours, colorExternal, colorInternal, input.width,
					input.height, null);

			Graphics2D g = (Graphics2D) visualContour.getGraphics();
			g.setColor(Color.RED);
			g.drawPolyline(x, y, contours.size());
			g.dispose();
			try {
				ImageIO.write(visualContour, "png", new File("./res/output" + m
						+ ".png"));
	            BufferedImage img = ExampleBinaryOps.rotate(radian, limage);
	            ImageIO.write(img, "png", new File("./res/rotada" + m +".png"));

			} catch (IOException e) {
				e.printStackTrace();
			}
			

//			ShowImages.showWindow(img, "Rotada");
			
		}
		// ShowImages.showWindow(visualContour, "Contours");
	}
	
	   public static BufferedImage rotate(double angle, BufferedImage image) {
	        int drawLocationX = 0;
	        int drawLocationY = 0;

	        // Valores para la rotación.
	        double rotationRequired = angle;
	        double locationX = image.getWidth() / 2;
	        double locationY = image.getHeight() / 2;
	        AffineTransform tx = AffineTransform.getRotateInstance(
	                rotationRequired, locationX, locationY);
	        AffineTransformOp op = new AffineTransformOp(tx,
	                AffineTransformOp.TYPE_BICUBIC);
	        BufferedImage result = new BufferedImage(image.getWidth(),
	                image.getHeight(), image.getType());
	        Graphics2D g2d = (Graphics2D) result.getGraphics();

	        // Se dibuja la imagen rotada.
	        g2d.drawImage(op.filter(image, null), drawLocationX, drawLocationY,
	                null);
	        return result;
	    }
	
}
