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
}
