package cl.eos.detection.boofcv;

import georegression.struct.point.Point2D_I32;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.Contour;
import boofcv.alg.filter.binary.ThresholdImageOps;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.gui.binary.VisualizeBinaryData;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.ConnectRule;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageSInt32;
import boofcv.struct.image.ImageUInt8;

/**
 * Imagen escaneada en una resolucion de 300dpi. 
 * 1) 
 * 1.l) El primer rectangulo oscuro está en 58,1541 
 * 1.2) La primera columna de circulo a leer está en 290,1528 y la segunda en 710, 1528 
 * 1.3) Los circulos tienen una dimension de 44 pixeles 
 * 1.4) La separacion entre ciruclos de un bloque es 6 pixeles.
 * 
 * 2) 
 * 2.1) 58,1541 (+335) 
 * 2.2) 58,1876 (+334) 
 * 2.3) 58,2210 (+334) 
 * 2.4) 58,2544 (+333) 
 * 2.5) 58,2877
 *
 *	
 */
public class ExampleBinaryOps {

	public static void main(String args[]) {
		for (int m = 5; m < 6; m++) {
			BufferedImage limage = UtilImageIO.loadImage("./res/prueba_00" + m
					+ ".png");
			process(limage);
		}
	}

	public static void process(BufferedImage limage) {
		BufferedImage rotated = rectify(limage);
		Point[] points = getMarksInit(rotated);
	
		int x = points[0].x;
		int y = points[0].y;
		for(int n = 0; n < 5;  n++)
		{
			
		}
		
	}

	private static BufferedImage rectify(BufferedImage limage) {
		List<Contour> contours = getContours(limage);
		double anlge = getRotationAngle(contours);
		return ExampleBinaryOps.rotate(anlge, limage);
	}

	private static Point[] getMarksInit(BufferedImage image) {
		List<Contour> contours = getContours(image);
		Point[] points = new Point[contours.size()];
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
			points[n].x = minX;
			points[n].y = minY;
			n++;
		}
		return points;
	}

	private static double getRotationAngle(List<Contour> contours) {
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

		return Math.PI / 2f - Math.atan2(dy, dx);
	}

	public static List<Contour> getContours(BufferedImage limage) {
		BufferedImage image = limage.getSubimage(0, 1450, 150, 1700);
		ImageFloat32 input = ConvertBufferedImage.convertFromSingle(image,
				null, ImageFloat32.class);

		ImageUInt8 binary = new ImageUInt8(input.width, input.height);
		ImageSInt32 label = new ImageSInt32(input.width, input.height);
		ThresholdImageOps.threshold(input, binary, (float) 145, true);
		ImageUInt8 filtered = BinaryImageOps.erode8(binary, 8, null);
		filtered = BinaryImageOps.dilate8(filtered, 8, null);

		return getContours(filtered, label);

	}

	public static List<Contour> getContours(ImageUInt8 image) {
		return BinaryImageOps.contour(image, ConnectRule.EIGHT, null);
	}

	public static List<Contour> getContours(ImageUInt8 image, ImageSInt32 label) {
		return BinaryImageOps.contour(image, ConnectRule.EIGHT, label);
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
