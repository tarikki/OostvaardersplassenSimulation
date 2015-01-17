package view;

import controller.Main;
import org.joda.time.LocalDate;
import util.ButtonUtils;
import util.Config;
import util.DateVerifier;
import util.IOUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Configuration screen to change variables in the preserve.
 * Created by extradikke on 27/11/14.
 */
public class ConfigPane extends JPanel {

    private static final String simulationConfig = "/media/extradikke/UbuntuData/SimulationProjectData/Simulation/src/Simulation.json";
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

    public JComboBox getSpeed() {
        return speed;
    }

    public JFormattedTextField getStartTime() {
        return startTime;
    }

    private JFormattedTextField endTime;
    private JFormattedTextField numAnimals;
    private JComboBox speed;
    private JPanel holder;
    private JLabel configLabels;
    private DateVerifier dateVerifier;



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

        Config.setNumberOfAnimals(Integer.parseInt(getNumAnimals().getValue().toString()));
        Config.setSpeedOfSimulation(Integer.parseInt(getSpeed().getSelectedItem().toString()));
        Config.setStartingDate(DateVerifier.formatter.parseDateTime(getStartTime().getText()));
        Config.setEndingDate(DateVerifier.formatter.parseDateTime(getEndTime().getText()));

//        util.JsonWriter.writeSimulationConfig(IOUtil.configLoader, IOUtil.DEFAULT_CONFIG_PATH); // OUTPUT ---- filepath
        IOUtil.saveConfig();


    }



    private void createButtons() {
        createOpenMapButton();
        ButtonUtils.addButton(configButtons, "Start Simulation", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createConfigFiles();

                /// IF user edited the configs, call a new preserve.
                Main.createPreserve();

                gui.tabbedPane.mapView.init();
                itsame.setVisible(false);
                gui.getMenuBarHandler().setVisible(true);
                gui.add(gui.tabbedPane);
                gui.tabbedPane.setVisible(true);
                gui.pack();


            }
        });


        ButtonUtils.addButton(configButtons, "Exit Application", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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


        numAnimals = new JFormattedTextField(Config.getNumberOfAnimals()); //// Get default num of animals
        numAnimals.setToolTipText("Enter the number of animals");
        numAnimals.setVisible(true);

        String [] speedValues = {String.valueOf(10),String.valueOf(50),String.valueOf(100),String.valueOf(200), String.valueOf(500), String.valueOf(1000) + " (MAX)"};
        speed = new JComboBox(speedValues);     /// Get default speed
        speed.setToolTipText("Select the speed of simulation");
        speed.setBackground(Color.WHITE);
        speed.setVisible(true);

    }


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
