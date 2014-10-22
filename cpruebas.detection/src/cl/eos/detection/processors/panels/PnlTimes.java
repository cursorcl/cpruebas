package cl.eos.detection.processors.panels;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;
import cl.eos.detection.processors.base.AErodeDilate;

import javax.swing.SwingConstants;

import java.awt.Font;

public class PnlTimes extends JPanel implements ChangeListener, IPanels {

	private static final long serialVersionUID = 1L;
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	private int oldSliderValue = 0;
	private JSpinner spnTimes;
	private JLabel lblTitulo;

	/**
	 * Create the panel.
	 */
	public PnlTimes() {
		setLayout(new MigLayout("", "[grow]", "[][][]"));
		add(getLblTitulo(), "cell 0 0,grow");

		JLabel lblNmeroVeces = new JLabel("Número veces");
		add(lblNmeroVeces, "cell 0 1,growx");
		add(getSpnTimes(), "cell 0 2,growx");
	}

	public int getTimes() {
		return (int) spnTimes.getValue();
	}

	public void setTimes(int times) {
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
		if (oldSliderValue != (int) spnTimes.getValue()) {
			this.pcs.firePropertyChange(AErodeDilate.TIMES, oldSliderValue,
					spnTimes.getValue());
			oldSliderValue = (int) spnTimes.getValue();
		}
	}

	private JSpinner getSpnTimes() {
		if (spnTimes == null) {
			spnTimes = new JSpinner();
			spnTimes.setModel(new SpinnerNumberModel(3, 1, 100, 1));
			spnTimes.addChangeListener(this);
		}
		return spnTimes;
	}

	private JLabel getLblTitulo() {
		if (lblTitulo == null) {
			lblTitulo = new JLabel("TITULO");
			lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 12));
			lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblTitulo;
	}

	public void setTitulo(String titulo) {
		lblTitulo.setText(titulo);

	}

}
