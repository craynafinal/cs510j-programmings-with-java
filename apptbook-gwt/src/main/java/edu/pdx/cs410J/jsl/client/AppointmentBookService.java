package edu.pdx.cs410J.jsl.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.Set;

/**
 * A GWT remote service that returns a dummy appointment book
 */
@RemoteServiceRelativePath("appointments")
public interface AppointmentBookService extends RemoteService {
  public String createAppointmentBook(String owner);

  public String createAppointment(String owner, String description, String beginTime, String endTime);

  public Set<String> receiveAllOwnerNames();

  public String prettyPrintAll(String owner);

  public String prettyPrintSearch(String owner, String beginTime, String endTime);

  public String getDumpFileLocation(String owner);
}
