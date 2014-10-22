package cl.eos.detection.processors.base;

import java.awt.image.BufferedImage;
import java.util.List;

import boofcv.core.image.ConvertBufferedImage;
import boofcv.core.image.ConvertImage;
import boofcv.gui.binary.VisualizeBinaryData;
import boofcv.struct.image.ImageBase;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageUInt8;

public class Procesador {

	private static Procesador instance = null;

	private Procesador() {

	}

	public static Procesador getInstance() {
		if (instance == null) {
			instance = new Procesador();
		}
		return instance;
	}

	public BufferedImage procesar(BufferedImage image,
			List<IProcessor> processors) {
		ImageFloat32 input = ConvertBufferedImage.convertFromSingle(image,
				null, ImageFloat32.class);
		ImageBase output = input;
		for (IProcessor processor : processors) {
			output = processor.execute(output);
		}
		ImageUInt8  img = null;
		if(!(output instanceof ImageUInt8 ))
		{
			
			img = ConvertImage.convert((ImageFloat32)output, img);
		}
		else
		{
			img = (ImageUInt8)output;
		}
		
		return VisualizeBinaryData.renderBinary(img, null);
	}
}
