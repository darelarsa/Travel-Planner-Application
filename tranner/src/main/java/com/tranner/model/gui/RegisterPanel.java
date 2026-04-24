package com.tranner.model.gui;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class RegisterPanel extends JPanel {
    private JLabel firstNameLabel;
    private JLabel lastNameLabel;
    private JLabel passwordLabel;
    private JLabel regPlaceLabel;

    private JLabel regBudgetLabel;
    private JLabel regTransportLabel;
    private JLabel regIntensityLabel;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField passwordField;
    private JTextField regPlaceField;
    private JRadioButton budgetLow;
    private JRadioButton budgetModerate;
    private JRadioButton budgetHigh;
    private JRadioButton budgetLuxury;
    private ButtonGroup budgetGroup;
    private JRadioButton transportWalking;
    private JRadioButton transportTransit;
    private JRadioButton transportRideshare;
    private JRadioButton transportCar;
    private JRadioButton transportBike;
    private ButtonGroup transportGroup;
    private JRadioButton intensityRelaxed;
    private JRadioButton intensityModerate;
    private JRadioButton intensityActive;
    private JRadioButton intensityExtreme;
    private ButtonGroup intensityGroup;
    private JButton submitRegisterButton;
    private JButton backToLoginButton;


    public RegisterPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // --- Name & Password ---
        firstNameLabel = new JLabel("First Name:");
        firstNameField = new JTextField(20);

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
        this.add(firstNameLabel);
        this.add(firstNameField);
        this.add(lastNameLabel);
        this.add(lastNameField);
        this.add(passwordLabel);
        this.add(passwordField);
        this.add(regPlaceLabel);
        this.add(regPlaceField);
        this.add(budgetPanel);
        this.add(transportPanel);
        this.add(intensityPanel);
        this.add(buttonPanel);
    }
 
    public JButton getSubmitRegisterButton() {
        return submitRegisterButton;
    }
}
