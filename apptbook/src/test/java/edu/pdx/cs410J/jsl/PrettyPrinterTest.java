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
 * A unit test for the {@link PrettyPrinter}.
 */
public class PrettyPrinterTest {
    private AppointmentBook appointmentBook = null;
    private PrettyPrinter prettyPrinter = null;
    private String filename = "/home/crayna/Downloads/test222.txt";
    private String ownerName = "My owner";
    private String description = "My description";
    private String[] beginTime = { "1/3/2016 1:00 AM", "1/1/2016 1:00 AM", "1/2/2016 1:00 AM", "1/2/2016 1:00 AM", "1/3/2016 1:00 AM" };
    private String[] endTime = { "3/3/2016 1:00 PM", "2/1/2016 1:00 PM", "2/2/2016 1:00 PM", "2/1/2016 1:00 PM", "3/3/2016 1:00 PM" };
    private int[] order = { 1, 3, 2, 0, 4 };
    private int[] minute = { 45360, 43920, 45360, 87120, 87120 };

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
        try {
            prettyPrinter = new PrettyPrinter(new PrintWriter(new File(filename)));
        } catch (FileNotFoundException e) {
            fail(e.getMessage());
        }

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

        try {
            for (int i = 0; i < beginTime.length; i++) {
                assertThat(fileContent, containsString((i + 1) + ") Appointment: " + description + (order[i] + 1)));
                assertThat(fileContent, containsString(DateUtility.parseStringToDatePrettyPrint(DateUtility.parseStringToDate(beginTime[i]))));
                assertThat(fileContent, containsString(DateUtility.parseStringToDatePrettyPrint(DateUtility.parseStringToDate(endTime[i]))));
                assertThat(fileContent, containsString(minute[i] + " Minutes"));
            }
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
}
