package util;

import model.Preserve;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.joda.time.Days;

import java.awt.*;

/**
 * Created by Pepe on 19.12.2014.
 */
public class TemperatureChart {

    public JFreeChart getChart() {
        return chart;
    }

    public JFreeChart chart;
    public  XYDataset dataset;

    /**
     * Created by extradikke on 14/12/14.
     */




        /**
         * Creates a new demo.
         *
         * @param title  the frame title.
         */
        public TemperatureChart(final String title) {



            dataset = createDataset();
            chart = createChart(dataset);


        }

        /// FIX LOGIC AND TIE TO PRESERVE
        private XYDataset createDataset() {





           TimeSeries timeSeries =  new TimeSeries("Random Data", Hour.class);
            final Day today = new Day();
            for (int i = 0; i <= Days.daysBetween(Preserve.getStartDate().toLocalDate(), Preserve.getCurrentDate().toLocalDate()).getDays(); i++)
            {

                timeSeries.add(new Hour(i, today), Preserve.getCurrentTemperature());


            }

            final TimeSeriesCollection dataset = new TimeSeriesCollection();
            dataset.addSeries(timeSeries);

            return dataset;

        }


        /// HOURLY TEMPERATURE
        private JFreeChart createChart(final XYDataset dataset) {
            final String chartTitle = "Hourly temperature";
            final JFreeChart chart = ChartFactory.createTimeSeriesChart(
                    chartTitle,
                    "Time",
                    "Temperature",
                    dataset,
                    true,
                    true,
                    false
            );

            final XYPlot plot = chart.getXYPlot();
            //      plot.setInsets(new Insets(0, 0, 0, 20));
            final Marker marker = new ValueMarker(700.0);
            marker.setPaint(Color.blue);
            marker.setAlpha(0.8f);
            plot.addRangeMarker(marker);
            plot.setBackgroundPaint(null);

            final XYItemRenderer renderer = plot.getRenderer();
            if (renderer instanceof StandardXYItemRenderer) {
                final StandardXYItemRenderer r = (StandardXYItemRenderer) renderer;
                r.setShapesFilled(true);
            }


            return chart;


        }
    }




