package com.tranner.model.gui;

import javax.swing.*;

public class RegisterPanel extends JPanel {
    private JLabel username, password, confirmPassword;
    private JTextField usernameField, passwordField, confirmPasswordField;
    private JButton submitLoginCredenntials;

    public RegisterPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // --- Username & Password ---
        username = new JLabel("Username:");
        usernameField = new JTextField(20);

        password = new JLabel("Password:");
        passwordField = new JTextField(20);

        confirmPassword = new JLabel("Confirm Password:");
        confirmPasswordField = new JTextField(20);

        submitLoginCredenntials = new JButton("Submit Login Credentials");

        // Add components to the panel
        this.add(username);
        this.add(usernameField);
        this.add(password);
        this.add(passwordField);
        this.add(confirmPassword);
        this.add(confirmPasswordField);
        this.add(submitLoginCredenntials);
    }

    public JButton getSubmitLoginCredentials() {
        return submitLoginCredenntials;
    }
}
