package view;

import model.Animal;
import model.MapHandler;
import util.ButtonUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static controller.Main.*;

/**
 * Created by Pepe on 27.11.2014.
 */
public class MapView extends JPanel {

    public Legend legend;
    private JPanel mapButtons;
    public  JLabel drawHolder;
    private BufferedImage backgroundImage;
    private JLabel bgImageHolder;


    private Graphics2D g2d;
    private BufferedImage drawingSurface;
    private ArrayList<AnimalRectangle> rectangles;

    public MapView(ArrayList <Animal> animals) {

        rectangles = new ArrayList<AnimalRectangle>();
        this.setLayout(new BorderLayout());

        this.setOpaque(false);

        // Create buttons
        createMapButtons();

        this.add(mapButtons, BorderLayout.SOUTH);

        /// Add legend
        legend = new Legend();
        this.add(legend, BorderLayout.NORTH);

        // Create the drawing surface
        drawingSurface = new BufferedImage(MapHandler.getWidth(), MapHandler.getHeight(), BufferedImage.TYPE_INT_ARGB);

        /// Set map as the background
        backgroundImage = MapHandler.getImage();
        bgImageHolder = new JLabel(new ImageIcon(backgroundImage));
        ToolTipManager.sharedInstance().registerComponent(bgImageHolder);
        bgImageHolder.setLayout(new BorderLayout()); /// Needs to be here so we can position drawHolder and drawingSurface


        this.add(bgImageHolder, BorderLayout.WEST); /// Add the background (map) holder to the JPanel



        /// Holder for drawingSurface
        drawHolder = new JLabel(new ImageIcon(drawingSurface));
        drawHolder.setVisible(true);
        drawHolder.setHorizontalAlignment(JLabel.CENTER); /// Center the img


        bgImageHolder.add(drawHolder);                    /// attach the drawingSurface to the background holder so we can have them on top of each other




        animalstoRectangles(animals);
        drawAnimals();

        tooltipTest();

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
                stopThemThreads();
                System.exit(1);
            }
        });


    }

    public void reset() {
        timer.cancel();
        clearAnimals();
        drawHolder.setVisible(true);
        createPreserve();
        resume();
    }




    public  void animalstoRectangles(ArrayList<Animal> animals) {


        for (int i = 0; i < animals.size() ; i++) {


            rectangles.add(new AnimalRectangle(animals.get(i).getxPos(),animals.get(i).getyPos(), 10, 10, animals.get(i))); //// LOCATION X, LOCATION Y, WIDTH, HEIGHT, ID


        }
    }


    public void drawAnimals() {
        g2d = (Graphics2D) drawingSurface.createGraphics();

        g2d.setColor(Color.red);


        super.paintComponent(g2d);


        for (AnimalRectangle animalRectangle : rectangles) {
            g2d.fill(animalRectangle);


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

    public void refresh(ArrayList<Animal> animals) {
        clearAnimals();
        animalstoRectangles(animals);
        drawAnimals();


    }


    /// Ask Harald about this. Only works with frame.pack() or without BorderLayOut
    public void tooltipTest() {
        final AnimalRectangle[] animalRectangle = new AnimalRectangle[1];


        bgImageHolder.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                for (int i = 0; i < rectangles.size(); i++) {
                    animalRectangle[0] = rectangles.get(i);
                    if (animalRectangle[0].contains(e.getPoint())) {

                        bgImageHolder.setToolTipText(animalRectangle[0].report);
                        repaint();
                        break;
                    } else {
                        bgImageHolder.setToolTipText("");
                    }

                }
            }
        });
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



}
