package edu.pdx.cs410J.jsl;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for the {@link Appointment} class.
 */
public class AppointmentTest {

  /**
   * It will check if a description of an appointment is assigned correctly.
   */
  @Test
  public void getDescriptionNeedsToBeImplemented() {
    String description = "test description";

    Appointment appointment = new Appointment(description, "", "");
    assertThat(appointment.getDescription(), is(equalTo(description)));
  }

  /**
   * It will check if a begin time of an appointment is assigned correctly.
   */
  @Test
  public void getBeginTimeStringNeedsToBeImplemented() {
    String begin_time = "11/11/1111 11:11";
    Appointment appointment = new Appointment("", begin_time, "");
    assertThat(appointment.getBeginTimeString(), is(equalTo(begin_time)));
  }

  /**
   * It will check if an end time of an appointment is assigned correctly.
   */
  @Test
  public void getEndTimeStringNeedsToBeImplemented() {
    String end_time = "11/11/1111 11:11";
    Appointment appointment = new Appointment("", "", end_time);
    assertThat(appointment.getEndTimeString(), is(equalTo(end_time)));
  }

  /**
   * It will check if toString method of the {@link Appointment} class works correctly.
   */
  @Test
  public void toStringShouldContainCorrectDescriptionAndTimes() {
    String description = "test description";
    String begin_time = "11/11/1111 11:11";
    String end_time = "22/22/2222 22:22";

    Appointment appointment = new Appointment(description, begin_time, end_time);
    assertThat(appointment.toString(), is(equalTo(description + " from " + begin_time + " until " + end_time)));
  }

  /**
   * getBeginTime method is okay to return null for the project 1
   */
  @Test
  public void forProject1ItIsOkayIfGetBeginTimeReturnsNull() {
    Appointment appointment = new Appointment();
    assertThat(appointment.getBeginTime(), is(nullValue()));
  }

}
