package com.tranner.Map;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import javax.swing.*;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.awt.BorderLayout;  // 只导入你需要的
import java.util.List;

public class MapViewClient extends JPanel{
    private JFXPanel fxPanel;
    private WebEngine engine;
    private JavaConnector connector;
    private boolean mapReady = false;

    public MapViewClient() {
        setLayout(new BorderLayout());
        fxPanel = new JFXPanel();
        add(fxPanel, BorderLayout.CENTER);
        connector = new JavaConnector();
        Platform.runLater(this::iniWebView);
    }

    private void iniWebView(){
        WebView view = new WebView();
        view.getEngine().setUserAgent( //Version of JavaFx Webview is too old
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "Chrome/120.0.0.0 Safari/537.36"
        );
        engine = view.getEngine();
        //inject JavaConnector
        engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) engine.executeScript("window");
                window.setMember("javaConnector", connector);
                mapReady = true;
                System.out.println("[MapWebView] Map ready.");
            }
        });

        String html = loadHtml();
        engine.loadContent(html);
        fxPanel.setScene(new Scene(view));
    }

    private String loadHtml() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("map.html")) {
            if (in == null) {
                System.err.println("[MapWebView] map.html not found in resources.");
                return "<html><body>map.html not found</body></html>";
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("[MapWebView] Error loading map.html: " + e.getMessage());
            return "<html><body>Error loading map</body></html>";
        }
    }

    public void setCity(double lat, double lng, int zoom) {
        runJs("setCity(" + lat + "," + lng + "," + zoom + ")");
    }
    public void addMarker(double lat, double lng, String name, String type) {
        String safeName = name.replace("'", "\\'");
        runJs("addMarker(" + lat + "," + lng + ",'" + safeName + "','" + type + "')");
    }
    public void clearMarkers() {
        runJs("clearMarkers()");
    }
    public void refresh() {
        runJs("refreshMap()");
    }
    public void drawRoute(List<String> points) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < points.size(); i++) {
            String[] parts = points.get(i).split(",");
            json.append("{lat:").append(parts[0].trim())
                    .append(",lng:").append(parts[1].trim()).append("}");
            if (i < points.size() - 1) json.append(",");
        }
        json.append("]");
        runJs("drawRoute('" + json + "')");
    }
    public JavaConnector getConnector() {
        return connector;
    }
    private void runJs(String script) {
        Platform.runLater(() -> {
            if (engine != null && mapReady) {
                engine.executeScript(script);
            }else{
                System.out.println("[runJs] SKIPPED (mapReady=" + mapReady + ")");
            }
        });
    }

    public static void main(String[] args) {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");

        // Fetch real data from Places API before opening the window
        System.out.println("[Test] Fetching places from API...");
        List<com.tranner.model.place.Attraction> attractions = PlacesApiClient.searchAttraction("Chicago");
        List<org.json.simple.JSONObject> hotels      = PlacesApiClient.searchHotels("Chicago");
        List<org.json.simple.JSONObject> restaurants = PlacesApiClient.searchRestaurants("Chicago");
        System.out.println("[Test] Got " + attractions.size() + " attractions, "
                + hotels.size() + " hotels, " + restaurants.size() + " restaurants.");

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("MapViewClient Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);

            MapViewClient mapView = new MapViewClient();

            mapView.getConnector().setOnMarkerClicked((name, type) -> {
                System.out.println("[Test] Marker clicked: " + name + " (" + type + ")");
            });

            frame.add(mapView, BorderLayout.CENTER);
            frame.setVisible(true);

            // Wait for Leaflet map to finish loading, then add markers from API
            new Thread(() -> {
                try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

                System.out.println("[Test] Adding markers from Places API...");
                mapView.setCity(41.8781, -87.6298, 12);

                for (com.tranner.model.place.Attraction a : attractions) {
                    mapView.addMarker(a.getLatitude(), a.getLongitude(),
                            a.getName(), "attraction");
                }
                for (org.json.simple.JSONObject p : hotels) {
                    mapView.addMarker(PlacesApiClient.getLat(p), PlacesApiClient.getLng(p),
                            PlacesApiClient.getName(p), "hotel");
                }
                for (org.json.simple.JSONObject p : restaurants) {
                    mapView.addMarker(PlacesApiClient.getLat(p), PlacesApiClient.getLng(p),
                            PlacesApiClient.getName(p), "restaurant");
                }

                System.out.println("[Test] Done. Click a marker to test the callback.");
            }).start();
        });
    }


}
