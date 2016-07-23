package edu.pdx.cs410J.jsl;

import edu.pdx.cs410J.jsl.AppointmentBook;
import org.hamcrest.Matcher;
import org.junit.Test;

import java.text.ParseException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link AppointmentBook} class.
 */
public class AppointmentBookTest {

    /**
     * It will check if a name of an owner assigned correctly.
     */
    @Test
    public void getOwnerNameShouldBeImplemented() {
        String owner = "owner name";
        AppointmentBook appointmentBook = new AppointmentBook(owner);
        assertThat(appointmentBook.getOwnerName(), is(equalTo(owner)));
    }

    /**
     * It will check if a list of appointments is empty initially.
     */
    @Test
    public void getAppointmentShouldReturnEmptyWithoutAdd() {
        AppointmentBook appointmentBook = new AppointmentBook();
        assertThat(appointmentBook.getAppointments().size(), is(equalTo(0)));
    }

    /**
     * It will check if an appointment is added correctly to a list of appointments.
     */
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
