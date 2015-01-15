package mapUtils;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import model.Preserve;
import util.Config;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.zip.DataFormatException;

/**
 * Created by extradikke on 19-11-14.
 */
public class MapHandlerAdvanced {
    private String terrainMapLocation;
    private String plantsLocation;
    private String terrainsLocation;
    private String displayableMapLocation;
    private String weatherFileLocation;

    private static int height;
    private static int width;
    private static int displayableImageHeight;
    private static int displayableImageWidth;
    private static int[][] map;
    private static Terrains terrains;
    private static HashMap<Integer, Terrain> terrainHash = new HashMap<>();
    private Plants plants;
    private static HashMap<Integer, Plant> plantsHash = new HashMap<>();
    private Months months;
    private static HashMap<Integer, MonthlyWeather> weatherHashMap = new HashMap<>();
    private int brokenPixels = 0;
    private static BufferedImage terrainImage;
    private static BufferedImage displayableImage;

    public final static int border = 0;
    public final static int water = 1;
    public final static int shallowWater = 2;
    //private int scale = 10;

    // Init block
    {
        System.out.println("Ekana?");
        starters();

    }

    /**
     * This method is just a wrapper for all the others that should be run in the init block
     */
    public void starters() {
        setPaths();
        loadImages();
        map = new int[width][height];
        loadJsons();
        jsonToHash();
        scanImage();


    }

    /**
     * Copy necessary file paths from config
     */
    public void setPaths() {
        terrainMapLocation = Config.getTerrainMapPath();
        plantsLocation = Config.getPlantsPath();
        terrainsLocation = Config.getTerrainsPath();
        displayableMapLocation = Config.getDisplayableMapPath();
        weatherFileLocation = Config.getWeatherFilePath();
    }

