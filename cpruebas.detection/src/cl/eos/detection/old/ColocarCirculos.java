package cl.eos.detection.old;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.ThresholdImageOps;
import boofcv.alg.misc.ImageStatistics;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.gui.binary.VisualizeBinaryData;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageUInt8;


public class ColocarCirculos {

  private static final int CIRCLE_WIDTH = 45;
  private static final int CIRCLE_HEIGHT = 48;
  
  public static int[] X = {2, 60, 119, 178, 239};

  public static void main(String[] args) throws IOException {
    String fileName = "./res/prueba 002/resp42.png";
    BufferedImage image = ImageIO.read(new File(fileName));

    ImageFloat32 input = ConvertBufferedImage.convertFromSingle(image, null, ImageFloat32.class);
    ImageUInt8 binary = new ImageUInt8(input.width, input.height);
    ThresholdImageOps.threshold(input, binary, (float) 200, false);
    ImageUInt8 output = BinaryImageOps.erode4(binary, 2, null);
    output = BinaryImageOps.dilate4(output, 6, null);
    output = BinaryImageOps.erode4(output, 3, null);
    output = BinaryImageOps.dilate4(output, 10, null);
    output = BinaryImageOps.erode4(output, 10, null);
    BufferedImage bImage = VisualizeBinaryData.renderBinary(output, null);



    Graphics2D g2d = (Graphics2D) bImage.getGraphics();
    g2d.setColor(Color.BLACK);
    Ellipse2D ellipse = new Ellipse2D.Float();
    for (int n = 0; n < 5; n++) {
      ellipse.setFrame(0, 0, 45, 48);
      
      int x = X[n];
      int y = 1;
      int w = CIRCLE_WIDTH;
      int h = CIRCLE_HEIGHT;
      
      BufferedImage subImage = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
      Graphics2D g = subImage.createGraphics();
      g.setColor(new Color(255, 255, 255, 0));
      g.fillRect(0, 0, w, h);
      g.setClip(ellipse);
      g.drawImage(bImage, -x, -y, null);
      g.dispose();
      ImageFloat32 img32 = ConvertBufferedImage.convertFromSingle(subImage, null, ImageFloat32.class);
      double value = ImageStatistics.mean(img32);
      if(value < 180)
      {
        g2d.fillOval(X[n], 1, 45, 48);
      }
      
    }
  }
}
