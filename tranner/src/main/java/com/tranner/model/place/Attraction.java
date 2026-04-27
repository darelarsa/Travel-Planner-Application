package com.tranner.model.place;

import java.time.LocalDate;
 
/**
 * Represents a tourist attraction or point of interest.
 * Extends Place with an indoor/outdoor flag.
 */
public class Attraction extends Place { 
    //private boolean isIndoor;
 
    // --- Constructor ---
 
    /**
     * @param name        Display name of the attraction
     * @param address     Physical address
     * @param description Short description
     * @param rating      Rating out of 5.0
     * @param price       Approximate entry cost in USD
     */
    public Attraction(String name, Address address, String description,
                      double rating, double price) {
        super(name, address, description, rating, price);
        //this.isIndoor = isIndoor;
    }

    // --- Getters/Setters ---
 
//    public boolean isIndoor() {
//        return isIndoor;
//    }
 
//    public void setIndoor(boolean isIndoor) {
//        this.isIndoor = isIndoor;
//    }
 
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
        return "Attraction";
    }
 
    // --- Utility ---
 
    @Override
    public String toString() {
        return String.format("Attraction{name='%s', indoor=%b, rating=%.1f, price=%.2f, address=%s}",
                getName(), getRating(), getPrice(), getAddress());
    }

    // Creates an Attraction from a Google Places API JSONObject.
    public static Attraction fromGooglePlace(org.json.simple.JSONObject place) {
        // name
        org.json.simple.JSONObject displayName = (org.json.simple.JSONObject) place.get("displayName");
        String name = displayName != null ? displayName.get("text").toString() : "Unknown";

        // address
        Object addr = place.get("formattedAddress");
        Address address = new Address(addr != null ? addr.toString() : "");

        // description
        org.json.simple.JSONObject summary = (org.json.simple.JSONObject) place.get("editorialSummary");
        String description = summary != null ? summary.get("text").toString() : "";

        // rating
        Object r = place.get("rating");
        double rating = r != null ? ((Number) r).doubleValue() : 0.0;

        // price — map Google's enum to a rough USD value
        Object priceLevel = place.get("priceLevel");
        double price = parsePriceLevel(priceLevel != null ? priceLevel.toString() : null);

        // isIndoor — infer from types
        org.json.simple.JSONArray types = (org.json.simple.JSONArray) place.get("types");

        Attraction attraction = new Attraction(name, address, description, rating, price);
        // set lat/lng coordinates
        org.json.simple.JSONObject loc = (org.json.simple.JSONObject) place.get("location");
        if (loc != null) {
            attraction.setLatitude(((Number) loc.get("latitude")).doubleValue());
            attraction.setLongitude(((Number) loc.get("longitude")).doubleValue());
        }
        return attraction;
    }

    private static double parsePriceLevel(String level) {
        if (level == null) return 0.0;
        switch (level) {
            case "PRICE_LEVEL_FREE":           return 0.0;
            case "PRICE_LEVEL_INEXPENSIVE":    return 10.0;
            case "PRICE_LEVEL_MODERATE":       return 30.0;
            case "PRICE_LEVEL_EXPENSIVE":      return 60.0;
            case "PRICE_LEVEL_VERY_EXPENSIVE": return 100.0;
            default:                           return 0.0;
        }
    }

}