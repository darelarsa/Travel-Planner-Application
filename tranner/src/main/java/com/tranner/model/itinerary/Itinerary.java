package com.tranner.model.itinerary;

import com.tranner.model.person.Person;
import com.tranner.model.place.Attraction;
import com.tranner.model.place.accommodation.Accommodation;
import com.tranner.model.place.Restaurant;
import com.tranner.model.weather.Weather;
 
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
 
/**
 * Represents a travel itinerary belonging to a specific user.
 *
 * An Itinerary holds:
 *   - The owning user's ID
 *   - A list of travel companions (Persons) on this trip
 *   - Lists of selected Attractions, Accommodations, and Restaurants
 *   - Trip date range (start and end date)
 *   - An optional Weather forecast for the destination
 */
public class Itinerary implements java.io.Serializable {
 
    // --- Static ID counter ---
    private static int nextId = 1;
 
    // --- Fields ---
 
    private final int itineraryID;
    private final int userID;               // Owner's UserID
 
    private String tripName;                // Optional label, e.g. "Paris Spring 2025"
    private LocalDate startDate;
    private LocalDate endDate;
 
    private final List<Person> companions;          // Travel buddies on this trip
    private final List<Attraction> attractions;     // Selected attractions
    private final List<Accommodation> accommodations; // Selected hotels/apartments
    private final List<Restaurant> restaurants;     // Selected restaurants
 
    private Weather weather;                // Forecast for the destination (nullable)
 
    // --- Constructor ---
 
    /**
     * Creates a new itinerary for the given user.
     *
     * @param userID    The UserID of the itinerary owner
     * @param tripName  A human-readable label for this trip (may be null)
     * @param startDate Trip start date
     * @param endDate   Trip end date
     */
    public Itinerary(int userID, String tripName, LocalDate startDate, LocalDate endDate) {
        if (userID <= 0)         throw new IllegalArgumentException("UserID must be positive.");
        if (startDate == null)   throw new IllegalArgumentException("Start date cannot be null.");
        if (endDate == null)     throw new IllegalArgumentException("End date cannot be null.");
        if (startDate.isAfter(endDate))
            throw new IllegalArgumentException("Start date cannot be after end date.");
 
        this.itineraryID    = nextId++;
        this.userID         = userID;
        this.tripName       = (tripName != null && !tripName.isBlank()) ? tripName : "My Trip";
        this.startDate      = startDate;
        this.endDate        = endDate;
 
        this.companions     = new ArrayList<>();
        this.attractions    = new ArrayList<>();
        this.accommodations = new ArrayList<>();
        this.restaurants    = new ArrayList<>();
    }
 
    /**
     * Minimal constructor — trip name defaults to "My Trip".
     */
    public Itinerary(int userID, LocalDate startDate, LocalDate endDate) {
        this(userID, null, startDate, endDate);
    }
 
    // --- Companion Management ---
 
    /**
     * Adds a travel companion to this itinerary.
     * Duplicates (by PersonID) are silently ignored.
     */
    public void addCompanion(Person person) {
        if (person == null) throw new IllegalArgumentException("Companion cannot be null.");
        if (companions.stream().noneMatch(p -> p.getPersonID() == person.getPersonID())) {
            companions.add(person);
        }
    }
 
    /**
     * Removes a companion by their PersonID.
     * @return true if the companion was found and removed
     */
    public boolean removeCompanion(int personID) {
        return companions.removeIf(p -> p.getPersonID() == personID);
    }
 
    // --- Attraction Management ---
 
    /**
     * Adds an attraction to the itinerary.
     * Duplicates (by name + address equality) are silently ignored.
     */
    public void addAttraction(Attraction attraction) {
        if (!attractions.contains(attraction))
            attractions.add(attraction);
    }
 
    /**
     * Removes an attraction by its name (case-insensitive).
     * @return true if at least one matching attraction was removed
     */
    public boolean removeAttraction(String name) {
        return attractions.removeIf(a -> a.getName().equalsIgnoreCase(name));
    }
 
    // --- Accommodation Management ---
 
    /**
     * Adds an accommodation to the itinerary.
     * Duplicates are silently ignored.
     */
    public void addAccommodation(Accommodation accommodation) {
        if (accommodation == null) throw new IllegalArgumentException("Accommodation cannot be null.");
        if (!accommodations.contains(accommodation)) accommodations.add(accommodation);
    }
 
    /**
     * Removes an accommodation by name (case-insensitive).
     * @return true if at least one match was removed
     */
    public boolean removeAccommodation(String name) {
        return accommodations.removeIf(a -> a.getName().equalsIgnoreCase(name));
    }
 
    // --- Restaurant Management ---
 
    /**
     * Adds a restaurant to the itinerary.
     * Duplicates are silently ignored.
     */
    public void addRestaurant(Restaurant restaurant) {
        if (restaurant == null) throw new IllegalArgumentException("Restaurant cannot be null.");
        if (!restaurants.contains(restaurant)) restaurants.add(restaurant);
    }
 
    /**
     * Removes a restaurant by name (case-insensitive).
     * @return true if at least one match was removed
     */
    public boolean removeRestaurant(String name) {
        return restaurants.removeIf(r -> r.getName().equalsIgnoreCase(name));
    }
 
