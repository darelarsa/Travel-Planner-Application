package com.tranner.api;

import com.tranner.model.weather.Weather;

public class WeatherConditionMapper {

    public static Weather.Condition mapCondition(String conditionText) {
        if (conditionText == null) return Weather.Condition.UNKNOWN;

        switch (conditionText) {
            case "Clear sky" ->
                    { return Weather.Condition.CLEAR_SKY; }
            case "Mainly clear, partly cloudy, and overcast" ->
                    { return Weather.Condition.PARTLY_CLOUDY; }
            case "Fog and depositing rime fog" ->
                    { return Weather.Condition.FOGGY; }
            case "Drizzle: Light, moderate, and dense intensity" ->
                    { return Weather.Condition.DRIZZLE; }
            case "Freezing Drizzle: Light and dense intensity" ->
                    { return Weather.Condition.FREEZING_DRIZZLE; }
            case "Rain: Slight, moderate and heavy intensity" ->
                    { return Weather.Condition.RAIN; }
            case "Freezing Rain: Light and heavy intensity" ->
                    { return Weather.Condition.FREEZING_RAIN; }
            case "Snow fall: Slight, moderate, and heavy intensity" ->
                    { return Weather.Condition.SNOW; }
            case "Snow grains" ->
                    { return Weather.Condition.SNOW_GRAINS; }
            case "Rain showers: Slight, moderate, and violent" ->
                    { return Weather.Condition.RAIN_SHOWERS; }
            case "Snow showers slight and heavy" ->
                    { return Weather.Condition.SNOW_SHOWERS; }
            case "Thunderstorm: Slight or moderate" ->
                    { return Weather.Condition.THUNDERSTORM; }
            case "Thunderstorm with slight and heavy hail" ->
                    { return Weather.Condition.THUNDERSTORM_WITH_HAIL; }
            default ->
                    { return Weather.Condition.UNKNOWN; }
        }
    }
}