package org.example.demo1;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BMIResultServiceTest {

    @Test
    void saveResult_insertsAllValues() throws SQLException {
        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        when(conn.prepareStatement(anyString())).thenReturn(stmt);

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(conn);

            BMIResultService.saveResult(70.0, 1.75, 22.86, "en");

            verify(stmt).setDouble(1, 70.0);
            verify(stmt).setDouble(2, 1.75);
            verify(stmt).setDouble(3, 22.86);
            verify(stmt).setString(4, "en");
            verify(stmt).executeUpdate();
        }
    }

    @Test
    void saveResult_databaseDown_doesNotCrash() {
        // Created BEFORE mocking DriverManager: SQLException's constructor calls DriverManager internally
        SQLException dbDown = new SQLException("DB down");

        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenThrow(dbDown);

            assertDoesNotThrow(() -> BMIResultService.saveResult(70.0, 1.75, 22.86, "en"));
        }
    }
}