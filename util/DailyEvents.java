package util;

import org.joda.time.DateTime;

/**
 * Created by extradikke on 17/01/15.
 */
public class DailyEvents {

    private DateTime date;
    private int deerDeaths = 0;
    private int deerBirths = 0;
    private int cowDeaths = 0;
    private int cowBirths = 0;
    private int horseDeaths = 0;
    private int horseBirths = 0;
    private int amountOfFood = 0;
    private double maxTemp;
    private double minTemp;
    private double lengthOfDay;
    private int starvingAnimals = 0;

    public DailyEvents(DateTime date) {
        this.date = date;

    }

    public int getAmountOfFood() {
        return amountOfFood;
    }

    public void setAmountOfFood(int amountOfFood) {
        this.amountOfFood = amountOfFood;
    }

    public int getCowBirths() {
        return cowBirths;
    }

    public int getCowDeaths() {
        return cowDeaths;
    }


    public int getDeerBirths() {
        return deerBirths;
    }


    public int getDeerDeaths() {
        return deerDeaths;
    }


    public int getHorseBirths() {
        return horseBirths;
    }


    public int getHorseDeaths() {
        return horseDeaths;
    }


    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getLengthOfDay() {
        return lengthOfDay;
    }

    public void setLengthOfDay(double lengthOfDay) {
        this.lengthOfDay = lengthOfDay;
    }

    public void deerDeathsIncrease() {
        deerDeaths++;
    }

    public void deerBirthsIncrease() {
        deerBirths++;
    }

    public void horseDeathsIncrease() {
        horseDeaths++;
    }

    public void horseBirthsIncrease() {
        horseBirths++;
    }

    public void cowDeathsIncrease() {
        cowDeaths++;
    }

    public void cowBirthsIncrease() {
        cowBirths++;
    }

    public void increaseStarvingAnimals(){
        starvingAnimals++;
    }


    @Override
    public String toString() {
        return "DailyEvents{" +
                "amountOfFood=" + amountOfFood +
                ", date=" + date +
                ", deerDeaths=" + deerDeaths +
                ", deerBirths=" + deerBirths +
                ", cowDeaths=" + cowDeaths +
                ", cowBirths=" + cowBirths +
                ", horseDeaths=" + horseDeaths +
                ", horseBirths=" + horseBirths +
                ", maxTemp=" + maxTemp +
                ", minTemp=" + minTemp +
                ", lengthOfDay=" + lengthOfDay +
                ", starvingAnimals=" + starvingAnimals +
                '}';
    }
}


