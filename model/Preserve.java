package model;

import mapUtils.MapHandlerAdvanced;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

    private boolean simulationComplete = false;


    private DateTime startDate;
    private DateTime endDate;
    private DateTime currentDate;

    private double latitude;



    private List<Animal> animals = new ArrayList<>();
    private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


    public Preserve(double latitude, int numberOfAnimals, DateTime startDate, DateTime endDate) {
        //TODO still possible to place animals on top of each other, not a big bug, will fix it if time

        this.latitude = latitude;
        this.currentDate = this.startDate = startDate;
        this.endDate = endDate;

        turn = 0;
        Random r = new Random();
        this.numberOfAnimals = numberOfAnimals;
        for (int id = 0; id < numberOfAnimals; id++) {

            int x = r.nextInt(MapHandlerAdvanced.getWidth());
            int y = r.nextInt(MapHandlerAdvanced.getHeight());
            while (!MapHandlerAdvanced.isValidMove(x, y)) {
                x = r.nextInt(MapHandlerAdvanced.getWidth());
                y = r.nextInt(MapHandlerAdvanced.getHeight());
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
        System.out.println("Now turn: "+turn++);
        currentDate = currentDate.plusMinutes(1);
        System.out.println(currentDate.toString());
        if (currentDate.isAfter(endDate)){
            simulationComplete = true;
        }
    }

    public void calculateDaylight(){
        DateTime startOfYear = new DateTime(currentDate.getYear(), 1, 1, 0, 0);
        double helper = Math.asin(.39795*Math.cos(.2163108 + 2*Math.atan(.9671396*Math.tan(.00860*(Days.daysBetween(startOfYear, currentDate).getDays()-186)))));
        double daylight = 24 - (24/Math.PI) *Math.acos(((Math.sin(0.8333 * Math.PI / 180)) + Math.sin(latitude * Math.PI / 180)
                * Math.sin(helper)) / (Math.cos(latitude * Math.PI / 180) * Math.cos(helper)));
        System.out.println("daylight: " + daylight);
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

    public boolean isSimulationComplete() {
        return simulationComplete;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public DateTime getCurrentDate() {
        return currentDate;
    }
}
