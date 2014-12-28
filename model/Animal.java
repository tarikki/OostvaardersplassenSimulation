package model;

import mapUtils.MapHandlerAdvanced;
import util.DijkstraNode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by extradikke on 20-11-14.
 */
public class Animal implements Runnable {
    private int id;
    private boolean sleepy = false;
    private int hunger = 0;
    private int thirst = 0;
    private int energy = 100;
    private boolean living = true;
    private boolean tired = false;
    private int age;
    private int lineOfSight = 50; //how far the animal can see around it
    private int xPos;
    private int yPos;
    private boolean dijkstraScanned = false;
    private boolean moved = true;
    private Deque<DijkstraNode> wayPoints = new ArrayDeque<DijkstraNode>(); // the path expressed in DjikstraNodes

    private HashMap<DijkstraNode, DijkstraNode> borderNodes;
    private HashMap<DijkstraNode, DijkstraNode> bufferNodes;
    private HashMap<DijkstraNode, DijkstraNode> innerNodes;



    public Animal(int id, int x, int y, int initialAge) {
        this.id = id;
        this.xPos = x;
        this.yPos = y;
        this.age = initialAge;

    }

    public boolean isDead() {
        if (this.energy <= 0) {
            this.living = false;
        }
        return this.living;
    }

    public void findFoodOrWater(String lookingFor, int threshHold) {


        borderNodes = new HashMap<>();
        bufferNodes = new HashMap<>();
        innerNodes = new HashMap<>();


        DijkstraNode homeNode = new DijkstraNode(xPos, yPos, 0, 0, 0);
        borderNodes.put(homeNode, homeNode);

        boolean foundIt = false;
        boolean loopingDone = false;
        DijkstraNode winnerNode = homeNode;

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
                        System.out.println("found food with cost:" + lowestCost);
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

                if (!MapHandlerAdvanced.isValidMove(borderNode.getCurrentX(), borderNode.getCurrentY())) {
                    borderNodes.remove(borderNode);
                }
            }

            // TODO implement removal of non-traversable nodes so this will not return impossible paths

            bufferNodes.putAll(borderNodes);
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

//TODO check the logic behind this
                        if ((y != 0 || x != 0) && terrainID != MapHandlerAdvanced.border) { // don't add self, don't add border
                            int currentXPos = bufferNode.getCurrentX() + x;
                            int currentYPos = bufferNode.getCurrentY() + y;

                            if (Math.abs(currentXPos - xPos) > lineOfSight || Math.abs(currentYPos - yPos) > lineOfSight) {
                                System.out.println("too far");
                                loopingDone = true;
                            }

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


            // TODO

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
            MapHandlerAdvanced.decreasePlantHealth(xPos, yPos, 50);
            this.energy += 10;
            if (this.energy >= 100) {
                this.energy = 100;
            }

//            reduceHunger(3);
        }
        MapHandlerAdvanced.decreasePlantHealth(xPos, yPos, 50);
        System.out.println("Food left: " + MapHandlerAdvanced.getPlantHealth(xPos, yPos));

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
    }


    public void sleep() {

    }


    public String report() {

        return "Animal [id = " + this.id + ", Energy = " + this.energy + ", Living = " + this.living + "]";
    }

    public void useBrain() {
//        System.out.println("waypoints size " + wayPoints.size());
        if (wayPoints.isEmpty()) {
            if (hunger > 1) {

                if (MapHandlerAdvanced.getPlantHealth(xPos, yPos) > 0) {
                    eat();

                } else if (moved) {
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
//        if (!Preserve.isNight()){
        System.out.println("animal id:" + id);
        useBrain();
//    }
        hunger++;
        thirst++;
        energy--;

    }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", xPos=" + xPos +
                ", yPos=" + yPos +
                '}';
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

    public void dailyCheckUp(){
        //TODO check for death, weight gain and everything else here
        age++;
    }


}
