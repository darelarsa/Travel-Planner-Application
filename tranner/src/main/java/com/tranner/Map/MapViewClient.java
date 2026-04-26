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

        String html = loadHtmlWithKey();
        engine.loadContent(html);
        fxPanel.setScene(new Scene(view));
    }

    private String loadHtmlWithKey() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("map.html")) {
            if (in == null) {
                System.err.println("[MapWebView] map.html not found in resources.");
                return "<html><body>map.html not found</body></html>";
            }
            String html = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            String apiKey = AppConfig.get("google.api.key");
            // Replace the empty script tag with the real Google Maps JS API loader
            String scriptTag = "<script src=\"https://maps.googleapis.com/maps/api/js"
                    + "?key=" + apiKey
                    + "&callback=initMap\" async defer></script>";
            return html.replace("<script id=\"gmaps-script\"></script>", scriptTag);
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
            }
        });
    }

    public static void main(String[] args) {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        // Must use SwingUtilities for Swing components
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("MapWebView Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            MapViewClient mapView = new MapViewClient();
            // Register click callback — prints to console when you click a marker
            mapView.getConnector().setOnMarkerClicked((name, type) -> {
                System.out.println("[Test] Marker clicked: " + name + " (" + type + ")");
            });
            frame.add(mapView, BorderLayout.CENTER);
            frame.setVisible(true);
            // Wait 3 seconds for the map to fully load, then add test markers
            // (map.html needs time to load Google Maps JS from the internet)
            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ignored) {}
                System.out.println("[Test] Adding test markers...");
                mapView.setCity(41.8781, -87.6298, 12);
                mapView.addMarker(41.8827, -87.6233, "Millennium Park", "attraction");
                mapView.addMarker(41.8796, -87.6237, "Art Institute of Chicago", "attraction");
                mapView.addMarker(41.8917, -87.6086, "Navy Pier", "attraction");
                mapView.addMarker(41.8788, -87.6359, "Marriott Chicago", "hotel");
                mapView.addMarker(41.8827, -87.6318, "Lou Malnati's", "restaurant");
                System.out.println("[Test] Done. Click a marker on the map to test the callback.");
            }).start();
        });
    }


}
