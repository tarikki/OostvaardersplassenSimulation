package testers;

import mapUtils.MapHandlerAndvanced;
import model.MapHandler;

/**
 * Created by extradikke on 06/12/14.
 */
public class AdvancedMapTester {

    public static void main(String[] args) {

MapHandlerAndvanced mapHandlerAndvanced = new MapHandlerAndvanced();
        int terrain = MapHandlerAndvanced.getTerrain(1000,1000);
        int health = MapHandlerAndvanced.getPlantHealth(1000,1000);
        int id = MapHandlerAndvanced.getPlantId(1000,1000);
        System.out.println(terrain);
        System.out.println(health);
        System.out.println(id);
        MapHandlerAndvanced.decreasePlantHealth(1000,1000,30);
        health = MapHandlerAndvanced.getPlantHealth(1000,1000);
        System.out.println(health);

    }
}
