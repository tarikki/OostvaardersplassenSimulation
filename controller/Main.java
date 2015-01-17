package controller;

import mapUtils.MapHandlerAdvanced;
import model.Preserve;
import util.Config;
import util.IOUtil;
import view.MainView;
import view.MapView;
import view.StartScreen;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Veera on 27.11.2014.
 */
public class Main {
    public static Timer timer;
    public static Timer timer2;
    public static Timer statsTimer;
    private static Preserve preserve;
    private static MapHandlerAdvanced mapHandler;
    private static MapView mapView;
    private static MainView mainView;
    private static StartScreen startScreen;



    /// Run new GUI.. TODO move these to static block?
    public static void main(String[] args) {

        IOUtil.loadConfig();
        mapHandler = new MapHandlerAdvanced(); /// Create map




        mainView = new MainView();


    }

    /// Just initializing
    public static void createPreserve() {


        Preserve.setupPreserve(Config.getLatitude(), Config.getStartingDate(), Config.getEndingDate()); /// Create preserve with X amount of animals

    }

    public static void stopMovement() {
        timer.cancel();
        statsTimer.cancel();

    }


    public static void stopThemThreads() {
        Preserve.stopThreads();
    }

    public static void resume() {
        moveTester();
        statsUpdater();
    }

    /// Acts as our start method
    public static void moveTester() {
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Preserve.executeTurn();
                    if (Preserve.getSimulationComplete()){
                        timer.cancel();
                        timer.purge();
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000/Config.getSpeedOfSimulation());

        timer2 = new Timer();

        timer2.schedule(new TimerTask() {
            @Override
            public void run() {


                    mainView.gui.tabbedPane.mapView.refresh();


            }
        }, 0, 50);

    }


    // Running every 10 seconds now
    public static void statsUpdater() {
        statsTimer = new Timer();
        //MapView.BriefStatistics briefStatistics = new MapView().new BriefStatistics();

        statsTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                mainView.gui.tabbedPane.mapView.briefStatistics.updateStats();

            }
        }, 0, 1000);

    }
}
