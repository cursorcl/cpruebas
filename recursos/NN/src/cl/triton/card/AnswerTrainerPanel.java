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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
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
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.events.NeuralNetworkEvent;
import org.neuroph.core.events.NeuralNetworkEventListener;
import org.neuroph.core.exceptions.VectorSizeMismatchException;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;

import cl.sisdef.util.UtilForm;

/**
 * @author Turbo7
 */
public class AnswerTrainerPanel extends JPanel
    implements ActionListener, ChangeListener,
        NeuralNetworkEventListener, LearningEventListener
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

  JButton button_loadImageSet = null;

  JButton button_loadImage = null;
  JSpinner spin_imageOutput = null;
  JButton button_addTrainingSet = null;
  JLabel label_trainingSetSize = null;

  JButton button_train = null;
  JLabel label_trainIteration = null;
  JLabel label_trainNetworkError = null;

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

  // need to store this outside to set the output labels (...)
  JPanel centerPanel = null;

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
        24, 16, 512, 1, "input height", null,
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
    button_train.setEnabled(false);
    label_trainIteration = UtilForm.createLabel(" ",
        SwingConstants.CENTER, SwingConstants.CENTER, "Train iteration", null,
        null);
//        UtilForm.createBorder(BevelBorder.LOWERED));
    label_trainIteration.setPreferredSize(new Dimension(30, 40));
    label_trainNetworkError = UtilForm.createLabel(" ",
        SwingConstants.CENTER, SwingConstants.CENTER, "Network error", null,
        UtilForm.createBorder(BevelBorder.LOWERED));
    label_trainNetworkError.setPreferredSize(new Dimension(50, 40));

    button_recognize = UtilForm.createButton(
        "Recognize", "recognize the current image!", null, null, this, null);
    button_recognize.setEnabled(false);

    button_saveNetwork = UtilForm.createButton(
        "Save network", "save the trained network in a file", null, null, this, null);
    button_saveNetwork.setEnabled(false);

    button_loadImageSet = UtilForm.createButton(
        "Load set", "load a set of images to train", null, null, this, null);

    button_loadImage = UtilForm.createButton(
        "Load Image", "load an image to train or recognize", null, null, this, null);
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
    nntb.add(label_trainIteration);
    nntb.add(label_trainNetworkError);
    nntb.addSeparator();
    nntb.add(button_recognize);
    nntb.addSeparator();
    nntb.add(button_saveNetwork);

    add(nntb, BorderLayout.NORTH);

    imgp.resetSize(
        (Integer) spin_inputMatrixWidth.getValue(),
        (Integer) spin_inputMatrixHeight.getValue());

    preview.setBorder(UtilForm.createBorder("Preview", null, BevelBorder.LOWERED));
    preview.setPreferredSize(new Dimension(110, 130));

    JPanel controlPanel = new JPanel(new FlowLayout());
    controlPanel.setPreferredSize(new Dimension(110, 200));
    controlPanel.add(preview);

    centerPanel = new JPanel(new BorderLayout());
    centerPanel.add(controlPanel, BorderLayout.EAST);
    centerPanel.add(imgp, BorderLayout.CENTER);

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
    // the output length
    int outputLength = (int) spin_outputLength.getValue();

    // the input
    double[] input = new double[imgp.getInputSize()];
    for (int n=0; n<imgp.getInputSize(); n++)
      input[n] = imgp.getInput(n);

    // the output
    int outputValue = (Integer) spin_imageOutput.getValue();
    double[] output = new double[outputLength];
    for (int n=0; n<outputLength; n++)
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

      int outputLength = (int) spin_outputLength.getValue();
      int innerCellsCount =
          (imgp.getInputWidth() * imgp.getInputHeight() + outputLength) / 2 + 1;
      neuralNetwork = new MultiLayerPerceptron(
          imgp.getInputWidth() * imgp.getInputHeight(), innerCellsCount, outputLength);
      neuralNetwork.addListener(this);
      neuralNetwork.getLearningRule().addListener(this);

      dataset = new DataSet(imgp.getInputWidth() * imgp.getInputHeight(), outputLength);

      setCursor(null);
      Toolkit.getDefaultToolkit().beep();

      JPanel outputPanel = new JPanel(new GridLayout(1, outputLength, 2, 2));
      outputPanel.setBorder(UtilForm.createBorder(BevelBorder.LOWERED));
      label_output = new JLabel[outputLength];
      for (int n=0; n<outputLength; n++)
      {
        label_output[n] = UtilForm.createLabel("", null);
        label_output[n].setOpaque(true);
        outputPanel.add(label_output[n]);
      }
      centerPanel.add(outputPanel, BorderLayout.SOUTH);

      try
      {
        spin_imageOutput = UtilForm.createSpinner(
            0, 0, outputLength, 1, "image output", null,
            UtilForm.createBorder("Training output", null),
            this);
      }
      catch (Exception e)
      {
        log.warning(e.toString());
      }
      finally
      {
        // do nothing
      }

      JToolBar nmtb = new JToolBar();
      nmtb.add(button_loadImageSet);
      nmtb.addSeparator();
      nmtb.add(button_loadImage);
      nmtb.add(spin_imageOutput);
      nmtb.add(button_addTrainingSet);
      nmtb.addSeparator();
      nmtb.add(label_trainingSetSize);
      nmtb.addSeparator();
      nmtb.add(button_noise);

      add(nmtb, BorderLayout.SOUTH);

      spin_inputMatrixWidth.setVisible(false);
      spin_inputMatrixHeight.setVisible(false);
      spin_outputLength.setVisible(false);
      button_createNetwork.setVisible(false);

      button_createNetwork.setEnabled(false);
      button_train.setEnabled(true);

      validate();
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

    else if (event.getSource() == button_loadImageSet)
    {
      fileChooser.setDialogTitle("Choose the image catalog");
      int result = fileChooser.showOpenDialog(this);
      if (result != JFileChooser.APPROVE_OPTION)
        return;

      setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

      try
      {
        BufferedReader brdr = new BufferedReader(
            new FileReader(fileChooser.getSelectedFile()));
        String line = null;
        while ( (line = brdr.readLine()) != null)
        {
          String[] imgspec = line.split(",");
          if (imgspec.length != 2)
          {
            log.warning(String.format("Invalid format <output>, <file>: '%s'",
                line));
            continue;
          }
          int imgout = Integer.parseInt(imgspec[0].trim());
          String imgname = imgspec[1].trim();
          File imgfile = new File(fileChooser.getSelectedFile().getParent(), imgname);
          if (imgfile.exists() == false)
          {
            log.warning(String.format("Image file '%s' does not exist",
                imgfile.getAbsolutePath()));
            continue;
          }

          // add this image
          lastImage = ImageIO.read(imgfile);
          imgp.setImage(lastImage);
          spin_imageOutput.setValue(imgout);

          lastElement = evalCurrentDataset();

          dataset.addRow(lastElement.getDatasetRow());
          label_trainingSetSize.setText(Integer.toString(dataset.getRows().size()));
        }
      }
      catch (NumberFormatException e)
      {
        log.warning(e.toString());
      }
      catch (VectorSizeMismatchException e)
      {
        log.warning(e.toString());
      }
      catch (FileNotFoundException e)
      {
        log.warning(e.toString());
      }
      catch (IOException e)
      {
        log.warning(e.toString());
      }

      setCursor(null);
      Toolkit.getDefaultToolkit().beep();
    }

    else if (event.getSource() == button_addTrainingSet)
    {
      lastElement = evalCurrentDataset();

      dataset.addRow(lastElement.getDatasetRow());
      label_trainingSetSize.setText(Integer.toString(dataset.getRows().size()));
    }

    else if (event.getSource() == button_train)
    {
      button_train.setEnabled(false);

      setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

      // run in a thread to avoid swing lock
      new Thread(new Runnable()
      {
        @Override
        public void run()
        {
          neuralNetwork.learn(dataset);
        }
      }).start();

      button_recognize.setEnabled(true);
      button_saveNetwork.setEnabled(true);

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

      try
      {
        File trainFile = fileChooser.getSelectedFile();
        File nnfile = new File(
            trainFile.getParent(),
            String.format("%s.nn", trainFile.getName()));

        log.info(String.format("grid is recorded as %d x %d",
            imgp.getInputWidth(), imgp.getInputHeight()));

        // the file with the grid size (plus the nn file)
        PrintStream pstrm = new PrintStream(trainFile);
        pstrm.printf("%d %d\n", imgp.getInputWidth(), imgp.getInputHeight());
        pstrm.println(nnfile.getName());
        pstrm.close();
        // the file with the net
        neuralNetwork.save(nnfile.getAbsolutePath());
      }
      catch (FileNotFoundException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

  }

  @Override
  public void stateChanged(ChangeEvent event)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public void handleLearningEvent(LearningEvent event)
  {
    if ( (event.getSource() instanceof MomentumBackpropagation) == true)
    {
      MomentumBackpropagation mbp = (MomentumBackpropagation) event.getSource();
      label_trainIteration.setText(Integer.toString(mbp.getCurrentIteration()));
      label_trainIteration.invalidate();
      label_trainNetworkError.setText(
          String.format("%.4f", mbp.getTotalNetworkError()));
      label_trainNetworkError.invalidate();
//
//      log.info(String.format(
//          "learning: iter: %d, rate: %f, momentum: %f, error: %f",
//          mbp.getCurrentIteration(), mbp.getLearningRate(), mbp.getMomentum(),
//          mbp.getTotalNetworkError()));
    }
    else
      log.info(event.toString());
  }

  @Override
  public void handleNeuralNetworkEvent(NeuralNetworkEvent event)
  {
    log.info(event.toString());
  }
}
