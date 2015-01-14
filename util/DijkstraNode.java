package util;

import java.util.HashMap;
import java.util.List;

/**
 * Created by extradikke on 22-11-14.
 *
 * A class to represent the directions the animal can move to
 * This class is also used to build the way points the animal will take
 */
public class DijkstraNode {

    private int currentX;
    private int currentY;
    private int xDirection;
    private int yDirection;
    private double cost;


    public DijkstraNode(int currentX, int currentY, int xDirection, int yDirection, double cost) {
        this.currentX = currentX;
        this.currentY = currentY;
        this.xDirection = xDirection;
        this.yDirection = yDirection;
        this.cost = cost;
    }

    public int getXDirection() {
        return xDirection;
    }

    public int getYDirection() {
        return yDirection;
    }

    public double getCost() {
        return cost;
    }

    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DijkstraNode that = (DijkstraNode) o;

        if (currentX != that.currentX) return false;
        if (currentY != that.currentY) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = currentX;
        result = 31 * result + currentY;
        return result;
    }

    @Override
    public String toString() {
        return "DijkstraNode{" +
                "currentX=" + currentX +
                ", currentY=" + currentY +
                ", xDirection=" + xDirection +
                ", yDirection=" + yDirection +
                ", cost=" + cost +
                '}';
    }
}
