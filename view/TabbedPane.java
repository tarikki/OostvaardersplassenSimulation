package view;

import javax.swing.*;

/**
 * Created by Pepe on 27.11.2014.
 */
public class TabbedPane extends JTabbedPane {
private JPanel dikkeHolder;
public  MapView mapView;
private StatisticsView statisticsView;
    private MainView.GUI gui;

    public TabbedPane(MainView.GUI gui) {

this.gui = gui;
        /// Holds mapHolder so ToolTips wont be retarded



        /// Create the mapHolder
        mapView = new MapView();
        statisticsView = new StatisticsView(gui);





        this.addTab("Map View", mapView); /// Add the mapHolder to our first tab
        this.addTab("Statistics", statisticsView);


    }
}

