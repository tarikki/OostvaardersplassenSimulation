package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;


/**
 * Created by extradikke on 6-12-14.
 */
public class JsonWriter {
        /// TODO add methods for plants, terrain and animals
    public static void writeSimulationConfig(SimulationConfig simulationConfig, String filepath)
    {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            FileWriter writer = new FileWriter(filepath);


                    writer.write(gson.toJson(simulationConfig));



            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
