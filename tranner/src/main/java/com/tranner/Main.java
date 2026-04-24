package com.tranner;

import javax.swing.*;

//import com.tranner.model.person.*;

import java.awt.CardLayout;

public class Main extends JFrame{
    private JPanel cardPanel; //initialize variables (private to ensure data protection)
    private JPanel loginPanel, insertPanel, registrationPanel;
    private JButton loginButton, insertInfoButton, registerButton;
    
    // Registration fields
    private JLabel firstNameLabel, lastNameLabel, passwordLabel, insertLocation;
    private JLabel regBudgetLabel, regTransportLabel, regIntensityLabel, regPlaceLabel;
    private JTextField firstNameField, lastNameField, passwordField, regPlaceField, locField;
    private JButton submitRegisterButton, backToLoginButton;

    //all the radio buttons
    //budget radio buttons
    private JRadioButton budgetLow, budgetModerate, budgetHigh, budgetLuxury;
    private ButtonGroup budgetGroup;
    //transport radio buttons
    private JRadioButton transportWalking, transportTransit, transportRideshare, transportCar, transportBike;
    private ButtonGroup transportGroup;
    //intensity radio buttons
    private JRadioButton intensityRelaxed, intensityModerate, intensityActive, intensityExtreme;
    private ButtonGroup intensityGroup;

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

    private void makeRegisterPanel() {
        this.registrationPanel = new JPanel();
        this.registrationPanel.setLayout(new BoxLayout(registrationPanel, BoxLayout.Y_AXIS));

        // --- Name & Password ---
        this.firstNameLabel = new JLabel("First Name:");
        this.firstNameField = new JTextField(20);

        this.lastNameLabel = new JLabel("Last Name:");
        this.lastNameField = new JTextField(20);

        this.passwordLabel = new JLabel("Password:");
        this.passwordField = new JTextField(20);

        // --- Place ---
        this.regPlaceLabel = new JLabel("Destination:");
        this.regPlaceField = new JTextField(20);

        // --- Budget ---
        this.regBudgetLabel = new JLabel("Budget:");
        this.budgetLow      = new JRadioButton("Low");
        this.budgetModerate = new JRadioButton("Moderate");
        this.budgetHigh     = new JRadioButton("High");
        this.budgetLuxury   = new JRadioButton("Luxury");
        this.budgetGroup    = new ButtonGroup();
        budgetGroup.add(budgetLow);
        budgetGroup.add(budgetModerate);
        budgetGroup.add(budgetHigh);
        budgetGroup.add(budgetLuxury);

        JPanel budgetPanel = new JPanel();
        budgetPanel.add(regBudgetLabel);
        budgetPanel.add(budgetLow);
        budgetPanel.add(budgetModerate);
        budgetPanel.add(budgetHigh);
        budgetPanel.add(budgetLuxury);

        // --- Transport ---
        this.regTransportLabel  = new JLabel("Transport Mode:");
        this.transportWalking  = new JRadioButton("Walking");
        this.transportTransit  = new JRadioButton("Public Transit");
        this.transportRideshare = new JRadioButton("Rideshare");
        this.transportCar      = new JRadioButton("Rental Car");
        this.transportBike     = new JRadioButton("Bicycle");
        this.transportGroup    = new ButtonGroup();
        transportGroup.add(transportWalking);
        transportGroup.add(transportTransit);
        transportGroup.add(transportRideshare);
        transportGroup.add(transportCar);
        transportGroup.add(transportBike);

        JPanel transportPanel = new JPanel();
        transportPanel.add(regTransportLabel);
        transportPanel.add(transportWalking);
        transportPanel.add(transportTransit);
        transportPanel.add(transportRideshare);
        transportPanel.add(transportCar);
        transportPanel.add(transportBike);

        // --- Intensity ---
        this.regIntensityLabel  = new JLabel("Trip Intensity:");
        this.intensityRelaxed  = new JRadioButton("Relaxed");
        this.intensityModerate = new JRadioButton("Moderate");
        this.intensityActive   = new JRadioButton("Active");
        this.intensityExtreme  = new JRadioButton("Extreme");
        this.intensityGroup    = new ButtonGroup();
        intensityGroup.add(intensityRelaxed);
        intensityGroup.add(intensityModerate);
        intensityGroup.add(intensityActive);
        intensityGroup.add(intensityExtreme);

        JPanel intensityPanel = new JPanel();
        intensityPanel.add(regIntensityLabel);
        intensityPanel.add(intensityRelaxed);
        intensityPanel.add(intensityModerate);
        intensityPanel.add(intensityActive);
        intensityPanel.add(intensityExtreme);

        // --- Buttons ---
        this.submitRegisterButton = new JButton("Submit");
        this.backToLoginButton    = new JButton("Back to Login");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitRegisterButton);
        buttonPanel.add(backToLoginButton);

        // --- Assemble ---
        registrationPanel.add(firstNameLabel);
        registrationPanel.add(firstNameField);
        registrationPanel.add(lastNameLabel);
        registrationPanel.add(lastNameField);
        registrationPanel.add(passwordLabel);
        registrationPanel.add(passwordField);
        registrationPanel.add(regPlaceLabel);
        registrationPanel.add(regPlaceField);
        registrationPanel.add(budgetPanel);
        registrationPanel.add(transportPanel);
        registrationPanel.add(intensityPanel);
        registrationPanel.add(buttonPanel);
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