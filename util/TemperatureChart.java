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
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
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



    public TimeSeries series;
    public JFreeChart chart;
    public TimeSeriesCollection dataset;
    private double[] dailyTemps;


    public TemperatureChart(final String title) {
        dailyTemps = Preserve.getDailyTemperatures();
        series = new TimeSeries("Hourly temperature in celsius", Hour.class);
        final Day today = new Day();

        for (int i = 0; i < dailyTemps.length; i++) {
            series.add(new Hour(i, today), dailyTemps[i]);
        }

        dataset = new TimeSeriesCollection(series);


        chart = createChart();


    }


    private JFreeChart createChart() {
        final String chartTitle = "Hourly temperature for " + Preserve.getCurrentDate().toString("dd/MM/yyyy");
        final JFreeChart chart = ChartFactory.createTimeSeriesChart(
                chartTitle, // title
                "Hour of day", /// x-axis title
                "Temperature in celsius", /// y-axis title
                dataset, /// dataset
                true, /// legend
                true, /// tooltip
                false /// urls
        );


        if (chart != null) {
            chart.fireChartChanged();
        }

        final XYPlot plot = chart.getXYPlot();
        plot.getDomainAxis().setAutoRange(false);
        return chart;


    }

    public JFreeChart getChart() {
        return chart;
    }


}




