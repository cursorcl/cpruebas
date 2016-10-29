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
public class Trainer extends JFrame {
    private static final long serialVersionUID = 1L;

    static void createAndShowGUI() {
        // Create and set up the window.
        final JFrame frame = new Trainer("Trainer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600, 540));

        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        UtilLineLogging.initialize(null);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Trainer.createAndShowGUI();
            }
        });
    }

    Trainer(String title) {
        super(title);

        initialize();
    }

    final void initialize() {
        getContentPane().add(new TrainerPanel(), BorderLayout.CENTER);
    }

}
