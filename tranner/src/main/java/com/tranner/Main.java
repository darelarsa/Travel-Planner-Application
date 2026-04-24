package com.tranner;

import javax.swing.*;

import com.tranner.model.gui.RegisterPanel;

//import com.tranner.model.person.*;

import java.awt.CardLayout;

public class Main extends JFrame{
    private JPanel cardPanel; //initialize variables (private to ensure data protection)
    private JPanel loginPanel, insertPanel, registrationPanel;
    private JButton loginButton, insertInfoButton, registerButton;
    private JLabel insertLocation;
    private JTextField locField;

    //current user data
    /*private User currentUser;
    private Preference currUserPreference;*/

    public Main(){
        super();
    
        makeLoginPanel(); //call helper function
        makeRegisterPanel();
        makeInsertPanel();

        this.cardPanel = new JPanel(new CardLayout()); //make card panel & frame
        cardPanel.add(loginPanel, "login");
        cardPanel.add(insertPanel, "insert");
        cardPanel.add(registrationPanel, "register");
        this.add(cardPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(700,700);
        this.setVisible(true);
    }

    private void makeLoginPanel(){ //helper function to help make login panel
        this.loginPanel = new JPanel();
        this.loginButton = new JButton("login");
        this.registerButton = new JButton("register");
        this.loginPanel.add(loginButton);
        loginButton.addActionListener(select -> changeScreen("insert"));
        this.loginPanel.add(registerButton);
        registerButton.addActionListener(select -> changeScreen("register"));
    }


    // ** FOR DAREL **
    private void makeRegisterPanel() {
        this.registrationPanel = new RegisterPanel();
        JButton submitButton = ((RegisterPanel) registrationPanel).getSubmitRegisterButton();  // Get the submit button from the RegisterPanel
        submitButton.addActionListener(e -> {   //Adds actionlistener to submit button to handle registration logic when clicked
            // TODO: Implement registration logic here
            System.out.println("Registration submitted! (This is where you'd handle the registration logic.)");
            changeScreen("login");
        });
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