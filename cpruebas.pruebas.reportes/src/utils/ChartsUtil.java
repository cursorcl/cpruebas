package utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;

public class ChartsUtil {

    public static File createBarChart(String title, String xTitle, String yTitle, List<String> xValues,
            List<Double> yValues) throws IOException {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int n = 0; n < xValues.size(); n++) {
            dataset.addValue(yValues.get(n), xTitle, xValues.get(n));
        }
        final JFreeChart chart = ChartFactory.createBarChart(title, // chart
                                                                    // title
                xTitle, // domain axis label
                yTitle, // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips?
                false // URLs?
        );
        chart.setBackgroundPaint(Color.white);

        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));

        final File f = File.createTempFile("report", "image");
        ChartUtilities.saveChartAsPNG(f, chart, 700, 300);
        return f;
    }

    public static File createBarChartSeries(String title, List<String> xValues, Map<String, List<Double>> yValues)
            throws IOException {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (final String serie : yValues.keySet()) {
            for (int n = 0; n < xValues.size(); n++) {
                dataset.addValue(yValues.get(serie).get(n), serie, xValues.get(n));
            }
        }
        final JFreeChart chart = ChartFactory.createBarChart(title, // chart
                                                                    // title
                "X", // domain axis label
                "Y", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips?
                false // URLs?
        );
        chart.setBackgroundPaint(Color.white);

        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        final BarRenderer renderer = new BarRenderer();
        renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        renderer.setDrawBarOutline(false);

        final CategoryPlot subplot2 = new CategoryPlot(dataset, null, rangeAxis, renderer);
        subplot2.setDomainGridlinesVisible(true);

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));

        final File f = File.createTempFile("report", "image");
        ChartUtilities.saveChartAsPNG(f, chart, 600, 300);
        return f;
    }

    public static File createLineChart(String title, String serie, List<String> xValues, List<Double> yValues)
            throws IOException {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int n = 0; n < xValues.size(); n++) {
            dataset.addValue(yValues.get(n), serie, xValues.get(n));
        }
        final JFreeChart chart = ChartFactory.createLineChart(title, // chart
                                                                     // title
                serie, // domain axis label
                "", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips?
                false // URLs?
        );
        chart.setBackgroundPaint(Color.white);

        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.BLACK);
        plot.setRangeGridlinePaint(Color.BLACK);
        plot.setRangeCrosshairPaint(Color.RED);
        plot.setOutlinePaint(Color.BLUE);
        final LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        plot.setRenderer(renderer);

        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        renderer.setSeriesShape(0, AbstractRenderer.DEFAULT_SHAPE);
        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 4.0));

        final File f = File.createTempFile("report", "image");
        ChartUtilities.saveChartAsPNG(f, chart, 800, 600);
        return f;
    }

    public static File createPieChart(String title, List<String> xValues, List<Double> yValues) throws IOException {

        final DefaultPieDataset dataset = new DefaultPieDataset();
        for (int n = 0; n < xValues.size(); n++) {
            dataset.setValue(xValues.get(n), yValues.get(n));
        }

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

        final File f = File.createTempFile("report", "image");
        ChartUtilities.saveChartAsPNG(f, chart, 600, 300);
        return f;
    }

}
