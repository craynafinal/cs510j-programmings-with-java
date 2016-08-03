package edu.pdx.cs410J.jsl.server;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookParser;
import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.jsl.client.Appointment;
import edu.pdx.cs410J.jsl.client.AppointmentBook;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * The <code>TextParser</code> class implements the {@link AppointmentBookParser}.
 * The main purpose of this class is to read a file given and bring the information back to
 * an instance of {@link AppointmentBook} with appointments.
 *
 * @author    Jong Seong Lee
 * @version   %I%, %G%
 * @since     1.0
 */
public class TextParser implements AppointmentBookParser {

    private String filename = null;
    private String owner = "default owner";

    /**
     * This constructor takes a file name as a parameter.
     *
     * @param filename a path to the file name to be read in string format
     */
    public TextParser(String filename) {
        this.filename = filename;
    }

    /**
     * This constructor takes a file name and the name of the owner in string format.
     * The name of the owner should match the one in the file to be read.
     *
     * @param filename a path to the file name to be read in string format
     * @param owner    the name of the owner who owns the appointment book in the file to be read
     */
    public TextParser(String filename, String owner) {
        this.filename = filename;
        this.owner = owner;
    }

    /**
     * Returns the name of file in string format.
     *
     * @return a name of file in string format
     */
    public String getFileName() {
        return filename;
    }

    /**
     * Returns a string indicating the current line number.
     * This method is used for the debugging purpose.
     *
     * @param line the current line number
     * @return     a string contains the current line number to be used for debugging purpose
     */
    private String lineNumber(int line) {
        return " - Line Number " + line;
    }

    /**
     * This method will return the next token read
     * from the {@link BufferedReader} object that is passed in as a parameter.
     *
     * @param br            an instance of the {@link BufferedReader} class
     * @return              a token in string format
     * @throws IOException  an IO exception can be thrown if it fails to read from buffer
     */
    private String getNextToken(BufferedReader br) throws IOException {
        String token = null;

        if (br != null) {
            token = br.readLine();
            if (token != null && !token.isEmpty()) {
                token = token.trim();
            }
        }

        return token;
    }

    /**
     * Overrides the <code>parse</code> method from the {@link AppointmentBookParser}.
     * It will parse the file to be read and construct an instance of the {@link AppointmentBook}
     * based on the file content. If file is not found, it will create an instance with
     * no appointments added. However, if any other exception happens, it will throw a ParserException.
     *
     * @return                 an instance of the {@link AppointmentBook}
     *                         that is constructed based on the file content
     * @throws ParserException if there is any failure reading the file except the situation of file not found,
     *                         it will throw an exception to describe parsing problem
     */
    @Override
    public AbstractAppointmentBook parse() throws ParserException {
        AppointmentBook appointmentBook = null;

        File file = new File(filename);
        FileReader fr = null;
        BufferedReader br = null;

        String token = null;
        List<String> appointment_data = null;
        int line = 0;

        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            // if file not found, return an empty appointment book
            return emptyAppointmentBook();
        }

        br = new BufferedReader(fr);

        try {
            token = getNextToken(br);
            line++;

            // checking appointment book data
            if (!token.equals(ParseToken.APPOINTMENTBOOK.getToken())) {
                throw new ParserException("Does not start with a token \"appointmentbook\"" + lineNumber(line));
            }

            token = getNextToken(br);
            line++;

            if (token.equals(ParseToken.APPOINTMENTBOOK_OWNER.getToken())) {
                token = getNextToken(br);
                line++;

                if (owner.equals(token)) {
                    appointmentBook = new AppointmentBook(token);
                } else {
                    throw new ParserException("The name of the owner does not match: expected " + owner +
                            " but got " + token + lineNumber(line));
                }
            } else {
                throw new ParserException("Does not contain information regarding appointmentbook: " +
                        "found \"" + token + "\"" + lineNumber(line));
            }

            token = getNextToken(br);
            line++;

            // checking appointment data
            while (token != null && !token.isEmpty()) {
                if (token.equals(ParseToken.APPOINTMENT.getToken())) {

                    appointment_data = new ArrayList<String>();

                    for (int i = 0; i < 3; i++) {
                        token = getNextToken(br);
                        line++;

                        try {
                            // check description, begin time, end time
                            if (token.equals(ParseToken.APPOINTMENT_DESCRIPTION.getToken())) {
                                token = getNextToken(br);
                                line++;

                                appointment_data.add(0, token);
                            } else if (token.equals(ParseToken.APPOINTMENT_BEGINTIME.getToken())) {
                                token = getNextToken(br);
                                line++;

                                appointment_data.add(1, token);
                            } else if (token.equals(ParseToken.APPOINTMENT_ENDTIME.getToken())) {
                                token = getNextToken(br);
                                line++;

                                appointment_data.add(2, token);
                            } else {
                                throw new ParserException("Unrecognized token: " + token + lineNumber(line));
                            }
                        } catch (NullPointerException e) {
                            throw new ParserException("Not enough information for an appointment" + lineNumber(line));
                        }
                    }

                    // using arraylist size to confirm data
                    // because each data has been inserted with a specific index
                    if (appointment_data.size() == 3) {
                        // added try catch for ParseException because of Project 3
                        appointmentBook.addAppointment(new Appointment(
                                replaceNewLineCharacters(appointment_data.get(0)),
                                replaceNewLineCharacters(appointment_data.get(1)),
                                replaceNewLineCharacters(appointment_data.get(2))));
                    }

                    token = getNextToken(br);
                    line++;
                } else {
                    throw new ParserException("Unrecognized token: " + token + lineNumber(line));
                }
            }

        } catch (IOException e) {
            throw new ParserException("IO Exception thrown in the middle of parsing");
        } finally {
            try {
                closeStream(br);
            } catch (IOException e) {
                throw new ParserException("IO Exception thrown while closing file");
            }
        }

        return appointmentBook;
    }

    /**
     * Any "\\n" or "\\r" characters in a string will be converted to new line characters.
     *
     * @param string a string that might contain "\\n" or "\\r"
     * @return       a converted string
     */
    private String replaceNewLineCharacters(String string) {
        return string.replace("\\n", "\n").replace("\\r", "\r");
    }

    /**
     * Returns an empty {@link AppointmentBook} class instance.
     * @return an empty {@link AppointmentBook} class instance
     */
    private AppointmentBook emptyAppointmentBook() {
        return new AppointmentBook(owner);
    }

    /**
     * Closes file stream of an instance of {@link BufferedReader} class.
     * @param br           an instance of {@link BufferedReader} class
     * @throws IOException if fails to close file stream, it will throw IO Exception
     */
    private void closeStream(BufferedReader br) throws IOException {
        if (br != null) {
            br.close();
        }
    }

}
