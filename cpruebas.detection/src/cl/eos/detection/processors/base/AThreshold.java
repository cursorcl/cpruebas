package cl.eos.detection.processors.base;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import boofcv.alg.filter.binary.ThresholdImageOps;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.gui.binary.VisualizeBinaryData;
import boofcv.struct.image.ImageBase;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageUInt8;
import cl.eos.detection.processors.panels.IPanels;
import cl.eos.detection.processors.panels.PnlUmbral;

public abstract class AThreshold implements IProcessor, PropertyChangeListener {

	public static final String UMBRAL = "UMBRAL";
	protected float umbral = 160f;
	protected String name;

	private PnlUmbral pnlUmbral = new PnlUmbral();

	@Override
	public Object getView() {
		pnlUmbral.setUmbral(umbral);
		pnlUmbral.addPropertyChangeListener(this);
		return pnlUmbral;
	}

	@Override
	public void setParameters(Map<String, Object> params) {
		if (params != null && !params.isEmpty()) {
			Object fUmbral = params.get(UMBRAL);
			umbral = (Float) fUmbral;
		}
	}

	@Override
	public ImageBase execute(ImageBase image) {
		if (image instanceof ImageFloat32) {
			ImageUInt8 output = new ImageUInt8(image.width, image.height);
			ThresholdImageOps.threshold((ImageFloat32) image, output, umbral, false);
			return output;
		} else {
			return null;
		}
	}

	public float getUmbral() {
		return umbral;
	}

	public void setUmbral(float umbral) {
		this.umbral = umbral;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(UMBRAL)) {
			umbral = (Float) evt.getNewValue();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public String getParameters() {
		return String.format("%s umbral[%f]", getName(), umbral);
	}
	
	

}
