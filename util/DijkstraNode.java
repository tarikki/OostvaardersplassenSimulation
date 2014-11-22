package util;

/**
 * Created by extradikke on 22-11-14.
 */
public class DijkstraNode {
    private int xDirection;
    private int yDirection;
    private int cost;


    public DijkstraNode(int xDirection, int yDirection, int cost) {
        this.xDirection = xDirection;
        this.yDirection = yDirection;
        this.cost = cost;
    }

    public int getxDirection() {
        return xDirection;
    }

    public int getyDirection() {
        return yDirection;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "DijkstraNode{" +
                "xDirection=" + xDirection +
                ", yDirection=" + yDirection +
                ", cost=" + cost +
                '}';
    }
}
