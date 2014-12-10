package util;

import org.joda.time.DateTime;

/**
 * Created by extradikke on 10/12/14.
 */
public class Config {


    // name of files
    private static String terrainMapPath ;
    private static String plantsPath;
    private static String terrainsPath;
    private static String displayableMapPath;
    private static String animalsFilePath;

    private static int numberOfAnimals;
    private static int speedOfSimulation;

    private static DateTime startingDate;
    private static DateTime endingDate;


    public static String getTerrainMapPath() {
        return terrainMapPath;
    }

    public static void setTerrainMapPath(String terrainMapPath) {
        Config.terrainMapPath = terrainMapPath;
    }

    public static String getPlantsPath() {
        return plantsPath;
    }

    public static void setPlantsPath(String plantsPath) {
        Config.plantsPath = plantsPath;
    }

    public static String getTerrainsPath() {
        return terrainsPath;
    }

    public static void setTerrainsPath(String terrainsPath) {
        Config.terrainsPath = terrainsPath;
    }

    public static String getDisplayableMapPath() {
        return displayableMapPath;
    }

    public static void setDisplayableMapPath(String displayableMapPath) {
        Config.displayableMapPath = displayableMapPath;
    }

    public static String getAnimalsFilePath() {
        return animalsFilePath;
    }

    public static void setAnimalsFilePath(String animalsFilePath) {
        Config.animalsFilePath = animalsFilePath;
    }

    public static int getNumberOfAnimals() {
        return numberOfAnimals;
    }

    public static void setNumberOfAnimals(int numberofAnimals) {
        Config.numberOfAnimals = numberofAnimals;
    }

    public static int getSpeedOfSimulation() {
        return speedOfSimulation;
    }

    public static void setSpeedOfSimulation(int speedOfSimulation) {
        Config.speedOfSimulation = speedOfSimulation;
    }

    public static DateTime getStartingDate() {
        return startingDate;
    }

    public static void setStartingDate(DateTime startingDate) {
        Config.startingDate = startingDate;
    }

    public static DateTime getEndingDate() {
        return endingDate;
    }

    public static void setEndingDate(DateTime endingDate) {
        Config.endingDate = endingDate;
    }



    public static String print() {
        return "Config{" +
                "terrainMapPath='" + terrainMapPath + '\'' +
                ", plantsPath='" + plantsPath + '\'' +
                ", terrainsPath='" + terrainsPath + '\'' +
                ", displayableMapPath='" + displayableMapPath + '\'' +
                ", animalsFilePath='" + animalsFilePath + '\'' +
                ", numberOfAnimals=" + numberOfAnimals +
                ", speedOfSimulation=" + speedOfSimulation +
                ", startingDate=" + startingDate +
                ", endingDate=" + endingDate +
                '}';
    }


}
