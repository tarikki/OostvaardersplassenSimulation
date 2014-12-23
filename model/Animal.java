package model;

import mapUtils.MapHandlerAdvanced;
import util.DijkstraNode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;

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
    private int lineOfSight = 100; //how far the animal can see around it
    private byte[][] fieldOfVision = new byte[lineOfSight + 2][lineOfSight + 2];
    private DijkstraNode[][] movementCostArray; // a temp array for calculating the optimal path to a desired location
    private int xPos;
    private int yPos;
    private Deque<DijkstraNode> wayPoints = new ArrayDeque<DijkstraNode>(); // the path expressed in DjikstraNodes

    private HashMap<DijkstraNode, DijkstraNode> borderNodes;
    private HashMap<DijkstraNode, DijkstraNode> bufferNodes;
    private HashMap<DijkstraNode, DijkstraNode> innerNodes;

    private static HashMap<DijkstraNode, DijkstraNode> allNodes;


    public Animal(int id, int x, int y) {
        this.id = id;
        xPos = x;
        yPos = y;
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
        allNodes = new HashMap<>();


        DijkstraNode homeNode = new DijkstraNode(xPos, yPos, 0, 0, 0);
        borderNodes.put(homeNode, homeNode);

        boolean foundIt = false;
        boolean loopingDone = false;
        DijkstraNode winnerNode = homeNode;

        while (!loopingDone) {
            innerNodes.putAll(bufferNodes);
            bufferNodes.clear();
//            System.out.println("in the loop");

            for (DijkstraNode borderNode : borderNodes.values()) {
                if (lookingFor.equals("food")) {
                    int currentFoodValue = MapHandlerAdvanced.getPlantHealth(xPos + borderNode.getXDirection(), yPos + borderNode.getYDirection());
                    if (currentFoodValue >= threshHold && !foundIt) {
                        foundIt = true;
                        winnerNode = borderNode;
                        loopingDone = true;
                        System.out.println("found food");
                    }
                }

                if (lookingFor.equals("water")) {
                    int terrainID = MapHandlerAdvanced.getTerrainID(xPos + borderNode.getXDirection(), yPos + borderNode.getYDirection());
                    if ((terrainID == MapHandlerAdvanced.water || terrainID == MapHandlerAdvanced.shallowWater) && !foundIt) {
                        foundIt = true;
                        winnerNode = borderNode;
                        loopingDone = true;
                    }
                }
            }

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

                        if (terrainID == MapHandlerAdvanced.water){
                            additionalCost = 1000;
                        }


                        if (y != 0 && x != 0 && terrainID != MapHandlerAdvanced.border) { // don't add self, don't add border
                            int currentXPos = bufferNode.getCurrentX() + x;
                            int currentYPos = bufferNode.getCurrentY() + y;

                            if (Math.abs(currentXPos-xPos) > lineOfSight || Math.abs(currentYPos - yPos) > lineOfSight ){
                                loopingDone = true;
                            }

                            newNode = new DijkstraNode(bufferNode.getCurrentX() + x, bufferNode.getCurrentY() + y, x, y,
                                    bufferNode.getCost() + additionalCost);
                            if (borderNodes.containsValue(newNode)) {
                                if (newNode.getCost() < borderNodes.get(newNode).getCost()) {
                                    borderNodes.put(newNode, newNode);
                                }
                            }
                            if (!bufferNodes.containsValue(newNode) && !innerNodes.containsValue(newNode)) { // don't add duplicates
                                borderNodes.put(newNode, newNode);
                            }

                        }

                    }
                }
            }
            System.out.println("borderNodes: " + borderNodes.size());


        }

        if (foundIt) {
            allNodes.putAll(innerNodes);
            stackDijkstra(winnerNode);
        }



    }

    private boolean stackDijkstra(DijkstraNode targetAcquired) {

        System.out.println("Cost: " + targetAcquired.getCost() + " nextX: " + targetAcquired.getXDirection() + " nextY" + targetAcquired.getYDirection());
        DijkstraNode next = allNodes.get(new DijkstraNode(targetAcquired.getCurrentX(), targetAcquired.getCurrentY(),
                targetAcquired.getCurrentX() + targetAcquired.getXDirection(), targetAcquired.getCurrentY() + targetAcquired.getYDirection(), 1));            // next node is where the current is pointing att
//        System.out.println("Cost: " + next.getCost() + " nextX: " + next.getXDirection() + " nextY" + next.getYDirection());

        wayPoints.add(new DijkstraNode(targetAcquired.getCurrentX(), targetAcquired.getCurrentY(), -targetAcquired.getXDirection(), -targetAcquired.getYDirection(), 1));                     // and make it point the way to the next node
        if (targetAcquired.getCost() > 1.2) {                                          // if we're more than one node away from the animal
            stackDijkstra(next);
        }

        return true;
    }



    public int getId() {
        return id;
    }

