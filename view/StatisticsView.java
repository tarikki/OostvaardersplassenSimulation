package view;

import model.Preserve;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import util.ButtonUtils;
import util.IOUtil;
import util.PopulationChangeChart;
import util.TemperatureChart;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Creates the statistics view tab of the program
 * Created by Pepe on 27.11.2014.
 */
public class StatisticsView extends JPanel {
    private JList chartList;
    private String[] chartNames = {"Hourly temperature for current day", "Changes in populations"};
    private JPanel statisticButtons;
    private JPanel chartHolder;
    private TemperatureChart temperatureChart;
    private PopulationChangeChart populationchangeChart;
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


    public void createCharts() {
        temperatureChart = new TemperatureChart("Temperatures");
        populationchangeChart = new PopulationChangeChart();
        chartHolder.add(test);
        image = temperatureChart.getChart().createBufferedImage(chartHolder.getWidth(), chartHolder.getHeight());
    }

    public void createStatisticButtons() {
        statisticButtons = new JPanel();
        statisticButtons.setLayout(new FlowLayout());
        statisticButtons.setVisible(true);


        /// Save button
        ButtonUtils.addButton(statisticButtons, "Save chart", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                   saveChart();
                    JOptionPane.showMessageDialog(gui.getRootPane(), "Saved chart successfully", "Chart saved", JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });


    }

    public void saveChart() throws IOException {
        File f = new File(IOUtil.getConfigDirectory() +"SavedChart.png");
        ImageIO.write(image, "PNG", f);
    }


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


                            image = temperatureChart.getChart().createBufferedImage(chartHolder.getWidth(), chartHolder.getHeight());
                            chartHolder.repaint();

                            test.setIcon(new ImageIcon(image));
                            chartHolder.repaint();
                            gui.pack();


                            break;
                        case 1:
                            // the user selected the 2nd item in the list; display the appropriate chart
                            createCharts();
                            test.setIcon(null);
                            image = populationchangeChart.getChart().createBufferedImage(chartHolder.getWidth(), chartHolder.getHeight());
                            chartHolder.repaint();

                            test.setIcon(new ImageIcon(image));
                            chartHolder.repaint();
                            gui.pack();
                            break;
                        default:
                            /// Do something here if nothing is selected. Maybe display the 1st one by default??
                            gui.pack();
                            break;
                    }
                } else {
                    /// Do something here if nothing is selected. Maybe display the 1st one by default??



                }
            }
        });
    }
}