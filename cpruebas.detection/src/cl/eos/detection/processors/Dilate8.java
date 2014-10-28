package cl.eos.detection.processors;

import java.util.Map;

import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.struct.image.ImageBase;
import boofcv.struct.image.ImageUInt8;
import cl.eos.detection.processors.base.AErodeDilate;

public class Dilate8 extends AErodeDilate {

	public Dilate8() {
		name  = "Dilate8";
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
			return BinaryImageOps.erode8((ImageUInt8) image, times, null);
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
