package edu.pdx.cs410J.jsl;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;

/**
 * A helper class for accessing the rest client.
 *
 * @author Jong Seong Lee
 * @version   %I%, %G%
 * @since     1.0
 */
public class AppointmentBookRestClient extends HttpRequestHelper
{
    private static final String WEB_APP = "apptbook";
    private static final String SERVLET = "appointments";
    private final String url;

    /**
     * Creates a client to the appointment book REST service running on the given host and port.
     *
     * @param hostName The name of the host
     * @param port The port
     */
    public AppointmentBookRestClient( String hostName, int port )
    {
        this.url = String.format( "http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET );
    }

    /**
     * Pretty prints the appointment book and its appointments.
     *
     * @param owner
     * @return
     * @throws IOException
     */
    public Response prettyPrintAppointmentBook(String owner) throws IOException {
        return get(this.url, "owner", owner);
    }

    /**
     * Creates an appointment.
     *
     * @param owner
     * @param description
     * @param beginTime
     * @param endTime
     * @return
     * @throws IOException
     */
    public Response createAppointment(String owner, String description, String beginTime, String endTime) throws IOException {
        return post(this.url, "owner", owner, "description", description, "beginTime", beginTime, "endTime", endTime);
    }

    /**
     * Searches appointments.
     *
     * @param owner
     * @param beginTime
     * @param endTime
     * @return
     * @throws IOException
     */
    public Response searchAppointment(String owner, String beginTime, String endTime) throws IOException {
        return get(this.url, "owner", owner, "beginTime", beginTime, "endTime", endTime);
    }
}
