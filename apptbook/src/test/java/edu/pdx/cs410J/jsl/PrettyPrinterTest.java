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
    AppointmentBook appointmentBook = null;
    PrettyPrinter prettyPrinter = null;

    static final String filename = "/home/crayna/Downloads/test222.txt";
    static final String owner = "owner";
    static final String desc1 = "desc1";
    static final String desc2 = "desc2";
    static final String begintime1 = "11/11/1999 11:11 am";
    static final String begintime2 = "11/11/1999 1:1 am";
    static final String endtime1 = "11/11/1999 11:11 pm";
    static final String endtime2 = "12/11/1999 1:1 pm";

    SimpleDateFormat dateFormat = null;
    DateFormat date_format = null;

    @Before
    public void appointmentSetup() {
        appointmentBook = new AppointmentBook(owner);
        try {
            appointmentBook.addAppointment(new Appointment(desc1, begintime1, endtime1));
            appointmentBook.addAppointment(new Appointment(desc2, begintime2, endtime2));
        } catch (ParseException e) {
            fail(e.getMessage());
        }

        dateFormat = new SimpleDateFormat("mm/dd/yyyy 'at' HH:mm a z");
        date_format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.ENGLISH);
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

        // delete file
        try {
            Files.delete(Paths.get(filename));
        } catch (IOException x) {
            fail(x.getMessage());
        }

        assertThat(fileContent, containsString(owner));
        assertThat(fileContent, containsString(desc1));
        assertThat(fileContent, containsString(desc2));

        try {
            assertThat(fileContent, containsString(dateFormat.format(date_format.parse(begintime1))));
            assertThat(fileContent, containsString(dateFormat.format(date_format.parse(endtime2))));
            assertThat(fileContent, containsString(dateFormat.format(date_format.parse(begintime1))));
            assertThat(fileContent, containsString(dateFormat.format(date_format.parse(endtime2))));
        } catch (ParseException e) {
            fail(e.getMessage());
        }

        assertThat(fileContent, containsString("720 Minutes"));
        assertThat(fileContent, containsString("43920 Minutes"));
    }
}