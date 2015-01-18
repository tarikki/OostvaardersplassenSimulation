package testers;

import util.Config;
import util.ConfigLoader;
import util.IOUtil;

/**
 * Created by extradikke on 10/12/14.
 *
 * A test class for the IOUtil class
 */
public class ConfigTester {

    public static void main(String[] args) {
        IOUtil.loadConfig();
        System.out.println(Config.print());
    }


}
