package view;

import util.ButtonUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Veera on 27.11.2014.
 */
public class StatisticsView extends JPanel {
    private JList chartList;
    private String[] chartNames = {"Chart 1", "Chart 2", "Chart 3"};
    private JPanel statisticButtons;
    private JPanel chartHolder;

    private JLabel test;


    public StatisticsView() {
        this.setLayout(new BorderLayout());

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

        chartHolder.add(test);
        mouseListeners();


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
                    // Add functionality to save chart here

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        // Stop button
        ButtonUtils.addButton(statisticButtons, "PLACEHOLDER", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /// ENABLE BUTTON FUNCTIONALITY HERE

            }
        });

    }

    // TODO add mouselisteners to each item in list and make them switch between charts
    public void createChartList() {
        chartList = new JList(chartNames);

        test = new JLabel("test");



    }

    public void mouseListeners()
    {
        chartList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                if (!e.getValueIsAdjusting())
                {
                    switch(chartList.getSelectedIndex())
                    {
                        case 0:
                            // the user selected the 1st item in the list; display the appropriate chart
                            test.setText("EKANA");
                            break;
                        case 1:
                            // the user selected the 2nd item in the list; display the appropriate chart
                            test.setText("TOKANA");
                            break;
                        default:
                            /// Do something here if nothing is selected. Maybe display the 1st one by default??
                            test.setText("DEFAULT");
                            break;
                    }
                }
                else
                {
                    /// Do something here if nothing is selected. Maybe display the 1st one by default??
                    /// Maybe do nothing
                    test.setText("ELSE");

                }
            }
        });
    }
}