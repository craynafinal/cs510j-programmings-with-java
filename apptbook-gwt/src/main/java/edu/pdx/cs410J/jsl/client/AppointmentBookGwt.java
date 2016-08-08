package edu.pdx.cs410J.jsl.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DatePicker;

import java.util.*;

/**
 * A basic GWT class that makes sure that we can send an appointment book back from the server
 */
public class AppointmentBookGwt implements EntryPoint {
  private final Alerter alerter;

  private static final String BUTTON_TEXT = "Submit";
  private static final String WARNING_OWNER = "Please create an owner before creating an appointment";
  private static final String WARNING_DESCRIPTION = "Please add a description";
  private static final String WARNING_BEGINTIME = "Please set begin time";
  private static final String WARNING_ENDTIME = "Please set end time";
  private static final String WARNING_UPLOAD = "Please add content to the text area";

  //private Set<AppointmentBook> owners = new TreeSet<>();
  Set<String> owners = new TreeSet<>();

  // create an appointment book
  Button button_createAppointmentBook = new Button(BUTTON_TEXT);
  TextBox textbox_owner = new TextBox();

  // create an appointment
  Button button_createAppointment = new Button(BUTTON_TEXT);
  TextBox textbox_description = new TextBox();
  DatePicker datepicker_begin = new DatePicker();
  DatePicker datepicker_end = new DatePicker();
  ListBox listbox_owners = new ListBox();
  ListBox listbox_begin_hour = new ListBox();
  ListBox listbox_begin_min = new ListBox();
  ListBox listbox_begin_ampm = new ListBox();
  ListBox listbox_end_hour = new ListBox();
  ListBox listbox_end_min = new ListBox();
  ListBox listbox_end_ampm = new ListBox();

  // pretty print
  Button button_prettyPrint = new Button(BUTTON_TEXT);
  ListBox listbox_owners_pretty = new ListBox();

  // search
  Button button_search = new Button(BUTTON_TEXT);
  ListBox listbox_owners_search = new ListBox();

  private ListBox listbox_begin_hour_search = new ListBox();
  private ListBox listbox_begin_min_search = new ListBox();
  private ListBox listbox_begin_ampm_search = new ListBox();
  private ListBox listbox_end_hour_search = new ListBox();
  private ListBox listbox_end_min_search = new ListBox();
  private ListBox listbox_end_ampm_search = new ListBox();

  DatePicker datepicker_begin_search = new DatePicker();
  DatePicker datepicker_end_search = new DatePicker();

  // download
  private Button button_download = new Button(BUTTON_TEXT);

  ListBox listbox_owners_download = new ListBox();

  // textarea_upload
  Button button_upload = new Button(BUTTON_TEXT);
  TextBox textbox_owner_upload = new TextBox();
  TextArea textarea_upload = new TextArea();

  /**
   * GWT constructor.
   */
  public AppointmentBookGwt() {
    this(new Alerter() {
      @Override
      public void alert(String message) {
        Window.alert(message);
      }
    });
  }

  /**
   * GWT constructor for testing.
   *
   * @param alerter
     */
  @VisibleForTesting
  AppointmentBookGwt(Alerter alerter) {
    this.alerter = alerter;

    buttonsSetup();
    listboxTimeSetup();
  }

  /**
   * Initial setup for the list boxes such as begin / end times and owners.
   */
  private void listboxTimeSetup() {
    addNumbersToListBox(listbox_begin_hour, 1, 12);
    addNumbersToListBox(listbox_end_hour, 1, 12);
    addNumbersToListBox(listbox_begin_hour_search, 1, 12);
    addNumbersToListBox(listbox_end_hour_search, 1, 12);
    addNumbersToListBox(listbox_begin_min, 0, 59);
    addNumbersToListBox(listbox_end_min, 0, 59);
    addNumbersToListBox(listbox_begin_min_search, 0, 59);
    addNumbersToListBox(listbox_end_min_search, 0, 59);

    List<String> ampm = new ArrayList<>();
    ampm.add("am");
    ampm.add("pm");

    addStringsToListBox(listbox_begin_ampm, ampm);
    addStringsToListBox(listbox_end_ampm, ampm);
    addStringsToListBox(listbox_begin_ampm_search, ampm);
    addStringsToListBox(listbox_end_ampm_search, ampm);

    receiveAllOwners();
  }

