package model;

import animalUtils.AgeGroup;
import animalUtils.AgeGroups;
import mapUtils.MapHandlerAdvanced;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import util.DijkstraNode;

import java.util.*;

/**
 * Created by extradikke on 20-11-14.
 */
public class Animal implements Runnable {


    private int id;
    private int hunger = 0;
    private int thirst = 0;
    private int energy = 100;
    private boolean dead;
    private int age;
    private int ageGroupNumerical;
    private double weight;
    private double energyNeededPerDay;



    //loadable traits
    private String name;
    private AgeGroup[] ageGroups;
    private DateTime birthDay;
    private String birthDayString;

    private int lineOfSight; //how far the animal can see around it
    private int xPos;
    private int yPos;
    private boolean dijkstraScanned = false;
    private int maxDijkstraLoops = lineOfSight;
    private boolean moved = true;
    private boolean stuck = false;
    private Deque<DijkstraNode> wayPoints = new ArrayDeque<DijkstraNode>(); // the path expressed in DjikstraNodes


    private HashMap<DijkstraNode, DijkstraNode> innerNodes;


    public Animal(int id, int x, int y, int initialAge) {
        this.id = id;
        this.xPos = x;
        this.yPos = y;
        this.age = initialAge;

    }

    public void setupAnimal() {
        hunger = thirst = 0;
        energy = 100;
        dead = false;
        wayPoints = new ArrayDeque<>();
        moved = true;
        stuck = false;
        maxDijkstraLoops = lineOfSight;
        birthDay = DateTime.parse(birthDayString, DateTimeFormat.forPattern("dd.MM.YYYY")).withYear(Preserve.getStartDate().getYear());
//        if (Preserve.getStartDate().isBefore(birthDay)){
        int daysDifference = Days.daysBetween(birthDay, Preserve.getStartDate()).getDays();
        System.out.println(daysDifference);
        age = age + daysDifference;

        System.out.println(ageGroups[ageGroupNumerical].getName() + ": age in days " + age);
    }

    public void findFoodOrWater(String lookingFor, int threshHold) {


        HashMap<DijkstraNode, DijkstraNode> borderNodes = new HashMap<>();
        HashMap<DijkstraNode, DijkstraNode> bufferNodes = new HashMap<>();
        innerNodes = new HashMap<>();


        DijkstraNode homeNode = new DijkstraNode(xPos, yPos, 0, 0, 0);
        borderNodes.put(homeNode, homeNode);

        boolean foundIt = false;
        boolean loopingDone = false;
        DijkstraNode winnerNode = homeNode;

        int currentLoops = 0;

        while (!loopingDone) {
            innerNodes.putAll(bufferNodes);
            bufferNodes.clear();
// TODO if no new borderNodes, then stop looping because no place to go

            for (DijkstraNode borderNode : borderNodes.values()) {
                double lowestCost = 100000;
                if (lookingFor.equals("food")) {
                    int currentFoodValue = MapHandlerAdvanced.getPlantHealth(borderNode.getCurrentX(), borderNode.getCurrentY());
                    if (currentFoodValue >= threshHold && borderNode.getCost() < lowestCost) {
                        foundIt = true;
                        winnerNode = borderNode;
                        loopingDone = true;
                        lowestCost = borderNode.getCost();
//                        System.out.println("found food with cost:" + lowestCost);
                    }
                }

                if (lookingFor.equals("water")) {
                    int terrainID = MapHandlerAdvanced.getTerrainID(borderNode.getCurrentX(), borderNode.getCurrentY());
                    if ((terrainID == MapHandlerAdvanced.water || terrainID == MapHandlerAdvanced.shallowWater) && !foundIt) {
                        foundIt = true;
                        winnerNode = borderNode;
                        loopingDone = true;
                    }
                }

//                if (!MapHandlerAdvanced.isValidMove(borderNode.getCurrentX(), borderNode.getCurrentY())) {
//                    borderNodes.remove(borderNode);
//                }

                if (MapHandlerAdvanced.isValidMove(borderNode.getCurrentX(), borderNode.getCurrentY())) {
                    bufferNodes.put(borderNode, borderNode);
                }
            }

            // TODO implement removal of non-traversable nodes so this will not return impossible paths

//            System.out.println("buffernodes size " + bufferNodes.size());
            if (bufferNodes.isEmpty()) {
//                System.out.println("stuck");
                stuck = true;
                loopingDone = true;
            }
            borderNodes.clear();

            for (DijkstraNode bufferNode : bufferNodes.values()) {
//                System.out.println("scanning buffered nodes");
                for (int y = -1; y < 2; y++) {
                    for (int x = -1; x < 2; x++) {                   // check all squares surrounding this square
                        int terrainID = MapHandlerAdvanced.getTerrainID(bufferNode.getCurrentX() + x, bufferNode.getCurrentY() + y);
                        DijkstraNode newNode;
//                        System.out.println("thinking about adding nodes");
                        double additionalCost = 0;
                        if (Math.abs(y) + Math.abs(x) == 2) {
                            additionalCost = 1.2;
                        } else {
                            additionalCost = 1;
                        }

                        if (terrainID == MapHandlerAdvanced.water) {
                            additionalCost = 1000;
                        }


                        if ((y != 0 ^ x != 0) && terrainID != MapHandlerAdvanced.border) { // don't add self, don't add border
                            int currentXPos = bufferNode.getCurrentX() + x;
                            int currentYPos = bufferNode.getCurrentY() + y;

//                            if (Math.abs(currentXPos - xPos) > lineOfSight || Math.abs(currentYPos - yPos) > lineOfSight) {
//                                System.out.println("too far");
//                                loopingDone = true;
//                            }

                            newNode = new DijkstraNode(currentXPos, currentYPos, x, y,
                                    bufferNode.getCost() + additionalCost);
                            if (borderNodes.containsValue(newNode)) {
                                if (newNode.getCost() < borderNodes.get(newNode).getCost()) {
                                    borderNodes.put(newNode, newNode);
                                }
                            }
                            if (!bufferNodes.containsValue(newNode) && !innerNodes.containsValue(newNode)) { // don't add duplicates
                                borderNodes.put(newNode, newNode);
//                                System.out.println(newNode);
                            }

                        }

                    }
                }
            }
//            System.out.println("borderNodes: " + borderNodes.size());
//            System.out.println("bufferNodes: " + bufferNodes.size());
//            System.out.println("innerNodes: " + innerNodes.size());


            currentLoops++;
            if (currentLoops > maxDijkstraLoops) {
                loopingDone = true;
//                System.out.println("max dijkstra");
            }
        }

        if (foundIt) {

            stackDijkstra(winnerNode);
        }

        // clean up
        innerNodes.clear();
        bufferNodes.clear();

        dijkstraScanned = true;
        moved = false;

    }

