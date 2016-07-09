package edu.pdx.cs410J.jsl;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookParser;
import edu.pdx.cs410J.ParserException;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TextParser implements AppointmentBookParser {

    private String filename = null;
    private String owner = "default owner";

    public TextParser(String filename) {
        this.filename = filename;
    }

    public TextParser(String filename, String owner) {
        this.filename = filename;
        this.owner = owner;
    }

    public String getFileName() {
        return filename;
    }

    private String lineNumber(int line) {
        return " - Line Number " + line;
    }

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
            if (!token.equals("--appointmentbook")) {
                throw new ParserException("Does not start with a token \"appointmentbook\"" + lineNumber(line));
            }

            token = getNextToken(br);
            line++;

            if (token.equals("---owner")) {
                token = getNextToken(br);
                line++;

                appointmentBook = new AppointmentBook(token);
            } else {
                throw new ParserException("Does not contain information regarding appointmentbook: " +
                        "found \"" + token + "\"" + lineNumber(line));
            }

            token = getNextToken(br);
            line++;

            // checking appointment data
            while (token != null && !token.isEmpty()) {
                if (token.equals("--appointment")) {

                    appointment_data = new ArrayList<String>();

                    for (int i = 0; i < 3; i++) {
                        token = getNextToken(br);
                        line++;

                        try {
                            // check description, begin time, end time
                            if (token.equals("---description")) {
                                token = getNextToken(br);
                                line++;

                                appointment_data.add(0, token);
                            } else if (token.equals("---begintime")) {
                                token = getNextToken(br);
                                line++;

                                appointment_data.add(1, token);
                            } else if (token.equals("---endtime")) {
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

    private String replaceNewLineCharacters(String string) {
        return string.replace("\\n", "\n").replace("\\r", "\r");
    }

    private AppointmentBook emptyAppointmentBook() {
        return new AppointmentBook(owner);
    }

    private void closeStream(BufferedReader br) throws IOException {
        if (br != null) {
            br.close();
        }
    }

}
