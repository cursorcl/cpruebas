package cl.eos.detection.processors.base;

import java.awt.image.BufferedImage;
import java.util.Map;

import boofcv.struct.image.ImageBase;

public interface IProcessor {

	
	void setParameters(final Map<String, Object> params);
	String getName();
	ImageBase execute(final ImageBase image);
	Object getView();
	
	String getParameters();
}
