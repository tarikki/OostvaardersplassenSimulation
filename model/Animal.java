package model;

import util.DijkstraNode;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by extradikke on 20-11-14.
 */
public class Animal implements Runnable {
    private int id;
    private int sleepiness = 0;
    private int hunger = 0;
    private int thirst = 0;
    private int energy;
    private boolean living = true;
    private boolean tired = false;
    private int lineOfSight = 139; //how far the animal can see around it
    private byte[][] fieldOfVision = new byte[lineOfSight + 2][lineOfSight + 2];
    private DijkstraNode[][] movementCostArray = new DijkstraNode[lineOfSight][lineOfSight]; // a temp array for calculating the optimal path to a desired location
    private int xPos;
    private int yPos;
    private Deque<DijkstraNode> wayPoints = new ArrayDeque<DijkstraNode>(); // the path expressed in DjikstraNodes


    public Animal(int id, int x, int y) {
        this.id = id;
        xPos = x;
        yPos = y;
    }

    public int getId() {
        return id;
    }

    public void scanSurroundings() {
        for (int w = 0; w < lineOfSight; w++) {
            for (int h = 0; h < lineOfSight; h++) {
                fieldOfVision[w][h] = MapHandler.getValue(xPos - lineOfSight / 2 + w, yPos - lineOfSight / 2 + h);
//                System.out.println(xPos-lineOfSight/2+w);
                System.out.print(fieldOfVision[w][h]);

            }
            System.out.println();
        }
    }

    public void FindClosestTarget(){

    }

    /**
     * a method for finding the way to a color code the animal might want to go to
     * so to find a way to some food would look like this animal.makeItDijkstra(MapHandler.ColorCode.GREEN);
     *
     * @param lookingFor the color code in the map that we want to find
     * @return foundIt
     */
    public boolean makeItDijkstra(MapHandler.ColorCode lookingFor) { // looking for is the color of the item we're looking for
        //TODO make this private once testing done
        //TODO still error in algorithm, animals don't take shortest path

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

                                if (currentX + i >= 0 && currentX  + i < movementCostArray.length &&
                                        currentY  + j >= 0 && currentY + j < movementCostArray.length)                       // make sure we remain within array bounds
                                    if (movementCostArray[currentX  + i][currentY + j] != null) {                            // if there's a dijkstraNode, check it's cost
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
                        if (MapHandler.getValue(xPos - scanRadius + w, yPos - scanRadius + h) == lookingFor.getValue() && !foundIt) {           // is this the value we're looking for?
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
        return foundIt;
    }

    private void dijkstraPrinter() {
        for (int h = 0; h < movementCostArray.length; h++) {
            for (int w = 0; w < movementCostArray.length; w++) {
                if (movementCostArray[w][h] != null) {
                    System.out.print(" " + movementCostArray[w][h].getxDirection()+" " + movementCostArray[w][h].getyDirection() + " ");
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
                    if (movementCostArray[w][h].getxDirection() == -1) {
                        arrows += " U";
                    }
                    else if (movementCostArray[w][h].getxDirection() == 1) {
                        arrows += " D";
                    }
                    else  {
                        arrows += " o";
                    }

                    if (movementCostArray[w][h].getyDirection() == -1){
                        arrows += "L ";
                    }else if (movementCostArray[w][h].getyDirection() == 1){
                        arrows += "R ";
                    }else {
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


    /**
     * This method recursively retraces the path from desired spot to animal
     *
     * @param targetAcquired the DijkstraNode we're heading to
     * @param currentX       its x position in the movementCostArray
     * @param currentY       its y position in the movementCostArray
     * @return
     */
    private boolean stackDijkstra(DijkstraNode targetAcquired, int currentX, int currentY) {

        System.out.println("Cost: " + targetAcquired.getCost() + " nextX: " + targetAcquired.getxDirection() + " nextY" + targetAcquired.getyDirection());
        DijkstraNode next = movementCostArray[targetAcquired.getxDirection()][targetAcquired.getyDirection()];              // next node is where the current is pointing att
//        System.out.println("Cost: " + next.getCost() + " nextX: " + next.getxDirection() + " nextY" + next.getyDirection());

        wayPoints.add(new DijkstraNode(currentX - targetAcquired.getxDirection(),   // add the current node to the waypoint list,
                currentY - targetAcquired.getyDirection(), 1));                     // and make it point the way to the next node
        if (targetAcquired.getCost() > 1) {                                          // if we're more than one node away from the animal
            stackDijkstra(next, targetAcquired.getxDirection(),                     // process the next node recursively
                    targetAcquired.getyDirection());
        }

        return true;
    }

    public boolean checkForWayPoints() {
        boolean result = false;
        if (!wayPoints.isEmpty()) {

            DijkstraNode nextStep = wayPoints.pop();
            if (MapHandler.isValidMove(nextStep.getxDirection() + xPos, nextStep.getyDirection() + yPos)) {

                move(nextStep.getxDirection(), nextStep.getyDirection());
            } else {
                wayPoints.clear();
                System.out.println(MapHandler.getValue(nextStep.getxDirection() + xPos, nextStep.getyDirection() + xPos));
                System.out.println("waypoint cleared");
            }

        }
        return result;
    }

    public void eat() {
        if (MapHandler.eatFromSquare(xPos, yPos))
            this.energy += 10;
        reduceHunger(1);
        System.out.println("Food left: " + MapHandler.getValue(xPos, yPos));

    }

    public void reduceHunger(int amount) {
        if (hunger - amount > 0) {
            hunger -= amount;
        } else hunger = 0;
    }

    public void useBrain() {
        if (wayPoints.isEmpty()) {
            if (hunger > 1) {

                if (MapHandler.getValue(xPos, yPos) > 0) {
                    eat();

                } else {
                    makeItDijkstra(MapHandler.ColorCode.GREEN);

                }

            }
        }
        checkForWayPoints();
//        System.out.println("Number of waypoints: " + wayPoints.size());
    }

    public void useBrain2() {
        if (wayPoints.isEmpty()) {
            if (thirst > 1) {

                if (standingNextToWater(xPos, yPos)) {
                    drink();

                } else {
                    makeItDijkstra(MapHandler.ColorCode.BLUE);

                }

            }
        }
        checkForWayPoints();
//        System.out.println("Number of waypoints: " + wayPoints.size());
    }

    public void drink() {
        if (standingNextToWater(xPos, yPos)) {
            reduceThirst(1);
        }

    }

    public void reduceThirst(int amount) {
        if (thirst - amount > 0) {
            thirst -= amount;
        } else thirst = 0;
    }

    public boolean standingNextToWater(int x, int y) {
        boolean water = false;
        for (int w = -1; w < 2; w++) {
            for (int h = -1; h < 2; h++) {
                if (MapHandler.getValue(x + w, y + h) == MapHandler.ColorCode.BLUE.getValue()) {
                    water = true;

                }
            }

        }
        return water;
    }

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

    @Override
    public void run() {
        useBrain();
        hunger++;
        thirst++;

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
