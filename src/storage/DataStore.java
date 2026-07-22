package storage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataStore<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<T> items;

    public DataStore() {
        this.items = new ArrayList<>();
    }

    public void add(T item) {
        items.add(item);
    }

    public boolean remove(T item) {
        return items.remove(item);
    }

    public List<T> getAll() {
        return new ArrayList<>(items);   // return a copy, not the real list
    }

    public int size() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }
}