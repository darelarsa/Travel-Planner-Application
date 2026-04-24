package com.tranner.model.person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
 
/**
 * Represents a registered user of the travel planner application.
 * Extends Person with authentication credentials and a list of travel companions.
 */
public class User extends Person {
 
    // --- Fields ---
 
    private final int userID;
    private String password;
    private List<Person> companions;   // Non-user travel buddies
 
    // --- Static ID counter (separate from Person's) ---
    private static int nextUserId = 1;
 
    // --- Constructors ---
 
    /**
     * Full constructor — used when registering a new user with all details.
     *
     * @param lastName       User's last name
     * @param firstName      User's first name
     * @param preference     Initial travel preferences (may be null if not yet set)
     * @param password Pre-hashed password string (hashing done in UserService)
     */
    public User(String lastName, String firstName, Preference preference, String password) {
        super(lastName, firstName, preference);
        this.userID = nextUserId++;
        this.password = password;
        this.companions = new ArrayList<>();
    }
 
    /**
     * Convenience constructor — creates a user without preferences set yet.
     * Preferences can be assigned later via setPreference().
     *
     * @param lastName       User's last name
     * @param firstName      User's first name
     * @param password Pre-hashed password string
     */
    public User(String lastName, String firstName, String password) {
        this(lastName, firstName, null, password);
    }
 
    // --- Authentication ---
 
    /**
     * Validates a candidate password against the stored one.
     *
     * @param candidatePassword The password attempt
     * @return true if the passwords match
     */
    public boolean verifyPassword(String candidatePassword) {
        return password.equals(candidatePassword);
    }
 
    /**
     * Updates the stored password.
     * Only call this after hashing the new raw password in UserService.
     *
     * @param newPassword The new hashed password to store
     */
    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
 
    // --- Companion Management ---
 
    /**
     * Adds a travel companion to this user's trip.
     * Companions are Person objects (not Users) — they have preferences
     * but no login credentials, per the project constraints.
     *
     * @param companion The Person to add as a companion
     */
    public void addCompanion(Person companion) {
        companions.add(companion);
    }
 
    /**
     * Removes a travel companion by their PersonID.
     *
     * @param personID The ID of the companion to remove
     * @return true if the companion was found and removed, false otherwise
     */
    public boolean removeCompanion(int personID) {
        return companions.removeIf(p -> p.getPersonID() == personID);
    }
 
    /**
     * Returns an unmodifiable view of the companions list.
     * Modifications must go through addCompanion / removeCompanion.
     */
    public List<Person> getCompanions() {
        return Collections.unmodifiableList(companions);
    }
 
    /**
     * Returns the total party size: this user + all companions.
     */
    public int getPartySize() {
        return 1 + companions.size();
    }
 
    // --- Getters ---
 
    public int getUserID() {
        return userID;
    }
 
    // --- Utility ---
 
    /**
     * Resets the user ID counter. Intended for use in unit tests only.
     */
    static void resetIdCounter() {
        nextUserId = 1;
    }
 
    @Override
    public String toString() {
        return String.format("User{userID=%d, personID=%d, name='%s', companions=%d, preference=%s}",
                userID, getPersonID(), getFullName(), companions.size(), getPreference());
    }
}