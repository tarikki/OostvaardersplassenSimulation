package util;

import view.ConfigPane;

/**
 * Created by extradikke on 6-12-14.
 */
public class SimulationConfig {
    private int numberofAnimals;
    private int speedofSimulation;
    private Object startingDate;
    private Object endingDate;


    public SimulationConfig(ConfigPane configPane)
    {
        this.numberofAnimals = Integer.parseInt(configPane.getNumAnimals().getValue().toString());
        this.speedofSimulation = Integer.parseInt(configPane.getSpeed().getValue().toString());
        this.startingDate = configPane.getStartTime().getText();
        this.endingDate = configPane.getEndTime().getText();
    }
}
