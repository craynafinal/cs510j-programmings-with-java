package edu.pdx.cs410J.jsl.server;

import edu.pdx.cs410J.jsl.client.AppointmentBook;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class AppointmentBookServiceImplTest {

  @Test
  public void serviceReturnsExpectedAirline() {
    AppointmentBookServiceImpl service = new AppointmentBookServiceImpl();

    int numberOfAppointments = 6;

    AppointmentBook airline = service.ping(numberOfAppointments);
    assertThat(airline.getAppointments().size(), equalTo(numberOfAppointments));
  }
}
