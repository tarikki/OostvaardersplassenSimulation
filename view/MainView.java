package view;

import model.Preserve;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Controller for GUI. Also contains the main JFrame of the application
 * Created by Pepe on 19.11.2014.
 */
public class MainView {

    //// Screen size
    public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int DEFAULT_WIDTH = SCREEN_SIZE.width * 2 / 3;
    public static final int DEFAULT_HEIGHT = SCREEN_SIZE.height * 2 / 3;
    public static final Color DEFAULT_BG_COLOR = new Color(238, 238, 238);

    private JMenu simulationMenu;

    private JMenuBar menuBar;
    public GUI gui;
    private StartScreen startScreen;


    // Constructor for creating the GUI
    public MainView() {


        EventQueue.invokeLater(new Runnable() {
            public void run() {


                gui = new GUI();

                gui.setVisible(true);
                gui.pack();

            }
        });
    }


    /// Our main frame for the program
    public class GUI extends JFrame {
        public TabbedPane tabbedPane;

        private static final long serialVersionUID = 1L;


        public GUI() {

            /// Create the main frame for the application.

            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            this.setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));


            createMenuBar();


            createSimulationMenu();
            createSimulationMenuButtons();


            this.setTitle("Simulation project - Group 2");

            tabbedPane = new TabbedPane(this);

            startScreen = new StartScreen(this);

            this.add(startScreen);


        }

        public void createMenuBar() {
            menuBar = new JMenuBar();
            menuBar.setBackground(Color.black);
            menuBar.setForeground(Color.white);
            menuBar.setVisible(true);
            this.setJMenuBar(menuBar);
        }


        public JMenuBar getMenuBarHandler() {
            return menuBar;
        }


        /// Menu and its buttons for Simulation
        public void createSimulationMenu() {
            simulationMenu = new JMenu("Simulation options");
            simulationMenu.setBackground(Color.black);
            simulationMenu.setForeground(Color.white);
            menuBar.add(simulationMenu);

        }

        public void createSimulationMenuButtons() {
            final boolean[] counter = {false};
            final boolean[] counter2 = {false};

            /// FIRST MENU ITEM
            final JMenuItem showHideAnimals = new JMenuItem("Show / hide animals");
            showHideAnimals.setBackground(Color.black);
            showHideAnimals.setForeground(Color.white);
            simulationMenu.add(showHideAnimals);
            showHideAnimals.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    /// Toggle between show and hide animals
                    counter2[0] = !counter2[0];

                    if (counter2[0]) {
                        tabbedPane.mapView.drawHolder.setVisible(false);
                    } else {
                        tabbedPane.mapView.drawHolder.setVisible(true);
                    }


                }
            });


            /// SECOND MENU ITEM
            JMenuItem showHideLegend = new JMenuItem("Show / hide legend");
            showHideLegend.setBackground(Color.black);
            showHideLegend.setForeground(Color.white);

            showHideLegend.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    /// Toggle between show and hide legend.
                    counter[0] = !counter[0];

                    if (counter[0]) {
                        tabbedPane.mapView.briefStatistics.cow.setVisible(false);
                        tabbedPane.mapView.briefStatistics.deer.setVisible(false);
                        tabbedPane.mapView.briefStatistics.horse.setVisible(false);
                        tabbedPane.mapView.briefStatistics.legendTitle.setVisible(false);
                        gui.pack();

                    } else {
                        tabbedPane.mapView.briefStatistics.cow.setVisible(true);
                        tabbedPane.mapView.briefStatistics.deer.setVisible(true);
                        tabbedPane.mapView.briefStatistics.horse.setVisible(true);
                        tabbedPane.mapView.briefStatistics.legendTitle.setVisible(true);
                        gui.pack();

                    }
                }
            });
            simulationMenu.add(showHideLegend);


        }


    }


}










