package edu.pdx.cs410J.jsl;

import edu.pdx.cs410J.AbstractAppointmentBook;

import java.util.ArrayList;
import java.util.List;


public class AppointmentBook extends AbstractAppointmentBook<Appointment> {
    private String owner_name = null;
    private List<Appointment> appointments = null;

    public AppointmentBook() {
        this("default name");
    }

    public AppointmentBook(String owner) {
        owner_name = owner;
        appointments = new ArrayList<Appointment>();
    }

    @Override
    public String getOwnerName() { return owner_name; }

    @Override
    public List<Appointment> getAppointments() {
        return appointments;
    }

    @Override
    public void addAppointment(Appointment abstractAppointment) {
        appointments.add(abstractAppointment);
    }
}
