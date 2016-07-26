package edu.pdx.cs410J.jsl;

/**
 * This enum is designed to be used in {@link TextDumper} and {@link TextParser} classes.
 * Because hard coded tokens may cause confusion or mismatch, this <code>ParseToken</code> enum
 * will be shared among them and data used will be consistent.
 *
 * @author    Jong Seong Lee
 * @version   %I%, %G%
 * @since     1.0
 */
public enum ParseToken {
    APPOINTMENTBOOK(0, "appointmentbook"),
    APPOINTMENTBOOK_OWNER(1, "appointmentbook_owner"),
    APPOINTMENT(0, "appointment"),
    APPOINTMENT_DESCRIPTION(1, "appointment_description"),
    APPOINTMENT_BEGINTIME(1, "appointment_begintime"),
    APPOINTMENT_ENDTIME(1, "appointment_endtime");

    String token = null;

    /**
     * This constructor takes a level of hierarchy and a string of a token.
     * The top hierarchy level is 0, and increases by 1 every time the hierarchy goes down.
     * When the hierarchy value is 0, it attaches "--" before the token value.
     * Everytime the level increases, the number of dashes increases as well.
     * For example, "---" will be used for the level 1.
     *
     * @param level a level of hierarchy
     * @param token a specific name of a token
     */
    ParseToken(int level, String token) {
        StringBuilder dash = new StringBuilder();
        for (int i = 0; i < level + 2; i++) {
            dash.append("-");
        }
        this.token = dash.toString() + token;
    }

    /**
     * Returns the constructed string of the token.
     * @return a token in string format
     */
    public String getToken() {
        return token;
    }
}
