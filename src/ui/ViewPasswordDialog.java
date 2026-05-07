package ui;

import database.DatabaseManager;
import model.PasswordEntry;
import model.StandardUser;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.List;

public class ViewPasswordDialog extends JDialog {

    private final StandardUser currentUser;
    private final MainFrame parentFrame;
    private final DatabaseManager db;

    private JComboBox<String> accountDropdown;
    private JPasswordField securityPinField;
    private JButton viewButton;

    private List<PasswordEntry> entries;

    public ViewPasswordDialog(MainFrame parent, StandardUser user, DatabaseManager db) {
        super(parent, "View Password", true);
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
        securityPinField = new JPasswordField();
        viewButton = new JButton("View");

        entries = currentUser.getVault().getEntries();
        for (PasswordEntry e : entries) {
            accountDropdown.addItem(e.getSiteName() + " - " + e.getUsername());
        }

        panel.add(new JLabel("Select Account"));
        panel.add(accountDropdown);
        panel.add(new JLabel("Security Pin"));
        panel.add(securityPinField);
        panel.add(new JLabel(""));
        panel.add(viewButton);

        add(panel, BorderLayout.CENTER);
        viewButton.addActionListener(e -> handleView());
    }

    private void handleView() {
        int index = accountDropdown.getSelectedIndex();
        if (index < 0 || entries.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No account selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String pin = new String(securityPinField.getPassword());
        if (pin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Security PIN is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!db.validateSecurityPin(currentUser.getUsername(), pin)) {
            JOptionPane.showMessageDialog(this, "Incorrect security PIN.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PasswordEntry entry = entries.get(index);
        String decrypted = entry.getPassword();

        JTextField passwordDisplay = new JTextField(decrypted);
        passwordDisplay.setEditable(false);

        JButton copyButton = new JButton("Copy");
        copyButton.addActionListener(e -> {
            Toolkit.getDefaultToolkit()
                   .getSystemClipboard()
                   .setContents(new StringSelection(decrypted), null);
            JOptionPane.showMessageDialog(this, "Password copied to clipboard.");
        });

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel("Password for " + entry.getSiteName() + ":"), BorderLayout.NORTH);
        panel.add(passwordDisplay, BorderLayout.CENTER);
        panel.add(copyButton, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(this, panel, "Password", JOptionPane.PLAIN_MESSAGE);
    }
}
