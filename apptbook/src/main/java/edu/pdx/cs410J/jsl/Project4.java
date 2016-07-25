package edu.pdx.cs410J.jsl;

import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.*;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.util.*;

/**
 * The main class that parses the command line and communicates with the
 * Appointment Book server using REST.
 */
public class Project4 {
    private static final int MAX_ARGUMENTS = 4;
    private static final String[] allowed_options = { "-README", "-print", "-host", "-port", "-search" };

    /**
     * Returns a readme option tag.
     *
     * @return
     */
    private static String getOptionReadme() {
        return allowed_options[0];
    }

    /**
     * Returns a print option tag.
     *
     * @return
     */
    private static String getOptionPrint() {
        return allowed_options[1];
    }

    /**
     * Returns a host option tag.
     *
     * @return
     */
    private static String getOptionHost() {
        return allowed_options[2];
    }

    /**
     * Returns a port option tag.
     *
     * @return
     */
    private static String getOptionPort() {
        return allowed_options[3];
    }

    /**
     * Returns a search option tag.
     *
     * @return
     */
    private static String getOptionSearch() {
        return allowed_options[4];
    }

    /**
     * This method will print a given error message
     * and will terminate the program execution with the exit code of 1.
     *
     * @param message a message of an error in string format
     */
    private static void programFail(String message) {
        System.err.println(message);
        System.exit(1);
    }

    /**
     * This method will check if a given command line argument is a recognizable option.
     *
     * @param option  a command line argument in string format
     * @return        true if a command line option is recognized, otherwise false
     */
    private static boolean isOption(String option) {
        return Arrays.asList(allowed_options).contains(option);
    }

    private static boolean isOptionHost(String option) {
        return option.equals(getOptionHost());
    }

    private static boolean isOptionPort(String option) {
        return option.equals(getOptionPort());
    }

    private static boolean isOptionSearch(String option) {
        return option.equals(getOptionSearch());
    }

    private static void checkNumberOfArguments(int target, int low, int high) {
        if (target < low) {
            programFail("Missing command line arguments");
        } else if (target > high) {
            programFail("Too many command line arguments");
        }
    }

    private static void checkDateFormatOfArguments(List<String> arguments, int low, int high) {
        // date format check
        for (String date : arguments.subList(low, high)) {
            if (!DateUtility.checkDateTimeFormat(date)) {
                programFail("Argument " + date + " is not in date format");
            }
        }
    }

    public static void main(String... args) {
        List<String> arguments = new ArrayList<String>();
        List<String> options = new ArrayList<String>();
        Appointment appointment = null;
        AppointmentBook appointment_book = null;

        AppointmentBookRestClient client = null;
        HttpRequestHelper.Response response = null;

        String host = null;
        int port = -1;

        // parsing the arguments
        for (int i = 0; i < args.length; i++) {
            switch (args[i].charAt(0)) {
                case '-':
                    // host argument
                    if (isOptionHost(args[i])) {
                        if (i < args.length - 1) {
                            options.add(args[i++]);
                            host = args[i];
                        } else {
                            programFail("Missing an argument for -host option");
                        }
                    // port argument
                    } else if (isOptionPort(args[i])) {
                        if (i < args.length - 1) {
                            options.add(args[i++]);
                            try {
                                port = Integer.parseInt(args[i]);
                            } catch (NumberFormatException e) {
                                programFail("Failed to parse " + args[i] + " to an integer");
                            }
                        } else {
                            programFail("Missing an argument for -port option");
                        }
                    } else if (isOption(args[i])) {
                        options.add(args[i]);
                    } else {
                        programFail("Option " + args[i] + " is not recognized");
                    }
                    break;
                default:
                    // date argument check
                    if (DateUtility.checkDateFormat(args[i]) &&
                            i < args.length - 2 &&
                            DateUtility.checkTimeFormat(args[i + 1]) &&
                            DateUtility.checkAMOrPM(args[i + 2])) {
                        arguments.add(args[i] + " " + args[i + 1] + " " + args[i + 2]);
                        i += 2;
                    // ordinary argument check
                    } else {
                        arguments.add(args[i]);
                    }
                    break;
            }
        }

        // readme comes first than other options
        if (options.contains(getOptionReadme())) {
            printReadMe();
            return;
        }

        // if either host or port is not provided, fail the program
        if (host == null ^ port == -1) {
            programFail("Both arguments for -host and -port options must be provided");
        }

        // if both are null, do not use server connection
        if (host == null && port == -1) {
            checkNumberOfArguments(arguments.size(), MAX_ARGUMENTS, MAX_ARGUMENTS);

            // date format check
            checkDateFormatOfArguments(arguments, 2, 4);

            // create an appointment
            try {
                appointment = new Appointment(arguments.get(1), arguments.get(2), arguments.get(3));
            } catch (ParseException e) {
                programFail("Failed to create an appointment");
            }

            appointment_book = new AppointmentBook(arguments.get(0));
            appointment_book.addAppointment(appointment);

        } else {

            client = new AppointmentBookRestClient(host, port);

            // checking the number of arguments
            if (options.contains(getOptionSearch())) {
                checkNumberOfArguments(arguments.size(), MAX_ARGUMENTS - 1, MAX_ARGUMENTS);

                // date format check
                if (arguments.size() == MAX_ARGUMENTS) {
                    checkDateFormatOfArguments(arguments, 2, 4);

                    // search for an appointment
                    try {
                        response = client.searchAppointment(arguments.get(0), arguments.get(2), arguments.get(3));
                    } catch (IOException e) {
                        programFail("Failed to connect to server: " + e);
                    }
                } else {
                    checkDateFormatOfArguments(arguments, 1, 3);

                    // search for an appointment
                    try {
                        response = client.searchAppointment(arguments.get(0), arguments.get(1), arguments.get(2));
                    } catch (IOException e) {
                        programFail("Failed to connect to server: " + e);
                    }
                }

                // pretty print the search result
                System.out.println(response.getContent());

            } else {
                checkNumberOfArguments(arguments.size(), MAX_ARGUMENTS, MAX_ARGUMENTS);

                // date format check
                checkDateFormatOfArguments(arguments, 2, 4);

                // create an appointment
                try {
                    response = client.createAppointment(arguments.get(0), arguments.get(1), arguments.get(2), arguments.get(3));
                } catch (IOException e) {
                    programFail("Failed to connect to server: " + e);
                }
            }
            checkResponseCode(HttpURLConnection.HTTP_OK, response);
        }

        printAppointment(options, appointment);
    }

