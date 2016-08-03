package edu.pdx.cs410J.jsl.client;

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
        String owner = "owner name";
        AppointmentBook appointmentBook = new AppointmentBook(owner);
        assertThat(appointmentBook.getAppointments().size(), is(equalTo(0)));
    }

    @Test
    public void getAppointmentShouldReturnAppointment() {
        String owner = "owner name";
        String description = "test description";
        String begin_time = "11/11/1999 11:11 am";
        String end_time = "11/11/1999 11:11 pm";

        AppointmentBook appointmentBook = new AppointmentBook(owner);
        Appointment appointment = null;
        appointment = new Appointment(description, begin_time, end_time);

        appointmentBook.addAppointment(appointment);
        assertThat(appointmentBook.getAppointments(), (Matcher)hasItems(appointment));
    }
}
