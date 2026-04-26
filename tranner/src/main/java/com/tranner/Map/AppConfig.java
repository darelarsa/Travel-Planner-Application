package com.tranner.Map;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads API keys from config.properties (gitignored).
 * Protect API keys from being committed to the repository.

 */
public class AppConfig {

    private static final Properties props = new Properties();
    private static boolean loaded = false;

    private static void load() {
        if (loaded) return;
        try (InputStream in = AppConfig.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (in == null) {
                System.err.println("[AppConfig] config.properties not found in resources/.");
                System.err.println("[AppConfig] Copy config.properties.example -> config.properties and fill in your key.");
                return;
            }
            props.load(in);
            loaded = true;
        } catch (IOException e) {
            System.err.println("[AppConfig] Failed to load config.properties: " + e.getMessage());
        }
    }

    /** Returns the value for the given key, or null if not found. */
    public static String get(String key) {
        load();
        return props.getProperty(key);
    }
}
