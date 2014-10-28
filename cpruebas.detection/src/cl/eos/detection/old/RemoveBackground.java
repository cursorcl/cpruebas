package cl.eos.detection.old;

import marvin.image.MarvinColorModelConverter;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import marvin.math.MarvinMath;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinPluginLoader;

public class RemoveBackground {

  public RemoveBackground(){
      // 1. Load plug-ins
      MarvinImagePlugin erode = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.morphological.erosion");
      MarvinImagePlugin dilate = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.morphological.dilation");
      MarvinImagePlugin invert = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.color.invert");

      // 2. Set plug-ins attributes
      boolean[][] m = MarvinMath.getTrueMatrix(15,15);
      erode.setAttribute("matrix", m);
      dilate.setAttributes("matrix", m);

      // 3. Load and process the image
      MarvinImage image = MarvinImageIO.loadImage("./res/prueba_002.png");
      invert.process(image.clone(), image);
      MarvinImage binImage = MarvinColorModelConverter.rgbToBinary(image, 127);
      MarvinImageIO.saveImage(binImage, "./res/prueba_002_bin.png");
      erode.process(binImage.clone(), binImage);
      dilate.process(binImage.clone(), binImage);
      MarvinImageIO.saveImage(binImage, "./res/prueba_002_out.png");
  }

  public static void main(String[] args) {
      new RemoveBackground();
  }
}
