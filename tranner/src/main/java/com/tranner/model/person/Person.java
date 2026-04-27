package com.tranner.model.person;

import java.io.Serializable;

/**
 * Represents a general person in the travel planner system.
 * Acts as the base class for User and any non-registered travel companions.
 */
public class Person implements Serializable {
 
    // --- Static ID counter ---
    private static int nextId = 1;
 
    // --- Fields ---
    private final int personID;
    private String lastName;
    private String firstName;
    private Preference preference;
 
    // --- Constructors ---
 
    /**
     * Full constructor — used when all person details are known up front.
     *
     * @param lastName   Person's last name
     * @param firstName  Person's first name
     * @param preference Travel preferences associated with this person
     */
    public Person(String lastName, String firstName, Preference preference) {
        this.personID  = nextId++;
        this.lastName  = lastName;
        this.firstName = firstName;
        this.preference = preference;
    }
 
    /**
     * Minimal constructor — preference can be set later via setter.
     * Useful when a travel companion is added before preferences are filled in.
     *
     * @param lastName  Person's last name
     * @param firstName Person's first name
     */
    public Person(String lastName, String firstName) {
        this(lastName, firstName, null);
    }
 
    // --- Getters ---
 
    public int getPersonID() {
        return personID;
    }
 
    public String getLastName() {
        return lastName;
    }
 
    public String getFirstName() {
        return firstName;
    }
 
    public String getFullName() {
        return firstName + " " + lastName;
    }
 
    public Preference getPreference() {
        return preference;
    }
 
    // --- Setters ---
 
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
 
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
 
    public void setPreference(Preference preference) {
        this.preference = preference;
    }
 
    // --- Utility ---
 
    /**
     * Resets the ID counter.
     */
    static void resetIdCounter() {
        nextId = 1;
    }
 
    @Override
    public String toString() {
        return String.format("Person{id=%d, name='%s', preference=%s}",
                personID, getFullName(), preference);
    }
 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person other = (Person) o;
        return this.personID == other.personID;
    }
}
 