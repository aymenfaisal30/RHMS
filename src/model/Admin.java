package model;

public class Admin extends User {

    private static final long serialVersionUID = 1L;

    private String adminLevel;   // "SUPER", "NORMAL"

    public Admin(String userId, String name, String email,
                 String password, String adminLevel) {
        super(userId, name, email, password, "ADMIN");
        this.adminLevel = adminLevel;
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }

    @Override
    public void displayDashboard() {
        System.out.println("===== ADMIN PANEL =====");
        System.out.println("Welcome, " + getName());
        System.out.println("Admin Level : " + adminLevel);
        System.out.println("Options: [1] View All Users  [2] Remove User  [3] Logout");
    }

    // Getter
    public String getAdminLevel() { return adminLevel; }

    // Setter
    public void setAdminLevel(String adminLevel) { this.adminLevel = adminLevel; }
}