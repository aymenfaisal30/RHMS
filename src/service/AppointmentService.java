package service;

import exception.AppointmentConflictException;
import model.Appointment;
import storage.DataStore;
import storage.FileManager;

import java.util.ArrayList;
import java.util.List;

public class AppointmentService {

  private static final String FILE = "data/appointments.ser";
    private DataStore<Appointment> store;

    public AppointmentService() {
        this.store = FileManager.load(FILE);
    }

    // Book — throws if doctor already has same date+time
    public void book(Appointment appointment) throws AppointmentConflictException {
        for (Appointment existing : store.getAll()) {
            if (existing.getDoctorId().equals(appointment.getDoctorId())
                    && existing.getDate().equals(appointment.getDate())
                    && existing.getTime().equals(appointment.getTime())
                    && existing.getStatus().equals("SCHEDULED")) {

                throw new AppointmentConflictException(
                    "Doctor " + appointment.getDoctorId()
                    + " is already booked on " + appointment.getDate()
                    + " at " + appointment.getTime()
                );
            }
        }
        store.add(appointment);
        FileManager.save(store, FILE);
        System.out.println("[AppointmentService] Booked: " + appointment.getAppointmentId());
    }

    // Cancel by appointment ID
    public boolean cancel(String appointmentId) {
        for (Appointment a : store.getAll()) {
            if (a.getAppointmentId().equals(appointmentId)) {
                a.setStatus("CANCELLED");
                FileManager.save(store, FILE);
                System.out.println("[AppointmentService] Cancelled: " + appointmentId);
                return true;
            }
        }
        System.out.println("[AppointmentService] Appointment not found: " + appointmentId);
        return false;
    }

    // Get all appointments for a patient
    public List<Appointment> getByPatientId(String patientId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : store.getAll()) {
            if (a.getPatientId().equals(patientId)) {
                result.add(a);
            }
        }
        return result;
    }

    // Get all appointments for a doctor
    public List<Appointment> getByDoctorId(String doctorId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : store.getAll()) {
            if (a.getDoctorId().equals(doctorId)) {
                result.add(a);
            }
        }
        return result;
    }

    public List<Appointment> getAllAppointments() {
        return store.getAll();
    }
}