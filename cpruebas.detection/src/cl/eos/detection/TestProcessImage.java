package cl.eos.detection;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.ThresholdImageOps;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.gui.binary.VisualizeBinaryData;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageUInt8;


public class TestProcessImage {

	private static String[] files = {"prueba_001.png", "prueba_002.png", "prueba_003.png", "prueba_004.png", "prueba_005.png","prueba00.jpg","prueba01.jpg","prueb02.jpg","prueba03.jpg"};
	public static void main(String[] args) {
			BufferedImage image;
			try {
				image = ImageIO.read(new File("./res/prueba_002.png"));
			    ImageFloat32 input = ConvertBufferedImage.convertFromSingle(image, null, ImageFloat32.class);
			    ImageUInt8 binary = new ImageUInt8(input.width, input.height);
			    ThresholdImageOps.threshold(input, binary, (float) 145, false);
			    ImageUInt8 eroded = BinaryImageOps.erode8(binary, 1, null);
			    ImageUInt8 filtered = BinaryImageOps.dilate4(eroded, 15, null);
			    eroded = BinaryImageOps.erode4(filtered, 12, null);
			    BufferedImage bImage = VisualizeBinaryData.renderBinary(eroded, null);
			    ImageIO.write(bImage, "png", new File("./res/prueba_002_p.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		    
	}

}
