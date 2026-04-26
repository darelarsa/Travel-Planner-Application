package com.tranner.api;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.tranner.model.weather.Weather;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherApiClient {

    private static String time;
    private static double temperature;
    private static double feelsLikeTemperature;
    private static int relativeHumidity;
    private static double windSpeed;
    private static int uvIndex;
    private static double precipitation;
    private static int condition_code;
    private static String condition;
    private static Weather.Condition mappedCondition;;

    public void retrieveWeatherInfo(String city){
        try{
            // Get location data
            JSONObject cityLocationData = (JSONObject) getLocationData(city);
            double latitude = (double) cityLocationData.get("latitude");
            double longitude = (double) cityLocationData.get("longitude");

            getWeatherData(latitude, longitude);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static JSONObject getLocationData(String city){
        city = city.replaceAll(" ", "+");

        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                city + "&count=1&language=en&format=json";

        try{
            // 1. Fetch the API response based on API Link
            HttpURLConnection apiConnection = fetchApiResponse(urlString);

            // check for response status
            // 200 - means that the connection was a success
            if(apiConnection.getResponseCode() != 200){
                System.out.println("Error: Could not connect to API");
                return null;
            }

            // 2. Read the response and convert store String type
            String jsonResponse = readApiResponse(apiConnection);

            // 3. Parse the string into a JSON Object
            JSONParser parser = new JSONParser();
            JSONObject resultsJsonObj = (JSONObject) parser.parse(jsonResponse);

            // 4. Retrieve Location Data
            JSONArray locationData = (JSONArray) resultsJsonObj.get("results");
            return (JSONObject) locationData.get(0);

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static void getWeatherData(double latitude, double longitude){
        try{
            // 1. Fetch the API response based on API Link
            String url = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude +
                    "&longitude=" + longitude + "&current=temperature_2m,relative_humidity_2m,wind_speed_10m" +
                    "apparent_temperature,precipitation,weather_code" +
                    "&daily=uv_index_max&forecast_days=1";
            HttpURLConnection apiConnection = fetchApiResponse(url);

            // check for response status
            // 200 - means that the connection was a success
            if(apiConnection.getResponseCode() != 200){
                System.out.println("Error: Could not connect to API");
                return;
            }

            // 2. Read the response and convert store String type
            String jsonResponse = readApiResponse(apiConnection);

            // 3. Parse the string into a JSON Object
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse);
            JSONObject currentWeatherJson = (JSONObject) jsonObject.get("current");
            //System.out.println(currentWeatherJson.toJSONString());

            // 4. Store the data into their corresponding data type
            time = (String) currentWeatherJson.get("time");
            //System.out.println("Current Time: " + time);
            temperature = (double) currentWeatherJson.get("temperature_2m");
            //System.out.println("Current Temperature (C): " + temperature);
            relativeHumidity = (int) currentWeatherJson.get("relative_humidity_2m");
            //System.out.println("Relative Humidity: " + relativeHumidity);
            windSpeed = (double) currentWeatherJson.get("wind_speed_10m");
            //System.out.println("Weather Description: " + windSpeed);
            uvIndex = (int) currentWeatherJson.get("uv_index_max");
            feelsLikeTemperature = (double) currentWeatherJson.get("apparent_temperature");
            precipitation = (double) currentWeatherJson.get("precipitation");
            condition_code = (int) currentWeatherJson.get("weather_code");

            // Convert weather code to condition string
            if (condition_code == 0) {
                condition = "Clear sky";
            } else if (condition_code <= 3) {
                condition = "Mainly clear, partly cloudy, and overcast";
            } else if (condition_code == 45 || condition_code == 48) {
                condition = "Fog and depositing rime fog";
            } else if (condition_code == 51 || condition_code == 53 || condition_code == 55) {
                condition = "Drizzle: Light, moderate, and dense intensity";
            } else if (condition_code == 56 || condition_code == 57) {
                condition = "Freezing Drizzle: Light and dense intensity";
            } else if (condition_code == 61 || condition_code == 63 || condition_code == 65) {
                condition = "Rain: Slight, moderate and heavy intensity";
            } else if (condition_code == 66 || condition_code == 67) {
                condition = "Freezing Rain: Light and heavy intensity";
            } else if (condition_code == 71 || condition_code == 73 || condition_code == 75) {
                condition = "Snow fall: Slight, moderate, and heavy intensity";
            } else if (condition_code == 77) {
                condition = "Snow grains";
            } else if (condition_code == 80 || condition_code == 81 || condition_code == 82) {
                condition = "Rain showers: Slight, moderate, and violent";
            } else if (condition_code == 85 || condition_code == 86) {
                condition = "Snow showers slight and heavy";
            } else if (condition_code == 95) {
                condition = "Thunderstorm: Slight or moderate";
            } else if (condition_code == 96 || condition_code == 99) {
                condition = "Thunderstorm with slight and heavy hail";
            } else {
                condition = "Unknown condition";
            }

            mappedCondition = WeatherConditionMapper.mapCondition(condition);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static String readApiResponse(HttpURLConnection apiConnection) {
        try {
            // Create a StringBuilder to store the resulting JSON data
            StringBuilder resultJson = new StringBuilder();

            // Create a Scanner to read from the InputStream of the HttpURLConnection
            Scanner scanner = new Scanner(apiConnection.getInputStream());

            // Loop through each line in the response and append it to the StringBuilder
            while (scanner.hasNext()) {
                // Read and append the current line to the StringBuilder
                resultJson.append(scanner.nextLine());
            }

            // Close the Scanner to release resources associated with it
            scanner.close();

            // Return the JSON data as a String
            return resultJson.toString();

        } catch (IOException e) {
            // Print the exception details in case of an IOException
            e.printStackTrace();
        }

        // Return null if there was an issue reading the response
        return null;
    }

    private static HttpURLConnection fetchApiResponse(String urlString){
        try{
            // attempt to create connection
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // set request method to get
            conn.setRequestMethod("GET");

            return conn;
        }catch(IOException e){
            e.printStackTrace();
        }

        // could not make connection
        return null;
    }

    public static String getTime(){
        return time;
    }

    public static double getTemperature(){
        return temperature;
    }

    public static long getRelativeHumidity(){
        return relativeHumidity;
    }

    public static double getWindSpeed(){
        return windSpeed;
    }

    public static double getUvIndex(){
        return uvIndex;
    }

    public static double getFeelsLikeTemperature(){
        return feelsLikeTemperature;
    }

    public static double getPrecipitation(){
        return precipitation;
    }

    public static String getCondition(){
        return condition;
    }

    public static Weather.Condition getMappedCondition(){
        return mappedCondition;
    }
}
