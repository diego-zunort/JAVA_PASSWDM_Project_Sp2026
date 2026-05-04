package ui;

import model.StandardUser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainFrame extends JFrame {

    private StandardUser currentUser;
    private JTable vaultTable;
    private DefaultTableModel tableModel;

    private JButton generateButton;
    private JButton addButton;
    private JButton modifyButton;
    private JButton removeButton;
    private JButton viewButton;

    public MainFrame(StandardUser user) {
        this.currentUser = user;
        setTitle("Password Manager");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        String[] columns = {"Username", "Site", "Password"};
        tableModel = new DefaultTableModel(columns, 0);
        vaultTable = new JTable(tableModel);

        JLabel userLabel = new JLabel(currentUser.getUsername(), SwingConstants.RIGHT);

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

        add(userLabel, BorderLayout.NORTH);
        add(new JScrollPane(vaultTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        generateButton.addActionListener(e -> new GeneratePasswordDialog(this).setVisible(true));
        addButton.addActionListener(e -> new AddPasswordDialog(this, currentUser).setVisible(true));
        modifyButton.addActionListener(e -> new ModifyPasswordDialog(this, currentUser).setVisible(true));
        removeButton.addActionListener(e -> new RemovePasswordDialog(this, currentUser).setVisible(true));
        viewButton.addActionListener(e -> new ViewPasswordDialog(this, currentUser).setVisible(true));
    }

    public void refreshTable() {
        // TODO: reload entries from vault/DB and repopulate tableModel
    }
}
