package ui;

import database.DatabaseManager;
import model.PasswordEntry;
import model.StandardUser;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RemovePasswordDialog extends JDialog {

    private final StandardUser currentUser;
    private final MainFrame parentFrame;
    private final DatabaseManager db;

    private JComboBox<String> accountDropdown;
    private JPasswordField masterPasswordField;
    private JButton removeButton;

    private List<PasswordEntry> entries;

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
        masterPasswordField = new JPasswordField();
        removeButton = new JButton("Remove");

        entries = currentUser.getVault().getEntries();
        for (PasswordEntry e : entries) {
            accountDropdown.addItem(e.getSiteName() + " - " + e.getUsername());
        }

        panel.add(new JLabel("Select Account"));
        panel.add(accountDropdown);
        panel.add(new JLabel("Master Password"));
        panel.add(masterPasswordField);
        panel.add(new JLabel(""));
        panel.add(removeButton);

        add(panel, BorderLayout.CENTER);
        removeButton.addActionListener(e -> handleRemove());
    }

    private void handleRemove() {
        int index = accountDropdown.getSelectedIndex();
        if (index < 0 || entries.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No account selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String masterPassword = new String(masterPasswordField.getPassword());
        if (masterPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Master password is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!currentUser.verifyPassword(masterPassword)) {
            JOptionPane.showMessageDialog(this, "Incorrect master password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PasswordEntry entry = entries.get(index);

        if (!db.removeEntry(entry.getId())) {
            JOptionPane.showMessageDialog(this, "Failed to remove entry.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        entries.remove(index);
        parentFrame.refreshTable();
        dispose();
    }
}
