package com.tranner.Map;

//JavaConnector — bridge between JavaScript (Google Maps) and Java.

import netscape.javascript.JSObject;
public class JavaConnector {
    public interface MarkerClickListener {
        void onClicked(String name, String type);
    }
    private MarkerClickListener listener;
    /** Register a callback for when the user clicks a map marker. */
    public void setOnMarkerClicked(MarkerClickListener listener) {
        this.listener = listener;
    }
    /**
     * Called by JavaScript when the user clicks a marker.
     * JS calls: javaConnector.onMarkerClicked(name, type)
     */
    public void onMarkerClicked(String name, String type) {
        System.out.println("[JavaConnector] Marker clicked: " + name + " (" + type + ")");
        if (listener != null) {
            listener.onClicked(name, type);
        }
    }
}
