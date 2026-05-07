package ui;

import database.DatabaseManager;

import javax.swing.*;
import java.awt.*;

public class SignUpFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField retypePasswordField;
    private JPasswordField securityPinField;
    private JButton createAccountButton;

    private final DatabaseManager db;

    public SignUpFrame(DatabaseManager db) {
        this.db = db;
        setTitle("Sign Up");
        setSize(320, 220);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        retypePasswordField = new JPasswordField();
        securityPinField = new JPasswordField();
        createAccountButton = new JButton("Create Account");

        panel.add(new JLabel("Set User"));
        panel.add(usernameField);
        panel.add(new JLabel("Set Password"));
        panel.add(passwordField);
        panel.add(new JLabel("Retype Password"));
        panel.add(retypePasswordField);
        panel.add(new JLabel("Set Security Pin"));
        panel.add(securityPinField);
        panel.add(new JLabel(""));
        panel.add(createAccountButton);

        add(panel, BorderLayout.CENTER);

        createAccountButton.addActionListener(e -> handleCreateAccount());
    }

    private void handleCreateAccount() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String retypePassword = new String(retypePasswordField.getPassword());
        String securityPin = new String(securityPinField.getPassword());

        if (username.isEmpty() || password.isEmpty() || securityPin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(retypePassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (db.userExists(username)) {
            JOptionPane.showMessageDialog(this, "Username already taken.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!db.addUser(username, password, securityPin)) {
            JOptionPane.showMessageDialog(this, "Failed to create account. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Account created! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
