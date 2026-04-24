package com.tranner;

import javax.swing.*;
import java.awt.CardLayout;

public class Main extends JFrame{
    private JPanel cardPanel; //initialize variables (private to ensure data protection)
    private JPanel loginPanel, insertPanel;
    private JButton loginButton, insertInfoButton;
    private JLabel insertLocation;
    private JTextField locField;

    public Main(){
        super();
    
        makeLoginPanel(); //call helper function
        makeInsertPanel();

        this.cardPanel = new JPanel(new CardLayout()); //make card panel & frame
        cardPanel.add(loginPanel, "login");
        cardPanel.add(insertPanel, "insert");
        this.add(cardPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(700,700);
        this.setVisible(true);
    }

    private void makeLoginPanel(){ //helper function to help make login panel
        this.loginPanel = new JPanel();
        this.loginButton = new JButton("login");
        this.loginPanel.add(loginButton);
        loginButton.addActionListener(select -> changeScreen("insert"));
    }

    private void makeInsertPanel(){ //helper function to help make insert info panel
        this.insertPanel = new JPanel();
        this.insertInfoButton = new JButton("GENERATE");
        this.insertLocation = new JLabel("insert the location of choice: ");
        this.locField = new JTextField(20);
        this.insertPanel.add(insertLocation);
        this.insertPanel.add(locField);
        this.insertPanel.add(insertInfoButton);
    }
    
    private void changeScreen(String screen){
        ((CardLayout) cardPanel.getLayout()).show(cardPanel, screen);
    }

    public static void main(String[] args) {
        new Main();
    }
    
}