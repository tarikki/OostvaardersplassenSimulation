package view;

import model.Preserve;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import util.ButtonUtils;
import util.IOUtil;
import util.TemperatureChart;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Creates the statistics view tab of the program
 * Created by Pepe on 27.11.2014.
 */
public class StatisticsView extends JPanel {
    private JList chartList;
    private String[] chartNames = {"Temperature", "Chart 2", "Chart 3"};
    private JPanel statisticButtons;
    private JPanel chartHolder;
    private TemperatureChart temperatureChart;
    private MainView.GUI gui;

    public BufferedImage image;

    private JLabel test;


    public StatisticsView(MainView.GUI gui) {
        this.setLayout(new BorderLayout());

        this.gui = gui;



        createStatisticButtons();
        this.add(statisticButtons, BorderLayout.SOUTH);

        createChartList();
        chartList.setBackground(MainView.DEFAULT_BG_COLOR);

        /// Testing border
        Border border = BorderFactory.createLineBorder(Color.black);
        chartList.setBorder(border);
        this.add(chartList, BorderLayout.WEST);

        chartHolder = new JPanel();
        this.add(chartHolder, BorderLayout.CENTER);


        mouseListeners();


        gui.pack();


    }


    public void createCharts()
    {
        temperatureChart = new TemperatureChart("Temperatures");

        chartHolder.add(test);
        image = temperatureChart.getChart().createBufferedImage(chartHolder.getWidth(), chartHolder.getHeight());
    }

    public void createStatisticButtons() {
        statisticButtons = new JPanel();
        statisticButtons.setLayout(new FlowLayout());
        statisticButtons.setVisible(true);

        //TODO fix file path below
        /// Save button
        ButtonUtils.addButton(statisticButtons, "Save chart", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ChartUtilities.saveChartAsPNG(new File(IOUtil.getConfigDirectory() + "extradikke.png"), temperatureChart.getChart(), chartHolder.getWidth(), chartHolder.getHeight());

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });



    }

    // TODO add mouselisteners to each item in list and make them switch between charts
    public void createChartList() {
        chartList = new JList(chartNames);


        test = new JLabel();


    }

    public void mouseListeners() {
        chartList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    switch (chartList.getSelectedIndex()) {
                        case 0:
                            // the user selected the 1st item in the list; display the appropriate chart

                            test.setIcon(null);
                            createCharts();

                            temperatureChart.updatestats();
                            image = temperatureChart.getChart().createBufferedImage(chartHolder.getWidth(), chartHolder.getHeight());
                            chartHolder.repaint();

                            test.setIcon(new ImageIcon(image));
                            chartHolder.repaint();
                            gui.pack();


                            break;
                        case 1:
                            // the user selected the 2nd item in the list; display the appropriate chart
                            test.setIcon(null);
                            gui.pack();
                            break;
                        default:
                            /// Do something here if nothing is selected. Maybe display the 1st one by default??
gui.pack();
                            break;
                    }
                } else {
                    /// Do something here if nothing is selected. Maybe display the 1st one by default??
                    /// Maybe do nothing


                }
            }
        });
    }
}