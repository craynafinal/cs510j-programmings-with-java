package edu.pdx.cs410J.jsl;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link PrettyPrinter} class.
 */
public class PrettyPrintTest {
    private AppointmentBook appointmentBook = null;
    private PrettyPrinter prettyPrinter = null;
    static final String filename = "/home/crayna/Downloads/test222.txt";
    static final String ownerName = "My owner";
    static final String description = "My description";
    static final String[] beginTime = { "1/3/2016 1:00 AM", "1/1/2016 1:00 AM", "1/2/2016 1:00 AM", "1/2/2016 1:00 AM", "1/3/2016 1:00 AM" };
    static final String[] endTime = { "3/3/2016 1:00 PM", "2/1/2016 1:00 PM", "2/2/2016 1:00 PM", "2/1/2016 1:00 PM", "3/3/2016 1:00 PM" };
    static final int[] order = { 1, 3, 2, 0, 4 };
    static final int[] minute = { 45360, 43920, 45360, 87120, 87120 };

    @Before
    public void appointmentSetup() {
        appointmentBook = new AppointmentBook(ownerName);

        try {
            for (int i = 0; i < beginTime.length; i++) {
                appointmentBook.addAppointment(new Appointment(description + (i + 1), beginTime[i], endTime[i]));
            }
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void shouldWriteToFileCorrectly() {


        prettyPrinter = new PrettyPrinter(filename);

        try {
            prettyPrinter.dump(appointmentBook);
        } catch (IOException e) {
            fail(e.getMessage());
        }

        byte[] encoded = null;
        String fileContent = null;

        try {
            encoded = Files.readAllBytes(Paths.get(filename));
        } catch (IOException e) {
            fail(e.getMessage());
        }

        try {
            fileContent = new String(encoded, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            fail(e.getMessage());
        }

        try {
            Files.delete(Paths.get(filename));
        } catch (IOException x) {
            fail(x.getMessage());
        }

        assertThat(fileContent, containsString(ownerName));

        for (int i = 0; i < beginTime.length; i++) {
            assertThat(fileContent, containsString((i + 1) + ") Appointment: " + description + (order[i] + 1)));
            assertThat(fileContent, containsString(getDateFormattedString(beginTime[i])));
            assertThat(fileContent, containsString(getDateFormattedString(endTime[i])));
            assertThat(fileContent, containsString(minute[i] + " Minutes"));
        }
    }

    private String getDateFormattedString(String string) {
        DateFormat date_format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.ENGLISH);
        SimpleDateFormat pretty_format = new SimpleDateFormat("mm/dd/yyyy 'at' HH:mm a z");

        String ret = null;

        try {
            ret = pretty_format.format(date_format.parse(string));
        } catch (ParseException e) {
            fail(e.getMessage());
        }

        return ret;
    }
}
