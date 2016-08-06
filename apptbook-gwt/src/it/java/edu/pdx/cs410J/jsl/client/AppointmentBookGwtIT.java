package edu.pdx.cs410J.jsl.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import org.junit.Test;

import java.util.Set;

/**
 * An integration test for the airline GWT UI.  Remember that GWTTestCase is JUnit 3 style.
 * So, test methods names must begin with "test".
 * And since this test code is compiled into JavaScript, you can't use hamcrest matchers.  :(
 */
public class AppointmentBookGwtIT extends GWTTestCase {
  private static final String[] OWNER = { "my owner 1", "my owner 2" };
  private static final String DESCRIPTION = "my description";
  private static final String BEGINTIME = "1/1/2000 11:11 am";
  private static final String ENDTIME = "2/2/2001 10:10 pm";

  static final String WARNING_OWNER = "Please create an owner before creating an appointment";
  static final String WARNING_DESCRIPTION = "Please add a description";
  static final String WARNING_BEGINTIME = "Please set begin time";
  static final String WARNING_ENDTIME = "Please set end time";

  private final CapturingAlerter alerter = new CapturingAlerter();

  @Override
  public String getModuleName() {
    return "edu.pdx.cs410J.jsl.AppointmentBookIntegrationTests";
  }

  @Test
  public void testCreatingAppointmentWithMissingOwner() {
    AppointmentBookGwt ui = new AppointmentBookGwt(alerter);

    ui.listbox_owners.setSelectedIndex(0);
    click(ui.button_createAppointment);

    Timer verify = new Timer() {
      @Override
      public void run() {
        checkMessageContains(true, WARNING_OWNER);
        finishTest();
      }
    };
    waitForRPCCall(verify);
  }

  @Test
  public void testCreatingAppointmentWithMissingDescription() {
    AppointmentBookGwt ui = new AppointmentBookGwt(alerter);

    // temporally add an owner name
    ui.owners.add(OWNER[0]);
    ui.listbox_owners.addItem(OWNER[0]);

    ui.listbox_owners.setSelectedIndex(0);
    click(ui.button_createAppointment);

    Timer verify = new Timer() {
      @Override
      public void run() {
        checkMessageContains(true, WARNING_DESCRIPTION);
        finishTest();
      }
    };
    waitForRPCCall(verify);
  }

  @Test
  public void testCreatingAppointmentWithMissingBeginTime() {
    AppointmentBookGwt ui = new AppointmentBookGwt(alerter);

    // temporally add an owner name
    ui.owners.add(OWNER[0]);
    ui.listbox_owners.addItem(OWNER[0]);

    ui.listbox_owners.setSelectedIndex(0);
    ui.textbox_description.setText(DESCRIPTION);
    click(ui.button_createAppointment);

    Timer verify = new Timer() {
      @Override
      public void run() {
        checkMessageContains(true, WARNING_BEGINTIME);
        finishTest();
      }
    };
    waitForRPCCall(verify);
  }

  @Test
  public void testCreatingAppointmentWithMissingEndTime() {
    AppointmentBookGwt ui = new AppointmentBookGwt(alerter);

    // temporally add an owner name
    ui.owners.add(OWNER[0]);
    ui.listbox_owners.addItem(OWNER[0]);

    ui.listbox_owners.setSelectedIndex(0);
    ui.textbox_description.setText(DESCRIPTION);
    ui.datepicker_begin.setValue(DateUtility.parseStringToDate(BEGINTIME));
    click(ui.button_createAppointment);

    Timer verify = new Timer() {
      @Override
      public void run() {
        checkMessageContains(true, WARNING_ENDTIME);
        finishTest();
      }
    };
    waitForRPCCall(verify);
  }

  @Test
  public void testPrettyPrintWithoutOwnerCreated() {
    AppointmentBookGwt ui = new AppointmentBookGwt(alerter);

    ui.listbox_owners_pretty.setSelectedIndex(0);
    click(ui.button_prettyPrint);

    Timer verify = new Timer() {
      @Override
      public void run() {
        checkMessageContains(true, WARNING_OWNER);
        finishTest();
      }
    };
    waitForRPCCall(verify);
  }

  @Test
  public void testPrettyPrintAll() {
    AppointmentBookGwt ui = new AppointmentBookGwt(alerter);

    // add a new owner
    ui.textbox_owner.setText(OWNER[0]);
    ui.createAppointmentBookSilent();
    ui.owners.add(OWNER[0]);
    ui.listbox_owners.addItem(OWNER[0]);
    ui.listbox_owners_pretty.addItem(OWNER[0]);

    // add an appointment
    ui.listbox_owners.setSelectedIndex(0);
    ui.createAppointmentSilent(OWNER[0], DESCRIPTION, BEGINTIME, ENDTIME);

    // pretty print
    ui.listbox_owners_pretty.setSelectedIndex(0);
    click(ui.button_prettyPrint);

    Timer verify = new Timer() {
      @Override
      public void run() {
        checkMessageContains(true, OWNER[0], DESCRIPTION,
                DateUtility.parseDateToStringPrettyPrint(DateUtility.parseStringToDate(BEGINTIME)),
                DateUtility.parseDateToStringPrettyPrint(DateUtility.parseStringToDate(ENDTIME)));
        finishTest();
      }
    };
    waitForRPCCall(verify);
  }

