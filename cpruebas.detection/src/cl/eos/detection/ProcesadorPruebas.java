package cl.eos.detection;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import marvin.image.MarvinImage;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;
import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.Contour;
import boofcv.alg.filter.binary.ThresholdImageOps;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.gui.binary.VisualizeBinaryData;
import boofcv.gui.image.ShowImages;
import boofcv.struct.ConnectRule;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageSInt32;
import boofcv.struct.image.ImageUInt8;

public class ProcesadorPruebas {

  
	private MarvinImagePlugin moravec;

	/**
	 * Estos valores corresponden a la distancia en x e y de las marcas de los
	 * bordes.
	 */
	private double width = 618;
	private double height = 768;

	public ProcesadorPruebas() {
		moravec = MarvinPluginLoader
				.loadImagePlugin("org.marvinproject.image.corner.moravec");
	}

	public OTResultadoScanner process(File file)
	{
	  OTResultadoScanner result = new OTResultadoScanner();
	  try {
      BufferedImage image = ImageIO.read(file);
      process(image);
    } catch (IOException e) {
      e.printStackTrace();
    }
	  
	  return result;
	}
	private List<Contour> process(BufferedImage image) {
		BufferedImage imgScaled = rectifyScale(image);
		BufferedImage imgRotated = imgScaled; //rectifyRotation(imgScaled);

		ImageFloat32 input = ConvertBufferedImage.convertFromSingle(imgRotated,
				null, ImageFloat32.class);

		ImageUInt8 binary = new ImageUInt8(input.width, input.height);
		ImageSInt32 label = new ImageSInt32(input.width, input.height);
		ThresholdImageOps.threshold(input, binary, (float) 145, true);
		binary  = BinaryImageOps.dilate4(binary, 1, null);
//		ImageUInt8 nBinary = BinaryImageOps.removePointNoise(binary, null);
		ImageUInt8 eroded = BinaryImageOps.erode4(binary, 4, null);
		ImageUInt8 filtered = BinaryImageOps.dilate4(eroded, 3, null);
		List<Contour> contours = BinaryImageOps.contour(filtered,
				ConnectRule.FOUR, label);

		int colorExternal = 0xFFFFFF;
		int colorInternal = 0xFF2020;
		BufferedImage visualContour = VisualizeBinaryData.renderContours(
				contours, colorExternal, colorInternal, input.width,
				input.height, null);

//		if (contours != null && !contours.isEmpty()) {
//			List<Path2D> paths = new ArrayList<Path2D>();
//			for (Contour contour : contours) {
//				Path2D path = new Path2D.Double();
//
//				for (int n = 0; n < contour.external.size(); n++) {
//					Point2D_I32 point = contour.external.get(n);
//					if (n == 0) {
//						path.moveTo(point.x, point.y);
//					} else {
//						path.lineTo(point.x, point.y);
//					}
//				}
//				path.closePath();
//				paths.add(path);
//			}
//			Graphics2D g2D = (Graphics2D) imgRotated.getGraphics();
//			g2D.setColor(Color.red);
//			for (Path2D pth : paths) {
//				g2D.draw(pth);
//			}
			
//		}
		try {
			//ImageIO.write(imgRotated, "png", new File("./res/rotada.png"));
			ImageIO.write(visualContour, "png", new File("./res/contornos.png"));
			
			BufferedImage thresold =  VisualizeBinaryData.renderBinary(binary, null);
			BufferedImage erode =  VisualizeBinaryData.renderBinary(eroded, null);
			BufferedImage filter =  VisualizeBinaryData.renderBinary(filtered, null);
			
			ShowImages.showWindow(imgRotated, "ORIGEN");
			ShowImages.showWindow(thresold, "THRESOLD");
			ShowImages.showWindow(erode, "ERODE");
			ShowImages.showWindow(filter, "DILATE");
			ShowImages.showWindow(visualContour, "CONTOURS");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ShowImages.showWindow(visualContour, "Contours");
		return contours;
	}

	/**
	 * Se implemente la diferencia entre las dos imagenes, se asume ambas
	 * imagenes de la misma medid. Imagen (blanca - actual + anterior)
	 * 
	 * @param actual
	 *            Imagen actual.
	 * @param anterior
	 *            Imagen anterior.
	 * @return Diferencia entre las dos imagenes.
	 */
	// private BufferedImage diference(BufferedImage actual, BufferedImage
	// anterior) {
	public void diference() {
		BufferedImage actual;
		BufferedImage anterior;
		try {
			actual = ImageIO.read(new File("./res/actual.png"));
			BufferedImage bfActual = new BufferedImage(746, 968,
					actual.getType());
			Graphics2D g2 = (Graphics2D) bfActual.getGraphics();
			g2.drawImage(actual, 0, 0, 745, 967, null);
			actual = rectifyRotation(bfActual);

			anterior = ImageIO.read(new File("./res/cpruebas.png"));

			ImageFloat32 iActual = ConvertBufferedImage.convertFromSingle(
					actual, null, ImageFloat32.class);
			ImageUInt8 bActual = new ImageUInt8(iActual.width, iActual.height);

			ImageFloat32 iAnterior = ConvertBufferedImage.convertFromSingle(
					anterior, null, ImageFloat32.class);
			ImageUInt8 bAnterior = new ImageUInt8(iAnterior.width,
					iAnterior.height);

			ThresholdImageOps.threshold(iActual, bActual, (float) 145, true);
			ThresholdImageOps
					.threshold(iAnterior, bAnterior, (float) 145, true);

			ImageUInt8 output = new ImageUInt8(iAnterior.width,
					iAnterior.height);
			ImageUInt8 ioutput = new ImageUInt8(iAnterior.width,
					iAnterior.height);
			for (int n = 0; n < bActual.data.length; n++) {
				output.data[n] = (byte) (bActual.data[n] - bAnterior.data[n]);
				ioutput.data[n] = (byte) (iActual.data[n] - iAnterior.data[n]);
			}

			List<Contour> contours = BinaryImageOps.contour(output,
					ConnectRule.EIGHT, null);

			int colorExternal = 0xFFFFFF;
			int colorInternal = 0xFF2020;
			BufferedImage visualContour = VisualizeBinaryData.renderContours(
					contours, colorExternal, colorInternal, output.width,
					output.height, null);

			BufferedImage resta = VisualizeBinaryData
					.renderBinary(output, null);
			BufferedImage iresta = VisualizeBinaryData.renderBinary(ioutput,
					null);
			ShowImages.showWindow(resta, "Resta Binaria");
			ShowImages.showWindow(iresta, "Resta");
			ShowImages.showWindow(visualContour, "Contornos");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return res;
	}

	private BufferedImage rectifyScale(MarvinImage mImage) {
		Double[] factors = getFactorsScaling(mImage);
		return scale(factors[0], factors[1], mImage);
	}

	private BufferedImage rectifyScale(BufferedImage bImage) {

		BufferedImage rImage = new BufferedImage(746, 968, bImage.getType());
		Graphics2D g2 = (Graphics2D) rImage.getGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2.drawImage(bImage, 0, 0, rImage.getWidth() - 1,
				rImage.getHeight() - 1, null);
		return rImage;
	}

	private Double[] getFactorsScaling(MarvinImage mImage) {

		MarvinAttributes attr = new MarvinAttributes();
		moravec.setAttribute("threshold", 2000);
		moravec.process(mImage, null, attr);
		// {upLeft, upRight, bottomRight, bottomLeft}
		Point[] bounds = boundaries(attr);
		int lWidth = bounds[1].x - bounds[0].x;
		int lHeight = bounds[3].y - bounds[0].y;

		Double[] factors = new Double[2];

		factors[0] = width / (double) lWidth;
		factors[1] = height / (double) lHeight;

		return factors;
	}

	private BufferedImage rectifyRotation(MarvinImage mImage) {
		double angle = getAngleRotation(mImage);
		return rotate(angle, mImage.getBufferedImage());
	}

	private BufferedImage rectifyRotation(BufferedImage mImage) {
		double angle = getAngleRotation(mImage);
		return rotate(angle, mImage);
	}

	/**
	 * Obtiene el angulo de rotacion de la imagen. Debe encontrar las marcas
	 * realizadas a la hoja.
	 * 
	 * @param image
	 *            La imagen que se va a procesar.
	 * @return El angulo de correción a aplicar a la imagen.
	 */
	private double getAngleRotation(MarvinImage image) {
		MarvinAttributes attr = new MarvinAttributes();

		moravec.setAttribute("threshold", 2000);
		moravec.process(image, null, attr);

		Point[] boundaries = boundaries(attr);
		return (Math.atan2((boundaries[1].y * -1) - (boundaries[0].y * -1),
				boundaries[1].x - boundaries[0].x) * 180 / Math.PI);

	}

	private double getAngleRotation(BufferedImage bImage) {
		MarvinAttributes attr = new MarvinAttributes();
		MarvinImage image = new MarvinImage(bImage);
		moravec.setAttribute("threshold", 2000);
		moravec.process(image, null, attr);

		Point[] boundaries = boundaries(attr);
		return (Math.atan2((boundaries[1].y * -1) - (boundaries[0].y * -1),
				boundaries[1].x - boundaries[0].x) * 180 / Math.PI);

	}

	/**
	 * Obtiene lo puntos externos de la prueba de las cuatro esquinas. Para ello
	 * la prueba debe tener las marcas en cada esquina.
	 * 
	 * @param attr
	 *            Atributos del Framework Marvin
	 * @return Puntos del rectángulo externo.
	 */
	private Point[] boundaries(MarvinAttributes attr) {
		Point upLeft = new Point(-1, -1);
		Point upRight = new Point(-1, -1);
		Point bottomLeft = new Point(-1, -1);
		Point bottomRight = new Point(-1, -1);
		double ulDistance = 9999, blDistance = 9999, urDistance = 9999, brDistance = 9999;
		double tempDistance = -1;
		int[][] cornernessMap = (int[][]) attr.get("cornernessMap");

		for (int x = 0; x < cornernessMap.length; x++) {
			for (int y = 0; y < cornernessMap[0].length; y++) {
				if (cornernessMap[x][y] > 0) {
					if ((tempDistance = Point.distance(x, y, 0, 0)) < ulDistance) {
						upLeft.x = x;
						upLeft.y = y;
						ulDistance = tempDistance;
					}
					if ((tempDistance = Point.distance(x, y,
							cornernessMap.length, 0)) < urDistance) {
						upRight.x = x;
						upRight.y = y;
						urDistance = tempDistance;
					}
					if ((tempDistance = Point.distance(x, y, 0,
							cornernessMap[0].length)) < blDistance) {
						bottomLeft.x = x;
						bottomLeft.y = y;
						blDistance = tempDistance;
					}
					if ((tempDistance = Point.distance(x, y,
							cornernessMap.length, cornernessMap[0].length)) < brDistance) {
						bottomRight.x = x;
						bottomRight.y = y;
						brDistance = tempDistance;
					}
				}
			}
		}
		return new Point[] { upLeft, upRight, bottomRight, bottomLeft };
	}

	private BufferedImage rotate(double angle, BufferedImage image) {
		int drawLocationX = 0;
		int drawLocationY = 0;

		// Valores para la rotación.
		double rotationRequired = Math.toRadians(angle);
		double locationX = image.getWidth() / 2;
		double locationY = image.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(
				rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx,
				AffineTransformOp.TYPE_BILINEAR);
		BufferedImage result = new BufferedImage(image.getWidth(),
				image.getHeight(), image.getType());
		Graphics2D g2d = (Graphics2D) result.getGraphics();

		// Se dibuja la imagen rotada.
		g2d.drawImage(op.filter(image, null), drawLocationX, drawLocationY,
				null);
		return result;
	}

	/**
	 * Se realiza el escalamiento de la imagen en X e Y independientemente.
	 * 
	 * @param factorX
	 *            Factor de escalamiento en X.
	 * @param factorY
	 *            Factor de escalamiento en Y.
	 * @param image
	 *            Imagen que se quiere escalar.
	 * @return Imagen escalada.
	 */
	private BufferedImage scale(double factorX, double factorY,
			MarvinImage image) {
		AffineTransform transform = new AffineTransform();
		transform.scale(factorX, factorY);
		AffineTransformOp op = new AffineTransformOp(transform,
				AffineTransformOp.TYPE_BILINEAR);

		BufferedImage result = new BufferedImage(
				(int) (image.getWidth() * factorX),
				(int) (image.getHeight() * factorY), image.getType());
		Graphics2D g2d = (Graphics2D) result.getGraphics();
		// Se dibuja la imagen escalada.
		g2d.drawImage(op.filter(image.getBufferedImage(), null), 0, 0, null);
		return result;
	}

	public static void main(String[] args) {
		ProcesadorPruebas processor = new ProcesadorPruebas();
		// processor.diference();
		BufferedImage image;
		try {
			Point2D p = new Point2D.Double(76.0, 61.0);
			image = ImageIO.read(new File("./res/actual.png"));
			List<Contour> contours = processor.process(image);
			// List<Path2D> paths = new ArrayList<Path2D>();
			// if (contours != null && !contours.isEmpty()) {
			// for (Contour contour : contours) {
			// Path2D path = new Path2D.Double();
			//
			// path.setWindingRule(Path2D.WIND_EVEN_ODD);
			// for (int n = 0; n < contour.external.size(); n++) {
			// Point2D_I32 point = contour.external.get(n);
			// if (n == 0) {
			// path.moveTo(point.x, point.y);
			// } else {
			// path.lineTo(point.x, point.y);
			// }
			// }
			// path.closePath();
			// paths.add(path);
			// }
			// BufferedImage draw = ImageIO.read(new File("./res/actual.png"));
			// Graphics2D g2D = (Graphics2D) draw.getGraphics();
			// g2D.setColor(Color.BLUE);
			// g2D.drawRect((int) (p.getX() - 1), (int) (p.getY() - 1), 2, 2);
			// g2D.setColor(Color.red);
			// for (Path2D path : paths) {
			// g2D.draw(path);
			// if (path.contains(p)) {
			// System.out.println("Encontrado:" + path);
			// }
			// }
			// ImageIO.write(draw, "png", new File("./res/test.png"));
			// ShowImages.showWindow(draw, "ejemplo");
			//
			// }
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
