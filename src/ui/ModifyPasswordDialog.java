package ui;

import database.DatabaseManager;
import model.PasswordEntry;
import model.StandardUser;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ModifyPasswordDialog extends JDialog {

    private final StandardUser currentUser;
    private final MainFrame parentFrame;
    private final DatabaseManager db;

    private JComboBox<String> accountDropdown;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JButton modifyButton;

    private List<PasswordEntry> entries;

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

        entries = currentUser.getVault().getEntries();
        for (PasswordEntry e : entries) {
            accountDropdown.addItem(e.getSiteName() + " - " + e.getUsername());
        }

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
        int index = accountDropdown.getSelectedIndex();
        if (index < 0 || entries.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No account selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String currentPassword = new String(currentPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());

        if (currentPassword.isEmpty() || newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both password fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PasswordEntry entry = entries.get(index);

        if (!entry.getPassword().equals(currentPassword)) {
            JOptionPane.showMessageDialog(this, "Current password is incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        entry.setPassword(newPassword);

        if (!db.updateEntryPassword(entry.getId(), entry.getEncryptedPassword())) {
            JOptionPane.showMessageDialog(this, "Failed to update password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        parentFrame.refreshTable();
        dispose();
    }
}
