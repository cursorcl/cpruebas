package utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

public class Charts extends JDialog {

    private static final long serialVersionUID = 1L;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            final Charts dialog = new Charts();
            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private final JPanel contentPanel = new JPanel();

    /**
     * Create the dialog.
     */
    public Charts() {
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        {
            final JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                final JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                okButton.addActionListener(e -> {
                    try {
                        example();
                    } catch (final IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                });
                getRootPane().setDefaultButton(okButton);
            }
            {
                final JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
    }

    private JFreeChart createChart(PieDataset dataset, String title) {
        final JFreeChart chart = ChartFactory.createPieChart3D(title, // chart
                                                                      // title
                dataset, // data
                true, // include legend
                true, false);

        final PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setForegroundAlpha(0.7f);
        return chart;
    }

    private PieDataset createDataset() {
        final DefaultPieDataset result = new DefaultPieDataset();
        result.setValue("Linux", 29);
        result.setValue("Mac", 20);
        result.setValue("Windows", 51);
        return result;
    }

    protected void example() throws IOException {
        final PieDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset, "PRUEBA");
        ChartUtilities.saveChartAsPNG(new File("miimagen.png"), chart, 200, 100);

        final ChartPanel chartPanel = new ChartPanel(chart);
        contentPanel.setLayout(new BorderLayout());
        final Runnable r = () -> {
            contentPanel.add(chartPanel, BorderLayout.CENTER);
            contentPanel.repaint();
            contentPanel.invalidate();
        };
        SwingUtilities.invokeLater(r);
    }

}
