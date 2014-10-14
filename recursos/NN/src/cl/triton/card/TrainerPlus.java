/**
 * Aug 9, 2014 - Turbo7
 */
package cl.triton.card;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import cl.sisdef.util.UtilLineLogging;

/**
 * @author Turbo7
 */
public class TrainerPlus extends JFrame
{
  private static final long serialVersionUID = 1L;

  TrainerPlus(String title)
  {
    super(title);

    initialize();
  }

  final void initialize()
  {
    getContentPane().add(new AnswerTrainerPanel(), BorderLayout.CENTER);
  }

  static void createAndShowGUI()
  {
    // Create and set up the window.
    JFrame frame = new TrainerPlus("Trainer");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setPreferredSize(new Dimension(800, 400));

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
