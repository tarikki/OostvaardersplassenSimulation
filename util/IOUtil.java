package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;

import java.io.*;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by extradikke on 7-11-14.
 */
public class IOUtil {

    private static String CONFIG_FILE_NAME = "config.json";
    public static String DEFAULT_CONFIG_PATH;
    private static String CONFIG_DIRECTORY;
    private static String WORKING_DIRECTORY;
    private static String DEFAULT_DIRECTORY_NAME = "config";

    public static ConfigLoader configLoader;

    public static DateTimeParser[] parsers = {
            DateTimeFormat.forPattern("dd-MM-yyyy").getParser(),
            DateTimeFormat.forPattern("dd/MM/yyyy").getParser(),
            DateTimeFormat.forPattern("dd.MM.yyyy").getParser()};
    public static DateTimeFormatter formatter = new DateTimeFormatterBuilder().append(null, parsers).toFormatter();



    static {
        WORKING_DIRECTORY = getWD();
        System.out.println(WORKING_DIRECTORY);
        CONFIG_DIRECTORY= WORKING_DIRECTORY+DEFAULT_DIRECTORY_NAME+osPathCorrection();
        System.out.println(CONFIG_DIRECTORY);
        DEFAULT_CONFIG_PATH = CONFIG_DIRECTORY + CONFIG_FILE_NAME;
        System.out.println(DEFAULT_CONFIG_PATH);


    }


    public static void loadConfig() {
        //TODO make sure ending date after start
        try {
            JsonReader configReader = new JsonReader(new FileReader(DEFAULT_CONFIG_PATH));
            Gson gson = new Gson();
            configLoader = gson.fromJson(configReader, ConfigLoader.class);

            Config.setTerrainMapPath(CONFIG_DIRECTORY+configLoader.getTerrainMapName());
            Config.setPlantsPath(CONFIG_DIRECTORY+configLoader.getPlantsName());
            Config.setTerrainsPath(CONFIG_DIRECTORY+configLoader.getTerrainsName());
            Config.setDisplayableMapPath(CONFIG_DIRECTORY+configLoader.getDisplayableMapName());
            Config.setAnimalsFilePath(CONFIG_DIRECTORY+configLoader.getAnimalsFileName());
            Config.setWeatherFilePath(CONFIG_DIRECTORY+configLoader.getWeatherFileName());
            Config.setPopulationsFilePath(CONFIG_DIRECTORY+configLoader.getPopulationsFileName());

            Config.setNumberOfAnimals(configLoader.getNumberOfAnimals());
            Config.setSpeedOfSimulation(configLoader.getSpeedOfSimulation());

            Config.setScale(configLoader.getScale());
            Config.setLatitude(configLoader.getLatitude());
            Config.setDateTimeZone(configLoader.getDateTimeZone());

            Config.setStartingDate(formatter.parseDateTime(configLoader.getStartingDate()));
            Config.setEndingDate(formatter.parseDateTime(configLoader.getEndingDate()));








        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void saveConfig(){
        try {

            configLoader.setNumberOfAnimals(Config.getNumberOfAnimals());
            configLoader.setSpeedOfSimulation(Config.getSpeedOfSimulation());
            configLoader.setStartingDate(Config.getStartingDate().toString("dd/MM/yyyy"));
            configLoader.setEndingDate(Config.getEndingDate().toString("dd/MM/yyyy"));

            System.out.println(configLoader.getStartingDate());

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter(DEFAULT_CONFIG_PATH);


            writer.write(gson.toJson(configLoader));



            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getWD() {
        Pattern opSysPattern = Pattern.compile("\\w+");
        String fullPath = "";
        String path = Paths.get(".").toAbsolutePath().normalize().toString();
        String opSys = System.getProperty("os.name").toLowerCase();

        Matcher matcher = opSysPattern.matcher(opSys);
        if (matcher.find()) {

            opSys = matcher.group();
            if (opSys.equals("linux")) {
//                System.out.println("linux");
                fullPath = path + "/";
            }
            if (opSys.equals("windows")) {
                fullPath = path + "\\";
            }
//            System.out.println(fullPath);
        }
        return fullPath;
    }

    public static String osPathCorrection() {
        Pattern opSysPattern = Pattern.compile("\\w+");
        String opSys = System.getProperty("os.name").toLowerCase();
        Matcher matcher = opSysPattern.matcher(opSys);
        String result = "";
        if (matcher.find()) {

            opSys = matcher.group();
            if (opSys.equals("linux")) {
//                System.out.println("linux");
                result = "/";
            }
            if (opSys.equals("windows")) {
                result = "\\";
            }

        }
        return result;


    }

    public boolean checkForConfig() {
        String path = getWD();
        File file = new File(path + "config.txt");
        boolean fileExists = file.exists();
        if (fileExists) {
//            System.out.println("yay");
        } else {
//            System.out.println("nay");
        }

        return fileExists;
    }





    public void createConfig() throws FileNotFoundException, UnsupportedEncodingException {
        if (!checkForConfig()) {

        }

    }



    public void readConfig() throws IOException {


    }


}
