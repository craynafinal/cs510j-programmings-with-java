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

    enum AppointmentField {
        DESCRIPTION(0), BEGIN_TIME(1), END_TIME(2);

        private final int value;
        private AppointmentField(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        static public int maxSize() {
            return values().length;
        }
    }

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
            if (!token.equals("appointmentbook")) {
                throw new ParserException("Does not start with a token \"appointmentbook\"" + lineNumber(line));
            }

            token = getNextToken(br);
            line++;

            if (token.equals("owner")) {
                token = getNextToken(br);
                line++;

                appointmentBook = new AppointmentBook(token);
            } else {
                throw new ParserException("Does not contain information regarding appointmentbook: " +
                        "found \"" + token + "\"" + lineNumber(line));
            }

            //System.out.println(appointmentBook.getOwnerName());

            token = getNextToken(br);
            line++;
            System.out.println(token);

            // checking appointment data
            while (token != null && !token.isEmpty()) {
                System.out.println("dd");
                if (token.equals("appointment")) {

                    appointment_data = new ArrayList<String>();

                    for (int i = 0; i < AppointmentField.maxSize(); i++) {
                        token = getNextToken(br);
                        line++;
                        System.out.println(token);
                        System.out.println("111");

                        try {
                            // check description, begin time, end time
                            if (token.equals("description")) {
                                token = getNextToken(br);
                                line++;

                                appointment_data.add(AppointmentField.DESCRIPTION.getValue(), token);
                            } else if (token.equals("begintime")) {
                                token = getNextToken(br);
                                line++;

                                appointment_data.add(AppointmentField.BEGIN_TIME.getValue(), token);
                            } else if (token.equals("endtime")) {
                                token = getNextToken(br);
                                line++;

                                appointment_data.add(AppointmentField.END_TIME.getValue(), token);
                            } else {
                                throw new ParserException("Unrecognized token: " + token + lineNumber(line));
                            }
                        } catch (NullPointerException e) {
                            throw new ParserException("Not enough information for an appointment" + lineNumber(line));
                        }
                    }

                    // using arraylist size to confirm data
                    // because each data has been inserted with a specific index
                    if (appointment_data.size() == AppointmentField.maxSize()) {
                        appointmentBook.addAppointment(new Appointment(
                                replaceNewLineCharacters(appointment_data.get(AppointmentField.DESCRIPTION.getValue())),
                                replaceNewLineCharacters(appointment_data.get(AppointmentField.BEGIN_TIME.getValue())),
                                replaceNewLineCharacters(appointment_data.get(AppointmentField.END_TIME.getValue()))));
                    }

                    System.out.println(appointmentBook.getAppointments().size());

                    token = getNextToken(br);
                    line++;
                } else {
                    throw new ParserException("Unrecognized token: " + token + lineNumber(line));
                }
            }

        } catch (IOException e) {

        } finally {
            // not yet implemented
            closeStream(br);
        }

        return appointmentBook;
    }

    private String replaceNewLineCharacters(String string) {
        return string.replace("\\n", "\n").replace("\\r", "\r");
    }

    public String[] testFunction() {
        File file = new File(filename);
        byte[] encoded = null;
        String fileContent = null;
        String[] tokens = null;
        String delims = "[ \n]+";

        try {
            encoded = Files.readAllBytes(Paths.get(filename));
        } catch (IOException e) {
            //return emptyAppointmentBook();
        }

        try {
            fileContent = new String(encoded, "UTF-8");
        } catch (UnsupportedEncodingException e) {

        } catch (NullPointerException e) {
            System.out.println("Nullpointer exception");
            return null;
        }

        return fileContent.split("\n");
    }

    private AppointmentBook emptyAppointmentBook() {
        return new AppointmentBook(owner);
    }

    private void closeStream(BufferedReader br) {
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                //Logger.getAnonymousLogger().severe("Unable to close reader.");
            }
        }
    }

}
