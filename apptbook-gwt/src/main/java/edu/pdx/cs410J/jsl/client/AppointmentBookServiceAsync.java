package edu.pdx.cs410J.jsl.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Set;

/**
 * The client-side interface to the ping service
 */
public interface AppointmentBookServiceAsync {
  /**
   * Calls the appointment book creation functionality on the server side.
   *
   * @param owner
   * @param async
     */
  void createAppointmentBook(String owner, AsyncCallback<String> async);

  /**
   * Receives all owner names from the server in <code>Set</code> collection.
   *
   * @param async
     */
  void getAllOwnerNames(AsyncCallback<Set<String>> async);

  /**
   * Calls the appointment creation functionality on the server side.
   *
   * @param owner
   * @param description
   * @param beginTime
   * @param endTime
     * @param async
     */
  void createAppointment(String owner, String description, String beginTime, String endTime, AsyncCallback<String> async);

  /**
   * Calls the pretty print functionality on the server side.
   *
   * @param owner
   * @param async
     */
  void prettyPrintAll(String owner, AsyncCallback<String> async);

  /**
   * Calls the pretty print search functionality on the server side.
   *
   * @param owner
   * @param beginTime
   * @param endTime
   * @param async
     */
  void prettyPrintSearch(String owner, String beginTime, String endTime, AsyncCallback<String> async);

  /**
   * Get dump file location on the server side.
   * @param owner
   * @param async
     */
  void getDumpFileLocation(String owner, AsyncCallback<String> async);

  /**
   * Upload the file content to server to restore saved information.
   * @param owner
   * @param fileContent
   * @param async
     */
  void restoreAppointmentBook(String owner, String fileContent, AsyncCallback<String> async);
}
