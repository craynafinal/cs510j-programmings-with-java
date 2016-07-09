package edu.pdx.cs410J.jsl;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class TextDumperTest {
    AppointmentBook appointmentBook = null;
    TextDumper textDumper = null;

    static final String filename = "file.txt";

    /**
     * Setup member variables.
     */
    @Before
    public void appointmentSetup() {
        appointmentBook = new AppointmentBook("owner name");
        appointmentBook.addAppointment(new Appointment("desc1", "begintime1", "endtime1"));
        appointmentBook.addAppointment(new Appointment("desc2", "begintime2", "endtime2"));

        textDumper = new TextDumper(filename);
    }

    /**
     * Checks if file name assigned correctly.
     */
    @Test
    public void shouldSetFileNameCorrectly() {
        assertThat(textDumper.getFileName(), is(equalTo(filename)));
    }

    /**
     * Checks if file gets written successfully.
     */
    @Test
    public void shouldWriteToFileCorrectly() {
        try {
            textDumper.dump(appointmentBook);
        } catch (IOException e) {
            fail("IO Exception triggered while dumping to file");
        }

        String expectedContent =
                "--appointmentbook\n" +
                "  ---owner\n" +
                "    owner name\n" +
                "--appointment\n" +
                "  ---description\n" +
                "    desc1\n" +
                "  ---begintime\n" +
                "    begintime1\n" +
                "  ---endtime\n" +
                "    endtime1\n" +
                "--appointment\n" +
                "  ---description\n" +
                "    desc2\n" +
                "  ---begintime\n" +
                "    begintime2\n" +
                "  ---endtime\n" +
                "    endtime2\n";

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
