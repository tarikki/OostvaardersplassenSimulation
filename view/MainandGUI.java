package view;

import model.MapLoader;
import util.ButtonUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

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
    private MapLoader mapLoader;


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

            this.setTitle("Simulation project - Group 2");
            this.setLayout(new BorderLayout());


            /// Initialize mapLoader and MapHolder
            mapLoader = new MapLoader();
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


        /// Panel to hold the Map
        class MapHolder extends JPanel {
            private BufferedImage backgroundImage;
            private JLabel bgImageHolder;



            public MapHolder(MapLoader mapLoader) {
            this.setLayout(new BorderLayout());

                /// Set map as the background
                backgroundImage = mapLoader.getImage();
                bgImageHolder = new JLabel(new ImageIcon(backgroundImage));
                add(bgImageHolder, BorderLayout.CENTER);

            }


        }
    }
}
