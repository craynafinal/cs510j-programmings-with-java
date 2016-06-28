package edu.pdx.cs410J.jsl;

import edu.pdx.cs410J.AbstractAppointmentBook;
import java.util.ArrayList;
import java.util.List;

/**
 * This AppointmentBook class describes who owns a list of appointments
 * and his / her list of appointments. For appointments it uses the Appointment class
 * to represent appointments owned by an instance of appointmentBook.
 *
 * @author Jong Seong Lee
 * @version   %I%, %G%
 * @since     1.0
 */
public class AppointmentBook extends AbstractAppointmentBook<Appointment> {
    private String owner_name = null;
    private List<Appointment> appointments = null;

    /**
     * This constructor initialize an instance with a default name
     */
    public AppointmentBook() {
        this("default name");
    }

    /**
     * This constructor initialize an instance with a given name
     * @param owner     a name of an owner of appointments in string format
     */
    public AppointmentBook(String owner) {
        owner_name = owner;
        appointments = new ArrayList<Appointment>();
    }

    /**
     * Returns a name of an owner of an instance
     * @return  a name of an owner in string format
     */
    @Override
    public String getOwnerName() { return owner_name; }

    /**
     * Returns a list of an appointments
     * @return  a list of a appointments
     */
    @Override
    public List<Appointment> getAppointments() {
        return appointments;
    }

    /**
     * Adds a given appointment to a list of appointments
     * @param appointment   an instance of the Appointment class
     */
    @Override
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }
}