    /**
     * Load the terrain map and the displayable map
     */
    public void loadImages() {
        try {
            terrainImage = ImageIO.read(new File(terrainMapLocation));
            height = terrainImage.getHeight();
            width = terrainImage.getWidth();
            System.out.println("height: " + height + " width: " + width);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            displayableImage = ImageIO.read(new File(displayableMapLocation));
            displayableImageHeight = displayableImage.getHeight();
            displayableImageWidth = displayableImage.getWidth();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Scan the terrain map and store it into an int map according to a certain encoding
     */
    private void scanImage() {
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {


                int pixel = terrainImage.getRGB(w, h);
                int id = recognizeTerrain(pixel);
                if (id == -1) {
                    brokenPixels++;
//                    System.out.println(w + " " + h);
                    id = getTerrainID(w - 1, h); // fixing pixels with wrong color
                }
//                System.out.println(w + " " + h);
                map[w][h] = encodeMap(id);
//                System.out.print(getPlantId(w,h));
            }
//            System.out.println();
        }
        System.out.println("Broken pixels: " + brokenPixels);
    }

    /**
     * load terrain types, plant types and monthly weather types from jsons
     */

    private void loadJsons() {
        //TODO move these to config, they don't belong here
        try {
            JsonReader readerTerrains = new JsonReader(new FileReader(terrainsLocation));
            JsonReader readerPlants = new JsonReader(new FileReader(plantsLocation));
            JsonReader readerMonths = new JsonReader(new FileReader(weatherFileLocation));

            Gson gson = new Gson();
            terrains = gson.fromJson(readerTerrains, Terrains.class);
            plants = gson.fromJson(readerPlants, Plants.class);
            months = gson.fromJson(readerMonths, Months.class);


            try {
                terrains.verifyRanges();
                plants.verifyRanges();
                months.verifyRanges();
            } catch (IllegalAccessException | NoSuchFieldException | DataFormatException e) {
                e.printStackTrace();
            }
//            System.out.println(plants);
//            System.out.println(terrains);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Convert the plant, terrain and weather type lists into hashmaps
     */

    private void jsonToHash() {
        for (Plant plant : plants.getPlants()) {
            plantsHash.put(plant.getId(), plant);
        }

        for (Terrain terrain : terrains.getTerrains()) {
            terrainHash.put(terrain.getId(), terrain);
        }

        for (MonthlyWeather month : months.getMonthlyWeathers()) {
            weatherHashMap.put(month.getMonth(), month);
        }
    }

    /**
     *
     * @param terrainID the type of terrain in question
     * @return  int that has been bitshifted to hold information on
     *          the type of terrain, the type of plant and the health of the plant
     */

    private int encodeMap(int terrainID) {
        int result = 0;
        if (terrainID != -1) {
            int plantInt = 0;
            int plantHealthInt = 0;
            int terrainInt = terrainID << Config.terrainIdPostion;
//        System.out.println(terrainID);
            int plantId = terrainHash.get(terrainID).returnRandomPlant();
//        System.out.println(plantName);
            if (plantId != 0) {
                Plant plant = plantsHash.get(plantId);
//            System.out.println(plant.getId());
                plantInt = plant.getId() << Config.plantIdPosition;
                plantHealthInt = plant.getMaxHealth();
            }
            result = terrainInt | plantInt | plantHealthInt;
        } else brokenPixels++;
//        System.out.println("ph " + Integer.toBinaryString(plantHealthInt));
//        System.out.println("pi " + Integer.toBinaryString(plantInt));
//        System.out.println("ti " + Integer.toBinaryString(terrainInt));
//        System.out.println("ti " + Integer.toBinaryString(result));
        return result;
    }


    /**
     * Decrease the plant health, usually as a result of an animal eating it
     *
     * @param  x the x position on the map
     * @param  y the y position on the map
     * @param  amountEaten how much should be reduced from the plant's health
     * @return return the actual amount of plant consumed
     */
    public static int decreasePlantHealth(int x, int y, int amountEaten) {
        int plantId = getPlantId(x,y);
        int plantInt = plantId<< Config.plantIdPosition;
        int plantHealthInt = getPlantHealth(x, y);
        int terrainInt = getTerrainID(x, y) << Config.terrainIdPostion;
        int plantRecoveryTime = getPlantRecoveryDays(x, y);
        int recoveryTime = plantRecoveryTime + 3;
        int maxRecoveryTime = (int) Math.pow(2, Config.plantRecoveryBits) - 1;
        Plant plant = plantsHash.get(plantId);
//        System.out.println(maxRecoveryTime);

        if (recoveryTime > maxRecoveryTime) {
            recoveryTime = maxRecoveryTime;
        }

        int recoveryInt = recoveryTime << Config.plantRecoveryPosition;


        int newHealth = plantHealthInt - amountEaten;
        if (newHealth < plant.getInediblePart()) {
            newHealth = plant.getInediblePart();
            amountEaten = plantHealthInt -20;
        }
//        System.out.println(newHealth);


        int encoded = terrainInt | plantInt | recoveryInt | newHealth;
        map[x][y] = encoded;
        return amountEaten;
    }

    /**
     * Increase the health of the plant at this position as a result of natural growth
     * @param x the x position
     * @param y the y position
     */
    public static void increasePlantHealth(int x, int y) {

        int plantId = getPlantId(x, y);
        if (plantId != 0) {
            int plantRecoveryTime = getPlantRecoveryDays(x, y);
            int plantHealth = getPlantHealth(x, y);
            int plantInt = plantId << Config.plantIdPosition;
            int terrainInt = getTerrainID(x, y) << Config.terrainIdPostion;

            Plant plant = plantsHash.get(plantId);
//        System.out.println(plant);


            int growth = 0;
            float currentGrowthRate = 0;
            if (plantRecoveryTime == 0) {
                currentGrowthRate = plant.getGrowthRate();
            } else {
                currentGrowthRate = plant.getGrowthWhenDamaged();
            }

            if (Preserve.getCurrentTemperature() < plant.getTemperatureThreshold()) {
                growth = (int) Math.ceil(plantHealth * currentGrowthRate * Preserve.getCurrentDayLength() / Preserve.getMaxLengthOfDay());
            }


            int newHealth = plantHealth + growth;
            int maxHealth = plant.getMaxHealth();
            if (newHealth > maxHealth) newHealth = maxHealth;

            int encoded = terrainInt | plantInt | newHealth;
            map[x][y] = encoded;
        }
    }

    public static int getTerrainID(int x, int y) {
        return (map[x][y] >>> Config.terrainIdPostion) & 0xff;
    }

    public static int getPlantHealth(int x, int y) {
        return map[x][y] & 0x7f;
    }

    public static int getPlantId(int x, int y) {
        return (map[x][y] >>> Config.plantIdPosition) & 0x1f;
    }

    public static int getPlantRecoveryDays(int x, int y) {
        return (map[x][y] >>> Config.plantRecoveryPosition) & 7;
    }

    /**
     * go through the map and make all existing plants grow
     */
    public static void growAllPlants() {
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                increasePlantHealth(w, h);
            }
        }
    }

    /**
     *
     * @param pixel int value from the scanned image
     * @return  terrain id value between 0 and 31
     *          or return -1 if pixel not recognized
     */
    private int recognizeTerrain(int pixel) {
        int id = -1;
//        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
//        System.out.println(red + " " + green + " " + blue);
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


    public static BufferedImage getTerrainImage() {
        return terrainImage;

    }

    /**
     *
     * @param   x the x position on the map
     * @param   y the y position on the map
     * @return  boolean whether the terrain on that location is traversable
     */
    public static boolean isValidMove(int x, int y) {
        int terrainID = getTerrainID(x, y);
        Terrain terrain = terrainHash.get(terrainID);
        return terrain.isTraversable();
    }

    public static BufferedImage getDisplayableImage() {
        return displayableImage;
    }

    public static int getDisplayableImageHeight() {
        return displayableImageHeight;
    }

    public static int getDisplayableImageWidth() {
        return displayableImageWidth;
    }

    public static HashMap<Integer, MonthlyWeather> getWeatherHashMap() {
        return weatherHashMap;
    }
}
