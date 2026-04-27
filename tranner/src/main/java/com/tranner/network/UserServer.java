package com.tranner.network;

import com.tranner.model.person.User;

import java.net.*;
import java.io.*;
import java.util.*;

public class UserServer {
    
    // -- Constants --

    public static final int PORT = 8189;

    // -- Fields --

    private final List<User> userStore;
    private ServerSocket serverSocket;
    private boolean isRunning;

    // -- Constructor --
    
    public UserServer() {
        this.userStore = new ArrayList<>();
        this.serverSocket = null;
        this.isRunning = false;
    }

    /* Starts the user server */
    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            isRunning = true;
            System.out.println("UserServer started on port " + PORT);

            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                try {
                    ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                    ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());

                    // Read the request message from the client
                    RequestMessage request = (RequestMessage) in.readObject();
                    
                    System.out.println("Received request: " + request.getType());

                    // Process the request and generate a response
                    if (request.getType() == RequestMessage.RequestType.LOGIN) {
                        String username = request.getUsername();
                        String password = request.getPassword();
                        boolean foundUser = false;
                        for (int i = 0; i < userStore.size(); i++) {
                            User user = userStore.get(i);
                            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                                out.writeObject(ResponseMessage.createSuccessResponse("Login successful", user));
                                foundUser = true;
                                break;
                            }
                        }
                        if (!foundUser) {
                            out.writeObject(ResponseMessage.createFailureResponse("Login failed: Invalid username or password"));
                        }
                    } else if (request.getType() == RequestMessage.RequestType.REGISTER) {
                        User newUser = request.getNewUser();
                        if (addUser(newUser)) {
                            out.writeObject(ResponseMessage.createSuccessResponse("Registration successful", newUser));
                        } else {
                            out.writeObject(ResponseMessage.createFailureResponse("Registration failed: User already exists"));
                        }
                    } else if (request.getType() == RequestMessage.RequestType.LOGOUT) {
                        // For simplicity, we just acknowledge the logout request
                        out.writeObject(ResponseMessage.createSuccessResponse("Logout successful", null));
                    } else {
                        out.writeObject(ResponseMessage.createFailureResponse("Unknown request type"));
                    }

                    printUsers(); // Print the current users in the system for debugging
                    out.flush();

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Stops the user server */
    public void stop() {
        isRunning = false;
        try {
            if (serverSocket != null) serverSocket.close();
            System.out.println("UserServer stopped.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean addUser(User user) {
        if (user == null || userStore.contains(user)) {
            return false; // Invalid user or already exists
        }
        userStore.add(user);
        return true;
    }

    public void printUsers() {
        System.out.println("Current users in the system:");
        for (User user : userStore) {
            System.out.println(user.toString());
        }
    }

    // main method for testing
    public static void main(String[] args) {
        UserServer server = new UserServer();
        server.start();
    }
}
