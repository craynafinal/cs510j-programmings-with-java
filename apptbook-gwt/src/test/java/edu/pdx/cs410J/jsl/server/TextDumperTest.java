package edu.pdx.cs410J.jsl.server;

import edu.pdx.cs410J.jsl.client.Appointment;
import edu.pdx.cs410J.jsl.client.AppointmentBook;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * A unit test for the {@link TextDumper}.
 */
public class TextDumperTest {
    AppointmentBook appointmentBook = null;
    TextDumper textDumper = null;

    static final String filename = "/home/crayna/Downloads/test000.txt";
    static final String owner = "owner";
    static final String desc1 = "desc1";
    static final String desc2 = "desc2";
    static final String begintime1 = "11/11/1999 11:11 am";
    static final String begintime2 = "11/11/1999 1:1 am";
    static final String endtime1 = "11/11/1999 11:11 pm";
    static final String endtime2 = "11/11/1999 1:1 pm";

    @Before
    public void appointmentSetup() {
        appointmentBook = new AppointmentBook(owner);
        appointmentBook.addAppointment(new Appointment(desc1, begintime1, endtime1));
        appointmentBook.addAppointment(new Appointment(desc2, begintime2, endtime2));
        textDumper = new TextDumper(filename);
    }

    @Test
    public void shouldSetFileNameCorrectly() {
        assertThat(textDumper.getFileName(), is(equalTo(filename)));
    }

    @Test
    public void shouldWriteToFileCorrectly() {
        try {
            textDumper.dump(appointmentBook);
        } catch (IOException e) {
            fail("IO Exception triggered while dumping to file");
        }

        String expectedContent =
                ParseToken.APPOINTMENTBOOK.getToken() + "\n" +
                "  " + ParseToken.APPOINTMENTBOOK_OWNER.getToken() + "\n" +
                "    " + owner + "\n" +
                ParseToken.APPOINTMENT.getToken() + "\n" +
                "  " + ParseToken.APPOINTMENT_DESCRIPTION.getToken() + "\n" +
                "    " + desc1 + "\n" +
                "  " + ParseToken.APPOINTMENT_BEGINTIME.getToken() + "\n" +
                "    " + begintime1 + "\n" +
                "  " + ParseToken.APPOINTMENT_ENDTIME.getToken() + "\n" +
                "    " + endtime1 + "\n" +
                ParseToken.APPOINTMENT.getToken() + "\n" +
                "  " + ParseToken.APPOINTMENT_DESCRIPTION.getToken() + "\n" +
                "    " + desc2 + "\n" +
                "  " + ParseToken.APPOINTMENT_BEGINTIME.getToken() + "\n" +
                "    " + begintime2 + "\n" +
                "  " + ParseToken.APPOINTMENT_ENDTIME.getToken() + "\n" +
                "    " + endtime2 + "\n";

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

        assertThat(fileContent, is(equalTo(expectedContent)));
    }
}
