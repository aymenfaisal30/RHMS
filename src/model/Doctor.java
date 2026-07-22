package model;

public class Doctor extends User {

    private static final long serialVersionUID = 1L;

    private String specialization;
    private String availableDays;   // e.g. "Mon, Wed, Fri"

    public Doctor(String userId, String name, String email, String password,
                  String specialization, String availableDays) {
        super(userId, name, email, password, "DOCTOR");
        this.specialization = specialization;
        this.availableDays  = availableDays;
    }

    @Override
    public String getRole() {
        return "DOCTOR";
    }

    @Override
    public void displayDashboard() {
        System.out.println("===== DOCTOR DASHBOARD =====");
        System.out.println("Welcome, Dr. " + getName());
        System.out.println("Specialization : " + specialization);
        System.out.println("Available Days : " + availableDays);
        System.out.println("Options: [1] View Appointments  [2] View Patient Records  [3] Logout");
    }

    // Getters
    public String getSpecialization() { return specialization; }
    public String getAvailableDays()  { return availableDays; }

    // Setters
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public void setAvailableDays(String availableDays)   { this.availableDays = availableDays; }
}