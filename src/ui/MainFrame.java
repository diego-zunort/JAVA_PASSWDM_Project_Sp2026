package ui;

import database.DatabaseManager;
import model.AdminUser;
import model.PasswordEntry;
import model.StandardUser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class MainFrame extends JFrame {

    private final StandardUser currentUser;
    private final DatabaseManager db;
    private JTable vaultTable;
    private DefaultTableModel tableModel;

    private JButton generateButton;
    private JButton addButton;
    private JButton modifyButton;
    private JButton removeButton;
    private JButton viewButton;
    private JButton logoutButton;

    public MainFrame(StandardUser user, DatabaseManager db) {
        this.currentUser = user;
        this.db = db;
        setTitle("Password Manager");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        buildUI();
        refreshTable();
    }

    private void buildUI() {
        String[] columns = {"Site", "Username", "Category"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        vaultTable = new JTable(tableModel);

        JLabel userLabel = new JLabel("Logged in as: " + currentUser.getUsername(), SwingConstants.LEFT);
        logoutButton = new JButton("Log Out");
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(userLabel, BorderLayout.WEST);
        topPanel.add(logoutButton, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel();
        generateButton = new JButton("Generate");
        addButton = new JButton("Add");
        modifyButton = new JButton("Modify");
        removeButton = new JButton("Remove");
        viewButton = new JButton("View");

        buttonPanel.add(generateButton);
        buttonPanel.add(addButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(viewButton);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(vaultTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        generateButton.addActionListener(e -> new GeneratePasswordDialog(this).setVisible(true));
        addButton.addActionListener(e -> new AddPasswordDialog(this, currentUser, db).setVisible(true));
        modifyButton.addActionListener(e -> new ModifyPasswordDialog(this, currentUser, db).setVisible(true));
        removeButton.addActionListener(e -> new RemovePasswordDialog(this, currentUser, db).setVisible(true));
        viewButton.addActionListener(e -> new ViewPasswordDialog(this, currentUser, db).setVisible(true));
        logoutButton.addActionListener(e -> handleLogout());

        if (currentUser instanceof AdminUser) {
            JButton adminButton = new JButton("Admin Panel");
            buttonPanel.add(adminButton);
            adminButton.addActionListener(e ->
                new AdminPanelDialog(this, (AdminUser) currentUser, db).setVisible(true));
        }
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        try {
            List<PasswordEntry> entries = db.getEntriesForUser(currentUser.getUsername());
            currentUser.getVault().getEntries().clear();
            for (PasswordEntry entry : entries) {
                currentUser.getVault().getEntries().add(entry);
                tableModel.addRow(new Object[]{
                    entry.getSiteName(),
                    entry.getUsername(),
                    entry.getCategory()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load entries.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleLogout() {
        new LoginFrame().setVisible(true);
        dispose();
    }

    public StandardUser getCurrentUser() { return currentUser; }
    public DatabaseManager getDb() { return db; }
}
