package edu.pdx.cs410J.jsl;

import edu.pdx.cs410J.web.HttpRequestHelper;
import edu.pdx.cs410J.web.HttpRequestHelper.Response;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.text.ParseException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.fail;

/**
 * Integration test that tests the REST calls made by {@link AppointmentBookRestClient}
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppointmentBookRestClientIT {
  private static final String HOSTNAME = "localhost";
  private static final String PORT = System.getProperty("http.port", "8080");

  private static final int SC_NOT_FOUND = 404;
  private static final int SC_BAD_REQUEST = 400;

  private AppointmentBookRestClient newAppointmentBookRestClient() {
    int port = Integer.parseInt(PORT);
    return new AppointmentBookRestClient(HOSTNAME, port);
  }

  @Test
  public void shouldFailWhenOwnerNameIsNotFoundOnGET() throws IOException {

    AppointmentBookRestClient client = newAppointmentBookRestClient();

    String owner = "No name";
    HttpRequestHelper.Response response = client.prettyPrintAppointmentBook(owner);

    assertThat(response.getContent(), response.getCode(), equalTo(SC_NOT_FOUND));
    assertThat(response.getContent(), containsString(owner));
  }

  @Test
  public void shouldFailWhenOwnerNameIsEmptyStringOnGET() throws IOException {

    AppointmentBookRestClient client = newAppointmentBookRestClient();

    HttpRequestHelper.Response response = client.prettyPrintAppointmentBook("");

    assertThat(response.getContent(), response.getCode(), equalTo(SC_BAD_REQUEST));
    assertThat(response.getContent(), containsString("owner"));
  }

  @Test
  public void shouldFailWithWrongDateOnPOST() throws IOException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();

    String owner = "My Owner";
    String description = "Description";
    String beginTime = "wrong date";
    String endTime = "1/2/2016 2:00 PM";
    Response response = client.createAppointment(owner, description, beginTime, endTime);

    System.out.println(response.getContent());

    assertThat(response.getContent(), response.getCode(), equalTo(SC_BAD_REQUEST));
    assertThat(response.getContent(), containsString(beginTime));
  }

  @Test
  public void shouldFailWithWrongDateOnGET() throws IOException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();

    String owner = "My Owner";
    String beginTime = "1/1/2016 1:00 PM";
    String endTime = "wrong date";

    Response response = client.searchAppointment(owner, beginTime, endTime);

    assertThat(response.getContent(), response.getCode(), equalTo(SC_BAD_REQUEST));
    assertThat(response.getContent(), containsString(endTime));
  }

  @Test
  public void invokingPOSTCreatesAnAppointment() throws IOException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();

    String owner = "My Owner";

    String description = "Description";
    String beginTime = "1/1/2016 1:00 PM";
    String endTime = "1/2/2016 2:00 PM";
    Response response = client.createAppointment(owner, description, beginTime, endTime);
    assertThat(response.getContent(), response.getCode(), equalTo(200));

    response = client.prettyPrintAppointmentBook(owner);

    assertThat(response.getContent(), response.getCode(), equalTo(200));
    assertThat(response.getContent(), containsString(owner));
    assertThat(response.getContent(), containsString(description));

    try {
      assertThat(response.getContent(), containsString(DateUtility.parseStringToDatePrettyPrint(DateUtility.parseStringToDate(beginTime))));
      assertThat(response.getContent(), containsString(DateUtility.parseStringToDatePrettyPrint(DateUtility.parseStringToDate(endTime))));
    } catch (ParseException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void invokingGETSearchesAppointments() throws IOException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();

    String owner = "My Owner";
    String description = "Description";
    String[] beginTime = { "1/1/2016 1:00 AM", "1/2/2016 1:00 AM", "1/3/2016 1:00 AM", "1/4/2016 1:00 AM", "1/5/2016 1:00 AM" };
    String[] endTime = { "1/1/2016 1:00 PM", "1/2/2016 1:00 PM", "1/3/2016 1:00 PM", "1/4/2016 1:00 PM", "1/5/2016 1:00 PM" };
    int i, max = 5, start = 1, end = 3;

    Response response = null;

    for (i = 0; i < max; i++) {
      response = client.createAppointment(owner, description + (i + 1), beginTime[i], endTime[i]);
      assertThat(response.getContent(), response.getCode(), equalTo(200));
    }

    response = client.searchAppointment(owner, beginTime[start], endTime[end]);

    assertThat(response.getContent(), response.getCode(), equalTo(200));
    assertThat(response.getContent(), containsString(owner));

    for (i = start; i < end; i++) {
      assertThat(response.getContent(), containsString(description + (i + 1)));
      try {
        assertThat(response.getContent(), containsString(DateUtility.parseStringToDatePrettyPrint(DateUtility.parseStringToDate(beginTime[i]))));
        assertThat(response.getContent(), containsString(DateUtility.parseStringToDatePrettyPrint(DateUtility.parseStringToDate(endTime[i]))));
      } catch (ParseException e) {
        fail(e.getMessage());
      }
    }
  }

  /*
  @Test
  public void invokingPOSTWithMissingBeginTime() throws IOException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();

    String owner = "PreCannedOwner";

    String description = "Description";
    String endTime = "1/2/2016 2:00 PM";
    Response response = client.createAppointment(owner, description, null, endTime);
    assertThat(response.getContent(), response.getCode(), equalTo(412));

  }

  */
}
