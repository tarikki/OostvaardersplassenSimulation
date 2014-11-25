package view;

import model.MapHandler;
import model.Preserve;
import util.ButtonUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Pepe on 19.11.2014.
 */
public class MainandGUI {

    //// Screen size
    public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int DEFAULT_WIDTH = SCREEN_SIZE.width * 2 / 3;
    public static final int DEFAULT_HEIGHT = SCREEN_SIZE.height * 2 / 3;

    private JMenu simulationMenu; ////  Simulation menu
    private JMenu mapMenu; ///  map menu
    private JMenuBar menuBar;
    private static MapHolder mapHolder;
    private static StatisticsPanel statisticsPanel;



    private JPanel mapButtons; /// Could be moved to mapHolder since it only uses these
    private JPanel statisticButtons;

    private static MapHandler mapHandler;
    private static Preserve preserve;
    private static java.util.Timer timer;

    /// Instantiated here because simulationMenu needs to use it
    private JLabel drawHolder;
    private Legend legend;
    private JFileChooser chooser;

    // Constructor for creating the GUI
    public MainandGUI() {


        EventQueue.invokeLater(new Runnable() {
            public void run() {

                GUI gui = new GUI();
                gui.setVisible(true);

            }
        });
    }


    /// Run new GUI.. TODO move these to static block?
    public static void main(String[] args) {
        mapHandler = new MapHandler(); /// Create map
        createPreserve();
        new MainandGUI();



    }


    public void stopMovement() {
        timer.cancel();

    }

    public void resume() {
        moveTester();
    }

