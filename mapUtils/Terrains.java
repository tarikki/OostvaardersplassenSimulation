package mapUtils;

import java.util.List;

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

    @Override
    public String toString() {
        return "Terrains{" +
                "terrains=" + terrains +
                '}';
    }
}



