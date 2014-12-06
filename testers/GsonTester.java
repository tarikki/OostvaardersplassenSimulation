package testers;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import mapUtils.PlantsCoverage;
import mapUtils.Terrain;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by extradikke on 06/12/14.
 */
public class GsonTester {

    public static void main(String[] args) {
        try {
            JsonReader reader3 = new JsonReader(new FileReader("/media/extradikke/UbuntuData/SimulationProjectData/Simulation/src/jsonTester.json"));
            JsonReader reader = new JsonReader(new FileReader("/media/extradikke/UbuntuData/SimulationProjectData/Simulation/src/terrainTypes.json"));
            JsonReader reader2 = new JsonReader(new FileReader("/media/extradikke/UbuntuData/SimulationProjectData/Simulation/src/Plants.json"));
            Gson gson = new Gson();
//            PlantsCoverage plants = gson.fromJson(reader, PlantsCoverage.class);
//            System.out.println(plants);

            Terrain terrain = gson.fromJson(reader3, Terrain.class);
            System.out.println(terrain);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
}
