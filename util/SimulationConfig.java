package util;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import view.ConfigPane;

/**
 * Created by extradikke on 6-12-14.
 */
public class SimulationConfig {
    private int numberofAnimals;
    private int speedofSimulation;
    private String startingDate;
    private String endingDate;
    public static DateTime startDate;
    public static DateTime endDate;


    public SimulationConfig(ConfigPane configPane) {
        this.numberofAnimals = Integer.parseInt(configPane.getNumAnimals().getValue().toString());
        this.speedofSimulation = Integer.parseInt(configPane.getSpeed().getValue().toString());
        this.startingDate = configPane.getStartTime().getText();
        this.endingDate = configPane.getEndTime().getText();


        ///// GO FROM STRING TO JODA LIKE THIS
        System.out.println(new LocalDate(LocalDate.parse(endingDate, DateVerifier.formatter)));


    }
}
