package edu.pdx.cs410J.jsl.client;

import edu.pdx.cs410J.AbstractAppointment;


import java.util.Date;

/**
 * The <code>Appointment</code> is the class extended from the {@link AbstractAppointment} class
 * that describes an appointment with description, begin time and end time.
 *
 * @author    Jong Seong Lee
 * @version   %I%, %G%
 * @since     1.0
 */
public class Appointment extends AbstractAppointment implements Comparable<Appointment> {
  private String description = null;
  private String begin_input = null;
  private String end_input = null;
  private Date begin_date = null;
  private Date end_date = null;

  /**
   * This constructor is for GWT application.
   */
  public Appointment() {
    this("My description", "01/01/2000 01:01 am", "01/01/2000 01:01 pm");
  }

  /**
   * This constructor takes description, begin time and end time in order.
   *
   * @param desc a description of an appointment
   * @param begin a begin time of an appointment
   * @param end an end time of an appointment
     */
  public Appointment(String desc, String begin, String end) {
    description = desc;
    begin_date = DateUtility.parseStringToDate(begin);
    end_date = DateUtility.parseStringToDate(end);

    // keep the initial date time inputs for dumping purpose
    begin_input = begin;
    end_input = end;
  }

  /**
   * Returns a begin time of an instance.
   *
   * @return a begin time
     */
  @Override
  public String getBeginTimeString() {
    return begin_date.toString();
  }

  /**
   * Returns an end time of an instance.
   *
   * @return an end time
     */
  @Override
  public String getEndTimeString() {
    return end_date.toString();
  }

  /**
   * Returns a description of an instance.
   *
   * @return a description
     */
  @Override
  public String getDescription() {
    return description;
  }

  /**
   * Returns a begin time.
   *
   * @return a begin time
     */
  @Override
  public Date getBeginTime() { return begin_date; }

  /**
   * Returns an end time.
   *
   * @return an end time
     */
  @Override
  public Date getEndTime() { return end_date; }

  /**
   * Returns the initial begin time input.
   *
   * @return a begin time
     */
  public String getBeginTimeInput() { return begin_input; }

  /**
   * Returns the initial end time input.
   *
   * @return an end time
   */
  public String getEndTimeInput() { return end_input; }

  /**
   * Returns a duration of an appointment.
   *
   * @return a duration
     */
  public int getDurationInMinutes() {
    return DateUtility.getMinutesBetweenDates(begin_date, end_date);
  }

  /**
   * Compares two <code>Appointment</code> object and determine their orders.
   * The order of comparison is begin time, end time, and description.
   *
   * @param appointment another <code>Appointment</code> object
   * @return returns 0 if two objects are equal, -1 if the other object is smaller, otherwise 1
     */
  @Override
  public int compareTo(Appointment appointment) {
    int result = this.getBeginTime().compareTo(appointment.getBeginTime());

    if (result != 0) {
      return result;
    }

    result = this.getEndTime().compareTo(appointment.getEndTime());;

    if (result != 0) {
      return result;
    }

    return this.getDescription().compareTo(appointment.getDescription());
  }
}
