package mapUtils;

/**
 * Created by extradikke on 14/12/14.
 *
 * a class for storing and loading the monthly weather information of the terrain
 */
public class MonthlyWeather {
    private int month = 1;
    private int avMaxTemp = 5;
    private int avMinTemp = 0;
    private int avSunshine = 2;
    private int avDaysRains = 19;
    private int avRainLowBound = 101;
    private int avRainHighBound = 200;

    public int getAvDaysRains() {
        return avDaysRains;
    }

    public void setAvDaysRains(int avDaysRains) {
        this.avDaysRains = avDaysRains;
    }

    public int getAvMaxTemp() {
        return avMaxTemp;
    }

    public void setAvMaxTemp(int avMaxTemp) {
        this.avMaxTemp = avMaxTemp;
    }

    public int getAvMinTemp() {
        return avMinTemp;
    }

    public void setAvMinTemp(int avMinTemp) {
        this.avMinTemp = avMinTemp;
    }

    public int getAvRainHighBound() {
        return avRainHighBound;
    }

    public void setAvRainHighBound(int avRainHighBound) {
        this.avRainHighBound = avRainHighBound;
    }

    public int getAvRainLowBound() {
        return avRainLowBound;
    }

    public void setAvRainLowBound(int avRainLowBound) {
        this.avRainLowBound = avRainLowBound;
    }

    public int getAvSunshine() {
        return avSunshine;
    }

    public void setAvSunshine(int avSunshine) {
        this.avSunshine = avSunshine;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
