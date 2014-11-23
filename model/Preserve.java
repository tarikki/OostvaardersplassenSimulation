package model;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by extradikke on 20-11-14.
 */
public class Preserve {
    private int numberOfAnimals;
    private int turn;


    private List<Animal> animals = new ArrayList<Animal>();
    private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


    public Preserve(int numberOfAnimals) {
        //TODO still possible to place animals on top of each other, not a big bug, will fix it if time
        turn = 0;
        Random r = new Random();
        this.numberOfAnimals = numberOfAnimals;
        for (int id = 0; id < numberOfAnimals; id++) {
            int x = r.nextInt(MapHandler.getWidth());
            int y = r.nextInt(MapHandler.getHeight());
            while (!MapHandler.isValidMove(x, y)) {
                x = r.nextInt(MapHandler.getWidth());
                y = r.nextInt(MapHandler.getHeight());
            }
            animals.add(new Animal(id, x, y));
//           System.out.println("Animal " + id + " x, y: " + x + " " + y);
        }
    }


    public int getNumberOfAnimals() {
        return numberOfAnimals;
    }

    public void executeTurn() throws InterruptedException {
        List<Callable<Object>> todo = new ArrayList<Callable<Object>>(animals.size());
        // TODO shuffle the animals first so they don't always move in order of creation
        for (Animal animal : animals) {
            todo.add(Executors.callable(animal));
        }
        List<Future<Object>> dikke = executor.invokeAll(todo);
        System.out.println(turn++);
    }


    public int getAnimalX(int id) {
        return animals.get(id).getxPos();
    }

    public int getAnimalY(int id) {
        return animals.get(id).getyPos();
    }

    public void stopThreads() {
        executor.shutdownNow();
    }

    public List<Animal> getAnimals() {
        return animals;
    }
}
