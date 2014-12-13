package mapUtils;

import com.sun.org.apache.bcel.internal.generic.NEW;
import util.Config;
import util.RangeChecker;

import java.util.List;
import java.util.zip.DataFormatException;

/**
 * Created by extradikke on 06/12/14.
 */

public class Terrains{

    private List<Terrain> terrains;

    public List<Terrain> getTerrains() {
        return terrains;
    }

    public void setTerrains(List<Terrain> terrains) {
        this.terrains = terrains;
    }

    public void verifyRanges() throws IllegalAccessException, NoSuchFieldException, DataFormatException {
        for (Terrain terrain : terrains) {
            RangeChecker.checkforIntRange(terrain,"id",0,(int)Math.pow(2, Config.plantIdBits)-1);
            RangeChecker.checkforIntRange(terrain,"r",0,255);
            RangeChecker.checkforIntRange(terrain,"g",0,255);
            RangeChecker.checkforIntRange(terrain,"b",0,255);




        }
    }

    @Override
    public String toString() {
        return "Terrains{" +
                "terrains=" + terrains +
                '}';
    }
}



