package com.tranner.model.gui;

import javax.swing.*;

public class mainMenu extends JPanel{
    private JButton loginButton, registerButton;
    private JTextField loginUsernameField, loginPasswordField;
    private JLabel loginlbl, passwordlbl, tempLogo;

    public mainMenu(){
        //this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.tempLogo = new JLabel("Tranner Logo Here");

        this.loginlbl = new JLabel("Username:");
        this.passwordlbl = new JLabel("Password:");
        this.loginUsernameField = new JTextField(10);
        this.loginPasswordField = new JTextField(10);

        this.loginButton = new JButton("Login");
        this.registerButton = new JButton("Register");

        this.add(tempLogo);
        this.add(loginlbl);
        this.add(loginUsernameField);
        this.add(passwordlbl);
        this.add(loginPasswordField);
        this.add(loginButton);
        this.add(registerButton);
    }

    public JButton getRegisterButton() {
        return registerButton;
    }
}
