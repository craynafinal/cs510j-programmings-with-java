package edu.pdx.cs410J.jsl.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The client-side interface to the ping service
 */
public interface PingServiceAsync {

  /**
   * Return the current date/time on the server
   */
  void ping(int numberOfAppointments, AsyncCallback<AppointmentBook> async);
}
