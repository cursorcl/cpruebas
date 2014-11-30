/**
 * Oct 9, 2014 - ayachan
 */
package cl.cursor.card.testbed;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import cl.cursor.card.Recognizer;
import cl.cursor.card.RecognizerFactory;
import cl.sisdef.util.Pair;
import cl.sisdef.util.UtilForm;
import cl.sisdef.util.UtilLineLogging;

/**
 * @author ayachan
 */
public class TestTrainedRecognizer extends JFrame implements ActionListener
{
  private static final long serialVersionUID = 1L;

  static final Logger log =
      Logger.getLogger(TestTrainedRecognizer.class.getName());

  static final String START_DIRECTORY = "C:/PROYECTOS/CARD/answers";
  final JFileChooser fileChooser = new JFileChooser(START_DIRECTORY);

  JButton button_loadNetwork = null;
  JButton button_loadImage = null;

  JLabel label_result = null;

  List<Recognizer> recognizerList = new ArrayList<Recognizer>();
//  Recognizer recognizer = null;

  TestTrainedRecognizer(String title)
  {
    super(title);

    initialize();
  }

  final void initialize()
  {
    button_loadNetwork = UtilForm.createButton(
        "Load Network", "Load the network fron file", null, null, this, null);
    button_loadImage = UtilForm.createButton(
        "Test image", "Test to recognize an image on file", null, null, this, null);
    button_loadImage.setEnabled(false);

    JToolBar nntb = new JToolBar();
    nntb.add(button_loadNetwork);
    nntb.addSeparator();
    nntb.add(button_loadImage);

    label_result = UtilForm.createLabel("",
        SwingConstants.CENTER, SwingConstants.CENTER, "(this is the result)",
        null, UtilForm.createBorder("Recognition result", null));

    getContentPane().add(nntb, BorderLayout.NORTH);
    getContentPane().add(label_result, BorderLayout.CENTER);
  }

  @Override
  public void actionPerformed(ActionEvent event)
  {
    if (event.getSource() == button_loadNetwork)
    {
      fileChooser.setDialogTitle("Choose the stored network file");
      int result = fileChooser.showOpenDialog(this);
      if (result != JFileChooser.APPROVE_OPTION)
        return;

      try
      {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        Recognizer recognizer =
            RecognizerFactory.create(fileChooser.getSelectedFile());
        recognizerList.add(recognizer);

        button_loadImage.setEnabled(recognizer != null);

        setCursor(null);
        Toolkit.getDefaultToolkit().beep();
      }
      catch (IOException e)
      {
        log.warning(e.toString());
      }
    }
    else if (event.getSource() == button_loadImage)
    {
      fileChooser.setDialogTitle("Choose the image file to recognize");
      int result = fileChooser.showOpenDialog(this);
      if (result != JFileChooser.APPROVE_OPTION)
        return;

      try
      {
        Image image = ImageIO.read(fileChooser.getSelectedFile());
        recognize(image);
      }
      catch (IOException e)
      {
        log.warning(e.toString());
      }
    }

    // TODO Auto-generated method stub
    
  }

  void recognize(Image image)
  {
    StringBuffer sb = new StringBuffer();
    sb.append("<html><ul>\n");
    for (int n=0; n<recognizerList.size(); n++)
    {
      Pair<Integer, Pair<Double, Double>> result =
          recognizerList.get(n).recognize(image, 0.7);
      sb.append(String.format("<li>net %2d: index %2d (%.3f, %.3f)</li>\n",
          n, result.getFirst(),
          result.getSecond().getFirst(), result.getSecond().getSecond()));
    }
    sb.append("</ul></html>");
    label_result.setText(sb.toString());

//
//    int result = recognizer.recognize(image, 0.5);
//    label_result.setText(String.format(
//        "<html>and the answer is ...<br /><b>index %d</b></html>", result));
  }

  static void createAndShowGUI()
  {
    // Create and set up the window.
    JFrame frame = new TestTrainedRecognizer("Trainer");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setPreferredSize(new Dimension(300, 200));

    // Display the window.
    frame.pack();
    frame.setVisible(true);
  }

  /**
   * @param args
   */
  public static void main(String[] args)
  {
    UtilLineLogging.initialize(null);
    System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");

    javax.swing.SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run()
      {
        createAndShowGUI();
      }
    });
  }
}
