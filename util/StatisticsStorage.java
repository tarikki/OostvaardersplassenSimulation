package util;

/**
 * Created by extradikke on 18/01/15.
 */
public class StatisticsStorage {
    private String name;
    private int numberAtStart;
    private int numberAtEnd;
    private int births;
    private int deaths;

    public StatisticsStorage(String name) {
        this.name = name;
    }

    public int getBirths() {
        return births;
    }

    public void increaseBirths() {
        births++;
    }

    public void increaseDeaths() {
        deaths++;
    }

    public void setBirths(int births) {
        this.births = births;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getNumberAtEnd() {
        return numberAtEnd;
    }

    public void setNumberAtEnd(int numberAtEnd) {
        this.numberAtEnd = numberAtEnd;
    }

    public int getNumberAtStart() {
        return numberAtStart;
    }

    public void increaseNumberAtStart() {
        numberAtStart++;
    }

    public void increaseNumberAtEnd() {
        numberAtEnd++;
    }

    public void setNumberAtStart(int numberAtStart) {
        this.numberAtStart = numberAtStart;
    }


    @Override
    public String toString() {
        return "StatisticsStorage{" +
                "births=" + births +
                ", name='" + name + '\'' +
                ", numberAtStart=" + numberAtStart +
                ", numberAtEnd=" + numberAtEnd +
                ", deaths=" + deaths +
                '}';
    }
}
