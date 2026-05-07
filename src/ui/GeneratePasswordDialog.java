package ui;

import model.PasswordGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class GeneratePasswordDialog extends JDialog {

    private JSpinner lengthSpinner;
    private JTextField resultField;
    private JLabel strengthLabel;
    private JButton generateButton;
    private JButton copyButton;

    private final PasswordGenerator generator = new PasswordGenerator();

    public GeneratePasswordDialog(JFrame parent) {
        super(parent, "Generate Password", true);
        setSize(320, 180);
        setLocationRelativeTo(parent);
        buildUI();
    }

    private void buildUI() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));

        lengthSpinner = new JSpinner(new SpinnerNumberModel(16, 8, 64, 1));
        resultField = new JTextField();
        resultField.setEditable(false);
        strengthLabel = new JLabel("");
        generateButton = new JButton("Generate");
        copyButton = new JButton("Copy");
        copyButton.setEnabled(false);

        panel.add(new JLabel("Length"));
        panel.add(lengthSpinner);
        panel.add(new JLabel("Generated"));
        panel.add(resultField);
        panel.add(new JLabel("Strength"));
        panel.add(strengthLabel);
        panel.add(generateButton);
        panel.add(copyButton);

        add(panel, BorderLayout.CENTER);

        generateButton.addActionListener(e -> handleGenerate());
        copyButton.addActionListener(e -> handleCopy());
    }

    private void handleGenerate() {
        int length = (int) lengthSpinner.getValue();
        String password = generator.generate(length);
        resultField.setText(password);

        String strength = generator.checkStrength(password);
        strengthLabel.setText(strength.substring(0, 1).toUpperCase() + strength.substring(1));
        if (strength.equals("strong")) strengthLabel.setForeground(new Color(0, 150, 0));
        else if (strength.equals("medium")) strengthLabel.setForeground(new Color(200, 130, 0));
        else strengthLabel.setForeground(Color.RED);

        copyButton.setEnabled(true);
    }

    private void handleCopy() {
        String password = resultField.getText();
        if (!password.isEmpty()) {
            Toolkit.getDefaultToolkit()
                   .getSystemClipboard()
                   .setContents(new StringSelection(password), null);
            JOptionPane.showMessageDialog(this, "Password copied to clipboard.");
        }
    }
}
