package model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by extradikke on 19-11-14.
 */
public class MapHandler {
    private String mapLocation = "/TestMapWeek1.png";
    private static int height;
    private static int width;
    private static byte[][] map;


    private static BufferedImage image;


    public enum ColorCode {
        GREEN((byte) 10),   //grass
        BLUE((byte) -1),    // water
        BROWN((byte) -2),   // swamp
        BLACK((byte) -10);  //border
        private final byte value;

        private ColorCode(byte value) {
            this.value = value;
        }

        public byte getValue() {
            return value;
        }
    }

    /// Init block
    {
        System.out.println("Ekana?");
        loadImage();
        map = new byte[width][height];
        scanImage();


    }

    public void loadImage() {
        try {
            image = ImageIO.read((this.getClass().getResourceAsStream(mapLocation)));
            height = image.getHeight();
            width = image.getWidth();
            System.out.println("height: " + height + " width: " + width);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void scanImage() {
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {


                int pixel = image.getRGB(w, h);
                String color = recognizeColorString(pixel);
                map[w][h] = recognizeColorByte(pixel);
                System.out.print(color);
            }
            System.out.println();
        }
    }


    private String recognizeColorString(int pixel) {
        String color = "0";
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        if (red < 15 && blue < 15 && green < 15) {
            color = "B";
        } else if (green > 200 && blue < 100) {
            color = "G";
        } else if (blue > 200) {
            color = "U";
        } else {
            color = "R";
        }
        return color;
    }

    private Byte recognizeColorByte(int pixel) {
        Byte color = 0;
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        if (red < 15 && blue < 15 && green < 15) {
            color = ColorCode.BLACK.getValue();
        } else if (green > 200 && blue < 100) {
            color = ColorCode.GREEN.getValue();
        } else if (blue > 200) {
            color = ColorCode.BLUE.getValue();
        } else {
            color = ColorCode.BROWN.getValue();
        }
        return color;
    }

    public static int getHeight() {
        return height;
    }

    public static int getWidth() {
        return width;
    }

    public synchronized static byte getValue(int x, int y) {
        return map[x][y];
    }

    public static BufferedImage getImage() {
        return image;

    }

    public synchronized static void increaseFoodValue(int x, int y) {
        map[x][y]++;
    }

    public synchronized static void decreaseFoodValue(int x, int y) {
        map[x][y]--;
    }

    public static boolean isValidMove(int x, int y) {
        if (map[x][y] > -1 || map[x][y] == ColorCode.BROWN.getValue()) {

            return true;
        } else {
            return false;
        }


    }



    public synchronized static boolean eatFromSquare(int x, int y){
        if (map[x][y] > 0){
            decreaseFoodValue(x, y);
            return true;
        } else return false;
    }

}