  /**
   * Update the owner related list boxes with a list of owners.
   */
  private void updateAllOwnersListBoxes() {
    addStringsToListBox(listbox_owners, owners);
    addStringsToListBox(listbox_owners_pretty, owners);
    addStringsToListBox(listbox_owners_search, owners);
    addStringsToListBox(listbox_owners_download, owners);
  }

  /**
   * Update the owner related list boxes with a single owner name.
   * @param owner
     */
  private void updateSingleOwnerListBoxes(String owner) {
    listbox_owners.addItem(owner);
    listbox_owners_pretty.addItem(owner);
    listbox_owners_search.addItem(owner);
    listbox_owners_download.addItem(owner);
  }

  /**
   * Add a new string value to a list box.
   *
   * @param listbox
   * @param items
     */
  private void addStringsToListBox(ListBox listbox, Collection<String> items) {
    for (String item : items) {
      listbox.addItem(item);
    }
  }

  /**
   * Add number values to a list box from start to max values specified.
   *
   * @param listbox
   * @param start
   * @param max
     */
  private void addNumbersToListBox(ListBox listbox, int start, int max) {
    for (int i = start; i <= max; i ++) {
      listbox.addItem(Integer.toString(i));
    }
  }

  /**
   * Get formatted date string by passing date information of GUI widgets.
   *
   * @param datepicker
   * @param hour
   * @param min
   * @param ampm
     * @return
     */
  private String getDateTime(DatePicker datepicker, ListBox hour, ListBox min, ListBox ampm) {
    return DateUtility.parseDateToStringWithoutTime(datepicker.getValue()) + " " + hour.getSelectedItemText() + ":" + min.getSelectedItemText() + " " + ampm.getSelectedItemText();
  }

  /**
   * Calls the pretty print functionality on the server side.
   *
   * @param owner
     */
  private void prettyPrintAll(String owner) {
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);

