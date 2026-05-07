package database;

import model.PasswordEntry;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:passwdm.db";
    private Connection connection;

    public DatabaseManager() {
        try {
            connect();
            createTables();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
    }

    public void createTables() throws SQLException {
        String createUsers =
            "CREATE TABLE IF NOT EXISTS users (" +
            "  id                   INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  username             TEXT UNIQUE NOT NULL," +
            "  master_password_hash TEXT NOT NULL," +
            "  security_pin_hash    TEXT NOT NULL," +
            "  role                 TEXT NOT NULL DEFAULT 'standard'" +
            ")";
        String createEntries =
            "CREATE TABLE IF NOT EXISTS password_entries (" +
            "  id                 INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  username           TEXT NOT NULL," +
            "  site_name          TEXT NOT NULL," +
            "  entry_username     TEXT NOT NULL," +
            "  encrypted_password TEXT NOT NULL," +
            "  encoded_key        TEXT NOT NULL," +
            "  category           TEXT," +
            "  FOREIGN KEY(username) REFERENCES users(username)" +
            ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createUsers);
            stmt.execute(createEntries);
        }
        seedAdmin();
    }

    private void seedAdmin() throws SQLException {
        if (userExists("admin")) return;
        String sql = "INSERT INTO users (username, master_password_hash, security_pin_hash, role) VALUES (?, ?, ?, 'admin')";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "admin");
            pstmt.setString(2, hash("Admin@1234"));
            pstmt.setString(3, hash("0000"));
            pstmt.executeUpdate();
        }
    }

    public boolean addUser(String username, String masterPassword, String securityPin) {
        String sql = "INSERT INTO users (username, master_password_hash, security_pin_hash, role) VALUES (?, ?, ?, 'standard')";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hash(masterPassword));
            pstmt.setString(3, hash(securityPin));
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public String getUserRole(String username) {
        String sql = "SELECT role FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getString("role");
        } catch (SQLException e) {
            // fall through
        }
        return "standard";
    }

    public boolean userExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean validateLogin(String username, String masterPassword) {
        String sql = "SELECT master_password_hash FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("master_password_hash").equals(hash(masterPassword));
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean validateSecurityPin(String username, String pin) {
        String sql = "SELECT security_pin_hash FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("security_pin_hash").equals(hash(pin));
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<String[]> getAllUsers() {
        String sql = "SELECT username, role FROM users ORDER BY username";
        List<String[]> users = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                users.add(new String[]{ rs.getString("username"), rs.getString("role") });
            }
        } catch (SQLException e) {
            // return empty list
        }
        return users;
    }

    public boolean deleteUser(String username) {
        String deleteEntries = "DELETE FROM password_entries WHERE username = ?";
        String deleteUser    = "DELETE FROM users WHERE username = ?";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement p1 = connection.prepareStatement(deleteEntries);
                 PreparedStatement p2 = connection.prepareStatement(deleteUser)) {
                p1.setString(1, username);
                p1.executeUpdate();
                p2.setString(1, username);
                p2.executeUpdate();
            }
            connection.commit();
            return true;
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ignored) {}
            return false;
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }

    public boolean updateUserPassword(String username, String newPassword) {
        String sql = "UPDATE users SET master_password_hash = ? WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, hash(newPassword));
            pstmt.setString(2, username);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean addEntry(String username, String siteName, String entryUsername, String encryptedPassword, String encodedKey, String category) {
        String sql = "INSERT INTO password_entries (username, site_name, entry_username, encrypted_password, encoded_key, category) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, siteName);
            pstmt.setString(3, entryUsername);
            pstmt.setString(4, encryptedPassword);
            pstmt.setString(5, encodedKey);
            pstmt.setString(6, category);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean removeEntry(int id) {
        String sql = "DELETE FROM password_entries WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean updateEntryPassword(int id, String encryptedPassword) {
        String sql = "UPDATE password_entries SET encrypted_password = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, encryptedPassword);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<PasswordEntry> getEntriesForUser(String username) throws SQLException {
        String sql = "SELECT id, site_name, entry_username, encrypted_password, encoded_key, category " +
                     "FROM password_entries WHERE username = ?";
        List<PasswordEntry> entries = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                PasswordEntry entry = PasswordEntry.fromDatabase(
                    rs.getString("site_name"),
                    rs.getString("entry_username"),
                    rs.getString("encrypted_password"),
                    rs.getString("encoded_key"),
                    rs.getString("category")
                );
                entry.setId(rs.getInt("id"));
                entries.add(entry);
            }
        }
        return entries;
    }

    public List<PasswordEntry> searchEntries(String username, String keyword) throws SQLException {
        String sql = "SELECT id, site_name, entry_username, encrypted_password, encoded_key, category " +
                     "FROM password_entries WHERE username = ? AND site_name LIKE ?";
        List<PasswordEntry> entries = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                PasswordEntry entry = PasswordEntry.fromDatabase(
                    rs.getString("site_name"),
                    rs.getString("entry_username"),
                    rs.getString("encrypted_password"),
                    rs.getString("encoded_key"),
                    rs.getString("category")
                );
                entry.setId(rs.getInt("id"));
                entries.add(entry);
            }
        }
        return entries;
    }

    public void close() throws SQLException {
        if (connection != null) connection.close();
    }

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
