package model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by extradikke on 19-11-14.
 */
public class MapLoader {
    private String mapLocation = "/media/extradikke/UbuntuData/SimulationProjectData/Simulation/basicmap.jpg";
    private int height;
    private int width;
    private static byte[][] map;
    private BufferedImage image;

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

    public MapLoader() {
        loadImage();
        map = new byte[width][height];
        scanImage();

    }

    private void loadImage() {
        try {
            this.image = ImageIO.read(new File(mapLocation));
            this.height = image.getHeight();
            this.width = image.getWidth();
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

    public void printPixelARGB(int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        System.out.println("argb: " + alpha + ", " + red + ", " + green + ", " + blue);
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

    public int getHeight() {
        return height;
    }



    public int getWidth() {
        return width;
    }

    public byte getValue(int x, int y){
        return map[x][y];
    }


}
