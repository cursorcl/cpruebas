package cl.eos.detection.processors;

import boofcv.alg.filter.binary.GThresholdImageOps;
import boofcv.alg.misc.ImageStatistics;
import boofcv.struct.image.ImageBase;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageUInt8;
import cl.eos.detection.processors.base.AThreshold;

public class ThresholdMean extends AThreshold {

	public ThresholdMean() {
		name = "ThresholdMean";
	}
	
	@Override
	public Object getView() {
		return null;
	}
	@Override
	public ImageBase execute(ImageBase image) {
		if (image instanceof ImageFloat32) {
			ImageUInt8 output = new ImageUInt8(image.width, image.height);
			GThresholdImageOps.threshold((ImageFloat32) image, output, (float)ImageStatistics.mean((ImageFloat32) image), false);
			return output;
		} else {
			return null;
		}
	}
	
	@Override
	public String getParameters() {
		return "";
	}

}
