package com.aruiz.loans.transport.http.controller.health;

import com.aruiz.loans.shared.infrastructure.persistence.DatabaseHealthChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public final class HealthController {
    private final DatabaseHealthChecker databaseHealthChecker;

    @Autowired
    public HealthController(DatabaseHealthChecker databaseHealthChecker) {
        this.databaseHealthChecker = databaseHealthChecker;
    }

    @GetMapping("/health")
    public HashMap<String, Object> getNodeHealth() {
        try {
            boolean databaseIsAlive = databaseHealthChecker.checkLiveness();
            return new HashMap<>() {{
                put("service_status", "ok");
                put("database_status", databaseIsAlive ? "ok" : "error");
            }};
        } catch (Exception ignored) {
        }

        return new HashMap<>() {{
            put("service_status", "ok");
            put("database_status", "error");
        }};
    }
}
