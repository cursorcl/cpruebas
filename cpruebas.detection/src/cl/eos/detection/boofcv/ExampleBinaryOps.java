package cl.eos.detection.boofcv;

import georegression.struct.line.LineSegment2D_F32;
import georegression.struct.point.Point2D_I32;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

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
 * @author Peter Abeles
 */
public class ExampleBinaryOps {

	public static void main(String args[]) {
		for (int m = 1; m < 5; m++) {
			BufferedImage limage = UtilImageIO.loadImage("./res/prueba_00" + m
					+ ".png");
			
			float  cx = limage.getWidth() / 2f;
			float cy = limage.getHeight() / 2f;
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

			double radian = Math.atan2(dy, dx);

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
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			MultiSpectral<ImageFloat32> distLeft =
					ConvertBufferedImage.convertFromMulti(limage, null,true, ImageFloat32.class);
			
			MultiSpectral<ImageFloat32> rectLeft = new MultiSpectral<ImageFloat32>(ImageFloat32.class,
					distLeft.getWidth(),distLeft.getHeight(),distLeft.getNumBands());
			DistortImageOps.rotate(distLeft, rectLeft, boofcv.alg.interpolate.TypeInterpolate.BICUBIC, radian);
			
		}
		// ShowImages.showWindow(visualContour, "Contours");
	}
}
