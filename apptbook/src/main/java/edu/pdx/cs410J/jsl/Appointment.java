package edu.pdx.cs410J.jsl;

import edu.pdx.cs410J.AbstractAppointment;
import java.util.List;

/**
 * Appointment is the class extended from the {@link AbstractAppointment} class
 * that describes an appointment with description, begin time and end time.
 *
 * @author    Jong Seong Lee
 * @version   %I%, %G%
 * @since     1.0
 */
public class Appointment extends AbstractAppointment {
  private String description = null;
  private String begin_time = null;
  private String end_time = null;

  /**
   * This constructor creates an instance of the Appointment class.
   * There is no parameter and it uses default values to initialize.
   */
  public Appointment() {
    this("description", "01/01/2000 00:00", "01/01/2000 00:00");
  }

  /**
   * This constructor creates an instance of the Appointment class.
   * A list of arguments is taken as a parameter, and the list should have
   * description, begin time, and end time in order as string data type.
   * @param arguments   a list of strings that contains description, begin time and end time in order
     */
  public Appointment(List<String> arguments) {
    this(arguments.get(0), arguments.get(1), arguments.get(2));
  }

  /**
   * This constructor creates an instance of the Appointment class.
   * It will take strings of description, begin time and end time in order.
   *
   * @param desc    a description of an appointment
   * @param begin   a begin time of an appointment
   * @param end     an end time of an appointment
     */
  public Appointment(String desc, String begin, String end) {
    description = desc;
    begin_time = begin;
    end_time = end;
  }

  /**
   * Returns a begin time of an instance in string format.
   *
   * @return  a begin time in string format
     */
  @Override
  public String getBeginTimeString() {
    return begin_time;
  }

  /**
   * Returns an end time of an instance in string format.
   *
   * @return  an end time in string format
     */
  @Override
  public String getEndTimeString() {
    return end_time;
  }

  /**
   * Returns a description of an instance in string format.
   *
   * @return  a description in string format
     */
  @Override
  public String getDescription() {
    return description;
  }
}
