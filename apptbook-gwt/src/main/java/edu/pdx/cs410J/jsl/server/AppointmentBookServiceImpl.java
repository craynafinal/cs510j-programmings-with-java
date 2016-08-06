package edu.pdx.cs410J.jsl.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.jsl.client.Appointment;
import edu.pdx.cs410J.jsl.client.AppointmentBook;
import edu.pdx.cs410J.jsl.client.AppointmentBookService;
import edu.pdx.cs410J.jsl.client.DateUtility;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.*;

/**
 * The server-side implementation of the division service
 */
public class AppointmentBookServiceImpl extends RemoteServiceServlet implements AppointmentBookService
{
  HashMap<String, AppointmentBook> appointmentBooks = new HashMap<>();

  @Override
  public String createAppointmentBook(String owner) {
    AppointmentBook book = new AppointmentBook(owner);

    if (!appointmentBooks.containsKey(owner)) {
      appointmentBooks.put(owner, book);
      return book.getOwnerName();
    } else {
      return "";
    }
  }

  @Override
  public String createAppointment(String owner, String description, String beginTime, String endTime) {
    AppointmentBook appointmentBook = appointmentBooks.get(owner);
    Appointment appointment = new Appointment(description, beginTime, endTime);
    appointmentBook.addAppointment(appointment);
    return appointment.toString();
  }

  @Override
  public Set<String> receiveAllOwnerNames() {
    Set<String> ownerNames = new TreeSet<>();

    for (String ownerName : appointmentBooks.keySet()) {
      ownerNames.add(ownerName);
    }

    return ownerNames;
  }

  @Override
  public String prettyPrintAll(String owner) {

    AppointmentBook appointmentBook = appointmentBooks.get(owner);
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    PrettyPrinter prettyPrinter = new PrettyPrinter(printWriter);

    try {
      prettyPrinter.dump(appointmentBook);
      return stringWriter.toString();
    } catch (IOException e) {
      return "Failed to print the searched appointment book for " + owner;
    }
  }

  /**
   * Returns an <code>AppointmentBook</code> object containing only <code>Appointment</code> objects within
   * <code>beginTime</code> and <code>endTime</code>.
   *
   * @param book an original <code>AppointmentBook</code> object
   * @param beginTime
   * @param endTime
   * @return a new <code>AppointmentBook</code> object containing only <code>Appointment</code> objects meets the condition
   * @throws ParseException
   */
  private AppointmentBook getAppointmentBookWithSearchedAppointments(AppointmentBook book, String beginTime, String endTime) throws ParseException {
    AppointmentBook tempAppointmentBook = new AppointmentBook(book.getOwnerName());
    Date begin_date = null;
    Date end_date = null;

    begin_date = DateUtility.parseStringToDate(beginTime);
    end_date = DateUtility.parseStringToDate(endTime);

    for (Appointment appointment: book.getAppointments()) {
      if (appointment.getBeginTime().compareTo(begin_date) >= 0
              && appointment.getEndTime().compareTo(end_date) <= 0) {
        tempAppointmentBook.addAppointment(appointment);
      }
    }

    return tempAppointmentBook;
  }

  @Override
  public String prettyPrintSearch(String owner, String beginTime, String endTime) {

    AppointmentBook appointmentBook = null;
    try {
      appointmentBook = getAppointmentBookWithSearchedAppointments(appointmentBooks.get(owner), beginTime, endTime);
    } catch (ParseException e) {
      return "Failed to search the appointment book for " + owner;
    }

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    PrettyPrinter prettyPrinter = new PrettyPrinter(printWriter);

    try {
      prettyPrinter.dump(appointmentBook);
      return stringWriter.toString();
    } catch (IOException e) {
      return "Failed to print the searched appointment book for " + owner;
    }
  }

  @Override
  public String getDumpFileLocation(String owner) {
    AppointmentBook appointmentBook = appointmentBooks.get(owner);
    String filename = owner + ".txt";
    TextDumper textDumper = new TextDumper(getServletContext().getRealPath(filename));
    try {
      textDumper.dump(appointmentBook);
      return filename;
    } catch (IOException e) {
      return getServletContext().getContextPath();
    }
  }

  @Override
  protected void doUnexpectedFailure(Throwable unhandled) {
    unhandled.printStackTrace(System.err);
    super.doUnexpectedFailure(unhandled);
  }

}
