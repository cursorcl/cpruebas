package cl.eos.detection.marvin;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;

public class FormCorners {

public FormCorners(){
    // Load plug-in
    MarvinImagePlugin moravec = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.corner.moravec");
    MarvinAttributes attr = new MarvinAttributes();

    // Load image
    MarvinImage image = MarvinImageIO.loadImage("./res/002.png");

    // Process and save output image
    moravec.setAttribute("threshold", 2000);
    moravec.process(image, null, attr);
    Point[] boundaries = boundaries(attr);
    image = showCorners(image, boundaries, 4);
    MarvinImageIO.saveImage(image, "./res/002_output.png");

    // Print rotation angle
    double angle =  (Math.atan2((boundaries[1].y*-1)-(boundaries[0].y*-1),boundaries[1].x-boundaries[0].x) * 180 / Math.PI);
    angle =  angle >= 0 ? angle : angle + 360;
    System.out.println("Rotation angle:"+angle);
    rotate(angle, image.getBufferedImage() );
}


private void rotate(double angle, BufferedImage image)
{
  int drawLocationX = 0;
  int drawLocationY = 0;

  // Valores para la rotación.
  double rotationRequired = Math.toRadians(angle);
  double locationX = image.getWidth() / 2;
  double locationY = image.getHeight() / 2;
  AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
  AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
  BufferedImage result =  new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
  Graphics2D g2d = (Graphics2D) result.getGraphics();
  
  //Se dibuja la imagen rotada.
  g2d.drawImage(op.filter(image, null), drawLocationX, drawLocationY, null);
  
  
  // Valores para el escalamiento.
  double xScale = 100/image.getWidth();
  double yScale = 300/image.getHeight();
  double theScale = xScale > yScale ? xScale : yScale;
  AffineTransform transform = new AffineTransform();
  transform.setToScale( theScale , theScale );
  tx.concatenate(transform);
  
  try {
    ImageIO.write(result, "png", new File("./res/output.png"));
  } catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  }
  
}

private Point[] boundaries(MarvinAttributes attr){
    Point upLeft = new Point(-1,-1);
    Point upRight = new Point(-1,-1);
    Point bottomLeft = new Point(-1,-1);
    Point bottomRight = new Point(-1,-1);
    double ulDistance=9999,blDistance=9999,urDistance=9999,brDistance=9999;
    double tempDistance=-1;
    int[][] cornernessMap = (int[][]) attr.get("cornernessMap");

    for(int x=0; x<cornernessMap.length; x++){
        for(int y=0; y<cornernessMap[0].length; y++){
            if(cornernessMap[x][y] > 0){
                if((tempDistance = Point.distance(x, y, 0, 0)) < ulDistance){
                    upLeft.x = x; upLeft.y = y;
                    ulDistance = tempDistance;
                } 
                if((tempDistance = Point.distance(x, y, cornernessMap.length, 0)) < urDistance){
                    upRight.x = x; upRight.y = y;
                    urDistance = tempDistance;
                }
                if((tempDistance = Point.distance(x, y, 0, cornernessMap[0].length)) < blDistance){
                    bottomLeft.x = x; bottomLeft.y = y;
                    blDistance = tempDistance;
                }
                if((tempDistance = Point.distance(x, y, cornernessMap.length, cornernessMap[0].length)) < brDistance){
                    bottomRight.x = x; bottomRight.y = y;
                    brDistance = tempDistance;
                }
            }
        }
    }
    return new Point[]{upLeft, upRight, bottomRight, bottomLeft};
}

private MarvinImage showCorners(MarvinImage image, Point[] points, int rectSize){
    MarvinImage ret = image.clone();
    for(Point p:points){
        ret.fillRect(p.x-(rectSize/2), p.y-(rectSize/2), rectSize, rectSize, Color.red);
    }
 
    return ret;
}

public static void main(String[] args) {
    new FormCorners();
}
}