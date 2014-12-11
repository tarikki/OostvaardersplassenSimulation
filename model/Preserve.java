package model;

import mapUtils.MapHandlerAdvanced;
import org.joda.time.*;
import util.Config;


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
    private boolean isNight;
    private DateTimeZone timeZone = DateTimeZone.forID(Config.getDateTimeZone());
    private Interval currentDaySpan;
    private DateTime sunrise;
    private DateTime sunset;


    private double latitude;


    private List<Animal> animals = new ArrayList<>();
    private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


    public Preserve(double latitude, int numberOfAnimals, DateTime startDate, DateTime endDate) {
        //TODO still possible to place animals on top of each other, not a big bug, will fix it if time

        this.latitude = latitude;
        this.currentDate = this.startDate = startDate;
        this.endDate = endDate;
        this.currentDaySpan = new Interval(currentDate, currentDate.plusDays(1).withTimeAtStartOfDay());
        setSunriseAndSunset(calculateDaylight());


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
            if (!checkDeath(animal)) {
                todo.add(Executors.callable(animal));
            }
        }
        System.out.println(animals.size());
        List<Future<Object>> dikke = executor.invokeAll(todo);
        System.out.println("Now turn: " + turn++);
        currentDate = currentDate.plusMinutes(1);
//        System.out.println(currentDate.toString());
        if (currentDate.isAfter(endDate)) {
            simulationComplete = true;
        }

        if (isNewDay()) {
            System.out.println("Bitchesss!");

        }
    }

    public boolean checkDeath(Animal animal) {
        if (animal.isDead()) {
            animals.remove(animal.getId());
            return false;
        } else return true;
    }

    public boolean isNewDay() {
        boolean newDay = false;
        if (!currentDaySpan.contains(currentDate)) {
            currentDaySpan = new Interval(currentDate, currentDate.plusDays(1).withTimeAtStartOfDay());
            newDay = true;
        }
        return newDay;
    }

    public void setSunriseAndSunset(double lengthOfDay) {
        System.out.println(lengthOfDay);
        long halfLightHoursDuration = (Hours.hours((int) Math.floor(lengthOfDay)).toStandardDuration().getMillis() / 2);
        long halfLightMinutesDuration = (Minutes.minutes((int) ((lengthOfDay - Math.floor(lengthOfDay)) * 60)).toStandardDuration().getMillis() / 2);
        System.out.println((lengthOfDay - Math.floor(lengthOfDay)));
//        System.out.println(halfLightHoursDuration.toStandardHours());
//        System.out.println(halfLightMinutesDuration.toStandardMinutes());
        Duration halfLightduration = new Duration(halfLightHoursDuration + halfLightMinutesDuration);
        sunrise = currentDate.withHourOfDay(12).minus(halfLightduration);
        sunset = currentDate.withHourOfDay(12).plus(halfLightduration);
        System.out.println(sunrise);
        System.out.println(sunset);

    }

    public double calculateDaylight() {
        DateTime startOfYear = new DateTime(currentDate.getYear(), 1, 1, 0, 0);
        double helper = Math.asin(.39795 * Math.cos(.2163108 + 2 * Math.atan(.9671396 * Math.tan(.00860 *
                (Days.daysBetween(startOfYear, currentDate).getDays() - 186)))));
        double daylight = 24 - (24 / Math.PI) * Math.acos(((Math.sin(0.8333 * Math.PI / 180))
                + Math.sin(latitude * Math.PI / 180)
                * Math.sin(helper)) / (Math.cos(latitude * Math.PI / 180) * Math.cos(helper)));
        System.out.println("daylight: " + daylight);
        return daylight;
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
