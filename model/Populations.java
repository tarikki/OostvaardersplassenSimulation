package model;

import util.RangeChecker;

import java.util.List;
import java.util.zip.DataFormatException;

/**
 * Created by extradikke on 30.12.14.
 */
public class Populations {
    private List<Population>populations;

    public List<Population> getPopulations() {
        return populations;
    }

    public void setPopulations(List<Population> populations) {
        this.populations = populations;
    }

    public void verifyRanges() throws IllegalAccessException, NoSuchFieldException, DataFormatException {
        for (Population population : populations) {
            RangeChecker.checkforIntRange(population, "id", 0, 1000000);
        }
    }

    @Override
    public String toString() {
        String list = "";
        for (Population population : populations) {
            list += population + "\n";

        }

        return "Populations{" +
                "populations=" + list +
                '}';
    }
}
