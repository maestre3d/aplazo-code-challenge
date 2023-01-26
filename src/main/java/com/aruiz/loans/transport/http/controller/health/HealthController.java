package com.aruiz.loans.transport.http.controller.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public final class HealthController {
    @GetMapping("/health")
    public HashMap<String, String> getNodeHealth() {
        return new HashMap<>() {{
            put("status", "ok");
        }};
    }
}
