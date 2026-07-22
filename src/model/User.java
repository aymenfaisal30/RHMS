package model;

import java.io.Serializable;

public abstract class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;
    private String name;
    private String email;
    private String password;
    private String role;  

    public User(String userId, String name, String email, String password, String role) {
        this.userId   = userId;
        this.name     = name;
        this.email    = email;
        this.password = password;
        this.role     = role;
    }

    // Abstract methods — every subclass must implement these
    public abstract void displayDashboard();
    public abstract String getRole();

    // Getters
    public String getUserId()   { return userId; }
    public String getName()     { return name; }
    public String getEmail()    { return email; }
    public String getPassword() { return password; }

    // Setters
    public void setName(String name)         { this.name = name; }
    public void setEmail(String email)       { this.email = email; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "ID: " + userId + " | Name: " + name + " | Email: " + email + " | Role: " + role;
    }
}