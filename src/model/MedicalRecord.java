package model;

import java.io.Serializable;

public class MedicalRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private String recordId;
    private String patientId;
    private String doctorId;
    private String date;
    private String diagnosis;
    private String notes;

    public MedicalRecord(String recordId, String patientId, String doctorId,
                         String date, String diagnosis, String notes) {
        this.recordId  = recordId;
        this.patientId = patientId;
        this.doctorId  = doctorId;
        this.date      = date;
        this.diagnosis = diagnosis;
        this.notes     = notes;
    }

    // Getters
    public String getRecordId()  { return recordId; }
    public String getPatientId() { return patientId; }
    public String getDoctorId()  { return doctorId; }
    public String getDate()      { return date; }
    public String getDiagnosis() { return diagnosis; }
    public String getNotes()     { return notes; }

    // Setters
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public void setNotes(String notes)         { this.notes = notes; }

    @Override
    public String toString() {
        return "Record ID  : " + recordId
             + "\n  Patient  : " + patientId
             + "\n  Doctor   : " + doctorId
             + "\n  Date     : " + date
             + "\n  Diagnosis: " + diagnosis
             + "\n  Notes    : " + notes;
    }
}