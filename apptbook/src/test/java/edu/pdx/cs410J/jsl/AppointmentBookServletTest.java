package edu.pdx.cs410J.jsl;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

/**
 * A unit test for the {@link AppointmentBookServlet}.  It uses mockito to
 * provide mock http requests and responses.
 */
public class AppointmentBookServletTest {
  private AppointmentBookServlet servlet = null;
  private HttpServletRequest request = null;
  private HttpServletResponse response = null;
  private PrintWriter printWriter = null;

  @Before
  public void appointmentBookServletTestInit() {
    servlet = new AppointmentBookServlet();
    request = mock(HttpServletRequest.class);
    response = mock(HttpServletResponse.class);
    printWriter = mock(PrintWriter.class);
  }

  @Test
  public void shouldPrintMessageWhenOwnerNameIsNotProvided() throws ServletException, IOException {
    when(request.getParameter("owner")).thenReturn(null);

    when(response.getWriter()).thenReturn(printWriter);

    servlet.doGet(request, response);

    verify(printWriter).println("The owner name is not provided, please try again...");
    verify(response).setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
  }

  @Test
  public void shouldPrintMessageWhenOwnerNameDoesNotExist() throws ServletException, IOException {
    String ownerName = "TestOwner";
    when(request.getParameter("owner")).thenReturn(ownerName);

    when(response.getWriter()).thenReturn(printWriter);

    servlet.doGet(request, response);

    verify(printWriter).println("There is no appointment book matching the owner name: " + ownerName);
    verify(response).setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
  }

  @Test
  public void shouldPrintAllListOfAppointments() throws ServletException, IOException {
    // create appointments
    String ownerName = "PreCannedOwner";
    String description = "My description";
    String beginTime = "1/1/2016 1:00 AM";
    String endTime = "1/1/2016 1:00 PM";
    int i, max = 5;

    for (i = 0; i < max; i++) {
      addAppointment(ownerName, description + (i + 1), beginTime, endTime);
    }

    when(request.getParameter("owner")).thenReturn(ownerName);

    when(response.getWriter()).thenReturn(printWriter);

    servlet.doGet(request, response);

    for (i = 0; i < max; i++) {
      verify(printWriter).println(" " + (i + 1) + ") Appointment: " + description + (i + 1));
    }
    verify(response, times(max + 1)).setStatus(HttpServletResponse.SC_OK);
  }

  @Test
  public void shouldPrintSearchedListOfAppointments() throws ServletException, IOException {
    // create appointments
    String ownerName = "PreCannedOwner";
    String description = "My description";
    String[] beginTime = { "1/1/2016 1:00 AM", "1/2/2016 1:00 AM", "1/3/2016 1:00 AM", "1/4/2016 1:00 AM", "1/5/2016 1:00 AM" };
    String[] endTime = { "1/1/2016 1:00 PM", "1/2/2016 1:00 PM", "1/3/2016 1:00 PM", "1/4/2016 1:00 PM", "1/5/2016 1:00 PM" };
    int i, max = 5, start = 1, end = 3;

    for (i = 0; i < max; i++) {
      addAppointment(ownerName, description + (i + 1), beginTime[i], endTime[i]);
    }

    // search get
    when(request.getParameter("owner")).thenReturn(ownerName);
    when(request.getParameter("beginTime")).thenReturn(beginTime[start]);
    when(request.getParameter("endTime")).thenReturn(endTime[end]);

    when(response.getWriter()).thenReturn(printWriter);

    servlet.doGet(request, response);

    for (i = start; i <= end; i++) {
      verify(printWriter).println(" " + i + ") Appointment: " + description + (i + 1));
    }
    verify(response, times(max + 1)).setStatus(HttpServletResponse.SC_OK);
  }

  private void addAppointment(String ownerName, String description, String beginTime, String endTime) throws IOException, ServletException {
    when(request.getParameter("owner")).thenReturn(ownerName);
    when(request.getParameter("description")).thenReturn(description);
    when(request.getParameter("beginTime")).thenReturn(beginTime);
    when(request.getParameter("endTime")).thenReturn(endTime);

    when(response.getWriter()).thenReturn(printWriter);
    servlet.doPost(request, response);
  }

  @Test
  public void shouldCreateAnAppointment() throws ServletException, IOException {
    String ownerName = "PreCannedOwner";
    String description = "My description";
    String beginTime = "1/1/2016 1:00 PM";
    String endTime = "1/2/2016 2:00 PM";

    addAppointment(ownerName, description, beginTime, endTime);
    verify(response).setStatus(HttpServletResponse.SC_OK);

    AppointmentBook book = servlet.getAppointmentBookForOwner(ownerName);
    Collection<Appointment> appointments = book.getAppointments();
    assertThat(appointments.size(), equalTo(1));
    Appointment appointment = appointments.iterator().next();
    assertThat(appointment.getDescription(), equalTo(description));
    assertThat(appointment.getBeginTimeInput(), equalTo(beginTime));
    assertThat(appointment.getEndTimeInput(), equalTo(endTime));
  }
}
