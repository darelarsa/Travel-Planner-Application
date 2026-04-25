package com.tranner.model.person;

/**
 * Contains a person's travel preferences.
 *
 * Budget     — approximate spending level using the Budget enum.
 * Transport  — preferred mode(s) of travel using the TransportMode enum.
 * Intensity  — preferred pace of the trip using the Intensity enum.
 */

public class Preference {
 
    // --- Enums ---
 
    /**
     * Approximate daily spending level.
     * Matches the project constraint that budget results are approximations.
     */
    public enum Budget {        // Change them to numbers
        LOW,        // Hostels, street food, free attractions
        MODERATE,   // Mid-range hotels, casual dining, some paid attractions
        HIGH,       // Upscale hotels, fine dining, premium experiences
        LUXURY      // 5-star everything, private tours
    }
 
    /**
     * Preferred mode(s) of transportation during the trip.
     */
    public enum TransportMode {
        WALKING,
        PUBLIC_TRANSIT,
        RIDESHARE,
        RENTAL_CAR,
        BICYCLE
    }
 
    /**
     * Trip pace / activity intensity.
     * Affects how tightly packed the generated itinerary will be.
     */
    public enum Intensity {
        RELAXED,    // Few stops, plenty of downtime
        MODERATE,   // Balanced mix of activity and rest
        ACTIVE,     // Many stops, long days
        EXTREME     // Back-to-back activities from open to close
    }
 
    // --- Fields ---
 
    private Budget budget;
    private TransportMode transport;
    private Intensity intensity;
 
    // --- Constructors ---
 
    /**
     * Full constructor for when all preferences are known.
     */
    public Preference(Budget budget, String place, TransportMode transport, Intensity intensity) {
        this.budget    = budget;
        this.transport = transport;
        this.intensity = intensity;
    }
 
    /**
     * Default no-arg constructor — allows building a Preference incrementally.
     * All fields start as null and can be set via setters.
     */
    public Preference() {
        // Empty; fields default to null
    }
 
    // --- Getters ---
 
    public Budget getBudget() {
        return budget;
    }
 
    public TransportMode getTransport() {
        return transport;
    }
 
    public Intensity getIntensity() {
        return intensity;
    }
 
    // --- Setters ---
 
    public void setBudget(Budget budget) {
        this.budget = budget;
    }
 
    public void setTransport(TransportMode transport) {
        this.transport = transport;
    }
 
    public void setIntensity(Intensity intensity) {
        this.intensity = intensity;
    }
 
    // --- Utility ---
 
    /**
     * Returns true only if every preference field has been filled in.
     * Useful for validating before itinerary generation.
     */
    public boolean isComplete() {
        return budget != null && transport != null && intensity != null;
    }
 
    @Override
    public String toString() {
        return String.format("Preference{budget=%s, transport=%s, intensity=%s}",
                budget, transport, intensity);
    }
 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Preference)) return false;
        Preference other = (Preference) o;
        return java.util.Objects.equals(budget,    other.budget)
            && java.util.Objects.equals(transport, other.transport)
            && java.util.Objects.equals(intensity, other.intensity);
    }
}