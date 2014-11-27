package view;

import model.MapHandler;
import model.Preserve;
import org.joda.time.DateTime;
import util.ButtonUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.Timer;

/**
 * Created by Pepe on 19.11.2014.
 */
public class MainandGUI {

    //// Screen size
    public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int DEFAULT_WIDTH = SCREEN_SIZE.width * 2 / 3;
    public static final int DEFAULT_HEIGHT = SCREEN_SIZE.height * 2 / 3;

    private JMenu menu;
    private JMenuBar menuBar;
    private static GUI.MapHolder mapHolder;

    private JPanel buttonPanel;
    private static MapHandler mapHandler;
    private static Preserve preserve;
    private static java.util.Timer timer;

    // Constructor for creating the GUI
    public MainandGUI() {


        EventQueue.invokeLater(new Runnable() {
            public void run() {

                GUI gui = new GUI();
                gui.setVisible(true);
                Timer timer2 = new Timer();

                timer2.schedule(new TimerTask() {
                    @Override
                    public void run() {


                            mapHolder.refresh();


                        }

                }, 100, 100);

            }
        });
    }


    /// Run new GUI
    public static void main(String[] args) {
        mapHandler = new MapHandler(); /// Create map
        createPreserve();
        new MainandGUI();
        ///    moveTester(); //// Probably needs to be called elsewhere


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
                    long start = System.nanoTime();
                    preserve.executeTurn();
//                    mapHolder.refresh();
                    System.out.println("Elapsed time: " + (System.nanoTime() - start) / 1000000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 5, 5);

    }

    /// Just initializing
    public static void createPreserve() {

        preserve = new Preserve(100, new DateTime(), (new DateTime()).plusDays(3)); /// Create preserve with X amount of animals
    }


    class GUI extends JFrame {


        private static final long serialVersionUID = 1L;
        private JLabel drawHolder;
        private Legend legend;

        public GUI() {

            /// Create the main frame for the application.

            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            this.setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

            createMenuBar();
            createMenuButtons();
            createButtons();


            this.setTitle("Simulation project - Group 2");
            this.setLayout(new BorderLayout());


            /// Attach map to it's holder

            mapHolder = new MapHolder(mapHandler);
            mapHolder.setVisible(true);

            this.add(mapHolder, BorderLayout.CENTER); //// Add the mapHolder to the center of the frame
            this.add(buttonPanel, BorderLayout.SOUTH); /// Add buttons to the bottom

            legend = new Legend();
            this.add(legend, BorderLayout.NORTH);


        }

        public void createMenuBar() {
            menu = new JMenu("Menu - click me for options");
            menu.setBackground(Color.black);
            menu.setForeground(Color.white);
            menuBar = new JMenuBar();
            menuBar.add(menu);
            menuBar.setBackground(Color.black);
            menuBar.setForeground(Color.white);
            menuBar.setVisible(true);
            this.setJMenuBar(menuBar);
        }

        public void createMenuButtons() {
            final boolean[] counter = {false};
            final boolean[] counter2 = {false};

            /// FIRST MENU ITEM
            final JMenuItem firstItem = new JMenuItem("Show / hide animals");
            firstItem.setBackground(Color.black);
            firstItem.setForeground(Color.white);
            menu.add(firstItem);
            firstItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    /// Toggle between show and hide legend
                    counter2[0] = !counter2[0];

                    if (counter2[0]) {
                        drawHolder.setVisible(false);
                    } else {
                        drawHolder.setVisible(true);
                    }

                    //createSecondPane();


                }
            });


            /// SECOND MENU ITEM
            JMenuItem secondItem = new JMenuItem("Show / hide legend");
            secondItem.setBackground(Color.black);
            secondItem.setForeground(Color.white);

            secondItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    /// Toggle between show and hide animals.
                    counter[0] = !counter[0];

                    if (counter[0]) {
                        legend.setVisible(false);
                    } else {
                        legend.setVisible(true);
                    }
                }
            });
            menu.add(secondItem);

            /// THIRD MENU ITEM
            JMenuItem thirdItem = new JMenuItem("PLACEHOLDER 3");
            thirdItem.setBackground(Color.black);
            thirdItem.setForeground(Color.white);
            thirdItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    createThirdPane();
                }
            });
            menu.add(thirdItem);
        }


        public void createButtons() {
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());
            buttonPanel.setVisible(true);


            /// Start button
            ButtonUtils.addButton(buttonPanel, "Start", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        // Enable restart and stop button and start simulation!
                        buttonPanel.getComponent(1).setEnabled(true);
                        buttonPanel.getComponent(2).setEnabled(true);
                        moveTester();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            });

            // Stop button
            ButtonUtils.addButton(buttonPanel, "Stop", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    stopMovement();

                }
            });

            /// Disable the stop button so it can't be clicked until simulation has started!
            buttonPanel.getComponent(1).setEnabled(false);

            // Reset button

            ButtonUtils.addButton(buttonPanel, "Reset", new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    /// Clear the animal and create a new preserve
                    reset();
                }
            });

            /// Disable the reset button so it can't be clicked until simulation has started!
            buttonPanel.getComponent(2).setEnabled(false);


            // Exit button
            ButtonUtils.addButton(buttonPanel, "Exit", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    preserve.stopThreads();
                    System.exit(1);
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


        /// Cancel timers, clear animals from map. Create new preserve and "resume" timer.
        public void reset() {
            timer.cancel();
            mapHolder.clearAnimals();
            drawHolder.setVisible(true);
            createPreserve();
            resume();
        }

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

        /// Panel to hold the Map
        class MapHolder extends JPanel {
            private BufferedImage backgroundImage;
            private JLabel bgImageHolder;

            private ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
            private Graphics2D g2d;
            private BufferedImage drawingSurface;

            public MapHolder(MapHandler mapLoader) {
                this.setLayout(new BorderLayout());
                this.setOpaque(false);
                drawingSurface = new BufferedImage(MapHandler.getWidth(), MapHandler.getHeight(), BufferedImage.TYPE_INT_ARGB);


                /// Set map as the background
                backgroundImage = mapLoader.getImage();
                bgImageHolder = new JLabel(new ImageIcon(backgroundImage));
                bgImageHolder.setLayout(new BorderLayout());
                add(bgImageHolder); /// Add the background (map) holder to the JPanel

                drawHolder = new JLabel(new ImageIcon(drawingSurface));
                drawHolder.setVisible(true);
                drawHolder.setHorizontalAlignment(JLabel.CENTER); /// Center the img
                bgImageHolder.add(drawHolder);                    /// attach the drawingSurface to the background holder so we can have them on top of each other


                setVisible(true);

                animalstoRectangles();
                drawAnimals();
                repaint();


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
}

