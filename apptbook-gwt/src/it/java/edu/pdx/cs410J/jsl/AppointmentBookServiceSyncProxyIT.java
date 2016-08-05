package edu.pdx.cs410J.jsl;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import edu.pdx.cs410J.jsl.client.Appointment;
import edu.pdx.cs410J.jsl.client.AppointmentBook;
import edu.pdx.cs410J.jsl.client.AppointmentBookService;
import edu.pdx.cs410J.web.HttpRequestHelper;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

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

    String owner = "My Owner";
    String description = "My description";
    String beginTime = "1/1/2000 11:11 am";
    String endTime = "2/2/2001 10:10 pm";

    AppointmentBookService service = SyncProxy.createSync(AppointmentBookService.class);
    String result = service.createAppointmentBook(owner);
    assertEquals(owner, result);

    result = service.createAppointment(owner, description, beginTime, endTime);
    assertEquals(new Appointment(description, beginTime, endTime).toString(), result);
  }
}
