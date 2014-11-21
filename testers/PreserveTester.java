package testers;

import model.Animal;
import model.MapHandler;
import model.Preserve;

/**
 * Created by extradikke on 20-11-14.
 */
public class PreserveTester {

    public static void main(String[] args) {
        MapHandler mapHandler = new MapHandler();
        Preserve preserve = new Preserve(10);
        for (int i = 0; i < 10; i++) {
            try {
                preserve.executeTurn();
                for (Animal animal : preserve.getAnimals()) {
                    System.out.println(animal.getId() + " "+animal.getX());
                }
                System.out.println(preserve.getAnimalX(0));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
