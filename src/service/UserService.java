package service;

import exception.UserNotFoundException;
import model.User;
import storage.DataStore;
import storage.FileManager;

import java.util.List;

public class UserService {

private static final String FILE = "data/users.ser";
    private DataStore<User> store;

    public UserService() {
        this.store = FileManager.load(FILE);
    }

    // Register — checks for duplicate email
    public boolean register(User user) {
        for (User u : store.getAll()) {
            if (u.getEmail().equalsIgnoreCase(user.getEmail())) {
                System.out.println("[UserService] Email already registered: " + user.getEmail());
                return false;
            }
        }
        store.add(user);
        FileManager.save(store, FILE);
        System.out.println("[UserService] Registered: " + user.getName());
        return true;
    }

    // Login — returns User if credentials match
    public User login(String email, String password) throws UserNotFoundException {
        for (User u : store.getAll()) {
            if (u.getEmail().equalsIgnoreCase(email)
                    && u.getPassword().equals(password)) {
                System.out.println("[UserService] Login success: " + u.getName());
                return u;
            }
        }
        throw new UserNotFoundException("No user found with email: " + email);
    }

    // Find by ID
    public User findById(String userId) throws UserNotFoundException {
        for (User u : store.getAll()) {
            if (u.getUserId().equals(userId)) {
                return u;
            }
        }
        throw new UserNotFoundException("No user found with ID: " + userId);
    }

    // Remove by ID
    public boolean removeById(String userId) {
        List<User> all = store.getAll();
        for (User u : all) {
            if (u.getUserId().equals(userId)) {
                store.remove(u);
                FileManager.save(store, FILE);
                System.out.println("[UserService] Removed user: " + u.getName());
                return true;
            }
        }
        System.out.println("[UserService] User not found for removal: " + userId);
        return false;
    }

    public List<User> getAllUsers() {
        return store.getAll();
    }
}