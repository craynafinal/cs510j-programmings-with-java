package edu.pdx.cs410J.jsl;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for the {@link Project2} main class.
 */
public class Project2IT extends InvokeMainTestCase {
  String owner;
  String description;
  String begin_time;
  String end_time;
  String print;
  String read_me;

  Appointment appointment;

  @Before
  public void initialization() {
    owner = "owner";
    description = "description";
    begin_time = "01/01/0001 01:0";
    end_time = "11/11/1111 11:11";
    print = "-print";
    read_me = "-README";

    appointment = new Appointment(description, begin_time, end_time);
  }

  /**
   * Invokes the main method of {@link Project2} with the given arguments.
   */
  private MainMethodResult invokeMain(String... args) {
    return invokeMain( Project2.class, args );
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
    String date = "11/11/11 14:00";
    MainMethodResult result = invokeMain(owner, description, date, end_time);
    assertThat(result.getExitCode(), is(equalTo(1)));
    assertThat(result.getErr(), containsString(date));
  }

  /**
   * It will check if the program runs fine with a 24 hour format date.
   */
  @Test
  public void shouldWorkWith24HourFormat() {
    String date = "11/11/1111 14:00";
    MainMethodResult result = invokeMain(owner, description, date, end_time, print);
    assertThat(result.getExitCode(), is(equalTo(null)));
    assertThat(result.getOut(), containsString(date));
  }

  /**
   * It will check if the program runs fine with a 12 hour format date.
   */
  @Test
  public void shouldWorkWith12HourFormat() {
    String date = "11/11/1111 1:00";
    MainMethodResult result = invokeMain(owner, description, date, end_time, print);
    assertThat(result.getExitCode(), is(equalTo(null)));
    assertThat(result.getOut(), containsString(date));
  }

  /**
   * It will check if the program runs fine with a 1 digit month.
   */
  @Test
  public void shouldWorkWith1DigitMonthFormat() {
    String date = "1/11/1111 00:00";
    MainMethodResult result = invokeMain(owner, description, date, end_time, print);
    assertThat(result.getExitCode(), is(equalTo(null)));
    assertThat(result.getOut(), containsString(date));
  }

  /**
   * It will check if the program runs fine with a 2 digit month.
   */
  @Test
  public void shouldWorkWith2DigitMonthFormat() {
    String date = "01/11/1111 00:00";
    MainMethodResult result = invokeMain(owner, description, date, end_time, print);
    assertThat(result.getExitCode(), is(equalTo(null)));
    assertThat(result.getOut(), containsString(date));
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
}