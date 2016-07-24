package edu.pdx.cs410J.jsl;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.fail;

/**
 * A unit test for the {@link DateUtility}.
 */
public class DateUtilityTest {
    @Test
    public void shouldParseStringToDateCorrectly() {
        String date_string = "1/1/2016 1:00 PM";
        DateFormat date_format = java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.SHORT, java.text.DateFormat.SHORT, Locale.ENGLISH);
        Date firstDate = null;
        Date secondDate = null;
        try {
            firstDate = DateUtility.parseStringToDate(date_string);
            secondDate = date_format.parse(date_string);
        } catch (ParseException e) {
            fail(e.getMessage());
        }

        assertThat(firstDate, is(equalTo(secondDate)));
    }

    @Test
    public void shouldCalculateDifferenceCorrectly() {
        Date firstDate = null;
        Date secondDate = null;

        try {
            firstDate = DateUtility.parseStringToDate("1/1/2016 1:00 PM");
            secondDate = DateUtility.parseStringToDate("1/1/2016 2:00 PM");
        } catch (ParseException e) {
            fail(e.getMessage());
        }

        assertThat(DateUtility.getMinutesBetweenDates(firstDate, secondDate), is(equalTo(60)));
    }
}