  @Test
  public void testPrettyPrintSearch() {
    AppointmentBookGwt ui = new AppointmentBookGwt(alerter);

    String date_1 = "1/1/2000 01:00 am";
    String date_2 = "2/1/2000 01:00 am";
    String date_3 = "3/1/2000 01:00 am";

    // add a new owner
    ui.textbox_owner.setText(OWNER[0]);
    ui.createAppointmentBookSilent();
    ui.owners.add(OWNER[0]);
    ui.listbox_owners.addItem(OWNER[0]);
    ui.listbox_owners_search.addItem(OWNER[0]);

    // add appointments
    ui.listbox_owners.setSelectedIndex(0);
    ui.createAppointmentSilent(OWNER[0], DESCRIPTION, date_1, date_1);
    ui.createAppointmentSilent(OWNER[0], DESCRIPTION, date_2, date_2);
    ui.createAppointmentSilent(OWNER[0], DESCRIPTION, date_3, date_3);

    // pretty print
    ui.listbox_owners_search.setSelectedIndex(0);
    ui.datepicker_begin_search.setValue(DateUtility.parseStringToDate(date_1));
    ui.datepicker_end_search.setValue(DateUtility.parseStringToDate(date_2));
    click(ui.button_search);

    Timer verify = new Timer() {
      @Override
      public void run() {
        checkMessageContains(true, OWNER[0], DESCRIPTION,
                DateUtility.parseDateToStringPrettyPrint(DateUtility.parseStringToDate("1/1/2000 01:00 am")),
                DateUtility.parseDateToStringPrettyPrint(DateUtility.parseStringToDate("2/1/2000 01:00 am")));
        checkMessageContains(false, DateUtility.parseDateToStringPrettyPrint(DateUtility.parseStringToDate("3/1/2000 1:1 pm")));
        finishTest();
      }
    };
    waitForRPCCall(verify);
  }

  @Test
  public void testCreatingAppointment() {
    AppointmentBookGwt ui = new AppointmentBookGwt(alerter);

    // add a new owner
    ui.textbox_owner.setText(OWNER[0]);
    ui.createAppointmentBookSilent();
    ui.owners.add(OWNER[0]);
    ui.listbox_owners.addItem(OWNER[0]);

    // setup info for the new appoinment
    ui.listbox_owners.setSelectedIndex(0);
    ui.textbox_description.setText(DESCRIPTION);
    ui.datepicker_begin.setValue(DateUtility.parseStringToDate(BEGINTIME));
    ui.datepicker_end.setValue(DateUtility.parseStringToDate(ENDTIME));
    ui.listbox_begin_hour.setSelectedIndex(10);
    ui.listbox_begin_min.setSelectedIndex(11);
    ui.listbox_begin_ampm.setSelectedIndex(0);
    ui.listbox_end_hour.setSelectedIndex(9);
    ui.listbox_end_min.setSelectedIndex(10);
    ui.listbox_end_ampm.setSelectedIndex(1);
    click(ui.button_createAppointment);

    Timer verify = new Timer() {
      @Override
      public void run() {
        checkMessageContains(true, new Appointment(DESCRIPTION, BEGINTIME, ENDTIME).toString());
        finishTest();
      }
    };
    waitForRPCCall(verify);
  }

  @Test
  public void testClickingCreateAppointmentBookButtonAlertsWithNotification() {

    AppointmentBookGwt ui = new AppointmentBookGwt(alerter);
    ui.textbox_owner.setValue(OWNER[1]);
    click(ui.button_createAppointmentBook);

    Timer verify = new Timer() {
      @Override
      public void run() {
        checkMessageContains(true, "The new appontment book for " + OWNER[1] + " has been created!");
        finishTest();
      }
    };
    waitForRPCCall(verify);
  }

  @Test
  public void testCreatingDuplicateAppointmentBook() {
    AppointmentBookGwt ui = new AppointmentBookGwt(alerter);
    Set<String> owners = ui.owners;
    owners.add(OWNER[0]);
    ui.textbox_owner.setText(OWNER[0]);
    click(ui.button_createAppointmentBook);

    Timer verify = new Timer() {
      @Override
      public void run() {
        checkMessageContains(true, "The owner name \"" + OWNER[0] + "\" already exists");
        finishTest();
      }
    };

    waitForRPCCall(verify);
  }

  private void waitForRPCCall(Timer verify) {
    // Wait for the RPC call to return
    verify.schedule(500);
    delayTestFinish(1000);
  }

  private void checkMessageContains(Boolean include, String ... msgs) {
    String message = alerter.getMessage();
    assertNotNull(message);
    for (String msg : msgs) {
      if (include) {
        assertTrue(message, message.contains(msg));
      } else {
        assertFalse(message, message.contains(msg));
      }
    }
  }

  /**
   * Clicks a <code>Button</code>
   *
   * One would think that you could testing clicking a button_createAppointment with Button.click(), but it looks
   * like you need to fire the native event instead.  Lame.
   *
   * @param button
   *        The button_createAppointment to click
   */
  private void click(Button button) {
    NativeEvent event = Document.get().createClickEvent(0, 0, 0, 0, 0, false, false, false, false);
    DomEvent.fireNativeEvent(event, button);
  }

  private class CapturingAlerter implements AppointmentBookGwt.Alerter {
    private String message;

    @Override
    public void alert(String message) {
      this.message = message;
    }

    public String getMessage() {
      return message;
    }
  }
}