package edu.pdx.cs410J.jsl.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.Set;

/**
 * A GWT remote service that returns a dummy appointment book
 */
@RemoteServiceRelativePath("appointments")
public interface AppointmentBookService extends RemoteService {

  /**
   * Returns the current date and time on the server
   * @param numberOfAppointments
   */
  public AppointmentBook createAppointmentBook2(int numberOfAppointments);

  public String createAppointmentBook(String owner);

  public Set<String> receiveAllOwnerNames();
}
