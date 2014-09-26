package cl.eos.detection;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.net.URL;
 
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
 
public class ZoomPanel extends JPanel {
 
 private BufferedImage originalImage;
 
 public ZoomPanel(String pathToImage) {
  originalImage = toBufferedImage(createImage(pathToImage).getImage());
  initPanel();
 }
 
 public ZoomPanel(BufferedImage image)
 {
	 originalImage = image;
	 initPanel();
 }
 private void initPanel(){
  final Zoom zoom = new Zoom();
 
  JButton plus = new JButton(new AbstractAction("+") {
 
   @Override
   public void actionPerformed(ActionEvent e) {
    zoom.increaseZoom();
   }
  });
  JButton minus = new JButton(new AbstractAction("-") {
 
   @Override
   public void actionPerformed(ActionEvent e) {
    zoom.decreaseZoom();
   }
  });
 
 
  this.setLayout(new BorderLayout());
  this.add(minus, BorderLayout.WEST);
  this.add(zoom, BorderLayout.CENTER);
  this.add(plus, BorderLayout.EAST);
 
 }
 
 private class Zoom extends JPanel {
 
  private float xScaleFactor = 1;
  private float yScaleFactor = 1;
 
  public void increaseZoom() {
   xScaleFactor+= 0.1;
   yScaleFactor+= 0.1;
   repaint();
  }
 
  public void decreaseZoom() {
   xScaleFactor-= 0.1;
   yScaleFactor-= 0.1;
   repaint();
  }
 
 
  @Override
  public void paintComponent(Graphics g) {
   Graphics2D g2 = (Graphics2D) g;
   int newW = (int) (originalImage.getWidth() * xScaleFactor);
   int newH = (int) (originalImage.getHeight() * yScaleFactor);
   g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
   g2.clearRect(0, 0, getWidth(), getHeight());
   g2.drawImage(originalImage, 0, 0, newW, newH, null);
  }
 
 }
 
 private ImageIcon createImage(String path) {
  URL imgURL = getClass().getResource(path);
     if (imgURL != null) {
         return new ImageIcon(imgURL);
     } else {
         System.err.println("Couldn't find file: " + path);
         return null;
     }
 }
 
 
 /**
  * Obtiene una BufferedImage a partir de una Image.
  * return BufferredImage bi
  */
 public static BufferedImage toBufferedImage(Image image) {
  if (image instanceof BufferedImage) {
   return (BufferedImage) image;
  }
 
  image = new ImageIcon(image).getImage();
 
  boolean hasAlpha = hasAlpha(image);
 
  BufferedImage bimage = null;
  GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
  try {
 
   int transparency = Transparency.OPAQUE;
   if (hasAlpha) {
    transparency = Transparency.BITMASK;
   }
 
   GraphicsDevice gs = ge.getDefaultScreenDevice();
   GraphicsConfiguration gc = gs.getDefaultConfiguration();
   bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
  } catch (HeadlessException e) {
   // The system does not have a screen
  }
 
  if (bimage == null) {
   int type = BufferedImage.TYPE_INT_RGB;
   if (hasAlpha) {
    type = BufferedImage.TYPE_INT_ARGB;
   }
   bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
  }
 
  Graphics g = bimage.createGraphics();
 
  g.drawImage(image, 0, 0, null);
  g.dispose();
 
  return bimage;
 }
 
 /**
  * Devuelve true si una imagen tiene pixeles transparentes.
  *
  * @param image a testear
  * @return true si tiene pixeles transparentes
  */
 public static boolean hasAlpha(Image image) {
 
  if (image instanceof BufferedImage) {
   BufferedImage bimage = (BufferedImage) image;
   return bimage.getColorModel().hasAlpha();
  }
 
  PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
  try {
   pg.grabPixels();
  } catch (InterruptedException e) {
  }
 
  ColorModel cm = pg.getColorModel();
  return cm.hasAlpha();
 }
 
}