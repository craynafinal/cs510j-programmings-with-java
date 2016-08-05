package edu.pdx.cs410J.jsl.server;

import edu.pdx.cs410J.jsl.client.AppointmentBook;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class AppointmentBookServiceImplTest {
  AppointmentBookServiceImpl service = null;

  @Before
  public void init() {
    service = new AppointmentBookServiceImpl();
  }

  @Test
  public void shouldAddNewAppointment() {
    String owner = "owner";
    String result = service.createAppointmentBook(owner);
    assertThat(owner, is(equalTo(result)));
  }

  @Test
  public void shouldReturnEmptyString() {
    String owner = "owner";
    service.createAppointmentBook(owner);
    String result = service.createAppointmentBook(owner);

    assertThat("", is(equalTo(result)));
  }

  @Test
  public void shouldReceiveAllOwnerNames() {
    int size = 3;

    for (int i = 0; i < size; i++) {
      service.createAppointmentBook("owner" + i);
    }

    Set<String> results = service.receiveAllOwnerNames();
    assertThat(results.size(), is(equalTo(size)));
  }
}
