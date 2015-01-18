package model;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import mapUtils.MapHandlerAdvanced;
import mapUtils.MonthlyWeather;
import org.joda.time.*;
import util.*;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by extradikke on 20-11-14.
 * <p/>
 * A model for the preserve
 */
public class Preserve {

    private static int turn;

    private static DateTime startDate;
    private static DateTime endDate;
    private static boolean simulationComplete;

    private static DateTime currentDate;

    private static boolean night;
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
    private static double[] dailyTemperatures = new double[24];
    private static HashMap<Integer, MonthlyWeather> weatherHashMap = new HashMap<>();
    private static Populations initialPopulations;
    private static Set<DateTime> birthDays = new HashSet<>();

    //statistics
    private static int deaths = 0;
    private static int births = 0;
    private static ArrayList<DailyEvents> allDailyEvents = new ArrayList<>();
    private static HashMap<String, StatisticsStorage> statisticsStorage;


    private static double latitude;


    private static ArrayList<Animal> animals = new ArrayList<>();
    private static int currentMaxId = 0;
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    public static CountDownLatch latch;

    /**
     * Initialize all the values needed in the beginning
     *
     * @param latitudeInput  latitude of the area in question, used to calculate the amount of daylight
     * @param startDateInput start date of simulation
     * @param endDateInput   end date of simulation
     */
    public static void setupPreserve(double latitudeInput, DateTime startDateInput, DateTime endDateInput) {
        //TODO still possible to place animals on top of each other, not a big bug, will fix it if time

        statisticsStorage = new HashMap<>(3);


        simulationComplete = false;
        allDailyEvents = new ArrayList<>();

        currentDate = new DateTime(2014, 6, 21, 0, 0, 0);
        maxLengthOfDay = calculateDaylight();
        weatherHashMap = MapHandlerAdvanced.getWeatherHashMap();


        latitude = latitudeInput;
        currentDate = startDate = startDateInput.withTimeAtStartOfDay();
        endDate = endDateInput.withTimeAtStartOfDay();
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

        loadInitialPopulations();
        loadAnimals();

    }


