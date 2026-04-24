package com.tranner.model.place.accommodation;

import java.time.LocalDate;
import java.util.List;

import com.tranner.model.place.Address;
 
/**
 * Represents a hotel — a commercially operated accommodation with named services.
 * Extends Accommodation with a list of hotel-specific services.
 */
public class Hotel extends Accommodation {
 
    // --- Fields ---
 
    private String service;
 
    // --- Constructor ---
 
    /**
     * @param name         Hotel name
     * @param address      Physical address
     * @param description  Short description
     * @param rating       Rating out of 5.0
     * @param price        Approximate nightly rate per room in USD
     * @param amenities    List of amenities (e.g., ["WiFi", "Pool"])
     * @param roomCapacity Maximum number of guests across all rooms
     * @param service      Service tier or primary service descriptor
     */
    public Hotel(String name, Address address, String description,
                 double rating, double price,
                 List<String> amenities, int roomCapacity,
                 String service) {
        super(name, address, description, rating, price, amenities, roomCapacity);
        this.service = service;
    }
 
    // --- LocationInfo Override ---
 
    /**
     * Hotels may offer weekend or seasonal discounts.
     * Stub logic — replace with real data from Amadeus or HotelAPI.
     */
    @Override
    public String checkDiscounts(LocalDate date) {
        if (date == null) return null;
        return switch (date.getDayOfWeek()) {       // This is just an placeholder for now
            case SATURDAY, SUNDAY -> "Weekend rate: 15% off";
            default               -> null;
        };
    }
 
    // --- Getters / Setters ---
 
    public String getService() { return service; }
 
    public void setService(String service) {
        this.service = service;
    }
 
    // --- Place Abstract Implementation ---
 
    @Override
    public String getCategory() { return "Hotel"; }
 
    // --- Utility ---
 
    @Override
    public String toString() {
        return String.format("Hotel{name='%s', service='%s', capacity=%d, rating=%.1f, price=%.2f, address=%s}",
                getName(), service, getRoomCapacity(), getRating(), getPrice(), getAddress());
    }
}