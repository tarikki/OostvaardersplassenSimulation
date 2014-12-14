package model;

import mapUtils.MapHandlerAdvanced;
import mapUtils.MonthlyWeather;
import org.joda.time.*;
import util.Config;


import java.util.ArrayList;
import java.util.HashMap;
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
    private static int numberOfAnimals;
    private static int turn;

    private static boolean simulationComplete = false;


    private static DateTime startDate;
    private static DateTime endDate;
    private static DateTime currentDate;
    private static boolean isNight;
    private static DateTimeZone timeZone = DateTimeZone.forID(Config.getDateTimeZone());
    private static Interval currentDaySpan;
    private static Interval currentHourSpan;
    private static Interval currentTempMonthSpan;
    private static DateTime sunrise;
    private static DateTime sunset;
    private static double maxLengthOfDay;
    private static double currentDayLength;
    private static double currentTemperature;

    private static int startMaxTemp;
    private static int endMaxTemp;
    private static int startMinTemp;
    private static int endMinTemp;

    private static double todayMaxTemp;
    private static double todayMinTemp;

    private static double currentDailyMaxTempIncrement;
    private static double currentDailyMinTempIncrement;
    private static double currentHourlyMaxTempIncrement;
    private static double currentHourlyMinTempIncrement;
    private static HashMap<Integer, MonthlyWeather> weatherHashMap = new HashMap<>();


    private static double latitude;


    private static ArrayList<Animal> animals = new ArrayList<>();
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void setupPreserve(double latitudeInput, int numberOfAnimalsInput, DateTime startDateInput,
                                     DateTime endDateInput) {
        //TODO still possible to place animals on top of each other, not a big bug, will fix it if time

        currentDate = new DateTime(2014, 6, 21, 0, 0, 0);
        maxLengthOfDay = calculateDaylight();
        weatherHashMap = MapHandlerAdvanced.getWeatherHashMap();


        latitude = latitudeInput;
        currentDate = startDate = startDateInput;
        endDate = endDateInput;
        currentDaySpan = new Interval(currentDate, currentDate.plusDays(1).withTimeAtStartOfDay());
        currentHourSpan = new Interval(currentDate, currentDate.plusHours(1));
        currentDayLength = calculateDaylight();
        setSunriseAndSunset(currentDayLength);
        if (currentDate.dayOfMonth().get() > 15) {
            DateTime start = currentDate.dayOfMonth().setCopy(15);
            currentTempMonthSpan = new Interval(start, start.monthOfYear().addToCopy(1));
        } else {
            DateTime start = currentDate.monthOfYear().addToCopy(-1).dayOfMonth().setCopy(15);
            currentTempMonthSpan = new Interval(start, start.monthOfYear().addToCopy(1));

        }
        System.out.println("tempmonth " + currentTempMonthSpan.toString());
        setupTempIncrements();
        calculateDailyTemperatures();


        turn = 0;
        Random r = new Random();
        numberOfAnimals = numberOfAnimalsInput;
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

    public static int getNumberOfAnimals() {
        return numberOfAnimals;
    }

    public static void executeTurn() throws InterruptedException {
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

        if (isNewHour()) {
            setupNewHour();
        }

        if (isNewDay()) {

            System.out.println("Bitchesss!");
            setupNewDay();
        }

        if (isNewTempMonth()) {
            setupNewTempMonth();
        }
    }

    public static boolean checkDeath(Animal animal) {
        if (animal.isDead()) {
            animals.remove(animal.getId());
            return false;
        } else return true;
    }

    public static boolean isNewDay() {
        boolean newDay = false;
        if (!currentDaySpan.contains(currentDate)) {
            newDay = true;
        }
        return newDay;
    }

    public static boolean isNewHour() {
        boolean newHour = false;
        if (!currentHourSpan.contains(currentDate)) {
            newHour = true;
        }
        return newHour;

    }

    public static boolean isNewTempMonth() {
        boolean newTempMonth = false;
        if (!currentTempMonthSpan.contains(currentDate)) {
            newTempMonth = true;
        }
        return newTempMonth;
    }


    public static void setupNewHour() {
        currentHourSpan = new Interval(currentDate, currentDate.hourOfDay().addToCopy(1));
        System.out.println("Mega Bithces!");
    }

    public static void setupNewTempMonth() {
        currentTempMonthSpan = new Interval(currentDate, currentDate.monthOfYear().addToCopy(1));
    }

    public static void setupTempIncrements() {
        startMaxTemp = MapHandlerAdvanced.getWeatherHashMap().get(currentTempMonthSpan.getStart().getMonthOfYear()).getAvMaxTemp();
        endMaxTemp = MapHandlerAdvanced.getWeatherHashMap().get(currentTempMonthSpan.getEnd().getMonthOfYear()).getAvMaxTemp();
        startMinTemp = MapHandlerAdvanced.getWeatherHashMap().get(currentTempMonthSpan.getStart().getMonthOfYear()).getAvMinTemp();
        endMinTemp = MapHandlerAdvanced.getWeatherHashMap().get(currentTempMonthSpan.getEnd().getMonthOfYear()).getAvMinTemp();
        todayMaxTemp = startMaxTemp;
        todayMinTemp = startMinTemp;
        System.out.println(currentTempMonthSpan.toDuration().getStandardDays());
        currentDailyMaxTempIncrement = (endMaxTemp - startMaxTemp) / ((double) currentTempMonthSpan.toDuration().getStandardDays());
        currentDailyMinTempIncrement = (endMinTemp - startMinTemp) / ((double) currentTempMonthSpan.toDuration().getStandardDays());
        System.out.println(startMinTemp - endMinTemp);
        System.out.println("startTemp: " + startMaxTemp + ", endTemp: " + endMaxTemp + ", increment: " + currentDailyMaxTempIncrement);
        System.out.println("startTemp: " + startMinTemp + ", endTemp: " + endMinTemp + ", increment: " + currentDailyMinTempIncrement);

    }

    public static void calculateDailyTemperatures() {
        todayMaxTemp = startMaxTemp + currentDailyMaxTempIncrement * (new Duration(currentTempMonthSpan.getStart(), currentDate)).getStandardDays();
        todayMinTemp = startMinTemp + currentDailyMinTempIncrement * (new Duration(currentTempMonthSpan.getStart(), currentDate)).getStandardDays();
        System.out.println("max today: " + todayMaxTemp);
        System.out.println("min today: " + todayMinTemp);
    }

    public static void calculateHourlyTemperature() {

    }

    public static void setupNewDay() {
        currentDaySpan = new Interval(currentDate, currentDate.dayOfMonth().addToCopy(1).withTimeAtStartOfDay());
        currentDayLength = calculateDaylight();
        calculateDailyTemperatures();
    }

    public static void setSunriseAndSunset(double lengthOfDay) {
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

    public static double calculateDaylight() {
        DateTime startOfYear = new DateTime(currentDate.getYear(), 1, 1, 0, 0);
        double helper = Math.asin(.39795 * Math.cos(.2163108 + 2 * Math.atan(.9671396 * Math.tan(.00860 *
                (Days.daysBetween(startOfYear, currentDate).getDays() - 186)))));
        double daylight = 24 - (24 / Math.PI) * Math.acos(((Math.sin(0.8333 * Math.PI / 180))
                + Math.sin(latitude * Math.PI / 180)
                * Math.sin(helper)) / (Math.cos(latitude * Math.PI / 180) * Math.cos(helper)));
        System.out.println("daylight: " + daylight);
        return daylight;
    }


    public static int getAnimalX(int id) {
        return animals.get(id).getxPos();
    }

    public static int getAnimalY(int id) {
        return animals.get(id).getyPos();
    }

    public static void stopThreads() {
        executor.shutdownNow();
    }

    public static ArrayList<Animal> getAnimals() {
        return animals;
    }

    public static boolean isSimulationComplete() {
        return simulationComplete;
    }

    public static DateTime getStartDate() {
        return startDate;
    }

    public static DateTime getEndDate() {
        return endDate;
    }

    public static DateTime getCurrentDate() {
        return currentDate;
    }

    public static double getMaxLengthOfDay() {
        return maxLengthOfDay;
    }

    public static void setMaxLengthOfDay(double maxLengthOfDay) {
        Preserve.maxLengthOfDay = maxLengthOfDay;
    }

    public static double getCurrentTemperature() {
        return currentTemperature;
    }

    public static void setCurrentTemperature(double currentTemperature) {
        Preserve.currentTemperature = currentTemperature;
    }

    public static double getCurrentDayLength() {
        return currentDayLength;
    }

    public static void setCurrentDayLength(double currentDayLength) {
        Preserve.currentDayLength = currentDayLength;
    }
}