    /**
     * Checks the option arguments and print a provided appointment if needed.
     *
     * @param options
     * @param appointment
     */
    private static void printAppointment(List<String> options, Appointment appointment) {
        if (options.contains(getOptionPrint()) && !options.contains(getOptionReadme()) && !options.contains(getOptionSearch())) {
            System.out.println(appointment);
        }
    }

    /**
     * Prints a README documentation3
     */
    private static void printReadMe() {
        System.out.println(
            "********************************************************\n" +
            "CS410/510J Advanced Java Programming\n" +
            "Project 4: Pretty Printing an Appointment Book\n" +
            "Student: Jong Seong Lee\n" +
            "********************************************************\n" +
            "\n" +
            "Usage: java edu.pdx.cs410J.jsl.Project4 [options] <args>\n" +
            "  args are (in this order):\n" +
            "    owner                    The person whose owns the appt book\n" +
            "    description              A description of the appointment\n" +
            "    beginTime                When the appt begins\n" +
            "    endTime                  When the appt ends\n" +
            "  options are (options may appear in any order):\n" +
            "    -host hostname           Host computer on which the server runs\n" +
            "    -port port               Port on which the server is listening\n" +
            "    -search                  Appointments should be searched for\n" +
            "    -print                   Prints a description of the new appointment\n" +
            "    -README                  Prints a README for this project and exits\n" +
            "  Date and time should be in the format: mm/dd/yyyy hh:mm a.\n\n" +
            "- General Information\n" +
            "This program sends HTTP requests to the specified server by host and port to handle creating " +
            "appointment books and appointment. It is required to use four arguments that are needed to create them: " +
            "a owner's name, a description, a begin time, and an end time.\n\n" +
            "- Options\n" +
            "-host hostname and -port port must be provided to make connection to a server. " +
            "If both are not provided, it is simply going to create an appointment in a local machine " +
            "and discard data after termination. If one of them is not provided while the other is, " +
            "the program will print an error message and exit.\n\n" +
            "With -search option, it is going to search appointments in an appointment book from a specified server, " +
            "and print the found ones, instead of adding a new one.\n\n" +
            "To print an information regarding an appointment created, " +
            "type -print, then the program will provide an appointment’s description, beginTime, " +
            "and endTime on the terminal screen.\n\n" +
            "If -README is provided, this program will perform nothing but printing this information " +
            "including program usage and description on the terminal screen."
        );
    }

    /**
     * Makes sure that the give response has the expected HTTP status code
     * @param code The expected status code
     * @param response The response from the server
     */
    private static void checkResponseCode( int code, HttpRequestHelper.Response response )
    {
        if (response.getCode() != code) {
            error(String.format("Expected HTTP code %d, got code %d.\n\n%s", code,
                                response.getCode(), response.getContent()));
        }
    }

    /**
     * Prints an error message.
     *
     * @param message
     */
    private static void error( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);

        System.exit(1);
    }

    /**
     * Prints usage information for this program and exits
     * @param message An error message to print
     */
    private static void usage( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);
        err.println();
        err.println("usage: java Project4 host port [key] [value]");
        err.println("  host    Host of web server");
        err.println("  port    Port of web server");
        err.println("  key     Key to query");
        err.println("  value   Value to add to server");
        err.println();
        err.println("This simple program posts key/value pairs to the server");
        err.println("If no value is specified, then all values are printed");
        err.println("If no key is specified, all key/value pairs are printed");
        err.println();

        System.exit(1);
    }
}