package testers;

import model.MapHandler;

/**
 * Created by extradikke on 19-11-14.
 */
public class mapTester {

    public static void main(String[] args) {
        MapHandler mapHandler = new MapHandler();
        mapHandler.increaseFoodValue(10, 10);
        for (int j = 0; j < mapHandler.getHeight(); j++) {
            for (int i = 0; i < mapHandler.getWidth(); i++) {

                System.out.print(mapHandler.getValue(j, i));
            }
            System.out.println();
        }
    }
}
