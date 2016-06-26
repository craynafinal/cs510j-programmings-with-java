package edu.pdx.cs410J.jsl;

import edu.pdx.cs410J.AbstractAppointment;

public class Appointment extends AbstractAppointment {
  private String description;
  private String begin_time;
  private String end_time;

  public Appointment() {
    this("description", "00/00/0000 00:00", "00/00/0000 00:00");
  }

  public Appointment(String desc, String begin, String end) {
    description = desc;
    begin_time = begin;
    end_time = end;
  }

  @Override
  public String getBeginTimeString() {
    return begin_time;
  }

  @Override
  public String getEndTimeString() {
    return end_time;
  }

  @Override
  public String getDescription() {
    return description;
  }
}