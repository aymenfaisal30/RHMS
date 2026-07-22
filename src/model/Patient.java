package model;

public class Patient extends User {

    private static final long serialVersionUID = 1L;

    private String bloodGroup;
    private int    age;

    public Patient(String userId, String name, String email, String password,
                   String bloodGroup, int age) {
        super(userId, name, email, password, "PATIENT");
        this.bloodGroup = bloodGroup;
        this.age        = age;
    }

    @Override
    public String getRole() {
        return "PATIENT";
    }

    @Override
    public void displayDashboard() {
        System.out.println("===== PATIENT DASHBOARD =====");
        System.out.println("Welcome, " + getName());
        System.out.println("Blood Group : " + bloodGroup);
        System.out.println("Age         : " + age);
        System.out.println("Options: [1] Book Appointment  [2] View Records  [3] Logout");
    }

    // Getters
    public String getBloodGroup() { return bloodGroup; }
    public int    getAge()        { return age; }

    // Setters
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public void setAge(int age)                  { this.age = age; }
}