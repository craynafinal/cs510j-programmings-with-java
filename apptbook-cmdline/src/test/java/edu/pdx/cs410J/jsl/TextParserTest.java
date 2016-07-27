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
    static final private String filename = "/home/crayna/Downloads/test111.txt";
    static final private String owner = "owner";
    private TextParser textParser = null;
    private TextDumper textDumper = null;

    private AppointmentBook ab = null;

    static final private String desc1 = "desc1";
    static final private String desc2 = "desc2";
    static final private String begintime1 = "11/11/1999 11:11 am";
    static final private String begintime2 = "11/11/1999 1:1 am";
    static final private String endtime1 = "11/11/1999 11:11 pm";
    static final private String endtime2 = "11/11/1999 1:1 pm";

    @Before
    public void appointmentSetup() {
        textParser = new TextParser(filename, owner);
        textDumper = new TextDumper(filename);

        ab = new AppointmentBook("owner");

        try {
            ab.addAppointment(new Appointment(desc1, begintime1, endtime1));
            ab.addAppointment(new Appointment(desc2, begintime2, endtime2));
        } catch (ParseException e) {
            fail("Failed to initialize appointments");
        }
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
