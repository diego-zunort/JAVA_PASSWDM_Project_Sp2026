package ui;

import javax.swing.*;
import java.awt.*;

public class SignUpFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField retypePasswordField;
    private JPasswordField securityPinField;
    private JButton createAccountButton;

    public SignUpFrame() {
        setTitle("Sign Up");
        setSize(320, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        retypePasswordField = new JPasswordField();
        securityPinField = new JPasswordField();
        createAccountButton = new JButton("Create Account");

        panel.add(new JLabel("Set User"));
        panel.add(usernameField);
        panel.add(new JLabel("Set Password"));
        panel.add(passwordField);
        panel.add(new JLabel("Retype Password"));
        panel.add(retypePasswordField);
        panel.add(new JLabel("Set Security Pin"));
        panel.add(securityPinField);
        panel.add(new JLabel(""));
        panel.add(createAccountButton);

        add(panel, BorderLayout.CENTER);

        createAccountButton.addActionListener(e -> handleCreateAccount());
    }

    private void handleCreateAccount() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String retypePassword = new String(retypePasswordField.getPassword());
        String securityPin = new String(securityPinField.getPassword());
        // TODO: validate fields match, save to DB, then open LoginFrame
    }
}
