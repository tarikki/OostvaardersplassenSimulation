package animalUtils;

import mapUtils.MapHandlerAdvanced;

/**
 * Created by extradikke on 28.12.14.
 */
public class AnimalMemory {
    private double[][] potentialFood; // how much food the area could have
    private boolean[][] traversable;
    private double[][] percentageExplored;
    private int[][] centerX;
    private int[][] centerY;
    private int pixelsInMemorySquare;

    private int lineOfSight;
    private int halfLineOfSight;
    private int memorySquaresX;
    private int memorySquaresY;

    public AnimalMemory(int lineOfSight) {
        this.lineOfSight = lineOfSight;
        this.halfLineOfSight = lineOfSight/2;
        this.memorySquaresX = MapHandlerAdvanced.getWidth()/lineOfSight;
        this.memorySquaresY = MapHandlerAdvanced.getHeight()/lineOfSight;
        this.pixelsInMemorySquare = lineOfSight*lineOfSight;
        this.potentialFood = new double[this.memorySquaresY][this.memorySquaresX];
        this.traversable = new boolean[this.memorySquaresY][this.memorySquaresX];
        this.percentageExplored = new double[this.memorySquaresY][this.memorySquaresX];
        for (int y = halfLineOfSight; y < this.memorySquaresY-1; y+=lineOfSight) {
            for (int x = halfLineOfSight; x < this.memorySquaresX-1; x+=lineOfSight) {
                memorySquaresX = x;
                memorySquaresY = y;
            }
        }
    }

    public void scanSurroundings(int startX, int startY){
        for (int x = -halfLineOfSight; x <halfLineOfSight; x++) {
            for (int y = -halfLineOfSight; y < halfLineOfSight; y++) {
                int currentX = startX + x;
                int currentY = startY + y;
                int squareHorizontal = ((currentX-halfLineOfSight)/lineOfSight);
                int squareVertical = ((currentY-halfLineOfSight)/lineOfSight);
                if (!MapHandlerAdvanced.isValidMove(currentX, currentY)){
                    this.traversable[squareHorizontal][squareVertical] = false;
                }

            }

        }

    }
}
