package edu.pdx.cs410J.jsl;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.fail;

/**
 * Integration tests for the {@link Project3} main class.
 */
public class Project3IT extends InvokeMainTestCase {
  String owner;
  String description;
  String begin_time;
  String end_time;
  String print;
  String pretty;
  String read_me;
  String filename;
  String text_file;
  String empty_filename;
  String extra_filename;

  Appointment appointment;
  AppointmentBook appointment_book;

  @Before
  public void initialization() {
    owner = "owner";
    description = "description";
    begin_time = "11/11/1999 11:11 am";
    end_time = "11/11/1999 11:11 pm";
    print = "-print";
    pretty = "-pretty";
    read_me = "-README";
    filename = "/home/crayna/Downloads/test111.txt";
    text_file = "-textFile";
    empty_filename = "/home/crayna/Downloads/test222.txt";
    extra_filename = "/home/crayna/Downloads/test333.txt";

    // added try catch for ParseException because of Project 3
    try {
      appointment = new Appointment(description, begin_time, end_time);
    } catch (ParseException e) {
      fail("Failed to initialize an appointment");
    }
    appointment_book = null;
  }

  /**
   * Invokes the main method of {@link Project3} with the given arguments.
   */
  private MainMethodResult invokeMain(String... args) {
    return invokeMain( Project3.class, args );
  }

