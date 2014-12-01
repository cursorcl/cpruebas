package cl.eos.detection;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.Contour;
import boofcv.alg.filter.binary.GThresholdImageOps;
import boofcv.alg.filter.binary.ThresholdImageOps;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.gui.binary.VisualizeBinaryData;
import boofcv.gui.image.ShowImages;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.ConnectRule;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageSInt32;
import boofcv.struct.image.ImageUInt8;

public class ExampleBinaryOps {
	 
	public static void main( String args[] ) {
		// load and convert the image into a usable format
//		BufferedImage image = UtilImageIO.loadImage("/res/20141127/Indep_1A_prueba08.jpg");
		BufferedImage image = UtilImageIO.loadImage("/res/sub_image.png");
 
		
		ImageFloat32 input = ConvertBufferedImage.convertFromSingle(image,
				null, ImageFloat32.class);

		ImageUInt8 binary = new ImageUInt8(input.width, input.height);
		ImageSInt32 label = new ImageSInt32(input.width, input.height);
		ThresholdImageOps.threshold(input, binary, (float) 145, true);
		ImageUInt8 filtered = BinaryImageOps.erode8(binary, 8, null);
		filtered = BinaryImageOps.dilate8(filtered, 8, null);

		try {
			BufferedImage img = VisualizeBinaryData.renderBinary(filtered, null);
			
			ImageIO.write(image, "png", new File("/res/sub_image.png"));
			ImageIO.write(img,
					"png", new File("/res/proc_sub_image.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Contour> contours =  BinaryImageOps.contour(filtered, ConnectRule.EIGHT, label);
		
		int colorExternal = 0xFFFFFF;
		int colorInternal = 0xFF2020;
 
		// display the results
		BufferedImage visualBinary = VisualizeBinaryData.renderBinary(binary, null);
		BufferedImage visualContour = VisualizeBinaryData.renderContours(contours,colorExternal,colorInternal,
				input.width,input.height,null);
 
		ShowImages.showWindow(visualBinary,"Binary Original");
		ShowImages.showWindow(visualContour,"Contours");
	}
 
}
