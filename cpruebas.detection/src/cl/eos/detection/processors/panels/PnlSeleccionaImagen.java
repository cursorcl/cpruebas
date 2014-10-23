package cl.eos.detection.processors.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

public class PnlSeleccionaImagen extends JPanel {
	private static final long serialVersionUID = 1L;
	private JList<File> listArchivos;
	private DefaultListModel<File> model = new DefaultListModel<File>();
	private JButton btnAdd;
	private JButton btnDel;
	JFileChooser fCh = new JFileChooser();
	private JButton btnClear;

	/**
	 * Create the panel.
	 */
	public PnlSeleccionaImagen() {
		setLayout(new MigLayout("", "[196.00px,grow][]", "[26.00px][][][grow]"));
		add(getListArchivos(), "cell 0 0 1 4,grow");
		add(getBtnAdd(), "cell 1 0,grow");
		add(getBtnDel(), "cell 1 1,grow");
		add(getBtnClear(), "cell 1 2");
	}

	private JList<File> getListArchivos() {
		if (listArchivos == null) {
			listArchivos = new JList<File>();
			listArchivos.setModel(model);
			listArchivos.addListSelectionListener(new ListSelectionListener() {

				@Override
				public void valueChanged(ListSelectionEvent e) {
					btnDel.setEnabled(e.getFirstIndex() != -1);

				}
			});
		}
		return listArchivos;
	}

	private JButton getBtnAdd() {
		if (btnAdd == null) {
			btnAdd = new JButton("+");
			btnAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Properties prop = new Properties();
					File file = new File("lastDir.properties");
					if(file.exists())
					{
						try {
							prop.load(new FileInputStream(file));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						String lastDir = prop.getProperty("DIR");
						fCh.setCurrentDirectory(new File(lastDir));
					}
					
					if (fCh.showOpenDialog(PnlSeleccionaImagen.this) == JFileChooser.APPROVE_OPTION) {
						prop.put("DIR", fCh.getCurrentDirectory().getAbsolutePath());
						try {
							prop.store(new FileOutputStream(file), "EOS");
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						model.addElement(fCh.getSelectedFile());
					}
				}
			});
		}
		return btnAdd;
	}

	private JButton getBtnDel() {
		if (btnDel == null) {
			btnDel = new JButton("-");
			btnDel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					model.remove(listArchivos.getSelectedIndex());
				}
			});
		}
		return btnDel;
	}
	private JButton getBtnClear() {
		if (btnClear == null) {
			btnClear = new JButton("--");
			btnClear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					model.removeAllElements();
				}
			});
		}
		return btnClear;
	}
	
	
	public List<File> getFiles()
	{
		List<File> files = new ArrayList<File>();
		for(int n = 0; n < model.getSize(); n++)
		{
			files.add(model.getElementAt(n));
		}
		return files;
	}
}