  /**
   * Tests that invoking the main method with no arguments issues an error.
   */
  @Test
  public void testNoCommandLineArguments() {
    MainMethodResult result = invokeMain();
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getErr(), containsString("Missing command line arguments"));
  }

  /**
   * It will check if the program fails when executed with too many arguments.
   */
  @Test
  public void testTooManyCommandLineArguments() {
    MainMethodResult result = invokeMain(owner, description, begin_time, end_time, owner);
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getErr(), containsString("Too many command line arguments"));
  }

  /**
   * It will check if the program fails when executed with an incorrect begin date.
   */
  @Test
  public void shouldFailWithIncorrectBeginDate() {
    String date = "wrong format";
    MainMethodResult result = invokeMain(owner, description, date, end_time);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString(date));
  }

  /**
   * It will check if the program fails when executed with an incorrect end date.
   */
  @Test
  public void shouldFailWithIncorrectEndDate() {
    String date = "wrong format";
    MainMethodResult result = invokeMain(owner, description, begin_time, date);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString(date));
  }

  /**
   * It will check if the program fails when executed with a 2 digit year date.
   */
  @Test
  public void shouldFailWith2DigitYearFormat() {
    String date = "11/11/11 14:00 am";
    MainMethodResult result = invokeMain(owner, description, date, end_time);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString(date));
  }

  /**
   * It will check if the program runs fine with a 24 hour format date.
   */
  @Test
  public void shouldWorkWith24HourFormat() {
    String date = "11/11/1111 14:00 am";
    MainMethodResult result = invokeMain(owner, description, date, end_time, print);
    assertThat(result.getExitCode(), is(equalTo(null)));
    assertThat(result.getOut(),
            containsString(getDateTimeString(date)));
  }

  /**
   * It will check if the program runs fine with a 12 hour format date.
   */
  @Test
  public void shouldWorkWith12HourFormat() {
    String date = "11/11/1111 1:00 am";
    MainMethodResult result = invokeMain(owner, description, date, end_time, print);
    assertThat(result.getExitCode(), is(equalTo(null)));
    assertThat(result.getOut(),
            containsString(getDateTimeString(date)));
  }

  /**
   * It will check if the program runs fine with a 1 digit month.
   */
  @Test
  public void shouldWorkWith1DigitMonthFormat() {
    String date = "1/11/1111 00:00 am";
    MainMethodResult result = invokeMain(owner, description, date, end_time, print);
    assertThat(result.getExitCode(), is(equalTo(null)));
    assertThat(result.getOut(),
            containsString(getDateTimeString(date)));
  }

  /**
   * It will check if the program runs fine with a 2 digit month.
   */
  @Test
  public void shouldWorkWith2DigitMonthFormat() {
    String date = "01/11/1999 00:00 am";
    MainMethodResult result = invokeMain(owner, description, date, end_time, print);
    assertThat(result.getExitCode(), is(equalTo(null)));
    assertThat(result.getOut(),
            containsString(getDateTimeString(date)));
  }

  /**
   * It will check if the program fails with a time over 23.
   */
  @Test
  public void shouldNotAllowHourOver23() {
    String date = "01/11/1111 24:00";
    MainMethodResult result = invokeMain(owner, description, date, end_time, print);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString(date));
  }

  /**
   * It will check if the program fails with a minute over 59.
   */
  @Test
  public void shouldNotAllowMinuteOver59() {
    String date = "01/11/1111 01:60";
    MainMethodResult result = invokeMain(owner, description, date, end_time, print);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString(date));
  }

  /**
   * It will check if the program fails with a month over 12.
   */
  @Test
  public void shouldNotAllowMonthOver12() {
    String date = "13/11/1111 01:01";
    MainMethodResult result = invokeMain(owner, description, date, end_time, print);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString(date));
  }

  /**
   * It will check if the program fails with a day over 13.
   */
  @Test
  public void shouldNotAllowDayOver31() {
    String date = "01/32/1111 01:01";
    MainMethodResult result = invokeMain(owner, description, date, end_time, print);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString(date));
  }

  /**
   * It will check if the program fails with a month value 0.
   */
  @Test
  public void shouldNotAllowMonth0() {
    String date = "0/11/1111 01:01";
    MainMethodResult result = invokeMain(owner, description, date, end_time, print);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString(date));
  }

  /**
   * It will check if the program fails with a day value 0.
   */
  @Test
  public void shouldNotAllowDay0() {
    String date = "01/0/1111 01:01";
    MainMethodResult result = invokeMain(owner, description, date, end_time, print);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString(date));
  }

  /**
   * It will check if the program prints information of an appointment with print option.
   */
  @Test
  public void printOptionShouldPrintAppointmentDescription() {
    MainMethodResult result = invokeMain(owner, description, begin_time, end_time, print);
    assertThat(result.getOut(), is(equalTo(appointment.toString() + "\n")));
  }

  /**
   * It will check if the program prints information when print option is at front.
   */
  @Test
  public void printOptionCouldBeAtFront() {
    MainMethodResult result = invokeMain(print, owner, description, begin_time, end_time);
    assertThat(result.getOut(), is(equalTo(appointment.toString() + "\n")));
  }

  /**
   * It will check if the program prints information when print option is in the middle.
   */
  @Test
  public void printOptionCouldBeInTheMiddle() {
    MainMethodResult result = invokeMain(owner, description, print, begin_time, end_time);
    assertThat(result.getOut(), is(equalTo(appointment.toString() + "\n")));
  }

  /**
   * It will check if the program does not print information without print option.
   */
  @Test
  public void appointmentDescriptionShouldNotPrintWithoutPrintOption() {
    MainMethodResult result = invokeMain(owner, description, begin_time, end_time);
    assertThat(result.getExitCode(), is(equalTo(null)));
    assertThat(result.getOut(), is(equalTo("")));
  }

  /**
   * It will check if the program prints readme when readme option is specified.
   */
  @Test
  public void printReadMeWhenReadMeOptionSpecified() {
    MainMethodResult result = invokeMain(read_me);
    assertThat(result.getExitCode(), is(equalTo(null)));
    assertThat(result.getOut(), containsString("README"));
  }

  /**
   * It will check if the program performs readme print even with other arguments and print option on.
   */
  @Test
  public void printReadMeShouldWorkEvenWithOtherArguments() {
    MainMethodResult result = invokeMain(read_me, owner, description, begin_time, end_time, print);
    assertThat(result.getExitCode(), is(equalTo(null)));
    assertThat(result.getOut(), containsString("README"));
  }

  /**
   * It will check if the program fails with a wrong option given.
   */
  @Test
  public void wrongOptionShouldFail() {
    MainMethodResult result = invokeMain(owner, description, begin_time, end_time, "-test");
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString("-test"));
  }

  /**
   * It will check if the program generates an empty appointment book when it cannot find the given file.
   */
  @Test
  public void wrongFileOptionShouldGenerateEmptyAppointmentBook() {
    deleteFile(empty_filename);
    String special_description = "description111";

    MainMethodResult result =
            invokeMain(owner, special_description, begin_time, end_time, print, text_file, empty_filename);
    assertThat(result.getExitCode(), is(equalTo(null)));
    assertThat(result.getOut(), containsString(special_description));
    assertThat(Files.exists(Paths.get(empty_filename)), is(equalTo(true)));
  }

  /**
   * Testing multi word dates
   */
  @Test
  public void twoDateStringShouldWork() {
    String beginDate = "12/12/1999";
    String beginTime = "00:00";
    String beginToken = "am";

    String endDate = "12/12/2000";
    String endTime = "11:11";
    String endToken = "pm";

    MainMethodResult result =
            invokeMain(owner, description, beginDate, beginTime, beginToken, endDate, endTime, endToken, print);
    System.out.println(result.getErr());
    assertThat(result.getExitCode(), is(equalTo(null)));
    assertThat(result.getOut(),
            containsString(getDateTimeString(beginDate + " " + beginTime + " " + beginToken)));
    assertThat(result.getOut(),
            containsString(getDateTimeString(endDate + " " + endTime + " " + endToken)));
  }

  /**
   * Testing mutl word end date missing
   */
  @Test
  public void twoDateStringMissingEndDate() {
    String beginDate = "12/12/1999";
    String beginTime = "00:00";
    String beginToken = "am";

    MainMethodResult result = invokeMain(owner, description, beginDate, beginTime, beginToken, print);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString("Missing"));
  }

  /**
   * It will check if the program works as expected when a file is given.
   */
  @Test
  public void shouldReadAndWriteToFileIfFileIsCorrect() {
    deleteFile(filename);
    TextDumper textDumper = new TextDumper(filename);

    String special_description = "description111";
    String special_begin_time = "11/11/2016 11:11 am";
    String temp = null;

    appointment_book = new AppointmentBook(owner);
    // added try catch for ParseException because of Project 3
    try {
      appointment_book.addAppointment(new Appointment(description, special_begin_time, end_time));
    } catch (ParseException e) {
      fail("Failed to initialize an appointment");
    }

    try {
      textDumper.dump(appointment_book);
    } catch (IOException e) {
      fail("IOException failed while writing to a file");
    }

    MainMethodResult result = invokeMain(owner, special_description, begin_time, end_time, print, text_file, filename);
    assertThat(result.getExitCode(), is(equalTo(null)));
    assertThat(Files.exists(Paths.get(filename)), is(equalTo(true)));

    temp = result.getOut();
    assertThat(temp, containsString(special_description));
    assertThat(temp,
            containsString(getDateTimeString(begin_time)));
  }

  /**
   * Deletes the provided file.
   *
   * @param filename  A name of a file to delete
     */
  private void deleteFile(String filename) {
    if (Files.exists(Paths.get(filename))) {
      try {
        Files.delete(Paths.get(filename));
      } catch (IOException e) {
        fail("Failed to delete the file:" + filename);
      }
    }
  }

  /**
   * Tests if a file generated by pretty printer.
   */
  @Test
  public void shouldPrettyPrintToFile() {
    deleteFile(filename);
    MainMethodResult result = invokeMain(owner, description, begin_time, end_time, pretty, filename);

    assertThat(result.getExitCode(), is(equalTo(null)));
    assertThat(Files.exists(Paths.get(filename)), is(equalTo(true)));
  }

  /**
   * Tests if pretty printed on standard output.
   */
  @Test
  public void shouldPrettyPrintToStandardOutputStream() {
    MainMethodResult result = invokeMain(owner, description, begin_time, end_time, pretty, "-");

    assertThat(result.getExitCode(), is(equalTo(null)));

    String temp = result.getOut();
    assertThat(temp, containsString(owner));
    assertThat(temp, containsString(description));
    assertThat(temp, containsString("Minutes"));
  }

  /**
   * Tests if pretty printed on standard output with print option.
   */
  @Test
  public void shouldPrettyPrintToStandardOutputStreamWithPrint() {
    MainMethodResult result = invokeMain(owner, description, begin_time, end_time, pretty, "-", print);

    assertThat(result.getExitCode(), is(equalTo(null)));

    String temp = result.getOut();
    assertThat(temp, containsString(owner));

    try {
      assertThat(temp, containsString(new Appointment(description, begin_time, end_time).toString()));
    } catch (ParseException e) {
      fail("Failed to initialize an appointment");
    }
  }

  /**
   * Tests if pretty printed on standard output with print and text file options.
   */
  @Test
  public void shouldPrettyPrintToStandardOutputStreamWithTextFileAndPrint() {
    deleteFile(filename);
    MainMethodResult result =
            invokeMain(owner, description, begin_time, end_time, pretty, "-", print, text_file, filename);

    assertThat(result.getExitCode(), is(equalTo(null)));

    String temp = result.getOut();
    assertThat(temp, containsString(owner));

    try {
      assertThat(temp, containsString(new Appointment(description, begin_time, end_time).toString()));
    } catch (ParseException e) {
      fail("Failed to initialize an appointment");
    }
  }

  /**
   * Tests if a file generated by pretty printer and print option should print the new appointment.
   */
  @Test
  public void shouldPrettyPrintToFileWithPrint() {
    deleteFile(filename);
    MainMethodResult result = invokeMain(owner, description, begin_time, end_time, pretty, filename, print);

    assertThat(result.getExitCode(), is(equalTo(null)));
    assertThat(Files.exists(Paths.get(filename)), is(equalTo(true)));

    String temp = result.getOut();
    assertThat(temp, not(containsString(owner)));
    assertThat(temp, containsString(description));
    assertThat(temp, not(containsString("Minutes")));
  }

  /**
   * Tests if a file generated by pretty printer and text file.
   */
  @Test
  public void shouldPrettyPrintToFileWithTextFile() {
    deleteFile(filename);
    deleteFile(extra_filename);
    MainMethodResult result =
            invokeMain(owner, description, begin_time, end_time, pretty, filename, text_file, extra_filename);

    assertThat(result.getExitCode(), is(equalTo(null)));
    assertThat(Files.exists(Paths.get(filename)), is(equalTo(true)));
    assertThat(Files.exists(Paths.get(extra_filename)), is(equalTo(true)));
  }

  /**
   * Tests if a file generated by pretty printer and text file,
   * and printer writes the appointment to standard output stream.
   */
  @Test
  public void shouldPrettyPrintToFileWithTextFileAndPrint() {
    deleteFile(filename);
    deleteFile(extra_filename);
    MainMethodResult result =
            invokeMain(owner, description, begin_time, end_time, pretty, filename, text_file, extra_filename, print);

    assertThat(result.getExitCode(), is(equalTo(null)));
    assertThat(Files.exists(Paths.get(filename)), is(equalTo(true)));
    assertThat(Files.exists(Paths.get(extra_filename)), is(equalTo(true)));

    String temp = result.getOut();
    assertThat(temp, not(containsString(owner)));
    assertThat(temp, containsString(description));
    assertThat(temp, not(containsString("Minutes")));
  }

  /**
   * Convert a date time string into a string filtered by <code>DateFormat.Short</code>.
   * @param dateTime  a date time format in mm/dd/yyyy hh:mm am/pm
   * @return
     */
  private String getDateTimeString(String dateTime) {
    DateFormat date_format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.ENGLISH);
    String dateTimeInFormat = null;
    try {
      dateTimeInFormat = date_format.parse(dateTime).toString();
    } catch (ParseException e) {
      fail("Failed to parse the date time string: " + dateTime);
    }

    return dateTimeInFormat;
  }

  /**
   * It will check if the program fails with no filename given.
   */
  @Test
  public void shouldFailWithoutFilename() {
    MainMethodResult result = invokeMain(owner, description, begin_time, end_time, print, text_file);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString("Missing"));
  }

  /**
   * It will check if the program fails when one of the arguments consumed as a text file path.
   */
  @Test
  public void shouldFailWithFileOptionInTheMiddleAndNoFilenameGiven() {
    MainMethodResult result = invokeMain(owner, description, text_file, begin_time, end_time, print);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString("Missing"));
  }

  /**
   * It will check if the program fails when one of the arguments consumed as a text file path
   * and using two word date format.
   */
  @Test
  public void shouldFailWithFileOptionInTheMiddleAndNoFilenameGivenTwoWordDateFormat() {
    MainMethodResult result =
            invokeMain(owner, description, text_file, "11/11/1999", "00:11", "am", "11/22/1234", "11:23", "am", print);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString("Too many command line arguments"));
  }

  /**
   * It should fail when the owner name in the file is different from the command line argument.
   */
  @Test
  public void shouldFailWhenOwnerNamesAreDifferent() {
    deleteFile(filename);
    TextDumper textDumper = new TextDumper(filename);

    String special_onwer = "different owner";
    String temp = null;

    appointment_book = new AppointmentBook(special_onwer);
    // added try catch for ParseException because of Project 3
    try {
      appointment_book.addAppointment(new Appointment(description, begin_time, end_time));
    } catch(ParseException e) {
      fail("Failed to initialize an appointment");
    }

    try {
      textDumper.dump(appointment_book);
    } catch (IOException e) {
      fail("IOException failed while writing to a file");
    }

    MainMethodResult result = invokeMain(owner, description, begin_time, end_time, print, text_file, filename);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(Files.exists(Paths.get(filename)), is(equalTo(true)));

    temp = result.getOut();
    assertThat(temp, containsString(owner));
    assertThat(temp, containsString(special_onwer));
  }
}