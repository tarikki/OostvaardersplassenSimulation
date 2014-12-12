package view;

import model.Animal;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Pepe on 19.11.2014.
 */
public class MainView {

    //// Screen size
    public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int DEFAULT_WIDTH = SCREEN_SIZE.width * 2 / 3;
    public static final int DEFAULT_HEIGHT = SCREEN_SIZE.height * 2 / 3;
    public static final Color DEFAULT_BG_COLOR = new Color(238, 238, 238);

    private JMenu simulationMenu; ////  Simulation menu
    private JMenu mapMenu; ///  map menu
    private JMenuBar menuBar;
    public GUI gui;
    private StartScreen startScreen;


    private static java.util.Timer timer;

    /// Instantiated here because simulationMenu needs to use it

    private JFileChooser chooser;


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


    /// Our frame with simulationMenu
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


            createMapMenu();
            createMapMenuButtons();


            createFileChooser();

            this.setTitle("Simulation project - Group 2");


            /// Create the tabbed pane
            tabbedPane = new TabbedPane();
//            tabbedPane.setVisible(true);
            startScreen = new StartScreen(this);

            this.add(startScreen);
//            this.add(tabbedPane); // Add tabbedPane tab to center


        }

        public void createMenuBar() {
            menuBar = new JMenuBar();
            menuBar.setBackground(Color.black);
            menuBar.setForeground(Color.white);
            menuBar.setVisible(true);
            this.setJMenuBar(menuBar);
        }

        /// Menu and its buttons for Maps
        public void createMapMenu() {
            mapMenu = new JMenu("Map configuration");
            mapMenu.setBackground(Color.black);
            mapMenu.setForeground(Color.white);

            menuBar.add(mapMenu);

        }

        public JMenuBar getMenuBarHandler() {
            return menuBar;
        }

        public void createMapMenuButtons() {
            /// FIRST ITEM
            JMenuItem saveMap = new JMenuItem("Save map");
            saveMap.setBackground(Color.black);
            saveMap.setForeground(Color.white);
            saveMap.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    saveMap();
                }
            });
            mapMenu.add(saveMap);

            /// FOURTH MENU ITEM
            JMenuItem openMap = new JMenuItem("Open map");
            openMap.setBackground(Color.black);
            openMap.setForeground(Color.white);
            openMap.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openNewMap();
                }
            });
            mapMenu.add(openMap);
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
                    /// Toggle between show and hide legend
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
                        tabbedPane.mapView.legend.setVisible(false);
                        gui.pack();

                    } else {
                        tabbedPane.mapView.legend.setVisible(true);
                        gui.pack();

                    }
                }
            });
            simulationMenu.add(showHideLegend);


        }

        // Filechooser
        public void createFileChooser() {
            chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("MapFiles")); //Set directory to local MapFiles folder
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.addChoosableFileFilter(new FileFilter() //User can only save and load txt files
            {

                @Override
                public boolean accept(File file) {
                    return file.getName().endsWith(".png");
                }

                @Override
                public String getDescription() {
                    return ".png files";
                }

            });
        }


        /// ADD FUNCTIONALITY FOR MENU ITEMS HERE!
        public void saveMap() {

            int returnVal = chooser.showSaveDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                /// Add functionality to save map here!

                System.out.println("Saved: " + chooser.getSelectedFile().getAbsolutePath());
            }

        }

        public void openNewMap() {
            int returnVal = chooser.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File input = new File(chooser.getSelectedFile().getAbsolutePath());

                /// Add functionality to open a new map here

            }
        }

        public void createThirdPane() {

        }


    }


}










