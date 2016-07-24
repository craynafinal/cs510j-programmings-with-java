package edu.pdx.cs410J.jsl;

import com.google.common.annotations.VisibleForTesting;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.*;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>AppointmentBook</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple key/value pairs.
 *
 * @author Jong Seong Lee
 * @version   %I%, %G%
 * @since     1.0
 */
public class AppointmentBookServlet extends HttpServlet
{
    private final Map<String, AppointmentBook> appointmentBooks = new HashMap<>();

    public AppointmentBookServlet() {
        //createPreCannedAppointmentBook();
    }

    private void createPreCannedAppointmentBook() {
        String owner = "PreCannedOwner";
        AppointmentBook book = new AppointmentBook(owner);
        this.appointmentBooks.put(owner, book);
    }

    /**
     * Writes a message to the response provided using a <code>PrintWriter</code>
     * @param message a message to be printed
     * @param response
     * @throws IOException
     */
    private void writeMessage(String message, HttpServletResponse response) throws IOException
    {
        PrintWriter pw = response.getWriter();
        pw.println(message);
        pw.flush();
    }

    /**
     * Sets the status of a <code>HttpServletResponse</code> object to <code>SC_OK</code>.
     *
     * @param response
     */
    private void setStatusOK(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Sets the status of a <code>HttpServletResponse</code> object to <code>SC_PRECONDITION_FAILED</code>.
     * @param response
     */
    private void setStatusPrecondFailed(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
    }

    /**
     * Handles an HTTP GET request from a client by writing the value of the key
     * specified in the "key" HTTP parameter to the HTTP response.  If the "key"
     * parameter is not specified, all of the key/value pairs are written to the
     * HTTP response.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/plain");
        String owner = getParameter("owner", request);
        String beginTime = getParameter("beginTime", request);
        String endTime = getParameter("endTime", request);

        if (owner == null) {
            writeMessage("The owner name is not provided, please try again...", response);
            setStatusPrecondFailed(response);
            return;
        }

        AppointmentBook book = getAppointmentBookForOwner(owner);

        if (book == null) {
            writeMessage("There is no appointment book matching the owner name: " + owner, response);
            setStatusPrecondFailed(response);
            return;
        }

        if (beginTime != null && endTime != null) {
            AppointmentBook tempAppointmentBook = null;
            try {
                tempAppointmentBook = getAppointmentBookWithSearchedAppointments(book, beginTime, endTime);
            } catch (ParseException e) {
                writeMessage(e.getMessage(), response);
                setStatusPrecondFailed(response);
                return;
            }

            prettyPrint(tempAppointmentBook, response.getWriter());
            setStatusOK(response);
        } else if (beginTime == null && endTime == null) {
            prettyPrint(book, response.getWriter());
            setStatusOK(response);
        } else if (beginTime == null) {
            writeMessage("The begin time is not provided, please try again...", response);
            setStatusPrecondFailed(response);
        } else {
            writeMessage("The end time is not provided, please try again...", response);
            setStatusPrecondFailed(response);
        }
    }

    /**
     * Returns an <code>AppointmentBook</code> object containig only <code>Appointment</code> objects within
     * <code>beginTime</code> and <code>endTime</code>.
     *
     * @param book an original <code>AppointmentBook</code> object
     * @param beginTime
     * @param endTime
     * @return a new <code>AppointmentBook</code> object containing only <code>Appointment</code> objects meets the condition
     * @throws ParseException
     */
    private AppointmentBook getAppointmentBookWithSearchedAppointments(AppointmentBook book, String beginTime, String endTime) throws ParseException {
        AppointmentBook tempAppointmentBook = new AppointmentBook(book.getOwnerName());
        Date begin_date = null;
        Date end_date = null;

        begin_date = DateUtility.parseStringToDate(beginTime);
        end_date = DateUtility.parseStringToDate(endTime);

        for (Appointment appointment: book.getAppointments()) {
            if (appointment.getBeginTime().compareTo(begin_date) >= 0
                    && appointment.getEndTime().compareTo(end_date) <= 0) {
                tempAppointmentBook.addAppointment(appointment);
            }
        }

        return tempAppointmentBook;
    }

    /**
     * Pretty print a provided <code>AppointmentBook</code> object.
     *
     * @param book
     * @param printWriter
     * @throws IOException
     */
    private void prettyPrint(AppointmentBook book, PrintWriter printWriter) throws IOException {
        PrettyPrinter pretty = new PrettyPrinter(printWriter);
        pretty.dump(book);
    }

    /**
     * Get an <code>AppointmentBook</code> object with a provided owner name.
     *
     * @param owner
     * @return
     */
    @VisibleForTesting
    AppointmentBook getAppointmentBookForOwner(String owner) {
        return this.appointmentBooks.get(owner);
    }

    /**
     * Handles an HTTP POST request by storing the key/value pair specified by the
     * "key" and "value" request parameters.  It writes the key/value pair to the
     * HTTP response.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/plain");

        String owner = getParameter("owner", request);

        AppointmentBook book = getAppointmentBookForOwner(owner);

        // if there is no appointment book with the owner name, create one
        if (book == null) {
            book = new AppointmentBook(owner);
            this.appointmentBooks.put(owner, book);
        }

        String description = getParameter("description", request);
        String beginTime = getParameter("beginTime", request);
        String endTime = getParameter("endTime", request);

        Appointment appointment = null;

        try {
            appointment = new Appointment(description, beginTime, endTime);
        } catch (ParseException e) {
            writeMessage("Failed to parse a time argument provided\n" + e.getMessage(),response);
            setStatusPrecondFailed(response);
            return;
        } catch (NullPointerException e) {

            String target = null;
            if (description == null) {
                target = "description";
            } else if (beginTime == null) {
                target = "beginTime";
            } else {
                target = "endTime";
            }

            writeMessage("Missing argument for an appointment " + target, response);
            setStatusPrecondFailed(response);
            return;
        }

        book.addAppointment(appointment);
        setStatusOK(response);
    }

    /**
     * Handles an HTTP DELETE request by removing all key/value pairs.  This
     * behavior is exposed for testing purposes only.  It's probably not
     * something that you'd want a real application to expose.
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");

        this.appointmentBooks.clear();

        PrintWriter pw = response.getWriter();
        pw.println(Messages.allMappingsDeleted());
        pw.flush();

        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Writes an error message about a missing parameter to the HTTP response.
     *
     * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
     */
    private void missingRequiredParameter(HttpServletResponse response, String parameterName) throws IOException
    {
        String message = Messages.missingRequiredParameter(parameterName);
        response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
    }

    /**
     * Returns the value of the HTTP request parameter with the given name.
     *
     * @return <code>null</code> if the value of the parameter is
     *         <code>null</code> or is the empty string
     */
    private String getParameter(String name, HttpServletRequest request) {
      String value = request.getParameter(name);
      if (value == null || "".equals(value)) {
        return null;

      } else {
        return value.replace("%20", " ");
      }
    }
}
