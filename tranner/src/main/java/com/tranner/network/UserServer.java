package com.tranner.network;

import com.tranner.model.itinerary.Itinerary;
import com.tranner.model.person.Person;
import com.tranner.model.person.User;
import com.tranner.model.person.Preference;

import java.net.*;
import java.io.*;
import java.util.*;

public class UserServer {
    
    // -- Constants --

    public static final int PORT = 8189;

    // -- Fields --

    private final Map<Integer, User> userStore;
    private final Map<Integer, Itinerary> itineraryStore;
    private ServerSocket serverSocket;
    private boolean isRunning;

    // -- Constructor --
    public UserServer() {
        this.userStore = new HashMap<>();
        this.itineraryStore = new HashMap<>();
    }

    /* Starts the user server */
    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            isRunning = true;
            System.out.println("UserServer started on port " + PORT);

            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                // Handle client connection in a separate thread (not implemented here)
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
        if (user == null || userStore.containsKey(user.getUserID())) {
            return false; // Invalid user or already exists
        }
        userStore.put(user.getUserID(), user);
        return true;
    }
}
