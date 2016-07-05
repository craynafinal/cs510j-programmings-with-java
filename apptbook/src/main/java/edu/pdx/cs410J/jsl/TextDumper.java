package edu.pdx.cs410J.jsl;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class TextDumper implements AppointmentBookDumper {

    private String filename = null;

    public TextDumper(String filename) {
        this.filename = filename;
    }

    public String getFileName() {
        return filename;
    }

    @Override
    public void dump(AbstractAppointmentBook abstractAppointmentBook) throws IOException {
        if (abstractAppointmentBook instanceof AppointmentBook) {
            dumpToFile((AppointmentBook)abstractAppointmentBook);
        }
    }

    private void dumpToFile(AppointmentBook appointmentBook) throws IOException {
        File file = new File(filename);
        FileWriter fw = new FileWriter(file);
        PrintWriter pw = new PrintWriter(fw);

        String ownerName = appointmentBook.getOwnerName();
        List<Appointment> listOfAppointments = appointmentBook.getAppointments();

        pw.println("appointmentbook");
        pw.println("  owner");
        pw.println("    " + ownerName);

        for (Appointment app: listOfAppointments) {
            pw.println("appointment");
            pw.println("  description");
            pw.println("    " + app.getDescription());
            pw.println("  begintime");
            pw.println("    " + app.getBeginTimeString());
            pw.println("  endtime");
            pw.println("    " + app.getEndTimeString());
        }
        pw.close();
    }
}
