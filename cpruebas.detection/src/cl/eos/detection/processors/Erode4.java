package cl.eos.detection.processors;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import cl.eos.detection.processors.base.AErodeDilate;
import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.gui.binary.VisualizeBinaryData;
import boofcv.struct.image.ImageBase;
import boofcv.struct.image.ImageUInt8;

public class Erode4 extends AErodeDilate {

	public Erode4() {
		name  = "Erode4";
	}
	@Override
	public void setParameters(Map<String, Object> params) {
		if(params != null && !params.isEmpty())
		{
			Object iTimes = params.get(TIMES);
			times = (Integer)iTimes;
		}
	}

	@Override
	public ImageBase execute(ImageBase image) {
		
		if (image instanceof ImageUInt8) {
			return BinaryImageOps.dilate4((ImageUInt8) image, times, null);
		} else {
			return null;
		}
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

}
