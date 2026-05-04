package model;

public class AdminUser extends User {

    public AdminUser(String username, String masterPassword) {
        super(username, masterPassword);
    }

    @Override
    public boolean hasPermission() {
        return true;
    }

    public void resetUserPassword(String targetUsername, String newPassword) {
        // TODO: update target user's password in DB
    }
}
