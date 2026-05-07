package ui;

import database.DatabaseManager;
import model.StandardUser;

import javax.swing.*;
import java.awt.*;

public class ModifyPasswordDialog extends JDialog {

    private StandardUser currentUser;
    private MainFrame parentFrame;
    private final DatabaseManager db;

    private JComboBox<String> accountDropdown;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JButton modifyButton;

    public ModifyPasswordDialog(MainFrame parent, StandardUser user, DatabaseManager db) {
        super(parent, "Modify Password", true);
        this.parentFrame = parent;
        this.currentUser = user;
        this.db = db;
        setSize(300, 180);
        setLocationRelativeTo(parent);
        buildUI();
    }

    private void buildUI() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));

        accountDropdown = new JComboBox<>();
        currentPasswordField = new JPasswordField();
        newPasswordField = new JPasswordField();
        modifyButton = new JButton("Modify");

        // TODO: populate accountDropdown with site names from user's vault

        panel.add(new JLabel("Select Account"));
        panel.add(accountDropdown);
        panel.add(new JLabel("Current Password"));
        panel.add(currentPasswordField);
        panel.add(new JLabel("New Password"));
        panel.add(newPasswordField);
        panel.add(new JLabel(""));
        panel.add(modifyButton);

        add(panel, BorderLayout.CENTER);

        modifyButton.addActionListener(e -> handleModify());
    }

    private void handleModify() {
        String selectedAccount = (String) accountDropdown.getSelectedItem();
        String currentPassword = new String(currentPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        // TODO: verify currentPassword, encrypt newPassword, update in DB, refresh parent table
        dispose();
    }
}
