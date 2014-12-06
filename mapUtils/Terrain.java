package mapUtils;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by extradikke on 06/12/14.
 */
public class Terrain {
    private String originalName;
    private String name;
    private int id;
    private int r;
    private int g;
    private int b;
    private String[] plants;
    private float[] coverage;


    public String getOriginalName() {
        return originalName;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public String[] getPlants() {
        return plants;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setR(int r) {
        this.r = r;
    }

    public void setG(int g) {
        this.g = g;
    }

    public void setB(int b) {
        this.b = b;
    }

    public void setPlants(String[] plants) {
        this.plants = plants;
    }

    public boolean compareColor(int red, int green, int blue) {
        boolean sameColor = false;
        if (red == r && green == g && blue == b) {
            sameColor = true;
        }

        return sameColor;
    }

    public int returnRandomPlant() {
        int result = 0;
        if (this.coverage != null) {
            Random random = new Random();
            float rand = random.nextFloat();
            float cumulativeProb = 0;
            int index = 0;
            boolean done = false;
            while (!done) {
                cumulativeProb += coverage[index];
                if (cumulativeProb >= rand) {
                    done = true;
                    result = Integer.valueOf(plants[index]);
                }
                index++;
                if (index > coverage.length - 1) {
                    done = true;
                }
            }
        }


        return result;
    }

    public float[] getCoverage() {
        return coverage;
    }

    public void setCoverage(float[] coverage) {
        this.coverage = coverage;
    }

    @Override
    public String toString() {
        return "Terrain{" +
                "originalName='" + originalName + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", r=" + r +
                ", g=" + g +
                ", b=" + b +
                ", plants=" + Arrays.toString(plants) +
                ", coverage=" + Arrays.toString(coverage) +
                '}';
    }
}