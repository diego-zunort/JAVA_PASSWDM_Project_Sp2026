package ui;

import database.DatabaseManager;
import model.AdminUser;
import model.StandardUser;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signUpButton;

    private final DatabaseManager db;

    public LoginFrame() {
        this.db = new DatabaseManager();
        setTitle("Log In");
        setSize(300, 175);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Log In");
        signUpButton = new JButton("Sign Up");

        panel.add(new JLabel("Username"));
        panel.add(usernameField);
        panel.add(new JLabel("Password"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(signUpButton);

        add(panel, BorderLayout.CENTER);

        loginButton.addActionListener(e -> handleLogin());
        signUpButton.addActionListener(e -> openSignUp());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!db.validateLogin(username, password)) {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String role = db.getUserRole(username);
        StandardUser user = role.equals("admin")
            ? new AdminUser(username, password)
            : new StandardUser(username, password);
        new MainFrame(user, db).setVisible(true);
        dispose();
    }

    private void openSignUp() {
        new SignUpFrame(db).setVisible(true);
    }
}
