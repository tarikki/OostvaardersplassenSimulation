package mapUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by extradikke on 06/12/14.
 */
public class Plants {
    private List<Plant> plants;

    public List<Plant> getPlants() {
        return plants;
    }

    public void setPlants(List<Plant> plants) {
        this.plants = plants;
    }

    @Override
    public String toString() {
        return "Plants{" +
                "plants=" + plants +
                '}';
    }
}
