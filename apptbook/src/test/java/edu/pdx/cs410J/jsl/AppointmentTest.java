package edu.pdx.cs410J.jsl;

import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link Appointment} class.
 */
public class AppointmentTest {
  Appointment appointment = null;
  String description = null;
  String begin_time = null;
  String end_time = null;

  @Before
  public void appointmentSetup() {
    description = "test description";
    begin_time = "11/11/1999 11:11 am";
    end_time = "11/11/1999 11:11 pm";

    appointment = createAppointment(description, begin_time, end_time);
  }

  /**
   * Creates an appointment instance.
   */
  private Appointment createAppointment(String description, String beginTime, String endTime) {
    // added try catch for ParseException because of Project 3
    try {
      return new Appointment(description, beginTime, endTime);
    } catch (ParseException e) {
      fail("Failed to initialize an appointment");
    }

    return null;
  }

  /**
   * It will check if a description of an appointment is assigned correctly.
   */
  @Test
  public void getDescriptionNeedsToBeImplemented() {
    assertThat(appointment.getDescription(), is(equalTo(description)));
  }

  /**
   * It will check if a begin time of an appointment is assigned correctly.
   */
  @Test
  public void getTimeStringNeedsToBeImplemented() {
    assertThat(appointment.getBeginTimeString(), containsString(getDateTimeString(begin_time)));
    assertThat(appointment.getEndTimeString(), containsString(getDateTimeString(end_time)));
  }

  /**
   * The get input methods should return raw time inputs.
   */
  @Test
  public void getInputMethodsShouldReturnRawInputs() {
    assertThat(appointment.getBeginTimeInput(), is(equalTo(begin_time)));
    assertThat(appointment.getEndTimeInput(), is(equalTo(end_time)));
  }

  /**
   * The duration of an appointment should be correct.
   */
  @Test
  public void durationOfAppointmentShouldBeCorrect() {
    assertThat(appointment.getDurationInMinutes(), is(equalTo(12*60)));
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
}
