package edu.pdx.cs410J.jsl;

import edu.pdx.cs410J.ParserException;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class TextParserTest {
    static final String filename = "/home/crayna/Downloads/test111.txt";
    static final String owner = "owner";
    TextParser textParser = null;
    TextDumper textDumper = null;

    AppointmentBook ab = null;

    @Before
    public void appointmentSetup() {
        textParser = new TextParser(filename, owner);
        textDumper = new TextDumper(filename);

        ab = new AppointmentBook("owner");
        ab.addAppointment(new Appointment("desc1", "time1", "time1"));
        ab.addAppointment(new Appointment("desc2", "time2", "time2"));
    }

    @Test
    public void shouldSetFileNameCorrectly() {
        assertThat(textParser.getFileName(), is(equalTo(filename)));
    }

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

    @Test
    public void shouldHandleNewLineCharacters() {
        ab.addAppointment(new Appointment("desc\n", "begintime1\r", "begintime2\n"));

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

    @Test
    public void shouldFailToParseFileWithoutAppointmentInformation() {
        String content = "--appointmentbook\n" +
                "---owner\n" +
                owner + "\n" +
                "--appointment";
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

    @Test
    public void shouldFailToParseFileWithMissingAppointmentInformation() {
        String content = "--appointmentbook\n" +
                "---owner\n" +
                owner + "\n" +
                "--appointment\n" +
                "---description\n" +
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

    @Test
    public void shouldFailToParseFileWithoutOwnerInformation() {
        String content = "--appointmentbook\n" +
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

    @Test
    public void shouldParseFileWithoutAppointments() {
        String content = "--appointmentbook\n" +
                "---owner\n" +
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

    private AppointmentBook getParsedString() throws ParserException {
        return (AppointmentBook)textParser.parse();
    }

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

    private void deleteFile() {
        try {
            Files.delete(Paths.get(filename));
        } catch (IOException x) {
            fail("IO Exception triggered while deleting file");
        }
    }
}
