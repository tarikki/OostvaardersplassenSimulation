package controller;

import model.MapHandler;
import model.Preserve;
import util.SimulationConfig;
import view.ConfigPane;
import view.MainView;
import view.MapView;
import view.StartScreen;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Veera on 27.11.2014.
 */
public class Main {
    public static Timer timer;
    private static Preserve preserve;
    private static MapHandler mapHandler;
    private static MapView mapView;
    private static MainView mainView;
    private static StartScreen startScreen;


    /// Run new GUI.. TODO move these to static block?
    public static void main(String[] args) {


        mapHandler = new MapHandler(); /// Create map

        createPreserve();


        mainView = new MainView((ArrayList<model.Animal>) preserve.getAnimals());



    }

    /// Just initializing
    public static void createPreserve() {

        ConfigPane.readConfigFiles(); //// Read info from config

        /// Create preserve according to configs
        preserve = new Preserve(52.3667, 100, SimulationConfig.startDate, SimulationConfig.endDate); /// Create preserve with X amount of animals
        System.out.println("Start on: " + SimulationConfig.startDate);
        System.out.println("End date on: " + SimulationConfig.endDate);
    }

    public static void stopMovement() {
        timer.cancel();

    }



    public static void stopThemThreads() {
        preserve.stopThreads();
    }

    public static void resume() {
        moveTester();
    }

    /// Acts as our start method
    public static void moveTester() {
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    preserve.executeTurn();
                    mainView.gui.tabbedPane.mapView.refresh((ArrayList<model.Animal>) preserve.getAnimals());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 100, 500);

    }
}
