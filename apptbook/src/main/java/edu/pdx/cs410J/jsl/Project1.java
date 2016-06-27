package edu.pdx.cs410J.jsl;

import edu.pdx.cs410J.AbstractAppointmentBook;

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
    return date.matches("[0-9]+/[0-9]+/[0-9]{4,4} [0-9]+:[0-9]+");
  }

  private static void printReadMe() {
    System.out.println(
            "********************************************************\n" +
            "CS410/510J Advanced Java Programming\n" +
            "Project 1: Designing an Appointment Book Application\n" +
            "Student: Jong Seong Lee\n" +
            "********************************************************\n" +
            "Usage: java -jar target/apptbook-1.0-SNAPSHOT.jar [options] <args>\n" +
            "\targs are (in this order):\n" +
            "\t\towner\t\t\t\tThe person whose owns the appt book\n" +
            "\t\tdescription\t\t\t\tA description of the appointment\n" +
            "\t\tbeginTime\t\t\t\tWhen the appt begins (24-hour time)\n" +
            "\t\tendTime\t\t\t\tWhen the appt ends (24-hour time)\n" +
            "\toptions are (options may appear in any order):\n" +
            "\t\t-print\t\t\t\tPrints a description of the new appointment\n" +
            "\t\t-README\t\t\t\tPrints a README for this project and exits\n" +
            "\tDate and time should be in the format: mm/dd/yyyy hh:mm\n\n" +
            "1. Appointment\n" +
            "A simple class that describes a appointment by description, begin time and end time." +
            "This class can be initialized with those arguments," +
            "but also it is possible to ignore them to use default data." +
            "There are not much interesting methods implemented other than get methods.\n" +
            "\n" +
            "2. AppointmentBook\n" +
            "Another simple class that describes the owner of appointments and contains a list of appointments." +
            "This class can be initialized with a name of owner or use a default name." +
            "This class will use Appointment class to store a list of appointments.\n" +
            "\n" +
            "3. Project1\n" +
            "The class that contains main method to utilize both Appointment and AppointmentBook classes." +
            "It will parse the command line arguments and create an Appointment" +
            "and an AppointmentBook instances to add the new appointment to the appointment book." +
            "When print option is specified, it will print the description of a new appointment." +
            "When readme option is specified, it will print this readme document.");
  }

  public static void main(String[] args) {
    List<String> arguments = new ArrayList<String>();
    List<String> options = new ArrayList<String>();
    Appointment appointment;
    AppointmentBook appointment_book;

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