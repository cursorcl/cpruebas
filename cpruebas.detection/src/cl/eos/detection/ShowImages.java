package cl.eos.detection;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import java.awt.Window.Type;

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

	JLabel lblSource;
	JLabel lblTarget;

	/**
	 * Create the dialog.
	 */
	public ShowImages() {
		setTitle("Comparación");

		setBounds(100, 100, 820, 625);
		getContentPane().setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerSize(2);
		splitPane.setContinuousLayout(true);
		getContentPane().add(splitPane, BorderLayout.CENTER);

		lblSource = new JLabel("New label");

		splitPane.setLeftComponent(lblSource);

		lblTarget = new JLabel("New label");
		splitPane.setRightComponent(lblTarget);
		splitPane.setDividerLocation(400);
		this.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				splitPane.setDividerLocation(getWidth() / 2);
			}
			
		});
	}

	public void mostrar(String source, String target) {
		ImageIcon imSource = new ImageIcon(source);
		ImageIcon imTarget = new ImageIcon(target);
		lblSource.setIcon(imSource);
		lblTarget.setIcon(imTarget);
		repaint();
		setVisible(true);
	}

}
