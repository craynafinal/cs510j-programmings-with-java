package edu.pdx.cs410J.jsl;

import edu.pdx.cs410J.ParserException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The main class for the CS410J appointment book Project.
 * This class will receive command line arguments to create each instance of
 * the {@link Appointment} and the {@link AppointmentBook} classes, and it is going to add
 * an appointment to an appointment book. To do this, it will parse the
 * command line arguments and will interpret argument data and options.
 *
 * - Regarding the Project3 update
 * This program has now a feature to read and write to a file. <code>Project3</code> will
 * utilize {@link TextDumper} and {@link TextParser} classes to read a file given
 * by a command line argument and will construct an appointment book along with a list of
 * appointments. Then, it is going to add a new appointment based on the command line argument,
 * and save back to the file to dump the appointment book including old and new information.
 *
 * @author Jong Seong Lee
 * @version   %I%, %G%
 * @since     1.0
 */
public class Project3 {
  private static final int MAX_ARGUMENTS = 4;
  private static HashMap<String, String> allowed_options = new HashMap<>();

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
   * This method will check if a given command line argument is a text file option.
   *
   * @param option  a command line argument in string format
   * @return        true if a command line option is -textFile, otherwise false
     */
  private static boolean isOptionTextFile(String option) {
    return option.equals(allowed_options.get("TextFile"));
  }

  /**
   * This method will check if a given command line argument is a recognizable option.
   *
   * @param option  a command line argument in string format
   * @return        true if a command line option is recognized, otherwise false
     */
  private static boolean isOption(String option) {
    return allowed_options.containsValue(option);
  }

  /**
   * This method will use regex to check if a given date time is in date time format.
   *
   * @param datetime  a date in string format
   * @return          true if a date is in date format, otherwise false
     */
  private static boolean isDateTimeFormatCorrect(String datetime) {
    return datetime.matches
            ("(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/[0-9]{4,4} ([01]?[0-9]|2[0-3]):[0-5]?[0-9] (am|AM|pm|PM)");
  }

  /**
   * This method will use regex to check if a given date is in date format.
   *
   * @param date  a date in string format
   * @return      true if a date is in date format, otherwise false
   */
  private static boolean isDateFormatCorrect(String date) {
    return date.matches("(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/[0-9]{4,4}");
  }

  /**
   * This method will use regex to check if a given date is in time format.
   *
   * @param time  a date in string format
   * @return      true if a date is in date format, otherwise false
   */
  private static boolean isTimeFormatCorrect(String time) {
    return time.matches("([01]?[0-9]|2[0-3]):[0-5]?[0-9]");
  }

  /**
   * This method will use regex to check if a given date is am, AM, pm, or PM.
   *
   * @param token a token in string format
   * @return      true if it is either am, AM, pm, or PM, otherwise false
     */
  private static boolean isAMOrPM(String token) {
    return token.matches("(am|AM|pm|PM)");
  }

  /**
   * This method will print the readme information to standard output.
   */
  private static void printReadMe() {
    System.out.println(
            "********************************************************\n" +
            "CS410/510J Advanced Java Programming\n" +
            "Project 2: Storing An Appointment Book in a Text File\n" +
            "Student: Jong Seong Lee\n" +
            "********************************************************\n" +
            "\n" +
            "Usage: java edu.pdx.cs410J.jsl.Project3 [options] <args>\n" +
            "  args are (in this order):\n" +
            "    owner                    The person whose owns the appt book\n" +
            "    description              A description of the appointment\n" +
            "    beginTime                When the appt begins (24-hour time)\n" +
            "    endTime                  When the appt ends (24-hour time)\n" +
            "  options are (options may appear in any order):\n" +
            "    -textFile                file Where to read/write the appointment book\n" +
            "    -print                   Prints a description of the new appointment\n" +
            "    -README                  Prints a README for this project and exits\n" +
            "  Date and time should be in the format: mm/dd/yyyy hh:mm\n\n" +
            "- General Information\n" +
            "This program will create an appointment book that belongs to an owner;" +
            "the owner’s name is given in the command line argument. " +
            "Then it is going to create an appointment to be added in the appointment book that is created. " +
            "For this, it will take three command line arguments: " +
            "description, beginTime, and endTime to describe an appointment.\n\n" +
            "- Options\n" +
            "To print an information regarding an appointment created, " +
            "type -print, then the program will provide an appointment’s description, beginTime, " +
            "and endTime on the terminal screen.\n\n" +
            "If -README is provided, this program will perform nothing but printing this information " +
            "including program usage and description on the terminal screen.\n\n" +
            "With -textFile option provided, this program will read the file given in the command line argument " +
            "and build an appointment book with a list of appointments based on the file content. " +
            "If the file is not found, it will create an appointment book with no appointments. " +
            "The important thing is that the owner name should match between the one in the file " +
            "and the command line argument. Then, it is going to add an appointment created based on the command" +
            "line argument to the appointment book, and save it back to the file given.");
  }

