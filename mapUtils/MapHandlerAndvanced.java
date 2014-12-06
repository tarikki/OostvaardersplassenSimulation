package mapUtils;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by extradikke on 19-11-14.
 */
public class MapHandlerAndvanced {
    private String mapLocation = "/testMapJSON.png";
    private String plantsLocation = "/media/extradikke/UbuntuData/SimulationProjectData/Simulation/src/Plants.json";
    private String terrainsLocation = "/media/extradikke/UbuntuData/SimulationProjectData/Simulation/src/terrainTypes.json";
    private static int height;
    private static int width;
    private static int[][] map;
    private static Terrains terrains;
    private HashMap<Integer, Terrain> terrainHash = new HashMap<>();
    private Plants plants;
    private HashMap<Integer, Plant> plantsHash = new HashMap<>();


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
//    {
//        System.out.println("Ekana?");
//        loadImage();
//        map = new int[width][height];
////        scanImage();
//
//
//    }

    public void starters() {

        loadImage();
        map = new int[width][height];
        loadJsons();
        jsontoHash();
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
                int id = recognizeTerrain(pixel);
                map[w][h] = encodeMap(id);
                System.out.print(getPlantId(w,h));
            }
            System.out.println();
        }
    }

    private void loadJsons() {
        try {
            JsonReader readerTerrains = new JsonReader(new FileReader(terrainsLocation));
            JsonReader readerPlants = new JsonReader(new FileReader(plantsLocation));

            Gson gson = new Gson();
            terrains = gson.fromJson(readerTerrains, Terrains.class);
            plants = gson.fromJson(readerPlants, Plants.class);
            System.out.println(plants);
            System.out.println(terrains);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void jsontoHash() {
        for (Plant plant : plants.getPlants()) {
            plantsHash.put(plant.getId(), plant);
        }

        for (Terrain terrain : terrains.getTerrains()) {
            terrainHash.put(terrain.getId(), terrain);
        }
    }

    private int encodeMap(int terrainID) {

        int plantInt = 0;
        int plantHealthInt = 0;
        int terrainInt = terrainID << 16;

        int plantId= terrainHash.get(terrainID).returnRandomPlant();
//        System.out.println(plantName);
        if (plantId != 0) {
            Plant plant = plantsHash.get(plantId);
//            System.out.println(plant.getId());
            plantInt = plant.getId() << 11;
            plantHealthInt = plant.getMaxHealth();
        }
        int result = terrainInt | plantInt | plantHealthInt;
//        System.out.println("ph " + Integer.toBinaryString(plantHealthInt));
//        System.out.println("pi " + Integer.toBinaryString(plantInt));
//        System.out.println("ti " + Integer.toBinaryString(terrainInt));
//        System.out.println("ti " + Integer.toBinaryString(result));
        return result;
    }

    public int getTerrain(int x, int y) {
        return (map[x][y] >>> 16) & 0xff;
    }

    public int getPlantHealth(int x, int y) {
        return map[x][y] & 0x7f;
    }

    public int getPlantId(int x, int y) {
        return (map[x][y] >>> 11) & 0x1f;
    }

    private int recognizeTerrain(int pixel) {
        int id = -1;
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        for (Terrain terrain : terrains.getTerrains()) {
            if (terrain.compareColor(red, green, blue)) {
                id = terrain.getId();
            }
        }
        return id;
    }

    public static int getHeight() {
        return height;
    }

    public static int getWidth() {
        return width;
    }

//    public synchronized static byte getValue(int x, int y) {
//        return map[x][y];
//    }

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


    public synchronized static boolean eatFromSquare(int x, int y) {
        if (map[x][y] > 0) {
            decreaseFoodValue(x, y);
            return true;
        } else return false;
    }

}
