package edu.pdx.cs410J.jsl.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.jsl.client.Appointment;
import edu.pdx.cs410J.jsl.client.AppointmentBook;
import edu.pdx.cs410J.jsl.client.AppointmentBookService;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

/**
 * The server-side implementation of the division service
 */
public class AppointmentBookServiceImpl extends RemoteServiceServlet implements AppointmentBookService
{
  Set<AppointmentBook> appointmentBooks = new TreeSet<>();

  @Override
  public AppointmentBook createAppointmentBook2(int numberOfAppointments) {
    AppointmentBook book = new AppointmentBook();
    for (int i = 0; i < numberOfAppointments; i++) {
      book.addAppointment(new Appointment());
    }
    return book;
  }

  @Override
  public String createAppointmentBook(String owner) {
    AppointmentBook book = new AppointmentBook(owner);

    if (appointmentBooks.add(book)) {
      return book.getOwnerName();
    } else {
      return "";
    }
  }

  @Override
  public Set<String> receiveAllOwnerNames() {
    Set<String> ownerNames = new TreeSet<>();

    for (AppointmentBook appointmentBook : appointmentBooks) {
      ownerNames.add(appointmentBook.getOwnerName());
    }

    return ownerNames;
  }

  @Override
  protected void doUnexpectedFailure(Throwable unhandled) {
    unhandled.printStackTrace(System.err);
    super.doUnexpectedFailure(unhandled);
  }

}
