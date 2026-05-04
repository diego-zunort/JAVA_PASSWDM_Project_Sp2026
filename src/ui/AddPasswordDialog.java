package ui;

import model.StandardUser;
import model.PasswordEntry;

import javax.swing.*;
import java.awt.*;

public class AddPasswordDialog extends JDialog {

    private StandardUser currentUser;
    private MainFrame parentFrame;

    private JTextField siteField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton addButton;

    public AddPasswordDialog(MainFrame parent, StandardUser user) {
        super(parent, "Add Password", true);
        this.parentFrame = parent;
        this.currentUser = user;
        setSize(300, 180);
        setLocationRelativeTo(parent);
        buildUI();
    }

    private void buildUI() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));

        siteField = new JTextField();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        addButton = new JButton("Add");

        panel.add(new JLabel("Site"));
        panel.add(siteField);
        panel.add(new JLabel("Username"));
        panel.add(usernameField);
        panel.add(new JLabel("Password"));
        panel.add(passwordField);
        panel.add(new JLabel(""));
        panel.add(addButton);

        add(panel, BorderLayout.CENTER);

        addButton.addActionListener(e -> handleAdd());
    }

    private void handleAdd() {
        String site = siteField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        // TODO: encrypt password, add entry to vault and DB, refresh parent table
        dispose();
    }
}