    async.prettyPrintAll(owner, new AsyncCallback<String>() {
      @Override
      public void onFailure(Throwable throwable) {
        alert(throwable);
      }

      @Override
      public void onSuccess(String s) {
        displayInAlertDialog(s);
      }
    });
  }

  /**
   * Calls the pretty print search functionality on the server side.
   *
   * @param owner
   * @param beginTime
   * @param endTime
     */
  private void prettyPrintSearch(String owner, String beginTime, String endTime) {
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);

    async.prettyPrintSearch(owner, beginTime, endTime, new AsyncCallback<String>() {
      @Override
      public void onFailure(Throwable throwable) {
        alert(throwable);
      }

      @Override
      public void onSuccess(String s) {
        displayInAlertDialog(s);
      }
    });
  }

  /**
   * Get dump file location on the server side and opens the file on the browser or download it.
   *
   * @param owner
     */
  private void getDumpFile(String owner) {
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);
    async.getDumpFileLocation(owner, new AsyncCallback<String>() {
      @Override
      public void onFailure(Throwable throwable) {
        alert(throwable);
      }

      @Override
      public void onSuccess(String s) {
        if (s.length() > 0) {
          Window.open(GWT.getHostPageBaseURL() + s, "", "");
        } else {
          displayInAlertDialog("Failed to create a dump file");
        }
      }
    });
  }

  /**
   * Get dump file location on the server side and opens the file on the browser or download it.
   * This function is purely for testing and the result handling could be different from the original function, <code>getDumpFile</code>.
   *
   * @param owner
     */
  @VisibleForTesting
  void getDumpFileTesting(String owner) {
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);
    async.getDumpFileLocation(owner, new AsyncCallback<String>() {
      @Override
      public void onFailure(Throwable throwable) { }

      @Override
      public void onSuccess(String s) { displayInAlertDialog(s); }
    });
  }

  /**
   * Upload the file content to server to restore saved information.
   *
   * @param owner
   * @param fileContent
     */
  private void uploadFileContentToServer(String owner, String fileContent) {
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);

    async.restoreAppointmentBook(owner, fileContent, new AsyncCallback<String>() {
      @Override
      public void onFailure(Throwable throwable) {
        alert(throwable);
      }

      @Override
      public void onSuccess(String s) {
        if (s.length() > 0) {
          if (!owners.contains(s)) {
            owners.add(s);
            updateAllOwnersListBoxes();
          }
          displayInAlertDialog("The appointment book for " + s + " has been restored!");
        } else {
          displayInAlertDialog("Failed to restore the appointment book!");
        }
      }
    });
  }

  /**
   * Initial setup for button handlers.
   */
  private void buttonsSetup() {
    button_upload.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        String ownerName = textbox_owner_upload.getValue();
        String fileContent = textarea_upload.getValue();

        if (ownerName.length() <= 0) {
          displayInAlertDialog("Please enter the name of the owner!");
        } else if (fileContent.length() <= 0) {
          displayInAlertDialog(WARNING_UPLOAD);
        } else {
          uploadFileContentToServer(ownerName, fileContent);
        }

      }
    });

    button_download.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        if (listbox_owners_download.getSelectedValue() == null) {
          displayInAlertDialog(WARNING_OWNER);
        } else {
          getDumpFile(listbox_owners_download.getSelectedItemText());
        }
      }
    });

    button_search.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        if (listbox_owners_search.getSelectedValue() == null) {
          displayInAlertDialog(WARNING_OWNER);
        } else if (datepicker_begin_search.getValue() == null) {
          displayInAlertDialog(WARNING_BEGINTIME);
        } else if (datepicker_end_search.getValue() == null) {
          displayInAlertDialog(WARNING_ENDTIME);
        } else {
          String owner = listbox_owners_search.getSelectedItemText();
          String beginTime = getDateTime(datepicker_begin_search, listbox_begin_hour_search, listbox_begin_min_search, listbox_begin_ampm_search);
          String endTime = getDateTime(datepicker_end_search, listbox_end_hour_search, listbox_end_min_search, listbox_end_ampm_search);

          prettyPrintSearch(owner, beginTime, endTime);
        }
      }
    });

    button_prettyPrint.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        if (listbox_owners_pretty.getSelectedValue() == null) {
          displayInAlertDialog(WARNING_OWNER);
        } else {
          prettyPrintAll(listbox_owners_pretty.getSelectedItemText());
        }
      }
    });

    button_createAppointment.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        if (listbox_owners.getSelectedValue() == null) {
          displayInAlertDialog(WARNING_OWNER);
        } else if (textbox_description.getText().length() <= 0 || textbox_description.getValue().length() <= 0) {
          displayInAlertDialog(WARNING_DESCRIPTION);
        } else if (datepicker_begin.getValue() == null) {
          displayInAlertDialog(WARNING_BEGINTIME);
        } else if (datepicker_end.getValue() == null) {
          displayInAlertDialog(WARNING_ENDTIME);
        } else {
          String owner = listbox_owners.getSelectedItemText();
          String description = textbox_description.getValue();
          String beginTime = getDateTime(datepicker_begin, listbox_begin_hour, listbox_begin_min, listbox_begin_ampm);
          String endTime = getDateTime(datepicker_end, listbox_end_hour, listbox_end_min, listbox_end_ampm);
          createAppointment(owner, description, beginTime, endTime);
        }
      }
    });

    button_createAppointmentBook.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        String owner = textbox_owner.getValue();

        if (owners.contains(owner)) {
          displayInAlertDialog("The owner name \"" + owner + "\" already exists!");
        } else if (owner.length() > 0) {
          createAppointmentBook(owner);
        } else {
          displayInAlertDialog("Please enter the owner name in the text field!");
        }
      }
    });
  }

  /**
   * Calls the appointment creation functionality on the server side.
   * This function is purely for testing and the result handling could be different from the original function, <code>createAppointment</code>.
   *
   * @param owner
   * @param description
   * @param beginTime
   * @param endTime
     */
  @VisibleForTesting
  void createAppointmentSilent(String owner, String description, String beginTime, String endTime) {
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);
    async.createAppointment(owner, description, beginTime, endTime, new AsyncCallback<String>() {
      @Override
      public void onFailure(Throwable throwable) { }

      @Override
      public void onSuccess(String s) { }
    });
  }

  /**
   * Calls the appointment creation functionality on the server side.
   *
   * @param owner
   * @param description
   * @param beginTime
   * @param endTime
     */
  private void createAppointment(String owner, String description, String beginTime, String endTime) {
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);
    async.createAppointment(owner, description, beginTime, endTime, new AsyncCallback<String>() {
      @Override
      public void onFailure(Throwable throwable) {
        alert(throwable);
      }

      @Override
      public void onSuccess(String s) {
        displayInAlertDialog("The new appointment created: " + s);
      }
    });
  }

  /**
   * Receives all owner names from the server in <code>Set</code> collection.
   */
  private void receiveAllOwners() {
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);

    async.getAllOwnerNames(new AsyncCallback<Set<String>>() {
      @Override
      public void onFailure(Throwable throwable) {
        alert(throwable);
      }

      @Override
      public void onSuccess(Set<String> strings) {
        owners.addAll(strings);
        updateAllOwnersListBoxes();
      }
    });
  }

  /**
   * Calls the appointment book creation functionality on the server side.
   * This function is purely for testing and the result handling could be different from the original function, <code>createAppointmentBookSilent</code>.
   */
  @VisibleForTesting
  void createAppointmentBookSilent(String owner) {
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);

    async.createAppointmentBook(owner, new AsyncCallback<String>() {
      @Override
      public void onFailure(Throwable throwable) { }

      @Override
      public void onSuccess(String s) { }
    });
  }

  /**
   * Calls the appointment book creation functionality on the server side.
   *
   * @param owner
     */
  private void createAppointmentBook(String owner) {
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);

    async.createAppointmentBook(owner, new AsyncCallback<String>() {
      @Override
      public void onFailure(Throwable throwable) {
        alert(throwable);
      }

      @Override
      public void onSuccess(String s) {
        if (s.length() > 0) {
          owners.add(s);
          updateSingleOwnerListBoxes(s);
          displayInAlertDialog("The new appointment book for " + s + " has been created!");
        } else {
          displayInAlertDialog("Failed to create an appointment book!");
        }
      }
    });
  }

  /**
   * Displays the message to the alert dialog.
   * @param text
     */
  private void displayInAlertDialog(String text) {
    alerter.alert(text);
  }

  /**
   * Alerts the error from the server.
   * @param ex
     */
  private void alert(Throwable ex) {
    alerter.alert(ex.toString());
  }

  /**
   * Creates a dock panel object with the label on the west and the widget in the center.
   * @param label
   * @param widget
   * @return
     */
  private DockPanel getDockPanel(Label label, Widget widget) {
    DockPanel panel = new DockPanel();
    panel.add(label, DockPanel.WEST);
    panel.add(widget, DockPanel.CENTER);
    return panel;
  }

  /**
   * Creates a flow panel object with multiple widgets.
   *
   * @param widgets
   * @return
     */
  private FlowPanel getFlowPanel(Widget ... widgets) {
    FlowPanel panel = new FlowPanel();
    for (Widget widget: widgets){
      panel.add(widget);
    }
    return panel;
  }

  /**
   * Creates a vertical panel with multiple widgets.
   *
   * @param widgets
   * @return
     */
  private VerticalPanel getVerticalPanel(Widget ... widgets) {
    VerticalPanel panel = new VerticalPanel();
    for (Widget widget: widgets){
      panel.add(widget);
    }
    return panel;
  }

  /**
   * Creates a HTML object with the specified content inside.
   *
   * @param text
   * @return
     */
  private HTML getHTML(String text) {
    return new HTML(text);
  }

  /**
   * Setup a new tab for the tab panel object with the tab name and the widget inside.
   *
   * @param tabPanel
   * @param tabText
   * @param widget
     */
  private void setTabPanel(TabPanel tabPanel, String tabText, Widget widget) {
    tabPanel.add(widget, tabText);
  }

  /**
   * Creates a label object with the specified text.
   *
   * @param text
   * @return
     */
  private Label getLabel(String text) {
    return new Label(text);
  }

  /**
   * Initializes the GUI structure of this web application.
   */
  @Override
  public void onModuleLoad() {
    RootPanel rootPanel = RootPanel.get();

    TabPanel tabPanel = new TabPanel();

    setTabPanel(tabPanel, "Create an appointment book",
            getVerticalPanel(
              getHTML("Please add a new owner of an appointment book if you are new."),
              getDockPanel(getLabel("Owner"), textbox_owner),
              button_createAppointmentBook
            )
    );

    setTabPanel(tabPanel, "Create an appointment",
            getVerticalPanel(
              getHTML("Create an appointment of an owner."),
              getDockPanel(getLabel("Owner"), listbox_owners),
              getDockPanel(getLabel("Description"), textbox_description),
              getDockPanel(getLabel("Begin Time"), getFlowPanel(datepicker_begin, listbox_begin_hour, listbox_begin_min, listbox_begin_ampm)),
              getDockPanel(getLabel("Emd Time"), getFlowPanel(datepicker_end, listbox_end_hour, listbox_end_min, listbox_end_ampm)),
              button_createAppointment
            )
    );

    setTabPanel(tabPanel, "Pretty print",
            getVerticalPanel(
              getHTML("Print all appointments of an owner."),
              getDockPanel(getLabel("Owner"), listbox_owners_pretty),
              button_prettyPrint
            )
    );

    setTabPanel(tabPanel, "Search",
            getVerticalPanel(
              getHTML("Search and print ranged appointments of an owner."),
              getDockPanel(getLabel("Owner"), listbox_owners_search),
              getDockPanel(getLabel("Begin Time"), getFlowPanel(datepicker_begin_search, listbox_begin_hour_search, listbox_begin_min_search, listbox_begin_ampm_search)),
              getDockPanel(getLabel("Emd Time"), getFlowPanel(datepicker_end_search, listbox_end_hour_search, listbox_end_min_search, listbox_end_ampm_search)),
              button_search
            )
    );

    setTabPanel(tabPanel, "Download",
            getVerticalPanel(
                    getHTML("Save an appointment book to restore data later.<br>" +
                            "This will create a text file for you to save your appointment book.<br>" +
                            "You may use the content of the file in the upload section of this application."),
                    getDockPanel(getLabel("Owner"), listbox_owners_download),
                    button_download
            )
    );

    setTabPanel(tabPanel, "Upload",
            getVerticalPanel(
                    getHTML("Open previously dumped appointment book file and paste the content here to restore data.<br>" +
                            "If there is already an appointment book of the same owner name, it will replace it.<br>" +
                            "Please enter the owner name, and the file content in the text box and the text area in order.<br>" +
                            "The owner names of the text box and the text area must match; otherwise it won't restore your data."
                    ),
                    getDockPanel(getLabel("Owner"), textbox_owner_upload),
                    getDockPanel((getLabel("File Content")), textarea_upload),
                    button_upload
            )
    );

    setTabPanel(tabPanel, "README",
            getVerticalPanel(
                    getHTML(readme())
            )
    );

    // set the first tab as default
    tabPanel.selectTab(0);

    rootPanel.add(tabPanel);
  }

  /**
   * Prints the readme document for this project.
   *
   * @return
     */
  private String readme() {
    String readme =
            "********************************************************<br>" +
            "CS410/510J Advanced Java Programming<br>" +
            "Project 5: A Rich Internet Application for an Appointment Book<br>" +
            "Student: Jong Seong Lee<br>" +
            "********************************************************<br><br>" +
            "This program will generate scripts for a web application based on GWT<br>" +
            "and is going to demonstrate functions from previous assignments.<br><br>" +
            "1. Create an appointment book: You should create an appointment to use other features of this web application.<br>" +
            "Enter a name of an owner and click submit button.<br><br>" +
            "2. Create an appointment: Select an owner and enter description, begin time and end time<br>" +
            "to create an appointment for the owner you chose.<br><br>" +
            "3. Pretty print: Print all appointments associated to the selected owner in a defined format.<br><br>" +
            "4. Search: Same as Pretty print but it will only print the appointments in a range you selects.<br><br>" +
            "5. Download: Make the servier generate a dump file to store an appointment book so you may restore later.<br><br>" +
            "6. Upload: Use a content of a generated file to restore an appointment book after the server reboots."
            ;
    return readme;
  }

  /**
   * Alert for the testing.
   */
  @VisibleForTesting
  interface Alerter {
    void alert(String message);
  }
}
