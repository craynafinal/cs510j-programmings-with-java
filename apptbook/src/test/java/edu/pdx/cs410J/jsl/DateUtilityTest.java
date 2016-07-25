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

    @Test
    public void shouldRecognizeDateTimeString() {
        String correct = "1/1/2016 1:00 PM";
        String incorrect = "wrong format";

        assertThat(DateUtility.checkDateTimeFormat(correct), is(equalTo(true)));
        assertThat(DateUtility.checkDateTimeFormat(incorrect), is(equalTo(false)));
    }

    @Test
    public void shouldRecognizeDateString() {
        String correct = "1/1/2016";
        String incorrect = "wrong format";

        assertThat(DateUtility.checkDateFormat(correct), is(equalTo(true)));
        assertThat(DateUtility.checkDateFormat(incorrect), is(equalTo(false)));
    }

    @Test
    public void shouldRecognizeTimeString() {
        String correct = "1:00";
        String incorrect = "wrong format";

        assertThat(DateUtility.checkTimeFormat(correct), is(equalTo(true)));
        assertThat(DateUtility.checkTimeFormat(incorrect), is(equalTo(false)));
    }

    @Test
    public void shouldRecognizeAMOrPMString() {
        String am = "AM";
        String pm = "PM";
        String incorrect = "wrong format";

        assertThat(DateUtility.checkAMOrPM(am), is(equalTo(true)));
        assertThat(DateUtility.checkAMOrPM(pm), is(equalTo(true)));
        assertThat(DateUtility.checkAMOrPM(incorrect), is(equalTo(false)));
    }
}