    /**
     * a method for performing all the actions during one time tick
     *
     * @throws InterruptedException
     */
    public static void executeTurn() throws InterruptedException {

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

//        System.out.println("Now turn: " + turn++ +" "+ currentDate.toString());
        currentDate = currentDate.plusMinutes(1);
//        System.out.println(currentDate.toString());
        if (currentDate.isAfter(endDate)) {
            simulationComplete = true;
        }

        if (isNewHour()) {
            setupNewHour();
        }

        if (isNewDay()) {
            setupNewDay();
        }

        if (isNewTempMonth()) {
            setupNewTempMonth();
        }
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

    /**
     * TempMonths are calculated from halfway of each month to the half of the next.
     * This method checks weather it's necessary to calculate new values for the temperature month
     *
     * @return boolean to indicate change of month
     */
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

    /**
     * This method calculates the rate of change in the average temperature during one temperature month
     */
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

    /**
     * Calculate the max and min temperatures of the current day
     */
    public static void calculateDailyTemperatures() {
        todayMaxTemp = startMaxTemp + currentDailyMaxTempIncrement * (new Duration(currentTempMonthSpan.getStart(), currentDate)).getStandardDays();
        todayMinTemp = startMinTemp + currentDailyMinTempIncrement * (new Duration(currentTempMonthSpan.getStart(), currentDate)).getStandardDays();
        System.out.println("max today: " + todayMaxTemp);
        System.out.println("min today: " + todayMinTemp);
        calculateAllDayTemperatures();
    }

    /**
     * This method sets the current temperature from the hourly temperature array
     */
    public static void calculateHourlyTemperature() {
        System.out.println("hour: " + currentDate.hourOfDay().get());
        currentTemperature = dailyTemperatures[currentDate.hourOfDay().get()];
        System.out.println(currentTemperature);

    }

    public static void calculateAllDayTemperatures() {
        double tempMultiplyer = 0;
        tempMultiplyer = todayMaxTemp - todayMinTemp;

        for (int hour = 0; hour < 24; hour++) {

            dailyTemperatures[hour] = Math.cos(2 * Math.PI * ((Config.lengthOfDayInMinutes / 2 + (double) new Duration(currentDate.withTimeAtStartOfDay(),
                    currentDate.withTimeAtStartOfDay().plusMinutes(hour * 60)).getStandardMinutes()) / Config.lengthOfDayInMinutes)) * tempMultiplyer / 2 + todayMinTemp + tempMultiplyer / 2;
        }
    }

    /**
     * This method sets all the variables and actions necessary for the start of a new day
     */
    public static void setupNewDay() {
        System.out.println("New Day");

        DailyEvents dailyEvents = new DailyEvents(currentDate.withTimeAtStartOfDay());

        currentDaySpan = new Interval(currentDate, currentDate.dayOfMonth().addToCopy(1).withTimeAtStartOfDay());
        currentDayLength = calculateDaylight();
        calculateDailyTemperatures();

        dailyEvents.setMaxTemp(todayMaxTemp);

        dailyEvents.setMinTemp(todayMinTemp);
        dailyEvents.setLengthOfDay(currentDayLength);

        MapHandlerAdvanced.growAllPlants();
        System.out.println("mycket growth");
        System.out.println(MapHandlerAdvanced.getAmountOfFoodBefore());
        System.out.println(MapHandlerAdvanced.getAmountOfFoodNow());
        dailyEvents.setAmountOfFood(MapHandlerAdvanced.getAmountOfFoodNow());

        allDailyEvents.add(dailyEvents);

        setSunriseAndSunset(currentDayLength);
        Iterator<Animal> animalsIterator = animals.iterator();
        while (animalsIterator.hasNext()) {
            Animal animal = animalsIterator.next();
            if (animal.isStarving()) {
                dailyEvents.increaseStarvingAnimals();
                System.out.println("me starving!");

            }
            animal.dailyCheckUp();

            if (animal.isDead()) {
                animalsIterator.remove();
                String animalClass = animal.getClass().getSimpleName().toLowerCase();
                try {
                    Method method = dailyEvents.getClass().getMethod(animalClass + "DeathsIncrease");
                    method.invoke(dailyEvents);
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                deaths++;
                statisticsStorage.get(animal.getName()).increaseDeaths();
//                System.out.println("Deaths: " + deaths);
            }
        }

        if (birthDays.contains(currentDate)) {
            System.out.println("it's my birthday!");
            reproduce();
        }

        if (currentDaySpan.contains(endDate)) {
            simulationComplete = true;
            System.out.println("DONE with simulation!!!");
            int foodBefore = 0;
            for (DailyEvents dailyEvent : allDailyEvents) {
                if (foodBefore != 0) {
                    System.out.println("Food change = " + (dailyEvent.getAmountOfFood() - foodBefore));
                }
                foodBefore = dailyEvent.getAmountOfFood();
            }

            for (Animal animal : animals) {
                statisticsStorage.get(animal.getName()).increaseNumberAtEnd();
            }

            for (StatisticsStorage storage : statisticsStorage.values()) {
                System.out.println(storage);
            }
        }

        System.out.println(allDailyEvents.get(allDailyEvents.size() - 1));


    }

    /**
     * This method loops through all the animals and makes them reproduce if they are pregnant
     */
    private static void reproduce() {
        boolean deerMales = false;
        boolean cowMales = false;
        boolean horseMales = false;

        for (Animal animal : animals) {
            if (animal.getClass().getSimpleName().equals("Deer") && animal.isMale()) {
                deerMales = true;
            }
            if (animal.getClass().getSimpleName().equals("Cow") && animal.isMale()) {
                cowMales = true;
            }
            if (animal.getClass().getSimpleName().equals("Horse") && animal.isMale()) {
                horseMales = true;
            }
        }

        ArrayList<Animal> youngOnes = new ArrayList<>();
        Random r = new Random();
        for (Animal animalBreeding : animals) {
            if (currentDaySpan.contains(animalBreeding.getBirthDay())) {
                if (r.nextDouble() < animalBreeding.getAgeGroups()[animalBreeding.getAgeGroupNumerical()].getChanceOfPregnancy()
                        && ((animalBreeding.getClass().getSimpleName().equals("Deer") && deerMales) ||
                                (animalBreeding.getClass().getSimpleName().equals("Cow") && cowMales) ||
                                (animalBreeding.getClass().getSimpleName().equals("Horse") && horseMales))) {
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
                        births++;
                        youngOnes.add(animal);
                        statisticsStorage.get(animal.getName()).increaseBirths();
                        String animalClass = animal.getClass().getSimpleName().toLowerCase();
                        try {
                            Method method = allDailyEvents.get(allDailyEvents.size() - 1).getClass().getMethod(animalClass + "BirthsIncrease");
                            method.invoke(allDailyEvents.get(allDailyEvents.size() - 1));
                            System.out.println("birth!!!");
                        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
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
        int statsNumber = 0;
        for (Population population : initialPopulations.getPopulations()) {
            statisticsStorage.put(population.getName(), new StatisticsStorage(population.getName()));
            statsNumber++;
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

                    int x = population.getStartX() + (int) (r.nextGaussian() * population.getStandardDeviationInPixels());
                    int y = population.getStartY() + (int) (r.nextGaussian() * population.getStandardDeviationInPixels());
                    while (!MapHandlerAdvanced.isValidMove(x, y)) {
                        x = population.getStartX() + (int) (r.nextGaussian() * population.getStandardDeviationInPixels());
                        y = population.getStartY() + (int) (r.nextGaussian() * population.getStandardDeviationInPixels());
                    }
                    animal.setxPos(x);
                    animal.setyPos(y);
                    animal.setAge(animal.getAgeGroups()[0].getStartAge());
                    animal.setId(currentMaxId);
                    animal.setAgeGroupNumerical(0);

                    animal.setupAnimal();
                    currentMaxId++;
                    animals.add(animal);
                    statisticsStorage.get(population.getName()).increaseNumberAtStart();
                    birthDays.add(animal.getBirthDay().withYear(currentDate.getYear()));
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

                    int x = population.getStartX() + (int) (r.nextGaussian() * population.getStandardDeviationInPixels());
                    int y = population.getStartY() + (int) (r.nextGaussian() * population.getStandardDeviationInPixels());
                    while (!MapHandlerAdvanced.isValidMove(x, y)) {
                        x = population.getStartX() + (int) (r.nextGaussian() * population.getStandardDeviationInPixels());
                        y = population.getStartY() + (int) (r.nextGaussian() * population.getStandardDeviationInPixels());
                    }
                    animal.setxPos(x);
                    animal.setyPos(y);
                    animal.setAge(animal.getAgeGroups()[1].getStartAge());
                    animal.setId(currentMaxId);
                    animal.setAgeGroupNumerical(1);
                    animal.setupAnimal();
                    currentMaxId++;
                    animals.add(animal);
                    statisticsStorage.get(population.getName()).increaseNumberAtStart();
                    birthDays.add(animal.getBirthDay().withYear(currentDate.getYear()));
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

                    int x = population.getStartX() + (int) (r.nextGaussian() * population.getStandardDeviationInPixels());
                    int y = population.getStartY() + (int) (r.nextGaussian() * population.getStandardDeviationInPixels());
                    while (!MapHandlerAdvanced.isValidMove(x, y)) {
                        x = population.getStartX() + (int) (r.nextGaussian() * population.getStandardDeviationInPixels());
                        y = population.getStartY() + (int) (r.nextGaussian() * population.getStandardDeviationInPixels());
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
                    statisticsStorage.get(population.getName()).increaseNumberAtStart();
                    birthDays.add(animal.getBirthDay().withYear(currentDate.getYear()));
                } catch (FileNotFoundException | ClassNotFoundException e) {
                    e.printStackTrace();

                }
            }
        }
        for (DateTime birthDay : birthDays) {
            System.out.println(birthDay);
        }

        for (StatisticsStorage storage : statisticsStorage.values()) {
            System.out.println(storage);
        }
    }

    public static boolean getSimulationComplete() {
        return simulationComplete;
    }

    public static double[] getDailyTemperatures() {
        return dailyTemperatures;
    }

    public static HashMap<String, StatisticsStorage> getStatisticsStorage() {
        return statisticsStorage;
    }

}
