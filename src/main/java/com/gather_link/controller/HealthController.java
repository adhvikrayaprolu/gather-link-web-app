package com.gather_link.controller;

import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** Public, minimal probes; database failures never expose connection details. */
@RestController
public class HealthController {
    private final JdbcTemplate database;

    public HealthController(JdbcTemplate database) {
        this.database = database;
    }

    @GetMapping("/health/live")
    public Map<String, String> live() {
        return Map.of("status", "up");
    }

    @GetMapping("/health/ready")
    public ResponseEntity<Map<String, String>> ready() {
        try {
            if (!Integer.valueOf(1).equals(database.queryForObject("SELECT 1", Integer.class))) {
                return ResponseEntity.status(503).body(Map.of("status", "unavailable"));
            }
            return ResponseEntity.ok(Map.of("status", "ready"));
        } catch (DataAccessException unavailable) {
            return ResponseEntity.status(503).body(Map.of("status", "unavailable"));
        }
    }
}
