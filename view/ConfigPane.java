package view;

import util.ButtonUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    //// Implement speed as drop down (combo box?) now we use JFormattedTextField

    private MainView.GUI gui;

    public ConfigPane(MainView.GUI gui) {
        super();
        this.gui = gui;
        configButtons = new JPanel();
        configButtons.setLayout(new FlowLayout());
        itsame = this;
        this.setVisible(true);

        createTitle();
        createButtons();
        createTextFields();
        this.add(configButtons);



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

    private void createTextFields() {

        startTime = new JFormattedTextField("dd/mm/yy"); //// what does this constructor take?
        startTime.setVisible(true);
        endTime = new JFormattedTextField("dd/mm/yy");
        endTime.setVisible(true);
        numAnimals = new JFormattedTextField("PLACEHOLDER"); //// add num of animals from preserve here
        numAnimals.setVisible(true);
        speed = new JFormattedTextField("PLACEHOLDER");     /// add current speed from preserve here
        speed.setVisible(true);

    }

    private void createTitle()
    {
        title = new JLabel("Configuration menu");
    }
}
