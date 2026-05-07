package ui;

import database.DatabaseManager;
import model.AdminUser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminPanelDialog extends JDialog {

    private final AdminUser adminUser;
    private final DatabaseManager db;

    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton resetPasswordButton;
    private JButton deleteUserButton;
    private JButton refreshButton;

    public AdminPanelDialog(MainFrame parent, AdminUser admin, DatabaseManager db) {
        super(parent, "Admin Panel", true);
        this.adminUser = admin;
        this.db = db;
        setSize(450, 350);
        setLocationRelativeTo(parent);
        buildUI();
        loadUsers();
    }

    private void buildUI() {
        String[] columns = {"Username", "Role"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        resetPasswordButton = new JButton("Reset Password");
        deleteUserButton = new JButton("Delete User");
        refreshButton = new JButton("Refresh");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(resetPasswordButton);
        buttonPanel.add(deleteUserButton);
        buttonPanel.add(refreshButton);

        add(new JScrollPane(userTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        resetPasswordButton.addActionListener(e -> handleResetPassword());
        deleteUserButton.addActionListener(e -> handleDeleteUser());
        refreshButton.addActionListener(e -> loadUsers());
    }

    private void loadUsers() {
        tableModel.setRowCount(0);
        List<String[]> users = db.getAllUsers();
        for (String[] u : users) {
            tableModel.addRow(new Object[]{ u[0], u[1] });
        }
    }

    private void handleResetPassword() {
        int row = userTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a user first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String targetUsername = (String) tableModel.getValueAt(row, 0);

        if (targetUsername.equals(adminUser.getUsername())) {
            JOptionPane.showMessageDialog(this, "Use the main app to change your own password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPasswordField newPassField = new JPasswordField();
        JPasswordField confirmField = new JPasswordField();
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("New Password:"));
        panel.add(newPassField);
        panel.add(new JLabel("Confirm:"));
        panel.add(confirmField);

        int result = JOptionPane.showConfirmDialog(this, panel,
            "Reset password for: " + targetUsername, JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION) return;

        String newPassword = new String(newPassField.getPassword());
        String confirm = new String(confirmField.getPassword());

        if (newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newPassword.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (adminUser.resetUserPassword(targetUsername, newPassword, db)) {
            JOptionPane.showMessageDialog(this, "Password reset for " + targetUsername + ".", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to reset password.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteUser() {
        int row = userTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a user first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String targetUsername = (String) tableModel.getValueAt(row, 0);

        if (targetUsername.equals(adminUser.getUsername())) {
            JOptionPane.showMessageDialog(this, "You cannot delete your own account.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete user \"" + targetUsername + "\" and all their entries?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) return;

        if (db.deleteUser(targetUsername)) {
            JOptionPane.showMessageDialog(this, "User " + targetUsername + " deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadUsers();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete user.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