    /// Acts as our start method
    public static void moveTester() {
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    preserve.executeTurn();
                    mapHolder.refresh();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 100, 500);

    }

    /// Just initializing
    public static void createPreserve() {

        preserve = new Preserve(100); /// Create preserve with X amount of animals
    }

    /// Our frame with simulationMenu
    class GUI extends JFrame {


        private static final long serialVersionUID = 1L;

        private GUI.TabbedPane TabbedPane;

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
            this.setLayout(new BorderLayout());

            /// Create the tabbed pane
            TabbedPane = new GUI.TabbedPane();
            TabbedPane.setVisible(true);

            this.add(TabbedPane, BorderLayout.CENTER); // Add TabbedPane tab to center





        }

        public void createMenuBar()
        {
            menuBar = new JMenuBar();
            menuBar.setBackground(Color.black);
            menuBar.setForeground(Color.white);
            menuBar.setVisible(true);
            this.setJMenuBar(menuBar);
        }

        /// Menu and its buttons for Maps
        public void createMapMenu()
        {
            mapMenu = new JMenu("Map configuration");
            mapMenu.setBackground(Color.black);
            mapMenu.setForeground(Color.white);

            menuBar.add(mapMenu);

                    }
        public void createMapMenuButtons()
        {
            /// FIRST ITEM
            JMenuItem saveMap = new JMenuItem("Save map");
            saveMap.setBackground(Color.black);
            saveMap.setForeground(Color.white);
            saveMap.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    /// FUNCTIONALITY HERE;
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
                    /// FUNCTIONALITY HERE();
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
                        drawHolder.setVisible(false);
                    } else {
                        drawHolder.setVisible(true);
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
                        legend.setVisible(false);
                    } else {
                        legend.setVisible(true);
                    }
                }
            });
            simulationMenu.add(showHideLegend);


        }

        // Filechooser
        public void createFileChooser()
        {
            chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("MapFiles")); //Set directory to local MapFiles folder
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.addChoosableFileFilter(new FileFilter() //User can only save and load txt files
            {

                @Override
                public boolean accept(File file)
                {
                    return file.getName().endsWith(".txt");
                }

                @Override
                public String getDescription()
                {
                    return ".txt files";
                }

            });
        }




        /// ADD FUNCTIONALITY FOR MENU ITEMS HERE!
        public void createFirstPane() {


        }

        public void createSecondPane() {

        }

        public void createThirdPane() {

        }





        /// Main pane with tabs. TODO change BG-color
        class TabbedPane extends JTabbedPane {

            public TabbedPane() {

                super();

                /// Create the mapHolder
                mapHolder = new MapHolder(mapHandler);
                statisticsPanel = new StatisticsPanel();

                this.addTab("Map View", mapHolder); /// Add the mapHolder to our first tab
                this.addTab("Statistics",statisticsPanel);




            }
        }



        }

        /// Legend which MapView uses
        class Legend extends JLabel {
            public Legend() {
                super();
                this.setPreferredSize(new Dimension(100, 100));
                this.setVisible(true);


            }


            //// Very preliminary version of legend. TODO implement as a icon instead so we can position it better.
            @Override
            public void paintComponent(Graphics g) {

                super.paintComponent(g);

                g.setColor(Color.red); //// Add animal color with getColor
                g.fillRect(this.getWidth() / 2, this.getHeight() / 2, 10, 10);
                g.drawString("Dikke deer", this.getWidth() / 2 + 15, this.getHeight() / 2 + 10);
                g.setColor(Color.blue);
                g.fillRect(this.getWidth() / 2, this.getHeight() / 2 + 15, 10, 10);
                g.drawString("Tarikki", this.getWidth() / 2 + 15, this.getHeight() / 2 + 25);
            }

        }

        /// Statistics view
        class StatisticsPanel extends JPanel {
            JList chartList;
            String[] chartNames = {"Chart 1", "Chart 2", "Chart 3"};
            public StatisticsPanel() {
                this.setLayout(new BorderLayout());

                createStatisticButtons();
                this.add(statisticButtons, BorderLayout.SOUTH);

                createChartList();
                this.add(chartList, BorderLayout.WEST);



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
            public void createChartList()
            {
            chartList = new JList(chartNames);

            }
        }

        /// Panel to hold the Map /// MapView
        class MapHolder extends JPanel {


            private BufferedImage backgroundImage;
            private JLabel bgImageHolder;

            private ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
            private Graphics2D g2d;
            private BufferedImage drawingSurface;

            public MapHolder(MapHandler mapLoader) {



                this.setLayout(new BorderLayout());
                this.setOpaque(false);

                // Create buttons
                createMapButtons();

                /// Add legend
                legend = new Legend();
                this.add(legend, BorderLayout.NORTH);

                // Create the drawing surface
                drawingSurface = new BufferedImage(MapHandler.getWidth(), MapHandler.getHeight(), BufferedImage.TYPE_INT_ARGB);

                /// Set map as the background
                backgroundImage = mapLoader.getImage();
                bgImageHolder = new JLabel(new ImageIcon(backgroundImage));
                bgImageHolder.setLayout(new BorderLayout());
                add(bgImageHolder, BorderLayout.CENTER); /// Add the background (map) holder to the JPanel
                add(mapButtons, BorderLayout.SOUTH);


                /// Holder for drawingSurface
                drawHolder = new JLabel(new ImageIcon(drawingSurface));
                drawHolder.setVisible(true);
                drawHolder.setHorizontalAlignment(JLabel.CENTER); /// Center the img
                bgImageHolder.add(drawHolder);                    /// attach the drawingSurface to the background holder so we can have them on top of each other


                setVisible(true);

                animalstoRectangles();
                drawAnimals();
                repaint();


            }

            public void createMapButtons() {
                mapButtons = new JPanel();
                mapButtons.setLayout(new FlowLayout());
                mapButtons.setVisible(true);


                /// Start button
                ButtonUtils.addButton(mapButtons, "Start", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            // Enable restart and stop button and start simulation!
                            mapButtons.getComponent(1).setEnabled(true);
                            mapButtons.getComponent(2).setEnabled(true);
                            moveTester();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });

                // Stop button
                ButtonUtils.addButton(mapButtons, "Stop", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        stopMovement();

                    }
                });

                /// Disable the stop button so it can't be clicked until simulation has started!
                mapButtons.getComponent(1).setEnabled(false);

                // Reset button

                ButtonUtils.addButton(mapButtons, "Reset", new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        /// Clear the animal and create a new preserve
                        reset();
                    }
                });

                /// Disable the reset button so it can't be clicked until simulation has started!
                mapButtons.getComponent(2).setEnabled(false);


                // Exit button
                ButtonUtils.addButton(mapButtons, "Exit", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        preserve.stopThreads();
                        System.exit(1);
                    }
                });


            }
            public void reset() {
                timer.cancel();
                mapHolder.clearAnimals();
                drawHolder.setVisible(true);
                createPreserve();
                resume();
            }

            public void animalstoRectangles() {
                for (int i = 0; i < preserve.getNumberOfAnimals(); i++) {


                    rectangles.add(new Rectangle(preserve.getAnimalX(i), preserve.getAnimalY(i), 5, 5)); //// LOCATION X, LOCATION Y, WIDTH, HEIGHT


                }
            }


            public void drawAnimals() {
                g2d = (Graphics2D) drawingSurface.createGraphics();

                g2d.setColor(Color.red);


                super.paintComponent(g2d);
                for (Rectangle rectangle : rectangles) {
                    g2d.fill(rectangle);

                }
                repaint();
                g2d.dispose(); /// Remove graphics after drawing
            }


            public void clearAnimals() {
                g2d = (Graphics2D) drawingSurface.createGraphics();


                /// Set composite so we can draw transparent rectangles. Clearrect doesn't work here.
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));

                for (Rectangle rectangle : rectangles) {
                    g2d.fill(rectangle);

                }
                rectangles.clear();
                repaint();
                g2d.dispose();
            }

            public void refresh() {
                clearAnimals();
                animalstoRectangles();
                drawAnimals();


            }

        }


    }






