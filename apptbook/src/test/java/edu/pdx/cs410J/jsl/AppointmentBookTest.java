package edu.pdx.cs410J.jsl;

import org.hamcrest.Matcher;
import org.junit.Test;

import java.text.ParseException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * A unit test for the {@link AppointmentBook}.
 */
public class AppointmentBookTest {

    @Test
    public void getOwnerNameShouldBeImplemented() {
        String owner = "owner name";
        AppointmentBook appointmentBook = new AppointmentBook(owner);
        assertThat(appointmentBook.getOwnerName(), is(equalTo(owner)));
    }

    @Test
    public void getAppointmentShouldReturnEmptyWithoutAdd() {
        AppointmentBook appointmentBook = new AppointmentBook();
        assertThat(appointmentBook.getAppointments().size(), is(equalTo(0)));
    }

    @Test
    public void getAppointmentShouldReturnAppointment() {
        String description = "test description";
        String begin_time = "11/11/1999 11:11 am";
        String end_time = "11/11/1999 11:11 pm";

        AppointmentBook appointmentBook = new AppointmentBook();
        Appointment appointment = null;
        try {
             appointment = new Appointment(description, begin_time, end_time);
        } catch (ParseException e) {
            fail("Failed to initialize an appointment");
        }
        appointmentBook.addAppointment(appointment);
        assertThat(appointmentBook.getAppointments(), (Matcher)hasItems(appointment));
    }
}
