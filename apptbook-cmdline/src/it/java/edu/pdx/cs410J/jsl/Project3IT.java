package edu.pdx.cs410J.jsl;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
  static final private String owner = "owner";
  static final private String description = "description";
  static final private String begin_time = "11/11/1999 11:11 am";
  static final private String end_time = "11/11/1999 11:11 pm";
  static final private String print = "-print";
  static final private String pretty = "-pretty";
  static final private String read_me = "-README";
  static final private String filename = "/home/crayna/Downloads/test111.txt";
  static final private String text_file = "-textFile";
  static final private String empty_filename = "/home/crayna/Downloads/test222.txt";
  static final private String extra_filename = "/home/crayna/Downloads/test333.txt";

  private Appointment appointment = null;
  private AppointmentBook appointment_book = null;

  @Before
  public void initialization() {
    try {
      appointment = new Appointment(description, begin_time, end_time);
    } catch (ParseException e) {
      fail("Failed to initialize an appointment");
    }
  }

  private MainMethodResult invokeMain(String... args) {
    return invokeMain( Project3.class, args );
  }

  @Test
  public void testNoCommandLineArguments() {
    MainMethodResult result = invokeMain();
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getErr(), containsString("Missing command line arguments"));
  }

  @Test
  public void testTooManyCommandLineArguments() {
    MainMethodResult result = invokeMain(owner, description, begin_time, end_time, owner);
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getErr(), containsString("Too many command line arguments"));
  }

  @Test
  public void shouldFailWithIncorrectBeginDate() {
    String date = "wrong format";
    MainMethodResult result = invokeMain(owner, description, date, end_time);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString(date));
  }

  @Test
  public void shouldFailWithIncorrectEndDate() {
    String date = "wrong format";
    MainMethodResult result = invokeMain(owner, description, begin_time, date);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString(date));
  }

  @Test
  public void shouldFailWith2DigitYearFormat() {
    String date = "11/11/11 14:00 am";
    MainMethodResult result = invokeMain(owner, description, date, end_time);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString(date));
  }

  @Test
  public void shouldNotWorkWith24HourFormat() {
    String date = "11/11/1111 14:00 am";
    MainMethodResult result = invokeMain(owner, description, date, end_time, print);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(),
            containsString(date));
  }

  @Test
  public void shouldWorkWith12HourFormat() {
    String date = "11/11/1111 1:00 am";
    MainMethodResult result = invokeMain(owner, description, date, end_time, print);
    assertThat(result.getExitCode(), is(equalTo(null)));
    assertThat(result.getOut(),
            containsString(getDateTimeString(date)));
  }

  @Test
  public void shouldWorkWith1DigitMonthFormat() {
    String date = "1/11/1111 00:00 am";
    MainMethodResult result = invokeMain(owner, description, date, end_time, print);
    assertThat(result.getExitCode(), is(equalTo(null)));
    assertThat(result.getOut(),
            containsString(getDateTimeString(date)));
  }

  @Test
  public void shouldWorkWith2DigitMonthFormat() {
    String date = "01/11/1999 00:00 am";
    MainMethodResult result = invokeMain(owner, description, date, end_time, print);
    assertThat(result.getExitCode(), is(equalTo(null)));
    assertThat(result.getOut(),
            containsString(getDateTimeString(date)));
  }

  @Test
  public void shouldNotAllowHourOver23() {
    String date = "01/11/1111 24:00";
    MainMethodResult result = invokeMain(owner, description, date, end_time, print);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString(date));
  }

  @Test
  public void shouldNotAllowMinuteOver59() {
    String date = "01/11/1111 01:60";
    MainMethodResult result = invokeMain(owner, description, date, end_time, print);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString(date));
  }

  @Test
  public void shouldNotAllowMonthOver12() {
    String date = "13/11/1111 01:01";
    MainMethodResult result = invokeMain(owner, description, date, end_time, print);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString(date));
  }

  @Test
  public void shouldNotAllowDayOver31() {
    String date = "01/32/1111 01:01";
    MainMethodResult result = invokeMain(owner, description, date, end_time, print);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString(date));
  }

  @Test
  public void shouldNotAllowMonth0() {
    String date = "0/11/1111 01:01";
    MainMethodResult result = invokeMain(owner, description, date, end_time, print);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString(date));
  }

  @Test
  public void shouldNotAllowDay0() {
    String date = "01/0/1111 01:01";
    MainMethodResult result = invokeMain(owner, description, date, end_time, print);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString(date));
  }

  @Test
  public void printOptionShouldPrintAppointmentDescription() {
    MainMethodResult result = invokeMain(owner, description, begin_time, end_time, print);
    assertThat(result.getOut(), is(equalTo(appointment.toString() + "\n")));
  }

  @Test
  public void printOptionCouldBeAtFront() {
    MainMethodResult result = invokeMain(print, owner, description, begin_time, end_time);
    assertThat(result.getOut(), is(equalTo(appointment.toString() + "\n")));
  }

  @Test
  public void printOptionCouldBeInTheMiddle() {
    MainMethodResult result = invokeMain(owner, description, print, begin_time, end_time);
    assertThat(result.getOut(), is(equalTo(appointment.toString() + "\n")));
  }

  @Test
  public void appointmentDescriptionShouldNotPrintWithoutPrintOption() {
    MainMethodResult result = invokeMain(owner, description, begin_time, end_time);
    assertThat(result.getExitCode(), is(equalTo(null)));
    assertThat(result.getOut(), is(equalTo("")));
  }

  @Test
  public void printReadMeWhenReadMeOptionSpecified() {
    MainMethodResult result = invokeMain(read_me);
    assertThat(result.getExitCode(), is(equalTo(null)));
    assertThat(result.getOut(), containsString("README"));
  }

  @Test
  public void printReadMeShouldWorkEvenWithOtherArguments() {
    MainMethodResult result = invokeMain(read_me, owner, description, begin_time, end_time, print);
    assertThat(result.getExitCode(), is(equalTo(null)));
    assertThat(result.getOut(), containsString("README"));
  }

  @Test
  public void wrongOptionShouldFail() {
    MainMethodResult result = invokeMain(owner, description, begin_time, end_time, "-test");
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString("-test"));
  }

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

  @Test
  public void twoDateStringMissingEndDate() {
    String beginDate = "12/12/1999";
    String beginTime = "00:00";
    String beginToken = "am";

    MainMethodResult result = invokeMain(owner, description, beginDate, beginTime, beginToken, print);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString("Missing"));
  }

  @Test
  public void shouldReadAndWriteToFileIfFileIsCorrect() {
    deleteFile(filename);
    TextDumper textDumper = new TextDumper(filename);

    String special_description = "description111";
    String special_begin_time = "11/11/2016 11:11 am";
    String temp = null;

    appointment_book = new AppointmentBook(owner);

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

  private void deleteFile(String filename) {
    if (Files.exists(Paths.get(filename))) {
      try {
        Files.delete(Paths.get(filename));
      } catch (IOException e) {
        fail("Failed to delete the file:" + filename);
      }
    }
  }
  @Test
  public void shouldPrettyPrintToFile() {
    deleteFile(filename);
    MainMethodResult result = invokeMain(owner, description, begin_time, end_time, pretty, filename);

    assertThat(result.getExitCode(), is(equalTo(null)));
    assertThat(Files.exists(Paths.get(filename)), is(equalTo(true)));
  }

  @Test
  public void shouldPrettyPrintToStandardOutputStream() {
    MainMethodResult result = invokeMain(owner, description, begin_time, end_time, pretty, "-");

    assertThat(result.getExitCode(), is(equalTo(null)));

    String temp = result.getOut();
    assertThat(temp, containsString(owner));
    assertThat(temp, containsString(description));
    assertThat(temp, containsString("Minutes"));
  }

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

  @Test
  public void shouldPrettyPrintGiveSortedOutput() {
    deleteFile(filename);

    String ownerName = "My owner";
    String desc = "My description";
    String[] beginTime = { "1/1/2016 1:00 AM", "1/2/2016 1:00 AM", "1/3/2016 1:00 AM", "1/4/2016 1:00 AM", "1/5/2016 1:00 AM" };
    String[] endTime = { "1/1/2016 1:00 PM", "1/2/2016 1:00 PM", "1/3/2016 1:00 PM", "1/4/2016 1:00 PM", "1/5/2016 1:00 PM" };
    int i, max = 5, start = 1, end = 3;

    MainMethodResult result = null;

    for (i = 0; i < max; i++) {
      result = invokeMain(ownerName, desc + (i + 1), beginTime[i], endTime[i], text_file, filename, pretty, "-", print);
      assertThat(result.getExitCode(), is(equalTo(null)));
    }

    String temp = result.getOut();
    assertThat(temp, containsString(ownerName));

    for (i = start; i < end; i++) {
      assertThat(result.getOut(), containsString(desc + (i + 1)));
      assertThat(result.getOut(), containsString(getDateFormattedString(beginTime[i])));
      assertThat(result.getOut(), containsString(getDateFormattedString(endTime[i])));
    }
  }

  private String getDateFormattedString(String string) {
    DateFormat date_format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.ENGLISH);
    SimpleDateFormat pretty_format = new SimpleDateFormat("mm/dd/yyyy 'at' HH:mm a z");

    String ret = null;

    try {
      ret = pretty_format.format(date_format.parse(string));
    } catch (ParseException e) {
      fail(e.getMessage());
    }

    return ret;
  }

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

  @Test
  public void shouldFailWithoutFilename() {
    MainMethodResult result = invokeMain(owner, description, begin_time, end_time, print, text_file);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString("Missing"));
  }

  @Test
  public void shouldFailWithFileOptionInTheMiddleAndNoFilenameGiven() {
    MainMethodResult result = invokeMain(owner, description, text_file, begin_time, end_time, print);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString("Missing"));
  }

  @Test
  public void shouldFailWithFileOptionInTheMiddleAndNoFilenameGivenTwoWordDateFormat() {
    MainMethodResult result =
            invokeMain(owner, description, text_file, "11/11/1999", "00:11", "am", "11/22/1234", "11:23", "am", print);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString("Too many command line arguments"));
  }

  @Test
  public void shouldFailWhenOwnerNamesAreDifferent() {
    deleteFile(filename);
    TextDumper textDumper = new TextDumper(filename);

    String special_onwer = "different owner";
    String temp = null;

    appointment_book = new AppointmentBook(special_onwer);
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