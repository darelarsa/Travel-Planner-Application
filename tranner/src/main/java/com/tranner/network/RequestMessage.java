package com.tranner.network;

import java.io.Serializable;

import com.tranner.model.person.User;

public class RequestMessage implements Serializable {
    
    public enum RequestType {
        LOGIN,
        LOGOUT,
        REGISTER
    }

    private final RequestType type;

    // For Login requests
    private final String username;
    private final String password;

    // For Register requests
    private final User newUser;
    
    // For Logout requests
    private final User usertoLogout;

    public RequestMessage(RequestType type, String username, String password, User newUser, User userToLogout) {
        this.type = type;
        this.username = username;
        this.password = password;
        this.newUser = newUser;
        this.usertoLogout = userToLogout;
    }

    // --- Static factory methods for convenience ---
    public static RequestMessage createLoginRequest(String username, String password) {
        return new RequestMessage(RequestType.LOGIN, username, password, null, null);
    }

    public static RequestMessage createRegisterRequest(User newUser) {
        return new RequestMessage(RequestType.REGISTER, null, null, newUser, null);
    }

    public static RequestMessage createLogoutRequest(User userToLogout) {
        return new RequestMessage(RequestType.LOGOUT, null, null, null, userToLogout);
    }

    // --- Getters ---
    public RequestType getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public User getNewUser() {
        return newUser;
    }

    public User getUserToLogout() {
        return usertoLogout;
    }

    // --- Utility ---
    public String toString() {
        return "RequestMessage{" +
                "type=" + type +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", newUser=" + newUser +
                ", usertoLogout=" + usertoLogout +
                '}';
    }
}
