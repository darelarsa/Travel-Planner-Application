package com.tranner.model.place.accommodation;

import java.util.List;

import com.tranner.model.place.Address;
 
/**
 * Represents a privately rented apartment or short-term rental.
 * Extends Accommodation with a landlord contact name.
 */
public class Apartment extends Accommodation {
 
    // --- Fields ---
 
    private String landlord;    // Contact name of the property owner
 
    // --- Constructor ---
 
    /**
     * @param name         Apartment or property name/identifier
     * @param address      Physical address
     * @param description  Short description of the rental
     * @param rating       Rating out of 5.0
     * @param price        Approximate nightly rate for the full unit in USD
     * @param amenities    List of included amenities (e.g., ["Kitchen", "Washer"])
     * @param roomCapacity Maximum number of guests the unit can accommodate
     * @param landlord     Name of the landlord or property manager
     */
    public Apartment(String name, Address address, String description,
                     double rating, double price,
                     List<String> amenities, int roomCapacity,
                     String landlord) {
        super(name, address, description, rating, price, amenities, roomCapacity);
        this.landlord = landlord;
    }
 
    // --- Getters / Setters ---
 
    public String getLandlord() { return landlord; }
 
    public void setLandlord(String landlord) {
        this.landlord = landlord;
    }
 
    // --- Place Abstract Implementation ---
 
    @Override
    public String getCategory() { return "Apartment"; }
 
    // --- Utility ---
 
    @Override
    public String toString() {
        return String.format("Apartment{name='%s', landlord='%s', capacity=%d, rating=%.1f, price=%.2f, address=%s}",
                getName(), landlord, getRoomCapacity(), getRating(), getPrice(), getAddress());
    }
}