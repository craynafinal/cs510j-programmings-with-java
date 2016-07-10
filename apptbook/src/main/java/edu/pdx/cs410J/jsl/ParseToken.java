package edu.pdx.cs410J.jsl;

public enum ParseToken {
    APPOINTMENTBOOK(0, "appointmentbook"),
    APPOINTMENTBOOK_OWNER(1, "appointmentbook_owner"),
    APPOINTMENT(0, "appointment"),
    APPOINTMENT_DESCRIPTION(1, "appointment_description"),
    APPOINTMENT_BEGINTIME(1, "appointment_begintime"),
    APPOINTMENT_ENDTIME(1, "appointment_endtime");

    String token = null;
    ParseToken(int level, String token) {
        StringBuilder dash = new StringBuilder();
        for (int i = 0; i < level + 2; i++) {
            dash.append("-");
        }
        this.token = dash.toString() + token;
    }

    public String getToken() {
        return token;
    }
}
