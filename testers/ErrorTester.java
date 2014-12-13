package testers;

import mapUtils.Plant;
import util.RangeChecker;

import java.lang.reflect.Field;
import java.util.zip.DataFormatException;

/**
 * Created by extradikke on 13/12/14.
 */
public class ErrorTester {

    public static void main(String[] args) {
        Plant plant = new Plant();
        System.out.println(plant.getClass().getSimpleName());
        plant.setId(33);
        try {

            Field field = Plant.class.getDeclaredField("id");
            System.out.println(field.getName());
            field.setAccessible(true);
            System.out.println(field.get(plant));
//            System.out.println(field.get(plant));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            RangeChecker.checkforIntRange(plant,"id",1,31);
        } catch (DataFormatException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }


    }
}
