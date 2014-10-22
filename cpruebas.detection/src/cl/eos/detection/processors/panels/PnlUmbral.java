package cl.eos.detection.processors.panels;

import java.awt.Font;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;
import cl.eos.detection.processors.base.AThreshold;

public class PnlUmbral extends JPanel implements ChangeListener, IPanels {

	private static final long serialVersionUID = 1L;
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	private float oldSliderValue = 0;
	private JSpinner spnTimes;
	private JLabel lblTitulo;

	/**
	 * Create the panel.
	 */
	public PnlUmbral() {
		setLayout(new MigLayout("", "[grow]", "[][][]"));
		add(getLblTitulo(), "cell 0 0,growx");

		JLabel lblNmeroVeces = new JLabel("Umbral");
		add(lblNmeroVeces, "cell 0 1,growx");
		add(getSpnTimes(), "cell 0 2,growx");
	}

	public float getUmbral() {
		return (float) spnTimes.getValue();
	}

	public void setUmbral(float times) {
		spnTimes.setValue(times);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(listener);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		Number value = (Number) spnTimes.getValue();
		if (oldSliderValue != value.floatValue()) {
			this.pcs.firePropertyChange(AThreshold.UMBRAL, oldSliderValue,
					value.floatValue());
			oldSliderValue = value.floatValue();
		}
	}

	private JSpinner getSpnTimes() {
		if (spnTimes == null) {
			spnTimes = new JSpinner();
			spnTimes.setModel(new SpinnerNumberModel(160f, 1f, 255f, 1f));
			spnTimes.addChangeListener(this);
		}
		return spnTimes;
	}
	private JLabel getLblTitulo() {
		if (lblTitulo == null) {
			lblTitulo = new JLabel("Titulo");
			lblTitulo.setHorizontalTextPosition(SwingConstants.CENTER);
			lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
			lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		return lblTitulo;
	}
	
	/* (non-Javadoc)
	 * @see cl.eos.detection.processors.panels.IPanels#setTitulo(java.lang.String)
	 */
	@Override
	public void setTitulo(String titulo)
	{
		lblTitulo.setText(titulo);
		
	}
}
