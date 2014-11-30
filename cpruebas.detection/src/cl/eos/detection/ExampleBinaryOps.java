package cl.eos.detection;

import java.awt.image.BufferedImage;
import java.util.List;

import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.Contour;
import boofcv.alg.filter.binary.ThresholdImageOps;
import boofcv.alg.misc.ImageStatistics;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.gui.binary.VisualizeBinaryData;
import boofcv.gui.image.ShowImages;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.ConnectRule;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageSInt32;
import boofcv.struct.image.ImageUInt8;

/**
 * Demonstrates how to create binary images by thresholding, applying binary morphological operations, and
 * then extracting detected features by finding their contours.
 *
 * @see boofcv.examples.segmentation.ExampleThresholding
 *
 * @author Peter Abeles
 */
public class ExampleBinaryOps {
 
    public static void main( String args[] ) {
        // load and convert the image into a usable format
        BufferedImage image = UtilImageIO.loadImage("res/prueba08.JPG");
 
        // convert into a usable format
        ImageFloat32 input = ConvertBufferedImage.convertFromSingle(image, null, ImageFloat32.class);
        ImageUInt8 binary = new ImageUInt8(input.width,input.height);
        ImageSInt32 label = new ImageSInt32(input.width,input.height);
 
        // Select a global threshold using Otsu's method.
//        double threshold = GThresholdImageOps.computeOtsu(input, 0, 256);
        double threshold = 140;
        // Apply the threshold to create a binary image
        ThresholdImageOps.threshold(input,binary,(float)threshold,true);
 
        // remove small blobs through erosion and dilation
        // The null in the input indicates that it should internally declare the work image it needs
        // this is less efficient, but easier to code.
        ImageUInt8 filtered = BinaryImageOps.erode8(binary, 1, null);
        filtered = BinaryImageOps.dilate8(filtered, 1, null);
 
        // Detect blobs inside the image using an 8-connect rule
        List<Contour> contours = BinaryImageOps.contour(filtered, ConnectRule.EIGHT, label);
 
        // colors of contours
        int colorExternal = 0xFFFFFF;
        int colorInternal = 0xFF2020;
 
        // display the results
        BufferedImage visualBinary = VisualizeBinaryData.renderBinary(binary, null);
        BufferedImage visualFiltered = VisualizeBinaryData.renderBinary(filtered, null);
        BufferedImage visualLabel = VisualizeBinaryData.renderLabeledBG(label, contours.size(), null);
        BufferedImage visualContour = VisualizeBinaryData.renderContours(contours,colorExternal,colorInternal,
                input.width,input.height,null);
 
        ShowImages.showWindow(visualBinary,"Binary Original");
        ShowImages.showWindow(visualFiltered,"Binary Filtered");
        ShowImages.showWindow(visualLabel,"Labeled Blobs");
        ShowImages.showWindow(visualContour,"Contours");
    }
 
}