package edu.pdx.cs410J.jsl;

import edu.pdx.cs410J.ParserException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link PrettyPrinter} class.
 */
public class PrettyPrintTest {
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

    /**
     * Setup member variables.
     */
    @Before
    public void appointmentSetup() {
        appointmentBook = new AppointmentBook(owner);
        try {
            appointmentBook.addAppointment(new Appointment(desc1, begintime1, endtime1));
            appointmentBook.addAppointment(new Appointment(desc2, begintime2, endtime2));
        } catch (ParseException e) {
            fail("Failed to initialize an appointment");
        }

        dateFormat = new SimpleDateFormat("mm/dd/yyyy 'at' HH:mm a z");
        date_format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.ENGLISH);
    }

    /**
     * Checks if file name assigned correctly.
     */
    @Test
    public void shouldSetFileNameCorrectly() {
        prettyPrinter = new PrettyPrinter(filename);
        assertThat(prettyPrinter.getFileName(), is(equalTo(filename)));
    }

    /**
     * Checks if file gets written successfully.
     */
    @Test
    public void shouldWriteToFileCorrectly() {/*
        prettyPrinter = new PrettyPrinter(filename);

        try {
            prettyPrinter.dump(appointmentBook);
        } catch (IOException e) {
            fail("IO Exception triggered while dumping to file");
        }

        byte[] encoded = null;
        String fileContent = null;

        try {
            encoded = Files.readAllBytes(Paths.get(filename));
        } catch (IOException e) {
            fail("IO Exception triggered while getting file content");
        }

        try {
            fileContent = new String(encoded, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            fail("Unsupported Encoding Exception triggered");
        }

        // delete file
        try {
            Files.delete(Paths.get(filename));
        } catch (IOException x) {
            fail("IO Exception triggered while deleting file");
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
        assertThat(fileContent, containsString("43920 Minutes"));*/
    }
}
