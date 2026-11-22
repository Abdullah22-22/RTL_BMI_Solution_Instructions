package org.example.demo1;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LocalizationServiceTest {

    @Test
    void getLocalizedStrings_returnsValuesFromDatabase() throws SQLException {
        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, true, false);
        when(rs.getString("key")).thenReturn("weight", "height");
        when(rs.getString("value")).thenReturn("Poids", "Taille");

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(conn);

            Map<String, String> result = LocalizationService.getLocalizedStrings(Locale.of("fr", "FR"));

            assertEquals(2, result.size());
            assertEquals("Poids", result.get("weight"));
            assertEquals("Taille", result.get("height"));
            verify(stmt).setString(1, "fr");
        }
    }

    @Test
    void getLocalizedStrings_databaseDown_returnsEmptyMap() {
        // Created BEFORE mocking DriverManager: SQLException's constructor calls DriverManager internally
        SQLException dbDown = new SQLException("DB down");

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenThrow(dbDown);

            Map<String, String> result = LocalizationService.getLocalizedStrings(Locale.of("en", "US"));

            assertTrue(result.isEmpty());
        }
    }
}