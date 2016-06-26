package edu.pdx.cs410J.jsl;

import edu.pdx.cs410J.jsl.AppointmentBook;
import org.hamcrest.Matcher;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by crayna on 6/26/16.
 */
public class AppointmentBookTest {
    public AppointmentBookTest() {
    }

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
        AppointmentBook appointmentBook = new AppointmentBook();
        Appointment appointment = new Appointment();
        appointmentBook.addAppointment(appointment);
        assertThat(appointmentBook.getAppointments(), (Matcher)hasItems(appointment));
    }
}
