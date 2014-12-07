package view;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.joda.time.LocalDate;
import util.ButtonUtils;
import util.DateVerifier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by extradikke on 27/11/14.
 */
public class ConfigPane extends JPanel {

    private final String simulationConfig = "C:/Workspace BU/Simulation-project/src/configuration/Simulation.json";
    private JLabel title;
    private JPanel configButtons;
    private JPanel itsame;
    private JFormattedTextField startTime;

    public JFormattedTextField getEndTime() {
        return endTime;
    }

    public JFormattedTextField getNumAnimals() {
        return numAnimals;
    }

    public JFormattedTextField getSpeed() {
        return speed;
    }

    public JFormattedTextField getStartTime() {
        return startTime;
    }

    private JFormattedTextField endTime;
    private JFormattedTextField numAnimals;
    private JFormattedTextField speed;
    private JPanel holder;
    private JLabel configLabels;
    private DateVerifier dateVerifier;

    //// Implement speed as drop down (combo box?) now we use JFormattedTextField

    private MainView.GUI gui;

    public ConfigPane(MainView.GUI gui) {

        super();

        this.setLayout(new BorderLayout());

        this.gui = gui;
        configButtons = new JPanel();
        configButtons.setLayout(new FlowLayout());
        itsame = this;
        this.setVisible(true);


        createTitle();

        createButtons();

        createTextFields();



        createConfigPanel();

        this.add(configButtons, BorderLayout.SOUTH);
        this.add(title, BorderLayout.NORTH);

        //holder.add("Starting date:", startTime);
        // holder.add("Ending date:", endTime);
        // holder.add ("Number of animals:", numAnimals);
        // holder.add("Speed of simulation:", speed);

        this.add(holder, BorderLayout.CENTER);


    }

    private void createConfigFiles() {

        /// TODO fix variable and string names so they are easier to read

        /// Simulation config

        util.JsonWriter.writeSimulationConfig(new util.SimulationConfig(this), simulationConfig); // OUTPUT ---- filepath


    }

    /// Testing reading from JSON file
    private void readConfigFiles() {
        JsonObject jsonObject = new JsonObject();
        try {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader(simulationConfig));
            jsonObject = jsonElement.getAsJsonObject();


            int numberofAnimals = jsonObject.get("numberofAnimals").getAsInt();
            int speedofSimulation = jsonObject.get("speedofSimulation").getAsInt();
            String startingDate = jsonObject.get("startingDate").getAsString();
            String endingDate = jsonObject.get("endingDate").getAsString();

            System.out.println("Animals: " + numberofAnimals);
            System.out.println("Speed: " + speedofSimulation);
            System.out.println("Starting date " + startingDate);
            System.out.println("Ending date " + endingDate);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void createButtons() {
        createOpenMapButton();
        ButtonUtils.addButton(configButtons, "Start Simulation", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createConfigFiles();
                itsame.setVisible(false);
                gui.getMenuBarHandler().setVisible(true);
                gui.add(gui.tabbedPane);
                gui.tabbedPane.setVisible(true);


            }
        });


        ButtonUtils.addButton(configButtons, "Exit Application", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readConfigFiles();
                System.exit(0);
            }
        });

    }

    private void createOpenMapButton() {
        ButtonUtils.addButton(this, "Select map", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.openNewMap();
            }
        });
    }


    // TODO display popup / change color of textfield if inputs are wrong.
    private void createTextFields() {



        String defaultStart = new LocalDate().toString("dd/MM/yyyy");
        String defaultEnd = new LocalDate().plusDays(1).toString("dd/MM/yyyy");

        startTime = new JFormattedTextField(defaultStart);
        startTime.setInputVerifier(new DateVerifier(dateVerifier.formatter));

        startTime.setToolTipText("Enter the start date, eg. 10/12/1990");
        startTime.setVisible(true);


        /// End time
        endTime = new JFormattedTextField(defaultEnd);
        endTime.setInputVerifier(new DateVerifier(dateVerifier.formatter));

        endTime.setToolTipText("Enter the end date, eg. 10/12/1990");
        endTime.setVisible(true);


        numAnimals = new JFormattedTextField(1000); //// Get default num of animals
        numAnimals.setToolTipText("Enter the number of animals");
        numAnimals.setVisible(true);

        speed = new JFormattedTextField("10");     /// Get default speed
        speed.setToolTipText("Enter the speed of simulation");
        speed.setVisible(true);

    }

    //TODO fix textField size
    private void createConfigPanel() {
        holder = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        GridBagLayout experimentLayout = new GridBagLayout();
        holder.setLayout(experimentLayout);

        JLabel start = new JLabel("Start date:");
        JLabel end = new JLabel("End date:");
        JLabel animuls = new JLabel("# of animals:");
        JLabel speedster = new JLabel("Speed:");


        gbc.insets = new Insets(3, 3, 15, 15);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        holder.add(start, gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        holder.add(startTime, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 1;
        holder.add(end, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        holder.add(endTime, gbc);


        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 2;
        holder.add(animuls, gbc);

        gbc.gridx = 3;
        gbc.gridy = 2;
        holder.add(numAnimals, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 3;
        holder.add(speedster, gbc);

        gbc.gridx = 3;
        gbc.gridy = 3;
        holder.add(speed, gbc);


    }


    private void createTitle() {
        title = new JLabel("Configuration menu");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalAlignment(JLabel.CENTER);
    }

}
