package util;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;

import javax.swing.*;

/**
 * Created by Pepe on 7.12.2014.
 */
public class DateVerifier extends InputVerifier {
    DateTimeFormatter df;

    public DateVerifier(DateTimeFormatter df) {
        this.df = df;

    }

    public boolean verify(JComponent c) {
        JTextField tf = (JTextField) c;
        String entry = tf.getText();

        LocalDate date;
        date = df.parseLocalDate(entry);

        System.out.println("date = " + date.toString());
        return true;
    }
}

