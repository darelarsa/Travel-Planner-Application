package com.tranner.model.person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tranner.model.itinerary.Itinerary;
 
/**
 * Represents a registered user of the travel planner application.
 * Extends Person with authentication credentials and a list of travel companions.
 */
public class User extends Person {
 
    // --- Fields ---
 
    private final int userID;
    private String username;
    private String password;
    private List<Person> companions;   // Non-user travel buddies
    private List<Itinerary> itineraries;   // Itineraries created by this user
 
    // --- Static ID counter (separate from Person's) ---
    private static int nextUserId = 1;
 
    // --- Constructors ---
 
    /**
     * Full constructor — used when registering a new user with all details.
     *
     * @param lastName       User's last name
     * @param firstName      User's first name
     * @param preference     Initial travel preferences (may be null if not yet set)
     * @param username       Unique username for login
     * @param password       Password string (hashing done in UserService)
     */
    public User(String lastName, String firstName, Preference preference, String username, String password) {
        super(lastName, firstName, preference);
        this.userID = nextUserId++;
        this.username = username;
        this.password = password;
        this.companions = new ArrayList<>();
        this.itineraries = new ArrayList<>();
    }
 
    /**
     * Convenience constructor — creates a user without preferences set yet.
     * Preferences can be assigned later via setPreference().
     *
     * @param lastName       User's last name
     * @param firstName      User's first name
     * @param username       Unique username for login
     * @param password       Password string
     */
    public User(String lastName, String firstName, String username, String password) {
        this(lastName, firstName, null, username, password);
    }

    // --- Getters ---
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
 
    // --- Authentication ---

    /**
     * Validates a candidate username against the stored one.
     * 
     * @param candidateUsername The username attempt
     * @return true if the usernames match
     */
    public boolean verifyUsername(String candidateUsername) {
        return username.equals(candidateUsername);
    }

    /**
     * Updates the stored username.
     *
     * @param newUsername The new username to store
     */
    public void updateUsername(String newUsername) {
        this.username = newUsername;
    }

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
 
    // --- Itinerary Management ---

    /**
     * Adds an itinerary to this user's list of created itineraries.
     *
     * @param itinerary The Itinerary to add
     */
    public void addItinerary(Itinerary itinerary) {
        itineraries.add(itinerary);
    }

    /**
     * Returns an unmodifiable view of the itineraries created by this user.
     * Modifications must go through addItinerary.
     * 
     * @return An unmodifiable list of the user's itineraries
     */
    public List<Itinerary> getItineraries() {
        return Collections.unmodifiableList(itineraries);
    }

    /**
     * Removes an itinerary by its ID.
     * @return
     */

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