package model;

public abstract class User {

    protected String username;
    protected String masterPassword;

    public User(String username, String masterPassword) {
        this.username = username;
        this.masterPassword = masterPassword;
    }

    public String getUsername() {
        return username;
    }

    public boolean verifyPassword(String input) {
        // TODO: hash comparison
        return masterPassword.equals(input);
    }

    public abstract boolean hasPermission();
}
