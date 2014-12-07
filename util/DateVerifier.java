package util;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Pepe on 7.12.2014.
 */
public class DateVerifier extends InputVerifier {
    DateTimeFormatter df;
   public static DateTimeParser[] parsers = {
            DateTimeFormat.forPattern("dd-MM-yyyy").getParser(),
            DateTimeFormat.forPattern("dd/MM/yyyy").getParser(),
            DateTimeFormat.forPattern("dd.MM.yyyy").getParser()};
    public static  DateTimeFormatter formatter = new DateTimeFormatterBuilder().append(null, parsers).toFormatter();

    public DateVerifier(DateTimeFormatter df) {
        this.df = df;

    }

    public boolean verify(JComponent c) {
        boolean success = false;
        JTextField tf = (JTextField) c;
        String entry = tf.getText();
        LocalDate date;

        //// Successs
        try {
            date = df.parseLocalDate(entry);
            success = true;
            tf.setBackground(Color.GREEN); /// Set bg color to green for valid input
        }
        catch (IllegalArgumentException e)
        {
            success = false;
            tf.setBackground(Color.RED);
            JOptionPane.showMessageDialog(tf.getRootPane(), "Please enter the date in the correct format" + "\n" + "dd/mm/yyyy" + "\n" + "dd-mm-yyyy" + "\n" + "dd.mm.yyyy", "Invalid date", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();

        }




        /// How do we fail?

       // System.out.println("date = " + date.toString());



        return true;




    }
}

