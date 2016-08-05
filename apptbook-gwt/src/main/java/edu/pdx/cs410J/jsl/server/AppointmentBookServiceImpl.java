package edu.pdx.cs410J.jsl.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.jsl.client.Appointment;
import edu.pdx.cs410J.jsl.client.AppointmentBook;
import edu.pdx.cs410J.jsl.client.AppointmentBookService;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

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
      return "";
    }
  }

  @Override
  protected void doUnexpectedFailure(Throwable unhandled) {
    unhandled.printStackTrace(System.err);
    super.doUnexpectedFailure(unhandled);
  }

}
