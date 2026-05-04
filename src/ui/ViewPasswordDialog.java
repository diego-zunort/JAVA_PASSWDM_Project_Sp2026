package ui;

import model.StandardUser;

import javax.swing.*;
import java.awt.*;

public class ViewPasswordDialog extends JDialog {

    private StandardUser currentUser;
    private MainFrame parentFrame;

    private JComboBox<String> accountDropdown;
    private JPasswordField securityPinField;
    private JButton viewButton;

    public ViewPasswordDialog(MainFrame parent, StandardUser user) {
        super(parent, "View Password", true);
        this.parentFrame = parent;
        this.currentUser = user;
        setSize(300, 150);
        setLocationRelativeTo(parent);
        buildUI();
    }

    private void buildUI() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        accountDropdown = new JComboBox<>();
        securityPinField = new JPasswordField();
        viewButton = new JButton("View");

        // TODO: populate accountDropdown with site names from user's vault

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
        String selectedAccount = (String) accountDropdown.getSelectedItem();
        String securityPin = new String(securityPinField.getPassword());
        // TODO: verify securityPin, decrypt and display password in a dialog
    }
}
