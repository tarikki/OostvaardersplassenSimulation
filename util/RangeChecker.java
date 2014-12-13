package util;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.zip.DataFormatException;

/**
 * Created by extradikke on 13/12/14.
 */
public class RangeChecker {

    public static void checkforIntRange(Object object, String fieldName, int low, int high) throws DataFormatException, NoSuchFieldException, IllegalAccessException {


        String objectClassName = object.getClass().getSimpleName();
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        int fieldValue = (int) field.get(object);


        if (fieldValue < low || fieldValue > high) {

            throw new DataFormatException(objectClassName + " field " + fieldName + " with value " + fieldValue +
                    " is out of range valid range " + low + " - " + high + ". Check your config files!");

        }


    }


}
