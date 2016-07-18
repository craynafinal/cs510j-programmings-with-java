package edu.pdx.cs410J.jsl;

import edu.pdx.cs410J.ParserException;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class TextParserTest {
    static final String filename = "/home/crayna/Downloads/test111.txt";
    static final String owner = "owner";
    TextParser textParser = null;
    TextDumper textDumper = null;

    AppointmentBook ab = null;

    static final String desc1 = "desc1";
    static final String desc2 = "desc2";
    static final String begintime1 = "11/11/1999 11:11 am";
    static final String begintime2 = "11/11/1999 1:1 am";
    static final String endtime1 = "11/11/1999 11:11 pm";
    static final String endtime2 = "11/11/1999 1:1 pm";

    /**
     * Setup member variables.
     */
    @Before
    public void appointmentSetup() {
        textParser = new TextParser(filename, owner);
        textDumper = new TextDumper(filename);

        ab = new AppointmentBook("owner");

        // added try catch for ParseException because of Project 3
        try {
            ab.addAppointment(new Appointment(desc1, begintime1, endtime1));
            ab.addAppointment(new Appointment(desc2, begintime2, endtime2));
        } catch (ParseException e) {
            fail("Failed to initialize appointments");
        }
    }

    /**
     * Checks if a file name gets assigned correctly.
     */
    @Test
    public void shouldSetFileNameCorrectly() {
        assertThat(textParser.getFileName(), is(equalTo(filename)));
    }

    /**
     * Ideal case should not throw any error.
     */
    @Test
    public void shouldParseCorrectFileWithoutError() {
        try {
            textDumper.dump(ab);
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        AppointmentBook appointmentBook = null;

        try {
            appointmentBook = getParsedString();
        } catch (ParserException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            deleteFile();
        }

        assertThat(appointmentBook.getOwnerName(), is(equalTo(ab.getOwnerName())));

        for (int i = 0; i < ab.getAppointments().size(); i++) {
            assertThat(appointmentBook.getAppointments().get(i).getBeginTimeString(),
                    is(equalTo(ab.getAppointments().get(i).getBeginTimeString())));
            assertThat(appointmentBook.getAppointments().get(i).getEndTimeString(),
                    is(equalTo(ab.getAppointments().get(i).getEndTimeString())));
            assertThat(appointmentBook.getAppointments().get(i).getDescription(),
                    is(equalTo(ab.getAppointments().get(i).getDescription())));
        }
    }

    /**
     * New line characters will be a problem to parse.
     * It should convert new line characters to string and convert back.
     */
    @Test
    public void shouldHandleNewLineCharacters() {
        // added try catch for ParseException because of Project 3
        try {
            ab.addAppointment(new Appointment("desc\n", "11/11/1999 11:11 am\r", "11/11/1999 11:11 pm\n"));
        } catch (ParseException e) {
            fail("Failed to initialize an appointment");
        }

        try {
            textDumper.dump(ab);
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        AppointmentBook appointmentBook = null;

        try {
            appointmentBook = getParsedString();
        } catch (ParserException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            deleteFile();
        }

        assertThat(appointmentBook.getOwnerName(), is(equalTo(ab.getOwnerName())));

        for (int i = 0; i < ab.getAppointments().size(); i++) {
            assertThat(appointmentBook.getAppointments().get(i).getBeginTimeString(),
                    is(equalTo(ab.getAppointments().get(i).getBeginTimeString())));
            assertThat(appointmentBook.getAppointments().get(i).getEndTimeString(),
                    is(equalTo(ab.getAppointments().get(i).getEndTimeString())));
            assertThat(appointmentBook.getAppointments().get(i).getDescription(),
                    is(equalTo(ab.getAppointments().get(i).getDescription())));
        }
    }

    /**
     * Parse syntax check.
     */
    @Test
    public void shouldFailToParseFileStartingWithoutAppointmentBook() {
        String content = "some random string";
        customWritingToFile(content);

        AppointmentBook appointmentBook = null;

        try {
            appointmentBook = getParsedString();
            fail("Parser Exception is expected");
        } catch (ParserException e) {
            assertThat(e.getMessage(), containsString("appointmentbook"));
        } finally {
            deleteFile();
        }
    }

    /**
     * Parse syntax check.
     */
    @Test
    public void shouldFailToParseFileWithoutAppointmentInformation() {
        String content = ParseToken.APPOINTMENTBOOK.getToken() + "\n" +
                ParseToken.APPOINTMENTBOOK_OWNER.getToken() + "\n" +
                owner + "\n" +
                ParseToken.APPOINTMENT.getToken() + "";
        customWritingToFile(content);

        AppointmentBook appointmentBook = null;

        try {
            appointmentBook = getParsedString();
            fail("Parser Exception is expected");
        } catch (ParserException e) {
            assertThat(e.getMessage(), containsString("Not enough"));
        } finally {
            deleteFile();
        }
    }

    /**
     * Parse syntax check.
     */
    @Test
    public void shouldFailToParseFileWithTwoDifferentOwnerName() {
        String content = ParseToken.APPOINTMENTBOOK.getToken() + "\n" +
                ParseToken.APPOINTMENTBOOK_OWNER.getToken() + "\n" +
                "special owner\n";
        customWritingToFile(content);

        AppointmentBook appointmentBook = null;

        try {
            appointmentBook = getParsedString();
            fail("Parser Exception is expected");
        } catch (ParserException e) {
            assertThat(e.getMessage(), containsString("special owner"));
        } finally {
            deleteFile();
        }
    }

    /**
     * Parse syntax check.
     */
    @Test
    public void shouldFailToParseFileWithMissingAppointmentInformation() {
        String content = ParseToken.APPOINTMENTBOOK.getToken() + "\n" +
                ParseToken.APPOINTMENTBOOK_OWNER.getToken() + "\n" +
                owner + "\n" +
                ParseToken.APPOINTMENT.getToken() + "\n" +
                ParseToken.APPOINTMENT_DESCRIPTION.getToken() + "\n" +
                "desc";
        customWritingToFile(content);

        AppointmentBook appointmentBook = null;

        try {
            appointmentBook = getParsedString();
            fail("Parser Exception is expected");
        } catch (ParserException e) {
            assertThat(e.getMessage(), containsString("Not enough"));
        } finally {
            deleteFile();
        }
    }

    /**
     * Parse syntax check.
     */
    @Test
    public void shouldFailToParseFileWithoutOwnerInformation() {
        String content = ParseToken.APPOINTMENTBOOK.getToken() + "\n" +
                "something";
        customWritingToFile(content);

        AppointmentBook appointmentBook = null;

        try {
            appointmentBook = getParsedString();
            fail("Parser Exception is expected");
        } catch (ParserException e) {
            assertThat(e.getMessage(), containsString("something"));
        } finally {
            deleteFile();
        }
    }

    /**
     * When a file is not found, it should create an empty appointment book, not throwing an error.
     */
    @Test
    public void shouldReturnEmptyAppointmentBookWithNoFileProvided() {
        AppointmentBook appointmentBook = null;

        try {
            appointmentBook = getParsedString();
            assertThat(appointmentBook.getOwnerName(), is(equalTo(owner)));
            assertThat(appointmentBook.getAppointments().size(), is(equalTo(0)));
        } catch (ParserException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Parse syntax check.
     */
    @Test
    public void shouldParseFileWithoutAppointments() {
        String content = ParseToken.APPOINTMENTBOOK.getToken() + "\n" +
                ParseToken.APPOINTMENTBOOK_OWNER.getToken() + "\n" +
                owner;
        customWritingToFile(content);

        AppointmentBook appointmentBook = null;

        try {
            appointmentBook = getParsedString();
            assertThat(appointmentBook.getOwnerName(), is(equalTo(owner)));
            assertThat(appointmentBook.getAppointments().size(), is(equalTo(0)));
        } catch (ParserException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            deleteFile();
        }
    }

    /**
     * Calls the parse method.
     *
     * @return an instance of <link>AppointmentBook</link> generated by the text file.
     * @throws ParserException
     */
    private AppointmentBook getParsedString() throws ParserException {
        return (AppointmentBook)textParser.parse();
    }

    /**
     * Writes the content to the file.
     * @param content  a content to be written in a file in string format.
     */
    private void customWritingToFile(String content) {
        File file = new File(filename);

        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        PrintWriter pw = new PrintWriter(fw);

        pw.println(content);

        pw.close();
    }

    /**
     * Deletes the file.
     */
    private void deleteFile() {
        try {
            Files.delete(Paths.get(filename));
        } catch (IOException x) {
            fail("IO Exception triggered while deleting file");
        }
    }
}
