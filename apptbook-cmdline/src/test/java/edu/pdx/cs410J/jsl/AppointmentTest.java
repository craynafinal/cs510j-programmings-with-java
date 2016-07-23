package edu.pdx.cs410J.jsl;

import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

  Appointment a1 = null;
  Appointment a2 = null;
  Appointment a3 = null;
  Appointment a4 = null;
  Appointment a5 = null;
  Appointment a6 = null;
  Appointment a7 = null;

  List<Appointment> list = null;

  @Before
  public void appointmentSetup() {
    description = "test description";
    begin_time = "11/11/1999 11:11 am";
    end_time = "11/11/1999 11:11 pm";

    appointment = createAppointment(description, begin_time, end_time);

    list = new ArrayList<>();
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

  /**
   * Check ordering of <code>Appointment</code> instances using descriptions.
   */
  @Test
  public void checkAppointmentOrderingDescription() {
    try {
      a1 = new Appointment("z", "11/11/1999 11:11 am", "11/11/1999 11:11 pm");
      a2 = new Appointment("b", "11/11/1999 11:11 am", "11/11/1999 11:11 pm");
      a3 = new Appointment("c", "11/11/1999 11:11 am", "11/11/1999 11:11 pm");
      a4 = new Appointment("d", "11/11/1999 11:11 am", "11/11/1999 11:11 pm");
      a5 = new Appointment("e", "11/11/1999 11:11 am", "11/11/1999 11:11 pm");
      a6 = new Appointment("a", "11/11/1999 11:11 am", "11/11/1999 11:11 pm");
      a7 = new Appointment("x", "11/11/1999 11:11 am", "11/11/1999 11:11 pm");
    } catch (ParseException e) {
      fail("Failed to initialize an appointment");
    }

    addAppointmentsToListAndSort();

    assertThat(list.get(0), is(equalTo(a6)));
    assertThat(list.get(1), is(equalTo(a2)));
    assertThat(list.get(2), is(equalTo(a3)));
    assertThat(list.get(3), is(equalTo(a4)));
    assertThat(list.get(4), is(equalTo(a5)));
    assertThat(list.get(5), is(equalTo(a7)));
    assertThat(list.get(6), is(equalTo(a1)));
  }

  /**
   * Add appointments to the list and sort.
   */
  private void addAppointmentsToListAndSort() {
    list.add(a1);
    list.add(a2);
    list.add(a3);
    list.add(a4);
    list.add(a5);
    list.add(a6);
    list.add(a7);

    Collections.sort(list);
  }

  /**
   * Check ordering of <code>Appointment</code> instances using begin times.
   */
  @Test
  public void checkAppointmentOrderingBeginTime() {

    try {
      a1 = new Appointment("a", "11/11/2000 11:11 am", "11/11/1999 11:11 pm");
      a2 = new Appointment("a", "11/11/1990 11:10 am", "11/11/1999 11:11 pm");
      a3 = new Appointment("a", "11/11/1999 03:11 pm", "11/11/1999 11:11 pm");
      a4 = new Appointment("a", "12/11/1999 11:11 am", "11/11/1999 11:11 pm");
      a5 = new Appointment("a", "11/11/1999 11:11 am", "11/11/1999 11:11 pm");
      a6 = new Appointment("a", "11/01/1999 11:11 am", "11/11/1999 11:11 pm");
      a7 = new Appointment("a", "11/11/1999 11:11 pm", "11/11/1999 11:11 pm");
    } catch (ParseException e) {
      fail("Failed to initialize an appointment");
    }

    addAppointmentsToListAndSort();

    assertThat(list.get(0), is(equalTo(a2)));
    assertThat(list.get(1), is(equalTo(a6)));
    assertThat(list.get(2), is(equalTo(a5)));
    assertThat(list.get(3), is(equalTo(a3)));
    assertThat(list.get(4), is(equalTo(a7)));
    assertThat(list.get(5), is(equalTo(a4)));
    assertThat(list.get(6), is(equalTo(a1)));
  }

  /**
   * Check ordering of <code>Appointment</code> instances using end times.
   */
  @Test
  public void checkAppointmentOrderingEndTime() {

    try {
      a1 = new Appointment("a", "11/11/1999 11:11 am", "11/11/2000 11:11 am");
      a2 = new Appointment("a", "11/11/1999 11:11 am", "11/11/1990 11:10 am");
      a3 = new Appointment("a", "11/11/1999 11:11 am", "11/11/1999 03:11 pm");
      a4 = new Appointment("a", "11/11/1999 11:11 am", "12/11/1999 11:11 am");
      a5 = new Appointment("a", "11/11/1999 11:11 am", "11/11/1999 11:11 am");
      a6 = new Appointment("a", "11/11/1999 11:11 am", "11/01/1999 11:11 am");
      a7 = new Appointment("a", "11/11/1999 11:11 am", "11/11/1999 11:11 pm");
    } catch (ParseException e) {
      fail("Failed to initialize an appointment");
    }

    addAppointmentsToListAndSort();

    assertThat(list.get(0), is(equalTo(a2)));
    assertThat(list.get(1), is(equalTo(a6)));
    assertThat(list.get(2), is(equalTo(a5)));
    assertThat(list.get(3), is(equalTo(a3)));
    assertThat(list.get(4), is(equalTo(a7)));
    assertThat(list.get(5), is(equalTo(a4)));
    assertThat(list.get(6), is(equalTo(a1)));
  }
}
