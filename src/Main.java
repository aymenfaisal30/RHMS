import exception.AppointmentConflictException;
import exception.UserNotFoundException;
import model.*;
import service.*;

public class Main {

    public static void main(String[] args) {

        System.out.println("========== RHMS Terminal Test ==========\n");

        // --- Services ---
        UserService        userService        = new UserService();
        AppointmentService appointmentService = new AppointmentService();
        RecordService      recordService      = new RecordService();

        // --- Register users ---
        Patient p1 = new Patient("P001", "Sara Khan",   "sara@email.com",   "pass123", "B+", 28);
        Doctor  d1 = new Doctor ("D001", "Dr. Ahmed",   "ahmed@email.com",  "doc123",  "Cardiology", "Mon, Wed, Fri");
        Admin   a1 = new Admin  ("A001", "Admin Raza",  "raza@email.com",   "admin123", "SUPER");

        userService.register(p1);
        userService.register(d1);
        userService.register(a1);

        System.out.println();

        // --- Login test ---
        try {
            User loggedIn = userService.login("sara@email.com", "pass123");
            loggedIn.displayDashboard();
        } catch (UserNotFoundException e) {
            System.out.println("Login failed: " + e.getMessage());
        }

        System.out.println();

        // --- Book appointment ---
        Appointment apt1 = new Appointment("APT001", "P001", "D001", "2025-06-10", "10:00");
        try {
            appointmentService.book(apt1);
        } catch (AppointmentConflictException e) {
            System.out.println("Conflict: " + e.getMessage());
        }

        // --- Conflict test: same doctor, same slot ---
        Appointment apt2 = new Appointment("APT002", "P002", "D001", "2025-06-10", "10:00");
        try {
            appointmentService.book(apt2);
        } catch (AppointmentConflictException e) {
            System.out.println("\n[CONFLICT CAUGHT] " + e.getMessage());
        }

        System.out.println();

        // --- Add medical record ---
        MedicalRecord rec1 = new MedicalRecord(
            "REC001", "P001", "D001", "2025-06-10", "Hypertension", "Prescribed BP medication"
        );
        recordService.addRecord(rec1);

        // --- Retrieve records ---
        System.out.println("\n--- Records for P001 ---");
        for (MedicalRecord r : recordService.getByPatientId("P001")) {
            System.out.println(r);
        }

        // --- View all users (admin feature) ---
        System.out.println("\n--- All Registered Users ---");
        for (User u : userService.getAllUsers()) {
            System.out.println(u);
        }

        System.out.println("\n========== Test Complete ==========");
    }
}
