package ui;

import database.DatabaseManager;
import model.PasswordEntry;
import model.StandardUser;

import javax.swing.*;
import java.awt.*;

public class AddPasswordDialog extends JDialog {

    private final StandardUser currentUser;
    private final MainFrame parentFrame;
    private final DatabaseManager db;

    private JTextField siteField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField categoryField;
    private JButton addButton;

    public AddPasswordDialog(MainFrame parent, StandardUser user, DatabaseManager db) {
        super(parent, "Add Password", true);
        this.parentFrame = parent;
        this.currentUser = user;
        this.db = db;
        setSize(300, 210);
        setLocationRelativeTo(parent);
        buildUI();
    }

    private void buildUI() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));

        siteField = new JTextField();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        categoryField = new JTextField();
        addButton = new JButton("Add");

        panel.add(new JLabel("Site"));
        panel.add(siteField);
        panel.add(new JLabel("Username"));
        panel.add(usernameField);
        panel.add(new JLabel("Password"));
        panel.add(passwordField);
        panel.add(new JLabel("Category"));
        panel.add(categoryField);
        panel.add(new JLabel(""));
        panel.add(addButton);

        add(panel, BorderLayout.CENTER);
        addButton.addActionListener(e -> handleAdd());
    }

    private void handleAdd() {
        String site = siteField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String category = categoryField.getText().trim();

        if (site.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Site, username, and password are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PasswordEntry entry = new PasswordEntry(site, username, password, category);

        boolean saved = db.addEntry(
            currentUser.getUsername(),
            entry.getSiteName(),
            entry.getUsername(),
            entry.getEncryptedPassword(),
            entry.getEncodedKey(),
            entry.getCategory()
        );

        if (!saved) {
            JOptionPane.showMessageDialog(this, "Failed to save entry.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        currentUser.getVault().addEntry(entry);
        parentFrame.refreshTable();
        dispose();
    }
}
