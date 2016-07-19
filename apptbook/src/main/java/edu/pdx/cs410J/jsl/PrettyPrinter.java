package edu.pdx.cs410J.jsl;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by crayna on 7/18/16.
 */
public class PrettyPrinter implements AppointmentBookDumper {

    private PrintStream printStream = null;
    private String filename = null;
    private SimpleDateFormat dateFormat = null;

    public PrettyPrinter() {
        this.printStream = System.out;
        setDateTimeFormat();
    }

    public PrettyPrinter(String filename) {
        this.filename = filename;
        setDateTimeFormat();
    }

    @Override
    public void dump(AbstractAppointmentBook abstractAppointmentBook) throws IOException {
        if (abstractAppointmentBook instanceof AppointmentBook) {
            dumpToFile((AppointmentBook)abstractAppointmentBook);
        }
    }

    private void setDateTimeFormat() {
        dateFormat = new SimpleDateFormat("mm/dd/yyyy 'at' HH:mm a z");
    }

    private void setFilenameToPrintStream() throws FileNotFoundException {
        if (filename != null) {
            printStream = new PrintStream(new File(filename));
        }
    }

    public String getFileName() {
        return filename;
    }

    private void dumpToFile(AppointmentBook appointmentBook) throws IOException {
        setFilenameToPrintStream();

        String ownerName = appointmentBook.getOwnerName();
        List<Appointment> listOfAppointments = appointmentBook.getAppointments();

        printStream.println("1. Appointment Book Information");
        printStream.println(" 1) Owner Name: " + ownerName + "\n");
        printStream.println("2. Appointments");

        for (Appointment app: listOfAppointments) {
            printStream.println(" 1) Appointment: " + app.getDescription());
            printStream.println("    Begin Time:  " + dateFormat.format(app.getBeginTime()));
            printStream.println("    End Time:    " + dateFormat.format(app.getEndTime()));
            printStream.println("    Duration:    " + app.getDurationInMinutes() + " Minutes");
        }
        printStream.close();
    }
}
