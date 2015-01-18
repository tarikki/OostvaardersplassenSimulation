package testers;

import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * Created by extradikke on 09/01/15.
 *
 * A test class to explore the funtionalities of Joda.time
 */
public class DateTester {

    public static void main(String[] args) {
        DateTime start = new DateTime(2015, 1, 1, 0,0);
        DateTime end = new DateTime(2015, 2, 2, 0,0);
        System.out.println(Days.daysBetween(end, start).getDays());
    }
}
