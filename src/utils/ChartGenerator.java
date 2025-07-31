package utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.util.List;

/**
 * Generates charts for the game statistics.
 */
public class ChartGenerator {

    /**
     * Creates a chart of the score progression.
     * @param scores the list of scores
     * @return the chart panel
     */
    public static JPanel createScoreChart(List<Integer> scores) {
        return createChart("Score Progression", "Episode", "Score", new XYSeries("Score"), scores);
    }

    /**
     * Creates a chart of the loss progression.
     * @param losses the list of losses
     * @return the chart panel
     */
    public static JPanel createLossChart(List<Double> losses) {
        return createChart("Loss Progression", "Training Step", "Loss", new XYSeries("Loss"), losses);
    }

    private static <T extends Number> JPanel createChart(String title, String xLabel, String yLabel, XYSeries series, List<T> data) {
        for (int i = 0; i < data.size(); i++) {
            series.add(i, data.get(i));
        }
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                xLabel,
                yLabel,
                dataset
        );
        return new ChartPanel(chart);
    }
}
