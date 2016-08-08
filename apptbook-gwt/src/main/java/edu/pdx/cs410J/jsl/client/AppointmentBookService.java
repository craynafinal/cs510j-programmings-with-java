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
   * Calls the appointment book creation functionality on the server side.
   *
   * @param owner
   * @return
     */
  public String createAppointmentBook(String owner);

  /**
   * Calls the appointment creation functionality on the server side.
   *
   * @param owner
   * @param description
   * @param beginTime
   * @param endTime
     * @return
     */
  public String createAppointment(String owner, String description, String beginTime, String endTime);

  /**
   * Receives all owner names from the server in <code>Set</code> collection.
   *
   * @return
     */
  public Set<String> getAllOwnerNames();

  /**
   * Calls the pretty print functionality on the server side.
   *
   * @param owner
   * @return
     */
  public String prettyPrintAll(String owner);

  /**
   * Calls the pretty print search functionality on the server side.
   *
   * @param owner
   * @param beginTime
   * @param endTime
     * @return
     */
  public String prettyPrintSearch(String owner, String beginTime, String endTime);

  /**
   * Get dump file location on the server side.
   * @param owner
   * @return
     */
  public String getDumpFileLocation(String owner);

  /**
   * Upload the file content to server to restore saved information.
   *
   * @param owner
   * @param fileContent
   * @return
     */
  public String restoreAppointmentBook(String owner, String fileContent);
}
