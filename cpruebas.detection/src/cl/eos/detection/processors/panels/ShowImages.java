package cl.eos.detection.processors.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import net.miginfocom.swing.MigLayout;

public class ShowImages extends JDialog {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ShowImages dialog = new ShowImages();
					dialog.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private JSplitPane splitPane;
	private JPanel panel;
	private JLabel lblParameters;

	/**
	 * Create the dialog.
	 */
	public ShowImages() {
		setTitle("Comparación");

		setBounds(100, 100, 820, 625);
		getContentPane().setLayout(new BorderLayout(0, 0));

		splitPane = new JSplitPane();
		splitPane.setDividerSize(2);
		splitPane.setContinuousLayout(true);
		getContentPane().add(splitPane, BorderLayout.CENTER);
		getContentPane().add(getPanel(), BorderLayout.NORTH);

		this.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				splitPane.setDividerLocation(getWidth() / 2);
			}
			
		});
	}

	public void mostrar(BufferedImage source, BufferedImage target) {
		
		splitPane.setLeftComponent(new ScalablePane(source));
		splitPane.setRightComponent(new ScalablePane(target));
		
		setVisible(true);
		
	}

	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setPreferredSize(new Dimension(10, 30));
			panel.setLayout(new MigLayout("", "[grow]", "[]"));
			panel.add(getLblParameters(), "cell 0 0,aligny top");
		}
		return panel;
	}
	private JLabel getLblParameters() {
		if (lblParameters == null) {
			lblParameters = new JLabel("Parametros");
		}
		return lblParameters;
	}
	
	public void setParameters(String parameters)
	{
		lblParameters.setText(parameters);
	}
}
