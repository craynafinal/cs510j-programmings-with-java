package edu.pdx.cs410J.jsl;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * The <code>TextDumper</code> is the class implements the <link>AppointmentBookDumper</code> class.
 * The main purpose of this class is to write a content of an instance of <link>AppointmentBook</link>
 * to a file that is given.
 *
 * @author    Jong Seong Lee
 * @version   %I%, %G%
 * @since     1.0
 */
public class TextDumper implements AppointmentBookDumper {

    private String filename = null;

    /**
     * This constructor takes a file name as a parameter.
     *
     * @param filename  a path to the file name to be written in string format
     */
    public TextDumper(String filename) {
        this.filename = filename;
    }

    /**
     * This method returns the file name saved in an instance.
     *
     * @return  a file name in string format
     */
    public String getFileName() {
        return filename;
    }

    /**
     * Overrides the dump method of the <link>AppointmentBookDumper</link> class.
     * This will take an instance of <link>AppointmentBook</link>
     * and it will dump the content to a file.
     *
     * @param abstractAppointmentBook an instance of <link>AppointmentBook</link>
     * @throws IOException            an exception is thrown if dumping process fails
     */
    @Override
    public void dump(AbstractAppointmentBook abstractAppointmentBook) throws IOException {
        if (abstractAppointmentBook instanceof AppointmentBook) {
            dumpToFile((AppointmentBook)abstractAppointmentBook);
        }
    }

    /**
     * This method implements the dumping logic.
     * It will write the information regarding the appointment book once
     * followed by zero or more appointment details depending on the number of appointments
     * in the <link>AppointmentBook</link> parameter.
     *
     * @param appointmentBook an instance of <link>AppointmentBook</link>
     * @throws IOException    an exception is thrown if dumping process fails
     */
    private void dumpToFile(AppointmentBook appointmentBook) throws IOException {
        File file = new File(filename);
        FileWriter fw = new FileWriter(file);
        PrintWriter pw = new PrintWriter(fw);

        String ownerName = appointmentBook.getOwnerName();
        List<Appointment> listOfAppointments = appointmentBook.getAppointments();

        pw.println(ParseToken.APPOINTMENTBOOK.getToken());
        pw.println("  " + ParseToken.APPOINTMENTBOOK_OWNER.getToken());
        pw.println("    " + replaceNewLineCharacters(ownerName));

        for (Appointment app: listOfAppointments) {
            pw.println(ParseToken.APPOINTMENT.getToken());
            pw.println("  " + ParseToken.APPOINTMENT_DESCRIPTION.getToken());
            pw.println("    " + replaceNewLineCharacters(app.getDescription()));
            pw.println("  " + ParseToken.APPOINTMENT_BEGINTIME.getToken());
            pw.println("    " + replaceNewLineCharacters(app.getBeginTimeString()));
            pw.println("  " + ParseToken.APPOINTMENT_ENDTIME.getToken());
            pw.println("    " + replaceNewLineCharacters(app.getEndTimeString()));
        }
        pw.close();
    }

    /**
     * Replaces new line characters with strings of "\\n" or "\\r".
     * This process is required for the style of dumping.
     *
     * @param string a string that might have new line characters to be converted
     * @return       converted string that contains no new line character
     */
    private String replaceNewLineCharacters(String string) {
        return string.replace("\n", "\\n").replace("\r", "\\r");
    }
}
