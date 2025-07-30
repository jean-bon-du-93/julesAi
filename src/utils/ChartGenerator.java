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
        XYSeries series = new XYSeries("Score");
        for (int i = 0; i < scores.size(); i++) {
            series.add(i, scores.get(i));
        }
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Score Progression",
                "Episode",
                "Score",
                dataset
        );
        return new ChartPanel(chart);
    }
}
