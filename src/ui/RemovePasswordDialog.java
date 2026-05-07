package ui;

import database.DatabaseManager;
import model.StandardUser;

import javax.swing.*;
import java.awt.*;

public class RemovePasswordDialog extends JDialog {

    private StandardUser currentUser;
    private MainFrame parentFrame;
    private final DatabaseManager db;

    private JComboBox<String> accountDropdown;
    private JPasswordField currentPasswordField;
    private JButton removeButton;

    public RemovePasswordDialog(MainFrame parent, StandardUser user, DatabaseManager db) {
        super(parent, "Remove Password", true);
        this.parentFrame = parent;
        this.currentUser = user;
        this.db = db;
        setSize(300, 150);
        setLocationRelativeTo(parent);
        buildUI();
    }

    private void buildUI() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        accountDropdown = new JComboBox<>();
        currentPasswordField = new JPasswordField();
        removeButton = new JButton("Remove");

        // TODO: populate accountDropdown with site names from user's vault

        panel.add(new JLabel("Select Account"));
        panel.add(accountDropdown);
        panel.add(new JLabel("Current Password"));
        panel.add(currentPasswordField);
        panel.add(new JLabel(""));
        panel.add(removeButton);

        add(panel, BorderLayout.CENTER);

        removeButton.addActionListener(e -> handleRemove());
    }

    private void handleRemove() {
        String selectedAccount = (String) accountDropdown.getSelectedItem();
        String currentPassword = new String(currentPasswordField.getPassword());
        // TODO: verify currentPassword, remove entry from vault and DB, refresh parent table
        dispose();
    }
}
