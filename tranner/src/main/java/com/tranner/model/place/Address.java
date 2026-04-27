package com.tranner.model.place;

import java.util.Objects;

/**
 * Object representing a physical address.
 */
public class Address implements java.io.Serializable {

    // --- Fields ---

    private String street;
    private String city;
    private String state;       // Can hold province/region for international addresses
    private String zipCode;
    private String country;

    // --- Constructor ---

    /**
     * @param street  Street number and name
     * @param city    City name
     * @param state   State, province, or region
     * @param zipCode Postal/zip code
     * @param country Country name
     */
    public Address(String street, String city, String state, String zipCode, String country) {

        this.street  = street;
        this.city    = city;
        this.state   = state;     // Optional — null is acceptable
        this.zipCode = zipCode;   // Optional — null is acceptable
        this.country = country;
    }

    /**
     * Minimal constructor for international addresses that may lack a state/zipcode.
     */
    public Address(String street, String city, String country) {
        this(street, city, null, null, country);
    }

    // --- Getters ---

    public String getStreet()  { return street; }
    public String getCity()    { return city; }
    public String getState()   { return state; }
    public String getZipCode() { return zipCode; }
    public String getCountry() { return country; }

    /**
     * Formats the address into a single human-readable string.
     * Omits null/blank optional fields gracefully.
     *
     * Example outputs:
     *   "123 Main St, Springfield, IL 62701, US"
     *   "10 Downing St, London, UK"
     */
    public String getFormattedAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append(street).append(", ").append(city);
        if (state   != null && !state.isBlank())   sb.append(", ").append(state);
        if (zipCode != null && !zipCode.isBlank())  sb.append(" ").append(zipCode);
        sb.append(", ").append(country);
        return sb.toString();
    }

    // --- Utility ---

    @Override
    public String toString() {
        return getFormattedAddress();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address other = (Address) o;
        return Objects.equals(street,  other.street)
            && Objects.equals(city,    other.city)
            && Objects.equals(state,   other.state)
            && Objects.equals(zipCode, other.zipCode)
            && Objects.equals(country, other.country);
    }
}