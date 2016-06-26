package edu.pdx.cs410J.jsl;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for the {@link Appointment} class.
 */
public class AppointmentTest {

  @Test
  public void getDescriptionNeedsToBeImplemented() {
    String description = "test description";

    Appointment appointment = new Appointment(description, "", "");
    assertThat(appointment.getDescription(), is(equalTo(description)));
  }

  @Test
  public void getBeginTimeStringNeedsToBeImplemented() {
    String begin_time = "11/11/1111 11:11";
    Appointment appointment = new Appointment("", begin_time, "");
    assertThat(appointment.getBeginTimeString(), is(equalTo(begin_time)));
  }

  @Test
  public void getEndTimeStringNeedsToBeImplemented() {
    String end_time = "11/11/1111 11:11";
    Appointment appointment = new Appointment("", "", end_time);
    assertThat(appointment.getEndTimeString(), is(equalTo(end_time)));
  }

  @Test
  public void toStringShouldContainCorrectDescriptionAndTimes() {
    String description = "test description";
    String begin_time = "11/11/1111 11:11";
    String end_time = "22/22/2222 22:22";

    Appointment appointment = new Appointment(description, begin_time, end_time);
    assertThat(appointment.toString(), is(equalTo(description + " from " + begin_time + " until " + end_time)));
  }

  @Test
  public void forProject1ItIsOkayIfGetBeginTimeReturnsNull() {
    Appointment appointment = new Appointment();
    assertThat(appointment.getBeginTime(), is(nullValue()));
  }

}
