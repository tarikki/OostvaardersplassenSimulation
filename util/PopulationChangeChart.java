package util;

import model.Preserve;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;

/**
 * Created by Pepe on 18.1.2015.
 */
public class PopulationChangeChart {
    CategoryDataset dataset;
    JFreeChart chart;


    public PopulationChangeChart() {
        dataset = createDataset();
        chart = createChart(dataset);
    }

    private CategoryDataset createDataset() {

        // row keys...
        final String series1 = "Population at start";
        final String series2 = "Final population";
        final String series3 = "Births";
        final String series4 = "Deaths";

        // column keys...
        final String category1 = "Cows";
        final String category2 = "Deer";
        final String category3 = "Horses";


        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();


        dataset.addValue(Preserve.getStatisticsStorage().get("Cow").getNumberAtStart(), series1, category1); /// Cow population at start
        dataset.addValue(Preserve.getStatisticsStorage().get("Deer").getNumberAtStart(), series1, category2); /// Deer population at start
        dataset.addValue(Preserve.getStatisticsStorage().get("Horse").getNumberAtStart(), series1, category3); /// Horse population at start

        dataset.addValue(Preserve.getStatisticsStorage().get("Cow").getNumberAtEnd(), series2, category1); // Final cow population
        dataset.addValue(Preserve.getStatisticsStorage().get("Deer").getNumberAtEnd(), series2, category2); // Final deer population
        dataset.addValue(Preserve.getStatisticsStorage().get("Horse").getNumberAtEnd(), series2, category3); // Final horse population

        dataset.addValue(Preserve.getStatisticsStorage().get("Cow").getBirths(), series3, category1); // Final cow births
        dataset.addValue(Preserve.getStatisticsStorage().get("Deer").getBirths(), series3, category2); // Final deer births
        dataset.addValue(Preserve.getStatisticsStorage().get("Horse").getBirths(), series3, category3); // Final horse births


        dataset.addValue(Preserve.getStatisticsStorage().get("Cow").getDeaths(), series4, category1); // Cow deaths
        dataset.addValue(Preserve.getStatisticsStorage().get("Deer").getDeaths(), series4, category2); // Deer deaths
        dataset.addValue(Preserve.getStatisticsStorage().get("Horse").getDeaths(), series4, category3); // Horse deaths




        return dataset;

    }


    public JFreeChart getChart() {
        return chart;
    }

    private JFreeChart createChart(final CategoryDataset dataset) {

        // create the chart...
        this.chart = ChartFactory.createBarChart(
                "Changes in populations",         // chart title
                "Changes in population per type of animal",               // domain axis label
                "Number of animals",                  // range axis label
                dataset,                  // data
                PlotOrientation.VERTICAL, // orientation
                true,                     // include legend
                true,                     // tooltips?
                false                     // URLs?
        );


        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);


        // disable bar outlines...
        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);


        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesPaint(1, Color.YELLOW);
        renderer.setSeriesPaint(2, Color.GREEN);


        return chart;

    }

}
