package com.tranner;

import javax.swing.*;

import com.tranner.gui.*;
import com.tranner.model.person.*;
import com.tranner.network.RequestMessage;
import com.tranner.network.RequestMessage.RequestType;
import com.tranner.network.ResponseMessage;

import java.awt.CardLayout;
import java.io.*;
import java.net.Socket;

public class Main extends JFrame{
    private JPanel cardPanel; //initialize variables (private to ensure data protection)
    private LoginPanel loginPanel;
    private RegisterPanel registrationPanel;
    private createProfilePanel createUserProfilePanel;
    private MainPanel mainPanel;
    private TripPanel tripPanel;

    //current user data
    private User currentUser;
    private boolean addingCompanion;

    public Main(){
        super();

        currentUser = null;
        addingCompanion = false;
    
        makeMainMenuPanel(); //call helper function
        makeProfilePanel();
        makeRegisterPanel();
        makeMainMenuPanel();
        makeProfilePanel();
        makeRegisterPanel();
        makeMainPanel();
        makeTripPanel();

        this.cardPanel = new JPanel(new CardLayout()); // make card panel & frame
        cardPanel.add(loginPanel, "login");
        cardPanel.add(createUserProfilePanel, "profile");
        cardPanel.add(registrationPanel, "register");
        cardPanel.add(mainPanel, "main");
        cardPanel.add(tripPanel, "trip");
        this.add(cardPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800,800);
        this.setVisible(true);
    }

    private void makeMainMenuPanel(){ //helper function to help make main menu panel
        this.loginPanel = new LoginPanel();
        JButton loginButton = loginPanel.getLoginButton();  // Get the submit button from the mainPanel
        loginButton.addActionListener(e -> {   //Adds actionlistener to submit button to handle login logic when clicked
            String username = loginPanel.getUsername();
            String password = loginPanel.getPassword();
            RequestMessage loginRequest = RequestMessage.createLoginRequest(username, password);
            sendRequestToServer(loginRequest);
            if (currentUser != null) {
                System.out.println("Login successful!");
                System.out.println("User info:" + currentUser.toString());
                changeScreen("main");
            } else {
                JOptionPane.showMessageDialog(this, "Login failed. Please check your credentials.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        JButton RegisterButton = loginPanel.getRegisterButton();  // Get the register button from the mainPanel
        RegisterButton.addActionListener(e -> {   //Adds actionlistener to submit button to handle registration logic when clicked
            System.out.println("Create your user profile, fill in profile & preference credentials");
            changeScreen("register");
        });
    }

    // Helper function to send a request message to the server and handle the response
    private void sendRequestToServer(RequestMessage request) {
        try {
            Socket socket = new Socket("localhost", 8189);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Send the request message to the server
            out.writeObject(request);

            // Read the response message from the server
            ResponseMessage response = (ResponseMessage) in.readObject();
            System.out.println("Received response: " + response);

            // Handle the response (for example, check if login was successful)
            if (response.isSuccess()) {
                System.out.println("Operation successful: " + response.getMessage());
                if (request.getType() == RequestType.LOGIN) {
                    currentUser = response.getUser(); // Set the current user based on server response
                }
            } else {
                System.out.println("Operation failed: " + response.getMessage());
            }

            // Close the connection
            in.close();
            out.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void makeProfilePanel() { //helper to help make main menu panel
        this.createUserProfilePanel = new createProfilePanel();
        JButton submitProfileButton = createUserProfilePanel.getSubmitProfileButton();  // Get the submit button from the makeProfilePanel
        JButton cancelButton = createUserProfilePanel.getBackToLoginButton(); // Get the cancel button from the makeProfilePanel

        submitProfileButton.addActionListener(e -> {   //Adds actionlistener to submit button to handle registration logic when clicked
            System.out.println("Profile creation successful, please fill in login credentials");

            String firstName = createUserProfilePanel.getFirstName();
            String lastName = createUserProfilePanel.getLastName();
            Preference currPreference = new Preference(
                createUserProfilePanel.getBudget(),
                createUserProfilePanel.getTransportMode(),
                createUserProfilePanel.getIntensity()
            );

            if (addingCompanion) {      // If we're adding a companion, we create a new Person for the companion and add it to the current user's companions list
                Person companion = new Person(lastName, firstName, currPreference);
                currentUser.addCompanion(companion);
                mainPanel.setCompanions(currentUser.getCompanions()); // Update the main panel's companion list display
                addingCompanion = false; // Reset the flag after adding the companion
                mainPanel.refreshTrips(); // Refresh the main panel to show the new companion
                createUserProfilePanel.resetFields(); // Clear the profile fields after adding a companion
                changeScreen("main");
            } else {
                currentUser.setFirstName(firstName);
                currentUser.setLastName(lastName);
                currentUser.setPreference(currPreference);
                RequestMessage registerRequest = RequestMessage.createRegisterRequest(currentUser);
                sendRequestToServer(registerRequest);
                currentUser = null; // Clear the current user after registration
                createUserProfilePanel.resetFields(); // Clear the profile fields after successful profile creation
                changeScreen("login");
            }
        });

        cancelButton.addActionListener(e -> { // Action listener for cancel button to return to login screen
            createUserProfilePanel.resetFields(); // Clear the profile fields when canceling

            if (addingCompanion) { // If we were in the process of adding a companion, reset the flag and return to the main screen instead of login
                addingCompanion = false;
                changeScreen("main");
                return;
            } else {
                changeScreen("login");
            }
        });
    }

    private void makeRegisterPanel() { //helper function to help make main menu panel
        this.registrationPanel = new RegisterPanel();
        JButton submitCredentialsButton = registrationPanel.getSubmitLoginCredentials();  // Get the submit button from the makeProfilePanel
        submitCredentialsButton.addActionListener(e -> {   //Adds actionlistener to submit button to handle registration logic when clicked
            System.out.println("user account successfully created");

            if (registrationPanel.getUsername().isEmpty() || registrationPanel.getPassword().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (!registrationPanel.getPassword().equals(registrationPanel.getConfirmPassword())) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                currentUser = new User(
                          "",
                          "",
                          null,
                          registrationPanel.getUsername(),
                          registrationPanel.getPassword()
                          );
                changeScreen("profile");
            }
            registrationPanel.clearFields(); // Clear the registration fields after successful registration
        });
    }

    // Helper function to create the main menu panel for the user
    // Current functionalities include companion creation and navigating to the trip panel
    private void makeMainPanel() { 
        this.mainPanel = new MainPanel();

        mainPanel.getAddTripButton().addActionListener(e -> {
            changeScreen("trip");
        });

        mainPanel.getAddPersonButton().addActionListener(e -> {
            addingCompanion = true;
            changeScreen("profile");
        });
    }

    private void makeTripPanel() {
        this.tripPanel = new TripPanel();
    }

    private void changeScreen(String screen){
        ((CardLayout) cardPanel.getLayout()).show(cardPanel, screen);
    }

    public static void main(String[] args) {
        new Main();
    }
    
}