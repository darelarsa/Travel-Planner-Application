package com.tranner.model.weather;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
 
/**
 * Represents weather conditions for a specific destination and date.
 * Populated by WeatherService after calling the WeatherAPI.
 * Contains both raw weather data and derived clothing suggestions.
 */
public class Weather {
 
    // --- Enums ---
 
    /**
     * Broad weather condition categories mapped from WeatherAPI's "condition" field.
     * Used to drive clothing suggestions and outdoor suitability checks.
     */
    public enum Condition {
        CLEAR_SKY,
        PARTLY_CLOUDY,
        FOGGY,
        DRIZZLE,
        FREEZING_DRIZZLE,
        RAIN,
        FREEZING_RAIN,
        SNOW,
        SNOW_GRAINS,
        RAIN_SHOWERS,
        SNOW_SHOWERS,
        THUNDERSTORM,
        THUNDERSTORM_WITH_HAIL,
        UNKNOWN
    }
 
    // --- Fields ---
 
    private final String city;
    private final String country;
    private final LocalDate date;
 
    private double tempCelsius;
    private double tempFahrenheit;
    private double feelsLikeCelsius;
    private double feelsLikeFahrenheit;
 
    private int humidityPercent;        // 0 – 100
    private double windSpeedKph;
    private double precipitationMm;     // Rainfall/snowfall in mm
    private int uvIndex;                // 0 – 11+
 
    private Condition condition;
    private String conditionText;       // Raw description from WeatherAPI e.g. "Patchy rain nearby"
 
    private List<String> clothingSuggestions;
 
    // --- Constructor ---
 
    /**
     * Primary constructor — called by WeatherService after parsing the API response.
     *
     * @param city        Destination city name
     * @param country     Destination country name
     * @param date        The date this forecast applies to
     * @param condition   Mapped weather condition enum value
     * @param conditionText Raw condition string from WeatherAPI
     */
    public Weather(String city, String country, LocalDate date,
                   double tempCelsius, double tempFahrenheit,
                   double feelsLikeCelsius, double feelsLikeFahrenheit,
                   int humidityPercent, double windSpeedKph,
                   double precipitationMm, int uvIndex,
                   Condition condition, String conditionText) {
 
        this.city                 = city;
        this.country              = country;
        this.date                 = date;
        this.tempCelsius          = tempCelsius;
        this.tempFahrenheit       = tempFahrenheit;
        this.feelsLikeCelsius     = feelsLikeCelsius;
        this.feelsLikeFahrenheit  = feelsLikeFahrenheit;
        this.humidityPercent      = humidityPercent;
        this.windSpeedKph         = windSpeedKph;
        this.precipitationMm      = precipitationMm;
        this.uvIndex              = uvIndex;
        this.condition            = condition != null ? condition : Condition.UNKNOWN;
        this.conditionText        = conditionText;
        this.clothingSuggestions  = new ArrayList<>();
 
        // Auto-generate clothing suggestions on construction
        generateClothingSuggestions();
    }
 
    // --- Clothing Suggestion Logic ---
 
    /**
     * Derives clothing suggestions from the current weather fields.
     * Called automatically on construction and whenever weather fields are updated.
     *
     * This logic intentionally lives in the model (not the service) because
     * it is a pure derivation of the weather state — no external data needed.
     * WeatherService is responsible for fetching and building the Weather object;
     * what to wear given that weather is the Weather object's own concern.
     */
    private void generateClothingSuggestions() {
        clothingSuggestions.clear();

        // --- Temperature-based suggestions ---
        if (tempCelsius >= 30) {
            clothingSuggestions.add("Wear lightweight, breathable clothing.");
            clothingSuggestions.add("Stay hydrated and seek shade during peak hours.");
        } else if (tempCelsius >= 20) {
            clothingSuggestions.add("Light clothing recommended — t-shirt and shorts weather.");
        } else if (tempCelsius >= 10) {
            clothingSuggestions.add("Light layers recommended — a light jacket or cardigan.");
        } else if (tempCelsius >= 0) {
            clothingSuggestions.add("Warm clothing advised — jacket, scarf, and layers.");
        } else {
            clothingSuggestions.add("Very cold — heavy coat, gloves, and thermal layers essential.");
        }

        // --- Condition-based suggestions ---
        switch (condition) {
            case CLEAR_SKY ->
                clothingSuggestions.add("Clear skies — sunglasses and sunscreen recommended.");
            case PARTLY_CLOUDY ->
                clothingSuggestions.add("Partly cloudy — a light layer in case it cools down.");
            case FOGGY ->
                clothingSuggestions.add("Foggy conditions — wear bright or reflective clothing.");
            case DRIZZLE ->
                clothingSuggestions.add("Light drizzle expected — a water-resistant jacket should suffice.");
            case FREEZING_DRIZZLE ->
                clothingSuggestions.add("Freezing drizzle — waterproof boots and a warm coat advised. Watch for icy surfaces.");
            case RAIN ->
                clothingSuggestions.add("Rain expected — bring a waterproof jacket or umbrella.");
            case FREEZING_RAIN ->
                clothingSuggestions.add("Freezing rain — heavy waterproof gear essential. Extremely slippery conditions likely.");
            case SNOW ->
                clothingSuggestions.add("Snowfall expected — waterproof boots and a heavy coat advised.");
            case SNOW_GRAINS ->
                clothingSuggestions.add("Snow grains — waterproof footwear and warm layers recommended.");
            case RAIN_SHOWERS ->
                clothingSuggestions.add("Rain showers likely — keep an umbrella or rain jacket handy.");
            case SNOW_SHOWERS ->
                clothingSuggestions.add("Snow showers — waterproof outerwear and insulated boots recommended.");
            case THUNDERSTORM ->
                clothingSuggestions.add("Thunderstorm — consider staying indoors; pack full rain gear if going out.");
            case THUNDERSTORM_WITH_HAIL ->
                clothingSuggestions.add("Thunderstorm with hail — stay indoors if possible. Protective outerwear essential.");
            default ->
                clothingSuggestions.add("Check the detailed weather conditions and dress accordingly.");
        }

        // --- UV Index warnings ---
        if (uvIndex >= 8) {
            clothingSuggestions.add("Very high UV index — apply SPF 50+ sunscreen and wear a hat.");
        } else if (uvIndex >= 6) {
            clothingSuggestions.add("High UV index — sunscreen and a hat are advised.");
        }

        // --- Humidity suggestions ---
        if (humidityPercent >= 80) {
            clothingSuggestions.add("High humidity — moisture-wicking fabrics will be more comfortable.");
        }
    }
    // --- Outdoor Suitability ---
 
