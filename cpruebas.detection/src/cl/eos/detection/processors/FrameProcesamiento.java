package cl.eos.detection.processors;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Processor;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;
import cl.eos.detection.processors.base.IProcessor;
import cl.eos.detection.processors.base.Procesador;
import cl.eos.detection.processors.panels.IPanels;
import cl.eos.detection.processors.panels.PnlSeleccionaImagen;
import cl.eos.detection.processors.panels.ShowImages;

public class FrameProcesamiento extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JSplitPane splitPane;
	private JPanel panel;
	private JButton btnAdd;
	private JButton btnDel;
	private JList<IProcessor> listAdded;
	private DefaultListModel<IProcessor> listProcessorModel = new DefaultListModel<IProcessor>();
	private JList<IProcessor> listProcessor;
	private DefaultListModel<IProcessor> listAddedModel = new DefaultListModel<IProcessor>();
	private JLabel lblProcesadores;
	private JLabel lblAgregados;
	private JPanel pnlConfig;
	private JButton btnProcesar;
	private JButton btnImagenes;
	protected PnlSeleccionaImagen pnlSelecciona;
	protected ShowImages show;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrameProcesamiento frame = new FrameProcesamiento();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FrameProcesamiento() {
		setTitle("Pre Procesador Imagnes");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 689, 297);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(getSplitPane(), BorderLayout.CENTER);
		fillListPorcessor();
	}

	private void fillListPorcessor() {
		listProcessorModel.addElement(new ThresholdValue());
		listProcessorModel.addElement(new Dilate4());
		listProcessorModel.addElement(new Dilate8());
		listProcessorModel.addElement(new Erode4());
		listProcessorModel.addElement(new Erode8());
		

	}

	private JSplitPane getSplitPane() {
		if (splitPane == null) {
			splitPane = new JSplitPane();
			splitPane.setLeftComponent(getPanel());
			splitPane.setRightComponent(getPnlConfig());
		}
		return splitPane;
	}

	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setPreferredSize(new Dimension(350, 10));
			panel.setLayout(new MigLayout("", "[120,grow][][120,grow]",
					"[][24.00][][grow][]"));
			panel.add(getLblProcesadores(), "cell 0 0");
			panel.add(getLblAgregados(), "cell 2 0");
			panel.add(getListProcessor(), "cell 0 1 1 3,grow");
			panel.add(getBtnAdd(), "cell 1 1");
			panel.add(getListAdded(), "cell 2 1 1 3,grow");
			panel.add(getBtnDel(), "cell 1 2");
			panel.add(getBtnImagenes(), "cell 0 4");
			panel.add(getBtnProcesar(), "cell 2 4,alignx right");
		}
		return panel;
	}

	private JButton getBtnAdd() {
		if (btnAdd == null) {
			btnAdd = new JButton(">");
			btnAdd.setEnabled(false);
			btnAdd.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					IProcessor processor = listProcessor.getSelectedValue();
					try {
						processor = processor.getClass().newInstance();
						pnlConfig.removeAll();
						if (processor.getView() != null) {
							IPanels panel = (IPanels) processor.getView();
							panel.setTitulo(processor.getName());
							pnlConfig.add((JPanel) processor.getView());
						}
						pnlConfig.updateUI();
						listAddedModel.addElement(processor);
						listAdded.setSelectedIndex(listAddedModel.size() - 1);

					} catch (InstantiationException | IllegalAccessException e1) {
						e1.printStackTrace();
					}
				}
			});
		}
		return btnAdd;
	}

	private JButton getBtnDel() {
		if (btnDel == null) {
			btnDel = new JButton("<");
			btnDel.setEnabled(false);
			btnDel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					listAddedModel.remove(listAdded.getSelectedIndex());
					pnlConfig.removeAll();
					pnlConfig.updateUI();

				}
			});
		}
		return btnDel;
	}

	private JList<IProcessor> getListAdded() {
		if (listAdded == null) {
			listAdded = new JList<IProcessor>();
			listAdded.setModel(listAddedModel);
			listAdded.setBorder(new TitledBorder(null, "",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			listAdded.addListSelectionListener(new ListSelectionListener() {

				@Override
				public void valueChanged(ListSelectionEvent e) {
					btnDel.setEnabled(e.getFirstIndex() != -1);
					IProcessor processor = listAdded.getSelectedValue();
					pnlConfig.removeAll();
					if (processor != null && processor.getView() != null) {
						pnlConfig.add((JPanel) processor.getView());
					}
					pnlConfig.updateUI();

				}
			});
			listAdded.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					btnDel.setEnabled(!listAdded.isSelectionEmpty());
					if (!listAdded.isSelectionEmpty()) {
						IProcessor processor = listAdded.getSelectedValue();
						pnlConfig.removeAll();
						if (processor != null && processor.getView() != null) {
							pnlConfig.add((JPanel) processor.getView());
						}
						pnlConfig.updateUI();
					}
				}

			});
		}
		return listAdded;
	}

	private JList<IProcessor> getListProcessor() {
		if (listProcessor == null) {
			listProcessor = new JList<IProcessor>();
			listProcessor.setModel(listProcessorModel);
			listProcessor.setBorder(new TitledBorder(null, "",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			listProcessor.addListSelectionListener(new ListSelectionListener() {

				@Override
				public void valueChanged(ListSelectionEvent e) {
					btnAdd.setEnabled(e.getFirstIndex() != -1);
				}
			});
		}
		return listProcessor;
	}

	private JLabel getLblProcesadores() {
		if (lblProcesadores == null) {
			lblProcesadores = new JLabel("Procesadores");
		}
		return lblProcesadores;
	}

	private JLabel getLblAgregados() {
		if (lblAgregados == null) {
			lblAgregados = new JLabel("Agregados");
		}
		return lblAgregados;
	}

	private JPanel getPnlConfig() {
		if (pnlConfig == null) {
			pnlConfig = new JPanel();
			pnlConfig.setLayout(new BoxLayout(pnlConfig, BoxLayout.X_AXIS));
		}
		return pnlConfig;
	}

	private JButton getBtnProcesar() {
		if (btnProcesar == null) {
			btnProcesar = new JButton("Procesar");
			btnProcesar.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String parameters ="";
					List<IProcessor> processors = new ArrayList<IProcessor>();
					for (int n = 0; n < listAdded.getModel().getSize(); n++) {
						IProcessor processor  = listAdded.getModel().getElementAt(n);
						processors.add(processor);
						parameters = parameters + processor.getParameters();
					}
					for (File file : pnlSelecciona.getFiles()) {
						BufferedImage image;
						try {
							image = ImageIO.read(file);
							BufferedImage imgResult = Procesador.getInstance()
									.procesar(image, processors);

							show = new ShowImages();
							show.setParameters(parameters);
							show.setTitle(file.getName());
							show.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
							show.mostrar(image, imgResult);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			});

		}
		return btnProcesar;
	}

	private JButton getBtnImagenes() {
		if (btnImagenes == null) {
			btnImagenes = new JButton("Imagenes");
			btnImagenes.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (pnlSelecciona == null) {
						pnlSelecciona = new PnlSeleccionaImagen();
					}
					pnlConfig.removeAll();
					pnlConfig.add(pnlSelecciona);
					pnlConfig.updateUI();
				}
			});
		}
		return btnImagenes;
	}
}
