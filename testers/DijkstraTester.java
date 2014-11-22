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
        animal.setX(7);
        animal.setY(17);
//        animal.scanSurroundings();
        System.out.println(animal.getId() + " " + animal.getX() + " " + animal.getY());
        animal.makeItDijkstra(MapHandler.ColorCode.GREEN);

    }

}
