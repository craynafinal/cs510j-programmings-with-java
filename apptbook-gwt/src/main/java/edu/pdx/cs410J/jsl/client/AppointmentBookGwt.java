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
  static final String WARNING_OWNER = "Please create an owner before creating an appointment";
  static final String WARNING_DESCRIPTION = "Please add a description";
  static final String WARNING_BEGINTIME = "Please set begin time";
  static final String WARNING_ENDTIME = "Please set end time";
  static final String WARNING_UPLOAD = "Please add content to the text area";

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
  ListBox listbox_begin_hour_search = new ListBox();
  ListBox listbox_begin_min_search = new ListBox();
  ListBox listbox_begin_ampm_search = new ListBox();
  ListBox listbox_end_hour_search = new ListBox();
  ListBox listbox_end_min_search = new ListBox();
  ListBox listbox_end_ampm_search = new ListBox();
  DatePicker datepicker_begin_search = new DatePicker();
  DatePicker datepicker_end_search = new DatePicker();

  // download
  Button button_download = new Button(BUTTON_TEXT);
  ListBox listbox_owners_download = new ListBox();

  // textarea_upload
  Button button_upload = new Button(BUTTON_TEXT);
  TextBox textbox_owner_upload = new TextBox();
  TextArea textarea_upload = new TextArea();

  public AppointmentBookGwt() {
    this(new Alerter() {
      @Override
      public void alert(String message) {
        Window.alert(message);
      }
    });
  }

  @VisibleForTesting
  AppointmentBookGwt(Alerter alerter) {
    this.alerter = alerter;

    buttonsSetup();
    listboxTimeSetup();
  }

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

    // should receive data
    receiveAllAppointmentBooks();
  }

  private void updateAllOwnersListBoxes() {
    addStringsToListBox(listbox_owners, owners);
    addStringsToListBox(listbox_owners_pretty, owners);
    addStringsToListBox(listbox_owners_search, owners);
    addStringsToListBox(listbox_owners_download, owners);
  }

  private void updateSingleOwnerListBoxes(String owner) {
    listbox_owners.addItem(owner);
    listbox_owners_pretty.addItem(owner);
    listbox_owners_search.addItem(owner);
    listbox_owners_download.addItem(owner);
  }

  private void addStringsToListBox(ListBox listbox, Collection<String> items) {
    for (String item : items) {
      listbox.addItem(item);
    }
  }

  private void addNumbersToListBox(ListBox listbox, int start, int max) {
    for (int i = start; i <= max; i ++) {
      listbox.addItem(Integer.toString(i));
    }
  }

  private String getDateTime(DatePicker datepicker, ListBox hour, ListBox min, ListBox ampm) {
    return DateUtility.parseDateToStringWithoutTime(datepicker.getValue()) + " " + hour.getSelectedItemText() + ":" + min.getSelectedItemText() + " " + ampm.getSelectedItemText();
  }

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

  private void getDumpFile(String owner) {
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);
    async.getDumpFileLocation(owner, new AsyncCallback<String>() {
      @Override
      public void onFailure(Throwable throwable) {
        alert(throwable);
      }

      @Override
      public void onSuccess(String s) {
        if (s != "") {
          Window.open(GWT.getHostPageBaseURL() + s, "", "");
        } else {
          displayInAlertDialog("Failed to create a dump file");
        }
      }
    });
  }

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

  void uploadFileContentToServer(String owner, String fileContent) {
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);

    async.restoreAppointmentBook(owner, fileContent, new AsyncCallback<String>() {
      @Override
      public void onFailure(Throwable throwable) {
        alert(throwable);
      }

      @Override
      public void onSuccess(String s) {
        if (s != null) {
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

  private void buttonsSetup() {
    button_upload.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        String ownerName = textbox_owner_upload.getValue();
        String fileContent = textarea_upload.getValue();

        if (ownerName == "") {
          displayInAlertDialog("Please enter the name of the owner!");
        } else if (fileContent == "") {
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
        } else if (textbox_description.getText() == "" || textbox_description.getValue() == "") {
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
        } else if (owner != "") {
          createAppointmentBook(owner);
        } else {
          displayInAlertDialog("Please enter the owner name in the text field!");
        }
      }
    });
  }

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

  private void receiveAllAppointmentBooks() {
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);

    async.receiveAllOwnerNames(new AsyncCallback<Set<String>>() {
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

  @VisibleForTesting
  void createAppointmentBookSilent() {
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);

    async.createAppointmentBook(textbox_owner.getText(), new AsyncCallback<String>() {
      @Override
      public void onFailure(Throwable throwable) { }

      @Override
      public void onSuccess(String s) { }
    });
  }

  private void createAppointmentBook(String owner) {
    AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);

    async.createAppointmentBook(owner, new AsyncCallback<String>() {
      @Override
      public void onFailure(Throwable throwable) {
        alert(throwable);
      }

      @Override
      public void onSuccess(String s) {
        if (s != "") {
          owners.add(s);
          updateSingleOwnerListBoxes(s);
          displayInAlertDialog("The new appointment book for " + s + " has been created!");
        } else {
          displayInAlertDialog("Failed to create an appointment book!");
        }
      }
    });
  }

  private void displayInAlertDialog(String text) {
    alerter.alert(text);
  }

  private void displayInAlertDialog(AppointmentBook airline) {
    StringBuilder sb = new StringBuilder(airline.toString());
    sb.append("\n");

    Collection<Appointment> flights = airline.getAppointments();
    for (Appointment flight : flights) {
      sb.append(flight);
      sb.append("\n");
    }
    alerter.alert(sb.toString());
  }

  private void alert(Throwable ex) {
    alerter.alert(ex.toString());
  }

  private DockPanel getDockPanel(Label label, Widget widget) {
    DockPanel panel = new DockPanel();
    panel.add(label, DockPanel.WEST);
    panel.add(widget, DockPanel.CENTER);
    return panel;
  }

  private FlowPanel getFlowPanel(Widget ... widgets) {
    FlowPanel panel = new FlowPanel();
    for (Widget widget: widgets){
      panel.add(widget);
    }
    return panel;
  }

  private VerticalPanel getVerticalPanel(Widget ... widgets) {
    VerticalPanel panel = new VerticalPanel();
    for (Widget widget: widgets){
      panel.add(widget);
    }
    return panel;
  }

  private void setTabPanel(TabPanel tabPanel, String tabText, Widget widget) {
    tabPanel.add(widget, tabText);
  }

  private Label getLabel(String text) {
    return new Label(text);
  }

  @Override
  public void onModuleLoad() {
    RootPanel rootPanel = RootPanel.get();

    TabPanel tabPanel = new TabPanel();

    setTabPanel(tabPanel, "Create an appointment book",
            getVerticalPanel(
              getLabel("Please add a new owner of an appointment book if you are new."),
              getDockPanel(getLabel("Owner"), textbox_owner),
              button_createAppointmentBook
            )
    );

    setTabPanel(tabPanel, "Create an appointment",
            getVerticalPanel(
              getLabel("Create an appointment of an owner."),
              getDockPanel(getLabel("Owner"), listbox_owners),
              getDockPanel(getLabel("Description"), textbox_description),
              getDockPanel(getLabel("Begin Time"), getFlowPanel(datepicker_begin, listbox_begin_hour, listbox_begin_min, listbox_begin_ampm)),
              getDockPanel(getLabel("Emd Time"), getFlowPanel(datepicker_end, listbox_end_hour, listbox_end_min, listbox_end_ampm)),
              button_createAppointment
            )
    );

    setTabPanel(tabPanel, "Pretty print",
            getVerticalPanel(
              getLabel("Print all appointments of an owner."),
              getDockPanel(getLabel("Owner"), listbox_owners_pretty),
              button_prettyPrint
            )
    );

    setTabPanel(tabPanel, "Search",
            getVerticalPanel(
              getLabel("Search and print ranged appointments of an owner."),
              getDockPanel(getLabel("Owner"), listbox_owners_search),
              getDockPanel(getLabel("Begin Time"), getFlowPanel(datepicker_begin_search, listbox_begin_hour_search, listbox_begin_min_search, listbox_begin_ampm_search)),
              getDockPanel(getLabel("Emd Time"), getFlowPanel(datepicker_end_search, listbox_end_hour_search, listbox_end_min_search, listbox_end_ampm_search)),
              button_search
            )
    );

    setTabPanel(tabPanel, "Download",
            getVerticalPanel(
                    getLabel("Save an appointment book to restore data later."),
                    getDockPanel(getLabel("Owner"), listbox_owners_download),
                    button_download
            )
    );

    setTabPanel(tabPanel, "Upload",
            getVerticalPanel(
                    getLabel("Open previously dumped appointment book file and paste the content here to restore data.\n" +
                            "If there is already an appointment book of the same owner name, it will replace it.\n" +
                            "The filename should be a name of an owner."
                    ),
                    getDockPanel(getLabel("Owner"), textbox_owner_upload),
                    getDockPanel((getLabel("File Content")), textarea_upload),
                    button_upload
            )
    );

    setTabPanel(tabPanel, "README",
            getVerticalPanel(
                    getLabel(readme())
            )
    );

    // set the first tab as default
    tabPanel.selectTab(0);

    rootPanel.add(tabPanel);
  }

  private String readme() {
    String readme =
            "********************************************************\n" +
            "CS410/510J Advanced Java Programming\n" +
            "Project 5: A Rich Internet Application for an Appointment Book\n" +
            "Student: Jong Seong Lee\n" +
            "********************************************************\n\n" +
            "This program will generate scripts for a web application based on GWT\n" +
            "and is going to demonstrate functions from previous assignments.\n\n" +
            "1. Create an appointment book: You should create an appointment to use other features of this web application.\n" +
            "Enter a name of an owner and click submit button.\n\n" +
            "2. Create an appointment: Select an owner and enter description, begin time and end time\n" +
            "to create an appointment for the owner you chose.\n\n" +
            "3. Pretty print: Print all appointments associated to the selected owner in a defined format.\n\n" +
            "4. Search: Same as Pretty print but it will only print the appointments in a range you selects.\n\n" +
            "5. Download: Make the servier generate a dump file to store an appointment book so you may restore later.\n\n" +
            "6. Upload: Use a generated file to restore an appointment book after the server reboots."
            ;
    return readme;
  }

  @VisibleForTesting
  interface Alerter {
    void alert(String message);
  }
}
