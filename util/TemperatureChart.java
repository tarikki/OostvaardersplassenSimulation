package util;

import model.Preserve;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.joda.time.Hours;

import java.awt.*;

/**
 * Created by Pepe on 19.12.2014.
 */
public class TemperatureChart {



    public JFreeChart chart;
    public DefaultCategoryDataset dataset;

    /**
     * Created by extradikke on 14/12/14.
     */


    /**
     * Creates a new demo.
     *
     * @param title the frame title.
     */
    public TemperatureChart(final String title) {


        dataset = createDataset();
        chart = createChart(dataset);


    }

    /// FIX LOGIC AND TIE TO PRESERVE
    private DefaultCategoryDataset createDataset() {

        Hours hours = Hours.hoursBetween(Preserve.getCurrentDate().toLocalTime(), Preserve.getCurrentDate().plusHours(1).toLocalTime());
        int hours2 = hours.getHours();


        dataset = new DefaultCategoryDataset();

      for (int i = 0; i<hours2; i++)
        {
            dataset.addValue(null, "Temperature", i);



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





        return chart;


    }

    public JFreeChart getChart() {
        return chart;
    }


    // Works somehow. TODO fix chance of eternal loop plus updating in StatsView
    // TODO make save chart button work. Create more charts!
    public void updatestats()
    {
        while (!Preserve.isNewDay())
        {
            dataset.addValue(Preserve.getCurrentTemperature(), "Temperature", Preserve.getCurrentDate().getHourOfDay() + ":00");



        }
    }
}




