package edu.pdx.cs410J.jsl;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import edu.pdx.cs410J.jsl.client.Appointment;
import edu.pdx.cs410J.jsl.client.AppointmentBook;
import edu.pdx.cs410J.jsl.client.AppointmentBookService;
import edu.pdx.cs410J.jsl.client.DateUtility;
import edu.pdx.cs410J.web.HttpRequestHelper;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AppointmentBookServiceSyncProxyIT extends HttpRequestHelper {

  private final int httpPort = Integer.getInteger("http.port", 8080);
  private String webAppUrl = "http://localhost:" + httpPort + "/apptbook";

  @Test
  public void gwtWebApplicationIsRunning() throws IOException {
    Response response = get(this.webAppUrl);
    assertEquals(200, response.getCode());
  }

  @Test
  public void canInvokeAppointmentBookServiceWithGwtSyncProxy() {
    String moduleName = "apptbook";
    SyncProxy.setBaseURL(this.webAppUrl + "/" + moduleName + "/");

    String[] owners = { "My Owner", "My Owner 2" };
    String description = "My description";
    String[] beginTimes = { "1/1/2000 11:11 am", "1/1/2011 11:11 am" };
    String[] endTimes = { "2/2/2001 10:10 pm", "2/2/2012 10:10 pm" };

    // add appointment books
    AppointmentBookService service = SyncProxy.createSync(AppointmentBookService.class);
    String result = service.createAppointmentBook(owners[0]);
    assertEquals(owners[0], result);


    // create an appointment
    result = service.createAppointment(owners[0], description, beginTimes[0], endTimes[0]);
    assertEquals(new Appointment(description, beginTimes[0], endTimes[0]).toString(), result);

    result = service.createAppointment(owners[0], description, beginTimes[1], endTimes[1]);
    assertEquals(new Appointment(description, beginTimes[1], endTimes[1]).toString(), result);

    // checking pretty print all
    result = service.prettyPrintAll(owners[0]);
    assertTrue(result, result.contains(owners[0]));
    assertTrue(result, result.contains(description));
    assertTrue(result, result.contains(DateUtility.parseDateToStringPrettyPrint(DateUtility.parseStringToDate(beginTimes[0]))));
    assertTrue(result, result.contains(DateUtility.parseDateToStringPrettyPrint(DateUtility.parseStringToDate(endTimes[0]))));
    assertTrue(result, result.contains(DateUtility.parseDateToStringPrettyPrint(DateUtility.parseStringToDate(beginTimes[1]))));
    assertTrue(result, result.contains(DateUtility.parseDateToStringPrettyPrint(DateUtility.parseStringToDate(endTimes[1]))));

    // checking pretty print search
    result = service.prettyPrintSearch(owners[0], beginTimes[0], endTimes[0]);
    assertTrue(result, result.contains(owners[0]));
    assertTrue(result, result.contains(description));
    assertTrue(result, result.contains(DateUtility.parseDateToStringPrettyPrint(DateUtility.parseStringToDate(beginTimes[0]))));
    assertTrue(result, result.contains(DateUtility.parseDateToStringPrettyPrint(DateUtility.parseStringToDate(endTimes[0]))));
    assertFalse(result, result.contains(DateUtility.parseDateToStringPrettyPrint(DateUtility.parseStringToDate(beginTimes[1]))));
    assertFalse(result, result.contains(DateUtility.parseDateToStringPrettyPrint(DateUtility.parseStringToDate(endTimes[1]))));


    result = service.getDumpFileLocation(owners[0]);
    assertTrue(result, result.contains(owners[0]));


    //result = service.restoreAppointmentBook(owners[0], owners)
  }
}
