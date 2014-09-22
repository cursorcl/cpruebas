package cl.eos.detection;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
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
   * Estos valores corresponden a la distancia en x e y de las marcas de los bordes.
   */
  private double width = 618;
  private double height = 768;

  public ProcesadorPruebas() {
    moravec = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.corner.moravec");
  }


  public BufferedImage process(BufferedImage image) {
    MarvinImage mImage = new MarvinImage(image);
    BufferedImage imgRotated = rectifyRotation(mImage);
    BufferedImage imgScaled = rectifyScale(new MarvinImage(imgRotated));

    // convert into a usable format
    
    ImageFloat32 input = ConvertBufferedImage.convertFromSingle(imgScaled, null, ImageFloat32.class);
    
    ImageUInt8 binary = new ImageUInt8(input.width, input.height);
    ImageSInt32 label = new ImageSInt32(input.width, input.height);
    
    
    ThresholdImageOps.threshold(input, binary, (float) 145, true);
    ImageUInt8 nBinary = BinaryImageOps.removePointNoise(binary, null);
    ImageUInt8 filtered = BinaryImageOps.erode4(nBinary, 3, null);
    filtered = BinaryImageOps.dilate4(filtered, 3, null);
    List<Contour> contours = BinaryImageOps.contour(filtered, ConnectRule.EIGHT, label);
    int colorExternal = 0xFFFFFF;
    int colorInternal = 0xFF2020;
    BufferedImage visualContour =
        VisualizeBinaryData.renderContours(contours, colorExternal, colorInternal, input.width,
            input.height, null);
    
    ShowImages.showWindow(visualContour, "Contours");
    return visualContour;
  }


  
  private BufferedImage rectifyScale(MarvinImage mImage) {
    Double[] factors = getFactorsScaling(mImage);
    return scale(factors[0], factors[1], mImage);
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

  /**
   * Obtiene el angulo de rotacion de la imagen. Debe encontrar las marcas realizadas a la hoja.
   * 
   * @param image La imagen que se va a procesar.
   * @return El angulo de correción a aplicar a la imagen.
   */
  private double getAngleRotation(MarvinImage image) {
    MarvinAttributes attr = new MarvinAttributes();

    moravec.setAttribute("threshold", 2000);
    moravec.process(image, null, attr);

    Point[] boundaries = boundaries(attr);
    return (Math.atan2((boundaries[1].y * -1) - (boundaries[0].y * -1), boundaries[1].x
        - boundaries[0].x) * 180 / Math.PI);

  }

  /**
   * Obtiene lo puntos externos de la prueba de las cuatro esquinas. Para ello la prueba debe tener
   * las marcas en cada esquina.
   * 
   * @param attr Atributos del Framework Marvin
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
          if ((tempDistance = Point.distance(x, y, cornernessMap.length, 0)) < urDistance) {
            upRight.x = x;
            upRight.y = y;
            urDistance = tempDistance;
          }
          if ((tempDistance = Point.distance(x, y, 0, cornernessMap[0].length)) < blDistance) {
            bottomLeft.x = x;
            bottomLeft.y = y;
            blDistance = tempDistance;
          }
          if ((tempDistance = Point.distance(x, y, cornernessMap.length, cornernessMap[0].length)) < brDistance) {
            bottomRight.x = x;
            bottomRight.y = y;
            brDistance = tempDistance;
          }
        }
      }
    }
    return new Point[] {upLeft, upRight, bottomRight, bottomLeft};
  }


  private BufferedImage rotate(double angle, BufferedImage image) {
    int drawLocationX = 0;
    int drawLocationY = 0;

    // Valores para la rotación.
    double rotationRequired = Math.toRadians(angle);
    double locationX = image.getWidth() / 2;
    double locationY = image.getHeight() / 2;
    AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
    BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
    Graphics2D g2d = (Graphics2D) result.getGraphics();

    // Se dibuja la imagen rotada.
    g2d.drawImage(op.filter(image, null), drawLocationX, drawLocationY, null);
    return result;
  }

  /**
   * Se realiza el escalamiento de la imagen en X e Y independientemente.
   * 
   * @param factorX Factor de escalamiento en X.
   * @param factorY Factor de escalamiento en Y.
   * @param image Imagen que se quiere escalar.
   * @return Imagen escalada.
   */
  private BufferedImage scale(double factorX, double factorY, MarvinImage image) {
    AffineTransform transform = new AffineTransform();
    transform.scale(factorX, factorY);
    AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);

    BufferedImage result =
        new BufferedImage((int) (image.getWidth() * factorX), (int) (image.getHeight() * factorY),
            image.getType());
    Graphics2D g2d = (Graphics2D) result.getGraphics();
    // Se dibuja la imagen escalada.
    g2d.drawImage(op.filter(image.getBufferedImage(), null), 0, 0, null);
    return result;
  }

  public static void main(String[] args) {
      ProcesadorPruebas processor = new ProcesadorPruebas();
      BufferedImage image;
      try {
        image = ImageIO.read(new File("./res/002.png"));
        processor.process(image);
      } catch (IOException e) {
        e.printStackTrace();
      }
      
  }
}
