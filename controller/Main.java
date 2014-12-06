package controller;

import model.MapHandler;
import model.Preserve;
import org.joda.time.DateTime;
import view.AnimalRectangle;
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

        preserve = new Preserve(52.3667, 100, new DateTime(), (new DateTime()).plusDays(3)); /// Create preserve with X amount of animals
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
        }, 50, 50);

    }
}
