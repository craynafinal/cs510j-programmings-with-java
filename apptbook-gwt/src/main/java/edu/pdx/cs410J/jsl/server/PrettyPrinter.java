package edu.pdx.cs410J.jsl.server;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;
import edu.pdx.cs410J.jsl.client.Appointment;
import edu.pdx.cs410J.jsl.client.AppointmentBook;
import edu.pdx.cs410J.jsl.client.DateUtility;

import java.io.*;
import java.util.Collections;
import java.util.List;

/**
 * <code>PrettyPrinter</code> is a class that implements {@link AppointmentBookDumper} interface.
 * The purpose of this class is to print information regarding an appointment book and appointments
 * in a form that a human can read easily compared to {@link TextDumper} class.
 *
 * @author Jong Seong Lee
 * @version   %I%, %G%
 * @since     1.0
 */
public class PrettyPrinter implements AppointmentBookDumper {

    private PrintWriter printWriter = null;

    /**
     * Constructor takes an instance of <code>PrintWriter</code> object.
     *
     * @param printWriter
     */
    public PrettyPrinter(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }

    /**
     * Overrides <code>dump</code> method of {@link AppointmentBookDumper}.
     * This method will take an instance of {@link AppointmentBook} class
     * and will print this information out to a specified output source.
     *
     * @param abstractAppointmentBook
     * @throws IOException
     */
    @Override
    public void dump(AbstractAppointmentBook abstractAppointmentBook) throws IOException {

        if (abstractAppointmentBook instanceof AppointmentBook) {
            dumpToFile((AppointmentBook)abstractAppointmentBook);
        }
    }

    /**
     * Dumps information of an appointment book to a file or a standard output stream.
     * The printing format will be easier to read compared to the <code>dump</code> function of
     * {@link TextDumper} class.
     *
     * @param appointmentBook
     * @throws IOException
     */
    private void dumpToFile(AppointmentBook appointmentBook) throws IOException {

        String ownerName = appointmentBook.getOwnerName();
        List<Appointment> listOfAppointments = appointmentBook.getAppointments();
        Collections.sort(listOfAppointments);

        int i = 1;

        printWriter.println("1. Appointment Book Information");
        printWriter.println(" 1) Owner Name: " + ownerName + "\n");
        printWriter.println("2. Appointments");

        for (Appointment app: listOfAppointments) {
            printWriter.println(" " + i++ + ") Appointment: " + app.getDescription());
            printWriter.println("    Begin Time:  " + DateUtility.parseStringToDatePrettyPrint(app.getBeginTime()));
            printWriter.println("    End Time:    " + DateUtility.parseStringToDatePrettyPrint(app.getEndTime()));
            printWriter.println("    Duration:    " + app.getDurationInMinutes() + " Minutes");
        }

        printWriter.flush();
        printWriter.close();
    }
}
