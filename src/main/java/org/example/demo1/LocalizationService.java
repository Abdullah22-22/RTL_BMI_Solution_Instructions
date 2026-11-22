package org.example.demo1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocalizationService {

    private static final Logger LOGGER = Logger.getLogger(LocalizationService.class.getName());

    private static final String DB_NAME =
            System.getenv().getOrDefault("DB_NAME", "bmi_localization");

    private static final String DB_USER =
            System.getenv().getOrDefault("DB_USER", "root");

    private static final String DB_PASSWORD =
            System.getenv().getOrDefault("DB_PASSWORD", "Test12");

    private LocalizationService() {
        // utility class, no instances
    }

    private static String getDatabaseHost() {
        String host = System.getenv("DB_HOST");
        if (host == null || host.isEmpty()) {
            host = "localhost";
        }
        return host;
    }

    private static String getDatabaseUrl() {
        return "jdbc:mysql://" + getDatabaseHost() + ":3306/" + DB_NAME
                + "?useSSL=false&allowPublicKeyRetrieval=true";
    }

    public static Map<String, String> getLocalizedStrings(Locale locale) {
        Map<String, String> strings = new HashMap<>();
        String lang = locale.getLanguage();
        String dbUrl = getDatabaseUrl();
        String query = "SELECT `key`, value FROM localization_strings WHERE language = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, lang);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    strings.put(rs.getString("key"), rs.getString("value"));
                }
            }
            LOGGER.log(Level.INFO, "Loaded localization strings for language: {0}", lang);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection failed.", e);
        }

        return strings;
    }
}