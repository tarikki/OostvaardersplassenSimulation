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
