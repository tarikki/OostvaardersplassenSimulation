package view;

import controller.Main;
import mapUtils.MapHandlerAdvanced;
import model.Animal;
import model.Preserve;
import org.joda.time.Days;
import util.ButtonUtils;
import util.ColorIcon;
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
 * A class that creates the map view of the preserve.
 * Created by Pepe on 27.11.2014.
 */
public class MapView extends JPanel {


    public BriefStatistics briefStatistics;
    private boolean isFinished;
    private JPanel mapButtons;
    public JLabel drawHolder;
    private BufferedImage backgroundImage;
    private JLabel bgImageHolder;
    private JPanel mapHolder;

    private Graphics2D g2d;
    private BufferedImage drawingSurface;
    private ArrayList<AnimalRectangle> rectangles;


    public MapView() {

        this.setLayout(new BorderLayout());
        isFinished = false;
        rectangles = new ArrayList<AnimalRectangle>();

        mapHolder = new JPanel();


        this.add(mapHolder);

        this.setOpaque(false);

        // Create buttons
        createMapButtons();

        this.add(mapButtons, BorderLayout.SOUTH);


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
                    statsUpdater();


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

    public void init() {
        animalstoRectangles();
        drawAnimals();
        briefStatistics.updateStats();
    }

    public void animalstoRectangles() {
        int scale = Config.getScale(); //// Add this as final somewhere

        for (int i = 0; i < Preserve.getAnimals().size(); i++) {

            int newX = Preserve.getAnimals().get(i).getxPos() / scale;
            int newY = Preserve.getAnimals().get(i).getyPos() / scale;

            rectangles.add(new AnimalRectangle(newX, newY, 2, 2, Preserve.getAnimals().get(i), Preserve.getAnimals().get(i).getName())); //// LOCATION X, LOCATION Y, WIDTH, HEIGHT, ID


        }
    }


    public void drawAnimals() {
        g2d = (Graphics2D) drawingSurface.createGraphics();

        g2d.setColor(Color.red);

        AffineTransform original = g2d.getTransform();

        super.paintComponent(g2d);


        for (AnimalRectangle animalRectangle : rectangles) {

            switch (animalRectangle.getName()) {
                case "Cow":
                    g2d.setColor(Color.red);
                    break;
                case "Deer":
                    g2d.setColor(Color.blue);
                    break;
                case "Horse":
                    g2d.setColor(Color.orange);
                    break;
                default:
            }

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
        endSimulation();


    }



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
    public  void endSimulation()
    {

        if (Preserve.isSimulationComplete() && !isFinished) {
            JOptionPane.showMessageDialog(this.getRootPane(), "The simulation successfully finished", "Simulation complete", JOptionPane.ERROR_MESSAGE);
            isFinished = true;
            Main.timer.cancel();
            Main.timer2.cancel();
            Main.statsTimer.cancel();
        }


    }


    public class BriefStatistics extends JPanel {
        private JLabel currentDate;
        private JLabel currentTime;
        private JLabel numberofAnimals;
        private JLabel timeElapsed;
        private JLabel births;
        private JLabel deaths;
        private JLabel birthDeathRatio;
        private JLabel temperature;
        private JLabel nightOrDay;


        protected JLabel cow;
        protected JLabel deer;
        protected JLabel horse;
        protected JLabel legendTitle;


        final String DEGREE = "\u00b0";


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


            createStats();


            this.add(currentDate);
            this.add(Box.createVerticalStrut(10));
            this.add(currentTime);
            this.add(Box.createVerticalStrut(10));
            this.add(timeElapsed);
            this.add(Box.createVerticalStrut(30));


            this.add(temperature);
            this.add(Box.createVerticalStrut(10));
            this.add(nightOrDay);
            this.add(Box.createVerticalStrut(30));


            this.add(numberofAnimals);
            this.add(Box.createVerticalStrut(10));
            this.add(births);
            this.add(Box.createVerticalStrut(10));
            this.add(deaths);
            this.add(Box.createVerticalStrut(10));
            this.add(birthDeathRatio);

            this.add(Box.createVerticalStrut(30));


            // TODO try to add these to a single JLabel
            this.add(legendTitle);
            this.add(Box.createVerticalStrut(10));
            this.add(cow);
            this.add(deer);
            this.add(horse);


        }

        public void createStats() {
            cow = new JLabel(new ColorIcon(Color.blue));
            deer = new JLabel(new ColorIcon(Color.red));
            horse = new JLabel(new ColorIcon(Color.orange));

            cow.setForeground(Color.blue);
            deer.setForeground(Color.red);
            horse.setForeground(Color.orange);

            numberofAnimals = new JLabel();
            births = new JLabel();
            deaths = new JLabel();
            birthDeathRatio = new JLabel();
            currentDate = new JLabel();
            timeElapsed = new JLabel();
            nightOrDay = new JLabel();
            temperature = new JLabel();
            currentTime = new JLabel();
            legendTitle = new JLabel();
        }

        public void updateStats() {

            legendTitle.setText("Legend");
            cow.setText("Cow");
            deer.setText("Deer");
            horse.setText("Horse");

            numberofAnimals.setText("# of animals:  " + Preserve.getAnimals().size());
            births.setText("# of births:");
            deaths.setText("# of deaths:");
            birthDeathRatio.setText("Births / deaths:");
            currentDate.setText("Current date: " + Preserve.getCurrentDate().toString("dd/MM/yyyy"));
            currentTime.setText("Current time: " + Preserve.getCurrentDate().toLocalTime().toString().substring(0, 5));
            timeElapsed.setText("Time elapsed: " + Days.daysBetween(Preserve.getStartDate().toLocalDate(), Preserve.getCurrentDate().toLocalDate()).getDays() + " days");
            nightOrDay.setText("It is currently: ");
            temperature.setText("Temperature: " + String.valueOf(Preserve.getCurrentTemperature()).substring(0, 3) + " " + DEGREE + "C");
        }



    }


}