    /**
     * Returns true if conditions are broadly suitable for outdoor attractions.
     * Used by WeatherService to flag or deprioritize outdoor Attractions
     * when the weather is poor.
     *
     * Criteria: not stormy, not heavy rain (>5mm), wind under 50kph.
     */
    public boolean isGoodForOutdoor() {
        if (condition == Condition.THUNDERSTORM) return false;
        if (condition == Condition.THUNDERSTORM_WITH_HAIL) return false;
        if (condition == Condition.FREEZING_RAIN) return false;
        if ((condition == Condition.RAIN || condition == Condition.RAIN_SHOWERS) 
                && precipitationMm > 5.0) return false;
        if (windSpeedKph > 50.0) return false;
        return true;
    }
 
    /**
     * Returns a short human-readable weather summary for display in the GUI
     * or at the top of an itinerary.
     *
     * Example: "Paris, France — Apr 24 | Sunny, 22°C (72°F) | UV: 6 | Humidity: 55%"
     */
    public String getSummary() {
        return String.format("%s, %s — %s | %s, %.1f°C (%.1f°F) | UV: %d | Humidity: %d%%",
                city, country, date, conditionText != null ? conditionText : condition,
                tempCelsius, tempFahrenheit, uvIndex, humidityPercent);
    }
 
    // --- Getters ---
 
    public String getCity()                  { return city; }
    public String getCountry()               { return country; }
    public LocalDate getDate()               { return date; }
    public double getTempCelsius()           { return tempCelsius; }
    public double getTempFahrenheit()        { return tempFahrenheit; }
    public double getFeelsLikeCelsius()      { return feelsLikeCelsius; }
    public double getFeelsLikeFahrenheit()   { return feelsLikeFahrenheit; }
    public int getHumidityPercent()          { return humidityPercent; }
    public double getWindSpeedKph()          { return windSpeedKph; }
    public double getPrecipitationMm()       { return precipitationMm; }
    public int getUvIndex()                  { return uvIndex; }
    public Condition getCondition()          { return condition; }
    public String getConditionText()         { return conditionText; }
 
    /**
     * Returns an unmodifiable view of clothing suggestions.
     * To regenerate, update weather fields and call refreshSuggestions().
     */
    public List<String> getClothingSuggestions() {
        return Collections.unmodifiableList(clothingSuggestions);
    }
 
    // --- Setters (for partial updates from API) ---
 
    public void setTempCelsius(double tempCelsius)         { this.tempCelsius = tempCelsius; }
    public void setTempFahrenheit(double tempFahrenheit)   { this.tempFahrenheit = tempFahrenheit; }
    public void setFeelsLikeCelsius(double v)              { this.feelsLikeCelsius = v; }
    public void setFeelsLikeFahrenheit(double v)           { this.feelsLikeFahrenheit = v; }
    public void setHumidityPercent(int humidityPercent)    { this.humidityPercent = humidityPercent; }
    public void setWindSpeedKph(double windSpeedKph)       { this.windSpeedKph = windSpeedKph; }
    public void setPrecipitationMm(double precipitationMm) { this.precipitationMm = precipitationMm; }
    public void setUvIndex(int uvIndex)                    { this.uvIndex = uvIndex; }
    public void setCondition(Condition condition)          { this.condition = condition; }
    public void setConditionText(String conditionText)     { this.conditionText = conditionText; }
 
    /**
     * Re-runs clothing suggestion generation after manual field updates.
     * Call this if any weather field is changed via a setter after construction.
     */
    public void refreshSuggestions() {
        generateClothingSuggestions();
    }
 
    // --- Utility ---
 
    @Override
    public String toString() {
        return getSummary();
    }
}