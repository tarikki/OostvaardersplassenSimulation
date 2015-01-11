package animalUtils;

/**
 * Created by extradikke on 26.12.14.
 */
public class AgeGroup {

    private String name;
    private String id;
    private int startAge; // days when transition to this age happens
    private int endAge;   // is this necessary?
    private double startWeight;
    private double chanceOfDeath;
    private double energyForKiloIncrease; // how much energy needed to increase body weight by one kilo
    private double energyExpenditurePerKilo; // how much energy the animal needs to sustain its body weight
    private double necessaryWeightIncreasePerDay; // how much needed to stay on normal growth curve
    private double chanceOfPregnancy;


    public double getChanceOfDeath() {
        return chanceOfDeath;
    }

    public void setChanceOfDeath(double chanceOfDeath) {
        this.chanceOfDeath = chanceOfDeath;
    }

    public double getChanceOfPregnancy() {
        return chanceOfPregnancy;
    }

    public void setChanceOfPregnancy(double chanceOfPregnancy) {
        this.chanceOfPregnancy = chanceOfPregnancy;
    }

    public int getEndAge() {
        return endAge;
    }

    public void setEndAge(int endAge) {
        this.endAge = endAge;
    }

    public double getEnergyExpenditurePerKilo() {
        return energyExpenditurePerKilo;
    }

    public void setEnergyExpenditurePerKilo(double energyExpenditurePerKilo) {
        this.energyExpenditurePerKilo = energyExpenditurePerKilo;
    }

    public double getEnergyForKiloIncrease() {
        return energyForKiloIncrease;
    }

    public void setEnergyForKiloIncrease(double energyForKiloIncrease) {
        this.energyForKiloIncrease = energyForKiloIncrease;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getNecessaryWeightIncreasePerDay() {
        return necessaryWeightIncreasePerDay;
    }

    public void setNecessaryWeightIncreasePerDay(double necessaryWeightIncreasePerDay) {
        this.necessaryWeightIncreasePerDay = necessaryWeightIncreasePerDay;
    }

    public int getStartAge() {
        return startAge;
    }

    public void setStartAge(int startAge) {
        this.startAge = startAge;
    }

    public double getStartWeight() {
        return startWeight;
    }

    public void setStartWeight(double startWeight) {
        this.startWeight = startWeight;
    }

    @Override
    public String toString() {
        return "AgeGroup{" +
                "chanceOfDeath=" + chanceOfDeath +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", startAge=" + startAge +
                ", endAge=" + endAge +
                ", energyForKiloIncrease=" + energyForKiloIncrease +
                ", energyExpenditurePerKilo=" + energyExpenditurePerKilo +
                ", necessaryWeightIncreasePerDay=" + necessaryWeightIncreasePerDay +
                ", chanceOfPregnancy=" + chanceOfPregnancy +
                '}';
    }
}
