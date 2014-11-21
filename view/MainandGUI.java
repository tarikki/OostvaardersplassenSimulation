package view;

import model.MapHandler;
import model.Preserve;
import util.ButtonUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
    private GUI.MapHolder mapHolder;
    private JPanel buttonPanel;
    private MapHandler mapLoader;
    private Preserve preserve;


    // Constructor for creating the GUI
    public MainandGUI() {


        EventQueue.invokeLater(new Runnable() {
            public void run() {

                GUI gui = new GUI();

                gui.setVisible(true);


            }
        });

    }


    /// Run new GUI
    public static void main(String[] args) {

        new MainandGUI();
    }


    class GUI extends JFrame {


        private static final long serialVersionUID = 1L;

        public GUI() {

            /// Create the main frame for the application.

            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            this.setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

            createMenuBar();
            createMenuButtons();
            createButtons();

            /// Register map and preserve for GUI to use
            createMap();
            createPreserve();

            this.setTitle("Simulation project - Group 2");
            this.setLayout(new BorderLayout());


            /// Attach map to it's holder

            mapHolder = new MapHolder(mapLoader);
            mapHolder.setVisible(true);

            this.add(mapHolder, BorderLayout.CENTER); //// Add the mapHolder to the center of the frame
            this.add(buttonPanel, BorderLayout.SOUTH); /// Add buttons to the bottom


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

            /// FIRST MENU ITEM
            JMenuItem firstItem = new JMenuItem("PLACEHOLDER");
            firstItem.setBackground(Color.black);
            firstItem.setForeground(Color.white);
            menu.add(firstItem);
            firstItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    createSecondPane();
                }
            });


            /// SECOND MENU ITEM
            JMenuItem secondItem = new JMenuItem("PLACEHOLDER 2");
            secondItem.setBackground(Color.black);
            secondItem.setForeground(Color.white);

            secondItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    createFirstPane();
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

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            });

            // Stop button
            ButtonUtils.addButton(buttonPanel, "Stop", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {


                }
            });

            // Reset button

            ButtonUtils.addButton(buttonPanel, "Reset", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //reset();
                    mapHolder.clearAnimals();
                }
            });

            // Exit button
            ButtonUtils.addButton(buttonPanel, "Exit", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
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


        public void createPreserve() {

            preserve = new Preserve(50000); /// Create preserve with X amount of animals
        }

        public void createMap() {
            mapLoader = new MapHandler();
        }

        /// Panel to hold the Map
        class MapHolder extends JPanel {
            private BufferedImage backgroundImage;
            private JLabel bgImageHolder;
            private JLabel drawHolder;
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
               // clearAnimals();

            }

            public void animalstoRectangles() {
                for (int i = 0; i < preserve.getNumberOfAnimals(); i++) {


                    rectangles.add(new Rectangle(preserve.getAnimalX(i), preserve.getAnimalY(i), 1, 1)); //// LOCATION X, LOCATION Y, WIDTH, HEIGHT


                }
            }


            public void drawAnimals()
            {
                g2d = (Graphics2D) drawingSurface.createGraphics();

                g2d.setColor(Color.red);
                System.out.println(preserve.getNumberOfAnimals()); /// Just to drawingSurface for NullPointerException

                super.paintComponent(g2d);
                for (Rectangle rectangle : rectangles) {
                    g2d.fill(rectangle);
                    repaint();
                }
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



            }


        }
    }

