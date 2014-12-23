package util;

import mapUtils.MapHandlerAdvanced;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;

/**
 * Created by extradikke on 20.12.14.
 */
public class MakeItDijkstra {

    private static HashMap<DijkstraNode, DijkstraNode> borderNodes;
    private static HashMap<DijkstraNode, DijkstraNode> bufferNodes;
    private static HashMap<DijkstraNode, DijkstraNode> innerNodes;
    private static Deque<DijkstraNode> wayPoints = new ArrayDeque<DijkstraNode>();
    private static HashMap<DijkstraNode, DijkstraNode> allNodes;


    public static Deque<DijkstraNode> findFoodOrWater(int animalX, int animalY, int lineOfSight, String lookingFor, int threshHold) {


        borderNodes = new HashMap<>();
        bufferNodes = new HashMap<>();
        innerNodes = new HashMap<>();
        allNodes = new HashMap<>();


        DijkstraNode homeNode = new DijkstraNode(0, 0, 0, 0, 0);
        borderNodes.put(homeNode, homeNode);

        boolean foundIt = false;
        boolean loopingDone = false;
        DijkstraNode winnerNode = homeNode;

        while (!loopingDone) {
            innerNodes.putAll(bufferNodes);
            bufferNodes.clear();

            for (DijkstraNode borderNode : borderNodes.values()) {
                if (lookingFor.equals("food")) {
                    int currentFoodValue = MapHandlerAdvanced.getPlantHealth(animalX + borderNode.getXDirection(), animalY + borderNode.getYDirection());
                    if (currentFoodValue >= threshHold && !foundIt) {
                        foundIt = true;
                        winnerNode = borderNode;
                        loopingDone = true;
                    }
                }

                if (lookingFor.equals("water")) {
                    int terrainID = MapHandlerAdvanced.getTerrainID(animalX + borderNode.getXDirection(), animalY + borderNode.getYDirection());
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
                for (int y = -1; y < 2; y++) {
                    for (int x = -1; x < 2; x++) {                   // check all squares surrounding this square
                        int terrainID = MapHandlerAdvanced.getTerrainID(bufferNode.getCurrentX() + x, bufferNode.getCurrentY() + y);
                        DijkstraNode newNode;
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
                            if (currentXPos > lineOfSight || currentYPos > lineOfSight ){
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


        }

        if (foundIt) {
            allNodes.putAll(innerNodes);
            stackDijkstra(winnerNode);
        }

        return wayPoints;

    }

    private static boolean stackDijkstra(DijkstraNode targetAcquired) {

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

    public static boolean findFoodOrWaterModel() { // lookingfor is the color of the item we're looking for
        //TODO make this private once testing done
        //TODO still error in algorithm, animals don't take shortest path
/*
        movementCostArray = new DijkstraNode[lineOfSight][lineOfSight];
        movementCostArray[lineOfSight / 2][lineOfSight / 2] = new DijkstraNode(0, 0, 0); // centre the costArray on the animal
        boolean foundIt = false;
        boolean loopingDone = false;
//        System.out.println(lineOfSight / 2);
        int scanRadius = 0;
        while (!loopingDone) { // loop until item found or field of vision scanned
            scanRadius++;
//            System.out.println("scanradius" + scanRadius);
            for (int h = 0; h < (scanRadius * 2) + 1; h++) {              // every time scan radius increases, double that so we get both sides
                for (int w = 0; w < (scanRadius * 2) + 1; w++) {

                    int currentX = (lineOfSight / 2) - scanRadius + w;  // coordinates for square being scanned
                    int currentY = (lineOfSight / 2) - scanRadius + h;
                    int lowestCost = 1000;                               // initialize it with an impossible cost
                    int closestX = 0;                                   // coordinates for the closest scanned square with the lowest cost
                    int closestY = 0;
//                    System.out.println("Cx: " + currentX + ",  Cy" + currentY);
                    if (movementCostArray[currentX][currentY] == null) { //&&        // TODO should values be recalculated at some conditions?
                        //MapHandler.isValidMove(xPos - scanRadius + w, yPos - scanRadius + h)) { // if not scanned and is valid square
                        for (int j = -1; j < 2; j++) {                   // check all squares surrounding this square
                            for (int i = -1; i < 2; i++) {

                                if (currentX + i >= 0 && currentX + i < movementCostArray.length &&
                                        currentY + j >= 0 && currentY + j < movementCostArray.length)                       // make sure we remain within array bounds
                                    if (movementCostArray[currentX + i][currentY + j] != null) {                            // if there's a dijkstraNode, check it's cost
                                        if (movementCostArray[currentX + i][currentY + j].getCost() < lowestCost) {         // if cost lower than previous lowest cost
                                            lowestCost = movementCostArray[currentX + i][currentY + j].getCost() + 1;       // make this the lowest cost node and add 1 for the node we're in
                                            closestX = currentX + i;                                                            // save it's coordinates in respect to the center of movementcostarray
                                            closestY = currentY + j;
                                        }
                                    }
                            }
                        }
                        movementCostArray[currentX][currentY] = new DijkstraNode(closestX, closestY, lowestCost);                   // make a new dijkstraNode in this place with the optimum cost
//                        System.out.println("Scanning at x " + (xPos - scanRadius + w) + " and y " + (yPos - scanRadius + h));
                        if (MapHandlerAdvanced.getValue(xPos - scanRadius + w, yPos - scanRadius + h) == lookingFor.getValue() && !foundIt) {           // is this the value we're looking for?
                            foundIt = loopingDone = true;                                                                           // we found it! stop looping!
                            System.out.println("Food found at x " + (xPos - scanRadius + w) + " and y " + (yPos - scanRadius + h));
                            stackDijkstra(movementCostArray[currentX][currentY], currentX, currentY);                               // this is the winning node, put it on the bottom of the dikstraStack
//                            dijkstraPrinter();
                        }
                    }
                }
            }
            if (scanRadius >= lineOfSight / 2) {            // lineOfSight scanned?
                loopingDone = true;                         // stop and admit defeat
            }
        }
        for (DijkstraNode wayPoint : wayPoints) {
            System.out.println(wayPoint);
        }

        // empty the array, otherwise takes an incredible amount of memory
        movementCostArray = new DijkstraNode[0][0];
        */
        return true;
    }
}
