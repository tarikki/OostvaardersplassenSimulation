package testers;

import mapUtils.MapHandlerAndvanced;

/**
 * Created by extradikke on 06/12/14.
 */
public class AdvancedMapTester {

    public static void main(String[] args) {
        MapHandlerAndvanced mapHandlerAndvanced = new MapHandlerAndvanced();
        mapHandlerAndvanced.starters();
        int terrain = mapHandlerAndvanced.getTerrain(0,0);
        int health = mapHandlerAndvanced.getPlantHealth(0, 0);
        int id = mapHandlerAndvanced.getPlantId(0,0);
        System.out.println(terrain);
        System.out.println(health);
        System.out.println(id);
        mapHandlerAndvanced.decreasePlantHealth(0,0,30);
        health = mapHandlerAndvanced.getPlantHealth(0, 0);
        System.out.println(health);

    }
}
