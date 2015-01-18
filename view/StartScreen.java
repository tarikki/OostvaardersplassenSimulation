package view;

import controller.Main;
import util.ButtonUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * First screen to display when the program is run. Contains options to start the simulation, exit the program or to configure the settings of the preserve
 * Created by Pepe on 27/11/14.
 */
public class StartScreen extends JPanel {
    private JLabel title;
    private JPanel startButtons;
    private JPanel itsame;
    private JPanel config;
    private MainView.GUI gui;
    private ConfigPane configPane;



    public StartScreen(MainView.GUI gui){
        super();
        this.gui = gui;
        gui.getMenuBarHandler().setVisible(false);
        startButtons = new JPanel();
        startButtons.setLayout(new FlowLayout());
        itsame = this;
        this.setVisible(true);
        config = new JPanel();
        createButtons();
        this.add(startButtons);
        gui.tabbedPane.setVisible(false);


    }

    //TODO add title + logo of Staatsbosbeheer. Maybe some better layout?

    private void createButtons(){
        ButtonUtils.addButton(startButtons, "Start Simulation", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.createPreserve();
                gui.tabbedPane.mapView.init();
                itsame.setVisible(false);
                gui.getMenuBarHandler().setVisible(true);
                gui.add(gui.tabbedPane);
                gui.tabbedPane.setVisible(true);
                gui.pack();



            }
        });

        ButtonUtils.addButton(startButtons, "Open Config", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

               configPane = new ConfigPane(gui);


                itsame.setVisible(false);
                gui.add(configPane);
                configPane.setVisible(true);
                gui.pack();

            }
        });

        ButtonUtils.addButton(startButtons, "Exit Application", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }
}
