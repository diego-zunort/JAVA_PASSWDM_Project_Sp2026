package model;

import database.DatabaseManager;

public class AdminUser extends StandardUser {

    public AdminUser(String username, String masterPassword) {
        super(username, masterPassword);
    }

    @Override
    public boolean hasPermission() {
        return true;
    }

    public boolean resetUserPassword(String targetUsername, String newPassword, DatabaseManager db) {
        if (!db.userExists(targetUsername)) return false;
        return db.updateUserPassword(targetUsername, newPassword);
    }
}
