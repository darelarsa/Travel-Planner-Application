//PlacesApiClient — Google Places API (New) client.
// *
// * Searches for attractions, restaurants, and hotels in a given city
// * using Google's Text Search endpoint. Returns raw JSON results that
// * can be converted into Place model objects (Attraction, Restaurant, Hotel).
package com.tranner.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class PlacesApiClient {
    private static final String PLACES_URL =
            "https://places.googleapis.com/v1/places:searchText";

    private static final String FIELD_MASK =
            "places.displayName,places.formattedAddress,places.rating," +
                    "places.priceLevel,places.types,places.editorialSummary,places.location";

    private static final String API_KEY = AppConfig.get("google.api.key");

    public static List<JSONObject> searchAttraction(String city ){
        return search ("tourist attractions in " + city);
    }
    public static List<JSONObject> searchRestaurants(String city ){
        return search ("popular restaurants in " + city);
    }
    public static List<JSONObject> searchHotels(String city) {
        return search("hotels in " + city);
    }

    private static List<JSONObject> search (String query){
        List<JSONObject> results = new ArrayList<>();

        //connect with google map api
        try{
            URL url = new URL(PLACES_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("X-Goog-Api-Key", API_KEY);
            conn.setRequestProperty("X-Goog-FieldMask", FIELD_MASK);

            String body = "{\"textQuery\":\"" + query + "\",\"maxResultCount\":10}";
            try{
                OutputStream os = conn.getOutputStream();
                os.write(body.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            int status = conn.getResponseCode();
            if(status != 200){
                System.err.println("[MapClient] API error: HTTP " + conn.getResponseCode());
                try {
                    Scanner errorScanner = new Scanner(conn.getErrorStream());
                    StringBuilder errorBody = new StringBuilder();
                    while (errorScanner.hasNextLine()) {
                        errorBody.append(errorScanner.nextLine());
                    }
                    System.err.println("[MapClient] Error detail: " + errorBody);
                } catch (Exception ignored) {}
                return results;

            }

            //Read response
            StringBuilder response = new StringBuilder();
            Scanner in = new Scanner(conn.getInputStream());
            while(in.hasNextLine()){
                response.append(in.nextLine());
            }
            in.close();
            String responseString = response.toString();
            System.out.println("[MapClient] API response: " + responseString);

            try {
                JSONParser parser = new JSONParser();
                JSONObject root = (JSONObject) parser.parse(responseString);
                JSONArray places = (JSONArray) root.get("places");


                if (places == null) {
                    System.out.println("[MapClient] No places found.");
                    return results;
                }
                for (Object obj : places) {
                    results.add((JSONObject) obj);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return results;

    }

    public static String getName(JSONObject place) {
        JSONObject displayName = (JSONObject) place.get("displayName");
        if (displayName == null) return "Unknown";
        Object text = displayName.get("text");
        return text != null ? text.toString() : "Unknown";
    }

    public static String getAddress(JSONObject place) {
        Object addr = place.get("formattedAddress");
        return addr != null ? addr.toString() : "";
    }

    public static double getRating(JSONObject place) {
        Object r = place.get("rating");
        if (r == null) return 0.0;
        return ((Number) r).doubleValue();
    }

    public static String getDescription(JSONObject place) {
        JSONObject summary = (JSONObject) place.get("editorialSummary");
        if (summary == null) return "";
        Object text = summary.get("text");
        return text != null ? text.toString() : "";
    }

    //get latitude and longitude of the place
    public static double getLat(JSONObject place) {
        JSONObject location = (JSONObject) place.get("location");
        if (location == null) return 0.0;
        return ((Number) location.get("latitude")).doubleValue();
    }

    public static double getLng(JSONObject place) {
        JSONObject location = (JSONObject) place.get("location");
        if (location == null) return 0.0;
        return ((Number) location.get("longitude")).doubleValue();
    }

    public static void main(String[] args) {
        System.out.println("=== ATTRACTIONS ===");
        List<JSONObject> attractions = searchAttraction("Chicago");
        if (attractions.isEmpty()) {
            System.out.println("No attractions found.");
        } else {
            for (JSONObject p : attractions) {
                System.out.println(getName(p) + " | lat=" + getLat(p) + " lng=" + getLng(p)
                        + " | rating=" + getRating(p));
            }
        }

        System.out.println("\n=== RESTAURANTS ===");
        List<JSONObject> restaurants = searchRestaurants("Chicago");
        if (restaurants.isEmpty()) {
            System.out.println("No restaurants found.");
        } else {
            for (JSONObject p : restaurants) {
                System.out.println(getName(p) + " | lat=" + getLat(p) + " lng=" + getLng(p)
                        + " | rating=" + getRating(p));
            }
        }

        System.out.println("\n=== HOTELS ===");
        List<JSONObject> hotels = searchHotels("Chicago");
        if (hotels.isEmpty()) {
            System.out.println("No hotels found.");
        } else {
            for (JSONObject p : hotels) {
                System.out.println(getName(p) + " | lat=" + getLat(p) + " lng=" + getLng(p)
                        + " | rating=" + getRating(p));
            }
        }
    }

}
