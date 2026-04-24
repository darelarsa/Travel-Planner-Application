package com.tranner;

import javax.swing.*;

import com.tranner.model.gui.RegisterPanel;
import com.tranner.model.gui.createProfilePanel;
import com.tranner.model.gui.mainMenu;
import com.tranner.model.person.*;

import java.awt.CardLayout;

public class Main extends JFrame{
    private JPanel cardPanel; //initialize variables (private to ensure data protection)
    private mainMenu menuPanel;
    private RegisterPanel registrationPanel;
    private createProfilePanel createUserProfilePanel;
    //current user data
    private User currentUser;
    private Preference currUserPreference;

    public Main(){
        super();
    
        makeMainMenuPanel(); //call helper function
        makeProfilePanel();
        makeRegisterPanel();

        this.cardPanel = new JPanel(new CardLayout()); //make card panel & frame
        cardPanel.add(menuPanel, "main");
        cardPanel.add(createUserProfilePanel, "profile");
        cardPanel.add(registrationPanel, "register");
        this.add(cardPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800,800);
        this.setVisible(true);
    }

    private void makeMainMenuPanel(){ //helper function to help make main menu panel
        this.menuPanel = new mainMenu();
        JButton RegisterButton = menuPanel.getRegisterButton();  // Get the register button from the mainPanel
        RegisterButton.addActionListener(e -> {   //Adds actionlistener to submit button to handle registration logic when clicked
            System.out.println("Create your user profile, fill in profile & preference credentials");
            changeScreen("profile");
        });
    }

    private void makeProfilePanel() { //helper to help make main menu panel
        this.createUserProfilePanel = new createProfilePanel();
        JButton submitProfileButton = createUserProfilePanel.getSubmitProfileButton();  // Get the submit button from the makeProfilePanel
        submitProfileButton.addActionListener(e -> {   //Adds actionlistener to submit button to handle registration logic when clicked
            System.out.println("Profile creation successful, please fill in login credentials");

            currUserPreference = new Preference();
            currUserPreference.setBudget(createUserProfilePanel.getBudget());
            currUserPreference.setPlace(createUserProfilePanel.getPlace());
            currUserPreference.setTransport(createUserProfilePanel.getTransportMode());
            currUserPreference.setIntensity(createUserProfilePanel.getIntensity());

            String preferencestatus = currUserPreference.toString();
            System.out.println(preferencestatus);
            changeScreen("register");
        });
    }

    private void makeRegisterPanel() { //helper function to help make main menu panel
        this.registrationPanel = new RegisterPanel();
        JButton submitCredentialsButton = registrationPanel.getSubmitLoginCredentials();  // Get the submit button from the makeProfilePanel
        submitCredentialsButton.addActionListener(e -> {   //Adds actionlistener to submit button to handle registration logic when clicked
            System.out.println("user account successfully created");

            currentUser = new User(
                          registrationPanel.getLastName(),
                          registrationPanel.getFirstName(),
                          currUserPreference,
                          registrationPanel.getUsername(),
                          registrationPanel.getPassword()
                          );
            
            String userstatus = currentUser.toString();
            System.out.println(userstatus);

            changeScreen("main");
        });
    }

    private void changeScreen(String screen){
        ((CardLayout) cardPanel.getLayout()).show(cardPanel, screen);
    }

    public static void main(String[] args) {
        new Main();
    }
    
}