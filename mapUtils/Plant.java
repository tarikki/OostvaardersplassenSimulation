package mapUtils;

/**
 * Created by extradikke on 06/12/14.
 */
public class Plant {
    private String name;
    private int maxHealth;
    private int recoveryDays;
    private int nutrition;

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

    @Override
    public String toString() {
        return "Plant{" +
                "name='" + name + '\'' +
                ", maxHealth=" + maxHealth +
                ", recoveryDays=" + recoveryDays +
                ", nutrition=" + nutrition +
                '}';
    }
}
