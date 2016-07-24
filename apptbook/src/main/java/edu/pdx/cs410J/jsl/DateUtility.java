package edu.pdx.cs410J.jsl;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateUtility {
    static final private DateFormat date_format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.ENGLISH);

    public static Date parseStringToDate(String date) throws ParseException {
        return date_format.parse(date);
    }

    public static int getMinutesBetweenDates(Date begin_date, Date end_date) {
        return (int) TimeUnit.MILLISECONDS.toMinutes(end_date.getTime() - begin_date.getTime());
    }
}
