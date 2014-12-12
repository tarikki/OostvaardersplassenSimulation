package view;

import mapUtils.MapHandlerAdvanced;
import model.Animal;
import model.Preserve;
import util.ButtonUtils;
import util.Config;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static controller.Main.*;

/**
 * Created by Pepe on 27.11.2014.
 */
public class MapView extends JPanel {

    public Legend legend;
    private JPanel mapButtons;
    public JLabel drawHolder;
    private BufferedImage backgroundImage;
    private JLabel bgImageHolder;
    private JPanel mapHolder;

    private Graphics2D g2d;
    private BufferedImage drawingSurface;
    private ArrayList<AnimalRectangle> rectangles;
    private BriefStatistics briefStatistics;

    public MapView() {
        this.setLayout(new BorderLayout());
        rectangles = new ArrayList<AnimalRectangle>();

        mapHolder = new JPanel();

        /// Shitty tooltip finally works with Borderlayout west! However, keep map centered for now
        this.add(mapHolder);

        this.setOpaque(false);

        // Create buttons
        createMapButtons();

        this.add(mapButtons, BorderLayout.SOUTH);

        /// Add legend
        legend = new Legend();
        this.add(legend, BorderLayout.NORTH);

        /// Add brief statistics
       briefStatistics = new BriefStatistics(Preserve.getAnimals());
        this.add(briefStatistics, BorderLayout.EAST);

        // Create the drawing surface
        drawingSurface = new BufferedImage(MapHandlerAdvanced.getDisplayableImageWidth(), MapHandlerAdvanced.getDisplayableImageHeight(), BufferedImage.TYPE_INT_ARGB);

        /// Set map as the background
        backgroundImage = MapHandlerAdvanced.getDisplayableImage();
        bgImageHolder = new JLabel(new ImageIcon(backgroundImage));
        ToolTipManager.sharedInstance().registerComponent(bgImageHolder);
        bgImageHolder.setLayout(new BorderLayout()); /// Needs to be here so we can position drawHolder and drawingSurface


        mapHolder.add(bgImageHolder); /// Add the background (map) holder to the JPanel


        /// Holder for drawingSurface
        drawHolder = new JLabel(new ImageIcon(drawingSurface));
        drawHolder.setVisible(true);
        drawHolder.setHorizontalAlignment(JLabel.CENTER); /// Center the img


        bgImageHolder.add(drawHolder);                    /// attach the drawingSurface to the background holder so we can have them on top of each other


        animalstoRectangles();
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

    public void init(){
        animalstoRectangles();
        drawAnimals();
    }

    public void animalstoRectangles() {
        int scale = Config.getScale(); //// Add this as final somewhere

        for (int i = 0; i < Preserve.getAnimals().size(); i++) {

            int newX = Preserve.getAnimals().get(i).getxPos() / scale;
            int newY = Preserve.getAnimals().get(i).getyPos() / scale;

            rectangles.add(new AnimalRectangle(newX, newY, 5, 5, Preserve.getAnimals().get(i))); //// LOCATION X, LOCATION Y, WIDTH, HEIGHT, ID



        }
    }


    public void drawAnimals() {
        g2d = (Graphics2D) drawingSurface.createGraphics();

        g2d.setColor(Color.red);

        AffineTransform original = g2d.getTransform();

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

    public void refresh() {
        clearAnimals();
        animalstoRectangles();
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

    class BriefStatistics extends JPanel {
        private JLabel currentDate;
        private JLabel numberofAnimals;
        private JLabel timeElapsed;
        private JLabel births;
        private JLabel deaths;
        private JLabel birthDeathRatio;

        public BriefStatistics(ArrayList<Animal> animals) {
            super();

            /// Set size like this
            //Dimension d = new Dimension (200, 200);
            //this.setPreferredSize(d);

            this.setVisible(true);
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            // Border for stats panel here
            TitledBorder border = BorderFactory.createTitledBorder("Brief statistics");
            border.setBorder(BorderFactory.createLineBorder(Color.red)); /// Change color of border here
            this.setBorder(border);



            /*
            statsTitle = new JLabel("Brief statistics");
            Font font = new Font("Serif", Font.BOLD, 24);
            Map attributes = font.getAttributes();
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            statsTitle.setFont(font.deriveFont(attributes));
*/




            numberofAnimals = new JLabel("# of animals:  " + animals.size());
            births = new JLabel("# of births:");
            deaths = new JLabel("# of deaths:");
            birthDeathRatio = new JLabel("Births / deaths:");
            currentDate = new JLabel("Current date: ");
            timeElapsed = new JLabel("Time elapsed:");


            this.add(Box.createVerticalStrut(10));
            this.add(numberofAnimals);
            this.add(Box.createVerticalStrut(10));
            this.add(births);
            this.add(Box.createVerticalStrut(10));
            this.add(deaths);
            this.add(Box.createVerticalStrut(10));
            this.add(birthDeathRatio);
            this.add(Box.createVerticalStrut(10));
            this.add(currentDate);
            this.add(Box.createVerticalStrut(10));
            this.add(timeElapsed);



        }
    }


}
