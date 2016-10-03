package utils;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;

public class ChartsUtil {

    public static File createPieChart(String title, List<String> xValues, List<Double> yValues ) throws IOException
    {
        
        DefaultPieDataset dataset = new DefaultPieDataset();
        for(int n = 0; n < xValues.size(); n++)
        {
            dataset.setValue(xValues.get(n), yValues.get(n));
        }
        
        JFreeChart chart = ChartFactory.createPieChart3D(title, // chart title
                dataset, // data
                true, // include legend
                true, false);

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setForegroundAlpha(0.7f);
        
        File f = File.createTempFile("report", "image");
        ChartUtilities.saveChartAsPNG(f, chart, 200, 100);
        return f;
    }
}
