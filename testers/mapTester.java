package testers;

import model.MapLoader;

/**
 * Created by extradikke on 19-11-14.
 */
public class mapTester {

    public static void main(String[] args) {
        MapLoader mapLoader = new MapLoader();
        for (int j = 0; j < mapLoader.getHeight(); j++) {
            for (int i = 0; i < mapLoader.getWidth(); i++) {

                System.out.print(mapLoader.getValue(j, i));
            }
            System.out.println();
        }
    }
}
