package model;

/**
 * Created by extradikke on 30.12.14.
 */
public class Population {
    private String name;
    private int id;
    private int young;
    private int adult;
    private int old;
    private int startX;
    private int startY;
    private int standardDeviationInPixels;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYoung() {
        return young;
    }

    public void setYoung(int young) {
        this.young = young;
    }

    public int getAdult() {
        return adult;
    }

    public void setAdult(int adult) {
        this.adult = adult;
    }

    public int getOld() {
        return old;
    }

    public void setOld(int old) {
        this.old = old;
    }

    public int getStandardDeviationInPixels() {
        return standardDeviationInPixels;
    }

    public void setStandardDeviationInPixels(int standardDeviationInPixels) {
        this.standardDeviationInPixels = standardDeviationInPixels;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Population that = (Population) o;

        if (!name.equals(that.name)) return false;

        return true;
    }


    @Override
    public String toString() {
        return "Population{" +
                "adult=" + adult +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", young=" + young +
                ", old=" + old +
                '}';
    }
}
