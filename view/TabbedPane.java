package view;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A TabbedPane to hold  map view and statistics view -tabs
 * Created by Pepe on 27.11.2014.
 */
public class TabbedPane extends JTabbedPane {

    public MapView mapView;
    private StatisticsView statisticsView;
    private MainView.GUI gui;

    public TabbedPane(MainView.GUI gui) {

        this.gui = gui;

        mapView = new MapView();
        statisticsView = new StatisticsView(gui);


        this.addTab("Map View", mapView);
        this.addTab("Statistics", statisticsView);



    }

}

