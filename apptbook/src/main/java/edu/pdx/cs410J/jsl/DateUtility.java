package edu.pdx.cs410J.jsl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * A collection of Java <code>Date</code> related utility functions.
 *
 * @author Jong Seong Lee
 * @version   %I%, %G%
 * @since     1.0
 */
public class DateUtility {
    static final private DateFormat date_format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.ENGLISH);
    static final private SimpleDateFormat pretty_format = new SimpleDateFormat("mm/dd/yyyy 'at' HH:mm a z");

    /**
     * Converts a <code>Date</code> object into a <code>String</code> object for pretty printing.
     *
     * @param date a <code>Date</code> object to be converted
     * @return a <code>String</code> object converted
     * @throws ParseException
     */
    public static String parseStringToDatePrettyPrint(Date date) {
        return pretty_format.format(date);
    }

    /**
     * Parses a <code>String</code> object into a <code>Date</code> object.
     *
     * @param date a <code>String</code> object to be parsed
     * @return a <code>Date</code> object with a date information of a <code>date</code> parameter
     * @throws ParseException
     */
    public static Date parseStringToDate(String date) throws ParseException {
        return date_format.parse(date);
    }

    /**
     * Calculates duration between two <code>Date</code> objects.
     *
     * @param begin_date
     * @param end_date
     * @return duration between two <code>Date</code> objects in minute
     */
    public static int getMinutesBetweenDates(Date begin_date, Date end_date) {
        return (int) TimeUnit.MILLISECONDS.toMinutes(end_date.getTime() - begin_date.getTime());
    }
}
