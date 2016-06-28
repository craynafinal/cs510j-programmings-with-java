package edu.pdx.cs410J.jsl;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The main class for the CS410J appointment book Project.
 * This class will receive command line arguments to create each instance of
 * the Appointment and the AppointmentBook classes, and it is going to add
 * an appointment to an appointment book. To do this, it will parse the
 * command line arguments and will interpret argument data and options.
 *
 * @author Jong Seong Lee
 * @version   %I%, %G%
 * @since     1.0
 */
public class Project1 {
  private static final int MAX_ARGUMENTS = 4;
  private static final String[] allowed_options = { "-print", "-README" };

  /**
   * The programFail method will print a message regarding an error
   * and will terminate the program execution with the exit code 1.
   *
   * @param message a message of an error in string format
     */
  private static void programFail(String message) {
    System.err.println(message);
    System.exit(1);
  }

  /**
   * The isOption method will check if a given command line option is recognizable.
   *
   * @param option  a command line option in string format
   * @return        it will return true if a command line option is recognized, otherwise false
     */
  private static boolean isOption(String option) {
    return Arrays.asList(allowed_options).contains(option);
  }

  /**
   * The isDateFormatCorrect method will use regex to check if a given date is in date format.
   *
   * @param date  a date in string format
   * @return      it will return true if a date is in date format, otherwise false
     */
  private static boolean isDateFormatCorrect(String date) {
    return date.matches("(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/[0-9]{4,4} ([01]?[0-9]|2[0-3]):[0-5]?[0-9]");
  }

  /**
   * The printReadMe will read the readme.txt document and print its content to standard output.
   */
  private static void printReadMe() {
    String line = null;
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Project1.class.getClassLoader().getResourceAsStream("readme.txt")));

    try {
      while ((line = bufferedReader.readLine()) != null) {
        System.out.println(line);
      }
    } catch (IOException e) {
      programFail("Readme I/O error found");
    }
  }

  /**
   * The main method will parse the command line arguments and will
   * create each instance of the Appointment and the AppointmentBook classes
   * to add an appointment to an appointment book.
   *
   * Arguments for an appointment and an appointment book must be provided in order.
   * They are a name of owner, a description, a begin time, and an end time.
   * The first two can be any string, but the last two has to be in date format.
   *
   * When -print option is given, it will print information regarding
   * an appointment which is created by arguments passed, description and begin / end times.
   *
   * When -README option is given, it will print readme text of this project.
   * This will explain what this program does and usage of this program.
   *
   * Following is the usage description from the project instruction document:
   *
   * Usage: java -jar target/apptbook-1.0-SNAPSHOT.jar [options] <args>
   *   args are (in this order):
   *   owner                    The person whose owns the appt book
   *   description              A description of the appointment
   *   beginTime                When the appt begins (24-hour time)
   *   endTime                  When the appt ends (24-hour time)
   * options are (options may appear in any order):
   *   -print                   Prints a description of the new appointment
   *   -README                  Prints a README for this project and exits
   * Date and time should be in the format: mm/dd/yyyy hh:mm
   *
   * @param args  a name of owner, a description, a begin time, a end time,
   *              and options -print, and -README
   *              those time arguments should be in date format of mm/dd/yyyy hh:mm
   *              year must be 4 digits and other fields may be 1 or 2 digits
     */
  public static void main(String[] args) {
    List<String> arguments = new ArrayList<String>();
    List<String> options = new ArrayList<String>();
    Appointment appointment = null;
    AppointmentBook appointment_book = null;

    for (int i = 0; i < args.length; i++) {
      switch (args[i].charAt(0)) {
        case '-':
          if (isOption(args[i])) {
            options.add(args[i]);
          } else {
            programFail("Option " + args[i] + " is not recognized");
          }
          break;
        default:
          arguments.add(args[i]);
          break;
      }
    }

    if (options.contains(allowed_options[1])) {
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
        if (!isDateFormatCorrect(date)) {
          programFail("Argument " + date + " is not in date format");
        }
      }

      appointment_book = new AppointmentBook(arguments.get(0));
      appointment = new Appointment(arguments.subList(1, arguments.size()));
      appointment_book.addAppointment(appointment);

      if (options.contains(allowed_options[0])) {
        System.out.println(appointment);
      }
    }
  }

}