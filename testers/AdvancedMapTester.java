package testers;


import mapUtils.MapHandlerAdvanced;
import util.IOUtil;

/**
 * Created by extradikke on 06/12/14.
 */
public class AdvancedMapTester {

    public static void main(String[] args) {
        IOUtil.loadConfig();
        MapHandlerAdvanced mapHandlerAdvanced = new MapHandlerAdvanced();
        int terrain = mapHandlerAdvanced.getTerrainID(1000, 1000);
        int health = mapHandlerAdvanced.getPlantHealth(1000, 1000);
        int id = mapHandlerAdvanced.getPlantId(1000, 1000);
        System.out.println(terrain);
        System.out.println(health);
        System.out.println(id);
        mapHandlerAdvanced.decreasePlantHealth(1000, 1000, 30);
        health = mapHandlerAdvanced.getPlantHealth(1000, 1000);
        System.out.println(health);

    }
}
