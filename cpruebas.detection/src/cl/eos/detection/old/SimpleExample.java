package cl.eos.detection.old;

import marvin.image.MarvinColorModelConverter;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import marvin.math.MarvinMath;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinPluginLoader;

public class SimpleExample {

  private MarvinImagePlugin eordePlugin;
  private MarvinImagePlugin dilatePlugin;
  private MarvinImagePlugin dilateInicialPlugin;

  public SimpleExample() {

    // 1. Load and set up plug-in.
    
    eordePlugin =
        MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.morphological.erosion");
    
    dilatePlugin = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.morphological.dilation");
    dilateInicialPlugin = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.morphological.dilation");
    
    //boolean[][] m = new boolean[][] { {true, true, true}, {true, true, true}, {true, true, true}};
    // 2. Set plug-ins attributes
    boolean[][] mi = MarvinMath.getTrueMatrix(3,3);
    boolean[][] m = MarvinMath.getTrueMatrix(15,15);
    
    eordePlugin.setAttribute("matrix", m);
    dilatePlugin.setAttributes("matrix", m);
    dilateInicialPlugin.setAttributes("matrix", mi);
    

    // 2. Load image
    MarvinImage image = MarvinImageIO.loadImage("./res/prueba02.jpg");
    MarvinImage resultImage = MarvinColorModelConverter.rgbToBinary(image, 127);

    // 3. Process and save image
    dilateInicialPlugin.process(resultImage.clone(), resultImage);
    eordePlugin.process(resultImage.clone(), resultImage);
    dilatePlugin.process(resultImage.clone(), resultImage);
    
    resultImage = MarvinColorModelConverter.binaryToRgb(resultImage);
    resultImage.update();
    MarvinImageIO.saveImage(resultImage, "./res/erosion_out.png");
  }

  public static void main(String[] args) {
    new SimpleExample();
  }

}
