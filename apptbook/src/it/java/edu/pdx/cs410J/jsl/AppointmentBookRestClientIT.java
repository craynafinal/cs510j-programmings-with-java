package edu.pdx.cs410J.jsl;

import edu.pdx.cs410J.web.HttpRequestHelper.Response;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

/**
 * Integration test that tests the REST calls made by {@link AppointmentBookRestClient}
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppointmentBookRestClientIT {
  private static final String HOSTNAME = "localhost";
  private static final String PORT = System.getProperty("http.port", "8080");

  private AppointmentBookRestClient newAppointmentBookRestClient() {
    int port = Integer.parseInt(PORT);
    return new AppointmentBookRestClient(HOSTNAME, port);
  }

  @Test
  public void invokingGETWithJustOwnerParameterPrettyPrintsOwnerName() throws IOException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();

    String owner = "PreCannedOwner";
    Response response = client.prettyPrintAppointmentBook(owner);
    assertThat(response.getContent(), response.getCode(), equalTo(200));
    assertThat(response.getContent(), containsString(owner));
  }

  @Test
  public void invokingPOSTCreatesAnAppointment() throws IOException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();

    String owner = "PreCannedOwner";

    String description = "Description";
    String beginTime = "1/1/2016 1:00 PM";
    String endTime = "1/2/2016 2:00 PM";
    Response response = client.createAppointment(owner, description, beginTime, endTime);
    assertThat(response.getContent(), response.getCode(), equalTo(200));

    response = client.prettyPrintAppointmentBook(owner);
    assertThat(response.getContent(), response.getCode(), equalTo(200));
    assertThat(response.getContent(), containsString(owner));
    assertThat(response.getContent(), containsString(description));
    //assertThat(response.getContent(), containsString(beginTime));
    //assertThat(response.getContent(), containsString(endTime));
  }

  /*
  @Test
  public void test0RemoveAllMappings() throws IOException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    Response response = client.removeAllMappings();
    assertThat(response.getContent(), response.getCode(), equalTo(200));
  }

  @Test
  public void test1EmptyServerContainsNoMappings() throws IOException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    Response response = client.getAllKeysAndValues();
    String content = response.getContent();
    assertThat(content, response.getCode(), equalTo(200));
    assertThat(content, containsString(Messages.getMappingCount(0)));
  }

  @Test
  public void test2AddOneKeyValuePair() throws IOException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    String testKey = "TEST KEY";
    String testValue = "TEST VALUE";
    Response response = client.addKeyValuePair(testKey, testValue);
    String content = response.getContent();
    assertThat(content, response.getCode(), equalTo(200));
    assertThat(content, containsString(Messages.mappedKeyValue(testKey, testValue)));
  }

  @Test
  public void missingRequiredParameterReturnsPreconditionFailed() throws IOException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    Response response = client.postToMyURL();
    assertThat(response.getContent(), containsString(Messages.missingRequiredParameter("key")));
    assertThat(response.getCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
  }
  */

}