/*    public void scanSurroundings() {
        for (int w = 0; w < lineOfSight; w++) {
            for (int h = 0; h < lineOfSight; h++) {
                fieldOfVision[w][h] = MapHandlerAdvanced.getValue(xPos - lineOfSight / 2 + w, yPos - lineOfSight / 2 + h);
//                System.out.println(xPos-lineOfSight/2+w);
                System.out.print(fieldOfVision[w][h]);

            }
            System.out.println();
        }
    }*/




    private void dijkstraPrinter() {
        for (int h = 0; h < movementCostArray.length; h++) {
            for (int w = 0; w < movementCostArray.length; w++) {
                if (movementCostArray[w][h] != null) {
                    System.out.print(" " + movementCostArray[w][h].getXDirection() + " " + movementCostArray[w][h].getYDirection() + " ");
                } else {
                    System.out.print(" X X ");
                }

            }
            System.out.println();
        }
    }

    private void dijkstraDirectionPrinter() {
        for (int h = 0; h < movementCostArray.length; h++) {
            for (int w = 0; w < movementCostArray.length; w++) {
                String arrows = "";
                if (movementCostArray[w][h] != null) {
                    if (movementCostArray[w][h].getXDirection() == -1) {
                        arrows += " U";
                    } else if (movementCostArray[w][h].getXDirection() == 1) {
                        arrows += " D";
                    } else {
                        arrows += " o";
                    }

                    if (movementCostArray[w][h].getYDirection() == -1) {
                        arrows += "L ";
                    } else if (movementCostArray[w][h].getYDirection() == 1) {
                        arrows += "R ";
                    } else {
                        arrows += "o ";
                    }

                    System.out.print(arrows);
                } else {
                    System.out.print(" XX ");
                }

            }
            System.out.println();
        }
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

            DijkstraNode nextStep = wayPoints.pop();
            if (MapHandlerAdvanced.isValidMove(nextStep.getXDirection() + xPos, nextStep.getYDirection() + yPos)) {

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
            MapHandlerAdvanced.decreasePlantHealth(xPos,yPos,50);
            this.energy += 10;
            if (this.energy >= 100){
                this.energy = 100;
            }

//            reduceHunger(3);
        }
        MapHandlerAdvanced.decreasePlantHealth(xPos,yPos,50);
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

    /*public void useBrain2() {
        if (wayPoints.isEmpty()) {
            if (thirst > 1) {

                if (standingNextToWater(xPos, yPos)) {
                    drink();

                } else {
                    makeItDijkstra(MapHandlerAdvanced.ColorCode.BLUE);

                }

            }
        }
        checkForWayPoints();
//        System.out.println("Number of waypoints: " + wayPoints.size());
    }*/

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
    }


    public void sleep() {

    }


    public String report() {

        return "Animal [id = " + this.id + ", Energy = " + this.energy + ", Living = " + this.living + "]";
    }

    public void useBrain() {
        if (wayPoints.isEmpty()) {
            if (hunger > 1) {

                if (MapHandlerAdvanced.getPlantHealth(xPos,yPos) > 0) {
                    eat();

                } else {
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
        useBrain();
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
}
