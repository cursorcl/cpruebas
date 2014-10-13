/**
 * Aug 9, 2014 - Turbo7
 */
package cl.triton.card;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
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

import cl.sisdef.util.UtilForm;

/**
 * @author Turbo7
 */
public class AnswerTrainerPanel extends JPanel
    implements ActionListener, ChangeListener
{
  private static final long serialVersionUID = 1L;

  static final Logger log = Logger.getLogger(AnswerTrainerPanel.class.getName());

  static final String START_DIRECTORY = "C:/PROYECTOS/CARD/answers";
  final JFileChooser fileChooser = new JFileChooser(START_DIRECTORY);

  JButton button_saveNetwork = null;

  JSpinner spin_inputMatrixWidth;
  JSpinner spin_inputMatrixHeight;
  JSpinner spin_outputLength;

  JButton button_createNetwork = null;

  JButton button_loadImage = null;
  JSpinner spin_imageOutput = null;
  JButton button_addTrainingSet = null;
  JLabel label_trainingSetSize = null;

  JButton button_train = null;
  JButton button_recognize = null;

  JButton button_noise = null;

  // a set of labels to know about the current output graphically
  JLabel[] label_output;

  int learningTargetValue;

  class TrainingElement
  {
    final BufferedImage sourceImage;
    final double[] input;
    final double[] output;

    final DataSetRow datasetRow;

    TrainingElement(BufferedImage sourceImage, double[] input, double[] output)
    {
      this.sourceImage = sourceImage;
      this.input = input;
      this.output = output;

      this.datasetRow = new DataSetRow(input, output);
    }

    public double[] getInput()
    {
      return input;
    }

    public double[] getOutput()
    {
      return output;
    }

    DataSetRow getDatasetRow()
    {
      return datasetRow;
    }
  }

  final List<TrainingElement> training = new ArrayList<TrainingElement>();
  TrainingElement lastElement = null;

  BufferedImage lastImage = null;

  /*
   * Grid image.
   */
  final ImagePanel imgp = new ImagePanel();
  final JLabel preview = new JLabel();

//  JComboBox<Integer> combo_desiredOutput;
//  JLabel label_netError;

  NeuralNetwork<BackPropagation> neuralNetwork = null;
  DataSet dataset = null;

  public AnswerTrainerPanel()
  {
    super();

    initialize();
  }

  final void initialize()
  {
    setLayout(new BorderLayout());

    Dimension spinSize = new Dimension(90, 40);
    spin_inputMatrixWidth = UtilForm.createSpinner(
        64, 16, 512, 1, "input width", null,
        UtilForm.createBorder("Input width", null),
        this);
    spin_inputMatrixWidth.setPreferredSize(spinSize);
    spin_inputMatrixHeight = UtilForm.createSpinner(
        24, 13, 256, 1, "input height", null,
        UtilForm.createBorder("Input height", null),
        this);
    spin_inputMatrixHeight.setPreferredSize(spinSize);
    spin_outputLength = UtilForm.createSpinner(
        10, 2, 20, 1, "output length", null,
        UtilForm.createBorder("Output length", null),
        this);
    spin_outputLength.setPreferredSize(spinSize);

    button_createNetwork = UtilForm.createButton(
        "Create network", "create the multi layer perceptron network", null, null, this, null);
    button_train = UtilForm.createButton(
        "Train", "train the network with the current training set", null, null, this, null);
    button_recognize = UtilForm.createButton(
        "Recognize", "recognize the current image!", null, null, this, null);

    button_saveNetwork = UtilForm.createButton(
        "Save network", "save the trained network in a file", null, null, this, null);

    button_loadImage = UtilForm.createButton(
        "Load Image", "load an image to train or recognize", null, null, this, null);
    spin_imageOutput = UtilForm.createSpinner(
        0, 0, 10, 1, "image output", null,
        UtilForm.createBorder("Training output", null),
        this);
    label_trainingSetSize = UtilForm.createLabel("000", null);
    button_addTrainingSet = UtilForm.createButton(
        "Add training set", "add current image to the training set", null, null, this, null);

    button_noise = UtilForm.createButton(
        "Noise", "put noise at the current image", null, null, this, null);

    JToolBar nntb = new JToolBar();
    nntb.addSeparator();
    nntb.add(spin_inputMatrixWidth);
    nntb.add(spin_inputMatrixHeight);
    nntb.add(spin_outputLength);
    nntb.addSeparator();
    nntb.add(button_createNetwork);
    nntb.addSeparator();
    nntb.add(button_train);
    nntb.add(button_recognize);
    nntb.addSeparator();
    nntb.add(button_saveNetwork);

    JToolBar nmtb = new JToolBar();
    nmtb.add(button_loadImage);
    nmtb.add(spin_imageOutput);
    nmtb.add(button_addTrainingSet);
    nmtb.addSeparator();
    nmtb.add(label_trainingSetSize);
    nmtb.addSeparator();
    nmtb.add(button_noise);

    add(nntb, BorderLayout.NORTH);
    add(nmtb, BorderLayout.SOUTH);

    imgp.resetSize(
        (Integer) spin_inputMatrixWidth.getValue(),
        (Integer) spin_inputMatrixHeight.getValue());

    preview.setBorder(UtilForm.createBorder("Preview", null, BevelBorder.LOWERED));
    preview.setPreferredSize(new Dimension(110, 130));

    JPanel controlPanel = new JPanel(new FlowLayout());
    controlPanel.setPreferredSize(new Dimension(110, 200));
    controlPanel.add(preview);

    JPanel outputPanel = new JPanel(new GridLayout(1, 10, 2, 2));
    outputPanel.setBorder(UtilForm.createBorder(BevelBorder.LOWERED));
//    Dimension labelOutputDim = new Dimension(20, 20);
    label_output = new JLabel[10];
    for (int n=0; n<10; n++)
    {
      label_output[n] = UtilForm.createLabel("", null);
//      label_output[n].setPreferredSize(labelOutputDim);
//      label_output[n].setPreferredSize(new Dimension(20, 20));
      label_output[n].setOpaque(true);
      outputPanel.add(label_output[n]);
    }

    JPanel centerPanel = new JPanel(new BorderLayout());
    centerPanel.add(controlPanel, BorderLayout.EAST);
    centerPanel.add(imgp, BorderLayout.CENTER);
    centerPanel.add(outputPanel, BorderLayout.SOUTH);

    add(centerPanel, BorderLayout.CENTER);
  }

  void showOutput()
  {
    for (int n=0; n<10; n++)
    {
//      label_output[n].setText(
//          Double.toString(outputLayer.getNeurons().get(n).getOutput()));
    }
  }

  TrainingElement evalCurrentDataset()
  {
    // the input
    double[] input = new double[imgp.getInputSize()];
    for (int n=0; n<imgp.getInputSize(); n++)
      input[n] = imgp.getInput(n);

    // the output
    int outputValue = (Integer) spin_imageOutput.getValue();
    double[] output = new double[10];
    for (int n=0; n<10; n++)
      output[n] = (outputValue == n) ? 1.0 : 0.0;

    // the data set
    return new TrainingElement(lastImage, input, output);
  }

  @Override
  public void actionPerformed(ActionEvent event)
  {
    if (event.getSource() == button_createNetwork)
    {
      setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

      imgp.resetSize(
          (Integer) spin_inputMatrixWidth.getValue(),
          (Integer) spin_inputMatrixHeight.getValue());
      if (lastImage != null)
        imgp.setImage(lastImage);

      int innerCellsCount =
          (imgp.getInputWidth() * imgp.getInputHeight() + 10) / 2 + 1;
      neuralNetwork = new MultiLayerPerceptron(
          imgp.getInputWidth() * imgp.getInputHeight(), innerCellsCount, 10);
      dataset = new DataSet(imgp.getInputWidth() * imgp.getInputHeight(), 10);

      setCursor(null);
      Toolkit.getDefaultToolkit().beep();
    }

    else if (event.getSource() == button_loadImage)
    {
      fileChooser.setDialogTitle("Choose an image to load");
      int result = fileChooser.showOpenDialog(this);
      if (result != JFileChooser.APPROVE_OPTION)
        return;

      try
      {
        lastImage = ImageIO.read(fileChooser.getSelectedFile());
        imgp.setImage(lastImage);
      }
      catch (IOException e)
      {
        log.warning(e.toString());
      }
    }

    else if (event.getSource() == button_addTrainingSet)
    {
      lastElement = evalCurrentDataset();

      dataset.addRow(lastElement.getDatasetRow());
      label_trainingSetSize.setText(Integer.toString(dataset.getRows().size()));
    }

    else if (event.getSource() == button_train)
    {
      setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

      neuralNetwork.learn(dataset);

      setCursor(null);
      Toolkit.getDefaultToolkit().beep();
    }

    else if (event.getSource() == button_recognize)
    {
      TrainingElement element = evalCurrentDataset();
      neuralNetwork.setInput(element.getInput());
      neuralNetwork.calculate();
      double[] output = neuralNetwork.getOutput();
      for (int n=0; n<output.length; n++)
        label_output[n].setText(String.format("%.3f", output[n]));
    }

    else if (event.getSource() == button_noise)
    {
      imgp.noise();
    }

    else if (event.getSource() == button_saveNetwork)
    {
      fileChooser.setDialogTitle("Choose a file to save the trained network");
      int result = fileChooser.showSaveDialog(this);
      if (result != JFileChooser.APPROVE_OPTION)
        return;

      neuralNetwork.save(fileChooser.getSelectedFile().getAbsolutePath());
    }

  }

//  /**
//   * @return The data set row corresponding to the current image
//   */
//  DataSetRow getImageDatasetRow()
//  {
//    // the input
//    double[] input = new double[imgp.getInputSize()];
//    for (int n=0; n<imgp.getInputSize(); n++)
//      input[n] = imgp.getInput(n);
//
//    // the output
//    double[] output = new double[10];
//    for (int n=0; n<10; n++)
//      output[n] = (learningTargetValue == n) ? 1.0 : 0.0;
//
//    return new DataSetRow(input, output);
//  }

  @Override
  public void stateChanged(ChangeEvent event)
  {
    // TODO Auto-generated method stub
    
  }

//  void setImage(String number, String fontReference)
//  {
//    // build the image
//    lastImage = new BufferedImage(96, 128, BufferedImage.TYPE_INT_RGB);
//    Graphics2D g2d = lastImage.createGraphics();
//    g2d.setColor(Color.WHITE);
//    g2d.fillRect(0, 0, lastImage.getWidth(), lastImage.getHeight());
//    g2d.setFont(new Font(fontReference, Font.PLAIN, 128));
//    g2d.setColor(Color.BLACK);
//
//    FontMetrics fm = g2d.getFontMetrics();
////    int totalWidth = (fm.stringWidth(number) * 2) + 4;
//    int totalWidth = fm.stringWidth(number) + 4;
//
//    // Baseline
//    int x = (lastImage.getWidth() - totalWidth) / 2;
//    int y = (lastImage.getHeight() - fm.getHeight()) / 2;
//
////    g2d.drawString(number, x, y + ((fm.getDescent() + fm.getAscent()) / 2));
//
//    // Absolute...
////    x += fm.stringWidth(number) + 2;
//    y = ((lastImage.getHeight() - fm.getHeight()) / 2) + fm.getAscent();
//    g2d.drawString(number, x, y);
//
//
//    g2d.dispose();
//    // to the preview
//    preview.setIcon(new ImageIcon(lastImage));
//    // paste to the grid
//    imgp.setImage(lastImage);
//  }
//
//  final NumberActionListener numberActionListener = new NumberActionListener();
//  class NumberActionListener implements ActionListener
//  {
//    @Override
//    public void actionPerformed(ActionEvent e)
//    {
//      /*
//       * generate the number image in the current form and set the input grid
//       * pattern
//       */
//
//      // get the reference
//      JButton reference = (JButton)e.getSource();
//      // the number
//      String number = reference.getText();
//
//      setImage(number, reference.getFont().getFontName());
//    }
//  }

}
