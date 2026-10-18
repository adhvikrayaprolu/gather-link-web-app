package com.gather_link;

import com.gather_link.controller.HealthController;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HealthControllerTests {
    @Test void databaseFailureDoesNotKillLivenessOrRevealDetails() {
        JdbcTemplate database = mock(JdbcTemplate.class);
        when(database.queryForObject("SELECT 1", Integer.class))
            .thenThrow(new CannotGetJdbcConnectionException("private database details"));
        HealthController controller = new HealthController(database);
        assertEquals(Map.of("status", "up"), controller.live());
        assertEquals(503, controller.ready().getStatusCode().value());
        assertEquals(Map.of("status", "unavailable"), controller.ready().getBody());
        assertFalse(controller.ready().getBody().toString().contains("private"));
    }

    @Test void unexpectedDatabaseResultIsNotReady() {
        JdbcTemplate database = mock(JdbcTemplate.class);
        when(database.queryForObject("SELECT 1", Integer.class)).thenReturn(null);
        assertEquals(503, new HealthController(database).ready().getStatusCode().value());
    }
}
