package view;

import model.Animal;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by Pepe on 27.11.2014.
 */
public class TabbedPane extends JTabbedPane {
private JPanel dikkeHolder;
public  MapView mapView;
private StatisticsView statisticsView;

    public TabbedPane(ArrayList<Animal> animals) {


        /// Holds mapHolder so ToolTips wont be retarded



        /// Create the mapHolder
        mapView = new MapView(animals);
        statisticsView = new StatisticsView();





        this.addTab("Map View", mapView); /// Add the mapHolder to our first tab
        this.addTab("Statistics", statisticsView);


    }
}

