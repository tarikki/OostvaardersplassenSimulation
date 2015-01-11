package mapUtils;

/**
 * Created by extradikke on 06/12/14.
 */
public class Plant {
    private String name;
    private int maxHealth;
    private int recoveryDays;
    private int nutrition;
    private int id;
    private float growthRate;
    private float growthWhenDamaged;
    private int temperatureThreshold;
    private int inediblePart;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getRecoveryDays() {
        return recoveryDays;
    }

    public void setRecoveryDays(int recoveryDays) {
        this.recoveryDays = recoveryDays;
    }

    public int getNutrition() {
        return nutrition;
    }

    public void setNutrition(int nutrition) {
        this.nutrition = nutrition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getGrowthRate() {
        return growthRate;
    }

    public void setGrowthRate(float growthRate) {
        this.growthRate = growthRate;
    }

    public float getGrowthWhenDamaged() {
        return growthWhenDamaged;
    }

    public void setGrowthWhenDamaged(float growthWhenDamaged) {
        this.growthWhenDamaged = growthWhenDamaged;
    }

    public int getTemperatureThreshold() {
        return temperatureThreshold;
    }

    public void setTemperatureThreshold(int temperatureThreshold) {
        this.temperatureThreshold = temperatureThreshold;
    }


    public int getInediblePart() {
        return inediblePart;
    }

    public void setInediblePart(int inediblePart) {
        this.inediblePart = inediblePart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Plant plant = (Plant) o;

        if (!name.equals(plant.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Plant{" +
                "growthRate=" + growthRate +
                ", name='" + name + '\'' +
                ", maxHealth=" + maxHealth +
                ", recoveryDays=" + recoveryDays +
                ", nutrition=" + nutrition +
                ", id=" + id +
                ", growthWhenDamaged=" + growthWhenDamaged +
                ", temperatureThreshold=" + temperatureThreshold +
                '}';
    }
}
