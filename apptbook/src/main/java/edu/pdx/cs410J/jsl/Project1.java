package edu.pdx.cs410J.jsl;

import edu.pdx.cs410J.AbstractAppointmentBook;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * The main class for the CS410J appointment book Project
 */
public class Project1 {
  private static final int MAX_ARGUMENTS = 4;
  private static final String[] allowed_options = { "-print", "-README" };
  private static final String date_format = "MM/dd/YYYY kk:mm";

  private static void programFail(String message) {
    System.err.println(message);
    System.exit(1);
  }

  private static boolean isOption(String option) {
    return Arrays.asList(allowed_options).contains(option);
  }

  public static void main(String[] args) {
    List<String> arguments = new ArrayList<String>();
    List<String> options = new ArrayList<String>();
    Appointment appointment;
    AppointmentBook appointment_book;
    Date date;

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

    // number of arguments check
    if (arguments.size() < MAX_ARGUMENTS) {
      programFail("Missing command line arguments");
    } else if (arguments.size() > MAX_ARGUMENTS) {
      programFail("Too many command line arguments");
    }

    // date format check
    try {
      SimpleDateFormat format = new SimpleDateFormat(date_format);
      format.setLenient(true);
      date = format.parse(arguments.get(2));
    } catch (ParseException e) {
      programFail("Argument " + arguments.get(2) + " is not in date format");
    }

    appointment_book = new AppointmentBook(arguments.get(0));
    appointment = new Appointment(arguments.subList(1, arguments.size()));
    appointment_book.addAppointment(appointment);

    if (options.contains(allowed_options[0])) {
      System.out.println(appointment);
    }

    if (options.contains(allowed_options[1])) {
      // print readme here
    }
  }

}