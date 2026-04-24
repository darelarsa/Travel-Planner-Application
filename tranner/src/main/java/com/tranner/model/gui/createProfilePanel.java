package com.tranner.model.gui;

import javax.swing.*;
import com.tranner.model.person.Preference;

public class createProfilePanel extends JPanel {
    private JLabel regPlaceLabel;
    private JLabel regBudgetLabel;
    private JLabel regTransportLabel;
    private JLabel regIntensityLabel;
    private JTextField regPlaceField;
    private JRadioButton budgetLow, budgetModerate, budgetHigh, budgetLuxury;
    private ButtonGroup budgetGroup;
    private JRadioButton transportWalking, transportTransit, transportRideshare,
                         transportCar, transportBike;
    private ButtonGroup transportGroup;
    private JRadioButton intensityRelaxed, intensityModerate, intensityActive, intensityExtreme;
    private ButtonGroup intensityGroup;
    private JButton submitProfileButton;
    private JButton backToLoginButton;

    public createProfilePanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // --- Place ---
        regPlaceLabel = new JLabel("Destination:");
        regPlaceField = new JTextField(20);

        // --- Budget ---
        regBudgetLabel = new JLabel("Budget:");
        budgetLow      = new JRadioButton("Low");
        budgetModerate = new JRadioButton("Moderate");
        budgetHigh     = new JRadioButton("High");
        budgetLuxury   = new JRadioButton("Luxury");
        budgetGroup    = new ButtonGroup();
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
        regTransportLabel  = new JLabel("Transport Mode:");
        transportWalking   = new JRadioButton("Walking");
        transportTransit   = new JRadioButton("Public Transit");
        transportRideshare = new JRadioButton("Rideshare");
        transportCar       = new JRadioButton("Rental Car");
        transportBike      = new JRadioButton("Bicycle");
        transportGroup     = new ButtonGroup();
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
        regIntensityLabel  = new JLabel("Trip Intensity:");
        intensityRelaxed   = new JRadioButton("Relaxed");
        intensityModerate  = new JRadioButton("Moderate");
        intensityActive    = new JRadioButton("Active");
        intensityExtreme   = new JRadioButton("Extreme");
        intensityGroup     = new ButtonGroup();
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
        submitProfileButton = new JButton("Submit Profile");
        backToLoginButton   = new JButton("Back to Login");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitProfileButton);
        buttonPanel.add(backToLoginButton);

        // --- Assemble ---
        this.add(regPlaceLabel);
        this.add(regPlaceField);
        this.add(budgetPanel);
        this.add(transportPanel);
        this.add(intensityPanel);
        this.add(buttonPanel);
    }

    public JButton getSubmitProfileButton() {
        return submitProfileButton;
    }

    public JButton getBackToLoginButton() {
        return backToLoginButton;
    }

    public String getPlace() {
        return regPlaceField.getText().trim();
    }

    public Preference.Budget getBudget() {
        if (budgetLow.isSelected())      return Preference.Budget.LOW;
        if (budgetModerate.isSelected()) return Preference.Budget.MODERATE;
        if (budgetHigh.isSelected())     return Preference.Budget.HIGH;
        if (budgetLuxury.isSelected())   return Preference.Budget.LUXURY;
        return null;
    }

    public Preference.TransportMode getTransportMode() {
        if (transportWalking.isSelected())   return Preference.TransportMode.WALKING;
        if (transportTransit.isSelected())   return Preference.TransportMode.PUBLIC_TRANSIT;
        if (transportRideshare.isSelected()) return Preference.TransportMode.RIDESHARE;
        if (transportCar.isSelected())       return Preference.TransportMode.RENTAL_CAR;
        if (transportBike.isSelected())      return Preference.TransportMode.BICYCLE;
        return null;
    }

    public Preference.Intensity getIntensity() {
        if (intensityRelaxed.isSelected())  return Preference.Intensity.RELAXED;
        if (intensityModerate.isSelected()) return Preference.Intensity.MODERATE;
        if (intensityActive.isSelected())   return Preference.Intensity.ACTIVE;
        if (intensityExtreme.isSelected())  return Preference.Intensity.EXTREME;
        return null;
    }

    public Preference getPreference() {
        return new Preference(getBudget(), getPlace(), getTransportMode(), getIntensity());
    }
}