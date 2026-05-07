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
        // TODO: persist to DB

        entries.add(entry);
    }

    public void removeEntry(int id) {
        // TODO: remove by id from list and DB

    }

    public ArrayList<PasswordEntry> searchEntries(String keyword) {
        // TODO: filter entries where siteName or username contains keyword
        
        return new ArrayList<>();
    }

    public ArrayList<PasswordEntry> getEntries() {
        return entries;
    }

    public void lock() {
        this.locked = true;
    }

    public void unlock(String password) {
        // TODO: verify master password then set locked = false
    }

    public boolean isLocked() {
        return locked;
    }
}
