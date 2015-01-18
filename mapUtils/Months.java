package mapUtils;

import util.Config;
import util.RangeChecker;

import java.util.List;
import java.util.zip.DataFormatException;

/**
 * Created by extradikke on 14/12/14.
 *
 * a wrapper class for the weather months, helps loading the files
 */
public class Months {

    private List<MonthlyWeather> monthlyWeathers;

    public List<MonthlyWeather> getMonthlyWeathers() {
        return monthlyWeathers;
    }

    public void setMonthlyWeathers(List<MonthlyWeather> monthlyWeathers) {
        this.monthlyWeathers = monthlyWeathers;
    }

    public void verifyRanges() throws IllegalAccessException, NoSuchFieldException, DataFormatException {
        for (MonthlyWeather monthlyWeather : monthlyWeathers) {
            RangeChecker.checkforIntRange(monthlyWeather, "month", 1, 12);
            RangeChecker.checkforIntRange(monthlyWeather,"avSunshine", 0, 31);
            RangeChecker.checkforIntRange(monthlyWeather,"avDaysRains",0, 31);
        }
    }

    @Override
    public String toString() {
        return "MonthlyWeathers{" +
                "monthlyWeathers=" + monthlyWeathers +
                '}';
    }

}

