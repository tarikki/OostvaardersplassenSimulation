package testers;

import model.Animal;
import model.MapHandler;
import model.Preserve;

/**
 * Created by extradikke on 22-11-14.
 */
public class DijkstraTester {
    public static void main(String[] args) {


        MapHandler mapHandler = new MapHandler();
        Preserve preserve = new Preserve(1);
        Animal animal = preserve.getAnimals().get(0);
        animal.setxPos(7);
        animal.setyPos(17);
//        animal.scanSurroundings();
        System.out.println(animal.getId() + " " + animal.getxPos() + " " + animal.getyPos());
        animal.makeItDijkstra(MapHandler.ColorCode.GREEN);
        System.out.println(animal);
        animal.checkForWayPoints();
        System.out.println(animal);
        animal.checkForWayPoints();
        System.out.println(animal);
        animal.checkForWayPoints();
        System.out.println(animal);
        animal.checkForWayPoints();        System.out.println(animal);
        animal.checkForWayPoints();        System.out.println(animal);
        animal.checkForWayPoints();


    }

}
