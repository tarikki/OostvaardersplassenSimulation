package view;

import util.ButtonUtils;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by extradikke on 27/11/14.
 */
public class ConfigPane extends JPanel {


    private JLabel title;
    private JPanel configButtons;
    private JPanel itsame;
    private JFormattedTextField startTime;
    private JFormattedTextField endTime;
    private JFormattedTextField numAnimals;
    private JFormattedTextField speed;
    private JPanel holder;
    private JLabel configLabels;

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

    private void createButtons() {
        createOpenMapButton();
        ButtonUtils.addButton(configButtons, "Start Simulation", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                itsame.setVisible(false);
                gui.getMenuBarHandler().setVisible(true);
                gui.add(gui.tabbedPane);
                gui.tabbedPane.setVisible(true);

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


    // TODO format dates with Joda instead?
    private void createTextFields() {


        //// Formatting for textFields
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormatter dateFormatter = new DateFormatter(dateFormat);




        startTime = new JFormattedTextField(dateFormatter);
        startTime.setValue(new Date()); //// Get default starting date here or use the current date?

        startTime.setToolTipText("Enter the start date, eg. 10/12/1990");
        startTime.addPropertyChangeListener("value", (java.beans.PropertyChangeListener) holder);
        startTime.setVisible(true);

        endTime = new JFormattedTextField(dateFormatter);
        endTime.setValue(new Date()); //Get default end date here or use the current date?
        endTime.setToolTipText("Enter the end date, eg. 10/12/1990");
        endTime.addPropertyChangeListener("value", (java.beans.PropertyChangeListener) holder);
        endTime.setVisible(true);

        numAnimals = new JFormattedTextField("1000"); //// Get default num of animals
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


        gbc.insets = new Insets(3,3,15,15);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        holder.add(start,gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        holder.add(startTime,gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 1;
        holder.add(end,gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        holder.add(endTime,gbc);


        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 2;
        holder.add(animuls,gbc);

        gbc.gridx = 3;
        gbc.gridy = 2;
        holder.add(numAnimals,gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 3;
        holder.add(speedster,gbc);

        gbc.gridx = 3;
        gbc.gridy = 3;
        holder.add(speed,gbc);



    }




        private void createTitle()
        {
            title = new JLabel("Configuration menu");
            title.setHorizontalAlignment(JLabel.CENTER);
            title.setVerticalAlignment(JLabel.CENTER);
        }

    }
