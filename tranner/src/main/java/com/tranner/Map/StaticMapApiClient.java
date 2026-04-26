/**
 * MapClient — Google Maps Static API client.
 *
 * Generates a static map image with location markers.
 * The image can be displayed directly in a Swing JLabel.
 *
 * API used: Maps Static API
 * Endpoint: GET https://maps.googleapis.com/maps/api/staticmap
**/

package com.tranner.Map;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;


//Fetch the map
public class StaticMapApiClient {
    private static final String API_KEY = AppConfig.get("google.api.key");
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/staticmap";

    public static ImageIcon getMapImage(String center, int zoom, int width, int height,
                                        List<String> markers) {
        try {
            String urlString = buildMapUrl(center, zoom, width, height, markers);
            System.out.println("[MapClient] Fetching map: " + urlString);
            BufferedImage image = ImageIO.read(new URL(urlString));
            if (image == null) {
                System.err.println("[MapClient] Failed to read image.");
                return null;
            }
            return new ImageIcon(image);
        } catch (Exception e) {
            System.err.println("[MapClient] Error: " + e.getMessage());
            return null;
        }
    }

    //Add the complete URL
    public static String buildMapUrl(String center, int zoom, int width, int height,
                                     List<String> markers) {
        StringBuilder url = new StringBuilder(BASE_URL);
        url.append("?center=").append(center.replace(" ", "+"));
        url.append("&zoom=").append(zoom);
        url.append("&size=").append(width).append("x").append(height);
        url.append("&maptype=roadmap");
        for (String marker : markers) {
            url.append("&markers=color:red|").append(marker.replace(" ", "+"));
        }
        url.append("&key=").append(API_KEY);
        return url.toString();
    }

    public static void main(String[] args) {
        List<String> markers = List.of(
                "Millennium Park,Chicago",
                "Art Institute of Chicago",
                "Navy Pier,Chicago",
                "Willis Tower,Chicago"
        );
        // Print the URL first — paste into browser to preview
        System.out.println("Map URL: " + buildMapUrl("Chicago", 12, 600, 400, markers));
        // Fetch and show the image in a popup
        ImageIcon mapIcon = getMapImage("Chicago", 12, 600, 400, markers);
        if (mapIcon == null) {
            System.out.println("Failed. Check your API key.");
            return;
        }
        javax.swing.JFrame frame = new javax.swing.JFrame("Map Test");
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.add(new javax.swing.JLabel(mapIcon));
        frame.pack();
        frame.setVisible(true);
        System.out.println("Map loaded successfully!");
    }

}
