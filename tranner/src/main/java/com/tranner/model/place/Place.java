package com.tranner.model.place;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Abstract base class for all visitable locations in the travel planner.
 * Implements LocationInfo to provide a default contract for booking,
 * hours management, and discount checking.
 *
 * Subclasses: Attraction, Restaurant, Accommodation (→ Hotel, Apartment)
 */
public abstract class Place implements LocationInfo, Serializable {

    // --- Fields ---

    private String name;
    private Address address;
    private String description;
    private double rating;              // 0.0 – 5.0
    private double price;               // Approximate cost in USD
    private List<LocalTime> operationalHours;   // Ordered open/close time pairs

    // --- Constructor ---

    /**
     * @param name        Display name of the location
     * @param address     Physical address
     * @param description Short description or summary
     * @param rating      Rating out of 5.0
     * @param price       Approximate entry/base cost in USD
     */
    public Place(String name, Address address, String description, double rating, double price) {
        this.name             = name;
        this.address          = address;
        this.description      = description;
        this.rating           = rating;
        this.price            = price;
        this.operationalHours = new ArrayList<>();
    }

    /**
     * Replaces the current operational hours list.
     * Hours should be provided as sequential open/close pairs:
     *   index 0 = open time, index 1 = close time, index 2 = next open, etc.
     * An even-sized list is expected; odd-sized lists are accepted but flagged.
     */
    @Override
    public void setOperationalHours(List<LocalTime> hours) {
        if (hours == null) throw new IllegalArgumentException("Hours list cannot be null.");
        this.operationalHours = new ArrayList<>(hours);
    }

    /**
     * Default booking implementation — checks that the location is open
     * during the requested window and that the party size is positive.
     *
     * @return true if the time window overlaps with any operational hours window
     */
    @Override
    public boolean bookLocation(LocalDate startDate, LocalDate endDate,
                                LocalTime startTime, LocalTime endTime,
                                int numPersons) {
        // If no hours defined, assume always open (e.g., outdoor attractions)
        if (operationalHours.isEmpty()) return true;

        // Walk through open/close pairs to check if startTime falls within any window
        for (int i = 0; i + 1 < operationalHours.size(); i += 2) {
            LocalTime open  = operationalHours.get(i);
            LocalTime close = operationalHours.get(i + 1);
            if (!startTime.isBefore(open) && !endTime.isAfter(close)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Default discount check — returns null (no discount) by default.
     */
    @Override
    public String checkDiscounts(LocalDate date) {
        return null;    // No discount by default
    }

    // --- Getters ---

    public String getName()        { return name; }
    public Address getAddress()    { return address; }
    public String getDescription() { return description; }
    public double getRating()      { return rating; }
    public double getPrice()       { return price; }

    /**
     * Returns an unmodifiable view of the operational hours list.
     */
    public List<LocalTime> getOperationalHours() {
        return Collections.unmodifiableList(operationalHours);
    }

    /**
     * Convenience method: returns the city this place is located in.
     * Used by SearchService when filtering places by destination.
     */
    public String getCity() {
        return address.getCity();
    }

    /**
     * Convenience method: returns the country this place is located in.
     */
    public String getCountry() {
        return address.getCountry();
    }

    // --- Setters ---

    public void setName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be blank.");
        this.name = name;
    }

    public void setAddress(Address address) {
        if (address == null) throw new IllegalArgumentException("Address cannot be null.");
        this.address = address;
    }

    public void setDescription(String description) { this.description = description; }

    public void setRating(double rating) {
        if (rating < 0.0 || rating > 5.0) throw new IllegalArgumentException("Rating must be between 0.0 and 5.0.");
        this.rating = rating;
    }

    public void setPrice(double price) {
        if (price < 0.0) throw new IllegalArgumentException("Price cannot be negative.");
        this.price = price;
    }

    // --- Abstract hook ---

    /**
     * Subclasses return a short category label used in itinerary display.
     * Examples: "Attraction", "Restaurant", "Hotel", "Apartment"
     */
    public abstract String getCategory();

    // --- Utility ---

    @Override
    public String toString() {
        return String.format("%s{name='%s', address=%s, rating=%.1f, price=%.2f}",
                getCategory(), name, address.getFormattedAddress(), rating, price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Place)) return false;
        Place other = (Place) o;
        return Objects.equals(name, other.name)
            && Objects.equals(address, other.address);
    }
}