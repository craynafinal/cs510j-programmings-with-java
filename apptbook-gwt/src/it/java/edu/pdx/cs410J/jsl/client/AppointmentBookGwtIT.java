package edu.pdx.cs410J.jsl.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Set;

/**
 * An integration test for the airline GWT UI.  Remember that GWTTestCase is JUnit 3 style.
 * So, test methods names must begin with "test".
 * And since this test code is compiled into JavaScript, you can't use hamcrest matchers.  :(
 */
public class AppointmentBookGwtIT extends GWTTestCase {
  private final CapturingAlerter alerter = new CapturingAlerter();

  @Override
  public String getModuleName() {
    return "edu.pdx.cs410J.jsl.AppointmentBookIntegrationTests";
  }

  @Ignore
  @Test
  public void testCreatingAppointment() {

    /*
    AppointmentBookGwt ui = new AppointmentBookGwt(alerter);

    ui.textbox_owner.setText("my owner");
    click(ui.button_createAppointmentBook);

    click(ui.button_createAppointment);

    Timer verify = new Timer() {
      @Override
      public void run() {
        checkMessage("The new appontment book for my owner has been created!");
      }
    };
    waitForRPCCall(verify);

    ui.listbox_owners.setSelectedIndex(0);
    ui.textbox_description.setText("my description");
    ui.datepicker_begin.setValue(DateUtility.parseStringToDate("1/1/2000 11:11 am"));
    ui.datepicker_end.setValue(DateUtility.parseStringToDate("2/2/2001 10:10 pm"));
    ui.listbox_begin_hour.setSelectedIndex(10);
    ui.listbox_begin_min.setSelectedIndex(11);
    ui.listbox_begin_ampm.setSelectedIndex(0);
    ui.listbox_end_hour.setSelectedIndex(9);
    ui.listbox_end_min.setSelectedIndex(10);
    ui.listbox_end_ampm.setSelectedIndex(1);

    Timer verify2 = new Timer() {
      @Override
      public void run() {
        checkMessage("my description");
        finishTest();
      }
    };
    waitForRPCCall(verify2);*/
  }

  @Test
  public void testClickingCreateAppointmentBookButtonAlertsWithNotification() {
    AppointmentBookGwt ui = new AppointmentBookGwt(alerter);
    ui.textbox_owner.setText("my owner");
    click(ui.button_createAppointmentBook);

    Timer verify = new Timer() {
      @Override
      public void run() {
        checkMessage("The new appontment book for my owner has been created!");
        finishTest();
      }
    };
    waitForRPCCall(verify);
  }

  @Test
  public void testCreatingDuplicateAppointmentBook() {
    AppointmentBookGwt ui = new AppointmentBookGwt(alerter);
    Set<String> owners = ui.getOwners();
    owners.add("my owner");
    ui.textbox_owner.setText("my owner");
    click(ui.button_createAppointmentBook);

    Timer verify = new Timer() {
      @Override
      public void run() {
        checkMessage("The owner name \"my owner\" already exists");
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

  private void checkMessage(String msg) {
    String message = alerter.getMessage();
    assertNotNull(message);
    assertTrue(message, message.contains(msg));
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