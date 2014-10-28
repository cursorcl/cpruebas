package cl.eos.detection.old;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;

public class Logos {

  private MarvinImagePlugin threshold = MarvinPluginLoader
      .loadImagePlugin("org.marvinproject.image.color.thresholding");
  private MarvinImagePlugin fill = MarvinPluginLoader
      .loadImagePlugin("org.marvinproject.image.fill.boundaryFill");
  private MarvinImagePlugin scale = MarvinPluginLoader
      .loadImagePlugin("org.marvinproject.image.transform.scale");
  private MarvinImagePlugin diff = MarvinPluginLoader
      .loadImagePlugin("org.marvinproject.image.difference.differenceColor");

  public Logos() {

    // 1. Load, segment and scale the object to be found
    MarvinImage target = segmentTarget();

    // 2. Load the image with multiple objects
    MarvinImage original = MarvinImageIO.loadImage("./res/logos/logos.jpg");
    MarvinImage image = original.clone();

    // 3. Segment
    threshold.process(image, image);
    MarvinImage image2 = new MarvinImage(image.getWidth(), image.getHeight());
    fill(image, image2);
    MarvinImageIO.saveImage(image2, "./res/logos/logos_fill.jpg");

    // 4. Filter segments by its their masses
    LinkedHashSet<Integer> objects = filterByMass(image2, 10000);
    int[][] rects = getRects(objects, image2, original);
    MarvinImage[] subimages = getSubimages(rects, original);

    // 5. Compare the target object with each object in the other image
    compare(target, subimages, original, rects);
    MarvinImageIO.saveImage(original, "./res/logos/logos_out.jpg");
  }

  private void compare(MarvinImage target, MarvinImage[] subimages, MarvinImage original,
      int[][] rects) {
    MarvinAttributes attrOut = new MarvinAttributes();
    for (int i = 0; i < subimages.length; i++) {
      diff.setAttribute("comparisonImage", subimages[i]);
      diff.setAttribute("colorRange", 30);
      diff.process(target, null, attrOut);
      if ((Integer) attrOut.get("total") < (50 * 50) * 0.6) {
        original.drawRect(rects[i][0], rects[i][6], rects[i][7], rects[i][8], 6, Color.green);
      }
    }
  }

  private MarvinImage segmentTarget() {
    MarvinImage original = MarvinImageIO.loadImage("./res/logos/target.jpg");
    MarvinImage target = original.clone();
    threshold.process(target, target);
    MarvinImage image2 = new MarvinImage(target.getWidth(), target.getHeight());
    fill(target, image2);
    LinkedHashSet<Integer> objects = filterByMass(image2, 10000);
    int[][] rects = getRects(objects, image2, target);
    MarvinImage[] subimages = getSubimages(rects, original);
    return subimages[0];
  }



  private int[][] getRects(LinkedHashSet<Integer> objects, MarvinImage mask, MarvinImage original) {
    List<int[]> ret = new ArrayList<int[]>();
    for (Integer color : objects) {
      ret.add(getObjectRect(mask, color));
    }
    return ret.toArray(new int[0][0]);
  }

  private MarvinImage[] getSubimages(int[][] rects, MarvinImage original) {
    List<MarvinImage> ret = new ArrayList<MarvinImage>();
    for (int[] r : rects) {
      ret.add(getSubimage(r, original));
    }
    return ret.toArray(new MarvinImage[0]);
  }

  private MarvinImage getSubimage(int rect[], MarvinImage original) {
    MarvinImage img = original.subimage(rect[0], rect[1], rect[2], rect[3]);
    MarvinImage ret = new MarvinImage(50, 50);
    scale.setAttribute("newWidth", 50);
    scale.setAttribute("newHeight", 50);
    scale.process(img, ret);
    return ret;
  }

  private void fill(MarvinImage imageIn, MarvinImage imageOut) {
    boolean found;
    int color = 0xFFFF0000;

    while (true) {
      found = false;

      Outerloop: for (int y = 0; y < imageIn.getHeight(); y++) {
        for (int x = 0; x < imageIn.getWidth(); x++) {
          if (imageOut.getIntColor(x, y) == 0 && imageIn.getIntColor(x, y) != 0xFFFFFFFF) {
            fill.setAttribute("x", x);
            fill.setAttribute("y", y);
            fill.setAttribute("color", color);
            fill.setAttribute("threshold", 120);
            fill.process(imageIn, imageOut);
            color = newColor(color);

            found = true;
            break Outerloop;
          }
        }
      }

      if (!found) {
        break;
      }
    }
  }

  private LinkedHashSet<Integer> filterByMass(MarvinImage image, int mass) {
    boolean found;
    HashSet<Integer> analysed = new HashSet<Integer>();
    LinkedHashSet<Integer> ret = new LinkedHashSet<Integer>();

    while (true) {
      found = false;

      outerLoop: for (int y = 0; y < image.getHeight(); y++) {
        for (int x = 0; x < image.getWidth(); x++) {
          int color = image.getIntColor(x, y);
          if (color != 0) {
            if (!analysed.contains(color)) {
              if (getMass(image, color) >= mass) {
                ret.add(color);
              }
              analysed.add(color);
              found = true;
              break outerLoop;
            }
          }
        }
      }

      if (!found) {
        break;
      }
    }
    return ret;
  }

  private int getMass(MarvinImage image, int color) {
    int total = 0;
    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        if (image.getIntColor(x, y) == color) {
          total++;
        }
      }
    }
    return total;
  }

  private int[] getObjectRect(MarvinImage mask, int color) {
    int x1 = -1;
    int x2 = -1;
    int y1 = -1;
    int y2 = -1;

    for (int y = 0; y < mask.getHeight(); y++) {
      for (int x = 0; x < mask.getWidth(); x++) {
        if (mask.getIntColor(x, y) == color) {

          if (x1 == -1 || x < x1) {
            x1 = x;
          }
          if (x2 == -1 || x > x2) {
            x2 = x;
          }
          if (y1 == -1 || y < y1) {
            y1 = y;
          }
          if (y2 == -1 || y > y2) {
            y2 = y;
          }
        }
      }
    }

    return new int[] {x1, y1, (x2 - x1), (y2 - y1)};
  }

  private int newColor(int color) {
    int red = (color & 0x00FF0000) >> 16;
    int green = (color & 0x0000FF00) >> 8;
    int blue = (color & 0x000000FF);

    if (red <= green && red <= blue) {
      red += 5;
    } else if (green <= red && green <= blue) {
      green += 5;
    } else {
      blue += 5;
    }

    return 0xFF000000 + (red << 16) + (green << 8) + blue;
  }

  public static void main(String[] args) {
    new Logos();
  }
}
