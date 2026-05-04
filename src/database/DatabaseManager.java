package database;

import java.sql.*;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:passwdm.db";
    private Connection connection;

    public DatabaseManager() {
        // TODO: call connect() and createTables() on startup
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
    }

    public void createTables() throws SQLException {
        // TODO: CREATE TABLE IF NOT EXISTS for users and password_entries
    }

    public boolean addUser(String username, String masterPassword, String securityPin) {
        // TODO: INSERT user into users table
        return false;
    }

    public boolean userExists(String username) {
        // TODO: SELECT from users where username = ?
        return false;
    }

    public boolean validateLogin(String username, String masterPassword) {
        // TODO: SELECT and compare hashed password
        return false;
    }

    public boolean addEntry(String username, String siteName, String entryUsername, String encryptedPassword, String category) {
        // TODO: INSERT into password_entries
        return false;
    }

    public boolean removeEntry(int id) {
        // TODO: DELETE from password_entries where id = ?
        return false;
    }

    public boolean updateEntryPassword(int id, String encryptedPassword) {
        // TODO: UPDATE password_entries set password = ? where id = ?
        return false;
    }

    public ResultSet getEntriesForUser(String username) throws SQLException {
        // TODO: SELECT * from password_entries where username = ?
        return null;
    }

    public ResultSet searchEntries(String username, String keyword) throws SQLException {
        // TODO: SELECT * from password_entries where username = ? AND siteName LIKE ?
        return null;
    }

    public void close() throws SQLException {
        if (connection != null) connection.close();
    }
}