    // --- Cost Estimation ---
 
    /**
     * Returns an approximate total trip cost by summing:
     *   - Attraction entry prices (per person, for all companions + user)
     *   - Accommodation nightly rates × number of trip days
     *   - Restaurant prices (per person, for all companions + user)
     *
     * Per project constraints, this is an approximation — not a precise budget.
     * Party size = companions + 1 (the user themselves).
     */
    public double getEstimatedCost() {
        int partySize  = companions.size() + 1;
        long tripDays  = startDate.until(endDate).getDays();
        if (tripDays == 0) tripDays = 1;    // Same-day trip counts as 1 day
 
        double total = 0.0;
 
        for (Attraction a : attractions) {
            total += a.getPrice() * partySize;
        }
        for (Accommodation acc : accommodations) {
            total += acc.getPrice() * tripDays;
        }
        for (Restaurant r : restaurants) {
            total += r.getPrice() * partySize;
        }
 
        return total;
    }
 
    /**
     * Returns the number of days in this trip.
     * A same-day trip returns 1.
     */
    public int getTripDurationDays() {
        int days = (int) startDate.until(endDate).getDays();
        return Math.max(days, 1);
    }
 
    /**
     * Returns total party size: 1 (the user) + all companions.
     */
    public int getPartySize() {
        return 1 + companions.size();
    }
 
    // --- Summary ---
 
    /**
     * Produces a formatted multi-line itinerary summary suitable for display
     * in the ItineraryPanel or for export.
     *
     * Per project constraints, time frames (not exact times) are shown.
     */
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append(String.format("  %s\n", tripName.toUpperCase()));
        sb.append("========================================\n");
        sb.append(String.format("Dates    : %s → %s (%d day(s))\n",
                startDate, endDate, getTripDurationDays()));
        sb.append(String.format("Party    : %d person(s)\n", getPartySize()));
 
        if (!companions.isEmpty()) {
            sb.append("Companions: ");
            companions.forEach(p -> sb.append(p.getFullName()).append("  "));
            sb.append("\n");
        }
 
        if (weather != null) {
            sb.append("\n[ WEATHER FORECAST ]\n");
            sb.append("  ").append(weather.getSummary()).append("\n");
            weather.getClothingSuggestions()
                   .forEach(s -> sb.append("  • ").append(s).append("\n"));
        }
 
        if (!accommodations.isEmpty()) {
            sb.append("\n[ WHERE YOU'RE STAYING ]\n");
            accommodations.forEach(a ->
                sb.append(String.format("  • %s (%s) — ~$%.0f/night\n",
                        a.getName(), a.getCategory(), a.getPrice())));
        }
 
        if (!attractions.isEmpty()) {
            sb.append("\n[ ATTRACTIONS ]\n");
            attractions.forEach(a ->
                sb.append(String.format("  • %s (%s) — ~$%.0f/person\n",
                        a.getName(), a.getCategory(), a.getPrice())));
        }
 
        if (!restaurants.isEmpty()) {
            sb.append("\n[ DINING ]\n");
            restaurants.forEach(r ->
                sb.append(String.format("  • %s (%s) — ~$%.0f/person\n",
                        r.getName(), r.getCuisine(), r.getPrice())));
        }
 
        sb.append(String.format("\nEstimated Total Cost : ~$%.2f\n", getEstimatedCost()));
        sb.append("========================================\n");
        return sb.toString();
    }
 
    // --- Getters ---
 
    public int getItineraryID()                   { return itineraryID; }
    public int getUserID()                        { return userID; }
    public String getTripName()                   { return tripName; }
    public LocalDate getStartDate()               { return startDate; }
    public LocalDate getEndDate()                 { return endDate; }
    public Weather getWeather()                   { return weather; }
 
    public List<Person> getCompanions() {
        return Collections.unmodifiableList(companions);
    }
    public List<Attraction> getAttractions() {
        return Collections.unmodifiableList(attractions);
    }
    public List<Accommodation> getAccommodations() {
        return Collections.unmodifiableList(accommodations);
    }
    public List<Restaurant> getRestaurants() {
        return Collections.unmodifiableList(restaurants);
    }
 
    // --- Setters ---
 
    public void setTripName(String tripName) {
        if (tripName == null || tripName.isBlank()) {
            this.tripName = "My Trip";
        } else {
            this.tripName = tripName;
        }
    }
 
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
 
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
 
    public void setWeather(Weather weather) { this.weather = weather; }
 
    // --- Utility ---
 
    /**
     * Resets the ID counter. For unit tests only — do not call in production.
     */
    static void resetIdCounter() { nextId = 1; }
 
    @Override
    public String toString() {
        return String.format("Itinerary{id=%d, userID=%d, trip='%s', dates=%s→%s, " +
                "attractions=%d, accommodations=%d, restaurants=%d}",
                itineraryID, userID, tripName, startDate, endDate,
                attractions.size(), accommodations.size(), restaurants.size());
    }
 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Itinerary)) return false;
        Itinerary other = (Itinerary) o;
        return this.itineraryID == other.itineraryID;
    }
}