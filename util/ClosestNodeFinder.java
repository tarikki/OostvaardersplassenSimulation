package util;

import java.util.HashMap;

/**
 * Created by extradikke on 17/01/15.
 */
public class ClosestNodeFinder {
    
    public static DijkstraNode findClosestNode(HashMap<DijkstraNode, DijkstraNode> nodes, int targetX, int targetY){
        double smallestDistance = 100000;
        DijkstraNode closestNode = new DijkstraNode(0,0,1,1, smallestDistance);
        for (DijkstraNode node : nodes.keySet()) {
            double distance = findDistance(node.getCurrentX(), node.getCurrentY(), targetX, targetY);
            if (distance <= smallestDistance){
                smallestDistance = distance;
                closestNode = node;
            }

        }
        return closestNode;
    }

    public static double findDistance(int xPos, int yPos, int targetX, int targetY){
        return Math.sqrt(Math.pow((targetX-xPos), 2) + Math.pow((targetY-yPos), 2));
    }
}
