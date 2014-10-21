package cl.eos.detection;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;

import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.ThresholdImageOps;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.gui.binary.VisualizeBinaryData;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageUInt8;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.JCheckBox;

public class TestParameters extends JDialog {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestParameters dialog = new TestParameters();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private ShowImages show;
	JCheckBox chckbxProcesaLErosion;
	JCheckBox chkProcesaErode2;
	JCheckBox chkProcesaDilate1;
	JCheckBox chkProcesaErode1;
	JTextField textImagen;
	JSlider sldThreshold;
	JRadioButton rdbtnErode8;
	JSlider sldErodeItera;
	JRadioButton rdbtnDilate8;
	JSlider sldDilateItera;
	JRadioButton rdbtnDilate4;
	JRadioButton rbtnErode4;
	JSlider sliderNErode;
	JRadioButton rbtnNErode4;
	JRadioButton rbtnNErode8;
	JSlider sliderLErode;
	JRadioButton rdbtnLastErode4;
	JRadioButton rdbtnLastErode8;

	/**
	 * Create the dialog.
	 */
	public TestParameters() {


		setBounds(100, 100, 535, 574);

		getContentPane().setLayout(
				new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("79px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("159px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("163px"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("23px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(27dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(27dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(27dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(27dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(27dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));

		JLabel lblImagen = new JLabel("Imagen");
		getContentPane().add(lblImagen, "2, 2, left, center");

		textImagen = new JTextField();
		getContentPane().add(textImagen, "4, 2, 3, 1, fill, top");
		textImagen.setColumns(10);

		JLabel lblThreshold = new JLabel("Threshold");
		getContentPane().add(lblThreshold, "2, 4");

		sldThreshold = new JSlider();
		sldThreshold.setMinimum(10);
		sldThreshold.setMajorTickSpacing(50);
		sldThreshold.setPaintLabels(true);
		sldThreshold.setValue(145);
		sldThreshold.setMaximum(255);
		getContentPane().add(sldThreshold, "4, 4, 3, 1, fill, default");

		JLabel lblErode = new JLabel("Dilatar");
		getContentPane().add(lblErode, "2, 8");

		rbtnErode4 = new JRadioButton("Dilate 4");
		getContentPane().add(rbtnErode4, "4, 8");
		rbtnErode4.setSelected(true);

		rdbtnErode8 = new JRadioButton("Dilate 8");
		getContentPane().add(rdbtnErode8, "6, 8");

		ButtonGroup grpErode = new ButtonGroup();
		grpErode.add(rbtnErode4);
		grpErode.add(rdbtnErode8);

		chkProcesaErode1 = new JCheckBox("Procesa");
		chkProcesaErode1.setSelected(true);
		getContentPane().add(chkProcesaErode1, "8, 8");

		JLabel lblNroItera = new JLabel("# Dilatar.");
		getContentPane().add(lblNroItera, "2, 10");

		sldErodeItera = new JSlider();
		sldErodeItera.setMinorTickSpacing(1);
		sldErodeItera.setMajorTickSpacing(2);
		sldErodeItera.setPaintLabels(true);
		sldErodeItera.setValue(1);
		sldErodeItera.setMinimum(1);
		sldErodeItera.setMaximum(20);
		getContentPane().add(sldErodeItera, "4, 10, 3, 1, fill, default");

		JLabel lblDilate = new JLabel("Erosionar");
		getContentPane().add(lblDilate, "2, 14");

		rdbtnDilate4 = new JRadioButton("Erode 4");
		getContentPane().add(rdbtnDilate4, "4, 14");
		rdbtnDilate4.setSelected(true);

		rdbtnDilate8 = new JRadioButton("Erode 8");
		getContentPane().add(rdbtnDilate8, "6, 14");

		ButtonGroup grpDilate = new ButtonGroup();
		grpDilate.add(rdbtnDilate4);
		grpDilate.add(rdbtnDilate8);

		chkProcesaDilate1 = new JCheckBox("Procesa");
		chkProcesaDilate1.setSelected(true);
		getContentPane().add(chkProcesaDilate1, "8, 14");
		JLabel lblDilateItera = new JLabel("# Erosionar");
		getContentPane().add(lblDilateItera, "2, 16");

		sldDilateItera = new JSlider();
		sldDilateItera.setMajorTickSpacing(2);
		sldDilateItera.setMinorTickSpacing(1);
		sldDilateItera.setSnapToTicks(true);
		sldDilateItera.setPaintLabels(true);
		sldDilateItera.setValue(1);
		sldDilateItera.setMinimum(1);
		sldDilateItera.setMaximum(20);
		getContentPane().add(sldDilateItera, "4, 16, 3, 1, fill, default");

		JLabel lblErode_1 = new JLabel("Dilatar");
		getContentPane().add(lblErode_1, "2, 20");

		rbtnNErode4 = new JRadioButton("Dilate 4");
		getContentPane().add(rbtnNErode4, "4, 20");
		rbtnNErode4.setSelected(true);

		rbtnNErode8 = new JRadioButton("Dilate 8");
		getContentPane().add(rbtnNErode8, "6, 20");
		ButtonGroup grpNErode = new ButtonGroup();
		grpNErode.add(rbtnNErode4);
		grpNErode.add(rbtnNErode8);

		chkProcesaErode2 = new JCheckBox("Procesa");
		chkProcesaErode2.setSelected(true);
		getContentPane().add(chkProcesaErode2, "8, 20");

		JLabel lblErodeItera = new JLabel("# Dilatar Itera.");
		getContentPane().add(lblErodeItera, "2, 22");

		sliderNErode = new JSlider();
		sliderNErode.setValue(12);
		sliderNErode.setPaintLabels(true);
		sliderNErode.setMinorTickSpacing(1);
		sliderNErode.setMajorTickSpacing(2);
		sliderNErode.setMinimum(1);
		sliderNErode.setMaximum(20);
		getContentPane().add(sliderNErode, "4, 22, 3, 1");

		JLabel lblEorisonar = new JLabel("Eorisonar");
		getContentPane().add(lblEorisonar, "2, 26");

		rdbtnLastErode4 = new JRadioButton("Erode 4");
		getContentPane().add(rdbtnLastErode4, "4, 26");
		rdbtnLastErode4.setSelected(true);

		rdbtnLastErode8 = new JRadioButton("Erode 8");
		getContentPane().add(rdbtnLastErode8, "6, 26");

		ButtonGroup grpLErode = new ButtonGroup();
		grpLErode.add(rdbtnLastErode8);
		grpLErode.add(rdbtnLastErode4);

		chckbxProcesaLErosion = new JCheckBox("Procesa");
		chckbxProcesaLErosion.setSelected(true);
		getContentPane().add(chckbxProcesaLErosion, "8, 26");

		JLabel lblErosionar = new JLabel("# Erosionar");
		getContentPane().add(lblErosionar, "2, 28");

		sliderLErode = new JSlider();
		sliderLErode.setMinimum(1);
		sliderLErode.setMajorTickSpacing(2);
		sliderLErode.setMaximum(20);
		sliderLErode.setPaintLabels(true);
		getContentPane().add(sliderLErode, "4, 28, 3, 1");

		JButton btnCancelar = new JButton("Cancelar");
		getContentPane().add(btnCancelar, "4, 32");

		JButton btnProcesar = new JButton("Procesar");
		btnProcesar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				process();
			}
		});
		getContentPane().add(btnProcesar, "6, 32");

	}

	private void process() {
		BufferedImage image;
		try {
			image = ImageIO.read(new File(textImagen.getText()));
			ImageFloat32 input = ConvertBufferedImage.convertFromSingle(image,
					null, ImageFloat32.class);

			ImageUInt8 output = new ImageUInt8(input.width, input.height);
			ThresholdImageOps.threshold(input, output,
					(float) sldThreshold.getValue(), false);

			if (chkProcesaErode1.isSelected()) {
				if (rbtnErode4.isSelected()) {
					output = BinaryImageOps.erode4(output,
							sldErodeItera.getValue(), null);

				} else {
					output = BinaryImageOps.erode8(output,
							sldErodeItera.getValue(), null);
				}
			}
			if (chkProcesaDilate1.isSelected()) {

				if (rdbtnDilate4.isSelected()) {
					output = BinaryImageOps.dilate4(output,
							sldDilateItera.getValue(), null);
				} else {
					output = BinaryImageOps.dilate8(output,
							sldDilateItera.getValue(), null);
				}
			}
			if (chkProcesaErode2.isSelected()) {

				if (rbtnNErode4.isSelected()) {
					output = BinaryImageOps.erode4(output,
							sliderNErode.getValue(), null);

				} else {
					output = BinaryImageOps.erode8(output,
							sliderNErode.getValue(), null);
				}
			}

			if (chckbxProcesaLErosion.isSelected()) {
				if (rdbtnLastErode4.isSelected()) {
					output = BinaryImageOps.dilate4(output,
							sliderLErode.getValue(), null);
				} else {
					output = BinaryImageOps.dilate8(output,
							sliderLErode.getValue(), null);
				}
			}
			BufferedImage bImage = VisualizeBinaryData.renderBinary(output,
					null);
			ImageIO.write(bImage, "png",
					new File(textImagen.getText() + ".png"));

			if (show != null) {
				show.dispose();
			}
			show = new ShowImages();
			show.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			show.mostrar(textImagen.getText(), textImagen.getText() + ".png");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
