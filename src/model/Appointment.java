package model;

import java.io.Serializable;

public class Appointment implements Serializable {

    private static final long serialVersionUID = 1L;

    private String appointmentId;
    private String patientId;
    private String doctorId;
    private String date;        // format: "YYYY-MM-DD"
    private String time;        // format: "HH:MM"
    private String status;      // "SCHEDULED", "CANCELLED", "COMPLETED"

    public Appointment(String appointmentId, String patientId, String doctorId,
                       String date, String time) {
        this.appointmentId = appointmentId;
        this.patientId     = patientId;
        this.doctorId      = doctorId;
        this.date          = date;
        this.time          = time;
        this.status        = "SCHEDULED";
    }

    // Getters
    public String getAppointmentId() { return appointmentId; }
    public String getPatientId()     { return patientId; }
    public String getDoctorId()      { return doctorId; }
    public String getDate()          { return date; }
    public String getTime()          { return time; }
    public String getStatus()        { return status; }

    // Setters
    public void setStatus(String status) { this.status = status; }
    public void setDate(String date)     { this.date = date; }
    public void setTime(String time)     { this.time = time; }

    @Override
    public String toString() {
        return "Appointment ID : " + appointmentId
             + "\n  Patient ID   : " + patientId
             + "\n  Doctor ID    : " + doctorId
             + "\n  Date         : " + date
             + "\n  Time         : " + time
             + "\n  Status       : " + status;
    }
}