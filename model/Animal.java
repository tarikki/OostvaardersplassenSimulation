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
    private int lineOfSight = 9; //how far the animal can see around it
    private byte[][] fieldOfVision = new byte[lineOfSight + 2][lineOfSight + 2];
    private DijkstraNode[][] movementCostArray = new DijkstraNode[lineOfSight][lineOfSight];
    private int[] location = new int[2];
    private Deque<DijkstraNode> wayPoints = new ArrayDeque<DijkstraNode>();
    private int[] destination = new int[2];


    public Animal(int id, int x, int y) {
        this.id = id;
        this.location[0] = x;
        this.location[1] = y;
    }

    public int getX() {
        return location[0];
    }

    public int getY() {
        return location[1];
    }

    public void setX(int x) {
        location[0] = x;
    }

    public void setY(int y) {
        location[1] = y;
    }


    public int getId() {
        return id;
    }

    public void scanSurroundings() {
        for (int w = 0; w < lineOfSight; w++) {
            for (int h = 0; h < lineOfSight; h++) {
                fieldOfVision[w][h] = MapHandler.getValue(location[0] - lineOfSight / 2 + w, location[1] - lineOfSight / 2 + h);
//                System.out.println(location[0]-lineOfSight/2+w);
                System.out.print(fieldOfVision[w][h]);

            }
            System.out.println();
        }
    }

    public boolean makeItDijkstra(MapHandler.ColorCode lookingFor) { // looking for is the byte value of either food or drink
        //TODO add comments on this before I forget how it works
        movementCostArray[lineOfSight / 2][lineOfSight / 2] = new DijkstraNode(0, 0, 0);
        boolean foundIt = false;
        boolean loopingDone = false;
        System.out.println(lineOfSight / 2);
        int scanRadius = 0;
        while (!loopingDone) {
            scanRadius++;
            System.out.println("scanradius" + scanRadius);
            for (int h = 0; h < (scanRadius*2) + 1; h++) {
                for (int w = 0; w < (scanRadius*2) + 1; w++) {

                    int currentX = (lineOfSight / 2) - scanRadius + w;
                    int currentY = (lineOfSight / 2) - scanRadius + h;
                    int lowestCost = 100;
                    int closestX = 0;
                    int closestY = 0;
                    System.out.println("Cx: " + currentX + ",  Cy" + currentY);
                    if (movementCostArray[currentX][currentY] == null && MapHandler.isValidMove(location[0] - scanRadius + w, location[1] - scanRadius + h)) {
                        for (int j = 0; j < 3; j++) {
                            for (int i = 0; i < 3; i++) {

                                if (currentX - 1 + i >= 0 && currentX - 1 + i < movementCostArray.length && currentY - 1 + j >= 0 && currentY - 1 + j < movementCostArray.length)
                                    if (movementCostArray[currentX - 1 + i][currentY - 1 + j] != null) {
                                        if (movementCostArray[currentX - 1 + i][currentY - 1 + j].getCost() < lowestCost) {
                                            lowestCost = movementCostArray[currentX - 1 + i][currentY - 1 + j].getCost() + 1;
                                            closestX = currentX - 1 + i;
                                            closestY = currentY - 1 + j;
                                        }
                                    }
                            }
                        }
                        movementCostArray[currentX][currentY] = new DijkstraNode(closestX, closestY, lowestCost);
                        System.out.println("Scanning at x " + (location[0] - scanRadius + w) + " and y " + (location[1] - scanRadius + h));
                        if (MapHandler.getValue(location[0] - scanRadius + w, location[1] - scanRadius + h) == lookingFor.getValue()) {
                            foundIt = loopingDone = true;
                            System.out.println("Food found at x " + (location[0] - scanRadius + w) + " and y " + (location[1] - scanRadius + h));
                            stackDijkstra(movementCostArray[currentX][currentY], currentX, currentY);
                        }
                    }
                }
            }
            if (scanRadius >= lineOfSight / 2) {
                loopingDone = true;
            }
        }
        for (DijkstraNode wayPoint : wayPoints) {
            System.out.println(wayPoint);
        }
        return foundIt;
    }

    private boolean stackDijkstra(DijkstraNode targetAcquired, int currentX, int currentY) {

        System.out.println("Cost: " + targetAcquired.getCost() + " nextX: " + targetAcquired.getxDirection()+ " nextY" + targetAcquired.getyDirection());
        DijkstraNode next = movementCostArray[targetAcquired.getxDirection()][targetAcquired.getyDirection()];
        System.out.println("Cost: " + next.getCost() + " nextX: " + next.getxDirection()+ " nextY" + next.getyDirection());

        wayPoints.add(new DijkstraNode(currentX - targetAcquired.getxDirection(), currentY - targetAcquired.getyDirection(), 1));
        if (targetAcquired.getCost() > 1){
            stackDijkstra(next, targetAcquired.getxDirection(), targetAcquired.getyDirection());
        }

        return true;
    }

    public void eat() {
        this.energy += 1;

    }

    public void drink() {

    }

    public void move() {
        //TODO check if waypoints in waypoints, move if yes

    }

    public void sleep() {

    }


    public void report() {
        System.out.println("Animal [id = " + this.id + ", Energy = " + this.energy + ", Living = " + this.living + "]");
    }

    @Override
    public void run() {
        this.location[0] += 10;

    }
}
