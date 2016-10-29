/**
 * Aug 9, 2014 - Turbo7
 */
package cl.triton.card;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;

import com.xenoage.util.gui.FontChooserComboBox;

import cl.sisdef.util.UtilForm;

/**
 * @author Turbo7
 */
public class TrainerPanel extends JPanel implements ActionListener, ChangeListener {
    class NumberActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            /*
             * generate the number image in the current form and set the input
             * grid pattern
             */

            // get the reference
            final JButton reference = (JButton) e.getSource();
            // the number
            final String number = reference.getText();

            setImage(number, reference.getFont().getFontName());
        }
    }

    private static final long serialVersionUID = 1L;

    static final Logger log = Logger.getLogger(TrainerPanel.class.getName());

    String fontSelectedName = null;
    JSpinner spin_inputMatrixWidth;
    JSpinner spin_inputMatrixHeight;

    JButton button_setInputSize;

    JButton button_createNetwork = null;
    JButton button_addTrainingSet = null;

    JLabel label_trainingSetSize = null;
    JButton button_train = null;

    JButton button_recognize = null;

    JButton button_noise = null;

    // a set of labels to know about the current output graphically
    JLabel[] label_output;

    int learningTargetValue;
    /*
     * Font reference: font selection and images.
     */
    FontChooserComboBox fontChooser;
    JButton[] numberImages;

    BufferedImage lastImage = null;
    /*
     * Grid image.
     */
    final ImagePanel imgp = new ImagePanel();

    final JLabel preview = new JLabel();
    JComboBox<Integer> combo_desiredOutput;

    JLabel label_netError;
    NeuralNetwork<BackPropagation> neuralNetwork = null;

    DataSet dataset = null;

    final NumberActionListener numberActionListener = new NumberActionListener();

    public TrainerPanel() {
        super();

        initialize();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == fontChooser) {
            fontSelectedName = fontChooser.getSelectedFontName();
            final Font imagesFont = new Font(fontSelectedName, Font.PLAIN, numberImages[0].getFont().getSize());
            for (int n = 0; n < 10; n++)
                numberImages[n].setFont(imagesFont);
        }

        else if (event.getSource() == button_setInputSize) {
            imgp.resetSize((Integer) spin_inputMatrixWidth.getValue(), (Integer) spin_inputMatrixHeight.getValue());
            if (lastImage != null)
                imgp.setImage(lastImage);
        }

        else if (event.getSource() == button_createNetwork) {
            final int innerCellsCount = (imgp.getInputWidth() * imgp.getInputHeight() + 10) / 2 + 1;
            neuralNetwork = new MultiLayerPerceptron(imgp.getInputWidth() * imgp.getInputHeight(), innerCellsCount, 10);
            dataset = new DataSet(imgp.getInputWidth() * imgp.getInputHeight(), 10);
        }

        else if (event.getSource() == button_addTrainingSet) {
            dataset.addRow(getImageDatasetRow());
            label_trainingSetSize.setText(Integer.toString(dataset.getRows().size()));
        }

        else if (event.getSource() == button_train) {
            neuralNetwork.learn(dataset);
        }

        else if (event.getSource() == button_recognize) {
            final DataSetRow dsr = getImageDatasetRow();
            neuralNetwork.setInput(dsr.getInput());
            neuralNetwork.calculate();
            final double[] output = neuralNetwork.getOutput();
            for (int n = 0; n < output.length; n++)
                label_output[n].setText(String.format("%.3f", output[n]));
        }

        else if (event.getSource() == button_noise) {
            imgp.noise();
        }

        // else if (event.getSource() == button_epoch)
        // {
        // for (int m=0; m<10; m++)
        // {
        // // set the image
        // setImage(Integer.toString(m), fontSelectedName);
        // // set the output target
        // learningTargetValue = m;
        // // feed the net with the image pixels
        // for (Layer layer : layers)
        // layer.invalidateOutput();
        // showOutput();
        // // train once
        // double error = 0.0;
        // {
        // // set the target value in the output neurons
        // for (int n=0; n<outputLayer.getSize(); n++)
        // {
        // OutputNeuron on = (OutputNeuron) outputLayer.getNeurons().get(n);
        // on.setTargetValue( (n == learningTargetValue) ? 1.0 : 0.0);
        // }
        //
        // for (int n=layers.size() - 1; n>=0; n--)
        // layers.get(n).adjustWeights();
        // showOutput();
        //
        // error = evaluateError();
        //// label_netError.setText(Double.toString(evaluateError()));
        // }
        //
        // log.info(String.format("epoch: learning pattern %d: error %f", m,
        // error));
        // }
        // }

        else if (event.getSource() == combo_desiredOutput) {
            learningTargetValue = ((Integer) combo_desiredOutput.getSelectedItem()).intValue();
            TrainerPanel.log.info(String.format("learning target value: %d", learningTargetValue));
        }

    }

    /**
     * @return The data set row corresponding to the current image
     */
    DataSetRow getImageDatasetRow() {
        // the input
        final double[] input = new double[imgp.getInputSize()];
        for (int n = 0; n < imgp.getInputSize(); n++)
            input[n] = imgp.getInput(n);

        // the output
        final double[] output = new double[10];
        for (int n = 0; n < 10; n++)
            output[n] = learningTargetValue == n ? 1.0 : 0.0;

        return new DataSetRow(input, output);
    }

    // double evaluateError()
    // {
    // double result = 0.0;
    // for (int n=0; n<10; n++)
    // {
    // double targetValue = (n == learningTargetValue) ? 1.0 : 0.0;
    // double value = outputLayer.getNeurons().get(n).getOutput() - targetValue;
    // result += (value * value);
    // }
    // return result;
    // }

    final void initialize() {
        setLayout(new BorderLayout());

        final Dimension spinSize = new Dimension(90, 40);
        spin_inputMatrixWidth = UtilForm.createSpinner(18, 8, 64, 1, "input width", null,
                UtilForm.createBorder("Input width", null), this);
        spin_inputMatrixWidth.setPreferredSize(spinSize);
        spin_inputMatrixHeight = UtilForm.createSpinner(24, 8, 64, 1, "input width", null,
                UtilForm.createBorder("Input height", null), this);
        spin_inputMatrixHeight.setPreferredSize(spinSize);
        button_setInputSize = UtilForm.createButton("Set size", "set the input size", null, null, this, null);

        button_createNetwork = UtilForm.createButton("Create network", "create the multi layer perceptron network",
                null, null, this, null);
        button_addTrainingSet = UtilForm.createButton("Add training set", "add current image to the training set", null,
                null, this, null);
        label_trainingSetSize = UtilForm.createLabel("000", null);
        button_train = UtilForm.createButton("Train", "train the network with the current training set", null, null,
                this, null);
        button_recognize = UtilForm.createButton("Recognize", "recognize the current image!", null, null, this, null);

        final JToolBar nntb = new JToolBar();
        nntb.addSeparator();
        nntb.add(spin_inputMatrixWidth);
        nntb.add(spin_inputMatrixHeight);
        nntb.add(button_setInputSize);
        nntb.addSeparator();
        nntb.add(button_createNetwork);
        nntb.add(button_addTrainingSet);
        nntb.add(label_trainingSetSize);
        nntb.addSeparator();
        nntb.add(button_train);
        nntb.addSeparator();
        nntb.add(button_recognize);

        fontChooser = new FontChooserComboBox();
        fontChooser.addActionListener(this);
        final JPanel numberImagesPanel = new JPanel(new GridLayout(1, 10));

        numberImages = new JButton[10];
        for (int n = 0; n < 10; n++) {
            numberImages[n] = UtilForm.createButton(Integer.toString(n), "use this pattern",
                    UtilForm.ContextFont.VVBIG.getFont(), null, numberActionListener, null);
            numberImages[n].setMargin(new Insets(0, 0, 0, 0));
            numberImagesPanel.add(numberImages[n]);
        }

        final JToolBar nmtb = new JToolBar();
        nmtb.add(fontChooser);
        nmtb.addSeparator();
        nmtb.add(numberImagesPanel);

        add(nntb, BorderLayout.NORTH);
        add(nmtb, BorderLayout.SOUTH);

        imgp.resetSize((Integer) spin_inputMatrixWidth.getValue(), (Integer) spin_inputMatrixHeight.getValue());

        preview.setBorder(UtilForm.createBorder("Preview", null, BevelBorder.LOWERED));
        preview.setPreferredSize(new Dimension(110, 130));

        label_netError = UtilForm.createLabel("0.0000", UtilForm.ContextFont.BIG.getFont());
        label_netError.setBorder(UtilForm.createBorder("Net error", null));
        label_netError.setPreferredSize(new Dimension(100, 40));
        combo_desiredOutput = UtilForm.createComboBox("desired output", null, UtilForm.createBorder("Output", null),
                this, null);
        for (int n = 0; n < 10; n++)
            combo_desiredOutput.addItem(n);
        combo_desiredOutput.setPreferredSize(new Dimension(60, 50));

        button_noise = UtilForm.createButton("Noise", "put noise at the current image", null, null, this, null);

        final JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setPreferredSize(new Dimension(110, 200));
        controlPanel.add(preview);
        controlPanel.add(label_netError);
        controlPanel.add(combo_desiredOutput);
        controlPanel.add(button_noise);

        final JPanel outputPanel = new JPanel(new GridLayout(1, 10, 2, 2));
        outputPanel.setBorder(UtilForm.createBorder(BevelBorder.LOWERED));
        final Dimension labelOutputDim = new Dimension(20, 20);
        label_output = new JLabel[10];
        for (int n = 0; n < 10; n++) {
            label_output[n] = UtilForm.createLabel("", null);
            label_output[n].setPreferredSize(labelOutputDim);
            label_output[n].setOpaque(true);
            outputPanel.add(label_output[n]);
        }

        final JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(controlPanel, BorderLayout.EAST);
        centerPanel.add(imgp, BorderLayout.CENTER);
        centerPanel.add(outputPanel, BorderLayout.SOUTH);
        // centerPanel.add(nnPanel, BorderLayout.NORTH);

        add(centerPanel, BorderLayout.CENTER);
    }

    void setImage(String number, String fontReference) {
        // build the image
        lastImage = new BufferedImage(96, 128, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g2d = lastImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, lastImage.getWidth(), lastImage.getHeight());
        g2d.setFont(new Font(fontReference, Font.PLAIN, 128));
        g2d.setColor(Color.BLACK);

        final FontMetrics fm = g2d.getFontMetrics();
        // int totalWidth = (fm.stringWidth(number) * 2) + 4;
        final int totalWidth = fm.stringWidth(number) + 4;

        // Baseline
        final int x = (lastImage.getWidth() - totalWidth) / 2;
        int y = (lastImage.getHeight() - fm.getHeight()) / 2;

        // g2d.drawString(number, x, y + ((fm.getDescent() + fm.getAscent()) /
        // 2));

        // Absolute...
        // x += fm.stringWidth(number) + 2;
        y = (lastImage.getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g2d.drawString(number, x, y);

        g2d.dispose();
        // to the preview
        preview.setIcon(new ImageIcon(lastImage));
        // paste to the grid
        imgp.setImage(lastImage);
    }

    void showOutput() {
        for (int n = 0; n < 10; n++) {
            // label_output[n].setText(
            // Double.toString(outputLayer.getNeurons().get(n).getOutput()));
        }
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        // TODO Auto-generated method stub

    }

}
