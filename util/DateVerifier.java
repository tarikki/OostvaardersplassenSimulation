package util;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;

import javax.swing.*;

/**
 * Created by Pepe on 7.12.2014.
 */
public class DateVerifier extends InputVerifier {
    DateTimeFormatter df;
    static DateTimeParser[] parsers = {
            DateTimeFormat.forPattern("dd-MM-yyyy").getParser(),
            DateTimeFormat.forPattern("dd/MM/yyyy").getParser(),
            DateTimeFormat.forPattern("dd.MM.yyyy").getParser()};
    public static  DateTimeFormatter formatter = new DateTimeFormatterBuilder().append(null, parsers).toFormatter();

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

