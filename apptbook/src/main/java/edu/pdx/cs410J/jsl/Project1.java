package edu.pdx.cs410J.jsl;

import edu.pdx.cs410J.AbstractAppointmentBook;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The main class for the CS410J appointment book Project
 */
public class Project1 {
  private static final int MAX_ARGUMENTS = 4;
  private static final String[] allowed_options = { "-print", "-README" };

  private static void programFail(String message) {
    System.err.println(message);
    System.exit(1);
  }

  private static boolean isOption(String option) {
    return Arrays.asList(allowed_options).contains(option);
  }

  private static boolean isDateFormatCorrect(String date) {
    //return date.matches("[0-9]+/[0-9]+/[0-9]{4,4} [0-9]+:[0-9]+");
    return date.matches("(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/[0-9]{4,4} ([01]?[0-9]|2[0-3]):[0-5]?[0-9]");
  }

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