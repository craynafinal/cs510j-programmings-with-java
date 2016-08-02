package edu.pdx.cs410J.jsl.client;

import com.google.gwt.i18n.client.DateTimeFormat;
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

  /**
   * Cannot parse dates in constructor because of ExceptionInInitializer from GWT,
   * so storing date information as <code>String</code>.
   */
  private String beginTime = null;
  private String endTime = null;

  /**
   * This constructor takes description, begin time and end time in order.
   *
   * @param desc a description of an appointment
   * @param begin a begin time of an appointment
   * @param end an end time of an appointment
     */
  public Appointment(String desc, String begin, String end) {
    description = desc;
    beginTime = begin;
    endTime = end;
  }

  private String formatDateToString(Date date) {
    String pattern = "MM/dd/yyyy hh:mm a";
    return DateTimeFormat.getFormat(pattern).format(date);
  }

  private Date parseStringToDate(String date) {
    String pattern = "MM/dd/yyyy hh:mm a";
    return DateTimeFormat.getFormat(pattern).parse(date);
  }


  public Appointment() {
    this("My Description", "01/01/1999 01:00 am", "01/01/1999 01:00 pm");
  }

    /**
   * Returns a begin time of an instance.
   *
   * @return a begin time
     */
  @Override
  public String getBeginTimeString() {
    return DateUtility.parseStringToDate(beginTime).toString();
  }

  /**
   * Returns an end time of an instance.
   *
   * @return an end time
     */
  @Override
  public String getEndTimeString() {
    return DateUtility.parseStringToDate(endTime).toString();
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
  public Date getBeginTime() { return DateUtility.parseStringToDate(beginTime); }

  /**
   * Returns an end time.
   *
   * @return an end time
     */
  @Override
  public Date getEndTime() { return DateUtility.parseStringToDate(endTime); }

  /**
   * Returns the initial begin time input.
   *
   * @return a begin time
     */
  public String getBeginTimeInput() { return beginTime; }

  /**
   * Returns the initial end time input.
   *
   * @return an end time
   */
  public String getEndTimeInput() { return endTime; }

  /**
   * Returns a duration of an appointment.
   *
   * @return a duration
     */
  public int getDurationInMinutes() {
    return DateUtility.getMinutesBetweenDates(getBeginTime(), getEndTime());
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
