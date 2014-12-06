package mapUtils;

import java.util.Arrays;

/**
 * Created by extradikke on 06/12/14.
 */
public class PlantsCoverage {
    private String[] plants;
    private float[] coverage;

    public String[] getPlants() {
        return plants;
    }

    public void setPlants(String[] plants) {
        this.plants = plants;
    }

    public float[] getCoverage() {
        return coverage;
    }

    public void setCoverage(float[] coverage) {
        this.coverage = coverage;
    }

    @Override
    public String toString() {
        return "Plants{" +
                "plants=" + Arrays.toString(plants) +
                ", coverage=" + Arrays.toString(coverage) +
                '}';
    }
}