  /**
   * This main method will parse the command line arguments and will
   * create each instance of the {@link Appointment} and the {@link AppointmentBook} classes
   * to add an appointment to an appointment book.
   *
   * Arguments for an appointment and an appointment book must be provided in order.
   * They are a name of owner and a description, a begin time, and an end time of an appointment.
   * The first two can be any string, but the last two has to be in date format.
   *
   * When "-textFile file" option is given, it will read the file given and
   * it is going to build an appointment book based on the file content. The owner name in the file content
   * and the command line argument should match; otherwise the program will fail. If file is not found,
   * it is going to create an appointment book with no appointments associated. Then, it is going to
   * add a new appointment based on the command line arguments to the appointment book, and the program will dump
   * its information back to the file.
   *
   * When -print option is given, it will print information regarding
   * an appointment which is created by arguments passed, description and begin / end times.
   *
   * When -README option is given, it will print readme text of this project.
   * This will explain what this program does and usage of this program.
   *
   * Following is the usage description from the project instruction document:
   *
   * Usage: java edu.pdx.cs410J.jsl.Project3 [options] <args>
   *   args are (in this order):
   *   owner                    The person whose owns the appt book
   *   description              A description of the appointment
   *   beginTime                When the appt begins (24-hour time)
   *   endTime                  When the appt ends (24-hour time)
   * options are (options may appear in any order):
   *   -textFile                file Where to read/write the appointment book
   *   -print                   Prints a description of the new appointment
   *   -README                  Prints a README for this project and exits
   * Date and time should be in the format: mm/dd/yyyy hh:mm
   *
   * @param args  a name of owner, a description, a begin time, a end time,
   *              and options "-textFile file", -print, and -README
   *              those time arguments should be in date format of mm/dd/yyyy hh:mm
   *              year must be 4 digits and other fields may be 1 or 2 digits
     */
  public static void main(String[] args) {
    List<String> arguments = new ArrayList<String>();
    List<String> options = new ArrayList<String>();
    Appointment appointment = null;
    AppointmentBook appointment_book = null;

    String filename = null;
    TextParser textParser = null;
    TextDumper textDumper = null;

    String date_time_temp = null;

    initOptions();

    for (int i = 0; i < args.length; i++) {
      switch (args[i].charAt(0)) {
        case '-':
          if (isOptionTextFile(args[i])) {
            if (i < args.length - 1) {
              options.add(args[i++]);
              filename = args[i];
            } else {
              programFail("Missing a filename argument");
            }
          } else if (isOption(args[i])) {
            options.add(args[i]);
          } else {
            programFail("Option " + args[i] + " is not recognized");
          }
          break;
        default:
          if (isDateFormatCorrect(args[i]) &&
                  i < args.length - 2 &&
                  isTimeFormatCorrect(args[i + 1]) &&
                  isAMOrPM(args[i + 2])) {
              arguments.add(args[i] + " " + args[i + 1] + " " + args[i + 2]);
              i += 2;
          } else {
            arguments.add(args[i]);
          }
          break;
      }
    }

    if (options.contains(allowed_options.get("ReadMe"))) {
      printReadMe();
    } else {
      // number of arguments check
      if (arguments.size() < MAX_ARGUMENTS) {
        programFail("Missing command line arguments");
      } else if (arguments.size() > MAX_ARGUMENTS) {
        programFail("Too many command line arguments");
      }

      // date format check
      for (String date : arguments.subList(2, 4)) {
        if (!isDateTimeFormatCorrect(date)) {
          programFail("Argument " + date + " is not in date format");
        }
      }

      // create an appointment
      if (options.contains(allowed_options.get("TextFile"))) {
        textParser = new TextParser(filename, arguments.get(0));
        try {
          appointment_book = (AppointmentBook) textParser.parse();
        } catch (ParserException e) {
          System.out.println(e.getMessage());
          programFail(e.getMessage());
        }
      } else {
        appointment_book = new AppointmentBook(arguments.get(0));
      }

      // add a new appointment
      // added try catch for ParseException because of Project 3
      try {
        appointment = new Appointment(arguments.subList(1, arguments.size()));
      } catch (ParseException e) {
        programFail(e.getMessage());
      }
      appointment_book.addAppointment(appointment);

      // save back to file
      if (options.contains(allowed_options.get("TextFile"))) {
        textDumper = new TextDumper(filename);
        try {
          textDumper.dump(appointment_book);
        } catch (IOException e) {
          programFail(e.getMessage());
        }
      }

      // print it out if option is on
      if (options.contains(allowed_options.get("Print"))) {
        for (Appointment app: appointment_book.getAppointments()) {
          System.out.println(app);
        }
      }
    }
  }

  /**
   * This method will initialize the hashmap contains recognizable options.
   * It is implemented in a hashmap style so the options can be accessed via string indice.
   */
  private static void initOptions() {
    allowed_options.put("TextFile", "-textFile");
    allowed_options.put("Print", "-print");
    allowed_options.put("ReadMe", "-README");
  }

}