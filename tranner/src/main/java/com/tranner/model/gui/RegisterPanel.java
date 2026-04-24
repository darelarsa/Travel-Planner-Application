package com.tranner.model.gui;

import javax.swing.*;

public class RegisterPanel extends JPanel {
    private JLabel firstNameLabel, lastNameLabel;
    private JLabel usernameLabel, passwordLabel;
    private JTextField firstNameField, lastNameField;
    private JTextField usernameField, passwordField;
    private JButton submitLoginCredentials;

    public RegisterPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        firstNameLabel = new JLabel("First Name:");
        firstNameField = new JTextField(20);

        lastNameLabel = new JLabel("Last Name:");
        lastNameField = new JTextField(20);

        usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);

        passwordLabel = new JLabel("Password:");
        passwordField = new JTextField(20);

        submitLoginCredentials = new JButton("Submit Login Credentials");

        this.add(firstNameLabel);
        this.add(firstNameField);
        this.add(lastNameLabel);
        this.add(lastNameField);
        this.add(usernameLabel);
        this.add(usernameField);
        this.add(passwordLabel);
        this.add(passwordField);
        this.add(submitLoginCredentials);
    }

    public JButton getSubmitLoginCredentials() {
        return submitLoginCredentials;
    }

    public String getFirstName() {
        return firstNameField.getText().trim();
    }

    public String getLastName() {
        return lastNameField.getText().trim();
    }

    public String getUsername() {
        return usernameField.getText().trim();
    }

    public String getPassword() {
        return passwordField.getText().trim();
    }
}