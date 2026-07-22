package service;

import model.MedicalRecord;
import storage.DataStore;
import storage.FileManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RecordService {
private static final String FILE = "data/records.ser";
    private DataStore<MedicalRecord> store;

    public RecordService() {
        this.store = FileManager.load(FILE);
    }

    // Add a new record
    public void addRecord(MedicalRecord record) {
        store.add(record);
        FileManager.save(store, FILE);
        System.out.println("[RecordService] Record added: " + record.getRecordId());
    }

    // Get all records for a patient, sorted by date (newest first)
    public List<MedicalRecord> getByPatientId(String patientId) {
        List<MedicalRecord> result = new ArrayList<>();
        for (MedicalRecord r : store.getAll()) {
            if (r.getPatientId().equals(patientId)) {
                result.add(r);
            }
        }
        result.sort(Comparator.comparing(MedicalRecord::getDate).reversed());
        return result;
    }

    public List<MedicalRecord> getAllRecords() {
        return store.getAll();
    }
}
