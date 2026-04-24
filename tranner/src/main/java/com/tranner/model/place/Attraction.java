package com.tranner.model.place;

import java.time.LocalDate;
 
/**
 * Represents a tourist attraction or point of interest.
 * Extends Place with an indoor/outdoor flag.
 */
public class Attraction extends Place { 
    private boolean isIndoor;
 
    // --- Constructor ---
 
    /**
     * @param name        Display name of the attraction
     * @param address     Physical address
     * @param description Short description
     * @param rating      Rating out of 5.0
     * @param price       Approximate entry cost in USD
     * @param isIndoor    Check if the attraction is indoors (true) or outdoors (false)
     */
    public Attraction(String name, Address address, String description,
                      double rating, double price, boolean isIndoor) {
        super(name, address, description, rating, price);
        this.isIndoor = isIndoor;
    }

    // --- Getters/Setters ---
 
    public boolean isIndoor() {
        return isIndoor;
    }
 
    public void setIndoor(boolean isIndoor) {
        this.isIndoor = isIndoor;
    }
 
    /**
     * Attractions may offer discounts on specific days (e.g., free Tuesdays).
     */
    @Override
    public String checkDiscounts(LocalDate date) {
        if (date == null) return null;
        return switch (date.getDayOfWeek()) {       // This is just an placeholder for now
            case MONDAY -> "10% weekday discount";
            default     -> null;
        };
    }
 
    @Override
    public String getCategory() {
        if (isIndoor) {
            return "Indoor Attraction";
        }
        return "Outdoor Attraction";
    }
 
    // --- Utility ---
 
    @Override
    public String toString() {
        return String.format("Attraction{name='%s', indoor=%b, rating=%.1f, price=%.2f, address=%s}",
                getName(), isIndoor, getRating(), getPrice(), getAddress());
    }
}