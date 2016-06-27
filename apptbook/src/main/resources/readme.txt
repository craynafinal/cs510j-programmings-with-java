********************************************************
CS410/510J Advanced Java Programming
Project 1: Designing an Appointment Book Application
Student: Jong Seong Lee
********************************************************

Usage: java -jar target/apptbook-1.0-SNAPSHOT.jar [options] <args>
  args are (in this order):
    owner                    The person whose owns the appt book
    description              A description of the appointment
    beginTime                When the appt begins (24-hour time)
    endTime                  When the appt ends (24-hour time)
  options are (options may appear in any order):
    -print                   Prints a description of the new appointment
    -README                  Prints a README for this project and exits
  Date and time should be in the format: mm/dd/yyyy hh:mm

1. Appointment
A simple class that describes a appointment by description, begin time and end time. This class can be initialized with those arguments, but also it is possible to ignore them to use default data. There are not much interesting methods implemented other than get methods.

2. AppointmentBook
Another simple class that describes the owner of appointments and contains a list of appointments. This class can be initialized with a name of owner or use a default name. This class will use Appointment class to store a list of appointments.

3. Project1
The class that contains main method to utilize both Appointment and AppointmentBook classes. It will parse the command line arguments and create an Appointment and an AppointmentBook instances to add the new appointment to the appointment book. When print option is specified, it will print the description of a new appointment. When readme option is specified, it will print this readme document.
