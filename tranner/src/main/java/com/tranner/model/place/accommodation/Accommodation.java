package com.tranner.model.place.accommodation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tranner.model.place.*;
 
/**
 * Abstract intermediate class for all lodging options.
 * Extends Place and adds amenities and room capacity — fields shared
 * by both Hotel and Apartment.
 *
 * Subclasses: Hotel, Apartment
 *
 * bookLocation() is overridden here to enforce capacity checks,
 * since all accommodations have a meaningful room/guest limit.
 */
public abstract class Accommodation extends Place {
 
    // --- Fields ---
 
    private List<String> amenities;
    private int roomCapacity;
 
    // --- Constructor ---
 
    /**
     * @param name         Display name of the accommodation
     * @param address      Physical address
     * @param description  Short description
     * @param rating       Rating out of 5.0
     * @param price        Approximate nightly rate in USD
     * @param amenities    List of available amenities (may be empty, not null)
     * @param roomCapacity Maximum guest capacity
     */
    public Accommodation(String name, Address address, String description,
                         double rating, double price,
                         List<String> amenities, int roomCapacity) {
        super(name, address, description, rating, price);
        this.amenities    = amenities;
        this.roomCapacity = roomCapacity;
    }
 
    // --- LocationInfo Override ---
 
    /**
     * Overrides Place's bookLocation to also enforce room capacity.
     * Accommodations are assumed to have no strict hourly hours (check-in windows
     * are handled separately), so the hours check from Place is relaxed here —
     * only date range and party size are validated.
     *
     * @return true if the party fits within room capacity and dates are valid
     */
    @Override
    public boolean bookLocation(LocalDate startDate, LocalDate endDate,
                                LocalTime startTime, LocalTime endTime,
                                int numPersons) {
        if (startDate == null || endDate == null) return false;
        if (startDate.isAfter(endDate))           return false;
        if (numPersons <= 0)                      return false;
        if (numPersons > roomCapacity)            return false;
        return true;
    }
 
    // --- Amenity Management ---
 
    /**
     * Adds a single amenity if not already present.
     * @param amenity Amenity string (e.g., "Pool")
     */
    public void addAmenity(String amenity) {
        if (amenity == null || amenity.isBlank()) return;
        if (!amenities.contains(amenity)) amenities.add(amenity);
    }
 
    /**
     * Removes a single amenity by name.
     * @return true if the amenity was found and removed
     */
    public boolean removeAmenity(String amenity) {
        return amenities.remove(amenity);
    }
 
    /**
     * Returns true if this accommodation has a specific amenity.
     * Case-sensitive comparison.
     */
    public boolean hasAmenity(String amenity) {
        return amenities.contains(amenity);
    }
 
    // --- Getters / Setters ---
 
    public List<String> getAmenities() {
        return Collections.unmodifiableList(amenities);
    }
 
    public void setAmenities(List<String> amenities) {
        this.amenities = amenities != null ? new ArrayList<>(amenities) : new ArrayList<>();
    }
 
    public int getRoomCapacity() { return roomCapacity; }
 
    public void setRoomCapacity(int roomCapacity) {
        if (roomCapacity <= 0) throw new IllegalArgumentException("Room capacity must be greater than 0.");
        this.roomCapacity = roomCapacity;
    }
 
    // --- Utility ---
 
    @Override
    public String toString() {
        return String.format("%s{name='%s', capacity=%d, amenities=%s, rating=%.1f, price=%.2f}",
                getCategory(), getName(), roomCapacity, amenities, getRating(), getPrice());
    }
}