package ui;

import model.PasswordGenerator;

import javax.swing.*;
import java.awt.*;

public class GeneratePasswordDialog extends JDialog {

    private JTextField siteField;
    private JTextField usernameField;
    private JButton generateButton;

    public GeneratePasswordDialog(JFrame parent) {
        super(parent, "Generate Password", true);
        setSize(300, 150);
        setLocationRelativeTo(parent);
        buildUI();
    }

    private void buildUI() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        siteField = new JTextField();
        usernameField = new JTextField();
        generateButton = new JButton("Generate");

        panel.add(new JLabel("Site"));
        panel.add(siteField);
        panel.add(new JLabel("Username"));
        panel.add(usernameField);
        panel.add(new JLabel(""));
        panel.add(generateButton);

        add(panel, BorderLayout.CENTER);

        generateButton.addActionListener(e -> handleGenerate());
    }

    private void handleGenerate() {
        PasswordGenerator generator = new PasswordGenerator();
        String generated = generator.generate(16);
        // TODO: display generated password to user, allow copying
    }
}
