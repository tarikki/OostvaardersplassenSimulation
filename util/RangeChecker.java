package util;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.zip.DataFormatException;

/**
 * Created by extradikke on 13/12/14.
 *
 * Utility class for validating int ranges on load out, uses reflection
 */
public class RangeChecker {
    /**
     *
     * @param object the object that we loaded
     * @param fieldName the field of the object we have to check
     * @param low the low bound, inclusive
     * @param high the higbound, inclusive
     * @throws DataFormatException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void checkforIntRange(Object object, String fieldName, int low, int high) throws DataFormatException, NoSuchFieldException, IllegalAccessException {


        String objectClassName = object.getClass().getSimpleName();
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        int fieldValue = (int) field.get(object);


        if (fieldValue < low || fieldValue > high) {

            throw new DataFormatException(objectClassName + " field " + fieldName + " with value " + fieldValue +
                    " is out of valid range " + low + " - " + high + ". Check your config files!");

        }


    }


}
