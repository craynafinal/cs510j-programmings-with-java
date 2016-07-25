package edu.pdx.cs410J.jsl;

import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.web.HttpRequestHelper;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.text.ParseException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

/**
 * An integration test for {@link Project4} that invokes its main method with
 * various arguments
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Project4IT extends InvokeMainTestCase {
    private static final String HOSTNAME = "localhost";
    private static final String PORT = System.getProperty("http.port", "8080");

    private static final String OPTION_HOST = "-host";
    private static final String OPTION_PORT = "-port";
    private static final String OPTION_SEARCH = "-search";
    private static final String OPTION_PRINT = "-print";
    private static final String OPTION_README = "-README";

    private MainMethodResult invokeMain(String... args) {
        return invokeMain( Project4.class, args );
    }

    @Test
    public void shouldFailWhenPortNumberIsNotProvided() {
        MainMethodResult result = invokeMain(OPTION_HOST, HOSTNAME);
        assertThat(result.getExitCode(), is(equalTo(1)));
        assertThat(result.getErr(), containsString(OPTION_PORT));
    }

    @Test
    public void shouldFailWhenHostIsNotProvided() {
        MainMethodResult result = invokeMain(OPTION_PORT, PORT);
        assertThat(result.getExitCode(), is(equalTo(1)));
        assertThat(result.getErr(), containsString(OPTION_HOST));
    }

    @Test
    public void shouldFailWhenPortNumberCannotBeParsed() {
        String wrongPortNumber = "asdf";

        MainMethodResult result = invokeMain(OPTION_HOST, HOSTNAME, OPTION_PORT, wrongPortNumber);
        assertThat(result.getExitCode(), is(equalTo(1)));
        assertThat(result.getErr(), containsString(wrongPortNumber));
    }

    @Test
    public void shouldAddNewAppointment() {
        String owner = "My owner";
        String description = "My description";
        String beginTime = "1/1/1999 1:00 AM";
        String endTime = "1/1/1999 5:00 AM";

        MainMethodResult result = invokeMain(OPTION_HOST, HOSTNAME, OPTION_PORT, PORT, owner, description, beginTime, endTime);

        assertThat(result.getExitCode(), is(equalTo(null)));

        AppointmentBookRestClient client = new AppointmentBookRestClient(HOSTNAME, Integer.parseInt(PORT));
        HttpRequestHelper.Response response = null;

        try {
            response = client.prettyPrintAppointmentBook(owner);
        } catch (IOException e) {
            fail(e.getMessage());
        }

        assertThat(response.getContent(), containsString(owner));
        assertThat(response.getContent(), containsString(description));

        try {
            assertThat(response.getContent(), containsString(DateUtility.parseStringToDatePrettyPrint(DateUtility.parseStringToDate(beginTime))));
            assertThat(response.getContent(), containsString(DateUtility.parseStringToDatePrettyPrint(DateUtility.parseStringToDate(endTime))));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void shouldPrintReadMeOnly() {
        String owner = "My owner";
        String description = "My description";
        String beginTime = "1/1/1999 1:00 AM";
        String endTime = "1/1/1999 5:00 AM";

        MainMethodResult result = invokeMain(owner, description, beginTime, endTime, OPTION_README);

        assertThat(result.getExitCode(), is(equalTo(null)));
        assertThat(result.getOut(), containsString("README"));
    }

    @Test
    public void shouldAddNewAppointmentWithoutConnection() {
        String owner = "My owner";
        String description = "My description";
        String beginTime = "1/1/1999 1:00 AM";
        String endTime = "1/1/1999 5:00 AM";

        MainMethodResult result = invokeMain(owner, description, beginTime, endTime, OPTION_PRINT);

        assertThat(result.getExitCode(), is(equalTo(null)));
        assertThat(result.getOut(), containsString(description));
        try {
            assertThat(result.getOut(), containsString((DateUtility.parseStringToDate(beginTime).toString())));
            assertThat(result.getOut(), containsString((DateUtility.parseStringToDate(endTime).toString())));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void shouldSearchAppointment() {
        String ownerName = "My owner";
        String description = "My description";
        String[] beginTime = { "1/1/2016 1:00 AM", "1/2/2016 1:00 AM", "1/3/2016 1:00 AM", "1/4/2016 1:00 AM", "1/5/2016 1:00 AM" };
        String[] endTime = { "1/1/2016 1:00 PM", "1/2/2016 1:00 PM", "1/3/2016 1:00 PM", "1/4/2016 1:00 PM", "1/5/2016 1:00 PM" };
        int i, max = 5, start = 1, end = 3;

        AppointmentBookRestClient client = new AppointmentBookRestClient(HOSTNAME, Integer.parseInt(PORT));
        HttpRequestHelper.Response response = null;

        for (i = 0; i < max; i++) {
            try {
                response = client.createAppointment(ownerName, description + (i + 1), beginTime[i], endTime[i]);
            } catch (IOException e) {
                fail(e.getMessage());
            }
        }

        MainMethodResult result = invokeMain(OPTION_HOST, HOSTNAME, OPTION_PORT, PORT, ownerName, description, beginTime[start], endTime[end], OPTION_SEARCH);

        assertThat(result.getExitCode(), is(equalTo(null)));
        assertThat(result.getOut(), containsString(ownerName));
        assertThat(result.getOut(), containsString("Minutes"));
        for (i = start; i < end; i++) {
            assertThat(result.getOut(), containsString(description + (i + 1)));

            try {
                assertThat(result.getOut(), containsString(DateUtility.parseStringToDatePrettyPrint(DateUtility.parseStringToDate(beginTime[i]))));
                assertThat(result.getOut(), containsString(DateUtility.parseStringToDatePrettyPrint(DateUtility.parseStringToDate(endTime[i]))));
            } catch (ParseException e) {
                fail(e.getMessage());
            }
        }
    }
}