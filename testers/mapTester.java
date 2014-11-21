package testers;

import model.MapHandler;

/**
 * Created by extradikke on 19-11-14.
 */
public class mapTester {

    public static void main(String[] args) {
        MapHandler mapHandler = new MapHandler();
        mapHandler.increaseFoodValue(10, 10);
        for (int i = 0; i < mapHandler.getWidth(); i++) {
        for (int j = 0; j < mapHandler.getHeight(); j++) {


                System.out.print(mapHandler.getValue(i, j));
            }
            System.out.println();
        }

        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}