    private boolean stackDijkstra(DijkstraNode targetAcquired) {

//        System.out.println(targetAcquired);
        DijkstraNode next = innerNodes.get(new DijkstraNode(
                targetAcquired.getCurrentX() + -targetAcquired.getXDirection(), targetAcquired.getCurrentY() + -targetAcquired.getYDirection(), 0, 0, 1));            // next node is where the current is pointing att
//        System.out.println("Cost: " + next.getCost() + " nextX: " + next.getXDirection() + " nextY" + next.getYDirection());
//        System.out.println("nextNode "+next);
        wayPoints.add(new DijkstraNode(targetAcquired.getCurrentX(), targetAcquired.getCurrentY(), targetAcquired.getXDirection(), targetAcquired.getYDirection(), targetAcquired.getCost()));                     // and make it point the way to the next node
        if (targetAcquired.getCost() > 1.2) {                                          // if we're more than one node away from the animal
            stackDijkstra(next);
        }

        return true;
    }


    public int getId() {
        return id;
    }

//    public boolean findClosestStraight(MapHandler.ColorCode lookingFor) {
//        int closestX = 0;
//        int closestY = 0;
//        double lowestCost = lineOfSight * lineOfSight;
//        for (int h = -lineOfSight / 2; h < lineOfSight; h++) {
//            for (int w = -lineOfSight / 2; w < lineOfSight; w++) {
//                double cost = Math.pow(xPos + w, 2) + Math.pow(yPos + h, 2);
//                if (MapHandler.getValue(xPos + w, yPos + h) == lookingFor.getValue()) {
//                    closestX = xPos + w;
//                    closestY = yPos + h;
//                    lowestCost = cost;
//                }
//            }
//        }
//
//        int xDistance = closestX-xPos;
//        int xDirection = xDistance/Math.abs(xDistance);
//        int yDistance = closestY - yPos;
//        int yDirection = yDistance/Math.abs(yDistance);
//        float scale = Math.abs(xDistance)/Math.abs(yDistance);
//        float y= 0;
//        float yExtra = 0;
//        for (int x = 0; x < Math.abs(xDistance); x++) {
//            y +=scale;
//            yExtra +=
//            wayPoints.add(new DijkstraNode(xDirection, yDirection,1));
//        }
//        return true;
//    }


    public boolean checkForWayPoints() {
        boolean result = false;
        if (!wayPoints.isEmpty()) {
//            System.out.println("path");
//            for (DijkstraNode wayPoint : wayPoints) {
//
//                System.out.println(wayPoint);
//                }System.out.println("end path");
            DijkstraNode nextStep = wayPoints.pop();
            if (MapHandlerAdvanced.isValidMove(nextStep.getCurrentX(), nextStep.getCurrentY())) {

                move(nextStep.getXDirection(), nextStep.getYDirection());
            } else {
                wayPoints.clear();
//                System.out.println(MapHandlerAdvanced.getValue(nextStep.getXDirection() + xPos, nextStep.getYDirection() + xPos));
                System.out.println("waypoint cleared");
            }

        }
        return result;
    }

