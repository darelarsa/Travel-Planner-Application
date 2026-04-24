package com.tranner.model.gui;

import javax.swing.*;

public class mainMenu extends JPanel{
    private JButton loginButton, registerButton;
    public mainMenu(){
        this.loginButton = new JButton("Login");
        this.registerButton = new JButton("Register");

        this.add(loginButton);
        this.add(registerButton);
    }

    public JButton getRegisterButton() {
        return registerButton;
    }
}
