package util;

/**
 * Created by extradikke on 10/12/14.
 *
 * A class for loading all the information necessary to run the simulation.
 * Could be replaced by some methods in the static config class so no need to have this extra class.
 */
public class ConfigLoader {



    // name of files
    private String terrainMapName;
    private String plantsName;
    private String terrainsName;
    private String displayableMapName;
    private String animalsFileName;
    private String weatherFileName;
    private String populationsFileName;

    private double latitude;
    private int scale;
    private String dateTimeZone;

    private int numberOfAnimals;
    private int speedOfSimulation;

    private String startingDate;
    private String endingDate;


    public int getNumberOfAnimals() {
        return numberOfAnimals;
    }

    public void setNumberOfAnimals(int numberOfAnimals) {
        this.numberOfAnimals = numberOfAnimals;
    }

    public int getSpeedOfSimulation() {
        return speedOfSimulation;
    }

    public void setSpeedOfSimulation(int speedOfSimulation) {
        this.speedOfSimulation = speedOfSimulation;
    }

    public String getTerrainMapName() {
        return terrainMapName;
    }

    public void setTerrainMapName(String terrainMapName) {
        this.terrainMapName = terrainMapName;
    }

    public String getPlantsName() {
        return plantsName;
    }

    public void setPlantsName(String plantsName) {
        this.plantsName = plantsName;
    }

    public String getTerrainsName() {
        return terrainsName;
    }

    public void setTerrainsName(String terrainsName) {
        this.terrainsName = terrainsName;
    }

    public String getDisplayableMapName() {
        return displayableMapName;
    }

    public void setDisplayableMapName(String displayableMapName) {
        this.displayableMapName = displayableMapName;
    }

    public String getAnimalsFileName() {
        return animalsFileName;
    }

    public void setAnimalsFileName(String animalsFileName) {
        this.animalsFileName = animalsFileName;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    public String getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(String endingDate) {
        this.endingDate = endingDate;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDateTimeZone() {
        return dateTimeZone;
    }

    public void setDateTimeZone(String dateTimeZone) {
        this.dateTimeZone = dateTimeZone;
    }

    public String getWeatherFileName() {
        return weatherFileName;
    }

    public void setWeatherFileName(String weatherFileName) {
        this.weatherFileName = weatherFileName;
    }

    public String getPopulationsFileName() {
        return populationsFileName;
    }

    public void setPopulationsFileName(String populationsFileName) {
        this.populationsFileName = populationsFileName;
    }

    @Override
    public String toString() {
        return "ConfigLoader{" +
                "terrainMapName='" + terrainMapName + '\'' +
                ", plantsName='" + plantsName + '\'' +
                ", terrainsName='" + terrainsName + '\'' +
                ", displayableMapName='" + displayableMapName + '\'' +
                ", animalsFileName='" + animalsFileName + '\'' +
                ", numberOfAnimals=" + numberOfAnimals +
                ", speedOfSimulation=" + speedOfSimulation +
                ", startingDate=" + startingDate +
                ", endingDate=" + endingDate +
                '}';
    }
}
