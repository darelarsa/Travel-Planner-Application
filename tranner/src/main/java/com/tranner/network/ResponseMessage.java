package com.tranner.network;

import java.io.Serializable;

import com.tranner.model.person.User;

public class ResponseMessage implements Serializable {

    public enum ResponseType {
        SUCCESS,
        LOGIN_SUCCESS,
        FAILURE
    }

    private final ResponseType type;

    // For success responses
    private final String message;
    private final User user;  // Optional user data for successful login responses

    public ResponseMessage(ResponseType type, String message, User user) {
        this.type = type;
        this.message = message;
        this.user = user;
    }

    // --- Static factory methods for convenience ---
    public static ResponseMessage createSuccessResponse(String message, User user) {
        return new ResponseMessage(ResponseType.SUCCESS, message, user);
    }

    public static ResponseMessage createFailureResponse(String message) {
        return new ResponseMessage(ResponseType.FAILURE, message, null);
    }

    // --- Getters ---
    public ResponseType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
    
    // --- Utiltiy ---
    public boolean isSuccess() {
        return type == ResponseType.SUCCESS || type == ResponseType.LOGIN_SUCCESS;
    }

    public String toString() {
        return "ResponseMessage{" +
                "type=" + type +
                ", message='" + message + '\'' +
                '}';
    }
}
