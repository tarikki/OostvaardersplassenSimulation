package testers;


import mapUtils.MapHandlerAdvanced;
import model.Preserve;
import util.Config;
import util.IOUtil;

/**
 * Created by extradikke on 06/12/14.
 */
public class AdvancedMapTester {

    public static void main(String[] args) {
        IOUtil.loadConfig();
        MapHandlerAdvanced mapHandlerAdvanced = new MapHandlerAdvanced();
        Preserve.setupPreserve(Config.getLatitude(), Config.getNumberOfAnimals(), Config.getStartingDate(), Config.getEndingDate()); /// Create preserve with X amount of animals

        int terrain = MapHandlerAdvanced.getTerrainID(1000, 1000);
        int health = MapHandlerAdvanced.getPlantHealth(1000, 1000);
        int id = MapHandlerAdvanced.getPlantId(1000, 1000);
        System.out.println(terrain);
        System.out.println(health);
        System.out.println(id);
        MapHandlerAdvanced.decreasePlantHealth(1000, 1000, 30);
        health = MapHandlerAdvanced.getPlantHealth(1000, 1000);
        System.out.println(health);
        MapHandlerAdvanced.increasePlantHealth(1000,1000,26);
        health = MapHandlerAdvanced.getPlantHealth(1000, 1000);
        System.out.println(health);

    }
}
