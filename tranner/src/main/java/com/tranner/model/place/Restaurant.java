package com.tranner.model.place;

/**
 * Represents a restaurant or dining establishment.
 * Extends Place with cuisine type information.
 */

public class Restaurant extends Place {
    private String cuisine;
 
    // --- Constructor ---
 
    /**
     * @param name        Display name of the restaurant
     * @param address     Physical address
     * @param description Short description or tagline
     * @param rating      Rating out of 5.0
     * @param price       Approximate cost per person in USD
     * @param cuisine     Cuisine type (free-text)
     */
    public Restaurant(String name, Address address, String description,
                      double rating, double price, String cuisine) {
        super(name, address, description, rating, price);
        this.cuisine = cuisine;
    }
 
    // --- Getters / Setters ---
 
    public String getCuisine() { return cuisine; }
 
    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }
 
    // --- Place Abstract Implementation ---
 
    @Override
    public String getCategory() { return "Restaurant"; }
 
    // --- Utility ---
 
    @Override
    public String toString() {
        return String.format("Restaurant{name='%s', cuisine='%s', rating=%.1f, price=%.2f, address=%s}",
                getName(), cuisine, getRating(), getPrice(), getAddress());
    }
}