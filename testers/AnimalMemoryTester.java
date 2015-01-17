package testers;

import animalUtils.AnimalMemory;
import mapUtils.MapHandlerAdvanced;
import util.IOUtil;

/**
 * Created by extradikke on 17/01/15.
 */
public class AnimalMemoryTester {
    public static void main(String[] args) {
        IOUtil.loadConfig();
        MapHandlerAdvanced mapHandler = new MapHandlerAdvanced(); /// Create map
        AnimalMemory animalMemory = new AnimalMemory(50);
        animalMemory.findClosestUnexploredMemSquare(299, 455);
    }
}
