package util;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import javafx.scene.chart.Chart;
import model.Preserve;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.joda.time.Hours;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Creates a line chart of daily temperatures in the preserve using the JFreeChart library
 * Created by Pepe on 19.12.2014.
 */
public class TemperatureChart {



    public JFreeChart chart;
    public DefaultCategoryDataset dataset;
    private double[] dailyTemps;





    public TemperatureChart(final String title) {


        dataset = createDataset();
        chart = createChart(dataset);
        dailyTemps = Preserve.getDailyTemperatures();

    }

    // TODO try to implement as XYChart instead if there's time. Mainly works as a blueprint for other charts / practice scenario at this point
    private DefaultCategoryDataset createDataset() {



        dataset = new DefaultCategoryDataset();

      for (int i = 0; i<24; i++)
        {
            dataset.addValue(null, "Temperature", i + ":00");



        }



        return dataset;

    }


    /// HOURLY TEMPERATURE
    private JFreeChart createChart(final DefaultCategoryDataset dataset) {
        final String chartTitle = "Hourly temperature for " + Preserve.getCurrentDate().toString("dd/MM/yyyy");
        this.chart = ChartFactory.createLineChart(chartTitle, "Hour of day", "Temperature in celsius", dataset, PlotOrientation.VERTICAL,
                true,
                true,
                false);



        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setDataset(dataset);
        if (chart != null)
        {
            chart.fireChartChanged();
        }
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinePaint(Color.black);


        /// For X-axis labels so we dont get "..." if value is over 3 characters
        plot.getDomainAxis().setMaximumCategoryLabelWidthRatio(10);


        return chart;


    }

    public JFreeChart getChart() {
        return chart;
    }


    // Get hourly temps per day
    public void updatestats()
    {

for (int i = 0; i < dailyTemps.length; i++) {
    dataset.addValue(dailyTemps[i], "Temperature", i + ":00");
}



    }


}




