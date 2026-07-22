package storage;

import java.io.*;

public class FileManager {

    // Save any DataStore object to a .ser file
    public static void save(DataStore<?> store, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            oos.writeObject(store);
            System.out.println("[FileManager] Saved to " + filename);
        } catch (IOException e) {
            System.out.println("[FileManager] Save error: " + e.getMessage());
        }
    }

    // Load a DataStore object from a .ser file
    @SuppressWarnings("unchecked")
    public static <T> DataStore<T> load(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("[FileManager] No file found: " + filename + " — starting fresh.");
            return new DataStore<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filename))) {
            return (DataStore<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[FileManager] Load error: " + e.getMessage());
            return new DataStore<>();
        }
    }
}