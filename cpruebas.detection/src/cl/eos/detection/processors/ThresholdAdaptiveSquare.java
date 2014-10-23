package cl.eos.detection.processors;

import boofcv.alg.filter.binary.GThresholdImageOps;
import boofcv.alg.filter.binary.ThresholdImageOps;
import boofcv.alg.misc.ImageStatistics;
import boofcv.struct.image.ImageBase;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageUInt8;
import cl.eos.detection.processors.base.AThreshold;

public class ThresholdAdaptiveSquare extends AThreshold {

	public ThresholdAdaptiveSquare() {
		name = "ThresholdAdaptiveSquare";
	}
	
	

	@Override
	public ImageBase execute(ImageBase image) {
		if (image instanceof ImageFloat32) {
			ImageUInt8 output = new ImageUInt8(image.width, image.height);
			
			GThresholdImageOps.adaptiveSquare((ImageFloat32) image, output, (int)umbral, 0, false, null, null);
			return output;
		} else {
			return null;
		}
	}

}
