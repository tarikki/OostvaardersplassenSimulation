package mapUtils;

import util.Config;
import util.RangeChecker;

import java.util.HashMap;
import java.util.List;
import java.util.zip.DataFormatException;

/**
 * Created by extradikke on 06/12/14.
 *
 *  a wrapper class for containing the several different plants
 */
public class Plants {
    private List<Plant> plants;

    public List<Plant> getPlants() {
        return plants;
    }

    public void setPlants(List<Plant> plants) {
        this.plants = plants;
    }

    public void verifyRanges() throws IllegalAccessException, NoSuchFieldException, DataFormatException {
        for (Plant plant : plants) {
            RangeChecker.checkforIntRange(plant,"id", 1, (int)Math.pow(2, Config.plantIdBits)-1);
            RangeChecker.checkforIntRange(plant,"maxHealth", 1, (int)Math.pow(2, Config.plantHealthBits)-1);
            RangeChecker.checkforIntRange(plant,"recoveryDays", 1, (int)Math.pow(2, Config.plantHealthBits)-1);
        }


    }







    @Override
    public String toString() {
        return "Plants{" +
                "plants=" + plants +
                '}';
    }


}
