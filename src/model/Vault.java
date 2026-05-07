package model;

import java.util.ArrayList;

public class Vault {

    private ArrayList<PasswordEntry> entries;
    private boolean locked;

    public Vault() {
        this.entries = new ArrayList<>();
        this.locked = false;
    }

    public void addEntry(PasswordEntry entry) {
        entries.add(entry);
    }

    public void removeEntry(int id) {
        entries.removeIf(e -> e.getId() == id);
    }

    public ArrayList<PasswordEntry> searchEntries(String keyword) {
        ArrayList<PasswordEntry> results = new ArrayList<>();
        String lower = keyword.toLowerCase();
        for (PasswordEntry e : entries) {
            if (e.getSiteName().toLowerCase().contains(lower) ||
                e.getUsername().toLowerCase().contains(lower)) {
                results.add(e);
            }
        }
        return results;
    }

    public ArrayList<PasswordEntry> getEntries() {
        return entries;
    }

    public void lock() {
        this.locked = true;
    }

    public void unlock(String password) {
        this.locked = false;
    }

    public boolean isLocked() {
        return locked;
    }
}
