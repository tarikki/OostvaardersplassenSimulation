package animalUtils;

import mapUtils.MapHandlerAdvanced;
import util.DijkstraNode;

/**
 * Created by extradikke on 28.12.14.
 */
public class AnimalMemory {
    private double[][] potentialFood; // how much food the area could have
    private boolean[][] traversable;
    private boolean[][] explored;
    private int[][] centerX;
    private int[][] centerY;

    private int pixelsInMemorySquare;
    private int squaresExplored = 0;

    private int lineOfSight;
    private int halfLineOfSight;
    private int memorySquaresXNumber;
    private int memorySquaresYNumber;

    public AnimalMemory(int lineOfSight) {
        this.lineOfSight = lineOfSight;
        this.halfLineOfSight = lineOfSight / 2;
        this.memorySquaresXNumber = MapHandlerAdvanced.getWidth() / lineOfSight;
        this.memorySquaresYNumber = MapHandlerAdvanced.getHeight() / lineOfSight;
        this.pixelsInMemorySquare = lineOfSight * lineOfSight;
        this.potentialFood = new double[this.memorySquaresYNumber][this.memorySquaresXNumber];
        this.traversable = new boolean[this.memorySquaresYNumber][this.memorySquaresXNumber];
        this.explored = new boolean[this.memorySquaresYNumber][this.memorySquaresXNumber];
        this.centerX = new int[this.memorySquaresXNumber][this.memorySquaresYNumber];
        this.centerY = new int[this.memorySquaresXNumber][this.memorySquaresYNumber];
        for (int y = 0; y < this.memorySquaresYNumber - 1; y++) {
            for (int x = 0; x < this.memorySquaresXNumber - 1; x++) {
                centerX[x][y] = halfLineOfSight + x * lineOfSight;
                centerY[x][y] = halfLineOfSight + y * lineOfSight;
            }
        }
    }

    public void scanSurroundings(int startX, int startY) {
        for (int x = -halfLineOfSight; x < halfLineOfSight; x++) {
            for (int y = -halfLineOfSight; y < halfLineOfSight; y++) {
                int currentX = startX + x;
                int currentY = startY + y;
                int squareHorizontal = ((currentX - halfLineOfSight) / lineOfSight);
                int squareVertical = ((currentY - halfLineOfSight) / lineOfSight);
                if (!MapHandlerAdvanced.isValidMove(currentX, currentY)) {
                    this.traversable[squareHorizontal][squareVertical] = false;
                }

            }

        }

    }

    public void findClosestUnexploredMemSquare(int currentX, int currentY) {
        boolean foundIt = false;
        int closestXCenter = Math.floorDiv(currentX, lineOfSight);
        int closestYCenter = Math.floorDiv(currentY, lineOfSight);
        System.out.println(closestXCenter);
        System.out.println(closestYCenter);
        int scanRadius = 1;
        while (!foundIt) {
            for (int i = 1; i < 3; i++) {


                for (int position = -scanRadius; position < scanRadius; i++) {
                    if (!explored[closestXCenter + (int)Math.pow(-1, i) * (int)Math.ceil(scanRadius/2)][closestYCenter + position]){
                        //TODO finish this, should it return a dijkstranode?

                    }
                }    }


//                System.out.println("x: " + centerX[closestXCenter - i][closestYCenter - j] + " y: " + centerY[closestXCenter - i][closestYCenter - j]);


        }


    }
}
