package model;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import mapUtils.MapHandlerAdvanced;
import mapUtils.MonthlyWeather;
import org.joda.time.*;
import util.ColorIcon;
import util.Config;
import util.IOUtil;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.*;

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
    private static boolean night;
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
    private static Populations initialPopulations;
    private static Set<DateTime> birthDays = new HashSet<>();

    private static int deaths = 0;

    private static double latitude;


    private static ArrayList<Animal> animals = new ArrayList<>();
    private static int currentMaxId = 0;
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    public static CountDownLatch latch;

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
        checkForNight();
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
        calculateHourlyTemperature();


        turn = 0;
//        Random r = new Random();
        numberOfAnimals = numberOfAnimalsInput;
        loadInitialPopulations();
        loadAnimals();

    }

    public static int getNumberOfAnimals() {
        return numberOfAnimals;
    }

    public static void executeTurn() throws InterruptedException {

        checkForNight();
        List<Callable<Object>> todo = new ArrayList<Callable<Object>>(animals.size());
        // TODO shuffle the animals first so they don't always move in order of creation
        // TODO using the checkdeath results in concurrentmodification exception, fix it
        for (Animal animal : animals) {
//            if (!checkDeath(animal)) {
            todo.add(Executors.callable(animal));
//            }
        }
//        System.out.println(animals.size());
        List<Future<Object>> dikke = executor.invokeAll(todo);
        todo.clear();
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
            MapHandlerAdvanced.growAllPlants();
            System.out.println("mycket growth");
            setupNewDay();
        }

        if (isNewTempMonth()) {
            setupNewTempMonth();
        }
    }

    public static void executeTurn2() throws InterruptedException {

        checkForNight();
//        List<Callable<Object>> todo = new ArrayList<Callable<Object>>(animals.size());
//        // TODO shuffle the animals first so they don't always move in order of creation
//        // TODO using the checkdeath results in concurrentmodification exception, fix it
//        for (Animal animal : animals) {
////            if (!checkDeath(animal)) {
//            todo.add(Executors.callable(animal));
////            }
//        }
////        System.out.println(animals.size());
//        List<Future<Object>> dikke = executor.invokeAll(todo);
//todo.clear();
        latch = new CountDownLatch(animals.size());
        for (Animal animal : animals) {
            executor.execute(animal);
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
            MapHandlerAdvanced.growAllPlants();
            System.out.println("mycket growth");
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
        calculateHourlyTemperature();

    }

    public static void setupNewTempMonth() {
        currentTempMonthSpan = new Interval(currentDate, currentDate.monthOfYear().addToCopy(1));
        setupTempIncrements();
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
        double tempMultiplyer = 0;
        if ((new Interval(currentDate.withTimeAtStartOfDay().withHourOfDay(6), currentDate.withTimeAtStartOfDay().withHourOfDay(18))).contains(currentDate)) {
            tempMultiplyer = todayMaxTemp;
        } else {
            tempMultiplyer = todayMinTemp;
        }

        tempMultiplyer = todayMaxTemp - todayMinTemp;
//        System.out.println(tempMultiplyer);
        currentTemperature = Math.cos(2 * Math.PI * ((Config.lengthOfDayInMinutes / 2 + (double) new Duration(currentDate.withTimeAtStartOfDay(),
                currentDate).getStandardMinutes()) / Config.lengthOfDayInMinutes)) * tempMultiplyer / 2 + todayMinTemp + tempMultiplyer / 2;
//        System.out.println(((double)new Duration(currentDate.withTimeAtStartOfDay(),
//                currentDate).getStandardMinutes() / Config.lengthOfDayInMinutes));
        System.out.println(currentDate.getHourOfDay() + " " + currentTemperature);
    }

    public static void setupNewDay() {
        currentDaySpan = new Interval(currentDate, currentDate.dayOfMonth().addToCopy(1).withTimeAtStartOfDay());
        currentDayLength = calculateDaylight();
        calculateDailyTemperatures();
        setSunriseAndSunset(currentDayLength);
        Iterator<Animal> animalsIterator = animals.iterator();
        while (animalsIterator.hasNext()) {
            Animal animal = animalsIterator.next();
            animal.dailyCheckUp();
            if (animal.isDead()) {
                animalsIterator.remove();
                deaths++;
                System.out.println("Deaths: " + deaths);
            }
        }

        if (birthDays.contains(currentDate)) {
           reproduce();
        }


    }

    private static void reproduce() {
        ArrayList<Animal> youngOnes = new ArrayList<>();
        Random r = new Random();
        for (Animal animalBreeding : animals) {
            if (currentDaySpan.contains(animalBreeding.getBirthDay())) {
                if (r.nextDouble() < animalBreeding.getAgeGroups()[animalBreeding.getAgeGroupNumerical()].getChanceOfPregnancy()) {
                    JsonReader animalLoader = null;

                    try {
                        animalLoader = new JsonReader(new FileReader(IOUtil.getConfigDirectory() + animalBreeding.getName() + ".json"));
                        Gson gson = new Gson();

                        Animal animal = gson.fromJson(animalLoader, Class.forName("model." + animalBreeding.getName()));
                        System.out.println(animal.getClass().getName());

                        int x = animalBreeding.getxPos();
                        int y = animalBreeding.getyPos();
                        animal.setxPos(x);
                        animal.setyPos(y);
                        animal.setAge(animal.getAgeGroups()[0].getStartAge());
                        animal.setId(currentMaxId);
                        animal.setAgeGroupNumerical(0);
                        animal.setupAnimal();
                        currentMaxId++;
                        youngOnes.add(animal);
                        System.out.println(animal);
                    } catch (FileNotFoundException | ClassNotFoundException e) {
                        e.printStackTrace();

                    }
                }
            }
        }

        animals.addAll(youngOnes);
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

    public static void checkForNight() {
        //TODO this is not working, must be able to check for times only, not dates
        if (currentDate.isAfter(sunset) || currentDate.isBefore(sunrise)) {
            night = true;
        } else {
            night = false;
        }
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

    public static boolean isNight() {
        return night;
    }

    public static void loadInitialPopulations() {
        JsonReader populationsLoader = null;
        try {
            populationsLoader = new JsonReader(new FileReader(Config.getPopulationsFilePath()));
            Gson gson = new Gson();
            initialPopulations = gson.fromJson(populationsLoader, Populations.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(initialPopulations);
        System.out.println(initialPopulations);
    }

    public static void loadAnimals() {
        System.out.println("asdfsd " + Animal.class);
//        System.out.println(Class.forName());
        Random r = new Random();
        for (Population population : initialPopulations.getPopulations()) {
            try {
                System.out.println(Class.forName("model.Animal"));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            JsonReader animalLoader = null;
            for (int i = 0; i < population.getYoung(); i++) {
                try {
                    animalLoader = new JsonReader(new FileReader(IOUtil.getConfigDirectory() + population.getName() + ".json"));
                    Gson gson = new Gson();
                    System.out.println(population.getName());
                    Animal animal = gson.fromJson(animalLoader, Class.forName("model." + population.getName()));
                    System.out.println(animal.getClass().getName());
//                    Animal animal = gson.fromJson(animalLoader, Animal.class);

                    int x = r.nextInt(MapHandlerAdvanced.getWidth());
                    int y = r.nextInt(MapHandlerAdvanced.getHeight());
                    while (!MapHandlerAdvanced.isValidMove(x, y)) {
                        x = r.nextInt(MapHandlerAdvanced.getWidth());
                        y = r.nextInt(MapHandlerAdvanced.getHeight());
                    }
                    animal.setxPos(x);
                    animal.setyPos(y);
                    animal.setAge(animal.getAgeGroups()[0].getStartAge());
                    animal.setId(currentMaxId);
                    animal.setAgeGroupNumerical(0);

                    animal.setupAnimal();
                    currentMaxId++;
                    animals.add(animal);
                    birthDays.add(animal.getBirthDay());
                    System.out.println(animal);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }


            for (int i = 0; i < population.getAdult(); i++) {
                try {
                    animalLoader = new JsonReader(new FileReader(IOUtil.getConfigDirectory() + population.getName() + ".json"));
                    Gson gson = new Gson();
                    System.out.println(population.getName());
                    Animal animal = gson.fromJson(animalLoader, Class.forName("model." + population.getName()));
                    System.out.println(animal.getClass().getName());
//                    Animal animal = gson.fromJson(animalLoader, Animal.class);

                    int x = r.nextInt(MapHandlerAdvanced.getWidth());
                    int y = r.nextInt(MapHandlerAdvanced.getHeight());
                    while (!MapHandlerAdvanced.isValidMove(x, y)) {
                        x = r.nextInt(MapHandlerAdvanced.getWidth());
                        y = r.nextInt(MapHandlerAdvanced.getHeight());
                    }
                    animal.setxPos(x);
                    animal.setyPos(y);
                    animal.setAge(animal.getAgeGroups()[1].getStartAge());
                    animal.setId(currentMaxId);
                    animal.setAgeGroupNumerical(1);
                    animal.setupAnimal();
                    currentMaxId++;
                    animals.add(animal);
                    birthDays.add(animal.getBirthDay());
                    System.out.println(animal);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < population.getOld(); i++) {
                try {
                    animalLoader = new JsonReader(new FileReader(IOUtil.getConfigDirectory() + population.getName() + ".json"));
                    Gson gson = new Gson();
                    System.out.println(population.getName());
                    Animal animal = gson.fromJson(animalLoader, Class.forName("model." + population.getName()));
                    System.out.println(animal.getClass().getName());
//                    Animal animal = gson.fromJson(animalLoader, Animal.class);

                    int x = r.nextInt(MapHandlerAdvanced.getWidth());
                    int y = r.nextInt(MapHandlerAdvanced.getHeight());
                    while (!MapHandlerAdvanced.isValidMove(x, y)) {
                        x = r.nextInt(MapHandlerAdvanced.getWidth());
                        y = r.nextInt(MapHandlerAdvanced.getHeight());
                    }
                    animal.setxPos(x);
                    animal.setyPos(y);
                    animal.setAge(animal.getAgeGroups()[2].getStartAge());
                    animal.setId(currentMaxId);
                    animal.setAgeGroupNumerical(2);
                    animal.setupAnimal();
                    currentMaxId++;
                    animals.add(animal);
                    System.out.println(animal);
                    birthDays.add(animal.getBirthDay());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void returnValidCoordinates() {
    }
}
