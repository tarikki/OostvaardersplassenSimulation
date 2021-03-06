package testers;

import mapUtils.MapHandlerAdvanced;
import model.Preserve;
import util.Config;
import util.IOUtil;

/**
 * Created by extradikke on 15/01/15.
 */
public class PreserveTempTest {
    public static void main(String[] args) {
        IOUtil.loadConfig();
        MapHandlerAdvanced mapHandler = new MapHandlerAdvanced();
        Preserve.setupPreserve(Config.getLatitude(), Config.getStartingDate(), Config.getEndingDate(), Config.getAnimalsToShoot(), Config.getShootingDay()); /// Create preserve with X amount of animals
        for (int minutes = 0; minutes < Config.lengthOfDayInMinutes; minutes++) {


        try {
            Preserve.executeTurn();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }}
    }
}
