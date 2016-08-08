package edu.pdx.cs410J.jsl.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Set;

/**
 * The client-side interface to the ping service
 */
public interface AppointmentBookServiceAsync {
  void createAppointmentBook(String owner, AsyncCallback<String> async);

  void getAllOwnerNames(AsyncCallback<Set<String>> async);

  void createAppointment(String owner, String description, String beginTime, String endTime, AsyncCallback<String> async);

  void prettyPrintAll(String owner, AsyncCallback<String> async);

  void prettyPrintSearch(String owner, String beginTime, String endTime, AsyncCallback<String> async);

  void getDumpFileLocation(String owner, AsyncCallback<String> async);

  void restoreAppointmentBook(String owner, String fileContent, AsyncCallback<String> async);
}