    public void eat() {
//        System.out.println("eating");
        if (MapHandlerAdvanced.eatFromSquare(xPos, yPos) && hunger == 0) {
            MapHandlerAdvanced.decreasePlantHealth(xPos, yPos, 10);
            this.energy += 10;
            if (this.energy >= 100) {
                this.energy = 100;
            }

//            reduceHunger(3);
        }
        MapHandlerAdvanced.decreasePlantHealth(xPos, yPos, 50);
//        System.out.println("Food left: " + MapHandlerAdvanced.getPlantHealth(xPos, yPos));

    }

    public void reduceHunger(int amount) {
        if (hunger - amount > 0) {
            hunger -= amount;
        } else hunger = 0;
    }

//    public void useBrain() {
//        System.out.println(hunger);
//        if (wayPoints.isEmpty()) {
//            if (hunger > 1) {
//
//                if (MapHandlerAdvanced.getValue(xPos, yPos) > 0) {
//                    eat();
//
//                } else {
//                    makeItDijkstra(MapHandlerAdvanced.ColorCode.GREEN);
//
//                }
//
//            }
//        }
//        checkForWayPoints();
//        System.out.println("Number of waypoints: " + wayPoints.size());
//    }


//    public void drink() {
//        if (standingNextToWater(xPos, yPos)) {
//            reduceThirst(1);
//        }
//
//    }

    public void reduceThirst(int amount) {
        if (thirst - amount > 0) {
            thirst -= amount;
        } else thirst = 0;
    }

//    public boolean standingNextToWater(int x, int y) {
//        boolean water = false;
//        for (int w = -1; w < 2; w++) {
//            for (int h = -1; h < 2; h++) {
//                if (MapHandlerAdvanced.getValue(x + w, y + h) == MapHandlerAdvanced.ColorCode.BLUE.getValue()) {
//                    water = true;
//
//                }
//            }
//
//        }
//        return water;
//    }

    public void move(int x, int y) {
        //TODO make this first check if mapSquare valid, then make sure no other animals there
        xPos += x;
        yPos += y;
        moved = true;
//        System.out.println("moving!");
    }


    public void sleep() {

    }


    public String report() {

        return "Animal [id = " + this.id + ", Energy = " + this.energy + ", Dead = " + this.dead+ "]";
    }

    public void useBrain() {

        if (wayPoints.isEmpty()) {

            if (hunger > 1) {

                if (MapHandlerAdvanced.getPlantHealth(xPos, yPos) > 0) {
                    eat();

                } else if (!stuck) {
                    findFoodOrWater("food", 20);

                }

            }
        }
        checkForWayPoints();
//        System.out.println("Number of waypoints: " + wayPoints.size());
    }

    @Override
    public void run() {
//        System.out.println("running");
        if (!Preserve.isNight()) {
//        System.out.println("animal id:" + id);
            useBrain();

        }
        hunger++;
        thirst++;
        energy--;
//        System.out.println(id + " " + energy);
        Preserve.latch.countDown();

    }


    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public void dailyCheckUp() {
        //TODO check for death, weight gain and everything else here
        age++;
        checkForAgeGroup();
        dailyDeathLottery();

    }

    private void checkForAgeGroup(){
        if (age > ageGroups[ageGroupNumerical].getEndAge()){
            if (ageGroupNumerical < 2){
                ageGroupNumerical++;
            } else {
                dead = true;
            }

        }
    }

    private void dailyDeathLottery(){
        System.out.println("Dying?");
        Random random = new Random();
        double ticket = random.nextDouble();
        double chanceOfDeath = ageGroups[ageGroupNumerical].getChanceOfDeath();
        System.out.println("chance of death: " +chanceOfDeath + " ticket number: " +ticket);
        if (ticket<chanceOfDeath){
            dead = true;
        }
    }


    public AgeGroup[] getAgeGroups() {
        return ageGroups;
    }

    public void setAgeGroups(AgeGroup[] ageGroups) {
        this.ageGroups = ageGroups;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLineOfSight() {
        return lineOfSight;
    }

    public void setLineOfSight(int lineOfSight) {
        this.lineOfSight = lineOfSight;
    }

    public int getAgeGroupNumerical() {
        return ageGroupNumerical;
    }

    public void setAgeGroupNumerical(int ageGroupNumerical) {
        this.ageGroupNumerical = ageGroupNumerical;
    }


    public String getBirthDayString() {
        return birthDayString;
    }

    public void setBirthDayString(String birthDayString) {
        this.birthDayString = birthDayString;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", ageGroup=" + ageGroups[ageGroupNumerical].getName() +
                ", name='" + name + '\'' +
                '}';
    }
}
