package edu.pdx.cs410J.jsl.client;

import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * A unit test for the {@link DateUtility}.
 */
public class DateUtilityTest {
    @Test
    public void shouldParseStringToDateCorrectly() {
        Date date = DateUtility.parseStringToDate("1/1/2016 1:00 PM");
        assertThat(date.toString(), is(equalTo("Fri Jan 01 13:00:00 PST 2016")));
    }

    @Test
    public void shouldCalculateDifferenceCorrectly() {
        Date firstDate = DateUtility.parseStringToDate("1/1/2016 1:00 PM");
        Date secondDate = DateUtility.parseStringToDate("1/1/2016 2:00 PM");

        assertThat(DateUtility.getMinutesBetweenDates(firstDate, secondDate), is(equalTo(60)));
    }

    @Test
    public void shouldParseDateToStringCorrectly() {
        String dateToTest = "01/01/2016 02:00 PM";
        Date date = DateUtility.parseStringToDate(dateToTest);
        String parsed = DateUtility.parseDateToString(date);

        assertThat(parsed, is(equalTo(dateToTest)));
    }

    @Test
    public void shouldParseDateToStringPrettyPrintCorrectly() {
        Date date = DateUtility.parseStringToDate("01/01/2016 02:00 PM");
        String parsed = DateUtility.parseDateToStringPrettyPrint(date);

        assertThat(parsed, is(equalTo("01/01/2016 at 02:00 PM UTC-8")));
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
