package edu.pdx.cs410J.jsl;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.*;
import java.text.SimpleDateFormat;
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

    private PrintWriter writer = null;
    private PrintStream printStream = null;
    private String filename = null;
    private SimpleDateFormat dateFormat = null;

    /**
     * The default constructor will set the print stream as <code>System.out</code>.
     */
    public PrettyPrinter() {
        this.printStream = System.out;
        setDateTimeFormat();
    }

    /**
     * This constructor will take a file name and use that to print information.
     *
     * @param filename  a file to print out information
     */
    public PrettyPrinter(String filename) {
        this.filename = filename;
        setDateTimeFormat();
    }

    public PrettyPrinter(PrintWriter pw) {
        this.writer = pw;
    }

    /**
     * Overrides <code>dump</code> method of {@link AppointmentBookDumper}.
     * This method will take an instance of {@link AppointmentBook} class
     * and will print this information out to a specified output source.
     *
     * @param abstractAppointmentBook   an instance of {@link AppointmentBook} class
     * @throws IOException              an exception will be thrown if IO problem happens
     */
    @Override
    public void dump(AbstractAppointmentBook abstractAppointmentBook) throws IOException {
        /*
        if (abstractAppointmentBook instanceof AppointmentBook) {
            dumpToFile((AppointmentBook)abstractAppointmentBook);
        }
        */
        this.writer.println(abstractAppointmentBook.getOwnerName());

        AppointmentBook appoitnmentBook = (AppointmentBook) abstractAppointmentBook;

        for (Appointment appointment: appoitnmentBook.getAppointments()) {
            this.writer.println(appointment.getDescription());
            this.writer.println(appointment.getBeginTimeString());
            this.writer.println(appointment.getEndTimeString());
        }
    }

    /**
     * Sets the date time format to print appointment's date information.
     */
    private void setDateTimeFormat() {
        dateFormat = new SimpleDateFormat("mm/dd/yyyy 'at' HH:mm a z");
    }

    /**
     * Sets print stream as a destination file if filename is assigned.
     *
     * @throws FileNotFoundException    an exception thrown if file is not found
     */
    private void setFilenameToPrintStream() throws FileNotFoundException {
        if (filename != null) {
            printStream = new PrintStream(new File(filename));
        }
    }

    /**
     * Returns the file name.
     *
     * @return  a filename in string format
     */
    public String getFileName() {
        return filename;
    }

    /**
     * Dumps information of an appointment book to a file or a standard output stream.
     * The printing format will be easier to read compared to the <code>dump</code> function of
     * {@link TextDumper} class.
     *
     * @param appointmentBook   an instance of {@link AppointmentBook} class
     * @throws IOException      an exception will be thrown if IO problem happens
     */
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
