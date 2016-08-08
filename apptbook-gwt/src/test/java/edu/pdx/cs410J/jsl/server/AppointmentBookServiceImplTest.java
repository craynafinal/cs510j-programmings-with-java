package edu.pdx.cs410J.jsl.server;

import edu.pdx.cs410J.jsl.client.Appointment;
import edu.pdx.cs410J.jsl.client.AppointmentBook;
import edu.pdx.cs410J.jsl.client.DateUtility;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.*;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class AppointmentBookServiceImplTest {
  private static final String OWNER = "owner";
  private static final String DESCRIPTION = "description";
  private static final String[] BEGINTIME = { "1/1/2000 1:0 am", "2/1/2000 1:0 am", "3/1/2000 1:0 am" };
  private static final String[] ENDTIME = { "1/2/2000 1:0 am", "2/2/2000 1:0 am", "3/2/2000 1:0 am" };

  AppointmentBookServiceImpl service = null;

  @Before
  public void init() {
    service = new AppointmentBookServiceImpl();

    try {
      service.init(new ServletConfig() {
        @Override
        public String getServletName() {
          return null;
        }

        @Override
        public ServletContext getServletContext() {
          return new ServletContext() {
            @Override
            public String getContextPath() {
              return null;
            }

            @Override
            public ServletContext getContext(String s) {
              return null;
            }

            @Override
            public int getMajorVersion() {
              return 0;
            }

            @Override
            public int getMinorVersion() {
              return 0;
            }

            @Override
            public String getMimeType(String s) {
              return null;
            }

            @Override
            public Set getResourcePaths(String s) {
              return null;
            }

            @Override
            public URL getResource(String s) throws MalformedURLException {
              return null;
            }

            @Override
            public InputStream getResourceAsStream(String s) {
              return null;
            }

            @Override
            public RequestDispatcher getRequestDispatcher(String s) {
              return null;
            }

            @Override
            public RequestDispatcher getNamedDispatcher(String s) {
              return null;
            }

            @Override
            public Servlet getServlet(String s) throws ServletException {
              return null;
            }

            @Override
            public Enumeration getServlets() {
              return null;
            }

            @Override
            public Enumeration getServletNames() {
              return null;
            }

            @Override
            public void log(String s) {

            }

            @Override
            public void log(Exception e, String s) {

            }

            @Override
            public void log(String s, Throwable throwable) {

            }

            @Override
            public String getRealPath(String s) {
              return this.getClass().getClassLoader().getResourceAsStream("").toString();
            }

            @Override
            public String getServerInfo() {
              return null;
            }

            @Override
            public String getInitParameter(String s) {
              return null;
            }

            @Override
            public Enumeration getInitParameterNames() {
              return null;
            }

            @Override
            public Object getAttribute(String s) {
              return null;
            }

            @Override
            public Enumeration getAttributeNames() {
              return null;
            }

            @Override
            public void setAttribute(String s, Object o) {

            }

            @Override
            public void removeAttribute(String s) {

            }

            @Override
            public String getServletContextName() {
              return null;
            }
          };
        }

        @Override
        public String getInitParameter(String s) {
          return null;
        }

        @Override
        public Enumeration getInitParameterNames() {
          return null;
        }
      });
    } catch (ServletException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void shouldAddNewAppointment() {
    String result = service.createAppointmentBook(OWNER);
    assertThat(OWNER, is(equalTo(result)));
  }

  @Test
  public void shouldReturnEmptyString() {
    service.createAppointmentBook(OWNER);
    String result = service.createAppointmentBook(OWNER);

    assertThat("", is(equalTo(result)));
  }

  @Test
  public void shouldReceiveAllOwnerNames() {
    int size = 3;

    for (int i = 0; i < size; i++) {
      service.createAppointmentBook(OWNER + i);
    }

    Set<String> results = service.receiveAllOwnerNames();
    assertThat(results.size(), is(equalTo(size)));
  }

  @Test
  public void shouldCreateAppointment() {

    service.createAppointmentBook(OWNER);
    String result = service.createAppointment(OWNER, DESCRIPTION, BEGINTIME[0], ENDTIME[0]);

    assertThat(result, is(equalTo(new Appointment(DESCRIPTION, BEGINTIME[0], ENDTIME[0]).toString())));
  }

  @Test
  public void shouldPrettyPrintAppointment() {
    service.createAppointmentBook(OWNER);
    for (int i = 0; i < BEGINTIME.length; i++) {
      service.createAppointment(OWNER, DESCRIPTION, BEGINTIME[i], ENDTIME[i]);
    }

    String result = service.prettyPrintAll(OWNER);

    assertThat(result, containsString(OWNER));
    assertThat(result, containsString(DESCRIPTION));

    for (int i = 0; i < BEGINTIME.length; i++) {
      assertThat(result, containsString(DateUtility.parseDateToStringPrettyPrint(DateUtility.parseStringToDate(BEGINTIME[i]))));
      assertThat(result, containsString(DateUtility.parseDateToStringPrettyPrint(DateUtility.parseStringToDate(ENDTIME[i]))));
    }
  }

  @Test
  public void shouldPrettyPrintSearch() {
    service.createAppointmentBook(OWNER);
    for (int i = 0; i < BEGINTIME.length; i++) {
      service.createAppointment(OWNER, DESCRIPTION, BEGINTIME[i], ENDTIME[i]);
    }

    String result = service.prettyPrintSearch(OWNER, BEGINTIME[0], ENDTIME[1]);

    assertThat(result, containsString(OWNER));
    assertThat(result, containsString(DESCRIPTION));

    int i = 0;
    int positiveTestMax = 1;

    for (; i <= 1; i++) {
      assertThat(result, containsString(DateUtility.parseDateToStringPrettyPrint(DateUtility.parseStringToDate(BEGINTIME[i]))));
      assertThat(result, containsString(DateUtility.parseDateToStringPrettyPrint(DateUtility.parseStringToDate(ENDTIME[i]))));
    }

    for (; i< BEGINTIME.length; i++) {
      assertThat(result, not(containsString(DateUtility.parseDateToStringPrettyPrint(DateUtility.parseStringToDate(BEGINTIME[i])))));
      assertThat(result, not(containsString(DateUtility.parseDateToStringPrettyPrint(DateUtility.parseStringToDate(ENDTIME[i])))));
    }
  }

  @Test
  public void shouldCreateFile() {
    service.createAppointmentBook(OWNER);

    String result = service.getDumpFileLocation(OWNER);
    assertThat(result, containsString(OWNER));
  }

  @Test
  public void shouldNotRestoreAppointmentBook() {
    // owner names don't match
    String fileContent =
            "--appointmentbook\n" +
            "  ---appointmentbook_owner\n" +
            "    asdf\n" +
            "--appointment\n" +
            "  ---appointment_description\n" +
            "    asdfasdf\n" +
            "  ---appointment_begintime\n" +
            "    08/12/2016 1:0 am\n" +
            "  ---appointment_endtime\n" +
            "    08/19/2016 1:0 am";

    String result = service.restoreAppointmentBook(OWNER, fileContent);
    assertThat(result, is(equalTo("")));
  }

  @Test
  public void shouldRestoreAppointmentBook() {
    String fileContent =
            "--appointmentbook\n" +
                    "  ---appointmentbook_owner\n" +
                    "    "+ OWNER + "\n" +
                    "--appointment\n" +
                    "  ---appointment_description\n" +
                    "    asdfasdf\n" +
                    "  ---appointment_begintime\n" +
                    "    08/12/2016 1:0 am\n" +
                    "  ---appointment_endtime\n" +
                    "    08/19/2016 1:0 am";

    String result = service.restoreAppointmentBook(OWNER, fileContent);
    assertThat(result, is(equalTo(OWNER)));
  }
}
