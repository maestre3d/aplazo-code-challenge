package com.aruiz.loans.shared.infrastructure.persistence;

import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.Statement;

public class H2HealthChecker implements DatabaseHealthChecker {
    private final Connection connection;

    @Autowired
    public H2HealthChecker(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean checkLiveness() throws Exception {
        try (Statement statement = connection.createStatement()) {
            return statement.executeQuery("SELECT CURRENT_TIMESTAMP").first();
        }
    }
}
