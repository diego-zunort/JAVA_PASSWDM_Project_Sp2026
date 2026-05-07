package model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        return hash(masterPassword).equals(hash(input));
    }

    public abstract boolean hasPermission();

    private String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }
}
