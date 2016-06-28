package edu.pdx.cs410J.jsl;

import edu.pdx.cs410J.jsl.AppointmentBook;
import org.hamcrest.Matcher;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for the {@link AppointmentBook} class.
 */
public class AppointmentBookTest {
    /**
     * The constructor of the AppointmentBookTest class.
     */
    public AppointmentBookTest() {
    }

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
        AppointmentBook appointmentBook = new AppointmentBook();
        Appointment appointment = new Appointment();
        appointmentBook.addAppointment(appointment);
        assertThat(appointmentBook.getAppointments(), (Matcher)hasItems(appointment));
    }
}
