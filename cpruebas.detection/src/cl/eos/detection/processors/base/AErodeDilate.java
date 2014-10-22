package cl.eos.detection.processors.base;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import cl.eos.detection.processors.panels.PnlTimes;


public abstract class AErodeDilate implements IProcessor, PropertyChangeListener {
	public static final String TIMES = "TIMES";
	protected int times = 2;
	protected String name;

	private PnlTimes pnlTimes = new PnlTimes();
	@Override
	public Object getView() {
		pnlTimes.setTimes(times);
		pnlTimes.addPropertyChangeListener(this);
		return pnlTimes;
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals(TIMES))
		{
			times = (Integer) evt.getNewValue();
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
		return String.format("%s veces[%d]", getName(), times);
	}


}
