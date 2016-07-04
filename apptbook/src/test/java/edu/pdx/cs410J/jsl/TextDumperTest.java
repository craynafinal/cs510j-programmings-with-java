package edu.pdx.cs410J.jsl;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class TextDumperTest {
    @Test
    public void shouldSetFileNameCorrectly() {
        AppointmentBook appointmentBook = new AppointmentBook("owner name");
        appointmentBook.addAppointment(new Appointment("desc1", "begintime1", "endtime1"));
        appointmentBook.addAppointment(new Appointment("desc2", "begintime2", "endtime2"));

        TextDumper textDumper = new TextDumper("file.txt");
        assertThat(textDumper.getFileName(), is(equalTo("file.txt")));
    }
}
