package model;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by extradikke on 20-11-14.
 */
public class Preserve {
    private int numberOfAnimals;



    private Animal[] animals;
    private ExecutorService executor = Executors.newFixedThreadPool(10);


    public Preserve(int numberOfAnimals) {
        //TODO still possible to place animals on top of each other, not a big bug, will fix it if time
        Random r = new Random();
        this.numberOfAnimals = numberOfAnimals;
        animals = new Animal[numberOfAnimals];
        for (int id = 0; id < numberOfAnimals; id++) {
            int x = r.nextInt(MapHandler.getWidth());
            int y = r.nextInt(MapHandler.getHeight());
            while (!MapHandler.putAnimal(x, y, id)) {
                x = r.nextInt(MapHandler.getWidth());
                y = r.nextInt(MapHandler.getHeight());
            }
            animals[id] = new Animal(id, x, y);
//            System.out.println("Animal " + id + " x, y: " + x + " " + y);
        }
    }

    public int getNumberOfAnimals() {
        return numberOfAnimals;
    }


    public int getAnimalX(int id){
        return animals[id].getX();
    }

    public int getAnimalY(int id){
        return animals[id].getY();
    }

    public Animal[] getAnimals() {
        return animals;
    }
}
