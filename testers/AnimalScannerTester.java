package testers;

import model.Animal;
import model.MapHandler;
import model.Preserve;
import org.joda.time.DateTime;

/**
 * Created by extradikke on 22-11-14.
 */
public class AnimalScannerTester {
    public static void main(String[] args) {
        MapHandler mapHandler = new MapHandler();
        Preserve preserve = new Preserve(52.3667, 1, new DateTime(), (new DateTime()).plusDays(3));
        Animal animal = preserve.getAnimals().get(0);
        animal.scanSurroundings();
        System.out.println(animal.getId() + " "+animal.getxPos() + " " + animal.getyPos());
    }
}
